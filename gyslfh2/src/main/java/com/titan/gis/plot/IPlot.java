package com.titan.gis.plot;

/**
 * Created by whs on 2017/7/18
 */

public interface IPlot {

    /**
     * 图层控制
     */
    void showLayers();

    /**
     * 标绘
     * @param plottype
     */
    void Plot(int plottype);


    /**
     * 撤销
     */
    void onRevok();

    /**
     * 分享
     */
    void onShare();

    /**
     * 保存
     */
    void onSave();

    /**
     * 确定
     */
    void onConfirm();
}
