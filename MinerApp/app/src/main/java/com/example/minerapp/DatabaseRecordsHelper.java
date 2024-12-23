package com.example.minerapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseRecordsHelper extends SQLiteOpenHelper {

    // Имя базы данных и версия
    private static final String DATABASE_NAME = "records.db";
    private static final int DATABASE_VERSION = 1;

    // Название таблицы и колонки
    public static final String TABLE_NAME = "records";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_LEVEL = "level";
    public static final String COLUMN_TIME = "time";

    // SQL-запрос для создания таблицы
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_LEVEL + " TEXT, " +
            COLUMN_TIME + " INTEGER);";

    // Конструктор
    public DatabaseRecordsHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Создание таблицы
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);  // Выполняем SQL для создания таблицы
    }

    // Обновление базы данных (если версия базы данных меняется)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);  // Удаляем старую таблицу, если она существует
        onCreate(db);  // Создаем новую таблицу
    }

    // Метод для добавления нового рекорда в таблицу
    public void addRecord(String username, String level, int time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);  // Добавляем имя пользователя
        values.put(COLUMN_LEVEL, level);        // Добавляем уровень сложности
        values.put(COLUMN_TIME, time);          // Добавляем время

        db.insert(TABLE_NAME, null, values);  // Вставляем запись в таблицу
        db.close();  // Закрываем базу данных
    }

    // Метод для получения всех рекордов из таблицы
    public Cursor getAllRecords() {
        SQLiteDatabase db = this.getReadableDatabase();

        // Запрос для выборки всех записей, отсортированных по времени
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TIME + " ASC", null);
    }

    // Метод для удаления всех записей из таблицы
    public void deleteAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);  // Удаляем все записи из таблицы
        db.close();  // Закрываем базу данных
    }

    // Метод для удаления базы данных (самого файла базы данных)
    public void deleteDatabase(Context context) {
        context.deleteDatabase(DATABASE_NAME);  // Удаляем файл базы данных
    }
}
