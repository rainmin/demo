package com.rainmin.demo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rainmin.demo.fragment.WebFragment;
import com.rainmin.demo.map.MapActivity;
import com.rainmin.demo.nfc.NfcActivity;
import com.rainmin.demo.noticeboard.NoticeboardActivity;
import com.rainmin.demo.palette.PaletteActivity;
import com.rainmin.demo.refreshlayout.RefreshLayoutActivity;
import com.rainmin.demo.skillmap.SkillMapActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

    Unbinder mUnbinder;
    private WebFragment mWebFragment;
    private int mSelectedItem;
    private boolean mIsItemCheck;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUnbinder = ButterKnife.bind(this);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        initView();
        initListener();
    }

    private void initView() {

    }

    private void initListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }

                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.nav_notice_board:
                        mSelectedItem = itemId;
                        break;
                    case R.id.nav_palette:
                        mSelectedItem = itemId;
                        break;
                    case R.id.nav_skill_map:
                        mSelectedItem = itemId;
                        break;
                    case R.id.nav_amap:
                        mSelectedItem = itemId;
                        break;
                    case R.id.nav_webview:
                        mSelectedItem = itemId;
                        break;
                    case R.id.nav_nfc:
                        mSelectedItem = itemId;
                        break;
                    case R.id.refresh_layout:
                        mSelectedItem = itemId;
                        break;
                }
                mIsItemCheck = true;

                return true;
            }
        });

        // 解决点击navigationView的item来切换页面不流畅的问题
        // 在navigationView的关闭动作执行完毕后再进行切换页面的操作
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (mIsItemCheck) {
                    switch (mSelectedItem) {
                        case R.id.nav_notice_board:
                            startActivity(new Intent(mContext, NoticeboardActivity.class));
                            break;
                        case R.id.nav_palette:
                            startActivity(new Intent(mContext, PaletteActivity.class));
                            break;
                        case R.id.nav_skill_map:
                            startActivity(new Intent(mContext, SkillMapActivity.class));
                            break;
                        case R.id.nav_amap:
                            startActivity(new Intent(mContext, MapActivity.class));
                            break;
                        case R.id.nav_webview:
                            if (mWebFragment == null) {
                                mWebFragment = new WebFragment();
                            }
                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.fm_content, mWebFragment);
                            transaction.commit();
                            break;
                        case R.id.nav_nfc:
                            startActivity(new Intent(mContext, NfcActivity.class));
                            break;
                        case R.id.refresh_layout:
                            startActivity(new Intent(mContext, RefreshLayoutActivity.class));
                    }
                    mIsItemCheck = false;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }
}
