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

public class TranslateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText et_1;
    TextView txt, txt_lan_1, txt_lan_2;
    Translator englishVietnameseTranslator, vietnameseEnglishTranslator;
    ClipboardManager clipboard;
    ClipData clip;
    Dialog dialog;
    TranslatorOptions options_2;
    Boolean flag = true;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);
        et_1 = findViewById(R.id.et_1);
        txt = findViewById(R.id.txt);
        txt_lan_1 = findViewById(R.id.txt_lan_1);
        txt_lan_2 = findViewById(R.id.txt_lan_2);
        findViewById(R.id.swap).setOnClickListener(this);
        findViewById(R.id.mic).setOnClickListener(this);
        findViewById(R.id.cp_1).setOnClickListener(this);
        findViewById(R.id.cp_2).setOnClickListener(this);

        clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        dialog = new Dialog(TranslateActivity.this, android.R.style.Theme_Dialog);
        open_dialog();

        et_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (flag)
                    translate_vie(et_1.getText().toString());
                else translate_eng(et_1.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Create an English-Vietnamese translator:
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(TranslateLanguage.VIETNAMESE)
                        .build();

        options_2 =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.VIETNAMESE)
                        .setTargetLanguage(TranslateLanguage.ENGLISH)
                        .build();

        englishVietnameseTranslator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        englishVietnameseTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                download_vie_eng();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txt.setText(e.getMessage());
                            }
                        });
    }

    void download_vie_eng() {
        vietnameseEnglishTranslator = Translation.getClient(options_2);
        vietnameseEnglishTranslator.downloadModelIfNeeded()
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void v) {
                                txt.setText(null);
                                dialog.dismiss();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txt.setText(e.getMessage());
                            }
                        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.mic) {
            voice();
        } else if (id == R.id.swap) {
            swap();
        } else if (id == R.id.cp_1) {
            try {
                copy(et_1.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.cp_2) {
            try {
                copy(txt.getText().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void toast(String message, int type) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    void voice() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        if (flag)
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
        else intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "vi");
        try {
            startActivityForResult(intent, 200);
        } catch (ActivityNotFoundException a) {
            toast("Intent Problem", Toast.LENGTH_SHORT);
        }
    }

    public void open_dialog() {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setCancelable(false);
        dialog.show();
    }

    void swap() {
        String a = txt_lan_1.getText().toString();
        String b = txt_lan_2.getText().toString();
        a = a + b;
        b = a.substring(0, a.length() - b.length());
        a = a.substring(b.length());
        txt_lan_1.setText(a);
        txt_lan_2.setText(b);
        flag = !flag;
        et_1.setText(null);
        txt.setText(null);
        toast("Language Changed", Toast.LENGTH_SHORT);
    }

    void copy(String text) {
        if (!text.equals("")) {
            clip = ClipData.newPlainText("text", text);
            clipboard.setPrimaryClip(clip);
            toast("Text Copied", Toast.LENGTH_SHORT);
        } else {
            toast("There is no text", Toast.LENGTH_SHORT);
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
                et_1.setText(result.get(0));

                if (flag)
                    translate_vie(et_1.getText().toString().trim());
                else translate_eng(et_1.getText().toString().trim());
            }
        }
    }

    void translate_vie(String text) {
        englishVietnameseTranslator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                txt.setText(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txt.setText(e.getMessage());
                            }
                        });
    }

    void translate_eng(String text) {
        vietnameseEnglishTranslator.translate(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@NonNull String translatedText) {
                                txt.setText(translatedText);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                txt.setText(e.getMessage());
                            }
                        });
    }
}
