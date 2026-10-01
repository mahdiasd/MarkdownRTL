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
    private var isUpdatingFromJs = false

    init {
        try {
            panel.browserPipe?.subscribe("pmUpdateSettings", object : org.intellij.plugins.markdown.ui.preview.BrowserPipe.Handler {
                override fun processMessageReceived(data: String): Boolean {
                    handleIncomingSettings(data)
                    return true
                }
            })
        } catch (_: Exception) {
        }

        connection.subscribe(PersianMarkdownSettingsListener.TOPIC, PersianMarkdownSettingsListener {
            if (isUpdatingFromJs) return@PersianMarkdownSettingsListener
            ApplicationManager.getApplication().invokeLater({
                try {
                    panel.reloadWithOffset(0)
                } catch (_: Exception) {
                }
            }, { ApplicationManager.getApplication().isDisposed })
        })
    }

    private fun handleIncomingSettings(data: String) {
        if (data.isBlank()) return
        ApplicationManager.getApplication().invokeLater {
            try {
                isUpdatingFromJs = true
                val settings = PersianMarkdownSettings.getInstance()
                val state = settings.state
                val params = data.split("&").associate { param ->
                    val parts = param.split("=", limit = 2)
                    val key = parts[0]
                    val value = if (parts.size > 1) {
                        try {
                            java.net.URLDecoder.decode(parts[1], "UTF-8")
                        } catch (_: Exception) {
                            parts[1]
                        }
                    } else ""
                    key to value
                }

                params["enabled"]?.let {
                    state.enabled = it.toBoolean()
                }
                params["mode"]?.let {
                    state.directionMode = com.persian.markdown.settings.DirectionMode.fromId(it)
                }
                params["fontSize"]?.toIntOrNull()?.let {
                    state.fontSize = it
                }
                params["lineHeight"]?.toFloatOrNull()?.let {
                    state.lineHeight = it
                }
                params["faFont"]?.takeIf { it.isNotBlank() }?.let {
                    state.fontFamily = it
                }
                params["enFont"]?.takeIf { it.isNotBlank() }?.let {
                    state.enFontFamily = it
                }
                params["codeFont"]?.takeIf { it.isNotBlank() }?.let {
                    state.codeFontFamily = it
                }
                params["frontmatter"]?.let {
                    state.renderFrontMatter = it.toBoolean()
                }

                settings.notifyChanged()
            } catch (_: Exception) {
            } finally {
                ApplicationManager.getApplication().invokeLater {
                    isUpdatingFromJs = false
                }
            }
        }
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
                val js = CssGenerator.generateAutoDirScript(state, cachedSystemFonts)
                ResourceProvider.Resource(js.toByteArray(Charsets.UTF_8), "application/javascript; charset=utf-8")
            }
            else -> null
        }
    }

    companion object {
        val cachedSystemFonts: Array<String> by lazy {
            try {
                java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .availableFontFamilyNames
                    .filter { it.isNotBlank() && !it.startsWith("@") }
                    .distinct()
                    .sorted()
                    .toTypedArray()
            } catch (_: Exception) {
                emptyArray()
            }
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
