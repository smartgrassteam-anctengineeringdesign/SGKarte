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
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;

// ToDo:年齢のedittextには整数しか入力できないようにする
// ToDo:性別はテキスト入力ではなく、選択制にする
// ToDo:IDの仕組みをしっかりする


public class A04PatientRegistrationActivity extends AppCompatActivity {

    private TextView mText04Id;   //Id primaryautocrementの最大値(table patientListより自動生成)+1の値を入れる

    private EditText mEditText04Name;           // 氏名
    private EditText mEditText04Age;            // 年齢
    private Spinner mSpinner04Sex;            // 性別
    private EditText mEditText04Affiliation;    // 所属
    private EditText mEditText04Detail;         // 詳細

    private TextView mText04Kome01;             // 氏名の※印
    private TextView mText04Kome02;             // 年齢の※印
    private TextView mText04Kome03;             // 性別の※印

    private Button mButton04MovePatientList;         //[患者一覧] 患者一覧へ遷移
    private Button mButton04PatientRegistration;     //[登録] DBへデータを登録
    private Button mButton04Clear;                   //[クリア] EditTextに入力されている文字をクリアする

    private Intent intent;


    //リスナ登録
    //[患者一覧]押下でpatientListActivityへ遷移
    View.OnClickListener button04MovePatientListOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A05PatientListActivity.class);
            startActivity(intent);
        }
    };

    //[登録]ボタン押下でデータベースに登録
    View.OnClickListener button04PatientRegistrationOnClickListener = new View.OnClickListener() {
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
    View.OnClickListener button04ClearOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //初期値処理
            init();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity04_patient_registration);

        //スピナー
        initSpinners();

        //各部品の結びつけ
        findViews();

        //最新Idの取得＆更新
        displayId();


        //初期値設定
        init();

        //リスナの設置
        mButton04MovePatientList.setOnClickListener(button04MovePatientListOnClickListener);
        mButton04PatientRegistration.setOnClickListener(button04PatientRegistrationOnClickListener);
        mButton04Clear.setOnClickListener(button04ClearOnClickListener);

    }

    //スピナー
    private void initSpinners() {
        Spinner Spinner04Sex = (Spinner)findViewById(R.id.spinner04Sex);
        String[] labels = getResources().getStringArray(R.array.PatientRegistrationActivity_sex_list);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
        Spinner04Sex.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }


    public void onItemSelected(AdapterView parent,View view, int position,long id) {
        String spinner=mSpinner04Sex.getSelectedItem().toString();

        Toast.makeText(getApplicationContext(), spinner ,
                Toast.LENGTH_SHORT).show();
    }

    // 何も選択されなかった時の動作
    public void onNothingSelected(AdapterView parent) {
    }


    private void findViews() {

        mText04Id = (TextView) findViewById(R.id.edit04Id);     //Id autoincrement

        mEditText04Name = (EditText) findViewById(R.id.editText04Name);   // 氏名
        mEditText04Age = (EditText) findViewById(R.id.editText04Age);     // 年齢
        mSpinner04Sex = (Spinner) findViewById(R.id.spinner04Sex);     // 性別
        mEditText04Affiliation = (EditText) findViewById(R.id.editText04Affiliation);       // 単価
        mEditText04Detail = (EditText) findViewById(R.id.editText04Detail);       // 単価


         mText04Kome01 = (TextView) findViewById(R.id.text04Kome01);             // 氏名の※印
         mText04Kome02 = (TextView) findViewById(R.id.text04Kome02);             // 年齢※印
         mText04Kome03 = (TextView) findViewById(R.id.text04Kome03);             // 性別の※印

        mButton04MovePatientList = (Button) findViewById(R.id.button04MovePatientList);           // 遷移
        mButton04PatientRegistration = (Button) findViewById(R.id.button04PatientRegistration);   // 登録
        mButton04Clear = (Button) findViewById(R.id.button04Clear);                               //  クリア
    }

    private void init() {
        mEditText04Name.setText("");
        mEditText04Age.setText("");

        mEditText04Affiliation.setText("");
        mEditText04Detail.setText("");

        mEditText04Name.requestFocus();     //フォーカスを氏名のEditTextに指定
    }




    private void saveList() {

        // 各EditTextで入力されたテキストを取得
        String strName = mEditText04Name.getText().toString();
        String strAge = mEditText04Age.getText().toString();
        String strSex = (String)mSpinner04Sex.getSelectedItem();
        String strAffiliation = mEditText04Affiliation.getText().toString();
        String strDetail = mEditText04Detail.getText().toString();

        //EditTextが空白の場合
        if (strName.equals("") || strAge.equals("") || strSex.equals("") ) {
            if (strName.equals("")) {
                mText04Kome01.setText("※");     // 氏名が空白の場合、※印を表示
            } else {
                mText04Kome01.setText("");      // 空白でない場合は※印を消す
            }

            if (strAge.equals("")) {
                mText04Kome02.setText("※");     // 年齢が空白の場合、※印を表示
            } else {
                mText04Kome02.setText("");      // 空白でない場合は※印を消す
            }

            if (strSex.equals("")) {
                mText04Kome03.setText("※");     // 性別が空白の場合、※印を表示
            } else {
                mText04Kome03.setText("");      // 空白でない場合は※印を消す
            }


            Toast.makeText(A04PatientRegistrationActivity.this, "※の箇所を入力して下さい。", Toast.LENGTH_SHORT).show();

        } else {        // EditTextが全て入力されている場合

            // 入力された単価と個数は文字列からint型へ変換
            int iAge = Integer.parseInt(strAge);

            // DBへの登録処理
            DBAdapterPatientList dbAdapterPatientList = new DBAdapterPatientList(this);
            dbAdapterPatientList.openDB();                                         // DBの読み書き
            dbAdapterPatientList.saveDB(strName, iAge, strSex, strAffiliation, strDetail);   // DBに登録
            dbAdapterPatientList.closeDB();                                        // DBを閉じる

            displayId();
            init();     // 初期値設定

        }
    }

    //Idの取得＆表示
    private void displayId() {

        DBAdapterSqliteSequence dbAdapter = new DBAdapterSqliteSequence(this);
        String[] columns = {"seq"};
        String[] name ={"patientList"};
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

        mText04Id.setText(String.valueOf(++getNumber));

        c.close();
        dbAdapter.closeDB();
    }








}
