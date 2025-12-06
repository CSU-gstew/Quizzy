package com.example.project_02.viewHolders;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.QuizzyLog;

import java.util.List;

public class QuizzyLogViewModel extends AndroidViewModel {
    private final QuizzyLogRepository repository;

    public QuizzyLogViewModel (Application application){
        super(application);
        repository = QuizzyLogRepository.getRepository(application);
    }

    public LiveData<List<QuizzyLog>> getAllLogsById(int userId) {
        return repository.getAllLogsByUserIdLiveData(userId);
    }

    public void insert(QuizzyLog log){
        repository.insertQuizzyLog(log);
    }
}
