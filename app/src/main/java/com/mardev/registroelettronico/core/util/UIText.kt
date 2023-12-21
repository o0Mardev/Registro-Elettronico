package com.mardev.registroelettronico.core.util

import android.content.Context
import androidx.annotation.StringRes

sealed class UIText {
    data class DynamicString(val value: String) : UIText()
    data class StringResource(
        @StringRes val resId: Int,
        val args: List<Args> = emptyList()
    ) : UIText() {
        sealed class Args {
            data class DynamicString(val value: String) : Args()
            data class UiTextArg(val uiText: UIText): Args()

            fun toString(context: Context) =
                when (this) {
                    is DynamicString -> value
                    is UiTextArg -> uiText.asString(context)
                }
        }
    }

    fun asString(context: Context): String =
        when (this) {
            is DynamicString -> value
            is StringResource ->
                context.getString(
                    resId, *(args.map { it.toString(context) }.toTypedArray())
                )
        }
}
