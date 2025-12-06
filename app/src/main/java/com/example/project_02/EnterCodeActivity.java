package com.example.project_02;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EnterCodeActivity extends AppCompatActivity{
    private EditText accessCodeEditText;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_code);

        accessCodeEditText = findViewById(R.id.AccessCodeEditText);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> {
            String code = accessCodeEditText.getText().toString().trim();

            if (code.isEmpty()){
                Toast.makeText(this, "Please enter a quiz code", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, QuizActivity.class);
            intent.putExtra("QUIZ_CODE", code);
            startActivity(intent);
        });
    }
}
