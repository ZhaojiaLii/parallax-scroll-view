package com.example.zoomparallax

import android.annotation.SuppressLint
import android.media.Image
import android.os.Build
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView

import com.example.zoomparallax.Adapters.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_scrolling.*
import kotlinx.android.synthetic.main.content_scrolling.*

import android.widget.Toolbar
import com.example.zoomparallax.CustomeViews.FlexibleLayout
import com.gavin.view.flexible.callback.OnReadyPullListener


class ScrollingActivity : AppCompatActivity() {

    var Scaling : Boolean = false
    lateinit var metric : DisplayMetrics
    private lateinit var header : ImageView
    private lateinit var rootView : FlexibleLayout
    private var mFirstPosition = 0f
    private var zoomViewWidth : Int = 0
    private var zoomViewHeight : Int = 0
    private var startDragY: Float = 0F
    private val ScaleRatio : Float = 0.4f


    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        super.onCreate(savedInstanceState)

        val photos:ArrayList<Int> = ArrayList()
        val names:ArrayList<String> = ArrayList()
        val header : View = findViewById(R.id.iv)

        for (i in 1..10){
            photos.add(R.drawable.shawshank)
        }
        for (i in 1..10){
            names.add("the Shawshank Redemption")
        }
        Log.d("TAG","Ready to load layout manager")

        setContentView(R.layout.activity_scrolling)


//        fab1.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val manager = LinearLayoutManager(this)
        show1.layoutManager = LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)
        show1.adapter = RecyclerAdapter(photos,names)
        show2.layoutManager = LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)
        show2.adapter = RecyclerAdapter(photos,names)

        val flexibleLayout:FlexibleLayout = findViewById(R.id.fv)
        flexibleLayout.setReadyListener(OnReadyPullListener { manager.findFirstCompletelyVisibleItemPosition() == 0 })
        flexibleLayout.setHeader(header)

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initView(){
        metric = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metric)

        //set original image size
//        val lp : ViewGroup.LayoutParams = topImage.layoutParams
//        lp.width = metric.widthPixels
//        lp.height = metric.widthPixels*9/16
//        topImage.layoutParams = lp

        app_bar.setOnTouchListener(object :View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event!!.action){
                    MotionEvent.ACTION_DOWN->{
                        System.out.println("action DOWN")
                    }
                    MotionEvent.ACTION_UP->{
                        System.out.println("action UP")
                    }
                    MotionEvent.ACTION_MOVE->{
                        System.out.println("action MOVE")
                    }
                    MotionEvent.ACTION_CANCEL->{
                        System.out.println("action CANCEL")
                    }
                }
                return v?.onTouchEvent(event)?:true
            }
        })
//        app_bar.setOnTouchListener{ _, event ->
//            val lp : ViewGroup.LayoutParams = topImage.layoutParams
//            val dy = event!!.rawY - startDragY
//            when(event.action){
//
//                MotionEvent.ACTION_UP ->{
//                    Log.d("Tag","action up")
//                    true
//                }
//                MotionEvent.ACTION_CANCEL->{
//                    Log.d("Tag","action cancel")
//                    true
//                }
//                MotionEvent.ACTION_DOWN ->{
//                    Log.d("Tag","action down")
//                    true
//                }
//                MotionEvent.ACTION_MOVE ->{
//                    Log.d("Tag","action move")
////                    if (!Scaling) {
////                        if (app_bar.scrollY <= 0) {
////                             mFirstPosition = event.y
////                        }
////                        if (dy > 0){
////                            val tempDy = (dy*ScaleRatio)
////                            Scaling = true
////                            setZoom(tempDy)
////                            return@setOnTouchListener true
////                        }
////                    }
////                    val distance : Int = ((event.y - mFirstPosition) * 0.6).toInt()
////                    if (distance < 0) {
////                    }
////                    Scaling = true
////                    lp.width = metric.widthPixels + distance
////                    lp.height = (metric.widthPixels + distance) * 9 / 16
////                    topImage.layoutParams = lp
//                    true
//
//                }
//                else -> {
//                    Log.d("Tag","else")
//                    true}
//            }
//
//
//        }

    }

    private fun setZoom(s:Float){
        var scaleTimes : Float = ((zoomViewWidth+s)/(zoomViewHeight*1.0)).toFloat()
        val layoutPara : ViewGroup.LayoutParams = app_bar.layoutParams
        layoutPara.width = (zoomViewWidth+s).toInt()
        layoutPara.height = (zoomViewHeight*((zoomViewWidth+s)/zoomViewWidth)).toInt()
        app_bar.layoutParams = layoutPara
    }


}

