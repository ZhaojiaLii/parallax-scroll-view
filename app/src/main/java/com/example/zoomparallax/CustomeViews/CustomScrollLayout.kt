package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.support.v4.view.NestedScrollingParent
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView


open class FLexibleLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr){

    private var mIsBeingDragged: Boolean = false
    private var mInitialY: Float = 0.toFloat()
    private var mInitialX: Float = 0.toFloat()
    private lateinit var headerView : View
    private var mHeaderHeight : Float = 0F
    private var mHeaderWidth : Float = 0F
    private var alreadyAdded : Int = 0
    private var getTop : Int = 0
    private var atTop : Int = 2
    private var temp : Float = 0F
    private var repeat_time : Int = 0
    private var positionArray = FloatArray(1000)
    private var direction : Int = 0  //0 is down, 1 is up
    private var Return : Int = 0
    private var isFirstMoveAfterIntercept : Boolean = true
    private lateinit var fadingView : View
    private lateinit var fadingHeightView : View
    private lateinit var titleView : View
    private var titleHeight : Float = 0f
    private var oldY : Int = 0
    private var fadingHeight : Int = 500
    var threshold : Float = 0f
    private lateinit var name_cenima : TextView
    private lateinit var liste : Button
    private var isFlinging = false


//    1）public boolean dispatchTouchEvent(MotionEvent ev)  这个方法用来分发TouchEvent
//    2）public boolean onInterceptTouchEvent(MotionEvent ev) 这个方法用来拦截TouchEvent
//    3）public boolean onTouchEvent(MotionEvent ev) 这个方法用来处理TouchEvent


    open fun setHeader(header : View,name:TextView,list:Button) {
        headerView = header
        name_cenima = name
        liste = list
        headerView.post {
            run {
                mHeaderHeight = headerView.height.toFloat()
                mHeaderWidth = headerView.width.toFloat()
                //System.out.println("the size of header is $mHeaderHeight * $mHeaderWidth")
                //handleFling()
            }
        }
    }
    open fun setTitle(title:View){
        this.titleView = title
        titleView.post{
            run{
                titleHeight = titleView.height.toFloat()
                titleView.alpha = 0f
            }
        }
    }
    open fun passSize(header: View):Float{
        headerView = header
        val location = IntArray(2)
        headerView.getLocationInWindow(location)
        val x = location[0]
        val y = location[1]
        return y.toFloat()
    }

    fun setFadingView(view:View){ this.fadingView = view }
    fun setFadingHeightView(view: View){this.fadingHeightView = view}

    private var i = 0

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN ->{
                isFirstMoveAfterIntercept = true
                headerView.scrollBy(0,0)
            }
            MotionEvent.ACTION_MOVE ->{
                isFirstMoveAfterIntercept = true
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                isFirstMoveAfterIntercept = false
            }
        }
        return super.dispatchTouchEvent(ev)

    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN -> {
                mInitialX = ev.x
                mInitialY = ev.y
                mIsBeingDragged = false
            }

            MotionEvent.ACTION_MOVE -> {
                // NaN : not a Number
                val diffX : Float = ev.x - mInitialX
                val diffY : Float = ev.y - mInitialY
                val location = IntArray(2)
                headerView.getLocationInWindow(location)
                val x = location[0]
                val y = location[1]

                if (alreadyAdded == 0){ getTop = runOneTime() }
                if (diffY > 0 && y==getTop){
                    direction = 1 // down
                    if (atTop == 0){ temp = ev.y - mInitialY }
                    mIsBeingDragged = true
                    atTop = 1
                    titleView.alpha = 0f
                    return true
                }else if (diffY > 0 && y != getTop){
                    direction = 1 // down
                    mIsBeingDragged = true
                    atTop = 0
                    isFirstMoveAfterIntercept = false
                    threshold = headerView.y-getTop
                    if (threshold<20&&threshold>0){ titleView.alpha = threshold/20 }
                    if (threshold >= 20 ){ titleView.alpha = 1f }
                    if (threshold <= 0 ){ titleView.alpha = 0f }
                    //System.out.println(diffY)
                }
                if (diffY < 0){
                    direction = 0 // up
                    isFirstMoveAfterIntercept = false
                    threshold = headerView.y-getTop
                    if (threshold>0&&threshold<20){ titleView.alpha = threshold/20 }
                    if (threshold >= 20 ){ titleView.alpha = 1f }
                    if (threshold <= 0 ){ titleView.alpha = 0f }
//                    System.out.println(threshold)
//                    System.out.println(titleView.alpha)
                }
            }
            MotionEvent.ACTION_UP ->{
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_MOVE -> {
                if (mIsBeingDragged){
                    val diffY = event.y - mInitialY
                    val location = IntArray(2)
                    headerView.getLocationInWindow(location)
                    val x = location[0]
                    val y = location[1]
                    if (atTop == 1){ changeHeader(diffY-temp) }
                    if (headerView.layoutParams.width.toFloat() == mHeaderWidth){
                        Return = 1
                        if (isFirstMoveAfterIntercept){
                            val ev = MotionEvent.obtain(event)
                            ev.action = MotionEvent.ACTION_DOWN
                            dispatchTouchEvent(ev)
                            isFirstMoveAfterIntercept = false
                        }
                        dispatchTouchEvent(event)
                        return false   // return to dispatchTouchEvent from here
                    }
                    if (headerView.layoutParams.width.toFloat() != mHeaderWidth){
                        Return = 0
                        isFirstMoveAfterIntercept = true
                    }

                }
            }
            MotionEvent.ACTION_UP ->{
                if (mIsBeingDragged){
                    resetHeader()
                    //System.out.println("header back to top")
                    temp = 0f
                    i = 0
                    Return = 0
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }



    fun changeHeader(offsetY : Float){
        val pullOffset : Int = Math.pow(offsetY.toDouble(),0.8).toInt()
        val newHeight : Float = pullOffset + mHeaderHeight
        val newWidth : Float = (newHeight/mHeaderHeight)*mHeaderWidth
        headerView.layoutParams.height = newHeight.toInt()
        headerView.layoutParams.width = newWidth.toInt()
        val margin : Float = (newWidth - mHeaderWidth)/2
        headerView.translationX = -margin
        headerView.requestLayout()
//        val top = name_cenima.y
//        name_cenima.y = top + offsetY
    }

    fun resetHeader(){
        headerView.layoutParams.height = mHeaderHeight.toInt()
        headerView.layoutParams.width = mHeaderWidth.toInt()
        headerView.translationX = 0F  //view's moving distance
        headerView.requestLayout()
    }



    fun runOneTime():Int{
        val location = IntArray(2)
        headerView.getLocationInWindow(location)
        val x = location[0]
        val y = location[1]
        alreadyAdded = 1
        return y
    }

    fun handleFling(){
        var mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        var mMaximumVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
        var mMinimumVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity
        System.out.println("$mMaximumVelocity+$mMinimumVelocity")
    }

    fun handleRepeat(diffY:Float,direction:Int){
        positionArray[i] = diffY
        //System.out.println("position $i is ${positionArray[i]}")
        System.out.println(direction)
        i += 1
        if (i>=1){
            if (positionArray[i]>positionArray[i-1]){
                //System.out.println(positionArray[i])
            }else if (positionArray[i]<positionArray[i-1]){
                //System.out.println(positionArray[i])
            }
        }
    }
}