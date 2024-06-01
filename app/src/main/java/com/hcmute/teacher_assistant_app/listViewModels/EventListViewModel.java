package com.hcmute.teacher_assistant_app.listViewModels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.hcmute.teacher_assistant_app.Event.OnEvent;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.helpers.Alert;
import com.hcmute.teacher_assistant_app.models.Event;

import java.util.ArrayList;

public class EventListViewModel extends ArrayAdapter<Event> {
    private Context context;
    private int resource;
    private ArrayList<Event> events;
    private OnEvent onEvent;

    // Constructor
    public EventListViewModel(@NonNull Context context, int resource, @NonNull ArrayList<Event> events, OnEvent onEvent) {
        super(context, resource, events);
        this.context = context;
        this.resource = resource;
        this.events = events;
        this.onEvent = onEvent;
    }

    // Method to update an item in the list
    public void UpdateItem(Event ev, int Position) {
        events.set(Position, ev);
        notifyDataSetChanged();
    }

    // Method to delete an item from the list
    public void DeleteItem(Event ev, int Position) {
        events.remove(Position);
        notifyDataSetChanged();
    }

    // Returns the size of the list
    public int count() {
        return events.size();
    }

    @NonNull
    @Override
    public View getView(int position, @NonNull View convertView, @NonNull ViewGroup parent) {
        // Inflate the layout for each row of the ListView
        convertView = LayoutInflater.from(context).inflate(resource, null);

        // Find views in the layout
        TextView name = convertView.findViewById(R.id.eventName);
        TextView datetime = convertView.findViewById(R.id.eventDateTime);
        TextView place = convertView.findViewById(R.id.eventPlace);
        ImageView btn_edit = convertView.findViewById(R.id.btn_edit);
        ImageView btn_delete = convertView.findViewById(R.id.btn_delete);

        // Get the Event object at the specified position
        Event event = events.get(position);

        // Extract data from the Event object
        String eventName = event.getNameEvent();
        String eventDateTime = event.getStartTime() + "-" + event.getEndTime() + " " + event.getDay();
        String eventPlace = event.getPlace();

        // Set data to the views
        name.setText(eventName);
        datetime.setText(eventDateTime);
        place.setText(eventPlace);

        // Set click listener for the edit button
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEvent.onEditEvent(event, position);
            }
        });

        // Alert for deletion
        Alert alert = new Alert(context);
        alert.confirm();

        // Set click listener for the delete button
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.showAlert("Bạn có chắc chắn xoá không?", R.drawable.info_icon);
            }
        });

        // Handle delete confirmation
        alert.btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEvent.onEventDelete(event, position);
                alert.dismiss();
            }
        });

        alert.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.dismiss();
            }
        });

        return convertView;
    }
}
