package com.titan.gis;

import android.graphics.Color;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.titan.model.FireRiskModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by whs on 2017/5/25
 * 气象数据解析
 */

public class WeatherUtil {

    //火险等级样式
    private static SimpleFillSymbol risk1_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.GREEN, null);
    private static SimpleFillSymbol risk2_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.BLUE, null);
    private static SimpleFillSymbol risk3_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, null);
    private static SimpleFillSymbol risk4_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.rgb(255, 165, 58), null);
    private static SimpleFillSymbol risk5_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.RED, null);




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
                        graphic = new Graphic(polygonGeometry.toGeometry(), risk1_Symbol);
                        break;
                    case 2:
                        graphic = new Graphic(polygonGeometry.toGeometry(), risk2_Symbol);

                        break;
                    case 3:
                        graphic = new Graphic(polygonGeometry.toGeometry(), risk3_Symbol);

                        break;
                    case 4:
                        graphic = new Graphic(polygonGeometry.toGeometry(), risk4_Symbol);

                        break;
                    case 5:
                        graphic = new Graphic(polygonGeometry.toGeometry(), risk5_Symbol);
                        break;
                }
                graphics.add(graphic);

            }

            //mainFragment.mGraphicsOverlay.getGraphics().add(graphic);


        }
        return  graphics;
    }
}
