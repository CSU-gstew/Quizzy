package com.example.project_02;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.appbar.MaterialToolbar;


public class AdminActivity extends BaseActivity {

    private MaterialToolbar toolbar;
    private Button btnCreateQuiz;
    private Button btnEditQuiz;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_page);
        btnCreateQuiz = findViewById(R.id.CreateQuiz);
        btnEditQuiz = findViewById(R.id.EditQuiz);

        toolbar = findViewById(R.id.action_bar);
        setupToolbar(toolbar);

        btnCreateQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, CreateQuizActivity.class);
                startActivity(intent);
            }
        });

        btnEditQuiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent(AdminActivity.this, EditQuizActivity.class);
            startActivity(intent);
            }
        });
    }
}