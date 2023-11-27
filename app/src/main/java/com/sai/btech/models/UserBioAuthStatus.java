package com.sai.btech.models;

public class UserBioAuthStatus {
    private final boolean bioAuthStatus;

    public UserBioAuthStatus(boolean bioAuthStatus) {
        this.bioAuthStatus = bioAuthStatus;
    }

    public boolean getBioAuthStatus() {
        return bioAuthStatus;
    }
}
