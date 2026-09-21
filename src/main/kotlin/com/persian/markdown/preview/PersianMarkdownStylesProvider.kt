package com.persian.markdown.preview

import com.intellij.openapi.vfs.VirtualFile
import com.persian.markdown.settings.PersianMarkdownSettings
import org.intellij.plugins.markdown.ui.preview.MarkdownPreviewStylesProvider

class PersianMarkdownStylesProvider : MarkdownPreviewStylesProvider {
    override fun getStyles(file: VirtualFile): String? {
        val settings = PersianMarkdownSettings.getInstance().state
        return CssGenerator.generateCss(settings)
    }
}
