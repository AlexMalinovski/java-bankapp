package ru.practicum.bankapp.chassis.config.url;

public interface ServicesUrls {
    String GATEWAY_SERVICE_NAME = "gateway";
    String ROOT = "http://" + GATEWAY_SERVICE_NAME;
    String FULL = ROOT + "/";

    interface Accounts {
        String PART = "accounts";
        String FULL = ROOT + "/" + PART;
    }

    interface Blocker {
        String PART = "blocker";
        String FULL = ROOT + "/" + PART;
    }

    interface Cash {
        String PART = "cash";
        String FULL = ROOT + "/" + PART;
    }

    interface Exchange {
        String PART = "exchange";
        String FULL = ROOT + "/" + PART;
    }

    interface ExchangeGen {
        String PART = "exchange-gen";
        String FULL = ServicesUrls.FULL + PART;
    }

    interface Notification {
        String PART = "notification";
        String FULL = ROOT + "/" + PART;
    }

    interface Transfer {
        String PART = "transfer";
        String FULL = ROOT + "/" + PART;
    }
}
