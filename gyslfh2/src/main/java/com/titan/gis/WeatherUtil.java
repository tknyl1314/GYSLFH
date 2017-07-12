package com.titan.gis;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.titan.model.FireRiskModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whs on 2017/5/25
 * 气象数据解析工具类
 */

public class WeatherUtil {
    //透明度
    public final  static float OPACITY=0.6f;


    public static List<Graphic> creatFireRiskGraphicsOverlay(FireRiskModel fireRiskModel) {
        List<Graphic> graphics=new ArrayList<Graphic>();

        int wkid = fireRiskModel.getSpatialReference().getWkid();
        for (int i = 0; i < fireRiskModel.getFeatures().size(); i++) {
            Graphic graphic = null;
            //等级
            int level = fireRiskModel.getFeatures().get(i).getAttributes().getLEVEL();
            List<List<List<Double>>> rings = fireRiskModel.getFeatures().get(i).getGeometry().getRings();
            for (List<List<Double>> ring : rings) {
                PointCollection points = new PointCollection(SpatialReference.create(wkid));
                for (List<Double> pts : ring) {
                    Point pt = new Point(pts.get(0), pts.get(1));
                    points.add(pt);

                }
                PolygonBuilder polygonGeometry = new PolygonBuilder(points, SpatialReference.create(wkid));
                switch (level) {

                    case 1:
                        graphic = new Graphic(polygonGeometry.toGeometry(), SymbolUtil.risk1_Symbol);
                        break;
                    case 2:
                        graphic = new Graphic(polygonGeometry.toGeometry(), SymbolUtil.risk2_Symbol);

                        break;
                    case 3:
                        graphic = new Graphic(polygonGeometry.toGeometry(), SymbolUtil.risk3_Symbol);

                        break;
                    case 4:
                        graphic = new Graphic(polygonGeometry.toGeometry(), SymbolUtil.risk4_Symbol);

                        break;
                    case 5:
                        graphic = new Graphic(polygonGeometry.toGeometry(), SymbolUtil.risk5_Symbol);
                        break;
                }
                graphics.add(graphic);

            }

        }
        return  graphics;
    }
}
