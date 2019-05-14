package com.example.zoomparallax.Behaviors

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import com.example.zoomparallax.CustomeViews.DisInterceptNestedScrollView
import kotlin.math.abs
import kotlin.coroutines.coroutineContext as coroutineContext1
import android.widget.OverScroller




open class AppBarLayoutOverScrollViewBehavior(context: Context?, attrs: AttributeSet?) :
    AppBarLayout.Behavior(context, attrs) {

    private val TAG = "overScroll"
    private val TAG_TOOLBAR = "toolbar"
    private val TAG_CONTENT = "content"
    private val TAG_HEADERCONTENT = "headercontent"

    private val TARGET_HEIGHT = 3000f

    private var mTargetView: View? = null
    private var contentView : View? = null
    private var headercontentView : View? = null
    private var mParentHeight: Int = 0
    private var mTargetViewHeight: Int = 0
    private var mTotalDy: Float = 0f
    private var mLastScale: Float = 0f
    private var mLastBottom: Int = 0

    private var isAnimate: Boolean = false
    private var mToolBar: Toolbar? = null

    private var isRecovering = false
    private val TYPE_FLING = 1

    private var isFlinging: Boolean = false
    private var shouldBlockNestedScroll: Boolean = false

    private var mScroller1 : OverScroller = OverScroller(context)


    private fun stopAppbarLayoutFlingInit() {
        try {
            System.out.println("overScroller.abortAnimation() start processing")
            val headerBehaviorType = this.javaClass.superclass!!.superclass!!.superclass
            val flingRunnableField = headerBehaviorType!!.getDeclaredField("flingRunnable")
            val scrollerField = headerBehaviorType.getDeclaredField("scroller")
            flingRunnableField.isAccessible = true
            scrollerField.isAccessible = true
            val flingRunnable = flingRunnableField.get(this)
            System.out.println("overScroller.abortAnimation() ${scrollerField}")
            scrollerField.set(this,mScroller1)
            System.out.println("overScroller.abortAnimation() ${scrollerField.get(this)}")
        }catch (e:Exception ){ }

    }

    private fun stopFling(){
        mScroller1.abortAnimation()
        System.out.println("overScroller.abortAnimation() has been executed")

    }


    override fun onLayoutChild(parent: CoordinatorLayout, abl: AppBarLayout, layoutDirection: Int): Boolean {
        val handled = super.onLayoutChild(parent, abl, layoutDirection)
        if (mToolBar == null) {
            mToolBar = parent.findViewWithTag<View>(TAG_TOOLBAR) as Toolbar


        }
        if (headercontentView == null){
            headercontentView = parent.findViewWithTag(TAG_HEADERCONTENT)
        }

        if (contentView == null){
            contentView = parent.findViewWithTag(TAG_CONTENT)
            System.out.println("$contentView")
        }

        if (mTargetView == null) {
            mTargetView = abl.findViewWithTag(TAG)
            if (mTargetView != null) {
                init(abl)
                stopAppbarLayoutFlingInit()
            }
        }

        abl.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, i ->
            val alpha_value = java.lang.Float.valueOf(Math.abs(i).toFloat()) / java.lang.Float.valueOf(appBarLayout.totalScrollRange.toFloat()/6)
            mToolBar!!.alpha = alpha_value
        })
        return handled

    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: AppBarLayout, ev: MotionEvent): Boolean {


        shouldBlockNestedScroll = false

        if (isFlinging) {
            shouldBlockNestedScroll = true
            //System.out.println("intercepted")
        }


        when (ev.actionMasked) {
            ACTION_DOWN ->{
                System.out.println("intercepted")
                stopFling()
            }

        }
        return super.onInterceptTouchEvent(parent, child, ev)
    }




    //start nested scroll
    override fun onStartNestedScroll(
        parent: CoordinatorLayout,
        child: AppBarLayout,
        directTargetChild: View,
        target: View,
        nestedScrollAxes: Int,
        type: Int
    ): Boolean {
        stopFling()
        isAnimate = true
        if (target is DisInterceptNestedScrollView){
            return true }
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type)
    }


    //即将开始嵌套滚动，每次滑动前，Child 先询问 Parent 是否需要滑动，即dispatchNestedPreScroll()，这就回调到 Parent 的onNestedPreScroll()
    // Parent 可以在这个回调中“劫持”掉 Child 的滑动，也就是先于 Child 滑动。
    override fun onNestedPreScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dx: Int,
        dy: Int,
        consumed: IntArray,
        type: Int
    ) {


        // Constant Value: 1 means fling without touching(from ViewCompat)
        if (type == TYPE_FLING){
            isFlinging = true
        }

        if (type == ViewCompat.TYPE_NON_TOUCH){
            if (mScroller1.computeScrollOffset()){
                mScroller1.abortAnimation()
            }
            if (topAndBottomOffset == 0){
                ViewCompat.stopNestedScroll(target,type)
            }
        }

        if (!shouldBlockNestedScroll){
            if (!isRecovering){
                if (mTargetView!=null && (dy<0&&child.bottom >=mParentHeight)){
                    scale(child,target,dy)
                    return
                }
            }
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
        }

    }



    //嵌套滚动的过程中
    override fun onNestedScroll(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        type: Int
    ) {
        if (type == TYPE_FLING){
            isFlinging = true

        }
        if (!shouldBlockNestedScroll) {
            System.out.println("嵌套滚动的过程中")
            System.out.println("overScroller.abortAnimation() ${mScroller1.computeScrollOffset()}")
            super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type)
        }
    }


    //即将开始快速划动，这里可以做一些对动画的缓冲处理，也就是我们如何去应对用户快速的操作
    override fun onNestedPreFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
//        if (abs(velocityY)>5000){
//            isAnimate = false
//            //System.out.println("v is pre : $velocityY")
//            isFlinging = true
//            return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
//        }else{
//            return false
//        }

        return if (abs(velocityY)>100){
            System.out.println("v is : $velocityY, start fling.")

            super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY)
        }else{
            true
        }
    }

    //快速的划动中
    /**
     * 当嵌套滚动的子View快速滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child 使用此Behavior的AppBarLayout
     * @param target 发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param velocityX 水平方向的速度
     * @param velocityY 垂直方向的速度
     * @param consumed 如果嵌套的子View消耗了快速滚动则为true
     * @return 如果Behavior消耗了快速滚动返回true
     */
    override fun onNestedFling(
        coordinatorLayout: CoordinatorLayout,
        child: AppBarLayout,
        target: View,
        velocityX: Float,
        velocityY: Float,
        consumed: Boolean
    ): Boolean {
//        if (abs(velocityY)<5000){
//            isAnimate = false
//            System.out.println("v is pre : $velocityY")
//            isFlinging = true
//            return false
//        }else{
//            return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)
//        }
        System.out.println("$velocityY")

        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed)

    }


    override fun onStopNestedScroll(coordinatorLayout: CoordinatorLayout, abl: AppBarLayout, target: View, type: Int) {

        recovery(abl)
        isFlinging = false
        shouldBlockNestedScroll = false
        System.out.println("v is 0 and stopped")
        //System.out.println("overScroller.abortAnimation() ${mScroller1.computeScrollOffset()}")
        System.out.println("overScroller.abortAnimation() is finished or not? ${mScroller1.isFinished}")
        super.onStopNestedScroll(coordinatorLayout, abl, target, type)
    }





    fun init(appBarLayout: AppBarLayout){
        appBarLayout.clipChildren = false  //child view can out of range of parent view or not (true:cannot, false:can)
        mParentHeight = appBarLayout.height
        mTargetViewHeight = mTargetView!!.height
//        System.out.println("parent appbar layout height: $mParentHeight")
//        System.out.println("target view header image height: $mTargetViewHeight")
    }

    fun scale(appBarLayout: AppBarLayout,target: View,dy: Int){
        mTotalDy += (-dy).toFloat()
        mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT)
        mLastScale = Math.max(1f, 1f + mTotalDy / TARGET_HEIGHT)
        mTargetView!!.scaleX = mLastScale
        mTargetView!!.scaleY = mLastScale

        mLastBottom = mParentHeight + (mTargetViewHeight / 2 * (mLastScale - 1)).toInt()
        appBarLayout.bottom = mLastBottom
        //appBarLayout.scrollY = -(appBarLayout.height*(mLastScale - 1)).toInt()

        //contentView!!.top = mLastBottom-contentView!!.height
        contentView!!.top = mLastBottom-contentView!!.height
        contentView!!.bottom = mLastBottom
        headercontentView!!.top = mLastBottom-headercontentView!!.height
        headercontentView!!.bottom = mLastBottom

        target.scrollY = 0
//        System.out.println("top of content is: ${contentView!!.top}")
//        System.out.println("bottom of header is: ${mTargetView!!.bottom}")

    }


    fun recovery(appBarLayout: AppBarLayout){

        if (mTotalDy > 0){
            mTotalDy = 0f
            mTargetView!!.scaleX = 1f
            mTargetView!!.scaleY = 1f
            appBarLayout.bottom = mParentHeight
            contentView!!.top = mParentHeight-contentView!!.height
            contentView!!.bottom = mParentHeight
            headercontentView!!.top = mParentHeight-headercontentView!!.height
            headercontentView!!.bottom = mParentHeight
            isRecovering = false

        }
    }
}