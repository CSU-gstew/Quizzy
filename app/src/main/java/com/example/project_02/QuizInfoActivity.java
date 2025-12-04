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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.User;
import com.example.project_02.viewHolders.QuizInfoAdapter;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class QuizInfoActivity extends AppCompatActivity {

    private static final int LOGGED_OUT = -1;
    private static final String PREFERENCE_USER_ID_KEY = "quizapp_user_id";
    private MaterialToolbar toolbar;
    private View adminButton;
    private QuizzyLogRepository repository;
    private int loggedInUserId = LOGGED_OUT;
    private User user;

    private QuizInfoAdapter adapter;
    private TextView quizCountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        quizCountTextView = findViewById(R.id.quizCountTextView);
        RecyclerView recyclerView = findViewById(R.id.logDisplayRecyclerView);

        adapter = new QuizInfoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // This just placeholder data
        // TODO: Replace with real code that puts info from quiz into recycler
        List<QuizInfo> demoData = createDemoData();

        adapter.setItems(demoData);
        quizCountTextView.setText("Number of Quizzes Taken: " + demoData.size());

        // For tracking the username when logged in
        readLoggedInUserFromSharedPreferences();
        loadUserFromDatabase();
    }

    // This just placeholder data
    // TODO: Replace with real data coming from the quizzes
    private List<QuizInfo> createDemoData() {
        List<QuizInfo> list = new ArrayList<>();
        list.add(new QuizInfo("Quiz 1", 4, 1));
        list.add(new QuizInfo("Quiz 2", 5, 0));
        list.add(new QuizInfo("Quiz 3", 6, 4));
        return list;
    }

    /**
     * This is preferably the ideal code to use
     * Once we get quizz functionality up and running
     * Leaving it here for later use
     * List<QuizInfo> infos = new ArrayList<>();
     * for (QuizzyLog log : logsForUser) {
     *     infos.add(new QuizInfo(
     *         log.getQuizName(),
     *         log.getQuestionsPassed(),
     *         log.getQuestionsFailed()
     *     ));
     * }
     * adapter.setItems(infos);
     * quizCountTextView.setText("Number of Quizzes Taken: " + infos.size());
     *
     * Gonna need this line too to overwrite the text view
     * Should update with the size of how many quizzes a user will have taken
     * quizCountTextView.setText("Number of Quizzes Taken: " + quizList.size());
     *
     */

    public static Intent quizInfoIntentFactory(Context context, int userId) {
        return new Intent(context, QuizInfoActivity.class);
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

                        invalidateOptionsMenu();
                    }
                });
    }

     // Following methods are specifically for logging out via username click

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
                new AlertDialog.Builder(QuizInfoActivity.this);

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