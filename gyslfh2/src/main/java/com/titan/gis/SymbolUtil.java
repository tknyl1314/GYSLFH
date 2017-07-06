package com.titan.gis;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;

import com.esri.arcgisruntime.symbology.MarkerSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.titan.newslfh.R;

/**
 * Created by whs on 2017/6/30
 */

public class SymbolUtil {
    private Context mContext;
    /**火险等级样式*/
    public static SimpleFillSymbol risk1_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.GREEN, null);
    public static SimpleFillSymbol risk2_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.BLUE, null);
    public static SimpleFillSymbol risk3_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.YELLOW, null);
    public static SimpleFillSymbol risk4_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.rgb(255, 165, 58), null);
    public static SimpleFillSymbol risk5_Symbol = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.RED, null);

    //轨迹线样式
    public static SimpleLineSymbol lineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.parseColor("#1266E6"), 4);


    /**态势标绘样式*/
    //火点样式
    public static MarkerSymbol firepoint;

    public SymbolUtil(Context mContext) {
        this.mContext = mContext;
        firepoint = new PictureMarkerSymbol((BitmapDrawable) ContextCompat.getDrawable(mContext, R.drawable.plot_firepoint));

    }
}
