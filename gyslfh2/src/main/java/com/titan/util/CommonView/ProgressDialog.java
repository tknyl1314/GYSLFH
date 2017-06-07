package com.titan.util.CommonView;

import android.app.Dialog;
import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by whs on 2017/5/18
 */

public class ProgressDialog {
    private Context mContext;
    //标题
    private String title;
    //内容
    private String content;
    //样式
    private int style=1;

    private static Dialog instance;

    public Dialog ProgressDialog(Context mContext, String title, String content) {
        this.mContext = mContext;
        this.title = title;
        this.content = content;
        return new  MaterialDialog.Builder(mContext)
                .title(title)
                .content(content)
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

   /* public static Dialog getInstance(Context context,String title,String content){
        return new ProgressDialog(context,title,content);
    }*/




}
