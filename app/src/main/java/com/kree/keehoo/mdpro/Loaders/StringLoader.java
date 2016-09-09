package com.kree.keehoo.mdpro.Loaders;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by keehoo on 09.09.2016.
 */
public class StringLoader extends AsyncTaskLoader<String> {
    public StringLoader(Context context) {
        super(context);
    }

    private String cachedData;  // przechowywanie informacji na wypadek pustej listy
    public static final String ACTION = "com.kree.keehoo";
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onStartLoading() {
        /**
         * To jest wywoływane gdy wywoływana jest instrukcja init w kliencie
         *  chcemy powiedziec loaderowi, zeby loadował
         */

        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(getContext());
        IntentFilter filter = new IntentFilter(ACTION);
        manager.registerReceiver(broadcastReceiver, filter);

        if (cachedData == null) forceLoad();
        else super.deliverResult(cachedData);
    }

    @Override
    public String loadInBackground() {
        /** This method is run on a backgroud thread!!!! do the downloading here */

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

    @Override
    public void deliverResult(String data) {
        cachedData = data;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            forceLoad();
        }
    };
}
