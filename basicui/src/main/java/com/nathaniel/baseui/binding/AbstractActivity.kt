package com.nathaniel.baseui.binding

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.BarProperties
import com.gyf.immersionbar.ImmersionBar
import com.nathaniel.baseui.R
import com.yumore.provider.EmptyUtils
import com.yumore.provider.LoggerUtils

/**
 * @author Nathaniel
 * @version V1.0.0
 * @package com.nathaniel.baseui.binding
 * @datetime 2021/3/31 - 19:50
 */
abstract class AbstractActivity<VB : ViewBinding> : AppCompatActivity(), ViewBinder {
    protected var firstTime = true
    private lateinit var alertDialog: AlertDialog
    private lateinit var _binding: VB
    protected val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = getViewBinding()
        setContentView(_binding.root)
        initialize()
    }

    protected abstract fun getViewBinding(): VB

    override fun getContext(): Context {
        return _binding.root.context
    }

    override fun initialize() {
        loadData()
        bindView()
    }

    override fun beforeInit() {
        LoggerUtils.logger(TAG, Thread.currentThread().stackTrace[1].methodName)
        ImmersionBar.with(this)
            .transparentStatusBar()
            .transparentNavigationBar()
            .transparentBar()
            .statusBarColor(statusBarColor())
            .navigationBarColor(navigationColor())
            .statusBarDarkFont(darkModeEnable())
            .navigationBarDarkIcon(darkModeEnable())
            .autoDarkModeEnable(darkModeEnable())
            .flymeOSStatusBarFontColor(statusFontColor())
            .fullScreen(fitsSystemWindows())
            .hideBar(barHide)
            .fitsSystemWindows(fitsSystemWindows())
            .navigationBarEnable(navigationEnable())
            .navigationBarWithKitkatEnable(navigationEnable())
            .navigationBarWithEMUI3Enable(navigationEnable())
            .keyboardEnable(keyboardEnable())
            .keyboardMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            .setOnKeyboardListener { popupEnable: Boolean, keyboardHeight: Int -> LoggerUtils.logger(TAG, popupEnable, keyboardHeight) }
            .setOnNavigationBarListener { show: Boolean -> LoggerUtils.logger(TAG, show) }
            .setOnBarListener { barProperties: BarProperties? -> LoggerUtils.logger(TAG, barProperties) }
            .init()
    }

    protected fun immersionEnable(): Boolean {
        return true
    }

    protected fun statusBarColor(): Int {
        return R.color.common_color_theme
    }

    protected fun navigationColor(): Int {
        return R.color.common_color_transparent
    }

    protected fun statusFontColor(): Int {
        return R.color.common_color_black_light
    }

    protected fun darkModeEnable(): Boolean {
        return true
    }

    protected fun fitsSystemWindows(): Boolean {
        return true
    }

    protected fun keyboardEnable(): Boolean {
        return true
    }

    private val barHide: BarHide
        get() = if (hideNavigation()) BarHide.FLAG_HIDE_NAVIGATION_BAR else BarHide.FLAG_SHOW_BAR

    protected fun hideNavigation(): Boolean {
        return false
    }

    protected fun navigationEnable(): Boolean {
        return true
    }

    protected val activity: FragmentActivity by lazy { this }

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
            textView.visibility = View.VISIBLE
        } else {
            textView.visibility = View.GONE
        }
        alertDialog = AlertDialog.Builder(activity, R.style.CustomDialog)
            .setCancelable(false)
            .setView(view)
            .create()
        alertDialog.show()
        val dialogWindow = alertDialog.window
        if (dialogWindow != null) {
            dialogWindow.setGravity(Gravity.CENTER)
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
        return findViewById(viewId)
    }

    protected fun <T : View?> obtainView(parent: View, viewId: Int): T {
        return parent.findViewById(viewId)
    }

    companion object {
        private val TAG = AbstractActivity::class.java.simpleName
    }
}