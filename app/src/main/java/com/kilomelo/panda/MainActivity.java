package com.kilomelo.panda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.Toaster;
import com.hjq.xtoast.XToast;
import com.kilomelo.panda.floatview.FloatButton;
import com.kilomelo.tools.LogTool;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final Handler HANDLER = new Handler(Looper.getMainLooper());

    private static volatile MainActivity mInstance;
    public static MainActivity getInstance() { return mInstance; }

    private FloatButton mFloatButton;
    private int mTaskId;
    private boolean mInForeground;
    // region lifecycle
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.logMethod();
        mInstance = this;
        setContentView(R.layout.ope_panel);

//        TitleBar titleBar = findViewById(R.id.tb_main_bar);
//        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
//            @Override public void onTitleClick(TitleBar titleBar) {
//            LogTool.logMethod();
//            Toaster.show("Hello pANDa.");
//            }
//        });
//
//        findViewById(R.id.button).setOnClickListener(this);
//        findViewById(R.id.button3).setOnClickListener(this);
        mTaskId = getTaskId();
    }
    @Override protected void onDestroy()
    {
        LogTool.logMethod();
        mInstance = null;
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
        mInForeground = false;
    }

    @Override protected void onResume()
    {
        super.onResume();
        LogTool.logMethod();
        mInForeground = true;
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
    @Override public void onConfigurationChanged(@NonNull Configuration newConfig)
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
    @Override
    public void onClick(View view) {
        LogTool.logMethod("view: " + view);
        int viewId = view.getId();
        if (viewId == R.id.button) {
            Log.d(TAG, "button clicked");
            if (null != mFloatButton && mFloatButton.isShowing()) return;
            XXPermissions.with(this)
                    .permission(Permission.SYSTEM_ALERT_WINDOW)
                    .request(new OnPermissionCallback() {

                        @Override
                        public void onGranted(@NonNull List<String> granted, boolean all) {
                            LogTool.logMethod();
                            showFloatButton();
                        }

                        @Override
                        public void onDenied(@NonNull List<String> denied, boolean never) {
                            new XToast<>(MainActivity.this)
                                    .setDuration(1000)
                                    .setContentView(R.layout.window_hint)
                                    .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_error)
                                    .setText(android.R.id.message, "请先授予悬浮窗权限")
                                    .show();
                        }
                    });
        }
        if (viewId == R.id.button3) {
            Log.d(TAG, "button3 clicked");
            cancelFloatButton();
        }
    }

    private void showFloatButton()
    {
        LogTool.logMethod();
        if (null == mFloatButton) {
            mFloatButton = new FloatButton(getApplication());
        }
        if (mFloatButton.isShowing()) return;
        mFloatButton.show();
        // 首次获得权限，直接调用不生效
        HANDLER.postDelayed(new Runnable() {
            @Override
            public void run() {
                moveToBackground();
            }
        }, 1);
    }

    private void cancelFloatButton()
    {
        LogTool.logMethod();
        if (null == mFloatButton) {
            Log.e(TAG, "mFloatWindow is null.");
            return;
        }
        if (!mFloatButton.isShowing()) return;
        mFloatButton.cancel();
    }

    public void moveToFront() {
        LogTool.logMethod();
        if (mInForeground) return;
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        List<ActivityManager.RunningTaskInfo> recentTasks = manager.getRunningTasks(Integer.MAX_VALUE);
//        for (int i = 0; i < recentTasks.size(); i++) {
//            Log.d(TAG, "taskId: " + recentTasks.get(i).id + " name: " + recentTasks.get(i).baseActivity.toShortString());
//            // bring to front
//            if (recentTasks.get(i).baseActivity.toShortString().contains(mFullActivityName))
//                manager.moveTaskToFront(recentTasks.get(i).id, ActivityManager.MOVE_TASK_WITH_HOME);
//        }
        manager.moveTaskToFront(mTaskId, ActivityManager.MOVE_TASK_WITH_HOME);
    }

    protected void moveToBackground()
    {
        LogTool.logMethod();
        if (!mInForeground) return;
        moveTaskToBack(true);
    }
}