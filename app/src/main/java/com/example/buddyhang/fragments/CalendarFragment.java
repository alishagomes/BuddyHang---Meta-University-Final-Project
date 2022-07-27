package com.example.buddyhang.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.buddyhang.controllers.DeclinedEventsController;
import com.example.buddyhang.R;
import com.example.buddyhang.adapters.CalendarAdapter;
import com.example.buddyhang.adapters.UserAcceptedEventsAdapter;
import com.example.buddyhang.adapters.UserHostedEventsAdapter;
import com.example.buddyhang.models.PrivateEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Shows a calendar view of all the user's events and the events the user accepts
 */

public class CalendarFragment extends Fragment implements CalendarAdapter.OnItemListener {

    private RecyclerView recycler_view_users_posts;
    private UserHostedEventsAdapter userEventAdapter;
    private List<PrivateEvent> eventList;
    private RecyclerView recycler_view_user_accepted_events;
    private UserAcceptedEventsAdapter userAcceptedEventsAdapter;
    private List<String> acceptedEvents;
    private List<PrivateEvent> userAcceptedEventsList;
    private TextView declined_button;
    private TextView month;
    private RecyclerView calendarRecyclerView;
    private LocalDate currentDate;
    private Button previousMonth;
    private Button nextMonth;

    public CalendarFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        // The callback can be enabled or disabled here or in handleOnBackPressed()
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
                Intent i = new Intent(getActivity(), DeclinedEventsController.class);
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
        userEventAdapter = new UserHostedEventsAdapter(getContext(), eventList);
        recycler_view_users_posts.setAdapter(userEventAdapter);
        recycler_view_users_posts.setVisibility(View.VISIBLE);
        userPosts();

        // setting up the recycler view for the user accepted events
        recycler_view_user_accepted_events = view.findViewById(R.id.recycler_view_user_accepted_events);
        recycler_view_user_accepted_events.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext());
        recycler_view_user_accepted_events.setLayoutManager(linearLayoutManager2);
        userAcceptedEventsList = new ArrayList<>();
        userAcceptedEventsAdapter = new UserAcceptedEventsAdapter(getContext(), userAcceptedEventsList);
        recycler_view_user_accepted_events.setAdapter(userAcceptedEventsAdapter);
        recycler_view_user_accepted_events.setVisibility(View.VISIBLE);
        userAcceptedEvents();

        // setting up the calendar to click through different months
        previousMonth = view.findViewById(R.id.previousMonth);
        nextMonth = view.findViewById(R.id.nextMonth);
        previousMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = currentDate.minusMonths(1);
                showMonthLayout();
            }
        });
        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDate = currentDate.plusMonths(1);
                showMonthLayout();
            }
        });

        calendarRecyclerView = view.findViewById(R.id.calendarRecyclerView);
        month = view.findViewById(R.id.month);
        currentDate = LocalDate.now();
        showMonthLayout();

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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PrivateEvent event = snapshot.getValue(PrivateEvent.class);
                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (event.getEventHost().equals(firebaseUser.getUid())) {
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
                            PrivateEvent event = snapshot.getValue(PrivateEvent.class);
                            for (String id : acceptedEvents) {
                                if (event.getEventId().equals(id)) {
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

    private ArrayList<String> daysInMonth(LocalDate date) {
        ArrayList<String> days = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);
        LocalDate firstDay = currentDate.withDayOfMonth(1);

        for (int i = 1; i <= 42; i++) {
            if (i <= firstDay.getDayOfWeek().getValue() || i > yearMonth.lengthOfMonth() + firstDay.getDayOfWeek().getValue()) {
                days.add("");
            } else {
                days.add(String.valueOf(i - firstDay.getDayOfWeek().getValue()));
            }
        }
        return days;
    }

    private void showMonthLayout() {
        ArrayList<String> daysInMonth = daysInMonth(currentDate);
        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this, currentDate.getMonth().toString(), currentDate.getYear() + "");
        month.setText(formatDate(currentDate));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }


    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
    }


    public void onItemClick(int position, String dayText) {
        if (!dayText.equals("")) {
            Toast.makeText(getContext(), dayText + " " + formatDate(currentDate), Toast.LENGTH_LONG).show();
            Log.i("daytext", "daytext" + dayText);
        }
    }


}

