package com.example.buddyhang.adapters;

import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buddyhang.models.PublicEvent;
import com.example.buddyhang.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PublicEventViewHolderAdapter extends RecyclerView.Adapter<PublicEventViewHolderAdapter.MyViewHolder> {

    private Context context;
    private List<PublicEvent> events;

    public PublicEventViewHolderAdapter(Context context, List<PublicEvent> events) {
        this.context = context;
        this.events = events;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.ticketmaster_event_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.text_title.setText(events.get(position).getName());
        holder.text_source.setText(events.get(position).getUrl());
        // loading the image into the imageview
        Picasso.get().load(events.get(position).getImageUrl()).into(holder.event_image);

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView text_source;
        TextView text_title;
        ImageView event_image;



        public MyViewHolder(View itemView) {
            super(itemView);

            text_source = itemView.findViewById(R.id.text_source);
            text_title = itemView.findViewById(R.id.text_title);
            text_source.setMovementMethod(LinkMovementMethod.getInstance());
            event_image = itemView.findViewById(R.id.event_image);


        }
    }

}
