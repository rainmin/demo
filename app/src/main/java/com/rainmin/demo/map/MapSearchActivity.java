package com.rainmin.demo.map;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.rainmin.demo.BaseActivity;
import com.rainmin.demo.R;

import butterknife.BindView;

public class MapSearchActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_map_search);
        initCustomizedView(R.layout.activity_map_search, "", false);
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
