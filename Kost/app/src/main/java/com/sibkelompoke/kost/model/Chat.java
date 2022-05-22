package com.sibkelompoke.kost.model;

public class Chat {
    private int id;
    private String chatId;
    private String outGoingMsgId;
    private String inComingMsgId;
    private String message;
    private boolean read;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getOutGoingMsgId() {
        return outGoingMsgId;
    }

    public void setOutGoingMsgId(String outGoingMsgId) {
        this.outGoingMsgId = outGoingMsgId;
    }

    public String getInComingMsgId() {
        return inComingMsgId;
    }

    public void setInComingMsgId(String inComingMsgId) {
        this.inComingMsgId = inComingMsgId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
