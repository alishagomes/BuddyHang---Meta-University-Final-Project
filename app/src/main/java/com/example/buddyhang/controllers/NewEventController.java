package com.example.buddyhang.controllers;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.example.buddyhang.R;
import com.example.buddyhang.activities.SuccessfulEventCreationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * For creating an event
 */
public class NewEventController extends AppCompatActivity {

    Button create_button;
    EditText eventDescription;
    EditText eventName;
    EditText eventLocation;
    String eventDate;
    String eventTime;
    private DatePickerDialog setDateDialog;
    private Button dateButton;
    Button timeButton;
    int hour;
    int minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        getSupportActionBar().hide();

        eventName = findViewById(R.id.eventName);
        eventLocation = findViewById(R.id.eventLocation);
        eventDescription = findViewById(R.id.eventDescription);
        create_button = findViewById(R.id.create_button);
        timeButton = findViewById(R.id.timeButton);
        dateButton = findViewById(R.id.dateButton);
        chooseDate();
        dateButton.setText(getDate());

        // when create button is pressed, navigate to the SuccessfulEventCreationActivity class
        create_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savepost();
                Intent i = new Intent(NewEventController.this , SuccessfulEventCreationActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.page_animation1, R.anim.page_animation2);

            }
        });
    }

    private String getDate()
    {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        return setDate(day, month + 1, year);
    }

    private void chooseDate()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                dateButton.setText(setDate(day, month + 1, year));
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        setDateDialog = new DatePickerDialog(this, dateSetListener, year, month, day);

    }

    public void pickDate(View view)
    {
        setDateDialog.show();
    }

    private String setDate(int day, int month, int year)
    {
         eventDate = day + " " + getMonth(month) + " " + year;
         return eventDate;
    }

    private String getMonth(int month)
    {
        if(month == 1)
            return "January";
        if(month == 2)
            return "February";
        if(month == 3)
            return "March";
        if(month == 4)
            return "April";
        if(month == 5)
            return "May";
        if(month == 6)
            return "June";
        if(month == 7)
            return "July";
        if(month == 8)
            return "August";
        if(month == 9)
            return "September";
        if(month == 10)
            return "October";
        if(month == 11)
            return "November";
        if(month == 12)
            return "December";
        return "January";
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
        hashMap.put("eventDate", eventDate);
        hashMap.put("eventTime", eventTime);
        reference.child(eventId).setValue(hashMap);
    }


    public void pickTime(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int currentHour, int currentMinute)
            {
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",currentHour, currentMinute));
                eventTime = String.format(Locale.getDefault(), "%02d:%02d",currentHour, currentMinute);
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Event Time");
        timePickerDialog.show();
    }
}
