package com.edisonxian.mytestkotlin01.ui

import android.content.Context
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.edisonxian.mytestkotlin01.R
import com.edisonxian.mytestkotlin01.listener.VideoListener
import com.edisonxian.mytestkotlin01.pojo.VideoBean
import com.edisonxian.mytestkotlin01.utils.GlideUtils
import com.shuyu.gsyvideoplayer.GSYVideoPlayer
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import kotlinx.android.synthetic.main.activity_video_detail.*
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.util.concurrent.ExecutionException

/**
 * Created by edison XianYongChou
 * on 2019-06-28
 */
class VideoDetailActivity : AppCompatActivity() {

    companion object {
        var VIDEO_BEAN_KEY = "video_bean"
        var MSG_IMAGE_LOADED = 101
    }

    var mContext: Context = this
    var isPlay: Boolean = false
    var isPause: Boolean = false
    lateinit var videoBean: VideoBean
    lateinit var mImageView: ImageView //播放器封面
    lateinit var orientationUtils: OrientationUtils

    private var mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when (msg?.what) {
                MSG_IMAGE_LOADED -> video_playr.setThumbImageView(mImageView)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)

        videoBean = intent.getParcelableExtra(VIDEO_BEAN_KEY)

        initView()
        initVideoPlayer()
    }

    override fun onResume() {
        super.onResume()
        isPause = false
    }

    override fun onPause() {
        super.onPause()
        isPause = true
    }

    override fun onDestroy() {
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.let { orientationUtils.releaseListener() }
    }

    private fun initView() {
        var bgUrl = videoBean.blurred
        //底部羽化背景
        bgUrl?.let { GlideUtils.displayHigh(this, iv_bottom_bg, bgUrl) }
        tv_video_desc.text = videoBean.description
        //TODO:字体格式
//        tv_video_desc.typeface = Typeface.createFromAsset()
        tv_video_title.text = videoBean.title
//        tv_video_title.typeface = Typeface.createFromAsset()

        var category = videoBean.category
        var duration = videoBean.duration
        var minute = duration?.div(60)
        var second = duration?.minus((minute?.times(60)) as Long)
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
        tv_video_time.text = "$category / $realMinute'$realSecond'"
        tv_video_favor.text = videoBean.collect.toString()
        tv_video_share.text = videoBean.share.toString()
        tv_video_reply.text = videoBean.reply.toString()


    }


    private fun initVideoPlayer() {
        var uri = intent.getStringExtra("localFile")
        if (uri == null) {
            //从列表进来
            video_playr.setUp(videoBean.playUrl, false, null, null)
        } else {
            //从缓存界面进来
            video_playr.setUp(uri, false, null, null)
        }

        //增加播放器封面
        mImageView = ImageView(this)
        mImageView.scaleType = ImageView.ScaleType.CENTER_CROP
        ImageViewAsyncTask(mHandler, this, mImageView).execute(videoBean.feed)
        video_playr.titleTextView.visibility = View.GONE
        video_playr.backButton.visibility = View.VISIBLE

        orientationUtils = OrientationUtils(this, video_playr)

        video_playr.isRotateViewAuto = false
        video_playr.isLockLand = false
        video_playr.isShowFullAnimation = false
        video_playr.isNeedLockFull = true
        video_playr.fullscreenButton.setOnClickListener {
            //直接横屏
            orientationUtils.resolveByClick()
            video_playr.startWindowFullscreen(mContext, true, true)
        }
        video_playr.setStandardVideoAllCallBack(object : VideoListener() {
            override fun onPrepared(url: String?, vararg objects: Any?) {
                super.onPrepared(url, *objects)
                //开始播放后才能旋转和全屏
                orientationUtils.isEnable = true
                isPlay = true
            }

            override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
                super.onQuitFullscreen(url, *objects)
                orientationUtils?.let { orientationUtils.backToProtVideo() }
            }
        })
        video_playr.setLockClickListener { view, lock ->
            orientationUtils.isEnable = !lock
        }

        video_playr.backButton.setOnClickListener {
            onBackPressed()
        }
    }


    private class ImageViewAsyncTask(
        private var handler: Handler,
        private var context: Context,
        private var iv: ImageView
    ) : AsyncTask<String, Void, String>() {

        private var mIs: FileInputStream? = null

        override fun doInBackground(vararg params: String): String? {
            var future = Glide.with(context)
                .load(params[0])
                .downloadOnly(100, 100)
            var path: String? = null
            try {
                val cacheFile = future.get()
                path = cacheFile.absolutePath
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }

            return path
        }

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            try {
                mIs = FileInputStream(result)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val bitmap = BitmapFactory.decodeStream(mIs)
            iv.setImageBitmap(bitmap)
            var message = handler.obtainMessage()
            message.what = MSG_IMAGE_LOADED
            handler.sendMessage(message)
        }


    }


    override fun onBackPressed() {
        orientationUtils?.let { orientationUtils.backToProtVideo() }
        if (StandardGSYVideoPlayer.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            if (newConfig?.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
                if (!video_playr.isIfCurrentIsFullscreen) {
                    video_playr.startWindowFullscreen(mContext, true, true)
                }
            } else {
                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
                if (video_playr.isIfCurrentIsFullscreen) {
                    StandardGSYVideoPlayer.backFromWindowFull(this);
                }
                orientationUtils?.let { orientationUtils.isEnable = true }
            }
        }
    }

}