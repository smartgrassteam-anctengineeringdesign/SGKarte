package com.example.kamadayuji.smartglass_systemflow;

import android.util.Log;

import java.io.Serializable;

public class BodyTemp implements Serializable{
    protected int id;
    protected int date;
    protected int bt;
    protected String remarks;

    /**
     * Patient
     *
     * @param id      int ID
     * @param date int 日時
     * @param bt  int 体温
     * @param remarks remarks 備考
     */

    public BodyTemp(int id, int date, int bt,String remarks) {
        this.id = id;
        this.date = date;
        this.bt = bt;
        this.remarks = remarks;
    }
    /**
     * IDを取得
     * getId()
     *
     * @return id int ID
     */
    public int getId() {
        Log.d("取得したID：", String.valueOf(id));
        return id;
    }

    /**
     * 日時を取得
     * getDate()
     *
     * @return date int 日時
     */
    public int getDate() {
        Log.d("取得したdate：", String.valueOf(date));
        return date;
    }

    /**
     * 体温を取得
     * getBt()
     *
     * @return bt int 体温
     */
    public int getBt() {
        Log.d("取得したbt：", String.valueOf(bt));
        return bt;
    }

    /**
     * 備考を取得
     * getRemarks()
     *
     * @return detail String 詳細
     */
    public String getRemarks() {
        Log.d("取得したremarks：", remarks);
        return remarks;
    }


}
