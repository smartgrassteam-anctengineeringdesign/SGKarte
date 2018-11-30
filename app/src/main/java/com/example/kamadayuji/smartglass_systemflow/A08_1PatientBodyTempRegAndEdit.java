package com.example.kamadayuji.smartglass_systemflow;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.widget.EditText;

public class A08_1PatientBodyTempRegAndEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity08_1_patient_body_temp_reg_and_edit);

        //日付入力制限
        @SuppressLint("WrongViewCast")//エラーをなかったことにするやつ？
        EditText et = (EditText)findViewById(R.id.edit08DateAndTime);
        //標準設定：日付と時刻の入力
        et.setInputType( InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);




    }
}
