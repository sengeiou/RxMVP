package com.nathaniel.baseui.binding

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

/**
 * @author nathaniel
 * @version V1.0.0
 * @package com.valuelink.common.binding
 * @datetime 4/1/21 - 10:16 AM
 */
class AbstractViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)