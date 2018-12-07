package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import java.util.concurrent.CountDownLatch;


public class A02QRCodeRecognitionActivity extends AppCompatActivity {

    private static int recognitionMode = 0x00;

    private final CountDownLatch countDownLatch = new CountDownLatch(1);

    //ボタンのリスナー登録
    View.OnClickListener button02MoveAfterQrRecognitonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A03CheakQRcodeActivity.class);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity02_qrcode_recognition);
        //QRCodeRecognitionActivity.recognitionMode = getIntent().getIntExtra(Const.IntentExtraKey.RECOGNITION_MODE, 0x00);
        //Log.d("モード", String.valueOf(QRCodeRecognitionActivity.recognitionMode));

        //ボタンが押された時に発動
        findViewById(R.id.MoveAfterQrRecognitonButton).setOnClickListener(button02MoveAfterQrRecognitonOnClickListener);

        new IntentIntegrator(this).initiateScan();




    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, A01MainActivity.class);
        startActivity(intent);
    }


    //以下、QRcode読取りに関するプログラムを書く
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null && resultCode==RESULT_OK) {
            //Log.d("readQR", result.getContents());
            Intent intent = new Intent(this,A03CheakQRcodeActivity.class);
            intent.putExtra("readQR",result.getContents());
            TextView textView = findViewById(R.id.textView2);
            textView.setText(result.getContents());
            startActivity(intent);

        } else {
            Intent getMainScreen = new Intent(this, A01MainActivity.class);//pentru test, de sters
            startActivity(getMainScreen);
           // Intent Backintent = new Intent(this, A01MainActivity.class);
           // startActivity(Backintent);
            //super.onActivityResult(requestCode, resultCode, data);
        }

    }



}







