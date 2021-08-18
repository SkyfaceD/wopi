package org.skyfaced.wopi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import org.skyfaced.wopi.model.adapter.Item

abstract class BaseItem<VB : ViewBinding, I : Item> {
    abstract fun isRelativeItem(item: Item): Boolean

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<VB, I>

    abstract fun getDiffUtil(): DiffUtil.ItemCallback<I>
}