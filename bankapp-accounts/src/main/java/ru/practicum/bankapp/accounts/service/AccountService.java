package ru.practicum.bankapp.accounts.service;

import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserProfileDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

import java.util.List;

public interface AccountService {
    UserAccountsDto getUserAccounts(String login);

    AccountsChangeResult changeUserAccounts(String login, UserAccountsDto userAccountsDto);

    void runCashTransaction(String login, CashOperationDto cashOperationDto);

    void runTransferTransaction(String login, TransferTransactionDto dto);

    UserProfileDto getUserProfile(String login);

    List<UserShortDto> getActiveUsers();

    void createUserProfileWithAccounts(SignUpDto signUpDto);

    void changeUserPassword(String login, PasswordChangeDto passwordChangeDto);
}
