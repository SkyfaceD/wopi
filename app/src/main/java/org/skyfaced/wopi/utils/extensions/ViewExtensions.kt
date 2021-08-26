package org.skyfaced.wopi.utils.extensions

import android.view.View
import org.skyfaced.wopi.utils.DebouncedOnClickListener

/**
 * Thanks to @gnoemes
 * https://github.com/gnoemes/Shikimori-App-Remastered/blob/dev/app/src/main/java/com/gnoemes/shikimori/utils/ViewExtenstions.kt
 */
inline fun View.onClick(mills: Long = 300L, crossinline body: (v: View) -> Unit) {
    setOnClickListener(object : DebouncedOnClickListener(mills) {
        override fun onDebouncedClick(v: View?) {
            if (v != null) body(v)
        }
    })
}