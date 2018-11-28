package com.example.kamadayuji.smartglass_systemflow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class A06PatientInfoDetailActivity extends AppCompatActivity {

    private static String KEYWORD_PATIENT = "KEY_PATIENT";
    private List<Patient> patientItems;
    private Patient patient;

    private TextView mText06Name;
    private TextView mText06Id;
    private TextView mText06Age;
    private TextView mText06Sex;
    private TextView mText06Affiliation;
    private TextView mText06Detail;

    private Button mButton06MoveBodyTempMeasure;
    private Button mButton06MoveBloodPressMeasure;
    private Button mButton06MoveDetailInsRe;

//    private DBAdapterGetPatientDetail dbAdapter;
//    private MyBaseAdapter myBaseAdapter;
//    private List<PatientInsReListItem> items;
    private ListView mListView06;
//    protected PatientInsReListItem patientInsReListItem;


    //リスナ登録
    View.OnClickListener button06MoveDetailInsReOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A07PatientInspectionResultActivity.class);
            intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity06_patient_info_detail);

        //各部品の結びつけ
        findViews();

        //前Activityにて選択した患者のIDを取得
        getSelectedPatientId();

        //取得した患者情報をset
        setPatientInfo();
    }

    private void findViews() {

        mText06Name = (TextView) findViewById(R.id.text06Name);
        mText06Id = (TextView) findViewById(R.id.text06Id);
        mText06Age = (TextView) findViewById(R.id.text06Age);
        mText06Sex = (TextView) findViewById(R.id.text06Sex);
        mText06Affiliation = (TextView) findViewById(R.id.text06Affiliation);
        mText06Detail = (TextView) findViewById(R.id.text06Detail);
        mButton06MoveBodyTempMeasure = (Button) findViewById(R.id.button06MoveBodyTempMeasure);
        mButton06MoveBloodPressMeasure = (Button) findViewById(R.id.button06MoveBloodPressMeasure);
        mButton06MoveDetailInsRe = (Button) findViewById(R.id.button06MoveDetailtInsRe);

        mListView06 = (ListView) findViewById(R.id.listView06);

        //リスナ登録
        mButton06MoveDetailInsRe.setOnClickListener(button06MoveDetailInsReOnClickListener);
    }

    private void getSelectedPatientId(){

        patientItems = (ArrayList<Patient>)getIntent().getSerializableExtra(KEYWORD_PATIENT);
        patient = patientItems.get(0);
    }

    private void setPatientInfo(){
        mText06Name.setText(patient.getName());
        mText06Id.setText(String.valueOf(patient.getId()));
        mText06Age.setText(String.valueOf(patient.getAge()));
        mText06Sex.setText(patient.getSex());
        mText06Affiliation.setText(patient.getAffiliation());
        mText06Detail.setText(patient.getDetail());
    }

}
