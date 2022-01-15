package com.example.covidpassproject;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
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
    TextView t1,t2;

    private CodeScanner mCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner);
        im = (ImageButton) findViewById(R.id.imageButton);
        w = (WebView) findViewById(R.id.webView2);
        t1 = (TextView) findViewById(R.id.idt);
        t2 = (TextView) findViewById(R.id.vidt);

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

        popup = new Dialog(this, R.style.PauseDialog);

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
                        t1.setText(result.getText().substring(0, 4));
                        t2.setText(result.getText().substring(result.getText().length() - 16));
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
        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!t1.getText().toString().equals("No Qr detected")) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", t1.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "saved to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
        });
        t2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!t2.getText().toString().equals("No Qr detected")) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("label", t2.getText().toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(getApplicationContext(), "saved to clipboard", Toast.LENGTH_SHORT).show();
                }
            }
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