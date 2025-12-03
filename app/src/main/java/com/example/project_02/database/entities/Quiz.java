package com.example.project_02.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "quiz_table")
public class Quiz {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String quizName;
    private final String accessCode;

    public Quiz(String quizName, String accessCode) {
        this.quizName = quizName;
        this.accessCode = accessCode;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccessCode() {
        return accessCode;
    }
}
