package com.nathaniel.baseui.binding

import android.content.Context

/**
 * @author nathaniel
 * @package com.nathaniel.basicui.binding
 * @datetime 12/4/20 - 10:09 AM
 * @version V1.0.0
 */
interface ViewBinder {

    /**
     * initialize before data and view initialized
     */
    fun initialize()

    /**
     * get context
     */
    fun getContext(): Context

    fun beforeInit()

    /**
     * load data
     */
    fun loadData()

    /**
     * bind events to views
     */
    fun bindView()

    /**
     * show message
     *
     * @param message message
     */
    fun showMessage(message: CharSequence)
}