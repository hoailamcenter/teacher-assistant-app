package com.hcmute.teacher_assistant_app.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.models.Event;


public class EventActivityCreation extends AppCompatActivity {
    private Button saveEvent; // Button to save the event
    private Button troVeBT; // Button to go back without saving
    private EditText eventName, startEV, endEV, Place, Date; // EditText fields for event details


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creation);
        // Initialize UI controls
        this.setControl();
        // Set up event listeners
        this.setEvent();
    }

    // Set up event listeners
    private void setEvent() {
        // Listener for the save button
        saveEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve input values
                String EVname = eventName.getText().toString();
                String EVstart = startEV.getText().toString();
                String EVend = endEV.getText().toString();
                String EVdate = Date.getText().toString();
                String EVPlace = Place.getText().toString();
                // Create a new Event object
                Event event = new Event();
                event.setNameEvent(EVname);
                event.setStartTime(EVstart);
                event.setEndTime(EVend);
                event.setDay(EVdate);
                event.setPlace(EVPlace);
                // Add the event to the main activity's event list
                EventActivity.weakReference.get().AddEvent(event);
                // Return to the previous activity with a result
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        // Listener for the go back button
        troVeBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the activity without saving
                finish();
                Toast.makeText(EventActivityCreation.this, "Da Thoat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Initialize UI controls
    private void setControl() {
        saveEvent = findViewById(R.id.eventCreationButtonConfirm);
        troVeBT = findViewById(R.id.eventCreationButtonGoBack);
        eventName = findViewById(R.id.eventNameAdd);
        startEV = findViewById(R.id.StartEV);
        endEV = findViewById(R.id.endEV);
        Date = findViewById(R.id.dateEV);
        Place = findViewById(R.id.eventPlaceAdd);
    }
}