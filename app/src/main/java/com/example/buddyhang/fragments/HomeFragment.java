package com.example.buddyhang.fragments;

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

import com.example.buddyhang.CreateEventActivity;
import com.example.buddyhang.R;
import com.example.buddyhang.adapters.EventAdapter;
import com.example.buddyhang.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Allows users to create a new event
 * see the events they have been invited to or from the Eventbrite API
 */
public class HomeFragment extends Fragment {

    Button create_event;
    private RecyclerView recycler_view_users_posts;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private List<String> followingList;

    public HomeFragment() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        create_event = view.findViewById(R.id.create_event);
        create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recycler_view_users_posts = view.findViewById(R.id.recycler_view_users_posts);
        recycler_view_users_posts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recycler_view_users_posts.setLayoutManager(linearLayoutManager);
        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(getContext() , eventList);
        recycler_view_users_posts.setAdapter(eventAdapter);
        followingList();
        return view;
    }


    private void viewEvents () {
        FirebaseDatabase.getInstance().getReference("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    for (String id : followingList) {
                        if (event.getEventHost().equals(id)){
                            eventList.add(event);
                        }
                    }
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void followingList() {
        followingList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference("Follow").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    followingList.add(snapshot.getKey());
                }
                viewEvents();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}