package com.edisonxian.mytestkotlin01.mvp.presenter

import android.content.Context
import com.edisonxian.mytestkotlin01.mvp.contract.HomeContract
import com.edisonxian.mytestkotlin01.mvp.model.HomeModel
import com.edisonxian.mytestkotlin01.pojo.HomeBean
import com.edisonxian.mytestkotlin01.utils.applySchedulers
import io.reactivex.Observable

/**
 * Created by edison XianYongChou
 * on 2019-06-29
 */
class HomePresnter(context: Context?, view: HomeContract.View) : HomeContract.Presenter {

    var mContext: Context? = null
    var mView: HomeContract.View? = null
    val mModel: HomeModel by lazy {
        HomeModel()
    }

    init {
        mContext = context
        mView = view
    }

    override fun start() {
        requestData()
    }


    override fun requestData() {
        val observable: Observable<HomeBean>? = mContext?.let {
            mModel.loadData(it, true, "0")
        }
        observable?.applySchedulers()?.subscribe { homeBean: HomeBean ->
            mView?.setData(homeBean)
        }
    }

    fun moreData(data: String?) {
        val observable: Observable<HomeBean>? = mContext?.let {
            mModel.loadData(it, false, data)
        }

        observable?.applySchedulers()?.subscribe { homeBean: HomeBean ->
            mView?.setData(homeBean)
        }
    }


}