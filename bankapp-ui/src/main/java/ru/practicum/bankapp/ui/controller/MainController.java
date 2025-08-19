package ru.practicum.bankapp.ui.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.practicum.bankapp.lib.common.constant.Currency;
import ru.practicum.bankapp.lib.common.marker.OnUiService;
import ru.practicum.bankapp.lib.dto.accounts.AccountDto;
import ru.practicum.bankapp.lib.dto.accounts.AccountsChangeResult;
import ru.practicum.bankapp.lib.dto.accounts.PasswordChangeDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;
import ru.practicum.bankapp.lib.dto.cash.CashOperationDto;
import ru.practicum.bankapp.lib.dto.cash.OperationResult;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;
import ru.practicum.bankapp.ui.service.AccountServiceAdapter;
import ru.practicum.bankapp.ui.service.CashService;
import ru.practicum.bankapp.ui.service.TransferService;
import ru.practicum.bankapp.ui.service.util.UserUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final AccountServiceAdapter accountAdapter;
    private final CashService cashService;
    private final TransferService transferService;

    public UserAccountsDto initUserAccountsDto() {
        return UserUtil.getCurrentUserLogin()
                .map(accountAdapter::getUserAccounts)
                .orElseGet(this::getDefaultUserAccountDto);
    }

    private UserAccountsDto getDefaultUserAccountDto() {
        var dto = new UserAccountsDto();
        dto.setAccounts(new ArrayList<>());
        dto.setName("Имя Пользователя");
        dto.setBirthdate(LocalDate.of(2000, 1, 15));

        dto.getAccounts().add(new AccountDto(1L, "defaultLogin", Currency.RUB, BigDecimal.valueOf(1000), true));

        return dto;
    }

    @ModelAttribute("login")
    public String initUserLogin() {
        return UserUtil.getCurrentUserLogin().orElse("Unknown-user");
    }

    @ModelAttribute("passwordChangeDto")
    public PasswordChangeDto initPasswordChangeDto() {
        return new PasswordChangeDto();
    }

    @ModelAttribute("passChangeResult")
    public AccountsChangeResult initEmptyAccountsChangeResult() {
        return new AccountsChangeResult(null, null);
    }

    @ModelAttribute("currencies")
    public List<Currency> initAvailableCurrencies() {
        return Currency.ALL;
    }

    @ModelAttribute("cashOperationDto")
    public CashOperationDto initDefaultCashOperation() {
        return new CashOperationDto();
    }

    @ModelAttribute("cashOperationResult")
    public OperationResult initCashOperationResult() {
        return new OperationResult(null, null);
    }

    @ModelAttribute("moneyTransferDto")
    public MoneyTransferDto initMoneyTransferDto() {
        return new MoneyTransferDto();
    }

    @ModelAttribute("moneyTransferResult")
    public OperationResult initMoneyTransferResult() {
        return new OperationResult(null, null);
    }

    @ModelAttribute("users")
    public List<UserShortDto> initUsers() {
        return UserUtil.getCurrentUserLogin()
                .map(accountAdapter::getAllUsersExcludeCurrent)
                .orElse(List.of());
    }

    @GetMapping(UiUrls.Main.FULL)
    public String getMain(Model model) {
        UserAccountsDto userAccountsDto = initUserAccountsDto();
        model.addAttribute("userAccountsDto", userAccountsDto);
        return "main";
    }

    @PostMapping(path = UiUrls.User.Login.EditUserAccounts.FULL,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String editUserAccounts(
            @PathVariable String login,
            @ModelAttribute @Validated(OnUiService.class) UserAccountsDto userAccountsDto,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("userAccountsDto", userAccountsDto);
            return "main";
        }
        AccountsChangeResult accountsChangeResult = accountAdapter.changeAccounts(login, userAccountsDto);

        if (!accountsChangeResult.success()) {
            model.addAttribute("userAccountsDto", userAccountsDto);
            model.addAttribute("accChangeResult", accountsChangeResult);
            return "main";
        }
        return "redirect:" + UiUrls.Main.FULL;
    }

    @PostMapping(path = UiUrls.User.Login.EditPassword.FULL,
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public String changeUserPassword(
            @PathVariable String login,
            @ModelAttribute @Valid PasswordChangeDto passwordChangeDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("passwordChangeDto", passwordChangeDto);
            return this.getMain(model);
        }

        var accountsChangeResult = accountAdapter.changeUserPassword(login, passwordChangeDto);
        if (!accountsChangeResult.success()) {
            model.addAttribute("passChangeResult", accountsChangeResult);
            this.getMain(model);
        }

        return "redirect:" + UiUrls.Main.FULL;
    }

    @PostMapping(path = UiUrls.User.Login.Cash.FULL,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String executeCashOperation(
            @PathVariable String login,
            @ModelAttribute @Valid CashOperationDto cashOperationDto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("cashOperationDto", cashOperationDto);
            return this.getMain(model);
        }

        OperationResult cashOperationResult = cashService.executeCashOperation(login, cashOperationDto);
        if (!cashOperationResult.success()) {
            model.addAttribute("cashOperationResult", cashOperationResult);
            return this.getMain(model);
        }


        return "redirect:" + UiUrls.Main.FULL;
    }

    @PostMapping(path = UiUrls.User.Login.Transfer.FULL,
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public String transferMoney(
            @PathVariable String login,
            @ModelAttribute("moneyTransferDto") @Valid MoneyTransferDto moneyTransferDto,
            BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("moneyTransferDto", moneyTransferDto);
            return this.getMain(model);
        }

        OperationResult moneyTransferResult = transferService.transferMoney(login, moneyTransferDto);

        if (!moneyTransferResult.success()) {
            model.addAttribute("moneyTransferResult", moneyTransferResult);
            return this.getMain(model);
        }

        return "redirect:" + UiUrls.Main.FULL;
    }

}
