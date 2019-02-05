package com.example.maxim.diabetesireland;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Lorcan on 03/03/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat Tag
    private static final String Log = "DatabaseHelper";

    // Database Version
    private static final int DB_VERSION = 1;

    // Database Name
    public static final String DB_NAME = "userDB";

    // User Data Table
    public static final String USER_DATA = "userTable";
    public static final String UCOL_1 = "BIRTH_DATE";     // primary key
    public static final String UCOL_2 = "GENDER";         // male / female
    public static final String UCOL_3 = "HEIGHT";         // in metres or/and feet
    public static final String UCOL_4 = "WEIGHT";         // in kg
    public static final String UCOL_5 = "GOAL";           // daily targets?

    // Daily Performance Table
    public static final String DAILY_DATA = "daily_table";
    public static final String DCOL_1 = "DATE";           // primary key
    public static final String DCOL_2 = "FAT_INTAKE";
    public static final String DCOL_3 = "PROTEIN_INTAKE";
    public static final String DCOL_4 = "DAIRY_INTAKE";
    public static final String DCOL_5 = "FRUIT&VEG_INTAKE";
    public static final String DCOL_6 = "CARBOHYDRATE_INTAKE";
    public static final String DCOL_7 = "ALCOHOL_INTAKE";
    public static final String DCOL_8 = "STEPS";                // calories burned stored in db?
    public static final String DCOL_9 = "LIGHT_EXERCISE";
    public static final String DCOL_10 = "MODERATE_EXERCISE";
    public static final String DCOL_11 = "VIGOROUS_EXERCISE";
    public static final String DCOL_12 = "GOAL";               // daily target stored elsewhere?

    // Create Table Statements
    // User Data table creation
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + USER_DATA
            + "(" + UCOL_1 + " TEXT PRIMARY KEY," + UCOL_2 + " TEXT," + UCOL_3 + " REAL,"
            + UCOL_4 + " REAL," + UCOL_5 + " TEXT" + ")";

    // Daily Performance table creation
    private static final String CREATE_TABLE_DAILY = "CREATE TABLE " + DAILY_DATA
            + "(" + DCOL_1 + " TEXT PRIMARY KEY," + DCOL_2 + " REAL," + DCOL_3 + " REAL,"
            + DCOL_4 + " REAL," + DCOL_5 + " REAL," + DCOL_6 + " REAL," + DCOL_7 + " REAL,"
            + DCOL_7 + " REAL," + DCOL_8 + " INTEGER," + DCOL_9 + " INTEGER,"
            + DCOL_10 + " INTEGER," + DCOL_11 + " INTEGER," + DCOL_12 + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_DAILY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+USER_DATA);
        db.execSQL("DROP TABLE IF EXISTS "+DAILY_DATA);
        onCreate(db);
    }

    // Store User Data from Registration Screen
    public boolean insertUserData(String birth_date, String gender, double height, double weight, String goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues content = new ContentValues();
        content.put(UCOL_1, birth_date);
        content.put(UCOL_2, gender);
        content.put(UCOL_3, height);
        content.put(UCOL_4, weight);
        content.put(UCOL_5, goal);
        long result = db.insert(USER_DATA, null, content);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // Create new daily entry in database
    public boolean initDailyData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(new Date());
        ContentValues content = new ContentValues();
        content.put(DCOL_1, date);
        content.put(DCOL_2, 0.0);
        content.put(DCOL_3, 0.0);
        content.put(DCOL_4, 0.0);
        content.put(DCOL_5, 0.0);
        content.put(DCOL_6, 0.0);
        content.put(DCOL_7, 0.0);
        content.put(DCOL_8, 0);
        content.put(DCOL_9, 0);
        content.put(DCOL_10, 0);
        content.put(DCOL_11, 0);
        content.put(DCOL_12, 0);
        long result = db.insert(DAILY_DATA, null, content);
        if (result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    // Methods for storing data in database
    // Update Daily Data with exercise intake
    public int updateDailyExercise(int duration, String intensity) {
        // currently only updating current day's data
        SQLiteDatabase db = this.getWritableDatabase();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(new Date());
        db.execSQL("UPDATE " + DAILY_DATA + " SET " + intensity + " = " + intensity + " + " + duration + " WHERE " + DCOL_1 + " = " + date);
        return 1;
    }

    // Update Daily Data with food intake
    public int updateDailyFood(double portionSize, String type) {
        // currently only updating current day's data
        SQLiteDatabase db = this.getWritableDatabase();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.UK).format(new Date());
        db.execSQL("UPDATE " + DAILY_DATA + " SET " + type + " = " + type + " + " + portionSize + " WHERE " + DCOL_1 + " = " + date);
        return 1;
    }

    // Methods for fetching data from database

    // Closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}