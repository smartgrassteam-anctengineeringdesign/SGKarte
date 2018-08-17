package com.example.kamadayuji.smartglass_systemflow;

import android.content.Context;
import android.content.DialogInterface;
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

import java.util.ArrayList;
import java.util.List;


//ToDo : 患者一覧閲覧時、一度読み込んだ患者情報について、二度読み込むような動きをするため動作が遅い。改善したい。
public class PatientListActivity extends AppCompatActivity {

    private DBAdapter dbAdapter;
    private MyBaseAdapter myBaseAdapter;
    private List<PatientListItem> items;
    private ListView mListView05;
    protected PatientListItem patientListItem;

    //参照するDBのカラム：ID,氏名,年齢,性別
    private String[] columns = {"_id","name","age","sex"};



    //リスナ設定
    //リストビューの中身長押しした時の処理
    AdapterView.OnItemLongClickListener mListView05OnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

            //アラートダイアログ表示
            AlertDialog.Builder builder = new AlertDialog.Builder(PatientListActivity.this);
            builder.setTitle("削除");
            builder.setMessage("削除しますか？");

            //OKの時
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    //IDを取得する
                    patientListItem = items.get(position);
                    int patientListId = patientListItem.getId();

                    dbAdapter.openDB();     //DBの読み込み(読み書きの方)
                    dbAdapter.selectDelete(String.valueOf(patientListId));
                    Log.d("Long click : ", String.valueOf(patientListId));
                    dbAdapter.closeDB();
                    loadPatinentList();
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

            return false;

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity05_patient_list);

        //DBAdapterのコンストラクタを呼び出し
        dbAdapter = new DBAdapter(this);


        //itemsのArrayList生成
        items = new ArrayList<>();

        //MyBaseAdapterのコンスタラクタの呼び出し(myBaseAdapterのオブジェクト生成)
        myBaseAdapter = new MyBaseAdapter(this, items);

        //ListViewの結びつけ
        mListView05 = (ListView)findViewById(R.id.listView05);

        loadPatinentList(); //DBを読み込む&更新する処理

        //リスナ登録
        mListView05.setOnItemLongClickListener(mListView05OnItemLongClickListener);
    }


    /**
     * DBを読み込む＆更新する処理
     * loadMyList()
     */

    private void loadPatinentList(){

        //ArrayAdapterに対してListViewのリスト(items)の更新
        items.clear();

        dbAdapter.openDB();     //DBの読み込み

        //DBのデータの取得
        Cursor c = dbAdapter.getDB(columns);

        if (c.moveToFirst()) {
            do {
                // MyListItemのコンストラクタ呼び出し(myListItemのオブジェクト生成)
                patientListItem = new PatientListItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getInt(2),
                        c.getString(3));

                Log.d("取得したCursor(ID):", String.valueOf(c.getInt(0)));
                Log.d("取得したCursor(氏名):", c.getString(1));
                Log.d("取得したCursor(年齢):", String.valueOf(c.getInt(2)));
                Log.d("取得したCursor(性別):", c.getString(3));

                items.add(patientListItem);          // 取得した要素をitemsに追加

            } while (c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();                    // DBを閉じる
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


            if (view == null) {
                LayoutInflater inflater =
                        (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity05_row_sheet_listview, parent, false);

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
}