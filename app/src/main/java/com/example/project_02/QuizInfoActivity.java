package com.example.project_02;

import android.os.Bundle;
import android.widget.TextView;

import com.example.project_02.database.entities.QuizzyLog;
import com.google.android.material.appbar.MaterialToolbar;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.project_02.database.QuizzyLogRepository;

import com.example.project_02.viewHolders.QuizInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuizInfoActivity extends BaseActivity {

    private QuizInfoAdapter adapter;
    private TextView quizCountTextView;
    private MaterialToolbar toolbar;
    private QuizzyLogRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        // Toolbar display
        toolbar = findViewById(R.id.action_bar);
        setupToolbar(toolbar);

        quizCountTextView = findViewById(R.id.quizCountTextView);
        RecyclerView recyclerView = findViewById(R.id.logDisplayRecyclerView);

        adapter = new QuizInfoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        repository = QuizzyLogRepository.getRepository(getApplication());

        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int userId = prefs.getInt(getString(R.string.preference_userId_key), -1);

        if (userId == -1) {
            quizCountTextView.setText("Number of Quizzes Taken: 0");
            return;
        }

        // LiveData to update the list of quizzes whenever one is completed
        repository.getAllLogsByUserIdLiveData(userId)
                .observe(this, logs -> {
                    if (logs == null) return;

                    List<QuizInfo> infos = new ArrayList<>();
                    for (QuizzyLog log : logs) {
                        infos.add(new QuizInfo(
                                log.getQuizName(),
                                log.getQuestionsPassed(),
                                log.getQuestionsFailed()
                        ));
                    }

                    adapter.setItems(infos);
                    quizCountTextView.setText("Number of Quizzes Taken: " + infos.size());
                });

    }
}