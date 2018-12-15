package com.arduinoproject.moldovanbalazs.joystick;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPClient {

    private String serverMessage;
    public final String SERVERIP = "192.168.56.1";
    public final int SERVERPORT = 8000;
    Socket clientSocket;

    PrintWriter out;
    BufferedReader in;

    public TCPClient() {
        try {
            clientSocket = new Socket(SERVERIP, SERVERPORT);
            Log.e("clientSocket: ", clientSocket.toString());
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            Log.e("out in method:", out.toString());
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        if(out != null) {
            out.println(message);
        } else {
            Log.e("Nullpointer at out:", "oh fuck!");
        }

    }

    public void endConnection() throws IOException {
        this.clientSocket.close();
    }
}
