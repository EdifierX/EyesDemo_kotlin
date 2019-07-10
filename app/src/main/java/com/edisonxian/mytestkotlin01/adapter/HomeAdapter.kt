package com.edisonxian.mytestkotlin01.adapter

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.edisonxian.mytestkotlin01.R
import com.edisonxian.mytestkotlin01.pojo.HomeBean
import com.edisonxian.mytestkotlin01.pojo.VideoBean
import com.edisonxian.mytestkotlin01.ui.VideoDetailActivity
import com.edisonxian.mytestkotlin01.utils.GlideUtils
import com.tt.lvruheng.eyepetizer.utils.ObjectSaveUtils
import com.tt.lvruheng.eyepetizer.utils.SPUtils

/**
 * Created by edison XianYongChou
 * on 2019-06-25
 */
class HomeAdapter(context: Context) : RecyclerView.Adapter<HomeAdapter.MyViewHolder>() {

    var inflater: LayoutInflater? = null
    var mData: MutableList<HomeBean.IssueListBean.ItemListBean>? = null
    var mContext: Context? = null

    init {
        this.mContext = context
        this.inflater = LayoutInflater.from(context)
        mData = ArrayList()
    }

    fun setData(data: MutableList<HomeBean.IssueListBean.ItemListBean>?) {
        if (data != null && data.size > 0) {
            mData?.clear()
            mData?.addAll(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAdapter.MyViewHolder {
        return MyViewHolder((inflater?.inflate(R.layout.item_home, parent, false))!!)
    }

    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: HomeAdapter.MyViewHolder, position: Int) {
        var bean = mData?.get(position)
        var title = bean?.data?.title
        var category = bean?.data?.category
        var minute = bean?.data?.duration?.div(60)
        var second = bean?.data?.duration?.minus((minute?.times(60)) as Long)
        var realMinute: String
        var realSecond: String
        if (minute!! < 10) {
            realMinute = "0" + minute
        } else {
            realMinute = minute.toString()
        }
        if (second!! < 10) {
            realSecond = "0" + second
        } else {
            realSecond = second.toString()
        }
        var playUrl = bean?.data?.playUrl
        var photo = bean?.data?.cover?.feed
        var author = bean?.data?.author
        GlideUtils.display(mContext!!, viewHolder?.iv_photo, photo as String)
        viewHolder?.tv_title?.text = title
        viewHolder?.tv_detail?.text = "发布于 $category / $realMinute:$realSecond"
        if (author != null) {
            GlideUtils.display(mContext!!, viewHolder?.iv_user, author.icon as String)
        } else {
            viewHolder?.iv_user?.visibility = View.GONE
        }
        viewHolder?.itemView?.setOnClickListener {
            //跳转视频详情页
            var intent: Intent = Intent(mContext, VideoDetailActivity::class.java)
            var desc = bean?.data?.description
            var duration = bean?.data?.duration
            var playUrl = bean?.data?.playUrl
            var blurred = bean?.data?.cover?.blurred
            var collect = bean?.data?.consumption?.collectionCount
            var share = bean?.data?.consumption?.shareCount
            var reply = bean?.data?.consumption?.replyCount
            var time = System.currentTimeMillis()
            var videoBean =
                VideoBean(photo, title, desc, duration, playUrl, category, blurred, collect, share, reply, time)
            var url = SPUtils.getInstance(mContext!!, "beans").getString(playUrl!!)
            if (url.equals("")) {
                var count = SPUtils.getInstance(mContext!!, "beans").getInt("count")
                if (count != -1) {
                    count = count.inc()//count.inc()相等于count++
                } else {
                    count = 1
                }
                SPUtils.getInstance(mContext!!, "beans").put("count", count)
                SPUtils.getInstance(mContext!!, "beans").put(playUrl, playUrl)
                ObjectSaveUtils.saveObject(mContext!!, "bean$count", videoBean)
            }
            intent.putExtra(VideoDetailActivity.VIDEO_BEAN_KEY, videoBean as Parcelable)
            mContext?.let { context -> context.startActivity(intent) }
        }

    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_detail: TextView? = null
        var tv_title: TextView? = null
        var tv_time: TextView? = null
        var iv_photo: ImageView? = null
        var iv_user: ImageView? = null

        init {
            tv_detail = itemView?.findViewById(R.id.tv_detail) as TextView?
            tv_title = itemView?.findViewById(R.id.tv_title) as TextView?
            iv_photo = itemView?.findViewById(R.id.iv_photo) as ImageView?
            iv_user = itemView?.findViewById(R.id.iv_user) as ImageView?
        }
    }

}