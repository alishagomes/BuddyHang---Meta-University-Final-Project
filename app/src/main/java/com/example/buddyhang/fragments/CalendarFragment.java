package com.example.buddyhang.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.buddyhang.DeclinedEventsActivity;
import com.example.buddyhang.R;
import com.example.buddyhang.adapters.UserAcceptedEventsAdapter;
import com.example.buddyhang.adapters.UserHostedEventsAdapter;
import com.example.buddyhang.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows a calendar view of all the user's events and the events the user accepts
 */

public class CalendarFragment extends Fragment {

    private RecyclerView recycler_view_users_posts;
    private UserHostedEventsAdapter userEventAdapter;
    private List<Event> eventList;

    private RecyclerView recycler_view_user_accepted_events;
    private UserAcceptedEventsAdapter userAcceptedEventsAdapter;
    private List<String> acceptedEvents;
    private List<Event> userAcceptedEventsList;

    private Button declined_button;

    public CalendarFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        declined_button = view.findViewById(R.id.declined_button);
        // when decline button is clicked, it navigates to the events that have been declined
        declined_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to the declinedEvents activity
                Intent i = new Intent(getActivity(), DeclinedEventsActivity.class);
                startActivity(i);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
            }
        });

        // setting up the recycler view for user hosted events
        recycler_view_users_posts = view.findViewById(R.id.userPosts);
        recycler_view_users_posts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycler_view_users_posts.setLayoutManager(linearLayoutManager);
        eventList = new ArrayList<>();
        userEventAdapter = new UserHostedEventsAdapter(getContext() , eventList);
        recycler_view_users_posts.setAdapter(userEventAdapter);
        recycler_view_users_posts.setVisibility(View.VISIBLE);
        userPosts();

        // setting up the recycler view for the user accepted events
        recycler_view_user_accepted_events = view.findViewById(R.id.recycler_view_user_accepted_events);
        recycler_view_user_accepted_events.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recycler_view_user_accepted_events.setLayoutManager(linearLayoutManager2);
        userAcceptedEventsList = new ArrayList<>();
        userAcceptedEventsAdapter = new UserAcceptedEventsAdapter(getContext() , userAcceptedEventsList);
        recycler_view_user_accepted_events.setAdapter(userAcceptedEventsAdapter);
        recycler_view_user_accepted_events.setVisibility(View.VISIBLE);
        userAcceptedEvents();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void userPosts() {
        FirebaseDatabase.getInstance().getReference("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Event event = snapshot.getValue(Event.class);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (event.getEventHost().equals(firebaseUser.getUid())){
                        eventList.add(event);
                    }
                }
                userEventAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void userAcceptedEvents() {
        acceptedEvents = new ArrayList<>();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference().child("Accepts").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    acceptedEvents.add(snapshot.getKey());
                }
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Events");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userAcceptedEventsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Event event = snapshot.getValue(Event.class);
                            for (String id : acceptedEvents){
                                if (event.getEventId().equals(id)){
                                    userAcceptedEventsList.add(event);
                                }
                            }
                        }
                        userAcceptedEventsAdapter.notifyDataSetChanged();
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