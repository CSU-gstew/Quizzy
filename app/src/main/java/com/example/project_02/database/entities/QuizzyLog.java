package com.example.project_02.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.project_02.database.QuizzyLogDatabase;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity(tableName = QuizzyLogDatabase.QUIZZY_LOG_TABLE)
public class QuizzyLog {
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**
    private String exercise;
    private double weight;
    private int reps;
    **/

    private LocalDateTime date;
    private int userId;

    public QuizzyLog(String exercise, double weight, int reps, int userId) {
        //this.exercise = exercise;
        //this.weight = weight;
        //this.reps = reps;
        this.userId = userId;
        date = LocalDateTime.now();
    }

    @Override
    public String toString() {
        /**
        return exercise + "\n" + "weight: " + weight + "\n" +
                "reps: " + reps + "\n" +
                "date: " + date.toString() + "\n" +
                "=-=-=-=-=-=-=-\n";
        **/
        return "date: " + date.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QuizzyLog quizzyLog = (QuizzyLog) o;
        //return id == gymLog.id && Double.compare(weight, gymLog.weight) == 0 && reps == gymLog.reps && userId == gymLog.userId && Objects.equals(exercise, gymLog.exercise) && Objects.equals(date, gymLog.date);
        return id == quizzyLog.id && userId == quizzyLog.userId && Objects.equals(date, quizzyLog.date);
    }

    @Override
    public int hashCode() {
        //return Objects.hash(id, exercise, weight, reps, date, userId);
        return Objects.hash(id, date, userId);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    /**
    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
    **/

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
