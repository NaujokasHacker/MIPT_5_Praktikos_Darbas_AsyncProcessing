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

import com.example.mipt_5_praktikos_darbas_asyncprocessing.Utilities.ApiDataReader;
import com.example.mipt_5_praktikos_darbas_asyncprocessing.Utilities.AsyncDataLoader;
import com.example.mipt_5_praktikos_darbas_asyncprocessing.Utilities.Constants;

public class MainActivity extends AppCompatActivity {
    private ListView lvItems;
    private TextView tvStatus;
    private ArrayAdapter listAdapter;
    private Switch swUseAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.lvItems = findViewById(R.id.lv_items);
        this.tvStatus = findViewById(R.id.tv_status);
        this.swUseAsyncTask = findViewById(R.id.sw_use_async_task);

        this.listAdapter = new ArrayAdapter<>(getApplication(), android.R.layout.simple_list_item_1, new ArrayList<>());
        this.lvItems.setAdapter(this.listAdapter);
    }

    public void onBtnGetDataClick(View view) {
        this.tvStatus.setText(R.string.loading_data);
        if (this.swUseAsyncTask.isChecked()) {
            getDataByAsyncTask();
            Toast.makeText(this, R.string.msg_using_async_task, Toast.LENGTH_SHORT).show();
        } else {
            getDataByThread();
            Toast.makeText(this, R.string.msg_using_thread, Toast.LENGTH_SHORT).show();
        }
    }

    public void getDataByAsyncTask() {
        new AsyncDataLoader() {
            @Override
            public void onPostExecute(String result) {
                tvStatus.setText(getString(R.string.data_loaded) + result);
            }
        }.execute(Constants.METEOLT_API_URL);
    }

    public void getDataByThread() {
        this.tvStatus.setText(R.string.loading_data);
        Runnable getDataAndDisplayRunnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final String result = ApiDataReader.getValuesFromApi(Constants.FLOARATES_API_URL);
                    Runnable updateUIRunnable = new Runnable() {
                        @Override
                        public void run() {
                            tvStatus.setText(getString(R.string.data_loaded) + result);
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