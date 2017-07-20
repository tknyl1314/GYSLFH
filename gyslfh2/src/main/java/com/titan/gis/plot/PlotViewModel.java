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
    public final ObservableField<PlotDialog.PlotType> mPlotType=new ObservableField<>(PlotDialog.PlotType.FIREPOINT);

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
       /* switch (plottype){
            case 1:
                mView.Plot()
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
        }*/

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


    }

    /**
     * 分享
     */
    public void onShare(){
        mView.onShare();

    }
}
