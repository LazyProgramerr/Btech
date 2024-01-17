package com.sai.btech.models;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomModel {
    String ChatRoomId,msgTimeStamp,msg,lastMsgSenderId,chatRoomType,chatRoomName,chatRoomImage;
    ArrayList<String> chatRoomMembers;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomId, String msgTimeStamp, String msg, String lastMsgSenderId, String chatRoomType, String chatRoomName, String chatRoomImage, ArrayList<String> chatRoomMembers) {
        ChatRoomId = chatRoomId;
        this.msgTimeStamp = msgTimeStamp;
        this.msg = msg;
        this.lastMsgSenderId = lastMsgSenderId;
        this.chatRoomType = chatRoomType;
        this.chatRoomName = chatRoomName;
        this.chatRoomImage = chatRoomImage;
        this.chatRoomMembers = chatRoomMembers;
    }

    public String getChatRoomId() {
        return ChatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        ChatRoomId = chatRoomId;
    }

    public String getMsgTimeStamp() {
        return msgTimeStamp;
    }

    public void setMsgTimeStamp(String msgTimeStamp) {
        this.msgTimeStamp = msgTimeStamp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLastMsgSenderId() {
        return lastMsgSenderId;
    }

    public void setLastMsgSenderId(String lastMsgSenderId) {
        this.lastMsgSenderId = lastMsgSenderId;
    }

    public String getChatRoomType() {
        return chatRoomType;
    }

    public void setChatRoomType(String chatRoomType) {
        this.chatRoomType = chatRoomType;
    }

    public String getChatRoomName() {
        return chatRoomName;
    }

    public void setChatRoomName(String chatRoomName) {
        this.chatRoomName = chatRoomName;
    }

    public String getChatRoomImage() {
        return chatRoomImage;
    }

    public void setChatRoomImage(String chatRoomImage) {
        this.chatRoomImage = chatRoomImage;
    }

    public ArrayList<String> getChatRoomMembers() {
        return chatRoomMembers;
    }

    public void setChatRoomMembers(ArrayList<String> chatRoomMembers) {
        this.chatRoomMembers = chatRoomMembers;
    }
}
