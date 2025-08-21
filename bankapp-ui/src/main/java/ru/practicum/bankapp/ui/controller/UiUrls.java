package ru.practicum.bankapp.ui.controller;

public interface UiUrls {
    String ROOT = "";
    String FULL = ROOT + "/";

    interface Main {
        String PART = "main";
        String FULL = ROOT + "/" + PART;
    }

    interface SignUp {
        String PART = "signup";
        String FULL = ROOT + "/" + PART;
    }

    interface LogIn {
        String PART = "login";
        String FULL = ROOT + "/" + PART;
    }

    interface LogOut {
        String PART = "logout";
        String FULL = ROOT + "/" + PART;
    }

    interface User {
        String PART = "user";
        String FULL = ROOT + "/" + PART;

        interface Login {
            String PART = "{login}";
            String FULL = User.FULL + "/" + PART;

            interface EditUserAccounts {
                String PART = "editUserAccounts";
                String FULL = Login.FULL + "/" + PART;
            }

            interface EditPassword {
                String PART = "editPassword";
                String FULL = Login.FULL + "/" + PART;
            }

            interface Cash {
                String PART = "cash";
                String FULL = Login.FULL + "/" + PART;
            }

            interface Transfer {
                String PART = "transfer";
                String FULL = Login.FULL + "/" + PART;
            }
        }
    }
}
