package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        findViewById(R.id.MoveQRcodebutton).setOnClickListener(MoveQRcodeButtonOnClickListener);
        findViewById(R.id.MovePatientListButton).setOnClickListener(MovePatientListButtonOnClickListener);
    }

}
