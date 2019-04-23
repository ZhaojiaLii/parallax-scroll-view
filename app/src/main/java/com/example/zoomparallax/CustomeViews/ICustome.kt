package com.example.zoomparallax.CustomeViews

interface ICustome {
    /**
     * 是否准备下拉
     *
     * @return
     */
    fun isReady(): Boolean

    /**
     * 头部ready
     *
     * @return
     */
    fun isHeaderReady(): Boolean

    /**
     * 下拉Header
     *
     * @param offsetY
     */
    fun changeHeader(offsetY: Int)

    /**
     * 重置Header
     */
    fun resetHeader()

    /**
     * 刷新
     */
    fun changeRefreshView(offsetY: Int)

    /**
     * 松开时的刷新控件
     * 重置 或 触发刷新
     *
     * @param offsetY
     */
    fun changeRefreshViewOnActionUp(offsetY: Int)

    /**
     * 刷新完成
     */
    fun onRefreshComplete()

    /**
     * 是否正在刷新
     */
    fun isRefreshing(): Boolean
}