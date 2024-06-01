package com.hcmute.teacher_assistant_app.Event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.hcmute.teacher_assistant_app.DB.EventDBHelper;
import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.listViewModels.EventListViewModel;
import com.hcmute.teacher_assistant_app.models.Event;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class EventActivity extends AppCompatActivity implements OnEvent {
    public static WeakReference<EventActivity> weakReference;
    private ListView listViewEvent;
    private ArrayList<Event> events;
    private EventListViewModel listViewModel;
    private SearchView EventSearch;
    private Button btn_addEvent;
    private int PositionUpdate = -1;
    private EventDBHelper db = new EventDBHelper(EventActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to the activity_event layout
        setContentView(R.layout.activity_event);
        // Create a weak reference to this activity
        weakReference = new WeakReference<>(EventActivity.this);
        // Recreate the events table and populate it with default events
        db.deletedAndCreateTable();
        // Retrieve all events from the database
        events = db.getAllEvents();
        // Initialize UI controls
        setControl();
        // Set up event listeners
        setEvent();
        // Initialize search functionality
        inItSearchWidgets();
    }

    // Initialize UI controls
    private void setControl() {
        listViewEvent = findViewById(R.id.eventListView);
        EventSearch = findViewById(R.id.eventSearchView);
        btn_addEvent = (Button) findViewById(R.id.eventButtonCreation);
    }

    // Set up event listeners
    private void setEvent() {
        // Create the list view model and set the adapter for the list view
        listViewModel = new EventListViewModel(this, R.layout.activity_event_element, events, this);
        listViewEvent.setAdapter(listViewModel);
        // Set the click listener for the add event button
        btn_addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the event creation activity when the button is clicked
                Intent intent = new Intent(EventActivity.this, EventActivityCreation.class);
                startActivity(intent);
            }
        });
    }

    // Initialize search functionality
    private void inItSearchWidgets() {
        EventSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                // Do nothing on query text submit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                // Filter events based on search query
                ArrayList<Event> filteredEvent = new ArrayList<>();

                for (Event evt : events) {
                    if (evt.getNameEvent().toLowerCase().trim().contains(s.toLowerCase().trim())) {
                        filteredEvent.add(evt);
                    }
                }
                // Update the list view with the filtered events
                setFilteredEvent(filteredEvent);
                return false;
            }
        });
    }

    // Update the list view with the filtered events
    private void setFilteredEvent(ArrayList<Event> filtered) {
        EventListViewModel eventModel = new EventListViewModel(this, R.layout.activity_event_element, filtered, this);
        listViewEvent.setAdapter(eventModel);
    }

    // Add a new event to the database and update the list view
    public void AddEvent(Event event) {
        if (db.AddEvent(event)) {
            events.add(event);
            listViewModel.notifyDataSetChanged();
            Toast.makeText(this, "Them thanh cong", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Them that bai", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEditEvent(Event event, int position) {
        PositionUpdate = position;
        Intent intent = new Intent(this, EventActivityUpdate.class);
        intent.putExtra("EV", event);
        startActivityForResult(intent, 100);
    }

    @Override
    public void onEventDelete(Event event, int position) {
        db.DeleteEvent(event);
        listViewModel.DeleteItem(event, position);
    }

    @Override
    // handle the result returned by the child activity.
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Event event = (Event) data.getSerializableExtra("Evupdate");
                listViewModel.UpdateItem(event, PositionUpdate);
            }
        }
    }
}