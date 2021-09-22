package com.glowingsoft.zvandiri.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.glowingsoft.zvandiri.R;

public class CatsGuideActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    RelativeLayout slide1, slide2, slide3, slide4, slide5, slide6, slide7, slide8, slide9, slide10, slide11;

    @Override
    protected void onCreate(Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        setContentView(R.layout.activity_cats_guide);
        bindViews();
    }

    /*
    Binding
     */
    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("" + getIntent().getExtras().getString("name"));

        setSupportActionBar(toolbar);
        slide1 = findViewById(R.id.slide1);
        slide1.setOnClickListener(this);

        slide2 = findViewById(R.id.slide2);
        slide2.setOnClickListener(this);

        slide3 = findViewById(R.id.slide3);
        slide3.setOnClickListener(this);

        slide4 = findViewById(R.id.slide4);
        slide4.setOnClickListener(this);

        slide5 = findViewById(R.id.slide5);
        slide5.setOnClickListener(this);

        slide6 = findViewById(R.id.slide6);
        slide6.setOnClickListener(this);

        slide7 = findViewById(R.id.slide7);
        slide7.setOnClickListener(this);

        slide8 = findViewById(R.id.slide8);
        slide8.setOnClickListener(this);

        slide9 = findViewById(R.id.slide9);
        slide9.setOnClickListener(this);

        slide10 = findViewById(R.id.slide10);
        slide10.setOnClickListener(this);

        slide11 = findViewById(R.id.slide11);
        slide11.setOnClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);

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
            case R.id.slide1:
                Intent intent = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent.putExtra("title", "Cats Guide");
                intent.putExtra("name", "Introduction");
                intent.putExtra("color", getResources().getColor(R.color.slide1));
                startActivity(intent);
                break;
            case R.id.slide2:
                Intent intent1 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent1.putExtra("title", "Cats Guide");
                intent1.putExtra("color", getResources().getColor(R.color.slide2));

                intent1.putExtra("name", "SupportingYourPeerstoKnowtheirHIVStatus");
                startActivity(intent1);
                break;
            case R.id.slide3:
                Intent intent2 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent2.putExtra("title", "Cats Guide");
                intent2.putExtra("color", getResources().getColor(R.color.slide3));

                intent2.putExtra("name", "SupportingYourPeertoUnderstandandAccepttheirHIVStatus");
                startActivity(intent2);
                break;
            case R.id.slide4:
                Intent intent4 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent4.putExtra("title", "Cats Guide");
                intent4.putExtra("color", getResources().getColor(R.color.slide4));

                intent4.putExtra("name", "SupportYourPeerstoStartART");
                startActivity(intent4);
                break;
            case R.id.slide5:
                Intent intent5 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent5.putExtra("title", "Cats Guide");
                intent5.putExtra("color", getResources().getColor(R.color.slide5));

                intent5.putExtra("name", "SupportingYourPeerstoEngageinServices");
                startActivity(intent5);
                break;
            case R.id.slide6:
                Intent intent6 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent6.putExtra("title", "Cats Guide");
                intent6.putExtra("color", getResources().getColor(R.color.slide6));


                intent6.putExtra("name", "SupportingtheMentalHealthofYourPeers");
                startActivity(intent6);
                break;
            case R.id.slide7:
                Intent intent7 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent7.putExtra("title", "Cats Guide");
                intent7.putExtra("color", getResources().getColor(R.color.slide7));


                intent7.putExtra("name", "SupportingSRHofYourPeers");
                startActivity(intent7);
                break;
            case R.id.slide8:
                Intent intent8 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent8.putExtra("title", "Cats Guide");
                intent8.putExtra("color", getResources().getColor(R.color.slide8));


                intent8.putExtra("name", "SupportingSocialProtectionforYourPeers");
                startActivity(intent8);
                break;
            case R.id.slide9:
                Intent intent9 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent9.putExtra("title", "Cats Guide");
                intent9.putExtra("color", getResources().getColor(R.color.slide9));


                intent9.putExtra("name", "SupportingYourPeerswithTB");
                startActivity(intent9);
                break;
            case R.id.slide10:
                Intent intent10 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent10.putExtra("title", "Cats Guide");
                intent10.putExtra("color", getResources().getColor(R.color.slide10));

                intent10.putExtra("name", "SupportingYourPeerswithDisability");
                startActivity(intent10);
                break;
            case R.id.slide11:
                Intent intent11 = new Intent(CatsGuideActivity.this, PdfActivity.class);
                intent11.putExtra("title", "Cats Guide");
                intent11.putExtra("color", getResources().getColor(R.color.slide11));
                intent11.putExtra("name", "SupportingYourPeerswhoarePregnantandBreastfeeding");
                startActivity(intent11);
                break;


        }
    }


}
