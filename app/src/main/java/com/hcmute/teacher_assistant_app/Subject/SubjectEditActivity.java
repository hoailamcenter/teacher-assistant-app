package com.hcmute.teacher_assistant_app.Subject;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.hcmute.teacher_assistant_app.R;
import com.hcmute.teacher_assistant_app.models.Subject;

//Nguyễn Ngọc Minh_21110784
public class SubjectEditActivity extends AppCompatActivity {
    private EditText txt_tenMH, txt_heSo, txt_namHoc;
    private CheckBox cb_HK1, cb_HK2;
    private AppCompatButton btn_save, btn_cancel;
    private boolean isError;
    private ImageView errorName, errorNH, errorHS, errorHK;
    private Subject subject;
    private String regex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_edit);

        this.setControl();
        this.Validate();
        this.setEvent();
    }

    public void setControl() {
        this.regex = "\\d{4}-\\d{4}";
        this.errorName = findViewById(R.id.errorName);
        this.errorNH = findViewById(R.id.errorNH);
        this.errorHS = findViewById(R.id.errorHS);
        this.errorHK = findViewById(R.id.errorHK);

        this.txt_tenMH = findViewById(R.id.txt_tenMH);
        this.txt_heSo = findViewById(R.id.txt_HeSo);
        this.txt_namHoc = findViewById(R.id.txt_namHoc);
        this.cb_HK1 = findViewById(R.id.cb_HK1);
        this.cb_HK2 = findViewById(R.id.cb_HK2);
        this.btn_save = findViewById(R.id.Btn_save);
        this.btn_cancel = findViewById(R.id.Btn_cancel);

        this.subject = (Subject) getIntent().getSerializableExtra("Subject");
        this.txt_tenMH.setText(subject.getTenMH());
        this.txt_namHoc.setText(subject.getNamHoc());
        this.txt_heSo.setText(String.valueOf(subject.getHeSo()));

        if (subject.getHocKy() == 1) cb_HK1.setChecked(true);
        if (subject.getHocKy() == 2) cb_HK2.setChecked(true);
    }

    public void setEvent() {
        // BUTTON
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorName.getVisibility() == View.VISIBLE) {
                    txt_tenMH.requestFocus();
                    Toast.makeText(getApplicationContext(), "Tên môn học không hợp lệ ", Toast.LENGTH_SHORT).show();
                } else if (errorHK.getVisibility() == View.VISIBLE) {
                    cb_HK1.requestFocus();
                    Toast.makeText(getApplicationContext(), "Hệ số không đúng (1 hoặc 2)", Toast.LENGTH_SHORT).show();
                } else if (errorHS.getVisibility() == View.VISIBLE) {
                    txt_heSo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Hệ số không đúng (1 hoặc 2)", Toast.LENGTH_SHORT).show();
                } else if (errorNH.getVisibility() == View.VISIBLE) {
                    txt_namHoc.requestFocus();
                    Toast.makeText(getApplicationContext(), "Năm học không hợp lệ (2021-2022)", Toast.LENGTH_SHORT).show();
                } else if (!isError) {
                    Subject fixedSubject = new Subject();
                    fixedSubject.setMaMH(subject.getMaMH());
                    fixedSubject.setTenMH(txt_tenMH.getText().toString());
                    fixedSubject.setNamHoc(txt_namHoc.getText().toString());
                    fixedSubject.setHeSo(Integer.parseInt(txt_heSo.getText().toString()));

                    if (cb_HK1.isChecked() && !cb_HK2.isChecked()) fixedSubject.setHocKy(1);
                    else if (cb_HK2.isChecked() && !cb_HK1.isChecked()) fixedSubject.setHocKy(2);

                    SubjectActivity.getmInstanceActivity().updateSubject(fixedSubject);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Validate() {
        // VALIDATE NAME
        txt_tenMH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tenMH = txt_tenMH.getText().toString();
                if (tenMH.isEmpty() || tenMH.matches(".*\\d.*")) {
                    isError = true;
                    errorName.setVisibility(View.VISIBLE);
                } else {
                    isError = false;
                    errorName.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // VALIDATE HE SO
        txt_heSo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String heSo = txt_heSo.getText().toString();
                if (heSo.isEmpty() || Integer.parseInt(heSo) > 2) {
                    isError = true;
                    errorHS.setVisibility(View.VISIBLE);
                } else {
                    isError = false;
                    errorHS.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // VALIDATE NAM HOC
        txt_namHoc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String namHoc = txt_namHoc.getText().toString();

                if (namHoc.matches(regex) && namHoc.split("-")[0] != namHoc.split("-")[1]) {
                    errorNH.setVisibility(View.INVISIBLE);
                    isError = false;
                } else {
                    errorNH.setVisibility(View.VISIBLE);
                    isError = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // VALIDATE COMBO BOX Hoc Ki
        cb_HK1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_HK2.isChecked() && cb_HK1.isChecked()) cb_HK2.setChecked(false);

                if (!cb_HK1.isChecked() && !cb_HK2.isChecked()) {
                    errorHK.setVisibility(View.VISIBLE);
                    isError = true;
                } else {
                    errorHK.setVisibility(View.INVISIBLE);
                    isError = false;
                }
            }
        });

        cb_HK2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_HK1.isChecked() && cb_HK2.isChecked()) cb_HK1.setChecked(false);

                if (!cb_HK1.isChecked() && !cb_HK2.isChecked()) {
                    errorHK.setVisibility(View.VISIBLE);
                    isError = true;
                } else {
                    errorHK.setVisibility(View.INVISIBLE);
                    isError = false;
                }
            }
        });
    }
}