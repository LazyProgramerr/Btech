package com.sai.btech.models;

public class UserListModel {
    String name, email, phoneN, image, uId;

    public UserListModel() {
    }

    public UserListModel(String name, String email, String phoneN, String image, String uId) {
        this.name = name;
        this.email = email;
        this.phoneN = phoneN;
        this.image = image;
        this.uId = uId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneN() {
        return phoneN;
    }

    public void setPhoneN(String phoneN) {
        this.phoneN = phoneN;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
