package com.titan.gyslfh.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.esri.arcgisruntime.geometry.PolylineBuilder;
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

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

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



   /* @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            *//***
             * 定位权限为必须权限，用户如果禁止，则每次进入都会申请
             *//*
            // 定位精确位置
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
			*//*
			 * 读写权限和电话状态权限非必要权限(建议授予)只会申请一次，用户同意或者禁止，只会弹一次
			 *//*
            // 读写权限
            if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }
            // 读取电话状态权限
            if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                //permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), requestCode);
            }
        }
    }

    @TargetApi(23)
    private boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)){
                return true;
            }else{
                permissionsList.add(permission);
                return false;
            }

        }else{
            return true;
        }
    }*/

    private void initView() {
        mSharedPreferences=mContext.getSharedPreferences(TitanApplication.PREFS_NAME,0);
        sw_istrack = (Switch) findViewById(R.id.sw_istrack);
        sw_istrack.setChecked(mSharedPreferences.getBoolean("istrack",false));
        sw_istrack.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Toast.makeText(mContext, "轨迹跟踪开启", Toast.LENGTH_SHORT).show();
                    //mViewModel.showTrackLine();
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
    private MainFragment findOrCreateViewFragment() {
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
    private MainViewModel findOrCreateViewModel() {
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



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Location permission was granted. This would have been triggered in response to failing to start the
            // LocationDisplay, so try starting this again.
            mainFragment.mLocationDisplay.startAsync();
            locationService.start();
        } else {
            // If permission was denied, show toast to inform user what was chosen. If LocationDisplay is started again,
            // request permission UX will be shown again, option should be shown to allow never showing the UX again.
            // Alternative would be to disable functionality so request is not shown again.
            //Toast.makeText(MainActivity.this, getResources().getString(R.string.location_permission_denied), Toast.LENGTH_SHORT).show();
            // Update UI to reflect that the location display did not actually start
            // mSpinner.setSelection(0, true);
        }
    }
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        // -----------location config ------------
      /*  //定位初始化
        locationService = ((TitanApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mViewModel);
        locationService.setLocationOption(locationService.getDefaultLocationClientOption());
        locationService.start();*/
        // 定位SDK

    }
    /**
     * Stop location service
     */
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if(locationService!=null){
            locationService.unregisterListener(mViewModel); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        super.onDestroy();
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
        if (secondTime - firstTime > 800) { // 如果两次按键时间间隔大于2秒，则不退出

            Toast.makeText(mContext,"再按一次退出程序",Toast.LENGTH_SHORT).show();
            firstTime = secondTime;// 更新firstTime

        } else {
            TitanApplication.getInstance().finshAllActivities();
           // System.exit(0);// 否则退出程序
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
