package com.glowingsoft.zvandiri.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.glowingsoft.zvandiri.R;


public class FactSheetActivity extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    RelativeLayout hivTrans, hivTesting, artMont, mentalHealth, srhr, tb, pmtct, prep;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fact_sheet);
        bindViews();
    }

    /*
    binding
     */
    private void bindViews() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("" + getIntent().getExtras().getString("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        hivTrans = findViewById(R.id.hivTrans);
        hivTesting = findViewById(R.id.hivTest);
        artMont = findViewById(R.id.artMont);
        mentalHealth = findViewById(R.id.mentalHealth);
        srhr = findViewById(R.id.srhr);
        tb = findViewById(R.id.tb);
        pmtct = findViewById(R.id.pmtct);
        prep = findViewById(R.id.prep);
        hivTrans.setOnClickListener(this);
        hivTesting.setOnClickListener(this);
        artMont.setOnClickListener(this);
        mentalHealth.setOnClickListener(this);
        srhr.setOnClickListener(this);
        tb.setOnClickListener(this);
        pmtct.setOnClickListener(this);
        prep.setOnClickListener(this);

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
            case R.id.hivTrans:
                Intent intent = new Intent(FactSheetActivity.this, PdfActivity.class);
                intent.putExtra("name", "HIVTransmission");
                intent.putExtra("title", "Fact Sheets");
                intent.putExtra("color", getResources().getColor(R.color.fSlide1));
                startActivity(intent);
                break;
            case R.id.hivTest:
                Intent intenthiv = new Intent(FactSheetActivity.this, PdfActivity.class);
                intenthiv.putExtra("name", "HIVTesting");
                intenthiv.putExtra("title", "Fact Sheets");
                intenthiv.putExtra("color", getResources().getColor(R.color.fSlide2));

                startActivity(intenthiv);
                break;

            case R.id.artMont:
                Intent intenmont = new Intent(FactSheetActivity.this, PdfActivity.class);
                intenmont.putExtra("name", "ARTMonitoring");
                intenmont.putExtra("title", "Fact Sheets");
                intenmont.putExtra("color", getResources().getColor(R.color.fSlide2));

                startActivity(intenmont);
                break;
            case R.id.mentalHealth:
                Intent intenmental = new Intent(FactSheetActivity.this, PdfActivity.class);
                intenmental.putExtra("name", "MentalHealth");
                intenmental.putExtra("title", "Fact Sheets");
                intenmental.putExtra("color", getResources().getColor(R.color.fSlide4));

                startActivity(intenmental);
                break;
            case R.id.srhr:
                Intent intentsrhr = new Intent(FactSheetActivity.this, PdfActivity.class);
                intentsrhr.putExtra("name", "SRHR");
                intentsrhr.putExtra("title", "Fact Sheets");
                intentsrhr.putExtra("color", getResources().getColor(R.color.fSlide5));


                startActivity(intentsrhr);
                break;
            case R.id.tb:
                Intent intenttb = new Intent(FactSheetActivity.this, PdfActivity.class);
                intenttb.putExtra("name", "Tuberclosis");
                intenttb.putExtra("title", "Fact Sheets");
                intenttb.putExtra("color", getResources().getColor(R.color.fSlide6));


                startActivity(intenttb);
                break;
            case R.id.pmtct:
                Intent intenpmtct = new Intent(FactSheetActivity.this, PdfActivity.class);
                intenpmtct.putExtra("name", "PMTCT");
                intenpmtct.putExtra("title", "Fact Sheets");
                intenpmtct.putExtra("color", getResources().getColor(R.color.fSlide7));


                startActivity(intenpmtct);
                break;
            case R.id.prep:
                Intent intentprep = new Intent(FactSheetActivity.this, PdfActivity.class);
                intentprep.putExtra("name", "PrEP");
                intentprep.putExtra("title", "Fact Sheets");
                intentprep.putExtra("color", getResources().getColor(R.color.fSlide8));

                startActivity(intentprep);
                break;
        }
    }
}
