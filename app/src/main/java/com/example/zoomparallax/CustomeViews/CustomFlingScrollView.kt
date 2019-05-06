package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent

open class CustomFlingScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    override fun fling(velocityY: Int) {
        super.fling(velocityY/8)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                System.out.println("ParentView----dispatchTouchEvent----MotionEvent.ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                //requestDisallowInterceptTouchEvent(true)
                System.out.println("ParentView----dispatchTouchEvent----MotionEvent.ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                System.out.println("ParentView----dispatchTouchEvent----MotionEvent.ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                System.out.println("ParentView----dispatchTouchEvent----MotionEvent.ACTION_CANCEL")
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                System.out.println("ParentView----onInterceptTouchEvent----MotionEvent.ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                //requestDisallowInterceptTouchEvent(true)
                System.out.println("ParentView----onInterceptTouchEvent----MotionEvent.ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                System.out.println("ParentView----onInterceptTouchEvent----MotionEvent.ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                System.out.println("ParentView----onInterceptTouchEvent----MotionEvent.ACTION_CANCEL")
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                System.out.println("ParentView----onTouchEvent----MotionEvent.ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                //requestDisallowInterceptTouchEvent(true)
                System.out.println("ParentView----onTouchEvent----MotionEvent.ACTION_MOVE")
            }
            MotionEvent.ACTION_UP -> {
                System.out.println("ParentView----onTouchEvent----MotionEvent.ACTION_UP")
            }
            MotionEvent.ACTION_CANCEL -> {
                System.out.println("ParentView----onTouchEvent----MotionEvent.ACTION_CANCEL")
            }
        }
        return super.onTouchEvent(ev)
    }
}