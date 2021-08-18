package org.skyfaced.wopi.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import org.skyfaced.wopi.model.adapter.Item
import org.skyfaced.wopi.ui.BaseDiffUtil
import org.skyfaced.wopi.ui.BaseItem
import org.skyfaced.wopi.ui.BaseViewHolder

class SearchAdapter2(
    private val searches: List<BaseItem<*, *>>,
) : ListAdapter<Item, BaseViewHolder<ViewBinding, Item>>(BaseDiffUtil(searches)) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ViewBinding, Item> {
        val inflater = LayoutInflater.from(parent.context)
        return searches.getOrNull(0)
            ?.getViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, Item> }
            ?: throw IllegalArgumentException("View type not found: $viewType")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, Item>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<ViewBinding, Item>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetached()
    }
}