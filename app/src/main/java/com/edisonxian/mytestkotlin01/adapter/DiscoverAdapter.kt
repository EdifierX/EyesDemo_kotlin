package com.edisonxian.mytestkotlin01.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by edison XianYongChou
 * on 2019-07-05
 */
class DiscoverAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var mData: List<DiscoverBean>? = null

    init {
        mContext = context
        mInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerView.ViewHolder {

    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
    }


    private class MyViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {

    }


}