package com.kilomelo.panda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.kilomelo.tools.LogTool;

import java.util.function.Function;

import app.dinus.com.loadingdrawable.LoadingView;
import app.dinus.com.loadingdrawable.render.LoadingRenderer;

public class SplashActivity extends AppCompatActivity implements Runnable {
    private static String TAG = SplashActivity.class.getSimpleName();

    private boolean mMainActivityStarted = false;
    private long mLatestFinishTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.logMethod();
        setContentView(R.layout.activity_splash);
        Context context = this;

        LoadingView loadingView = findViewById(R.id.day_night_view);
        LoadingRenderer loadingRenderer = loadingView.getLoadingRenderer();
        if (null != loadingRenderer) {
            mLatestFinishTime = SystemClock.uptimeMillis() + loadingRenderer.getDuration();
            loadingRenderer.setCompleteCallback(new Function() {
                @Override
                public Object apply(Object o) {
                    Log.d(TAG, "loadingRenderer anim onCompleteCallback");
                    startMainActivity();
                    return null;
                }
            });
        }
    }

    @Override protected void onResume()
    {
        super.onResume();
        LogTool.logMethod();
        long safeFinishTime = SystemClock.uptimeMillis();
        LoadingView loadingView = findViewById(R.id.day_night_view);
        LoadingRenderer loadingRenderer = loadingView.getLoadingRenderer();
        if (null != loadingRenderer) {
            safeFinishTime += loadingRenderer.getDuration() + 1;
        }
        MainActivity.HANDLER.removeCallbacksAndMessages(this);
        Log.d(TAG, "set delay action");
        MainActivity.HANDLER.postAtTime(this, safeFinishTime);
        mLatestFinishTime = safeFinishTime;
    }

    @Override
    public void run() {
        Log.d(TAG, "run");
        startMainActivity();
    }

    private void startMainActivity()
    {
        if (mMainActivityStarted) {
            Log.d(TAG, "dumplicate startMainActivity");
            return;
        }
        LogTool.logMethod();
        long now = SystemClock.uptimeMillis();
        if (now < mLatestFinishTime) {
            Log.d(TAG, "too early to finish, now: " + now + " expectedFinishTime: " + mLatestFinishTime);
            return;
        }
        LoadingView loadingView = findViewById(R.id.day_night_view);
        LoadingRenderer loadingRenderer = loadingView.getLoadingRenderer();
        if (null != loadingRenderer) {
            loadingRenderer.setCompleteCallback(null);
        }
        mMainActivityStarted = true;
        // 不知道为什么不管用
        MainActivity.HANDLER.removeCallbacksAndMessages(this);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(0, R.anim.splash_moveout);
    }
}