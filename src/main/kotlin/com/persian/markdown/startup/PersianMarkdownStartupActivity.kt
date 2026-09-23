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
        private const val JCEF_FALLBACK_CLASS_NAME = "org.intellij.plugins.markdown.ui.preview.jcef.JCEFHtmlPanelProvider"

        fun ensureJcefPreview(project: Project) {
            if (project.isDisposed) return
            try {
                val settings = MarkdownSettings.getInstance(project)
                val currentInfo = settings.previewPanelProviderInfo

                val realJcefProvider = MarkdownHtmlPanelProvider.getAvailableProviders()
                    .firstOrNull { it.providerInfo.className.contains("jcef", ignoreCase = true) }
                    ?.providerInfo
                    ?: MarkdownHtmlPanelProvider.ProviderInfo(
                        "Chromium browser",
                        JCEF_FALLBACK_CLASS_NAME
                    )

                if (currentInfo.className != realJcefProvider.className) {
                    settings.update {
                        it.previewPanelProviderInfo = realJcefProvider
                    }
                }
            } catch (e: Exception) {
                if (e is java.util.concurrent.CancellationException) {
                    throw e
                }
            }
        }
    }
}
