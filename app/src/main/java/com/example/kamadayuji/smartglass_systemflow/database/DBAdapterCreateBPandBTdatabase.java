package com.example.kamadayuji.smartglass_systemflow.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.example.kamadayuji.smartglass_systemflow.database.DBAdapterBloodPress.COL_REMARKS;

public class DBAdapterCreateBPandBTdatabase {

    private static String DB_NAME;
    private final static String DB_NAME1 = "patientID_";
    private final static String DB_NAME2 = ".db";
    //テーブル名をbloodPressに直したい
    private static String DB_TABLE_BP = "bodyPress";
    private static String DB_TABLE_BT = "bodyTemp";
    private final static int DB_VERSION = 1;

    /**
     * bodyPressテーブルのカラム名
     */
    public final static String COL_BPID = "_id";
    public final static String COL_BP_DATE = "date";       //UnixTime
    public final static String COL_SBP = "sbp";         //最高血圧
    public final static String COL_DBP = "dbp";         //最低血圧
    public final static String COL_PR = "pr";           //pulse rate
    public final static String COL_BP_REMARKS = "remarks";   //備考

    /**
     *　bodyTempテーブルのカラム名
     */
    public final static String COL_BTID = "_id";
    public final static String COL_BT_DATE = "date";         //UnixTime
    public final static String COL_BT = "bt";                //体温
    public final static String COL_BT_REMARKS = "remarks";   //備考


    private SQLiteDatabase db = null;
    private DBHelper dbHelper = null;
    protected Context context;
    protected static String patientId;

    //コンストラクタ
    public DBAdapterCreateBPandBTdatabase(Context context,String patientId) {
        this.context = context;
        this.patientId = patientId;
        DB_NAME = DB_NAME1 + patientId + DB_NAME2;      //patientID_xx.db (DB名)
        dbHelper = new DBHelper(this.context);
    }

    /**
     * DBの読み書き
     *
     * @return this 自身のオブジェクト
     */
    public DBAdapterCreateBPandBTdatabase openDB() {
        db = dbHelper.getWritableDatabase(); //DB読み書き
        return this;
    }

    /**
     * DBの読み込み 今回は未使用
     * readDB()
     *
     * @return this 自身のオブジェクト
     */
    public DBAdapterCreateBPandBTdatabase readDB() {
        db = dbHelper.getReadableDatabase();        // DBの読み込み
        return this;
    }

    /**
     * DBを閉じる
     * closeDB()
     */
    public void closeDB() {
        db.close();     // DBを閉じる
        db = null;
    }


    /**
     * データベースの生成やアップグレードを管理するSQLiteOpenHelperを継承したクラス
     * DBHelper
     */

    private static class DBHelper extends SQLiteOpenHelper {

        // コンストラクタ
        public DBHelper(Context context) {
            //第1引数：コンテキスト
            //第2引数：DB名
            //第3引数：factory nullでよい
            //第4引数：DBのバージョン

            super(context, DB_NAME, null, DB_VERSION);
        }


        /**
         * DB生成時に呼ばれる
         * onCreate()

         * @param db SQLiteDatabase
         */

        @Override
        public void onCreate(SQLiteDatabase db) {

            //テーブルを作成するSQL文の定義 ※スペースに気を付ける
            //血圧DBの作成
            String createTbl_BP = "CREATE TABLE " + DB_TABLE_BP + " ("
                    + COL_BPID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_BP_DATE + " INTEGER NOT NULL,"
                    + COL_SBP + " INTEGER NOT NULL,"
                    + COL_DBP + " INTEGER NOT NULL,"
                    + COL_PR + " INTEGER NOT NULL,"
                    + COL_BP_REMARKS + " TEXT"
                    + ");";

            String createTbl_BT = "CREATE TABLE " + DB_TABLE_BT + " ("
                    + COL_BTID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COL_BT_DATE + " INTEGER NOT NULL,"
                    + COL_BT + " REAL NOT NULL,"
                    + COL_REMARKS + " TEXT"
                    + ");";

            Log.d("log","createdb" + DB_NAME);

            db.execSQL(createTbl_BP);      //SQL文の実行
            db.execSQL(createTbl_BT);      //SQL文の実行

        }

        /**
         * DBアップグレード(バージョンアップ)時に呼ばれる
         *
         * @param db         SQLiteDatabase
         * @param oldVersion int 古いバージョン
         * @param newVersion int 新しいバージョン
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // DBからテーブル削除
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_BP);
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE_BT);
            // テーブル生成
            onCreate(db);

            Log.d("log","upgradeTable");
        }
    }
}


