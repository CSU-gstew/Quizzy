package com.example.project_02;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02.viewHolders.QuizInfoAdapter;

import java.util.ArrayList;
import java.util.List;

public class QuizInfoActivity extends AppCompatActivity {

    private QuizInfoAdapter adapter;
    private TextView quizCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        quizCountTextView = findViewById(R.id.quizCountTextView);
        RecyclerView recyclerView = findViewById(R.id.logDisplayRecyclerView);

        adapter = new QuizInfoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // This just placeholder data
        // TODO: Replace with real code that puts info from quiz into recycler
        List<QuizInfo> demoData = createDemoData();

        adapter.setItems(demoData);
        quizCountTextView.setText("Number of Quizzes Taken: " + demoData.size());
    }

    // This just placeholder data
    // TODO: Replace with real data coming from the quizzes
    private List<QuizInfo> createDemoData() {
        List<QuizInfo> list = new ArrayList<>();
        list.add(new QuizInfo("Quiz 1", 4, 1));
        list.add(new QuizInfo("Quiz 2", 5, 0));
        list.add(new QuizInfo("Quiz 3", 6, 4));
        return list;
    }

    /**
     * This is preferably the ideal code to use
     * Once we get quizz functionality up and running
     * Leaving it here for later use
     * List<QuizInfo> infos = new ArrayList<>();
     * for (QuizzyLog log : logsForUser) {
     *     infos.add(new QuizInfo(
     *         log.getQuizName(),
     *         log.getQuestionsPassed(),
     *         log.getQuestionsFailed()
     *     ));
     * }
     * adapter.setItems(infos);
     * quizCountTextView.setText("Number of Quizzes Taken: " + infos.size());
     *
     * Gonna need this line too to overwrite the text view
     * Should update with the size of how many quizzes a user will have taken
     * quizCountTextView.setText("Number of Quizzes Taken: " + quizList.size());
     *
     */
}