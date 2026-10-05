package com.persian.markdown.preview

import com.persian.markdown.settings.DirectionMode
import com.persian.markdown.settings.PersianMarkdownState
import java.util.Base64
import kotlin.math.roundToInt

object CssGenerator {

    private val bundledRegularFontBase64: String? by lazy {
        loadFontAsBase64("/fonts/Vazirmatn-Regular.ttf")
    }

    private val bundledBoldFontBase64: String? by lazy {
        loadFontAsBase64("/fonts/Vazirmatn-Bold.ttf")
    }

    private val bundledJbRegularFontBase64: String? by lazy {
        loadFontAsBase64("/fonts/JetBrainsMono-Regular.ttf")
    }

    private val bundledJbBoldFontBase64: String? by lazy {
        loadFontAsBase64("/fonts/JetBrainsMono-Bold.ttf")
    }

    private fun loadFontAsBase64(resourcePath: String): String? {
        return try {
            val stream = CssGenerator::class.java.getResourceAsStream(resourcePath) ?: return null
            val bytes = stream.use { it.readBytes() }
            Base64.getEncoder().encodeToString(bytes)
        } catch (e: Exception) {
            null
        }
    }

    private val cachedBundledVazirFontCss: String by lazy {
        buildString {
            if (bundledRegularFontBase64 != null) {
                append("""
                    @font-face {
                        font-family: 'PersianMarkdownBundledVazir';
                        src: url('data:font/truetype;charset=utf-8;base64,${bundledRegularFontBase64}') format('truetype');
                        font-weight: 400;
                        font-style: normal;
                    }
                """.trimIndent()).append("\n")

                if (bundledBoldFontBase64 != null) {
                    append("""
                        @font-face {
                            font-family: 'PersianMarkdownBundledVazir';
                            src: url('data:font/truetype;charset=utf-8;base64,${bundledBoldFontBase64}') format('truetype');
                            font-weight: 700;
                            font-style: normal;
                        }
                    """.trimIndent()).append("\n")
                }
            }
        }
    }

    private val cachedBundledJbFontCss: String by lazy {
        buildString {
            if (bundledJbRegularFontBase64 != null) {
                append("""
                    @font-face {
                        font-family: 'PersianMarkdownBundledJBMono';
                        src: url('data:font/truetype;charset=utf-8;base64,${bundledJbRegularFontBase64}') format('truetype');
                        font-weight: 400;
                        font-style: normal;
                    }
                """.trimIndent()).append("\n")

                if (bundledJbBoldFontBase64 != null) {
                    append("""
                        @font-face {
                            font-family: 'PersianMarkdownBundledJBMono';
                            src: url('data:font/truetype;charset=utf-8;base64,${bundledJbBoldFontBase64}') format('truetype');
                            font-weight: 700;
                            font-style: normal;
                        }
                    """.trimIndent()).append("\n")
                }
            }
        }
    }

    fun generateCss(state: PersianMarkdownState): String {
        val sb = StringBuilder()

        if (state.useBundledFont) {
            sb.append(cachedBundledVazirFontCss)
        }
        sb.append(cachedBundledJbFontCss)

        val faFontStack = buildString {
            if (state.useBundledFont && bundledRegularFontBase64 != null) {
                append("'PersianMarkdownBundledVazir', ")
            }
            if (!state.fontFamily.isNullOrBlank()) {
                append("${state.fontFamily}, ")
            }
            append("'Vazirmatn', -apple-system, BlinkMacSystemFont, 'Segoe UI', Tahoma, sans-serif")
        }

        val enFontStack = buildString {
            if (bundledJbRegularFontBase64 != null) {
                append("'PersianMarkdownBundledJBMono', ")
            }
            if (!state.enFontFamily.isNullOrBlank()) {
                append("${state.enFontFamily}, ")
            }
            append("'JetBrains Mono', 'SF Pro', Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif")
        }

        val codeFontStack = buildString {
            if (bundledJbRegularFontBase64 != null) {
                append("'PersianMarkdownBundledJBMono', ")
            }
            if (!state.codeFontFamily.isNullOrBlank()) {
                append("${state.codeFontFamily}, ")
            }
            append("'JetBrains Mono', Menlo, Monaco, Consolas, monospace")
        }

        val fs = state.fontSize
        val lh = state.lineHeight

        sb.append("""
            :root {
                --pm-font-size: ${fs}px;
                --pm-line-height: $lh;
                --pm-fa-font: $faFontStack;
                --pm-en-font: $enFontStack;
                --pm-code-font: $codeFontStack;
            }
            body:not(#persian-markdown-switcher) {
                padding-bottom: 85px !important;
            }
            html:not(.pm-disabled) p:not(#persian-markdown-switcher *),
            html:not(.pm-disabled) li:not(#persian-markdown-switcher *),
            html:not(.pm-disabled) blockquote:not(#persian-markdown-switcher *),
            html:not(.pm-disabled) table:not(#persian-markdown-switcher *),
            html:not(.pm-disabled) td:not(#persian-markdown-switcher *),
            html:not(.pm-disabled) th:not(#persian-markdown-switcher *) {
                font-family: $faFontStack !important;
                font-family: var(--pm-fa-font, $faFontStack) !important;
                font-size: ${fs}px !important;
                font-size: var(--pm-font-size, ${fs}px) !important;
                line-height: $lh !important;
                line-height: var(--pm-line-height, $lh) !important;
            }
        """.trimIndent()).append("\n")

        if (state.enhanceHeadings) {
            sb.append("""
                html:not(.pm-disabled) h1:not(#persian-markdown-switcher *),
                html:not(.pm-disabled) h2:not(#persian-markdown-switcher *),
                html:not(.pm-disabled) h3:not(#persian-markdown-switcher *),
                html:not(.pm-disabled) h4:not(#persian-markdown-switcher *),
                html:not(.pm-disabled) h5:not(#persian-markdown-switcher *),
                html:not(.pm-disabled) h6:not(#persian-markdown-switcher *) {
                    font-family: var(--pm-fa-font, $faFontStack) !important;
                }
                html:not(.pm-disabled) h1:not(#persian-markdown-switcher *) { font-size: calc(var(--pm-font-size, ${fs}px) * 1.75) !important; line-height: 1.3 !important; }
                html:not(.pm-disabled) h2:not(#persian-markdown-switcher *) { font-size: calc(var(--pm-font-size, ${fs}px) * 1.50) !important; line-height: 1.35 !important; }
                html:not(.pm-disabled) h3:not(#persian-markdown-switcher *) { font-size: calc(var(--pm-font-size, ${fs}px) * 1.25) !important; line-height: 1.4 !important; }
                html:not(.pm-disabled) h4:not(#persian-markdown-switcher *) { font-size: calc(var(--pm-font-size, ${fs}px) * 1.12) !important; line-height: 1.4 !important; }
                html:not(.pm-disabled) h5:not(#persian-markdown-switcher *), html:not(.pm-disabled) h6:not(#persian-markdown-switcher *) { font-size: var(--pm-font-size, ${fs}px) !important; line-height: 1.4 !important; }
            """.trimIndent()).append("\n")
        }

        // Direction rules driven by [data-pm-mode] on <html> and active BiDi markers
        sb.append("""
            /* --- Mode: Force RTL --- */
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) body,
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) p:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) h1:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) h2:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) h3:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) h4:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) h5:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) h6:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) ul:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) ol:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) blockquote:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) table:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) tr:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) td:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) th:not(#persian-markdown-switcher *) {
                direction: rtl !important;
                text-align: right !important;
                font-family: var(--pm-fa-font, $faFontStack) !important;
            }
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) ul:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) ol:not(#persian-markdown-switcher *) {
                padding-right: 1.8em !important;
                padding-left: 0 !important;
            }
            html[data-pm-mode="force_rtl"]:not(.pm-disabled) blockquote:not(#persian-markdown-switcher *) {
                border-right: 4px solid #1EB4EB !important;
                border-left: none !important;
                padding-right: 1em !important;
                padding-left: 0 !important;
            }

            /* --- Mode: Force LTR --- */
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) body,
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) p:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) h1:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) h2:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) h3:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) h4:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) h5:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) h6:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) ul:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) ol:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) blockquote:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) table:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) tr:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) td:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) th:not(#persian-markdown-switcher *) {
                direction: ltr !important;
                text-align: left !important;
                font-family: var(--pm-en-font, $enFontStack) !important;
            }
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) ul:not(#persian-markdown-switcher *),
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) ol:not(#persian-markdown-switcher *) {
                padding-left: 1.8em !important;
                padding-right: 0 !important;
            }
            html[data-pm-mode="force_ltr"]:not(.pm-disabled) blockquote:not(#persian-markdown-switcher *) {
                border-left: 4px solid #1EB4EB !important;
                border-right: none !important;
                padding-left: 1em !important;
                padding-right: 0 !important;
            }

            /* --- Mode: Auto BiDi (Default) --- */
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) [dir="rtl"]:not(#persian-markdown-switcher):not(#persian-markdown-switcher *),
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) .persian-dir-rtl:not(#persian-markdown-switcher):not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) [dir="rtl"]:not(#persian-markdown-switcher):not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) .persian-dir-rtl:not(#persian-markdown-switcher):not(#persian-markdown-switcher *) {
                direction: rtl !important;
                text-align: right !important;
                font-family: var(--pm-fa-font, $faFontStack) !important;
            }
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) [dir="ltr"]:not(#persian-markdown-switcher):not(#persian-markdown-switcher *),
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) .persian-dir-ltr:not(#persian-markdown-switcher):not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) [dir="ltr"]:not(#persian-markdown-switcher):not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) .persian-dir-ltr:not(#persian-markdown-switcher):not(#persian-markdown-switcher *) {
                direction: ltr !important;
                text-align: left !important;
                font-family: var(--pm-en-font, $enFontStack) !important;
            }
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) blockquote[dir="rtl"]:not(#persian-markdown-switcher *),
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) blockquote.persian-dir-rtl:not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) blockquote[dir="rtl"]:not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) blockquote.persian-dir-rtl:not(#persian-markdown-switcher *) {
                border-right: 4px solid #1EB4EB !important;
                border-left: none !important;
                padding-right: 1em !important;
                padding-left: 0 !important;
            }
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) ul[dir="rtl"]:not(#persian-markdown-switcher *),
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) ol[dir="rtl"]:not(#persian-markdown-switcher *),
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) ul.persian-dir-rtl:not(#persian-markdown-switcher *),
            html:not([data-pm-mode="force_rtl"]):not([data-pm-mode="force_ltr"]):not(.pm-disabled) ol.persian-dir-rtl:not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) ul[dir="rtl"]:not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) ol[dir="rtl"]:not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) ul.persian-dir-rtl:not(#persian-markdown-switcher *),
            html[data-pm-mode="auto"]:not(.pm-disabled) ol.persian-dir-rtl:not(#persian-markdown-switcher *) {
                padding-right: 1.8em !important;
                padding-left: 0 !important;
            }
        """.trimIndent()).append("\n")

        // Code blocks: strictly LTR, left-aligned, isolated
        sb.append("""
            pre, .code-fence, .markdown-code-fence {
                direction: ltr !important;
                text-align: left !important;
                unicode-bidi: isolate !important;
                font-family: var(--pm-code-font, $codeFontStack) !important;
                margin-left: 0 !important;
                margin-right: auto !important;
                line-height: 1.45 !important;
            }
            pre code {
                direction: ltr !important;
                text-align: left !important;
                unicode-bidi: isolate !important;
                font-family: var(--pm-code-font, $codeFontStack) !important;
            }
            code, kbd, samp, tt {
                unicode-bidi: isolate !important;
                font-family: var(--pm-code-font, $codeFontStack) !important;
            }
            a:not(#persian-markdown-switcher *) {
                unicode-bidi: isolate !important;
            }

            /* --- Complete styling deactivation when disabled --- */
            html.pm-disabled p:not(#persian-markdown-switcher *),
            html.pm-disabled h1:not(#persian-markdown-switcher *),
            html.pm-disabled h2:not(#persian-markdown-switcher *),
            html.pm-disabled h3:not(#persian-markdown-switcher *),
            html.pm-disabled h4:not(#persian-markdown-switcher *),
            html.pm-disabled h5:not(#persian-markdown-switcher *),
            html.pm-disabled h6:not(#persian-markdown-switcher *),
            html.pm-disabled li:not(#persian-markdown-switcher *),
            html.pm-disabled blockquote:not(#persian-markdown-switcher *),
            html.pm-disabled table:not(#persian-markdown-switcher *),
            html.pm-disabled tr:not(#persian-markdown-switcher *),
            html.pm-disabled td:not(#persian-markdown-switcher *),
            html.pm-disabled th:not(#persian-markdown-switcher *),
            html.pm-disabled pre:not(#persian-markdown-switcher *),
            html.pm-disabled code:not(#persian-markdown-switcher *),
            html.pm-disabled kbd:not(#persian-markdown-switcher *),
            html.pm-disabled samp:not(#persian-markdown-switcher *),
            html.pm-disabled tt:not(#persian-markdown-switcher *) {
                direction: inherit !important;
                text-align: inherit !important;
                font-family: inherit !important;
                font-size: inherit !important;
                line-height: inherit !important;
            }
            html.pm-disabled blockquote:not(#persian-markdown-switcher *) {
                border-left: inherit !important;
                border-right: none !important;
                padding-left: inherit !important;
                padding-right: inherit !important;
            }
            html.pm-disabled ul:not(#persian-markdown-switcher *),
            html.pm-disabled ol:not(#persian-markdown-switcher *) {
                padding-left: inherit !important;
                padding-right: inherit !important;
            }

            /* --- Front Matter Metadata Card & Table Styling --- */
            .pm-frontmatter-container {
                margin: 16px 0 28px 0 !important;
                border: 1px solid rgba(142, 217, 245, 0.22) !important;
                background: rgba(17, 23, 34, 0.75) !important;
                border-radius: 10px !important;
                overflow: hidden !important;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25), 0 0 0 1px rgba(255, 255, 255, 0.04) !important;
                backdrop-filter: blur(12px) !important;
                -webkit-backdrop-filter: blur(12px) !important;
                font-size: var(--pm-font-size, 15px) !important;
                direction: ltr !important;
                text-align: left !important;
            }
            .pm-frontmatter-container.pm-fm-hidden {
                display: none !important;
            }
            .pm-fm-header {
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                padding: 7px 12px !important;
                background: rgba(26, 33, 46, 0.85) !important;
                border-bottom: 1px solid rgba(142, 217, 245, 0.15) !important;
                user-select: none !important;
            }
            .pm-fm-title-group {
                display: flex !important;
                align-items: center !important;
                gap: 8px !important;
            }
            .pm-fm-icon {
                display: inline-flex !important;
                align-items: center !important;
                justify-content: center !important;
                color: #1EB4EB !important;
                width: 16px !important;
                height: 16px !important;
            }
            .pm-fm-title {
                font-family: var(--pm-code-font, monospace) !important;
                font-size: 11.5px !important;
                font-weight: 600 !important;
                color: #8ED9F5 !important;
                letter-spacing: 0.04em !important;
                text-transform: uppercase !important;
            }
            .pm-fm-actions {
                display: flex !important;
                align-items: center !important;
                gap: 6px !important;
            }
            .pm-fm-btn {
                background: rgba(255, 255, 255, 0.06) !important;
                border: 1px solid rgba(255, 255, 255, 0.1) !important;
                color: #94A3B8 !important;
                font-family: var(--pm-code-font, monospace) !important;
                font-size: 11px !important;
                padding: 2px 8px !important;
                border-radius: 5px !important;
                cursor: pointer !important;
                transition: all 0.15s ease !important;
                display: inline-flex !important;
                align-items: center !important;
                gap: 4px !important;
                line-height: 1.4 !important;
            }
            .pm-fm-btn:hover {
                background: rgba(30, 180, 235, 0.2) !important;
                color: #F0F9FF !important;
                border-color: rgba(30, 180, 235, 0.4) !important;
            }
            .pm-fm-btn.pm-active {
                background: #1EB4EB !important;
                color: #0B111A !important;
                font-weight: 600 !important;
                border-color: #1EB4EB !important;
            }
            .pm-fm-content {
                padding: 0 !important;
                overflow-x: auto !important;
            }
            .pm-fm-table {
                width: 100% !important;
                border-collapse: collapse !important;
                margin: 0 !important;
                font-size: 13px !important;
            }
            .pm-fm-table tr {
                border-bottom: 1px solid rgba(255, 255, 255, 0.06) !important;
                transition: background 0.12s ease !important;
            }
            .pm-fm-table tr:last-child {
                border-bottom: none !important;
            }
            .pm-fm-table tr:hover {
                background: rgba(255, 255, 255, 0.02) !important;
            }
            .pm-fm-key-td {
                width: 28% !important;
                min-width: 120px !important;
                max-width: 200px !important;
                padding: 7px 12px !important;
                font-family: var(--pm-code-font, monospace) !important;
                font-size: 12px !important;
                color: #7DD3FC !important;
                background: rgba(14, 20, 31, 0.5) !important;
                border-right: 1px solid rgba(255, 255, 255, 0.06) !important;
                vertical-align: top !important;
                direction: ltr !important;
                text-align: left !important;
                word-break: break-all !important;
            }
            .pm-fm-val-td {
                padding: 7px 14px !important;
                vertical-align: top !important;
                color: #E2E8F0 !important;
                line-height: 1.5 !important;
            }
            .pm-fm-val-td[dir="rtl"] {
                direction: rtl !important;
                text-align: right !important;
                font-family: var(--pm-fa-font) !important;
            }
            .pm-fm-val-td[dir="ltr"] {
                direction: ltr !important;
                text-align: left !important;
                font-family: var(--pm-en-font) !important;
            }
            .pm-fm-badge {
                display: inline-block !important;
                padding: 2px 7px !important;
                border-radius: 4px !important;
                background: rgba(30, 180, 235, 0.15) !important;
                border: 1px solid rgba(30, 180, 235, 0.3) !important;
                color: #BAE6FD !important;
                font-size: 11.5px !important;
                margin: 2px 3px !important;
                line-height: 1.3 !important;
            }
            .pm-fm-badge[dir="rtl"] {
                direction: rtl !important;
                font-family: var(--pm-fa-font) !important;
            }
            .pm-fm-badge[dir="ltr"] {
                direction: ltr !important;
                font-family: var(--pm-en-font) !important;
            }
            .pm-fm-raw {
                display: none !important;
                margin: 0 !important;
                padding: 10px 14px !important;
                background: rgba(10, 14, 22, 0.9) !important;
                font-family: var(--pm-code-font, monospace) !important;
                font-size: 12.5px !important;
                color: #94A3B8 !important;
                white-space: pre !important;
                direction: ltr !important;
                text-align: left !important;
                overflow-x: auto !important;
            }
            .pm-frontmatter-container.pm-view-raw .pm-fm-table {
                display: none !important;
            }
            .pm-frontmatter-container.pm-view-raw .pm-fm-raw {
                display: block !important;
            }
            .pm-fm-text-body {
                padding: 10px 14px !important;
                color: #E2E8F0 !important;
                font-size: var(--pm-font-size, 15px) !important;
                line-height: var(--pm-line-height, 1.6) !important;
                margin: 0 !important;
            }
            .pm-fm-text-body[dir="rtl"] {
                direction: rtl !important;
                text-align: right !important;
                font-family: var(--pm-fa-font) !important;
            }
            .pm-fm-text-body[dir="ltr"] {
                direction: ltr !important;
                text-align: left !important;
                font-family: var(--pm-en-font) !important;
            }
            pre.frontmatter-header:not(.pm-wrapped) {
                display: none !important;
            }
        """.trimIndent()).append("\n")

        // Styles for Linear / Raycast Bento Switcher in preview (bottom-left popup)
        sb.append("""
            /* --- Mermaid Diagram Card & Container Styling --- */
            .pm-mermaid-container {
                margin: 20px 0 28px 0 !important;
                border: 1px solid rgba(142, 217, 245, 0.22) !important;
                background: rgba(17, 23, 34, 0.75) !important;
                border-radius: 10px !important;
                overflow: hidden !important;
                box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25), 0 0 0 1px rgba(255, 255, 255, 0.04) !important;
                backdrop-filter: blur(12px) !important;
                -webkit-backdrop-filter: blur(12px) !important;
                direction: ltr !important;
                text-align: left !important;
                transition: border-color 0.2s ease, box-shadow 0.2s ease !important;
            }
            .pm-mermaid-container:hover {
                border-color: rgba(30, 180, 235, 0.38) !important;
                box-shadow: 0 6px 24px rgba(0, 0, 0, 0.32), 0 0 0 1px rgba(30, 180, 235, 0.1) !important;
            }
            .pm-mermaid-container.pm-mermaid-hidden {
                display: none !important;
            }
            .pm-mermaid-header {
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                padding: 7px 12px !important;
                background: rgba(26, 33, 46, 0.85) !important;
                border-bottom: 1px solid rgba(142, 217, 245, 0.15) !important;
                user-select: none !important;
            }
            .pm-mermaid-title-group {
                display: flex !important;
                align-items: center !important;
                gap: 8px !important;
            }
            .pm-mermaid-icon {
                display: inline-flex !important;
                align-items: center !important;
                justify-content: center !important;
                color: #1EB4EB !important;
                width: 16px !important;
                height: 16px !important;
            }
            .pm-mermaid-title {
                font-family: var(--pm-code-font, monospace) !important;
                font-size: 11.5px !important;
                font-weight: 600 !important;
                color: #8ED9F5 !important;
                letter-spacing: 0.04em !important;
                text-transform: uppercase !important;
            }
            .pm-mermaid-lang-badge {
                font-family: var(--pm-code-font, monospace) !important;
                font-size: 10px !important;
                font-weight: 600 !important;
                padding: 1px 6px !important;
                border-radius: 4px !important;
                background: rgba(255, 255, 255, 0.06) !important;
                color: #94A3B8 !important;
                border: 1px solid rgba(255, 255, 255, 0.08) !important;
            }
            .pm-mermaid-lang-badge.pm-fa {
                color: #38BDF8 !important;
                background: rgba(56, 189, 248, 0.12) !important;
                border-color: rgba(56, 189, 248, 0.25) !important;
            }
            .pm-mermaid-status {
                font-size: 10.5px !important;
                color: #F59E0B !important;
                display: none !important;
                align-items: center !important;
                gap: 4px !important;
            }
            .pm-mermaid-status.pm-visible {
                display: inline-flex !important;
            }
            .pm-mermaid-zoom-bar {
                display: inline-flex !important;
                align-items: center !important;
                background: rgba(15, 23, 42, 0.75) !important;
                border: 1px solid rgba(142, 217, 245, 0.2) !important;
                border-radius: 6px !important;
                padding: 1px 3px !important;
                gap: 2px !important;
            }
            .pm-mermaid-zoom-btn {
                width: 22px !important;
                height: 22px !important;
                border-radius: 4px !important;
                background: transparent !important;
                color: #94A3B8 !important;
                border: none !important;
                cursor: pointer !important;
                font-size: 14px !important;
                font-weight: 600 !important;
                line-height: 1 !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
                transition: all 0.12s ease !important;
                padding: 0 !important;
                user-select: none !important;
            }
            .pm-mermaid-zoom-btn:hover {
                color: #FFFFFF !important;
                background: rgba(30, 180, 235, 0.25) !important;
            }
            .pm-mermaid-zoom-btn:active {
                transform: scale(0.9) !important;
            }
            .pm-mermaid-zoom-val {
                font-family: var(--pm-code-font, monospace) !important;
                font-size: 10.5px !important;
                font-weight: 500 !important;
                color: #8ED9F5 !important;
                padding: 2px 5px !important;
                cursor: pointer !important;
                user-select: none !important;
                min-width: 38px !important;
                text-align: center !important;
                border-radius: 3px !important;
                transition: all 0.12s ease !important;
                line-height: 1.2 !important;
            }
            .pm-mermaid-zoom-val:hover {
                background: rgba(30, 180, 235, 0.2) !important;
                color: #FFFFFF !important;
            }
            .pm-mermaid-zoom-val.pm-zoomed {
                color: #38BDF8 !important;
                font-weight: 600 !important;
            }
            .pm-mermaid-body {
                padding: 16px !important;
                position: relative !important;
            }
            .pm-mermaid-svg-wrap {
                display: flex !important;
                justify-content: center !important;
                align-items: center !important;
                overflow-x: auto !important;
                min-height: 60px !important;
            }
            .pm-mermaid-svg-wrap svg {
                max-width: 100% !important;
                height: auto !important;
                display: block !important;
            }
            .pm-mermaid-error-banner {
                margin-top: 10px !important;
                padding: 8px 12px !important;
                background: rgba(239, 68, 68, 0.15) !important;
                border: 1px solid rgba(239, 68, 68, 0.3) !important;
                border-radius: 6px !important;
                color: #FCA5A5 !important;
                font-size: 12px !important;
                font-family: var(--pm-code-font, monospace) !important;
                display: none !important;
            }
            .pm-mermaid-error-banner.pm-visible {
                display: block !important;
            }

            /* --- Isolated Floating Switcher & Dialog --- */
            html {
                transform: none !important;
                filter: none !important;
                contain: none !important;
            }
            #persian-markdown-switcher,
            #persian-markdown-switcher * {
                box-sizing: border-box !important;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif !important;
                letter-spacing: normal !important;
                text-transform: none !important;
                direction: ltr !important;
                text-align: left !important;
            }
            #persian-markdown-switcher {
                position: fixed !important;
                bottom: 14px !important;
                left: 14px !important;
                right: auto !important;
                top: auto !important;
                z-index: 2147483647 !important;
                user-select: none !important;
                -webkit-user-select: none !important;
                line-height: 1.2 !important;
                transform: none !important;
                margin: 0 !important;
            }

            /* Floating Trigger Rounded Button (Concentric 10px radius matching squircle icon) */
            #pm-trigger {
                position: relative !important;
                display: inline-flex !important;
                align-items: center !important;
                gap: 8px !important;
                padding: 4px 13px 4px 5px !important;
                height: 32px !important;
                border-radius: 10px !important;
                background: #111722 !important;
                border: 1px solid rgba(30, 180, 235, 0.35) !important;
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.7), 0 2px 8px rgba(30, 180, 235, 0.15), inset 0 1px 0 rgba(142, 217, 245, 0.12) !important;
                cursor: pointer !important;
                transition: all 0.15s ease !important;
                outline: none !important;
                user-select: none !important;
            }
            #pm-trigger:hover {
                border-color: #1EB4EB !important;
                background: #162030 !important;
                box-shadow: 0 10px 28px rgba(0, 0, 0, 0.75), 0 3px 12px rgba(30, 180, 235, 0.35), inset 0 1px 0 rgba(142, 217, 245, 0.22) !important;
                transform: translateY(-1px) !important;
            }
            #pm-trigger:active {
                transform: scale(0.97) !important;
            }
            #persian-markdown-switcher.pm-open #pm-trigger {
                border-color: #1EB4EB !important;
                background: #162030 !important;
                box-shadow: 0 0 16px rgba(30, 180, 235, 0.45), 0 8px 24px rgba(0, 0, 0, 0.7) !important;
            }

            /* Squircle Icon Box with Align Vertical Top SVG */
            #pm-status-dot {
                width: 22px !important;
                height: 22px !important;
                border-radius: 6.5px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
                flex-shrink: 0 !important;
                transition: all 0.2s ease !important;
                background: transparent !important;
                border: none !important;
                box-shadow: none !important;
                overflow: visible !important;
            }
            #pm-status-dot svg {
                width: 22px !important;
                height: 22px !important;
                display: block !important;
                transition: transform 0.18s ease, filter 0.2s ease !important;
                filter: drop-shadow(0 0 5px rgba(30, 180, 235, 0.45)) !important;
            }
            #pm-trigger:hover #pm-status-dot:not(.pm-disabled-dot) svg {
                transform: scale(1.08) !important;
                filter: drop-shadow(0 0 9px rgba(30, 180, 235, 0.8)) !important;
            }
            #pm-status-dot.pm-disabled-dot {
                filter: grayscale(1) opacity(0.35) !important;
            }

            /* Markdown RTL Text Label */
            #pm-current-label {
                font-size: 12px !important;
                font-weight: 600 !important;
                color: #F0F9FF !important;
                letter-spacing: -0.01em !important;
                white-space: nowrap !important;
                line-height: 1 !important;
            }
            #pm-status-dot.pm-disabled-dot ~ #pm-current-label {
                color: #94A3B8 !important;
            }

            /* Popover Bento Card */
            #pm-card {
                position: absolute !important;
                bottom: calc(100% + 9px) !important;
                left: 0 !important;
                width: 285px !important;
                max-width: calc(100vw - 24px) !important;
                background: rgba(13, 17, 25, 0.97) !important;
                backdrop-filter: blur(20px) saturate(180%) !important;
                -webkit-backdrop-filter: blur(20px) saturate(180%) !important;
                border-radius: 13px !important;
                border: 1px solid #202737 !important;
                box-shadow: 0 20px 48px rgba(0, 0, 0, 0.75), 0 0 0 1px rgba(255, 255, 255, 0.06) !important;
                display: flex !important;
                flex-direction: column !important;
                opacity: 0 !important;
                visibility: hidden !important;
                transform: translateY(6px) scale(0.97) !important;
                transform-origin: bottom left !important;
                transition: opacity 0.18s ease, transform 0.18s ease, visibility 0.18s !important;
                pointer-events: none !important;
                color: #F1F5F9 !important;
                overflow: visible !important;
            }
            #pm-card::before {
                content: '' !important;
                position: absolute !important;
                top: 0 !important;
                left: 0 !important;
                right: 0 !important;
                height: 1px !important;
                background: linear-gradient(to right, transparent, rgba(30, 180, 235, 0.5), transparent) !important;
                pointer-events: none !important;
                border-radius: 13px 13px 0 0 !important;
            }
            #persian-markdown-switcher.pm-open #pm-card {
                opacity: 1 !important;
                visibility: visible !important;
                transform: translateY(0) scale(1) !important;
                pointer-events: auto !important;
            }

            /* Header */
            #pm-header {
                padding: 7px 10px !important;
                border-bottom: 1px solid #1A212E !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                background: rgba(16, 20, 29, 0.8) !important;
                border-radius: 13px 13px 0 0 !important;
            }
            .pm-header-left {
                display: flex !important;
                align-items: center !important;
                gap: 7px !important;
            }
            .pm-brand-icon-box {
                width: 20px !important;
                height: 20px !important;
                border-radius: 6px !important;
                background: linear-gradient(135deg, rgba(30, 180, 235, 0.18), rgba(142, 217, 245, 0.1)) !important;
                border: 1px solid rgba(30, 180, 235, 0.35) !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
                flex-shrink: 0 !important;
            }
            .pm-brand-icon-box svg {
                width: 11px !important;
                height: 11px !important;
                color: #1EB4EB !important;
            }
            .pm-header-title {
                font-size: 13.5px !important;
                font-weight: 600 !important;
                color: #F1F5F9 !important;
                line-height: 1 !important;
            }
            .pm-header-badge {
                padding: 2px 6px !important;
                background: rgba(30, 180, 235, 0.15) !important;
                border: 1px solid rgba(30, 180, 235, 0.3) !important;
                font-size: 10.5px !important;
                font-family: 'JetBrains Mono', monospace !important;
                font-weight: 500 !important;
                color: #8ED9F5 !important;
                border-radius: 4px !important;
                line-height: 1 !important;
            }
            #pm-close-btn {
                height: 22px !important;
                padding: 0 7px !important;
                border-radius: 4px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
                color: #94A3B8 !important;
                background: transparent !important;
                border: 1px solid transparent !important;
                font-family: 'JetBrains Mono', monospace !important;
                font-size: 11px !important;
                cursor: pointer !important;
                transition: all 0.15s !important;
                line-height: 1 !important;
            }
            #pm-close-btn:hover {
                background: rgba(30, 41, 59, 0.6) !important;
                color: #F1F5F9 !important;
                border-color: rgba(51, 65, 85, 0.5) !important;
            }

            /* Card Body */
            #pm-body {
                padding: 7px 8px !important;
                display: flex !important;
                flex-direction: column !important;
                gap: 6px !important;
                overflow: visible !important;
            }

            /* Master Enable Card */
            .pm-master-card {
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                padding: 5px 9px !important;
                height: 31px !important;
                border-radius: 7px !important;
                background: #121722 !important;
                border: 1px solid #1E2536 !important;
            }
            .pm-master-label-wrap {
                display: flex !important;
                align-items: center !important;
                gap: 6px !important;
            }
            .pm-master-title {
                font-size: 11.5px !important;
                font-weight: 500 !important;
                color: #F1F5F9 !important;
                line-height: 1 !important;
            }

            /* Controls Container (which dims when disabled) */
            #pm-controls-wrap {
                display: flex !important;
                flex-direction: column !important;
                gap: 6px !important;
                transition: opacity 0.2s ease, filter 0.2s ease !important;
            }
            #pm-controls-wrap.pm-controls-disabled {
                opacity: 0.35 !important;
                pointer-events: none !important;
                filter: grayscale(0.6) !important;
            }

            /* Direction Mode 3-Segment Tab Bar */
            .pm-tabs-bar {
                display: flex !important;
                align-items: center !important;
                background: #0E131E !important;
                border: 1px solid #1E2536 !important;
                border-radius: 8px !important;
                padding: 2.5px !important;
                gap: 3px !important;
                position: relative !important;
            }
            .pm-tab-btn {
                flex: 1 !important;
                display: inline-flex !important;
                align-items: center !important;
                justify-content: center !important;
                gap: 5px !important;
                height: 27px !important;
                padding: 0 5px !important;
                border-radius: 6px !important;
                background: transparent !important;
                border: 1px solid transparent !important;
                color: #8292A6 !important;
                font-size: 11.5px !important;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif !important;
                font-weight: 500 !important;
                cursor: pointer !important;
                transition: all 0.15s ease !important;
                outline: none !important;
                user-select: none !important;
                white-space: nowrap !important;
                position: relative !important;
            }
            .pm-tab-btn:hover {
                color: #F1F5F9 !important;
                background: rgba(255, 255, 255, 0.04) !important;
            }
            .pm-tab-btn.pm-active {
                background: #162031 !important;
                color: #F0F9FF !important;
                border: 1px solid #1EB4EB !important;
                box-shadow: 0 0 10px rgba(30, 180, 235, 0.25) !important;
                font-weight: 600 !important;
            }

            /* Info Icon (i) & Tooltip */
            .pm-info-icon {
                width: 13px !important;
                height: 13px !important;
                display: inline-flex !important;
                align-items: center !important;
                justify-content: center !important;
                border-radius: 50% !important;
                background: rgba(255, 255, 255, 0.08) !important;
                color: #8292A6 !important;
                font-size: 9px !important;
                font-family: 'JetBrains Mono', monospace !important;
                font-weight: 700 !important;
                line-height: 1 !important;
                flex-shrink: 0 !important;
                cursor: help !important;
                transition: all 0.15s ease !important;
            }
            .pm-tab-btn:hover .pm-info-icon,
            .pm-info-icon:hover {
                background: rgba(30, 180, 235, 0.25) !important;
                color: #1EB4EB !important;
            }

            .pm-tooltip {
                position: absolute !important;
                bottom: calc(100% + 7px) !important;
                background: #090D14 !important;
                color: #E2E8F0 !important;
                border: 1px solid #232D40 !important;
                border-radius: 6px !important;
                padding: 6px 9px !important;
                font-size: 10.5px !important;
                font-weight: 400 !important;
                line-height: 1.35 !important;
                white-space: normal !important;
                width: 185px !important;
                text-align: center !important;
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.85) !important;
                pointer-events: none !important;
                opacity: 0 !important;
                visibility: hidden !important;
                transform: translateY(4px) scale(0.96) !important;
                transition: opacity 0.15s ease, transform 0.15s ease, visibility 0.15s !important;
                z-index: 1000 !important;
            }
            .pm-tooltip::after {
                content: '' !important;
                position: absolute !important;
                top: 100% !important;
                border-width: 4px !important;
                border-style: solid !important;
                border-color: #232D40 transparent transparent transparent !important;
            }

            /* Tooltip positioning per tab to prevent overflow */
            #pm-tab-auto .pm-tooltip {
                left: 0 !important;
                transform: translateY(4px) scale(0.96) !important;
            }
            #pm-tab-auto:hover .pm-tooltip,
            #pm-tab-auto .pm-info-icon:hover .pm-tooltip {
                opacity: 1 !important;
                visibility: visible !important;
                transform: translateY(0) scale(1) !important;
            }
            #pm-tab-auto .pm-tooltip::after {
                left: 20px !important;
            }

            #pm-tab-force-rtl .pm-tooltip {
                left: 50% !important;
                transform: translateX(-50%) translateY(4px) scale(0.96) !important;
            }
            #pm-tab-force-rtl:hover .pm-tooltip,
            #pm-tab-force-rtl .pm-info-icon:hover .pm-tooltip {
                opacity: 1 !important;
                visibility: visible !important;
                transform: translateX(-50%) translateY(0) scale(1) !important;
            }
            #pm-tab-force-rtl .pm-tooltip::after {
                left: 50% !important;
                transform: translateX(-50%) !important;
            }

            #pm-tab-force-ltr .pm-tooltip {
                right: 0 !important;
                left: auto !important;
                transform: translateY(4px) scale(0.96) !important;
            }
            #pm-tab-force-ltr:hover .pm-tooltip,
            #pm-tab-force-ltr .pm-info-icon:hover .pm-tooltip {
                opacity: 1 !important;
                visibility: visible !important;
                transform: translateY(0) scale(1) !important;
            }
            #pm-tab-force-ltr .pm-tooltip::after {
                right: 20px !important;
                left: auto !important;
            }
            .pm-kbd-tag {
                padding: 0 3.5px !important;
                border-radius: 3px !important;
                background: #1B2130 !important;
                font-size: 9.5px !important;
                font-family: 'JetBrains Mono', monospace !important;
                color: #8292A6 !important;
                border: 1px solid #242E42 !important;
                line-height: 1.2 !important;
            }

            /* Compact Switch */
            .pm-switch {
                position: relative !important;
                display: inline-block !important;
                width: 27px !important;
                height: 16px !important;
                flex-shrink: 0 !important;
                cursor: pointer !important;
                margin: 0 !important;
            }
            .pm-switch input {
                position: absolute !important;
                opacity: 0 !important;
                width: 0 !important;
                height: 0 !important;
                margin: 0 !important;
            }
            .pm-switch-track {
                position: absolute !important;
                top: 0 !important; left: 0 !important; right: 0 !important; bottom: 0 !important;
                background: #232B3B !important;
                border-radius: 9999px !important;
                transition: background 0.15s ease !important;
            }
            .pm-switch-track::after {
                content: '' !important;
                position: absolute !important;
                top: 2px !important;
                left: 2px !important;
                width: 12px !important;
                height: 12px !important;
                background: #FFFFFF !important;
                border-radius: 50% !important;
                transition: transform 0.15s ease !important;
                box-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            }
            .pm-switch input:checked + .pm-switch-track {
                background: #1EB4EB !important;
            }
            .pm-switch input:checked + .pm-switch-track::after {
                transform: translateX(11px) !important;
            }

            /* Section Title - Clean, subtle */
            .pm-section-title {
                font-size: 11px !important;
                font-weight: 500 !important;
                color: #64748B !important;
                padding: 2px 2px 0 2px !important;
                letter-spacing: normal !important;
                line-height: 1 !important;
            }

            /* Bento Card Container */
            .pm-bento-card {
                background: #121722 !important;
                border-radius: 7px !important;
                border: 1px solid #1E2536 !important;
                overflow: visible !important;
            }

            /* Searchable Font Combobox Row */
            .pm-font-combobox {
                position: relative !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                gap: 6px !important;
                padding: 5px 8px !important;
                border-bottom: 1px solid #181F2C !important;
            }
            .pm-font-combobox:last-child {
                border-bottom: none !important;
            }
            .pm-combobox-label {
                font-size: 11.5px !important;
                font-weight: 500 !important;
                color: #94A3B8 !important;
                white-space: nowrap !important;
                width: 42px !important;
                flex-shrink: 0 !important;
                line-height: 1 !important;
            }
            .pm-combobox-input-wrap {
                position: relative !important;
                flex: 1 !important;
                display: flex !important;
                align-items: center !important;
            }
            .pm-font-search-input {
                width: 100% !important;
                height: 26px !important;
                background: #0C1018 !important;
                border: 1px solid #1C2332 !important;
                border-radius: 4px !important;
                color: #E2E8F0 !important;
                font-size: 12px !important;
                font-family: 'JetBrains Mono', -apple-system, sans-serif !important;
                padding: 0 18px 0 7px !important;
                outline: none !important;
                transition: all 0.12s ease !important;
                line-height: 1 !important;
            }
            .pm-font-search-input:focus {
                border-color: #1EB4EB !important;
                background: #111622 !important;
                box-shadow: 0 0 0 1px rgba(30, 180, 235, 0.3) !important;
            }
            .pm-combobox-arrow {
                position: absolute !important;
                right: 6px !important;
                top: 50% !important;
                transform: translateY(-50%) !important;
                pointer-events: none !important;
                color: #64748B !important;
                width: 9px !important;
                height: 9px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
            }
            .pm-combobox-arrow svg {
                width: 9px !important;
                height: 9px !important;
            }

            /* Floating Searchable Dropdown */
            .pm-combobox-dropdown {
                position: absolute !important;
                top: calc(100% + 2px) !important;
                left: 0 !important;
                right: 0 !important;
                max-height: 150px !important;
                overflow-y: auto !important;
                background: #0E131E !important;
                border: 1px solid #252F42 !important;
                border-radius: 6px !important;
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.8) !important;
                z-index: 999999 !important;
                display: none;
                padding: 3px !important;
            }
            .pm-combobox-dropdown.pm-open {
                display: block !important;
            }
            .pm-combobox-dropdown::-webkit-scrollbar {
                width: 4px !important;
            }
            .pm-combobox-dropdown::-webkit-scrollbar-thumb {
                background: #263145 !important;
                border-radius: 2px !important;
            }
            .pm-dropdown-header {
                font-size: 10px !important;
                font-weight: 600 !important;
                color: #64748B !important;
                padding: 4px 6px 2px 6px !important;
                user-select: none !important;
            }
            .pm-dropdown-item {
                font-size: 11.5px !important;
                font-family: 'JetBrains Mono', -apple-system, sans-serif !important;
                color: #CBD5E1 !important;
                padding: 4px 7px !important;
                border-radius: 4px !important;
                cursor: pointer !important;
                white-space: nowrap !important;
                overflow: hidden !important;
                text-overflow: ellipsis !important;
                transition: background 0.1s, color 0.1s !important;
                line-height: 1.3 !important;
            }
            .pm-dropdown-item:hover {
                background: #1C2333 !important;
                color: #FFFFFF !important;
            }
            .pm-dropdown-item.pm-selected {
                background: rgba(30, 180, 235, 0.22) !important;
                color: #8ED9F5 !important;
                font-weight: 500 !important;
            }
            .pm-dropdown-item.pm-dropdown-custom {
                color: #1EB4EB !important;
                border-bottom: 1px dashed #1E2738 !important;
                margin-bottom: 2px !important;
            }
            .pm-dropdown-empty {
                font-size: 11px !important;
                color: #64748B !important;
                padding: 6px !important;
                text-align: center !important;
            }

            /* Metrics & Layout Stack (Two Separate Lines) */
            .pm-metrics-grid {
                display: flex !important;
                flex-direction: column !important;
                gap: 5px !important;
            }
            .pm-metric-card {
                background: #121722 !important;
                border-radius: 7px !important;
                border: 1px solid #1E2536 !important;
                padding: 5px 9px !important;
                height: 31px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
            }
            .pm-metric-label {
                font-size: 11.5px !important;
                font-weight: 500 !important;
                color: #94A3B8 !important;
                line-height: 1 !important;
            }
            .pm-stepper-group {
                display: flex !important;
                align-items: center !important;
                gap: 4px !important;
            }
            .pm-stepper-btn {
                width: 20px !important;
                height: 20px !important;
                border-radius: 4px !important;
                background: #1A2130 !important;
                color: #94A3B8 !important;
                border: none !important;
                cursor: pointer !important;
                font-size: 13px !important;
                font-weight: 500 !important;
                line-height: 1 !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
                transition: all 0.1s !important;
                padding: 0 !important;
            }
            .pm-stepper-btn:hover {
                color: #FFFFFF !important;
                background: #252F44 !important;
            }
            .pm-stepper-btn:active {
                transform: scale(0.9) !important;
            }
            .pm-metric-val {
                font-family: 'JetBrains Mono', monospace !important;
                font-size: 11px !important;
                color: #8ED9F5 !important;
                font-weight: 500 !important;
                padding: 2px 5px !important;
                background: rgba(30, 180, 235, 0.15) !important;
                border-radius: 4px !important;
                border: 1px solid rgba(30, 180, 235, 0.3) !important;
                min-width: 35px !important;
                text-align: center !important;
                line-height: 1.2 !important;
            }

            /* Footer */
            #pm-footer {
                padding: 6px 9px !important;
                background: rgba(16, 20, 29, 0.8) !important;
                border-top: 1px solid #1A212E !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                border-radius: 0 0 13px 13px !important;
            }
            #pm-reset-btn {
                padding: 3px 7px !important;
                font-size: 11.5px !important;
                color: #94A3B8 !important;
                font-weight: 500 !important;
                border-radius: 4px !important;
                background: transparent !important;
                border: none !important;
                cursor: pointer !important;
                transition: all 0.12s !important;
                line-height: 1 !important;
            }
            #pm-reset-btn:hover {
                color: #F1F5F9 !important;
                background: #1C2332 !important;
            }
            .pm-github-btn {
                display: flex !important;
                align-items: center !important;
                gap: 5px !important;
                padding: 3px 8px !important;
                border-radius: 4px !important;
                background: transparent !important;
                border: 1px solid transparent !important;
                color: #94A3B8 !important;
                font-size: 11.5px !important;
                font-weight: 500 !important;
                text-decoration: none !important;
                transition: all 0.12s !important;
                cursor: pointer !important;
                line-height: 1 !important;
            }
            .pm-github-btn:hover {
                background: #1C2332 !important;
                color: #F1F5F9 !important;
                border-color: #283244 !important;
            }
            .pm-star-icon {
                width: 12px !important;
                height: 12px !important;
                fill: #F59E0B !important;
                stroke: #F59E0B !important;
                stroke-width: 1.5 !important;
                display: inline-block !important;
                transition: transform 0.15s ease !important;
            }
            .pm-github-btn:hover .pm-star-icon {
                transform: scale(1.18) rotate(12deg) !important;
            }
        """.trimIndent()).append("\n")

        return sb.toString()
    }

    private fun escapeJs(str: String): String {
        return str.replace("\\", "\\\\")
            .replace("'", "\\'")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r")
    }

    @JvmOverloads
    fun generateAutoDirScript(
        state: PersianMarkdownState,
        systemFonts: Array<String> = emptyArray()
    ): String {
        val initialEnabled = state.enabled
        val initialMode = state.directionMode.id
        val defaultFontSize = state.fontSize
        val defaultLineHeight = state.lineHeight
        val defaultFaFont = state.fontFamily?.split(",")?.firstOrNull()?.trim()?.replace("'", "")?.replace("\"", "")?.ifBlank { null } ?: "Vazirmatn"
        val defaultEnFont = state.enFontFamily?.split(",")?.firstOrNull()?.trim()?.replace("'", "")?.replace("\"", "")?.ifBlank { null } ?: "JetBrains Mono"
        val defaultCodeFont = state.codeFontFamily?.split(",")?.firstOrNull()?.trim()?.replace("'", "")?.replace("\"", "")?.ifBlank { null } ?: "JetBrains Mono"
        val initialMermaid = state.renderMermaid

        val systemFontsJson = systemFonts
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
            .joinToString(prefix = "[", postfix = "]") {
                "\"" + it.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", " ") + "\""
            }

        return """
            (function() {
                var defaultFs = $defaultFontSize;
                var defaultLh = $defaultLineHeight;
                var defaultFaFont = '${escapeJs(defaultFaFont)}';
                var defaultEnFont = '${escapeJs(defaultEnFont)}';
                var defaultCodeFont = '${escapeJs(defaultCodeFont)}';
                function savePref(k, v) { try { localStorage.setItem('pm_' + k, v); } catch(e){} }
                function getPref(k, def) { try { var v = localStorage.getItem('pm_' + k); return v !== null ? v : def; } catch(e){ return def; } }

                function cleanFontName(name) {
                    if (!name) return '';
                    if (name.indexOf(',') !== -1) {
                        name = name.split(',')[0];
                    }
                    return name.trim().replace(/['"]/g, '');
                }

                var savedFa = cleanFontName(getPref('fa_font', defaultFaFont)) || defaultFaFont;
                var savedEn = cleanFontName(getPref('en_font', defaultEnFont)) || defaultEnFont;
                var savedCode = cleanFontName(getPref('code_font', defaultCodeFont)) || defaultCodeFont;

                var savedPrefEnabled = getPref('enabled', '$initialEnabled');
                var isEnabled = savedPrefEnabled === '1' || savedPrefEnabled === 'true';
                var currentMode = getPref('mode', '${escapeJs(initialMode)}');
                if (currentMode !== 'auto' && currentMode !== 'force_rtl' && currentMode !== 'force_ltr') {
                    currentMode = '${escapeJs(initialMode)}';
                }
                var currentFs = parseInt(getPref('font_size', defaultFs), 10) || defaultFs;
                var currentLh = parseFloat(getPref('line_height', defaultLh)) || defaultLh;
                var systemFonts = $systemFontsJson;
                var persianRegex = /[\u0600-\u06FF\u0750-\u077F\u08A0-\u08FF\uFB50-\uFDFF\uFE70-\uFEFF]/;

                document.documentElement.setAttribute('data-pm-mode', currentMode);
                if (!isEnabled) {
                    document.documentElement.classList.add('pm-disabled');
                } else {
                    document.documentElement.classList.remove('pm-disabled');
                }

                function hasPersian(text) {
                    return text ? persianRegex.test(text) : false;
                }

                function isPersianProse(el) {
                    if (el.tagName === 'PRE' || el.classList.contains('code-fence') || el.classList.contains('markdown-code-fence')) {
                        return false;
                    }
                    if (!el.querySelector('code, pre')) {
                        var raw = (el.textContent || '');
                        if (!raw) return false;
                        raw = raw.replace(/\[[xX\s]?\]/g, '').replace(/https?:\/\/\S+/g, '');
                        return hasPersian(raw);
                    }
                    var text = '';
                    var walker = document.createTreeWalker(el, NodeFilter.SHOW_TEXT, {
                        acceptNode: function(node) {
                            var parent = node.parentElement;
                            if (parent && (parent.tagName === 'CODE' || parent.tagName === 'PRE' || parent.tagName === 'KBD' || parent.tagName === 'SAMP' || parent.tagName === 'TT')) {
                                return NodeFilter.FILTER_REJECT;
                            }
                            return NodeFilter.FILTER_ACCEPT;
                        }
                    });
                    while (walker.nextNode()) {
                        text += walker.currentNode.nodeValue + ' ';
                    }
                    text = text.replace(/\[[xX\s]?\]/g, '').replace(/https?:\/\/\S+/g, '').trim();
                    return hasPersian(text);
                }

                var renderFrontMatterEnabled = ${state.renderFrontMatter};
                var renderMermaidEnabled = ${state.renderMermaid};
                var savedPrefMermaid = getPref('mermaid', '$initialMermaid');
                renderMermaidEnabled = savedPrefMermaid === '1' || savedPrefMermaid === 'true';

                function cleanYamlQuotes(str) {
                    if (!str) return '';
                    str = str.trim();
                    if ((str.indexOf('"') === 0 && str.lastIndexOf('"') === str.length - 1 && str.length >= 2) ||
                        (str.indexOf("'") === 0 && str.lastIndexOf("'") === str.length - 1 && str.length >= 2)) {
                        str = str.substring(1, str.length - 1);
                    }
                    return str;
                }

                function parseYamlSimple(rawYaml) {
                    if (!rawYaml || typeof rawYaml !== 'string') return null;
                    var lines = rawYaml.split(/\r?\n/);
                    var result = [];
                    var inMultiline = false;
                    var multilineKey = null;
                    var multilineLines = [];
                    var inArray = false;
                    var arrayKey = null;
                    var arrayItems = [];

                    function flushMultiline() {
                        if (multilineKey && multilineLines.length > 0) {
                            result.push({ key: multilineKey, value: multilineLines.join(' ').trim(), type: 'string' });
                        }
                        multilineKey = null;
                        multilineLines = [];
                        inMultiline = false;
                    }

                    function flushArray() {
                        if (arrayKey && arrayItems.length > 0) {
                            result.push({ key: arrayKey, value: arrayItems, type: 'array' });
                        }
                        arrayKey = null;
                        arrayItems = [];
                        inArray = false;
                    }

                    for (var i = 0; i < lines.length; i++) {
                        var line = lines[i];
                        var trimmed = line.trim();
                        if (!trimmed || trimmed === '---' || trimmed === '...') continue;
                        if (trimmed.indexOf('#') === 0) continue;

                        if (inMultiline) {
                            if (/^\s+/.test(line)) {
                                multilineLines.push(trimmed);
                                continue;
                            } else {
                                flushMultiline();
                            }
                        }

                        if (inArray) {
                            if (/^\s*-\s+/.test(line)) {
                                var itemVal = trimmed.replace(/^-\s+/, '').trim();
                                itemVal = cleanYamlQuotes(itemVal);
                                arrayItems.push(itemVal);
                                continue;
                            } else {
                                flushArray();
                            }
                        }

                        var colonIdx = line.indexOf(':');
                        if (colonIdx > 0) {
                            var k = line.substring(0, colonIdx).trim();
                            var v = line.substring(colonIdx + 1).trim();

                            if (v === '>' || v === '|') {
                                flushMultiline();
                                flushArray();
                                inMultiline = true;
                                multilineKey = k;
                                multilineLines = [];
                                continue;
                            }

                            if (!v) {
                                if (i + 1 < lines.length && /^\s*-\s+/.test(lines[i + 1])) {
                                    flushMultiline();
                                    flushArray();
                                    inArray = true;
                                    arrayKey = k;
                                    arrayItems = [];
                                    continue;
                                }
                                result.push({ key: k, value: '', type: 'empty' });
                                continue;
                            }

                            if (v.indexOf('[') === 0 && v.lastIndexOf(']') === v.length - 1) {
                                var inner = v.substring(1, v.length - 1).trim();
                                var items = inner ? inner.split(',').map(function(s) { return cleanYamlQuotes(s.trim()); }) : [];
                                result.push({ key: k, value: items, type: 'array' });
                                continue;
                            }

                            v = cleanYamlQuotes(v);
                            result.push({ key: k, value: v, type: 'string' });
                        }
                    }
                    flushMultiline();
                    flushArray();

                    return result.length > 0 ? result : null;
                }

                function buildFrontMatterCard(rawYaml, items) {
                    var container = document.createElement('div');
                    container.className = 'pm-frontmatter-container';

                    var header = document.createElement('div');
                    header.className = 'pm-fm-header';

                    var titleGroup = document.createElement('div');
                    titleGroup.className = 'pm-fm-title-group';
                    titleGroup.innerHTML = 
                        '<span class="pm-fm-icon">' +
                            '<svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                                '<path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>' +
                                '<polyline points="14 2 14 8 20 8"></polyline>' +
                                '<line x1="16" y1="13" x2="8" y2="13"></line>' +
                                '<line x1="16" y1="17" x2="8" y2="17"></line>' +
                                '<polyline points="10 9 9 9 8 9"></polyline>' +
                            '</svg>' +
                        '</span>' +
                        '<span class="pm-fm-title">Metadata</span>';

                    var actions = document.createElement('div');
                    actions.className = 'pm-fm-actions';

                    var toggleViewBtn = document.createElement('button');
                    toggleViewBtn.type = 'button';
                    toggleViewBtn.className = 'pm-fm-btn';
                    toggleViewBtn.textContent = 'Raw YAML';
                    toggleViewBtn.title = 'Switch between Table and Code view';
                    toggleViewBtn.addEventListener('click', function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        var isRaw = container.classList.toggle('pm-view-raw');
                        toggleViewBtn.textContent = isRaw ? 'Table View' : 'Raw YAML';
                        if (isRaw) {
                            toggleViewBtn.classList.add('pm-active');
                        } else {
                            toggleViewBtn.classList.remove('pm-active');
                        }
                    });

                    var copyBtn = document.createElement('button');
                    copyBtn.type = 'button';
                    copyBtn.className = 'pm-fm-btn';
                    copyBtn.textContent = 'Copy';
                    copyBtn.title = 'Copy YAML Front Matter';
                    copyBtn.addEventListener('click', function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        var textToCopy = rawYaml.trim();
                        if (navigator.clipboard && navigator.clipboard.writeText) {
                            navigator.clipboard.writeText(textToCopy).then(function() {
                                copyBtn.textContent = 'Copied!';
                                setTimeout(function() { copyBtn.textContent = 'Copy'; }, 1800);
                            }).catch(function() {
                                copyBtn.textContent = 'Error';
                            });
                        }
                    });

                    actions.appendChild(toggleViewBtn);
                    actions.appendChild(copyBtn);
                    header.appendChild(titleGroup);
                    header.appendChild(actions);

                    var content = document.createElement('div');
                    content.className = 'pm-fm-content';

                    var table = document.createElement('table');
                    table.className = 'pm-fm-table';
                    var tbody = document.createElement('tbody');

                    for (var k = 0; k < items.length; k++) {
                        var it = items[k];
                        var tr = document.createElement('tr');

                        var tdKey = document.createElement('td');
                        tdKey.className = 'pm-fm-key-td';
                        tdKey.textContent = it.key;
                        if (hasPersian(it.key)) {
                            tdKey.setAttribute('dir', 'rtl');
                            tdKey.style.textAlign = 'right';
                        }

                        var tdVal = document.createElement('td');
                        tdVal.className = 'pm-fm-val-td';

                        if (it.type === 'array') {
                            for (var a = 0; a < it.value.length; a++) {
                                var badge = document.createElement('span');
                                badge.className = 'pm-fm-badge';
                                badge.textContent = it.value[a];
                                if (hasPersian(it.value[a])) {
                                    badge.setAttribute('dir', 'rtl');
                                } else {
                                    badge.setAttribute('dir', 'ltr');
                                }
                                tdVal.appendChild(badge);
                            }
                        } else {
                            var valStr = String(it.value || '');
                            if (/^https?:\/\/\S+$/.test(valStr)) {
                                var link = document.createElement('a');
                                link.href = valStr;
                                link.target = '_blank';
                                link.textContent = valStr;
                                link.style.direction = 'ltr';
                                link.style.unicodeBidi = 'isolate';
                                tdVal.appendChild(link);
                                tdVal.setAttribute('dir', 'ltr');
                            } else {
                                tdVal.textContent = valStr;
                                if (hasPersian(valStr)) {
                                    tdVal.setAttribute('dir', 'rtl');
                                } else {
                                    tdVal.setAttribute('dir', 'ltr');
                                }
                            }
                        }

                        tr.appendChild(tdKey);
                        tr.appendChild(tdVal);
                        tbody.appendChild(tr);
                    }
                    table.appendChild(tbody);

                    var rawPre = document.createElement('pre');
                    rawPre.className = 'pm-fm-raw';
                    rawPre.textContent = rawYaml.trim();

                    content.appendChild(table);
                    content.appendChild(rawPre);

                    container.appendChild(header);
                    container.appendChild(content);

                    return container;
                }

                function buildFrontMatterTextCard(bodyText, rawText) {
                    var container = document.createElement('div');
                    container.className = 'pm-frontmatter-container pm-fm-text-card';

                    var header = document.createElement('div');
                    header.className = 'pm-fm-header';

                    var titleGroup = document.createElement('div');
                    titleGroup.className = 'pm-fm-title-group';
                    titleGroup.innerHTML = 
                        '<span class="pm-fm-icon">' +
                            '<svg viewBox="0 0 24 24" width="15" height="15" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                                '<path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8z"></path>' +
                                '<polyline points="14 2 14 8 20 8"></polyline>' +
                                '<line x1="16" y1="13" x2="8" y2="13"></line>' +
                                '<line x1="16" y1="17" x2="8" y2="17"></line>' +
                                '<polyline points="10 9 9 9 8 9"></polyline>' +
                            '</svg>' +
                        '</span>' +
                        '<span class="pm-fm-title">Front Matter</span>';

                    var actions = document.createElement('div');
                    actions.className = 'pm-fm-actions';

                    var copyBtn = document.createElement('button');
                    copyBtn.type = 'button';
                    copyBtn.className = 'pm-fm-btn';
                    copyBtn.textContent = 'Copy';
                    copyBtn.title = 'Copy Front Matter';
                    copyBtn.addEventListener('click', function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        var textToCopy = (rawText || bodyText).trim();
                        if (navigator.clipboard && navigator.clipboard.writeText) {
                            navigator.clipboard.writeText(textToCopy).then(function() {
                                copyBtn.textContent = 'Copied!';
                                setTimeout(function() { copyBtn.textContent = 'Copy'; }, 1800);
                            }).catch(function() {
                                copyBtn.textContent = 'Error';
                            });
                        }
                    });

                    actions.appendChild(copyBtn);
                    header.appendChild(titleGroup);
                    header.appendChild(actions);

                    var content = document.createElement('div');
                    content.className = 'pm-fm-content';

                    var p = document.createElement('p');
                    p.className = 'pm-fm-text-body';
                    p.textContent = bodyText;
                    if (hasPersian(bodyText)) {
                        p.setAttribute('dir', 'rtl');
                    } else {
                        p.setAttribute('dir', 'ltr');
                    }
                    content.appendChild(p);

                    container.appendChild(header);
                    container.appendChild(content);

                    return container;
                }

                function processFrontMatter() {
                    if (!renderFrontMatterEnabled || !isEnabled) {
                        var existing = document.querySelectorAll('.pm-frontmatter-container');
                        for (var e = 0; e < existing.length; e++) {
                            existing[e].classList.add('pm-fm-hidden');
                        }
                        var wrappedPres = document.querySelectorAll('pre.frontmatter-header.pm-wrapped');
                        for (var wp = 0; wp < wrappedPres.length; wp++) {
                            wrappedPres[wp].style.display = '';
                        }
                        var hiddenFallbacks = document.querySelectorAll('.pm-fallback-hidden');
                        for (var hf = 0; hf < hiddenFallbacks.length; hf++) {
                            hiddenFallbacks[hf].style.display = '';
                        }
                        return;
                    }

                    // 1. Primary path: JetBrains generated pre.frontmatter-header
                    var fmPres = document.querySelectorAll('pre.frontmatter-header');
                    if (fmPres.length > 0) {
                        for (var i = 0; i < fmPres.length; i++) {
                            var pre = fmPres[i];
                            var rawText = (pre.textContent || '').trim();
                            var prevSibling = pre.previousElementSibling;

                            // If already processed and card exists, ensure it is unhidden
                            if (pre.classList.contains('pm-wrapped') && prevSibling && prevSibling.classList.contains('pm-frontmatter-container')) {
                                prevSibling.classList.remove('pm-fm-hidden');
                                pre.style.display = 'none';
                                continue;
                            }

                            var cleanedBody = rawText.replace(/^---[\r\n]+/, '').replace(/[\r\n]+---$/, '').trim();
                            if (!cleanedBody) {
                                pre.classList.add('pm-wrapped');
                                pre.style.display = 'none';
                                continue;
                            }

                            if (prevSibling && prevSibling.classList.contains('pm-frontmatter-container')) {
                                prevSibling.remove();
                            }

                            var items = parseYamlSimple(rawText);
                            pre.classList.add('pm-wrapped');
                            pre.style.display = 'none';

                            var newCard;
                            if (items && items.length > 0) {
                                newCard = buildFrontMatterCard(rawText, items);
                            } else {
                                newCard = buildFrontMatterTextCard(cleanedBody, rawText);
                            }
                            pre.parentNode.insertBefore(newCard, pre);
                        }
                        return;
                    }

                    // 2. Fallback heuristic path: if JetBrains did NOT generate pre.frontmatter-header
                    var existingCards = document.querySelectorAll('.pm-frontmatter-container');
                    if (existingCards.length > 0) {
                        for (var ec = 0; ec < existingCards.length; ec++) {
                            existingCards[ec].classList.remove('pm-fm-hidden');
                        }
                        var hiddenFBs = document.querySelectorAll('.pm-fallback-hidden');
                        for (var h = 0; h < hiddenFBs.length; h++) {
                            hiddenFBs[h].style.display = 'none';
                        }
                        return;
                    }

                    var body = document.body;
                    if (!body) return;
                    var firstEl = null;
                    for (var b = 0; b < body.children.length; b++) {
                        var child = body.children[b];
                        if (child.id === 'persian-markdown-switcher' || child.classList.contains('pm-frontmatter-container')) continue;
                        firstEl = child;
                        break;
                    }

                    if (firstEl && firstEl.tagName === 'HR') {
                        var candidateNodes = [];
                        var curr = firstEl.nextElementSibling;
                        var closingFound = false;
                        var collectedText = '';

                        while (curr && curr.id !== 'persian-markdown-switcher') {
                            if (curr.tagName === 'HR') {
                                closingFound = true;
                                candidateNodes.push(curr);
                                break;
                            }
                            var text = (curr.textContent || '').trim();
                            if (curr.tagName === 'H2' && text.indexOf(':') !== -1) {
                                collectedText += '\n' + text;
                                candidateNodes.push(curr);
                                closingFound = true;
                                break;
                            }
                            if (text.indexOf(':') !== -1 || /^\s*-\s+/.test(text)) {
                                collectedText += '\n' + text;
                                candidateNodes.push(curr);
                            } else {
                                break;
                            }
                            curr = curr.nextElementSibling;
                        }

                        if (closingFound && collectedText.trim()) {
                            var fallbackItems = parseYamlSimple(collectedText);
                            if (fallbackItems && fallbackItems.length > 0) {
                                var fallbackCard = buildFrontMatterCard(collectedText.trim(), fallbackItems);
                                firstEl.parentNode.insertBefore(fallbackCard, firstEl);
                                firstEl.classList.add('pm-fallback-hidden');
                                firstEl.style.display = 'none';
                                for (var c = 0; c < candidateNodes.length; c++) {
                                    candidateNodes[c].classList.add('pm-fallback-hidden');
                                    candidateNodes[c].style.display = 'none';
                                }
                            }
                        }
                    }
                }

                /* --- Mermaid Diagram Rendering & Controls --- */
                function detectIdeDarkTheme() {
                    try {
                        var bodyBg = window.getComputedStyle(document.body).backgroundColor;
                        if (!bodyBg || bodyBg === 'transparent' || bodyBg === 'rgba(0, 0, 0, 0)') {
                            bodyBg = window.getComputedStyle(document.documentElement).backgroundColor;
                        }
                        var m = bodyBg.match(/rgba?\((\d+),\s*(\d+),\s*(\d+)/);
                        if (m) {
                            var r = parseInt(m[1], 10);
                            var g = parseInt(m[2], 10);
                            var b = parseInt(m[3], 10);
                            var lum = 0.299 * r + 0.587 * g + 0.114 * b;
                            return lum < 140;
                        }
                    } catch (_) {}
                    return true;
                }

                function cleanMermaidGlobalErrors() {
                    try {
                        var errDivs = document.querySelectorAll('div[id^="dpm-mermaid-"], #dpm-mermaid, .mermaidError');
                        for (var e = 0; e < errDivs.length; e++) {
                            errDivs[e].remove();
                        }
                    } catch (_) {}
                }

                function sanitizeMermaidCode(code) {
                    if (!code) return '';
                    var inQuote = false;
                    var out = '';
                    for (var i = 0; i < code.length; i++) {
                        var ch = code[i];
                        if (ch === '"') {
                            var backslashes = 0;
                            var j = i - 1;
                            while (j >= 0 && code[j] === '\\') {
                                backslashes++;
                                j--;
                            }
                            if (backslashes % 2 === 0) {
                                inQuote = !inQuote;
                            }
                            out += ch;
                        } else if (!inQuote && (ch === '\u200C' || ch === '\u200D')) {
                            out += '_';
                        } else {
                            out += ch;
                        }
                    }
                    return out;
                }

                var mermaidCardCounter = 0;

                function createMermaidCard(rawCode, isFa, diagramType) {
                    var container = document.createElement('div');
                    container.className = 'pm-mermaid-container';
                    var cardId = 'pm-mermaid-card-' + (++mermaidCardCounter);
                    container.id = cardId;
                    container.setAttribute('data-raw-code', rawCode);
                    container._zoomScale = 1.0;

                    var header = document.createElement('div');
                    header.className = 'pm-mermaid-header';

                    var titleGroup = document.createElement('div');
                    titleGroup.className = 'pm-mermaid-title-group';
                    titleGroup.innerHTML = 
                        '<span class="pm-mermaid-icon">' +
                            '<svg viewBox="0 0 24 24" width="16" height="16" fill="none" stroke="currentColor" stroke-width="2"><path d="M3 3h7v7H3zM14 3h7v7h-7zM14 14h7v7h-7zM3 14h7v7H3z"/><path d="M10 6.5h4M6.5 10v4M17.5 10v4M10 17.5h4"/></svg>' +
                        '</span>' +
                        '<span class="pm-mermaid-title">Mermaid</span>' +
                        '<span class="pm-mermaid-lang-badge' + (isFa ? ' pm-fa' : '') + '">' + (isFa ? 'FA' : 'EN') + '</span>' +
                        (diagramType ? '<span class="pm-mermaid-lang-badge">' + diagramType + '</span>' : '') +
                        '<span class="pm-mermaid-status" id="' + cardId + '-status">● Updating...</span>';

                    var zoomBar = document.createElement('div');
                    zoomBar.className = 'pm-mermaid-zoom-bar';

                    var btnMinus = document.createElement('button');
                    btnMinus.type = 'button';
                    btnMinus.className = 'pm-mermaid-zoom-btn';
                    btnMinus.textContent = '−';
                    btnMinus.title = 'Zoom Out (−)';
                    btnMinus.setAttribute('aria-label', 'Zoom Out');

                    var zoomVal = document.createElement('span');
                    zoomVal.className = 'pm-mermaid-zoom-val';
                    zoomVal.textContent = '100%';
                    zoomVal.title = 'Click to Reset (100%)';

                    var btnPlus = document.createElement('button');
                    btnPlus.type = 'button';
                    btnPlus.className = 'pm-mermaid-zoom-btn';
                    btnPlus.textContent = '+';
                    btnPlus.title = 'Zoom In (+)';
                    btnPlus.setAttribute('aria-label', 'Zoom In');

                    zoomBar.appendChild(btnMinus);
                    zoomBar.appendChild(zoomVal);
                    zoomBar.appendChild(btnPlus);

                    header.appendChild(titleGroup);
                    header.appendChild(zoomBar);

                    var body = document.createElement('div');
                    body.className = 'pm-mermaid-body';

                    var svgWrap = document.createElement('div');
                    svgWrap.className = 'pm-mermaid-svg-wrap';

                    var errorBanner = document.createElement('div');
                    errorBanner.className = 'pm-mermaid-error-banner';

                    body.appendChild(svgWrap);
                    body.appendChild(errorBanner);

                    container.appendChild(header);
                    container.appendChild(body);

                    function applyZoom(newScale) {
                        newScale = Math.round(newScale * 100) / 100;
                        if (newScale < 0.4) newScale = 0.4;
                        if (newScale > 3.0) newScale = 3.0;
                        container._zoomScale = newScale;

                        zoomVal.textContent = Math.round(newScale * 100) + '%';
                        if (newScale === 1.0) {
                            zoomVal.classList.remove('pm-zoomed');
                        } else {
                            zoomVal.classList.add('pm-zoomed');
                        }

                        var svg = svgWrap.querySelector('svg');
                        if (!svg) return;

                        if (newScale === 1.0) {
                            svg.style.width = '';
                            svg.style.maxWidth = '100%';
                            svg.style.flexShrink = '';
                        } else {
                            var baseW = container._initialSvgWidth;
                            if (!baseW) {
                                baseW = svg.getBoundingClientRect().width || (svg.viewBox ? svg.viewBox.baseVal.width : 500);
                                container._initialSvgWidth = baseW;
                            }
                            var targetW = Math.round(baseW * newScale);
                            svg.style.setProperty('width', targetW + 'px', 'important');
                            svg.style.setProperty('max-width', 'none', 'important');
                            svg.style.setProperty('flex-shrink', '0', 'important');
                        }
                    }

                    container._applyZoom = applyZoom;

                    btnMinus.addEventListener('click', function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        applyZoom((container._zoomScale || 1.0) - 0.2);
                    });

                    btnPlus.addEventListener('click', function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        applyZoom((container._zoomScale || 1.0) + 0.2);
                    });

                    zoomVal.addEventListener('click', function(e) {
                        e.preventDefault();
                        e.stopPropagation();
                        applyZoom(1.0);
                    });

                    return container;
                }

                function renderMermaidCard(card, rawCode, isFa, diagramType) {
                    if (!window.mermaid) {
                        var retryCount = 0;
                        var retryTimer = setInterval(function() {
                            retryCount++;
                            if (window.mermaid) {
                                clearInterval(retryTimer);
                                renderMermaidCard(card, rawCode, isFa, diagramType);
                            } else if (retryCount > 60) {
                                clearInterval(retryTimer);
                            }
                        }, 100);
                        return;
                    }
                    card.setAttribute('data-raw-code', rawCode);

                    var svgWrap = card.querySelector('.pm-mermaid-svg-wrap');
                    var errorBanner = card.querySelector('.pm-mermaid-error-banner');
                    var statusEl = card.querySelector('.pm-mermaid-status');

                    // Choose typography based on language detection (English by default, Vazirmatn for Persian)
                    var diagramFont;
                    if (!isFa) {
                        var enFont = cleanFontName(savedEn) || cleanFontName(savedCode) || 'JetBrains Mono';
                        diagramFont = "'" + enFont + "', 'PersianMarkdownBundledJBMono', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif";
                    } else {
                        var faFont = cleanFontName(savedFa) || 'Vazirmatn';
                        diagramFont = "'" + faFont + "', 'PersianMarkdownBundledVazir', -apple-system, BlinkMacSystemFont, 'Segoe UI', Tahoma, sans-serif";
                    }

                    var isDark = detectIdeDarkTheme();
                    try {
                        window.mermaid.initialize({
                            startOnLoad: false,
                            theme: isDark ? 'dark' : 'default',
                            securityLevel: 'loose',
                            suppressErrorRendering: true,
                            fontFamily: diagramFont
                        });
                    } catch (_) {}

                    var renderId = 'pm-svg-' + Math.random().toString(36).substring(2, 9);

                    if (statusEl) {
                        statusEl.textContent = '● Updating...';
                        statusEl.classList.add('pm-visible');
                    }

                    function onRenderSuccess(svgContent) {
                        cleanMermaidGlobalErrors();
                        card._lastValidSvg = svgContent;
                        svgWrap.innerHTML = svgContent;

                        // Smart BiDi enhancement for Persian/Arabic text inside nodes
                        if (isFa) {
                            var svgEl = svgWrap.querySelector('svg');
                            if (svgEl) {
                                var textEls = svgEl.querySelectorAll('text, .label, foreignObject, foreignObject span, foreignObject div, foreignObject p');
                                for (var t = 0; t < textEls.length; t++) {
                                    var el = textEls[t];
                                    if (hasPersian(el.textContent)) {
                                        el.setAttribute('dir', 'rtl');
                                        el.style.direction = 'rtl';
                                        el.style.textAlign = 'right';
                                    }
                                }
                            }
                        }

                        // Record initial rendered width for zoom scaling calculation
                        var renderedSvg = svgWrap.querySelector('svg');
                        if (renderedSvg) {
                            var rectW = renderedSvg.getBoundingClientRect().width;
                            if (rectW > 50) {
                                card._initialSvgWidth = rectW;
                            } else if (renderedSvg.viewBox && renderedSvg.viewBox.baseVal.width) {
                                card._initialSvgWidth = renderedSvg.viewBox.baseVal.width;
                            }
                            if (card._applyZoom && card._zoomScale && card._zoomScale !== 1.0) {
                                card._applyZoom(card._zoomScale);
                            }
                        }

                        if (statusEl) statusEl.classList.remove('pm-visible');
                        if (errorBanner) errorBanner.classList.remove('pm-visible');
                    }

                    function onRenderError(err) {
                        cleanMermaidGlobalErrors();
                        if (card._lastValidSvg) {
                            // Live Editing Resilience: keep displaying the last valid diagram!
                            if (statusEl) {
                                statusEl.textContent = '● Editing...';
                                statusEl.classList.add('pm-visible');
                            }
                            if (errorBanner) errorBanner.classList.remove('pm-visible');
                        } else {
                            // No prior valid render: show error banner gracefully
                            if (statusEl) statusEl.classList.remove('pm-visible');
                            if (errorBanner) {
                                errorBanner.textContent = (err && err.message) ? err.message : 'Syntax error in Mermaid diagram';
                                errorBanner.classList.add('pm-visible');
                            }
                        }
                    }

                    var sanitizedCode = sanitizeMermaidCode(rawCode);

                    try {
                        var res = window.mermaid.render(renderId, sanitizedCode);
                        if (res && typeof res.then === 'function') {
                            res.then(function(result) {
                                var svgStr = result && result.svg ? result.svg : result;
                                onRenderSuccess(svgStr);
                            }).catch(onRenderError);
                        } else if (res && res.svg) {
                            onRenderSuccess(res.svg);
                        }
                    } catch (e) {
                        onRenderError(e);
                    }
                }

                function processMermaidDiagrams() {
                    if (!renderMermaidEnabled || !isEnabled) {
                        var existing = document.querySelectorAll('.pm-mermaid-container');
                        for (var e = 0; e < existing.length; e++) {
                            existing[e].classList.add('pm-mermaid-hidden');
                        }
                        var wrappedPres = document.querySelectorAll('pre.pm-mermaid-wrapped');
                        for (var wp = 0; wp < wrappedPres.length; wp++) {
                            wrappedPres[wp].style.display = '';
                        }
                        return;
                    }

                    var pres = document.querySelectorAll('pre');
                    for (var i = 0; i < pres.length; i++) {
                        var pre = pres[i];
                        if (pre.classList.contains('frontmatter-header')) continue;
                        if (pre.closest('#persian-markdown-switcher')) continue;
                        if (pre.closest('.pm-frontmatter-container')) continue;
                        if (pre.closest('.pm-mermaid-container')) continue;

                        var code = pre.querySelector('code');
                        var className = (code ? code.className : '') + ' ' + (pre.className || '');
                        var isMermaidClass = /\b(language-mermaid|src-mermaid|mermaid)\b/i.test(className);
                        var rawText = (code ? code.textContent : pre.textContent) || '';
                        var trimmed = rawText.trim();

                        var isMermaidSyntax = /^(flowchart|graph|sequenceDiagram|classDiagram|stateDiagram(-v2)?|erDiagram|gantt|pie|gitGraph|mindmap|timeline|quadrantChart|xychart-beta|packet-beta|architecture-beta)\b/m.test(trimmed);

                        if (!isMermaidClass && !isMermaidSyntax) {
                            continue;
                        }

                        var isFa = hasPersian(trimmed);

                        var diagramType = 'DIAGRAM';
                        if (/^(flowchart|graph)\b/i.test(trimmed)) diagramType = 'FLOWCHART';
                        else if (/^sequenceDiagram\b/i.test(trimmed)) diagramType = 'SEQUENCE';
                        else if (/^classDiagram\b/i.test(trimmed)) diagramType = 'CLASS';
                        else if (/^stateDiagram/i.test(trimmed)) diagramType = 'STATE';
                        else if (/^erDiagram\b/i.test(trimmed)) diagramType = 'ER MODEL';
                        else if (/^gantt\b/i.test(trimmed)) diagramType = 'GANTT';
                        else if (/^pie\b/i.test(trimmed)) diagramType = 'PIE';
                        else if (/^gitGraph\b/i.test(trimmed)) diagramType = 'GIT GRAPH';
                        else if (/^mindmap\b/i.test(trimmed)) diagramType = 'MINDMAP';
                        else if (/^timeline\b/i.test(trimmed)) diagramType = 'TIMELINE';

                        pre.classList.add('pm-mermaid-wrapped');
                        pre.style.display = 'none';

                        var prevSibling = pre.previousElementSibling;
                        var card = null;

                        if (prevSibling && prevSibling.classList.contains('pm-mermaid-container')) {
                            card = prevSibling;
                            card.classList.remove('pm-mermaid-hidden');
                            var svgWrap = card.querySelector('.pm-mermaid-svg-wrap');
                            var hasSvg = svgWrap && svgWrap.querySelector('svg');
                            if (!hasSvg || card.getAttribute('data-raw-code') !== rawText) {
                                renderMermaidCard(card, rawText, isFa, diagramType);
                            }
                        } else {
                            card = createMermaidCard(rawText, isFa, diagramType);
                            pre.parentNode.insertBefore(card, pre);
                            renderMermaidCard(card, rawText, isFa, diagramType);
                        }
                    }
                }

                function resetDirections() {
                    var existingFm = document.querySelectorAll('.pm-frontmatter-container');
                    for (var ef = 0; ef < existingFm.length; ef++) {
                        existingFm[ef].classList.add('pm-fm-hidden');
                    }
                    var wrappedPres = document.querySelectorAll('pre.frontmatter-header.pm-wrapped');
                    for (var wp = 0; wp < wrappedPres.length; wp++) {
                        wrappedPres[wp].style.display = '';
                    }
                    var existingMm = document.querySelectorAll('.pm-mermaid-container');
                    for (var em = 0; em < existingMm.length; em++) {
                        existingMm[em].classList.add('pm-mermaid-hidden');
                    }
                    var wrappedMmPres = document.querySelectorAll('pre.pm-mermaid-wrapped');
                    for (var wm = 0; wm < wrappedMmPres.length; wm++) {
                        wrappedMmPres[wm].style.display = '';
                    }
                    var hiddenFallbacks = document.querySelectorAll('.pm-fallback-hidden');
                    for (var hf = 0; hf < hiddenFallbacks.length; hf++) {
                        hiddenFallbacks[hf].style.display = '';
                    }
                    var elements = document.querySelectorAll('p, h1, h2, h3, h4, h5, h6, li, blockquote, td, th, pre, code, kbd, samp, tt, a');
                    for (var i = 0; i < elements.length; i++) {
                        var el = elements[i];
                        if (el.closest('#persian-markdown-switcher')) continue;
                        el.removeAttribute('dir');
                        el.classList.remove('persian-dir-rtl');
                        el.classList.remove('persian-dir-ltr');
                        el.style.direction = '';
                        el.style.textAlign = '';
                        el.style.unicodeBidi = '';
                    }
                }

                function applyDirections() {
                    if (!isEnabled) {
                        resetDirections();
                        return;
                    }

                    // Process Front Matter first
                    processFrontMatter();

                    // Process Mermaid Diagrams
                    processMermaidDiagrams();

                    // 1. Code blocks (<pre>) are ALWAYS LTR and left-aligned
                    var preElements = document.querySelectorAll('pre, .code-fence, .markdown-code-fence');
                    for (var k = 0; k < preElements.length; k++) {
                        var p = preElements[k];
                        p.setAttribute('dir', 'ltr');
                        p.style.direction = 'ltr';
                        p.style.textAlign = 'left';
                        p.style.unicodeBidi = 'isolate';
                    }

                    // 2. Inline code snippets
                    var codeElements = document.querySelectorAll('code:not(pre code), kbd, samp, tt');
                    for (var j = 0; j < codeElements.length; j++) {
                        var codeEl = codeElements[j];
                        codeEl.style.unicodeBidi = 'isolate';
                        if (hasPersian(codeEl.textContent)) {
                            codeEl.setAttribute('dir', 'rtl');
                            codeEl.style.direction = 'rtl';
                        } else {
                            codeEl.setAttribute('dir', 'ltr');
                            codeEl.style.direction = 'ltr';
                        }
                    }

                    // 3. Links and isolated tokens
                    var links = document.querySelectorAll('a:not(.pm-github-btn)');
                    for (var a = 0; a < links.length; a++) {
                        links[a].setAttribute('dir', 'auto');
                        links[a].style.unicodeBidi = 'isolate';
                    }

                    // 4. Block-level elements (p, h1-h6, li, blockquote, td, th)
                    var elements = document.querySelectorAll('p, h1, h2, h3, h4, h5, h6, li, blockquote, td, th');
                    for (var i = 0; i < elements.length; i++) {
                        var el = elements[i];
                        if (el.closest('pre, #persian-markdown-switcher')) continue;

                        if (currentMode === 'force_rtl') {
                            el.setAttribute('dir', 'rtl');
                            el.classList.add('persian-dir-rtl');
                            el.classList.remove('persian-dir-ltr');
                        } else if (currentMode === 'force_ltr') {
                            el.setAttribute('dir', 'ltr');
                            el.classList.add('persian-dir-ltr');
                            el.classList.remove('persian-dir-rtl');
                        } else { // AUTO
                            if (isPersianProse(el)) {
                                el.setAttribute('dir', 'rtl');
                                el.classList.add('persian-dir-rtl');
                                el.classList.remove('persian-dir-ltr');
                            } else {
                                el.setAttribute('dir', 'ltr');
                                el.classList.add('persian-dir-ltr');
                                el.classList.remove('persian-dir-rtl');
                            }
                        }
                    }
                }

                function setupFontCombobox(type, inputEl, dropdownEl, suggestedList, initialVal, onSelect) {
                    var selected = cleanFontName(initialVal);
                    inputEl.value = selected;
                    inputEl.placeholder = selected;

                    function closeDropdown() {
                        dropdownEl.classList.remove('pm-open');
                    }

                    function selectFont(fontName) {
                        selected = cleanFontName(fontName);
                        if (!selected) return;
                        inputEl.value = selected;
                        inputEl.placeholder = selected;
                        closeDropdown();
                        onSelect(selected);
                    }

                    function renderList(query) {
                        dropdownEl.innerHTML = '';
                        var q = (query || '').trim().toLowerCase();
                        var seen = {};
                        var matchCount = 0;

                        function addItem(fontName, isCustom) {
                            var item = document.createElement('div');
                            item.className = 'pm-dropdown-item';
                            if (isCustom) {
                                item.classList.add('pm-dropdown-custom');
                                item.textContent = 'Use: "' + fontName + '"';
                            } else {
                                if (selected && fontName.toLowerCase() === selected.toLowerCase()) {
                                    item.classList.add('pm-selected');
                                }
                                item.textContent = fontName;
                            }
                            item.addEventListener('mousedown', function(e) {
                                e.preventDefault();
                                e.stopPropagation();
                                selectFont(fontName);
                            });
                            dropdownEl.appendChild(item);
                            matchCount++;
                        }

                        function addHeader(title) {
                            var h = document.createElement('div');
                            h.className = 'pm-dropdown-header';
                            h.textContent = title;
                            dropdownEl.appendChild(h);
                        }

                        // If user typed something custom not exactly in lists, offer it at top
                        if (q) {
                            var exactMatch = false;
                            for (var k = 0; k < suggestedList.length; k++) {
                                if (suggestedList[k].toLowerCase() === q) { exactMatch = true; break; }
                            }
                            if (!exactMatch && systemFonts) {
                                for (var m = 0; m < systemFonts.length; m++) {
                                    if (systemFonts[m].toLowerCase() === q) { exactMatch = true; break; }
                                }
                            }
                            if (!exactMatch) {
                                addItem(query.trim(), true);
                            }
                        }

                        // Recommended
                        var recMatches = [];
                        for (var i = 0; i < suggestedList.length; i++) {
                            var f = suggestedList[i];
                            if (!q || f.toLowerCase().indexOf(q) !== -1) {
                                recMatches.push(f);
                                seen[f.toLowerCase()] = true;
                            }
                        }
                        if (recMatches.length > 0) {
                            addHeader('Recommended');
                            for (var r = 0; r < recMatches.length; r++) {
                                addItem(recMatches[r], false);
                            }
                        }

                        // System Fonts
                        if (systemFonts && systemFonts.length > 0) {
                            var sysMatches = [];
                            for (var j = 0; j < systemFonts.length; j++) {
                                var sf = systemFonts[j];
                                if (seen[sf.toLowerCase()]) continue;
                                if (!q || sf.toLowerCase().indexOf(q) !== -1) {
                                    sysMatches.push(sf);
                                    seen[sf.toLowerCase()] = true;
                                }
                            }
                            if (sysMatches.length > 0) {
                                addHeader('System Fonts');
                                for (var s = 0; s < sysMatches.length; s++) {
                                    addItem(sysMatches[s], false);
                                }
                            }
                        }

                        if (matchCount === 0 && !dropdownEl.firstChild) {
                            var empty = document.createElement('div');
                            empty.className = 'pm-dropdown-empty';
                            empty.textContent = 'No fonts found';
                            dropdownEl.appendChild(empty);
                        }
                    }

                    function openDropdown() {
                        var allDrops = document.querySelectorAll('.pm-combobox-dropdown');
                        for (var d = 0; d < allDrops.length; d++) {
                            if (allDrops[d] !== dropdownEl) allDrops[d].classList.remove('pm-open');
                        }
                        inputEl.placeholder = selected;
                        inputEl.value = '';
                        renderList('');
                        dropdownEl.classList.add('pm-open');
                    }

                    inputEl.addEventListener('focus', function() {
                        openDropdown();
                    });

                    inputEl.addEventListener('click', function(e) {
                        e.stopPropagation();
                        if (!dropdownEl.classList.contains('pm-open')) {
                            openDropdown();
                        }
                    });

                    inputEl.addEventListener('input', function() {
                        renderList(inputEl.value);
                        if (!dropdownEl.classList.contains('pm-open')) {
                            dropdownEl.classList.add('pm-open');
                        }
                    });

                    inputEl.addEventListener('keydown', function(e) {
                        if (e.key === 'Enter') {
                            e.preventDefault();
                            var firstItem = dropdownEl.querySelector('.pm-dropdown-item');
                            if (firstItem) {
                                var text = firstItem.textContent || '';
                                if (text.indexOf('Use: "') === 0) {
                                    selectFont(text.substring(6, text.length - 1));
                                } else {
                                    selectFont(text);
                                }
                            } else if (inputEl.value.trim()) {
                                selectFont(inputEl.value.trim());
                            }
                        } else if (e.key === 'Escape') {
                            if (dropdownEl.classList.contains('pm-open')) {
                                e.stopPropagation();
                                closeDropdown();
                            }
                        }
                    });

                    inputEl.addEventListener('blur', function() {
                        setTimeout(function() {
                            closeDropdown();
                            var val = cleanFontName(inputEl.value);
                            if (val && val.toLowerCase() !== selected.toLowerCase()) {
                                selectFont(val);
                            } else {
                                inputEl.value = selected;
                            }
                        }, 200);
                    });

                    return {
                        setValue: function(v) {
                            selected = cleanFontName(v);
                            inputEl.value = selected;
                            inputEl.placeholder = selected;
                        },
                        getValue: function() {
                            return selected;
                        },
                        close: closeDropdown
                    };
                }

                function createSwitcherUI() {
                    var target = document.documentElement || document.body;
                    if (!target) return;

                    var existing = document.getElementById('persian-markdown-switcher');
                    if (existing) {
                        if (existing.parentElement !== target) {
                            target.appendChild(existing);
                        }
                        return;
                    }

                    var switcher = document.createElement('div');
                    switcher.id = 'persian-markdown-switcher';
                    switcher.innerHTML = 
                        '<div id="pm-trigger" title="Markdown RTL (⌥R)">' +
                            '<div id="pm-status-dot">' +
                                '<svg viewBox="0 0 28 28" fill="none">' +
                                    '<defs>' +
                                        '<clipPath id="pm_clip0"><rect width="28" height="28" fill="#fff"/></clipPath>' +
                                        '<radialGradient id="pm_radial1" cx="0" cy="0" r="1" gradientTransform="matrix(15.00002 14.77582 -9.28916 9.43011 6.5 6.5)" gradientUnits="userSpaceOnUse">' +
                                            '<stop stop-color="#8ED9F5"/>' +
                                            '<stop offset=".5" stop-color="#1EB4EB"/>' +
                                        '</radialGradient>' +
                                        '<radialGradient id="pm_radial2" cx="0" cy="0" r="1" gradientTransform="matrix(2.25 8.86549 -1.39337 5.65804 9.5 11)" gradientUnits="userSpaceOnUse">' +
                                            '<stop stop-color="#8ED9F5"/>' +
                                            '<stop offset=".5" stop-color="#1EB4EB"/>' +
                                        '</radialGradient>' +
                                        '<radialGradient id="pm_radial3" cx="0" cy="0" r="1" gradientTransform="matrix(2.25 6.1098 -1.39337 3.89933 16.25 11)" gradientUnits="userSpaceOnUse">' +
                                            '<stop stop-color="#8ED9F5"/>' +
                                            '<stop offset=".5" stop-color="#1EB4EB"/>' +
                                        '</radialGradient>' +
                                        '<linearGradient id="pm_linear0" x1="14" x2="14" y1="19" y2="25" gradientUnits="userSpaceOnUse">' +
                                            '<stop stop-color="#1EB4EB" stop-opacity="0"/>' +
                                            '<stop offset="1" stop-color="#1EB4EB"/>' +
                                        '</linearGradient>' +
                                        '<filter id="pm_blur0" width="28" height="14" x="0" y="15" color-interpolation-filters="sRGB" filterUnits="userSpaceOnUse">' +
                                            '<feFlood flood-opacity="0" result="BackgroundImageFix"/>' +
                                            '<feBlend in="SourceGraphic" in2="BackgroundImageFix" result="shape"/>' +
                                            '<feGaussianBlur result="effect1_foregroundBlur" stdDeviation="2"/>' +
                                        '</filter>' +
                                    '</defs>' +
                                    '<g clip-path="url(#pm_clip0)">' +
                                        '<path fill="#1EB4EB" fill-opacity=".2" d="M0 14C0 2.471 2.471 0 14 0C25.529 0 28 2.471 28 14C28 25.529 25.529 28 14 28C2.471 28 0 25.529 0 14Z"/>' +
                                        '<g filter="url(#pm_blur0)" opacity=".6">' +
                                            '<ellipse cx="14" cy="22" fill="url(#pm_linear0)" rx="10" ry="3"/>' +
                                        '</g>' +
                                        '<path fill="url(#pm_radial1)" d="M17.75 9.5H17C15.7625 9.5 14.75 10.5125 14.75 11.75V16.4525C14.75 17.69 15.7625 18.7025 17 18.7025H17.75C18.9875 18.7025 20 17.69 20 16.4525V11.75C20 10.5125 18.9875 9.5 17.75 9.5ZM18.5 16.4525C18.5 16.865 18.1625 17.2025 17.75 17.2025H17C16.5875 17.2025 16.25 16.865 16.25 16.4525V11.75C16.25 11.3375 16.5875 11 17 11H17.75C18.1625 11 18.5 11.3375 18.5 11.75V16.4525ZM20.75 6.5H7.25C6.8375 6.5 6.5 6.8375 6.5 7.25C6.5 7.6625 6.8375 8 7.25 8H20.75C21.1625 8 21.5 7.6625 21.5 7.25C21.5 6.8375 21.1625 6.5 20.75 6.5ZM11 9.5H10.25C9.0125 9.5 8 10.5125 8 11.75V19.25C8 20.4875 9.0125 21.5 10.25 21.5H11C12.2375 21.5 13.25 20.4875 13.25 19.25V11.75C13.25 10.5125 12.2375 9.5 11 9.5ZM11.75 19.25C11.75 19.6625 11.4125 20 11 20H10.25C9.8375 20 9.5 19.6625 9.5 19.25V11.75C9.5 11.3375 9.8375 11 10.25 11H11C11.4125 11 11.75 11.3375 11.75 11.75V19.25Z"/>' +
                                        '<path fill="url(#pm_radial2)" fill-opacity=".4" d="M11 11H10.25C9.83579 11 9.5 11.3358 9.5 11.75V19.25C9.5 19.6642 9.83579 20 10.25 20H11C11.4142 20 11.75 19.6642 11.75 19.25V11.75C11.75 11.3358 11.4142 11 11 11Z"/>' +
                                        '<path fill="url(#pm_radial3)" fill-opacity=".4" d="M17.75 11H17C16.5858 11 16.25 11.3358 16.25 11.75V16.4525C16.25 16.8667 16.5858 17.2025 17 17.2025H17.75C18.1642 17.2025 18.5 16.8667 18.5 16.4525V11.75C18.5 11.3358 18.1642 11 17.75 11Z"/>' +
                                    '</g>' +
                                '</svg>' +
                            '</div>' +
                            '<span id="pm-current-label">Markdown RTL</span>' +
                        '</div>' +
                        '<div id="pm-card">' +
                            '<div id="pm-header">' +
                                '<div class="pm-header-left">' +
                                    '<div class="pm-brand-icon-box">' +
                                        '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">' +
                                            '<polyline points="4 7 4 4 20 4 20 7"></polyline>' +
                                            '<line x1="9" x2="15" y1="20" y2="20"></line>' +
                                            '<line x1="12" x2="12" y1="4" y2="20"></line>' +
                                            '<path d="M7 11l-3 3 3 3"></path>' +
                                        '</svg>' +
                                    '</div>' +
                                    '<span class="pm-header-title">Markdown RTL</span>' +
                                '</div>' +
                                '<div class="pm-header-right">' +
                                    '<button id="pm-close-btn" type="button" title="Close (ESC)">esc</button>' +
                                '</div>' +
                            '</div>' +
                            '<div id="pm-body">' +
                                '<div class="pm-master-card">' +
                                    '<div class="pm-master-label-wrap">' +
                                        '<span class="pm-master-title">Enable Markdown RTL</span>' +
                                        '<span class="pm-kbd-tag">⌥E</span>' +
                                    '</div>' +
                                    '<label class="pm-switch">' +
                                        '<input type="checkbox" id="pm-opt-enabled">' +
                                        '<span class="pm-switch-track"></span>' +
                                    '</label>' +
                                '</div>' +
                                '<div id="pm-controls-wrap">' +
                                    '<div class="pm-tabs-bar">' +
                                        '<button type="button" class="pm-tab-btn" id="pm-tab-auto" data-mode="auto">' +
                                            '<span>Auto</span>' +
                                            '<span class="pm-info-icon">' +
                                                'ⓘ' +
                                                '<span class="pm-tooltip">Detects text direction per paragraph (Persian/Arabic → RTL, English/Code → LTR)</span>' +
                                            '</span>' +
                                        '</button>' +
                                        '<button type="button" class="pm-tab-btn" id="pm-tab-force-rtl" data-mode="force_rtl">' +
                                            '<span>Force RTL</span>' +
                                            '<span class="pm-info-icon">' +
                                                'ⓘ' +
                                                '<span class="pm-tooltip">Forces all paragraphs and lists to render Right-to-Left</span>' +
                                            '</span>' +
                                        '</button>' +
                                        '<button type="button" class="pm-tab-btn" id="pm-tab-force-ltr" data-mode="force_ltr">' +
                                            '<span>Force LTR</span>' +
                                            '<span class="pm-info-icon">' +
                                                'ⓘ' +
                                                '<span class="pm-tooltip">Forces all paragraphs and lists to render Left-to-Right</span>' +
                                            '</span>' +
                                        '</button>' +
                                    '</div>' +
                                    '<div class="pm-section-title">Fonts</div>' +
                                    '<div class="pm-bento-card">' +
                                        '<div class="pm-font-combobox" id="pm-combo-fa">' +
                                            '<span class="pm-combobox-label">FA/AR</span>' +
                                            '<div class="pm-combobox-input-wrap">' +
                                                '<input type="text" class="pm-font-search-input" id="pm-input-fa" placeholder="Search font..." autocomplete="off" spellcheck="false">' +
                                                '<div class="pm-combobox-arrow">' +
                                                    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m6 9 6 6 6-6"/></svg>' +
                                                '</div>' +
                                                '<div class="pm-combobox-dropdown" id="pm-drop-fa"></div>' +
                                            '</div>' +
                                        '</div>' +
                                        '<div class="pm-font-combobox" id="pm-combo-en">' +
                                            '<span class="pm-combobox-label">EN</span>' +
                                            '<div class="pm-combobox-input-wrap">' +
                                                '<input type="text" class="pm-font-search-input" id="pm-input-en" placeholder="Search font..." autocomplete="off" spellcheck="false">' +
                                                '<div class="pm-combobox-arrow">' +
                                                    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m6 9 6 6 6-6"/></svg>' +
                                                '</div>' +
                                                '<div class="pm-combobox-dropdown" id="pm-drop-en"></div>' +
                                            '</div>' +
                                        '</div>' +
                                        '<div class="pm-font-combobox" id="pm-combo-code">' +
                                            '<span class="pm-combobox-label">Code</span>' +
                                            '<div class="pm-combobox-input-wrap">' +
                                                '<input type="text" class="pm-font-search-input" id="pm-input-code" placeholder="Search font..." autocomplete="off" spellcheck="false">' +
                                                '<div class="pm-combobox-arrow">' +
                                                    '<svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="m6 9 6 6 6-6"/></svg>' +
                                                '</div>' +
                                                '<div class="pm-combobox-dropdown" id="pm-drop-code"></div>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>' +
                                    '<div class="pm-section-title">Typography</div>' +
                                    '<div class="pm-metrics-grid">' +
                                        '<div class="pm-metric-card">' +
                                            '<span class="pm-metric-label">Font Size</span>' +
                                            '<div class="pm-stepper-group">' +
                                                '<button type="button" class="pm-stepper-btn" id="pm-dec-fs">-</button>' +
                                                '<span class="pm-metric-val" id="pm-fs-val">' + defaultFs + 'px</span>' +
                                                '<button type="button" class="pm-stepper-btn" id="pm-inc-fs">+</button>' +
                                            '</div>' +
                                        '</div>' +
                                        '<div class="pm-metric-card">' +
                                            '<span class="pm-metric-label">Line Height</span>' +
                                            '<div class="pm-stepper-group">' +
                                                '<button type="button" class="pm-stepper-btn" id="pm-dec-lh">-</button>' +
                                                '<span class="pm-metric-val" id="pm-lh-val">' + defaultLh.toFixed(1) + 'x</span>' +
                                                '<button type="button" class="pm-stepper-btn" id="pm-inc-lh">+</button>' +
                                            '</div>' +
                                        '</div>' +
                                    '</div>' +
                                    '<div class="pm-section-title">Features</div>' +
                                    '<div class="pm-bento-card" style="padding: 6px 12px; gap: 8px; display: flex; flex-direction: column;">' +
                                        '<div style="display: flex; align-items: center; justify-content: space-between; width: 100%;">' +
                                            '<span style="font-size: 11.5px; font-weight: 500; color: #CBD5E1;">Front Matter</span>' +
                                            '<label class="pm-switch">' +
                                                '<input type="checkbox" id="pm-opt-fm">' +
                                                '<span class="pm-switch-track"></span>' +
                                            '</label>' +
                                        '</div>' +
                                        '<div style="display: flex; align-items: center; justify-content: space-between; width: 100%; border-top: 1px solid rgba(255,255,255,0.06); padding-top: 6px;">' +
                                            '<div style="display: flex; align-items: center; gap: 6px;">' +
                                                '<span style="font-size: 11.5px; font-weight: 500; color: #CBD5E1;">Mermaid</span>' +
                                                '<span style="font-size: 9.5px; padding: 1px 5px; border-radius: 4px; background: rgba(56, 189, 248, 0.15); color: #38BDF8; font-family: var(--pm-code-font);">v1.1</span>' +
                                            '</div>' +
                                            '<label class="pm-switch">' +
                                                '<input type="checkbox" id="pm-opt-mermaid">' +
                                                '<span class="pm-switch-track"></span>' +
                                            '</label>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                            '<div id="pm-footer">' +
                                '<button id="pm-reset-btn" type="button">Reset to default</button>' +
                                '<a href="https://github.com/mahdiasd/MarkdownRTL" target="_blank" rel="noopener noreferrer" class="pm-github-btn" title="Star Markdown RTL on GitHub">' +
                                    '<svg viewBox="0 0 24 24" class="pm-star-icon">' +
                                        '<polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"></polygon>' +
                                    '</svg>' +
                                    '<span>Star on github</span>' +
                                '</a>' +
                            '</div>' +
                        '</div>';

                    var trigger = switcher.querySelector('#pm-trigger');
                    var card = switcher.querySelector('#pm-card');
                    var statusDot = switcher.querySelector('#pm-status-dot');
                    var closeBtn = switcher.querySelector('#pm-close-btn');

                    function closeAllDropdowns() {
                        var drops = switcher.querySelectorAll('.pm-combobox-dropdown');
                        for (var d = 0; d < drops.length; d++) {
                            drops[d].classList.remove('pm-open');
                        }
                    }

                    trigger.addEventListener('click', function(e) {
                        e.stopPropagation();
                        switcher.classList.toggle('pm-open');
                        if (!switcher.classList.contains('pm-open')) {
                            closeAllDropdowns();
                        }
                    });

                    card.addEventListener('click', function(e) {
                        e.stopPropagation();
                    });

                    closeBtn.addEventListener('click', function(e) {
                        e.stopPropagation();
                        switcher.classList.remove('pm-open');
                        closeAllDropdowns();
                    });

                    var ghBtn = switcher.querySelector('.pm-github-btn');
                    if (ghBtn) {
                        ghBtn.addEventListener('click', function(e) {
                            e.preventDefault();
                            e.stopPropagation();
                            var targetUrl = 'https://github.com/mahdiasd/MarkdownRTL';
                            try {
                                if (window.__IntelliJTools && window.__IntelliJTools.messagePipe && typeof window.__IntelliJTools.messagePipe.post === 'function') {
                                    window.__IntelliJTools.messagePipe.post('openLink', targetUrl);
                                    return;
                                }
                            } catch (_) {}
                            try {
                                if (window.__IntelliJTools && typeof window.__IntelliJTools.processClick === 'function') {
                                    window.__IntelliJTools.processClick(ghBtn);
                                    return;
                                }
                            } catch (_) {}
                            try {
                                window.open(targetUrl, '_blank');
                            } catch (_) {
                                window.location.href = targetUrl;
                            }
                        });
                    }

                    document.addEventListener('click', function(e) {
                        if (!switcher.contains(e.target)) {
                            switcher.classList.remove('pm-open');
                        }
                        closeAllDropdowns();
                    });

                    // Controls
                    var optEnabled = card.querySelector('#pm-opt-enabled');
                    var controlsWrap = card.querySelector('#pm-controls-wrap');
                    var tabBtns = card.querySelectorAll('.pm-tab-btn');
                    var decLh = card.querySelector('#pm-dec-lh');
                    var incLh = card.querySelector('#pm-inc-lh');
                    var lhVal = card.querySelector('#pm-lh-val');
                    var decFs = card.querySelector('#pm-dec-fs');
                    var incFs = card.querySelector('#pm-inc-fs');
                    var fsVal = card.querySelector('#pm-fs-val');
                    var resetBtn = card.querySelector('#pm-reset-btn');

                    // Fonts population
                    var suggestedFa = ['Vazirmatn', 'Sahel', 'Shabnam', 'Samim', 'Parastoo', 'Tanha', 'Tahoma', 'Segoe UI', 'Noto Sans Arabic', 'Arial'];
                    var suggestedEn = ['JetBrains Mono', 'SF Pro', 'Inter', 'Geist', 'Segoe UI', 'Roboto', 'Helvetica Neue', 'Arial', 'System UI'];
                    var suggestedCode = ['JetBrains Mono', 'Cascadia Code', 'Fira Code', 'SF Mono', 'Geist Mono', 'Consolas', 'Courier New', 'Menlo', 'Monaco'];

                    savedFa = cleanFontName(getPref('fa_font', defaultFaFont)) || defaultFaFont;
                    savedEn = cleanFontName(getPref('en_font', defaultEnFont)) || defaultEnFont;
                    savedCode = cleanFontName(getPref('code_font', defaultCodeFont)) || defaultCodeFont;

                    function updateFaFont(val) {
                        val = cleanFontName(val);
                        if (!val) return;
                        savedFa = val;
                        savePref('fa_font', val);
                        document.documentElement.style.setProperty('--pm-fa-font', "'" + val + "', 'PersianMarkdownBundledVazir', -apple-system, BlinkMacSystemFont, 'Segoe UI', Tahoma, sans-serif");
                    }

                    function updateEnFont(val) {
                        val = cleanFontName(val);
                        if (!val) return;
                        savedEn = val;
                        savePref('en_font', val);
                        document.documentElement.style.setProperty('--pm-en-font', "'" + val + "', 'PersianMarkdownBundledJBMono', 'SF Pro', Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif");
                    }

                    function updateCodeFont(val) {
                        val = cleanFontName(val);
                        if (!val) return;
                        savedCode = val;
                        savePref('code_font', val);
                        document.documentElement.style.setProperty('--pm-code-font', "'" + val + "', 'PersianMarkdownBundledJBMono', Menlo, Monaco, Consolas, monospace");
                    }

                    function postUpdateToIde() {
                        try {
                            if (window.__IntelliJTools && window.__IntelliJTools.messagePipe && typeof window.__IntelliJTools.messagePipe.post === 'function') {
                                var params = [
                                    'enabled=' + (isEnabled ? 'true' : 'false'),
                                    'mode=' + encodeURIComponent(currentMode),
                                    'fontSize=' + encodeURIComponent(currentFs),
                                    'lineHeight=' + encodeURIComponent(currentLh.toFixed(1)),
                                    'faFont=' + encodeURIComponent(comboFa ? comboFa.getValue() : savedFa),
                                    'enFont=' + encodeURIComponent(comboEn ? comboEn.getValue() : savedEn),
                                    'codeFont=' + encodeURIComponent(comboCode ? comboCode.getValue() : savedCode),
                                    'frontmatter=' + (renderFrontMatterEnabled ? 'true' : 'false'),
                                    'mermaid=' + (renderMermaidEnabled ? 'true' : 'false')
                                ].join('&');
                                window.__IntelliJTools.messagePipe.post('pmUpdateSettings', params);
                            }
                        } catch (_) {}
                    }

                    var comboFa = setupFontCombobox('fa', 
                        card.querySelector('#pm-input-fa'), 
                        card.querySelector('#pm-drop-fa'), 
                        suggestedFa, 
                        savedFa, 
                        function(selectedFont) {
                            updateFaFont(selectedFont);
                            postUpdateToIde();
                        }
                    );

                    var comboEn = setupFontCombobox('en', 
                        card.querySelector('#pm-input-en'), 
                        card.querySelector('#pm-drop-en'), 
                        suggestedEn, 
                        savedEn, 
                        function(selectedFont) {
                            updateEnFont(selectedFont);
                            postUpdateToIde();
                        }
                    );

                    var comboCode = setupFontCombobox('code', 
                        card.querySelector('#pm-input-code'), 
                        card.querySelector('#pm-drop-code'), 
                        suggestedCode, 
                        savedCode, 
                        function(selectedFont) {
                            updateCodeFont(selectedFont);
                            postUpdateToIde();
                        }
                    );

                    function updateControlsState() {
                        if (isEnabled) {
                            document.documentElement.classList.remove('pm-disabled');
                            statusDot.classList.remove('pm-disabled-dot');
                            if (controlsWrap) controlsWrap.classList.remove('pm-controls-disabled');
                            applyDirections();
                        } else {
                            document.documentElement.classList.add('pm-disabled');
                            statusDot.classList.add('pm-disabled-dot');
                            if (controlsWrap) controlsWrap.classList.add('pm-controls-disabled');
                            resetDirections();
                        }
                    }

                    function setEnabled(val) {
                        isEnabled = !!val;
                        savePref('enabled', isEnabled ? '1' : '0');
                        optEnabled.checked = isEnabled;
                        updateControlsState();
                        postUpdateToIde();
                    }

                    function setMode(newMode) {
                        if (newMode !== 'auto' && newMode !== 'force_rtl' && newMode !== 'force_ltr') {
                            newMode = 'auto';
                        }
                        currentMode = newMode;
                        savePref('mode', currentMode);

                        for (var t = 0; t < tabBtns.length; t++) {
                            var tab = tabBtns[t];
                            if (tab.getAttribute('data-mode') === currentMode) {
                                tab.classList.add('pm-active');
                            } else {
                                tab.classList.remove('pm-active');
                            }
                        }

                        document.documentElement.setAttribute('data-pm-mode', currentMode);
                        if (isEnabled) {
                            applyDirections();
                        } else {
                            resetDirections();
                        }
                        postUpdateToIde();
                    }

                    // Enabled toggle
                    optEnabled.addEventListener('change', function() {
                        setEnabled(optEnabled.checked);
                    });

                    // Tab buttons
                    for (var tb = 0; tb < tabBtns.length; tb++) {
                        (function(btn) {
                            btn.addEventListener('click', function(e) {
                                e.stopPropagation();
                                var m = btn.getAttribute('data-mode');
                                setMode(m);
                            });
                        })(tabBtns[tb]);
                    }

                    // Font Size stepper
                    function updateFsDisplay() {
                        fsVal.textContent = currentFs + 'px';
                        document.documentElement.style.setProperty('--pm-font-size', currentFs + 'px');
                        savePref('font_size', currentFs);
                    }
                    decFs.addEventListener('click', function(e) {
                        e.stopPropagation();
                        currentFs = Math.max(10, currentFs - 1);
                        updateFsDisplay();
                        postUpdateToIde();
                    });
                    incFs.addEventListener('click', function(e) {
                        e.stopPropagation();
                        currentFs = Math.min(32, currentFs + 1);
                        updateFsDisplay();
                        postUpdateToIde();
                    });

                    // Line Height stepper
                    function updateLhDisplay() {
                        lhVal.textContent = currentLh.toFixed(1) + 'x';
                        document.documentElement.style.setProperty('--pm-line-height', currentLh.toFixed(1));
                        savePref('line_height', currentLh.toFixed(1));
                    }
                    decLh.addEventListener('click', function(e) {
                        e.stopPropagation();
                        currentLh = Math.max(1.0, Math.round((currentLh - 0.1) * 10) / 10);
                        updateLhDisplay();
                        postUpdateToIde();
                    });
                    incLh.addEventListener('click', function(e) {
                        e.stopPropagation();
                        currentLh = Math.min(2.8, Math.round((currentLh + 0.1) * 10) / 10);
                        updateLhDisplay();
                        postUpdateToIde();
                    });

                    // Feature Toggles (Front Matter & Mermaid)
                    var optFm = card.querySelector('#pm-opt-fm');
                    if (optFm) {
                        optFm.checked = renderFrontMatterEnabled;
                        optFm.addEventListener('change', function(e) {
                            e.stopPropagation();
                            renderFrontMatterEnabled = optFm.checked;
                            savePref('frontmatter', renderFrontMatterEnabled ? '1' : '0');
                            postUpdateToIde();
                            processFrontMatter();
                        });
                    }

                    var optMermaid = card.querySelector('#pm-opt-mermaid');
                    if (optMermaid) {
                        optMermaid.checked = renderMermaidEnabled;
                        optMermaid.addEventListener('change', function(e) {
                            e.stopPropagation();
                            renderMermaidEnabled = optMermaid.checked;
                            savePref('mermaid', renderMermaidEnabled ? '1' : '0');
                            postUpdateToIde();
                            processMermaidDiagrams();
                        });
                    }

                    // Reset Defaults
                    resetBtn.addEventListener('click', function(e) {
                        e.stopPropagation();
                        setEnabled(true);
                        setMode('auto');

                        comboFa.setValue('Vazirmatn');
                        updateFaFont('Vazirmatn');
                        comboEn.setValue('JetBrains Mono');
                        updateEnFont('JetBrains Mono');
                        comboCode.setValue('JetBrains Mono');
                        updateCodeFont('JetBrains Mono');

                        currentFs = defaultFs;
                        updateFsDisplay();
                        currentLh = defaultLh;
                        updateLhDisplay();

                        if (optFm) optFm.checked = true;
                        renderFrontMatterEnabled = true;
                        savePref('frontmatter', '1');
                        processFrontMatter();

                        if (optMermaid) optMermaid.checked = true;
                        renderMermaidEnabled = true;
                        savePref('mermaid', '1');
                        processMermaidDiagrams();

                        postUpdateToIde();
                    });

                    // Keyboard shortcuts
                    document.addEventListener('keydown', function(e) {
                        if (e.key === 'Escape') {
                            var anyOpen = switcher.querySelector('.pm-combobox-dropdown.pm-open');
                            if (anyOpen) {
                                closeAllDropdowns();
                            } else if (switcher.classList.contains('pm-open')) {
                                switcher.classList.remove('pm-open');
                            }
                        } else if (e.altKey && (e.key === 'e' || e.key === 'E' || e.code === 'KeyE')) {
                            e.preventDefault();
                            setEnabled(!isEnabled);
                        } else if (e.altKey && (e.key === 'r' || e.key === 'R' || e.code === 'KeyR')) {
                            e.preventDefault();
                            if (currentMode === 'auto') {
                                setMode('force_rtl');
                            } else if (currentMode === 'force_rtl') {
                                setMode('force_ltr');
                            } else {
                                setMode('auto');
                            }
                        }
                    });

                    // Initialize widget state
                    optEnabled.checked = isEnabled;
                    updateControlsState();
                    setMode(currentMode);

                    updateFsDisplay();
                    updateLhDisplay();
                    updateFaFont(savedFa);
                    updateEnFont(savedEn);
                    updateCodeFont(savedCode);

                    target.appendChild(switcher);
                }

                function init() {
                    createSwitcherUI();
                    applyDirections();
                }

                if (document.readyState === 'loading') {
                    document.addEventListener('DOMContentLoaded', init);
                } else {
                    init();
                }

                if (window.IncrementalDOM && window.IncrementalDOM.notifications && window.IncrementalDOM.notifications.afterPatchListeners) {
                    window.IncrementalDOM.notifications.afterPatchListeners.push(function() {
                        createSwitcherUI();
                        applyDirections();
                    });
                }

                if (window.MutationObserver) {
                    var debounceTimer = null;
                    var observer = new MutationObserver(function(mutations) {
                        var onlyInternalPlugin = true;
                        for (var m = 0; m < mutations.length; m++) {
                            var mut = mutations[m];
                            var target = mut.target;
                            var isInternal = target && (
                                target.id === 'persian-markdown-switcher' ||
                                (target.classList && (target.classList.contains('pm-frontmatter-container') || target.classList.contains('pm-mermaid-container'))) ||
                                (target.closest && (target.closest('#persian-markdown-switcher') || target.closest('.pm-frontmatter-container') || target.closest('.pm-mermaid-container')))
                            );
                            if (!isInternal) {
                                onlyInternalPlugin = false;
                                break;
                            }
                        }
                        if (onlyInternalPlugin) return;

                        if (debounceTimer) clearTimeout(debounceTimer);
                        debounceTimer = setTimeout(function() {
                            createSwitcherUI();
                            applyDirections();
                        }, 50);
                    });
                    var obsTarget = document.documentElement || document.body;
                    if (obsTarget) {
                        observer.observe(obsTarget, { childList: true, subtree: true });
                    }
                }
            })();
        """.trimIndent()
    }
}

