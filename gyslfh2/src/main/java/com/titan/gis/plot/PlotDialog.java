package com.titan.gis.plot;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaActionSound;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Multipoint;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.mapping.view.SketchCreationMode;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.esri.arcgisruntime.mapping.view.SketchStyle;
import com.esri.arcgisruntime.symbology.FillSymbol;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.MarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.util.ListChangedEvent;
import com.esri.arcgisruntime.util.ListChangedListener;
import com.titan.gis.Calculate;
import com.titan.gis.SymbolUtil;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.DialogPlotBinding;
import com.titan.util.DeviceUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by whs on 2017/7/18
 * 态势标绘
 */

public class PlotDialog extends DialogFragment implements IPlot{
    //基本线样式
    private static LineSymbol plotlineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID , Color.BLACK, 3);
    //箭头样式
    private static FillSymbol plotArrowfill = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,Color.argb(80, 255, 0, 0),plotlineSymbol);
    // 防火带样式
    private static LineSymbol plotbreaklineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 20);
    // 火场范围
    static FillSymbol plotFireArea = new SimpleFillSymbol(SimpleFillSymbol.Style.FORWARD_DIAGONAL,Color.RED,plotlineSymbol);
    // 火点样式
    public static MarkerSymbol firepointSymbol;
    public static MarkerSymbol flagSymbol;


    public void setActive(boolean active) {
        this.active = active;
    }

    //标绘tag
    private boolean active = false;
    public Graphic plotgarphic;
    //点集合
    private PointCollection mPtCollection;
    //构造器
    private PolylineBuilder mPolylineBuilder;
    private PolygonBuilder mPolygonBuilder;

    public void setViewmodel(PlotViewModel viewmodel) {
        this.mViewModel = viewmodel;
    }

    @Override
    public void showLayers() {

    }

    @Override
    public void Plot(int plottype) {
        /*if(active){

            saveSketchGraphic(mPlotType);
            active=!active;
        }*/
        switch (plottype){
            case 1:
                mPlotType=PlotType.ARROW;
                sketchStyle.setVertexSymbol(SymbolUtil.vertexSymbol);
                sketchEditor.start(SketchCreationMode.MULTIPOINT);
                mViewModel.snackbarText.set("箭头");
                //activate(PlotType.ARROW);
                break;
            case 2:
                active=true;
                mPlotType=PlotType.FIREAREA;
                sketchStyle.setFillSymbol(plotFireArea);
                sketchEditor.start(SketchCreationMode.FREEHAND_POLYGON);
                mViewModel.snackbarText.set("火场范围");
                //activate(PlotType.FIREAREA);
                break;
            case 3:
                active=true;
                mPlotType=PlotType.FIREBREAK;
                sketchStyle.setLineSymbol(plotbreaklineSymbol);
                sketchEditor.start(SketchCreationMode.FREEHAND_LINE);
                mViewModel.snackbarText.set("防火带");
                //activate(PlotType.FIREBREAK);
                break;
            case 4:
                mPlotType=PlotType.FIREPOINT;
                sketchStyle.setVertexSymbol(firepointSymbol);
                sketchEditor.start(SketchCreationMode.POINT);
                mViewModel.snackbarText.set("火点");
                //activate(PlotType.FIREPOINT);
                break;
            case 5:
                mPlotType=PlotType.FLAG;
                sketchStyle.setVertexSymbol(flagSymbol);
                sketchEditor.start(SketchCreationMode.POINT);
                mViewModel.snackbarText.set("旗帜");
                //activate(PlotType.FLAG);
                break;
        }
    }

    /**
     * 保存绘制的图形
     * @param plottype
     */
    private static void saveSketchGraphic(PlotType plottype) {
        Graphic graphic=null;
        switch (plottype){
            case ARROW:
                Multipoint pts= (Multipoint) sketchEditor.getGeometry();
                if(pts.getPoints().size()>=2){
                    PointCollection ptscollection=new PointCollection(pts.getPoints());
                    graphic = plotArrow(pts.getPoints().get(pts.getPoints().size()), ptscollection);
                    //mPlotOverlay.getGraphics().add(graphic);
                }
                break;
            case FIREAREA:
                if(sketchEditor.getGeometry()!=null){
                    graphic=new Graphic(sketchEditor.getGeometry(),plotFireArea);

                }
                //mPlotOverlay.getGraphics().add(graphic);
                break;
            case FIREBREAK:
                graphic=new Graphic(sketchEditor.getGeometry(),plotbreaklineSymbol);
                break;
            case FIREPOINT:
                graphic=new Graphic(sketchEditor.getGeometry(),firepointSymbol);

                break;
            case FLAG:
                graphic=new Graphic(sketchEditor.getGeometry(),flagSymbol);

                break;
        }
        mPlotOverlay.getGraphics().add(graphic);
    }

    @Override
    public void onRevok() {

    }

    /**
     * 分享
     */
    @Override
    public void onShare() {
        captureScreenshotAsync();
    }

    @Override
    public void onSave() {

    }

    @Override
    public void onConfirm() {

    }

    /**
     * capture the map as an image
     */
    private void captureScreenshotAsync() {

        // export the image from the mMapView
        final ListenableFuture<Bitmap> export = mMapView.exportImageAsync();
        export.addDoneListener(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap currentMapImage = export.get();
                    // play the camera shutter sound
                    MediaActionSound sound = new MediaActionSound();
                    sound.play(MediaActionSound.SHUTTER_CLICK);
                    //Log.d(TAG,"Captured the image!!");
                    // save the exported bitmap to an image file
                    //SaveImageTask saveImageTask = new SaveImageTask();
                    //saveImageTask.execute(currentMapImage);
                } catch (Exception e) {
                    mViewModel.snackbarText.set("截图失败"+e.toString());
                    //Toast.makeText(getApplicationContext(), getResources().getString(R.string.map_export_failure) + e.getMessage(), Toast.LENGTH_SHORT).show();
                    //Log.e(TAG, getResources().getString(R.string.map_export_failure) + e.getMessage());
                }
            }
        });
    }

    //标绘类型
    public enum PlotType {

        //贝塞尔箭头
        ARROW,
        //贝塞尔多边形
        JUNGLE,
        //火场范围
        FIREAREA,
        //防火带
        FIREBREAK,
        //火点
        FIREPOINT,
        //旗帜
        FLAG

    }

    private static PlotType mPlotType;


    public static PlotDialog Singleton;

    private static Context mContext;

    private static MapView mMapView;
    //标绘层
    private static GraphicsOverlay mPlotOverlay;
    //当前标绘的图形id
    private   int plotgraphicID=0;

    private DialogPlotBinding mDatabinding;

    private PlotViewModel mViewModel;
    //绘制样式
    private SketchStyle sketchStyle;
    //草图编辑器
    private static SketchEditor sketchEditor;

    public PlotTouchListener getmPlotTouchLisener() {
        return mPlotTouchLisener;
    }

    private static PlotTouchListener mPlotTouchLisener;

    public static PlotDialog getInstance(Context context,MapView mapView){
        if(Singleton==null){
            // = context;
            mMapView = mapView;
            //mPlotTouchLisener=new PlotTouchListener(context,mapView)
            Singleton=new PlotDialog();
        }
        //mPlotTouchLisener=new PlotTouchListener(context,mapView);
        return Singleton;

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                if (DeviceUtil.isTablet(getActivity())) {
                    window.setGravity(Gravity.TOP);
                } else {
                    window.setGravity(Gravity.BOTTOM);
                }
                //设置背景颜色,只有设置了这个属性,宽度才能全屏MATCH_PARENT
                window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));//设置dialog背景透明
            }

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.PlotDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mDatabinding= DataBindingUtil.inflate(inflater, R.layout.dialog_plot,container,false);
        mDatabinding.setViewmodel(mViewModel);
        mContext=getActivity();
        mPlotOverlay = addGraphicsOverlay();
        mPlotOverlay.getGraphics().addListChangedListener(new ListChangedListener<Graphic>() {
            @Override
            public void listChanged(ListChangedEvent<Graphic> listChangedEvent) {
                plotgraphicID=listChangedEvent.getIndex();
                Log.e("TItan","plotgraphicID"+plotgraphicID);
            }
        });
        //PlotTouchListener da=new PlotTouchListener(,mMapView);
        //mMapView.setOnTouchListener(new PlotTouchListener(,mMapView));

        sketchEditor = new SketchEditor();
        sketchStyle=new SketchStyle();
        //sketchStyle.setFeedbackLineSymbol(plotbreaklineSymbol);
        //sketchStyle.setLineSymbol(plotlineSymbol);
        sketchEditor.setSketchStyle(sketchStyle);
        mMapView.setSketchEditor(sketchEditor);
        return mDatabinding.getRoot();

    }

    private GraphicsOverlay addGraphicsOverlay() {
        //create the graphics overlay
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        //add the overlay to the map view
        mMapView.getGraphicsOverlays().add(graphicsOverlay);
        return graphicsOverlay;
    }


    /**
     * 地图事件监听
     */
    public static class PlotTouchListener extends DefaultMapViewOnTouchListener {
        MapView mapview;
        Context context;

        public PlotTouchListener(Context context, MapView mapview) {
            super(context, mapview);
            this.mapview = mapview;
            this.context = context;
        }

        /**
         * 双击
         * @param event
         * @return
         */
        @Override
        public boolean onDoubleTouchDrag(MotionEvent event) {
            saveSketchGraphic(mPlotType);
            return true;
        }







    }
    /**
     * 结束标绘
     */
    public void deactivate() {
        active = false;
    }

    /**
     * 启动标绘
     * @param plotType
     */
    public void activate(PlotType plotType) {
        if (mMapView == null)
            return;

        deactivate();
        //this.plotType = plotType;
        //plotgraphicID++;
        mPlotType=plotType;
        mPtCollection=new PointCollection(mMapView.getSpatialReference());
        active = true;
        switch (plotType) {
            //防火带
            case FIREBREAK:
                //polyline = new Polyline(mPtCollection);
                mPolylineBuilder=new PolylineBuilder(mPtCollection);
                plotgarphic = new Graphic(mPolylineBuilder.toGeometry(), plotbreaklineSymbol);
                mPlotOverlay.getGraphics().add(plotgarphic);
                break;
            //火场范围
            case FIREAREA:
                mPolygonBuilder=new PolygonBuilder(mPtCollection);
                //polygon = new Polygon(mPtCollection);
                plotgarphic = new Graphic(mPolygonBuilder.toGeometry(), plotFireArea);
                mPlotOverlay.getGraphics().add(plotgarphic);
                break;
            //箭头
            case ARROW:
                mPolygonBuilder=new PolygonBuilder(mPtCollection);
                //polyline = new Polyline(mPtCollection);
                plotgarphic = new Graphic(mPolygonBuilder.toGeometry(), plotArrowfill);
                mPlotOverlay.getGraphics().add(plotgarphic);
                break;


            case JUNGLE:
                //集结地
                mPolygonBuilder=new PolygonBuilder(mPtCollection);
                //polygon = new Polygon(mPtCollection);
                plotgarphic = new Graphic(mPolygonBuilder.toGeometry(), plotArrowfill);
                mPlotOverlay.getGraphics().add(plotgarphic);
                break;
            //旗帜
            case FLAG:
                //火点
            case FIREPOINT:
                break;
        }
    }

    /**
     * 绘制贝塞尔箭头
     */
    public static Graphic plotArrow(Point candidatePoint, PointCollection mPtCollection) {
        double px, py, pre_px, pre_py;
        PointCollection pointCollection=new PointCollection(mMapView.getSpatialReference());
        //Point prepoint = polyline.getPoint(0);
        Point prepoint= mPtCollection.get(0);
        px = candidatePoint.getX();
        py = candidatePoint.getY();
        pre_px = prepoint.getX();
        pre_py = prepoint.getY();
        double tailFactor = 0.05, headPercentage = 0.07;
        double slope = 0, plen = 0;
        Graphic resultgraphic = null;
        ArrayList<Double> angleArray = null;

        //int pointcount =part.getPointCount() polyline.getPointCount();
        int pointcount=mPtCollection.size();
        if (pointcount <= 2) {
            plen = Calculate.TwoPoint_Distance(prepoint, candidatePoint);
            slope = (py - pre_py) / (px - pre_px);
            switch (Calculate.twoPtsRelationShip(prepoint,
                    candidatePoint)) {
                //判断两点位置关系
                case "ne":
                    slope += Math.PI / 2;
                    break;
                case "nw":
                    slope += Math.PI * 3 / 2;
                    break;
                case "sw":
                    slope += Math.PI * 3 / 2;
                    break;
                case "se":
                    slope += Math.PI / 2;
                    break;
            }
            // tail two points
            Point pt1 = new Point(tailFactor * plen * Math.cos(slope)
                    + prepoint.getX(), tailFactor * plen
                    * Math.sin(slope) + prepoint.getY());
            Point pt2 = new Point((-1) * tailFactor * plen * Math.cos(slope)
                    + prepoint.getX(), (-1) * tailFactor * plen
                    * Math.sin(slope) + prepoint.getY());
            double partiallen = (1 - headPercentage) * plen;

            Point p1 = new Point(tailFactor * partiallen * Math.cos(slope)
                    + prepoint.getX(), tailFactor * partiallen
                    * Math.sin(slope) + prepoint.getY());

            Point p2 = new Point((-1) * tailFactor * partiallen
                    * Math.cos(slope) + prepoint.getX(), (-1)
                    * tailFactor * partiallen * Math.sin(slope)
                    + prepoint.getY());
            pointCollection.add(pt1);
            pointCollection.add(p1);

            PointCollection pc_arrowhead = createArrowHeadPathEx(p1, candidatePoint,
                    p2, plen, headPercentage, 15);
            pointCollection.addAll(pc_arrowhead);
            pointCollection.add(p2);
            pointCollection.add(pt2);
            pointCollection.add(pt1);

            resultgraphic = new Graphic(new PolygonBuilder(pointCollection).toGeometry(), plotArrowfill);


        } else {
            ArrayList<Point> tempArray = new ArrayList<Point>();
            ArrayList<Point> leftArray = new ArrayList<Point>();
            ArrayList<Point> rightArray = new ArrayList<Point>();
            for (int i = 0; i < pointcount; i++) {
                tempArray.add(mPtCollection.get(i));
            }
            angleArray = Calculate.vertexAngle(tempArray);
            double totalL = Calculate.ptCollectionLen(tempArray, 0);
            for (int i = 0, len = tempArray.size() - 1; i < len; i++) {
                double partialLen = Calculate.ptCollectionLen(tempArray, i);
                partialLen += totalL / 2.4;
                //console.log(partialLen);
                Point pt1 = new Point(
                        tailFactor * partialLen * Math.cos(angleArray.get(i))
                                + tempArray.get(i).getX(), tailFactor * partialLen
                        * Math.sin(angleArray.get(i)) + tempArray.get(i).getY());
                Point pt2 = new Point(
                        (-1) * tailFactor * partialLen
                                * Math.cos(angleArray.get(i))
                                + tempArray.get(i).getX(), (-1) * tailFactor
                        * partialLen * Math.sin(angleArray.get(i))
                        + tempArray.get(i).getY());

                leftArray.add(pt1);
                rightArray.add(pt2);
            }

            leftArray.add(candidatePoint);
            rightArray.add(candidatePoint);

            //计算贝塞尔曲线
            leftArray = Calculate.CreateBezierPathPCOnly(leftArray, 70);
            rightArray = Calculate.CreateBezierPathPCOnly(rightArray, 70);
            PointCollection  pc_arrowhead = createArrowHeadPathEx(leftArray.get(leftArray.size() - 1), candidatePoint,
                    rightArray.get(rightArray.size() - 1), Calculate.ptCollectionLen(tempArray, 0), headPercentage, 15);

            ArrayList<Point> resultpoints = new ArrayList<Point>();
            resultpoints.addAll(leftArray);
            Collections.reverse(rightArray);
            resultpoints.addAll(rightArray);
            for (int i = 0; i < resultpoints.size(); i++) {
                pointCollection.add(resultpoints.get(i));
            }
            Geometry uniongeo = GeometryEngine.union(new PolygonBuilder(pointCollection).toGeometry(),
                    new PolygonBuilder(pc_arrowhead).toGeometry());
            resultgraphic = new Graphic(uniongeo, plotArrowfill);
        }

        return resultgraphic;

    }

    /**
     * 绘出箭头头部
     */
    public static PointCollection createArrowHeadPathEx(Point pt1, Point candidatePt,
                                                        Point pt2, double totalLen, double headPercentage, int headAngle) {

        double headSizeBaseRatio = 1.7;// 箭头大小比例
        double addangle = (headAngle / 180.000) * Math.PI;
        // headAngle = 45;
        double headBaseLen = totalLen * headPercentage;
        double headSideLen = headBaseLen * headSizeBaseRatio;
        double angle1 = Calculate.twoPtsAngle(candidatePt, pt1);
        double angle2 = Calculate.twoPtsAngle(candidatePt, pt2);
        double midAngle = (Math.abs(angle1 - angle2)) / 2.0;
        if (Math.abs(angle1 - angle2) > Math.PI * 1.88) {
            midAngle += Math.PI;
        }

        double len = Math.sqrt(headBaseLen * headBaseLen + headSideLen
                * headSideLen - 2 * headSideLen * headBaseLen
                * Math.cos(midAngle + addangle));

        double upAngle = Math.asin(headBaseLen
                * Math.sin(midAngle + addangle)
                / len);
        double centAngle = upAngle + addangle;
        double result = headBaseLen * Math.sin(Math.PI - centAngle - midAngle)
                / Math.sin(centAngle);

        //MultiPath path = new Polygon();


        PointCollection pointCollection=new PointCollection(mMapView.getSpatialReference());
        Point p1 = new Point(candidatePt.getX() + result * Math.cos(angle1),
                candidatePt.getY() + result * Math.sin(angle1));
        //path.startPath(p1);
        pointCollection.add(p1);
        // DecimalFormat df=new DecimalFormat();

        Point p2 = new Point(candidatePt.getX() + headSideLen
                * Math.cos(angle1 - addangle),
                candidatePt.getY() + headSideLen
                        * Math.sin(angle1 - addangle));

        // path.lineTo(new Point());
        //path.lineTo(p2);
        pointCollection.add(p2);
        pointCollection.add(candidatePt);
        //path.lineTo(candidatePt);
        Point p3 = new Point(candidatePt.getX() + headSideLen
                * Math.cos(angle2 + addangle),
                candidatePt.getY() + headSideLen
                        * Math.sin(angle2 + addangle));
        //path.lineTo(p3);
        pointCollection.add(p3);
        Point p4 = new Point(candidatePt.getX() + result * Math.cos(angle2),
                candidatePt.getY() + result * Math.sin(angle2));
        //path.lineTo(p4);
        pointCollection.add(p4);
        return pointCollection;
    }

    /**
     * 绘制贝塞尔Polygon
     */
    public static ArrayList<Point> createBezierPathPolygon(
            ArrayList<Point> pointCollection) {
        ArrayList<Point> pts = new ArrayList<Point>();
        int pointcount = pointCollection.size();
        if (pointCollection.get(pointcount - 1).getX() == pointCollection.get(
                pointcount - 2).getX()
                && pointCollection.get(pointcount - 1).getY() == pointCollection
                .get(pointcount - 2).getY()) {
            pointCollection
                    .remove(pointCollection.get(pointCollection.size() - 1));
        }
        pointcount = pointCollection.size();
        if (pointCollection.get(pointcount - 1).getX() == pointCollection.get(
                pointcount - 2).getX()
                && pointCollection.get(pointcount - 1).getY() == pointCollection
                .get(pointcount - 2).getY()) {
            pointCollection
                    .remove(pointCollection.get(pointCollection.size() - 1));
        }

        int n = pointCollection.size() - 1; //
        // for (int i = 0; i <= numberOfPts; i++) {
        for (float u = 0; u <= 1; u += 0.01) {

            Point[] p = new Point[n + 1];
            for (int i = 0; i <= n; i++) {
                p[i] = new Point(pointCollection.get(i).getX(), pointCollection
                        .get(i).getY());
            }

            for (int r = 1; r <= n; r++) {
                for (int i = 0; i <= n - r; i++) {
                    p[i]=new Point((1 - u) * p[i].getX() + u * p[i + 1].getX(),(1 - u) * p[i].getY() + u * p[i + 1].getY());
					/*p[i].setX((1 - u) * p[i].getX() + u * p[i + 1].getX());
					p[i].setY((1 - u) * p[i].getY() + u * p[i + 1].getY());*/
                }
            }
            pts.add(p[0]);
        }

        return pts;

    }




}
