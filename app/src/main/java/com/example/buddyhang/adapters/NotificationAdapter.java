package com.example.buddyhang.adapters;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.buddyhang.R;
import com.example.buddyhang.models.Notification;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Notification> notifications;

    public NotificationAdapter(Context context, ArrayList<Notification> notifications) {
        this.context = context;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item,parent,false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification model = notifications.get(position);
        String timestamp = model.getTimestamp();
        String acceptId = model.getUserAcceptId();
        String eventName = model.getEventName();
        // rendering the eventName
        holder.event_name.setText("Accepted: " + eventName);
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String time = DateFormat.format("MM/dd/yyyy hh:mm aa",calendar).toString();
        // rendering the timestamp
        holder.notification_time.setText(time);
        // query for the name and profile image of the user who accepted host's event
        // Reference users; if the usersId is equal to acceptId, get the name of the user
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("id").equalTo(acceptId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    // rendering the userAcceptedName
                    String name = ""+ds.child("name").getValue();
                    holder.notification_name.setText(name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView notification_name;
        public TextView notification_time;
        public TextView event_name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            notification_name = itemView.findViewById(R.id.notification_name);
            notification_time = itemView.findViewById(R.id.notification_time);
            event_name = itemView.findViewById(R.id.event_name);

        }
    }
}