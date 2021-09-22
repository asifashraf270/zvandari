package com.glowingsoft.zvandiri.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.glowingsoft.zvandiri.controllers.CtrUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class LoginActivity extends CtrUser implements View.OnClickListener {
    Button btnLogin, btnContactUs;
    EditText userId, userPassword;
    String id, pass;
    AsyncHttpClient client;
    String url;
    TextView forgotPassword;
    ProgressDialog progressDialog;
    LayoutInflater layoutInflater;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        Stetho.initializeWithDefaults(this);
        SharedPreferencesClass.sharedPreference = getSharedPreferences(SharedPreferencesClass.fileName, MODE_PRIVATE);
        SharedPreferencesClass.editor = SharedPreferencesClass.sharedPreference.edit();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isNetworkAvailable(LoginActivity.this)) {
            if (SharedPreferencesClass.sharedPreference.getString("id", "").length() > 0) {
                startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                finish();
            } else {
                bindViews();

            }
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).setTitle("Warning").setMessage("No Internet connection Found").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    dialogInterface.dismiss();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            }).create();
            alertDialog.show();
        }


    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            // Network is present and connected
            isAvailable = true;
        }
        return isAvailable;
    }

    /*

     */
    private void bindViews() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.setCancelable(false);
        btnLogin = findViewById(R.id.btnLogin);
        btnContactUs = findViewById(R.id.contactUS);
        userId = findViewById(R.id.userId);
        userPassword = findViewById(R.id.password);
        btnLogin.setOnClickListener(this);
        btnContactUs.setOnClickListener(this);
        client = new AsyncHttpClient();
        rootLayout = findViewById(R.id.relativeLayout);
        forgotPassword = findViewById(R.id.forgetPassword);
        forgotPassword.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);
                id = getString(userId);
                pass = getString(userPassword);
                if (id.length() > 0 && pass.length() > 0) {
                    if (pass.length() < 8) {
                        Snackbar snackbar = Snackbar.make(rootLayout, "PassWord Must be 8 digit", Snackbar.LENGTH_LONG);
                        View view = snackbar.getView();
                        view.setBackgroundColor(Color.RED);
                        snackbar.show();
                    } else {
                        LoginApi(id, pass);
                    }

                } else {
                    if (id.isEmpty()) {
                        userId.setError("UserId is required");
                    }
                    if (pass.isEmpty()) {
                        userPassword.setError("Password is Required");
                    }
                }
                break;
            case R.id.contactUS:
                startActivity(new Intent(LoginActivity.this, ContactUsActivity.class));
                break;
            case R.id.forgetPassword:
                CustomAlertDialog();
                break;
        }
    }

    private void CustomAlertDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.custom_alert_dialog, null);
        final EditText email, userIs;
        email = view.findViewById(R.id.emailId);
        userIs = view.findViewById(R.id.userId);
        Button cancel, ok;
        cancel = view.findViewById(R.id.cancel);
        ok = view.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(rootLayout.getWindowToken(), 0);
                String emailValue = email.getText().toString();
                String userId = userIs.getText().toString();
                if (isValidEmail(emailValue) && !userId.isEmpty()) {
                    ForgotPasswordApi(emailValue, userId);
                    alertDialog.dismiss();

                } else {
                    Snackbar snackbar = Snackbar.make(rootLayout, "Some Field Missing or Invalid Email", Snackbar.LENGTH_LONG);
                    View view1 = snackbar.getView();
                    view1.setBackgroundColor(Color.RED);
                    snackbar.show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.setView(view);
        alertDialog.show();

    }

    /*
    Call Rest Api of Login
     */
    private void LoginApi(String userId, String pass) {
        url = ServerConnection.connectionIp + ServerConnection.Login;
        RequestParams requestParams = new RequestParams();
        requestParams.put("userID", userId);
        requestParams.put("password", pass);
        client.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                progressDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.dismiss();
                Log.d("response", response.toString());
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("status") == 200) {
                        JSONObject jsonObject = response.getJSONObject("user");
                        Snackbar.make(findViewById(R.id.relativeLayout), "" + response.getString("message"), Snackbar.LENGTH_LONG).show();
                        SharedPreferencesClass.editor.putString("id", jsonObject.getString("id")).commit();
                        SharedPreferencesClass.editor.putString("image", jsonObject.getString("image")).commit();
                        SharedPreferencesClass.editor.putString("name", jsonObject.getString("name")).commit();
                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                        finish();
                    } else {
                        Snackbar.make(findViewById(R.id.relativeLayout), "" + response.getString("message"), Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("exception", e.getMessage());
                    Snackbar.make(rootLayout, "No Internet Connection", Snackbar.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                Log.d("response", responseString);
            }
        });
    }

    /*
    get data from edittext
     */
    private String getString(EditText editText) {
        return editText.getText().toString();
    }

    /*
    Forgot Password Api
     */
    private void ForgotPasswordApi(String email, String userId) {
        RequestParams requestParams = new RequestParams();
        requestParams.put("email", email);
        requestParams.put("userID", userId);
        client.post(ServerConnection.forgotPassword, requestParams, new JsonHttpResponseHandler() {
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
                        Snackbar.make(rootLayout, "" + response.getString("message"), Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(rootLayout, "" + response.getString("message"), Snackbar.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar.make(rootLayout, "No internet Connection", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                Snackbar.make(rootLayout, "No Internet Connection", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
