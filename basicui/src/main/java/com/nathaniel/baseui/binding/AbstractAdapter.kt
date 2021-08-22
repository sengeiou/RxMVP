package com.nathaniel.baseui.binding

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author nathaniel
 * @version V1.0.0
 * @package com.valuelink.common.binding
 * @datetime 4/1/21 - 10:15 AM
 */
abstract class AbstractAdapter<T : Any, VB : ViewBinding> : RecyclerView.Adapter<AbstractViewHolder<VB>>() {
    protected val attach2parent: Boolean = false
    open var dataList: MutableList<T> = mutableListOf()
        set(value) {
            field = value
            notifyItemRangeChanged(0, value.size)
        }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder<VB>

    abstract fun bindData2View(holder: AbstractViewHolder<VB>, position: Int, binding: VB, data: T)

    override fun getItemCount(): Int = dataList.size

    override fun onBindViewHolder(holder: AbstractViewHolder<VB>, position: Int) {
        bindData2View(holder, holder.adapterPosition, holder.binding, dataList[holder.adapterPosition])
    }
}