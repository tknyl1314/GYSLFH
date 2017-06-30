package com.titan.gis;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polygon;
import com.esri.arcgisruntime.geometry.PolygonBuilder;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.MapView;
import com.esri.arcgisruntime.symbology.FillSymbol;
import com.esri.arcgisruntime.symbology.LineSymbol;
import com.esri.arcgisruntime.symbology.MarkerSymbol;
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol;
import com.esri.arcgisruntime.symbology.SimpleFillSymbol;
import com.esri.arcgisruntime.symbology.SimpleLineSymbol;
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol;
import com.esri.arcgisruntime.util.ListChangedEvent;
import com.esri.arcgisruntime.util.ListChangedListener;
import com.titan.newslfh.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 
 *  地图标绘模块
 *  Arcgis version:runtime 100
 * 
 */
public class PlotUtil implements OnClickListener {
    //基本线样式
    private static LineSymbol plotlineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID ,Color.BLACK, 3);
    //箭头样式
	private static FillSymbol plotArrowfill = new SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,Color.argb(80, 255, 0, 0),plotlineSymbol);
	static MarkerSymbol plotmarker = new SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE,Color.BLACK, 16);

	// 防火带样式
	private static LineSymbol plotbreaklineSymbol = new SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLUE, 20);
	// 火场范围
	static FillSymbol plotFireArea = new SimpleFillSymbol(SimpleFillSymbol.Style.FORWARD_DIAGONAL,Color.RED,plotlineSymbol);
	// 火点样式
	public MarkerSymbol firepointSymbol, flagSymbol;
	Context mContext;
	public PopupWindow pop_plot;
    //标绘图层
    private GraphicsOverlay mGraphicsOverlay;
    //plotGraphicId 初始为0
	public  int plotgraphicID=0, plotType;
	private MapView mapview;
	public static boolean active = false;
	private Point point;
	private Polyline polyline;
	public Graphic plotgarphic;
	private Polygon polygon;
	private Point startPoint;
	// 地图事件监听
	public PlotTouchListener plotTouchListener;

    private PointCollection mPtCollection;
    //构造器
    private PolylineBuilder mPolylineBuilder;
    private PolygonBuilder mPolygonBuilder;
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

    private PlotType mPlotType;
	/**
	 * 标绘方式及geometry类型
	 */
	// 箭头
	public static final int ARROW = 10;
	// 集结地
	public static final int JUNGLE = 12;
	// 火场范围
	public static final int FIREAREA = 13;
	// 防火带
	public static final int FIREBREAK = 2;
	// 火点
	public static final int FIREPOINT = 3;
	// 旗帜
	public static final int FLAG = 4;


	public PlotUtil(Context context, MapView mapview) {
		this.mContext = context;
        this.mapview = mapview;
		this.mGraphicsOverlay = addGraphicsOverlay();
        mGraphicsOverlay.getGraphics().addListChangedListener(new ListChangedListener<Graphic>() {
            @Override
            public void listChanged(ListChangedEvent<Graphic> listChangedEvent) {
                plotgraphicID=listChangedEvent.getIndex();
                Log.e("TItan","plotgraphicID"+plotgraphicID);
            }
        });

		plotTouchListener = new PlotTouchListener(context, mapview);
		firepointSymbol = new PictureMarkerSymbol((BitmapDrawable)ContextCompat.getDrawable(mContext,R.drawable.plot_firepoint));
		flagSymbol = new PictureMarkerSymbol((BitmapDrawable)ContextCompat.getDrawable(mContext,R.drawable.plot_flag_symbol));
	}

	/**
	 * 展示标绘对话框
	 */
	public void showPlotDialog(View parentview) {
		//MapActivity.actionMode = MapActivity.actionMode.MODE_PLOT;
		LayoutInflater layInflater = LayoutInflater.from(mContext);
		View view = layInflater.inflate(R.layout.dialog_plot, null);
		view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
		// 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
		pop_plot = new PopupWindow(view, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);
		pop_plot.setBackgroundDrawable(mContext.getResources().getDrawable(
				android.R.color.white));
		// 设置动画效果
		// pop_plot.setAnimationStyle(R.style);
		// 这里是位置显示方式,在屏幕的左侧
		ImageButton plotarrow = (ImageButton) view.findViewById(R.id.btn_plot_ARROW);
		plotarrow.setOnClickListener(this);

		ImageButton plotpolygon = (ImageButton) view
				.findViewById(R.id.btn_plot_JUNGLE);
		plotpolygon.setOnClickListener(this);

		ImageButton plotpoint = (ImageButton) view.findViewById(R.id.btn_plot_FIREPOINT);
		plotpoint.setOnClickListener(this);

		ImageButton firebreak = (ImageButton) view.findViewById(R.id.btn_plot_FIREBREAK);
		firebreak.setOnClickListener(this);

		ImageButton firearea = (ImageButton) view.findViewById(R.id.btn_plot_FIREAREA);
		firearea.setOnClickListener(this);

		ImageButton flag = (ImageButton) view.findViewById(R.id.btn_plot_FLAG);
		flag.setOnClickListener(this);
		pop_plot.setFocusable(false);
		pop_plot.setOutsideTouchable(false);
		// pop_plot.setAnimationStyle(R.style.AnimationPreview);//设置动画样式
		pop_plot.showAsDropDown(parentview, -view.getMeasuredWidth(),
				-parentview.getHeight());
	}

    /**
     * 添加绘制层
     * @return
     */
    private GraphicsOverlay addGraphicsOverlay() {
        //create the graphics overlay
        GraphicsOverlay graphicsOverlay = new GraphicsOverlay();
        //add the overlay to the map view
        boolean isadd=mapview.getGraphicsOverlays().add(graphicsOverlay);
        return graphicsOverlay;
    }

	/**
	 * 关闭标绘对话框
	 */
	public void closePlotDialog() {
        mGraphicsOverlay.getGraphics().clear();
		if (pop_plot != null && pop_plot.isShowing()) {
			pop_plot.dismiss();
			pop_plot = null;
		}
	}

	public class PlotTouchListener extends DefaultMapViewOnTouchListener {
		MapView mapview;
		Context context;

		public PlotTouchListener(Context context, MapView mapview) {
			super(context, mapview);
			this.mapview = mapview;
			this.context = context;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			//Point point = mapview.toMapPoint(event.getX(), event.getY());
          Point point=mapview.screenToLocation(new android.graphics.Point(Math.round(event.getX()), Math.round(event.getY())));
            
			if (point == null || point.isEmpty()) {
				return false;
			}
			if (active && event.getAction() == MotionEvent.ACTION_DOWN) {
				switch (mPlotType) {
					/*case FIREAREA:
						polygon.startPath(point);
						break;
					case FIREBREAK:
						polyline.startPath(point);
						break;*/
                    case FIREAREA:
                        mPtCollection.add(point);

                        //mPolygonBuilder.addPoint(point);
                        break;
                    case FIREBREAK:
                        mPtCollection.add(point);


                       // mPolylineBuilder.addPoint(point);
                        break;

					default:
						break;
				}
			}
			return super.onTouch(v, event);

		}

        @Override
        public boolean onFling(MotionEvent from, MotionEvent to, float velocityX, float velocityY) {
            Point point=mapview.screenToLocation(new android.graphics.Point(Math.round(to.getX()), Math.round(to.getY())));
            if (point == null || point.isEmpty()) {
                return false;
            }
            if (active) {
                switch (mPlotType) {
                    case FIREAREA:
                        mPtCollection.add(point);

                        //mPolygonBuilder.addPoint(point);
                        mPolygonBuilder =new PolygonBuilder(mPtCollection);
                        mGraphicsOverlay.getGraphics().get(plotgraphicID).setGeometry(mPolygonBuilder.toGeometry());
                        break;
                    case FIREBREAK:
                        mPtCollection.add(point);
                        mPolylineBuilder =new PolylineBuilder(mPtCollection);
                        mGraphicsOverlay.getGraphics().get(plotgraphicID).setGeometry(mPolylineBuilder.toGeometry());
                        break;
                }
                return false;
            }
            return super.onFling(from, to, velocityX, velocityY);
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent event) {
            Point point=mapview.screenToLocation(new android.graphics.Point(Math.round(event.getX()), Math.round(event.getY())));
            if (active) {
                switch (mPlotType) {
                    case FIREPOINT:
                        plotgarphic = new Graphic(point, firepointSymbol);// hotsymbol);
                        mGraphicsOverlay.getGraphics().add(plotgarphic);
                        break;
                    case FLAG:
                        plotgarphic = new Graphic(point, flagSymbol);// hotsymbol);
                        mGraphicsOverlay.getGraphics().add(plotgarphic);
                        break;
                    case ARROW:
                        //箭头
                        if (startPoint == null) {
                            startPoint = point;
                            mPtCollection.add(point);
                            //mPolylineBuilder=new PolylineBuilder(mPtCollection);
                            //polyline=new Polyline(part);
                            //polyline.startPath(point);
                        } else {
                            mPtCollection.add(point);
                            /*mPolylineBuilder.addPoint(point);
                            polyline=mPolylineBuilder.toGeometry();*/
                            // 贝塞尔箭头
                            plotgarphic = plotArrow(point, mPtCollection);
                            //mGraphicsOverlay.getGraphics().add(plotgarphic);
                            mGraphicsOverlay.getGraphics().get(plotgraphicID).setGeometry(plotgarphic.getGeometry());
                            /*if(mGraphicsOverlay.getGraphics().get(plotgraphicID)==null){
                                mGraphicsOverlay.getGraphics().add(plotgraphicID,plotgarphic);

                            }else {
                                mGraphicsOverlay.getGraphics().get(plotgraphicID).setGeometry(plotgarphic.getGeometry());
                            }*/
                        }
                        break;

                    case JUNGLE:
                        //集结地
                        /*if (startPoint == null) {
                            startPoint = point;
                            mPtCollection.add(point);
                            mPolylineBuilder=new PolylineBuilder(mPtCollection);
                            polygon=mPolygonBuilder.toGeometry();

                            //polygon.startPath(point);
                        } else {
                            //polygon.lineTo(point);
                            mPolygonBuilder.addPoint(point);
                            polygon=mPolygonBuilder.toGeometry();
                            //plotgarphic = PlotUtil.plot_BEZIER_POLYGON(point, plotgraphicID);

                            mGraphicsOverlay.getGraphics().get(plotgraphicID).setGeometry(polygon);

                            //plotgraphiclayer.updateGraphic(plotgraphicID, plotgarphic);
                        }*/

                        break;
                }

            }
            return super.onSingleTapConfirmed(event);

        }


		@Override
		public boolean onDoubleTap(MotionEvent point) {
			deactivate();//结束标绘
			return super.onDoubleTap(point);
		}


		/*@Override
		public void onLongPress(MotionEvent event) {
			Point point = mapview.toMapPoint(event.getX(), event.getY());
			//super.onLongPress(point);
		}
*/

	}

	@Override
	public void onClick(View v) {
        //plotgraphicID++;
		//MapActivity.actionMode = MapActivity.ActionMode.MODE_PLOT;
		switch (v.getId()) {
			case R.id.btn_plot_ARROW:
				//ToastUtil.setToast(mContext, "你选择了箭头标绘");
				Toast.makeText(mContext, "箭头标绘", Toast.LENGTH_SHORT).show();
				activate(PlotType.ARROW);
				break;
			case R.id.btn_plot_FIREAREA:
				Toast.makeText(mContext, "火场范围标绘", Toast.LENGTH_SHORT).show();
				activate(PlotType.FIREAREA);
				break;
			case R.id.btn_plot_FIREPOINT:
				Toast.makeText(mContext, "火点标绘", Toast.LENGTH_SHORT).show();
				//plotType = FIREPOINT;
				activate(PlotType.FIREPOINT);
				break;
			case R.id.btn_plot_JUNGLE:
				Toast.makeText(mContext, "集结地标绘", Toast.LENGTH_SHORT).show();
				//plotType = JUNGLE;
				activate(PlotType.JUNGLE);
				break;
			case R.id.btn_plot_FIREBREAK:
				Toast.makeText(mContext, "防火带标绘", Toast.LENGTH_SHORT).show();
				//plotType = FIREBREAK;
				activate(PlotType.FIREBREAK);
				break;
			//旗帜
			case R.id.btn_plot_FLAG:
				Toast.makeText(mContext, "旗帜标绘", Toast.LENGTH_SHORT).show();
				//plotType = FLAG;
				activate(PlotType.FLAG);
				break;
			default:
				break;
		}
	}
 /**
  * 结束标绘
  * */
	public void deactivate() {
		/*if (plotgraphiclayer != null) {
			this.plotgraphiclayer.removeGraphic(plotgraphicID);
		}*/
		active = false;
		/*this.point = null;
		this.polygon = null;
		this.polyline = null;
		this.plotgarphic = null;
		this.startPoint = null;*/
	}

	public void activate(PlotType plotType) {
		if (mapview == null)
			return;

		//this.deactivate();
		//this.plotType = plotType;
        //plotgraphicID++;
		mPlotType=plotType;
        mPtCollection=new PointCollection(mapview.getSpatialReference());
		active = true;
		switch (plotType) {
            //防火带
			case FIREBREAK:
				//polyline = new Polyline(mPtCollection);
                mPolylineBuilder=new PolylineBuilder(mPtCollection);
				plotgarphic = new Graphic(mPolylineBuilder.toGeometry(), plotbreaklineSymbol);
                mGraphicsOverlay.getGraphics().add(plotgarphic);
				break;
            //火场范围
            case FIREAREA:
                mPolygonBuilder=new PolygonBuilder(mPtCollection);
                //polygon = new Polygon(mPtCollection);
                plotgarphic = new Graphic(mPolygonBuilder.toGeometry(), plotFireArea);
                mGraphicsOverlay.getGraphics().add(plotgarphic);
                break;
            //箭头
			case ARROW:
			    mPolygonBuilder=new PolygonBuilder(mPtCollection);
                //polyline = new Polyline(mPtCollection);
				plotgarphic = new Graphic(mPolygonBuilder.toGeometry(), plotArrowfill);
                mGraphicsOverlay.getGraphics().add(plotgarphic);

                break;


			case JUNGLE:
                mPolygonBuilder=new PolygonBuilder(mPtCollection);
                //polygon = new Polygon(mPtCollection);
				plotgarphic = new Graphic(mPolygonBuilder.toGeometry(), plotArrowfill);
                mGraphicsOverlay.getGraphics().add(plotgarphic);

                break;
            //旗帜
			case FLAG:
			//火点
			case FIREPOINT:
				/*point = new Point();
				plotgarphic = new Graphic(point, firepointSymbol);*/
				break;
		}
		//plotgraphicID =mGraphicsOverlay.getGraphics().add(plotgarphic);
	}

	/*public static Graphic plotArrow(Polyline polyline, MapView mapview) {
		double px, py, pre_px, pre_py, top_px, top_py;
		//
		double slope, bb, cos_number, sin_number;
		Point cpoint, prepoint;
		// 定义中轴线离上下两边的距离
		double dis = mapview.getWidth() / 20.0;
		// List<Point> points = new ArrayList<>();
		int pointcount = polyline.getPointCount();
		Point[] points = new Point[polyline.getPointCount()];
		// polyline.calculateLength2D();

		// polyline.calculateLength2D()/4

		List<Point> top_points = new ArrayList<Point>();
		List<Point> bottom_points = new ArrayList<Point>();
		// Point[] bottom_points = new Point[polyline.getPointCount()];

		// 获取主控制点
		// getControlPoints();
		for (int i = 0; i < polyline.getPointCount(); i++) {
			if (i == 0) {
				continue;
			} else {
				cpoint = polyline.getPoint(i);
				px = cpoint.getX();
				py = cpoint.getY();
				prepoint = polyline.getPoint(i - 1);
				pre_px = prepoint.getX();
				pre_py = prepoint.getY();

				slope = (py - pre_py) / (px - pre_px);

				// 上边点确定
				bb = dis * (1 - i / polyline.getPointCount() * 0.9);
				cos_number = Math.cos(Math.atan(slope) - Math.PI / 2);
				sin_number = Math.sin(Math.atan(slope) - Math.PI / 2);
				top_px = 0.0;
				top_py = 0.0;
				// 下面分四种情况画线。
				if (pre_px <= px && pre_py <= py) {
					top_px = pre_px
							- ((bb * cos_number) < 0 ? (-bb * cos_number)
							: (bb * cos_number));
					top_py = pre_py
							+ ((bb * sin_number) < 0 ? (-bb * sin_number)
							: (bb * sin_number));
				} else if (pre_px <= px && pre_py > py) {
					top_px = pre_px
							+ ((bb * cos_number) < 0 ? (-bb * cos_number)
							: (bb * cos_number));
					top_py = pre_py
							+ ((bb * sin_number) < 0 ? (-bb * sin_number)
							: (bb * sin_number));
				} else if (pre_px >= px && pre_py >= py) {
					top_px = pre_px
							+ ((bb * cos_number) < 0 ? (-bb * cos_number)
							: (bb * cos_number));
					top_py = pre_py
							- ((bb * sin_number) < 0 ? (-bb * sin_number)
							: (bb * sin_number));
				} else if (pre_px >= px && pre_py <= py) {
					top_px = pre_px
							- ((bb * cos_number) < 0 ? (-bb * cos_number)
							: (bb * cos_number));
					top_py = pre_py
							- ((bb * sin_number) < 0 ? (-bb * sin_number)
							: (bb * sin_number));
				}
				// double top_point:MapPoint=new MapPoint(top_px, top_py);
				Point top_point = new Point(top_px, top_py);
				// clogger.logger.info("上半部分是：" + top_px + "yy:" + top_py);
				top_points.add(top_point);
				// 下面来确定下边的曲线
				*//*
				 * double bottom_px; double bottom_py;
				 *//*
				double bottom_px = 0.0;
				double bottom_py = 0.0;
				if (pre_px <= px && pre_py <= py) {
					bottom_px = pre_px
							+ ((bb * cos_number) < 0 ? (-bb * cos_number)
							: (bb * cos_number));
					bottom_py = pre_py
							- ((bb * sin_number) < 0 ? (-bb * sin_number)
							: (bb * sin_number));
				} else if (pre_px <= px && pre_py > py) {
					bottom_px = pre_px
							- ((bb * cos_number) < 0 ? (-bb * cos_number)
							: (bb * cos_number));
					bottom_py = pre_py
							- ((bb * sin_number) < 0 ? (-bb * sin_number)
							: (bb * sin_number));
				} else if (pre_px >= px && pre_py >= py) {
					bottom_px = pre_px
							- ((bb * cos_number) < 0 ? (-bb * cos_number)
							: (bb * cos_number));
					bottom_py = pre_py
							+ ((bb * sin_number) < 0 ? (-bb * sin_number)
							: (bb * sin_number));
				} else if (pre_px >= px && pre_py <= py) {
					bottom_px = pre_px
							+ ((bb * cos_number) < 0 ? (-bb * cos_number)
							: (bb * cos_number));
					bottom_py = pre_py
							+ ((bb * sin_number) < 0 ? (-bb * sin_number)
							: (bb * sin_number));
				}
				Point bottom_point = new Point(bottom_px, bottom_py);
				// double bottom_point:MapPoint=new MapPoint(bottom_px, bottom_py);
				bottom_points.add(bottom_point);

			}
			// points[i] = polyline.getPoint(i);
		}

		for (int i = 0; i < top_points.size(); i++) {
			*//*
			 * if (i == 0) continue;
			 *//*
			Graphic graphic = new Graphic(top_points.get(i), plotmarker);
			MapActivity.plotgraphiclayer.addGraphic(graphic);
			// Graphic graphic = new Graphic(polygon, plotArrowfill);
		}
		for (int i = 0; i < bottom_points.size(); i++) {
			*//*
			 * if (i == 0) continue;
			 *//*
			Graphic graphic = new Graphic(bottom_points.get(i), plotmarker);
			MapActivity.plotgraphiclayer.addGraphic(graphic);
			// Graphic graphic = new Graphic(polygon, plotArrowfill);
		}
		*//*
		 * *下面是添加军标箭头的程序,因为现在已经得到上下边界的曲线了。*
		 *//*

		Point end_curvepoint = polyline.getPoint(polyline.getPointCount() - 1);
		Point end_top_point = top_points.get(top_points.size() - 1);
		Point end_top_point2 = top_points.get(top_points.size() - 2);

		*//*
		 * if (pointcount > 2) { Point end_top_point2 =
		 * top_points.get(top_points.size() - 2); pre_px =
		 * end_top_point2.getX(); pre_py = end_top_point2.getY(); } else { Point
		 * end_top_point2 = top_points.get(top_points.size() - 1); pre_px =
		 * end_top_point2.getX(); pre_py = end_top_point2.getY(); }
		 *//*

		// 求箭头上方的点
		double x2 = end_curvepoint.getX();
		double y2 = end_curvepoint.getY();
		//px=end_top_point.getX();
		//py=end_top_point.getY();
		px = end_curvepoint.getX();
		py = end_curvepoint.getY();

		pre_px = end_top_point2.getX();
		pre_py = end_top_point2.getY();

		double end_slope = (pre_py - py) / (pre_px - px);
		double b = dis;
		double end_point_x = 0;
		double end_point_y = 0;
		cos_number = Math.cos(Math.atan(end_slope) - Math.PI / 2);
		sin_number = Math.sin(Math.atan(end_slope) - Math.PI / 2);
		// 下面分四种情况画线。
		if (pre_px <= px && pre_py <= py) {
			end_point_x = pre_px - ((b * cos_number) < 0 ? (-b * cos_number) : (b * cos_number));
			end_point_y = pre_py + ((b * sin_number) < 0 ? (-b * sin_number) : (b * sin_number));
		} else if (pre_px <= px && pre_py > py) {
			end_point_x = pre_px + ((b * cos_number) < 0 ? (-b * cos_number) : (b * cos_number));
			end_point_y = pre_py + ((b * sin_number) < 0 ? (-b * sin_number) : (b * sin_number));
		} else if (pre_px >= px && pre_py >= py) {
			end_point_x = pre_px + ((b * cos_number) < 0 ? (-b * cos_number) : (b * cos_number));
			end_point_y = pre_py - ((b * sin_number) < 0 ? (-b * sin_number) : (b * sin_number));
		} else if (pre_px >= px && pre_py <= py) {
			end_point_x = pre_px - ((b * cos_number) < 0 ? (-b * cos_number) : (b * cos_number));
			end_point_y = pre_py - ((b * sin_number) < 0 ? (-b * sin_number) : (b * sin_number));
		}
		// 创建第三个点，然后创建三角形
		// clogger.logger.info("右上面的点的横坐标点x:" + end_point_x +
		// "----------右上方点的纵坐标的y:" + end_point_y);
		// 得到右上方的点
		Point end_right_point = new Point(end_point_x, end_point_y);
		top_points.add(end_right_point);
		// 最右边，箭头顶点的坐标是
		// clogger.logger.info("顶点的横坐标是x:" + end_curvepoint.getX() +
		// "---------顶点的纵坐标y:" + end_curvepoint.getY());
		top_points.add(end_curvepoint);
		// 下边线
		Point end_bottom_point = bottom_points.get(bottom_points.size() - 1);
		Point end_bottom_point2 = bottom_points.get(bottom_points.size() - 2);
		*//*
		 * double bx0 = 0.0, by0 = 0.0; if (pointcount > 2) { Point
		 * end_bottom_point2 = top_points.get(bottom_points.size() - 2); bx0 =
		 * end_bottom_point2.getX(); by0 = end_bottom_point2.getY(); } else {
		 * Point end_bottom_point2 = top_points.get(bottom_points.size() - 1);
		 * bx0 = end_bottom_point2.getX(); by0 = end_bottom_point2.getY(); }
		 *//*
		// 求箭头下方向的点
		double bx1 = end_bottom_point.getX();
		double by1 = end_bottom_point.getY();

		double bx0 = end_bottom_point2.getX();
		double by0 = end_bottom_point2.getY();

		double bend_slope = (by1 - by0) / (bx1 - bx0);
		double bend_point_x = 0;
		double bend_point_y = 0;
		double bend_cos_number = Math.cos(Math.atan(bend_slope) - Math.PI / 2);
		double bend_sin_number = Math.sin(Math.atan(bend_slope) - Math.PI / 2);
		if (bx0 <= bx1 && by0 <= by1) {
			bend_point_x = bx0 + ((b * bend_cos_number) < 0 ? (-b * bend_cos_number) : (b * bend_cos_number));
			bend_point_y = by0 - ((b * bend_sin_number) < 0 ? (-b * bend_sin_number) : (b * bend_sin_number));
		} else if (bx0 <= bx1 && by0 > by1) {
			bend_point_x = bx0 - ((b * bend_cos_number) < 0 ? (-b * bend_cos_number) : (b * bend_cos_number));
			bend_point_y = by0 - ((b * bend_sin_number) < 0 ? (-b * bend_sin_number) : (b * bend_sin_number));
		} else if (bx0 >= bx1 && by0 >= by1) {
			bend_point_x = bx0 - ((b * bend_cos_number) < 0 ? (-b * bend_cos_number) : (b * bend_cos_number));
			bend_point_y = by0 + ((b * bend_sin_number) < 0 ? (-b * bend_sin_number) : (b * bend_sin_number));
		} else if (bx0 >= bx1 && by0 <= by1) {
			bend_point_x = bx0 + ((b * bend_cos_number) < 0 ? (-b * bend_cos_number) : (b * bend_cos_number));
			bend_point_y = by0 + ((b * bend_sin_number) < 0 ? (-b * bend_sin_number) : (b * bend_sin_number));
		}
		// clogger.logger.info("下方的点的横坐标x为:" + bend_point_x +
		// "-------下方的点的纵坐标y为:" + bend_point_y);
		// 得到右下方的点
		Point bend_right_point = new Point(bend_point_x, bend_point_y);
		top_points.add(bend_right_point);
		for (int k = bottom_points.size() - 1; k >= 0; k--) {
			// 这个是下半部分
			// clogger.logger.info("由此开始下半部分:" + bottomcurepoints[k].getX() +
			// "yyyyy:" + bottomcurepoints[k].getY());
			top_points.add(bottom_points.get(k));
		}
		// 添加尾部的部分,至此完成对军标多边行的算法，
		top_points.add(polyline.getPoint(1));
		top_points.add(top_points.get(0));
		Polygon polygon = new Polygon();
		polygon.startPath(top_points.get(0));
		for (int i = 0; i < top_points.size(); i++) {
			*//*
			 * if (i == 0) continue;
			 *//*
			polygon.lineTo(top_points.get(i));
			Graphic graphic = new Graphic(top_points.get(i), plotmarker);
			MapActivity.plotgraphiclayer.addGraphic(graphic);
			// Graphic graphic = new Graphic(polygon, plotArrowfill);
		}

		// clogger.logger.info("军标上的点的个数是" + top_points.length);
		*//*
		 * double rings:Array=new Array(); rings.add(top_points); return rings;
		 *//*
		Graphic graphic = new Graphic(polygon, getPlotArrowfill());
		return graphic;

	}*/

	/*public static Graphic plotArrow2(Polyline polyline, MapView mapview) {
		double px, py, pre_px, pre_py, top_px, top_py;
		Polygon bufferpolygon = GeometryEngine.buffer(polyline,
				mapview.getSpatialReference(), 20.0,
				null);
		Point endpoint = mapview.toScreenPoint(polyline.getPoint(polyline
				.getPointCount() - 1));
		Point prepoint = mapview.toScreenPoint(polyline.getPoint(polyline
				.getPointCount() - 2));
		px = endpoint.getX();
		py = endpoint.getY();
		pre_px = prepoint.getX();
		pre_py = prepoint.getY();
		// 斜率
		double end_slope = (pre_py - py) / (pre_px - px);
		double x1 = end_slope * (-1) * (py + 50);
		if (x1 < 0) {
			x1 = x1 * (-1);
		}
		double x2 = end_slope * (-1) * (py - 50);
		if (x2 < 0) {
			x2 = x2 * (-1);
		}
		Point top_right = new Point(x1, py + 50);
		Point top_left = new Point(x2, py - 50);
		double y = end_slope * (px + 45);
		Point topPoint = new Point(px + 50, y);
		ArrayList<Point> toppoints = new ArrayList<Point>();
		toppoints.add(top_left);
		toppoints.add(topPoint);
		toppoints.add(top_right);
		Polygon top_polygon = new Polygon();
		top_polygon.startPath(toppoints.get(0));
		for (int i = 0; i < toppoints.size(); i++) {
			*//*
			 * if (i == 0) continue;
			 *//*
			top_polygon.lineTo(toppoints.get(i));
			Graphic graphic = new Graphic(toppoints.get(i), plotmarker);
			MapActivity.plotgraphiclayer.addGraphic(graphic);
			// Graphic graphic = new Graphic(polygon, plotArrowfill);
		}
		Geometry[] geometrys = new Geometry[]{bufferpolygon, top_polygon};
		GeometryEngine.union(geometrys, mapview.getSpatialReference());
		Graphic graphic = new Graphic(bufferpolygon, getPlotArrowfill());
		return graphic;
	}*/

	/**
	 * 绘制贝塞尔箭头
	 */
	public  Graphic plotArrow(Point candidatePoint, PointCollection mPtCollection) {
		double px, py, pre_px, pre_py;
        PointCollection pointCollection=new PointCollection(mapview.getSpatialReference());
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
			/*
			 * ArrayList<Point> ring = new ArrayList<Point>(); ring.add(pt1);
			 * ring.add(p1);
			 */

			//PolygonBuilder polygonBuilder=new PolygonBuilder()
            pointCollection.add(pt1);
            pointCollection.add(p1);

            PointCollection pc_arrowhead = createArrowHeadPathEx(p1, candidatePoint,
                    p2, plen, headPercentage, 15);
            pointCollection.addAll(pc_arrowhead);
            pointCollection.add(p2);
            pointCollection.add(pt2);
            pointCollection.add(pt1);

			/*MultiPath path = new Polygon();
			Polygon polygon = new Polygon();
			path.startPath(pt1);


			path.lineTo(p1);
			MultiPath arrowheadpath = createArrowHeadPathEx(p1, candidatePoint,
					p2, plen, headPercentage, 15);


			path.add(arrowheadpath, false);

			// ring.push(candidatePoint);
			path.lineTo(p2);
			path.lineTo(pt2);
			path.lineTo(pt1);
			polygon.add(path, false);
			resultgraphic = new Graphic(polygon, getPlotArrowfill());*/
            resultgraphic = new Graphic(new PolygonBuilder(pointCollection).toGeometry(), plotArrowfill);


		} else {
			ArrayList<Point> tempArray = new ArrayList<Point>();
			ArrayList<Point> leftArray = new ArrayList<Point>();
			ArrayList<Point> rightArray = new ArrayList<Point>();
			for (int i = 0; i < pointcount; i++) {
				tempArray.add(mPtCollection.get(i));
				/*
				 * Graphic graphic = new Graphic(polyline.getPoint(i),
				 * plotmarker);
				 * MapActivity.plotgraphiclayer.addGraphic(graphic);
				 */
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
		/*	  leftArray.splice(Math.floor((1 - this._headPercentage) * 70),
			  Number.MAX_VALUE);*/

			rightArray = Calculate.CreateBezierPathPCOnly(rightArray, 70);
			 /* rightArray.splice(Math.floor((1 - this._headPercentage) * 70),
			  Number.MAX_VALUE);
			  */

			/*MultiPath arrowheadpath = createArrowHeadPathEx(leftArray.get(leftArray.size() - 1), candidatePoint,
					rightArray.get(rightArray.size() - 1), Calculate.ptCollectionLen(tempArray, 0), headPercentage, 15);*/
            PointCollection  pc_arrowhead = createArrowHeadPathEx(leftArray.get(leftArray.size() - 1), candidatePoint,
                    rightArray.get(rightArray.size() - 1), Calculate.ptCollectionLen(tempArray, 0), headPercentage, 15);

			//Polygon polygon1 = new Polygon();
			ArrayList<Point> resultpoints = new ArrayList<Point>();
			resultpoints.addAll(leftArray);
			Collections.reverse(rightArray);
			resultpoints.addAll(rightArray);
			for (int i = 0; i < resultpoints.size(); i++) {
                pointCollection.add(resultpoints.get(i));
				/*if (i == 0) {
                    pointCollection.add(resultpoints.get(i));
					polygon1.startPath(resultpoints.get(i));

				} else {
					polygon1.lineTo(resultpoints.get(i));
				}*/
			}

			//Geometry[] geometrys = new Geometry[]{polygon1, arrowheadpath};

			Geometry uniongeo = GeometryEngine.union(new PolygonBuilder(pointCollection).toGeometry(),
					new PolygonBuilder(pc_arrowhead).toGeometry());
			resultgraphic = new Graphic(uniongeo, plotArrowfill);
		}

		return resultgraphic;

	}

	/**
	 * 绘制贝塞尔polygon
	 */
	/*private   Graphic plot_BEZIER_POLYGON(Part part, Point candidatePoint, int plotgraphicID) {
		Graphic resultgraphic = null;
		//int pointcount = polygon.getPointCount();
        int pointcount=part.getPointCount();
		ArrayList<Point> tempArray = new ArrayList<Point>();
		ArrayList<Point> BezierArray = new ArrayList<Point>();
		ArrayList<Point> BezierArray2 = new ArrayList<Point>();
		int dis = 100;
		double x = 0.0, y = 0.0;
		Polygon resultpolygon = new Polygon();
		Polygon resultpolygon2 = new Polygon();
		if (polygon.getPointCount() <= 2) {
			resultgraphic = new Graphic(polygon, getPlotlineSymbol());
		} else {
			for (int i = 0; i < pointcount; i++) {

				tempArray.add(polygon.getPoint(i));
				if (i == pointcount - 1) {

					switch (Calculate.twoPtsRelationShip(polygon.getPoint(i),
							polygon.getPoint(0))) {
						case "ne":
							x = (polygon.getPoint(i).getX() + polygon.getPoint(0)
									.getX()) / 2 - dis;
							y = (polygon.getPoint(i).getY() + polygon.getPoint(0)
									.getY()) / 2 + dis;
							break;
						case "nw":
							x = (polygon.getPoint(i).getX() + polygon.getPoint(0)
									.getX()) / 2 - dis;
							y = (polygon.getPoint(i).getY() + polygon.getPoint(0)
									.getY()) / 2 - dis;
							break;
						case "sw":
							x = (polygon.getPoint(i).getX() + polygon.getPoint(0)
									.getX()) / 2 + dis;
							y = (polygon.getPoint(i).getY() + polygon.getPoint(0)
									.getY()) / 2 - dis;
							break;
						case "se":
							x = (polygon.getPoint(i).getX() + polygon.getPoint(0)
									.getX()) / 2 + dis;
							y = (polygon.getPoint(i).getY() + polygon.getPoint(0)
									.getY()) / 2 + dis;
							break;
					}

					tempArray.add(new Point(x, y));
				} else {
					switch (Calculate.twoPtsRelationShip(polygon.getPoint(i),
							polygon.getPoint(i + 1))) {
						case "ne":
							x = (polygon.getPoint(i).getX() + polygon.getPoint(
									i + 1)
									.getX()) / 2 - dis;
							y = (polygon.getPoint(i).getY() + polygon.getPoint(
									i + 1)
									.getY()) / 2 + dis;
							break;
						case "nw":
							x = (polygon.getPoint(i).getX() + polygon.getPoint(
									i + 1)
									.getX()) / 2 - dis;
							y = (polygon.getPoint(i).getY() + polygon.getPoint(
									i + 1)
									.getY()) / 2 - dis;
							break;
						case "sw":
							x = (polygon.getPoint(i).getX() + polygon.getPoint(
									i + 1)
									.getX()) / 2 + dis;
							y = (polygon.getPoint(i).getY() + polygon.getPoint(
									i + 1)
									.getY()) / 2 - dis;
							break;
						case "se":
							x = (polygon.getPoint(i).getX() + polygon.getPoint(
									i + 1)
									.getX()) / 2 + dis;
							y = (polygon.getPoint(i).getY() + polygon.getPoint(
									i + 1)
									.getY()) / 2 + dis;
							break;
					}
					tempArray.add(new Point(x, y));
				}


			}
			for (int i = 0; i < tempArray.size() - 1; i += 2) {
				ArrayList<Point> tempArray2 = new ArrayList<Point>();
				if (i == tempArray.size() - 2) {
					tempArray2.add(tempArray.get(i));
					tempArray2.add(tempArray.get(i + 1));
					tempArray2.add(tempArray.get(0));
				} else {
					tempArray2.add(tempArray.get(i));
					tempArray2.add(tempArray.get(i + 1));
					tempArray2.add(tempArray.get(i + 2));
				}
				BezierArray.addAll(createBezierPathPolygon(tempArray2));

			}
			*//*
			 * tempArray.add(polygon.getPoint(1));
			 *
			 * BezierArray = createBezierPathPolygon(tempArray); for (int i = 1;
			 * i < tempArray.size(); i++) { if (i != tempArray.size() - 1) {
			 * Point mirrorp = Calculate.PointToLine(tempArray.get(0),
			 * tempArray.get(tempArray.size() - 1), tempArray.get(i));
			 * tempArray.set(i, mirrorp); } } // Collections.reverse(tempArray);
			 * BezierArray2 = createBezierPathPolygon(tempArray);
			 *//*
			// BezierArray.addAll(BezierArray2);
			for (int i = 0; i < BezierArray.size(); i++) {
				if (i == 0) {
					resultpolygon.startPath(BezierArray.get(i));
				} else {
					resultpolygon.lineTo(BezierArray.get(i));
				}
			}
			*//*
			 * for (int i = 0; i < BezierArray2.size(); i++) { if (i == 0) {
			 * resultpolygon2.startPath(BezierArray2.get(i)); } else {
			 * resultpolygon2.lineTo(BezierArray2.get(i)); } } Geometry[]
			 * geometrys = new Geometry[] { resultpolygon, resultpolygon2 };
			 * Geometry uniongeo = GeometryEngine.union(geometrys,
			 * mapview.getSpatialReference());
			 *//*
			resultgraphic = new Graphic(resultpolygon, getPlotArrowfill());

		}
		return resultgraphic;

	}
*/
	/**
	 * 获取主控制点
	 */
	/*private static void getControlPoints(Polyline pl) {
		ArrayList<Point> points = new ArrayList<Point>();
		double linelength = pl.calculateLength2D();

		ArrayList<Point> controlpoints = new ArrayList<Point>();
		Point firstpoint = new Point();
		Point endpoint = new Point();
		for (int i = 0; i < pl.getPointCount(); i++) {
			points.add(pl.getPoint(i));

		}
		firstpoint = points.get(0);
		endpoint = points.get(points.size() - 1);

		for (int i = 0; i < pl.getPathCount(); i++) {

			// pl.getPathEnd(pathIndex)
			points.add(pl.getPoint(i));

		}

	}*/

	/**
	 * 绘出箭头
	 */
	public  PointCollection createArrowHeadPathEx(Point pt1, Point candidatePt,
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


        PointCollection pointCollection=new PointCollection(mapview.getSpatialReference());
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


	public static void setPlotArrowfill(FillSymbol plotArrowfill) {
		PlotUtil.plotArrowfill = plotArrowfill;
	}


	public static LineSymbol getPlotlineSymbol() {
		return plotlineSymbol;
	}

	public static void setPlotlineSymbol(LineSymbol plotlineSymbol) {
		PlotUtil.plotlineSymbol = plotlineSymbol;
	}


	public static void setPlotbreaklineSymbol(LineSymbol plotbreaklineSymbol) {
		PlotUtil.plotbreaklineSymbol = plotbreaklineSymbol;
	}



}

