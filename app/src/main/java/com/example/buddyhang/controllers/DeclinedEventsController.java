package com.example.buddyhang.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;

import com.example.buddyhang.R;
import com.example.buddyhang.adapters.UserDeclinedEventsAdapter;
import com.example.buddyhang.models.PrivateEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DeclinedEventsController extends AppCompatActivity {

    private RecyclerView recycler_view_declined_events;
    private UserDeclinedEventsAdapter userDeclinedEventsAdapter;
    private List<PrivateEvent> declinedEventsList;
    private List<String> declinedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_declined_events);

        // displays the back button in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recycler_view_declined_events = findViewById(R.id.recycler_view_declined_events);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recycler_view_declined_events.setLayoutManager(linearLayoutManager2);
        declinedEventsList = new ArrayList<>();
        userDeclinedEventsAdapter = new UserDeclinedEventsAdapter(this,declinedEventsList);
        recycler_view_declined_events.setAdapter(userDeclinedEventsAdapter);
        recycler_view_declined_events.setVisibility(View.VISIBLE);
        userDeclinedEvents();;
    }

    private void userDeclinedEvents() {
        declinedEvents = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Declines")
                .child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    declinedEvents.add(snapshot.getKey());
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        declinedEventsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PrivateEvent event = snapshot.getValue(PrivateEvent.class);
                            for (String id : declinedEvents){
                                if (event.getEventId().equals(id)){
                                    declinedEventsList.add(event);
                                }
                            }
                        }
                         userDeclinedEventsAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}