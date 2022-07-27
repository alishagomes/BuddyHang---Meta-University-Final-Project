package com.example.buddyhang.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.buddyhang.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Gives the user the option to login or signup
 */
public class LaunchActivity extends AppCompatActivity {

    private TextView register_button;
    private Button login_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        // don't show the action bar if we can’t get the action bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        login_button = findViewById(R.id.login_button);
        register_button = findViewById(R.id.register_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LaunchActivity.this , LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LaunchActivity.this , RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent i = new Intent(LaunchActivity.this , MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}
