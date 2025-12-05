package com.example.project_02;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.User;
import com.google.android.material.appbar.MaterialToolbar;

public class BaseActivity extends AppCompatActivity {

    protected static final int LOGGED_OUT = -1;
    protected int loggedInUserId = LOGGED_OUT;
    protected User user;
    protected QuizzyLogRepository repository;

    protected void setupToolbar(MaterialToolbar toolbar) {
        setSupportActionBar(toolbar);
        repository = QuizzyLogRepository.getRepository(getApplication());

        readLoggedInUserFromSharedPreferences();
        loadUser();
    }

    private void readLoggedInUserFromSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        loggedInUserId = prefs.getInt(
                getString(R.string.preference_userId_key), LOGGED_OUT);
    }

    private void loadUser() {
        if (loggedInUserId == LOGGED_OUT) return;

        repository.getUserByUserId(loggedInUserId)
                .observe(this, loadedUser -> {
                    user = loadedUser;
                    invalidateOptionsMenu();
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
        new AlertDialog.Builder(this)
                .setMessage("Logout?")
                .setPositiveButton("Logout", (dialog, which) -> logout())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }

    protected void logout() {
        loggedInUserId = LOGGED_OUT;

        SharedPreferences prefs = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        prefs.edit().putInt(getString(R.string.preference_userId_key), LOGGED_OUT).apply();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}