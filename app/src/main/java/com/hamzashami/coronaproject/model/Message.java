package com.hamzashami.coronaproject.model;

public class Message {
    private String senderId, receiverId, messageText;
    private long date;

    public Message() {
    }

    public Message(String senderId, String receiverId, String messageText, long date) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageText = messageText;
        this.date = date;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
