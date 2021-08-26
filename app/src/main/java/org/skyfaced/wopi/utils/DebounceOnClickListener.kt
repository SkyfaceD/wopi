package org.skyfaced.wopi.utils

import android.os.SystemClock
import android.view.View
import java.util.*
import kotlin.math.abs

/**
 * Thanks to @gnoemes
 * https://github.com/gnoemes/Shikimori-App-Remastered/blob/dev/app/src/main/java/com/gnoemes/shikimori/utils/widgets/DebouncedOnClickListener.java
 */

/**
 * A Debounced OnClickListener
 * Rejects clicks that are too close together in time.
 * This class is safe to use as an OnClickListener for multiple views, and will debounce each one separately.
 *
 * The one and only constructor
 * @param minimumInterval The minimum allowed time between clicks - any click sooner than this after a previous click will be rejected
 */
abstract class DebouncedOnClickListener constructor(private val minimumInterval: Long) :
    View.OnClickListener {
    private val lastClickMap: MutableMap<View, Long> = WeakHashMap()

    /**
     * Implement this in your subclass instead of onClick
     * @param v The view that was clicked
     */
    abstract fun onDebouncedClick(v: View?)

    override fun onClick(clickedView: View) {
        val previousClickTimestamp = lastClickMap[clickedView]
        val currentTimestamp = SystemClock.uptimeMillis()

        lastClickMap[clickedView] = currentTimestamp
        if (previousClickTimestamp == null || abs(currentTimestamp - previousClickTimestamp) > minimumInterval) {
            onDebouncedClick(clickedView)
        }
    }
}