package ru.practicum.bankapp.accounts.service;

import ru.practicum.bankapp.lib.common.constant.NotificationMethod;

public interface NotificationServiceAdapter {
    void sendNonCriticalNotification(String receiver, NotificationMethod method, String message);
}
