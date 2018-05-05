package com.example.ashwiniabhishek.whatstheweather;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    boolean flag;
    TextView textView;
    String weatherInfo="";
    String main="",description="";
    class DownloadTask extends AsyncTask<String,Void,String>{
        String result = "";

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            TextView textView = findViewById(R.id.textView3);
            try {
                JSONObject jsonObject = new JSONObject(result);

                //JSONObject jsonObject3 = new JSONObject(genInfo);
                String cod = jsonObject.getString("cod");
                int code = Integer.parseInt(cod);
                if(code!=404){
                    weatherInfo = jsonObject.getString("weather");
                    String temperatureInfo = jsonObject.getString("main");
                    JSONObject jsonObject2 = new JSONObject(temperatureInfo);
                String temperature = jsonObject2.getString("temp");
                float temp = (Float.parseFloat(temperature)-273.15f);
                temp = Float.parseFloat(String.format("%.2f",temp));
                JSONArray arr = new JSONArray(weatherInfo);

                    JSONObject jsonPart = arr.getJSONObject(0);
                    main = jsonPart.getString("main");
                    description = jsonPart.getString("description");

                textView.setText("main: "+main+"\n"+"description: "+description+"\n"+"temperature: "+temp);}
                else if(code==404){
                    textView.setText("City not found");
                }
                textView.setVisibility(View.VISIBLE);
                EditText editText = findViewById(R.id.editText2);
                editText.setText("");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int  character = reader.read();
                while(character!=-1){
                    result = result + (char)character;
                    character = reader.read();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (FileNotFoundException e){
                    result = "{\"cod\":\"404\",\"message\":\"city not found\"}";
            }catch (IOException e) {
                e.printStackTrace();
            }

            return result;

        }
    }
    public void getWeather(View view){
        //if(flag)
          //  textView.setText("");
        EditText editText = findViewById(R.id.editText2);
        String city = editText.getText().toString();
        DownloadTask downloadTask = new DownloadTask();
        downloadTask.execute("http://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=16d98920670910b20d17f26bcefb7c54");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*Thread t = new Thread(new Runnable(){

            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        // Stuff that updates the UI
                        TextView textView = findViewById(R.id.textView4);

                        while(true){
                            boolean flag = isOnline();
                            if(flag){
                                String text = (String) textView.getText();
                                if(text.equals("Check internet connectivity"))
                                    textView.setText("");
                                else
                                    ;
                            }
                            else{
                                textView.setText("Check internet connectivity");
                            }
                        }

                    }
                });
            }
        });
        t.start();*/
        /*textView = findViewById(R.id.textView4);
        flag =isOnline();
        if(!flag){
            textView.setText("Check connectivity");
        }*/


    }
   /* protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }*/
}
