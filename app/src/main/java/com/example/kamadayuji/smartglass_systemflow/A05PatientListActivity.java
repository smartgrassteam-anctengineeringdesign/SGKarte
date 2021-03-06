package com.example.kamadayuji.smartglass_systemflow;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.kamadayuji.smartglass_systemflow.database.DBAdapterPatientList;
import com.example.kamadayuji.smartglass_systemflow.database.DBAESEncryption;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


//ToDo : 患者一覧閲覧時、一度読み込んだ患者情報について、二度読み込むような動きをするため動作が遅い。改善したい。
public class A05PatientListActivity extends AppCompatActivity {

    private DBAdapterPatientList dbAdapterPatientList;
    private MyBaseAdapter myBaseAdapter;
    private List<PatientListItem> items;
    private ListView mListView05;
    protected PatientListItem patientListItem;

    //参照するDBのカラム：ID,氏名,年齢,性別
    private String[] columns = {"_id","name","age","sex"};
    //参照DBカラム：所属,詳細
    private String[] columnsGetDetail = {"affiliation","detail"};

    //Patient Listを作成
    private List<Patient> patientItems;
    protected Patient patient;

    //選択した患者のみ、所属と詳細を取得する
    private String patientAffiliation;
    private String patientDetail;

    //リスナ設定
    //リストビューの中身長押しした時の処理
    //ToDo:長押しした時は、「編集」、「削除」、「詳細な患者情報」等ボタンが出てくるようにしてそれぞれの処理をさせる
    AdapterView.OnItemLongClickListener mListView05OnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            //アラートダイアログ表示
            AlertDialog.Builder builder = new AlertDialog.Builder(A05PatientListActivity.this);
            builder.setTitle("削除");
            builder.setMessage("削除しますか？");

            //OKの時
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //IDを取得する
                    patientListItem =items.get(position);
                    int patientListId = patientListItem.getId();

                    dbAdapterPatientList.openDB();     //DBの読み込み(読み書きの方)
                    dbAdapterPatientList.selectDelete(String.valueOf(patientListId));
                    Log.d("Long click : ", String.valueOf(patientListId));
                    dbAdapterPatientList.closeDB();
                    try {
                        loadPatinentList();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidAlgorithmParameterException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (IllegalBlockSizeException e) {
                        e.printStackTrace();
                    } catch (BadPaddingException e) {
                        e.printStackTrace();
                    } catch (InvalidKeyException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }

                }
            });
            //キャンセルの時
            builder.setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            //ダイアログの表示
            AlertDialog dialog = builder.create();
            dialog.show();

            //trueの場合、長押し後onClickは発生しない
            return true;

        }
    };

    //タップで患者詳細情報画面へ
    AdapterView.OnItemClickListener mListView05OnItemOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            //IDを取得する
            patientListItem = items.get(position);

            final int patientListId = patientListItem.getId();

            //所属、詳細を取得
            loadPatientDetailInfo(patientListId);


            //患者情報を格納するリストを生成
            patientItems = new ArrayList<>();

            //patientを生成
            patient = new Patient(
                    patientListId,
                    patientListItem.getName(),
                    patientListItem.getAge(),
                    patientListItem.getSex(),
                    patientAffiliation,
                    patientDetail);

            patientItems.add(patient);

            Intent intent = new Intent(getApplication(), A06PatientInfoDetailActivity.class);

            //患者情報をintentに入れる
            intent = intent.putExtra("KEY_PATIENT", (Serializable) patientItems);
            startActivity(intent);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity05_patient_list);

        //DBAdapterのコンストラクタを呼び出し
        dbAdapterPatientList = new DBAdapterPatientList(this);


        //itemsのArrayList生成
        items = new ArrayList<>();

        //MyBaseAdapterのコンスタラクタの呼び出し(myBaseAdapterのオブジェクト生成)
        myBaseAdapter = new MyBaseAdapter(this, items);

        //ListViewの結びつけ
        mListView05 = (ListView)findViewById(R.id.listView05);

        try {
            loadPatinentList(); //DBを読み込む&更新する処理
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }

        //リスナ登録
        mListView05.setOnItemClickListener(mListView05OnItemOnClickListener);
        mListView05.setOnItemLongClickListener(mListView05OnItemLongClickListener);
    }


    /**
     * DBを読み込む＆更新する処理
     * loadMyList()
     */

    private void loadPatinentList() throws UnsupportedEncodingException, NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items.clear();

        dbAdapterPatientList.openDB();     //DBの読み込み

        //DBのデータの取得
        Cursor c = dbAdapterPatientList.getDB(columns);

        String password = "test12345";


        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                patientListItem = new PatientListItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getInt(2),
                        c.getString(3));
                DBAESEncryption.EncryptedData encdata = new DBAESEncryption.EncryptedData();
                encdata.salt = new byte[8];
                encdata.iv = new byte[16];

              byte[] decryname =  DBAESEncryption.decrypt(password, new byte[8], new byte[16], c.getString(1).getBytes("UTF-8"));


                Log.d("取得したCursor(ID):", String.valueOf(c.getInt(0)));
                Log.d("取得したCursor(氏名):", c.getString(1));
                Log.d("取得したCursor(年齢):", String.valueOf(c.getInt(2)));
                Log.d("取得したCursor(性別):", c.getString(3));

                items.add(patientListItem);          // 取得した要素をitemsに追加

            } while (c.moveToNext());
        }

        Log.d("DBからの読み取り終了", "狩猟");

        c.close();
        dbAdapterPatientList.closeDB();                    // DBを閉じる
        mListView05.setAdapter(myBaseAdapter);  // ListViewにmyBaseAdapterをセット
        myBaseAdapter.notifyDataSetChanged();   // Viewの更新
    }

    /**
     * BaseAdapterを継承したクラス
     * MyBaseAdapter
     */

    public class MyBaseAdapter extends BaseAdapter {
        private Context context;
        private List<PatientListItem> items;

        // 毎回findViewByIdをする事なく、高速化が出来るようするholderクラス
        private class ViewHolder {
            TextView text05Id;
            TextView text05Name;
            TextView text05Age;
            TextView text05Sex;
        }

        // コンストラクタの生成
        public MyBaseAdapter(Context context, List<PatientListItem> items) {
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
            ViewHolder holder;

            // データを取得
            patientListItem = items.get(position);
            //以下は逆順に表示したい場合に使用
            //patientListItem = items.get(items.size()-1-position);


            Log.d("取得したああああああああ",String.valueOf(items.get(position)));
            Log.d("取得したいいいいい",String.valueOf(position));
            Log.d("取得したうううう" ,String.valueOf(items.size()));



            if (view == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity05_row_sheet_patient_listview, parent, false);


                TextView text05Name = (TextView) view.findViewById(R.id.text05Name);        // 名前のTextView
                TextView text05Id = (TextView) view.findViewById(R.id.text05Id);            // IdのTextView
                TextView text05Age = (TextView) view.findViewById(R.id.text05Age);       // 年齢のTextView
                TextView text05Sex = (TextView) view.findViewById(R.id.text05Sex);        // 性別のTextView

                // holderにviewを持たせておく
                holder = new ViewHolder();
                holder.text05Name = text05Name;
                holder.text05Id = text05Id;
                holder.text05Age = text05Age;
                holder.text05Sex = text05Sex;
                view.setTag(holder);

            } else {
                // 初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (ViewHolder) view.getTag();
            }

            // 取得した各データを各TextViewにセット
            holder.text05Name.setText(patientListItem.getName());
            //Log.d("ok","getName");
            holder.text05Id.setText(String.valueOf(patientListItem.getId()));
            //Log.d("ok","getid");
            holder.text05Age.setText(String.valueOf(patientListItem.getAge()));
            //Log.d("ok","getage");
            holder.text05Sex.setText(patientListItem.getSex());
            //Log.d("ok","getsex");

            return view;

        }
    }

    /**
     * 選択された患者の所属、詳細をDBから取得する
     * loadPatientDetailInfo
     */

    private void loadPatientDetailInfo(int id){

        dbAdapterPatientList.openDB();

        String column = "_id";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor c = dbAdapterPatientList.searchDB(columnsGetDetail,column, selectionArgs);

        if(c.moveToFirst()) {
            do{
                patientAffiliation = c.getString(0);
                patientDetail = c.getString(1);
            } while (c.moveToNext());
        }
        //Log.d("がんばって取得した所属",patientAffiliation);
        //Log.d("がんばたて取得した詳細",patientDetail);

        c.close();
        dbAdapterPatientList.closeDB();
    }
}