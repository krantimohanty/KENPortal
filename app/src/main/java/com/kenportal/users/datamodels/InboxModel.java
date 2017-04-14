package com.kenportal.users.datamodels;

/**
 * Created by kranti on 11/26/2016.
 */
public class InboxModel {

    String Photo;
    String greeting_id;
    String message;
    String reply_status;
    String send_by_id;
    String wish_date;
    String wish_type;

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getGreeting_id() {
        return greeting_id;
    }

    public void setGreeting_id(String greeting_id) {
        this.greeting_id = greeting_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReply_status() {
        return reply_status;
    }

    public void setReply_status(String reply_status) {
        this.reply_status = reply_status;
    }

    public String getSend_by_id() {
        return send_by_id;
    }

    public void setSend_by_id(String send_by_id) {
        this.send_by_id = send_by_id;
    }

    public String getWish_date() {
        return wish_date;
    }

    public void setWish_date(String wish_date) {
        this.wish_date = wish_date;
    }

    public String getWish_type() {
        return wish_type;
    }

    public void setWish_type(String wish_type) {
        this.wish_type = wish_type;
    }
}
