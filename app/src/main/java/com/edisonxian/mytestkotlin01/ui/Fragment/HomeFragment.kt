package com.edisonxian.mytestkotlin01.ui.Fragment

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.edisonxian.mytestkotlin01.R
import com.edisonxian.mytestkotlin01.adapter.HomeAdapter
import com.edisonxian.mytestkotlin01.mvp.contract.HomeContract
import com.edisonxian.mytestkotlin01.mvp.presenter.HomePresnter
import com.edisonxian.mytestkotlin01.pojo.HomeBean
import kotlinx.android.synthetic.main.fragment_home_layout.*
import java.util.ArrayList
import java.util.regex.Pattern

/**
 * Created by edison XianYongChou
 * on 2019-06-21
 */
class HomeFragment : BaseFragment(), HomeContract.View, SwipeRefreshLayout.OnRefreshListener {

    var mIsRefresh: Boolean = false
    var mAdapter: HomeAdapter? = null
    var mPresenter: HomePresnter? = null
    var mList = ArrayList<HomeBean.IssueListBean.ItemListBean>()
    var data: String? = null

    override fun initView() {
        mPresenter = HomePresnter(context, this)
        mPresenter?.start()
        recycler_view.layoutManager = LinearLayoutManager(context)
        mAdapter = HomeAdapter(context!!)
        recycler_view.adapter = mAdapter
        refresh_layout.setOnRefreshListener(this)
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                var linearLayout = recyclerView.layoutManager as LinearLayoutManager
                var lastPosition = linearLayout.findLastVisibleItemPosition()
                if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition == mList.size - 1) {
                    if (data != null) {
                        mPresenter?.moreData(data)
                    }
                }
            }
        })
    }

    override fun setData(bean: HomeBean) {
        val regEx = "[^0-9]"
        val p = Pattern.compile(regEx)
        val m = p.matcher(bean.nextPageUrl)
        data = m.replaceAll("").subSequence(1, m.replaceAll("").length - 1).toString()
        if (mIsRefresh) {
            mIsRefresh = false
            refresh_layout.isRefreshing = false
            if (mList.size > 0) {
                mList.clear()
            }
        }
        bean.issueList!!
            .flatMap { it.itemList!! }
            .filter { it.type.equals("video") }
            .forEach { mList.add(it) }
        mAdapter?.setData(mList)
        mAdapter?.notifyDataSetChanged()
    }

    override fun getLayoutResources(): Int {
        return R.layout.fragment_home_layout
    }


    override fun onRefresh() {
        if (!mIsRefresh) {
            mIsRefresh = true
            mPresenter?.start()
        }
    }

}

