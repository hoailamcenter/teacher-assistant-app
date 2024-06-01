package com.hcmute.teacher_assistant_app.helpers;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class InterfaceHelper {

    // Method to blur the current screen behind a PopupWindow
    public static void blurCurrentScreen(PopupWindow popupWindow) {
        // Get the root view of the PopupWindow's content view
        View container = popupWindow.getContentView().getRootView();
        // Get the context of the PopupWindow's content view
        Context context = popupWindow.getContentView().getContext();
        // Get the WindowManager service
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        // Get the layout parameters of the container view
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        // Set the flag to dim the background behind the PopupWindow
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        // Set the amount of dimming (0.0f to 1.0f)
        p.dimAmount = 0.3f; // Adjust the dimAmount as needed
        // Update the layout parameters to apply the changes
        wm.updateViewLayout(container, p);
    }
}
