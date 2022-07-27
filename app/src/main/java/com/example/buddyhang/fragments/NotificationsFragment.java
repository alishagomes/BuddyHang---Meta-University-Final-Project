package com.example.buddyhang.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.buddyhang.R;
import com.example.buddyhang.adapters.NotificationAdapter;
import com.example.buddyhang.models.Notification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    RecyclerView notificationRecyclerView;
    private FirebaseAuth firebaseAuth;
    private ArrayList<Notification> notifications;

    private NotificationAdapter notificationAdapter;

    public NotificationsFragment() {
        notifications = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        firebaseAuth = FirebaseAuth.getInstance();
        getNotifications();
        return view;
    }

    private void getNotifications() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).child("notifications").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                notifications.clear();
                for (DataSnapshot ds: snapshot.getChildren()) {
                    Notification model = ds.getValue(Notification.class);
                    notifications.add(model);
                }
                notificationAdapter = new NotificationAdapter(getContext(),notifications);
                notificationRecyclerView.setAdapter(notificationAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}