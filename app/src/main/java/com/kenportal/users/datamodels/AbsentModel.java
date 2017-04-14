package com.kenportal.users.datamodels;

import com.kenportal.users.app_interface.Item;

/**
 * Created by kranti on 11/26/2016.
 * Create AbsentModel Class
 */
public class AbsentModel implements Item{


    public String slno;
    public String empname;
    public String status;
    public String Date;
    public String dayTime;

    public AbsentModel(){

    }

    public AbsentModel(String slno,String empname,String status){
        this.slno=slno;
        this.empname=empname;
        this.status=status;

    }

    public void setDate(String date) {
        this.Date = Date;
    }

    public String getDate() {
        return Date;
    }

    public void setDayTime(String dayTime) {
        this.dayTime = dayTime;
    }

    public String getDayTime() {
        return dayTime;
    }

    public String getSlno() {
        return slno;
    }

    public void setSlno(String slno) {
        this.slno = slno;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getEmpname() {
        return empname;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public boolean isSection() {
        return false;
    }
}
