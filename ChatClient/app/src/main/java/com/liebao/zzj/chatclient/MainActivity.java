package com.liebao.zzj.chatclient;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button btsend;
    private Button btconnect;
    private EditText etip;
    private EditText etmessage;
    private ListView lvmessage;

    ArrayList<String> mData = new ArrayList<String>();
    MyAdapter myAdapter;

    private Socket socket = null;

    public static final int RECIEVEMESSAGE = 1001;
    public static final int SETBUTTONSTATE = 1002;
    public static final int RESETETMESSAGE = 1003;
    MyClient myClient;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RECIEVEMESSAGE:
                    Bundle bundle = msg.getData();
                    mData.add(mData.size(), bundle.getString("msg"));
                    lvmessage.setAdapter(myAdapter);
                    lvmessage.setSelection(mData.size() - 1);
                    break;
                case SETBUTTONSTATE:
                    btconnect.setEnabled(false);
                    btsend.setEnabled(true);
                    break;
                case RESETETMESSAGE:
                    etmessage.setText("");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btsend = (Button) findViewById(R.id.bt_send);
        btconnect = (Button) findViewById(R.id.bt_connect);
        lvmessage = (ListView) findViewById(R.id.lv_message);
        etip = (EditText) findViewById(R.id.et_ip);
        etmessage = (EditText) findViewById(R.id.et_message);

        myAdapter = new MyAdapter(this, mData);
        lvmessage.setAdapter(myAdapter);

        btconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new myChatThread().start();
                new Thread(new Runnable() {
                    @Override
                    public synchronized void run() {
                        if (myClient == null) {
                            MyClient myClient1 = new MyClient();
                            myClient = myClient1;
                        }
                        try {
                            wait(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (myClient.socket.isConnected()) {
                            Message msg = new Message();
                            msg.what = SETBUTTONSTATE;
                            handler.sendMessage(msg);
                            myClient.receivemessage(handler);
                        }
                    }
                }).start();
            }
        });

        btsend.setOnClickListener(new View.OnClickListener() {
            Message msg = new Message();
            @Override
            public void onClick(View v) {
                //new myChatThread().start();
                new Thread(new Runnable() {
                    @Override
                    public synchronized void run() {
                        myClient.sendmessage(etmessage.getText().toString());
                        msg.what = RESETETMESSAGE;
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        myClient.uninit();
    }
}



