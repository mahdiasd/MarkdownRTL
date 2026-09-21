package com.persian.markdown.preview

import com.intellij.openapi.application.ApplicationManager
import com.persian.markdown.settings.PersianMarkdownSettings
import com.persian.markdown.settings.PersianMarkdownSettingsListener
import org.intellij.plugins.markdown.extensions.MarkdownBrowserPreviewExtension
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanel
import org.intellij.plugins.markdown.ui.preview.ResourceProvider

class PersianMarkdownBrowserExtension(
    private val panel: MarkdownHtmlPanel
) : MarkdownBrowserPreviewExtension, ResourceProvider {

    private val connection = ApplicationManager.getApplication().messageBus.connect(this)

    init {
        connection.subscribe(PersianMarkdownSettingsListener.TOPIC, PersianMarkdownSettingsListener {
            ApplicationManager.getApplication().invokeLater {
                try {
                    panel.reloadWithOffset(0)
                } catch (_: Exception) {
                }
            }
        })
    }

    override val priority: MarkdownBrowserPreviewExtension.Priority
        get() = MarkdownBrowserPreviewExtension.Priority.AFTER_ALL

    override val styles: List<String> = listOf("persianMarkdown/persian.css")
    override val scripts: List<String> = listOf("persianMarkdown/persian.js")

    override val resourceProvider: ResourceProvider
        get() = this

    override fun canProvide(resourceName: String): Boolean {
        return resourceName == "persianMarkdown/persian.css" || resourceName == "persianMarkdown/persian.js"
    }

    override fun loadResource(resourceName: String): ResourceProvider.Resource? {
        return when (resourceName) {
            "persianMarkdown/persian.css" -> {
                val state = PersianMarkdownSettings.getInstance().state
                val css = CssGenerator.generateCss(state)
                ResourceProvider.Resource(css.toByteArray(Charsets.UTF_8), "text/css; charset=utf-8")
            }
            "persianMarkdown/persian.js" -> {
                val state = PersianMarkdownSettings.getInstance().state
                val js = CssGenerator.generateAutoDirScript(state)
                ResourceProvider.Resource(js.toByteArray(Charsets.UTF_8), "application/javascript; charset=utf-8")
            }
            else -> null
        }
    }

    override fun compareTo(other: MarkdownBrowserPreviewExtension): Int {
        return priority.value.compareTo(other.priority.value)
    }

    override fun dispose() {
        try {
            connection.disconnect()
        } catch (_: Exception) {
        }
    }

    class Provider : MarkdownBrowserPreviewExtension.Provider {
        override fun createBrowserExtension(panel: MarkdownHtmlPanel): MarkdownBrowserPreviewExtension {
            return PersianMarkdownBrowserExtension(panel)
        }
    }
}
