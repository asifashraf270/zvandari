package com.glowingsoft.zvandiri.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.glowingsoft.zvandiri.Adapaters.QuizFragmentAdapter;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.glowingsoft.zvandiri.models.McqOptionModel;
import com.glowingsoft.zvandiri.models.QuestionsModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ShowQuizActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    QuizFragmentAdapter adapter;
    List<QuestionsModel> QuestionWithAnswer;
    List<McqOptionModel> mcqOptions;
    AsyncHttpClient client;
    ProgressDialog progressDialog;
    String url;
    Bundle d;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_quiz);

        QuestionWithAnswer = new ArrayList<>();
        mcqOptions = new ArrayList<>();
        bindViews();
    }

    private void bindViews() {
        client = new AsyncHttpClient();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        listView = findViewById(R.id.listView);
        progressDialog = new ProgressDialog(ShowQuizActivity.this);
        progressDialog.setMessage("Loading....");
        GetQuizById(getIntent().getExtras().getString("cat_id"));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetQuizById(getIntent().getExtras().getString("cat_id"));
            }
        });

    }


    private void GetQuizById(String cat_id) {

        url = ServerConnection.getQuizByCategory;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesClass.sharedPreference.getString("id", ""));
        requestParams.put("category_id", cat_id);
        client.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(ShowQuizActivity.this, "Called", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Log.d("response", response.toString());
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                try {
                    if (response.getInt("status") == 200) {
                        JSONArray jsonArray = response.getJSONArray("questions");

                        adapter = new QuizFragmentAdapter(ShowQuizActivity.this, jsonArray);
                        listView.setAdapter(adapter);
                        Log.d("size", String.valueOf(jsonArray.length()));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
