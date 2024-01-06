package com.sai.btech.models;

public class UserData {
    public String uName,PhoneNumber,eMail,img,uid;

    public UserData(String uName, String eMail, String phoneNumber,String img,String uid) {
        this.uName = uName;
        PhoneNumber = phoneNumber;
        this.eMail = eMail;
        this.img = img;
        this.uid = uid;
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
    public String getUid(){return uid;}
}
