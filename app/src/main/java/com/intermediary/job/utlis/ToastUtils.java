package com.intermediary.job.utlis;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by kalogchen on 2016/12/19.
 */

public class ToastUtils {
    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
