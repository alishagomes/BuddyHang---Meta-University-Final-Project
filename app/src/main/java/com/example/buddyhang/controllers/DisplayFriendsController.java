package com.example.buddyhang.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.example.buddyhang.R;
import com.example.buddyhang.adapters.UserAdapter;
import com.example.buddyhang.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;
/**
 * Displays the users a user can follow
 */
public class DisplayFriendsController extends AppCompatActivity {

    private UserAdapter userAdapter;
    private List<User> usersList;
    private RecyclerView recyclerView;
    private SocialAutoCompleteTextView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        getSupportActionBar().hide();

        recyclerView = findViewById(R.id.recycler_view_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersList = new ArrayList<>();
        userAdapter = new UserAdapter(this, usersList);

        recyclerView.setAdapter(userAdapter);

        search = findViewById(R.id.search);


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUser(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        displayUsers();

    }

    private void displayUsers() {
        FlingAnimation fling = new FlingAnimation(recyclerView, DynamicAnimation.SCROLL_Y);
        fling.setStartVelocity(2).setMinValue(0)
                .setFriction(1.1f)
                .start();

        FirebaseDatabase.getInstance().getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
                        usersList.add(user);
                    }
                    userAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUser (String s) {
        Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                .orderByChild("username").startAt(s).endAt(s + "\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    usersList.add(user);
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
