package org.skyfaced.wopi.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.ItemDashboardExperimentalBinding
import org.skyfaced.wopi.model.adapter.DashboardItem
import org.skyfaced.wopi.model.adapter.Item
import org.skyfaced.wopi.ui.base.BaseItem
import org.skyfaced.wopi.ui.base.BaseViewHolder

class FilledDashboard(
    private val onItemClick: (DashboardItem) -> Unit,
) : BaseItem<ItemDashboardExperimentalBinding, DashboardItem>() {
    override fun isRelativeItem(item: Item) = item is DashboardItem

    override fun getLayoutId() = R.layout.item_dashboard_experimental

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<ItemDashboardExperimentalBinding, DashboardItem> {
        return FilledDashboardViewHolder(
            binding = ItemDashboardExperimentalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getDiffUtil() = object : DiffUtil.ItemCallback<DashboardItem>() {
        //@formatter:off
        override fun areItemsTheSame(oldItem: DashboardItem, newItem: DashboardItem) =
            oldItem.city == newItem.city &&
            oldItem.weatherState == newItem.weatherState &&
            oldItem.temperature == newItem.temperature
        //@formatter:on

        override fun areContentsTheSame(oldItem: DashboardItem, newItem: DashboardItem) =
            oldItem == newItem
    }

    private inner class FilledDashboardViewHolder(
        binding: ItemDashboardExperimentalBinding,
    ) : BaseViewHolder<ItemDashboardExperimentalBinding, DashboardItem>(binding) {
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
                imgWeatherState.setImageResource(item.weatherState.drawableRes)
                txtTemperature.text = item.temperature.celsius
            }
        }
    }
}
