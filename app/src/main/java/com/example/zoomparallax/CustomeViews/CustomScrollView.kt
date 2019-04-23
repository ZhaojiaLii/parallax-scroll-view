package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.support.animation.SpringAnimation
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import kotlinx.android.synthetic.main.content_scrolling.*

class CustomScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    var springAnimation : SpringAnimation? = null
    private var startDragY: Float = 0F

    init {
        springAnimation = SpringAnimation(this, SpringAnimation.TRANSLATION_Y, 0f)
        springAnimation!!.spring.stiffness = 800.0F
        springAnimation!!.spring.dampingRatio = 1.0F
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_MOVE ->{
                if (scrollY<=0){  //pull down from top
                    if (startDragY == 0F){
                        startDragY = ev.rawY
                        Log.d("Tag",startDragY.toString())
                    }
                    if (ev.rawY - startDragY > 0){
                        translationY=((ev.rawY-startDragY)/3)
                        Log.d("Tag",translationY.toString())
                        return true
                    }else{
                        springAnimation?.cancel()
                        translationY = 0F
                        Log.d("Tag","3")
                    }
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                Log.d("Tag","cancelled")
                if (translationY != 0F){
                    springAnimation?.start()
                }
                startDragY = 0F
            }
        }
        return super.onTouchEvent(ev)
    }
}