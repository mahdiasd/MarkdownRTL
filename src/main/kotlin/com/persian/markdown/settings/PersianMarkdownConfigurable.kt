package com.persian.markdown.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.*

class PersianMarkdownConfigurable : BoundConfigurable("Persian Markdown") {

    private val settings = PersianMarkdownSettings.getInstance()
    private val state = settings.state

    override fun createPanel(): DialogPanel {
        return panel {
            group("Text Direction (RTL / LTR)") {
                buttonsGroup {
                    row {
                        radioButton("Auto RTL (Detect direction per paragraph/block)", DirectionMode.AUTO)
                            .comment("Automatically aligns Persian/Arabic text to right, English to left using BiDi heuristics.")
                    }
                    row {
                        radioButton("Force RTL (Right-to-Left)", DirectionMode.FORCE_RTL)
                            .comment("Forces all text in the preview to right-to-left.")
                    }
                    row {
                        radioButton("Force LTR (Left-to-Right)", DirectionMode.FORCE_LTR)
                            .comment("Standard left-to-right alignment.")
                    }
                }.bind(state::directionMode)

                row {
                    checkBox("Preserve LTR for code blocks (pre, code)")
                        .bindSelected(state::keepCodeLTR)
                        .comment("Keeps code blocks and monospace snippets left-to-right.")
                }
            }

            group("Typography & Fonts") {
                row {
                    checkBox("Use bundled high-quality Vazirmatn font")
                        .bindSelected(state::useBundledFont)
                        .comment("Includes embedded Vazirmatn (Regular & Bold) so no system font installation is required.")
                }

                row("Font Family:") {
                    textField()
                        .columns(COLUMNS_LARGE)
                        .bindText(
                            getter = { state.fontFamily ?: "" },
                            setter = { state.fontFamily = it }
                        )
                        .comment("Fallback font stack (e.g. Vazirmatn, Shabnam, Tahoma, sans-serif)")
                }

                row("Font Size (px):") {
                    spinner(10..40, step = 1)
                        .bindIntValue(state::fontSize)
                }

                row("Line Height:") {
                    spinner(1.0..3.0, step = 0.1)
                        .bindValue(
                            getter = { state.lineHeight.toDouble() },
                            setter = { state.lineHeight = it.toFloat() }
                        )
                        .comment("Recommended line height for Persian readability is 1.7 - 2.0.")
                }
            }

            group("Element Enhancements") {
                row {
                    checkBox("Scale and align headings (H1-H6) proportionally")
                        .bindSelected(state::enhanceHeadings)
                }
                row {
                    checkBox("Right-align quotes and lists in RTL")
                        .bindSelected(state::enhanceQuotes)
                }
            }

            group("Preview Engine Compatibility") {
                row {
                    text("Persian & RTL rendering requires the <b>Chromium browser (JCEF)</b> engine in Markdown preview.")
                }
                row {
                    button("Ensure Chromium Preview Engine is Active") {
                        val projects = com.intellij.openapi.project.ProjectManager.getInstance().openProjects
                        projects.forEach { project ->
                            com.persian.markdown.startup.PersianMarkdownStartupActivity.ensureJcefPreview(project)
                        }
                        com.intellij.openapi.ui.Messages.showInfoMessage(
                            "Markdown preview engine has been set to Chromium browser.",
                            "Persian Markdown"
                        )
                    }
                }
            }
        }
    }

    override fun apply() {
        super.apply()
        com.intellij.openapi.project.ProjectManager.getInstance().openProjects.forEach { project ->
            com.persian.markdown.startup.PersianMarkdownStartupActivity.ensureJcefPreview(project)
        }
        settings.notifyChanged()
    }
}
