package com.example.league95.json_demoapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {



    //Start with download Task
    public class DownloadTask extends AsyncTask<String, Void, String>
    {
        @Override
        protected String doInBackground(String... urls) {
            // our final result
            StringBuilder result = new StringBuilder();
            //Url
            URL url;
            //Http Connection
            HttpURLConnection httpURLConnection;

            try{
                //get url from the string passed to DownloadTask
                url = new URL(urls[0]);
                //Get the http connection by opening the url
                httpURLConnection = (HttpURLConnection) url.openConnection();
                //Input method, stream
                InputStream inputStream = httpURLConnection.getInputStream();
                //Reader
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                //data
                int data = inputStreamReader.read();
                char c;
                while (data != -1)
                {
                    c = (char) data;
                    result.append(c);
                    data = inputStreamReader.read();
                }
                //Finally return result string
                return result.toString();
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }

        //Post method
        //Another way to work with the result
        //The method that is called when the doInBackground
        //Method has completed!
        //And it will be passed what ever we return from the backGround
        //Method i.e our 'result' String
        //The difference is that the doInBackground method cannot
        //Interact with the U.I
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //This is what we need to read the data from the weather API
            //Since it's in JSON
            try {
                //Opens the result String as a JSON object
                //Obviously this might fail since it won't be guaranteed
                //To be JSOn.
                JSONObject jsonObject = new JSONObject(result);
                //Then, we specify the name of the JSOn object
                //In this case, we are interested in 'weather'
                jsonObject.getString("weather");
                //Get the weather content only by calling this method getString(part we are interested in);
                String weatherContent = jsonObject.getString("weather");
                //Log teh weather content
                Log.i("Weather Content: ", weatherContent);

                //Now if we want to loop through the mini JSON objects, we can do so lik this
                JSONArray jsonArray = new JSONArray(weatherContent);

                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonPart = jsonArray.getJSONObject(i);

                    Log.i("main: ", jsonPart.getString("main"));
                    Log.i("description: ", jsonPart.getString("description"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            //Log.i("Website Contetn: ", result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Setup task manager
        DownloadTask downloadTask = new DownloadTask();
        //String url
        String url = "http://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b1b15e88fa797225412429c1c50c122a1";

            downloadTask.execute(url);



    }




}
