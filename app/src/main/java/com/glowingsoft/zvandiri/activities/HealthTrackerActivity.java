package com.glowingsoft.zvandiri.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.glowingsoft.zvandiri.Adapaters.MedicineActivityAdapter;
import com.glowingsoft.zvandiri.Interfaces.GetMedicineCheckStatus;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.glowingsoft.zvandiri.models.MedicineActivityModel;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HealthTrackerActivity extends AppCompatActivity implements View.OnClickListener, GetMedicineCheckStatus {
    List<MedicineActivityModel> listModel;
    MedicineActivityAdapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listview;
    ProgressDialog progressDialog;
    AsyncHttpClient client;
    Toolbar toolbar;
    List<String> medicineId;
    View footer;
    Button update;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_tracker);
        bindViews();
    }

    /*
    View bindings
     */
    private void bindViews() {
        medicineId = new ArrayList<>();
        progressDialog = new ProgressDialog(HealthTrackerActivity.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        client = new AsyncHttpClient();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        swipeRefreshLayout = findViewById(R.id.refreshLayout);
        listview = findViewById(R.id.listView);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(R.layout.healthtracker_foot, null);
        update = footer.findViewById(R.id.btnUpdatefooter);
        listview.addFooterView(footer);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateApi();
            }
        });
        GetApi();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetApi();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }

    /*
    Calling rest api
     */
    private void GetApi() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesClass.sharedPreference.getString("id", ""));
        client.post(ServerConnection.getHealthTracker, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                listModel = new ArrayList<>();
                swipeRefreshLayout.setRefreshing(false);
                progressDialog.dismiss();
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.d("response", response.toString());
                    if (response.getInt("status") == 200) {
                        JSONArray jsonArray = response.getJSONArray("health_trackter");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            listModel.add(new MedicineActivityModel(jsonObject.getString("id"), jsonObject.getString("title"), jsonObject.getBoolean("is_joined")));

                        }
                        adapter = new MedicineActivityAdapter(HealthTrackerActivity.this, listModel, HealthTrackerActivity.this);
                        listview.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
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

    @Override
    public void getId(String id) {
        medicineId.add(id);
    }
    /*
    Calling UpdateApi

     */

    private void updateApi() {

        RequestParams requestParams = new RequestParams();
        requestParams.put("id", SharedPreferencesClass.sharedPreference.getString("id", ""));
        if (medicineId.size() > 0) {
            data = medicineId.get(0);
            for (int i = 1; i < medicineId.size(); i++) {
                data = data + "," + medicineId.get(i);
            }
            requestParams.put("health", data);
            client.post(ServerConnection.updateHealth, requestParams, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);
                    try {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "" + response.getString("message"), Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snackbar.show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "Check Internet Connection", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(Color.RED);
                        snackbar.show();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "Please Select Some Medicine", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(Color.RED);
            snackbar.show();
        }
    }

}
