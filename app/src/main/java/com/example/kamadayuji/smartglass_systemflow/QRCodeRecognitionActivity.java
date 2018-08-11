package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class QRCodeRecognitionActivity extends AppCompatActivity {

    //ボタンのリスナー登録
    View.OnClickListener MoveAfterQrRecognitonButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), CheakQRcodeActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_recognition);

        //ボタンが押された時に発動
        findViewById(R.id.MoveAfterQrRecognitonButton).setOnClickListener(MoveAfterQrRecognitonButtonOnClickListener);

    }

    //以下、QRcode読取りに関するプログラムを書く

}
