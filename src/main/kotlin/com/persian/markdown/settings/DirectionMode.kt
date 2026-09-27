package com.persian.markdown.settings

enum class DirectionMode(val id: String, val displayName: String) {
    AUTO("auto", "Auto RTL (Detect per element)"),
    FORCE_RTL("force_rtl", "Force RTL (All text right-to-left)"),
    FORCE_LTR("force_ltr", "Force LTR (All text left-to-right)");

    companion object {
        fun fromId(value: String?): DirectionMode {
            if (value.isNullOrBlank()) return AUTO
            return entries.firstOrNull {
                it.id.equals(value, ignoreCase = true) ||
                it.name.equals(value, ignoreCase = true) ||
                it.displayName.equals(value, ignoreCase = true) ||
                (value.contains("Force RTL", ignoreCase = true) && it == FORCE_RTL) ||
                (value.contains("Force LTR", ignoreCase = true) && it == FORCE_LTR) ||
                (value.contains("Auto", ignoreCase = true) && it == AUTO)
            } ?: AUTO
        }
    }
}
