package com.example.minerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {
    private EditText usernameEditText, passwordEditText;
    private Button registerButton, enterButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        enterButton = findViewById(R.id.enterButton);

        dbHelper = new DatabaseHelper(this);

        // Обработчик клика по кнопке "Войти"
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                // Хэшируем введенный пароль перед проверкой
                String hashedPassword = HashingPassword.hashPassword(password);

                if (dbHelper.checkUser(username, hashedPassword)) {
                    // Сохраняем username в Singleton
                    GameSession.getInstance().setUsername(username);

                    GameSession.getInstance().logCurrentState();


                    Toast.makeText(RegistrationActivity.this, "Добро пожаловать, " + username + "!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegistrationActivity.this, MainMenuActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Неверный логин или пароль!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Обработчик клика по кнопке "Регистрация"
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, NewUserActivity.class));
                finish();
            }
        });
    }
}
