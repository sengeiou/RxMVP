package com.valuelink.common.basic

import android.content.Context
import androidx.annotation.LayoutRes

/**
 * BaseView
 *
 * @author Nathaniel
 * @contact <a href="mailto:nathanwriting@126.com">contact me</a>
 * @version v1.0.0
 * @date 2018/3/8 - 15:08
 */
interface BaseViewer {
    /**
     * obtain context
     *
     * @return Context
     */
    val context: Context

    /**
     * initialize before data and view initialized
     */
    fun initialize()

    /**
     * obtain layout resource
     *
     * @return layout id
     */
    @get:LayoutRes
    val layoutId: Int

    /**
     * initialize views
     */
    fun initView()

    /**
     * load data
     */
    fun loadData()

    /**
     * bind events to views
     */
    fun bindView()

    /**
     * display dialog
     *
     * @param message message
     */
    fun showLoading(message: String?)

    /**
     * cancel dialog
     */
    fun dismissLoading()

    /**
     * show message
     *
     * @param message message
     */
    fun showMessage(message: String?)

    /**
     * without more data or not
     *
     * @param withoutMore withoutMore
     */
    fun setWithoutMore(withoutMore: Boolean)

    fun getViewerTag(): String
}