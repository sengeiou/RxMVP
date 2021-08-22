package com.nathaniel.baseui.binding

import com.valuelink.common.basic.BaseViewer
import io.reactivex.disposables.Disposable

/**
 * BasePresenter
 *
 * @author Nathaniel
 * @contact <a href="mailto:nathanwriting@126.com">contact me</a>
 * @version v1.0.0
 * @date 2018/3/8 - 15:02
 */
interface BaseContract<V : BaseViewer> {
    fun attachView(baseViewer: V)
    fun detachView()
    fun addDisposable(disposable: Disposable?)
    fun unDisposable()
    fun setLoadable(loadable: Boolean)
}