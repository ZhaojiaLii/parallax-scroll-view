package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.OverScroller

open class CustomeScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {

    private lateinit var content : View
    private lateinit var appBarLayout: AppBarLayout
    private var contentInitPosition = 0f

    open fun getContent(content : View,appBarLayout: AppBarLayout) {
        this.content = content
        this.appBarLayout = appBarLayout
        content.post {
            run {

                contentInitPosition = content.y
                System.out.println("content Y is $contentInitPosition")
            }
        }
    }

    private fun stopAppbarLayoutFling(appBarLayout: AppBarLayout) {
        //通过反射拿到HeaderBehavior中的flingRunnable变量
        try {
            val headerBehaviorType = this.javaClass.superclass!!.superclass
            val flingRunnableField = headerBehaviorType!!.getDeclaredField("mFlingRunnable")
            val scrollerField = headerBehaviorType.getDeclaredField("mScroller")
            flingRunnableField.isAccessible = true
            scrollerField.isAccessible = true

            val flingRunnable = flingRunnableField.get(this) as Runnable
            val overScroller = scrollerField.get(this) as OverScroller
            if (flingRunnable != null) {
                appBarLayout.removeCallbacks(flingRunnable)
                flingRunnableField.set(this, null)
            }
            if (overScroller != null && !overScroller.isFinished) {
                overScroller.abortAnimation()
            }
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }



    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                //content.requestFocus()
                //val contentY = content.y
                //System.out.println("content Y is $contentY")
//                if (contentY != contentInitPosition){
//                    return super.dispatchTouchEvent(ev)
//                }
                //stopAppbarLayoutFling(appBarLayout)

            }
            MotionEvent.ACTION_MOVE -> {
                //requestDisallowInterceptTouchEvent(true)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
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
        parent.requestDisallowInterceptTouchEvent(true)
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                System.out.println("ParentView----onTouchEvent----MotionEvent.ACTION_DOWN")
            }
            MotionEvent.ACTION_MOVE -> {
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