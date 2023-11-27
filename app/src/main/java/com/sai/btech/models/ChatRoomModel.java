package com.sai.btech.models;

import java.util.List;

public class ChatRoomModel {
    String ChatroomId;
    List<String> userIds;
    String msgTimeStamp;
    String lastMsgSenderId;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatroomId, List<String> userIds, String msgTimeStamp, String lastMsgSenderId) {
        ChatroomId = chatroomId;
        this.userIds = userIds;
        this.msgTimeStamp = msgTimeStamp;
        this.lastMsgSenderId = lastMsgSenderId;
    }

    public String getChatroomId() {
        return ChatroomId;
    }

    public void setChatroomId(String chatroomId) {
        ChatroomId = chatroomId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
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
