package org.skyfaced.wopi.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.skyfaced.wopi.databinding.ItemDetailWeekBinding
import org.skyfaced.wopi.model.adapter.DetailItem
import org.skyfaced.wopi.utils.extensions.onClick

class DetailWeekAdapter(
    private val onItemClick: (item: DetailItem) -> Unit,
) : ListAdapter<DetailItem, DetailWeekAdapter.ViewHolder>(DiffUtil) {
    inner class ViewHolder(
        private val binding: ItemDetailWeekBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private var _item: DetailItem? = null
        private val item get() = requireNotNull(_item) { "DetailItem isn't initialized" }

        init {
            binding.root.onClick {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@onClick
                onItemClick(item)
            }
        }

        fun onBind(item: DetailItem) {
            _item = item
            with(binding) {
                txtTemperature.text = item.helper.temperature.celsius
                imgWeatherState.setImageResource(item.weatherState.drawableRes)
                txtDayOfWeek.text = item.week.dayOfWeek
                root.isChecked = item.week.isChecked
            }
        }

        fun onBind(item: DetailItem, payloads: List<Any>) {
            _item = item
            val isChecked = payloads.last() as Boolean
            binding.root.isChecked = isChecked
        }
    }

    companion object {
        @Suppress("FunctionName")
        val DiffUtil = object : DiffUtil.ItemCallback<DetailItem>() {
            override fun areItemsTheSame(oldItem: DetailItem, newItem: DetailItem) =
                oldItem.date == newItem.date

            override fun areContentsTheSame(oldItem: DetailItem, newItem: DetailItem) =
                oldItem.week.isChecked == newItem.week.isChecked
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = ItemDetailWeekBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.onBind(currentList[position], payloads)
        }
    }
}