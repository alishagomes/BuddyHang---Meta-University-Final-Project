package com.example.buddyhang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDeepLinkBuilder;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buddyhang.fragments.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
/**
 * For creating an event
 */
public class CreateEventActivity extends AppCompatActivity {

    Button create_button;
    EditText eventDescription;
    EditText eventName;
    EditText eventLocation;
    EditText eventDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // for actionbar
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View imageView = inflater.inflate(R.layout.my_logo, null);
        actionBar.setCustomView(imageView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initializing variables
        eventName = findViewById(R.id.eventName);
        eventLocation = findViewById(R.id.eventLocation);
        eventDescription = findViewById(R.id.eventDescription);
        create_button = findViewById(R.id.create_button);
        eventDate = findViewById(R.id.eventDate);

        // when create is pressed
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savepost();
                Toast.makeText(getBaseContext(), "Event has been created!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savepost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
        String eventId = reference.push().getKey();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("eventId" , eventId);
        hashMap.put("eventName" , eventName.getText().toString());
        hashMap.put("eventDescription" , eventDescription.getText().toString());
        hashMap.put("eventLocation" , eventLocation.getText().toString());
        hashMap.put("eventHost" , FirebaseAuth.getInstance().getCurrentUser().getUid());
        hashMap.put("eventDate", eventDate.getText().toString());
        reference.child(eventId).setValue(hashMap);

        // display confetti here

        // navigate from activity to calendar fragment


    }
}
