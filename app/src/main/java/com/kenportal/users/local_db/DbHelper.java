package com.kenportal.users.local_db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mantosh on 10/31/2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    // Database Name
    public static String DATABASE_NAME = "kenportal.db";

    // Current version of database
    public static final int DATABASE_VERSION = 3;

    private SQLiteDatabase mDB;

    // Name of table
    public static final String TABLE_EMP = "kenportal_emp_directory";

    // All Keys used in table
    public static final String AUTO_ID = "auto_id";
    public static final String EMP_ID = "id";
    public static final String EMP_NAME = "emp_name";
    public static final String EMP_PHOTO = "emp_photo";
    public static final String EMP_CODE = "emp_code";
    public static final String EMP_DATE_OF_JOINING = "emp_date_of_joining";
    public static final String EMP_DESIGNATION = "emp_designation";
    public static final String EMP_SECTION_ID = "emp_section_id";
    public static final String EMP_SECTION = "emp_section";
    public static final String EMP_DEPT_ID = "emp_dept_id";
    public static final String EMP_DEPARTMENT = "emp_department";
    public static final String EMP_OFFCIE_LOC = "emp_office_loc";
    public static final String EMP_OFFCIE_LOC_ID = "emp_office_loc_id";
    public static final String EMP_RA_ID = "emp_ra_id";
    public static final String EMP_RA = "emp_ra";
    public static final String EMP_EMAIL = "emp_email";
    public static final String EMP_MOBILE = "emp_mobile";
    public static final String EMP_PRE_ADDRESS = "emp_pre_address";
    public static final String EMP_PRE_CITY = "emp_pre_city";
    public static final String EMP_PRE_CITY_ID = "emp_pre_city_id";
    public static final String EMP_PRE_STATE = "emp_pre_state";
    public static final String EMP_PRE_COUNTRY = "emp_pre_country";
    public static final String EMP_PRE_TEL_R = "emp_pre_tel_r";
    public static final String EMP_PRE_TEL_O = "emp_pre_tel_o";
    public static final String EMP_PER_ADDRESS = "emp_per_address";
    public static final String EMP_PER_CITY = "emp_per_city";
    public static final String EMP_PER_CITY_ID = "emp_per_city_id";
    public static final String EMP_PER_STATE = "emp_per_state";
    public static final String EMP_PER_COUNTRY = "emp_per_country";
    public static final String EMP_PER_TEL_R = "emp_per_tel_r";
    public static final String EMP_PER_TEL_O = "emp_per_tel_o";


    public static String TAG = "tag";

    public static final String CREATE_TABLE_EMP = "CREATE TABLE "
            + TABLE_EMP
            + "("
            + AUTO_ID + " INTEGER PRIMARY KEY,"
            + EMP_ID + " TEXT ,"
            + EMP_NAME + " TEXT,"
            + EMP_PHOTO + " TEXT,"
            + EMP_CODE + " TEXT,"
            + EMP_DATE_OF_JOINING + " TEXT,"
            + EMP_DESIGNATION + " TEXT,"
            + EMP_SECTION_ID + " TEXT,"
            + EMP_SECTION + " TEXT,"
            + EMP_DEPT_ID + " TEXT,"
            + EMP_DEPARTMENT + " TEXT,"
            + EMP_OFFCIE_LOC + " TEXT,"
            + EMP_RA_ID + " TEXT,"
            + EMP_RA + " TEXT,"
            + EMP_EMAIL + " TEXT,"
            + EMP_MOBILE + " TEXT,"
            + EMP_PRE_ADDRESS + " TEXT,"
            + EMP_PRE_CITY + " TEXT,"
            + EMP_PRE_CITY_ID + " TEXT,"
            + EMP_PRE_STATE + " TEXT,"
            + EMP_PRE_COUNTRY + " TEXT,"
            + EMP_PRE_TEL_R + " TEXT,"
            + EMP_PRE_TEL_O + " TEXT,"
            + EMP_PER_ADDRESS + " TEXT,"
            + EMP_PER_CITY + " TEXT,"
            + EMP_PER_CITY_ID + " TEXT,"
            + EMP_PER_STATE + " TEXT,"
            + EMP_PER_COUNTRY + " TEXT,"
            + EMP_PER_TEL_R + " TEXT,"
            + EMP_OFFCIE_LOC_ID + " TEXT,"
            + EMP_PER_TEL_O + " TEXT);";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME
                , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_EMP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMP); // drop table if exists
        onCreate(db);
    }


    public void addAllEmp(DbModel dbModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(EMP_ID, dbModel.getEmp_id());
        values.put(EMP_NAME, dbModel.getEmp_name());
        values.put(EMP_PHOTO, dbModel.getEmp_photo());
        values.put(EMP_CODE, dbModel.getEmp_code());
        values.put(EMP_DATE_OF_JOINING, dbModel.getEmp_date_of_joining());
        values.put(EMP_DESIGNATION, dbModel.getEmp_designation());
        values.put(EMP_SECTION_ID, dbModel.getEmp_section_id());
        values.put(EMP_SECTION, dbModel.getEmp_section());
        values.put(EMP_DEPT_ID, dbModel.getEmp_department_id());
        values.put(EMP_DEPARTMENT, dbModel.getEmp_department());
        values.put(EMP_OFFCIE_LOC, dbModel.getOffc_loc());
        values.put(EMP_RA_ID, dbModel.getRa_id());
        values.put(EMP_RA, dbModel.getRa());
        values.put(EMP_EMAIL, dbModel.getEmail());
        values.put(EMP_MOBILE, dbModel.getMobile());
        values.put(EMP_PRE_ADDRESS, dbModel.getEmp_pre_address());
        values.put(EMP_PRE_CITY, dbModel.getEmp_pre_city());
        values.put(EMP_PRE_CITY_ID, dbModel.getEmp_pre_city_id());
        values.put(EMP_PRE_STATE, dbModel.getEmp_pre_state());
        values.put(EMP_PRE_COUNTRY, dbModel.getEmp_pre_country());
        values.put(EMP_PRE_TEL_R, dbModel.getEmp_pre_tel_r());
        values.put(EMP_PRE_TEL_O, dbModel.getEmp_pre_tel_o());
        values.put(EMP_PER_ADDRESS, dbModel.getEmp_per_address());
        values.put(EMP_PER_CITY, dbModel.getEmp_per_city());
        values.put(EMP_PER_CITY_ID, dbModel.getEmp_per_city_id());
        values.put(EMP_PER_STATE, dbModel.getEmp_per_state());
        values.put(EMP_PER_COUNTRY, dbModel.getEmp_per_country());
        values.put(EMP_PER_TEL_R, dbModel.getEmp_per_tel_r());
        values.put(EMP_OFFCIE_LOC_ID, dbModel.getEmp_offc_location_id());
        values.put(EMP_PER_TEL_O, dbModel.getEmp_per_tel_o());
        // insert row in emp table
        db.insert(TABLE_EMP, null, values);
        db.close();
    }

    public Cursor getAllCustomers() {
        return mDB.query(TABLE_EMP, new String[]{EMP_ID, EMP_NAME, EMP_PHOTO, EMP_CODE, EMP_DATE_OF_JOINING, EMP_DESIGNATION, EMP_SECTION_ID, EMP_SECTION, EMP_DEPT_ID, EMP_DEPARTMENT, EMP_OFFCIE_LOC, EMP_RA_ID, EMP_RA, EMP_EMAIL, EMP_MOBILE, EMP_PRE_ADDRESS, EMP_PRE_CITY, EMP_PRE_CITY_ID, EMP_PRE_STATE, EMP_PRE_COUNTRY, EMP_PRE_TEL_R, EMP_PRE_TEL_O, EMP_PER_ADDRESS, EMP_PER_CITY, EMP_PER_CITY_ID, EMP_PER_STATE, EMP_PER_COUNTRY, EMP_PER_TEL_R, EMP_OFFCIE_LOC_ID, EMP_PER_TEL_O},
                null, null, null, null, null, null);
    }

    public List<DbModel> getAllEmp() {
        List<DbModel> modelList = new ArrayList<DbModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_EMP + " ORDER BY " + EMP_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DbModel model = new DbModel();
                model.setEmp_id(cursor.getString(1));
                model.setEmp_name(cursor.getString(2));
                model.setEmp_photo(cursor.getString(3));
                model.setEmp_code(cursor.getString(4));
                model.setEmp_date_of_joining(cursor.getString(5));
                model.setEmp_designation(cursor.getString(6));
                model.setEmp_section_id(cursor.getString(7));
                model.setEmp_section(cursor.getString(8));
                model.setEmp_department_id(cursor.getString(9));
                model.setEmp_department(cursor.getString(10));
                model.setOffc_loc(cursor.getString(11));
                model.setRa_id(cursor.getString(12));
                model.setRa(cursor.getString(13));
                model.setEmail(cursor.getString(14));
                model.setMobile(cursor.getString(15));
                model.setEmp_pre_address(cursor.getString(16));
                model.setEmp_pre_city(cursor.getString(17));
                model.setEmp_pre_city_id(cursor.getString(18));
                model.setEmp_pre_state(cursor.getString(19));
                model.setEmp_pre_country(cursor.getString(20));
                model.setEmp_pre_tel_r(cursor.getString(21));
                model.setEmp_pre_tel_o(cursor.getString(22));
                model.setEmp_per_address(cursor.getString(23));
                model.setEmp_per_city(cursor.getString(24));
                model.setEmp_per_city_id(cursor.getString(25));
                model.setEmp_per_state(cursor.getString(26));
                model.setEmp_per_country(cursor.getString(27));
                model.setEmp_per_tel_r(cursor.getString(28));
                model.setEmp_per_tel_o(cursor.getString(29));
                // Adding data to list
                modelList.add(model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return modelList;
    }

    public List<DbModel> getAllEmpName() {
        List<DbModel> modelList = new ArrayList<DbModel>();
        // Select All Query
        String selectQuery = "SELECT " + EMP_NAME + " FROM " + TABLE_EMP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DbModel model = new DbModel();
                //model.setEmp_id(cursor.getString(1));
                model.setEmp_name(cursor.getString(0));
              /*  model.setEmp_photo(cursor.getString(3));
                model.setEmp_code(cursor.getString(4));
                model.setEmp_date_of_joining(cursor.getString(5));
                model.setEmp_designation(cursor.getString(6));
                model.setEmp_section_id(cursor.getString(7));
                model.setEmp_section(cursor.getString(8));
                model.setEmp_department_id(cursor.getString(9));
                model.setEmp_department(cursor.getString(10));
                model.setOffc_loc(cursor.getString(11));
                model.setRa_id(cursor.getString(12));
                model.setRa(cursor.getString(13));
                model.setEmail(cursor.getString(14));
                model.setMobile(cursor.getString(15));
                model.setEmp_pre_address(cursor.getString(16));
                model.setEmp_pre_city(cursor.getString(17));
                model.setEmp_pre_city_id(cursor.getString(18));
                model.setEmp_pre_state(cursor.getString(19));
                model.setEmp_pre_country(cursor.getString(20));
                model.setEmp_pre_tel_r(cursor.getString(21));
                model.setEmp_pre_tel_o(cursor.getString(22));
                model.setEmp_per_address(cursor.getString(23));
                model.setEmp_per_city(cursor.getString(24));
                model.setEmp_per_city_id(cursor.getString(25));
                model.setEmp_per_state(cursor.getString(26));
                model.setEmp_per_country(cursor.getString(27));
                model.setEmp_per_tel_r(cursor.getString(28));
                model.setEmp_per_tel_o(cursor.getString(29));*/
                // Adding data to list
                modelList.add(model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return modelList;
    }

    public List<DbModel> getPaggingEmp(int start) {
        List<DbModel> modelList = new ArrayList<DbModel>();
        // Select All Query
        int end = start + 20;
        String selectQuery = "SELECT  * FROM " + TABLE_EMP + " where " + AUTO_ID + ">=" + start + " and " + AUTO_ID + "<" + end + " ORDER BY " + EMP_NAME;

        Log.d("atag", selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DbModel model = new DbModel();
                model.setEmp_id(cursor.getString(1));
                model.setEmp_name(cursor.getString(2));
                model.setEmp_photo(cursor.getString(3));
                model.setEmp_code(cursor.getString(4));
                model.setEmp_date_of_joining(cursor.getString(5));
                model.setEmp_designation(cursor.getString(6));
                model.setEmp_section_id(cursor.getString(7));
                model.setEmp_section(cursor.getString(8));
                model.setEmp_department_id(cursor.getString(9));
                model.setEmp_department(cursor.getString(10));
                model.setOffc_loc(cursor.getString(11));
                model.setRa_id(cursor.getString(12));
                model.setRa(cursor.getString(13));
                model.setEmail(cursor.getString(14));
                model.setMobile(cursor.getString(15));
                model.setEmp_pre_address(cursor.getString(16));
                model.setEmp_pre_city(cursor.getString(17));
                model.setEmp_pre_city_id(cursor.getString(18));
                model.setEmp_pre_state(cursor.getString(19));
                model.setEmp_pre_country(cursor.getString(20));
                model.setEmp_pre_tel_r(cursor.getString(21));
                model.setEmp_pre_tel_o(cursor.getString(22));
                model.setEmp_per_address(cursor.getString(23));
                model.setEmp_per_city(cursor.getString(24));
                model.setEmp_per_city_id(cursor.getString(25));
                model.setEmp_per_state(cursor.getString(26));
                model.setEmp_per_country(cursor.getString(27));
                model.setEmp_per_tel_r(cursor.getString(28));
                model.setEmp_per_tel_o(cursor.getString(29));
                // Adding data to list
                modelList.add(model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return modelList;
    }

    public List<DbModel> getPaggingLocationEmp(int start, int deptId) {
        List<DbModel> modelList = new ArrayList<DbModel>();
        // Select All Query
        int end = start + 20;
        String selectQuery = "SELECT  * FROM " + TABLE_EMP + " where " + AUTO_ID + ">=" + start + " and " + AUTO_ID + "<" + end;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DbModel model = new DbModel();
                model.setEmp_id(cursor.getString(1));
                model.setEmp_name(cursor.getString(2));
                model.setEmp_photo(cursor.getString(3));
                model.setEmp_code(cursor.getString(4));
                model.setEmp_date_of_joining(cursor.getString(5));
                model.setEmp_designation(cursor.getString(6));
                model.setEmp_section_id(cursor.getString(7));
                model.setEmp_section(cursor.getString(8));
                model.setEmp_department_id(cursor.getString(9));
                model.setEmp_department(cursor.getString(10));
                model.setOffc_loc(cursor.getString(11));
                model.setRa_id(cursor.getString(12));
                model.setRa(cursor.getString(13));
                model.setEmail(cursor.getString(14));
                model.setMobile(cursor.getString(15));
                model.setEmp_pre_address(cursor.getString(16));
                model.setEmp_pre_city(cursor.getString(17));
                model.setEmp_pre_city_id(cursor.getString(18));
                model.setEmp_pre_state(cursor.getString(19));
                model.setEmp_pre_country(cursor.getString(20));
                model.setEmp_pre_tel_r(cursor.getString(21));
                model.setEmp_pre_tel_o(cursor.getString(22));
                model.setEmp_per_address(cursor.getString(23));
                model.setEmp_per_city(cursor.getString(24));
                model.setEmp_per_city_id(cursor.getString(25));
                model.setEmp_per_state(cursor.getString(26));
                model.setEmp_per_country(cursor.getString(27));
                model.setEmp_per_tel_r(cursor.getString(28));
                model.setEmp_per_tel_o(cursor.getString(29));
                // Adding data to list
                modelList.add(model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return modelList;
    }


    //get db count
    public int getDbCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_EMP;
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    //get department count
    public String getDepartmentCount(String id) {
        String cnt = "0";
        try {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select count(*) from " + TABLE_EMP + " where " + EMP_DEPT_ID + " =" + id + ";";

            Log.i("atag",query);

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
//            name = cursor.getString(0);
            cnt = cursor.getString(0);
//            }
        }
        cursor.close();
        }catch (SQLiteException e){

        }
        return cnt;
    }

    //get location count
    public String getLocationCount(String id) {
        String cnt = "0";
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "select count(*) from " + TABLE_EMP + " where " + EMP_OFFCIE_LOC_ID + " =" + id + ";";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
//            name = cursor.getString(0);
                cnt = cursor.getString(0);
//            }
            }

            cursor.close();
        }catch (SQLiteException e){

        }
        return cnt;
    }

    //get emp photo by using id
    public String getEmpPic(String id) {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + EMP_PHOTO + " from " + TABLE_EMP + " where " + EMP_ID + " = " + id + ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
            name = cursor.getString(0);
//            }
        }

        cursor.close();
        return name;
    }

    //get emp name by using id
    public String getEmpName(String id) {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + EMP_NAME + " from " + TABLE_EMP + " where " + EMP_ID + " =" + id + ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
            name = cursor.getString(0);
//            }
        }

        cursor.close();
        return name;
    }

    //get emp designation by using id
    public String getEmpDesignation(String id) {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + EMP_DESIGNATION + " from " + TABLE_EMP + " where " + EMP_ID + " =" + id + ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
            name = cursor.getString(0);
//            }
        }

        cursor.close();
        return name;
    }

    //get emp location by using id
    public String getEmpLocation(String id) {
        String name = "";
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select " + EMP_OFFCIE_LOC + " from " + TABLE_EMP + " where " + EMP_ID + " =" + id + ";";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
//            while (cursor.isAfterLast() != true) {
            name = cursor.getString(0);
//            }
        }

        cursor.close();
        return name;
    }

    //get emp name by using id
    public DbModel getEmpById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        DbModel model = new DbModel();
        // Select All Query
        String selectQuery = "select * from " + TABLE_EMP + " where " + EMP_ID + " =" + Integer.parseInt(id) + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {

                model.setEmp_id(cursor.getString(1));
                model.setEmp_name(cursor.getString(2));
                model.setEmp_photo(cursor.getString(3));
                model.setEmp_code(cursor.getString(4));
                model.setEmp_date_of_joining(cursor.getString(5));
                model.setEmp_designation(cursor.getString(6));
                model.setEmp_section_id(cursor.getString(7));
                model.setEmp_section(cursor.getString(8));
                model.setEmp_department_id(cursor.getString(9));
                model.setEmp_department(cursor.getString(10));
                model.setOffc_loc(cursor.getString(11));
                model.setRa_id(cursor.getString(12));
                model.setRa(cursor.getString(13));
                model.setEmail(cursor.getString(14));
                model.setMobile(cursor.getString(15));
                model.setEmp_pre_address(cursor.getString(16));
                model.setEmp_pre_city(cursor.getString(17));
                model.setEmp_pre_city_id(cursor.getString(18));
                model.setEmp_pre_state(cursor.getString(19));
                model.setEmp_pre_country(cursor.getString(20));
                model.setEmp_pre_tel_r(cursor.getString(21));
                model.setEmp_pre_tel_o(cursor.getString(22));
                model.setEmp_per_address(cursor.getString(23));
                model.setEmp_per_city(cursor.getString(24));
                model.setEmp_per_city_id(cursor.getString(25));
                model.setEmp_per_state(cursor.getString(26));
                model.setEmp_per_country(cursor.getString(27));
                model.setEmp_per_tel_r(cursor.getString(28));
                model.setEmp_per_tel_o(cursor.getString(29));

            } while (cursor.moveToNext());
        }

        // return contact list
        return model;
    }

    //getEmployee by location
    public List<DbModel> getPaggingEmpByLoc(String loc_id, int start) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DbModel> modelList = new ArrayList<DbModel>();
        int end = start + 20;
        // Select All Query

//        String selectQuery = "select (select count(*) from " + TABLE_EMP + "  b  where a." + AUTO_ID + " >= b." + AUTO_ID + " and b." + EMP_OFFCIE_LOC_ID + "=" + loc_id + " )" +
//                " cnt_id, * from " + TABLE_EMP + " a where a." + EMP_OFFCIE_LOC_ID + "=" + loc_id + " and cnt_id>=" + start + " and cnt_id<" + end + " ORDER BY " + EMP_NAME;

        String selectQuery = "select * from " + TABLE_EMP + " a where a." + EMP_OFFCIE_LOC_ID + "=" + loc_id + "  ORDER BY " + EMP_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DbModel model = new DbModel();
                model.setEmp_id(cursor.getString(1));
                model.setEmp_name(cursor.getString(2));
                model.setEmp_photo(cursor.getString(3));
                model.setEmp_code(cursor.getString(4));
                model.setEmp_date_of_joining(cursor.getString(5));
                model.setEmp_designation(cursor.getString(6));
                model.setEmp_section_id(cursor.getString(7));
                model.setEmp_section(cursor.getString(8));
                model.setEmp_department_id(cursor.getString(9));
                model.setEmp_department(cursor.getString(10));
                model.setOffc_loc(cursor.getString(11));
                model.setRa_id(cursor.getString(12));
                model.setRa(cursor.getString(13));
                model.setEmail(cursor.getString(14));
                model.setMobile(cursor.getString(15));
                model.setEmp_pre_address(cursor.getString(16));
                model.setEmp_pre_city(cursor.getString(17));
                model.setEmp_pre_city_id(cursor.getString(18));
                model.setEmp_pre_state(cursor.getString(19));
                model.setEmp_pre_country(cursor.getString(20));
                model.setEmp_pre_tel_r(cursor.getString(21));
                model.setEmp_pre_tel_o(cursor.getString(22));
                model.setEmp_per_address(cursor.getString(23));
                model.setEmp_per_city(cursor.getString(24));
                model.setEmp_per_city_id(cursor.getString(25));
                model.setEmp_per_state(cursor.getString(26));
                model.setEmp_per_country(cursor.getString(27));
                model.setEmp_per_tel_r(cursor.getString(28));
                model.setEmp_per_tel_o(cursor.getString(29));
                // Adding data to list
                modelList.add(model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return modelList;
    }

    //getEmployee by location
    public List<DbModel> getPaggingEmpByDept(String dept_id, int start) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<DbModel> modelList = new ArrayList<DbModel>();
//        int end = start + 20;
        // Select All Query
//        String selectQuery = "select (select count(*) from " + TABLE_EMP + "  b  where a." + AUTO_ID + " >= b." + AUTO_ID + " and b." + EMP_DEPT_ID + "=" + dept_id + " )" +
//                " cnt_id, * from " + TABLE_EMP + " a where a." + EMP_DEPT_ID + "=" + dept_id + " and cnt_id>=" + start + " and cnt_id<" + end + " ORDER BY " + EMP_NAME;
        String selectQuery = "select * from " + TABLE_EMP + " a where a." + EMP_DEPT_ID + "=" + dept_id + " ORDER BY " + EMP_NAME;

        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DbModel model = new DbModel();
                model.setEmp_id(cursor.getString(1));
                model.setEmp_name(cursor.getString(2));
                model.setEmp_photo(cursor.getString(3));
                model.setEmp_code(cursor.getString(4));
                model.setEmp_date_of_joining(cursor.getString(5));
                model.setEmp_designation(cursor.getString(6));
                model.setEmp_section_id(cursor.getString(7));
                model.setEmp_section(cursor.getString(8));
                model.setEmp_department_id(cursor.getString(9));
                model.setEmp_department(cursor.getString(10));
                model.setOffc_loc(cursor.getString(11));
                model.setRa_id(cursor.getString(12));
                model.setRa(cursor.getString(13));
                model.setEmail(cursor.getString(14));
                model.setMobile(cursor.getString(15));
                model.setEmp_pre_address(cursor.getString(16));
                model.setEmp_pre_city(cursor.getString(17));
                model.setEmp_pre_city_id(cursor.getString(18));
                model.setEmp_pre_state(cursor.getString(19));
                model.setEmp_pre_country(cursor.getString(20));
                model.setEmp_pre_tel_r(cursor.getString(21));
                model.setEmp_pre_tel_o(cursor.getString(22));
                model.setEmp_per_address(cursor.getString(23));
                model.setEmp_per_city(cursor.getString(24));
                model.setEmp_per_city_id(cursor.getString(25));
                model.setEmp_per_state(cursor.getString(26));
                model.setEmp_per_country(cursor.getString(27));
                model.setEmp_per_tel_r(cursor.getString(28));
                model.setEmp_per_tel_o(cursor.getString(29));
                // Adding data to list
                modelList.add(model);
            } while (cursor.moveToNext());
        }

        // return contact list
        return modelList;
    }


    //get emp name by using id
    public DbModel getObjectEmpById(String id) {
//        DbModel modelList = new DbModel();
        DbModel model = new DbModel();
        // Select All Query
        String selectQuery = "select * from " + TABLE_EMP + " where " + EMP_ID + " =" + id + ";";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
//            do {

            model.setEmp_id(cursor.getString(1));
            model.setEmp_name(cursor.getString(2));
            model.setEmp_photo(cursor.getString(3));
            model.setEmp_code(cursor.getString(4));
            model.setEmp_date_of_joining(cursor.getString(5));
            model.setEmp_designation(cursor.getString(6));
            model.setEmp_section_id(cursor.getString(7));
            model.setEmp_section(cursor.getString(8));
            model.setEmp_department_id(cursor.getString(9));
            model.setEmp_department(cursor.getString(10));
            model.setOffc_loc(cursor.getString(11));
            model.setRa_id(cursor.getString(12));
            model.setRa(cursor.getString(13));
            model.setEmail(cursor.getString(14));
            model.setMobile(cursor.getString(15));
            model.setEmp_pre_address(cursor.getString(16));
            model.setEmp_pre_city(cursor.getString(17));
            model.setEmp_pre_city_id(cursor.getString(18));
            model.setEmp_pre_state(cursor.getString(19));
            model.setEmp_pre_country(cursor.getString(20));
            model.setEmp_pre_tel_r(cursor.getString(21));
            model.setEmp_pre_tel_o(cursor.getString(22));
            model.setEmp_per_address(cursor.getString(23));
            model.setEmp_per_city(cursor.getString(24));
            model.setEmp_per_city_id(cursor.getString(25));
            model.setEmp_per_state(cursor.getString(26));
            model.setEmp_per_country(cursor.getString(27));
            model.setEmp_per_tel_r(cursor.getString(28));
            model.setEmp_per_tel_o(cursor.getString(29));
            // Adding data to list
//                modelList.add(model);
//            } while (cursor.moveToNext());
        }

        // return contact list
        return model;
    }

    //get user status

  /*  public DbModel getCordinate(int keyid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_LOCATION, new String[]{KEY_ID, KEY_NAME,
                        KEY_DISTANCE, KEY_ADDRESS, KEY_LATLNGS, KEY_FLAG}, KEY_ID + "=?",
                new String[]{String.valueOf(keyid)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DbModel model = new DbModel(Integer.parseInt(cursor.getString(0)), cursor.getString(1),
                cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        // return result
        return model;
    }*/

    public void deleteEmployeeData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMP, null, null);
        db.close();
    }



    public List<String> getSearchName(String term){
        SQLiteDatabase db = this.getReadableDatabase();
        List<String> modelList = new ArrayList<String>();
//        int end = start + 20;
        // Select All Query
//        String selectQuery = "select (select count(*) from " + TABLE_EMP + "  b  where a." + AUTO_ID + " >= b." + AUTO_ID + " and b." + EMP_DEPT_ID + "=" + dept_id + " )" +
//                " cnt_id, * from " + TABLE_EMP + " a where a." + EMP_DEPT_ID + "=" + dept_id + " and cnt_id>=" + start + " and cnt_id<" + end + " ORDER BY " + EMP_NAME;
        String selectQuery = "select "+EMP_NAME+" from " + TABLE_EMP + " where " +  EMP_NAME +" like '"+term+"%'";
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                modelList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        // return contact list
        return modelList;
    }


}
