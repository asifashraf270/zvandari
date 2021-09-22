package com.glowingsoft.zvandiri.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.glowingsoft.zvandiri.R;


public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        bindViews();

    }

    /*
    Binding
     */
    private void bindViews() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnBack:
                finish();
                break;
        }
    }
}
