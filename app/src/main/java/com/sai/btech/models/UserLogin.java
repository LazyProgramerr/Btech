package com.sai.btech.models;

public class UserLogin {
    private final boolean loginStatus;

    public UserLogin(boolean loginStatus) {
        this.loginStatus = loginStatus;
    }

    public boolean getLoginStatus() {
        return loginStatus;
    }
}
