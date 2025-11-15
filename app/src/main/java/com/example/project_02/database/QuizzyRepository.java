package com.example.project_02.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project_02.database.entities.Quizzy;
import com.example.project_02.MainActivity;
import com.example.project_02.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class QuizzyRepository {
    private final QuizzyDAO quizzyDAO;
    private final UserDAO userDAO;

    private ArrayList<Quizzy> allLogs;

    private static QuizzyRepository repository;

    private QuizzyRepository(Application application){
        QuizzyDatabase db = QuizzyDatabase.getDatabase(application);
        this.quizzyDAO = db.quizzyDao();
        this.userDAO = db.userDAO();
        this.allLogs = (ArrayList<Quizzy>) this.quizzyDAO.getAllRecords();
    }
}
