package com.kilomelo.panda;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hjq.bar.OnTitleBarListener;
import com.hjq.bar.TitleBar;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.toast.ToastUtils;
import com.hjq.xtoast.XToast;
import com.kilomelo.panda.overlay.FloatButton;
import com.kilomelo.tools.LogTool;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = MainActivity.class.getSimpleName();

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
        setContentView(R.layout.activity_main);

        TitleBar titleBar = findViewById(R.id.tb_main_bar);
        Context context = this;
        titleBar.setOnTitleBarListener(new OnTitleBarListener() {
            @Override public void onTitleClick(TitleBar titleBar) {
            LogTool.logMethod();
            ToastUtils.show("Hello pANDa.");
            }
        });

        findViewById(R.id.button).setOnClickListener(this);

        mFloatButton = new FloatButton(getApplication());
        mFloatButton.init(this);

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
            XXPermissions.with(this)
                    .permission(Permission.SYSTEM_ALERT_WINDOW)
                    .request(new OnPermissionCallback() {

                        @Override
                        public void onGranted(@NonNull List<String> granted, boolean all) {
                            showGlobalWindow(getApplication());
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
    }

    private void showGlobalWindow(Application app)
    {
        LogTool.logMethod();
        if (null == mFloatButton) {
            Log.e(TAG, "mFloatWindow not inited.");
            return;
        }
        mFloatButton.show();
        moveToBackground();
//        new XToast<>(app)
//                .setContentView(R.layout.window_hint)
//                .setGravity(Gravity.END | Gravity.BOTTOM)
//                .setYOffset(200)
//                .setText(android.R.id.message, "全局弹窗")
//                // 设置指定的拖拽规则
//                .setDraggable(new SpringDraggable())
//                .setOnClickListener(android.R.id.icon, new XToast.OnClickListener<ImageView>() {
//
//                    @Override
//                    public void onClick(XToast<?> toast, ImageView view) {
//                        ToastUtils.show("我被点击了");
//                        toast.cancel();
//                        // 点击后跳转到拨打电话界面
//                        // Intent intent = new Intent(Intent.ACTION_DIAL);
//                        // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        // toast.startActivity(intent);
//                        // 安卓 10 在后台跳转 Activity 需要额外适配
//                        // https://developer.android.google.cn/about/versions/10/privacy/changes#background-activity-starts
//                    }
//                })
//                .show();
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