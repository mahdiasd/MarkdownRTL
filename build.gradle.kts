import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.kotlin.jvm") version "2.0.21"
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = property("pluginGroup") as String
version = property("pluginVersion") as String

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3")
        bundledPlugin("org.intellij.plugins.markdown")
        pluginVerifier()
        testFramework(TestFrameworkType.Platform)
    }
    testImplementation("junit:junit:4.13.2")
}

intellijPlatform {
    pluginConfiguration {
        id = "com.persian.markdown"
        name = "Markdown RTL"
        version = providers.gradleProperty("pluginVersion")
        description = """
            <h3>Seamless Right-to-Left (RTL) &amp; Persian/Arabic Experience for Markdown</h3>
            <p>
                <b>Markdown RTL</b> transforms the built-in Markdown preview in <b>IntelliJ IDEA</b> and <b>Android Studio</b> into a first-class Right-to-Left writing and reading environment. Engineered specifically for Persian, Arabic, and bilingual documentation, it delivers intelligent bidirectional rendering without disrupting your code blocks.
            </p>
            <h4>Key Capabilities:</h4>
            <ul>
                <li><b>Smart BiDi Heuristics (Auto Mode):</b> Automatically detects paragraph language and applies RTL alignment to Persian and Arabic prose while keeping pure English text LTR.</li>
                <li><b>Strict Code Fence Isolation:</b> Code blocks (<code>pre</code>, <code>code</code>), inline backticks, and technical tokens remain strictly Left-to-Right with isolated bidirectional formatting.</li>
                <li><b>Bundled Vazirmatn &amp; JetBrains Mono:</b> Out-of-the-box typography featuring embedded high-legibility fonts (Regular &amp; Bold) without requiring any operating system installations.</li>
                <li><b>Interactive Floating Bento Widget:</b> Sleek, modern floating overlay in the preview window for live adjustments to font family, font size, and line height with zero lag.</li>
                <li><b>Full Direction Control:</b> Switch effortlessly between <i>Auto BiDi</i>, <i>Force RTL</i>, and <i>Force LTR</i> with global hotkeys (<code>⌥E</code> / <code>⌥R</code> or <code>⇧⌥R</code>).</li>
                <li><b>Enhanced Elements:</b> Right-aligned bulleted and numbered lists, RTL blockquotes with themed accent borders, and proportional heading scales.</li>
            </ul>
            <p>
                Source repository &amp; issue tracker:<br/>
                <a href="https://github.com/mahdiasd/MarkdownRTL">https://github.com/mahdiasd/MarkdownRTL</a>
            </p>
        """.trimIndent()
        changeNotes = """
            <h3>What's New in Version 1.1.1</h3>
            <ul>
                <li><b>Enhanced Android Studio &amp; Multi-IDE Compatibility:</b> Fixed JCEF preview provider resolution for Android Studio and modern JetBrains IDEs (2024.2+), safely resolving both modern (<code>com.intellij.markdown.jcef.preview.JCEFHtmlPanelProvider</code>) and legacy provider classes without configuration corruption.</li>
                <li><b>Universal Offline Mermaid Diagram Rendering:</b> Fixed Mermaid rendering in Android Studio by guaranteeing offline bundled Mermaid scripts load seamlessly, rendering all diagram code blocks even in environments where native Mermaid modules are absent or inactive.</li>
                <li><b>Graceful JCEF Environment Detection:</b> Safeguarded startup activity and action handlers to respect environment capabilities without forced invalid settings.</li>
            </ul>
            <h3>What's New in Version 1.1.0</h3>
            <ul>
                <li><b>Native Mermaid Diagram Support:</b> Renders <code>```mermaid</code> fenced code blocks directly into crisp, interactive SVG diagrams completely offline without external plugins.</li>
                <li><b>Smart Diagram Language Detection &amp; Typography:</b> Diagrams with pure English text default to <i>JetBrains Mono</i>, while diagrams containing Persian/Arabic automatically use <i>Vazirmatn</i> with RTL-aligned text labels.</li>
                <li><b>Modern Interactive Card UI:</b> Features a sleek toolbar to toggle between Diagram and Source views, copy SVG image, and copy source code.</li>
                <li><b>Fullscreen Modal with Pan &amp; Zoom:</b> View complex architectural diagrams in a fullscreen overlay with mouse wheel zoom and drag-to-pan navigation.</li>
                <li><b>Live Editing Resilience:</b> Gracefully handles in-progress syntax errors during typing by preserving the last valid diagram render.</li>
                <li><b>Bento Widget Quick Toggle:</b> Easily enable or disable Mermaid diagram rendering on the fly via the floating bottom-left Bento controller.</li>
            </ul>
            <h3>What's New in Version 1.0.2</h3>
            <ul>
                <li><b>Smart YAML Front Matter Metadata Card (#1):</b> Automatically detects and renders document metadata blocks (<code>--- ... ---</code>) as a styled interactive card with bidirectional (BiDi) language detection (RTL for Persian/Arabic, LTR for English/URLs).</li>
                <li><b>Structured Array Badges &amp; Direct Links:</b> Formats tag and list items as visual badges and converts URLs into clickable web links.</li>
                <li><b>Interactive Card Controls:</b> Toggle between formatted Table View and Raw YAML, plus single-click copying to clipboard.</li>
                <li><b>Robust Fallback Parser:</b> Preserves freeform front matter text and gracefully handles CommonMark Setext H2 and HR edge cases without data loss.</li>
            </ul>
            <h3>What's New in Version 1.0.1</h3>
            <ul>
                <li><b>Seamless Viewport Positioning:</b> Fixed Bento widget positioning in IntelliJ IDEA and Android Studio so it stays smoothly docked at the bottom-left during page scrolling.</li>
                <li><b>Comfortable Bento Dimensions:</b> Enlarged trigger button, popover card, font pickers, and metric steppers for enhanced legibility and touch/click ergonomics.</li>
                <li><b>Settings Simplification:</b> Cleaned up IDE settings to make the in-preview Bento controller the single responsive source of truth for direction and typography.</li>
                <li><b>Branding Unification:</b> Unified plugin display name to <b>Markdown RTL</b> across all settings and dialogs.</li>
            </ul>
            <h3>Version 1.0.0</h3>
            <ul>
                <li><b>Official Release:</b> Comprehensive RTL and Persian/Arabic support for JetBrains IDEs and Android Studio.</li>
                <li><b>Interactive Floating Controller:</b> Added sleek bottom-left floating widget with quick toggles and searchable font comboboxes.</li>
                <li><b>Live Typography Tuning:</b> Real-time font size and line height stepper controls with instant preview synchronization.</li>
                <li><b>Embedded Fonts:</b> Bundled premium <i>Vazirmatn</i> and <i>JetBrains Mono</i>.</li>
                <li><b>Code Isolation:</b> Strict BiDi isolation preventing syntax distortion in code blocks and inline snippets.</li>
            </ul>
        """.trimIndent()
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = provider { null }
        }
        vendor {
            name = "Mahdi Asadollahpour"
            url = "https://github.com/mahdiasd/MarkdownRTL"
        }
    }
    pluginVerification {
        ides {
            ide(org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaCommunity, "2024.3")
            ide(org.jetbrains.intellij.platform.gradle.IntelliJPlatformType.IntellijIdeaCommunity, "2025.1")
        }
    }
}

tasks {
    withType<JavaCompile> {
        sourceCompatibility = "21"
        targetCompatibility = "21"
    }
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
}
