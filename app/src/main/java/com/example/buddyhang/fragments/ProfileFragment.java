package com.example.buddyhang.fragments;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buddyhang.FollowersActivity;
import com.example.buddyhang.FriendsActivity;
import com.example.buddyhang.LaunchActivity;
import com.example.buddyhang.R;
import com.example.buddyhang.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
/**
 * Displays user information
 * who they follow
 * bio, profile picture, etc.
 */
public class ProfileFragment extends Fragment {

    private Button add_friends;
    private ImageView image_profile;
    private Button logout;
    private TextView followers;
    private TextView following;
    private TextView name;
    private EditText bio;
    private TextView username;
    private Button updateButton;

    private FirebaseUser firebaseUser;
    String id;

    public ProfileFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);


        image_profile = view.findViewById(R.id.image_profile);
        logout = view.findViewById(R.id.logout);
        followers = view.findViewById(R.id.followers);
        following = view.findViewById(R.id.following);
        name = view.findViewById(R.id.name);
        bio = view.findViewById(R.id.bio);
        username = view.findViewById(R.id.username);
        updateButton = view.findViewById(R.id.updateButton);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        id = firebaseUser.getUid();

        userInfo();
        getFollowers();

        add_friends = view.findViewById(R.id.add_friends);
        add_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), FriendsActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);
            }
        });

        // for logging out the user
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), LaunchActivity.class);
                startActivity(i);
                getActivity().overridePendingTransition(0, 0);

            }
        });


        followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext() , FollowersActivity.class);
                intent.putExtra("id" , id);
                intent.putExtra("title" , "Followers");
                startActivity(intent);
            }
        });

        following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FollowersActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("title", "Following");
                startActivity(intent);
            }
        });

        return view;
    }


    private void userInfo() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(id);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }

                User user = dataSnapshot.getValue(User.class);

                Picasso.get().load(user.getProfilePicture()).placeholder(R.drawable.ic_baseline_person_24).into(image_profile);
                username.setText(user.getUsername());
                name.setText(user.getName());

                // updating user bio
                if (user.getBio() == null) {
                    bio.setText("");
                } else {
                    update();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // updated the bio
    private void update() {
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

                HashMap<String , Object> hashMap = new HashMap<>();
                hashMap.put("bio" , bio.toString());
                reference.updateChildren(hashMap);
                Toast.makeText(getContext(), "Bio has been updated", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void getFollowers () {
        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("followers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followers.setText("" + dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        FirebaseDatabase.getInstance().getReference().child("Follow").child(id).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                following.setText("" + dataSnapshot.getChildrenCount());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
