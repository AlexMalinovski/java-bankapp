package ru.practicum.bankapp.chassis.config.url;

public interface CashUrls {
    String ROOT = "";
    String FULL = ROOT + "/";

    interface Operation {
        String PART = "operation";
        String FULL = ROOT + "/" + PART;
    }
}
