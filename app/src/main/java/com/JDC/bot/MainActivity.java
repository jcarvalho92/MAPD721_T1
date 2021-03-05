package com.JDC.bot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    TextView messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SERVICE_RESPONSE_GENERATE_MESSAGE");
        registerReceiver(chatBotServiceResponseReceiver, intentFilter);

        Button btnGenerateMsg = (Button) findViewById(R.id.btnGenerateMsg);
        btnGenerateMsg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Getting a reference
                messages = findViewById(R.id.txtMessages);

                Bundle data = new Bundle();
                data.putInt(ChatBotService.CMD, ChatBotService.CMD_GENERATE_MSG);
                data.putString(ChatBotService.MESSAGE_TEXT, "Hello!");
                Intent intent = new Intent(getApplicationContext(), ChatBotService.class);
                intent.putExtras(data);
                startService(intent);

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(chatBotServiceResponseReceiver);
    }

    private final BroadcastReceiver chatBotServiceResponseReceiver = new BroadcastReceiver() {
        private static final String TAG = "BroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle data = intent.getExtras();
            String valueString = data.getString("value");

            Log.d(TAG, "received broadcast message from service: " + action);

            messages.append(valueString+ "\n");

        }

    };
}