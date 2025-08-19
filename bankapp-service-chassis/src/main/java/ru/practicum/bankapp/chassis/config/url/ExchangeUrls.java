package ru.practicum.bankapp.chassis.config.url;

public interface ExchangeUrls {
    String ROOT = "";
    String FULL = ROOT + "/";

    interface Rates {
        String PART = "rates";
        String FULL = ROOT + "/" + PART;
    }

    interface Exchange {
        String PART = "exchange";
        String FULL = ROOT + "/" + PART;
    }
}
