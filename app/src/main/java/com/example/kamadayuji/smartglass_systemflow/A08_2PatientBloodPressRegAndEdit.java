package com.example.kamadayuji.smartglass_systemflow;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class A08_2PatientBloodPressRegAndEdit extends AppCompatActivity {

    private static String KEYWORD_PATIENT = "KEY_PATIENT";
    private List<Patient> patientItems;
    private Patient patient;

    //患者情報を表示する部品
    private TextView mText08_2Name;             // 氏名
    private TextView mText08_2Id;               // ID
    private TextView mText08_2Age;              // 年齢
    private TextView mText08_2Sex;              // 性別
    private TextView mText08_2Affiliation;      // 所属
    private TextView mText08_2Detail;           // 詳細

    //血圧データ入力に関する部品
    private TextView mText08_2BpId;
    private EditText mEditText08_2DateAndTime;
    private EditText mEditText08_2SystolicPress;
    private EditText mEditText08_2DiastolicPress;
    private EditText mEditText08_2Pulse;
    private EditText mEditText08_2Remarks;

    //入力を促す警告に使用する「※」
    private TextView mText08_2Kome01;             // 氏名の※印
    private TextView mText08_2Kome02;             // 年齢の※印
    private TextView mText08_2Kome03;             // 性別の※印
    private TextView mText08_2Kome04;

    //ボタン
    private Button mButton08_2MovePatientInspectionResult;          //[血圧＆体温情報一覧] 07患者血圧＆体温情報一覧へ遷移
    private Button mButton08_2BpReg;                                //[登録] DBへデータを登録
    private Button mButton08_2Clear;                                //[クリア] EditTextに入力されている文字をクリアする

    //リスナ登録
    //[患者一覧]押下で07PatientInspectionResultActivityへ遷移
    View.OnClickListener button08_2MovePatientInspectionResultOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A07PatientInspectionResultActivity.class);
            intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };

    //[登録]ボタン押下でデータベースに登録
    View.OnClickListener button08_2BpRegOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // キーボードを非表示
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);

            // DBに登録
            saveList();
        }
    };

    //[クリア]ボタンを押した時の処理
    View.OnClickListener button08_2ClearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //初期値処理
            init();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity08_2_patient_blood_press_reg_and_edit);

        //部品の結びつけ
        findViews();

        //前Activityにて選択した患者のIDを取得
        getSelectedPatientId();

        //取得した患者情報をset
        setPatientInfo();

        //最新Idの取得＆更新
        displayId();

        //初期値設定
        init();
    }


    private void findViews() {

        //患者の情報
        mText08_2Name = (TextView) findViewById(R.id.text08_1Name);
        mText08_2Id = (TextView) findViewById(R.id.text08_2Id);
        mText08_2Age = (TextView) findViewById(R.id.text08_2Age);
        mText08_2Sex = (TextView) findViewById(R.id.text08_2Sex);
        mText08_2Affiliation = (TextView) findViewById(R.id.text08_2Affiliation);
        mText08_2Detail = (TextView) findViewById(R.id.text08_2Detail);

        //血圧データ入力に関する部品
        mText08_2BpId = (TextView) findViewById(R.id.text08_2BpId);
        mEditText08_2DateAndTime = (EditText) findViewById(R.id.edit08_2DateAndTime);
        mEditText08_2SystolicPress = (EditText) findViewById(R.id.edit08_2SystolicPress);
        mEditText08_2DiastolicPress = (EditText) findViewById(R.id.edit08_2DiastolicPress);
        mEditText08_2Pulse = (EditText) findViewById(R.id.edit08_2Pulse);
        mEditText08_2Remarks = (EditText) findViewById(R.id.edit08_2Remarks);

        //※マーク
        mText08_2Kome01 = (TextView) findViewById(R.id.text08_2Kome01);             // 日時の※印
        mText08_2Kome02 = (TextView) findViewById(R.id.text08_2Kome02);             // 最高血圧※印
        mText08_2Kome03 = (TextView) findViewById(R.id.text08_2Kome03);             // 最低血圧の※印
        mText08_2Kome04 = (TextView) findViewById(R.id.text08_2Kome04);             // 脈拍の※印

        //ボタン
        mButton08_2MovePatientInspectionResult = (Button) findViewById(R.id.button08_2MovePatientInspectionResult);
        mButton08_2Clear = (Button) findViewById(R.id.button08_2Clear);
        mButton08_2BpReg = (Button) findViewById(R.id.button08_2BpReg);

        //リスナ登録
        mButton08_2MovePatientInspectionResult.setOnClickListener(button08_2MovePatientInspectionResultOnClickListener);
        mButton08_2Clear.setOnClickListener(button08_2ClearOnClickListener);
        mButton08_2BpReg.setOnClickListener(button08_2BpRegOnClickListener);
    }

    private void init() {
        mEditText08_2DateAndTime.setText("");
        mEditText08_2SystolicPress.setText("");
        mEditText08_2DiastolicPress.setText("");
        mEditText08_2Pulse.setText("");
        mEditText08_2Remarks.setText("");

        mEditText08_2DateAndTime.requestFocus();     //フォーカスを氏名のEditTextに指定
    }

    private void getSelectedPatientId(){

        patientItems = (ArrayList<Patient>)getIntent().getSerializableExtra(KEYWORD_PATIENT);
        patient = patientItems.get(0);
    }

    private void setPatientInfo(){
        mText08_2Name.setText(patient.getName());
        mText08_2Id.setText(String.valueOf(patient.getId()));
        mText08_2Age.setText(String.valueOf(patient.getAge()));
        mText08_2Sex.setText(patient.getSex());
        mText08_2Affiliation.setText(patient.getAffiliation());
        mText08_2Detail.setText(patient.getDetail());
    }

    private void saveList() {

        // 各EditTextで入力されたテキストを取得
        String strDateAndTime = mEditText08_2DateAndTime.getText().toString();
        String strSysPress = mEditText08_2SystolicPress.getText().toString();
        String strDisPress = mEditText08_2DiastolicPress.getText().toString();
        String strPulse = mEditText08_2Pulse.getText().toString();
        String strRemarks = mEditText08_2Remarks.getText().toString();

        //EditTextが空白の場合
        if (strDateAndTime.equals("") || strSysPress.equals("") || strDisPress.equals("") || strPulse.equals("")) {
            if (strDateAndTime.equals("")) {
                mText08_2Kome01.setText("※");     // 氏名が空白の場合、※印を表示
            } else {
                mText08_2Kome01.setText("");      // 空白でない場合は※印を消す
            }

            if (strSysPress.equals("")) {
                mText08_2Kome02.setText("※");     // 年齢が空白の場合、※印を表示
            } else {
                mText08_2Kome02.setText("");      // 空白でない場合は※印を消す
            }

            if (strDisPress.equals("")) {
                mText08_2Kome03.setText("※");     // 性別が空白の場合、※印を表示
            } else {
                mText08_2Kome03.setText("");      // 空白でない場合は※印を消す
            }

            if(strPulse.equals("")) {
                mText08_2Kome04.setText("※");
            } else {
                mText08_2Kome04.setText("");
            }

            Toast.makeText(A08_2PatientBloodPressRegAndEdit.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された単価と個数は文字列からint型へ変換
            int iSysPress = Integer.parseInt(strSysPress);
            int iDisPress = Integer.parseInt(strDisPress);
            int iPulse = Integer.parseInt(strPulse);

            //日付に関しては後で変更が必要
            int iDateAndTime = Integer.parseInt(strDateAndTime);

            //患者Idを取得
            String patientId = String.valueOf(patient.getId());

            // DBへの登録処理
            DBAdapterBloodPress dbAdapterBloodPress = new DBAdapterBloodPress(this,patientId);
            dbAdapterBloodPress.openDB();                                         // DBの読み書き
            dbAdapterBloodPress.saveDB(iDateAndTime, iSysPress, iDisPress, iPulse, strRemarks);   // DBに登録
            dbAdapterBloodPress.closeDB();                                        // DBを閉じる

            displayId();
            init();     // 初期値設定

        }
    }


    //Idの取得＆表示
    private void displayId() {

        //dbの中で自動で生成されるsqlite_sequenceテーブルにある各テーブルの要素の最大値を得る
        DBAdapterInspectionResultSqliteSequence dbAdapter = new DBAdapterInspectionResultSqliteSequence(this,String.valueOf(patient.getId()));
        String[] columns = {"seq"};
        String[] name ={"bodyPress"};
        String column = "name";

        int getNumber = 0;

        dbAdapter.openDB();
        Cursor c = dbAdapter.searchDB(columns,column,name);

        if (c.moveToFirst()) {
            do {
                getNumber = c.getInt(0);    //c.getInt(0);についてはif文の外でやるとエラーになる
                Log.d("取得したIDの最大値", String.valueOf(getNumber));
            } while (c.moveToNext());
        }

        mText08_2BpId.setText(String.valueOf(++getNumber));

        c.close();
        dbAdapter.closeDB();
    }
}
