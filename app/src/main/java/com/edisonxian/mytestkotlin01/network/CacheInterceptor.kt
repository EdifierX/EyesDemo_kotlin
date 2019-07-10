package com.edisonxian.mytestkotlin01.network

import android.content.Context
import android.util.Log
import com.edisonxian.mytestkotlin01.utils.NetworkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by edison XianYongChou
 * on 2019-07-02
 */
class CacheInterceptor(context: Context) : Interceptor {
    var mContext = context

    override fun intercept(chain: Interceptor.Chain?): Response? {
        var request = chain?.request()
        if (NetworkUtils.isNetConnected(mContext)) {
            val response = chain?.proceed(request)
            val maxAge = 60
            val cacheControl = request?.cacheControl().toString()
            Log.e("CacheInterceptor", "6s load cahe" + cacheControl)
            return response?.newBuilder()?.removeHeader("Pragma")?.removeHeader("Cache-Control")
                ?.header("Cache-Control", "public, max-age=" + maxAge)?.build()
        } else {
            Log.e("CacheInterceptor", " no network load cahe")
            request = request?.newBuilder()?.cacheControl(CacheControl.FORCE_CACHE)?.build()
            val response = chain?.proceed(request)
            //set cahe times is 3 days
            val maxStale = 60 * 60 * 24 * 3
            return response?.newBuilder()?.removeHeader("Pragma")?.removeHeader("Cache-Control")
                ?.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)?.build()
        }
    }
}