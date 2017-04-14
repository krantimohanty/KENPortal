package com.kenportal.users.datamodels;

/**
 * Created by kranti on 11/26/2016.
 */
public class TodayWishModel {


    String user_id;
    String wish_date;
    String wish_type;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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