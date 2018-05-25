package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private EditText portServer;
    private EditText currency;
    private Button start;
    private Button go;
    private TextView textView;


    private MyServerThread serverThread;
    private ClientThread clientThread;

    private Listener1 listener1 = new Listener1();
    private class Listener1 implements View.OnClickListener {
        @Override
        public void onClick(View view) {


                serverThread = new MyServerThread(Integer.parseInt(portServer.getText().toString()));
                serverThread.startServer();

                Log.v(Constants.TAG, "Starting server...");

        }
    }

    private Listener2 listener2 = new Listener2();
    private class Listener2 implements View.OnClickListener {
        @Override
        public void onClick(View view) {

            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }

            clientThread = new ClientThread(Integer.parseInt(portServer.getText().toString()), currency.getText().toString(), textView);
            clientThread.start();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        portServer = (EditText)findViewById(R.id.editText1);
        currency = (EditText)findViewById(R.id.editText2);
        textView = (TextView)findViewById(R.id.textView);

        start = (Button)findViewById(R.id.button1);
        go = (Button)findViewById(R.id.button2);

        start.setOnClickListener(listener1);
        go.setOnClickListener(listener2);

        portServer.setText("2017");
        currency.setText("USD");
    }
}
