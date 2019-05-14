package com.example.zoomparallax

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.Nullable
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.Toolbar
import android.view.*
import android.widget.*

import com.example.zoomparallax.Adapters.RecyclerAdapter
import com.example.zoomparallax.CustomeViews.CustomFlingScrollView


import com.example.zoomparallax.CustomeViews.FLexibleLayout
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*
import org.w3c.dom.Text
import android.os.CountDownTimer
import com.example.zoomparallax.CustomeViews.CustomeScrollView
import com.example.zoomparallax.CustomeViews.DisInterceptNestedScrollView
import kotlinx.android.synthetic.main.behaviorcontent.*




//CollapsingToolbarLayout: the folding header
//AppbarLayout: to handle the Toolbar movements(Scrolling header gestures)

open class ScrollingActivity : AppCompatActivity() {

    private lateinit var appBarLayout: AppBarLayout
    private lateinit var toolbar: Toolbar
    private lateinit var title : View
    private lateinit var titlebackup : View
    private lateinit var coordinatorLayout: CoordinatorLayout
    private lateinit var behavior_content : DisInterceptNestedScrollView

    private var status = false

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        super.onCreate(savedInstanceState)

        val photos: ArrayList<Int> = ArrayList()
        val names: ArrayList<String> = ArrayList()

        for (i in 1..10) {
            photos.add(R.drawable.avengers)
        }
        for (i in 1..10) {
            names.add("Avengers : Endgame")
        }
        setContentView(R.layout.activity_scrolling)


        val manager = LinearLayoutManager(this)

        show1.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        show1.adapter = RecyclerAdapter(photos, names)

        show2.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
        show2.adapter = RecyclerAdapter(photos, names)

        initView()
        initListener()





//        object : CountDownTimer(10000*1000, 1) {
//
//            override fun onTick(millisUntilFinished: Long) {
//                val range = appBarLayout.totalScrollRange
//                coordinatorLayout.scrollTo(0, (10000*1000 - millisUntilFinished).toInt()/10) // from zero to 2000
//            }
//
//            override fun onFinish() {}
//
//        }.start()


    }


    fun initListener(){

        val index = 6
        var percent = 0f


//================= listener for title alpha ====================
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            percent = Math.abs(verticalOffset).toFloat() / (appBarLayout.totalScrollRange.toFloat()/index)

            if (percent > 1f){
                title.alpha = 1f
                status = false
            }else{
                title.alpha = percent
            }
            if (percent == index.toFloat()){
                status = true
            }
        })

        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (status){
                    titlebackup.alpha = 1f

                }else if (percent<1f){
                    titlebackup.alpha = 0f
                }
                handler.postDelayed(this, 1)
            }
        }
        handler.postDelayed(runnable,1)

//================= listener for title alpha ====================

    }

    fun initView (){
        appBarLayout = findViewById(R.id.appbarlayout)
        toolbar = findViewById(R.id.toolbar)
        title = findViewById(R.id.title)
        coordinatorLayout = findViewById(R.id.coor_layout)
        behavior_content = findViewById(R.id.content)
        titlebackup = findViewById(R.id.title_backup)

        val titleheight : Int = title.layoutParams.height
        val content_paddingTop = behavior_content.paddingTop
//        System.out.println("the title height is: ${getdp(titleheight.toFloat())}")
//        System.out.println("the status_bar height is: ${getdp(getStatusBarHeight())}")
        title.layoutParams.height = (titleheight+getStatusBarHeight()).toInt()
        titlebackup.layoutParams.height = (titleheight+getStatusBarHeight()).toInt()
        behavior_content.setPadding(0, (content_paddingTop + getStatusBarHeight()).toInt(),0,0)
    }

    //-----------------------  get status bar height and add to title block height  ------------------------

    fun getStatusBarHeight():Float{
        var height = 0
        val resourceID : Int = applicationContext.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceID > 0){
            height = applicationContext.resources.getDimensionPixelSize(resourceID)
        }
        return height.toFloat()
    }

    fun getdp( pixel : Float):Float{
        val density : Float = applicationContext.resources.displayMetrics.density
        return pixel/density
    }
//-----------------------  get status bar height and add to title block height  ------------------------

}

