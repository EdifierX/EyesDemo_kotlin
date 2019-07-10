package com.edisonxian.mytestkotlin01.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * Created by edison XianYongChou
 * on 2019-07-02
 */
object NetworkUtils {

    fun isNetConnected(context: Context): Boolean {
        val connectManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectManager.activeNetworkInfo
        if (networkInfo == null) {
            return false
        } else {
            return networkInfo.isAvailable && networkInfo.isConnected
        }
    }
}