package com.hcmute.teacher_assistant_app.Event;
import com.hcmute.teacher_assistant_app.models.Event;
public interface OnEvent {
    void onEditEvent(Event event, int position);

    void onEventDelete(Event event, int position);
}

