package com.example.mipt_5_praktikos_darbas_asyncprocessing.Utilities;

import android.os.AsyncTask;

import java.io.IOException;

public class AsyncDataLoader extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... params){
            try{
                return ApiDataReader.getValuesFromApi(params[0]);
            } catch (IOException ex){
                return String.format("Some error ocurred => %s", ex.getMessage());
            }
        }

        @Override
    protected void  onPostExecute(String result){super.onPostExecute(result);}
}
