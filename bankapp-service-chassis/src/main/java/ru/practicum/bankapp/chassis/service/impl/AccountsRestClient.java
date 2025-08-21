package ru.practicum.bankapp.chassis.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import ru.practicum.bankapp.chassis.config.url.AccountsUrls;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.chassis.service.AccountsClient;
import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserProfileDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

import java.util.List;

@RequiredArgsConstructor
public class AccountsRestClient implements AccountsClient {
    private final RestClient restClient;

    @Override
    public void changeUserPassword(String login, PasswordChangeDto passwordChangeDto) {
        restClient.post().uri(AccountsUrls.User.Profile.Password.FULL)
                .header(CustomHeaders.USER_LOGIN, login)
                .body(passwordChangeDto)
                .retrieve()
                .body(Void.class);
    }

    @Override
    public UserAccountsDto getUserAccounts(String login) {
        return restClient.get().uri(AccountsUrls.User.Accounts.FULL)
                .header(CustomHeaders.USER_LOGIN, login)
                .retrieve()
                .body(UserAccountsDto.class);
    }

    @Override
    public AccountsChangeResult changeUserAccounts(String login, UserAccountsDto userAccountsDto) {
        return restClient.post().uri(AccountsUrls.User.Accounts.FULL)
                .header(CustomHeaders.USER_LOGIN, login)
                .body(userAccountsDto)
                .retrieve()
                .body(AccountsChangeResult.class);
    }

    @Override
    public void runCashTransaction(String login, CashOperationDto cashOperationDto) {
        restClient.post().uri(AccountsUrls.User.Cash.FULL)
                .header(CustomHeaders.USER_LOGIN, login)
                .body(cashOperationDto)
                .retrieve()
                .body(Void.class);
    }

    @Override
    public void runTransferTransaction(String login, TransferTransactionDto transactionDto) {
        restClient.post().uri(AccountsUrls.User.Transfer.FULL)
                .header(CustomHeaders.USER_LOGIN, login)
                .body(transactionDto)
                .retrieve()
                .body(Void.class);
    }

    @Override
    public UserProfileDto getUserProfile(String login) {
        return restClient.get().uri(AccountsUrls.User.Profile.FULL)
                .header(CustomHeaders.USER_LOGIN, login)
                .retrieve()
                .body(UserProfileDto.class);
    }

    @Override
    public List<UserShortDto> findAllUsers() {
        return restClient
                .get()
                .uri(AccountsUrls.User.Users.FULL)
                .retrieve().body(new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public void registerNewUser(SignUpDto signUpDto) {
        restClient.post().uri(AccountsUrls.User.Profile.FULL)
                .body(signUpDto)
                .retrieve()
                .body(Void.class);
    }
}
