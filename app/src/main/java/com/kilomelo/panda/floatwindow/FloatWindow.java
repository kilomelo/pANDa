package com.kilomelo.panda.floatwindow;

import android.app.Application;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;

import com.hjq.xtoast.XToast;
import com.kilomelo.panda.R;
import com.kilomelo.tools.LogTool;

public class FloatWindow extends XToast {
    private static String TAG = FloatWindow.class.getSimpleName();

    enum State
    {
        collapsed,
        expanded,
    }

    private FloatWindowDraggable mDraggable;
    private int mCollapseWidth = 200;
    private int mCollapseHeight = 200;
    private int mExpandWidth = 400;
    private int mExpandHeight = 600;
    private State mState;

    public FloatWindow(Application application) {
        super(application);
        LogTool.logMethod();
        setContentView(R.layout.window_float_window);
        setGravity(Gravity.TOP | Gravity.START);
        // 设置指定的拖拽规则
        mDraggable = new FloatWindowDraggable();
        mDraggable.deserializeLocation();
        setDraggable(mDraggable);
        mState = State.collapsed;
    }

    @Override
    public void show() {
        super.show();
        LogTool.logMethod();
        if (null != mDraggable) mDraggable.relocate();

        setSize(mCollapseWidth, mCollapseHeight);

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

    public void setSize(int x, int y)
    {
        LogTool.logMethod("x: " + x + " y: " + y);
        setWidth(x);
        setHeight(y);
        postUpdate();
    }
    //region business

    public void setConfig(int collapseWidth, int collapseHeight, int expandWidth, int expandHeight)
    {
        LogTool.logMethod("collapseWidth: " + collapseWidth + " collapseHeight: " + collapseHeight + " expandWidth: " + expandWidth + " expandHeight: " + expandHeight);
        mCollapseWidth = collapseWidth;
        mCollapseHeight = collapseHeight;
        mExpandWidth = expandWidth;
        mExpandHeight = expandHeight;
    }

    private void expand(String params)
    {
        LogTool.logMethod();
        mState = State.expanded;
        setSize(mExpandWidth, mExpandHeight);
    }
    private void collapse(String params)
    {
        LogTool.logMethod();
        mState = State.collapsed;
        setSize(mCollapseWidth, mCollapseHeight);
    }
    //endregion
}