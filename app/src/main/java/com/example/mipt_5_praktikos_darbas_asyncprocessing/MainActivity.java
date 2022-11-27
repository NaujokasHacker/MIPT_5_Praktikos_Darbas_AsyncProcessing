package com.example.mipt_5_praktikos_darbas_asyncprocessing;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.mipt_5_praktikos_darbas_asyncprocessing.Utilities.ApiDataReader;
import com.example.mipt_5_praktikos_darbas_asyncprocessing.Utilities.AsyncDataLoader;
import com.example.mipt_5_praktikos_darbas_asyncprocessing.Utilities.Constants;

public class MainActivity extends AppCompatActivity {
    private ListView lvItems;
    private TextView tvStatus;
    private ArrayAdapter listAdapter;
    private Switch swUseAsyncTask;

    List<String> listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lvItems = findViewById(R.id.lvItems);
        this.tvStatus = findViewById(R.id.tvStatus);
        this.swUseAsyncTask = findViewById(R.id.swUseAsyncTask);

    }

    public void onBtnGetDataClick(View view) {
        this.tvStatus.setText(R.string.Kraunama);
        if (this.swUseAsyncTask.isChecked()) {
            getDataByAsyncTask();
            Toast.makeText(this, R.string.NaudojamaAsyncTask, Toast.LENGTH_SHORT).show();
        } else {
            getDataByThread();
            Toast.makeText(this, R.string.MsgUsingThread, Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataByAsyncTask() {
        new AsyncDataLoader() {
            @Override
            public void onPostExecute(String result) {
                tvStatus.setText(getString(R.string.Pakrauta) + result);
            }
        }.execute(Constants.METEOLT_API_URL);
    }

    public void getDataByThread() {
        this.tvStatus.setText(R.string.Kraunama);
        Runnable getDataAndDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final String result = ApiDataReader.getValuesFromApi(Constants.FLOARATES_API_URL);
                    Runnable updateUIRunnable = new Runnable() {
                        @Override
                        public void run() {
                            List<String> listView;
                            listView = new ArrayList<>();
                            listView.add(result);
                            listAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, listView);
                            lvItems.setAdapter(listAdapter);
//                            tvStatus.setText(getString(R.string.Pakrauta) + result);
                        }
                    };
                    runOnUiThread(updateUIRunnable);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(getDataAndDisplayRunnable);
        thread.start();

    }
}