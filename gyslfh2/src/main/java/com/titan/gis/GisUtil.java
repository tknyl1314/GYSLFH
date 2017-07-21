package com.titan.gis;

import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;

/**
 * Created by whs on 2017/7/21
 * gis 公用方法
 */

public class GisUtil {
    /**
     * 添加绘制层
     * @param mapView
     * @return
     */
    public static GraphicsOverlay addGraphicsOverlay(MapView mapView) {
        //create the graphics overlay
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        //add the overlay to the map view
        mapView.getGraphicsOverlays().add(graphicsOverlay);
        return graphicsOverlay;
    }
}
