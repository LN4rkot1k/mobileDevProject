package com.example.minerapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;


import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private GridLayout minesweeperGrid;
    private TextView timerTextView;
    private TextView scoreTextView; // Новый элемент для отображения очков
    private Button backButton;

    private int rowsSize; // Количество строк
    private int columnSize; // Количество столбцов
    private int mineCount; // Количество мин
    private int secondsElapsed = 0;

    private boolean[][] isMine; // Хранит информацию о том, есть ли мина в клетке
    private int[][] neighborMineCount; // Хранит количество мин вокруг клетки
    private boolean[][] isRevealed;
    private boolean[][] isFlagged; // Хранит информацию о том, есть ли флажок на клетке
    // Хранит информацию о том, открыта ли клетка
    private Button[][] buttons; // Ссылки на кнопки на игровом поле

    private int revealedCount = 0; // Счетчик открытых клеток
    private boolean gameOver = false;
    private int score = 0; // Переменная для хранения очков
    private CountDownTimer gameTimer; // Таймер игры

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        minesweeperGrid = findViewById(R.id.gameGrid);
        timerTextView = findViewById(R.id.timerTextView);
        scoreTextView = findViewById(R.id.scoreTextView); // Связываем с элементом XML
        backButton = findViewById(R.id.returnButton);

        String username = GameSession.getInstance().getUsername();
        String level = GameSession.getInstance().getLevel();


        // Получаем параметры из Intent
        //Intent intent = getIntent();
        //String level = intent.getStringExtra("level"); // Уровень сложности

        // Устанавливаем параметры в зависимости от уровня сложности
        if ("easy".equals(level)) {
            rowsSize = 9;
            columnSize = 8;
            mineCount = 2; // Легкий уровень 10
        } else if ("medium".equals(level)) {
            rowsSize = 11;
            columnSize = 8;
            mineCount = 30; // Средний уровень 30
        } else if ("hard".equals(level)) {
            rowsSize = 13;
            columnSize = 8;
            mineCount = 50; // Сложный уровень 50
        } else {
            rowsSize = 9; // Значения по умолчанию
            columnSize = 8;
            mineCount = 10;
        }

        // Инициализация игрового поля
        initGame();

        // Обработчик кнопки "Вернуться"
        backButton.setOnClickListener(v -> showExitConfirmationDialog());

        // Таймер
        startTimer();
    }

    private void initGame() {
        isMine = new boolean[rowsSize][columnSize];
        neighborMineCount = new int[rowsSize][columnSize];
        isRevealed = new boolean[rowsSize][columnSize];
        isFlagged = new boolean[rowsSize][columnSize];
        buttons = new Button[rowsSize][columnSize];

        minesweeperGrid.setRowCount(rowsSize);
        minesweeperGrid.setColumnCount(columnSize);

        // Устанавливаем мины
        placeMines();

        // Подсчитываем количество мин вокруг каждой клетки
        calculateNeighborMines();

        // Создаем кнопки
        createButtons();
    }

    private void placeMines() {
        Random random = new Random();
        int placedMines = 0;

        while (placedMines < mineCount) {
            int row = random.nextInt(rowsSize);
            int col = random.nextInt(columnSize);

            if (!isMine[row][col]) {
                isMine[row][col] = true;
                placedMines++;
            }
        }
    }

    private void calculateNeighborMines() {
        for (int row = 0; row < rowsSize; row++) {
            for (int col = 0; col < columnSize; col++) {
                if (isMine[row][col]) continue;

                int mineCount = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int newRow = row + i;
                        int newCol = col + j;

                        if (newRow >= 0 && newRow < rowsSize && newCol >= 0 && newCol < columnSize && isMine[newRow][newCol]) {
                            mineCount++;
                        }
                    }
                }
                neighborMineCount[row][col] = mineCount;
            }
        }
    }

    private void createButtons() {
        for (int row = 0; row < rowsSize; row++) {
            for (int col = 0; col < columnSize; col++) {
                Button cellButton = new Button(this);
                buttons[row][col] = cellButton;

                final int currentRow = row;
                final int currentCol = col;

                cellButton.setOnClickListener(v -> {
                    if (!gameOver && !isFlagged[currentRow][currentCol]) {
                        revealCell(currentRow, currentCol);
                    }
                });

                // Обработчик долгого нажатия (установка/снятие флажка)
                cellButton.setOnLongClickListener(v -> {
                    if (!gameOver) {
                        toggleFlag(currentRow, currentCol);
                    }
                    return true; // Возвращаем true, чтобы долгое нажатие не вызывало дополнительного действия
                });

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col);
                params.width = 120;
                params.height = 120;
                params.setMargins(2, 2, 2, 2);

                cellButton.setLayoutParams(params);
                minesweeperGrid.addView(cellButton);
            }
        }
    }

    private void toggleFlag(int row, int col) {
        Button cellButton = buttons[row][col];

        if (isFlagged[row][col]) {
            // Если флажок уже установлен, снимаем его
            isFlagged[row][col] = false;
            cellButton.setText(""); // Убираем текст флажка
        } else {
            // Если флажка нет, ставим его
            isFlagged[row][col] = true;
            cellButton.setText("🚩"); // Устанавливаем флажок (можно заменить на любой символ)
        }
    }



    private void revealCell(int row, int col) {
        if (gameOver || isRevealed[row][col] || isFlagged[row][col]) return; // Проверка на завершение игры

        isRevealed[row][col] = true; // Отметим клетку как открытую

        Button cellButton = buttons[row][col];

        // Устанавливаем фон клетки как зеленый при ее открытии
        cellButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

        if (isMine[row][col]) {
            cellButton.setText("💣");
            cellButton.setBackgroundColor(getResources().getColor(android.R.color.holo_red_light)); // Красный для мины
            gameOver(); // Завершаем игру (вызываем только один метод завершения)
            return; // Прерываем выполнение, чтобы не вызвать gameWon()
        }

        int mines = neighborMineCount[row][col];
        if (mines > 0) {
            cellButton.setText(String.valueOf(mines)); // Показываем количество мин вокруг
        } else {
            cellButton.setText(""); // Если мин нет, оставляем пустую клетку
            // Открытие соседних клеток
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int newRow = row + i;
                    int newCol = col + j;

                    if (newRow >= 0 && newRow < rowsSize && newCol >= 0 && newCol < columnSize) {
                        revealCell(newRow, newCol); // Рекурсивное открытие соседних клеток
                    }
                }
            }
        }

        // Увеличиваем очки за каждую открытую клетку
        score += 3;
        updateScoreDisplay(); // Обновляем отображение очков
        revealedCount++;

        // Проверка на выигрыш (только если игра еще не закончена)
        if (!gameOver && revealedCount == (rowsSize * columnSize - mineCount)) {
            gameWon(); // Игра выиграна
        }
    }




    private void updateScoreDisplay() {
        scoreTextView.setText("Очки: " + score);
    }

    private void gameOver() {
        stopTimer(); // Останавливаем таймер
        gameOver = true;
        new AlertDialog.Builder(this)
                .setTitle("Игра окончена!")
                .setMessage("Вы проиграли.")
                .setPositiveButton("OK", (dialog, which) -> finish())
                .show();
    }

    private void gameWon() {
        stopTimer(); // Останавливаем таймер
        gameOver = true;

        // Получаем данные о пользователе и уровне
        String username = GameSession.getInstance().getUsername();
        String level = GameSession.getInstance().getLevel();
        int timeInSeconds = secondsElapsed;                  // Время, затраченное на игру (в секундах)

        // Преобразуем время в формат минуты:секунды
        int minutes = timeInSeconds / 60;
        int seconds = timeInSeconds % 60;

        // Преобразуем в строковый формат времени
        String timeFormatted = String.format("%02d:%02d", minutes, seconds);

        // Сохраняем время в GameSession
        GameSession.getInstance().setTime(timeFormatted);  // Сохраняем отформатированное время (минуты:секунды)
        GameSession.getInstance().logCurrentState();        // Логируем текущее состояние GameSession


        Log.d("GameActivity", "Saving record");
        Log.d("GameActivity", "Username: " + username);
        Log.d("GameActivity", "Level: " + level);
        Log.d("GameActivity", "Time: " + timeFormatted);


        // Сохраняем рекорд в базу данных
        DatabaseRecordsHelper dbHelper = new DatabaseRecordsHelper(this);
        boolean isRecordSaved = dbHelper.addRecord(username, level, timeFormatted);  // Добавляем запись в базу данных

        // Проверка, был ли успешно сохранен рекорд
        if (isRecordSaved) {
            Toast.makeText(this, "Рекорд сохранен!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка при сохранении рекорда", Toast.LENGTH_SHORT).show();
        }

        // Показываем сообщение об окончании игры и победе
        new AlertDialog.Builder(this)
                .setTitle("Поздравляем!")
                .setMessage("Вы выиграли!")
                .setPositiveButton("OK", (dialog, which) -> finish())  // Закрываем игру
                .show();
    }





    private void startTimer() {
        gameTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = secondsElapsed / 60;
                int seconds = secondsElapsed % 60;
                timerTextView.setText(String.format("%02d:%02d", minutes, seconds));
                secondsElapsed++;
            }

            @Override
            public void onFinish() {
                // Таймер не закончится, так как его время бесконечно
            }
        };
        gameTimer.start(); // Запускаем таймер
    }

    private void stopTimer() {
        if (gameTimer != null) {
            gameTimer.cancel(); // Останавливаем таймер
        }
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", (dialog, which) -> finish())
                .setNegativeButton("Нет", null)
                .show();
    }
}
