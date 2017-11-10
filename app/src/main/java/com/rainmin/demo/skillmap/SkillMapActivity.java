package com.rainmin.demo.skillmap;

import android.os.Bundle;

import com.rainmin.demo.BaseActivity;
import com.rainmin.demo.R;

/**
 * Created by chenming on 2017/11/10
 */

public class SkillMapActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomizedView(R.layout.activity_skillmap, "技能展示图");
    }
}
