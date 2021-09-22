package com.glowingsoft.zvandiri.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {
    EditText name, height, weight, viralLoad, cd4Count, date, email;
    Spinner spinner;
    Button btnUpdate;
    ImageView back;
    ArrayAdapter<String> values;
    AsyncHttpClient client;
    DatePickerDialog datePickerDialog;
    Calendar calendar;
    String spinnerValue;
    int SELECT_PICTURE = 100;
    ProgressDialog progressDialog;
    CircleImageView profilePicture;
    int loc = -1;
    Uri selectedImage;
    String nameValue, heightValue, weightValue, viralLoadValue, cd4CountValue, dateValue, emailValue;
    String[] months = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "12+"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        viewsBinding();
    }

    /*
    Views Binding
     */
    private void viewsBinding() {
        client = new AsyncHttpClient();
        back = findViewById(R.id.back);
        progressDialog = new ProgressDialog(UpdateProfileActivity.this);
        progressDialog.setMessage("Loading....");
        name = findViewById(R.id.name);
        height = findViewById(R.id.etHeight);
        weight = findViewById(R.id.etWeight);
        spinner = findViewById(R.id.spinnerMonth);
        date = findViewById(R.id.etdate);
        profilePicture = findViewById(R.id.profile);
        email = findViewById(R.id.emailId);
        viralLoad = findViewById(R.id.viralLoad);
        cd4Count = findViewById(R.id.cd4Count);
        calendar = Calendar.getInstance();
        back.setOnClickListener(this);
        values = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, months);
        spinner.setAdapter(values);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);
        profilePicture.setOnClickListener(this);
        date.setOnClickListener(this);
        getProfileApi();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etdate:
                datePickerDialog = new DatePickerDialog(this, R.style.AppTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dateValue = datePicker.getYear() + "-" + datePicker.getMonth() + 1 + "-" + datePicker.getDayOfMonth();
                        date.setText("" + i + "-" + i1 + "-" + i2);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.btnUpdate:
                nameValue = getValue(name);
                weightValue = getValue(weight);
                heightValue = getValue(height);
                viralLoadValue = getValue(viralLoad);
                cd4CountValue = getValue(cd4Count);
                emailValue = getValue(email);
                spinnerValue = spinner.getSelectedItem().toString();
                if (nameValue.isEmpty() && weightValue.isEmpty() && heightValue.isEmpty() && viralLoadValue.isEmpty() && cd4CountValue.isEmpty() && dateValue.isEmpty()) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayout), "Some Field Missing", Snackbar.LENGTH_LONG);
                    View view1 = snackbar.getView();
                    view1.setBackgroundColor(Color.RED);
                    snackbar.show();
                } else {
                    UpdateApi(nameValue, emailValue, weightValue, heightValue, viralLoadValue, cd4CountValue, dateValue, spinnerValue);

                }
                break;
            case R.id.back:
                finish();
                break;
            case R.id.profile:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
                break;
        }
    }

    private String getValue(EditText editText) {
        return editText.getText().toString();
    }

    /*
    Update Record Function
     */
    private void UpdateApi(String name, String email, String weight, String height, String viral, String c4count, String date, String month) {
        client = new AsyncHttpClient();
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", SharedPreferencesClass.sharedPreference.getString("id", ""));
        requestParams.put("name", name);
        requestParams.put("email", email);
        requestParams.put("height", height);
        requestParams.put("weight", weight);
        requestParams.put("date", date);
        requestParams.put("treatmentMonth", month);
        requestParams.put("viral_load", viral);
        requestParams.put("cd4_count", c4count);
        Log.d("name", name + "/" + email + "height" + height + "/" + weight + "/" + date + "/" + month + "/" + viral + "/" + c4count);
        client.post(ServerConnection.updateProfileEndPoint, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "" + response.getString("message"), Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(Color.GREEN);
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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImage = data.getData();
                profilePicture.setImageURI(selectedImage);
                try {
                    InputStream iStream = getContentResolver().openInputStream(selectedImage);
                    byte[] inputData = getBytes(iStream);
                    updateProfileImage(inputData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void updateProfileImage(byte[] buffer) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesClass.sharedPreference.getString("id", ""));
        requestParams.put("image", new ByteArrayInputStream(buffer));
        client.post(ServerConnection.updateProfileImageEndPoint, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                try {
                    Snackbar.make(findViewById(R.id.relativeLayout), "" + response.getString("message"), Snackbar.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "Check Internet Connection", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(Color.RED);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "Check Internet Connection", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();
            }
        });
    }

    /*
    getProfileApi
     */
    private void getProfileApi() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", SharedPreferencesClass.sharedPreference.getString("id", ""));
        client.post(ServerConnection.profile, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("status") == 200) {
                        JSONObject jsonObject = response.getJSONObject("user");
                        name.setText("" + jsonObject.getString("name"));
                        email.setText("" + jsonObject.getString("email"));
                        height.setText("" + jsonObject.getString("height"));
                        weight.setText("" + jsonObject.getString("weight"));
                        date.setText("" + jsonObject.getString("date"));
                        dateValue = jsonObject.getString("date");
                        for (int i = 0; i < months.length; i++) {
                            if (jsonObject.getString("treatmentMonth").equals(months[i])) {
                                loc = i;
                                break;
                            }
                        }
                        try {
                            spinner.setSelection(Integer.parseInt(months[loc]));
                        } catch (Exception e) {
                            Log.d("exception", e.getMessage());
                        }
                        viralLoad.setText("" + jsonObject.getString("viral_load"));
                        cd4Count.setText("" + jsonObject.getString("cd4_count"));
                        Picasso.get().load(jsonObject.getString("image")).fit().placeholder(R.drawable.loading).into(profilePicture);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
            }
        });

    }
}
