package com.kilomelo.panda.overlay;

import android.app.Application;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.hjq.xtoast.XToast;
import com.kilomelo.panda.MainActivity;
import com.kilomelo.panda.R;
import com.kilomelo.tools.LogTool;

public class FloatButton extends XToast {
    private static String TAG = FloatButton.class.getSimpleName();

    private static int OPERATION_PANEL_DURATION = 5000;
    private MainActivity mMainActivity;
    private OperationPanel mOperationPanel;
    enum State
    {
        collapsed,
        expanded,
    }

    private FloatButtonDraggable mDraggable;
    private State mState;

    public FloatButton(Application application) {
        super(application);
    }

    public void init(MainActivity mainActivity)
    {
        LogTool.logMethod();
        if (null == mainActivity)
        {
            Log.e(TAG, "mainActivity is null");
            return;
        }
        mMainActivity = mainActivity;
        setContentView(R.layout.float_button);

        setGravity(Gravity.TOP | Gravity.START);
        // 设置指定的拖拽规则
        mDraggable = new FloatButtonDraggable();
        mDraggable.deserializeLocation();
        setDraggable(mDraggable);
        mState = State.collapsed;
    }

    @Override
    public void show() {
        super.show();
        LogTool.logMethod();
        if (null != mDraggable) mDraggable.relocate();

        ViewGroup decorView = (ViewGroup)getDecorView();
        if (null == decorView)
        {
            Log.e(TAG, "decorView of xtoast is null");
        }
    }

    @Override
    public void cancel() {
        LogTool.logMethod();
        mMainActivity = null;
        mDraggable = null;
        if (null != mOperationPanel) mOperationPanel.cancel();
        mOperationPanel = null;
        super.cancel();
    }
    //region business
    private void showOperationPanel(boolean show) {
        LogTool.logMethod();
        if (show) {
            if (null != mOperationPanel)
            {
                Log.e(TAG, "mOperationPanel is not null, perhaps mem leak.");
                return;
            }
            mOperationPanel = new OperationPanel(mMainActivity.getApplication());
            mOperationPanel.setDuration(OPERATION_PANEL_DURATION).setOnToastLifecycle(new OnLifecycle() {
                @Override
                public void onDismiss(XToast<?> toast) {
                    OnLifecycle.super.onDismiss(toast);
                    mOperationPanel = null;
                    mDraggable.mEnableDrag = true;
                }
            }).show();
            mDraggable.mEnableDrag = false;
        } else {
            if (null == mOperationPanel)
            {
                Log.e(TAG, "mOperationPanel is null");
                return;
            }
            else {
                mOperationPanel.cancel();
                mOperationPanel = null;
                mDraggable.mEnableDrag = true;
            }
        }
    }
    public void ToggleOperationPanel()
    {
        LogTool.logMethod();
        showOperationPanel(null == mOperationPanel);
    }
    public void awakeMainWindow()
    {
        LogTool.logMethod();
        if (null == mOperationPanel) mMainActivity.moveToFront();
    }
    //endregion
}