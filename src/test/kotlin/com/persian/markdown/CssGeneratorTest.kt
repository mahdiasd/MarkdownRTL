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

    @Test
    fun testBundledJetBrainsMonoLoaded() {
        val state = PersianMarkdownState()
        val css = CssGenerator.generateCss(state)
        assertTrue(css.contains("PersianMarkdownBundledJBMono"))
        assertTrue(css.contains("--pm-en-font"))
        assertTrue(css.contains("--pm-code-font"))
    }

    @Test
    fun testSystemFontsInjectedIntoScript() {
        val state = PersianMarkdownState()
        val customSystemFonts = arrayOf("Sahel", "Shabnam", "B Nazanin")
        val js = CssGenerator.generateAutoDirScript(state, customSystemFonts)
        assertTrue(js.contains("\"Sahel\""))
        assertTrue(js.contains("\"Shabnam\""))
        assertTrue(js.contains("\"B Nazanin\""))
        assertTrue(js.contains("setupFontCombobox"))
        assertTrue(js.contains("pm-input-fa"))
        assertTrue(js.contains("pm-input-en"))
        assertTrue(js.contains("pm-input-code"))
    }

    @Test
    fun testStitchDesignStructureAndNoSettingsInToolbar() {
        val state = PersianMarkdownState()
        val js = CssGenerator.generateAutoDirScript(state)
        // Check core components
        assertTrue(js.contains("pm-trigger"))
        assertTrue(js.contains("pm-status-dot"))
        assertTrue(js.contains("Star on github"))
        assertTrue(js.contains("pm-close-btn"))
        assertTrue(js.contains("pm-opt-enabled"))
        assertTrue(js.contains("pm-opt-force-rtl"))
        assertTrue(js.contains("pm-lh-val"))
        assertTrue(js.contains("pm-fs-val"))
        assertTrue(js.contains("pm-reset-btn"))
        // Apply button removed as changes are instant
        org.junit.Assert.assertFalse(js.contains("pm-apply-btn"))
        // Check that settings gear button is NOT in header/toolbar
        org.junit.Assert.assertFalse(js.contains("aria-label=\"Settings\""))
    }

    @Test
    fun testCssIsolationFromSwitcher() {
        val state = PersianMarkdownState()
        val css = CssGenerator.generateCss(state)
        assertTrue(css.contains(":not(#persian-markdown-switcher"))
        assertTrue(css.contains("#persian-markdown-switcher *"))
    }

    @Test
    fun testDirectionModeFromId() {
        org.junit.Assert.assertEquals(DirectionMode.AUTO, DirectionMode.fromId("auto"))
        org.junit.Assert.assertEquals(DirectionMode.FORCE_RTL, DirectionMode.fromId("force_rtl"))
        org.junit.Assert.assertEquals(DirectionMode.FORCE_LTR, DirectionMode.fromId("force_ltr"))
        org.junit.Assert.assertEquals(DirectionMode.AUTO, DirectionMode.fromId("invalid"))
        org.junit.Assert.assertEquals(DirectionMode.AUTO, DirectionMode.fromId(null))
    }

    @Test
    fun testCleanFontNameNotDuplicatedInScript() {
        val state = PersianMarkdownState()
        val js = CssGenerator.generateAutoDirScript(state)
        val matches = Regex("""function cleanFontName""").findAll(js).count()
        org.junit.Assert.assertEquals("cleanFontName should be defined exactly once", 1, matches)
    }

    @Test
    fun testMutationObserverDebounced() {
        val state = PersianMarkdownState()
        val js = CssGenerator.generateAutoDirScript(state)
        assertTrue(js.contains("debounceTimer"))
        assertTrue(js.contains("clearTimeout(debounceTimer)"))
        assertTrue(js.contains("setTimeout"))
    }

    @Test
    fun testDumpScriptToFile() {
        val state = PersianMarkdownState()
        val fonts = try {
            java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().availableFontFamilyNames
        } catch (_: Exception) {
            emptyArray<String>()
        }
        val js = CssGenerator.generateAutoDirScript(state, fonts)
        val file = java.io.File("build/test_persian.js")
        file.parentFile?.mkdirs()
        file.writeText(js)
    }
}
