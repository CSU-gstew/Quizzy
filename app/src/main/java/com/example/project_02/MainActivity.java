package com.example.project_02;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project_02.database.QuizzyLogRepository;
import com.example.project_02.database.entities.QuizzyLog;
import com.example.project_02.database.entities.User;
import com.example.project_02.databinding.ActivityMainBinding;
import com.example.project_02.viewHolders.QuizzyLogAdapter;
import com.example.project_02.viewHolders.QuizzyLogViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final String MAIN_ACTIVITY_USER_ID = "com.example.project_02.MAIN_ACTIVITY_USER_ID";
    static final String SHARED_PREFERENCE_USERID_KEY = "com.example.project_02.SHARED_PREFERENCE_USERID_KEY";
    static final String SAVED_INSTANCE_STATE_USERID_KEY = "com.example.project_02.SAVED_INSTANCE_STATE_USERID_KEY";


    private static final int LOGGED_OUT = -1;
    private ActivityMainBinding binding;
    private QuizzyLogRepository repository;
    private QuizzyLogViewModel quizzyLogViewModel;


    /**
     * This is where any variables for the application will be located
     * But for now, we're just focusing on the user/admin logging in/out
     * TODO: Implement any needed variables
     */
    private int loggedInUserId = -1;
    private User user;


    public static final String TAG = "DAC_QUIZZYLOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        quizzyLogViewModel = new ViewModelProvider(this).get(QuizzyLogViewModel.class);


        /**
        // The following is code for the recycler view from gymlog
        // Won't be used for now but I'll leave it here just in case
        RecyclerView recyclerView = binding.logDisplayRecyclerView;
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        **/

        final QuizzyLogAdapter adapter = new QuizzyLogAdapter(new QuizzyLogAdapter.QuizzyLogDiff());

        repository = QuizzyLogRepository.getRepository(getApplication());
        loginUser(savedInstanceState);

        quizzyLogViewModel.getAllLogsById(loggedInUserId).observe(this, quizzyLogs -> {
            adapter.submitList(quizzyLogs);
        });

        // User is not logged in at this point, go to log in screen
        if(loggedInUserId == -1){
            Intent intent = LogInActivity.loginIntentFactory(getApplicationContext());
            startActivity(intent);
        }

        updateSharedReference();

        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInformationFromDisplay();
                insertQuizzyLogRecord();
            }
        });
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
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_USERID_KEY, loggedInUserId);
        updateSharedReference();
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
        item.setVisible(true);
        if (user == null) {
            return false;
        }
        item.setTitle(user.getUsername());
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {

                showLogoutDialog();
                return false;

            }
        });
        return true;
    }

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

    static Intent mainActivityIntentFactory(Context context, int userId){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(MAIN_ACTIVITY_USER_ID, userId);
        return intent;
    }

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

    @Deprecated
    private void updateDisplay(){
        ArrayList<QuizzyLog> allLogs = repository.getAllLogsByUserId(loggedInUserId);
        if(allLogs.isEmpty()){
            //binding.logDisplayTextView.setText("Nothing to show, time to take a quiz!");
        }
        StringBuilder sb = new StringBuilder();
        for(QuizzyLog log : allLogs){
            sb.append(log);
        }

        //binding.logDisplayTextView.setText(sb.toString());
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