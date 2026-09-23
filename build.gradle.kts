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
            <h3>What's New in Version 1.0.0</h3>
            <ul>
                <li><b>Official 1.0.0 Release:</b> Comprehensive RTL and Persian/Arabic support for JetBrains IDEs and Android Studio.</li>
                <li><b>Interactive Floating Controller:</b> Added a sleek bottom-left floating widget with quick toggles (Enabled, Force RTL) and searchable font comboboxes.</li>
                <li><b>Live Typography Tuning:</b> Real-time font size and line height stepper controls with instant preview synchronization.</li>
                <li><b>Embedded Fonts:</b> Bundled premium <i>Vazirmatn</i> (Regular/Bold) and <i>JetBrains Mono</i> for beautiful cross-platform rendering.</li>
                <li><b>Code Isolation:</b> Bulletproof BiDi isolation preventing syntax distortion in code blocks and inline snippets.</li>
                <li><b>GitHub Integration:</b> One-click direct link to the GitHub repository from the preview widget.</li>
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
            recommended()
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
