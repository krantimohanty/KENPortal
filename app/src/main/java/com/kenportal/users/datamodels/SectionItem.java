package com.kenportal.users.datamodels;

import com.kenportal.users.app_interface.Item;

/**
 * Created by kranti on 11/26/2016.
 */
public class SectionItem implements Item{


    private  String title;
    private  String tag;

    public SectionItem(String title,String tag) {
        this.title = title;
        this.tag = tag;
    }

    public String getTitle(){
        return title;
    }

    public String getTag(){
        return tag;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Override
    public boolean isSection() {
        return true;
    }
}
