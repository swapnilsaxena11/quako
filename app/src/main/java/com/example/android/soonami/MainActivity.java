package com.example.android.soonami;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.android.soonami.Event;
import com.example.android.soonami.EventAdapter;
import com.example.android.soonami.R;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();
    private EventAdapter adapter;
    private String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2020-01-01&endtime=2020-05-05&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_layout);
        ListView earthquakeListView = (ListView) findViewById(R.id.list_event);
        adapter = new EventAdapter(this, new ArrayList<Event>());
        assert earthquakeListView != null;
        earthquakeListView.setAdapter(adapter);

        View headerView = getLayoutInflater().inflate(R.layout.headerlayout, null);
        earthquakeListView.addHeaderView(headerView);

        Spinner spinner= (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mag_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Button button = (Button) findViewById(R.id.button) ;
        assert button != null;

        final TaskAsync task = new TaskAsync();
        task.execute(USGS_REQUEST_URL);

        button.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                TaskAsync task1 = new TaskAsync();
                String starttime = ((EditText) Objects.requireNonNull(findViewById(R.id.starttime))).getText().toString();
                String endtime = ((EditText) Objects.requireNonNull(findViewById(R.id.endtime))).getText().toString();
                String spinner_option = ((Spinner)findViewById(R.id.spinner)).getSelectedItem().toString();
                String minmag = Character.toString(spinner_option.charAt(spinner_option.length()-1));
                USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime="+ starttime + "&endtime=" + endtime + "&orderby=time&minmag=" + minmag + "&limit=10";
                Log.v("URL",USGS_REQUEST_URL);
                task1.execute(USGS_REQUEST_URL);
            }
        });
    }
    private class TaskAsync extends AsyncTask<String,Void,ArrayList<Event>>{

        @Override
        protected ArrayList<Event> doInBackground(String... url) {
            try {
                return com.example.android.soonami.QueryUtils.fetchEarthquakeData(url[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Event> Event) {
            // Clear the adapter of previous earthquake data
            adapter.clear();


            if (Event != null && !Event.isEmpty()) {
                adapter.addAll(Event);
            }
            else{
                ArrayList<Event> ev = new ArrayList<>();
                ev.add(new Event("Phusro",1511672877,1,6.1));
                adapter.addAll(ev);
            }
        }
    }
}