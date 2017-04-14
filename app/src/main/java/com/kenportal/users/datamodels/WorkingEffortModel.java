package com.kenportal.users.datamodels;

/**
 * Created by kranti on 11/26/2016.
 */
public class WorkingEffortModel {

    String DATE;
    String DAYNAME;
    String EFFORT_HOUR;
    String HOLIDAYTYPE;
    String INTIME;
    String LEAVESTATUS;
    String OUTTIME;

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }

    public void setDAYNAME(String DAYNAME) {
        this.DAYNAME = DAYNAME;
    }

    public String getDATE() {
        return DATE;
    }

    public String getDAYNAME() {
        return DAYNAME;
    }

    public void setEFFORT_HOUR(String EFFORT_HOUR) {
        this.EFFORT_HOUR = EFFORT_HOUR;
    }

    public String getEFFORT_HOUR() {
        return EFFORT_HOUR;
    }

    public void setHOLIDAYTYPE(String HOLIDAYTYPE) {
        this.HOLIDAYTYPE = HOLIDAYTYPE;
    }

    public String getHOLIDAYTYPE() {
        return HOLIDAYTYPE;
    }

    public void setINTIME(String INTIME) {
        this.INTIME = INTIME;
    }

    public String getINTIME() {
        return INTIME;
    }

    public void setLEAVESTATUS(String LEAVESTATUS) {
        this.LEAVESTATUS = LEAVESTATUS;
    }

    public String getLEAVESTATUS() {
        return LEAVESTATUS;
    }

    public void setOUTTIME(String OUTTIME) {
        this.OUTTIME = OUTTIME;
    }

    public String getOUTTIME() {
        return OUTTIME;
    }
}
