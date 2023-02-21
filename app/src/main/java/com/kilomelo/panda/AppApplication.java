package com.kilomelo.panda;

import android.app.Application;
import com.hjq.toast.Toaster;
import com.kilomelo.tools.LogTool;

public final class AppApplication extends android.app.Application {
    private static String TAG = Application.class.getSimpleName();
    @Override
    public void onCreate() {
        super.onCreate();
        LogTool.logMethod();
        Toaster.init(this);
    }
}