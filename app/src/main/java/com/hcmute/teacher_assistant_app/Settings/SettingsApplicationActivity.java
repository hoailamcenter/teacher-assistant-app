package com.hcmute.teacher_assistant_app.Settings;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.hcmute.teacher_assistant_app.R;

public class SettingsApplicationActivity extends AppCompatActivity {
    private SwitchCompat switchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_application);

        this.setControl();
        this.setEvent();
    }

    private void setControl() {
        switchButton = findViewById(R.id.darkModeSwitch);
    }

    private void setEvent() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switchButton.setChecked(true);
            setTheme(R.style.Theme_Teacherassistantapp_Night);
        } else {
            switchButton.setChecked(false);
            setTheme(R.style.Theme_Teacherassistantapp);
        }

        /*
         * MODE_NIGHT_NO = light mode
         * MODE_NIGHT_YES = dark mode
         * */
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switchButton.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });
    }
}