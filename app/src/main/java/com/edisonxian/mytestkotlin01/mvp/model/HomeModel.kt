package com.edisonxian.mytestkotlin01.mvp.model

import android.content.Context
import com.edisonxian.mytestkotlin01.network.ApiService
import com.edisonxian.mytestkotlin01.network.RetrofitClient
import com.edisonxian.mytestkotlin01.pojo.HomeBean
import io.reactivex.Observable

/**
 * Created by edison XianYongChou
 * on 2019-06-29
 */
class HomeModel {
    fun loadData(context: Context, isFirst: Boolean, data: String?): Observable<HomeBean>? {
        var retrofitClient = RetrofitClient.getInstance(context, ApiService.BASE_URL)
        var apiService = retrofitClient.create(ApiService::class.java)
        when (isFirst) {
            true -> return apiService?.getHomeData()

            false -> return apiService?.getHomeMoreData(data.toString(), "2")
        }
    }
}