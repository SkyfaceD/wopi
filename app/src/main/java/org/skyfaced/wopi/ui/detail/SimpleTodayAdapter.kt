package org.skyfaced.wopi.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.skyfaced.wopi.databinding.ItemDetailTodayBinding
import org.skyfaced.wopi.repository.DetailRepositoryImpl

class SimpleTodayAdapter(
    val list: List<DetailRepositoryImpl.Detail.Today>,
) : RecyclerView.Adapter<SimpleTodayAdapter.ViewHolder>() {
    class ViewHolder(private val binding: ItemDetailTodayBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DetailRepositoryImpl.Detail.Today) {
            with(binding) {
                txtTemperature.text = item.temperature
                imgWeatherState.setImageResource(item.weatherState.drawableRes)
                txtTime.text = item.time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDetailTodayBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size
}