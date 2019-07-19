package com.rainmin.demo;

import android.app.Application;
import android.os.Environment;
import android.text.TextUtils;

import com.rainmin.demo.utils.LogUtils;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String logPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        if (!TextUtils.isEmpty(logPath)) {
            LogUtils.init("chenming", logPath, true, true);
        }
    }
}
