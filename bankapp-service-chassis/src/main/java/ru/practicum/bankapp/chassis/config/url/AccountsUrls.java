package ru.practicum.bankapp.chassis.config.url;

public interface AccountsUrls {
    String ROOT = "";
    String FULL = ROOT + "/";

    interface User {
        String PART = "user";
        String FULL = ROOT + "/" + PART;

        interface Accounts {
            String PART = "accounts";
            String FULL = User.FULL + "/" + PART;
        }

        interface Cash {
            String PART = "cash";
            String FULL = User.FULL + "/" + PART;
        }

        interface Transfer {
            String PART = "transfer";
            String FULL = User.FULL + "/" + PART;
        }

        interface Profile {
            String PART = "profile";
            String FULL = User.FULL + "/" + PART;

            interface Password {
                String PART = "password";
                String FULL = Profile.FULL + "/" + PART;
            }
        }

        interface Users {
            String PART = "users";
            String FULL = User.FULL + "/" + PART;
        }
    }
}
