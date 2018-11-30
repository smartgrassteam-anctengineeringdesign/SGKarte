package com.example.kamadayuji.smartglass_systemflow;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    //listViewに関して
    private DBAdapterBodyTemp dbAdapterBodyTemp;
    private DBAdapterBloodPress dbAdapterBloodPress;
    private BTBaseAdapter btBaseAdapter;
    //private BPBaseAdapter bpBaseAdapter;
    private List<BloodPressListItem> bpItems;
    private List<BodyTempListItem> btItems;
    protected BodyTempListItem bodyTempListItem;
    protected BloodPressListItem bloodPressListItem;

    //参照するテーブル「bodyTemp」のカラムと備考
    private String[] btColumns = {"_id","date","bt"};
    private String[] btColumnsGetDetail = {"remarks"};
    //参照するテーブル「bloodPress」のカラムと備考
    private String[] bpColumns = {"_id","date","sbp","dbp","pulse"};
    private String[] bpColumnsGetDetail = {"remarks"};

    //選択したデータのみ，詳細を取得する
    private String btRemarks;
    private String bpRemarks;

    //patientID
    private String patientId;

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
            intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };

    View.OnClickListener button07MoveBodyTempRegOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getApplication(), A08_1PatientBodyTempRegAndEdit.class);
            intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
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

        //読み込むDBの準備等をする
        loadBtAndBpList();

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
        //idを取得
        patientId = String.valueOf(patient.getId());

        mText07Name.setText(patient.getName());
        mText07Id.setText(patientId);
        mText07Age.setText(String.valueOf(patient.getAge()));
        mText07Sex.setText(patient.getSex());
        mText07Affiliation.setText(patient.getAffiliation());
        mText07Detail.setText(patient.getDetail());
    }

    /**
     * DBを読み込む＆更新する処理
     * loadMyList()
     */

    private void loadBtAndBpList(){

        //dbへアクセス
        dbAdapterBloodPress = new DBAdapterBloodPress(this,patientId);
        dbAdapterBodyTemp = new DBAdapterBodyTemp(this,patientId);

        //itemsのArrayList生成
        btItems = new ArrayList<>();
        bpItems = new ArrayList<>();

        //BaseAdapterのコンスタラクタの呼び出し(bt&bpBaseAdapterのオブジェクト生成)
        btBaseAdapter = new BTBaseAdapter(this,btItems);
        //bpBaseAdapter = new BPBaseAdapter(this,bpItems);

        //ArrayAdapterに対してListViewのリスト(items)の更新
        btItems.clear();
        //bpItems.clear();

        dbAdapterBodyTemp.openDB();     //DBの読み込み

        //DBのデータの取得
        Cursor cBt = dbAdapterBodyTemp.getDB(btColumns);

        if (cBt.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                bodyTempListItem = new BodyTempListItem(
                        cBt.getInt(0),
                        cBt.getInt(1),
                        cBt.getInt(2));

                Log.d("取得したCursorBodyTemp(ID):", String.valueOf(cBt.getInt(0)));
                Log.d("取得したCursorBodyTemp(日時):", cBt.getString(1));
                Log.d("取得したCursorBodyTemp(体温):", String.valueOf(cBt.getInt(2)));

                btItems.add(bodyTempListItem);          // 取得した要素をitemsに追加

            } while (cBt.moveToNext());
        }
        cBt.close();
        dbAdapterBodyTemp.closeDB();                    // DBを閉じる
        mListView07BodyTemp.setAdapter(btBaseAdapter);  // ListViewにmyBaseAdapterをセット
        btBaseAdapter.notifyDataSetChanged();   // Viewの更新
    }

    /**
     * BaseAdapterを継承したクラス
     * MyBaseAdapter
     */

    public class BTBaseAdapter extends BaseAdapter {
        private Context context;
        private List<BodyTempListItem> items;

        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private class ViewHolder {
            TextView text07Id;
            TextView text07Date;
            TextView text07Bt;
        }

        // コンストラクタの生成
        public BTBaseAdapter(Context context, List<BodyTempListItem> items) {
            this.context = context;
            this.items = items;
        }
        // Listの要素数を返す
        @Override
        public int getCount() {
            return items.size();
        }

        // indexやオブジェクトを返す
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        // IDを他のindexに返す
        @Override
        public long getItemId(int position) {
            return position;
        }



        // 新しいデータが表示されるタイミングで呼び出される
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;
            A07PatientInspectionResultActivity.BTBaseAdapter.ViewHolder holder;

            // データを取得
            bodyTempListItem = items.get(position);


            if (view == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity07_row_sheet_body_temp_listview, parent, false);


                TextView text07Date = (TextView) view.findViewById(R.id.text07BtDateAndTime);        // 日時のTextView
                TextView text07Id = (TextView) view.findViewById(R.id.text07BtId);            // IdのTextView
                TextView text07Bt = (TextView) view.findViewById(R.id.text07Bt);       // 体温のTextView

                // holderにviewを持たせておく
                holder = new A07PatientInspectionResultActivity.BTBaseAdapter.ViewHolder();
                holder.text07Date = text07Date;
                holder.text07Id = text07Id;
                holder.text07Bt = text07Bt;
                view.setTag(holder);

            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (A07PatientInspectionResultActivity.BTBaseAdapter.ViewHolder) view.getTag();
            }

            // 取得した各データを各TextViewにセット
            holder.text07Date.setText(String.valueOf(BodyTempListItem.getDate()));
            //Log.d("ok","getName");
            holder.text07Id.setText(String.valueOf(BodyTempListItem.getId()));
            //Log.d("ok","getid");
            holder.text07Bt.setText(String.valueOf(BodyTempListItem.getBt()));
            //Log.d("ok","getage");
            return view;

        }
    }


}

