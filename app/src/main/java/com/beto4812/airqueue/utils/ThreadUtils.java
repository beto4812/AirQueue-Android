package com.beto4812.airqueue.utils;

import android.os.Handler;
import android.os.Looper;

public class ThreadUtils {
    private ThreadUtils() {
    }

    public static void runOnUiThread(final Runnable runnable) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new Handler(Looper.getMainLooper()).post(runnable);
        } else {
            runnable.run();
        }
    }
}
