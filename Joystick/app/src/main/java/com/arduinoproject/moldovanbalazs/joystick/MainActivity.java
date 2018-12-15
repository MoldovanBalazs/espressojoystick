package com.arduinoproject.moldovanbalazs.joystick;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements JoystickView.JoyStickListener{

    TCPClient tcpClient;
    Socket clientSocket;
    DataOutputStream dout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main );
        JoystickView joystickView = new JoystickView(this);
        //tcpClient = new TCPClient();
        addContentView(joystickView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));



    }

    @Override
    public synchronized void onJoyStickMoved(final float xPercent,final float yPercent, int id) {
        Log.d("main method:"," xPrecent " + xPercent + " yPercent " + yPercent);

        Thread thread = new Thread() {
            public void run() {
                try {
                    InetAddress serverAddress = InetAddress.getByName("192.168.4.1");
                    Socket connection = new Socket(serverAddress, 80);

                    Log.e("Connection", "Connection is: " + connection.toString());
                    DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                    PrintWriter pw = new PrintWriter(new DataOutputStream(connection.getOutputStream()));
                    /*pw.write("this is message");
                    pw.flush();
                    pw.close();*/

                    output.writeUTF("/x:/" + String.valueOf(-(int) (xPercent * 100) ) + "\n");
                    output.flush();
                    output.writeUTF("/y:/" + String.valueOf(-(int) (yPercent * 100) ) + "\n");
                    output.flush();
                    output.close();
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        //tcpClient.sendMessage(" xPrecent " + xPercent + " yPercent " + yPercent);
       /* try {
            dout.writeUTF(" xPrecent " + xPercent + " yPercent " + yPercent);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //textView.setText(String.valueOf(xPercent));
    }


}
