package com.persian.markdown.startup

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.intellij.plugins.markdown.settings.MarkdownSettings
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanelProvider

class PersianMarkdownStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        ensureJcefPreview(project)
    }

    companion object {
        private const val JCEF_CLASS_NAME = "com.intellij.markdown.jcef.preview.JCEFHtmlPanelProvider"

        fun ensureJcefPreview(project: Project) {
            try {
                val settings = MarkdownSettings.getInstance(project)
                val currentInfo = settings.previewPanelProviderInfo
                if (currentInfo.className != JCEF_CLASS_NAME) {
                    val jcefProvider = MarkdownHtmlPanelProvider.ProviderInfo(
                        "Chromium browser",
                        JCEF_CLASS_NAME
                    )
                    settings.update {
                        it.previewPanelProviderInfo = jcefProvider
                    }
                }
            } catch (_: Throwable) {
            }
        }
    }
}
