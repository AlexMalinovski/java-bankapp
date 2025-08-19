package ru.practicum.bankapp.exchange.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.bankapp.exchange.entity.Rate;
import ru.practicum.bankapp.exchange.entity.id.RateId;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Repository
public interface RateRepository extends JpaRepository<Rate, RateId> {
    List<Rate> findAllByCurrencyInAndTimeAfter(Set<Currency> currencyFrom, LocalDateTime localDateTime);
    List<Rate> findAllByTimeAfter(LocalDateTime localDateTime);
}
