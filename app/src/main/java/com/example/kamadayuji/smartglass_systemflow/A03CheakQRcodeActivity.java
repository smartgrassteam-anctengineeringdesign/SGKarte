package com.example.kamadayuji.smartglass_systemflow;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class A03CheakQRcodeActivity extends AppCompatActivity {

    public static String qrRegIddata;
    private List<Patient> patientItems;
    protected Patient patient;

    private DBAdapterPatientList dbAdapterPatientList;

    //参照するDBのカラム：ID,氏名,年齢,性別
    private String[] columns = {"_id","name","age","sex","affiliation,detail"};

    private TextView textView;


    //リスナー登録　OKボタン　患者情報の詳細を表示する
    View.OnClickListener MovePatientInfoDetailButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A06PatientInfoDetailActivity.class);

            //患者情報を格納するリストを生成
            patientItems = new ArrayList<>();
            patientItems.add(patient);
            //患者情報をintentに入れる
            intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };

    //リスナー登録　QRコード際読取りボタン
    View.OnClickListener ReQRCodeRecognitionButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A02QRCodeRecognitionActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, A02QRCodeRecognitionActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity03_cheak_qrcode);

        findViews();

        getIntentDate();

        qrInfoRefDb();

    }


    private void findViews() {

        //qrから読み取ったデータを表示する要素
        textView = findViewById(R.id.textView);

        //ボタンが押された時に発動
        findViewById(R.id.ReQRCodeRecognitionButton).setOnClickListener(ReQRCodeRecognitionButtonOnClickListener);
        findViewById(R.id.MovePatientInfoDetailButton).setOnClickListener(MovePatientInfoDetailButtonOnClickListener);
    }


    private void getIntentDate() {
        Intent intent = getIntent();
        qrRegIddata = intent.getStringExtra("readQR");
        //textView.setText(qrRegIddata);
    }


        /**
         * DBを読み込む＆更新する処理
         * loadMyList()
         */

        private void qrInfoRefDb() {

            //検索する時に使う値
            String[] name ={qrRegIddata};
            //検索する列の名前
            String column = "_id";

            String getStringName = null;
            int getId = 0;
            String getSex=null;
            int getAge=0;
            String getAffiliation=null;
            String getDetail=null;

            //ArrayAdapterに対してListViewのリスト(items)の更新
            //items.clear();

            //DBAdapterのコンストラクタを呼び出し
            dbAdapterPatientList = new DBAdapterPatientList(this);

            dbAdapterPatientList.openDB();     //DBの読み込み

            //qrから読み取ったIDdateをpatientDbに探しに行く

            Cursor c = null;


            c = dbAdapterPatientList.searchDB(columns,column,name);


            if (c.moveToFirst()) {
                do {
                    getId = c.getInt(0);
                    getStringName = c.getString(1);//c.getInt(0);についてはif文の外でやるとエラーになる
                    getAge = c.getInt(2);
                    getSex = c.getString(3);
                    getAffiliation = c.getString(4);
                    getDetail = c.getString(5);

                    //Log.d("取得したIDの最大値", String.valueOf(getStringName));
                } while (c.moveToNext());
            }

            Log.d("DBからの読み取り終了", "狩猟");

            c.close();

            dbAdapterPatientList.closeDB();                    // DBを閉じる

            //patientを生成
            patient = new Patient(
                    getId,
                    getStringName,
                    getAge,
                    getSex,
                    getAffiliation,
                    getDetail);

            //読み取ったQRデータとDBから参照した名前を表示
            textView.setText("ID: "+qrRegIddata+" Name: "+ getStringName);

            //患者がないときの動作↓
            if(getStringName==null){
                textView.setText("患者データが見つかりませんでした。もう一度スキャンしてください。");
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("患者のデータが存在しません。");
                builder.setPositiveButton("了解", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //安全性の向上
                        Button button = (Button)findViewById(R.id.MovePatientInfoDetailButton);
                        button.setEnabled(false);


                    }
                });
                builder.show();
            }

        }
    }

