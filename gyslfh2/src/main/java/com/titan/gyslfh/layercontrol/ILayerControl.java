package com.titan.gyslfh.layercontrol;

import com.titan.model.FireRiskModel;

/**
 * Created by whs on 2017/5/11
 * 图层控制接口
 */

public interface ILayerControl {
    /**
     * close
     */
    void closeLayerControl();

    /**
     * 底图选择
     * @param position
     */
    void showBaseMap(int position);

    /**
     * 专题图层
     * @param index
     * @param isvisable
     */
    void onCheckLayer(int index,boolean isvisable);

    /**
     * 火点等级图
     * @param fireRiskModel
     */
    void showWeatherLayer(FireRiskModel fireRiskModel);

    void closeWeatherLayer();


}
