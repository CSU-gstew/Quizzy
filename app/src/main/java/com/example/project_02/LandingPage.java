package com.example.project_02;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LandingPage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);
    }

    // Upon logging in successfully, the user should be taken to this activity
    // TODO: Add some functionality to this mainmenu/landing activity

    public static android.content.Intent mainMenuActivityIntentFactory(Context ctx, int id) {
        return new android.content.Intent(ctx, LandingPage.class);
    }
}
