package com.hcmute.teacher_assistant_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import java.util.ArrayList;
import java.util.Locale;
//Nguyễn Hoài Lâm_21110778
public class TranslateActivity extends AppCompatActivity implements View.OnClickListener {
    // Declare UI components and translator objects
    EditText et_1;  // EditText for user input
    TextView txt, txt_lan_1, txt_lan_2;  // TextViews for displaying translated text and language labels
    Translator englishVietnameseTranslator, vietnameseEnglishTranslator;  // Translators for English-Vietnamese and vice versa
    ClipboardManager clipboard;  // Clipboard manager for copying text
    ClipData clip;  // ClipData for holding copied text
    Dialog dialog;  // Dialog for loading indicator
    TranslatorOptions options_2;  // Translator options for Vietnamese-English
    Boolean flag = true;  // Flag to switch between translation directions

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);  // Set the layout for the activity
        // Initialize UI components
        et_1 = findViewById(R.id.et_1);  // Find EditText by ID
        txt = findViewById(R.id.txt);  // Find TextView by ID
        txt_lan_1 = findViewById(R.id.txt_lan_1);  // Find first language label by ID
        txt_lan_2 = findViewById(R.id.txt_lan_2);  // Find second language label by ID
        // Set onClick listeners for buttons
        findViewById(R.id.swap).setOnClickListener(this);  // Swap button
        findViewById(R.id.mic).setOnClickListener(this);  // Microphone button
        findViewById(R.id.cp_1).setOnClickListener(this);  // Copy input text button
        findViewById(R.id.cp_2).setOnClickListener(this);  // Copy translated text button

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);  // Get clipboard service

        dialog = new Dialog(TranslateActivity.this, android.R.style.Theme_Dialog);  // Initialize dialog
        open_dialog();  // Show loading dialog

        // Add text change listener to EditText
        et_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Translate text based on flag
                if (flag)
                    translate_vie(et_1.getText().toString());  // Translate to Vietnamese
                else translate_eng(et_1.getText().toString());  // Translate to English
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed before text changes
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed after text changes
            }
        });

        // Create an English-Vietnamese translator
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.VIETNAMESE)
                        .build();

        // Create a Vietnamese-English translator
        options_2 =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.VIETNAMESE)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();

        englishVietnameseTranslator = Translation.getClient(options);  // Initialize English-Vietnamese translator

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        englishVietnameseTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                download_vie_eng();  // Download Vietnamese-English model on success
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txt.setText(e.getMessage());  // Show error message on failure
                            }
                        });
    }

    void download_vie_eng() {
        // Initialize and download the Vietnamese-English translator model
        vietnameseEnglishTranslator = Translation.getClient(options_2);
        vietnameseEnglishTranslator.downloadModelIfNeeded()
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                txt.setText(null);  // Clear text on success
                                dialog.dismiss();  // Dismiss loading dialog
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txt.setText(e.getMessage());  // Show error message on failure
                            }
                        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();  // Get the ID of the clicked view

        if (id == R.id.mic) {
            voice();  // Start voice recognition if mic button is clicked
        } else if (id == R.id.swap) {
            swap();  // Swap languages if swap button is clicked
        } else if (id == R.id.cp_1) {
            try {
                copy(et_1.getText().toString());  // Copy input text if copy button is clicked
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.cp_2) {
            try {
                copy(txt.getText().toString());  // Copy translated text if copy button is clicked
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void toast(String message, int type) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();  // Show a toast message
    }

    void voice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);  // Create intent for speech recognition
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);  // Set language model
        if (flag)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);  // Set language to English if flag is true
        else intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi");  // Set language to Vietnamese if flag is false
        try {
            startActivityForResult(intent, 200);  // Start activity for result with request code 200
        } catch (ActivityNotFoundException a) {
            toast("Intent Problem", Toast.LENGTH_SHORT);  // Show toast if activity is not found
        }
    }

    public void open_dialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);  // Remove title from dialog
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);  // Set background to transparent
        dialog.setContentView(R.layout.dialog_loading);  // Set dialog content view
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  // Hide soft input
        dialog.setCancelable(false);  // Make dialog non-cancelable
        dialog.show();  // Show the dialog
    }

    void swap() {
        // Swap the languages and reset the input and output texts
        String a = txt_lan_1.getText().toString();
        String b = txt_lan_2.getText().toString();
        a = a + b;
        b = a.substring(0, a.length() - b.length());
        a = a.substring(b.length());
        txt_lan_1.setText(a);
        txt_lan_2.setText(b);
        flag = !flag;  // Toggle the flag
        et_1.setText(null);  // Clear input text
        txt.setText(null);  // Clear translated text
        toast("Language Changed", Toast.LENGTH_SHORT);  // Show toast message
    }

    void copy(String text) {
        // Copy text to clipboard if not empty
        if (!text.equals("")) {
            clip = ClipData.newPlainText("text", text);
            clipboard.setPrimaryClip(clip);
            toast("Text Copied", Toast.LENGTH_SHORT);  // Show toast message
        } else {
            toast("There is no text", Toast.LENGTH_SHORT);  // Show toast message if text is empty
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                assert result != null;
                et_1.setText(result.get(0));  // Set recognized text to input field

                // Translate the recognized text based on the flag
                if (flag)
                    translate_vie(et_1.getText().toString().trim());
                else translate_eng(et_1.getText().toString().trim());
            }
        }
    }

    void translate_vie(String text) {
        // Translate English text to Vietnamese
        englishVietnameseTranslator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                txt.setText(translatedText);  // Set translated text to TextView
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txt.setText(e.getMessage());  // Show error message on failure
                            }
                        });
    }

    void translate_eng(String text) {
        // Translate Vietnamese text to English
        vietnameseEnglishTranslator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                txt.setText(translatedText);  // Set translated text to TextView
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txt.setText(e.getMessage());  // Show error message on failure
                            }
                        });
    }
}
