package com.example.chitchat.Models;

public class Message {

    private String uId;         //sender
    private String message;
    private String messageId;
    private Long timestamp;
    private String receiver;
    private boolean isSeen;


    public Message() {
    }

    public Message(String uId, String message, Long timestamp, String receiver, boolean isSeen) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
        this.receiver = receiver;
        this.isSeen= isSeen;
    }

    public Message(String uId, String message, Long timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public Message(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
