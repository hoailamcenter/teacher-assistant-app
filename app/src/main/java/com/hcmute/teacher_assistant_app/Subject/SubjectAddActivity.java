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
public class SubjectAddActivity extends AppCompatActivity {
    private EditText txt_tenMH, txt_heSo, txt_namHoc;
    private CheckBox cb_HK1, cb_HK2;
    private AppCompatButton btn_save, btn_cancel;
    private boolean isError;
    private ImageView errorName, errorNH, errorHS, errorHK;
    private String regex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_add);

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
    }

    public void setEvent() {
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (errorName.getVisibility() == View.VISIBLE || txt_tenMH.getText().toString().isEmpty()) {
                    isError = true;
                    errorName.setVisibility(View.VISIBLE);
                    txt_tenMH.requestFocus();
                    Toast.makeText(getApplicationContext(), "Tên môn học không hợp lệ ", Toast.LENGTH_SHORT).show();
                    return;
                } else if (errorHK.getVisibility() == View.VISIBLE || cb_HK1.isChecked() == cb_HK2.isChecked()) {
                    isError = true;
                    errorHK.setVisibility(View.VISIBLE);
                    cb_HK1.requestFocus();
                    Toast.makeText(getApplicationContext(), "Vui lòng chọn học kì", Toast.LENGTH_SHORT).show();
                    return;
                } else if (errorHS.getVisibility() == View.VISIBLE || txt_heSo.getText().toString().isEmpty()) {
                    isError = true;
                    errorHS.setVisibility(View.VISIBLE);
                    txt_heSo.requestFocus();
                    Toast.makeText(getApplicationContext(), "Hệ số không đúng (1 hoặc 2)", Toast.LENGTH_SHORT).show();
                    return;
                } else if (errorNH.getVisibility() == View.VISIBLE || txt_namHoc.getText().toString().isEmpty()) {
                    isError = true;
                    errorNH.setVisibility(View.VISIBLE);
                    txt_namHoc.requestFocus();
                    Toast.makeText(getApplicationContext(), "Năm học không hợp lệ (2021-2022)", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isError) {
                    Subject newSubject = new Subject();

                    newSubject.setTenMH(txt_tenMH.getText().toString());
                    newSubject.setNamHoc(txt_namHoc.getText().toString());
                    newSubject.setHeSo(Integer.parseInt(txt_heSo.getText().toString()));

                    if (cb_HK1.isChecked() && !cb_HK2.isChecked()) newSubject.setHocKy(1);
                    else if (cb_HK2.isChecked() && !cb_HK1.isChecked()) newSubject.setHocKy(2);

                    SubjectActivity.getmInstanceActivity().addSubject(newSubject);
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
        //        VALIDATE NAME
        txt_tenMH.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Get the current text from the subject name EditText
                String tenMH = txt_tenMH.getText().toString();
                // Check if the name is empty or contains digits
                if (tenMH.isEmpty() || tenMH.matches(".*\\d.*")) {
                    isError = true;// Set error flag to true
                    errorName.setVisibility(View.VISIBLE);// Show error icon for name
                } else {
                    isError = false;// Set error flag to false
                    errorName.setVisibility(View.INVISIBLE);// Hide error icon for name
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //        VALIDATE HE SO
        txt_heSo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Get the current text from the coefficient EditText
                String heSo = txt_heSo.getText().toString();
                // Check if the coefficient is empty or greater than 2
                if (heSo.isEmpty() || Integer.parseInt(heSo) > 2) {
                    isError = true;
                    errorHS.setVisibility(View.VISIBLE); // Show error icon for coefficient
                } else {
                    isError = false;
                    errorHS.setVisibility(View.INVISIBLE); // Hide error icon for coefficient
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //        VALIDATE NAM HOC
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

//      VALIDATE COMBO BOX Hoc Ki
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
                // If both checkboxes are checked, uncheck the first one
                if (cb_HK1.isChecked() && cb_HK2.isChecked()) cb_HK1.setChecked(false);
                // If neither checkbox is checked, show error icon for semester
                if (!cb_HK1.isChecked() && !cb_HK2.isChecked()) {
                    errorHK.setVisibility(View.VISIBLE);
                    isError = true; // Set error flag to true
                } else {
                    errorHK.setVisibility(View.INVISIBLE);// Hide error icon for semester
                    isError = false; // Set error flag to false
                }
            }
        });
    }
}