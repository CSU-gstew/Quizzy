package com.example.project_02.database;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.project_02.MainActivity;
import com.example.project_02.database.entities.Question;
import com.example.project_02.database.entities.Quiz;
import com.example.project_02.database.entities.QuizzyLog;
import com.example.project_02.database.entities.User;
import com.example.project_02.database.typeConverters.LocalDateTypeConverter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@TypeConverters(LocalDateTypeConverter.class)
@Database(entities = {QuizzyLog.class, User.class, Quiz.class, Question.class}, version = 1, exportSchema = false)
public abstract class QuizzyLogDatabase extends RoomDatabase {

    public static final String USER_TABLE = "usertable";
    private static final String DATABASE_NAME = "QuizzyLogdatabase";

    public static final String QUIZZY_LOG_TABLE = "quizzyLogTable";

    private static volatile QuizzyLogDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static QuizzyLogDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            synchronized (QuizzyLogDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                                    context.getApplicationContext(),
                                    QuizzyLogDatabase.class,
                                    DATABASE_NAME
                            )
                            .fallbackToDestructiveMigration()
                            .addCallback(addDefaultValues)
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    private static final RoomDatabase.Callback addDefaultValues = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            Log.i(MainActivity.TAG, "DATABASE CREATED!");
            databaseWriteExecutor.execute(() -> {
                UserDAO dao = INSTANCE.userDAO();
                QuizDAO quizDao = INSTANCE.quizDAO();
                QuestionDAO questionDao = INSTANCE.questionDAO();
                dao.deleteAll();

                // Admin Creation
                User admin = new User("admin2", "admin2", true);

                // Insert Admin into Database
                dao.insert(admin);

                // User Creation
                User testUser1 = new User("testuser1", "testuser1");

                // Insert User into Database
                dao.insert(testUser1);

                Quiz quiz1 = new Quiz("testquiz_a", "11111");
                long quiz1Id = quizDao.insert(quiz1);

                questionDao.insert(
                        new Question((int) quiz1Id,
                                "What is the capital of France?",
                                "London", "Berlin", "Paris", "Madrid",
                                "C", "MCQ"),

                        new Question((int) quiz1Id,
                                "Which planet is known as the Red Planet?",
                                "Earth", "Mars", "Venus", "Jupiter",
                                "B", "MCQ"),

                        new Question((int) quiz1Id,
                                "How many continents are there on Earth?",
                                "5", "6", "7", "8",
                                "C", "MCQ")
                );
                Quiz quiz2 = new Quiz("testquiz_b", "22222");
                long quiz2Id = quizDao.insert(quiz2);

                questionDao.insert(
                        new Question((int) quiz2Id,
                                "What is 5 + 7?",
                                "10", "11", "12", "13",
                                "C", "MCQ"),

                        new Question((int) quiz2Id,
                                "What is 9 × 3?",
                                "27", "28", "21", "24",
                                "A", "MCQ"),

                        new Question((int) quiz2Id,
                                "What is 15 ÷ 5?",
                                "2", "3", "4", "5",
                                "B", "MCQ")
                );
            });
        }
    };

    public abstract QuizzyLogDAO quizzyLogDAO();

    public abstract UserDAO userDAO();
    public abstract QuizDAO quizDAO();
    public abstract QuestionDAO questionDAO();
}


