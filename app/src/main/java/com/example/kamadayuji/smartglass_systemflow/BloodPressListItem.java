package com.example.kamadayuji.smartglass_systemflow;

import android.util.Log;

public class BloodPressListItem extends BloodPress{


    /**
     * BloodPressListItem()
     *
     * @param id    int ID
     * @param date  String 日時
     * @param sbp   int 最高血圧
     * @param dbp   int 最低血圧
     * @param pulse int 脈拍
     */

    public BloodPressListItem(int id, int date, int sbp,int dbp,int pulse) {
        super(id,date,sbp,dbp,pulse,null);
    }
}
