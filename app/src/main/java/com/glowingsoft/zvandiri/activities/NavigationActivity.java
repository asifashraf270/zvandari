package com.glowingsoft.zvandiri.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glowingsoft.zvandiri.Fragments.HomeFragment;
import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.SharedPreferencesClass;
import com.glowingsoft.zvandiri.Utils.ServerConnection;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import de.hdodenhof.circleimageview.CircleImageView;

public class NavigationActivity extends AppCompatActivity
        implements View.OnClickListener {
    LinearLayout home;
    DrawerLayout drawer;
    LinearLayout profileFragment, healthTracker, privacyPolicy, contact, logOut, aboutUs;
    ImageView cancel;
    CircleImageView profileImage;
    TextView userName;
    ImageView fb, youtube, twitter, insta;
    AsyncHttpClient client;
    JSONArray jsonArray;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.app_name));
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        informationApi();
        toggle.syncState();
        bindViews();
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Confirmaton ?").setIcon(R.mipmap.ic_launcher).setMessage("Are you sure to Close this Application").
                    setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setCancelable(false).create();
            alertDialog.show();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home:
                loadFragment(new HomeFragment());
                drawer.closeDrawers();
                break;
            case R.id.profileFragment:
                startActivity(new Intent(NavigationActivity.this, HealthTrackerProfileActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.healthTracker:
                startActivity(new Intent(NavigationActivity.this, HealthTrackerProfileActivity.class));
                drawer.closeDrawers();
                break;

            case R.id.privacyPolicy:
                Intent privacyPoliy = new Intent(NavigationActivity.this, PrivacyPolicyactivity.class);
                try {
                    privacyPoliy.putExtra("title", jsonArray.getJSONObject(0).getString("post_title"));
                    privacyPoliy.putExtra("value", jsonArray.getJSONObject(0).getString("post_description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(privacyPoliy);
                drawer.closeDrawers();
                break;

            case R.id.aboutUs:
                Intent aboutUS = new Intent(NavigationActivity.this, PrivacyPolicyactivity.class);
                try {
                    aboutUS.putExtra("title", jsonArray.getJSONObject(1).getString("post_title"));
                    aboutUS.putExtra("value", jsonArray.getJSONObject(1).getString("post_description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(aboutUS);
                drawer.closeDrawers();
                break;

            case R.id.contact:
                startActivity(new Intent(NavigationActivity.this, ContactUsActivity.class));
                drawer.closeDrawers();
                break;
            case R.id.cancel:
                drawer.closeDrawers();
                break;
            case R.id.fb:
                String url = "https://www.facebook.com/search/top/?q=zvandiri%20africaid&epa=SEARCH_BOX";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.youtube:
                Intent i1 = new Intent(Intent.ACTION_VIEW);
                i1.setData(Uri.parse("https://www.youtube.com/user/maruvavideo"));
                startActivity(i1);
                break;
            case R.id.insta:
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse("https://www.instagram.com/explore/locations/1020889286/africaid-zvandiri/"));
                startActivity(i2);
                break;
            case R.id.twitter:
                Intent i3 = new Intent(Intent.ACTION_VIEW);
                i3.setData(Uri.parse("https://twitter.com/zvandiri"));
                startActivity(i3);
                break;
        }
    }

    /*
    Bind Views
     */
    private void bindViews() {
        home = findViewById(R.id.home);
        fb = findViewById(R.id.fb);
        rootLayout = findViewById(R.id.rootLayout);
        youtube = findViewById(R.id.youtube);
        insta = findViewById(R.id.insta);
        twitter = findViewById(R.id.twitter);
        fb.setOnClickListener(this);
        youtube.setOnClickListener(this);
        insta.setOnClickListener(this);
        twitter.setOnClickListener(this);
        logOut = findViewById(R.id.logout);
        aboutUs = findViewById(R.id.aboutUs);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesClass.sharedPreference.edit().clear().commit();
                finish();
            }
        });
        profileFragment = findViewById(R.id.profileFragment);
        healthTracker = findViewById(R.id.healthTracker);
        healthTracker.setOnClickListener(this);
        home.setOnClickListener(this);
        profileFragment.setOnClickListener(this);
        privacyPolicy = findViewById(R.id.privacyPolicy);
        privacyPolicy.setOnClickListener(this);
        contact = findViewById(R.id.contact);
        contact.setOnClickListener(this);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        aboutUs.setOnClickListener(this);

        profileImage = findViewById(R.id.profileImage);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NavigationActivity.this, HealthTrackerProfileActivity.class));
            }
        });
        userName = findViewById(R.id.userName);
        userName.setText("" + SharedPreferencesClass.sharedPreference.getString("name", ""));
        if (SharedPreferencesClass.sharedPreference.getString("image", "").length() > 0) {
            Picasso.get().load(SharedPreferencesClass.sharedPreference.getString("image", "")).fit().placeholder(R.drawable.loading).into(profileImage);
        }
    }

    /*
    load fragment on Main container
     */
    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();

    }

    private void informationApi() {
        client = new AsyncHttpClient();

        client.get(ServerConnection.getPages, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    if (response.getInt("status") == 200) {
                        jsonArray = response.getJSONArray("pages");
                        SharedPreferencesClass.sharedPreference.edit().putString("promisesTitle", jsonArray.getJSONObject(2).getString("post_title")).commit();
                        SharedPreferencesClass.sharedPreference.edit().putString("promisesDesc", jsonArray.getJSONObject(2).getString("post_description")).commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Snackbar snackbar = Snackbar.make(rootLayout, "No Internet Connection", Snackbar.LENGTH_LONG);
                    View view = snackbar.getView();
                    view.setBackgroundColor(Color.RED);
                    snackbar.show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Snackbar snackbar = Snackbar.make(rootLayout, "No Internet Connection", Snackbar.LENGTH_LONG);
                View view = snackbar.getView();
                view.setBackgroundColor(Color.RED);
                snackbar.show();

            }
        });
    }
}
