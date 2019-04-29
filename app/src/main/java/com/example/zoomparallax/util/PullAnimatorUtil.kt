package com.example.zoomparallax.util

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ObjectAnimator.ofFloat
import android.animation.ValueAnimator
import android.support.v4.view.animation.FastOutSlowInInterpolator
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout

open class PullAnimatorUtil {
    /**
     * @param headerView
     * @param headerHeight
     * @param offsetY
     */
    companion object{
        fun pullAnimator(headerView: View?, headerHeight: Int, headerWidth: Int, offsetY: Int, maxHeight: Int) {
            if (headerView == null) {
                return
            }
            val pullOffset = Math.pow(offsetY.toDouble(), 0.8).toInt()
            val newHeight = Math.min(maxHeight + headerHeight, pullOffset + headerHeight)
            val newWidth = (newHeight.toFloat() / headerHeight * headerWidth).toInt()
            headerView.layoutParams.height = newHeight
            headerView.layoutParams.width = newWidth
            var margin = (newWidth - headerWidth) / 2
            if (headerView.parent != null && headerView.parent is RelativeLayout) {
                // TODO: gavin 2018/6/26  RelativeLayout会有问题，需要查明原因
                margin = 0
            }
            headerView.translationX = (-margin).toFloat()
            headerView.requestLayout()
        }

        /**
         * 高度重置动画
         *
         * @param headerView
         * @param headerHeight
         */
        fun resetAnimator(headerView: View?, headerHeight: Int, headerWidth: Int) {
            if (headerView == null) {
                return
            }
            val heightAnimator = ValueAnimator.ofInt(headerView.layoutParams.height, headerHeight)
            heightAnimator.addUpdateListener { animation ->
                val height = animation.animatedValue as Int
                headerView.layoutParams.height = height
            }
            val widthAnimator = ValueAnimator.ofInt(headerView.layoutParams.width, headerWidth)
            widthAnimator.addUpdateListener { animation ->
                val width = animation.animatedValue as Int
                headerView.layoutParams.width = width
            }
            val translationAnimator = ValueAnimator.ofInt(headerView.translationX.toInt(), 0)
            translationAnimator.addUpdateListener { animation ->
                val translation = animation.animatedValue as Int
                headerView.translationX = translation.toFloat()
                headerView.requestLayout()
            }
            val set = AnimatorSet()
            set.interpolator = FastOutSlowInInterpolator()
            set.duration = 100
            set.play(heightAnimator).with(widthAnimator).with(translationAnimator)
            set.start()

        }

        /**
         * 下拉时 刷新控件动画
         *
         * @param refreshView
         * @param offsetY
         * @param refreshViewHeight
         * @param maxRefreshPullHeight
         */
        fun pullRefreshAnimator(refreshView: View?, offsetY: Int, refreshViewHeight: Int, maxRefreshPullHeight: Int) {
            if (refreshView == null) {
                return
            }
            val pullOffset = Math.pow(offsetY.toDouble(), 0.9).toInt()
            val newHeight = Math.min(maxRefreshPullHeight, pullOffset)
            refreshView.translationY = (-refreshViewHeight + newHeight).toFloat()
            refreshView.rotation = pullOffset.toFloat()
            refreshView.requestLayout()
        }

        private var mRefreshingAnimator: ObjectAnimator? = null

        /**
         * 刷新动画
         * 一直转圈圈
         *
         * @param refreshView
         */
        fun onRefreshing(refreshView: View) {
            val rotation = refreshView.rotation
            mRefreshingAnimator = ObjectAnimator.ofFloat(refreshView, "rotation", rotation, rotation + 360)
            mRefreshingAnimator!!.duration = 1000
            mRefreshingAnimator!!.interpolator = LinearInterpolator()
            mRefreshingAnimator!!.repeatMode = ValueAnimator.RESTART
            mRefreshingAnimator!!.repeatCount = -1
            mRefreshingAnimator!!.start()
        }

        /**
         * 重置刷新动画
         *
         * @param refreshView
         * @param refreshViewHeight
         */
        fun resetRefreshView(refreshView: View, refreshViewHeight: Int, animatorListener: Animator.AnimatorListener) {
            if (mRefreshingAnimator != null) {
                mRefreshingAnimator!!.cancel()
            }
            val translation = refreshView.translationY
//            val animator = ObjectAnimator.ofFloat(refreshView, "translationY", translation, refreshViewHeight)
//            animator.setDuration(500)
//            animator.setInterpolator(LinearInterpolator())
//            animator.addListener(animatorListener)
//            animator.start()
        }
    }



}