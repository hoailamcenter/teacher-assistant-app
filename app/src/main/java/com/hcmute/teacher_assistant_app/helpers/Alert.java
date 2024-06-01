package com.hcmute.teacher_assistant_app.helpers;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hcmute.teacher_assistant_app.R;

public class Alert {
    private Dialog dialog;
    private Context context;
    private TextView msgText;
    private ImageView iconAlert;
    public Button btnOK;
    public Button btnCancel;

    // Constructor
    public Alert(Context context) {
        this.context = context;
    }

    // Method to create a normal alert dialog
    public void normal() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.normal_alert);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);

        setControl();
    }

    // Method to create a confirmation alert dialog
    public void confirm() {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.confirm_alert);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(true);
        setControl();
    }

    // Method to set controls (views) for the dialog
    private void setControl() {
        msgText = dialog.findViewById(R.id.msgText);
        iconAlert = dialog.findViewById(R.id.iconAlert);
        btnOK = dialog.findViewById(R.id.btnOK);
        btnCancel = dialog.findViewById(R.id.btnCancel);
    }

    // Method to show an alert dialog with a given title and icon
    public void showAlert(String title, Integer icon) {
        msgText.setText(title);
        iconAlert.setBackgroundResource(icon);
        dialog.show();
    }

    // Method to show an alert dialog with a title from resources and an icon
    public void showAlert(Integer resid, Integer icon) {
        String title = context.getResources().getString(resid);
        msgText.setText(title);
        iconAlert.setBackgroundResource(icon);
        dialog.show();
    }

    // Method to dismiss the alert dialog
    public void dismiss() {
        // Dismiss this dialog, removing it from the screen.
        dialog.dismiss();
    }
}
