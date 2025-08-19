package ru.practicum.bankapp.accounts.service.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.practicum.bankapp.accounts.data.entity.User;
import ru.practicum.bankapp.accounts.data.entity.UserAccount;
import ru.practicum.bankapp.chassis.service.impl.mapper.DefaultMapperConfig;
import ru.practicum.bankapp.chassis.util.UtilMoney;
import ru.practicum.bankapp.lib.dto.accounts.AccountDto;
import ru.practicum.bankapp.lib.dto.accounts.SignUpDto;
import ru.practicum.bankapp.lib.dto.accounts.UserAccountsDto;
import ru.practicum.bankapp.lib.dto.accounts.UserProfileDto;
import ru.practicum.bankapp.lib.dto.accounts.UserShortDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Mapper(config = DefaultMapperConfig.class)
public interface UserMapper {

    @Named("mapLongToBigDecimal")
    default BigDecimal mapLongToBigDecimal(Long val) {
        if (val == null) {
            return null;
        }
        return UtilMoney.toPrice(val);
    }

    @Named("toLowerCase")
    default String toLowerCase(String src) {
        if (src == null) {
            return null;
        }
        return src.toLowerCase(Locale.ROOT);
    }

    @Named("toUserAccountUpdate")
    default List<UserAccount> toUserAccountUpdate(List<AccountDto> accounts) {
        if (accounts == null || accounts.isEmpty()) {
            return null;
        }
        List<UserAccount> result = accounts.stream()
                .filter(Objects::nonNull)
                .map(this::toUserAccountUpdate)
                .filter(account -> account.getId() != null && account.getActive() != null)
                .toList();

        return result.isEmpty() ? null : result;
    }

    UserAccountsDto toUserAccountsDto(User user);

    @Mapping(target = "exists", source = "active")
    @Mapping(target = "value", source = "value", qualifiedByName = "mapLongToBigDecimal")
    AccountDto toAccountDto(UserAccount account);

    List<AccountDto> toAccountDto(List<UserAccount> accounts);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "login", source = "login")
    @Mapping(target = "name", source = "dto.name")
    @Mapping(target = "birthdate", source = "dto.birthdate")
    @Mapping(target = "accounts", source = "dto.accounts", qualifiedByName = "toUserAccountUpdate")
    User toUserWithAccountsUpdate(String login, UserAccountsDto dto);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "userLogin", source = "userLogin")
    @Mapping(target = "active", source = "exists")
    UserAccount toUserAccountUpdate(AccountDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "accounts", ignore = true)
    void updateUser(@MappingTarget User user, User updates);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserAccount(@MappingTarget UserAccount account, UserAccount updates);

    UserProfileDto toUserProfileDto(User user);

    UserShortDto toUserShortDto(User user);

    @Mapping(target = "password", source = "password")
    @Mapping(target = "login", source = "dto.login", qualifiedByName = "toLowerCase")
    User toUser(SignUpDto dto, String password);
}
