package com.glowingsoft.zvandiri.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.github.barteksc.pdfviewer.PDFView;
import com.glowingsoft.zvandiri.R;

public class PdfActivity extends AppCompatActivity {
    Toolbar toolbar;
    PDFView pdf;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getIntent().getExtras().getInt("color"));
        }
        setContentView(R.layout.activity_pdf);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("" + getIntent().getExtras().getString("title"));
        toolbar.setBackgroundColor(getIntent().getExtras().getInt("color"));
        rootLayout = findViewById(R.id.rootLayout);
        rootLayout.setBackgroundColor(getIntent().getExtras().getInt("color"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        pdf = findViewById(R.id.pdfView);
        pdf.setBackgroundColor(getIntent().getExtras().getInt("color"));

        String name = getIntent().getExtras().getString("name");
        pdf.fromAsset(name + ".pdf").load();
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
}
