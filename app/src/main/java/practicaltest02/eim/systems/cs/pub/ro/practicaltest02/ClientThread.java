package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by student on 25.05.2018.
 */

public class ClientThread extends Thread {

    private String address;
    private int port;
    private String currency;
    private String informationType;
    private TextView textView;

    private Socket socket;

    public ClientThread(int port, String currency, TextView textView) {
        this.address = "localhost";
        this.port = port;

        this.currency = currency;
        this.textView = textView;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(address, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }
            printWriter.println(currency);
            printWriter.flush();

            String price;
            textView.post(new Runnable() {
                @Override
                public void run() {
                    textView.setText("");
                }
            });
            while ((price = bufferedReader.readLine()) != null) {
                final String finalPrice = price;
                textView.post(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(textView.getText().toString() + "\n" + finalPrice);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }

}