package org.skyfaced.wopi.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.skyfaced.wopi.databinding.ItemSearchBinding
import org.skyfaced.wopi.model.adapter.SearchItem

class SearchAdapter(private val onItemClick: (SearchItem) -> Unit) :
    ListAdapter<SearchItem, SearchAdapter.SearchViewHolder>(DiffUtil) {
    class SearchViewHolder(
        private val binding: ItemSearchBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchItem, onItemClick: (SearchItem) -> Unit) {
            with(binding) {
                txtId.text = item.id.toString()
                txtCity.text = item.city
                txtLocationType.text = item.locationType.name
                txtLocationLattLong.text = item.lattLong
                txtLocationDistance.text = item.distance?.toString() ?: ""
                root.setOnClickListener {
                    onItemClick(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }
}

private val DiffUtil = object : DiffUtil.ItemCallback<SearchItem>() {
    //TODO
    override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem) =
        oldItem == newItem
}