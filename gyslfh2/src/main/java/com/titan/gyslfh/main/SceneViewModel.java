package com.titan.gyslfh.main;

import android.content.Context;

import com.titan.BaseViewModel;

/**
 * Created by whs on 2017/7/4
 * 三维场景
 */

public class SceneViewModel extends BaseViewModel {
    private SceneView mView;
    public SceneViewModel(Context context,SceneView sceneView) {
        this.mContext=context;
        this.mView=sceneView;
    }
}
