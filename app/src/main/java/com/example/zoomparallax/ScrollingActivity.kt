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
import android.view.*
import android.widget.*

import com.example.zoomparallax.Adapters.RecyclerAdapter
import com.example.zoomparallax.CustomeViews.CustomFlingScrollView


import com.example.zoomparallax.CustomeViews.FLexibleLayout
import kotlinx.android.synthetic.main.content_scrolling.*
import org.w3c.dom.Text

//CollapsingToolbarLayout: the folding header
//AppbarLayout: to handle the Toolbar movements(Scrolling header gestures)

open class ScrollingActivity : AppCompatActivity() {

    private lateinit var header : View
    private lateinit var mFLexibleLayout : FLexibleLayout
    private lateinit var toolbar : android.support.v7.widget.Toolbar
    private lateinit var mCollapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var appbatlayout : AppBarLayout
    private lateinit var customFlingScrollView: CustomFlingScrollView
    private lateinit var content : FrameLayout
    private lateinit var header_btns : FrameLayout
    private lateinit var headerbarLayout : View
    private lateinit var btn_info : Button
    private lateinit var btn_add : Button
    private lateinit var btn_play : Button
    private lateinit var btn_list : Button
    private lateinit var title : TextView
    private lateinit var name_cinema : TextView
    private var Height : Float = 0f


    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY)

        val photos:ArrayList<Int> = ArrayList()
        val names:ArrayList<String> = ArrayList()

        for (i in 1..10){
            photos.add(R.drawable.avengers)
        }
        for (i in 1..10){
            names.add("Avengers : Endgame")
        }
        setContentView(R.layout.activity_scrolling)

        if (Build.VERSION.SDK_INT >= 21){
            val decorView : View = window.decorView
            val option : Int = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION ; View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN ; View.SYSTEM_UI_FLAG_LAYOUT_STABLE ; View.SYSTEM_UI_FLAG_FULLSCREEN ; View.SYSTEM_UI_FLAG_LAYOUT_STABLE ; View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            decorView.systemUiVisibility = option
            window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT

        }
        initView()

        val manager = LinearLayoutManager(this)
        show1.layoutManager = LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)
        show1.adapter = RecyclerAdapter(photos,names)
        show2.layoutManager = LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)
        show2.adapter = RecyclerAdapter(photos,names)

        mFLexibleLayout.setHeader(header)
        mFLexibleLayout.setTitle(title)
        mFLexibleLayout.setContent(content,header_btns)


        btn_play.setOnClickListener { System.out.println("play") }
        btn_info.setOnClickListener { System.out.println("info") }
        btn_add.setOnClickListener { System.out.println("add") }
        btn_list.setOnClickListener { System.out.println("list") }


        System.out.println("header height now is : ${mesureHeight(header)},need to show title")
        //getScrollChange(header)





//----------------------------------  postDelayed(Runnable,long)  --------------------------------------

        val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {

                if (header.y-Height>0){
                    if (header.y - Height < 200){
                        System.out.println("${header.y - Height}")
                        title.alpha = (header.y)/200
                    }
                    if (header.y - Height > 200){
                        title.alpha = 1f
                    }

                }
                if (header.y-Height == 0f){
                    title.alpha = 0f
                }
                handler.postDelayed(this, 1)
            }
        }
        handler.postDelayed(runnable,1)
//----------------------------------  postDelayed(Runnable,long)  --------------------------------------



    }

    fun initView (){
        mFLexibleLayout = findViewById(R.id.fv)
        header = findViewById(R.id.iv)
        //coordinatorLayout = findViewById(R.id.coor_layout)
        appbatlayout = findViewById(R.id.appbarlayout)
        header_btns = findViewById(R.id.header_btns)
        customFlingScrollView = findViewById(R.id.nestedview)
        content = findViewById(R.id.content)
        toolbar = findViewById(R.id.toolbar)
        mCollapsingToolbarLayout = findViewById(R.id.collapsing_layout)
        name_cinema = findViewById(R.id.name_cinema)
        btn_add = findViewById(R.id.add)
        btn_info = findViewById(R.id.info)
        btn_play = findViewById(R.id.play)
        btn_list = findViewById(R.id.list)
        title = findViewById(R.id.title)
        setSupportActionBar(toolbar)

        btn_list.background.alpha = 100
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener{
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxx")
        }

        val titleheight : Int = title.layoutParams.height
        System.out.println("the title height is: ${getdp(titleheight.toFloat())}")
        System.out.println("the status_bar height is: ${getdp(getStatusBarHeight())}")
        title.layoutParams.height = (titleheight+getStatusBarHeight()).toInt()
    }
//-----------------------  viewTreeObserver used for listening actions of views  -----------------------
    fun mesureHeight(header : View){
        header.viewTreeObserver.addOnGlobalLayoutListener {
            //header.viewTreeObserver.removeOnGlobalLayoutListener(this)
            Height = header.y
        }
    }

    fun getScrollChange(header: View){
        btn_add.viewTreeObserver.addOnScrollChangedListener{
            val height : Float = header.y
            System.out.println("header height is $height")
        }
    }
//----------------------  viewTreeObserver used for listening actions of views  ------------------------

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

