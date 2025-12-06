package com.example.project_02;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.project_02.database.QuizzyLogDatabase;
import com.example.project_02.database.entities.Question;
import com.example.project_02.database.entities.Quiz;

import java.util.List;
public class QuizActivity extends AppCompatActivity {
    private Button buttonA, buttonB, buttonC, buttonD;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        buttonA = findViewById(R.id.QuestionA);
        buttonB = findViewById(R.id.QuestionB);
        buttonC = findViewById(R.id.QuestionC);
        buttonD = findViewById(R.id.QuestionD);
        setupButtonListeners();


        String quizCode = getIntent().getStringExtra("QUIZ_CODE");
        if (quizCode == null){
            Toast.makeText(this, "No quiz code was given", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        QuizzyLogDatabase.getDatabase(getApplicationContext())
                .quizDAO()
                .getQuizByAccessCode(quizCode)
                .observe(this, new Observer<Quiz>() {
                    @Override
                    public void onChanged(Quiz quiz) {
                        if(quiz == null){
                            Toast.makeText(QuizActivity.this, "Quiz not found", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                        QuizzyLogDatabase.getDatabase(getApplicationContext())
                                .questionDAO()
                                .getQuestionsForQuiz(quiz.getId())
                                .observe(QuizActivity.this, new Observer<List<Question>>() {
                                    @Override
                                    public void onChanged(List<Question> questions) {
                                        questionList = questions;
                                        showCurrentQuestion();
                                    }
                                });
                    }
                });
    }

    private void showCurrentQuestion(){
        if (questionList == null || questionList.isEmpty() || currentQuestionIndex >= questionList.size()){
            Toast.makeText(this, "Quiz Completed", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Question q = questionList.get(currentQuestionIndex);
        buttonA.setText("A: " + q.getOptionA());
        buttonB.setText("B: " + q.getOptionB());
        buttonC.setText("C: " + q.getOptionC());
        buttonD.setText("D: " + q.getOptionD());
    }
    private void setupButtonListeners(){
        buttonA.setOnClickListener(v -> checkAnswer("A"));
        buttonB.setOnClickListener(v -> checkAnswer("B"));
        buttonC.setOnClickListener(v -> checkAnswer("C"));
        buttonD.setOnClickListener(v -> checkAnswer("D"));
    }
    private void checkAnswer(String selectedOption){
        if (questionList == null || currentQuestionIndex >= questionList.size()) return;
        currentQuestionIndex++;
        showCurrentQuestion();
    }


}
