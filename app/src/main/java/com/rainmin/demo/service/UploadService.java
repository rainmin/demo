package com.rainmin.demo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.rainmin.demo.R;
import com.rainmin.demo.activity.UploadFileActivity;
import com.rainmin.demo.utils.LogUtils;

import java.io.File;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.io.RandomAccessFile;
import java.lang.ref.WeakReference;
import java.net.Socket;

public class UploadService extends Service {

    private final static String TAG = "chenming";
    private static final String CHANNEL_ID = "com.rainmin.demo";
    private static final String CHANNEL_NAME = "rainmin";
    public static final String ACTION_START_UPLOAD = "action_start_upload";
    public static final String ACTION_STOP_UPLOAD = "action_stop_upload";
//    public static final String UPLOAD_FILE_PATH = "/storage/emulated/0/DCIM/Camera/VID_20200319_144821.mp4";
    public static final String UPLOAD_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/Download/cike.mkv";
    private static final int NOTIFY_ID_UPLOAD = 1993;
    private static final String SERVER_HOST = "192.168.1.4";
    private static final int SERVER_PORT = 7878;

    private PowerManager.WakeLock mWakeLock;
    private CountThread mCountThread;
    private boolean mRun = true;
    private final ServiceHandler mHandler = new ServiceHandler(this);
    private NotificationManager mNotifyMgr;
    private NotificationCompat.Builder mNotifyBuilder;
    private UploadServiceBinder mBinder = new UploadServiceBinder();

    public class UploadServiceBinder extends Binder {
        public UploadService getService() {
            return UploadService.this;
        }
    }

    class CountThread extends Thread {
        int count;

        @Override
        public void run() {
            super.run();
            while (mRun) {
                count++;
                LogUtils.d("UploadService: count = " + count);
                try {
                    Thread.sleep(10 * 1000);
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
//        keepCpuAlive(this);
//        mCountThread = new CountThread();
//        mCountThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.d("UploadService: onStartCommand...");
//        boolean run = intent.getBooleanExtra("service_run", true);
//        if (!run) {
//            mRun = false;
//            LogUtils.d("UploadService: stop service");
//            stopSelf();
//        }
        if (intent != null) {
            String action = intent.getAction();
            if (TextUtils.equals(action, ACTION_START_UPLOAD)) {
                File file = new File(UPLOAD_FILE_PATH);
                if (file.exists()) {
                    uploadFile(file);
                } else {
                    LogUtils.e("file is not exist");
                }
            } else if (TextUtils.equals(action, ACTION_STOP_UPLOAD)) {
                mRun = false;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtils.d("UploadService: onBind...");
        return mBinder;
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

    private Notification generateNotification(String title, String content) {
        if (mNotifyMgr == null) {
            mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        Intent intent = new Intent(this, UploadFileActivity.class);
        intent.putExtra("upload_status", "文件已经上传完成");
        // TODO: 2019/7/26 intent.setExtra 填充同步状态数据

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setSmallIcon(R.drawable.ic_avatar)
                .setContentTitle(title)
                .setContentText(content)
                .setProgress(100, 0, false)
                .setContentIntent(pendingIntent)
                .setFullScreenIntent(pendingIntent, true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("rainmin's demo");
            mNotifyMgr.createNotificationChannel(channel);
        }
        mNotifyBuilder = builder;
        return builder.build();
    }

    private static class ServiceHandler extends Handler {
        private final WeakReference<UploadService> mService;

        ServiceHandler(UploadService service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 119) {
                int progress = msg.getData().getInt("size");
                mService.get().mNotifyBuilder.setProgress(100, progress, false);
                mService.get().mNotifyBuilder.setContentText("上传中 " + progress + "%");
                mService.get().mNotifyMgr.notify(NOTIFY_ID_UPLOAD, mService.get().mNotifyBuilder.build());
            } else if (msg.what == 120) {
                mService.get().mNotifyBuilder.setProgress(100, 100, false);
                mService.get().mNotifyBuilder.setContentText("上传完成");
                mService.get().mNotifyMgr.notify(NOTIFY_ID_UPLOAD, mService.get().mNotifyBuilder.build());
            } else if (msg.what == 121) {
                mService.get().stopForeground(true);
                mService.get().stopSelf();
            }
        }
    }

    private void uploadFile(final File uploadFile) {
        startForeground(NOTIFY_ID_UPLOAD, generateNotification("上传文件", "上传中0%"));
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    UploadLogService logService = new UploadLogService();
                    String sourceId = logService.getBindId(uploadFile);
                    String head = "Content-Length=" + uploadFile.length() + ";filename=" + uploadFile.getName() + ";sourceid=" +
                            (sourceId == null ? "" : sourceId) + "\r\n";
                    Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
                    LogUtils.d("connecting to " + SERVER_HOST + " : " + SERVER_PORT + "...");
                    OutputStream outStream = socket.getOutputStream();
                    outStream.write(head.getBytes());

                    PushbackInputStream inStream = new PushbackInputStream(socket.getInputStream());
                    String response = StreamTool.readLine(inStream);
                    LogUtils.d("server response header is: " + response);
                    String[] items = response.split(";");
                    String responseid = items[0].substring(items[0].indexOf("=") + 1);
                    String position = items[1].substring(items[1].indexOf("=") + 1);
                    if (sourceId == null) {//代表原来没有上传过此文件，往数据库添加一条绑定记录
                        logService.save(responseid, uploadFile);
                    }
                    RandomAccessFile fileOutStream = new RandomAccessFile(uploadFile, "r");
                    fileOutStream.seek(Long.parseLong(position));
                    byte[] buffer = new byte[1024];
                    int len = -1;
                    long length = Long.parseLong(position);
                    int lastProgress = 0;
                    int currentProgress = 0;
                    LogUtils.d("start upload file");
                    while (mRun && (len = fileOutStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                        length += len;
                        currentProgress = (int) (length * 100 / uploadFile.length());
                        if ((currentProgress - lastProgress) >= 1) {
                            lastProgress = currentProgress;
                            Message msg = new Message();
                            msg.what = 119;
                            msg.getData().putInt("size", currentProgress);
                            mHandler.sendMessage(msg);
                            Log.d("chenming", "current progress is: " + currentProgress + "%");
                        }
                    }
                    fileOutStream.close();
                    outStream.close();
                    inStream.close();
                    socket.close();
                    if (length == uploadFile.length()) {
                        logService.delete(uploadFile);
                        Message msg = new Message();
                        msg.what = 120;
                        mHandler.sendMessage(msg);
                        LogUtils.d("upload file has completed...");
                    } else {
                        Message msg = new Message();
                        msg.what = 121;
                        mHandler.sendMessage(msg);
                        LogUtils.d("upload file is stopping...");
                    }
                } catch (Exception e) {
                    LogUtils.e(e, e.getMessage());
                }
            }
        }).start();
    }
}
