package com.example.buddyhang.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.buddyhang.controllers.NewEventController;
import com.example.buddyhang.R;
import com.example.buddyhang.adapters.PublicEventViewHolderAdapter;
import com.example.buddyhang.adapters.EventAdapter;
import com.example.buddyhang.models.PublicEvent;
import com.example.buddyhang.models.PrivateEvent;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

/**
 * Allows users to create a new event
 * see the events they have been invited to or from the Eventbrite API
 */
public class HomeFragment extends Fragment {

    Button create_event;
    private RecyclerView recycler_view_users_posts;
    private EventAdapter eventAdapter;
    private List<PrivateEvent> eventList;
    private List<String> followingList;

    // for api
    private final String JSON_URL = "https://app.ticketmaster.com/discovery/v2/events.json?countryCode=US&apikey=9Jeo5x9G0hshdjmf91d4sCXKDFPvVs3h";
    private JsonObjectRequest request;
    private RequestQueue requestQueue;
    private List<PublicEvent> lstApiEvent;
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
                Intent i = new Intent(getActivity(), NewEventController.class);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
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


        String deletedPost = null;

        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            PrivateEvent deletedEvent = null;

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        deletedEvent = eventList.get(position);
                        eventList.remove(position);
                        eventAdapter.notifyItemRemoved(position);
                        Snackbar.make(recycler_view_users_posts,deletedEvent.getEventName() + " was declined.",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                eventList.add(position,deletedEvent);
                                eventAdapter.notifyItemInserted(position);

                            }
                        }).show();
                        break;
                    case ItemTouchHelper.RIGHT:
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        deletedEvent = eventList.get(position);
                        FirebaseDatabase.getInstance().getReference().child("Accepts").child(firebaseUser.getUid()).child(deletedEvent.getEventId()).setValue(true);
                        eventList.remove(position);
                        eventAdapter.notifyItemRemoved(position);
                        Snackbar.make(recycler_view_users_posts,deletedEvent.getEventName() + " was accepted.",Snackbar.LENGTH_LONG).setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FirebaseDatabase.getInstance().getReference().child("Accepts").child(firebaseUser.getUid()).child(deletedEvent.getEventId()).setValue(true);
                                eventList.add(position,deletedEvent);
                                eventAdapter.notifyItemInserted(position);
                            }
                        }).show();
                        break;
                }

            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(getContext(),c,recyclerView,viewHolder,dX,dY,actionState,isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(ContextCompat.getColor(getContext(),R.color.red))
                        .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_24)
                        .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(),R.color.green))
                        .addSwipeRightActionIcon(R.drawable.ic_baseline_check_circle_outline_24)
                        .create()
                        .decorate();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recycler_view_users_posts);

        return view;


    }


    private void viewEvents () {
        FirebaseDatabase.getInstance().getReference("Events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PrivateEvent event = snapshot.getValue(PrivateEvent.class);
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
                                PublicEvent event = new PublicEvent();
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

    private void setupRecyclerView(List<PublicEvent> lstApiEvent) {
        PublicEventViewHolderAdapter adapter = new PublicEventViewHolderAdapter(getContext(),lstApiEvent) ;
        ticketmasterEvents.setLayoutManager(new LinearLayoutManager(getContext()));
        ticketmasterEvents.setAdapter(adapter);
    }

}