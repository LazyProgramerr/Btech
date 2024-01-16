package com.sai.btech.models;

import java.security.Timestamp;

public class ChatMessageModel {
    private String message;
    private String senderId;
    private String timestamp;
    private String senderImg;

    public ChatMessageModel() {
    }

    public ChatMessageModel(String message, String senderId, String timestamp,String senderImg) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.senderImg = senderImg;
    }

    public String getSenderImg() {
        return senderImg;
    }

    public void setSenderImg(String senderImg) {
        this.senderImg = senderImg;
    }

    public  String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public  String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public  String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
