package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CheakQRcodeActivity extends AppCompatActivity {

    //リスナー登録　OKボタン　患者情報の詳細を表示する
    View.OnClickListener MovePatientInfoDetailButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), PatientInfoDetailActivity.class);
            startActivity(intent);
        }
    };

    //リスナー登録　QRコード際読取りボタン
    View.OnClickListener ReQRCodeRecognitionButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), QRCodeRecognitionActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheak_qrcode);
        //ボタンが押された時に発動
        findViewById(R.id.ReQRCodeRecognitionButton).setOnClickListener(ReQRCodeRecognitionButtonOnClickListener);
        findViewById(R.id.MovePatientInfoDetailButton).setOnClickListener(MovePatientInfoDetailButtonOnClickListener);


    }

    //QRコードから読み取った情報を整理して表示する

}
