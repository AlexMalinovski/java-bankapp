package ru.practicum.bankapp.accounts.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.bankapp.accounts.data.entity.UserAccount;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findOneByUserLoginAndCurrencyAndActiveTrue(String login, Currency currency);
}
