package com.titan.base

/**
 * Created by whs on 2018/1/30
 */
open class ItemViewModel() : BaseViewModel() {
    constructor(item: Any,itemview: ItemView) : this()
    constructor(item: Any) : this()

    fun onItemClick(){
        //itemview.onItemClick()
    }
}