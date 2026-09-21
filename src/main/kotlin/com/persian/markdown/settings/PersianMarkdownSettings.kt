package com.persian.markdown.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.SimplePersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.messages.Topic

class PersianMarkdownState : BaseState() {
    var directionMode by enum(DirectionMode.AUTO)
    var fontFamily by string("Vazirmatn, -apple-system, BlinkMacSystemFont, 'Segoe UI', Tahoma, sans-serif")
    var fontSize by property(16)
    var lineHeight by property(1.8f)
    var useBundledFont by property(true)
    var keepCodeLTR by property(true)
    var enhanceHeadings by property(true)
    var enhanceQuotes by property(true)
    var enhanceTables by property(true)
}

fun interface PersianMarkdownSettingsListener {
    fun settingsChanged(newSettings: PersianMarkdownState)

    companion object {
        val TOPIC: Topic<PersianMarkdownSettingsListener> =
            Topic.create("PersianMarkdownSettingsListener", PersianMarkdownSettingsListener::class.java)
    }
}

@Service(Service.Level.APP)
@State(
    name = "PersianMarkdownSettings",
    storages = [Storage("persianMarkdownSettings.xml")]
)
class PersianMarkdownSettings : SimplePersistentStateComponent<PersianMarkdownState>(PersianMarkdownState()) {

    fun notifyChanged() {
        ApplicationManager.getApplication().messageBus
            .syncPublisher(PersianMarkdownSettingsListener.TOPIC)
            .settingsChanged(state)
    }

    companion object {
        fun getInstance(): PersianMarkdownSettings =
            ApplicationManager.getApplication().getService(PersianMarkdownSettings::class.java)
    }
}
