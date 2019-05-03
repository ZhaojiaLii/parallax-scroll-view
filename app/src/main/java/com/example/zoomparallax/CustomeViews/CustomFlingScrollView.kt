package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet

open class CustomFlingScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : NestedScrollView(context, attrs, defStyleAttr) {
    override fun fling(velocityY: Int) {
        super.fling(velocityY/8)
    }
}