/*
package com.titan.gis.plot

import android.content.Context
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat.getDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.*
import com.esri.arcgisruntime.util.ListChangedListener
import com.titan.gis.Calculate
import com.titan.newslfh.R
import com.titan.newslfh.databinding.DialogPlotBinding
import java.util.*

*/
/**
 * Created by whs on 2017/7/17
 * 态势标绘
 *//*


class PlotDialog : DialogFragment(),IPlot {
    override fun text() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //基本线样式
    private var plotlineSymbol: LineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 3f)
    //箭头样式
    private var : FillSymbol = SimpleFillSymbol(SimpleFillSymbol.Style.SOLID, Color.argb(80, 255, 0, 0), plotlineSymbol)
    // 防火带样式
    private var plotbreaklineSymbol: LineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 20f)
    // 火场范围
    internal var plotFireArea: FillSymbol = SimpleFillSymbol(SimpleFillSymbol.Style.FORWARD_DIAGONAL, Color.RED, plotlineSymbol)

    // 防火带样式
    //private var plotbreaklineSymbol: LineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 20f)
    // 火场范围
    //internal var plotFireArea: FillSymbol = SimpleFillSymbol(SimpleFillSymbol.Style.FORWARD_DIAGONAL, Color.RED, plotlineSymbol)

    private var mDataBinding:DialogPlotBinding? = null

    private var mViewModel:PlotViewModel? = null
    //功能是否启动
    private var active = false
    //标绘类型
    private var mPlotType:PlotType?=null
    //点集合
    private var mPtCollection:PointCollection? = null
    //构造器
    private var mPolylineBuilder:PolylineBuilder? = null
    private var mPolygonBuilder:PolygonBuilder? = null
    //火点样式
    private var firepointSymbol:PictureMarkerSymbol?=null
    //旗帜样式
    private var flagSymbol:PictureMarkerSymbol?=null


    var plotgarphic: Graphic?=null


    //标绘类型
    enum class PlotType {
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



    companion object {
         var singleton: PlotDialog?=null
         var mMapView:MapView?=null
         var mPlotOverlay:GraphicsOverlay? = null
         //绘制图形ID
         var plotgraphicID = 0
         var plotTouchListener:PlotTouchListener?=null

         fun getInstance(mapView: MapView): PlotDialog {
            if (singleton == null) {
                singleton = PlotDialog()
                mMapView = mapView
                mPlotOverlay!!.graphics?.addListChangedListener(ListChangedListener<Graphic> { listChangedEvent ->
                    plotgraphicID = listChangedEvent.index
                    Log.e("TItan", "plotgraphicID" + plotgraphicID)
                })

                if(mPlotOverlay==null){
                    mPlotOverlay = addGraphicsOverlay()
                }

            }
            return singleton as PlotDialog

        }

        */
/**
         * 添加绘制层
         * @return
         *//*

        private fun addGraphicsOverlay(): GraphicsOverlay {
            //create the graphics overlay
            val graphicsOverlay = GraphicsOverlay()
            //add the overlay to the map view
            mMapView!!.graphicsOverlays!!.add(graphicsOverlay)
            return graphicsOverlay
        }
    }




    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_plot, container, false)
        mDataBinding!!.viewmodel=mViewModel
        Companion.plotTouchListener = PlotTouchListener(context, mMapView as MapView)
        firepointSymbol =  PictureMarkerSymbol(getDrawable(context, R.drawable.plot_firepoint) as BitmapDrawable)
        flagSymbol = PictureMarkerSymbol(getDrawable(context, R.drawable.plot_flag_symbol) as BitmapDrawable)
        return mDataBinding!!.root
    }

    fun  setViewmodel(mViewModel: PlotViewModel)=mViewModel



    inner class PlotTouchListener(context:Context,mapView: MapView):DefaultMapViewOnTouchListener(context, mapView){
       var mapview:MapView?=null
       var context:Context?=null

       init{
           this.mapview = mapView
           this.context = context
       }

        override fun onTouch(v: View?, event: MotionEvent): Boolean {
            //Point point = mapview.toMapPoint(event.getX(), event.getY());
            val point = mapview?.screenToLocation(android.graphics.Point(Math.round(event.x), Math.round(event.y)))

            if (point == null || point.isEmpty) {
                return false
            }
            if (active && event.action == MotionEvent.ACTION_DOWN) {
                when (mPlotType) {
                */
/*case FIREAREA:
						polygon.startPath(point);
						break;
					case FIREBREAK:
						polyline.startPath(point);
						break;*//*

                        PlotType.FIREAREA ->
                        //火场范围
                        mPtCollection?.add(point)

                        PlotType.FIREBREAK ->
                        //防火带
                        mPtCollection?.add(point)

                }
            }
            return super.onTouch(v, event)

        }

        override fun onFling(from: MotionEvent?, to: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            val point = mapview?.screenToLocation(android.graphics.Point(Math.round(to!!.x), Math.round(to.y)))
            if (point == null || point.isEmpty) {
                return false
            }
            if (active) {
                when (mPlotType) {
                    PlotType.FIREAREA -> {
                        //火场范围
                        mPtCollection!!.add(point)
                        //mPolygonBuilder.addPoint(point);
                        mPolygonBuilder = PolygonBuilder(mPtCollection)
                        mPlotOverlay?.graphics?.get(plotgraphicID)?.geometry = mPolygonBuilder?.toGeometry()
                    }
                    PlotType.FIREBREAK -> {
                        //防火带
                        mPtCollection!!.add(point)
                        mPolylineBuilder = PolylineBuilder(mPtCollection)
                        mPlotOverlay?.graphics?.get(plotgraphicID)?.geometry = mPolylineBuilder?.toGeometry()
                    }
                }
                return true
            }
            return super.onFling(from, to, velocityX, velocityY)
        }


        override fun onSingleTapConfirmed(event: MotionEvent?): Boolean {
            val point = mapview?.screenToLocation(android.graphics.Point(Math.round(event!!.x), Math.round(event.y)))
            if (active) {
                when (mPlotType) {
                    PlotType.FIREPOINT -> {
                        //火点
                        plotgarphic = Graphic(point, firepointSymbol)// hotsymbol);
                        mPlotOverlay!!.graphics?.add(plotgarphic)
                    }
                    PlotType.FLAG -> {
                        //旗帜
                        plotgarphic = Graphic(point, flagSymbol)// hotsymbol);
                        mPlotOverlay!!.graphics?.add(plotgarphic)
                    }
                    PlotType.ARROW -> {
                        //箭头
                        mPtCollection!!.add(point)
                        */
/*mPolylineBuilder.addPoint(point);
                            polyline=mPolylineBuilder.toGeometry();*//*

                        // 贝塞尔箭头
                        if (mPtCollection!!.size >= 2) {
                            plotgarphic = plotArrow(point!!, mPtCollection!!)
                            mPlotOverlay!!.graphics!!.get(plotgraphicID)?.geometry = plotgarphic?.geometry
                        }
                    }
                    PlotType.JUNGLE -> {
                        //集结地
                    }
                }
                return true
            }
            return super.onSingleTapConfirmed(event)

        }


        */
/**
         * 双击
         * @param point
         * @return
         *//*

        override fun onDoubleTap(point: MotionEvent): Boolean {
            deactivate()//结束标绘
            return super.onDoubleTap(point)
        }



    }

    private fun deactivate() {
         active=false
    }

    */
/**
     * 绘制贝塞尔箭头
     *//*

    fun plotArrow(candidatePoint: Point, mPtCollection: PointCollection): Graphic {
        val px: Double = candidatePoint.x
        val py: Double = candidatePoint.y
        val pre_px: Double
        val pre_py: Double
        val pointCollection = PointCollection(mMapView!!.spatialReference)
        //Point prepoint = polyline.getPoint(0);
        val prepoint = mPtCollection[0]
        pre_px = prepoint.x
        pre_py = prepoint.y
        val tailFactor = 0.05
        val headPercentage = 0.07
        var slope = 0.0
        var plen = 0.0
        var resultgraphic: Graphic? = null
        var angleArray: ArrayList<Double>? = null

        //int pointcount =part.getPointCount() polyline.getPointCount();
        val pointcount = mPtCollection.size
        if (pointcount <= 2) {
            plen = Calculate.TwoPoint_Distance(prepoint, candidatePoint)
            slope = (py - pre_py) / (px - pre_px)
            when (Calculate.twoPtsRelationShip(prepoint,
                    candidatePoint)) {
            //判断两点位置关系
                "ne" -> slope += Math.PI / 2
                "nw" -> slope += Math.PI * 3 / 2
                "sw" -> slope += Math.PI * 3 / 2
                "se" -> slope += Math.PI / 2
            }
            // tail two points
            val pt1 = Point(tailFactor * plen * Math.cos(slope) + prepoint.x, tailFactor * plen * Math.sin(slope) + prepoint.y)
            val pt2 = Point((-1).toDouble() * tailFactor * plen * Math.cos(slope) + prepoint.x, (-1).toDouble() * tailFactor * plen * Math.sin(slope) + prepoint.y)
            val partiallen = (1 - headPercentage) * plen

            val p1 = Point(tailFactor * partiallen * Math.cos(slope) + prepoint.x, tailFactor * partiallen * Math.sin(slope) + prepoint.y)

            val p2 = Point((-1).toDouble() * tailFactor * partiallen * Math.cos(slope) + prepoint.x, (-1).toDouble() * tailFactor * partiallen * Math.sin(slope) + prepoint.y)
            pointCollection.add(pt1)
            pointCollection.add(p1)

            val pc_arrowhead = createArrowHeadPathEx(p1, candidatePoint,
                    p2, plen, headPercentage, 15)
            pointCollection.addAll(pc_arrowhead)
            pointCollection.add(p2)
            pointCollection.add(pt2)
            pointCollection.add(pt1)

            resultgraphic = Graphic(PolygonBuilder(pointCollection).toGeometry(), )


        } else {
            val tempArray = ArrayList<Point>()
            var leftArray = ArrayList<Point>()
            var rightArray = ArrayList<Point>()
            for (i in 0..pointcount - 1) {
                tempArray.add(mPtCollection[i])
            }
            angleArray = Calculate.vertexAngle(tempArray)
            val totalL = Calculate.ptCollectionLen(tempArray, 0)
            run {
                var i = 0
                val len = tempArray.size - 1
                while (i < len) {
                    var partialLen = Calculate.ptCollectionLen(tempArray, i)
                    partialLen += totalL / 2.4
                    //console.log(partialLen);
                    val pt1 = Point(
                            tailFactor * partialLen * Math.cos(angleArray!![i]) + tempArray[i].x, tailFactor * partialLen * Math.sin(angleArray!![i]) + tempArray[i].y)
                    val pt2 = Point(
                            (-1).toDouble() * tailFactor * partialLen * Math.cos(angleArray!![i]) + tempArray[i].x, (-1).toDouble() * tailFactor * partialLen * Math.sin(angleArray!![i]) + tempArray[i].y)

                    leftArray.add(pt1)
                    rightArray.add(pt2)
                    i++
                }
            }

            leftArray.add(candidatePoint)
            rightArray.add(candidatePoint)

            //计算贝塞尔曲线
            leftArray = Calculate.CreateBezierPathPCOnly(leftArray, 70)
            rightArray = Calculate.CreateBezierPathPCOnly(rightArray, 70)
            val pc_arrowhead = createArrowHeadPathEx(leftArray[leftArray.size - 1], candidatePoint,
                    rightArray[rightArray.size - 1], Calculate.ptCollectionLen(tempArray, 0), headPercentage, 15)

            val resultpoints = ArrayList<Point>()
            resultpoints.addAll(leftArray)
            Collections.reverse(rightArray)
            resultpoints.addAll(rightArray)
            for (i in resultpoints.indices) {
                pointCollection.add(resultpoints[i])
            }
            val uniongeo = GeometryEngine.union(PolygonBuilder(pointCollection).toGeometry(),
                    PolygonBuilder(pc_arrowhead).toGeometry())
            resultgraphic = Graphic(uniongeo, )
        }

        return resultgraphic

    }

    */
/**
     * 绘出箭头头部
     *//*

    fun createArrowHeadPathEx(pt1: Point, candidatePt: Point,
                              pt2: Point, totalLen: Double, headPercentage: Double, headAngle: Int): PointCollection {

        val headSizeBaseRatio = 1.7// 箭头大小比例
        val addangle = headAngle / 180.000 * Math.PI
        // headAngle = 45;
        val headBaseLen = totalLen * headPercentage
        val headSideLen = headBaseLen * headSizeBaseRatio
        val angle1 = Calculate.twoPtsAngle(candidatePt, pt1)
        val angle2 = Calculate.twoPtsAngle(candidatePt, pt2)
        var midAngle = Math.abs(angle1 - angle2) / 2.0
        if (Math.abs(angle1 - angle2) > Math.PI * 1.88) {
            midAngle += Math.PI
        }

        val len = Math.sqrt(headBaseLen * headBaseLen + headSideLen * headSideLen - 2.0 * headSideLen * headBaseLen * Math.cos(midAngle + addangle))

        val upAngle = Math.asin(headBaseLen * Math.sin(midAngle + addangle) / len)
        val centAngle = upAngle + addangle
        val result = headBaseLen * Math.sin(Math.PI - centAngle - midAngle) / Math.sin(centAngle)

        //MultiPath path = new Polygon();


        val pointCollection = PointCollection(mMapView?.spatialReference)
        val p1 = Point(candidatePt.x + result * Math.cos(angle1),
                candidatePt.y + result * Math.sin(angle1))
        //path.startPath(p1);
        pointCollection.add(p1)
        // DecimalFormat df=new DecimalFormat();

        val p2 = Point(candidatePt.x + headSideLen * Math.cos(angle1 - addangle),
                candidatePt.y + headSideLen * Math.sin(angle1 - addangle))

        // path.lineTo(new Point());
        //path.lineTo(p2);
        pointCollection.add(p2)
        pointCollection.add(candidatePt)
        //path.lineTo(candidatePt);
        val p3 = Point(candidatePt.x + headSideLen * Math.cos(angle2 + addangle),
                candidatePt.y + headSideLen * Math.sin(angle2 + addangle))
        //path.lineTo(p3);
        pointCollection.add(p3)
        val p4 = Point(candidatePt.x + result * Math.cos(angle2),
                candidatePt.y + result * Math.sin(angle2))
        //path.lineTo(p4);
        pointCollection.add(p4)
        return pointCollection
    }



}


*/
