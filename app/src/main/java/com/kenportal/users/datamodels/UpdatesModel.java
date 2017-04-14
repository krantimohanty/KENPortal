package com.kenportal.users.datamodels;

/**
 * Created by kranti on 11/26/2016.
 */
public class UpdatesModel {
    String date;
    String read_status;
    String update_detail;
    String update_id;
    String update_subject;
    String user_id;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRead_status() {
        return read_status;
    }

    public void setRead_status(String read_status) {
        this.read_status = read_status;
    }

    public String getUpdate_id() {
        return update_id;
    }

    public void setUpdate_id(String update_id) {
        this.update_id = update_id;
    }

    public String getUpdate_detail() {
        return update_detail;
    }

    public void setUpdate_detail(String update_detail) {
        this.update_detail = update_detail;
    }

    public String getUpdate_subject() {
        return update_subject;
    }

    public void setUpdate_subject(String update_subject) {
        this.update_subject = update_subject;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
