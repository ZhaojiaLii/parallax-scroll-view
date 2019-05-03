package com.example.zoomparallax.Behaviors

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.FrameMetrics
import android.view.View
import android.widget.FrameLayout
import android.support.v4.view.ViewCompat.setTranslationY
import android.opengl.ETC1.getHeight
import android.R.attr.dependency
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat.setAlpha
import android.opengl.ETC1.getHeight
import android.R.attr.dependency
import android.text.Layout
import android.widget.ScrollView


open class TitleBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<View>(context, attrs) {
    private var deltaY: Float = 0f

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is NestedScrollView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {

        if (deltaY == 0f) {
            deltaY = dependency.y - child.height
        }

        var dy = dependency.y - child.height
        dy = if (dy < 0) 0f else dy
        val alpha = 1 - dy / deltaY
        child.alpha = alpha

        return true
    }
}