package com.example.project_02.database;

import android.app.Application;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project_02.MainActivity;
import com.example.project_02.database.entities.Quizzy;
import com.example.project_02.database.entities.User;
import com.example.project_02.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizzyDatabase {
    public static QuizzyDatabase getDatabase(Application application) {
    }
}
