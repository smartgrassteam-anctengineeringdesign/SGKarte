package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    //リスナー登録
    View.OnClickListener MoveQRcodeButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent intent = new Intent(getApplication(), QRCodeRecognitionActivity.class);
        startActivity(intent);
        }
    };
    View.OnClickListener MovePatientListButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), PatientListActivity.class);
            startActivity(intent);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity01_main);
        //ボタンが押された時に発動
        findViewById(R.id.button01MoveQRcode).setOnClickListener(MoveQRcodeButtonOnClickListener);
        findViewById(R.id.button01MovePatientList).setOnClickListener(MovePatientListButtonOnClickListener);
    }

}
