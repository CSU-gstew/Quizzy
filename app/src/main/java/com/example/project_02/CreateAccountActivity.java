package com.example.project_02;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.User;
import com.example.project_02.databinding.ActivityCreateAccountBinding;

public class CreateAccountActivity extends AppCompatActivity {
    private ActivityCreateAccountBinding binding;
    private QuizzyLogRepository repository;

    // This right here is a regular expression
    // Basically, it states that any character in a username must
    // Either be all characters between A-Z, a-z, or digits between 0-9
    // The + symbol at the ends ensures that there is one or more of a character present
    // Very useful if you know how to use regular expressions already
    private static final String username_regex = "^[A-Za-z0-9]+$";
    // Was going to initially do something like username.contains() but for each symbol
    // That is way too tedious, faster and smarter to use this regular expression instead


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
            toastMaker(getString(R.string.error_create_account_empty_fields), false);
            return;
        }

        // Make sure that the password that was retyped is the same as the previously typed
        if(!password.equals(password_copy)){
            toastMaker(getString(R.string.error_create_account_password_mismatch), false);
            return;
        }

        // Check if the typed username doesn't exist in the database
        // Essentially checking if the user exists beforehand or not
        LiveData<User> userLiveData = repository.getUserByUsername(username);
        userLiveData.observe(this, user -> {
            userLiveData.removeObservers(this);

            // Check if user exists beforehand
            if(user != null) {
                toastMaker(getString(R.string.error_create_account_existing_username), false);
            }

            // This is to ensure that the username has no odd/weird symbols such as the following:
            // ? ! . / * & ^ % $ # @ ( ) ~ ` - _ + = [ ] { } \ | ; : ' " , < >
            // Though the password will be able to have them for security reasons
            else if(!username.matches(username_regex)){ // Uses the previously declared regular expression
                toastMaker(getString(R.string.error_create_account_username_invalid_characters), false);
                toastMaker("Invalid Characters:\n?!./*&^%$#@()~`-_=+[]{}\\|;:'\",<>", true);
            } else {
                // Read the Admin switch state
                boolean isAdmin = binding.adminSwitch.isChecked();

                // Successfully create the account and insert into the database
                User newUser = new User(username, password, isAdmin);
                //newUser.setAdmin(isAdmin); // Sets the User to Admin if true
                repository.insertUser(newUser);

                // Display the appropriate toast if the User is an Admin upon creation
                if(isAdmin){
                    toastMaker(getString(R.string.create_admin_account_successful), false);
                }
                toastMaker(getString(R.string.create_account_successful), false);

                finish(); // Should send user back to the Main Activity where they can sign in
            }
        });
    }

    private void saveAccountCreatedUser(int id) {
    }

    private void toastMaker(String message, boolean is_long) {
        int length = is_long ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT;
        Toast.makeText(this, message, length).show();
    }

    static Intent createAccountIntentFactory(Context context) {
        return new Intent(context, CreateAccountActivity.class);
    }
}
