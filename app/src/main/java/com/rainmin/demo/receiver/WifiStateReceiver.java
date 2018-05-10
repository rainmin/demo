package com.rainmin.demo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.rainmin.demo.service.UploadService;

public class WifiStateReceiver extends BroadcastReceiver {
    
    private final static String TAG = "chenming";
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            if (info.getState().equals(NetworkInfo.State.CONNECTING)) {
                Toast.makeText(context, "wifi is connecting...", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"wifi is connecting...");
            } else if (info.getState().equals(NetworkInfo.State.CONNECTED)) {
                Toast.makeText(context, "wifi is connected!", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"wifi is connected!");
            } else if (info.getState().equals(NetworkInfo.State.DISCONNECTING)) {
                Toast.makeText(context, "wifi is disconnecting...", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"wifi is disconnecting...");
            } else if (info.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                Toast.makeText(context, "wifi is disconnected!", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"wifi is disconnected!");
            }
        } else if (TextUtils.equals(action, WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
            if (wifistate == WifiManager.WIFI_STATE_DISABLED) {
                Toast.makeText(context, "wifi is disabled!", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"wifi is disabled!");
            } else if (wifistate == WifiManager.WIFI_STATE_ENABLED) {
                Toast.makeText(context, "wifi is enabled!", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"wifi is enabled!");
            }
        } else if (TextUtils.equals(action, WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {                Toast.makeText(context, "wifi is supplicant connected!", Toast.LENGTH_SHORT).show();
            Log.d(TAG,"wifi supplicant state change");

            boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
            if (connected) {
                Toast.makeText(context, "wifi is supplicant connected!", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"wifi is supplicant connected!");
                context.startService(new Intent(context, UploadService.class));
            } else {
                Toast.makeText(context, "wifi is supplicant disconnected!", Toast.LENGTH_SHORT).show();
                Log.d(TAG,"wifi is supplicant disconnected!");
            }
        }
    }
}
