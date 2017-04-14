package com.kenportal.users.datamodels;

import com.kenportal.users.app_interface.Item;

import java.util.ArrayList;

/**
 * Created by kranti on 11/26/2016.
 */
public class LeaveModel implements Item {

    public String title;
    public String subtitle;
    public String sub;
    private ArrayList<String> allItemsInSection;

    public LeaveModel(){

    }

    public LeaveModel(String title,String subtitle,String sub) {
        this.title = title;
        this.subtitle = subtitle;
        this.sub = sub;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle(){
        return subtitle;
    }
    public void setSubtitle(String subtitle){
        this.subtitle=subtitle;
    }

    public String getSub(){
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    @Override
    public boolean isSection() {
        return false;
    }

    public ArrayList<String> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<String> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }
}
