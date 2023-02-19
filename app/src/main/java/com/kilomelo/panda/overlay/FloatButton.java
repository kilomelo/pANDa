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
    private MainActivity mMainActivity;
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
        Log.w(TAG, "do not call cancel on unity floating window, call feeUnity instead");
    }
    //region business
    private void showOperationPanel(boolean show) {
        LogTool.logMethod();
        if (show) {
            new OperationPanel(mMainActivity.getApplication()).show();
            mDraggable.mEnableDrag = false;
        } else {
            mDraggable.mEnableDrag = true;
        }
    }
    public void ToggleOperationPanel()
    {
        if (mOperationPanel == null)
        {
            Log.e(TAG, "mOperationPanel is null");
            return;
        }
        showOperationPanel(!mOperationPanel.isShowing());
    }
    public void awakeMainWindow()
    {
        mMainActivity.moveToFront();
    }
    //endregion
}