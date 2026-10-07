package com.persian.markdown.startup

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.intellij.plugins.markdown.settings.MarkdownSettings
import org.intellij.plugins.markdown.ui.preview.MarkdownHtmlPanelProvider

class PersianMarkdownStartupActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        ensureFrontMatterPreviewEnabled()
        ensureJcefPreview(project)
    }

    companion object {
        private val KNOWN_JCEF_PROVIDER_CLASSES = listOf(
            "com.intellij.markdown.jcef.preview.JCEFHtmlPanelProvider",
            "org.intellij.plugins.markdown.ui.preview.jcef.JCEFHtmlPanelProvider"
        )

        private fun isClassLoadable(className: String): Boolean {
            return try {
                Class.forName(className, false, MarkdownHtmlPanelProvider::class.java.classLoader)
                true
            } catch (_: Throwable) {
                try {
                    Class.forName(className, false, PersianMarkdownStartupActivity::class.java.classLoader)
                    true
                } catch (_: Throwable) {
                    false
                }
            }
        }

        fun resolveJcefProviderInfo(): MarkdownHtmlPanelProvider.ProviderInfo? {
            // 1. Check registered providers from the markdown extension point
            try {
                val registeredJcef = MarkdownHtmlPanelProvider.getProviders()
                    .firstOrNull { it.providerInfo.className.contains("jcef", ignoreCase = true) }
                if (registeredJcef != null) {
                    return registeredJcef.providerInfo
                }
            } catch (_: Throwable) {
            }

            // 2. Check loadable known provider classes
            for (className in KNOWN_JCEF_PROVIDER_CLASSES) {
                if (isClassLoadable(className)) {
                    return MarkdownHtmlPanelProvider.ProviderInfo("Chromium browser", className)
                }
            }

            return null
        }

        fun ensureJcefPreview(project: Project) {
            if (project.isDisposed) return
            try {
                val isJcefSupported = try {
                    com.intellij.ui.jcef.JBCefApp.isSupported()
                } catch (_: Throwable) {
                    false
                }
                if (!isJcefSupported) return

                val settings = MarkdownSettings.getInstance(project)
                val currentInfo = settings.previewPanelProviderInfo

                val resolvedJcef = resolveJcefProviderInfo() ?: return

                if (currentInfo.className == resolvedJcef.className) {
                    return
                }

                if (currentInfo.className.contains("jcef", ignoreCase = true) && isClassLoadable(currentInfo.className)) {
                    return
                }

                settings.update {
                    it.previewPanelProviderInfo = resolvedJcef
                }
            } catch (e: Exception) {
                if (e is java.util.concurrent.CancellationException) {
                    throw e
                }
            }
        }

        fun ensureFrontMatterPreviewEnabled() {
            try {
                val key = "markdown.experimental.show.frontmatter.in.preview"
                val registry = com.intellij.openapi.util.registry.Registry.get(key)
                if (!registry.asBoolean()) {
                    registry.setValue(true)
                }
            } catch (_: Exception) {
            }
        }
    }
}
