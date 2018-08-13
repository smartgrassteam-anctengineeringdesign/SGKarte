package com.example.kamadayuji.smartglass_systemflow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

public class PatientRegistrationActivity extends AppCompatActivity {

    private EditText mEditText04Name;           // 氏名
    private EditText mEditText04Age;            // 年齢
    private EditText mEditText04Affiliation;    // 所属
    private EditText mEditText04Detail;         // 詳細

    private TextView mText04Kome01;             // 氏名の※印
    private TextView mText04Kome02;             // 年齢の※印
    private TextView mText04Kome03;             // 個数の※印
    private TextView mText04Kome04;             // 単価の※印

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity04_patient_registration);
    }
}
