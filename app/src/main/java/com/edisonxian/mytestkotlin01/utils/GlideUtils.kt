package com.edisonxian.mytestkotlin01.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.edisonxian.mytestkotlin01.R

/**
 * Created by edison XianYongChou
 * on 2019-06-28
 */
class GlideUtils {
    companion object {
        fun display(context: Context, imageView: ImageView?, url: String) {
            if (imageView == null) {
                throw IllegalAccessException("argument error")
            }
            Glide.with(context).load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_image_loading)
                .error(R.mipmap.ic_empty_picture)
                .crossFade()
//                .transition(DrawableTransitionOptions.withCrossFade())//加载图片过渡
                .into(imageView)
        }

        fun displayHigh(context: Context, imageView: ImageView?, url: String) {
            if (imageView == null) {
                throw IllegalAccessException("argument error")
            }
            Glide.with(context).load(url)
                .asBitmap()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .placeholder(R.mipmap.ic_image_loading)
                .error(R.mipmap.ic_empty_picture)
                .into(imageView)
        }
    }
}