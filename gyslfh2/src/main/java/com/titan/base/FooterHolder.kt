package com.titan.base

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.titan.newslfh.R

/**
 * Created by hanyw on 2018/3/12.
 * 底部提示信息holder
 */
class FooterHolder(var binding: ViewDataBinding): RecyclerView.ViewHolder(binding.root) {
    val mLoadingView = binding.root.findViewById<View>(R.id.loading)
    val mEndView = binding.root.findViewById<View>(R.id.end)
    val mErrorView = binding.root.findViewById<View>(R.id.error)

    fun setStatu(statu:Int){
        when(statu){
            0->{
                mLoadingView.visibility = View.VISIBLE
                mEndView.visibility = View.GONE
                mErrorView.visibility = View.GONE
            }
            1->{
                mLoadingView.visibility = View.GONE
                mEndView.visibility = View.VISIBLE
                mErrorView.visibility = View.GONE
            }
            2->{
                mLoadingView.visibility = View.GONE
                mEndView.visibility = View.GONE
                mErrorView.visibility = View.VISIBLE
            }
            4->{
                mLoadingView.visibility = View.GONE
                mEndView.visibility = View.GONE
                mErrorView.visibility = View.GONE
            }
        }
    }

}