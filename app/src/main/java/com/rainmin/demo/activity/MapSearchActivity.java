package com.rainmin.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rainmin.demo.R;

public class MapSearchActivity extends BaseActivity {

    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map_search);
        initCustomizedView(R.layout.activity_map_search, "", false);
        mBack = (ImageView) findViewById(R.id.iv_back);
        initListener();
    }

    private void initListener() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
