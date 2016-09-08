package com.kree.keehoo.mdpro;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DataListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    private List<Obj> values;
    private String result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);

        values = new ArrayList<>();
        View recyclerView = findViewById(R.id.data_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        Task task = new Task(this);
        task.execute();

        if (findViewById(R.id.data_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        SimpleItemRecyclerViewAdapter adapter = new SimpleItemRecyclerViewAdapter(this, values);
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
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Obj> mValues;
        Context context;

        public SimpleItemRecyclerViewAdapter(Context context, List<Obj> items) {
            mValues = items;
            this.context = context;

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.data_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.obj = mValues.get(position);
            holder.name.setText(holder.obj.getName());
            setTestImage(holder.obj, context, holder.image);
            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(Keys.KLUCZ, holder.obj.getName());  // tutaj musze przeslac Id
                        DataDetailFragment fragment = new DataDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.data_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, DataDetailActivity.class);
                        intent.putExtra(Keys.KLUCZ, holder.obj.getName());
                        Log.d(Keys.KLUCZ, holder.obj.getName());
                        Log.d("DataListActivity", "obj.getName = " + holder.obj.getName());
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        private void setTestImage(Obj dt, Context context, ImageView imageView) {
            Picasso.with(context)
                    .load(dt.getImage())
                    .placeholder(R.drawable.c)
                    .error(R.drawable.c)
                    .into(imageView);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final TextView name;
            public final ImageView image;
            public final View mView;
            public Obj obj;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                name = (TextView) view.findViewById(R.id.id);
                image = (ImageView) view.findViewById(R.id.content);
            }

        }
    }

    public static class Task extends AsyncTask<Void, Void, String> {
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
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
