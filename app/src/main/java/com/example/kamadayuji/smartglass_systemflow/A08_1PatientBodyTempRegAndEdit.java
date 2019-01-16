package com.example.kamadayuji.smartglass_systemflow;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class A08_1PatientBodyTempRegAndEdit extends AppCompatActivity {

    private static String KEYWORD_PATIENT = "KEY_PATIENT";
    private List<Patient> patientItems;
    private Patient patient;

    //患者情報を表示する部品
    private TextView mText08_1Name;             // 氏名
    private TextView mText08_1Id;               // ID
    private TextView mText08_1Age;              // 年齢
    private TextView mText08_1Sex;              // 性別
    private TextView mText08_1Affiliation;      // 所属
    private TextView mText08_1Detail;           // 詳細

    //体温データ入力に関する部品
    private TextView mText08_1BtId;
    private EditText mEditText08_1Date;
    private EditText mEditText08_1Time;
    private EditText mEditText08_1BodyTemp;
    private EditText mEditText08_1Remarks;

    //日付取得に関する部品
    DatePickerDialog dpd;
    TimePickerDialog tpd;

    //入力を促す警告に使用する「※」
    private TextView mText08_1Kome01;           // 日時の※印
    private TextView mText08_1Kome02;              //時刻の※印
    private TextView mText08_1Kome03;             // 体温の※印

    //ボタン
    private Button mButton08_1MovePatientInspectionResult;          //[血圧＆体温情報一覧] 07患者血圧＆体温情報一覧へ遷移
    private Button mButton08_1BtReg;                                //[登録] DBへデータを登録
    private Button mButton08_1Clear;                                //[クリア] EditTextに入力されている文字をクリアする

    //リスナ登録
    //[患者一覧]押下で07PatientInspectionResultActivityへ遷移
    View.OnClickListener button08_1MovePatientInspectionResultOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A07PatientInspectionResultActivity.class);
            intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };

    //[登録]ボタン押下でデータベースに登録
    View.OnClickListener button08_1BtRegOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // キーボードを非表示
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);


            // DBに登録
            try {
                saveList();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    };

    //[クリア]ボタンを押した時の処理
    View.OnClickListener button08_1ClearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //初期値処理
            init();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity08_1_patient_body_temp_reg_and_edit);

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

        //日付、時刻
        mEditText08_1Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar=Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH);
            int year = calendar.get(Calendar.YEAR);

            dpd = new DatePickerDialog(A08_1PatientBodyTempRegAndEdit.this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                    mEditText08_1Date.setText(mYear+"/"+(++mMonth)+"/"+mDay);
                }
            }, year, month, day);
                dpd.show();
            }
    });

        mEditText08_1Time.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            tpd = new TimePickerDialog(A08_1PatientBodyTempRegAndEdit.this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int mHour, int mMinute) {
                        mEditText08_1Time.setText(mHour + ":" + mMinute);
                    }
                }, hour, minute, true);
            tpd.show();
                }

        });
    }

    private void findViews() {
        //患者の情報
        mText08_1Name = (TextView) findViewById(R.id.text08_1Name);
        mText08_1Id = (TextView) findViewById(R.id.text08_1Id);
        mText08_1Age = (TextView) findViewById(R.id.text08_1Age);
        mText08_1Sex = (TextView) findViewById(R.id.text08_1Sex);
        mText08_1Affiliation = (TextView) findViewById(R.id.text08_1Affiliation);
        mText08_1Detail = (TextView) findViewById(R.id.text08_1Detail);

        //血圧データ入力に関する部品
        mText08_1BtId = (TextView) findViewById(R.id.text08_1BtId);
        mEditText08_1Date = (EditText) findViewById(R.id.edit08_1Date);
        mEditText08_1Time = (EditText) findViewById(R.id.edit08_1Time);
        mEditText08_1BodyTemp = (EditText) findViewById(R.id.edit08_1BodyTemp);
        mEditText08_1Remarks = (EditText) findViewById(R.id.edit08_1Remarks);

        //標準設定：日付と時刻の入力
       // @SuppressLint("WrongViewCast");//エラーをなかったことにするやつ？
            mEditText08_1Date.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);
            mEditText08_1Time.setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);


        //※マーク
        mText08_1Kome01 = (TextView) findViewById(R.id.text08_1Kome01);             // 日付の※印
        mText08_1Kome02 = (TextView) findViewById(R.id.text08_1Kome02);             // 時刻の※印
        mText08_1Kome03 = (TextView) findViewById(R.id.text08_1Kome03);             // 体温※印

        //ボタン
        mButton08_1MovePatientInspectionResult = (Button) findViewById(R.id.button08_1MovePatientInspectionResult);
        mButton08_1Clear = (Button) findViewById(R.id.button08_1Clear);
        mButton08_1BtReg = (Button) findViewById(R.id.button08_1BtReg);

        //リスナ登録
        mButton08_1MovePatientInspectionResult.setOnClickListener(button08_1MovePatientInspectionResultOnClickListener);
        mButton08_1Clear.setOnClickListener(button08_1ClearOnClickListener);
        mButton08_1BtReg.setOnClickListener(button08_1BtRegOnClickListener);
    }

    private void init() {
        mEditText08_1Date.setText("");
        mEditText08_1Time.setText("");
        mEditText08_1BodyTemp.setText("");
        mEditText08_1Remarks.setText("");

        mEditText08_1Date.requestFocus();
        mEditText08_1Time.requestFocus();       //フォーカスを氏名のEditTextに指定
    }

    private void getSelectedPatientId(){

        patientItems = (ArrayList<Patient>)getIntent().getSerializableExtra(KEYWORD_PATIENT);
        patient = patientItems.get(0);
    }

    private void setPatientInfo(){
        mText08_1Name.setText(patient.getName());
        mText08_1Id.setText(String.valueOf(patient.getId()));
        mText08_1Age.setText(String.valueOf(patient.getAge()));
        mText08_1Sex.setText(patient.getSex());
        mText08_1Affiliation.setText(patient.getAffiliation());
        mText08_1Detail.setText(patient.getDetail());
    }

    private void saveList() throws ParseException {

        // 各EditTextで入力されたテキストを取得
        String strDate = mEditText08_1Date.getText().toString();
        String strTime = mEditText08_1Time.getText().toString();
        String strBodyTemp = mEditText08_1BodyTemp.getText().toString();
        String strRemarks = mEditText08_1Remarks.getText().toString();

        //EditTextが空白の場合
        if (strDate.equals("") || strBodyTemp.equals("")) {
            if (strDate.equals("")) {
                mText08_1Kome01.setText("※");     // 日が空白の場合、※印を表示
            } else {
                mText08_1Kome01.setText("");      // 空白でない場合は※印を消す
            }

            if (strTime.equals("")) {
                mText08_1Kome02.setText("※");     // 時が空白の場合、※印を表示
            } else {
                mText08_1Kome02.setText("");      // 空白でない場合は※印を消す
            }

            if (strBodyTemp.equals("")) {
                mText08_1Kome03.setText("※");     // 体温が空白の場合、※印を表示
            } else {
                mText08_1Kome03.setText("");      // 空白でない場合は※印を消す
            }

            Toast.makeText(this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された単価と個数は文字列からint型へ変換
            int iBodyTemp = Integer.parseInt(strBodyTemp);

            //UnixTimeへ変換
            String dataAndTime;

            dataAndTime = strDate +" " +strTime+":00";
            Log.d("seireki",dataAndTime);

            SimpleDateFormat format = new SimpleDateFormat();
            format.applyPattern("yyyy/MM/dd HH:mm:ss");
            Date d = format.parse(dataAndTime);
            long intUnixTime = d.getTime()/1000;
            Date toUnixTime = new Date();
            Log.d("Tounixtime",String.valueOf(intUnixTime));
            toUnixTime.setTime(intUnixTime*1000);
            Log.d("setTime",toUnixTime.toString());


            //患者Idを取得
            String patientId = String.valueOf(patient.getId());

            // DBへの登録処理
            DBAdapterBodyTemp dbAdapterBodyTemp = new DBAdapterBodyTemp(this,patientId);
            dbAdapterBodyTemp.openDB();                                         // DBの読み書き
            dbAdapterBodyTemp.saveDB((int)intUnixTime, iBodyTemp, strRemarks);      // DBに登録
            dbAdapterBodyTemp.closeDB();                                        // DBを閉じる

            displayId();
            init();     // 初期値設定

        }
    }


    //Idの取得＆表示
    private void displayId() {

        //dbの中で自動で生成されるsqlite_sequenceテーブルにある各テーブルの要素の最大値を得る
        DBAdapterInspectionResultSqliteSequence dbAdapter = new DBAdapterInspectionResultSqliteSequence(this,String.valueOf(patient.getId()));
        String[] columns = {"seq"};
        String[] name ={"bodyTemp"};
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

        mText08_1BtId.setText(String.valueOf(++getNumber));

        c.close();
        dbAdapter.closeDB();
    }



}
