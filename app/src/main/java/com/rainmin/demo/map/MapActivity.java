package com.rainmin.demo.map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.rainmin.demo.BaseActivity;
import com.rainmin.demo.R;

public class MapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomizedView(R.layout.activity_map, "Map");
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment mapFragment = new MapFragment();
        transaction.add(R.id.fl_content, mapFragment);
        transaction.commit();
    }
}
