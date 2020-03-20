package com.rainmin.demo.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.rainmin.demo.R;
import com.rainmin.demo.service.UploadService;

public class UploadFileActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvMessage;
    private UploadService mService;
    private boolean mBound = false;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            UploadService.UploadServiceBinder binder = (UploadService.UploadServiceBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initCustomizedView(R.layout.activity_upload_file, "文件上传", true);
        tvMessage = findViewById(R.id.tv_message);
        findViewById(R.id.btn_start_upload).setOnClickListener(this);
        findViewById(R.id.btn_stop_upload).setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && tvMessage != null) {
            String message = intent.getStringExtra("upload_status");
            tvMessage.setText(message);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, UploadService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mService != null) {
            mService.stopSelf();
        }
    }

    @Override
    public void onClick(View v) {
        Intent uploadIntent = new Intent(this, UploadService.class);
        switch (v.getId()) {
            case R.id.btn_start_upload:
                uploadIntent.setAction(UploadService.ACTION_START_UPLOAD);
                startService(uploadIntent);
                break;
            case R.id.btn_stop_upload:
                uploadIntent.setAction(UploadService.ACTION_STOP_UPLOAD);
                startService(uploadIntent);
                break;
        }
    }
}
