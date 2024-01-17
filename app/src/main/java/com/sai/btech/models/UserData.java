package com.sai.btech.models;

public class UserData {
    public String Name,Phone,eMail,userImg,uId;

    public UserData() {
    }

    public UserData(String name, String phone, String eMail, String userImg, String uId) {
        Name = name;
        Phone = phone;
        this.eMail = eMail;
        this.userImg = userImg;
        this.uId = uId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }
}
