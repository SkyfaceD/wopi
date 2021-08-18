package org.skyfaced.wopi.ui

import androidx.recyclerview.widget.DiffUtil
import org.skyfaced.wopi.model.adapter.Item

class BaseDiffUtil(
    private val list: List<BaseItem<*, *>>,
) : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        if (oldItem::class != newItem::class) return false
        return getItemCallback(oldItem).areItemsTheSame(oldItem, newItem)
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        if (oldItem::class != newItem::class) return false
        return getItemCallback(oldItem).areContentsTheSame(oldItem, newItem)
    }

    private fun getItemCallback(
        item: Item,
    ): DiffUtil.ItemCallback<Item> = list.find { it.isRelativeItem(item) }
        ?.getDiffUtil()
        ?.let { it as DiffUtil.ItemCallback<Item> }
        ?: throw IllegalStateException("DiffUtil not found: $item")
}