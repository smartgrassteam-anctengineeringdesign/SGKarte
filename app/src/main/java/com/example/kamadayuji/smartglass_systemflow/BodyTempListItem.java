package com.example.kamadayuji.smartglass_systemflow;

import android.util.Log;

public class BodyTempListItem extends BodyTemp{


    /**
     * BloodPressListItem()
     *
     * @param id    int ID
     * @param date  String 日時
     * @param bt   float 体温
     */

    public BodyTempListItem(int id, int date, float bt) {
        super(id,date,bt,null);
    }
}
