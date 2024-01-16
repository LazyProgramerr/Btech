package com.sai.btech.models;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomModel {
    String ChatRoomId;
    ArrayList<String> chatRoomMembers;
    String msgTimeStamp;
    String msg;
    String lastMsgSenderId;
    String chatRoomType;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomId, ArrayList<String> chatRoomMembers, String msgTimeStamp,String msg, String lastMsgSenderId,String ChatRoomType) {
        this.ChatRoomId = chatRoomId;
        this.chatRoomMembers = chatRoomMembers;
        this.msgTimeStamp = msgTimeStamp;
        this.msg = msg;
        this.lastMsgSenderId = lastMsgSenderId;
        this.chatRoomType = ChatRoomType;
    }

    public ChatRoomModel(String chatRoomId, ArrayList<String> members, String valueOf, String s, String user1) {
    }

    public String getChatRoomType() {
        return chatRoomType;
    }

    public String getChatRoomId() {
        return ChatRoomId;
    }

    public ArrayList<String> getChatRoomMembers() {
        return chatRoomMembers;
    }

    public String getMsgTimeStamp() {
        return msgTimeStamp;
    }

    public String getMsg() {
        return msg;
    }

    public String getLastMsgSenderId() {
        return lastMsgSenderId;
    }
}
