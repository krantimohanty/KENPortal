package com.kenportal.users.datamodels;

import com.kenportal.users.app_interface.Item;

/**
 * Created by kranti on 11/26/2016.
 */
public class EntryItem implements Item {

    public final String title;
    public final String subtitle;
    public final String sub;

    public EntryItem(String title, String subtitle,String sub) {
        this.title = title;
        this.subtitle = subtitle;
        this.sub = sub;
    }


    @Override
    public boolean isSection() {
        return false;
    }
}
