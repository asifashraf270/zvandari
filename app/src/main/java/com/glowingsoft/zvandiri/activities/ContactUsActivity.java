package com.glowingsoft.zvandiri.activities;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    EditText name, email, phoneNumber, message;
    Button send;
    AsyncHttpClient client;
    ProgressDialog progressDialog;
    RelativeLayout rootLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_us);
        bindViews();
    }

    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        name = findViewById(R.id.name);
        rootLayout = findViewById(R.id.rootLayout);
        email = findViewById(R.id.email);
        phoneNumber = findViewById(R.id.pNumber);
        message = findViewById(R.id.message);
        send = findViewById(R.id.Send);
        send.setOnClickListener(this);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        client = new AsyncHttpClient();
        progressDialog = new ProgressDialog(ContactUsActivity.this);
        progressDialog.setMessage("Loading.....");
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.Send:
                String nameValue = GetStrint(name);
                String emailValue = GetStrint(email);
                String phoneNumbervalue = GetStrint(phoneNumber);
                String messageValue = GetStrint(message);
                if (nameValue.isEmpty()) {
                    name.setError("Name is Required");
                    if (emailValue.isEmpty()) {
                        email.setError("Email is Required");
                        if (phoneNumber.length() <= 0) {
                            phoneNumber.setError("Phone Number is Required");

                        }
                    }
                } else {
                    if (countNumber(messageValue) <= 10 && countNumber(phoneNumbervalue) <= 7) {
                        Snackbar snackbar = Snackbar.make(rootLayout, "Message Must Be More than 10 Words and phone Number Must be More Than 7 digits ", Snackbar.LENGTH_LONG);
                        View view1 = snackbar.getView();
                        view1.setBackgroundColor(Color.RED);
                        snackbar.show();
                    } else {
                        if (nameValue.length() > 0 && emailValue.length() > 0 && phoneNumbervalue.length() > 0 && messageValue.length() > 0 && isValidEmail(emailValue)) {
                            CallApi(nameValue, emailValue, phoneNumbervalue, messageValue);
                        }
                    }

                }


                break;
        }
    }

    /*
    Get String from edittext
     */
    private String GetStrint(EditText value) {
        return value.getText().toString();
    }

    /*
    Button clicked
     */
    /*
    calling Api
     */
    private void CallApi(String name, String email, String phoneNumber, String message) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("name", name);
        requestParams.put("email", email);
        requestParams.put("phone", phoneNumber);
        requestParams.put("message", message);
        client.post(ServerConnection.contactUs, requestParams, new JsonHttpResponseHandler() {
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
                    if (response.getInt("status") == 200) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "" + response.getString("message"), Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        snackbar.show();
                    }
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
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar.make(findViewById(R.id.rootLayout), "Check Internet Connection", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();

            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    private int countNumber(String string) {

        int count = 0;

        char ch[] = new char[string.length()];
        for (int i = 0; i < string.length(); i++) {
            ch[i] = string.charAt(i);
            if (((i > 0) && (ch[i] != ' ') && (ch[i - 1] == ' ')) || ((ch[0] != ' ') && (i == 0)))
                count++;
        }
        return count;

    }
}
