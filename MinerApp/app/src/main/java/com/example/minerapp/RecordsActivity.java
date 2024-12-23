package com.example.minerapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RecordsActivity extends AppCompatActivity {

    //private ListView recordsListView;
    private Button backButton;
    //private Button deleteButton;
    //private DatabaseRecordsHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        // Инициализируем элементы интерфейса
        //recordsListView = findViewById(R.id.recordsListView);
        backButton = findViewById(R.id.backButton);
        //deleteButton = findViewById(R.id.deleteButton);

        // Инициализируем объект DatabaseHelper
        //databaseHelper = new DatabaseRecordsHelper(this);

        // Устанавливаем адаптер для ListView
        //loadRecords();

        // Обработчик кнопки "Назад"
        backButton.setOnClickListener(v -> {
            finish();  // Завершаем активность и возвращаемся на предыдущий экран
        });

        // Обработчик кнопки "Удалить все записи"
        /*deleteButton.setOnClickListener(v -> {
            databaseHelper.deleteAllRecords();  // Удаляем все записи из базы данных
            loadRecords();  // Перезагружаем данные в ListView
            Toast.makeText(RecordsActivity.this, "Все записи удалены", Toast.LENGTH_SHORT).show();
        });*/
    }

    // Метод для загрузки всех записей и отображения их в ListView
    /*private void loadRecords() {
        // Получаем все записи из базы данных
        Cursor cursor = databaseHelper.getAllRecords();

        // Определяем колонки, которые нам нужны для отображения
        String[] from = {
                DatabaseRecordsHelper.COLUMN_USERNAME,
                DatabaseRecordsHelper.COLUMN_LEVEL,
                DatabaseRecordsHelper.COLUMN_TIME
        };

        // Определяем соответствие между колонками и TextView
        int[] to = {R.id.username, R.id.level, R.id.time};

        // Создаем адаптер
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(
                this,
                R.layout.record_item, // Используем вашу разметку
                cursor,
                from,
                to,
                0
        );

        // Устанавливаем адаптер для ListView
        recordsListView.setAdapter(adapter);
    }*/


    // Адаптер для отображения данных в ListView
    /*private class RecordsAdapter extends android.widget.BaseAdapter {

        private final ArrayList<String> records;

        public RecordsAdapter(RecordsActivity context, ArrayList<String> records) {
            this.records = records;
        }

        @Override
        public int getCount() {
            return records.size();
        }

        @Override
        public Object getItem(int position) {
            return records.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, android.view.ViewGroup parent) {
            // Используем TextView для отображения записи
            TextView recordTextView;

            if (convertView == null) {
                // Если view еще не существует, создаем новый TextView
                recordTextView = new TextView(RecordsActivity.this);
                recordTextView.setPadding(20, 20, 20, 20);
                recordTextView.setTextSize(16);
            } else {
                // Если view существует, используем его
                recordTextView = (TextView) convertView;
            }

            // Устанавливаем текст для каждой записи
            recordTextView.setText(records.get(position));

            return recordTextView;
        }
    }*/
}
