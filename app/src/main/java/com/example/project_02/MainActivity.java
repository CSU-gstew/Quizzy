package com.example.project_02;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.User;
import com.example.project_02.databinding.ActivityMainBinding;
import com.example.project_02.viewHolders.QuizzyLogViewModel;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.project_02.MAIN_ACTIVITY_USER_ID";
    private static final String SAVED_INSTANCE_STATE_USERID_KEY = "loggedInUserId";


    private static final int LOGGED_OUT = -1;
    private ActivityMainBinding binding;
    private QuizzyLogRepository repository;
    private QuizzyLogViewModel quizzyLogViewModel;
    private int loggedInUserId = LOGGED_OUT;
    private User user;

    public static final String TAG = "DAC_QUIZZYLOG";

    /**
     * This is where any variables for the application will be located
     * But for now, we're just focusing on the user/admin logging in/out
     * TODO: Implement any needed variables
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        repository = QuizzyLogRepository.getRepository(getApplication());

        loginUser(savedInstanceState);

        // If not logged in
        if (loggedInUserId == LOGGED_OUT) {
            onLoggedOut();
        } else {
            onLoggedIn();
        }
    }

    // Once the user has been logged in succesfully
    private void onLoggedIn() {
        View loginButton = findViewById(R.id.loginButton);
        if (loginButton != null) loginButton.setVisibility(View.GONE);

        Toast.makeText(this, "Logged in (userId=" + loggedInUserId + ")", Toast.LENGTH_SHORT).show();

        // TODO: This is where the app would take the user to the LandingPage Activity after logging in
    }

    // When no one is logged in
    private void onLoggedOut() {
        Button loginBtn = findViewById(R.id.loginButton);
        if (loginBtn != null) {
            loginBtn.setVisibility(View.VISIBLE);
            loginBtn.setOnClickListener(v ->
                    startActivity(LogInActivity.loginIntentFactory(MainActivity.this))
            );
        }
    }

    private int getSavedUserId() {
        return getSharedPrefs().getInt(getString(R.string.preference_userId_key), LOGGED_OUT);
    }

    private void persistUserId(int userId) {
        getSharedPrefs().edit()
                .putInt(getString(R.string.preference_userId_key), userId).apply();
    }

    private SharedPreferences getSharedPrefs() {
        return getApplicationContext()
                .getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
    }

    static Intent mainActivityIntentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }

    private void loginUser(Bundle savedInstanceState) {
        // check shared preference for logged in user
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        loggedInUserId = sharedPreferences.getInt(getString(R.string.preference_userId_key), LOGGED_OUT);

        // check intent for logged in user
        if(loggedInUserId == LOGGED_OUT & savedInstanceState != null && savedInstanceState.containsKey(SAVED_INSTANCE_STATE_USERID_KEY)){
            loggedInUserId = savedInstanceState.getInt(SAVED_INSTANCE_STATE_USERID_KEY, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            loggedInUserId = getIntent().getIntExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);
        }
        if(loggedInUserId == LOGGED_OUT){
            return;
        }

        LiveData<User> userObserver = repository.getUserByUserId(loggedInUserId);
        userObserver.observe(this, user -> {
            this.user = user;
            if (user != null) {
                invalidateOptionsMenu();
            }
        });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        super.onSaveInstanceState(outState);
    }


    // Display a menu when user chooses to log out (eventually will be used)
    // TODO: Use this at some point (preferably for the landing page)
    private void showLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
        final AlertDialog alertDialog = alertBuilder.create();

        alertBuilder.setMessage("Logout?");

        alertBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                logout();
            }
        });
        alertBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        alertBuilder.create().show();
    }

    private void logout() {
        loggedInUserId = LOGGED_OUT;
        updateSharedReference();

        getIntent().putExtra(MAIN_ACTIVITY_USER_ID, LOGGED_OUT);

        startActivity(LogInActivity.loginIntentFactory(getApplicationContext()));

    }

    private void updateSharedReference() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key),
                Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(getString(R.string.preference_userId_key), loggedInUserId);
        sharedPrefEditor.apply();
    }


    // The bottom two methods might be used later on
    // Still need to decide if they could be useful at some point

    // TODO: Make this insert user/quiz information later on
    private void insertQuizzyLogRecord(){
        /**
         // The following code comes from gymlog
         // For now, this method shall remain incomplete
         if(mExercise.isEmpty()){
         return;
         }

         GymLog log = new GymLog(mExercise, mWeight, mReps, loggedInUserId);
         repository.insertGymLog(log);
         **/
    }

    // TODO: Make this retrieve user/quiz information later on
    private void getInformationFromDisplay(){
        /**
         // The following code comes from gymlog
         // For now, this method shall remain incomplete
        mExercise = binding.exerciseInputEditText.getText().toString();
        try{
            mWeight = Double.parseDouble(binding.weightInputEditText.getText().toString());
        } catch(NumberFormatException e){
            Log.d(TAG, "Error reading value from Weight edit text.");
        }

        try{
            mReps = Integer.parseInt(binding.repInputEditText.getText().toString());
        } catch(NumberFormatException e){
            Log.d(TAG, "Error reading value from Reps edit text.");
        }
        **/
    }
}