package com.example.unionline.DTO;

public class Notification {
    private String id;
    private String title;
    private String content;
    private int image;
    private String sentFrom;
    private String sentTo;
    private String createDate;

    public Notification(){}

    public Notification(String id, String title, String content, int image, String sentFrom, String sentTo, String createDate) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.image = image;
        this.sentFrom = sentFrom;
        this.sentTo = sentTo;
        this.createDate = createDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSentTo() {
        return sentTo;
    }

    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getSentFrom() {
        return sentFrom;
    }

    public void setSentFrom(String sentFrom) {
        this.sentFrom = sentFrom;
    }
}
