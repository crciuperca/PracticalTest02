package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.ResponseHandler;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.BasicResponseHandler;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.protocol.HTTP;
import cz.msebera.android.httpclient.util.EntityUtils;

public class CommunicationThread extends Thread {

    private MyServerThread serverThread;
    private Socket socket;
    private EditText serverTextEditText;
    private String link = "https://api.coindesk.com/v1/bpi/currentprice/EUR.json";
    private Spinner dropdown;
    private String city;
    private String informationType;
    private String currency;
    String retStr;
    String time;
    String usd;
    String eur;

    public CommunicationThread(Socket socket, EditText serverTextEditText, Spinner dropdown, MyServerThread serverThread) {
        this.socket = socket;
        this.serverTextEditText = serverTextEditText;
        this.dropdown = dropdown;
        this.serverThread = serverThread;

    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);


            currency = bufferedReader.readLine();





            getInfo();
 //           printWriter.println(retStr);
            //printWriter.println(getInfo());
            socket.close();
            Log.v(Constants.TAG, "Conenction closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    public void getInfo() {
        this.retStr = "";
        String humidity = "";
        String temperature = "";

            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGetXKCD = new HttpGet(link );//serverTextEditText.getText().toString());
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            String pageSourceCode = null;
            Document document = new Document("Page empty");
            Element htmlTag = new Element("Empty");
            HttpResponse httpResponse;
            try {
                pageSourceCode = httpClient.execute(httpGetXKCD, responseHandler);

            } catch (ClientProtocolException clientProtocolException) {
                Log.e(Constants.TAG, clientProtocolException.getMessage());
                if (Constants.DEBUG) {
                    clientProtocolException.printStackTrace();
                }
            } catch (IOException ioException) {
                Log.e(Constants.TAG, ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
            if (pageSourceCode != null) {

                try {
                    JSONObject content = new JSONObject(pageSourceCode);
                    time = content.getJSONObject("time").getString("updated");

                        usd = content.getJSONObject("bpi").getJSONObject("USD").getString("rate");

                        eur = content.getJSONObject("bpi").getJSONObject("EUR").getString("rate");
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                PrintWriter printWriter = null;
                try {
                    printWriter = Utilities.getWriter(socket);
                    printWriter.println(time);
                    if(currency.equals("USD"))
                        printWriter.println(usd);
                    if(currency.equals("EUR"))
                        printWriter.println(eur);
                    printWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }





            }




    }

}
