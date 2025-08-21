package ru.practicum.bankapp.chassis.config.url;

public interface ExchangeGenUrls {
    String ROOT = "";
    String FULL = ROOT + "/";

    interface Current {
        String PART = "current";
        String FULL = ROOT + "/" + PART;
    }
}
