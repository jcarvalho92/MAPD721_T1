package com.JDC.bot.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.JDC.bot.MainActivity;
import com.JDC.bot.notification.NotificationDecorator;

public class ChatBotService extends Service {

    private static final String TAG = "ChatBotService";

    public static final String CMD = "cmd";
    public static final int CMD_GENERATE_MSG = 1;
    public static final int CMD_STOP_SERVICE = 60;
    public static final String MESSAGE_TEXT = "message";
    public static final String NAME = "Juliana";

    private NotificationManager notificationMgr;
    private NotificationDecorator notificationDecorator;

    public ChatBotService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        super.onCreate();
        notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationDecorator = new NotificationDecorator(this, notificationMgr);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Bundle data = intent.getExtras();

            int command = data.getInt(CMD);
            Log.d(TAG, "-(<- received command data to service: command=" + command);

            if (command == CMD_GENERATE_MSG) {

                String responseMessage = data.getString(MESSAGE_TEXT);
                notificationDecorator.displaySimpleNotification("Generating message...", responseMessage);

                sendBroadcastConnected(responseMessage);

            }
            else if (command == CMD_STOP_SERVICE) {
                notificationDecorator.displaySimpleNotification("ChatBot Stopped: ", String.valueOf(CMD_STOP_SERVICE));
                stopSelf();
            }
            else {
                Log.w(TAG, "Ignoring Unknown Command! id=" + command);
            }
        }
        return START_STICKY;
    }

    private void sendBroadcastConnected(String responseMessage) {
        Log.d(TAG, "->(+)<- sending broadcast: BROADCAST_SERVER_CONNECTED");
        Intent responseIntent = new Intent();

        Bundle responseData = new Bundle();
        responseData.putString("value",responseMessage);

        responseIntent.putExtras(responseData);

        responseIntent.setAction("SERVICE_RESPONSE_GENERATE_MESSAGE");

        sendBroadcast(responseIntent);
    }
}