package ru.practicum.bankapp.exchange.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.bankapp.exchange.entity.id.RateId;
import ru.practicum.bankapp.lib.common.constant.Currency;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rates", schema = "exchange_service")
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(RateId.class)
public class Rate {
    @Id
    @Column(name = "on_time", nullable = false)
    private LocalDateTime time;

    @Id
    @Column(name = "currency", nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(name = "val", nullable = false)
    private BigDecimal value;
}
