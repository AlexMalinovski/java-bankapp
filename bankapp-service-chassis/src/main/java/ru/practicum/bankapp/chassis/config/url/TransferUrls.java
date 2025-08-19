package ru.practicum.bankapp.chassis.config.url;

public interface TransferUrls {
    String ROOT = "";
    String FULL = ROOT + "/";

    interface Transfer {
        String PART = "transfer";
        String FULL = ROOT + "/" + PART;
    }
}
