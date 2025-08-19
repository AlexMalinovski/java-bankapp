package ru.practicum.bankapp.transfer.service;

import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;

import java.math.BigDecimal;

public interface NotificationServiceAdapter {
    void sendNonCriticalNotification(String receiver, NotificationMethod method, String message);

    void sendTransferNotifications(String loginFrom, MoneyTransferDto dto, BigDecimal valueTo);
}
