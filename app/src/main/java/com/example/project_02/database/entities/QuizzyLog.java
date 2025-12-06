package com.example.project_02.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project_02.database.QuizzyLogDatabase;

import androidx.room.Ignore;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = QuizzyLogDatabase.QUIZZY_LOG_TABLE)
public class QuizzyLog {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private LocalDateTime date;
    private int userId;

    // Quiz Info
    private String quizName;
    private int questionsPassed;
    private int questionsFailed;

    public QuizzyLog(){}

    @Ignore
    public QuizzyLog(int userId, String quizName, int questionsPassed, int questionsFailed) {
        this.userId = userId;
        this.quizName = quizName;
        this.questionsPassed = questionsPassed;
        this.questionsFailed = questionsFailed;
        this.date = LocalDateTime.now();
    }

    // I don't even know why or where this is used
    // But apparently it is needed for the database
    @Override
    public String toString() {
        return "QuizzyLog{" +
                "id=" + id +
                ", date=" + date +
                ", userId=" + userId +
                ", quizName='" + quizName + '\'' +
                ", questionsPassed=" + questionsPassed +
                ", questionsFailed=" + questionsFailed +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuizzyLog quizzyLog = (QuizzyLog) o;
        return id == quizzyLog.id && userId == quizzyLog.userId && questionsPassed == quizzyLog.questionsPassed && questionsFailed == quizzyLog.questionsFailed && Objects.equals(date, quizzyLog.date) && Objects.equals(quizName, quizzyLog.quizName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, userId, quizName, questionsPassed, questionsFailed);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getQuestionsPassed() {
        return questionsPassed;
    }

    public void setQuestionsPassed(int questionsPassed) {
        this.questionsPassed = questionsPassed;
    }

    public int getQuestionsFailed() {
        return questionsFailed;
    }

    public void setQuestionsFailed(int questionsFailed) {
        this.questionsFailed = questionsFailed;
    }
}
