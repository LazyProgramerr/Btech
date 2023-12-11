package com.sai.btech.models;

import java.util.List;

public class ChatRoomModel {
    String ChatroomId;
    String user1;
    String user2;
    String msgTimeStamp;
    String msg;
    String lastMsgSenderId;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatroomId, String user1,String user2, String msgTimeStamp,String msg, String lastMsgSenderId) {
        this.ChatroomId = chatroomId;
        this.user1 = user1;
        this.user2 = user2;
        this.msgTimeStamp = msgTimeStamp;
        this.msg = msg;
        this.lastMsgSenderId = lastMsgSenderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public String getChatroomId() {
        return ChatroomId;
    }

    public void setChatroomId(String chatroomId) {
        ChatroomId = chatroomId;
    }




    public String getMsgTimeStamp() {
        return msgTimeStamp;
    }

    public void setMsgTimeStamp(String msgTimeStamp) {
        this.msgTimeStamp = msgTimeStamp;
    }

    public String getLastMsgSenderId() {
        return lastMsgSenderId;
    }

    public void setLastMsgSenderId(String lastMsgSenderId) {
        this.lastMsgSenderId = lastMsgSenderId;
    }
}
