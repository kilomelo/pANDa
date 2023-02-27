package com.kilomelo.circleactionmenu;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kilomelo.tools.LogTool;

import java.util.ArrayList;
import java.util.List;

public class CircleActionMenu extends ViewGroup {
    private static final String TAG = CircleActionMenu.class.getSimpleName();

    public static final int EXPAND_LEFT = 0;
    public static final int EXPAND_RIGHT = 1;
    private static final int ANIMATION_DURATION = 300;

    private int mExpandDirection;
    private boolean mExpanded;
    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);

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
        LogTool.logMethod("changed: " + changed + " l: " + l + " t: " + t + " r: " + r + " b: " + b);
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
            if (mExpandDirection == EXPAND_LEFT) {
                left = r - l - left - childW;
            }
            int top = y - childH / 2;
            int right = left + childW;
            int bottom = top + childH;
            child.layout(left, top, right, bottom);
            Log.d(TAG, "child " + i + " l: " + left + " t: " + top + " r: " + right + " b: " + bottom);
        }
    }

    // region collapse expand
    public void collapse() {
        collapse(false);
    }

    public void collapseImmediately() {
        collapse(true);
    }

    private void collapse(boolean immediately) {
        if (mExpanded) {
            mExpanded = false;
//            mTouchDelegateGroup.setEnabled(false);
            mCollapseAnimation.setDuration(immediately ? 0 : ANIMATION_DURATION);
            mCollapseAnimation.start();
            mExpandAnimation.cancel();

//            if (mListener != null) {
//                mListener.onMenuCollapsed();
//            }
        }
    }

    public void toggle() {
        if (mExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        if (!mExpanded) {
            mExpanded = true;
//            mTouchDelegateGroup.setEnabled(true);
            mCollapseAnimation.cancel();
            mExpandAnimation.start();

//            if (mListener != null) {
//                mListener.onMenuExpanded();
//            }
        }
    }

    public boolean isExpanded() {
        return mExpanded;
    }
    // endregion

    private class LayoutParams extends ViewGroup.LayoutParams {

        private ObjectAnimator mExpandDir = new ObjectAnimator();
        private ObjectAnimator mExpandAlpha = new ObjectAnimator();
        private ObjectAnimator mCollapseDir = new ObjectAnimator();
        private ObjectAnimator mCollapseAlpha = new ObjectAnimator();
        private boolean animationsSetToPlay;

        private Interpolator sExpandInterpolator = new OvershootInterpolator();
        private Interpolator sCollapseInterpolator = new DecelerateInterpolator(3f);
        private Interpolator sAlphaExpandInterpolator = new DecelerateInterpolator();

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);

            mExpandDir.setInterpolator(sExpandInterpolator);
            mExpandAlpha.setInterpolator(sAlphaExpandInterpolator);
            mCollapseDir.setInterpolator(sCollapseInterpolator);
            mCollapseAlpha.setInterpolator(sCollapseInterpolator);

            mCollapseAlpha.setProperty(View.ALPHA);
            mCollapseAlpha.setFloatValues(1f, 0f);

            mExpandAlpha.setProperty(View.ALPHA);
            mExpandAlpha.setFloatValues(0f, 1f);

            switch (mExpandDirection) {
//                case EXPAND_UP:
//                case EXPAND_DOWN:
//                    mCollapseDir.setProperty(View.TRANSLATION_Y);
//                    mExpandDir.setProperty(View.TRANSLATION_Y);
//                    break;
//                case EXPAND_LEFT:
//                case EXPAND_RIGHT:
//                    mCollapseDir.setProperty(View.TRANSLATION_X);
//                    mExpandDir.setProperty(View.TRANSLATION_X);
//                    break;
            }
        }

        public void setAnimationsTarget(View view) {
            mCollapseAlpha.setTarget(view);
            mCollapseDir.setTarget(view);
            mExpandAlpha.setTarget(view);
            mExpandDir.setTarget(view);

            // Now that the animations have targets, set them to be played
            if (!animationsSetToPlay) {
                addLayerTypeListener(mExpandDir, view);
                addLayerTypeListener(mCollapseDir, view);

                mCollapseAnimation.play(mCollapseAlpha);
                mCollapseAnimation.play(mCollapseDir);
                mExpandAnimation.play(mExpandAlpha);
                mExpandAnimation.play(mExpandDir);
                animationsSetToPlay = true;
            }
        }

        private void addLayerTypeListener(Animator animator, final View view) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setLayerType(LAYER_TYPE_NONE, null);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    view.setLayerType(LAYER_TYPE_HARDWARE, null);
                }
            });
        }
    }
}
