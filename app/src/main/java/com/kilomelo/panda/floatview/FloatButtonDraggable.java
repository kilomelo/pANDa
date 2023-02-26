package com.kilomelo.panda.floatview;

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

public class FloatButtonDraggable extends SpringDraggable implements View.OnClickListener, ConfigHolder {
    private static String TAG = FloatButtonDraggable.class.getSimpleName();
    public boolean mEnableDrag;

    public FloatButtonDraggable() {
        super();
        mEnableDrag = true;
    }

    @Override
    public void start(XToast<?> toast) {
        super.start(toast);
        LogTool.logMethod();
        View view = toast.getDecorView();
//        View fab = view.findViewById(R.id.float_btn);
//        if (null == fab) Log.e(TAG, "fab is null");
//        else {
//            fab.setOnClickListener(this);
//        }

        deserializeLocation();
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
                    serializeLocation();
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
//        if (viewId == R.id.float_btn) {
//            Log.d(TAG, "float_btn clicked");
//            FloatButton floatButton = (FloatButton)getXToast();
//            if (null == floatButton) {
//                Log.e(TAG, "floatButton is null");
//            }
//            else floatButton.ToggleOperationPanel();
//        }
    }

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
                editor.apply();
            }
        }
    }


    @Override
    public SharedPreferences getSharedPreference() {
        return MainActivity.getInstance().getSharedPreferences(TAG, Context.MODE_PRIVATE);
    }
}
