package com.example.project_02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.User;
import com.google.android.material.appbar.MaterialToolbar;

public class LandingPage extends AppCompatActivity {
    private static final int LOGGED_OUT = -1;
    private static final String PREFERENCE_USER_ID_KEY = "quizapp_user_id";
    private MaterialToolbar toolbar;
    private View adminButton;
    private QuizzyLogRepository repository;
    private int loggedInUserId = LOGGED_OUT;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        toolbar = findViewById(R.id.action_bar);
        setSupportActionBar(toolbar);
        repository = QuizzyLogRepository.getRepository(getApplication());

        // Code for wiring up View Scores button
        Button viewScoresButton = findViewById(R.id.viewScoresButton);
        viewScoresButton.setOnClickListener(v -> {
            Intent intent = new Intent(LandingPage.this, QuizInfoActivity.class);
            startActivity(intent);
        });

        adminButton = findViewById(R.id.adminButton);

        readLoggedInUserFromSharedPreferences();
        loadUserFromDatabase();

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

    private void loadUserFromDatabase() {
        if (loggedInUserId == LOGGED_OUT) {
            goToLogin();
            return;
        }

        repository.getUserByUserId(loggedInUserId)
                .observe(this, loadedUser -> {
                    user = loadedUser;
                    if (user != null) {

                        // 🔹 Add this block:
                        // TODO: REMOVE LATER
                        Log.d("LandingPage",
                                "Loaded user id=" + user.getId()
                                        + ", username=" + user.getUsername()
                                        + ", isAdmin=" + user.isAdmin());

                        setupUIForUser(user);
                        invalidateOptionsMenu();
                    }
                });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.logoutMenuItem);

        if (user == null) {
            item.setVisible(false);
            return true;
        }

        item.setVisible(true);
        item.setTitle(user.getUsername());

        item.setOnMenuItemClickListener(menuItem -> {
            showLogoutDialog();
            return true;
        });

        return super.onPrepareOptionsMenu(menu);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(LandingPage.this);

        builder.setMessage("Logout?")
                .setPositiveButton("Logout",
                        (dialog, which) -> logout())
                .setNegativeButton("Cancel",
                        (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void logout() {
        loggedInUserId = LOGGED_OUT;

        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        prefs.edit().putInt(
                getString(R.string.preference_userId_key), LOGGED_OUT
        ).apply();

        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
