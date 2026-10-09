package com.persian.markdown

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.persian.markdown.actions.ToggleDirectionAction
import com.persian.markdown.preview.PersianMarkdownBrowserExtension
import com.persian.markdown.settings.DirectionMode

class SettingsAndActionTest : BasePlatformTestCase() {

    fun testFontSanitizationStripsDangerousCharacters() {
        val dangerous = "Vazirmatn'; alert(1); /*"
        val sanitized = PersianMarkdownBrowserExtension.sanitizeFontName(dangerous)
        assertEquals("Vazirmatn alert(1) /*", sanitized)
        assertFalse(sanitized.contains(";"))
        assertFalse(sanitized.contains("'"))

        val xssFont = "<script>alert(1)</script>"
        val sanitizedXss = PersianMarkdownBrowserExtension.sanitizeFontName(xssFont)
        assertEquals("scriptalert(1)/script", sanitizedXss)
        assertFalse(sanitizedXss.contains("<"))
        assertFalse(sanitizedXss.contains(">"))
    }

    fun testFontSanitizationLengthLimit() {
        val veryLong = "A".repeat(300)
        val sanitized = PersianMarkdownBrowserExtension.sanitizeFontName(veryLong)
        assertEquals(120, sanitized.length)
    }

    fun testDirectionModeCycle() {
        assertEquals(DirectionMode.FORCE_RTL, getNextMode(DirectionMode.AUTO))
        assertEquals(DirectionMode.FORCE_LTR, getNextMode(DirectionMode.FORCE_RTL))
        assertEquals(DirectionMode.AUTO, getNextMode(DirectionMode.FORCE_LTR))
    }

    private fun getNextMode(current: DirectionMode): DirectionMode {
        return when (current) {
            DirectionMode.AUTO -> DirectionMode.FORCE_RTL
            DirectionMode.FORCE_RTL -> DirectionMode.FORCE_LTR
            DirectionMode.FORCE_LTR -> DirectionMode.AUTO
        }
    }

    fun testActionVisibleOnlyForMarkdownFiles() {
        val action = ToggleDirectionAction()
        val presentation = Presentation()

        // 1. Markdown file (.md) -> Should be visible and enabled
        val mdFile = LightVirtualFile("test.md", "Content")
        val mdContext = SimpleDataContext.builder()
            .add(CommonDataKeys.VIRTUAL_FILE, mdFile)
            .build()
        val mdEvent = AnActionEvent.createFromDataContext(ActionPlaces.EDITOR_POPUP, presentation, mdContext)
        action.update(mdEvent)
        assertTrue(presentation.isEnabledAndVisible)

        // 2. Kotlin file (.kt) -> Should NOT be visible or enabled
        val ktFile = LightVirtualFile("Test.kt", "class Test")
        val ktContext = SimpleDataContext.builder()
            .add(CommonDataKeys.VIRTUAL_FILE, ktFile)
            .build()
        val ktEvent = AnActionEvent.createFromDataContext(ActionPlaces.EDITOR_POPUP, presentation, ktContext)
        action.update(ktEvent)
        assertFalse(presentation.isEnabledAndVisible)

        // 3. No file context (null) -> Should NOT be visible
        val emptyContext = SimpleDataContext.builder().build()
        val emptyEvent = AnActionEvent.createFromDataContext(ActionPlaces.EDITOR_POPUP, presentation, emptyContext)
        action.update(emptyEvent)
        assertFalse(presentation.isEnabledAndVisible)
    }
}
