package com.example.project_02;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, LogInActivity.class);
            startActivity(intent);
        });
    }
}