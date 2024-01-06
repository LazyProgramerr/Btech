package com.sai.btech.models;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomModel {
    String ChatroomId;
    ArrayList<String> chatRoomMembers;
    String msgTimeStamp;
    String msg;
    String lastMsgSenderId;
    String chatRoomType;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatroomId, ArrayList<String> chatRoomMembers, String msgTimeStamp,String msg, String lastMsgSenderId,String ChatRoomType) {
        this.ChatroomId = chatroomId;
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

    public String getChatroomId() {
        return ChatroomId;
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
