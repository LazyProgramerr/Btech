package com.sai.btech.models;

public class UserData {
    public String Name,Phone,eMail,userImg,uId;

    public UserData(String Name, String eMail, String Phone,String userImg,String uId) {
        this.Name = Name;
        this.Phone = Phone;
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

    public String getUid() {
        return uId;
    }

    public void setUid(String uid) {
        this.uId = uid;
    }
}
