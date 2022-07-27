package com.example.buddyhang.adapters;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buddyhang.R;
import com.example.buddyhang.models.PrivateEvent;
import com.example.buddyhang.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ramotion.foldingcell.FoldingCell;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    public Context context;
    public List<PrivateEvent> events;
    public int event_position;

    public EventAdapter(Context context, List<PrivateEvent> events) {
        this.context = context;
        this.events = events;
    }

    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        final String hostId = events.get(position).getEventHost();
        final String userAcceptId = currentUser.getUid();
        final String eventName = events.get(position).getEventName();
        final PrivateEvent event = events.get(position);
        // setting description
        holder.eventDesc.setText(event.getEventDescription());
        // setting location
        holder.location.setText(event.getEventLocation());
        // setting event name
        holder.eventname.setText(event.getEventName());
        // setting event date
        holder.eventDate.setText(event.getEventDate());
        // setting event time
        holder.eventTime.setText(event.getEventTime());

        // setting the event host's information
        host(holder.eventHostPicture, holder.eventhost, event.getEventHost());
        // when the accept button is clicked, it needs to display in the calendar
        holder.accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Accepts").child(currentUser.getUid()).child(event.getEventId()).setValue(true);
                // makes the event disappear in feed after click
                event_position = holder.getAdapterPosition();
                clear(event_position);

                // when the accept button is clicked, this sends a notification to the user
                addNotification(hostId,userAcceptId,eventName);

                // set the notification content
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_baseline_notification_important_24)
                        .setContentTitle("Accepted Event Notification")
                        .setContentText(userAcceptId + " has accepted your event")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                // show the notification
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
                // notificationId is a unique int for each notification that you must define
                notificationManager.notify(0, builder.build());
            }
        });

        holder.decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference().child("Declines").child(currentUser.getUid()).child(event.getEventId()).setValue(true);
                // makes the event disappear in feed after click
                event_position = holder.getAdapterPosition();
                clear(event_position);
            }
        });
        // get our folding cell

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView eventname;
        public TextView eventhost;
        public ImageView eventHostPicture;
        public TextView eventDesc;
        public TextView location;
        public TextView eventDate;
        public TextView eventTime;
        public Button accept_button;
        public Button decline_button;
        public FoldingCell fc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventname = itemView.findViewById(R.id.eventname);
            eventhost = itemView.findViewById(R.id.eventhost);
            eventHostPicture = itemView.findViewById(R.id.eventHostPicture);
            eventDesc = itemView.findViewById(R.id.eventDesc);
            location = itemView.findViewById(R.id.location);
            eventDate = itemView.findViewById(R.id.eventDate);
            accept_button = itemView.findViewById(R.id.accept_button);
            decline_button = itemView.findViewById(R.id.decline_button);
            eventTime = itemView.findViewById(R.id.eventTime);
            fc = (FoldingCell) itemView.findViewById(R.id.folding_cell);

            // attach click listener to folding cell
            fc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fc.toggle(false);
                }
            });

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
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item, parent , false);
        return new EventAdapter.ViewHolder(view);
    }


    public void clear (int event_position) {
        events.remove(event_position);
        notifyDataSetChanged();
    }


    private void addNotification(String hostId, String userAcceptId, String eventName) {
        String timestamp = "" + System.currentTimeMillis();
        HashMap<Object, String> hashMap = new HashMap<>();
        hashMap.put("hostId",hostId);
        hashMap.put("timestamp",timestamp);
        hashMap.put("userAcceptId",userAcceptId);
        hashMap.put("eventName",eventName);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(hostId).child("notifications").child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i("Notification", "Notification information saved on Firebase");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Notification", "Notification information not saved on Firebase");
                    }
                });
    }

}