package com.deepakbaliga.ulka.Models;

/**
 * Created by deezdroid on 28/09/15.
 */
public class ChatModel {

    private String messageID;
    private String message;
    private boolean me;


    public ChatModel() {
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }
}
