package com.kilomelo.panda.floatview;

import android.app.Application;
import android.view.Gravity;

import androidx.annotation.NonNull;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hjq.xtoast.XToast;
import com.kilomelo.panda.MainActivity;
import com.kilomelo.panda.R;
import com.kilomelo.tools.LogTool;

import java.util.List;

public class FloatButton extends XToast {
    private static final String TAG = FloatButton.class.getSimpleName();

    private static final int OPERATION_PANEL_DURATION = 5000;
    private OperationPanel mOperationPanel;

    private FloatButtonDraggable mDraggable;
    public FloatButton(Application application) {
        super(application);
        LogTool.logMethod();
        init();
    }

    private void init()
    {
        LogTool.logMethod();
        setContentView(R.layout.float_button);

        setGravity(Gravity.TOP | Gravity.START);
        // 设置指定的拖拽规则
        mDraggable = new FloatButtonDraggable();
        setDraggable(mDraggable);
    }

    @Override
    public void cancel() {
        LogTool.logMethod();
        if (null != mOperationPanel) mOperationPanel.cancel();
        super.cancel();
    }
    //region business
    private void showOperationPanel(boolean show) {
        LogTool.logMethod();
        if (null == mOperationPanel) {
            mOperationPanel = new OperationPanel(MainActivity.getInstance().getApplication());
        }
        if (show) {
            XXPermissions.with(MainActivity.getInstance())
                .permission(Permission.SYSTEM_ALERT_WINDOW)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(@NonNull List<String> granted, boolean all) {
                        LogTool.logMethod();
                        mOperationPanel.setDuration(OPERATION_PANEL_DURATION).setOnToastLifecycle(new OnLifecycle() {
                            @Override
                            public void onDismiss(XToast<?> toast) {
                                OnLifecycle.super.onDismiss(toast);
                                mDraggable.mEnableDrag = true;
                            }
                        }).show();
                        mDraggable.mEnableDrag = false;
                    }

                    @Override
                    public void onDenied(@NonNull List<String> denied, boolean never) {
                        new XToast<>(MainActivity.getInstance())
                                .setDuration(1000)
                                .setContentView(R.layout.window_hint)
                                .setImageDrawable(android.R.id.icon, R.mipmap.ic_dialog_tip_error)
                                .setText(android.R.id.message, "请先授予悬浮窗权限")
                                .show();
                    }
                });
        } else {
            mOperationPanel.cancel();
            mDraggable.mEnableDrag = true;
        }
    }
    public void ToggleOperationPanel()
    {
        LogTool.logMethod();
        showOperationPanel(null == mOperationPanel || !mOperationPanel.isShowing());
    }
    public void awakeMainWindow()
    {
        LogTool.logMethod();
        if (null == mOperationPanel || !mOperationPanel.isShowing()) {
            MainActivity.getInstance().moveToFront();
        }
    }
    //endregion
}