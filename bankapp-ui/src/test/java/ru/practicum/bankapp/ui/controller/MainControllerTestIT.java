package ru.practicum.bankapp.ui.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import ru.practicum.bankapp.lib.common.constant.CashOperation;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.dto.accounts.AccountDto;
import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class MainControllerTestIT extends AbstractUiControllerTest {

    @Test
    @SneakyThrows
    void getMain() {
        when(accountServiceAdapter.getUserAccounts("login")).thenReturn(new UserAccountsDto());

        mockMvc.perform(get(UiUrls.Main.FULL))
                .andExpect(status().isOk())
                .andExpect(view().name("main"));
    }

    @Test
    @SneakyThrows
    void editUserAccounts() {
        AccountDto accountDto = new AccountDto(111L, "login", Currency.RUB, BigDecimal.TEN, true);
        UserAccountsDto userAccountsDto = new UserAccountsDto(
                "name", LocalDate.now().minusYears(18), List.of(accountDto));
        when(accountServiceAdapter.changeAccounts("login", userAccountsDto))
                .thenReturn(new AccountsChangeResult(true, null));

        mockMvc.perform(post(UiUrls.User.Login.EditUserAccounts.FULL.replaceAll("\\{login}", "login"))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .flashAttr("userAccountsDto", userAccountsDto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(UiUrls.Main.FULL));
    }

    @Test
    @SneakyThrows
    void changeUserPassword() {
        PasswordChangeDto passwordChangeDto = new PasswordChangeDto("pass", "pass");
        when(accountServiceAdapter.changeUserPassword("login", passwordChangeDto))
                .thenReturn(new AccountsChangeResult(true, null));

        mockMvc.perform(post(UiUrls.User.Login.EditPassword.FULL.replaceAll("\\{login}", "login"))
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                        .flashAttr("passwordChangeDto", passwordChangeDto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(UiUrls.Main.FULL));
    }

    @Test
    @SneakyThrows
    void executeCashOperation() {
        CashOperationDto cashOperationDto = new CashOperationDto(Currency.RUB, CashOperation.PUT, BigDecimal.TEN);
        when(cashService.executeCashOperation("login", cashOperationDto))
                .thenReturn(new OperationResult(true, null));

        mockMvc.perform(post(UiUrls.User.Login.Cash.FULL.replaceAll("\\{login}", "login"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .flashAttr("cashOperationDto", cashOperationDto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(UiUrls.Main.FULL));
    }

    @Test
    @SneakyThrows
    void transferMoney() {
        MoneyTransferDto moneyTransferDto = new MoneyTransferDto(
                Currency.RUB, Currency.USD, "loginTo", BigDecimal.TEN);
        when(transferService.transferMoney("login", moneyTransferDto))
                .thenReturn(new OperationResult(true, null));

        mockMvc.perform(post(UiUrls.User.Login.Transfer.FULL.replaceAll("\\{login}", "login"))
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .flashAttr("moneyTransferDto", moneyTransferDto)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(UiUrls.Main.FULL));
    }
}