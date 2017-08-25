package com.titan.gyslfh.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.geometry.PointCollection;
import com.esri.arcgisruntime.geometry.Polyline;
import com.esri.arcgisruntime.geometry.PolylineBuilder;
import com.esri.arcgisruntime.geometry.SpatialReference;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.mapping.view.Graphic;
import com.titan.BaseActivity;
import com.titan.Injection;
import com.titan.ViewModelHolder;
import com.titan.gyslfh.TitanApplication;
import com.titan.gyslfh.layercontrol.LayerControlFragment;
import com.titan.gyslfh.layercontrol.LayerControlViewModel;
import com.titan.gyslfh.login.LoginActivity;
import com.titan.loction.baiduloc.LocationService;
import com.titan.navi.BaiduNavi;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

import java.util.List;

import static com.titan.gis.SymbolUtil.lineSymbol;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity {

    public static final String MAIN_VIEWMODEL_TAG = "MAIN_VIEWMODEL_TAG";
       //图层控制
    public static final String LAYERCONTROL_TAG = "LAYERCONTROL_TAG";

    //定位服务
    private LocationService locationService;

    //private DrawerLayout mDrawerLayout;

    public MainViewModel getmViewModel() {
        return mViewModel;
    }

    public void setmViewModel(MainViewModel mViewModel) {
        this.mViewModel = mViewModel;
    }

    private MainViewModel mViewModel;

    private LayerControlViewModel mlayerControlViewModel;
    //轨迹开关
    Switch sw_istrack;

    Context mContext;
    /**定位需要动态获取的权限*/
    String[] reqPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission
            .ACCESS_COARSE_LOCATION};
    private int requestCode = 2;

    SharedPreferences mSharedPreferences;
    //主界面
    public   MainFragment mainFragment;
    //图层控制
    private LayerControlFragment layerControlFragment;
    //导航
    public BaiduNavi mBaiduNavi;
    //构建
    PolylineBuilder lineBuilder=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitanApplication.getInstance().addActivity(this);
        //测试市级用户
        //String user= "{\"dqid\":\"1470\",\"dqName\":\"贵阳市\",\"role\":\"超级管理员\",\"accountStatus\":\"1\",\"clientID\":\"cbe72c90a5468581fe8ca983521e55eb\",\"userID\":\"1\",\"dqLevel\":\"3\"}";
        //区县用户
        //String byquser="{\"dqid\":\"1459\",\"dqName\":\"白云区\",\"role\":\"区县用户\",\"accountStatus\":\"1\",\"clientID\":\"231431\",\"userID\":\"10\",\"dqLevel\":\"4\"}";

        //TitanApplication.setmUserModel(new Gson().fromJson(byquser,UserModel.class));
        //推送回调
        TitanApplication.mainActivity=this;
        mContext=this;

        //setupNavigationDrawer();

        mainFragment = findOrCreateViewFragment();

        mViewModel = findOrCreateViewModel();
        // Link View and ViewModel
        mainFragment.setViewModel(mViewModel);
        //initView();
        //getPersimmions();

    }




    private void initView() {
        mSharedPreferences=mContext.getSharedPreferences(TitanApplication.PREFS_NAME,0);
        sw_istrack = (Switch) findViewById(R.id.sw_istrack);
        sw_istrack.setChecked(mSharedPreferences.getBoolean("istrack",false));
        sw_istrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(mContext, "轨迹跟踪开启", Toast.LENGTH_SHORT).show();
                    List<Graphic> gcList=mainFragment.mGraphicsOverlay.getGraphics();
                    Point point = mainFragment.mMainFragBinding.mapview.getLocationDisplay().getLocation().getPosition();
                    //mViewModel.showTrackLine();
                    SpatialReference sp2= SpatialReferences.getWgs84();
                    PointCollection points = new PointCollection(sp2);
//                    points.add(new Point(13051280.084232, 3714230.644090));
//                    points.add(new Point(13052280.084232, 3714230.644090));
                   //points.add((Point) GeometryEngine.project(new Point(117.234089, 31.792675),sp3));
//                    points.add((Point) GeometryEngine.project(new Point(117.235089, 31.792675),sp3));
//                    points.add((Point) GeometryEngine.project(new Point(117.236089, 31.792675),sp3));
                    points.add(point);
//                    points.add(new Point(117.234125, 31.792717));
                    Polyline polyline = new Polyline(points);
                    Graphic graphic = new Graphic(polyline, lineSymbol);
                    mainFragment.mGraphicsOverlay.getGraphics().add(graphic);
                }else {
                    mainFragment.mGraphicsOverlay.getGraphics().clear();
                    Toast.makeText(mContext, "轨迹跟踪关闭", Toast.LENGTH_SHORT).show();
                }
                mSharedPreferences.edit().putBoolean("istrack",isChecked).apply();
                mViewModel.istrack.set(isChecked);
            }
        });
    }

    /**
     * 初始化
     */
    /*private void setupNavigationDrawer() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
    }*/

    /**
     * 设置点击事件
     */
    /*private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            //切换帐号
                            case R.id.list_navigation_menu_resign:
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                // Do nothing, we're already on that screen
                                break;
                            case R.id.list_nav_menu_setting:
                                Intent intent1 = new Intent(mContext, LoginActivity.class);
                                startActivity(intent1);
                                break;
                            default:
                                break;
                        }
                        // Close the navigation drawer when an item is selected.
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
    }*/

    @NonNull
    public MainFragment findOrCreateViewFragment() {
        MainFragment tasksFragment =
                (MainFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = MainFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.content_frame);
        }
        return tasksFragment;
    }




    @NonNull
    public MainViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<MainViewModel> retainedViewModel =
                (ViewModelHolder<MainViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(MAIN_VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            //LayerControlViewModel layerControlViewModel=new LayerControlViewModel(getApplicationContext(),this);
             // There is no ViewModel yet, create it.
            MainViewModel viewModel = new MainViewModel(getApplicationContext(), Injection.provideDataRepository(mContext),mainFragment);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    MAIN_VIEWMODEL_TAG);
            return viewModel;
        }
    }



    /**
     * 更改登录
     * @param view
     */
    public void onRelogin(View view) {
        if(locationService!=null){
            locationService.unregisterListener(mViewModel); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    /**
     * 监听回退按钮
     */
    long firstTime = 0;
    @Override
    public void onBackPressed() {
        // exitApp();
        long secondTime = System.currentTimeMillis();
        int d=getFragmentManager().getBackStackEntryCount();
        getFragmentManager().popBackStack();
        if(getFragmentManager().getBackStackEntryCount()>0){

            super.onBackPressed();
        }else{
            if (secondTime - firstTime > 800) { // 如果两次按键时间间隔大于2秒，则不退出

                Toast.makeText(mContext,"再按一次退出程序",Toast.LENGTH_SHORT).show();
                firstTime = secondTime;// 更新firstTime

            } else {
                TitanApplication.getInstance().finshAllActivities();
                // System.exit(0);// 否则退出程序
            }
        }

    }

}
