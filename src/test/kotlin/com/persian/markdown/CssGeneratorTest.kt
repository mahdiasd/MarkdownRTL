package com.persian.markdown

import com.persian.markdown.preview.CssGenerator
import com.persian.markdown.settings.DirectionMode
import com.persian.markdown.settings.PersianMarkdownState
import org.junit.Assert.assertTrue
import org.junit.Test

class CssGeneratorTest {

    @Test
    fun testForceRtlGeneratesRtlRules() {
        val state = PersianMarkdownState().apply {
            directionMode = DirectionMode.FORCE_RTL
            fontSize = 18
            lineHeight = 2.0f
        }
        val css = CssGenerator.generateCss(state)
        assertTrue(css.contains("direction: rtl !important"))
        assertTrue(css.contains("font-size: 18px !important"))
        assertTrue(css.contains("pre, .code-fence"))
        assertTrue(css.contains("code, kbd, samp"))
        assertTrue(css.contains("direction: ltr !important"))
    }

    @Test
    fun testForceLtrGeneratesLtrRules() {
        val state = PersianMarkdownState().apply {
            directionMode = DirectionMode.FORCE_LTR
        }
        val css = CssGenerator.generateCss(state)
        assertTrue(css.contains("direction: ltr !important"))
    }

    @Test
    fun testAutoRtlGeneratesPlaintextAndDirSelectors() {
        val state = PersianMarkdownState().apply {
            directionMode = DirectionMode.AUTO
        }
        val css = CssGenerator.generateCss(state)
        assertTrue(css.contains("""[dir="rtl"]"""))
        assertTrue(css.contains("persian-markdown-switcher"))
    }

    @Test
    fun testBundledFontLoaded() {
        val state = PersianMarkdownState().apply {
            useBundledFont = true
        }
        val css = CssGenerator.generateCss(state)
        assertTrue(css.contains("@font-face"))
        assertTrue(css.contains("PersianMarkdownBundledVazir"))
    }

    @Test
    fun testAutoDirScript() {
        val state = PersianMarkdownState()
        val js = CssGenerator.generateAutoDirScript(state)
        assertTrue(js.contains("persianRegex"))
        assertTrue(js.contains("persian-markdown-switcher"))
        assertTrue(js.contains("MutationObserver"))
    }
}
