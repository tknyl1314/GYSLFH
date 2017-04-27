package com.titan.maps;

import java.util.Random;

public class TDTUrl  {
    private TianDiTuTiledMapServiceType _tiandituMapServiceType;
    private int _level;
    private int _col;
    private int _row;
    public TDTUrl(int level, int col, int row,TianDiTuTiledMapServiceType tiandituMapServiceType){
        this._level=level;
        this._col=col;
        this._row=row;
        this._tiandituMapServiceType=tiandituMapServiceType;
    }



    public String generatUrl(){
        /**
         * img
         */
        StringBuilder url=new StringBuilder("http://t");
        Random random=new Random();
        int subdomain = (random.nextInt(6) + 1);
        url.append(subdomain);
        switch(this._tiandituMapServiceType){
        case VEC_C:
             /*<span style="white-space:pre">	</span> */url.append(".tianditu.com/DataServer?T=vec_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
            break;
        case CVA_C:
        	 url.append(".tianditu.com/DataServer?T=cva_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
            break;
        case CIA_C:
        	 url.append(".tianditu.com/DataServer?T=cia_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
            break;
        case IMG_C:
        	 url.append(".tianditu.com/DataServer?T=img_c&X=").append(this._col).append("&Y=").append(this._row).append("&L=").append(this._level);
            break;
            default:
                return null;
        }
        return url.toString();
    }
   
}
