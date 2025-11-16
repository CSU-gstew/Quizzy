package com.example.project_02.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project_02.database.entities.QuizzyLog;

import java.util.List;

@Dao
public interface QuizzyLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(QuizzyLog log);

    @Query("SELECT * FROM " + QuizzyLogDatabase.QUIZZY_LOG_TABLE + " ORDER BY date DESC")
    List<QuizzyLog> getAllRecords();

    @Query("SELECT * FROM " + QuizzyLogDatabase.QUIZZY_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    List<QuizzyLog> getRecordsByUserId(int loggedInUserId);

    @Query("SELECT * FROM " + QuizzyLogDatabase.QUIZZY_LOG_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")

    LiveData<List<QuizzyLog>> getRecordsByUserIdLiveData(int loggedInUserId);
}
