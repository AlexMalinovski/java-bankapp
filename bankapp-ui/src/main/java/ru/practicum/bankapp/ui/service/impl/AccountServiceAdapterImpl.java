package ru.practicum.bankapp.ui.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.practicum.bankapp.chassis.service.AccountsClient;
import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.ui.service.AccountServiceAdapter;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AccountServiceAdapterImpl implements AccountServiceAdapter, UserDetailsService {
    private final AccountsClient accountsClient;

    @Override
    public AccountsChangeResult changeUserPassword(String login, PasswordChangeDto passwordChangeDto) {
        try {
            accountsClient.changeUserPassword(login, passwordChangeDto);
            return new AccountsChangeResult(true, null);
        } catch (Exception ex) {
            log.error("Непридвиденная ошибка при изменениий пароля пользователя: {}", ex.getMessage(), ex);
            return new AccountsChangeResult(false, ex.getMessage());
        }
    }

    @Override
    public UserAccountsDto getUserAccounts(String login) {
        return accountsClient.getUserAccounts(login);
    }

    @Override
    public AccountsChangeResult changeAccounts(String login, UserAccountsDto userAccountsDto) {
        try {
            return accountsClient.changeUserAccounts(login, userAccountsDto);
        } catch (Exception ex) {
            log.error("Непридвиденная ошибка при изменениий счета: {}", ex.getMessage(), ex);
            return new AccountsChangeResult(false, ex.getMessage());
        }
    }

    @Override
    public List<UserShortDto> getAllUsersExcludeCurrent(String currentUser) {
        return accountsClient.findAllUsers()
                .stream()
                .filter(Objects::nonNull)
                .filter(user -> !Objects.equals(user.getLogin(), currentUser))
                .sorted(Comparator.comparing(UserShortDto::getLogin))
                .toList();
    }

    @Override
    public OperationResult registerNewUser(SignUpDto signUpDto) {
        try {
            accountsClient.registerNewUser(signUpDto);
            return new OperationResult(true, null);
        } catch (Exception ex) {
            return new OperationResult(false, ex.getMessage());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return Optional.ofNullable(accountsClient.getUserProfile(username))
                    .map(appUser -> new User(appUser.getLogin(), appUser.getPassword(), List.of(
                            new SimpleGrantedAuthority("ROLE_USER"))))
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "Непридвиденная ошибка при получении профиля пользователя"));
        } catch (Exception ex) {
            throw new UsernameNotFoundException(
                    String.format("Не удалось загрузить профиль пользователя '%s': %s", username, ex.getMessage()), ex);
        }
    }
}
