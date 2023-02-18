package com.kilomelo.panda;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.kilomelo.tools.LogTool;

import java.util.function.Function;

import app.dinus.com.loadingdrawable.LoadingView;
import app.dinus.com.loadingdrawable.render.LoadingRenderer;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.logMethod();
        setContentView(R.layout.activity_splash);
        Context context = this;

        LoadingView loadingView = findViewById(R.id.day_night_view);
        LoadingRenderer loadingRenderer = loadingView.getLoadingRenderer();
        if (null != loadingRenderer) loadingRenderer.setCompleteCallback(new Function() {
            @Override
            public Object apply(Object o) {
                LogTool.logMethod();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(0, R.anim.splash_moveout);
                return null;
            }
        });
    }
}