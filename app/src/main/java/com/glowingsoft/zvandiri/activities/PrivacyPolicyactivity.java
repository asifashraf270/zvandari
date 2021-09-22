package com.glowingsoft.zvandiri.activities;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.glowingsoft.zvandiri.R;


public class PrivacyPolicyactivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView title, description;
    String descriptionValue;
    ImageView back;
    RelativeLayout rootLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        bindViews();
    }

    /*
    Binding
     */
    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        back = findViewById(R.id.back);
        rootLayout = findViewById(R.id.rootLayout);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        try {
            title.setText("" + getIntent().getExtras().getString("title"));
            descriptionValue = stripHtml(getIntent().getExtras().getString("value"));
            description.setText("" + descriptionValue);
        } catch (Exception e) {
            Snackbar snackbar = Snackbar.make(rootLayout, "No Internet Connection", Snackbar.LENGTH_LONG);
            View view = snackbar.getView();
            view.setBackgroundColor(Color.RED);
            snackbar.show();
            finish();
        }
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

    public String stripHtml(String html) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            return Html.fromHtml(html).toString();
        }
    }
}
