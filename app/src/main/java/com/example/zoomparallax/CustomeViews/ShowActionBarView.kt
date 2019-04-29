package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ScrollView

open class ShowActionBarView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {
    private lateinit var fadingView : View
    private lateinit var fadingHeightView : View
    private var oldY : Int = 0
    private var fadingHeight : Int = 500



    fun setFadingView(view:View){ this.fadingView = view }
    fun setFadingHeightView(view: View){this.fadingHeightView = view}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (fadingHeightView != null){
            fadingHeight = fadingHeightView.measuredHeight
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        //oldl:x postion before moving ; oldt:y postion before moving
        //l:x position after moving ; t:y position after moving
        val fading : Float = if (t > fadingHeight) fadingHeight.toFloat() else if (t > 30) t.toFloat() else 0f
        updateActionBarAlpha(fading/fadingHeight)
        System.out.println("scrolling")
    }

    fun updateActionBarAlpha(alpha: Float){
        try{
            setActionBar(alpha)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    open fun setActionBar(alpha:Float){
        fadingView.alpha = alpha
    }
}