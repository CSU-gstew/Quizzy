package com.example.project_02;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.User;
import com.example.project_02.databinding.ActivityLogInBinding;

public class LogInActivity extends AppCompatActivity {
    private ActivityLogInBinding binding;
    private QuizzyLogRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = QuizzyLogRepository.getRepository(getApplication());

        binding.loginButton.setOnClickListener(v -> verifyUser());
    }

    private void verifyUser() {
        // These were added to the strings.xml file for later use
        final String username = binding.userNameLoginEditText.getText().toString().trim();
        final String password = binding.passwordLoginEditText.getText().toString();

        // For empty fields
        if (username.isEmpty() || password.isEmpty()) {
            toastMaker(getString(R.string.error_empty_fields));
            return;
        }

        LiveData<User> userLiveData = repository.getUserByUsername(username);
        userLiveData.observe(this, user -> {
            userLiveData.removeObservers(this);

            if (user == null) {
                // For wrong username
                toastMaker(getString(R.string.error_invalid_username, username));
                return;
            }

            if (!password.equals(user.getPassword())) {
                // For wrong password
                toastMaker(getString(R.string.error_invalid_password));
                return;
            }

            // Successful login and proceed to Main Activity
            saveLoggedInUser(user.getId());
            startActivity(MainActivity.mainActivityIntentFactory(LogInActivity.this, user.getId()));
            finish();
        });
    }

    // Method for saving the logged in user
    private void saveLoggedInUser(int userId){
        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        sp.edit().putInt(getString(R.string.preference_userId_key), userId).apply();
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent loginIntentFactory(Context context) {
        return new Intent(context, LogInActivity.class);
    }
}


