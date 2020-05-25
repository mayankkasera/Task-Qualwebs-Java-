package com.example.task_qualwebs_java.model;

public class Chat {

    String message;
    String status;
    Long time;
    String type;
    String sender;
    String reciver;

    public Chat() {
    }

    @Override
    public String toString() {
        return "Chat{" +
                "message='" + message + '\'' +
                ", status='" + status + '\'' +
                ", time=" + time +
                ", type='" + type + '\'' +
                ", sender='" + sender + '\'' +
                ", reciver='" + reciver + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciver() {
        return reciver;
    }

    public void setReciver(String reciver) {
        this.reciver = reciver;
    }
}
