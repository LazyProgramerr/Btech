package com.sai.btech.models;

public class UserListModel {
    public UserListModel() {
    }
    String Name,uId,userImg;

    public UserListModel(String name, String uId, String userImg) {
        Name = name;
        this.uId = uId;
        this.userImg = userImg;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }
}
