package com.rainmin.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import com.rainmin.demo.R;
import com.rainmin.demo.fragment.NoticeboardFragment;
import com.rainmin.demo.fragment.PaletteFragment;
import com.rainmin.demo.fragment.RefreshLayoutFragment;
import com.rainmin.demo.fragment.SkillMapFragment;
import com.rainmin.demo.fragment.WebFragment;
import com.rainmin.demo.fragment.MapFragment;

public class TestActivity extends BaseActivity {

    private static final String FUNCTION_NAME = "function_name";
    private String functionName;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initView(intent);
    }

    private void getBundle(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            functionName = bundle.getString(FUNCTION_NAME);
        }
    }

    private void initView(Intent intent) {
        getBundle(intent);
        initCustomizedView(R.layout.activity_test, functionName, true);
        if (TextUtils.equals(functionName, getString(R.string.notice_board))) {
            fragment = new NoticeboardFragment();
        } else if (TextUtils.equals(functionName, getString(R.string.palette))) {
            fragment = new PaletteFragment();
        } else if (TextUtils.equals(functionName, getString(R.string.skill_map))) {
            fragment = new SkillMapFragment();
        } else if (TextUtils.equals(functionName, getString(R.string.amap))) {
            fragment = new MapFragment();
        } else if (TextUtils.equals(functionName, getString(R.string.web_view))) {
            fragment = new WebFragment();
        } else if (TextUtils.equals(functionName, getString(R.string.NFC))) {
            // TODO: 2019/6/12  
        } else if (TextUtils.equals(functionName, getString(R.string.pull_down_refresh))) {
            fragment = new RefreshLayoutFragment();
        }

        if (fragment != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fl_content, fragment);
            transaction.commit();
        }
    }

    public static void startTest(Context context, String functionName) {
        Intent intent = new Intent(context, TestActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(FUNCTION_NAME, functionName);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
