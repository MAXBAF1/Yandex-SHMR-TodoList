package com.around_team.todolist.ui.screens.about

import android.content.Context
import android.net.Uri
import android.widget.Toast
import com.yandex.div.core.DivActionHandler
import com.yandex.div.core.DivViewFacade
import com.yandex.div.json.expressions.ExpressionResolver
import com.yandex.div2.DivAction

class SampleDivActionHandler(private val onCloseAction: () -> Unit) : DivActionHandler() {
    override fun handleAction(
        action: DivAction,
        view: DivViewFacade,
        resolver: ExpressionResolver
    ): Boolean {
        val url = action.url?.evaluate(resolver) ?: return super.handleAction(action, view, resolver)

        return if (url.scheme == SCHEME && handleSampleAction(url, view.view.context)) {
            true
        } else {
            super.handleAction(action, view, resolver)
        }
    }

    private fun handleSampleAction(action: Uri, context: Context): Boolean {
        return when (action.host) {
            "close" -> {
                onCloseAction()
                true
            }
            else -> false
        }
    }

    companion object {
        const val SCHEME = "action"
    }
}