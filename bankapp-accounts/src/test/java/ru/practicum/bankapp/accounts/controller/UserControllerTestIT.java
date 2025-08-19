package ru.practicum.bankapp.accounts.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.bankapp.chassis.config.url.AccountsUrls;
import ru.practicum.bankapp.chassis.config.url.CustomHeaders;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.accounts.AccountDto;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.TransferTransactionDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTestIT extends AbstractControllerTest {
    @Test
    @SneakyThrows
    void getUserAccounts() {
        mockMvc.perform(get(AccountsUrls.User.Accounts.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login"))
                .andExpect(status().isOk());

        verify(accountService).getUserAccounts("login");
    }

    @Test
    @SneakyThrows
    void getUserProfile() {
        mockMvc.perform(get(AccountsUrls.User.Profile.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login"))
                .andExpect(status().isOk());

        verify(accountService).getUserProfile("login");
    }

    @Test
    @SneakyThrows
    void getActiveUsers() {
        mockMvc.perform(get(AccountsUrls.User.Users.FULL))
                .andExpect(status().isOk());

        verify(accountService).getActiveUsers();
    }

    @Test
    @SneakyThrows
    void changeUserAccounts() {
        AccountDto accountDto = new AccountDto(1L, "login", Currency.USD, BigDecimal.ZERO, true);
        UserAccountsDto userAccountsDto = new UserAccountsDto(
                "name", LocalDate.now().minusYears(20), List.of(accountDto));

        mockMvc.perform(post(AccountsUrls.User.Accounts.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userAccountsDto)))
                .andExpect(status().isOk());


        verify(accountService).changeUserAccounts("login", userAccountsDto);
    }

    @Test
    @SneakyThrows
    void changeUserPassword() {
        var passwordChangeDto = new PasswordChangeDto("pass", "pass");

        mockMvc.perform(post(AccountsUrls.User.Profile.Password.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(passwordChangeDto)))
                .andExpect(status().isOk());


        verify(accountService).changeUserPassword("login", passwordChangeDto);
    }

    @Test
    @SneakyThrows
    void runCashTransaction() {
        CashOperationDto cashOperationDto = new CashOperationDto(Currency.RUB, CashOperation.PUT, BigDecimal.TEN);

        mockMvc.perform(post(AccountsUrls.User.Cash.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(cashOperationDto)))
                .andExpect(status().isOk());

        verify(accountService).runCashTransaction("login", cashOperationDto);
    }

    @Test
    @SneakyThrows
    void runTransferTransaction() {
        var dto = TransferTransactionDto.builder()
                .currencyFrom( Currency.RUB).currencyTo( Currency.RUB)
                .valueTo(BigDecimal.TEN).valueFrom(BigDecimal.TEN)
                .loginTo("to")
                .build();

        mockMvc.perform(post(AccountsUrls.User.Transfer.FULL)
                        .header(CustomHeaders.USER_LOGIN, "login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto)))
                .andExpect(status().isOk());

        verify(accountService).runTransferTransaction("login", dto);
    }

    @Test
    @SneakyThrows
    void createUserProfileWithAccounts() {
        SignUpDto signUpDto = new SignUpDto(
                "login", "pass", "pass", "name", LocalDate.now().minusYears(18));

        mockMvc.perform(post(AccountsUrls.User.Profile.FULL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(signUpDto)))
                .andExpect(status().isOk());

        verify(accountService).createUserProfileWithAccounts(signUpDto);
    }
}