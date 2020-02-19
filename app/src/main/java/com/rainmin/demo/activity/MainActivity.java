package com.rainmin.demo.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.rainmin.demo.R;
import com.rainmin.demo.utils.Utils;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private int mSelectedItem;
    private boolean mIsItemCheck;
    private Context mContext;
    private FunctionAdapter mAdapter;
    private Paint mPaint;
    private int mPaddingSize;
    private int mMarginSize;
    private RecyclerView rvFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = this;
        rvFunction = (RecyclerView) findViewById(R.id.rv_function);
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
        String[] functionNames = getResources().getStringArray(R.array.grid_function_name);
        mAdapter = new FunctionAdapter(mContext, functionNames);
        rvFunction.setAdapter(mAdapter);
        rvFunction.setHasFixedSize(true);
        mPaint = new Paint();
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 20,
                Resources.getSystem().getDisplayMetrics());
        mPaint.setTextSize(textSize);
        mPaddingSize = 20;
        mMarginSize = 20;
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        final int rvWidth = point.x;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, rvWidth);
        //动态设置每个item的宽度
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //计算文字加上padding与margin的宽度
                int textWidth = (int) mPaint.measureText(mAdapter.getItem(position)) + (mPaddingSize + mMarginSize) * 2;
                return textWidth > rvWidth ? rvWidth : textWidth;
            }
        });
        rvFunction.setLayoutManager(gridLayoutManager);
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
                    case R.id.nav_simple:
                        mSelectedItem = itemId;
                        break;
                    default:
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
                        case R.id.nav_simple:
                            showMessage("you clicked simple");
                            break;
                    }
                    mIsItemCheck = false;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        mAdapter.setOnItemClickListener(new FunctionAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(FunctionAdapter adapter, View view, int position) {
                String functionName = adapter.getItem(position);
                showMessage(functionName);
                if (TextUtils.equals(functionName, getString(R.string.NFC))) {
                    startActivity(new Intent(mContext, NfcActivity.class));
                } else {
                    TestActivity.startTest(mContext, functionName);
                }
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
        } else if (id == R.id.action_device_id) {
//            String ANDROID_ID = Settings.System.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
            String IMEI = Utils.getIMEI(getApplicationContext(), 0);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your DEVICE_ID(IMEI) is:")
                    .setMessage(IMEI)
                    .create()
                    .show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    static class FunctionAdapter extends RecyclerView.Adapter<FunctionAdapter.FunctionHolder> {

        private Context mContext;
        private List<String> mData;
        private FunctionAdapter mAdapter;
        private OnItemClickListener mItemClickListener;

        FunctionAdapter(Context context, String[] data) {
            mContext = context;
            mData = Arrays.asList(data);
            mAdapter = this;
        }

        @Override
        public FunctionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(mContext);
            textView.setTextSize(20);
            textView.setTextColor(Color.BLUE);
            textView.setBackground(mContext.getDrawable(R.drawable.selector_function_name));
            GridLayoutManager.LayoutParams lp = new GridLayoutManager.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(20, 20, 20, 0);
            textView.setLayoutParams(lp);

            FunctionHolder holder = new FunctionHolder(textView);
            bindViewClickListener(holder);

            return holder;
        }

        @Override
        public void onBindViewHolder(FunctionHolder holder, final int position) {
            holder.textView.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        String getItem(int position) {
            return mData.get(position);
        }

        void setOnItemClickListener(OnItemClickListener listener) {
            mItemClickListener = listener;
        }

        void bindViewClickListener(final FunctionHolder holder) {
            if (mItemClickListener != null) {
                holder.textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mItemClickListener.OnItemClick(mAdapter, v, holder.getAdapterPosition());
                    }
                });
            }
        }

        class FunctionHolder extends RecyclerView.ViewHolder {
            TextView textView;
            FunctionHolder(View view) {
                super(view);
                textView = (TextView) view;
            }
        }

        public interface OnItemClickListener {
            void OnItemClick(FunctionAdapter adapter, View view, int position);
        }
    }
}
