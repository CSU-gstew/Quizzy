package com.example.project_02;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.Question;
import com.example.project_02.database.entities.Quiz;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class EditQuizActivity extends BaseActivity {

    private MaterialToolbar toolbar;

    private EditText quizCodeInput;
    private Button loadQuizButton;

    private EditText quizTitleInput;
    private LinearLayout questionsContainer;
    private Button addQuestionButton;
    private Button saveChangesButton;

    private QuizzyLogRepository repository;

    private Quiz loadedQuiz;
    private List<Question> questionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_quiz);

        toolbar = findViewById(R.id.action_bar);
        setupToolbar(toolbar);

        repository = QuizzyLogRepository.getRepository(getApplication());

        quizCodeInput = findViewById(R.id.quizCodeInput);
        loadQuizButton = findViewById(R.id.loadQuizButton);

        quizTitleInput = findViewById(R.id.editTextQuizTitle);
        questionsContainer = findViewById(R.id.questionsContainer);
        addQuestionButton = findViewById(R.id.buttonAddQuestion);
        saveChangesButton = findViewById(R.id.buttonSaveChanges);

        quizTitleInput.setVisibility(View.GONE);
        questionsContainer.setVisibility(View.GONE);
        saveChangesButton.setVisibility(View.GONE);
        addQuestionButton.setVisibility(View.GONE);

        loadQuizButton.setOnClickListener(v -> loadQuiz());
        addQuestionButton.setOnClickListener(v -> addQuestionField(null));
        saveChangesButton.setOnClickListener(v -> saveChanges());
    }

    private void loadQuiz() {
        String code = quizCodeInput.getText().toString().trim();

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(this, "Enter access code", Toast.LENGTH_SHORT).show();
            return;
        }

        repository.getQuizByAccessCode(code).observe(this, quiz -> {

            if (quiz == null) {
                Toast.makeText(this, "Invalid quiz code!", Toast.LENGTH_SHORT).show();
                return;
            }

            loadedQuiz = quiz;

            quizTitleInput.setVisibility(View.VISIBLE);
            questionsContainer.setVisibility(View.VISIBLE);
            saveChangesButton.setVisibility(View.VISIBLE);
            addQuestionButton.setVisibility(View.VISIBLE);

            quizTitleInput.setText(quiz.getQuizName());

            loadQuestions(quiz.getId());
        });
    }

    private void loadQuestions(int quizId) {
        repository.getQuestionsForQuiz(quizId).observe(this, list -> {
            questionList.clear();
            if (list != null) questionList.addAll(list);
            refreshQuestionsUI();
        });
    }
    private void refreshQuestionsUI() {
        questionsContainer.removeAllViews();

        for (Question q : questionList) {
            addQuestionField(q);
        }
    }

    private void addQuestionField(Question existing) {
        View view = getLayoutInflater().inflate(R.layout.question_item, questionsContainer, false);

        EditText questionTextInput = view.findViewById(R.id.questionText);
        EditText optionAInput = view.findViewById(R.id.optionA);
        EditText optionBInput = view.findViewById(R.id.optionB);
        EditText optionCInput = view.findViewById(R.id.optionC);
        EditText optionDInput = view.findViewById(R.id.optionD);
        EditText correctOptionInput = view.findViewById(R.id.correctOption);

        Button deleteBtn = view.findViewById(R.id.buttonDeleteQuestion);

        deleteBtn.setOnClickListener(v -> {
            questionsContainer.removeView(view);
            questionList.remove(existing);
        });

        if (existing != null) {
            questionTextInput.setText(existing.getQuestionText());
            optionAInput.setText(existing.getOptionA());
            optionBInput.setText(existing.getOptionB());
            optionCInput.setText(existing.getOptionC());
            optionDInput.setText(existing.getOptionD());
            correctOptionInput.setText(existing.getCorrectOption());
        }
        questionsContainer.addView(view);
    }

    private void saveChanges() {
        if (loadedQuiz == null) return;

        String newTitle = quizTitleInput.getText().toString().trim();
        if (newTitle.isEmpty()) {
            Toast.makeText(this, "Quiz name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        loadedQuiz.setQuizName(newTitle);
        repository.updateQuiz(loadedQuiz);

        List<Question> newQuestions = new ArrayList<>();

        for (int i = 0; i < questionsContainer.getChildCount(); i++) {
            View v = questionsContainer.getChildAt(i);

            EditText qText = v.findViewById(R.id.questionText);
            EditText a = v.findViewById(R.id.optionA);
            EditText b = v.findViewById(R.id.optionB);
            EditText c = v.findViewById(R.id.optionC);
            EditText d = v.findViewById(R.id.optionD);
            EditText correct = v.findViewById(R.id.correctOption);

            String q1 = qText.getText().toString().trim();
            if (q1.isEmpty()) continue;

            Question q = new Question(
                    loadedQuiz.getId(),
                    q1,
                    a.getText().toString(),
                    b.getText().toString(),
                    c.getText().toString(),
                    d.getText().toString(),
                    correct.getText().toString(),
                    "MCQ" // or "True/False" later (might implement)
            );

            newQuestions.add(q);
        }

        repository.replaceAllQuestionsForQuiz(loadedQuiz.getId(), newQuestions);

        Toast.makeText(this, "Quiz updated!", Toast.LENGTH_SHORT).show();
        finish();
    }
}