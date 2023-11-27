package com.sai.btech.models;

public class UserData {
    public String uName,PhoneNumber,eMail,img;

    public UserData(String uName, String eMail, String phoneNumber,String img) {
        this.uName = uName;
        PhoneNumber = phoneNumber;
        this.eMail = eMail;
        this.img = img;
    }

    public String getuName() {
        return uName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public String geteMail() {
        return eMail;
    }
    public String getImg(){return img;}
}
