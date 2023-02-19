package com.kilomelo.panda.overlay;

import android.app.Application;

import com.hjq.xtoast.XToast;
import com.kilomelo.panda.R;
import com.kilomelo.tools.LogTool;

public class OperationPanel extends XToast {
    private static String TAG = OperationPanel.class.getSimpleName();

    public OperationPanel(Application application) {
        super(application);
        LogTool.logMethod();
        setContentView(R.layout.operation_panel);

    }
}
