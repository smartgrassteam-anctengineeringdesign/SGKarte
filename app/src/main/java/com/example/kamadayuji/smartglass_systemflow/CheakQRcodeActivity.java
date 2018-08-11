package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CheakQRcodeActivity extends AppCompatActivity {

    //リスナー登録
    /*View.OnClickListener MoveQRcodeButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), QRCodeRecognitionActivity.class);
            startActivity(intent);
        }
    };
    */
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

        findViewById(R.id.ReQRCodeRecognitionButton).setOnClickListener(ReQRCodeRecognitionButtonOnClickListener);

    }

    //QRコードから読み取った情報を整理して表示する

}
