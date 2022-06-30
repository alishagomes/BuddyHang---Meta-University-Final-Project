package com.example.buddyhang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.buddyhang.adapters.UserAdapter;
import com.example.buddyhang.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * Handles followers/following
 */
public class FollowersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private String userId;
    private List<String> userIdList;
    private String option;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers);

        // to delete actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // recycler view with users
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userId = getIntent().getStringExtra("id");
        option = getIntent().getStringExtra("title");
        userList = new ArrayList<>();
        userAdapter = new UserAdapter(this , userList , false);
        recyclerView.setAdapter(userAdapter);
        userIdList = new ArrayList<>();

        // show either following or followers list
        if(option.equals("Following")){
            getFollowing();
        } else if(option.equals("Followers")){
            getFollowers();

        }
    }
    private void getFollowing() {
        FirebaseDatabase.getInstance().getReference("Follow").child(userId).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userIdList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    userIdList.add(snapshot.getKey());
                }
                displayUsersList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
    private void getFollowers() {
        FirebaseDatabase.getInstance().getReference("Follow").child(userId).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userIdList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    userIdList.add(snapshot.getKey());
                }
                displayUsersList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void displayUsersList () {
        FirebaseDatabase.getInstance().getReference("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (String id : userIdList){
                        if (user.getId().equals(id))
                            userList.add(user);
                    }
                }
                userAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}