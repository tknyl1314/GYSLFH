package com.titan.gis.plot;

/**
 * Created by whs on 2017/7/18
 */

public interface IPlot {
    void test();

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
}
