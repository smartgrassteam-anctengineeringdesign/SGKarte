package com.example.kamadayuji.smartglass_systemflow;

import android.util.Log;

public class PatientListItem extends Patient{


    /**
     * PatientListItem()
     *
     * @param id      int ID
     * @param name String 氏名
     * @param age  int 年齢
     * @param sex  String 性別
     */

    public PatientListItem(int id, String name, int age, String sex) {
        super(id,name,age,sex,null,null);
    }
}
