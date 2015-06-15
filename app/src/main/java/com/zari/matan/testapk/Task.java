package com.zari.matan.testapk;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Matan on 4/20/2015
 */
public class Task extends AsyncTask<String, Void, String> {
    private Activity activity;
    private Fragment fragment;

    public Task(Activity activity) {
        this.activity = activity;
    }

    public Task(Fragment fragment) {
        this.fragment = fragment;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        URL url;
        HttpURLConnection connection = null;
        StringBuilder builder;
        BufferedReader reader;
        String result;


        try {
            url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            result = builder.toString();
        } catch (IOException e) {
            Log.e(e.getMessage(), "failed");
            result = null;
        } finally {
            if (connection != null)
                connection.disconnect();
        }
        return result;
    }


    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            DoIt listener;
            if (fragment != null) {
                if (fragment instanceof DoIt) {
                    listener = (DoIt) fragment;
                    listener.send(result);
                }
            }
            if (activity != null) {
                if (activity instanceof DoIt) {
                    listener = (DoIt) activity;
                    listener.send(result);
                }
            }
        }
    }

    public interface DoIt {
        void send(String result);
    }
}
