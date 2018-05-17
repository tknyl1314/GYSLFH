package com.titan.base

import android.databinding.BaseObservable
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

/**
 * Created by hanyw on 2018/3/12.
 * 带上拉加载功能的适配器
 */
abstract class BaseRefreshAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    //footer的状态 0加载中，1加载完成，2加载出错
    var statu: Int? = 0

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent!!.context), getLayoutIdForPosition(), parent, false)
            BaseViewHolder(binding)
        } else {
            val binding: ViewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent!!.context), getLayoutIdForFooter(), parent, false)
            FooterHolder(binding)
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position + 1 != itemCount) {
            val viewmodel = getLayoutViewModel(position)
            (holder as BaseViewHolder).bind(viewmodel as BaseObservable)
        } else {
            val footerHolder = holder as FooterHolder
            footerHolder.setStatu(statu!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position + 1 == itemCount) {
            1
        } else {
            0
        }
    }

    protected abstract fun getLayoutIdForPosition(): Int

    protected abstract fun getLayoutViewModel(position: Int): Any

    /**
     * 设置footer的布局id
     */
    protected abstract fun getLayoutIdForFooter(): Int

    /**
     * 设置footer的状态 0加载中，1加载完成，2加载出错
     */
    fun setStatu(statu: Int) {
        this.statu = statu
    }

}