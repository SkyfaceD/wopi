package org.skyfaced.wopi.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.ItemDashboardAddBinding
import org.skyfaced.wopi.model.adapter.DashboardAdd
import org.skyfaced.wopi.model.adapter.Item
import org.skyfaced.wopi.ui.base.BaseItem
import org.skyfaced.wopi.ui.base.BaseViewHolder

class AddDashboard(
    private val onItemClick: () -> Unit,
) : BaseItem<ItemDashboardAddBinding, DashboardAdd>() {
    override fun isRelativeItem(item: Item) = item is DashboardAdd

    override fun getLayoutId() = R.layout.item_dashboard_add

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<ItemDashboardAddBinding, DashboardAdd> {
        return AddDashboardViewHolder(
            ItemDashboardAddBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick
        )
    }

    override fun getDiffUtil() = object : DiffUtil.ItemCallback<DashboardAdd>() {
        override fun areItemsTheSame(oldItem: DashboardAdd, newItem: DashboardAdd) = true

        override fun areContentsTheSame(oldItem: DashboardAdd, newItem: DashboardAdd) = true
    }

    private class AddDashboardViewHolder(
        binding: ItemDashboardAddBinding,
        private val onItemClick: () -> Unit,
    ) : BaseViewHolder<ItemDashboardAddBinding, DashboardAdd>(binding) {
        init {
            binding.root.setOnClickListener {
                onItemClick()
            }
        }
    }
}