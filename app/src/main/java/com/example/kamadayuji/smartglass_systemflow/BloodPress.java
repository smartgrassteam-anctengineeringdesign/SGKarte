package com.example.kamadayuji.smartglass_systemflow;

import android.util.Log;

import java.io.Serializable;

public class BloodPress implements Serializable{
    protected int id;
    protected int date;
    protected int sbp;
    protected int dbp;
    protected int pulse;
    protected String remarks;

    /**
     * Patient
     *
     * @param id      int ID
     * @param date int 日時
     * @param sbp  int 最高血圧
     * @param dbp  int 最低血圧
     * @param pulse int 脈拍
     * @param remarks remarks 備考
     */

    public BloodPress(int id, int date, int sbp, int dbp,int pulse,String remarks) {
        this.id = id;
        this.date = date;
        this.sbp = sbp;
        this.dbp = dbp;
        this.pulse = pulse;
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
     * 最高血圧を取得
     * getSbp()
     *
     * @return sbp int 最高血圧
     */
    public int getSbp() {
        Log.d("取得したsbp：", String.valueOf(sbp));
        return sbp;
    }

    /**
     * 最低血圧を取得
     * getDbp()
     *
     * @return dbp int 最低血圧
     */
    public int getDbp() {
        Log.d("取得したdbp：", String.valueOf(dbp));
        return dbp;
    }

    /**
     * 脈拍を取得
     * getPulse()
     *
     * @return pulse int 脈拍
     */
    public int getPulse() {
        Log.d("取得したpulse：", String.valueOf(pulse));
        return pulse;
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
