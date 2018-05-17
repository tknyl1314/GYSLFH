package com.titan.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDelegate
import android.util.Log

/**
 * Created by hanyw on 2017/12/4/004.
 * 基础activity
 */

abstract class BaseActivity : AppCompatActivity(){
    //
    protected  var mContext: Context? = null

    /** 日志输出标志  */
    protected val TAG = this.javaClass.simpleName
    /** ViewModel标志  */
    protected val VIEWMODEL_TAG = this.javaClass.simpleName

    protected var mViewModel: BaseViewModel? = null


    protected var mFragment: Fragment? = null


    abstract fun findOrCreateViewFragment(): Fragment
    abstract fun findOrCreateViewModel(): BaseViewModel

    /**
     * [页面跳转]
     *
     * @param clz
     */
    fun startActivity(clz: Class<*>) {
        startActivity(Intent(this@BaseActivity, clz))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        mContext = this

    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart()")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy()")
    }

    companion object {

        //矢量支持
        init {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }
}
