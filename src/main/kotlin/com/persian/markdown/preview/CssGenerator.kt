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

    fun generateCss(state: PersianMarkdownState): String {
        val sb = StringBuilder()

        if (state.useBundledFont && bundledRegularFontBase64 != null) {
            sb.append("""
                @font-face {
                    font-family: 'PersianMarkdownBundledVazir';
                    src: url('data:font/truetype;charset=utf-8;base64,${bundledRegularFontBase64}') format('truetype');
                    font-weight: 400;
                    font-style: normal;
                }
            """.trimIndent()).append("\n")

            if (bundledBoldFontBase64 != null) {
                sb.append("""
                    @font-face {
                        font-family: 'PersianMarkdownBundledVazir';
                        src: url('data:font/truetype;charset=utf-8;base64,${bundledBoldFontBase64}') format('truetype');
                        font-weight: 700;
                        font-style: normal;
                    }
                """.trimIndent()).append("\n")
            }
        }

        if (bundledJbRegularFontBase64 != null) {
            sb.append("""
                @font-face {
                    font-family: 'PersianMarkdownBundledJBMono';
                    src: url('data:font/truetype;charset=utf-8;base64,${bundledJbRegularFontBase64}') format('truetype');
                    font-weight: 400;
                    font-style: normal;
                }
            """.trimIndent()).append("\n")

            if (bundledJbBoldFontBase64 != null) {
                sb.append("""
                    @font-face {
                        font-family: 'PersianMarkdownBundledJBMono';
                        src: url('data:font/truetype;charset=utf-8;base64,${bundledJbBoldFontBase64}') format('truetype');
                        font-weight: 700;
                        font-style: normal;
                    }
                """.trimIndent()).append("\n")
            }
        }

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
            p:not(#persian-markdown-switcher *),
            li:not(#persian-markdown-switcher *),
            blockquote:not(#persian-markdown-switcher *),
            table:not(#persian-markdown-switcher *),
            td:not(#persian-markdown-switcher *),
            th:not(#persian-markdown-switcher *) {
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
                h1:not(#persian-markdown-switcher *),
                h2:not(#persian-markdown-switcher *),
                h3:not(#persian-markdown-switcher *),
                h4:not(#persian-markdown-switcher *),
                h5:not(#persian-markdown-switcher *),
                h6:not(#persian-markdown-switcher *) {
                    font-family: var(--pm-fa-font, $faFontStack) !important;
                }
                h1:not(#persian-markdown-switcher *) { font-size: calc(var(--pm-font-size, ${fs}px) * 1.75) !important; line-height: 1.3 !important; }
                h2:not(#persian-markdown-switcher *) { font-size: calc(var(--pm-font-size, ${fs}px) * 1.50) !important; line-height: 1.35 !important; }
                h3:not(#persian-markdown-switcher *) { font-size: calc(var(--pm-font-size, ${fs}px) * 1.25) !important; line-height: 1.4 !important; }
                h4:not(#persian-markdown-switcher *) { font-size: calc(var(--pm-font-size, ${fs}px) * 1.12) !important; line-height: 1.4 !important; }
                h5:not(#persian-markdown-switcher *), h6:not(#persian-markdown-switcher *) { font-size: var(--pm-font-size, ${fs}px) !important; line-height: 1.4 !important; }
            """.trimIndent()).append("\n")
        }

        when (state.directionMode) {
            DirectionMode.FORCE_RTL -> {
                sb.append("""
                    body:not(#persian-markdown-switcher),
                    p:not(#persian-markdown-switcher *),
                    h1:not(#persian-markdown-switcher *),
                    h2:not(#persian-markdown-switcher *),
                    h3:not(#persian-markdown-switcher *),
                    h4:not(#persian-markdown-switcher *),
                    h5:not(#persian-markdown-switcher *),
                    h6:not(#persian-markdown-switcher *),
                    ul:not(#persian-markdown-switcher *),
                    ol:not(#persian-markdown-switcher *),
                    blockquote:not(#persian-markdown-switcher *),
                    table:not(#persian-markdown-switcher *),
                    tr:not(#persian-markdown-switcher *),
                    td:not(#persian-markdown-switcher *),
                    th:not(#persian-markdown-switcher *) {
                        direction: rtl !important;
                        text-align: right !important;
                    }
                    ul:not(#persian-markdown-switcher *), ol:not(#persian-markdown-switcher *) {
                        padding-right: 1.8em !important;
                        padding-left: 0 !important;
                    }
                    blockquote:not(#persian-markdown-switcher *) {
                        border-right: 4px solid #6366f1 !important;
                        border-left: none !important;
                        padding-right: 1em !important;
                        padding-left: 0 !important;
                    }
                """.trimIndent()).append("\n")
            }
            DirectionMode.FORCE_LTR -> {
                sb.append("""
                    body:not(#persian-markdown-switcher),
                    p:not(#persian-markdown-switcher *),
                    h1:not(#persian-markdown-switcher *),
                    h2:not(#persian-markdown-switcher *),
                    h3:not(#persian-markdown-switcher *),
                    h4:not(#persian-markdown-switcher *),
                    h5:not(#persian-markdown-switcher *),
                    h6:not(#persian-markdown-switcher *),
                    ul:not(#persian-markdown-switcher *),
                    ol:not(#persian-markdown-switcher *),
                    blockquote:not(#persian-markdown-switcher *),
                    table:not(#persian-markdown-switcher *),
                    tr:not(#persian-markdown-switcher *),
                    td:not(#persian-markdown-switcher *),
                    th:not(#persian-markdown-switcher *) {
                        direction: ltr !important;
                        text-align: left !important;
                        font-family: var(--pm-en-font, $enFontStack) !important;
                    }
                    ul:not(#persian-markdown-switcher *), ol:not(#persian-markdown-switcher *) {
                        padding-left: 1.8em !important;
                        padding-right: 0 !important;
                    }
                    blockquote:not(#persian-markdown-switcher *) {
                        border-left: 4px solid #6366f1 !important;
                        border-right: none !important;
                        padding-left: 1em !important;
                        padding-right: 0 !important;
                    }
                """.trimIndent()).append("\n")
            }
            DirectionMode.AUTO -> {
                sb.append("""
                    [dir="rtl"]:not(#persian-markdown-switcher):not(#persian-markdown-switcher *),
                    .persian-dir-rtl:not(#persian-markdown-switcher):not(#persian-markdown-switcher *) {
                        direction: rtl !important;
                        text-align: right !important;
                        font-family: var(--pm-fa-font, $faFontStack) !important;
                    }
                    [dir="ltr"]:not(#persian-markdown-switcher):not(#persian-markdown-switcher *),
                    .persian-dir-ltr:not(#persian-markdown-switcher):not(#persian-markdown-switcher *) {
                        direction: ltr !important;
                        text-align: left !important;
                        font-family: var(--pm-en-font, $enFontStack) !important;
                    }
                    blockquote[dir="rtl"]:not(#persian-markdown-switcher *),
                    blockquote.persian-dir-rtl:not(#persian-markdown-switcher *) {
                        border-right: 4px solid #6366f1 !important;
                        border-left: none !important;
                        padding-right: 1em !important;
                        padding-left: 0 !important;
                    }
                    ul[dir="rtl"]:not(#persian-markdown-switcher *),
                    ol[dir="rtl"]:not(#persian-markdown-switcher *),
                    ul.persian-dir-rtl:not(#persian-markdown-switcher *),
                    ol.persian-dir-rtl:not(#persian-markdown-switcher *) {
                        padding-right: 1.8em !important;
                        padding-left: 0 !important;
                    }
                """.trimIndent()).append("\n")
            }
        }

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
            html.pm-disabled [dir="rtl"]:not(#persian-markdown-switcher *),
            html.pm-disabled .persian-dir-rtl:not(#persian-markdown-switcher *) {
                direction: ltr !important;
                text-align: left !important;
            }
            html.pm-disabled blockquote:not(#persian-markdown-switcher *) {
                border-left: 4px solid #3b82f6 !important;
                border-right: none !important;
                padding-left: 1em !important;
                padding-right: 0 !important;
            }
            html.pm-disabled ul:not(#persian-markdown-switcher *),
            html.pm-disabled ol:not(#persian-markdown-switcher *) {
                padding-left: 1.8em !important;
                padding-right: 0 !important;
            }
        """.trimIndent()).append("\n")

        // Styles for Linear / Raycast Bento Switcher in preview (bottom-left popup)
        sb.append("""
            /* --- Isolated Floating Switcher & Dialog --- */
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
                bottom: 12px !important;
                left: 12px !important;
                z-index: 2147483647 !important;
                user-select: none !important;
                -webkit-user-select: none !important;
                line-height: 1.2 !important;
            }

            /* Floating Trigger Capsule Pill */
            #pm-trigger {
                position: relative !important;
                display: inline-flex !important;
                align-items: center !important;
                gap: 7px !important;
                padding: 3px 11px 3px 4px !important;
                height: 28px !important;
                border-radius: 9999px !important;
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
                width: 20px !important;
                height: 20px !important;
                border-radius: 6px !important;
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
                width: 20px !important;
                height: 20px !important;
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
                font-size: 11.5px !important;
                font-weight: 600 !important;
                color: #F0F9FF !important;
                letter-spacing: -0.01em !important;
                white-space: nowrap !important;
                line-height: 1 !important;
            }
            #pm-status-dot.pm-disabled-dot ~ #pm-current-label {
                color: #94A3B8 !important;
            }

            /* Popover Compact Bento Card */
            #pm-card {
                position: absolute !important;
                bottom: calc(100% + 8px) !important;
                left: 0 !important;
                width: 270px !important;
                max-width: calc(100vw - 24px) !important;
                background: rgba(13, 17, 25, 0.97) !important;
                backdrop-filter: blur(20px) saturate(180%) !important;
                -webkit-backdrop-filter: blur(20px) saturate(180%) !important;
                border-radius: 12px !important;
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
                border-radius: 12px 12px 0 0 !important;
            }
            #persian-markdown-switcher.pm-open #pm-card {
                opacity: 1 !important;
                visibility: visible !important;
                transform: translateY(0) scale(1) !important;
                pointer-events: auto !important;
            }

            /* Header */
            #pm-header {
                padding: 6px 9px !important;
                border-bottom: 1px solid #1A212E !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                background: rgba(16, 20, 29, 0.8) !important;
                border-radius: 12px 12px 0 0 !important;
            }
            .pm-header-left {
                display: flex !important;
                align-items: center !important;
                gap: 6px !important;
            }
            .pm-brand-icon-box {
                width: 18px !important;
                height: 18px !important;
                border-radius: 5px !important;
                background: linear-gradient(135deg, rgba(30, 180, 235, 0.18), rgba(142, 217, 245, 0.1)) !important;
                border: 1px solid rgba(30, 180, 235, 0.35) !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
                flex-shrink: 0 !important;
            }
            .pm-brand-icon-box svg {
                width: 10px !important;
                height: 10px !important;
                color: #1EB4EB !important;
            }
            .pm-header-title {
                font-size: 13px !important;
                font-weight: 600 !important;
                color: #F1F5F9 !important;
                line-height: 1 !important;
            }
            .pm-header-badge {
                padding: 1.5px 5px !important;
                background: rgba(99, 102, 241, 0.12) !important;
                border: 1px solid rgba(99, 102, 241, 0.25) !important;
                font-size: 10px !important;
                font-family: 'JetBrains Mono', monospace !important;
                font-weight: 500 !important;
                color: #A5B4FC !important;
                border-radius: 4px !important;
                line-height: 1 !important;
            }
            #pm-close-btn {
                height: 20px !important;
                padding: 0 6px !important;
                border-radius: 4px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
                color: #94A3B8 !important;
                background: transparent !important;
                border: 1px solid transparent !important;
                font-family: 'JetBrains Mono', monospace !important;
                font-size: 10.5px !important;
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
                padding: 6px 7px !important;
                display: flex !important;
                flex-direction: column !important;
                gap: 5px !important;
                overflow: visible !important;
            }

            /* Toggle Controls */
            .pm-toggle-grid {
                display: grid !important;
                grid-template-columns: 1fr 1fr !important;
                gap: 5px !important;
            }
            .pm-toggle-card {
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                padding: 4px 8px !important;
                height: 28px !important;
                border-radius: 6px !important;
                background: #121722 !important;
                border: 1px solid #1E2536 !important;
            }
            .pm-toggle-label-wrap {
                display: flex !important;
                align-items: center !important;
                gap: 4px !important;
            }
            .pm-toggle-title {
                font-size: 11.5px !important;
                font-weight: 500 !important;
                color: #CBD5E1 !important;
                line-height: 1 !important;
            }
            .pm-kbd-tag {
                padding: 0 3px !important;
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
                width: 24px !important;
                height: 14px !important;
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
                width: 10px !important;
                height: 10px !important;
                background: #FFFFFF !important;
                border-radius: 50% !important;
                transition: transform 0.15s ease !important;
                box-shadow: 0 1px 2px rgba(0,0,0,0.3) !important;
            }
            .pm-switch input:checked + .pm-switch-track {
                background: #1EB4EB !important;
            }
            .pm-switch input:checked + .pm-switch-track::after {
                transform: translateX(10px) !important;
            }

            /* Section Title - Clean, subtle, no uppercase screaming badge */
            .pm-section-title {
                font-size: 10.5px !important;
                font-weight: 500 !important;
                color: #64748B !important;
                padding: 2px 2px 0 2px !important;
                letter-spacing: normal !important;
                line-height: 1 !important;
            }

            /* Bento Card Container */
            .pm-bento-card {
                background: #121722 !important;
                border-radius: 6px !important;
                border: 1px solid #1E2536 !important;
                overflow: visible !important;
            }

            /* Searchable Font Combobox Row */
            .pm-font-combobox {
                position: relative !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                gap: 5px !important;
                padding: 4px 7px !important;
                border-bottom: 1px solid #181F2C !important;
            }
            .pm-font-combobox:last-child {
                border-bottom: none !important;
            }
            .pm-combobox-label {
                font-size: 11px !important;
                font-weight: 500 !important;
                color: #94A3B8 !important;
                white-space: nowrap !important;
                width: 40px !important;
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
                height: 24px !important;
                background: #0C1018 !important;
                border: 1px solid #1C2332 !important;
                border-radius: 4px !important;
                color: #E2E8F0 !important;
                font-size: 11.5px !important;
                font-family: 'JetBrains Mono', -apple-system, sans-serif !important;
                padding: 0 16px 0 6px !important;
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
                width: 8px !important;
                height: 8px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: center !important;
            }
            .pm-combobox-arrow svg {
                width: 8px !important;
                height: 8px !important;
            }

            /* Floating Searchable Dropdown */
            .pm-combobox-dropdown {
                position: absolute !important;
                top: calc(100% + 2px) !important;
                left: 0 !important;
                right: 0 !important;
                max-height: 140px !important;
                overflow-y: auto !important;
                background: #0E131E !important;
                border: 1px solid #252F42 !important;
                border-radius: 5px !important;
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.8) !important;
                z-index: 999999 !important;
                display: none;
                padding: 2px !important;
            }
            .pm-combobox-dropdown.pm-open {
                display: block !important;
            }
            .pm-combobox-dropdown::-webkit-scrollbar {
                width: 3px !important;
            }
            .pm-combobox-dropdown::-webkit-scrollbar-thumb {
                background: #263145 !important;
                border-radius: 2px !important;
            }
            .pm-dropdown-header {
                font-size: 9.5px !important;
                font-weight: 600 !important;
                color: #64748B !important;
                padding: 3px 5px 2px 5px !important;
                user-select: none !important;
            }
            .pm-dropdown-item {
                font-size: 11px !important;
                font-family: 'JetBrains Mono', -apple-system, sans-serif !important;
                color: #CBD5E1 !important;
                padding: 3px 6px !important;
                border-radius: 3px !important;
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
                color: #34D399 !important;
                border-bottom: 1px dashed #1E2738 !important;
                margin-bottom: 2px !important;
            }
            .pm-dropdown-empty {
                font-size: 10.5px !important;
                color: #64748B !important;
                padding: 5px !important;
                text-align: center !important;
            }

            /* Metrics & Layout Grid */
            .pm-metrics-grid {
                display: grid !important;
                grid-template-columns: 1fr 1fr !important;
                gap: 5px !important;
            }
            .pm-metric-card {
                background: #121722 !important;
                border-radius: 6px !important;
                border: 1px solid #1E2536 !important;
                padding: 4px 7px !important;
                height: 28px !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
            }
            .pm-metric-label {
                font-size: 11px !important;
                font-weight: 500 !important;
                color: #94A3B8 !important;
                line-height: 1 !important;
            }
            .pm-stepper-group {
                display: flex !important;
                align-items: center !important;
                gap: 3px !important;
            }
            .pm-stepper-btn {
                width: 17px !important;
                height: 17px !important;
                border-radius: 3px !important;
                background: #1A2130 !important;
                color: #94A3B8 !important;
                border: none !important;
                cursor: pointer !important;
                font-size: 12px !important;
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
                font-size: 10.5px !important;
                color: #A5B4FC !important;
                font-weight: 500 !important;
                padding: 1px 4px !important;
                background: rgba(99, 102, 241, 0.1) !important;
                border-radius: 3px !important;
                border: 1px solid rgba(99, 102, 241, 0.2) !important;
                min-width: 32px !important;
                text-align: center !important;
                line-height: 1.2 !important;
            }

            /* Compact Footer */
            #pm-footer {
                padding: 5px 8px !important;
                background: rgba(16, 20, 29, 0.8) !important;
                border-top: 1px solid #1A212E !important;
                display: flex !important;
                align-items: center !important;
                justify-content: space-between !important;
                border-radius: 0 0 12px 12px !important;
            }
            #pm-reset-btn {
                padding: 3px 6px !important;
                font-size: 11px !important;
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
                padding: 3px 7px !important;
                border-radius: 4px !important;
                background: transparent !important;
                border: 1px solid transparent !important;
                color: #94A3B8 !important;
                font-size: 11px !important;
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
        val initialMode = state.directionMode.id
        val defaultFontSize = state.fontSize
        val defaultLineHeight = state.lineHeight
        val defaultFaFont = state.fontFamily?.split(",")?.firstOrNull()?.trim()?.replace("'", "")?.replace("\"", "")?.ifBlank { null } ?: "Vazirmatn"
        val defaultEnFont = state.enFontFamily?.split(",")?.firstOrNull()?.trim()?.replace("'", "")?.replace("\"", "")?.ifBlank { null } ?: "JetBrains Mono"
        val defaultCodeFont = state.codeFontFamily?.split(",")?.firstOrNull()?.trim()?.replace("'", "")?.replace("\"", "")?.ifBlank { null } ?: "JetBrains Mono"

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
                var currentMode = '${escapeJs(initialMode)}';
                var systemFonts = $systemFontsJson;
                var persianRegex = /[\u0600-\u06FF\u0750-\u077F\u08A0-\u08FF\uFB50-\uFDFF\uFE70-\uFEFF]/;

                function savePref(k, v) { try { localStorage.setItem('pm_' + k, v); } catch(e){} }
                function getPref(k, def) { try { var v = localStorage.getItem('pm_' + k); return v !== null ? v : def; } catch(e){ return def; } }

                function hasPersian(text) {
                    return text ? persianRegex.test(text) : false;
                }

                function isPersianProse(el) {
                    if (el.tagName === 'PRE' || el.classList.contains('code-fence') || el.classList.contains('markdown-code-fence')) {
                        return false;
                    }
                    var clone = el.cloneNode(true);
                    var codes = clone.querySelectorAll('pre, code');
                    for (var c = 0; c < codes.length; c++) {
                        codes[c].remove();
                    }
                    var text = (clone.textContent || '').trim();
                    if (!text) {
                        text = (el.textContent || '').trim();
                    }
                    text = text.replace(/\[[xX\s]?\]/g, '')
                               .replace(/\[.*?\]/g, '')
                               .replace(/https?:\/\/\S+/g, '')
                               .trim();
                    return hasPersian(text);
                }

                function resetDirections() {
                    var elements = document.querySelectorAll('p, h1, h2, h3, h4, h5, h6, li, blockquote, td, th');
                    for (var i = 0; i < elements.length; i++) {
                        var el = elements[i];
                        if (el.closest('pre, #persian-markdown-switcher')) continue;
                        el.removeAttribute('dir');
                        el.classList.remove('persian-dir-rtl');
                        el.classList.remove('persian-dir-ltr');
                    }
                }

                function applyDirections() {
                    var enabledInput = document.getElementById('pm-opt-enabled');
                    if (enabledInput && !enabledInput.checked) {
                        resetDirections();
                        return;
                    }

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

                function cleanFontName(name) {
                    if (!name) return '';
                    if (name.indexOf(',') !== -1) {
                        name = name.split(',')[0];
                    }
                    return name.trim().replace(/['"]/g, '');
                }

                function cleanFontName(name) {
                    if (!name) return '';
                    if (name.indexOf(',') !== -1) {
                        name = name.split(',')[0];
                    }
                    return name.trim().replace(/['"]/g, '');
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
                    var existing = document.getElementById('persian-markdown-switcher');
                    var container = document.body || document.documentElement;
                    if (!container) return;

                    if (existing) {
                        if (document.body && existing.parentElement !== document.body) {
                            document.body.appendChild(existing);
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
                                    '<span class="pm-header-badge">v1.3</span>' +
                                '</div>' +
                                '<div class="pm-header-right">' +
                                    '<button id="pm-close-btn" type="button" title="Close (ESC)">esc</button>' +
                                '</div>' +
                            '</div>' +
                            '<div id="pm-body">' +
                                '<div class="pm-toggle-grid">' +
                                    '<div class="pm-toggle-card">' +
                                        '<div class="pm-toggle-label-wrap">' +
                                            '<span class="pm-toggle-title">Enabled</span>' +
                                            '<span class="pm-kbd-tag">⌥E</span>' +
                                        '</div>' +
                                        '<label class="pm-switch">' +
                                            '<input type="checkbox" id="pm-opt-enabled" checked>' +
                                            '<span class="pm-switch-track"></span>' +
                                        '</label>' +
                                    '</div>' +
                                    '<div class="pm-toggle-card">' +
                                        '<div class="pm-toggle-label-wrap">' +
                                            '<span class="pm-toggle-title">Force RTL</span>' +
                                            '<span class="pm-kbd-tag">⌥R</span>' +
                                        '</div>' +
                                        '<label class="pm-switch">' +
                                            '<input type="checkbox" id="pm-opt-force-rtl">' +
                                            '<span class="pm-switch-track"></span>' +
                                        '</label>' +
                                    '</div>' +
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
                                        '<span class="pm-metric-label">Line</span>' +
                                        '<div class="pm-stepper-group">' +
                                            '<button type="button" class="pm-stepper-btn" id="pm-dec-lh">-</button>' +
                                            '<span class="pm-metric-val" id="pm-lh-val">' + defaultLh.toFixed(1) + 'x</span>' +
                                            '<button type="button" class="pm-stepper-btn" id="pm-inc-lh">+</button>' +
                                        '</div>' +
                                    '</div>' +
                                    '<div class="pm-metric-card">' +
                                        '<span class="pm-metric-label">Size</span>' +
                                        '<div class="pm-stepper-group">' +
                                            '<button type="button" class="pm-stepper-btn" id="pm-dec-fs">-</button>' +
                                            '<span class="pm-metric-val" id="pm-fs-val">' + defaultFs + 'px</span>' +
                                            '<button type="button" class="pm-stepper-btn" id="pm-inc-fs">+</button>' +
                                        '</div>' +
                                    '</div>' +
                                '</div>' +
                            '</div>' +
                            '<div id="pm-footer">' +
                                '<button id="pm-reset-btn" type="button">Reset Defaults</button>' +
                                '<a href="https://github.com/mahdiasd/MarkdownRTL" class="pm-github-btn" title="Star Markdown RTL on GitHub">' +
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
                            try {
                                window.location.href = 'https://github.com/mahdiasd/MarkdownRTL';
                            } catch (_) {
                                window.open('https://github.com/mahdiasd/MarkdownRTL', '_blank');
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
                    var optForceRtl = card.querySelector('#pm-opt-force-rtl');
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

                    var savedFa = cleanFontName(getPref('fa_font', defaultFaFont)) || 'Vazirmatn';
                    var savedEn = cleanFontName(getPref('en_font', defaultEnFont)) || 'JetBrains Mono';
                    var savedCode = cleanFontName(getPref('code_font', defaultCodeFont)) || 'JetBrains Mono';

                    function updateFaFont(val) {
                        val = cleanFontName(val);
                        if (!val) return;
                        savePref('fa_font', val);
                        document.documentElement.style.setProperty('--pm-fa-font', "'" + val + "', 'PersianMarkdownBundledVazir', -apple-system, BlinkMacSystemFont, 'Segoe UI', Tahoma, sans-serif");
                    }

                    function updateEnFont(val) {
                        val = cleanFontName(val);
                        if (!val) return;
                        savePref('en_font', val);
                        document.documentElement.style.setProperty('--pm-en-font', "'" + val + "', 'PersianMarkdownBundledJBMono', 'SF Pro', Inter, -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif");
                    }

                    function updateCodeFont(val) {
                        val = cleanFontName(val);
                        if (!val) return;
                        savePref('code_font', val);
                        document.documentElement.style.setProperty('--pm-code-font', "'" + val + "', 'PersianMarkdownBundledJBMono', Menlo, Monaco, Consolas, monospace");
                    }

                    var comboFa = setupFontCombobox('fa', 
                        card.querySelector('#pm-input-fa'), 
                        card.querySelector('#pm-drop-fa'), 
                        suggestedFa, 
                        savedFa, 
                        function(selectedFont) {
                            updateFaFont(selectedFont);
                        }
                    );

                    var comboEn = setupFontCombobox('en', 
                        card.querySelector('#pm-input-en'), 
                        card.querySelector('#pm-drop-en'), 
                        suggestedEn, 
                        savedEn, 
                        function(selectedFont) {
                            updateEnFont(selectedFont);
                        }
                    );

                    var comboCode = setupFontCombobox('code', 
                        card.querySelector('#pm-input-code'), 
                        card.querySelector('#pm-drop-code'), 
                        suggestedCode, 
                        savedCode, 
                        function(selectedFont) {
                            updateCodeFont(selectedFont);
                        }
                    );

                    // Enabled toggle
                    optEnabled.addEventListener('change', function() {
                        var isEnabled = optEnabled.checked;
                        savePref('enabled', isEnabled ? '1' : '0');
                        if (isEnabled) {
                            document.documentElement.classList.remove('pm-disabled');
                            statusDot.classList.remove('pm-disabled-dot');
                            applyDirections();
                        } else {
                            document.documentElement.classList.add('pm-disabled');
                            statusDot.classList.add('pm-disabled-dot');
                            resetDirections();
                        }
                    });

                    // Force RTL toggle
                    optForceRtl.addEventListener('change', function() {
                        var isForce = optForceRtl.checked;
                        currentMode = isForce ? 'force_rtl' : 'auto';
                        savePref('force_rtl', isForce ? '1' : '0');
                        applyDirections();
                    });

                    // Font Size stepper
                    var currentFs = parseInt(getPref('font_size', defaultFs), 10) || defaultFs;
                    function updateFsDisplay() {
                        fsVal.textContent = currentFs + 'px';
                        document.documentElement.style.setProperty('--pm-font-size', currentFs + 'px');
                        savePref('font_size', currentFs);
                    }
                    decFs.addEventListener('click', function(e) {
                        e.stopPropagation();
                        currentFs = Math.max(10, currentFs - 1);
                        updateFsDisplay();
                    });
                    incFs.addEventListener('click', function(e) {
                        e.stopPropagation();
                        currentFs = Math.min(32, currentFs + 1);
                        updateFsDisplay();
                    });

                    // Line Height stepper
                    var currentLh = parseFloat(getPref('line_height', defaultLh)) || defaultLh;
                    function updateLhDisplay() {
                        lhVal.textContent = currentLh.toFixed(1) + 'x';
                        document.documentElement.style.setProperty('--pm-line-height', currentLh.toFixed(1));
                        savePref('line_height', currentLh.toFixed(1));
                    }
                    decLh.addEventListener('click', function(e) {
                        e.stopPropagation();
                        currentLh = Math.max(1.0, Math.round((currentLh - 0.1) * 10) / 10);
                        updateLhDisplay();
                    });
                    incLh.addEventListener('click', function(e) {
                        e.stopPropagation();
                        currentLh = Math.min(2.8, Math.round((currentLh + 0.1) * 10) / 10);
                        updateLhDisplay();
                    });

                    // Reset Defaults
                    resetBtn.addEventListener('click', function(e) {
                        e.stopPropagation();
                        optEnabled.checked = true;
                        savePref('enabled', '1');
                        document.documentElement.classList.remove('pm-disabled');
                        statusDot.classList.remove('pm-disabled-dot');

                        optForceRtl.checked = false;
                        currentMode = 'auto';
                        savePref('force_rtl', '0');

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

                        applyDirections();
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
                            optEnabled.checked = !optEnabled.checked;
                            optEnabled.dispatchEvent(new Event('change'));
                        } else if (e.altKey && (e.key === 'r' || e.key === 'R' || e.code === 'KeyR')) {
                            e.preventDefault();
                            optForceRtl.checked = !optForceRtl.checked;
                            optForceRtl.dispatchEvent(new Event('change'));
                        }
                    });

                    // Load saved preferences on init
                    var savedEnabled = getPref('enabled', '1');
                    optEnabled.checked = savedEnabled === '1';
                    if (!optEnabled.checked) {
                        document.documentElement.classList.add('pm-disabled');
                        statusDot.classList.add('pm-disabled-dot');
                    } else {
                        statusDot.classList.remove('pm-disabled-dot');
                    }

                    var savedForceRtl = getPref('force_rtl', currentMode === 'force_rtl' ? '1' : '0');
                    optForceRtl.checked = savedForceRtl === '1';
                    currentMode = optForceRtl.checked ? 'force_rtl' : 'auto';

                    updateFsDisplay();
                    updateLhDisplay();
                    updateFaFont(savedFa);
                    updateEnFont(savedEn);
                    updateCodeFont(savedCode);

                    container.appendChild(switcher);
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

                if (window.MutationObserver) {
                    var observer = new MutationObserver(function() {
                        createSwitcherUI();
                        applyDirections();
                    });
                    var obsTarget = document.body || document.documentElement;
                    if (obsTarget) {
                        observer.observe(obsTarget, { childList: true, subtree: true });
                    }
                }
            })();
        """.trimIndent()
    }
}

