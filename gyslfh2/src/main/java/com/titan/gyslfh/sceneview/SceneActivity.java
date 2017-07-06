package com.titan.gyslfh.sceneview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.titan.BaseActivity;
import com.titan.BaseViewModel;
import com.titan.ViewModelHolder;
import com.titan.model.TitanLayer;
import com.titan.model.TitanLocation;
import com.titan.newslfh.R;
import com.titan.util.ActivityUtils;

import java.util.List;

/**
 * 三维场景界面
 */
public class SceneActivity extends BaseActivity  {



    private SceneFragment mFragment;

    private SceneViewModel mViewModel;
    //位置信息
    private TitanLocation titanLocation;
    //图层信息
    private List<TitanLayer> mLayerList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene);
        //位置信息
        titanLocation= (TitanLocation) getIntent().getExtras().getSerializable("loc");
        //图层信息
        mLayerList= (List<TitanLayer>) getIntent().getExtras().getSerializable("layers");

        mFragment= (SceneFragment) findOrCreateViewFragment();

        mViewModel= (SceneViewModel) findOrCreateViewModel();

        mFragment.setViewModel(mViewModel);
        //mContext=this;

    }
    @Override
    public Fragment findOrCreateViewFragment() {
        SceneFragment tasksFragment =
                (SceneFragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (tasksFragment == null) {
            // Create the fragment
            tasksFragment = SceneFragment.newInstance(titanLocation,mLayerList);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), tasksFragment, R.id.content_frame);
        }
        return tasksFragment;
    }

    @Override
    public BaseViewModel findOrCreateViewModel() {
        // In a configuration change we might have a ViewModel present. It's retained using the
        // Fragment Manager.
        @SuppressWarnings("unchecked")
        ViewModelHolder<SceneViewModel> retainedViewModel =
                (ViewModelHolder<SceneViewModel>) getSupportFragmentManager()
                        .findFragmentByTag(VIEWMODEL_TAG);

        if (retainedViewModel != null && retainedViewModel.getViewmodel() != null) {
            // If the model was retained, return it.
            return retainedViewModel.getViewmodel();
        } else {
            //LayerControlViewModel layerControlViewModel=new LayerControlViewModel(getApplicationContext(),this);
            // There is no ViewModel yet, create it.
            SceneViewModel viewModel = new SceneViewModel(getApplicationContext(),mFragment);
            // and bind it to this Activity's lifecycle using the Fragment Manager.
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    ViewModelHolder.createContainer(viewModel),
                    VIEWMODEL_TAG);
            return viewModel;
        }
    }
}
