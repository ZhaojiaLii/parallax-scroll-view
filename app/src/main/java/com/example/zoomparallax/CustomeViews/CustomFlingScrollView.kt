package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent

open class CustomFlingScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private var initX = 0f
    private var initY = 0f
    private var dragX = 0f
    private var dragY = 0f


    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                initX = ev.x
                initY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                //requestDisallowInterceptTouchEvent(true)


            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                System.out.println("ParentView----onInterceptTouchEvent----MotionEvent.ACTION_DOWN")
                initX = ev.x
                initY = ev.y
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
        parent.requestDisallowInterceptTouchEvent(true)
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                initX = ev.x
                initY = ev.y
                System.out.println("ParentView----onTouchEvent----MotionEvent.ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
                dragX = ev.x - initX
                dragY = ev.y - initY
                System.out.println("$dragY")

                if (dragY > 0){
                    System.out.println("from recyclerview drag > 0")
                    System.out.println("下拉需要放大")
                    //parent.requestDisallowInterceptTouchEvent(true)

                }
                if (dragY < 0){
                    System.out.println("from recyclerview drag < 0")
                    System.out.println("上拉不用拦截")
                    //parent.requestDisallowInterceptTouchEvent(true)

                }

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