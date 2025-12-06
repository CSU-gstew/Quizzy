package com.example.project_02;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class QuizCodeActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_code);

        EditText codeInput = findViewById(R.id.AccessCodeEditText);
        Button enterButton = findViewById(R.id.loginButton);

        enterButton.setOnClickListener(v -> {
            String code = codeInput.getText().toString().trim();

            Intent intent = new Intent(QuizCodeActivity.this, QuizInfoActivity.class);
            intent.putExtra("quizCode", code);
            startActivity(intent);
        });
    }

}
