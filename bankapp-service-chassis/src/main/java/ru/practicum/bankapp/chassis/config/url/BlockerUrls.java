package ru.practicum.bankapp.chassis.config.url;

public interface BlockerUrls {
    String ROOT = "";
    String FULL = ROOT + "/";

    interface Checker {
        String PART = "checker";
        String FULL = ROOT + "/" + PART;
    }
}
