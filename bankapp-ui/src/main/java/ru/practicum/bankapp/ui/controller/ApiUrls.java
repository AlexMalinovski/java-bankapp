package ru.practicum.bankapp.ui.controller;

public interface ApiUrls {
    String ROOT = "/api";
    String FULL = ROOT + "/";

    interface Rates {
        String PART = "rates";
        String FULL = ROOT + "/" + PART;
    }
}
