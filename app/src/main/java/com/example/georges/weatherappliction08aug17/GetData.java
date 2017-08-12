package com.example.georges.weatherappliction08aug17;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class GetData extends AsyncTask<String , Void ,String> {
    private String server_response;

    private Activity act;

    GetData(Activity activity) {
        act = activity;
    }

    @Override
    protected String doInBackground(String... strings) {

        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(strings[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                server_response = readStream(urlConnection.getInputStream());
                try {
                    JSONObject json = new JSONObject(server_response);

                    JSONObject windSpeedObject = json.getJSONObject("wind");
                    final String windSpeedString = windSpeedObject.getString("speed");

                    JSONObject windDirObject = json.getJSONObject("wind");
                    final String windDirString = windDirObject.getString("deg");

                    JSONObject latCoordObject = json.getJSONObject("coord");
                    final String latCoordString = latCoordObject.getString("lat");

                    JSONObject lonCoordObject = json.getJSONObject("coord");
                    final String lonCoordString = lonCoordObject.getString("lon");

                    JSONObject tempObject = json.getJSONObject("main");
                    final String tempString = tempObject.getString("temp");

                    final String placeNameString = json.getString("name");

                    act.runOnUiThread(new Runnable() {
                       @Override
                        public void run() {
                           TextView windvalue = (TextView) act.findViewById(R.id.windValue);
                           windvalue.setText(windSpeedString);
                           TextView winddirection = (TextView) act.findViewById(R.id.windDir);
                           winddirection.setText(windDirString);
                           TextView latitude = (TextView) act.findViewById(R.id.latCoord);
                           latitude.setText(latCoordString);
                           TextView longitude = (TextView) act.findViewById(R.id.lonCoord);
                           longitude.setText(lonCoordString);
                           TextView temperature = (TextView) act.findViewById(R.id.tempValue);
                           temperature.setText(tempString);
                           TextView placename = (TextView) act.findViewById(R.id.placeName);
                           placename.setText(placeNameString);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.v("CatalogClient", server_response);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.e("Response", "" + server_response);
    }


    // Converting InputStream to String
    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuilder response = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

}