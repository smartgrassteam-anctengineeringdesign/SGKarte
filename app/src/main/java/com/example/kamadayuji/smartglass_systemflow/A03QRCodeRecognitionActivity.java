package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class A03QRCodeRecognitionActivity extends AppCompatActivity {

    //ボタンのリスナー登録
    View.OnClickListener button02MoveAfterQrRecognitonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A02CheakQRcodeActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity02_qrcode_recognition);


        
        //ボタンが押された時に発動
        findViewById(R.id.MoveAfterQrRecognitonButton).setOnClickListener(button02MoveAfterQrRecognitonOnClickListener);
    }

    //以下、QRcode読取りに関するプログラムを書く

}
