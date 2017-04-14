package com.kenportal.users.utils;

import com.joanzapata.iconify.Icon;

public enum IcoMoonIcons implements Icon {

    ic_attend('\ue900'),
    ic_claim('\ue902'),
    ic_down('\ue903'),
    ic_empdir('\ue904'),
    ic_emptrack('\ue905'),
    ic_gal('\ue906'),
    ic_leave('\ue907'),
    ic_meetSchedule('\ue908'),
    ic_menu('\ue909'),
    ic_messengr('\ue90a'),
    ic_notofictn('\ue90b'),
    ic_pass('\ue90c'),
    ic_salslip('\ue90d'),
    ic_sett('\ue90e'),
    ic_user('\ue90f'),
    ic_tour('\ue912'),
    ic_wish('\ue910'),
    ic_noconnection('\ue911'),
    ic_double_check('\ue916'),
    ic_sad('\ue901'),
    ic_linkedin('\ue913'),
    ic_facebook('\ue914'),
    ic_twitter('\ue915'),
    ic_orgfb('\ue917'),
    ic_orgtw('\ue918'),
    ic_warn('\ue91a'),
    ic_orgin('\ue919');


    char character;

    IcoMoonIcons(char character) {
        this.character = character;
    }

    @Override
    public String key() {
        return name().replace('_', '-');
    }

    @Override
    public char character() {
        return character;
    }
}