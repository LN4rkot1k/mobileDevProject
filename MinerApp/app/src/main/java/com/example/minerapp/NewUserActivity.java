package com.example.minerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class NewUserActivity extends AppCompatActivity {

    private EditText newUsernameEditText, newPasswordEditText;
    private TextView errorTextView;
    private DatabaseHelper dbHelper;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        newUsernameEditText = findViewById(R.id.newUsernameEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        errorTextView = findViewById(R.id.errorTextView);
        Button confirmButton = findViewById(R.id.confirmButton);
        backButton = findViewById(R.id.backButton);

        dbHelper = new DatabaseHelper(this);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = newUsernameEditText.getText().toString().trim();
                String password = newPasswordEditText.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    errorTextView.setText("Все поля должны быть заполнены");
                    errorTextView.setVisibility(View.VISIBLE);
                } else if (!isValidInput(username) || !isValidInput(password)) {
                    errorTextView.setText("Логин и пароль должны содержать только английские буквы и цифры");
                    errorTextView.setVisibility(View.VISIBLE);
                } else {
                    // Хэшируем пароль перед записью в БД
                    String hashedPassword = HashingPassword.hashPassword(password);

                    if (dbHelper.addUser(username, hashedPassword)) {
                        // Сохраняем username в Singleton
                        GameSession.getInstance().setUsername(username);

                        GameSession.getInstance().logCurrentState();


                        Toast.makeText(NewUserActivity.this, "Регистрация успешна, " + username + "!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewUserActivity.this, MainMenuActivity.class));
                        finish();
                    } else {
                        errorTextView.setText("Пользователь уже существует");
                        errorTextView.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        // Обработчик кнопки "Назад"
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewUserActivity.this, RegistrationActivity.class));
                finish();
            }
        });
    }

    private boolean isValidInput(String input) {
        return input.matches("^[a-zA-Z0-9]+$");
    }
}
