package com.example.zoomparallax.CustomeViews

import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView

class handleFlingScrollview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {
    override fun fling(velocityY: Int) {
        super.fling(velocityY)
    }
}