package com.example.project_02;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.User;
import com.example.project_02.databinding.ActivityCreateAccountBinding;
import com.example.project_02.databinding.ActivityLogInBinding;

public class CreateAccountActivity extends Activity {
    private ActivityCreateAccountBinding binding;
    private QuizzyLogRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = QuizzyLogRepository.getRepository(getApplication());

        binding.signUpButton.setOnClickListener(v -> signUserUp());
    }

    private void signUserUp() {
        final String username = binding.userNameCreateAccountEditText.getText().toString().trim();
        final String password = binding.passwordCreateAccountEditText.getText().toString();
        final String password_copy = binding.passwordCreateAccountEditTextCopy.getText().toString();

        // For empty fields
        if (username.isEmpty() || password.isEmpty() || password_copy.isEmpty()) {
            toastMaker(getString(R.string.error_create_account_empty_fields));
            return;
        }

        // Make sure that the password that was retyped is the same as the previously typed
        if(!password.equals(password_copy)){
            toastMaker(getString(R.string.error_create_account_password_mismatch));
            return;
        }

        // Check if the typed username doesn't exist in the database
        // Essentially checking if the user exists beforehand or not
        LiveData<User> userLiveData = repository.getUserByUsername(username);
        userLiveData.observe((LifecycleOwner) this, user -> {
            userLiveData.removeObservers((LifecycleOwner) this);

            // Check if user exists beforehand
            if(user != null){
                toastMaker(getString(R.string.error_create_account_existing_username));
            } else {
                // Succesfully create the account and insert into the database
                User newUser = new User(username, password);
                repository.insertUser(newUser);

                toastMaker(getString(R.string.create_account_successful));

                finish(); // Should send user back to the Main Activity where they can sign in
            }
        });
    }

    private void saveAccountCreatedUser(int id) {
    }

    private void toastMaker(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    static Intent createAccountIntentFactory(Context context) {
        return new Intent(context, CreateAccountActivity.class);
    }
}
