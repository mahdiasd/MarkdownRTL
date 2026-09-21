package com.persian.markdown.settings

enum class DirectionMode(val id: String, val displayName: String) {
    AUTO("auto", "Auto RTL (Detect per element)"),
    FORCE_RTL("force_rtl", "Force RTL (All text right-to-left)"),
    FORCE_LTR("force_ltr", "Force LTR (All text left-to-right)");

    override fun toString(): String = displayName

    companion object {
        fun fromId(id: String?): DirectionMode =
            entries.firstOrNull { it.id.equals(id, ignoreCase = true) } ?: AUTO
    }
}
