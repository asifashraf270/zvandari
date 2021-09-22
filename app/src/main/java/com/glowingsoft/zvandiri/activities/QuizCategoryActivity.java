package com.glowingsoft.zvandiri.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.glowingsoft.zvandiri.Adapaters.QuizCategoryAdapter;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.glowingsoft.zvandiri.models.QuizCategoryModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class QuizCategoryActivity extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    AsyncHttpClient client;
    ProgressDialog progressDialog;
    String url;
    List<QuizCategoryModel> modelList;
    QuizCategoryAdapter adapter;
    QuizCategoryModel quizCategoryModel;
    Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_caegory);
        bindViews();
    }


    /*
    Bindviews
     */
    private void bindViews() {
        client = new AsyncHttpClient();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        progressDialog = new ProgressDialog(QuizCategoryActivity.this);
        progressDialog.setMessage("Loading....");
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        listView = findViewById(R.id.listViewCat);
        CategoryApi();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QuizCategoryActivity.this, ShowQuizActivity.class);
                intent.putExtra("cat_id", modelList.get(i).getId());
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CategoryApi();

            }
        });
    }

    /*
    Call Rest Api
     */
    private void CategoryApi() {
        modelList = new ArrayList<>();
        url = ServerConnection.connectionIp + ServerConnection.getQuizCategories;
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
                progressDialog.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                try {
                    if (response.getInt("status") == 200) {
                        JSONArray jsonArray = response.getJSONArray("categories");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            quizCategoryModel = new QuizCategoryModel(jsonArray.getJSONObject(i).getString("id"), jsonArray.getJSONObject(i).getString("cat_name"));
                            modelList.add(quizCategoryModel);
                        }
                        adapter = new QuizCategoryAdapter(QuizCategoryActivity.this, modelList);
                        listView.setAdapter(adapter);
                    } else {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "" + response.getString("message"), Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snackbar.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
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
