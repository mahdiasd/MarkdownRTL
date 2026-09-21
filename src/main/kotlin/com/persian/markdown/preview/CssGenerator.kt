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

        val fontStack = buildString {
            if (state.useBundledFont && bundledRegularFontBase64 != null) {
                append("'PersianMarkdownBundledVazir', ")
            }
            if (!state.fontFamily.isNullOrBlank()) {
                append("${state.fontFamily}, ")
            }
            append("Tahoma, -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif")
        }

        val codeFontStack = buildString {
            append("'JetBrains Mono', Menlo, Monaco, Consolas, 'Courier New', ")
            if (state.useBundledFont && bundledRegularFontBase64 != null) {
                append("'PersianMarkdownBundledVazir', ")
            }
            if (!state.fontFamily.isNullOrBlank()) {
                append("${state.fontFamily}, ")
            }
            append("'Vazirmatn', monospace")
        }

        val fs = state.fontSize
        val lh = state.lineHeight

        sb.append("""
            :root {
                --pm-font-size: ${fs}px;
                --pm-line-height: $lh;
                --pm-fa-font: $fontStack;
                --pm-code-font: $codeFontStack;
            }
            body {
                padding-bottom: 85px !important;
            }
            body, p, li, blockquote, table, div, span, td, th {
                font-family: $fontStack !important;
                font-family: var(--pm-fa-font, $fontStack) !important;
                font-size: ${fs}px !important;
                font-size: var(--pm-font-size, ${fs}px) !important;
                line-height: $lh !important;
                line-height: var(--pm-line-height, $lh) !important;
            }
        """.trimIndent()).append("\n")

        if (state.enhanceHeadings) {
            sb.append("""
                h1 { font-family: var(--pm-fa-font, $fontStack) !important; font-size: calc(var(--pm-font-size, ${fs}px) * 1.75) !important; line-height: 1.3 !important; }
                h2 { font-family: var(--pm-fa-font, $fontStack) !important; font-size: calc(var(--pm-font-size, ${fs}px) * 1.50) !important; line-height: 1.35 !important; }
                h3 { font-family: var(--pm-fa-font, $fontStack) !important; font-size: calc(var(--pm-font-size, ${fs}px) * 1.25) !important; line-height: 1.4 !important; }
                h4 { font-family: var(--pm-fa-font, $fontStack) !important; font-size: calc(var(--pm-font-size, ${fs}px) * 1.12) !important; line-height: 1.4 !important; }
                h5, h6 { font-family: var(--pm-fa-font, $fontStack) !important; font-size: var(--pm-font-size, ${fs}px) !important; line-height: 1.4 !important; }
            """.trimIndent()).append("\n")
        }

        when (state.directionMode) {
            DirectionMode.FORCE_RTL -> {
                sb.append("""
                    body, p, h1, h2, h3, h4, h5, h6, ul, ol, blockquote, table, tr, td, th {
                        direction: rtl !important;
                        text-align: right !important;
                    }
                    ul, ol {
                        padding-right: 1.8em !important;
                        padding-left: 0 !important;
                    }
                    blockquote {
                        border-right: 4px solid #3b82f6 !important;
                        border-left: none !important;
                        padding-right: 1em !important;
                        padding-left: 0 !important;
                    }
                """.trimIndent()).append("\n")
            }
            DirectionMode.FORCE_LTR -> {
                sb.append("""
                    body, p, h1, h2, h3, h4, h5, h6, ul, ol, blockquote, table, tr, td, th {
                        direction: ltr !important;
                        text-align: left !important;
                    }
                    ul, ol {
                        padding-left: 1.8em !important;
                        padding-right: 0 !important;
                    }
                    blockquote {
                        border-left: 4px solid #3b82f6 !important;
                        border-right: none !important;
                        padding-left: 1em !important;
                        padding-right: 0 !important;
                    }
                """.trimIndent()).append("\n")
            }
            DirectionMode.AUTO -> {
                sb.append("""
                    [dir="rtl"], .persian-dir-rtl {
                        direction: rtl !important;
                        text-align: right !important;
                    }
                    [dir="ltr"], .persian-dir-ltr {
                        direction: ltr !important;
                        text-align: left !important;
                    }
                    blockquote[dir="rtl"], blockquote.persian-dir-rtl {
                        border-right: 4px solid #3b82f6 !important;
                        border-left: none !important;
                        padding-right: 1em !important;
                        padding-left: 0 !important;
                    }
                    ul[dir="rtl"], ol[dir="rtl"], ul.persian-dir-rtl, ol.persian-dir-rtl {
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
            a {
                unicode-bidi: isolate !important;
            }
            html.pm-disabled [dir="rtl"],
            html.pm-disabled .persian-dir-rtl {
                direction: ltr !important;
                text-align: left !important;
            }
            html.pm-disabled blockquote {
                border-left: 4px solid #3b82f6 !important;
                border-right: none !important;
                padding-left: 1em !important;
                padding-right: 0 !important;
            }
            html.pm-disabled ul, html.pm-disabled ol {
                padding-left: 1.8em !important;
                padding-right: 0 !important;
            }
        """.trimIndent()).append("\n")

        // Styles for floating settings card in preview (bottom-left popup)
        sb.append("""
            #persian-markdown-switcher {
                position: fixed;
                bottom: 16px;
                left: 16px;
                z-index: 2147483647;
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif !important;
                user-select: none;
                -webkit-user-select: none;
            }
            #persian-markdown-switcher * {
                box-sizing: border-box;
            }
            #pm-trigger {
                display: inline-flex;
                align-items: center;
                gap: 7px;
                height: 30px;
                padding: 0 12px 0 10px;
                border-radius: 15px;
                background: rgba(24, 28, 38, 0.88);
                backdrop-filter: blur(16px);
                -webkit-backdrop-filter: blur(16px);
                color: #e2e8f0;
                border: 1px solid rgba(255, 255, 255, 0.16);
                box-shadow: 0 4px 14px rgba(0, 0, 0, 0.35);
                cursor: pointer;
                font-size: 11px;
                font-weight: 600;
                letter-spacing: 0.2px;
                transition: all 0.2s cubic-bezier(0.16, 1, 0.3, 1);
            }
            #pm-trigger:hover {
                background: rgba(34, 40, 54, 0.96);
                border-color: rgba(255, 255, 255, 0.3);
                transform: translateY(-1px);
                box-shadow: 0 6px 20px rgba(0, 0, 0, 0.45);
                color: #ffffff;
            }
            #pm-trigger svg.pm-icon {
                width: 13px;
                height: 13px;
                opacity: 0.9;
            }
            #pm-trigger svg.pm-gear {
                width: 12px;
                height: 12px;
                opacity: 0.75;
                transition: transform 0.3s ease;
            }
            #persian-markdown-switcher.pm-open #pm-trigger svg.pm-gear {
                transform: rotate(60deg);
                opacity: 1;
            }
            #pm-card {
                position: absolute;
                bottom: calc(100% + 10px);
                left: 0;
                width: 275px;
                padding: 16px;
                border-radius: 16px;
                background: rgba(22, 27, 39, 0.96);
                backdrop-filter: blur(24px) saturate(180%);
                -webkit-backdrop-filter: blur(24px) saturate(180%);
                border: 1px solid rgba(255, 255, 255, 0.12);
                box-shadow: 0 18px 45px rgba(0, 0, 0, 0.65), 0 2px 8px rgba(0, 0, 0, 0.3);
                opacity: 0;
                visibility: hidden;
                transform: translateY(8px) scale(0.95);
                transform-origin: bottom left;
                transition: opacity 0.2s cubic-bezier(0.16, 1, 0.3, 1),
                            transform 0.2s cubic-bezier(0.16, 1, 0.3, 1),
                            visibility 0.2s;
                pointer-events: none;
                color: #f1f5f9;
            }
            #persian-markdown-switcher.pm-open #pm-card {
                opacity: 1;
                visibility: visible;
                transform: translateY(0) scale(1);
                pointer-events: auto;
            }
            .pm-card-title {
                font-size: 14px;
                font-weight: 700;
                color: #ffffff;
                text-align: center;
                letter-spacing: 0.3px;
                padding-bottom: 12px;
                border-bottom: 1px solid rgba(255, 255, 255, 0.08);
                margin-bottom: 14px;
            }
            .pm-row {
                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-bottom: 11px;
                font-size: 12px;
                font-weight: 500;
                color: #cbd5e1;
            }
            .pm-row-label {
                display: flex;
                align-items: center;
                gap: 5px;
                color: #cbd5e1;
            }
            .pm-info-icon {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                width: 13px;
                height: 13px;
                border-radius: 50%;
                border: 1px solid #64748b;
                font-size: 9px;
                color: #94a3b8;
                cursor: help;
                user-select: none;
            }
            /* Switch */
            .pm-switch {
                position: relative;
                display: inline-block;
                width: 38px;
                height: 22px;
                flex-shrink: 0;
            }
            .pm-switch input {
                opacity: 0;
                width: 0;
                height: 0;
            }
            .pm-switch-slider {
                position: absolute;
                cursor: pointer;
                top: 0; left: 0; right: 0; bottom: 0;
                background-color: rgba(255, 255, 255, 0.16);
                transition: 0.22s cubic-bezier(0.4, 0, 0.2, 1);
                border-radius: 22px;
            }
            .pm-switch-slider:before {
                position: absolute;
                content: "";
                height: 18px;
                width: 18px;
                left: 2px;
                bottom: 2px;
                background-color: white;
                transition: 0.22s cubic-bezier(0.4, 0, 0.2, 1);
                border-radius: 50%;
                box-shadow: 0 2px 4px rgba(0,0,0,0.3);
            }
            .pm-switch input:checked + .pm-switch-slider {
                background-color: #5856d6;
            }
            .pm-switch input:checked + .pm-switch-slider:before {
                transform: translateX(16px);
            }
            /* Inputs */
            .pm-input {
                width: 126px;
                height: 25px;
                background: rgba(255, 255, 255, 0.07);
                border: 1px solid rgba(255, 255, 255, 0.12);
                border-radius: 6px;
                color: #e2e8f0;
                font-size: 11px;
                padding: 0 8px;
                outline: none;
                transition: border-color 0.15s, background 0.15s;
                font-family: inherit;
            }
            .pm-input:focus {
                border-color: #6366f1;
                background: rgba(255, 255, 255, 0.12);
            }
            .pm-input::placeholder {
                color: #94a3b8;
            }
            /* Sliders */
            .pm-slider-wrap {
                display: flex;
                align-items: center;
                gap: 7px;
                width: 126px;
            }
            .pm-slider {
                -webkit-appearance: none;
                appearance: none;
                width: 100%;
                height: 4px;
                border-radius: 2px;
                background: rgba(255, 255, 255, 0.18);
                outline: none;
                cursor: pointer;
            }
            .pm-slider::-webkit-slider-thumb {
                -webkit-appearance: none;
                appearance: none;
                width: 14px;
                height: 14px;
                border-radius: 50%;
                background: #5856d6;
                cursor: pointer;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.4);
                transition: transform 0.1s;
            }
            .pm-slider::-webkit-slider-thumb:hover {
                transform: scale(1.15);
            }
            .pm-reset-btn {
                background: none;
                border: none;
                color: #94a3b8;
                cursor: pointer;
                padding: 2px;
                font-size: 13px;
                line-height: 1;
                transition: color 0.15s;
                display: flex;
                align-items: center;
            }
            .pm-reset-btn:hover {
                color: #ffffff;
            }
            .pm-card-divider {
                border-top: 1px solid rgba(255, 255, 255, 0.08);
                margin: 12px 0 10px 0;
            }
            .pm-github-btn {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 7px;
                color: #f59e0b;
                font-size: 12px;
                font-weight: 600;
                text-decoration: none;
                padding: 6px 0 2px 0;
                cursor: pointer;
                transition: opacity 0.15s, transform 0.15s;
            }
            .pm-github-btn:hover {
                opacity: 0.88;
                transform: translateY(-0.5px);
            }
            .pm-github-icon {
                width: 15px;
                height: 15px;
                fill: currentColor;
            }
        """.trimIndent()).append("\n")

        return sb.toString()
    }

    fun generateAutoDirScript(state: PersianMarkdownState): String {
        val initialMode = state.directionMode.id
        val defaultFontSize = state.fontSize
        val defaultLineHeight = state.lineHeight
        return """
            (function() {
                var defaultFs = $defaultFontSize;
                var defaultLh = $defaultLineHeight;
                var currentMode = '$initialMode';
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
                    var links = document.querySelectorAll('a');
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

                function createSwitcherUI() {
                    if (document.getElementById('persian-markdown-switcher')) return;

                    var switcher = document.createElement('div');
                    switcher.id = 'persian-markdown-switcher';
                    switcher.innerHTML = 
                        '<div id="pm-trigger" title="Markdown RTL Settings">' +
                            '<svg class="pm-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">' +
                                '<path d="M4 6h16M4 12h10M4 18h14"/>' +
                            '</svg>' +
                            '<span id="pm-current-label">Markdown RTL</span>' +
                            '<svg class="pm-gear" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">' +
                                '<circle cx="12" cy="12" r="3"/>' +
                                '<path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 1 1-2.83 2.83l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 1 1-4 0v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 1 1-2.83-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 1 1 0-4h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 1 1 2.83-2.83l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 1 1 4 0v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 1 1 2.83 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 1 1 0 4h-.09a1.65 1.65 0 0 0-1.51 1z"/>' +
                            '</svg>' +
                        '</div>' +
                        '<div id="pm-card">' +
                            '<div class="pm-card-title">Markdown RTL</div>' +
                            '<div class="pm-row">' +
                                '<div class="pm-row-label">' +
                                    '<span>Enabled</span>' +
                                    '<span class="pm-info-icon" title="Enable or disable RTL enhancement">ⓘ</span>' +
                                '</div>' +
                                '<label class="pm-switch">' +
                                    '<input type="checkbox" id="pm-opt-enabled" checked>' +
                                    '<span class="pm-switch-slider"></span>' +
                                '</label>' +
                            '</div>' +
                            '<div class="pm-row">' +
                                '<div class="pm-row-label">' +
                                    '<span>Force RTL</span>' +
                                    '<span class="pm-info-icon" title="Force all paragraphs and blocks to RTL">ⓘ</span>' +
                                '</div>' +
                                '<label class="pm-switch">' +
                                    '<input type="checkbox" id="pm-opt-force-rtl">' +
                                    '<span class="pm-switch-slider"></span>' +
                                '</label>' +
                            '</div>' +
                            '<div class="pm-row">' +
                                '<span class="pm-row-label">FA/AR Font</span>' +
                                '<input type="text" id="pm-input-fa-font" class="pm-input" placeholder="Default: Vazirmatn">' +
                            '</div>' +
                            '<div class="pm-row">' +
                                '<span class="pm-row-label">EN Font</span>' +
                                '<input type="text" id="pm-input-en-font" class="pm-input" placeholder="Default: System">' +
                            '</div>' +
                            '<div class="pm-row">' +
                                '<span class="pm-row-label">Code Font</span>' +
                                '<input type="text" id="pm-input-code-font" class="pm-input" placeholder="Default: System">' +
                            '</div>' +
                            '<div class="pm-row">' +
                                '<span class="pm-row-label">Line Height</span>' +
                                '<div class="pm-slider-wrap">' +
                                    '<input type="range" id="pm-slider-line-height" class="pm-slider" min="1.2" max="2.6" step="0.05" value="' + defaultLh + '">' +
                                    '<button id="pm-reset-line-height" class="pm-reset-btn" title="Reset Line Height">↺</button>' +
                                '</div>' +
                            '</div>' +
                            '<div class="pm-row">' +
                                '<span class="pm-row-label">Font Size</span>' +
                                '<div class="pm-slider-wrap">' +
                                    '<input type="range" id="pm-slider-font-size" class="pm-slider" min="12" max="26" step="1" value="' + defaultFs + '">' +
                                    '<button id="pm-reset-font-size" class="pm-reset-btn" title="Reset Font Size">↺</button>' +
                                '</div>' +
                            '</div>' +
                            '<div class="pm-card-divider"></div>' +
                            '<a href="https://github.com/mahdiasd/MarkdownRTL" target="_blank" class="pm-github-btn" title="Star Markdown RTL on GitHub">' +
                                '<svg class="pm-github-icon" viewBox="0 0 24 24">' +
                                    '<path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0024 12c0-6.63-5.37-12-12-12z"/>' +
                                '</svg>' +
                                '<span>Star on GitHub</span>' +
                            '</a>' +
                        '</div>';

                    var trigger = switcher.querySelector('#pm-trigger');
                    var card = switcher.querySelector('#pm-card');

                    trigger.addEventListener('click', function(e) {
                        e.stopPropagation();
                        switcher.classList.toggle('pm-open');
                    });

                    card.addEventListener('click', function(e) {
                        e.stopPropagation();
                    });

                    document.addEventListener('click', function(e) {
                        if (!switcher.contains(e.target)) {
                            switcher.classList.remove('pm-open');
                        }
                    });

                    // Controls
                    var optEnabled = card.querySelector('#pm-opt-enabled');
                    var optForceRtl = card.querySelector('#pm-opt-force-rtl');
                    var inputFaFont = card.querySelector('#pm-input-fa-font');
                    var inputEnFont = card.querySelector('#pm-input-en-font');
                    var inputCodeFont = card.querySelector('#pm-input-code-font');
                    var sliderFs = card.querySelector('#pm-slider-font-size');
                    var resetFs = card.querySelector('#pm-reset-font-size');
                    var sliderLh = card.querySelector('#pm-slider-line-height');
                    var resetLh = card.querySelector('#pm-reset-line-height');

                    // Enabled toggle
                    optEnabled.addEventListener('change', function() {
                        var isEnabled = optEnabled.checked;
                        savePref('enabled', isEnabled ? '1' : '0');
                        if (isEnabled) {
                            document.documentElement.classList.remove('pm-disabled');
                            applyDirections();
                        } else {
                            document.documentElement.classList.add('pm-disabled');
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

                    // Font Size
                    sliderFs.addEventListener('input', function() {
                        var val = sliderFs.value;
                        document.documentElement.style.setProperty('--pm-font-size', val + 'px');
                        savePref('font_size', val);
                    });
                    resetFs.addEventListener('click', function(e) {
                        e.stopPropagation();
                        sliderFs.value = defaultFs;
                        document.documentElement.style.setProperty('--pm-font-size', defaultFs + 'px');
                        savePref('font_size', defaultFs);
                    });

                    // Line Height
                    sliderLh.addEventListener('input', function() {
                        var val = sliderLh.value;
                        document.documentElement.style.setProperty('--pm-line-height', val);
                        savePref('line_height', val);
                    });
                    resetLh.addEventListener('click', function(e) {
                        e.stopPropagation();
                        sliderLh.value = defaultLh;
                        document.documentElement.style.setProperty('--pm-line-height', defaultLh);
                        savePref('line_height', defaultLh);
                    });

                    // Fonts
                    function updateFaFont() {
                        var val = inputFaFont.value.trim();
                        savePref('fa_font', val);
                        if (val) {
                            document.documentElement.style.setProperty('--pm-fa-font', "'" + val + "', 'PersianMarkdownBundledVazir', Tahoma, sans-serif");
                        } else {
                            document.documentElement.style.removeProperty('--pm-fa-font');
                        }
                    }
                    inputFaFont.addEventListener('change', updateFaFont);
                    inputFaFont.addEventListener('blur', updateFaFont);

                    function updateCodeFont() {
                        var val = inputCodeFont.value.trim();
                        savePref('code_font', val);
                        if (val) {
                            document.documentElement.style.setProperty('--pm-code-font', "'" + val + "', 'JetBrains Mono', Menlo, monospace");
                        } else {
                            document.documentElement.style.removeProperty('--pm-code-font');
                        }
                    }
                    inputCodeFont.addEventListener('change', updateCodeFont);
                    inputCodeFont.addEventListener('blur', updateCodeFont);

                    // Load saved preferences
                    var savedEnabled = getPref('enabled', '1');
                    optEnabled.checked = savedEnabled === '1';
                    if (!optEnabled.checked) {
                        document.documentElement.classList.add('pm-disabled');
                    }

                    var savedForceRtl = getPref('force_rtl', currentMode === 'force_rtl' ? '1' : '0');
                    optForceRtl.checked = savedForceRtl === '1';
                    currentMode = optForceRtl.checked ? 'force_rtl' : 'auto';

                    var savedFs = getPref('font_size', '');
                    if (savedFs) {
                        sliderFs.value = savedFs;
                        document.documentElement.style.setProperty('--pm-font-size', savedFs + 'px');
                    }
                    var savedLh = getPref('line_height', '');
                    if (savedLh) {
                        sliderLh.value = savedLh;
                        document.documentElement.style.setProperty('--pm-line-height', savedLh);
                    }

                    var savedFaFont = getPref('fa_font', '');
                    if (savedFaFont) {
                        inputFaFont.value = savedFaFont;
                        updateFaFont();
                    }
                    var savedEnFont = getPref('en_font', '');
                    if (savedEnFont) {
                        inputEnFont.value = savedEnFont;
                    }
                    var savedCodeFont = getPref('code_font', '');
                    if (savedCodeFont) {
                        inputCodeFont.value = savedCodeFont;
                        updateCodeFont();
                    }

                    document.body.appendChild(switcher);
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
                    observer.observe(document.body || document.documentElement, { childList: true, subtree: true });
                }
            })();
        """.trimIndent()
    }
}

