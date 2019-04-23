package com.example.zoomparallax.CustomeViews
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ImageView
import com.gavin.view.flexible.IFlexible
import com.gavin.view.flexible.R

import com.gavin.view.flexible.callback.OnPullListener
import com.gavin.view.flexible.callback.OnReadyPullListener
import com.gavin.view.flexible.callback.OnRefreshListener
import com.gavin.view.flexible.util.PullAnimatorUtil

/**
 * Created by gavin
 * date 2018/6/12
 * 带有下拉放大效果的FrameLayout
 */
class FlexibleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr),
    IFlexible {

    /**
     * 是否允许下拉放大
     */
    private var isEnable = true

    /**
     * 是否允许下拉刷新
     */
    private var isRefreshable = false

    /**
     * 头部高度
     */
    private var mHeaderHeight = 0

    /**
     * 头部宽度
     */
    private var mHeaderWidth = 0

    /**
     * 头部size ready
     */
    private var mHeaderSizeReady: Boolean = false

    /**
     * 头部
     */
    private var mHeaderView: View? = null

    /**
     * 刷新
     */
    private var mRefreshView: View? = null

    /**
     * 刷新View的宽高
     */
    private var mRefreshSize = screenWidth / 15

    /**
     * 最大头部下拉高度
     */
    private var mMaxPullHeight = screenWidth / 3

    /**
     * 最大 刷新 下拉高度
     */
    private var mMaxRefreshPullHeight = screenWidth / 3

    /**
     * true 开始下拽
     */
    private var mIsBeingDragged: Boolean = false

    /**
     * 标志：正在刷新
     */
    private var mIsRefreshing: Boolean = false

    /**
     * 准备下拉监听
     */
    private var mListener: OnReadyPullListener? = null

    /**
     * 刷新监听
     */
    private var mRefreshListener: OnRefreshListener? = null

    /**
     * 初始坐标
     */
    private var mInitialY: Float = 0.toFloat()
    private var mInitialX: Float = 0.toFloat()

    /**
     * 下拉监听
     */
    private var mOnPullListener: OnPullListener? = null

    /**
     * 刷新动画消失监听
     */
    private val mRefreshAnimatorListener = RefreshAnimatorListener()

    /**
     * 获取屏幕宽度
     *
     * @return
     */
    private val screenWidth: Int
        get() {
            val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val metrics = DisplayMetrics()
            if (mWindowManager != null) {
                mWindowManager.defaultDisplay.getMetrics(metrics)
                return metrics.widthPixels
            } else {
                return 300
            }
        }

    init {
        init()
    }

    private fun init() {
        mIsRefreshing = false
        mHeaderSizeReady = false
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        log("onInterceptTouchEvent")
        if (isEnable && isHeaderReady && isReady) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    log("onInterceptTouchEvent DOWN")
                    mInitialX = ev.x
                    mInitialY = ev.y
                    mIsBeingDragged = false
                }
                MotionEvent.ACTION_MOVE -> {
                    log("onInterceptTouchEvent MOVE")
                    val diffY = ev.y - mInitialY
                    val diffX = ev.x - mInitialX
                    if (diffY > 0 && diffY / Math.abs(diffX) > 2) {
                        mIsBeingDragged = true
                        log("onInterceptTouchEvent return true")
                        return true
                    }
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                }
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        log("onTouchEvent")
        if (isEnable && isHeaderReady && isReady) {
            when (ev.action) {
                MotionEvent.ACTION_MOVE -> if (mIsBeingDragged) {
                    val diffY = ev.y - mInitialY
                    changeHeader(diffY.toInt())
                    changeRefreshView(diffY.toInt())
                    if (mOnPullListener != null) {
                        mOnPullListener!!.onPull(diffY.toInt())
                    }
                    log("onTouchEvent return true")
                    //return true;
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> if (mIsBeingDragged) {
                    resetHeader()
                    if (mOnPullListener != null) {
                        mOnPullListener!!.onRelease()
                    }
                    //刷新操作
                    val diffY = ev.y - mInitialY
                    changeRefreshViewOnActionUp(diffY.toInt())
                    return true
                }
            }
        }
        return super.onTouchEvent(ev)
    }

    override fun isReady(): Boolean {
        return mListener != null && mListener!!.isReady
    }

    override fun isHeaderReady(): Boolean {
        return mHeaderView != null && mHeaderSizeReady
    }

    override fun changeHeader(offsetY: Int) {
        PullAnimatorUtil.pullAnimator(mHeaderView, mHeaderHeight, mHeaderWidth, offsetY, mMaxPullHeight)
    }

    override fun resetHeader() {
        PullAnimatorUtil.resetAnimator(mHeaderView, mHeaderHeight, mHeaderWidth)
    }

    override fun changeRefreshView(offsetY: Int) {
        if (!isRefreshable || mRefreshView == null || isRefreshing) {
            return
        }
        PullAnimatorUtil.pullRefreshAnimator(mRefreshView, offsetY, mRefreshSize, mMaxRefreshPullHeight)
    }

    override fun changeRefreshViewOnActionUp(offsetY: Int) {
        if (!isRefreshable || mRefreshView == null || isRefreshing) {
            return
        }
        mIsRefreshing = true
        if (offsetY > mMaxRefreshPullHeight) {
            PullAnimatorUtil.onRefreshing(mRefreshView!!)
            if (mRefreshListener != null) {
                mRefreshListener!!.onRefreshing()
            }
        } else {
            PullAnimatorUtil.resetRefreshView(mRefreshView!!, mRefreshSize, mRefreshAnimatorListener)
        }
    }

    override fun onRefreshComplete() {
        if (!isRefreshable || mRefreshView == null) {
            return
        }
        PullAnimatorUtil.resetRefreshView(mRefreshView!!, mRefreshSize, mRefreshAnimatorListener)
    }


    override fun isRefreshing(): Boolean {
        return mIsRefreshing
    }

    /**
     * 是否允许下拉放大
     *
     * @param isEnable
     * @return
     */
    fun setEnable(isEnable: Boolean): FlexibleLayout {
        this.isEnable = isEnable
        return this
    }

    /**
     * 是否允许下拉刷新
     *
     * @param isEnable
     * @return
     */
    fun setRefreshable(isEnable: Boolean): FlexibleLayout {
        this.isRefreshable = isEnable
        return this
    }

    /**
     * 设置头部
     *
     * @param header
     * @return
     */
    fun setHeader(header: View): FlexibleLayout {
        mHeaderView = header
        mHeaderView!!.post {
            mHeaderHeight = mHeaderView!!.height
            mHeaderWidth = mHeaderView!!.width
            mHeaderSizeReady = true
        }
        return this
    }

    /**
     * Header最大下拉高度
     *
     * @param height
     * @return
     */
    fun setMaxPullHeight(height: Int): FlexibleLayout {
        mMaxPullHeight = height
        return this
    }

    /**
     * 刷新控件 最大下拉高度
     *
     * @param height
     * @return
     */
    fun setMaxRefreshPullHeight(height: Int): FlexibleLayout {
        mMaxRefreshPullHeight = height
        return this
    }

    /**
     * 设置刷新View的尺寸（正方形）
     *
     * @param size
     * @return
     */
    fun setRefreshSize(size: Int): FlexibleLayout {
        mRefreshSize = size
        return this
    }

    /**
     * 设置刷新View
     *
     * @param refreshView
     * @param listener
     * @return
     */
    fun setRefreshView(refreshView: View, listener: OnRefreshListener): FlexibleLayout {
        if (mRefreshView != null) {
            removeView(mRefreshView)
        }
        mRefreshView = refreshView
        mRefreshListener = listener
        val layoutParams = FrameLayout.LayoutParams(mRefreshSize, mRefreshSize)
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        mRefreshView!!.layoutParams = layoutParams
        mRefreshView!!.translationY = (-mRefreshSize).toFloat()
        addView(mRefreshView)
        return this
    }

    /**
     * 设置默认的刷新头
     *
     * @param listener
     * @return
     */
    fun setDefaultRefreshView(listener: OnRefreshListener): FlexibleLayout {
        val refreshView = ImageView(context)
        refreshView.setImageResource(R.drawable.flexible_loading)
        return setRefreshView(refreshView, listener)
    }

    /**
     * 监听 是否可以下拉放大
     *
     * @param listener
     * @return
     */
    fun setReadyListener(listener: OnReadyPullListener): FlexibleLayout {
        mListener = listener
        return this
    }

    /**
     * 设置下拉监听
     *
     * @param onPullListener
     * @return
     */
    fun setOnPullListener(onPullListener: OnPullListener): FlexibleLayout {
        mOnPullListener = onPullListener
        return this
    }

    private fun log(str: String) {
        //Log.i("FlexibleView", str);
    }

    internal inner class RefreshAnimatorListener : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            mIsRefreshing = false
        }

        override fun onAnimationCancel(animation: Animator) {
            super.onAnimationCancel(animation)
            mIsRefreshing = false
        }
    }
}
