package com.example.zoomparallax.callback

interface OnPullListener {
    /**
     * 下拉
     * @param offset
     */
     fun onPull(offset: Int)

    /**
     * 松开
     */
     fun onRelease()
}