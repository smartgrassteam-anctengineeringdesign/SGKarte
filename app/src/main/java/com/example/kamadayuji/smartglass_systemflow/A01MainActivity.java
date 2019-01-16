package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.graphics.Color;



import org.opencv.android.OpenCVLoader;

public class A01MainActivity extends AppCompatActivity {

    //リスナー登録
    View.OnClickListener button01MoveQRcodeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        Intent intent = new Intent(getApplication(), A02QRCodeRecognitionActivity.class);
        startActivity(intent);
        }
    };
    View.OnClickListener button01MovePatientListOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A05PatientListActivity.class);
            startActivity(intent);
        }
    };
    View.OnClickListener button01MovePatientRegistrationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A04PatientRegistrationActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity01_main);
        //ボタンが押された時に発動
        findViewById(R.id.button01MoveQRcode).setOnClickListener(button01MoveQRcodeOnClickListener);
        findViewById(R.id.button01MovePatientList).setOnClickListener(button01MovePatientListOnClickListener);
        findViewById(R.id.button01MovePatientRegistration).setOnClickListener(button01MovePatientRegistrationOnClickListener);


//        Button button = (Button) findViewById(R.id.button01MoveQRcode);
//        button.setBackgroundColor(R.drawable.activity01_button_background);

        //opencv導入確認
        /*if(!OpenCVLoader.initDebug()){
            Log.i("OpenCV", "Failed");
        }else{
            Log.i("OpenCV", "successfully built !");
        }*/

    }

}
