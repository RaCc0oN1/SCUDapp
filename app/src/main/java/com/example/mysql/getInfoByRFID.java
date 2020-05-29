package com.example.mysql;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class getInfoByRFID extends Thread {
    String id;
    String name = "oops";
    String surname = "oops";
    String middlename = "oops";
    String rfid_id;
    int btnstate;
    InputStream is = null;
    String result = null;
    String line = null;

    public void run() {
        // создаем лист для отправки запросов
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        // один параметр, если нужно два и более просто добоовляем также
        nameValuePairs.add(new BasicNameValuePair("RFID_ID", rfid_id));

        //  подключаемся к php запросу и отправляем в него id
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://racc0on.xyz/get_floByRFID.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1 ", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1 ", e.toString());
        }

        // получаем ответ от php запроса в формате json
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("pass 2 ", "connection success " + result);
        } catch (Exception e) {
            Log.e("Fail 2 ", e.toString());
        }

        // обрабатываем полученный json
        try {
            JSONObject json_data = new JSONObject(result);
            surname = (json_data.getString("Surname"));
            name = (json_data.getString("Name"));
            middlename = (json_data.getString("Middlename"));
            btnstate = (json_data.getInt("Access"));
            id = (json_data.getString("ID"));
            Log.e("pass 3 ", name);
        } catch (Exception e) {
            Log.e("Fail 3 ", e.toString());
        }
    }

    // принемаем id при запуске потока
    public void start(String rfidsm) {
        this.rfid_id = rfidsm;
        this.start();
    }

    public String resname() {
        return name;
    }

    public String ressurname() {
        return surname;
    }

    public String resmiddlename() {
        return middlename;
    }

    public int resbtnstate(){
        return btnstate;
    }


    public String getIde() {
        return id;
    }

}
