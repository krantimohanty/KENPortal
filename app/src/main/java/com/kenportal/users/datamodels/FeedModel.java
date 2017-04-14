package com.kenportal.users.datamodels;

/**
 * Created by kranti on 11/26/2016.
 */
public class FeedModel {



    private String message;
    private String id;
    private String created_time;
    private String full_picture;
    private String link;
    private String story;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getFull_picture() {
        return full_picture;
    }

    public void setFull_picture(String full_picture) {
        this.full_picture = full_picture;
    }

    public String getLink() {
        return link;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
