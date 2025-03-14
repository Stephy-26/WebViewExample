package com.example.webviewexample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {
    private WebView webView;
    private SwipeRefreshLayout swipeRefresh;
    private static final int PERMISSION_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefresh = findViewById(R.id.swipeContainer);
        webView = findViewById(R.id.web_view);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                onBack();
            }
        });

        swipeRefresh.setOnRefreshListener(() -> {
            webView.reload();
            new Handler().postDelayed(() -> swipeRefresh.setRefreshing(false), 2000);
        });

        getPermissions();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                webView.loadUrl("file:///android_asset/pagina.html");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, android.net.http.SslError error) {
                handler.proceed();
            }
        });

        webView.loadUrl(Constants.NATURE_URL);
    }
    //Ciclo de vida
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("MyApp", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("MyApp", "onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("MyApp", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("MyApp", "onPause");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("MyApp", "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("MyApp", "onStop");
    }

    private void getPermissions() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_AUDIO,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.POST_NOTIFICATIONS
        };

        if (!hasPermissions(this, permissions)) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean hasPermissions(Context context, String... permissions) {
        if (context == null || permissions == null) {
            return false;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Algunos permisos fueron denegados.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private void onBack() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }
}
