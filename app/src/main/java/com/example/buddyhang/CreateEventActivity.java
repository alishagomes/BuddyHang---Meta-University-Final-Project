package com.example.buddyhang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
/**
 * For creating an event
 */
public class CreateEventActivity extends AppCompatActivity {

    Button create_button;
    EditText eventDescription;
    EditText eventName;
    Date eventDate;
    EditText eventLocation;

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
        create_button = findViewById(R.id.create_button);
        create_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), "Event has been created. Check your feed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void savepost() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
        String eventId = reference.push().getKey();
        HashMap<String , Object> hashMap = new HashMap<>();
        hashMap.put("eventId" , eventId);
        hashMap.put("eventDescription" , eventDescription.getText().toString());
        hashMap.put("eventHost" , FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.child(eventId).setValue(hashMap);
    }
}