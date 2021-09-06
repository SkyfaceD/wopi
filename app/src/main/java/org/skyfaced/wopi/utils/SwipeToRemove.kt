package org.skyfaced.wopi.utils

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import org.skyfaced.wopi.R

//TODO Fix remove drawable
class SwipeToRemove(
    private val theme: Resources.Theme,
    val onItemDelete: (Int) -> Unit,
) : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.START
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder,
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        onItemDelete(viewHolder.bindingAdapterPosition)
    }

    override fun getSwipeDirs(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
    ): Int {
        return if (viewHolder.itemViewType != R.layout.item_dashboard_experimental) {
            ItemTouchHelper.ACTION_STATE_IDLE
        } else {
            super.getSwipeDirs(recyclerView, viewHolder)
        }
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder) = 0.4f

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean,
    ) {
        val itemView: View = viewHolder.itemView

        // Draw background
        val backgroundColor = MaterialColors.getColor(recyclerView, R.attr.colorError)
        val backgroundPaint = Paint().also { it.color = backgroundColor }
        c.drawRect(
            itemView.right.toFloat() + dX,
            itemView.top.toFloat(),
            itemView.right.toFloat(),
            itemView.bottom.toFloat(),
            backgroundPaint
        )

        //Draw icon
        val iconColor = MaterialColors.getColor(recyclerView, R.attr.colorOnError)
        val iconPaint = Paint().also { it.color = iconColor }
        val drawable = getDrawable(recyclerView.context.resources, R.drawable.ic_remove, theme)
            ?: throw NullPointerException("Can't be null")
        val icon = drawableToBitmap(drawable) ?: throw NullPointerException("Can't be null")
        val iconMarginRight = (dX * -0.1f).coerceAtMost(70f).coerceAtLeast(0f)
        c.drawBitmap(
            icon,
            itemView.right.toFloat() - iconMarginRight - icon.width,
            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top.toFloat() - icon.height) / 2,
            iconPaint
        )

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}