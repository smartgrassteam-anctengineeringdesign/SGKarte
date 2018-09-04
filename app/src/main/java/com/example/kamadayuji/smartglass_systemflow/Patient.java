package com.example.kamadayuji.smartglass_systemflow;

import android.util.Log;
import java.io.Serializable;

public class Patient implements Serializable {
    protected int id;
    protected String name;
    protected int age;
    protected String sex;
    protected String affiliation;
    protected String detail;

    /**
     * Patient
     *
     * @param id      int ID
     * @param name String 氏名
     * @param age  int 年齢
     * @param sex  String 性別
     * @param affiliation String 所属
     * @param detail String 詳細
     */

    public Patient(int id, String name, int age, String sex, String affiliation, String detail) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.affiliation = affiliation;
        this.detail = detail;
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
     * @return name String 名前
     */
    public String getName() {
        Log.d("取得したname：", name);
        return name;
    }

    /**
     * 年齢を取得
     * getMadeIn()
     *
     * @return age int 年齢
     */
    public int getAge() {
        Log.d("取得したage：", String.valueOf(age));
        return age;
    }

    /**
     * 性別を取得
     * getSex()
     *
     * @return sex String 性別
     */
    public String getSex() {
        Log.d("取得したsex：", sex);
        return sex;
    }

    /**
     * 所属を取得
     * getAffiliation()
     *
     * @return affiliation String 所属
     */
    public String getAffiliation() {
        Log.d("取得したaffiliation：", affiliation);
        return affiliation;
    }

    /**
     * 所属を取得
     * getDetail()
     *
     * @return detail String 詳細
     */
    public String getDetail() {
        Log.d("取得したaffiliation：", detail);
        return detail;
    }


}
