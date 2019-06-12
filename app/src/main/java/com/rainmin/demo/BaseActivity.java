package com.rainmin.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

public class BaseActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * the inheriting class should call this function in onCreate, to add customized view
     * and initialize ButterKnife
     * @param resId customized view id
     * @param title title displayed on toolbar
     */
    public void initCustomizedView(int resId, String title, boolean showToolbar) {
        setContentView(R.layout.activity_base);
        frameLayout = (FrameLayout) findViewById(R.id.content_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        // add customized view
        View contentView = View.inflate(this, resId, null);
        frameLayout.addView(contentView);
        // init toolbar
        if (showToolbar) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            toolbar.setVisibility(View.GONE);
        }
    }
}
