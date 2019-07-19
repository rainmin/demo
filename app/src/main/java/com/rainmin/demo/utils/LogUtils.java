package com.rainmin.demo.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.CsvFormatStrategy;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

public class LogUtils {

    private LogUtils() {

    }

    /**
     * called in onCreate method of application
     *
     * @param tag      log tag
     * @param logPath  log存储路径
     * @param isPrint  是否打印log
     * @param isRecord 是否将log写入文件
     */
    public static void init(@NonNull String tag, @NonNull String logPath, boolean isPrint, boolean isRecord) {
        if (isPrint) {
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)
                    .methodCount(0)
                    .tag(tag)
                    .build();
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        }

        if (isRecord) {
            FormatStrategy diskFormatStrategy = CsvFormatStrategy.newBuilder()
                    .logStrategy(new MyDiskLogStrategy(logPath, "demo"))
                    .tag(tag)
                    .build();
            Logger.addLogAdapter(new DiskLogAdapter(diskFormatStrategy));
        }
    }

    public static void d(@Nullable Object object) {
        Logger.d(object);
    }

    public static void d(@NonNull String message, @Nullable Object... args) {
        Logger.d(message, args);
    }

    public static void e(@NonNull String message, @Nullable Object... args) {
        Logger.e(null, message, args);
    }

    public static void e(@Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        Logger.e(throwable, message, args);
    }

    public static void i(@NonNull String message, @Nullable Object... args) {
        Logger.i(message, args);
    }

    public static void v(@NonNull String message, @Nullable Object... args) {
        Logger.v(message, args);
    }

    public static void w(@NonNull String message, @Nullable Object... args) {
        Logger.w(message, args);
    }

    /**
     * Tip: Use this for exceptional situations to log
     * ie: Unexpected errors etc
     */
    public static void wtf(@NonNull String message, @Nullable Object... args) {
        Logger.wtf(message, args);
    }

    /**
     * Formats the given json content and print it
     */
    public static void json(@Nullable String json) {
        Logger.json(json);
    }

    /**
     * Formats the given xml content and print it
     */
    public static void xml(@Nullable String xml) {
        Logger.xml(xml);
    }
}
