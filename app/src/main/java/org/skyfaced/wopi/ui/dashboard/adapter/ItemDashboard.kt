package org.skyfaced.wopi.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import org.skyfaced.wopi.model.adapter.Item
import org.skyfaced.wopi.ui.BaseViewHolder

interface ItemDashboard<VB : ViewBinding, I : Item> {
    fun isRelativeItem(item: Item): Boolean

    @LayoutRes
    fun getLayoutId(): Int

    fun getViewHolder(
        layoutInflater: LayoutInflater,
        parent: ViewGroup,
    ): BaseViewHolder<VB, I>

    fun getDiffUtil(): DiffUtil.ItemCallback<I>
}