package com.glowingsoft.zvandiri.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;


public class HealthTrackerProfileActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnViewMedicine, updateProfile;
    String url;
    AsyncHttpClient client;
    ProgressDialog progressDialog;
    TextView name, weight, height, date, treatmentMonth, viralLoad, c4Count;
    CircleImageView profileImage;
    ImageView imageView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.healthtracker_activity);
        bindviews();
    }


    /*
    View Binding
     */
    private void bindviews() {
        viralLoad = findViewById(R.id.viralLoad);
        c4Count = findViewById(R.id.cd4Count);
        imageView = findViewById(R.id.btnBack);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        profileImage = findViewById(R.id.profile);
        name = findViewById(R.id.name);
        weight = findViewById(R.id.weight);
        height = findViewById(R.id.height);
        date = findViewById(R.id.date);
        treatmentMonth = findViewById(R.id.month);
        progressDialog = new ProgressDialog(HealthTrackerProfileActivity.this);
        progressDialog.setMessage("Loading....");
        client = new AsyncHttpClient();
        btnViewMedicine = findViewById(R.id.viewMedicines);
        btnViewMedicine.setOnClickListener(this);
        updateProfile = findViewById(R.id.updateProfile);
        updateProfile.setOnClickListener(this);
        getProfile();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.viewMedicines:
                startActivity(new Intent(HealthTrackerProfileActivity.this, com.glowingsoft.zvandiri.activities.HealthTrackerActivity.class));
                break;
            case R.id.updateProfile:
                startActivity(new Intent(HealthTrackerProfileActivity.this, UpdateProfileActivity.class));
                break;
        }
    }

    /*
    Call Rest Api of get Profile
     */
    private void getProfile() {
        url = ServerConnection.profile;
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
                Log.d("response", response.toString());
                progressDialog.dismiss();
                try {
                    if (response.getInt("status") == 200) {
                        JSONObject jsonObject = response.getJSONObject("user");
                        name.setText("" + jsonObject.getString("name"));
                        weight.setText("" + jsonObject.getString("weight"));
                        height.setText("" + jsonObject.getString("height"));
                        date.setText("" + jsonObject.getString("date"));
                        treatmentMonth.setText("" + jsonObject.getString("treatmentMonth"));
                        String imagePath = jsonObject.getString("image");
                        viralLoad.setText("" + jsonObject.getString("viral_load"));
                        c4Count.setText("" + jsonObject.getString("cd4_count"));
                        Picasso.get().load(imagePath).fit().placeholder(R.drawable.loading).into(profileImage);
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
}
