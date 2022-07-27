package com.example.buddyhang.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.buddyhang.R;
import com.example.buddyhang.models.PrivateEvent;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CalendarViewHolderAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public final TextView day;
    private final CalendarAdapter.OnItemListener onItemListener;

    public CalendarViewHolderAdapter(@NonNull View itemView, CalendarAdapter.OnItemListener onItemListener) {
        super(itemView);
        day = itemView.findViewById(R.id.calendarItemNumber);
        this.onItemListener = onItemListener;
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String dayString = (String) day.getText();
        onItemListener.onItemClick(getAdapterPosition(), dayString);
    }

}

