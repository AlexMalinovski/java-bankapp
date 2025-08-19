package ru.practicum.bankapp.chassis.config.url;

public interface NotificationUrls {
    String ROOT = "";
    String FULL = ROOT + "/";

    interface Notification {
        String PART = "notification";
        String FULL = ROOT + "/" + PART;
    }
}
