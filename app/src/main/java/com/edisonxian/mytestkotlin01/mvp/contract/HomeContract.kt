package com.edisonxian.mytestkotlin01.mvp.contract

import com.edisonxian.mytestkotlin01.base.BasePresenter
import com.edisonxian.mytestkotlin01.base.BaseView
import com.edisonxian.mytestkotlin01.pojo.HomeBean

/**
 * Created by edison XianYongChou
 * on 2019-06-29
 */
interface HomeContract {
    interface View : BaseView<Presenter> {
        fun setData(bean: HomeBean)
    }

    interface Presenter : BasePresenter {
        fun requestData()
    }
}