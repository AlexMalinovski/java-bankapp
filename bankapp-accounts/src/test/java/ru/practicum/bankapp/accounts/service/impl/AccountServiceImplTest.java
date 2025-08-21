package ru.practicum.bankapp.accounts.service.impl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.practicum.bankapp.accounts.data.entity.User;
import ru.practicum.bankapp.accounts.data.entity.UserAccount;
import ru.practicum.bankapp.accounts.repository.UserAccountRepository;
import ru.practicum.bankapp.accounts.repository.UserRepository;
import ru.practicum.bankapp.accounts.service.NotificationServiceAdapter;
import ru.practicum.bankapp.accounts.service.mapper.UserMapper;
import ru.practicum.bankapp.chassis.exception.BadRequestException;
import ru.practicum.bankapp.chassis.exception.NotFoundException;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserProfileDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {
    private static final String LOGIN = "login";

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserAccountRepository accountRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private NotificationServiceAdapter notificationServiceAdapter;

    @Mock
    private Validator validator;

    @Mock
    private PasswordEncoder dbUserPasswordEncoder;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void getUserAccounts_whenNotFound_thenException() {
        when(userRepository.findOneByLogin(LOGIN)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.getUserAccounts(LOGIN));
    }

    @Test
    void getUserAccounts() {
        User user = new User();
        UserAccountsDto expected = new UserAccountsDto();
        when(userRepository.findOneByLogin(LOGIN)).thenReturn(Optional.of(user));
        when(userMapper.toUserAccountsDto(user)).thenReturn(expected);

        var actual = accountService.getUserAccounts(LOGIN);

        assertEquals(expected, actual);
    }

    @Test
    void changeUserAccounts_whenInvalid_thenNotSuccess() {
        UserAccountsDto userAccountsDto = new UserAccountsDto();
        User updates = new User();
        when(userMapper.toUserWithAccountsUpdate(LOGIN, userAccountsDto)).thenReturn(updates);
        ConstraintViolation<User> mockedViolation = mock(ConstraintViolation.class);
        when(validator.validate(updates)).thenReturn(Set.of(mockedViolation));

        var actual = accountService.changeUserAccounts(LOGIN, userAccountsDto);

        assertNotNull(actual);
        assertFalse(actual.success());
    }

    @Test
    void changeUserAccounts_whenUserNotFound_thenException() {
        UserAccountsDto userAccountsDto = new UserAccountsDto();
        User updates = new User();
        updates.setAccounts(List.of());

        when(userMapper.toUserWithAccountsUpdate(LOGIN, userAccountsDto)).thenReturn(updates);
        when(validator.validate(updates)).thenReturn(Set.of());
        when(userRepository.findOneByLogin(LOGIN)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.changeUserAccounts(LOGIN, userAccountsDto));
    }

    @Test
    void changeUserAccounts() {
        UserAccountsDto userAccountsDto = new UserAccountsDto();
        User updates = new User();
        UserAccount updatesAccount = UserAccount.builder()
                .id(111L)
                .active(false)
                .build();
        updates.setAccounts(List.of(updatesAccount));

        UserAccount existAccount = updatesAccount.toBuilder().active(true).build();
        User existUser = new User(LOGIN, "", "name", LocalDate.now(), List.of(existAccount));

        when(userMapper.toUserWithAccountsUpdate(LOGIN, userAccountsDto)).thenReturn(updates);
        when(validator.validate(updates)).thenReturn(Set.of());
        when(userRepository.findOneByLogin(LOGIN)).thenReturn(Optional.of(existUser));


        var actual = accountService.changeUserAccounts(LOGIN, userAccountsDto);

        verify(userMapper).updateUser(existUser, updates);
        verify(userMapper).updateUserAccount(existAccount, updatesAccount);
        assertNotNull(actual);
        assertTrue(actual.success());
        verify(notificationServiceAdapter).sendNonCriticalNotification(
                LOGIN, NotificationMethod.LOG, "Данные аккаунта обновлены");
    }

    @Test
    void runCashTransaction_whenAccountNotFound_thenException() {
        CashOperationDto operationDto = new CashOperationDto(Currency.RUB, CashOperation.PUT, BigDecimal.TEN);
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, operationDto.getCurrency()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.runCashTransaction(LOGIN, operationDto));
    }

    @Test
    void runCashTransaction_whenInsufficientFunds_thenException() {
        CashOperationDto operationDto = new CashOperationDto(Currency.RUB, CashOperation.GET, BigDecimal.TEN);
        UserAccount account = new UserAccount(11L, LOGIN, Currency.RUB, 0L, true);
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, operationDto.getCurrency()))
                .thenReturn(Optional.of(account));

        assertThrows(BadRequestException.class, () -> accountService.runCashTransaction(LOGIN, operationDto));
    }

    @Test
    void runCashTransaction_whenGet() {
        CashOperationDto operationDto = new CashOperationDto(Currency.RUB, CashOperation.PUT, BigDecimal.TEN);
        UserAccount account = Mockito.mock(UserAccount.class);
        when(account.getValue()).thenReturn(1000L);
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, operationDto.getCurrency()))
                .thenReturn(Optional.of(account));

        accountService.runCashTransaction(LOGIN, operationDto);

        verify(account).setValue(2000L);
    }

    @Test
    void runCashTransaction_whenPut() {
        CashOperationDto operationDto = new CashOperationDto(Currency.RUB, CashOperation.GET, BigDecimal.TEN);
        UserAccount account = Mockito.mock(UserAccount.class);
        when(account.getValue()).thenReturn(1000L);
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, operationDto.getCurrency()))
                .thenReturn(Optional.of(account));

        accountService.runCashTransaction(LOGIN, operationDto);

        verify(account).setValue(0L);
    }

    @Test
    void runTransferTransaction_whenAccountNotFound_thenException() {
        TransferTransactionDto dto = TransferTransactionDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.USD)
                .valueFrom(BigDecimal.TEN).valueTo(BigDecimal.ONE)
                .loginTo("loginTo")
                .build();
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, Currency.RUB))
                .thenReturn(Optional.empty());


        assertThrows(NotFoundException.class, () -> accountService.runTransferTransaction(LOGIN, dto));
    }

    @Test
    void runTransferTransaction_whenInsufficientFunds_thenException() {
        TransferTransactionDto dto = TransferTransactionDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.USD)
                .valueFrom(BigDecimal.TEN).valueTo(BigDecimal.ONE)
                .loginTo("loginTo")
                .build();
        UserAccount account = new UserAccount(11L, LOGIN, Currency.RUB, 100L, true);
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, Currency.RUB))
                .thenReturn(Optional.of(account));

        assertThrows(BadRequestException.class, () -> accountService.runTransferTransaction(LOGIN, dto));
    }

    @Test
    void runTransferTransaction_whenUserToNotFound_thenException() {
        TransferTransactionDto dto = TransferTransactionDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.USD)
                .valueFrom(BigDecimal.TEN).valueTo(BigDecimal.ONE)
                .loginTo("loginTo")
                .build();
        UserAccount account = new UserAccount(11L, LOGIN, Currency.RUB, 1000000L, true);
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, dto.getCurrencyFrom()))
                .thenReturn(Optional.of(account));
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(dto.getLoginTo(), dto.getCurrencyTo()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.runTransferTransaction(LOGIN, dto));
    }

    @Test
    void runTransferTransaction() {
        TransferTransactionDto dto = TransferTransactionDto.builder()
                .currencyFrom(Currency.RUB).currencyTo(Currency.USD)
                .valueFrom(BigDecimal.TEN).valueTo(BigDecimal.ONE)
                .loginTo("loginTo")
                .build();
        UserAccount accountFrom = Mockito.mock(UserAccount.class);
        UserAccount accountTo = Mockito.mock(UserAccount.class);
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, dto.getCurrencyFrom()))
                .thenReturn(Optional.of(accountFrom));
        when(accountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(dto.getLoginTo(), dto.getCurrencyTo()))
                .thenReturn(Optional.of(accountTo));

        when(accountFrom.getValue()).thenReturn(2000L);
        when(accountTo.getValue()).thenReturn(0L);

        accountService.runTransferTransaction(LOGIN, dto);

        verify(accountFrom).setValue(1000L);
        verify(accountTo).setValue(100L);
    }

    @Test
    void getUserProfile_whenUserNotFound_thenException() {
        when(userRepository.findById(LOGIN)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> accountService.getUserProfile(LOGIN));
    }

    @Test
    void getUserProfile() {
        User user = new User();
        UserProfileDto expected = UserProfileDto.builder().build();
        when(userRepository.findById(LOGIN)).thenReturn(Optional.of(user));
        when(userMapper.toUserProfileDto(user)).thenReturn(expected);

        var actual = accountService.getUserProfile(LOGIN);

        assertEquals(expected, actual);
    }

    @Test
    void getActiveUsers() {
        User user = new User();
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);
        UserShortDto dto = new UserShortDto();
        when(userMapper.toUserShortDto(user)).thenReturn(dto);

        var actual = accountService.getActiveUsers();

        var expected = List.of(dto);
        assertEquals(expected, actual);
    }

    @Test
    void createUserProfileWithAccounts_whenUserExists_thenException() {
        SignUpDto signUpDto = new SignUpDto();
        when(userRepository.existsById(anyString())).thenReturn(true);
        when(userMapper.toUser(any(), any())).thenReturn(new User(LOGIN, null, null, null, null));

        assertThrows(BadRequestException.class, () -> accountService.createUserProfileWithAccounts(signUpDto));
    }

    @Test
    void createUserProfileWithAccounts() {
        SignUpDto signUpDto = new SignUpDto(
                LOGIN, "password", null, "name", LocalDate.now().minusYears(18));
        when(dbUserPasswordEncoder.encode(signUpDto.getPassword())).thenReturn("pass");
        User newUser = new User(LOGIN, "pass", signUpDto.getName(), signUpDto.getBirthdate(), null);
        when(userMapper.toUser(signUpDto, "pass")).thenReturn(newUser);
        when(userRepository.existsById(LOGIN)).thenReturn(false);
        when(userRepository.save(newUser)).thenReturn(newUser);

        accountService.createUserProfileWithAccounts(signUpDto);

        verify(userRepository).save(newUser);
        verify(accountRepository, times(Currency.values().length)).save(any());
    }

    @Test
    void changeUserPassword_whenUserNotFound_thenException() {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto();
        when(userRepository.findById(LOGIN)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> accountService.changeUserPassword(LOGIN, passwordChangeDto));
    }

    @Test
    void changeUserPassword() {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("password", "password");
        User user = Mockito.mock(User.class);
        when(userRepository.findById(LOGIN)).thenReturn(Optional.of(user));
        when(dbUserPasswordEncoder.encode(passwordChangeDto.getPasswordNew())).thenReturn("pass");

        accountService.changeUserPassword(LOGIN, passwordChangeDto);
        verify(user).setPassword("pass");
    }
}