package com.rainmin.demo.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.text.TextUtils;

import com.rainmin.demo.R;
import com.rainmin.demo.service.UploadService;
import com.rainmin.demo.utils.LogUtils;

public class WifiStateReceiver extends BroadcastReceiver {
    
    private final static String TAG = "chenming";
    private final static String ACTION_LIGHTUP = "com.rainmin.demo.action.lightup";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (networkInfo.getState().equals(NetworkInfo.State.CONNECTING)) {
                LogUtils.d("NETWORK_STATE_CHANGED_ACTION >>> wifi is connecting...");
            } else if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                LogUtils.d("NETWORK_STATE_CHANGED_ACTION >>> wifi is connected!");
                WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                if (wifiInfo != null) {
                    LogUtils.d("NETWORK_STATE_CHANGED_ACTION >>> the connected ssid is: " + wifiInfo.getSSID());
                }
            } else if (networkInfo.getState().equals(NetworkInfo.State.DISCONNECTING)) {
                LogUtils.d("NETWORK_STATE_CHANGED_ACTION >>> wifi is disconnecting...");
            } else if (networkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                LogUtils.d("NETWORK_STATE_CHANGED_ACTION >>> wifi is disconnected!");
            }
        } else if (TextUtils.equals(action, WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
            boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
//            if (connected) {
//                LogUtils.d("SUPPLICANT_CONNECTION_CHANGE_ACTION: wifi is supplicant connected!");
//            } else {
//                LogUtils.d("SUPPLICANT_CONNECTION_CHANGE_ACTION: wifi is supplicant disconnected!");
//            }
        } else if (TextUtils.equals(action, WifiManager.SUPPLICANT_STATE_CHANGED_ACTION)) {
            SupplicantState state = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
//            LogUtils.d("SUPPLICANT_STATE_CHANGED_ACTION >>> state is " + state);
        } else if (TextUtils.equals(action, ACTION_LIGHTUP)) {
            boolean run = intent.getBooleanExtra("service_run", false);
            Intent uploadIntent = new Intent(context, UploadService.class);
            uploadIntent.putExtra("service_run", run);
            context.startService(uploadIntent);
        }
    }

    private void lightupScreen(Context context) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        try {
            if (pm != null && !pm.isInteractive()) {
                PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP
                        | PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE, "chenming:lightup");
                wakeLock.acquire(10*1000);
                LogUtils.d("light up the screen");
            } else {
                LogUtils.d("the screen is interactive");
            }
        } catch (Exception e) {
            LogUtils.e("cause an exception while light up the screen: " + e.getMessage());
        }
    }

    private void sendNotification(Context context, boolean run) {
        LogUtils.d("receiver: send a Notification");
        Intent intent = new Intent(context, UploadService.class);
        intent.putExtra("service_run", run);
        PendingIntent pendingIntent = PendingIntent.getService(context, 110, intent, PendingIntent.FLAG_ONE_SHOT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setTicker("have a notice");
        builder.setContentTitle("notice");
        builder.setContentText("test demo");
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);
        builder.setFullScreenIntent(pendingIntent, true);
        manager.notify(1, builder.build());
    }
}
