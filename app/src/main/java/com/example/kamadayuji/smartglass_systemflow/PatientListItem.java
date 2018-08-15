package com.example.kamadayuji.smartglass_systemflow;

import android.util.Log;

public class PatientListItem {
    protected int id;
    protected String name;
    protected int age;
    protected String sex;

    /**
     * PatientListItem()
     *
     * @param id      int ID
     * @param name String 氏名
     * @param age  String 年齢
     * @param sex  String 性別
     */
    public PatientListItem(int id, String name, int age, String sex) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
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
     * 氏名を取得
     * getName()
     *
     * @return product String 名前
     */
    public String getName() {
        Log.d("取得したname：", String.valueOf(name));
        return name;
    }

    /**
     * 年齢を取得
     * getMadeIn()
     *
     * @return madeIn String 年齢
     */
    public int getAge() {
        Log.d("取得したage：", String.valueOf(age));
        return age;
    }

    /**
     * 性別を取得
     * getSex()
     *
     * @return number String 性別
     */
    public String getSex() {
        Log.d("取得したsex：", String.valueOf(sex));
        return sex;
    }

}
