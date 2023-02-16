package com.kilomelo.panda;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Bundle;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.toast.ToastUtils;
import com.kilomelo.tools.LogTool;
import com.kilomelo.tools.PersistentData;

public class MainActivity extends AppCompatActivity {

    // region lifecycle
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.logMethod();
        PersistentData.getInstance().init(this);
        setContentView(R.layout.activity_main);

        TitleBar titleBar = findViewById(R.id.tb_main_bar);
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override public void onTitleClick(TitleBar titleBar) {
            LogTool.logMethod();
            ToastUtils.show("Hello pANDa.");
            }
        });
    }
    @Override protected void onDestroy()
    {
        LogTool.logMethod();
        super.onDestroy();
    }

    @Override protected void onStop()
    {
        super.onStop();
        LogTool.logMethod();
    }

    @Override protected void onStart()
    {
        super.onStart();
        LogTool.logMethod();
    }

    @Override protected void onPause()
    {
        super.onPause();
        LogTool.logMethod();
    }

    @Override protected void onResume()
    {
        super.onResume();
        LogTool.logMethod();
    }

    @Override public void onLowMemory()
    {
        super.onLowMemory();
        LogTool.logMethod();
    }

    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        LogTool.logMethod();
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {}
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        LogTool.logMethod();
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        LogTool.logMethod("hasFocus: " + hasFocus);
    }
    // endregion
}