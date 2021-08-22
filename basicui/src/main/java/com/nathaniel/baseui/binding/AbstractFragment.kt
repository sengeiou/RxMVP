package com.nathaniel.baseui.binding

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.nathaniel.baseui.R
import com.yumore.provider.EmptyUtils
import com.yumore.provider.LoggerUtils

/**
 * @author Nathaniel
 * @version V1.0.0
 * @package com.nathaniel.baseui.binding
 * @datetime 2021/3/31 - 19:57
 */
abstract class AbstractFragment<VB : ViewBinding> : Fragment(), ViewBinder {
    private lateinit var _binding: VB
    protected val binding get() = _binding
    private lateinit var alertDialog: AlertDialog
    private var context: Context? = null
    protected var initialized = false
    protected var firstLoaded = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getViewBinding(container)
        initialize()
        return _binding.root
    }

    protected abstract fun getViewBinding(viewGroup: ViewGroup?): VB

    override fun getContext(): Context {
        return _binding.root.context
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.context = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeInit()
    }

    override fun beforeInit() {
        LoggerUtils.logger(TAG, "beforeUI()")
    }

    override fun initialize() {
        loadData()
        bindView()
    }

    protected val fragment: Fragment by lazy { this }

    override fun showMessage(message: CharSequence) {}
    fun showLoading(message: CharSequence?, displayed: Boolean) {
        if (!displayed) {
            return
        }
        dismissLoading()
        val view = LayoutInflater.from(activity).inflate(R.layout.common_dialog_loading, null)
        val textView = view.findViewById<TextView>(R.id.loading_dialog_message)
        if (!TextUtils.isEmpty(message)) {
            textView.text = message
        } else {
            textView.visibility = View.GONE
        }
        alertDialog = AlertDialog.Builder(context!!, R.style.CustomDialog)
            .setCancelable(false)
            .setView(view)
            .create()
        alertDialog.show()
        val dialogWindow = alertDialog.window
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            val layoutParams = dialogWindow.attributes
            layoutParams.dimAmount = .35f
            dialogWindow.attributes = layoutParams
        }
    }

    fun dismissLoading() {
        if (!EmptyUtils.isEmpty(alertDialog) && alertDialog.isShowing) {
            alertDialog.dismiss()
        }
    }

    fun <T : View?> obtainView(viewId: Int): T {
        return binding.root.findViewById(viewId)
    }

    protected fun <T : View?> obtainView(parent: View, viewId: Int): T {
        return parent.findViewById(viewId)
    }

    companion object {
        private val TAG = AbstractFragment::class.java.simpleName
    }


    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract fun lazyLoad()

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以调用此方法
     */
    protected fun stopLoad() {

    }
}