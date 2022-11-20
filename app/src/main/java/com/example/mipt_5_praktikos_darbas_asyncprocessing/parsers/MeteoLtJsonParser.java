package com.example.mipt_5_praktikos_darbas_asyncprocessing.parsers;

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
            JSONObject coordinatesNode = jData.getJSONObject("coordinates");
            String lat = coordinatesNode.getString("latitude");
            String lon = coordinatesNode.getString("longitude");
            String administrativeDivision = placeNode.getString("administrativeDivision");
            result = String.format("location name: %s,\n lat: %s,\n lon: %s", administrativeDivision, lat, lon);
        }   catch (JSONException e){
            e.printStackTrace();
        } return result;
    }
}
