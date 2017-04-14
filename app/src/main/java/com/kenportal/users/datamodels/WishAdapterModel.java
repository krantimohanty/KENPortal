package com.kenportal.users.datamodels;

/**
 * Created by kranti on 11/26/2016.
 */
public class WishAdapterModel {

    String list_text;
    String list_icon;
    String num_wish;

    public WishAdapterModel(String list_icon, String list_text, String num_wish) {
        this.list_icon = list_icon;
        this.list_text = list_text;
        this.num_wish = num_wish;
    }

    public String getNum_wish() {
        return num_wish;
    }

    public void setNum_wish(String num_wish) {
        this.num_wish = num_wish;
    }

    public String getList_text() {
        return list_text;
    }

    public void setList_text(String list_text) {
        this.list_text = list_text;
    }

    public String getList_icon() {
        return list_icon;
    }

    public void setList_icon(String list_icon) {
        this.list_icon = list_icon;
    }
}
