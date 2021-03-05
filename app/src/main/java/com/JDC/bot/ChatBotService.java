package com.JDC.bot;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class ChatBotService extends Service {

    private static final String TAG = "ChatBotService";

    public static final String CMD = "cmd";
    public static final int CMD_GENERATE_MSG = 1;
    public static final int CMD_STOP_SERVICE = 2;
    public static final String MESSAGE_TEXT = "message";

    public ChatBotService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
                sendBroadcastConnected(responseMessage);

            } else if (command == CMD_STOP_SERVICE) {

            } else {
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