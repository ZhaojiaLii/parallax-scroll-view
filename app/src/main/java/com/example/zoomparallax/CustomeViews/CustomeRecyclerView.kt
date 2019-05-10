package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import java.lang.Math.*

class CustomeRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private var mInitialY: Float = 0f
    private var mInitialX: Float = 0f
    private var status = false


    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        when(e!!.action){
            MotionEvent.ACTION_DOWN->{
                mInitialX = e.x
                mInitialY = e.y
                status = false

            }
            MotionEvent.ACTION_MOVE->{
                parent.requestDisallowInterceptTouchEvent(true)
                val diffY : Float = e.y - mInitialY
                val diffX : Float = e.x - mInitialX

                if (abs(diffX) > 3* abs(diffY)) {
                    System.out.println("$diffX abd $diffY in 横向 ==========")
                    parent.requestDisallowInterceptTouchEvent(true)
                    return false
                } else if (3*abs(diffX) < abs(diffY)) {
                    System.out.println("需要返回 ==========")
                    parent.requestDisallowInterceptTouchEvent(true)
                    status = true
                    return true
                }
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{}
        }
        return super.onInterceptTouchEvent(e)
    }

    override fun onTouchEvent(e: MotionEvent?): Boolean {

        when(e!!.action){
            MotionEvent.ACTION_DOWN -> {
                mInitialX = e.x
                mInitialY = e.y
                status = false
            }
            MotionEvent.ACTION_MOVE->{
                val diffY : Float = e.y - mInitialY
                val diffX : Float = e.x - mInitialX
                parent.requestDisallowInterceptTouchEvent(true)

                if (3*abs(diffX) < abs(diffY)){
                    System.out.println("$status ==========")
                    status = true
                    return false
                }
            }
            MotionEvent.ACTION_UP ->{status = false}
        }

        return super.onTouchEvent(e)
//        return if (status){
//            System.out.println("$status ==========")
//            false
//        }else{
//            System.out.println("$status ==========")
//            super.onTouchEvent(e)
//        }

    }
}