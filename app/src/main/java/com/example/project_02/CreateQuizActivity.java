package com.example.project_02;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.Question;
import com.example.project_02.database.entities.Quiz;

public class CreateQuizActivity extends AppCompatActivity {

    private QuizzyLogRepository repository;
    private LinearLayout questionsContainer;
    private EditText quizNameInput;
    private Button addQuestionButton, saveQuizButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        repository = QuizzyLogRepository.getRepository(getApplication());

        quizNameInput = findViewById(R.id.quizNameInput);
        addQuestionButton = findViewById(R.id.addQuestionButton);
        saveQuizButton = findViewById(R.id.saveQuizButton);
        questionsContainer = findViewById(R.id.questionsContainer);

        addQuestionButton.setOnClickListener(v -> addQuestionView());
        saveQuizButton.setOnClickListener(v -> saveQuiz());
    }

    private void addQuestionView() {
        View questionView = getLayoutInflater()
                .inflate(R.layout.question_item, null);
        questionsContainer.addView(questionView);
    }

    private void saveQuiz() {
        String quizName = quizNameInput.getText().toString();

        if (quizName.isEmpty()) {
            Toast.makeText(this, "Please enter a quiz name", Toast.LENGTH_SHORT).show();
            return;
        }

        String accessCode = String.valueOf((int)(Math.random() * 90000) + 10000);

        Quiz quiz = new Quiz(quizName, accessCode);

        repository.insertQuiz(quiz, quizId -> {
            runOnUiThread(() -> saveQuestions(Math.toIntExact(quizId)));
        });
    }

    private void saveQuestions(int quizId) {
        for (int i = 0; i < questionsContainer.getChildCount(); i++) {
            View view = questionsContainer.getChildAt(i);

            String q = ((EditText)view.findViewById(R.id.questionText)).getText().toString();
            String a = ((EditText)view.findViewById(R.id.optionA)).getText().toString();
            String b = ((EditText)view.findViewById(R.id.optionB)).getText().toString();
            String c = ((EditText)view.findViewById(R.id.optionC)).getText().toString();
            String d = ((EditText)view.findViewById(R.id.optionD)).getText().toString();
            String correct = ((EditText)view.findViewById(R.id.correctOption)).getText().toString();

            Question question = new Question(quizId, q, a, b, c, d, correct, "MCQ");
            repository.insertQuestion(question);
        }

        Toast.makeText(this, "Quiz saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}