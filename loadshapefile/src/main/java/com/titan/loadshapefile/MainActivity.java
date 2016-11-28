package com.titan.loadshapefile;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.esri.android.map.FeatureLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geodatabase.Geodatabase;
import com.esri.core.geodatabase.GeodatabaseFeatureTable;
import com.esri.core.geodatabase.ShapefileFeatureTable;
import com.esri.core.renderer.Renderer;
import com.esri.core.renderer.SimpleRenderer;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.Symbol;
import com.esri.core.table.FeatureTable;

import java.io.FileNotFoundException;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //@BindView(R.id.mapview)
    MapView mapview;
   // @BindView(R.id.btn_addlayer)
    Button btnAddlayer;
    Context mContext;
    FeatureLayer featureLayer,featureLayer2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnAddlayer= (Button) findViewById(R.id.btn_addlayer);
        loadShapeFile();
        btnAddlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadGeodatabase();
            }
        });

        mContext=this;
        ButterKnife.bind(this);
        try {
            mapview= (MapView) findViewById(R.id.mapview);
            // 贵阳矢量切片
           /* String titlePath = ResourcesManager.getInstance(mContext)
                    .getArcGISLocalTiledLayerPath();*/
           // ArcGISTiledMapServiceLayer tiledMapServiceLayer=new ArcGISTiledMapServiceLayer("http://cache1.arcgisonline.cn/ArcGIS/rest/services/ChinaOnlineCommunity/MapServer");
            ArcGISTiledMapServiceLayer tiledMapServiceLayer=new ArcGISTiledMapServiceLayer(" http://223.99.164.236:6080/arcgis/rest/services/ZZLY/ZZLY_BASEMAP/MapServer");


            if (!tiledMapServiceLayer.equals("")) {
                /*ArcGISLocalTiledLayer arcGISLocalTiledLayer = new ArcGISLocalTiledLayer(titlePath);
                arcGISLocalTiledLayer.setVisible(true);*/
                mapview.addLayer(tiledMapServiceLayer);
              // mapview.toMapPoint(new Point(672679.534108, 2972909.353704));
            } else {
                Toast.makeText(mContext, "加载底图失败", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(mContext, "加载底图失败", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadShapeFile() {
        String shpPath="";
        String layerpath="";
        String countypath="";
        try {
             countypath="file:///android_asset/ShapeFile/COUNTY.shp";

       /*     shpPath=ResourcesManager.getInstance(mContext).getlocdb();
            layerpath=ResourcesManager.getInstance(mContext).getlayerpath();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        //String shpPath=getSDPath()+ "/TITAN/test.shp";
        //String shpPath="/mnt/sdcard/download/bou2_4p.shp";
        Toast.makeText(this,shpPath,Toast.LENGTH_LONG).show();
        Symbol symbol=new SimpleFillSymbol(Color.BLUE);
        Symbol symbol2=new SimpleFillSymbol(Color.RED);
        Renderer renderer=new SimpleRenderer(symbol);
        Renderer renderer2=new SimpleRenderer(symbol2);
        try {
            ShapefileFeatureTable shp=new ShapefileFeatureTable(countypath);
          /*  ShapefileFeatureTable shapefileFeatureTable=new ShapefileFeatureTable(shpPath);
            ShapefileFeatureTable shapefileFeatureTable2=new ShapefileFeatureTable(layerpath);
            // Envelope extent= shapefileFeatureTable.getExtent();
            featureLayer=new FeatureLayer(shapefileFeatureTable);
            featureLayer2=new FeatureLayer(shapefileFeatureTable2);*/
            featureLayer=new FeatureLayer(shp);
            featureLayer.setRenderer(renderer);
          /*  featureLayer2.setRenderer(renderer2);
            mapview.addLayer(featureLayer2);*/
            featureLayer.setVisible(true);
            mapview.addLayer(featureLayer);
            Toast.makeText(this,featureLayer.getFeatureTable().getTableName(),Toast.LENGTH_LONG).show();
            FeatureTable tb= featureLayer.getFeatureTable();
            //mMapView.centerAt(extent.getCenter(),true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(mContext,"加载shapefile失败",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_addlayer)
    public void onClick() {
        loadGeodatabase();
    }

    /**
     * 加载离线的geodatabase数据
     *
     */
    public void loadGeodatabase() {

        Geodatabase geodatabase=null;
        try {
             String path=ResourcesManager.getInstance(mContext).getlocdb();
            //InputStream db=getResources().getAssets().open("GYSLFH_1010.sqlite");
             geodatabase = new Geodatabase(path);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mContext, "加载图层失败", Toast.LENGTH_SHORT).show();
            return;
            // ToastUtil.setToast(MapActivity.this, "加载数据错误");
        }

		/*
		 * for (Layer l : mapView.getLayers()) { if (l instanceof
		 * ArcGISDynamicMapServiceLayer) { mapView.removeLayer(l); } }
		 */
        if (geodatabase != null) {
            List<GeodatabaseFeatureTable> list = geodatabase
                    .getGeodatabaseTables();
            for (GeodatabaseFeatureTable gdbFeatureTable : list) {
                if (gdbFeatureTable.hasGeometry()) {
                    final FeatureLayer featureLayer = new FeatureLayer(
                            gdbFeatureTable);
                    mapview.addLayer(featureLayer);
                }
            }

        } else {
            Toast.makeText(mContext, "加载数据错误", Toast.LENGTH_SHORT).show();
        }
    }
}
