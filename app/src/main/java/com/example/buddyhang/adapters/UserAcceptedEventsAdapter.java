package com.example.buddyhang.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buddyhang.R;
import com.example.buddyhang.models.Event;
import com.example.buddyhang.models.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.List;

public class UserAcceptedEventsAdapter extends RecyclerView.Adapter<UserAcceptedEventsAdapter.ViewHolder>{

    private Context context;
    private List<Event> eventList;

    public UserAcceptedEventsAdapter(Context context, List<Event> eventList) {
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_hosted_events, parent , false);
        return new UserAcceptedEventsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Event event = eventList.get(position);
        // setting description
        holder.eventDesc.setText(event.getEventDescription());
        // setting location
        holder.location.setText(event.getEventLocation());
        // setting event name
        holder.eventname.setText(event.getEventName());
        // setting event date
        holder.eventDate.setText(event.getEventDate());
        // setting the event host's information
        host(holder.eventHostPicture, holder.eventhost, event.getEventHost());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView eventname;
        public TextView eventhost;
        public ImageView eventHostPicture;
        public TextView eventDesc;
        public TextView location;
        public TextView eventDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventname = itemView.findViewById(R.id.eventname);
            eventhost = itemView.findViewById(R.id.eventhost);
            eventHostPicture = itemView.findViewById(R.id.eventHostPicture);
            eventDesc = itemView.findViewById(R.id.eventDesc);
            location = itemView.findViewById(R.id.location);
            eventDate = itemView.findViewById(R.id.eventDate);
        }
    }

    private void host (final ImageView eventHostPicture, final TextView eventhost , String userid) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                Picasso.get().load(user.getProfilePicture()).placeholder(R.drawable.ic_baseline_person_24).into(eventHostPicture);
                eventhost.setText(user.getName());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
