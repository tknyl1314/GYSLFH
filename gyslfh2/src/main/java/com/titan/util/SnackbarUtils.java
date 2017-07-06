package com.titan.util;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Provides a method to show a Snackbar.
 * 提示类
 */
public class SnackbarUtils {

    /**
     * 长时间
     * @param v
     * @param snackbarText
     */
    public static void showSnackbar(View v, String snackbarText) {
        if (v == null || snackbarText == null) {
            return;
        }
        Snackbar.make(v, snackbarText, Snackbar.LENGTH_LONG).show();
    }

    /**
     * 短时间
     * @param v
     * @param snackbarText
     */
    public static void showSnackbarShort(View v, String snackbarText) {
        if (v == null || snackbarText == null) {
            return;
        }
        Snackbar.make(v, snackbarText, Snackbar.LENGTH_SHORT).show();
    }

    /**
     *
     * 无限期
     * @param v
     * @param snackbarText
     */
    public static void showSnackbarIndefinite(View v, String snackbarText) {
        if (v == null || snackbarText == null) {
            return;
        }
        Snackbar.make(v, snackbarText, Snackbar.LENGTH_INDEFINITE).show();
    }
}
