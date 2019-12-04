package com.example.kamadayuji.smartglass_systemflow;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;

public class SearchActivity extends FragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity05_row_sheet_listview);

        Toolbar _toolbar = (Toolbar) findViewById(R.id.toolbar);
        _toolbar.inflateMenu(R.menu.activity_toolbar);

        SearchView _searchView = (SearchView) MenuItemCompat.getActionView(_toolbar.getMenu().findItem(R.id.searchview));;

        // 検索フォーム上の検索ボタンは非表示にする.
        _searchView.setSubmitButtonEnabled(false);
        // 入力欄が空の場合に表示する文言の設定.
        _searchView.setQueryHint("患者名を検索");

        _searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchWord) {
                // 検索ボタンの押下かEnterキーの押下で実行される.
                // 入力内容（searchWord）でDBを検索して、マーカーを再設置する.
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 入力される度に呼び出される.
                // 入力候補を検索して、検索フォームの下に表示する.
                return false;
            }
        });
    }
}