package com.glowingsoft.zvandiri.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.glowingsoft.zvandiri.R;
import com.glowingsoft.zvandiri.Utils.ServerConnection;

public class youtubeVideoPlayerActivity extends AppCompatActivity {
    WebView webView;
    String videoId;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youtube_video_player);
        webView = findViewById(R.id.webView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("" + getIntent().getExtras().getString("title"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        videoId = getIntent().getExtras().getString("videoId");
        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    if (webView != null) {

                        webView.stopLoading();
                        webView.destroy();
                    }
                }
                return false;
            }
        });
        if (getIntent().getExtras().getString("id").equals("1")) {
            webView.loadUrl(videoId);
        } else {
            webView.loadUrl(ServerConnection.videoSearch + videoId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.audio_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                webView.stopLoading();
                webView.destroy();
                finish();
                break;
//            case R.id.download:
//                final DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
//                Uri downloadUri = Uri.parse(videoId);
//                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
//                request.setDescription("Downloading a file");
//                long id = downloadmanager.enqueue(request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
//                        .setAllowedOverRoaming(false)
//                        .setTitle("File Downloading...")
//                        .setDescription("Downloading file")
//                        .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getIntent().getExtras().getString("title")));
//                break;

        }
        return super.onOptionsItemSelected(item);
    }
}
