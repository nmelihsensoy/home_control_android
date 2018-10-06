package com.nmelihsensoy.homecontrol;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by nmelihsensoy on 17.05.2017.
 */

public class NotificationService extends Service {

    private final String LOG_TAG = "NotificationService";
    Notification status;
    private Socket mSocket;
    private boolean isConnected;

    {
        try {
            mSocket = IO.socket(Constant.SERVER);
        } catch (URISyntaxException e) {
        }
    }

    @Override
    public void onDestroy() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.cancel(0);
        //mSocket.disconnect();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals("LAMP")) {
            sendEmit("/melih/oda/lamp", "ON");
        } else if (intent.getAction().equals("UNLCK")) {
            sendEmit("/melih/doorServo", "OP");

        } else if (intent.getAction().equals("LOCK")) {
            sendEmit("/melih/doorServo", "LO");

        } else if (intent.getAction().equals("APT")) {
            sendEmit("/melih/outDoor", "ON");

        } else if (intent.getAction().equals("STOP")) {
            Toast.makeText(this, "STOP", Toast.LENGTH_SHORT).show();
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            mNotificationManager.cancel(0);
        } else if (intent.getAction().equals("FRS")) {

            showNotification();

        } else {

            Toast.makeText(this, "" + intent.getAction(), Toast.LENGTH_SHORT).show();
        }
        return START_NOT_STICKY;
    }

    private void showNotification() {
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.status_layout);
        //views.setImageViewResource(R.id.status_play,R.mipmap.ic_launcher);


        Intent Play = new Intent(this, NotificationService.class);
        Play.setAction("LAMP");
        PendingIntent PlayPend = PendingIntent.getService(this, 0, Play, 0);
        views.setOnClickPendingIntent(R.id.lamp, PlayPend);

        Play.setAction("UNLCK");
        PendingIntent PausePend = PendingIntent.getService(this, 0, Play, 0);
        views.setOnClickPendingIntent(R.id.unlock, PausePend);

        Play.setAction("LOCK");
        PendingIntent LockPend = PendingIntent.getService(this, 0, Play, 0);
        views.setOnClickPendingIntent(R.id.lock, LockPend);

        Play.setAction("APT");
        PendingIntent AptPend = PendingIntent.getService(this, 0, Play, 0);
        views.setOnClickPendingIntent(R.id.lockApt, AptPend);

        Play.setAction("STOP");
        PendingIntent StopePend = PendingIntent.getService(this, 0, Play, 0);
        views.setOnClickPendingIntent(R.id.stop, StopePend);


        NotificationCompat.Builder builder =
                (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.abc_ic_menu_share_mtrl_alpha)
                        .setContentTitle("title text")
                        .setContentText("content text");
        builder.setContent(views);
        builder.setCustomBigContentView(views);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
        builder.setPriority(Notification.PRIORITY_MIN);
    }

    private void sendEmit(String topic, String payload) {
        if (!mSocket.connected()) {
            mSocket.connect();
            final JSONObject obj = new JSONObject();
            try {
                obj.put("topic", topic);
                obj.put("payload", payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("publish", obj, new Ack() {
                @Override
                public void call(Object... args) {
                    System.out.println("acknowledge " + args);
                    Toast.makeText(getApplicationContext(), "SENDD", Toast.LENGTH_SHORT).show();
                }
            });
            //mSocket.emit("publish", obj);
        } else {
            final JSONObject obj = new JSONObject();
            try {
                obj.put("topic", topic);
                obj.put("payload", payload);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mSocket.emit("publish", obj, new Ack() {
                @Override
                public void call(Object... args) {
                    System.out.println("acknowledge " + args);
                    Toast.makeText(getApplicationContext(), "SENDD", Toast.LENGTH_SHORT).show();
                }
            });
            // mSocket.emit("publish", obj);
        }
    }

}
