package com.example.kamadayuji.smartglass_systemflow;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;


public class A08_1PatientBodyTempRegAndEdit extends AppCompatActivity {

    TextView mTv_D; //TextView_DatePicker
    Button mBtn_D;  //Button_DatePicker
    TextView mTv_T; //TextView_TimePicker
    Button mBtn_T;  //Button_Time_Picker
    EditText mEt_D;
    EditText mEt_T;

    Calendar c;
    DatePickerDialog dpd;
    TimePickerDialog tpd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity08_1_patient_body_temp_reg_and_edit);

//        mTv_D = (TextView) findViewById(R.id.Text08_1_DatePicker);
//        mBtn_D = (Button) findViewById(R.id.Button08_1_DatePicker);
//        mTv_T = (TextView) findViewById(R.id.Text08_1_TimePicker);
//        mBtn_T = (Button) findViewById(R.id.Button08_1_TimePicker);
//        mTv_D = (TextView) findViewById(R.id.text08Date);
//        mTv_T = (TextView) findViewById(R.id.text08Time);
        mEt_D = (EditText) findViewById(R.id.text08Date);
        mEt_T = (EditText) findViewById(R.id.text08Time) ;


        mEt_D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(A08_1PatientBodyTempRegAndEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datepicker, int mYear, int mMonth, int mDay) {
                        mEt_D.setText(mYear+"年"+mMonth+"月"+mDay+"日");
                    }
                }, day, month, year);
                dpd.show();

            }
        });

        mEt_T.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                tpd = new TimePickerDialog(A08_1PatientBodyTempRegAndEdit.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timepicker, int mHour, int mMinute) {
                        mEt_T.setText(mHour + "時" + mMinute+"分");
                    }
                }, hour, minute, true);
                tpd.show();

            }
        });
    }
}
