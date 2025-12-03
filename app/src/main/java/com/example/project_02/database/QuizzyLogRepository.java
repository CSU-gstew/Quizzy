package com.example.project_02.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.project_02.MainActivity;
import com.example.project_02.database.entities.Question;
import com.example.project_02.database.entities.Quiz;
import com.example.project_02.database.entities.QuizzyLog;
import com.example.project_02.database.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class QuizzyLogRepository {
    private final QuizzyLogDAO quizzyLogDAO;
    private final UserDAO userDAO;

    private QuizDAO quizDAO;
    private QuestionDAO questionDAO;

    private ArrayList<QuizzyLog> allLogs;

    private static QuizzyLogRepository repository;


    private QuizzyLogRepository(Application application) {
        QuizzyLogDatabase db = QuizzyLogDatabase.getDatabase(application);
        this.quizzyLogDAO = db.quizzyLogDAO();
        this.userDAO = db.userDAO();
        this.allLogs = (ArrayList<QuizzyLog>) this.quizzyLogDAO.getAllRecords();
    }

    public static QuizzyLogRepository getRepository(Application application) {
        if (repository != null) {
            return repository;
        }
        Future<QuizzyLogRepository> future = QuizzyLogDatabase.databasedWriteExecutor.submit(
                new Callable<QuizzyLogRepository>() {
                    @Override
                    public QuizzyLogRepository call() throws Exception {
                        return new QuizzyLogRepository(application);
                    }
                }
        );
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.d(MainActivity.TAG, "Problem getting QuizzyLogRepository, thread error.");
        }
        return null;
    }


    public ArrayList<QuizzyLog> getAllLogs() {
        Future<ArrayList<QuizzyLog>> future = QuizzyLogDatabase.databasedWriteExecutor.submit(
                new Callable<ArrayList<QuizzyLog>>() {
                    @Override
                    public ArrayList<QuizzyLog> call() throws Exception {
                        return (ArrayList<QuizzyLog>) quizzyLogDAO.getAllRecords();
                    }
                }
        );

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {

            Log.i(MainActivity.TAG, "Problem when getting all QuizzyLogs in the repository");
        }
        return null;
    }

    public void insertQuizzyLog(QuizzyLog quizzyLog) {
        QuizzyLogDatabase.databasedWriteExecutor.execute(() -> {
            quizzyLogDAO.insert(quizzyLog);
        });
    }

    public void insertUser(User... user) {
        QuizzyLogDatabase.databasedWriteExecutor.execute(() -> {
            userDAO.insert(user);
        });
    }

    public LiveData<User> getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public LiveData<User> getUserByUserId(int userId) {
        return userDAO.getUserByUserId(userId);
    }


    public LiveData<List<QuizzyLog>> getAllLogsByUserIdLiveData(int loggedInUserId){
        return quizzyLogDAO.getRecordsByUserIdLiveData(loggedInUserId);
    }

    @Deprecated
    public ArrayList<QuizzyLog> getAllLogsByUserId(int loggedInUserId) {
        Future<ArrayList<QuizzyLog>> future = QuizzyLogDatabase.databasedWriteExecutor.submit(
                new Callable<ArrayList<QuizzyLog>>() {
                    @Override
                    public ArrayList<QuizzyLog> call() throws Exception {
                        return (ArrayList<QuizzyLog>) quizzyLogDAO.getRecordsByUserId(loggedInUserId);
                    }
                }
        );

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            //e.printStackTrace();
            Log.i(MainActivity.TAG, "Problem when getting all QuizzyLogs in the repository");
        }
        return null;


    }

    public void insertQuiz(Quiz quiz, Consumer<Long> callback) {
        QuizzyLogDatabase.databasedWriteExecutor.execute(() -> {
            long id = quizDAO.insertQuiz(quiz);
            callback.accept(id);
        });
    }

    public void insertQuestion(Question q) {
        QuizzyLogDatabase.databasedWriteExecutor.execute(() -> {
            questionDAO.insertQuestion(q);
        });
    }
}
