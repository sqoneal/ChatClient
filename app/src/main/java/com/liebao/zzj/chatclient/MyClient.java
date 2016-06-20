package com.liebao.zzj.chatclient;

/**
 * Created by kszen on 6/15/2016.
 */

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MyClient {

    // private BufferedReader br;
    private DataOutputStream dos;
    private DataInputStream dis;
    private Socket socket;
    public String messagestr;
    //public boolean flag = true; // 用于控制循环结束

    public MyClient() {
        try {
            //this.br = new BufferedReader(new InputStreamReader(System.in)); // 用于从控制台接受输入的信息，再发送到服务器
            this.socket = new Socket("192.168.31.209", 8899);
            this.dos = new DataOutputStream(socket.getOutputStream()); // 向服务器写数据的输出流
            this.dis = new DataInputStream(socket.getInputStream()); // 获取服务器返回数据的输入流
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receivemessage(Handler handler) {
        while (this.socket.isConnected()) {
            try {
                while ((messagestr = this.dis.readUTF()) != null) {
                    Message message = new Message();
                    message.what = MainActivity.RECIEVEMESSAGE;
                    Bundle b = new Bundle();
                    b.putString("msg", messagestr);
                    message.setData(b);
                    handler.sendMessage(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendmessage(String str) {
        if (this.socket.isConnected()) {
            try {
                //str = "from client:" + br.readLine();
                if ("exit".equals(str)) { // 客户端终止发送信息标记 exit
                    //this.br.close();
                    this.dos.writeUTF(str);
                    this.dos.flush();

                    String res = dis.readUTF();
                    System.out.println(res);

                    this.dis.close();
                    this.dos.close();
                    //this.flag = false;
                } else {
                    this.dos.writeUTF(str);// 每读一行就发送一行
                    this.dos.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void uninit(){
        try {
            this.dis.close();
            this.dos.close();
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}