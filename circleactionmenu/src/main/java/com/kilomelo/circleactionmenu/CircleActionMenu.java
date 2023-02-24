package com.kilomelo.circleactionmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.kilomelo.tools.LogTool;

public class CircleActionMenu extends ViewGroup {
    private static final String TAG = CircleActionMenu.class.getSimpleName();

    private int x = 20;
    private int y = 50;

    public CircleActionMenu(Context context) {
        super(context);
    }

    public CircleActionMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleActionMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        LogTool.logMethod("widthMeasureSpec: " + widthMeasureSpec + " heightMeasureSpec: " + heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        int childCnt = getChildCount();
        for (int i = 0; i < childCnt; i++)
        {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            Log.d(TAG, "child " + i + " childWidth: " + child.getMeasuredWidth() + " childHeight: " + child.getMeasuredHeight());
            width += child.getMeasuredWidth() + x;
            height = Math.max(height, child.getMeasuredHeight() + y * i);
        }
        width -= x;

        Log.d(TAG, "w: " + width + " h: " + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        LogTool.logMethod();
        int left = 0;
        int top = 0;
        int childCnt = getChildCount();
        for (int idx = 0; idx < childCnt; idx++)
        {
            View child = getChildAt(idx);
            if (child.getVisibility() == GONE) continue;
            int r = left + child.getMeasuredWidth();
            int bo = top + child.getMeasuredHeight();
            child.layout(left, top, r, bo);
            Log.d(TAG, "child " + idx + " l: " + left + " top: " + top + " right: " + r + " bottom: " + bo);
            left += x + child.getMeasuredHeight();
            top += y;
        }
    }
}
