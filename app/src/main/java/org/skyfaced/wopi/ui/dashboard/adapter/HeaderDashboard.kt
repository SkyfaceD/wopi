package org.skyfaced.wopi.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.ItemDashboardHeaderBinding
import org.skyfaced.wopi.model.adapter.DashboardHeader
import org.skyfaced.wopi.model.adapter.Item
import org.skyfaced.wopi.ui.base.BaseItem
import org.skyfaced.wopi.ui.base.BaseViewHolder

class HeaderDashboard : BaseItem<ItemDashboardHeaderBinding, DashboardHeader>() {
    override fun isRelativeItem(item: Item) = item is DashboardHeader

    override fun getLayoutId() = R.layout.item_dashboard_header

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<ItemDashboardHeaderBinding, DashboardHeader> {
        return HeaderDashboardViewHolder(
            ItemDashboardHeaderBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )
    }

    override fun getDiffUtil() = object : DiffUtil.ItemCallback<DashboardHeader>() {
        override fun areItemsTheSame(oldItem: DashboardHeader, newItem: DashboardHeader) =
            oldItem.title == oldItem.title

        override fun areContentsTheSame(oldItem: DashboardHeader, newItem: DashboardHeader) =
            oldItem == oldItem
    }

    private class HeaderDashboardViewHolder(
        binding: ItemDashboardHeaderBinding,
    ) : BaseViewHolder<ItemDashboardHeaderBinding, DashboardHeader>(binding) {
        override fun onBind(item: DashboardHeader) {
            super.onBind(item)
            binding.txtHeader.text = item.title
        }
    }
}