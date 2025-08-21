package ru.practicum.bankapp.accounts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.bankapp.accounts.service.AccountService;
import ru.practicum.bankapp.chassis.config.url.AccountsUrls;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserProfileDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final AccountService accountService;

    @GetMapping(AccountsUrls.User.Accounts.FULL)
    public ResponseEntity<UserAccountsDto> getUserAccounts(
            @RequestHeader(CustomHeaders.USER_LOGIN) String login) {

        return ResponseEntity.ok(accountService.getUserAccounts(login));
    }

    @GetMapping(AccountsUrls.User.Profile.FULL)
    public ResponseEntity<UserProfileDto> getUserProfile(
            @RequestHeader(CustomHeaders.USER_LOGIN) String login) {

        return ResponseEntity.ok(accountService.getUserProfile(login));
    }

    @GetMapping(AccountsUrls.User.Users.FULL)
    public ResponseEntity<List<UserShortDto>> getActiveUsers() {

        return ResponseEntity.ok(accountService.getActiveUsers());
    }

    @PostMapping(AccountsUrls.User.Accounts.FULL)
    public ResponseEntity<AccountsChangeResult> changeUserAccounts(
            @RequestBody @Valid UserAccountsDto userAccountsDto,
            @RequestHeader(CustomHeaders.USER_LOGIN) String login) {

        return ResponseEntity.ok(accountService.changeUserAccounts(login, userAccountsDto));
    }

    @PostMapping(AccountsUrls.User.Profile.Password.FULL)
    public ResponseEntity<Void> changeUserPassword(
            @RequestBody @Valid PasswordChangeDto passwordChangeDto,
            @RequestHeader(CustomHeaders.USER_LOGIN) String login) {

        accountService.changeUserPassword(login, passwordChangeDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping(AccountsUrls.User.Cash.FULL)
    public ResponseEntity<Void> runCashTransaction(@RequestBody @Valid CashOperationDto cashOperationDto,
                                                   @RequestHeader(CustomHeaders.USER_LOGIN) String login) {
        accountService.runCashTransaction(login, cashOperationDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(AccountsUrls.User.Transfer.FULL)
    public ResponseEntity<Void> runTransferTransaction(@RequestBody @Valid TransferTransactionDto dto,
                                                       @RequestHeader(CustomHeaders.USER_LOGIN) String login) {
        accountService.runTransferTransaction(login, dto);
        return ResponseEntity.ok().build();
    }

    @PostMapping(AccountsUrls.User.Profile.FULL)
    public ResponseEntity<Void> createUserProfileWithAccounts(@RequestBody @Valid SignUpDto signUpDto) {
        accountService.createUserProfileWithAccounts(signUpDto);

        return ResponseEntity.ok().build();
    }
}
