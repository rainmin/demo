package com.rainmin.demo.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;

import com.rainmin.demo.utils.LogUtils;

public class UploadService extends Service {

    private final static String TAG = "chenming";
    private PowerManager.WakeLock mWakeLock;
    private CountThread mCountThread;
    private boolean mRun = true;

    class CountThread extends Thread {
        int count;
        @Override
        public void run() {
            super.run();
            while (mRun) {
                count++;
                LogUtils.d("UploadService: count = " + count);
                try {
                    Thread.sleep(10*1000);
                } catch (InterruptedException e) {
                    LogUtils.d("UploadService: occur InterruptedException");
                    e.printStackTrace();
                }
            }
        }
    }

    public UploadService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.d("UploadService: onCreate...");
        keepCpuAlive(this);
        mCountThread = new CountThread();
        mCountThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("UploadService: onStartCommand...");
        boolean run = intent.getBooleanExtra("service_run", true);
        if (!run) {
            mRun = false;
            LogUtils.d("UploadService: stop service");
            stopSelf();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d("UploadService: onBind...");
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtils.d("UploadService: onUnbind...");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtils.d("UploadService: onDestroy...");
        if (mWakeLock != null && mWakeLock.isHeld()) {
            mWakeLock.release();
            LogUtils.d("UploadService: release wakelock");
        }
    }

    private void keepCpuAlive(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        try {
            if (pm != null) {
                mWakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "chenming:keepAlive");
                mWakeLock.acquire();
                LogUtils.d("UploadService: start to keep cpu alive");
            } else {
                LogUtils.d("UploadService: the PowerManager is null");
            }
        } catch (Exception e) {
            LogUtils.e("UploadService: cause an exception while keep cpu alive: " + e.getMessage());
        }
    }
}
