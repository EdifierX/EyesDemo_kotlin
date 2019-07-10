package com.edisonxian.mytestkotlin01

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.edisonxian.mytestkotlin01.ui.Fragment.*
import com.gyf.barlibrary.ImmersionBar
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    val LOG = "TestKotlin"

    var mHomeFragment: HomeFragment? = null
    var mDiscoverFragment: DiscoverFragment? = null
    var mHotFragment: HotFragment? = null
    var mMineFragment: MineFragment? = null
    lateinit var mSearchFragment: SearchFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ImmersionBar.with(this).transparentBar()
            .barAlpha(0.3f).fitsSystemWindows(true).init()

        //隐藏底部导航栏 列如:小米8
        val params = window.attributes
        params.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        window.attributes = params

        setRadioButton()
        initToolbar()
        initFragment(savedInstanceState)
    }

    private fun initToolbar() {
        var today = getToday()
        tv_bar_title.text = today
        //TODO:增加字体样式
//        tv_bar_title.typeface = Typeface.createFromAsset()
        iv_search.setOnClickListener {
            if (rb_mine.isChecked) {

            } else {
                mSearchFragment = SearchFragment()
                mSearchFragment.show(supportFragmentManager, SEARCH_TAG)
            }
        }
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        val mFragments: List<Fragment> = supportFragmentManager.fragments
        if (savedInstanceState != null) {
            //异常处理
            for (item in mFragments) {
                if (item is HomeFragment) {
                    mHomeFragment = item
                }
                if (item is DiscoverFragment) {
                    mDiscoverFragment = item
                }
                if (item is HotFragment) {
                    mHotFragment = item
                }
                if (item is MineFragment) {
                    mMineFragment = item
                }
            }
        } else {
            //初始化Fragment
            mHomeFragment = HomeFragment()
            mDiscoverFragment = DiscoverFragment()
//            mHotFragment = HotFragment()
//            mMineFragment = MineFragment()
            var fragmentTrans = supportFragmentManager.beginTransaction()
            fragmentTrans.add(R.id.fl_content, mHomeFragment!!)
            fragmentTrans.add(R.id.fl_content, mDiscoverFragment!!)
//            fragmentTrans.add(R.id.fl_content, mHotFragment!!)
//            fragmentTrans.add(R.id.fl_content, mMineFragment!!)
            fragmentTrans.commit()

            supportFragmentManager.beginTransaction().show(mHomeFragment!!)
                .hide(mDiscoverFragment!!)
//                    .hide(mHotFragment!!)
//                    .hide(mMineFragment!!)
                .commit()
        }
    }

    private fun getToday(): String {
        var list = arrayOf(
            "Sunday", "Monday", "Tuesday", "Wednesday"
            , "Thursday", "Friday", "Saturday"
        )
        var data: Date = Date()
        var calendar: Calendar = Calendar.getInstance()
        calendar.time = data
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0)
            index = 0
        else if (index >= list.size)
            index = 6
        return list[index]
    }

    /**
     * 设置RadioButton
     */
    private fun setRadioButton() {
        rb_home.isChecked = true
        rb_home.setTextColor(resources.getColor(R.color.black))
        rb_home.setOnClickListener(this)
        rb_discover.setOnClickListener(this)
        rb_hot.setOnClickListener(this)
        rb_mine.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        clearButtonState()
        when (v?.id) {
            R.id.rb_home -> {
                rb_home.isChecked = true
                rb_home.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(mHomeFragment!!)
                    .hide(mDiscoverFragment!!)
//                    .hide(mHotFragment!!)
//                    .hide(mMineFragment!!)
                    .commit()
                tv_bar_title.text = getToday()
                tv_bar_title.visibility = View.VISIBLE
                //TODO:
//                iv_search.setImageResource(R.drawable.icon_search)
            }
            R.id.rb_discover -> {
                rb_discover.isChecked = true
                rb_discover.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(mDiscoverFragment!!)
                    .hide(mHomeFragment!!)
//                    .hide(mHotFragment!!)
//                    .hide(mMineFragment!!)
                    .commit()
                tv_bar_title.text = "Discover"
                tv_bar_title.visibility = View.VISIBLE
                //TODO:
//                iv_search.setImageResource(R.drawable.icon_search)
            }

            R.id.rb_hot -> {
                rb_hot.isChecked = true
                rb_hot.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(mHotFragment!!)
                    .hide(mHomeFragment!!)
                    .hide(mDiscoverFragment!!)
                    .hide(mMineFragment!!)
                    .commit()
                tv_bar_title.text = "Ranking"
                tv_bar_title.visibility = View.VISIBLE
                //TODO:
//                iv_search.setImageResource(R.drawable.icon_search)
            }

            R.id.rb_mine -> {
                rb_mine.isChecked = true
                rb_mine.setTextColor(resources.getColor(R.color.black))
                supportFragmentManager.beginTransaction().show(mMineFragment!!)
                    .hide(mHomeFragment!!)
                    .hide(mDiscoverFragment!!)
                    .hide(mHotFragment!!)
                    .commit()
                tv_bar_title.visibility = View.INVISIBLE
                //TODO:
//                iv_search.setImageResource(R.drawable.icon_setting)
            }
        }
    }

    private fun clearButtonState() {
        rg_root.clearCheck()
        rb_home.setTextColor(resources.getColor(R.color.gray))
        rb_discover.setTextColor(resources.getColor(R.color.gray))
        rb_hot.setTextColor(resources.getColor(R.color.gray))
        rb_mine.setTextColor(resources.getColor(R.color.gray))
    }

}
