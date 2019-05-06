package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.NestedScrollingParent
import android.util.AttributeSet
import android.view.*
import android.view.animation.AlphaAnimation
import android.widget.*
import android.view.ViewGroup
import android.support.v4.view.VelocityTrackerCompat.getYVelocity
import android.view.VelocityTracker





open class FLexibleLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var mIsBeingDragged: Boolean = false
    private var mInitialY: Float = 0.toFloat()
    private var mInitialX: Float = 0.toFloat()
    private lateinit var headerView : View
    private lateinit var content : View
    private lateinit var header_btns : FrameLayout
    private var mHeaderHeight : Float = 0F
    private var mHeaderWidth : Float = 0F
    private var alreadyAdded : Int = 0
    private var getTop : Int = 0
    private var atTop : Int = 2
    private var temp : Float = 0F
    private var direction : Int = 0  //0 is down, 1 is up
    private var Return : Int = 0
    private var isFirstMoveAfterIntercept : Boolean = true
    private lateinit var titleView : View
    private var titleHeight : Float = 0f
    private val velocityTracker = VelocityTracker.obtain()
    private val speed = ArrayList<Float>()
    private val scroller = Scroller(context)
    val minV = ViewConfiguration.get(context).scaledMinimumFlingVelocity
    val maxV = ViewConfiguration.get(context).scaledMaximumFlingVelocity

    open fun setHeader(header : View) {
        headerView = header
        headerView.post {
            run {
                mHeaderHeight = headerView.height.toFloat()
                mHeaderWidth = headerView.width.toFloat()

//                System.out.println("min fling velocity is $minV") //175
//                System.out.println("max fling velocity is $maxV") //28000
            }
        }
    }
    open fun setContent(content:View, header_btns:FrameLayout){
        this.content = content
        this.header_btns = header_btns
        content.post {
            run{
                val top = content.paddingTop
                System.out.println("padding top is $top")
            }
        }
        header_btns.post {
            run{
                System.out.println("header wigets")
            }
        }
    }
    open fun setTitle(title:View){
        this.titleView = title
        titleView.post{
            run{
                titleHeight = titleView.height.toFloat()
                //titleView.alpha = 0f
            }
        }
    }
    private var i = 0

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        when(ev!!.action){
            MotionEvent.ACTION_DOWN ->{
                isFirstMoveAfterIntercept = true
                System.out.println("dispatchTouchEvent--ACTION_DOWN")

            }
            MotionEvent.ACTION_MOVE ->{
                velocityTracker.addMovement(ev)
                velocityTracker.computeCurrentVelocity(1)
                System.out.println("speed is ${velocityTracker.yVelocity}")
                speed.add(velocityTracker.yVelocity)

                isFirstMoveAfterIntercept = true
                System.out.println("dispatchTouchEvent--ACTION_MOVE")
            }
            MotionEvent.ACTION_UP,MotionEvent.ACTION_CANCEL ->{
                isFirstMoveAfterIntercept = false

                System.out.println("dispatchTouchEvent--ACTION_UP")
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
                System.out.println("onInterceptTouchEvent--ACTION_DOWN")

            }

            MotionEvent.ACTION_MOVE -> {

                // NaN : not a Number
                val diffX : Float = ev.x - mInitialX
                val diffY : Float = ev.y - mInitialY
                val location = IntArray(2)
                headerView.getLocationInWindow(location)
                val x = location[0]
                val y = location[1]
                System.out.println("onInterceptTouchEvent--ACTION_MOVE")

                if (alreadyAdded == 0){ getTop = runOneTime() }

                if (diffY > 0 && y==getTop && (diffY>2*diffX)){
                    direction = 1 // down
                    if (atTop == 0){ temp = ev.y - mInitialY }
                    mIsBeingDragged = true
                    atTop = 1
                    //titleView.alpha = 0f
                    return true
                }else if (diffY > 0 && y != getTop){
                    direction = 1 // down
                    mIsBeingDragged = true
                    atTop = 0
                    isFirstMoveAfterIntercept = false
                }

                if (diffY < 0){
                    direction = 0 // up
                    isFirstMoveAfterIntercept = false

                }
            }
            MotionEvent.ACTION_UP ->{
                if (speed.size < 10){
                    System.out.println("quick touch")
                }
                System.out.println("onInterceptTouchEvent--ACTION_UP")
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
                    if (atTop == 1){
                        changeHeader(diffY-temp)


                    }
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
                System.out.println("onTouchEvent--ACTION_MOVE")

            }
            MotionEvent.ACTION_UP ->{
                System.out.println("onTouchEvent--ACTION_UP")
                if (mIsBeingDragged){
                    resetHeader()
                    //System.out.println("header back to top")
                    temp = 0f
                    i = 0
                    Return = 0

                    speed.clear()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onNestedFling(target: View, velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {

        return true
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

        //content.scrollBy(0,-pullOffset/20)
        content.setPadding(0,headerView.height,0,0)
        header_btns.setPadding(0,pullOffset,0,0)

    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return true
    }

    fun resetHeader(){
        headerView.layoutParams.height = mHeaderHeight.toInt()
        headerView.layoutParams.width = mHeaderWidth.toInt()
        headerView.translationX = 0F  //view's moving distance
        headerView.requestLayout()
        //content.scrollTo(0,0)
        content.setPadding(0,1500,0,0)
        header_btns.setPadding(0,0,0,0)
    }



    fun runOneTime():Int{
        val location = IntArray(2)
        headerView.getLocationInWindow(location)
        val x = location[0]
        val y = location[1]
        alreadyAdded = 1
        return y
    }


}