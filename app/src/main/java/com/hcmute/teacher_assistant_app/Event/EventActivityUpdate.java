package com.hcmute.teacher_assistant_app.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmute.teacher_assistant_app.DB.EventDBHelper;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.models.Event;


public class EventActivityUpdate extends AppCompatActivity {
    private Event event; // Event object to be updated
    private EditText NameEv, StartEv, EndEV, DateEv, PlaceEV; // EditText fields for event details
    private Button ConfirmUpdate; // Button to confirm the update
    private EventDBHelper eventDBHelper; // Database helper


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);
        // Retrieve the event object passed from the previous activity
        event = (Event) getIntent().getSerializableExtra("EV");
        // Initialize the database helper
        eventDBHelper = new EventDBHelper(this);
        // Initialize UI controls
        this.setControl();
        // Set up event listeners
        this.setEvent();
    }
    // Set up event listeners
    private void setEvent() {
        this.ConfirmUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Update the event object with new values from input fields
                event.setNameEvent(NameEv.getText().toString().trim());
                event.setStartTime(StartEv.getText().toString());
                event.setEndTime(EndEV.getText().toString());
                event.setDay(DateEv.getText().toString());
                event.setPlace(PlaceEV.getText().toString());
                // Update the event in the database
                if (eventDBHelper.UpdateEvent(event)) {
                    Toast.makeText(EventActivityUpdate.this, "Cap nhat thanh cong", Toast.LENGTH_SHORT).show();
                    // Create an intent to pass the updated event back to the previous activity
                    Intent intent = new Intent();
                    intent.putExtra("Evupdate", event);
                    // Set the result and finish the activity
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(EventActivityUpdate.this, "Cap nhat that bai", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Initialize UI controls and populate fields with current event details
    private void setControl() {
        this.NameEv = findViewById(R.id.eventNameupdate);
        this.StartEv = findViewById(R.id.StartEVupdate);
        this.EndEV = findViewById(R.id.endEVupdate);
        this.DateEv = findViewById(R.id.dateEVupdate);
        this.PlaceEV = findViewById(R.id.eventPlaceupdate);
        this.ConfirmUpdate = findViewById(R.id.eventUpdateButtonConfirm);
        // Populate fields with current event details
        this.NameEv.setText(event.getNameEvent());
        this.StartEv.setText(event.getStartTime());
        this.EndEV.setText(event.getEndTime());
        this.DateEv.setText(event.getDay());
        this.PlaceEV.setText(event.getPlace());
    }
}
