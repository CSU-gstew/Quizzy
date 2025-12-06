package com.example.project_02.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.project_02.database.entities.Question;

import java.util.List;

@Dao
public interface QuestionDAO {

    @Insert
    void insert(Question... question);

    @Query("SELECT * FROM question_table WHERE quizId = :quizId")
    LiveData<List<Question>> getQuestionsForQuiz(int quizId);
    @Query("DELETE FROM question_table WHERE quizId = :quizId")
    void deleteQuestionsForQuiz(int quizId);

}