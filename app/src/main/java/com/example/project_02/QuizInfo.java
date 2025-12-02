package com.example.project_02;
public class QuizInfo {
    private final String quizName;
    private final int questionsPassed;
    private final int questionsFailed;

    // Necessary for Recycler View
    public QuizInfo(String quizName, int questionsPassed, int questionsFailed) {
        this.quizName = quizName;
        this.questionsPassed = questionsPassed;
        this.questionsFailed = questionsFailed;
    }

    public String getQuizName() {
        return quizName;
    }

    public int getQuestionsPassed() {
        return questionsPassed;
    }

    public int getQuestionsFailed() {
        return questionsFailed;
    }
}
