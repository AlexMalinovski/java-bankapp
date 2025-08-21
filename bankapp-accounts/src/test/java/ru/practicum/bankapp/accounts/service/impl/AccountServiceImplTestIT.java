package ru.practicum.bankapp.accounts.service.impl;

import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.bankapp.accounts.AbstractIntegrationDataTest;
import ru.practicum.bankapp.accounts.data.entity.UserAccount;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.accounts.AccountDto;
import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
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
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountServiceImplTestIT extends AbstractIntegrationDataTest {
    private static final String LOGIN = "user1";
    private static final String LOGIN2 = "user2";

    @Test
    @Transactional
    void getUserAccounts() {
        UserAccountsDto actual = accountService.getUserAccounts(LOGIN);
        assertNotNull(actual);
        assertEquals(LOGIN, actual.getName());
        assertEquals(LocalDate.of(1995, 10, 1), actual.getBirthdate());

        var accounts = actual.getAccounts().stream().collect(Collectors
                .toMap(AccountDto::getCurrency, Function.identity()));
        assertNotNull(accounts);
        assertEquals(3, accounts.size());
        assertEquals(LOGIN, accounts.get(Currency.RUB).getUserLogin());
        assertEquals(LOGIN, accounts.get(Currency.USD).getUserLogin());
        assertEquals(LOGIN, accounts.get(Currency.CNY).getUserLogin());
        assertEquals(BigDecimal.valueOf(100000L, 2), accounts.get(Currency.RUB).getValue());
        assertEquals(0, BigDecimal.ZERO.compareTo(accounts.get(Currency.USD).getValue()));
        assertEquals(0, BigDecimal.ZERO.compareTo(accounts.get(Currency.CNY).getValue()));
        assertTrue(accounts.get(Currency.RUB).getExists());
        assertFalse(accounts.get(Currency.USD).getExists());
        assertFalse(accounts.get(Currency.CNY).getExists());
    }

    @Test
    @Transactional
    void changeUserAccounts() {
        var acc = userRepository.findOneByLogin(LOGIN).get().getAccounts().stream()
                .collect(Collectors.toMap(UserAccount::getCurrency, Function.identity()));
        assertFalse(acc.get(Currency.CNY).getActive());


        String expectedName = "nameNew";
        LocalDate expectedBirth = LocalDate.now();
        AccountDto accountDto = new AccountDto(acc.get(Currency.CNY).getId(), LOGIN, null, null, true);

        UserAccountsDto dto = new UserAccountsDto(expectedName, expectedBirth, List.of(accountDto));

        AccountsChangeResult accountsChangeResult = accountService.changeUserAccounts(LOGIN, dto);
        assertTrue(accountsChangeResult.success());

        var actual = userRepository.findOneByLogin(LOGIN).get();
        assertEquals(expectedBirth, actual.getBirthdate());
        assertEquals(expectedName, actual.getName());

        var accounts = actual.getAccounts().stream()
                .collect(Collectors.toMap(UserAccount::getCurrency, Function.identity()));
        assertTrue(accounts.get(Currency.CNY).getActive());
    }

    @Test
    @Transactional
    void runCashTransaction() {
        CashOperationDto operationDto = new CashOperationDto(
                Currency.RUB, CashOperation.PUT, BigDecimal.valueOf(200000, 2));

        accountService.runCashTransaction(LOGIN, operationDto);

        var accRu = userAccountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, Currency.RUB).get();
        assertEquals(300000, accRu.getValue());
    }

    @Test
    @Transactional
    void runTransferTransaction() {
        TransferTransactionDto operation = TransferTransactionDto.builder()
                .loginTo(LOGIN2)
                .currencyFrom(Currency.RUB).currencyTo(Currency.USD)
                .valueFrom(BigDecimal.valueOf(100000, 2)).valueTo(BigDecimal.TEN)
                .build();

        accountService.runTransferTransaction(LOGIN, operation);
        var accRu = userAccountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN, Currency.RUB).get();
        var accUsd = userAccountRepository.findOneByUserLoginAndCurrencyAndActiveTrue(LOGIN2, Currency.USD).get();

        assertEquals(0, accRu.getValue());
        assertEquals(101000, accUsd.getValue());
    }

    @Test
    void getUserProfile() {
        UserProfileDto actual = accountService.getUserProfile(LOGIN);

        assertEquals("user1", actual.getLogin());
        assertTrue(dbUserPasswordEncoder.matches("password", actual.getPassword()));
    }

    @Test
    void getActiveUsers() {
        var actual = accountService.getActiveUsers()
                .stream()
                .collect(Collectors.toMap(UserShortDto::getLogin, Function.identity()));

        assertEquals(Set.of("defaultLogin", "user1", "user2"), actual.keySet());
    }

    @Test
    @Transactional
    void createUserProfileWithAccounts() {
        var birth = LocalDate.now().minusYears(18);
        SignUpDto signUpDto = new SignUpDto(
                "newlogin", "password", null, "name", birth);

        accountService.createUserProfileWithAccounts(signUpDto);

        var user = userRepository.findById("newlogin").get();
        assertEquals(birth, user.getBirthdate());
        assertEquals("name", user.getName());
        assertTrue(dbUserPasswordEncoder.matches("password", user.getPassword()));

        var accRu = userAccountRepository.findOneByUserLoginAndCurrencyAndActiveTrue("newlogin", Currency.RUB);
        assertTrue(accRu.isPresent());
    }

    @Test
    @Transactional
    void changeUserPassword() {
        PasswordChangeDto dto = new PasswordChangeDto("pass", "pass");

        accountService.changeUserPassword(LOGIN, dto);

        var user = userRepository.findById(LOGIN).get();
        assertTrue(dbUserPasswordEncoder.matches("pass", user.getPassword()));
    }
}