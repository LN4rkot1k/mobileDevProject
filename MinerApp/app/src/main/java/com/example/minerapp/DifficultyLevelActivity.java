package com.example.minerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class DifficultyLevelActivity extends AppCompatActivity {

    Button easyButton, mediumButton, hardButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        // Инициализация кнопок
        easyButton = findViewById(R.id.easyButton);
        mediumButton = findViewById(R.id.mediumButton);
        hardButton = findViewById(R.id.hardButton);
        backButton = findViewById(R.id.backButton);

        // Легкий уровень
        easyButton.setOnClickListener(v -> {

            GameSession.getInstance().setLevel("easy");
            GameSession.getInstance().logCurrentState();

            Intent intent = new Intent(DifficultyLevelActivity.this, GameActivity.class);
            intent.putExtra("level", "easy");
            intent.putExtra("rowsSize", 8); // размер поля для легкого уровня
            intent.putExtra("columnSize", 9);
            intent.putExtra("mineCount", 10); // количество мин для легкого уровня
            startActivity(intent);
        });

        // Средний уровень
        mediumButton.setOnClickListener(v -> {

            GameSession.getInstance().setLevel("medium");
            GameSession.getInstance().logCurrentState();

            Intent intent = new Intent(DifficultyLevelActivity.this, GameActivity.class);
            intent.putExtra("level", "medium");
            intent.putExtra("rowsSize", 8); // размер поля для легкого уровня
            intent.putExtra("columnSize", 12);
            intent.putExtra("mineCount", 30); // количество мин для среднего уровня
            startActivity(intent);
        });

        // Сложный уровень
        hardButton.setOnClickListener(v -> {

            GameSession.getInstance().setLevel("hard");
            GameSession.getInstance().logCurrentState();

            Intent intent = new Intent(DifficultyLevelActivity.this, GameActivity.class);
            intent.putExtra("level", "hard");
            intent.putExtra("rowsSize", 8); // размер поля для легкого уровня
            intent.putExtra("columnSize", 15);
            intent.putExtra("mineCount", 50); // количество мин для сложного уровня
            startActivity(intent);
        });

        // Кнопка "Назад"
        backButton.setOnClickListener(v -> finish());  // Закрыть текущую активность (вернуться в MainMenuActivity)
    }
}
