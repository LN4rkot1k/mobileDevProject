package com.example.minerapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.util.Log;

public class DatabaseRecordsHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "records.db"; // Новая база для рекордов
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_RECORDS = "records";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_TIME = "time";

    public DatabaseRecordsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создаем таблицу для рекордов
        String createTable = "CREATE TABLE " + TABLE_RECORDS + " (" +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_LEVEL + " TEXT, " +
                COLUMN_TIME + " TEXT, " +
                "PRIMARY KEY (" + COLUMN_USERNAME + ", " + COLUMN_LEVEL + "));";  // Уникальная комбинация username + level
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    // Метод для добавления нового рекорда
    public boolean addRecord(String username, String level, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("username", username);
        values.put("level", level);
        values.put("time", time);

        long result = db.insert("records", null, values);

        if (result == -1) {
            Log.e("DatabaseRecordsHelper", "Error to added");
            return false; // Ошибка при вставке
        } else {
            Log.d("DatabaseRecordsHelper", "Success to added");
            return true; // Успех
        }
    }


    public Cursor getTopRecords() {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT rowid AS _id, username, level, time " +
                "FROM records " +
                "WHERE (level, username, time) IN ( " +
                "   SELECT level, username, MIN(time) FROM records " +
                "   GROUP BY level, username " +
                "   UNION ALL " +
                "   SELECT level, username, MIN(time) FROM records " +
                "   WHERE (level, username, time) NOT IN ( " +
                "       SELECT level, username, MIN(time) FROM records " +
                "       GROUP BY level, username " +
                "   ) " +
                "   GROUP BY level, username " +
                ") " +
                "ORDER BY username ASC, level ASC, time ASC";


        return db.rawQuery(query, null);
    }


}
