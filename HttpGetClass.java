package com.example.mohsin.tsgarbagemonitor;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by mohsin on 12/3/18.
 */

public class HttpGetClass extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... strings) {
        String result = "";
        String url = strings[0];
        String inputLine;

        try {
            URL myUrl = new URL(url);

            HttpURLConnection connection =  (HttpURLConnection) myUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.connect();
            InputStreamReader inputStreamReader= new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            while ( (inputLine =reader.readLine())!=null){
                stringBuilder.append(inputLine);
            }
            reader.close();
            inputStreamReader.close();
            result = stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}
