package com.example.project_02.database.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.project_02.database.QuizzyLogDatabase;

import java.util.Objects;

@Entity(tableName = QuizzyLogDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    public boolean isAdmin = false;

    // Quiz Related Variables
    private int quizzes_taken;
    private int questions_passed;
    private int questions_failed;


    public User(String username, String password, boolean isAdmin, int quizzes_taken, int questions_passed, int questions_failed) {
        this.username        = username;
        this.password        = password;
        this.isAdmin         = isAdmin;

        this.quizzes_taken   = quizzes_taken;
        this.questions_passed = questions_passed;
        this.questions_failed = questions_failed;
    }

    // These two constructors are used for creating a new user with just the username and password
    // No need to pass in values such as isAdmin, quizzes_taken, questions_passed, or questions_failed
    // Both are tagged with @Ignore so that the compiler knows to choose the previous constructor
    @Ignore
    public User(String username, String password) {
        this(username, password, false, 0, 0, 0);
    }
    @Ignore
    public User(String username, String password, boolean isAdmin) {
        this(username, password, isAdmin, 0, 0, 0);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && isAdmin == user.isAdmin && quizzes_taken == user.quizzes_taken && questions_passed == user.questions_passed && questions_failed == user.questions_failed && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, isAdmin, quizzes_taken, questions_passed, questions_failed);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public int getQuizzes_taken() {
        return quizzes_taken;
    }

    public void setQuizzes_taken(int quizzes_taken) {
        this.quizzes_taken = quizzes_taken;
    }

    public int getQuestions_passed() {
        return questions_passed;
    }

    public void setQuestions_passed(int questions_passed) {
        this.questions_passed = questions_passed;
    }

    public int getQuestions_failed() {
        return questions_failed;
    }

    public void setQuestions_failed(int questions_failed) {
        this.questions_failed = questions_failed;
    }
}
