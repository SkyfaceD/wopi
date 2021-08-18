package org.skyfaced.wopi.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding
import org.skyfaced.wopi.model.adapter.Item
import org.skyfaced.wopi.ui.BaseViewHolder

class DashboardAdapter(
    private val dashboards: List<ItemDashboard<*, *>>,
) : ListAdapter<Item, BaseViewHolder<ViewBinding, Item>>(DashboardDiffUtil(dashboards)) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): BaseViewHolder<ViewBinding, Item> {
        val inflater = LayoutInflater.from(parent.context)
        return dashboards.find { it.getLayoutId() == viewType }
            ?.getViewHolder(inflater, parent)
            ?.let { it as BaseViewHolder<ViewBinding, Item> }
            ?: throw IllegalAccessException("View type not found: $viewType")
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding, Item>, position: Int) {
        holder.onBind(getItem(position))
    }

    override fun onViewDetachedFromWindow(holder: BaseViewHolder<ViewBinding, Item>) {
        super.onViewDetachedFromWindow(holder)
        holder.onViewDetached()
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return dashboards.find { it.isRelativeItem(item) }
            ?.getLayoutId()
            ?: throw IllegalArgumentException("Item view type not found: $item")
    }
}
