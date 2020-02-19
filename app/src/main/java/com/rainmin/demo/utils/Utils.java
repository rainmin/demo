package com.rainmin.demo.utils;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;

import java.lang.reflect.Method;

/**
 * Created by chenming on 2017/11/10
 */

public class Utils {
    
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static float dp2pxF(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float sp2pxF(Context context, float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    /**
     * 获取设备的IMEI
     *
     * @param slotId slotId为卡槽Id，它的值为 0、1；
     * @return IMEI
     */
    public static String getIMEI(Context context, int slotId) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (manager == null) {
                Log.e("rainmin", "getIMEI,manager is null");
                return null;
            } else {
                Method method = manager.getClass().getMethod("getImei", int.class);
                return (String) method.invoke(manager, slotId);
            }
        } catch (Exception e) {
            Log.getStackTraceString(e);
            return null;
        }
    }
}
