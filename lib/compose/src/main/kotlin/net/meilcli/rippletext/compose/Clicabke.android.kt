/**
 * Original Source: https://androidx.tech/artifacts/compose.foundation/foundation/1.1.1-source/androidx/compose/foundation/Clickable.android.kt.html
 */
package net.meilcli.rippletext.compose

import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalView

@Composable
internal fun isComposeRootInScrollableContainer(): () -> Boolean {
    val view = LocalView.current
    return {
        view.isInScrollableViewGroup()
    }
}

private fun View.isInScrollableViewGroup(): Boolean {
    var p = parent
    while (p != null && p is ViewGroup) {
        if (p.shouldDelayChildPressedState()) {
            return true
        }
        p = p.parent
    }
    return false
}