package com.persian.markdown.actions

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.persian.markdown.settings.DirectionMode
import com.persian.markdown.settings.PersianMarkdownSettings

class ToggleDirectionAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        if (project != null) {
            com.persian.markdown.startup.PersianMarkdownStartupActivity.ensureJcefPreview(project)
        }
        val settings = PersianMarkdownSettings.getInstance()
        val current = settings.state.directionMode
        val next = when (current) {
            DirectionMode.AUTO -> DirectionMode.FORCE_RTL
            DirectionMode.FORCE_RTL -> DirectionMode.FORCE_LTR
            DirectionMode.FORCE_LTR -> DirectionMode.AUTO
        }
        settings.state.directionMode = next
        settings.notifyChanged()
    }

    override fun update(e: AnActionEvent) {
        val settings = PersianMarkdownSettings.getInstance()
        val current = settings.state.directionMode
        e.presentation.text = "RTL Direction: ${current.displayName}"
        e.presentation.description = "Switch Markdown preview direction: currently ${current.displayName}"
        e.presentation.icon = AllIcons.Actions.SplitVertically
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
