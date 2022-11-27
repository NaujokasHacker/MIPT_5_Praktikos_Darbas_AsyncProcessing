package com.example.mipt_5_praktikos_darbas_asyncprocessing.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MeteoLtJsonParser {
    public static String getKaunasWeatherForecast(InputStream stream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line = "";
        String data = "";
        while (line!=null){
            line = bufferedReader.readLine();
            data= data + line;
        }


        String result= "";
        try{
            JSONObject jData = new JSONObject(data);
            JSONObject placeNode = jData.getJSONObject("place");

            JSONObject coordinatesNode = placeNode.getJSONObject("coordinates");

            JSONArray weather = jData.getJSONArray("forecastTimestamps");
            JSONObject weather1 = weather.getJSONObject(0);


            String lat = coordinatesNode.getString("latitude");
            String lon = coordinatesNode.getString("longitude");

            String time = weather1.getString("forecastTimeUtc");
            String temp = weather1.getString("airTemperature");
            String fLike = weather1.getString("feelsLikeTemperature");

            String administrativeDivision = placeNode.getString("administrativeDivision");
            result = String.format("location name: %s,\n lat: %s,\n lon: %s,\n forTimeUtc: %s,\n airTemp: %s,\n feelsLiketemp: %s", administrativeDivision, lat, lon, time, temp, fLike);
        }
        catch (JSONException e){
            e.printStackTrace();
        } return result;
    }
}
