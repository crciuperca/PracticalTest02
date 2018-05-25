package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by student on 25.05.2018.
 */

public class MyServerThread extends Thread {

    private boolean isRunning;

    private ServerSocket serverSocket;

    private EditText serverTextEditText;

    private int port;

    private Spinner dropdown;

    private String price;

    public MyServerThread(int port) {
        this.port = port;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        Log.v(Constants.TAG, "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket, serverTextEditText, dropdown, this);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    public synchronized void setData(String price) {
        this.price = price;
    }

    public synchronized String getData() {
        return this.price;
    }

}