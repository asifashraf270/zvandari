package com.glowingsoft.zvandiri.Fragments;

import android.app.ProgressDialog;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.glowingsoft.zvandiri.Adapaters.AudioFragmentAdapter;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.glowingsoft.zvandiri.models.AudioFragmentModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class AudioFragment extends Fragment {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    AudioFragmentAdapter adapter;
    AsyncHttpClient client;
    String url;
    List<AudioFragmentModel> models;
    MediaPlayer mediaPlayer;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        bindViews(view);
        return view;
    }

    private void bindViews(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        listView = view.findViewById(R.id.listView);
        mediaPlayer = new MediaPlayer();
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        getAudios();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAudios();
            }
        });


    }


    /*
           Caling Rest api
            */
    private void getAudios() {
        models = new ArrayList<>();
        client = new AsyncHttpClient();
        url = ServerConnection.getAudios;
        Log.d("url", url);
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesClass.sharedPreference.getString("id", ""));
        client.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                models = new ArrayList<>();
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                try {
                    if (response.getInt("status") == 200) {
                        Log.d("response", response.toString());
                        JSONArray jsonArray = response.getJSONArray("audios");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            models.add(new AudioFragmentModel(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("title"), jsonArray.getJSONObject(i).getString("url")));
                        }
                        adapter = new AudioFragmentAdapter(getContext(), models);
                        listView.setAdapter(adapter);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("response", e.getMessage());
                    progressDialog.dismiss();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
            }
        });
    }


}
