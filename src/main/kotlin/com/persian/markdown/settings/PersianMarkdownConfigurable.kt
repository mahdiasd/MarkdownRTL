package com.persian.markdown.settings

import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.openapi.ui.Messages
import com.intellij.ui.dsl.builder.*

class PersianMarkdownConfigurable : BoundConfigurable("Markdown RTL") {

    private val settings by lazy { PersianMarkdownSettings.getInstance() }
    private val state get() = settings.state

    override fun createPanel(): DialogPanel {
        return panel {
            group("Interactive In-Preview Controller") {
                row {
                    text("Direction modes (<b>Auto</b>, <b>Force RTL</b>, <b>Force LTR</b>), master enable toggle, fonts, font size, and line height are controlled interactively directly inside the Markdown preview using the sleek floating widget at the bottom-left corner.")
                }
                row {
                    text("Keyboard shortcuts available in Markdown preview:<br/>" +
                            "• <b>⌥E</b> (Alt+E): Toggle Enable / Disable<br/>" +
                            "• <b>⌥R</b> (Alt+R): Cycle between Auto, Force RTL, and Force LTR")
                }
            }

            group("Typography") {
                row {
                    checkBox("Use bundled high-quality Vazirmatn font")
                        .bindSelected(state::useBundledFont)
                        .comment("Includes embedded Vazirmatn (Regular & Bold) so no system font installation is required.")
                }
            }

            group("Preview Engine Compatibility") {
                row {
                    text("Markdown RTL requires the <b>Chromium browser (JCEF)</b> engine for bidirectional rendering and interactive widget controls.")
                }
                row {
                    button("Ensure Chromium Preview Engine is Active") {
                        val projects = com.intellij.openapi.project.ProjectManager.getInstance().openProjects
                        projects.filter { !it.isDisposed }.forEach { project ->
                            com.persian.markdown.startup.PersianMarkdownStartupActivity.ensureJcefPreview(project)
                        }
                        Messages.showInfoMessage(
                            "Markdown preview engine has been set to Chromium browser.",
                            "Markdown RTL"
                        )
                    }
                }
            }
        }
    }

    override fun apply() {
        super.apply()
        com.intellij.openapi.project.ProjectManager.getInstance().openProjects
            .filter { !it.isDisposed }
            .forEach { project ->
                com.persian.markdown.startup.PersianMarkdownStartupActivity.ensureJcefPreview(project)
            }
        settings.notifyChanged()
    }
}
