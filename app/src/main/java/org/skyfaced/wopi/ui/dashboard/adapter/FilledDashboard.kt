package org.skyfaced.wopi.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.ItemDashboardBinding
import org.skyfaced.wopi.model.adapter.DashboardItem
import org.skyfaced.wopi.model.adapter.Item
import org.skyfaced.wopi.ui.BaseViewHolder

class FilledDashboard(
    private val onItemClick: (DashboardItem) -> Unit,
) : ItemDashboard<ItemDashboardBinding, DashboardItem> {
    override fun isRelativeItem(item: Item) = item is DashboardItem

    override fun getLayoutId() = R.layout.item_dashboard

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<ItemDashboardBinding, DashboardItem> {
        return FilledDashboardViewHolder(
            binding = ItemDashboardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick
        )
    }

    override fun getDiffUtil() = object : DiffUtil.ItemCallback<DashboardItem>() {
        //@formatter:off
        override fun areItemsTheSame(oldItem: DashboardItem, newItem: DashboardItem) =
            oldItem.city == newItem.city &&
            oldItem.imageUrl == newItem.imageUrl &&
            oldItem.temperature == newItem.temperature
        //@formatter:on

        override fun areContentsTheSame(oldItem: DashboardItem, newItem: DashboardItem) =
            oldItem == newItem
    }

    private class FilledDashboardViewHolder(
        binding: ItemDashboardBinding,
        private val onItemClick: (DashboardItem) -> Unit,
    ) : BaseViewHolder<ItemDashboardBinding, DashboardItem>(binding) {
        init {
            binding.root.setOnClickListener {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                onItemClick(item)
            }
        }

        override fun onBind(item: DashboardItem) {
            super.onBind(item)
            binding {
                txtCity.text = item.city
                imgWeatherState.load(item.imageUrl)
                txtTemperature.text = item.temperature
            }
        }
    }
}
