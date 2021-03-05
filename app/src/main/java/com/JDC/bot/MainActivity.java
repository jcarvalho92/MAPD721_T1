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
import android.widget.TextView;

import com.JDC.bot.service.ChatBotService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView messages;
    List<String> chatMessages = new ArrayList<>();
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatMessages.add("Hello "+ ChatBotService.NAME+"!");
        chatMessages.add("How are you?");
        chatMessages.add("Good Bye "+ChatBotService.NAME+"!");

        //Getting a reference
        messages = findViewById(R.id.txtMessages);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SERVICE_RESPONSE_GENERATE_MESSAGE");
        registerReceiver(chatBotServiceResponseReceiver, intentFilter);

        Button btnGenerateMsg = (Button) findViewById(R.id.btnGenerateMsg);
        btnGenerateMsg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                generateMessage(chatMessages.get(i));

                if(i == chatMessages.size() - 1){
                    i = 0;
                }
                else{
                    i++;
                }

            }
        });

        Button btnStopService = (Button) findViewById(R.id.btnStopService);
        btnStopService.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                stopService();
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

    private void generateMessage(String messageText){
        Bundle data = new Bundle();
        data.putInt(ChatBotService.CMD, ChatBotService.CMD_GENERATE_MSG);
        data.putString(ChatBotService.MESSAGE_TEXT, messageText);

        Intent intent = new Intent(getApplicationContext(), ChatBotService.class);
        intent.putExtras(data);
        startService(intent);
    }

    private void stopService(){
        Bundle data = new Bundle();
        data.putInt(ChatBotService.CMD, ChatBotService.CMD_STOP_SERVICE);

        Intent intent = new Intent(getApplicationContext(), ChatBotService.class);
        intent.putExtras(data);
        startService(intent);
    }
}