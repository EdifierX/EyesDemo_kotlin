package com.edisonxian.mytestkotlin01.utils

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

/**
 * Created by edison XianYongChou
 * on 2019-07-02
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}