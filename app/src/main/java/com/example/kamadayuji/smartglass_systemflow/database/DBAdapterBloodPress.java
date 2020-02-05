package com.example.kamadayuji.smartglass_systemflow.database;

import android.content.Context;
import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteOpenHelper;
import android.util.Log;
import java.io.File;
import android.app.Activity;
import android.os.Bundle;

public class DBAdapterBloodPress extends Activity{

    private static String DB_NAME;
    private final static String DB_NAME1 = "patientID_";
    private final static String DB_NAME2 = ".db";
    //テーブル名をbloodPressに直したい
    private static String DB_TABLE = "bodyPress";
    private final static int DB_VERSION = 1;

    /**
     * DBのカラム名 patientBP_(患者ID)
     */
    public final static String COL_BPID = "_id";
    public final static String COL_DATE = "date";       //UnixTime
    public final static String COL_SBP = "sbp";         //最高血圧
    public final static String COL_DBP = "dbp";         //最低血圧
    public final static String COL_PR = "pr";           //pulse rate
    public final static String COL_REMARKS = "remarks";   //備考


    String Password = "test123";
    File databaseFile = getDatabasePath("demo.db");
    private SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(databaseFile, Password, null);
    private DBHelper dbHelper = null;
    protected Context context;
    protected static String patientId;

    //コンストラクタ
    public DBAdapterBloodPress(Context context,String patientId) {
        this.context = context;
        this.patientId = patientId;
        DB_NAME = DB_NAME1 + patientId + DB_NAME2;
        dbHelper = new DBHelper(this.context);
    }

    /**
     * DBの読み書き
     *
     * @return this 自身のオブジェクト
     */
    public DBAdapterBloodPress openDB() {
        SQLiteDatabase.loadLibs(this);
        db = dbHelper.getWritableDatabase(Password); //DB読み書き
        return this;
    }

    /**
     * DBの読み込み 今回は未使用
     * readDB()
     *
     * @return this 自身のオブジェクト
     */
    public DBAdapterBloodPress readDB() {
        db = dbHelper.getReadableDatabase(Password);        // DBの読み込み
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
     * DBのレコードへ登録
     * saveDB()
     *
     * @param dateAndTime        日時
     * @param sbp         最高血圧
     * @param dbp         最低血圧
     * @param pr          脈拍数
     * @param remarks     備考
     */

    public void saveDB(int dateAndTime, int sbp, int dbp, int pr, String remarks) {

        db.beginTransaction();

        try {
            ContentValues values = new ContentValues();  // ContentValuesでデータを設定していく
            values.put(COL_DATE, dateAndTime);
            values.put(COL_SBP, sbp);
            values.put(COL_DBP, dbp);
            values.put(COL_PR, pr);
            values.put(COL_REMARKS, remarks);

            // insertメソッド データ登録
            // 第1引数：DBのテーブル名
            // 第2引数：更新する条件式
            // 第3引数：ContentValues
            db.insert(DB_TABLE, null, values);      // レコードへ登録
            Log.d("log","insert " + values);

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                // トランザクションの終了
        }
    }

    /**
     * DBのデータを取得
     * getDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @return DBのデータ
     */

    public Cursor getDB(String[] columns) {

        // queryメソッド DBのデータを取得
        // 第1引数：DBのテーブル名
        // 第2引数：取得するカラム名
        // 第3引数：選択条件(WHERE句)
        // 第4引数：第3引数のWHERE句において?を使用した場合に使用
        // 第5引数：集計条件(GROUP BY句)
        // 第6引数：選択条件(HAVING句)
        // 第7引数：ソート条件(ODERBY句)
        return db.query(DB_TABLE, columns, null, null, null, null, null);
    }

    /**
     * DBの検索したデータを取得
     * searchDB()
     *
     * @param columns String[] 取得するカラム名 nullの場合は全カラムを取得
     * @param column  String 選択条件に使うカラム名
     * @param name    String[]
     * @return DBの検索したデータ
     */

    public Cursor searchDB(String[] columns, String column, String[] name) {
        return db.query(DB_TABLE, columns, column + " like ?", name, null, null, null);
    }

    /**
     * DBのレコードを全削除
     * allDelete()
     */

    public void allDelete() {

        db.beginTransaction();                      // トランザクション開始
        try {
            // deleteメソッド DBのレコードを削除
            // 第1引数：テーブル名
            // 第2引数：削除する条件式 nullの場合は全レコードを削除
            // 第3引数：第2引数で?を使用した場合に使用
            db.delete(DB_TABLE, null, null);        // DBのレコードを全削除
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                    // トランザクションの終了
        }
    }

    /**
     * DBのレコードの単一削除
     * selectDelete()
     *
     * @param position String
     */

    public void selectDelete(String position) {

        db.beginTransaction();                      // トランザクション開始
        try {
            db.delete(DB_TABLE, COL_BPID + "=?", new String[]{position});
            db.setTransactionSuccessful();          // トランザクションへコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();                    // トランザクションの終了
        }
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
            db.execSQL("DROP TABLE IF EXISTS" + DB_TABLE);
            // テーブル生成
            //onCreate(db);

            Log.d("log","upgradeTable");
        }
    }
}


