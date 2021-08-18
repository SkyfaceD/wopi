package org.skyfaced.wopi.ui

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import org.skyfaced.wopi.model.adapter.Item

abstract class BaseViewHolder<VB : ViewBinding, I : Item>(
    protected val binding: VB,
) : RecyclerView.ViewHolder(binding.root) {
    private var _item: I? = null
    protected val item get() = requireNotNull(_item) { "Item isn't initialized" }

    protected fun binding(block: VB.() -> Unit) = binding.apply(block)

    open fun onBind(item: I) {
        _item = item
    }

    open fun onViewDetached() = Unit
}