package com.titan.gis.plot;

import android.databinding.ObservableField;

import com.titan.BaseViewModel;
import com.titan.data.source.DataRepository;

/**
 * Created by whs on 2017/7/18
 * 态势标绘
 */

public class PlotViewModel extends BaseViewModel {
    private IPlot mView;
    //标绘类型
    public final ObservableField<PlotUtil.PlotType> mPlotType=new ObservableField<>(PlotUtil.PlotType.FIREPOINT);


    public PlotViewModel(IPlot iplot, DataRepository datarepository) {
        mView=iplot;
        mDataRepository=datarepository;
    }

    /**
     * 标绘
     * @param plottype
     */
    public void onPlot(int plottype){
        mView.Plot(plottype);

    }

    public void showLayers(){
        mView.showLayers();
    }



    /**
     * 撤销
     */
    public void onRevok(){
        mView.onRevok();

    }

    /**
     * 保存
     */
    public void onSave(){
        mView.onSave();


    }

    /**
     * 分享
     */
    public void onShare(){
        mView.onShare();

    }


    /**
     * 保存绘制的图形
     * @param plottype
     */
    /*private  void saveSketchGraphic(PlotUtil.PlotType plottype) {
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
                    graphic=new Graphic(sketchEditor.getGeometry(),PlotUtil.plotFireArea);

                }
                //mPlotOverlay.getGraphics().add(graphic);
                break;
            case FIREBREAK:
                graphic=new Graphic(sketchEditor.getGeometry(),PlotUtil.plotbreaklineSymbol);
                break;
            case FIREPOINT:
                graphic=new Graphic(sketchEditor.getGeometry(),PlotUtil.firepointSymbol);

                break;
            case FLAG:
                graphic=new Graphic(sketchEditor.getGeometry(),PlotUtil.flagSymbol);

                break;
        }
        mPlotOverlay.getGraphics().add(graphic);
    }*/
}
