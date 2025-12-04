package com.example.project_02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.User;
import com.google.android.material.appbar.MaterialToolbar;

public class LandingPage extends BaseActivity {
    private static final String PREFERENCE_USER_ID_KEY = "quizapp_user_id";
    private View adminButton;
    private int loggedInUserId = LOGGED_OUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        repository = QuizzyLogRepository.getRepository(getApplication());

        MaterialToolbar toolbar = findViewById(R.id.action_bar);
        setupToolbar(toolbar); // sets up logout menu and user info

        // Code for wiring up View Scores button
        readLoggedInUserFromSharedPreferences();
        Button viewScoresButton = findViewById(R.id.viewScoresButton);
        viewScoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, QuizInfoActivity.class);
            startActivity(intent);
        });
        adminButton = findViewById(R.id.adminButton);
        repository.getUserByUserId(loggedInUserId).observe(this, loadedUser -> {
            user = loadedUser;
            if (user != null) {
                setupUIForUser(user);
            }
        });
        setupButtons();
    }
    public static Intent landingPageIntentFactory(Context context, int userId) {
        return new Intent(context, LandingPage.class);
    }
    private void readLoggedInUserFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        loggedInUserId = prefs.getInt(
                getString(R.string.preference_userId_key), LOGGED_OUT);
    }

    private void setupUIForUser(User user) {
        if (user.isAdmin()) {
            adminButton.setVisibility(View.VISIBLE);
        } else {
            adminButton.setVisibility(View.INVISIBLE);
        }
    }
    private void setupButtons() {
        adminButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminActivity.class);
            startActivity(intent);
        });
    }
}
