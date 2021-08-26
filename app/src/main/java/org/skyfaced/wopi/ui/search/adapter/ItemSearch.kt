package org.skyfaced.wopi.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.skyfaced.wopi.R
import org.skyfaced.wopi.databinding.ItemSearchBinding
import org.skyfaced.wopi.model.adapter.Item
import org.skyfaced.wopi.model.adapter.SearchItem
import org.skyfaced.wopi.ui.base.BaseItem
import org.skyfaced.wopi.ui.base.BaseViewHolder
import org.skyfaced.wopi.utils.extensions.onClick

class ItemSearch(
    private val onItemClick: (SearchItem) -> Unit,
) : BaseItem<ItemSearchBinding, SearchItem>() {
    override fun isRelativeItem(item: Item) = item is SearchItem

    override fun getLayoutId() = R.layout.item_search

    override fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<ItemSearchBinding, SearchItem> {
        return ViewHolder(
            binding = ItemSearchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClick = onItemClick
        )
    }

    override fun getDiffUtil() = object : DiffUtil.ItemCallback<SearchItem>() {
        //@formatter:off
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem) =
            oldItem.city == newItem.city &&
            oldItem.description == newItem.description
        //@formatter:on

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem) =
            oldItem == newItem
    }

    private class ViewHolder(
        binding: ItemSearchBinding,
        private val onItemClick: (SearchItem) -> Unit,
    ) : BaseViewHolder<ItemSearchBinding, SearchItem>(binding) {
        init {
            binding.root.onClick {
                if (bindingAdapterPosition == RecyclerView.NO_POSITION) return@onClick
                onItemClick(item)
            }
        }

        override fun onBind(item: SearchItem) {
            super.onBind(item)
            with(binding) {
                txtCity.text = item.city
                txtDescription.text = item.description
            }
        }
    }
}