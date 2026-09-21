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
            Enhances Markdown preview with full Right-to-Left (RTL) and Persian/Arabic support in IntelliJ IDEA and Android Studio.
            Features include Auto BiDi detection, Force RTL mode, custom typography (bundled Vazirmatn font),
            floating settings card with live font size and line height sliders, and code fence isolation.
            Source repository: https://github.com/mahdiasd/MarkdownRTL
        """.trimIndent()
        changeNotes = "Initial 1.0.0 release of Markdown RTL with floating interactive settings card, custom font controls, and GitHub integration."
        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = provider { null }
        }
        vendor {
            name = "Mahdi"
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
