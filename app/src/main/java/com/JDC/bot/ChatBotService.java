package com.JDC.bot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChatBotService extends Service {
    public ChatBotService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}