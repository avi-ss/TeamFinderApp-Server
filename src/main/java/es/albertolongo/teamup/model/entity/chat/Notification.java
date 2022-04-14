package es.albertolongo.teamup.model.entity.chat;

import java.io.Serializable;

public class Notification implements Serializable {

    private Long id;
    private String senderId;
    private String senderName;

    public Notification(Long id, String senderId, String senderName) {
    }

    public Long getId() {
        return id;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
