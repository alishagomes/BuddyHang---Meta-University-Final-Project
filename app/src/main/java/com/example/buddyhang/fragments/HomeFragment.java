package com.example.buddyhang.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.buddyhang.CreateEventActivity;
import com.example.buddyhang.R;
import com.example.buddyhang.adapters.ApiRecyclerViewAdapter;
import com.example.buddyhang.adapters.EventAdapter;
import com.example.buddyhang.models.ApiEvent;
import com.example.buddyhang.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

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

    // for api
    private final String JSON_URL = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=US&apikey=9Jeo5x9G0hshdjmf91d4sCXKDFPvVs3h";
    private JsonObjectRequest request;
    private RequestQueue requestQueue;
    private List<ApiEvent> lstApiEvent;
    RecyclerView ticketmasterEvents;

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

        // Creating a list of events from ticketmaster
        lstApiEvent = new ArrayList<>();
        // recyclerview that displays ticketmaster events
        ticketmasterEvents = view.findViewById(R.id.ticketmasterEvents);
        jsonRequest();

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

    private void jsonRequest() {

        request = new JsonObjectRequest
                (Request.Method.GET, JSON_URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                ApiEvent event = new ApiEvent();
                                event.setName(response.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getString("name"));
                                event.setUrl(response.getJSONObject("_embedded").getJSONArray("events").getJSONObject(i).getString("url"));
                                lstApiEvent.add(event);
                            } catch (JSONException e) {
                                Log.i("Error",e.toString());
                            }
                        }
                        setupRecyclerView(lstApiEvent);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        
                    }
                });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

    }

    private void setupRecyclerView(List<ApiEvent> lstApiEvent) {
        ApiRecyclerViewAdapter adapter = new ApiRecyclerViewAdapter(getContext(),lstApiEvent) ;
        ticketmasterEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        ticketmasterEvents.setAdapter(adapter);
    }

}