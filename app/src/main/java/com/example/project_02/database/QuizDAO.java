package com.example.project_02.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_02.database.entities.Quiz;

import java.util.List;

@Dao
public interface QuizDAO {

    @Insert
    long insertQuiz(Quiz quiz);

    @Query("SELECT * FROM quiz_table WHERE accessCode = :accessCode")
    LiveData<Quiz> getQuizByAccessCode(String accessCode);

    @Query("SELECT * FROM quiz_table")
    LiveData<List<Quiz>> getAllQuizzes();
}