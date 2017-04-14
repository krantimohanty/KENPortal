package com.kenportal.users.local_db;

/**
 * Created by mantosh on 10/31/2015.
 */
public class DbModel {


    String emp_id;
    String emp_name;
    String emp_photo;
    String emp_code;
    String emp_date_of_joining;
    String emp_designation;
    String emp_section_id;
    String emp_section;
    String emp_department_id;
    String emp_department;
    String offc_loc;
    String ra_id;
    String ra;
    String email;
    String mobile;
    String emp_pre_address;
    String emp_pre_city;
    String emp_pre_city_id;
    String emp_pre_state;
    String emp_pre_country;
    String emp_pre_tel_r;
    String emp_pre_tel_o;
    String emp_per_address;
    String emp_per_city;
    String emp_per_city_id;
    String emp_per_state;
    String emp_per_country;
    String emp_per_tel_r;
    String emp_offc_location_id;
    String emp_per_tel_o;
    String emp_status;

    public DbModel() {
    }

    public DbModel(String emp_name) {
        this.emp_name = emp_name;
    }

    public DbModel(String emp_id, String emp_name, String emp_photo, String emp_code, String emp_date_of_joining, String emp_designation, String emp_section_id, String emp_section, String emp_department_id, String emp_department, String offc_loc, String ra_id, String ra, String email, String mobile, String emp_pre_address, String emp_pre_city, String emp_pre_city_id, String emp_pre_state, String emp_pre_country, String emp_pre_tel_r, String emp_pre_tel_o, String emp_per_address, String emp_per_city, String emp_per_city_id, String emp_per_state, String emp_per_country, String emp_per_tel_r, String emp_offc_location_id, String emp_per_tel_o,String emp_status) {
        this.emp_id = emp_id;
        this.emp_name = emp_name;
        this.emp_photo = emp_photo;
        this.emp_date_of_joining = emp_date_of_joining;
        this.emp_code = emp_code;
        this.emp_designation = emp_designation;
        this.emp_section_id = emp_section_id;
        this.emp_department_id = emp_department_id;
        this.emp_section = emp_section;
        this.emp_department = emp_department;
        this.offc_loc = offc_loc;
        this.ra_id = ra_id;
        this.ra = ra;
        this.email = email;
        this.mobile = mobile;
        this.emp_pre_address = emp_pre_address;
        this.emp_pre_city = emp_pre_city;
        this.emp_pre_city_id = emp_pre_city_id;
        this.emp_pre_state = emp_pre_state;
        this.emp_pre_country = emp_pre_country;
        this.emp_pre_tel_r = emp_pre_tel_r;
        this.emp_pre_tel_o = emp_pre_tel_o;
        this.emp_per_address = emp_per_address;
        this.emp_per_city = emp_per_city;
        this.emp_per_city_id = emp_per_city_id;
        this.emp_per_state = emp_per_state;
        this.emp_per_country = emp_per_country;
        this.emp_per_tel_r = emp_per_tel_r;
        this.emp_offc_location_id = emp_offc_location_id;
        this.emp_per_tel_o = emp_per_tel_o;
        this.emp_status = emp_status;
    }

    public String getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(String emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_name() {
        return emp_name;
    }

    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }

    public String getEmp_photo() {
        return emp_photo;
    }

    public void setEmp_photo(String emp_photo) {
        this.emp_photo = emp_photo;
    }

    public String getEmp_code() {
        return emp_code;
    }

    public void setEmp_code(String emp_code) {
        this.emp_code = emp_code;
    }

    public String getEmp_date_of_joining() {
        return emp_date_of_joining;
    }

    public void setEmp_date_of_joining(String emp_date_of_joining) {
        this.emp_date_of_joining = emp_date_of_joining;
    }

    public String getEmp_designation() {
        return emp_designation;
    }

    public void setEmp_designation(String emp_designation) {
        this.emp_designation = emp_designation;
    }

    public String getEmp_section_id() {
        return emp_section_id;
    }

    public void setEmp_section_id(String emp_section_id) {
        this.emp_section_id = emp_section_id;
    }

    public String getEmp_section() {
        return emp_section;
    }

    public void setEmp_section(String emp_section) {
        this.emp_section = emp_section;
    }

    public String getEmp_department_id() {
        return emp_department_id;
    }

    public void setEmp_department_id(String emp_department_id) {
        this.emp_department_id = emp_department_id;
    }

    public String getEmp_department() {
        return emp_department;
    }

    public void setEmp_department(String emp_department) {
        this.emp_department = emp_department;
    }

    public String getOffc_loc() {
        return offc_loc;
    }

    public void setOffc_loc(String offc_loc) {
        this.offc_loc = offc_loc;
    }

    public String getRa_id() {
        return ra_id;
    }

    public void setRa_id(String ra_id) {
        this.ra_id = ra_id;
    }

    public String getRa() {
        return ra;
    }

    public void setRa(String ra) {
        this.ra = ra;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmp_pre_address() {
        return emp_pre_address;
    }

    public void setEmp_pre_address(String emp_pre_address) {
        this.emp_pre_address = emp_pre_address;
    }

    public String getEmp_pre_city() {
        return emp_pre_city;
    }

    public void setEmp_pre_city(String emp_pre_city) {
        this.emp_pre_city = emp_pre_city;
    }

    public String getEmp_pre_city_id() {
        return emp_pre_city_id;
    }

    public void setEmp_pre_city_id(String emp_pre_city_id) {
        this.emp_pre_city_id = emp_pre_city_id;
    }

    public String getEmp_pre_state() {
        return emp_pre_state;
    }

    public void setEmp_pre_state(String emp_pre_state) {
        this.emp_pre_state = emp_pre_state;
    }

    public String getEmp_pre_country() {
        return emp_pre_country;
    }

    public void setEmp_pre_country(String emp_pre_country) {
        this.emp_pre_country = emp_pre_country;
    }

    public String getEmp_pre_tel_r() {
        return emp_pre_tel_r;
    }

    public void setEmp_pre_tel_r(String emp_pre_tel_r) {
        this.emp_pre_tel_r = emp_pre_tel_r;
    }

    public String getEmp_pre_tel_o() {
        return emp_pre_tel_o;
    }

    public void setEmp_pre_tel_o(String emp_pre_tel_o) {
        this.emp_pre_tel_o = emp_pre_tel_o;
    }

    public String getEmp_per_address() {
        return emp_per_address;
    }

    public void setEmp_per_address(String emp_per_address) {
        this.emp_per_address = emp_per_address;
    }

    public String getEmp_per_city() {
        return emp_per_city;
    }

    public void setEmp_per_city(String emp_per_city) {
        this.emp_per_city = emp_per_city;
    }

    public String getEmp_per_city_id() {
        return emp_per_city_id;
    }

    public void setEmp_per_city_id(String emp_per_city_id) {
        this.emp_per_city_id = emp_per_city_id;
    }

    public String getEmp_per_state() {
        return emp_per_state;
    }

    public void setEmp_per_state(String emp_per_state) {
        this.emp_per_state = emp_per_state;
    }

    public String getEmp_per_country() {
        return emp_per_country;
    }

    public void setEmp_per_country(String emp_per_country) {
        this.emp_per_country = emp_per_country;
    }

    public String getEmp_per_tel_r() {
        return emp_per_tel_r;
    }

    public void setEmp_per_tel_r(String emp_per_tel_r) {
        this.emp_per_tel_r = emp_per_tel_r;
    }

    public String getEmp_per_tel_o() {
        return emp_per_tel_o;
    }

    public void setEmp_per_tel_o(String emp_per_tel_o) {
        this.emp_per_tel_o = emp_per_tel_o;
    }

    public String getEmp_offc_location_id() {
        return emp_offc_location_id;
    }

    public String getEmp_status() {
        return emp_status;
    }

    public void setEmp_status(String emp_status) {
        this.emp_status = emp_status;
    }

    public void setEmp_offc_location_id(String emp_offc_location_id) {
        this.emp_offc_location_id = emp_offc_location_id;
    }
}
