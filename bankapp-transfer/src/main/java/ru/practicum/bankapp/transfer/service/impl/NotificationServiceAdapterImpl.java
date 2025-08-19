package ru.practicum.bankapp.transfer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.bankapp.chassis.service.AbstractNotificationService;
import ru.practicum.bankapp.chassis.service.NotificationsClient;
import ru.practicum.bankapp.lib.common.constant.NotificationMethod;
import ru.practicum.bankapp.lib.dto.transfer.MoneyTransferDto;
import ru.practicum.bankapp.transfer.service.NotificationServiceAdapter;

import java.math.BigDecimal;

@Component
@Slf4j
public class NotificationServiceAdapterImpl extends AbstractNotificationService
        implements NotificationServiceAdapter {

    public NotificationServiceAdapterImpl(@Autowired NotificationsClient notificationsClient) {
        super(notificationsClient);
    }

    @Override
    public void sendTransferNotifications(String loginFrom, MoneyTransferDto dto, BigDecimal valueTo) {
        if (loginFrom.equals(dto.getLoginTo())) {
            sendSelfTransferNotification(loginFrom, dto);
        } else {
            sendAllNotifications(loginFrom, dto, valueTo);
        }
    }

    private void sendAllNotifications(String loginFrom, MoneyTransferDto dto, BigDecimal valueTo) {
        this.sendNonCriticalNotification(loginFrom, NotificationMethod.LOG,
                String.format("Отправлено %s %s пользователю %s",
                        dto.getValue(), dto.getCurrencyFrom(),  dto.getLoginTo()));

        this.sendNonCriticalNotification(dto.getLoginTo(), NotificationMethod.LOG,
                String.format("Поступил перевод на %s %s от пользователя %s",
                        valueTo, dto.getCurrencyTo(), loginFrom));
    }

    private void sendSelfTransferNotification(String loginFrom, MoneyTransferDto dto) {
        this.sendNonCriticalNotification(loginFrom, NotificationMethod.LOG,
                String.format("Выполнен перевод %s %s на счет %s",
                        dto.getValue(), dto.getCurrencyFrom(), dto.getCurrencyTo()));
    }
}
