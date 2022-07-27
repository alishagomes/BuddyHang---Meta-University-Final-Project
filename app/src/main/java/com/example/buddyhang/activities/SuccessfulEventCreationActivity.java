package com.example.buddyhang.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import com.example.buddyhang.R;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;


public class SuccessfulEventCreationActivity extends AppCompatActivity {

    Button button_return;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_successful_event_creation);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        button_return = findViewById(R.id.return_button);

        DisplayMetrics display = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(display);
        final KonfettiView konfettiView = findViewById(R.id.confetti);
        konfettiView.build()
                .addColors(Color.rgb(125, 157, 156),Color.rgb(87,111,114))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, display.widthPixels + 50f, -50f, -50f)
                .streamFor(300, 1500L);



        button_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SuccessfulEventCreationActivity.this , MainActivity.class);
                startActivity(i);
            }
        });

    }
}