package ru.practicum.bankapp.accounts.service.impl;

import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.bankapp.accounts.data.entity.User;
import ru.practicum.bankapp.accounts.data.entity.UserAccount;
import ru.practicum.bankapp.accounts.repository.UserAccountRepository;
import ru.practicum.bankapp.accounts.repository.UserRepository;
import ru.practicum.bankapp.accounts.service.AccountService;
import ru.practicum.bankapp.accounts.service.NotificationServiceAdapter;
import ru.practicum.bankapp.accounts.service.mapper.UserMapper;
import ru.practicum.bankapp.chassis.exception.BadRequestException;
import ru.practicum.bankapp.chassis.exception.NotFoundException;
import ru.practicum.bankapp.chassis.util.UtilMoney;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserProfileDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountServiceImpl implements AccountService {
    private final UserRepository userRepository;
    private final UserAccountRepository accountRepository;
    private final UserMapper userMapper;
    private final NotificationServiceAdapter notificationServiceAdapter;
    private final Validator validator;
    private final PasswordEncoder dbUserPasswordEncoder;

    @Override
    public UserAccountsDto getUserAccounts(String login) {
        return userRepository.findOneByLogin(login)
                .map(userMapper::toUserAccountsDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь %s не найден", login)));
    }

    @Override
    @Transactional
    public AccountsChangeResult changeUserAccounts(String login, UserAccountsDto userAccountsDto) {
        log.info("Поступило обновление данных для аккаунта {}", login);
        User updates = userMapper.toUserWithAccountsUpdate(login, userAccountsDto);
        var errors = validator.validate(updates);
        if (!errors.isEmpty()) {
            log.warn("Получены невалидные обновления аккаунта {}", login);
            return new AccountsChangeResult(false, "Ошибка валидации обновлений");
        }
        Map<Long, UserAccount> updatesAccount = updates.getAccounts().stream()
                .collect(Collectors.toMap(UserAccount::getId, Function.identity()));

        User existUser = userRepository.findOneByLogin(login)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь %s не найден", login)));
        userMapper.updateUser(existUser, updates);
        existUser.getAccounts()
                .forEach(account -> userMapper.updateUserAccount(account, updatesAccount.get(account.getId())));

        log.info("Данные аккаунта {} обновлены", login);
        notificationServiceAdapter.sendNonCriticalNotification(login, NotificationMethod.LOG,
                "Данные аккаунта обновлены");

        return new AccountsChangeResult(true, null);
    }

    @Override
    @Transactional
    public void runCashTransaction(String login, CashOperationDto operationDto) {
        log.info("Начало транзакции пользователя {} по выполнению операции с наличными {}", login, operationDto);
        var account = accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(login, operationDto.getCurrency())
                .orElseThrow(() -> new NotFoundException("Не найден активный счет пользователя"));
        long operationValue = UtilMoney.toPriceInternal(operationDto.getValue());

        if (operationDto.getAction() == CashOperation.GET
                && account.getValue() < operationValue) {
            throw new BadRequestException("Недостаточно средств для выполнения операции");
        }

        long accountValue = account.getValue();
        account.setValue(accountValue + operationDto.getAction().getMultiple() * operationValue);
        log.info("Транзакция по операции с наличными {} завершена", operationDto);
    }

    @Override
    @Transactional
    public void runTransferTransaction(String login, TransferTransactionDto dto) {
        log.info("Начало транзакции пользователя {} по выполнению перевода средств {}", login, dto);
        var accountFrom = accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(login, dto.getCurrencyFrom())
                .orElseThrow(() -> new NotFoundException("Не найден активный счет для списания"));

        long valueFrom = UtilMoney.toPriceInternal(dto.getValueFrom());
        if (accountFrom.getValue() < valueFrom) {
            throw new BadRequestException("Недостаточно средств для выполнения операции");
        }

        var accountTo = accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(
                        dto.getLoginTo(), dto.getCurrencyTo())
                .orElseThrow(() -> new NotFoundException("Не найден активный счет для зачисления"));

        accountFrom.setValue(accountFrom.getValue() - valueFrom);
        accountTo.setValue(accountTo.getValue() + UtilMoney.toPriceInternal(dto.getValueTo()));
        log.info("Транзакция по переводу средств {} завершена", dto);
    }

    @Override
    public UserProfileDto getUserProfile(String login) {
        return userRepository.findById(login)
                .map(userMapper::toUserProfileDto)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь '%s' не найден", login)));
    }

    @Override
    public List<UserShortDto> getActiveUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserShortDto)
                .toList();
    }

    @Override
    @Transactional
    public void createUserProfileWithAccounts(SignUpDto signUpDto) {
        User user = userMapper.toUser(signUpDto, dbUserPasswordEncoder.encode(signUpDto.getPassword()));
        log.info("Начало транзакции по созданию аккаунта для пользователя {}", user.getLogin());
        if (userRepository.existsById(user.getLogin())) {
            throw new BadRequestException(
                    String.format("Пользователь '%s' уже существует", signUpDto.getLogin()));
        }
        user = userRepository.save(user);

        final String userLogin = user.getLogin();
        Arrays.stream(Currency.values())
                .map(currency -> createAccount(userLogin, currency))
                .forEach(accountRepository::save);
        log.info("Аккаунта для пользователя {} создан", user.getLogin());
    }

    @Override
    @Transactional
    public void changeUserPassword(String login, PasswordChangeDto passwordChangeDto) {
        log.info("Изменение пароля пользователя {}", login);
        User user = userRepository.findById(login)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Пользователь '%s' не найден", login)));
        String newPass = dbUserPasswordEncoder.encode(passwordChangeDto.getPasswordNew());
        user.setPassword(newPass);
    }

    private UserAccount createAccount(String login, Currency currency) {
        return UserAccount.builder()
                .userLogin(login)
                .currency(currency)
                .value(0L)
                .active(currency == Currency.RUB)
                .build();
    }
}
