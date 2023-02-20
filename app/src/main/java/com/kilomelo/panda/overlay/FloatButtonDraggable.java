package com.kilomelo.panda.overlay;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.hjq.xtoast.XToast;
import com.hjq.xtoast.draggable.SpringDraggable;
import com.kilomelo.panda.ConfigHolder;
import com.kilomelo.panda.MainActivity;
import com.kilomelo.panda.R;
import com.kilomelo.tools.LogTool;

public class FloatButtonDraggable extends SpringDraggable implements View.OnClickListener, View.OnLongClickListener, ConfigHolder {
    private static String TAG = FloatButtonDraggable.class.getSimpleName();
    public boolean mEnableDrag;
//    private static int mX;
//    private static int mY;
//    private FloatButton mFloatButton;

    public FloatButtonDraggable() {
        super();
        mEnableDrag = true;
    }
//    @Override
//    protected void updateLocation(int x, int y) {
//        super.updateLocation(x, y);
//        mX = x;
//        mY = y;
//    }

    @Override
    public void start(XToast<?> toast) {
        super.start(toast);
        LogTool.logMethod();
//        mFloatButton = (FloatButton)getXToast();
//        if (null == mFloatButton)
//        {
//            Log.e(TAG, "toast is not FloatBtn");
//            return;
//        }
        View view = toast.getDecorView();
        View btn = view.findViewById(R.id.button2);
        if (null == btn) Log.e(TAG, "btn is null");
        else {
            btn.setOnClickListener(this);
            btn.setOnLongClickListener(this);
        }

//        deserializeLocation();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        if (!mEnableDrag) return false;
        boolean moved = super.onTouch(v, event);
        Log.d(TAG, "moved: " + moved);
        if (moved)
        {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
//                    serializeLocation();
                    break;
                default:
                    break;
            }
        }
        return moved;
    }

    @Override
    public void onClick(View view) {
        LogTool.logMethod();
        int viewId = view.getId();
        if (viewId == R.id.button2) {
            Log.d(TAG, "button2 clicked");
            FloatButton floatButton = (FloatButton)getXToast();
            if (null == floatButton) {
                Log.e(TAG, "floatButton is null");
            }
            floatButton.ToggleOperationPanel();
        }
    }

    @Override
    public boolean onLongClick(View view) {
        LogTool.logMethod();
        int viewId = view.getId();
        if (viewId == R.id.button2) {
            Log.d(TAG, "button2 long clicked");
            FloatButton floatButton = (FloatButton)getXToast();
            if (null == floatButton) {
                Log.e(TAG, "floatButton is null");
            }
            floatButton.awakeMainWindow();
            return true;
        }
        return false;
    }

//    public void relocate()
//    {
//        LogTool.logMethod();
//        updateLocation(mX, mY);
//    }
    private void deserializeLocation()
    {
        LogTool.logMethod();
        SharedPreferences sharedPreferences = getSharedPreference();
        if (null != sharedPreferences)
        {
            int savedX = sharedPreferences.getInt("x", 0);
            int savedY = sharedPreferences.getInt("y", 0);
            Log.d(TAG, "deserialize Location from sharedPreferences, x: " + savedX + " y: " + savedY);
            updateLocation(savedX, savedY);
        }
    }
    public void serializeLocation()
    {
        LogTool.logMethod();
        SharedPreferences sharedPreferences = getSharedPreference();
        if (null != sharedPreferences)
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (null != editor) {
                WindowManager.LayoutParams params = getXToast().getWindowParams();
                if (params == null) {
                    return;
                }
                Log.d(TAG, "serialize Location to sharedPreferences, x: " + params.x + " y: " + params.y);
                editor.putInt("x", params.x);
                editor.putInt("y", params.y);
                editor.commit();
            }
        }
    }


    @Override
    public SharedPreferences getSharedPreference() {
        return MainActivity.getInstance().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }
}
