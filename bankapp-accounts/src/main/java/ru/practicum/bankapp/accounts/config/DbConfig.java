package ru.practicum.bankapp.accounts.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.practicum.bankapp.accounts.data.entity.User;
import ru.practicum.bankapp.accounts.data.entity.UserAccount;
import ru.practicum.bankapp.accounts.repository.UserAccountRepository;
import ru.practicum.bankapp.accounts.repository.UserRepository;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.time.LocalDate;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DbConfig {
    private final UserRepository userRepository;
    private final UserAccountRepository userAccountRepository;

    @Bean
    ApplicationRunner initDb() {
        return args -> {
            userAccountRepository.deleteAll();
            userRepository.deleteAll();

            String password = "password";

            createUserWithAccounts("defaultLogin", password, 10000000L, 100000L, 0L);
            createUserWithAccounts("user1", password, 100000L, 0L, 0L);
            createUserWithAccounts("user2", password, 10000000L, 100000L, 1000L);
        };
    }

    @Bean
    PasswordEncoder dbUserPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void createUserWithAccounts(String login, String passwordRaw, long valRub, long valUsd, long valCny) {
        String password = dbUserPasswordEncoder().encode(passwordRaw);
        User user1 = User.builder()
                .login(login)
                .password(password)
                .name(login)
                .birthdate(LocalDate.of(1995, 10, 1))
                .build();
        user1 = userRepository.save(user1);
        UserAccount user1AccountRub = UserAccount.builder()
                .userLogin(user1.getLogin())
                .currency(Currency.RUB)
                .value(valRub)
                .active(valRub != 0).build();
        UserAccount user1AccountUsd = UserAccount.builder()
                .userLogin(user1.getLogin())
                .currency(Currency.USD)
                .value(valUsd)
                .active(valUsd != 0).build();
        UserAccount user1AccountCny = UserAccount.builder()
                .userLogin(user1.getLogin())
                .currency(Currency.CNY)
                .value(valCny)
                .active(valCny != 0).build();
        userAccountRepository.saveAll(List.of(user1AccountRub, user1AccountUsd, user1AccountCny));
        log.info("Добавлен демо-пользователь: login='{}' password='{}'", user1.getLogin(), passwordRaw);
    }
}
