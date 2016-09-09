package com.kree.keehoo.mdpro.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.kree.keehoo.mdpro.KeysAndConstants.Obj;
import com.kree.keehoo.mdpro.Loaders.StringLoader;
import com.kree.keehoo.mdpro.R;
import com.kree.keehoo.mdpro.RVAdapters.SimpleViewAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import android.support.v4.app.LoaderManager;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DataListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private List<Obj> values;
    private String result;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        recyclerView = (RecyclerView) findViewById(R.id.data_list);
        if (findViewById(R.id.data_detail_container) != null) {
            mTwoPane = true;
        }
        values = new ArrayList<>();
        //getSupportLoaderManager().initLoader(R.id.string_loader_id, null, listLoaderCallbacks);
        assert recyclerView != null;
        setupRecyclerView(Collections.<Obj>emptyList(), mTwoPane);
        getSupportLoaderManager().initLoader(R.id.string_loader_id, null, listLoaderCallbacks);

       /*Task task = new Task(this);
        task.execute();*/
    }

    public void setupRecyclerView(List<Obj> values, boolean mTwoPane) {
        SimpleViewAdapter adapter = new SimpleViewAdapter(this, values, mTwoPane);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    public void start() {
        // tv.setText("Uruchomienie z onPosta \n  " + getResult());
        try {
            JSONArray ja = new JSONArray(getResult());
            values.clear();
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                values.add(new Obj(jo.getString("name"), jo.getString("image")));
            }
            //tv.setText("dlugosc listy "+lista.size());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("JSONArray", "JSONEXCEPTION");
            Toast.makeText(DataListActivity.this, "JSONArray Exception", Toast.LENGTH_SHORT).show();
        }
        setupRecyclerView(values, mTwoPane);
    }

    private LoaderManager.LoaderCallbacks<String> listLoaderCallbacks = new LoaderManager.LoaderCallbacks<String>() {
        @Override
        public Loader<String> onCreateLoader(int id, Bundle args) {
            return new StringLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<String> loader, String data) {
            Log.d("onLoadFinished", "data = "+ data);
            setResult(data);
            start();
        }

        @Override
        public void onLoaderReset(Loader<String> loader) {
            setupRecyclerView(Collections.<Obj>emptyList(), mTwoPane);
        }
    };


    /**public static class Task extends AsyncTask<Void, Void, String> {
        private DataListActivity activity;
        private final OkHttpClient client = new OkHttpClient();

        public Task(DataListActivity activity) {
            this.activity = activity;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            //activity.tv.setText(s);
            activity.setResult(s);
            activity.start();
            activity.setupRecyclerView(Collections.<Obj>emptyList(), activity.mTwoPane);


        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected String doInBackground(Void... voids) {

            Request request = new Request.Builder()
                    .url("http://dev.tapptic.com/test/json.php")
                    .build();
            Response response = null;
            try {
                response = client.newCall(request).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (!response.isSuccessful()) try {
                throw new IOException("Unexpected code " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Headers responseHeaders = response.headers();
            for (int i = 0; i < responseHeaders.size(); i++) {
                //System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                Log.d("run", "Response header" + responseHeaders.value(i));
            }
            try {
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return "IOException" + e;
            }
        }
    }*/

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
