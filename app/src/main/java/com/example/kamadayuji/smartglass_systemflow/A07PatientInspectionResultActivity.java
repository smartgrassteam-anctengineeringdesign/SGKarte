package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class A07PatientInspectionResultActivity extends AppCompatActivity {

    private static String KEYWORD_PATIENT = "KEY_PATIENT";
    private List<Patient> patientItems;
    private Patient patient;

    private TextView mText07Name;
    private TextView mText07Id;
    private TextView mText07Age;
    private TextView mText07Sex;
    private TextView mText07Affiliation;
    private TextView mText07Detail;

    private Button mButton07MoveBloodPressReg;
    private Button mButton07MoveBodyTempReg;

    private ListView mListView07BloodPressure;
    private ListView mListView07BodyTemp;

    //リスナ登録
    View.OnClickListener button07MoveBloodPressRegOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A08_2PatientBloodPressRegAndEdit.class);
            //intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };

    View.OnClickListener button07MoveBodyTempRegOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A08_1PatientBodyTempRegAndEdit.class);
            //intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity07_patient_inspection_result);

        //部品の結びつけ
        findViews();

        //前Activityにて選択した患者のIDを取得
        getSelectedPatientId();

        //取得した患者情報をset
        setPatientInfo();

    }


    private void findViews() {
        mText07Name = (TextView) findViewById(R.id.text07Name);
        mText07Id = (TextView) findViewById(R.id.text07Id);
        mText07Age = (TextView) findViewById(R.id.text07Age);
        mText07Sex = (TextView) findViewById(R.id.text07Sex);
        mText07Affiliation = (TextView) findViewById(R.id.text07Affiliation);
        mText07Detail = (TextView) findViewById(R.id.text07Detail);
        mButton07MoveBloodPressReg = (Button) findViewById(R.id.button07MoveBloodPressReg);
        mButton07MoveBodyTempReg = (Button) findViewById(R.id.button07MoveBodyTempReg);

        mListView07BloodPressure = (ListView) findViewById(R.id.listView07BloodPressure);
        mListView07BodyTemp = (ListView) findViewById(R.id.listView07BodyTemp);

        //リスナ登録
        mButton07MoveBloodPressReg.setOnClickListener(button07MoveBloodPressRegOnClickListener);
        mButton07MoveBodyTempReg.setOnClickListener(button07MoveBodyTempRegOnClickListener);

    }


    private void getSelectedPatientId(){

        patientItems = (ArrayList<Patient>)getIntent().getSerializableExtra(KEYWORD_PATIENT);
        patient = patientItems.get(0);
    }

    private void setPatientInfo(){
        mText07Name.setText(patient.getName());
        mText07Id.setText(String.valueOf(patient.getId()));
        mText07Age.setText(String.valueOf(patient.getAge()));
        mText07Sex.setText(patient.getSex());
        mText07Affiliation.setText(patient.getAffiliation());
        mText07Detail.setText(patient.getDetail());
    }


}

