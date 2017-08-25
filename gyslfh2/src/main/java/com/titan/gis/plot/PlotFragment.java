package com.titan.gis.plot;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.SketchCreationMode;
import com.esri.arcgisruntime.mapping.view.SketchEditor;
import com.esri.arcgisruntime.mapping.view.SketchStyle;
import com.titan.Injection;
import com.titan.gis.GisUtil;
import com.titan.gyslfh.layercontrol.LayerControlFragment;
import com.titan.gyslfh.layercontrol.LayerControlViewModel;
import com.titan.model.TitanLocation;
import com.titan.newslfh.R;
import com.titan.newslfh.databinding.FragmentPlotBinding;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link PlotFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlotFragment extends Fragment implements IPlot{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArcGISMap mArcMap;

    private GraphicsOverlay mPlotOverlay;
    //标绘类型
    private PlotUtil.PlotType mPlotType;

    private FragmentPlotBinding mDataBinding;

    //绘制样式
    private SketchStyle sketchStyle;
    //草图编辑器
    private static SketchEditor sketchEditor;
    //图层控制
    private LayerControlFragment mlayerControlFragment;
    //标绘工具类
    private PlotUtil mPlotUtil;

    public void setmViewModel(PlotViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    private PlotViewModel mViewModel;
    //当前位置
    private TitanLocation mLoc;

    private static Viewpoint mViewpoint;

    public PlotFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment PlotFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PlotFragment newInstance(Viewpoint viewpoint) {
        PlotFragment fragment = new PlotFragment();
        mViewpoint=viewpoint;
       /* Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, viewpoint);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mLoc = (TitanLocation) getArguments().getSerializable(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        intiMapView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mDataBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_plot,container,false);
        mDataBinding.setViewmodel(mViewModel);
        return mDataBinding.getRoot();

    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 初始化地图
     */
    private void intiMapView() {
        //ArcGISRuntime.setClientId("qwvvlkN4jCDmbEAO");//去除水印的
        ArcGISRuntimeEnvironment.setLicense("runtimelite,1000,rud8065403504,none,RP5X0H4AH7CLJ9HSX018");
        //初始化底图()
        mArcMap = new ArcGISMap(Basemap.createOpenStreetMap());
        //添加绘制图层
        mPlotOverlay = GisUtil.addGraphicsOverlay(mDataBinding.mapview);
        mDataBinding.mapview.setMap(mArcMap);
        //去除版权声明
        mDataBinding.mapview.setAttributionTextVisible(false);

        if(mViewpoint!= null){
            mDataBinding.mapview.setViewpoint(mViewpoint);
        }

        //sketchEditor = new SketchEditor();
        //sketchStyle=new SketchStyle();
        //initSketchEditor();
        //sketchEditor.setSketchStyle(sketchStyle);
        //mDataBinding.mapview.setOnTouchListener(new PlotTouchListener(getActivity(),mDataBinding.mapview));


    }


    /**
     * 保存绘制的图形
     * @param plottype
     */
    public   void saveSketchGraphic(PlotUtil.PlotType plottype) {
        Graphic graphic=null;

        if (plottype==null){
            //Toast.makeText(getActivity(), "没有标绘", Toast.LENGTH_LONG).show();
            return;
        }
        switch (plottype){
            case ARROW:
                //mDataBinding.mapview.setOnTouchListener(null);
                /*if(mPlotUtil!=null){
                    graphic=mPlotUtil.getmGraphicsOverlay().getGraphics().get(mPlotUtil.getPlotgraphicID());
                }*/

                /*Multipoint pts= (Multipoint) sketchEditor.getGeometry();
                if(pts.getPoints().size()>=2){
                    PointCollection ptscollection=new PointCollection(pts.getPoints());
                    graphic = plotArrow(pts.getPoints().get(pts.getPoints().size()), ptscollection);
                    //mPlotOverlay.getGraphics().add(graphic);
                }*/

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
            default:
                break;
        }
        mPlotOverlay.getGraphics().add(graphic);
    }


    @Override
    public void showLayers() {
        if(mlayerControlFragment==null){
            mlayerControlFragment= LayerControlFragment.getInstance(mArcMap,mDataBinding.mapview,mPlotOverlay);
            LayerControlViewModel viewModel=new LayerControlViewModel(getActivity(),mlayerControlFragment, Injection.provideDataRepository(getActivity()),mlayerControlFragment.getmLayerList());
            mlayerControlFragment.setViewModel(viewModel);
        }
        String LAYERCONTROL_TAG = "LAYEARS";
        mlayerControlFragment.show(getFragmentManager(), LAYERCONTROL_TAG);
    }

    @Override
    public void Plot(int plottype) {
        initSketchEditor();
        mPlotUtil=new PlotUtil(getActivity(),mDataBinding.mapview,mPlotOverlay);
        /*if( sketchEditor!=null&&sketchEditor.getGeometry()!=null){
            saveSketchGraphic(mPlotType);
        }*/
        //mDataBinding.mapview.setOnTouchListener(new PlotTouchListener(getActivity(),mDataBinding.mapview));
        switch (plottype){
            case 1:
                mPlotType= PlotUtil.PlotType.ARROW;
                //mPlotUtil=new PlotUtil(getActivity(),mDataBinding.mapview,mPlotOverlay);
                //mPlotUtil.setmPlotType(mPlotType);
                mPlotUtil.activate(mPlotType);
                mDataBinding.mapview.setOnTouchListener(mPlotUtil.getPlotTouchListener());
                //sketchEditor.stop();
                //mPlotType= PlotUtil.PlotType.ARROW;
                //sketchStyle.setVertexSymbol(SymbolUtil.vertexSymbol);
                //sketchEditor.start(SketchCreationMode.MULTIPOINT);
                mViewModel.snackbarText.set("箭头");
                //activate(PlotType.ARROW);
                break;
            case 2:
                mPlotType= PlotUtil.PlotType.FIREAREA;
                sketchStyle.setFillSymbol(PlotUtil.plotFireArea);
                sketchEditor.start(SketchCreationMode.FREEHAND_POLYGON);
                mViewModel.snackbarText.set("火场范围");
                //activate(PlotType.FIREAREA);
                break;
            case 3:
                mPlotType= PlotUtil.PlotType.FIREBREAK;
                sketchStyle.setLineSymbol(PlotUtil.plotbreaklineSymbol);
                sketchEditor.start(SketchCreationMode.FREEHAND_LINE);
                mViewModel.snackbarText.set("防火带");
                //activate(PlotType.FIREBREAK);
                break;
            case 4:
                mPlotType= PlotUtil.PlotType.FIREPOINT;
                sketchStyle.setVertexSymbol(mPlotUtil.firepointSymbol);
                sketchEditor.start(SketchCreationMode.POINT);
                mViewModel.snackbarText.set("火点");
                //activate(PlotType.FIREPOINT);
                break;
            case 5:
                mPlotType= PlotUtil.PlotType.FLAG;
                sketchStyle.setVertexSymbol(mPlotUtil.flagSymbol);
                sketchEditor.start(SketchCreationMode.POINT);
                mViewModel.snackbarText.set("旗帜");
                //activate(PlotType.FLAG);
                break;
        }

    }

    /**
     * 初始化style
     */
    private void initSketchEditor() {
        if(mDataBinding.mapview.getSketchEditor()==null){
            sketchEditor=new SketchEditor();
            mDataBinding.mapview.setSketchEditor(sketchEditor);
        }
        sketchStyle=new SketchStyle();
        sketchEditor.setSketchStyle(sketchStyle);


    }

    /**
     * 撤销
     */
    @Override
    public void onRevok() {
        //mPlotOverlay.getGraphics().get(0).g

    }

    /**
     * 分享
     */
    @Override
    public void onShare() {

    }

    /**
     * 保存
     */
    @Override
    public void onSave() {
        saveSketchGraphic(mPlotType);

    }

    /**
     * 确认
     */
    @Override
    public void onConfirm() {

    }
}
