package ru.practicum.bankapp.ui.service;

import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;

import java.util.List;

public interface AccountServiceAdapter {
    AccountsChangeResult changeUserPassword(String login, PasswordChangeDto passwordChangeDto);

    UserAccountsDto getUserAccounts(String login);

    AccountsChangeResult changeAccounts(String login, UserAccountsDto userAccountsDto);

    List<UserShortDto> getAllUsersExcludeCurrent(String currentUser);

    OperationResult registerNewUser(SignUpDto signUpDto);
}
