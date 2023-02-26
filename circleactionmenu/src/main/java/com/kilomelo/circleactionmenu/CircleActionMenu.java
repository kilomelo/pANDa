package com.kilomelo.circleactionmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kilomelo.tools.LogTool;

import java.util.ArrayList;
import java.util.List;

public class CircleActionMenu extends ViewGroup {
    private static final String TAG = CircleActionMenu.class.getSimpleName();



    private float mCenterX;
    private float mCenterY;
    private float mRadius = 500;
    private float mPrimaryBtnAngle = 0;
    private float mAngleInterval = 25;
    private List<Float> mX;
    private List<Float> mY;

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

        if (null == mX) mX = new ArrayList<>();
        if (null == mY) mY = new ArrayList<>();

        mX.clear();
        mY.clear();

        float xMin = 0;
        float xMax = 0;
        float yMin = 0;
        float yMax = 0;

        float angle = mPrimaryBtnAngle;
        if (angle < 0) angle = 0;
        if (angle > 90) angle = 90;

        int childCnt = getChildCount();
        for (int i = 0; i < childCnt; i++)
        {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            int childW = child.getMeasuredWidth();
            int childH = child.getMeasuredHeight();
            double radians = Math.toRadians(angle);
            float centerX = mRadius * (float)Math.sin(radians);
            float centerY = -mRadius * (float)Math.cos(radians);
            Log.d(TAG, "child " + i + " childWidth: " + childW + " childHeight: " + childH + " x: " + centerX + " y: " + centerY + " angle: " + angle);
            mX.add(centerX);
            mY.add(centerY);
            angle += mAngleInterval;

            if (0 == i) {
                xMin = centerX - childW * 0.5f;
                xMax = centerX + childW * 0.5f;
                yMin = centerY - childH * 0.5f;
                yMax = centerY + childH * 0.5f;
            }
            else {
                xMin = Math.min(xMin, centerX - childW * 0.5f);
                xMax = Math.max(xMax, centerX + childW * 0.5f);
                yMin = Math.min(yMin, centerY - childH * 0.5f);
                yMax = Math.max(yMax, centerY + childH * 0.5f);
            }
            Log.d(TAG, "xMin: " + xMin + " xMax: " + xMax + " yMin: " + yMin + " yMax: " + yMax + " width: " + (int)(xMax - xMin) + " height: " + (int)(yMax - yMin));
        }
        setMeasuredDimension((int)(xMax - xMin), (int)(yMax - yMin));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        LogTool.logMethod();
        int offsetX = 0;
        int offsetY = 0;
        int childCnt = getChildCount();
        for (int i = 0; i < childCnt; i++)
        {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            int childW = child.getMeasuredWidth();
            int childH = child.getMeasuredHeight();
            int x = Math.round(mX.get(i));
            int y = Math.round(mY.get(i));
            Log.d(TAG, "child " + i + " x: " + x + " y: " + y + " childW: " + childW + " childH: " + childH);
            if (0 == i) {
                offsetX = -(int)(x - childW * 0.5f);
                offsetY = -(int)(y - childH * 0.5f);
                Log.d(TAG, "set offset to " + offsetX + " / " + offsetY);
            }
            x += offsetX;
            y += offsetY;
            int left = x - childW / 2;
            int top = y - childH / 2;
            int right = x + childW / 2;
            int bottom = y + childH / 2;
            child.layout(left, top, right, bottom);
            Log.d(TAG, "child " + i + " l: " + left + " t: " + top + " r: " + right + " b: " + bottom);
        }
    }
}
