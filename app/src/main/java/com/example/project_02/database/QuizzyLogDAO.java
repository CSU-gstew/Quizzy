package com.example.project_02.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.project_02.database.entities.Quizzy;

import java.util.List;

@Dao
public interface QuizzyLogDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quizzy log);

    @Query("SELECT * FROM " + QuizzyLogDatabase.QUIZZY_TABLE + " ORDER BY date DESC")
    List<Quizzy> getAllRecords();

    @Query("SELECT * FROM " + QuizzyLogDatabase.QUIZZY_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")
    List<Quizzy> getRecordsByUserId(int loggedInUserId);

    @Query("SELECT * FROM " + QuizzyLogDatabase.QUIZZY_TABLE + " WHERE userId = :loggedInUserId ORDER BY date DESC")

    LiveData<List<Quizzy>> getRecordsByUserIdLiveData(int loggedInUserId);
}
