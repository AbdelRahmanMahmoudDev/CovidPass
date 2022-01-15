package com.example.covidpassproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class QRCodeScanner extends FragmentActivity {
    Dialog popup;
    ImageButton im;
    WebView w;
    TextView t1;

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        im=(ImageButton)findViewById(R.id.imageButton);
        w=(WebView)findViewById(R.id.webView2);
        t1=(TextView)findViewById(R.id.textView12) ;

        w.setWebViewClient(new WebViewClient());
        w.getSettings().setLoadsImagesAutomatically(true);
        w.getSettings().setJavaScriptEnabled(true);
        w.getSettings().setAllowContentAccess(true);

        w.loadUrl("https://egcovac.mohp.gov.eg/#/track-request");

        w.getSettings().setUseWideViewPort(true);
        w.getSettings().setLoadWithOverviewMode(true);
        w.getSettings().setDomStorageEnabled(true);
        w.clearView();
        w.setHorizontalScrollBarEnabled(false);
        w.getSettings().setAppCacheEnabled(true);
        w.getSettings().setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            w.getSettings().setDatabasePath("/data/data/" + this.getPackageName() + "/databases/");
        }
        w.setVerticalScrollBarEnabled(false);
        w.getSettings().setBuiltInZoomControls(true);
        w.getSettings().setDisplayZoomControls(false);
        w.getSettings().setAllowFileAccess(true);
        w.getSettings().setPluginState(WebSettings.PluginState.OFF);
        w.setScrollbarFadingEnabled(false);
        w.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        w.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        w.setWebViewClient(new WebViewClient());
        w.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        w.setInitialScale(1);

        w.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                }
                return false;
            }
        });

        popup=new Dialog(this);

        popup.setContentView(R.layout.activity_qrcode_scanner);
        popup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        CodeScannerView scannerView = popup.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        t1.setText(result.getText());
                        popup.hide();
                    }
                });
            }
        });
        im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.show();
            }
        });

        scannerView.setOnClickListener(click -> {
            mCodeScanner.startPreview();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

}