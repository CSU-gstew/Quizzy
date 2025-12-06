package com.example.project_02;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.project_02.database.QuizzyLogDatabase;
import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.Question;
import com.example.project_02.database.entities.Quiz;
import com.example.project_02.database.entities.QuizzyLog;

import java.util.List;
public class QuizActivity extends AppCompatActivity {
    private Button buttonA, buttonB, buttonC, buttonD;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;
    private TextView questionTextView;

    // For saving quiz info
    private Quiz currentQuiz;
    private QuizzyLogRepository repository;

    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        // Initialize the repository
        repository = QuizzyLogRepository.getRepository(getApplication());

        questionTextView = findViewById(R.id.questionTextView);

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

                        // Store the current quiz
                        currentQuiz = quiz;

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
            // Save the quiz results
            saveQuizResults();
            Toast.makeText(this, "Quiz Completed!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        Question q = questionList.get(currentQuestionIndex);
        questionTextView.setText(q.getQuestionText());
        buttonA.setText("A: " + q.getOptionA());
        buttonB.setText("B: " + q.getOptionB());
        buttonC.setText("C: " + q.getOptionC());
        buttonD.setText("D: " + q.getOptionD());
    }

    private void saveQuizResults() {
        if (currentQuiz == null || questionList == null) return;

        int totalQuestions = questionList.size();
        int questionsPassed = score;
        int questionsFailed = totalQuestions - score;

        // get logged-in user id from the same prefs used elsewhere
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        int userId = prefs.getInt(getString(R.string.preference_userId_key), -1);
        if (userId == -1) return;   // skip logging if no logged in user

        // Create a QuizzyLog. Adjust constructor params to match your entity
        QuizzyLog log = new QuizzyLog(userId, currentQuiz.getQuizName(),
                questionsPassed, questionsFailed
        );

        repository.insertQuizzyLog(log);
    }

    private void setupButtonListeners(){
        buttonA.setOnClickListener(v -> checkAnswer("A"));
        buttonB.setOnClickListener(v -> checkAnswer("B"));
        buttonC.setOnClickListener(v -> checkAnswer("C"));
        buttonD.setOnClickListener(v -> checkAnswer("D"));
    }
    private void checkAnswer(String selectedOption){
        if (questionList == null || currentQuestionIndex >= questionList.size()) return;
        Question currentQuestion = questionList.get(currentQuestionIndex);
        if (currentQuestion.getCorrectOption().equals(selectedOption)){
            score++;
            Toast.makeText(this, "CORRECT!", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Wrong!", Toast.LENGTH_SHORT).show();
        }

        currentQuestionIndex++;
        showCurrentQuestion();
    }


}
