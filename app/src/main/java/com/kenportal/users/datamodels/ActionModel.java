package com.kenportal.users.datamodels;

/**
 * Created by kranti on 11/26/2016.
 */
public class ActionModel {
    String action_id;
    String action_subject;
    String action_type;
    String action_url;
    String date;
    String user_id;

    public String getAction_id() {
        return action_id;
    }

    public void setAction_id(String action_id) {
        this.action_id = action_id;
    }

    public String getAction_subject() {
        return action_subject;
    }

    public void setAction_subject(String action_subject) {
        this.action_subject = action_subject;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getAction_url() {
        return action_url;
    }

    public void setAction_url(String action_url) {
        this.action_url = action_url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
