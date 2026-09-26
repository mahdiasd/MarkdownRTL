# Markdown RTL for IntelliJ IDEA & Android Studio

<p align="center">
  <img src="docs/interactive-widget-ui.svg" alt="Markdown RTL Banner" width="100%"/>
</p>

<p align="center">
  <b>Intelligent Right-to-Left (RTL) Balancing, Smart BiDi Detection, and Persian/Arabic Typography for Markdown Previews in IntelliJ IDEA & Android Studio</b>
</p>

<p align="center">
  <a href="https://plugins.jetbrains.com/plugin/34457-markdown-rtl"><img src="https://img.shields.io/jetbrains/plugin/v/34457-markdown-rtl?style=for-the-badge&logo=jetbrains&label=Marketplace&color=007ACC" alt="JetBrains Marketplace"/></a>
  <a href="https://plugins.jetbrains.com/plugin/34457-markdown-rtl"><img src="https://img.shields.io/jetbrains/plugin/d/34457-markdown-rtl?style=for-the-badge&logo=jetbrains&label=Downloads&color=success" alt="Marketplace Downloads"/></a>
  <a href="https://plugins.jetbrains.com/plugin/34457-markdown-rtl"><img src="https://img.shields.io/jetbrains/plugin/r/rating/34457-markdown-rtl?style=for-the-badge&logo=jetbrains&label=Rating&color=ffb400" alt="Rating"/></a>
  <a href="https://github.com/mahdiasd/MarkdownRTL"><img src="https://img.shields.io/github/stars/mahdiasd/MarkdownRTL?style=for-the-badge&logo=github&color=gold" alt="GitHub Stars"/></a>
  <a href="https://github.com/mahdiasd/MarkdownRTL/issues"><img src="https://img.shields.io/github/issues/mahdiasd/MarkdownRTL?style=for-the-badge&logo=github&color=critical" alt="GitHub Issues"/></a>
  <a href="LICENSE"><img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?style=for-the-badge" alt="Apache 2.0 License"/></a>
</p>

<p align="center">
  <a href="#quick-start"><b>⚡ Install Now</b></a> •
  <a href="https://plugins.jetbrains.com/plugin/34457-markdown-rtl"><b>📦 Marketplace Page</b></a> •
  <a href="https://github.com/mahdiasd/MarkdownRTL/issues"><b>🐛 Report Issue</b></a> •
  <a href="#support-and-feedback"><b>⭐ Star Project</b></a>
</p>

---

## 📑 Table of Contents

- [🚀 Quick Start & Installation](#quick-start)
  - [Method 1: JetBrains Marketplace (In-IDE) - Recommended](#marketplace-ide)
  - [Method 2: JetBrains Marketplace (Web)](#marketplace-web)
  - [Method 3: Offline / Manual Installation from ZIP](#manual-zip-install)
  - [👁️ Activating in IntelliJ IDEA & Android Studio (Preview Mode)](#activating-preview-mode)
- [⭐ Support & Feedback](#support-and-feedback)
- [📌 Executive Overview](#executive-overview)
- [🔄 Direction Modes Comparison](#direction-modes)
- [🎛️ In-Preview Interactive Bento Control Card](#bento-controller)
- [⌨️ Keyboard Shortcuts & Quick Actions](#keyboard-shortcuts)
- [⚙️ IDE Settings Configuration](#ide-settings)
- [⚡ Smart BiDi Detection & Code Protection Pipeline](#bidi-engine)
- [🏗️ System Architecture](#system-architecture)
- [🛠️ Building & Running from Source](#building-from-source)
- [📂 Project Structure](#project-structure)
- [🤝 Contributing](#contributing)
- [📄 License & Credits](#license-and-credits)

---

## <a id="quick-start"></a>🚀 Quick Start & Installation

You can install **Markdown RTL** directly via the official JetBrains Marketplace or manually from a pre-built distribution archive.

### <a id="marketplace-ide"></a>Method 1: JetBrains Marketplace (In-IDE) - Recommended

The easiest and fastest method. Updates are delivered automatically by your IDE:

1. Open **IntelliJ IDEA**, **Android Studio**, or any JetBrains IDE.
2. Open Settings / Preferences:
   - **Windows / Linux:** <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>S</kbd>
   - **macOS:** <kbd>Cmd</kbd> + <kbd>,</kbd>
3. Select **Plugins** from the left sidebar.
4. Click the **Marketplace** tab at the top.
5. In the search box, type **`Markdown RTL`**.
6. Click **Install**, then click **Restart IDE** when prompted.

> [!TIP]
> After restarting, open any `.md` file in Split or Preview mode. Markdown RTL activates automatically!

---

### <a id="marketplace-web"></a>Method 2: JetBrains Marketplace (Web)

1. Visit the official plugin page on JetBrains Marketplace:  
   👉 **[https://plugins.jetbrains.com/plugin/34457-markdown-rtl](https://plugins.jetbrains.com/plugin/34457-markdown-rtl)**
2. Click the **Get** button and choose **Install to...** to launch the installation directly in your running IDE.

---

### <a id="manual-zip-install"></a>Method 3: Offline / Manual Installation from ZIP

If you work behind an air-gapped corporate firewall or prefer installing pre-built binaries directly:

1. Download the latest `PersianMarkdown-x.x.x.zip` (or `MarkdownRTL-x.x.x.zip`) from [GitHub Releases](https://github.com/mahdiasd/MarkdownRTL/releases).
2. Open your IDE's **Settings / Preferences** (<kbd>Ctrl+Alt+S</kbd> or <kbd>Cmd+,</kbd>).
3. Navigate to **Plugins**.
4. Click the **Gear icon (⚙️)** in the top-right of the Plugins panel and choose **Install Plugin from Disk...**.
5. Select the downloaded `.zip` archive file and click **OK**.
6. Restart your IDE to complete the installation.

---

### <a id="activating-preview-mode"></a>👁️ Activating in IntelliJ IDEA & Android Studio (Preview Mode)

> [!IMPORTANT]
> **Where does Markdown RTL work?**  
> Markdown RTL operates directly within the **Markdown Preview Window** in **IntelliJ IDEA** and **Android Studio**. It seamlessly activates when viewing documents in **Split Mode** or **Preview-Only Mode**.

#### Step-by-Step Usage:
1. **Open any Markdown file (`.md`)** in IntelliJ IDEA or Android Studio.
2. In the top-right corner of the editor tab, switch the editor view to:
   - 🌓 **Split View (`Editor and Preview`)**: Keep writing your Markdown on the left while enjoying live, bidirectional RTL rendering on the right.
   - 📄 **Preview Only (`Preview`)**: Distraction-free, full-width formatted reading experience.
3. **Instant RTL Activation:**
   - Persian and Arabic text automatically formats with right-to-left alignment, proper punctuation placement, and embedded *Vazirmatn* typography.
   - All code fences (`<pre>`, `.code-fence`, inline `<code>`) remain strictly Left-to-Right (LTR) and left-aligned.
   - The floating **Bento Control Pill** appears in the bottom-left corner of the preview pane for on-the-fly typography adjustments.

---

## <a id="support-and-feedback"></a>⭐ Support & Feedback

If you find **Markdown RTL** helpful, here is how you can support the project and help fellow developers:

- **⭐ Star the Repository:** Give this project a star on [GitHub](https://github.com/mahdiasd/MarkdownRTL) to help others discover it.
- **🌟 Rate on JetBrains Marketplace:** Leave a review and rating on the [JetBrains Plugin Page](https://plugins.jetbrains.com/plugin/34457-markdown-rtl) — your feedback means the world to us!
- **🐛 Report Bugs & Request Features:** Encountered an issue or have an idea to make this plugin even better? Please **[Open an Issue](https://github.com/mahdiasd/MarkdownRTL/issues)** with details, steps to reproduce, or feature suggestions. Every issue and pull request is reviewed promptly.

---

## <a id="executive-overview"></a>📌 Executive Overview

**Markdown RTL** is an advanced, zero-configuration IntelliJ Platform plugin engineered to provide **smart Right-to-Left (RTL) text balancing**, **intelligent BiDi layout detection**, and **premium Persian / Arabic typography** directly inside the built-in Markdown preview window of **IntelliJ IDEA** and **Android Studio**.

Standard IDE Markdown previews render documents under an assumption of Left-to-Right (LTR) Western typography. For Persian, Arabic, and bilingual documentation writers, this creates severe usability challenges:
- Sentences become inverted and punctuation marks shift to incorrect sides.
- Bullet points, numbered lists, and blockquotes render backwards.
- Workarounds that force global RTL break technical code blocks, causing source code to become right-aligned or flipped.

**Markdown RTL solves these challenges completely:**

- ✅ **Automatic BiDi Balancing:** Intelligently identifies Persian and Arabic scripts on an element-by-element basis.
- ✅ **Strict Code Block & Token Protection:** Monospace code fences (`<pre>`, `.code-fence`, `<code>`) remain strictly Left-to-Right (LTR), left-aligned, and bidirectionally isolated.
- ✅ **Embedded Vazirmatn Typography:** No OS-level font setup required. Bundled with high-legibility **Vazirmatn** (Regular and Bold) and **JetBrains Mono**.
- ✅ **Live In-Preview Bento Controller:** A floating glassmorphic control capsule inside the preview window for instantaneous typography adjustments with zero reloads.
- ✅ **Zero Impact on Raw Markdown:** Preserves your raw markdown source files cleanly without injecting HTML tags or metadata.

---

## <a id="direction-modes"></a>🔄 Direction Modes Comparison

Markdown RTL supports three flexible direction modes tailored to any writing workflow:

<p align="center">
  <img src="docs/direction-modes.svg" alt="Direction Modes Visual Comparison" width="100%"/>
</p>

| Mode | Identifier | Description | Best Suited For | Code Blocks |
| :--- | :--- | :--- | :--- | :---: |
| **Auto RTL** *(Default)* | `auto` | Analyzes every paragraph, heading, list item, and table cell independently. Persian aligns right, English aligns left. | Mixed documentation, technical tutorials, bilingual notes | **LTR Protected** |
| **Force RTL** | `force_rtl` | Aligns all paragraphs, headings, blockquotes, and tables to the right. | Monolingual Persian / Arabic articles, blog posts, books | **LTR Protected** |
| **Force LTR** | `force_ltr` | Reverts preview layout to classic standard Left-to-Right orientation. | Standard English specifications, API references | **LTR Protected** |

---

## <a id="bento-controller"></a>🎛️ In-Preview Interactive Bento Control Card

Markdown RTL features an interactive floating Bento card right inside the Chromium (JCEF) preview pane. Adjust typography and modes instantly without navigating through complex IDE dialogs:

<p align="center">
  <img src="docs/interactive-widget-ui.svg" alt="Interactive Widget UI" width="100%"/>
</p>

### Key Capabilities:
- **Capsule Trigger Pill**: Neatly pinned to the bottom-left corner with an active glowing status indicator. Displays current mode and quick shortcuts.
- **Mode Toggles**:
  - **Enabled Switch** (<kbd>Alt</kbd>+<kbd>E</kbd> / <kbd>⌥E</kbd>): Instantly enable or disable the RTL engine.
  - **Force RTL Switch** (<kbd>Alt</kbd>+<kbd>R</kbd> / <kbd>⌥R</kbd>): Toggle between Auto BiDi and Force RTL modes.
- **Searchable Font Comboboxes**:
  - **FA/AR Font**: Choose from bundled *Vazirmatn*, or system fonts (*Sahel*, *Shabnam*, *Samim*, *IRANSans*, *Tahoma*, etc.).
  - **EN Font**: Choose from *JetBrains Mono*, *SF Pro*, *Inter*, *Geist*, *Segoe UI*, or custom entries.
  - **Code Font**: Dedicated monospace font for code snippets (*JetBrains Mono*, *Fira Code*, *Cascadia Code*, *Consolas*).
  - *System Font Autocompletion*: Automatically queries installed operating system fonts via `java.awt.GraphicsEnvironment`.
- **Typography Steppers**:
  - **Line Height Multiplier**: Scale from `1.0x` to `2.8x` (Default: `1.8x` for optimal Persian readability).
  - **Base Font Size**: Scale from `10px` to `32px` (Default: `16px`). Headings (`H1`–`H6`) scale proportionally.
- **Local Persistence**: Widget preferences are automatically saved in `localStorage` and persist across IDE restarts.
- **Instant Reset**: One-click restore button to revert to default typography recommendations.

---

## <a id="keyboard-shortcuts"></a>⌨️ Keyboard Shortcuts & Quick Actions

| Shortcut | Scope | Action |
| :--- | :--- | :--- |
| <kbd>Shift</kbd> + <kbd>Alt</kbd> + <kbd>R</kbd> | Global IDE / Editor | Cycle direction: **Auto RTL** ➔ **Force RTL** ➔ **Force LTR** |
| <kbd>Alt</kbd> + <kbd>R</kbd> *(or <kbd>⌥R</kbd>)* | Markdown Preview Pane | Toggle **Force RTL** mode on / off |
| <kbd>Alt</kbd> + <kbd>E</kbd> *(or <kbd>⌥E</kbd>)* | Markdown Preview Pane | Toggle **Plugin Enabled** state |
| <kbd>Esc</kbd> | Markdown Preview Pane | Dismiss / collapse floating Bento card |

### UI Access Points:
- **Editor Top-Right Toolbar:** Click the split direction icon.
- **Editor Floating Toolbar:** Instant access bar above active Markdown lines.
- **Right-Click Context Menu:** Select **Markdown RTL Direction** from the editor menu.
- **Main Menu Bar:** Navigate to **Tools** ➔ **Markdown RTL Direction**.

---

## <a id="ide-settings"></a>⚙️ IDE Settings Configuration

For global, project-wide preferences, open the IDE settings:

- **Windows / Linux:** `Settings` ➔ `Tools` ➔ `Persian Markdown` (<kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>S</kbd>)
- **macOS:** `Preferences` ➔ `Tools` ➔ `Persian Markdown` (<kbd>Cmd</kbd> + <kbd>,</kbd>)

### Available Options:
1. **Text Direction (RTL / LTR):** Select default mode (*Auto RTL*, *Force RTL*, *Force LTR*).
2. **Preserve LTR for Code Blocks:** Strict bidirectional isolation of monospace blocks.
3. **Use Bundled Vazirmatn Font:** Embeds Base64 font definitions into CSS stylesheet.
4. **Font Family Fallback Stack:** Configure custom fallback font order.
5. **Base Font Size & Line Height Multiplier:** Set default metrics across all Markdown previews.
6. **Element Enhancements:** Proportional heading scaling and right-aligned quotes/lists.
7. **Chromium Compatibility Button:** One-click action to verify and lock JCEF as the active Markdown renderer.

---

## <a id="bidi-engine"></a>⚡ Smart BiDi Detection & Code Protection Pipeline

Markdown RTL does not naively apply `direction: rtl` to the whole page. Instead, it performs an intelligent heuristic analysis across block elements while strictly isolating code blocks and metadata.

<p align="center">
  <img src="docs/bidi-engine-pipeline.svg" alt="BiDi Detection and Code Protection Pipeline" width="100%"/>
</p>

### Pipeline Execution Flow:

1. **Element Traversal:** On initial document load and every DOM mutation, the engine inspects block elements: `p`, `h1`–`h6`, `li`, `blockquote`, `td`, and `th`.
2. **Code Fence Bypass:** Any element matching `pre`, `.code-fence`, or `.markdown-code-fence` immediately bypasses RTL classification:
   ```css
   direction: ltr !important;
   text-align: left !important;
   unicode-bidi: isolate !important;
   font-family: var(--pm-code-font) !important;
   ```
3. **Noise & Markdown Stripping:** Strips child `<code>` snippets, task checkmarks (`[x]`, `[ ]`), links (`[label]`), and URLs (`https://...`) so technical tokens never bias Persian prose detection.
4. **Unicode BiDi Evaluation:** The remaining clean text is tested against Persian and Arabic Unicode character blocks:
   ```javascript
   /[\u0600-\u06FF\u0750-\u077F\u08A0-\u08FF\uFB50-\uFDFF\uFE70-\uFEFF]/
   ```
5. **Direction & Style Assignment:**
   - **Match Found:** Receives `dir="rtl"`, `.persian-dir-rtl`, and Vazirmatn font stack. Blockquotes receive an elegant right-hand border (`border-right: 4px solid #6366f1`).
   - **No Match (English):** Receives `dir="ltr"`, `.persian-dir-ltr`, and JetBrains Mono/Inter typography. Blockquotes keep standard left border.
6. **Inline Code & Link Isolation:** Inline `<code>` and `<a>` elements receive `unicode-bidi: isolate !important` to ensure technical words inside Persian sentences never flip parentheses or phrases.

---

## <a id="system-architecture"></a>🏗️ System Architecture

Markdown RTL hooks directly into the IntelliJ Platform's native Chromium Embedded Framework (JCEF) preview pipeline via custom `browserPreviewExtensionProvider` extension points.

<p align="center">
  <img src="docs/system-architecture.svg" alt="Markdown RTL Architecture" width="100%"/>
</p>

### Architecture Layers:

1. **Host IDE Environment (`org.intellij.plugins.markdown`)**:
   - **Startup Activity ([`PersianMarkdownStartupActivity`](file:///src/main/kotlin/com/persian/markdown/startup/PersianMarkdownStartupActivity.kt))**: Ensures that the preview engine is configured to use the high-performance Chromium JCEF provider (`JCEFHtmlPanelProvider`).
   - **Persistent State Service ([`PersianMarkdownSettings`](file:///src/main/kotlin/com/persian/markdown/settings/PersianMarkdownSettings.kt))**: Application-level service storing user preferences (`DirectionMode`, fonts, font size, line height). Emits changes across the IDE message bus.
   - **Actions & Shortcuts ([`ToggleDirectionAction`](file:///src/main/kotlin/com/persian/markdown/actions/ToggleDirectionAction.kt))**: Seamlessly integrated into toolbars and context menus.

2. **Plugin Extension & Generator Layer**:
   - **Browser Preview Extension ([`PersianMarkdownBrowserExtension`](file:///src/main/kotlin/com/persian/markdown/preview/PersianMarkdownBrowserExtension.kt))**: Registered with priority `AFTER_ALL` to ensure styles evaluate on top of all Markdown rendering passes.
   - **CSS Generator ([`CssGenerator.kt`](file:///src/main/kotlin/com/persian/markdown/preview/CssGenerator.kt))**: Dynamically generates embedded Base64 `@font-face` definitions, responsive CSS custom variables, and the interactive Bento controller script.
   - **Virtual Resource Provider**: Serves `persianMarkdown/persian.css` and `persianMarkdown/persian.js` over memory streams.

3. **Chromium JCEF Preview Runtime**:
   - The browser panel dynamically loads stylesheet and JavaScript runtime.
   - A `MutationObserver` continuously watches the DOM tree for user edits in split editor view, instantly applying BiDi rules in under **2 milliseconds**.

---

## <a id="building-from-source"></a>🛠️ Building & Running from Source

### Prerequisites:
- **Java JDK 21** or later
- **Gradle 8.x** (wrapper included)
- Compatible with IntelliJ IDEA Community / Ultimate (2024.3+) and Android Studio (Ladybug 2024.2.1+)

### 1. Clone Repository
```bash
git clone https://github.com/mahdiasd/MarkdownRTL.git
cd MarkdownRTL
```

### 2. Run Test Suite
```bash
./gradlew test
```

### 3. Build Distributable ZIP Archive
```bash
./gradlew buildPlugin
```
The output package will be generated at:
```text
build/distributions/PersianMarkdown-1.0.0.zip
```
*(You can upload this ZIP directly to GitHub Releases or install it via **Install Plugin from Disk...** in your IDE)*

### 4. Launch Sandboxed IDE Instance
```bash
./gradlew runIde
```

---

## <a id="project-structure"></a>📂 Project Structure

```text
MarkdownRTL/
├── docs/                                  # High-resolution SVG architecture diagrams
│   ├── system-architecture.svg            # End-to-end execution pipeline diagram
│   ├── bidi-engine-pipeline.svg           # BiDi parsing & code fence guard pipeline
│   ├── interactive-widget-ui.svg          # Floating Bento card design blueprint
│   └── direction-modes.svg                # Visual comparison of Auto, Force RTL & LTR
├── src/
│   ├── main/
│   │   ├── kotlin/com/persian/markdown/
│   │   │   ├── actions/
│   │   │   │   └── ToggleDirectionAction.kt       # Shortcut & toolbar direction toggler
│   │   │   ├── preview/
│   │   │   │   ├── CssGenerator.kt                # CSS & JS dynamic bundle generator
│   │   │   │   └── PersianMarkdownBrowserExtension.kt # JCEF browser provider & resource server
│   │   │   ├── settings/
│   │   │   │   ├── DirectionMode.kt               # Direction enum (Auto, Force RTL, Force LTR)
│   │   │   │   ├── PersianMarkdownConfigurable.kt # IDE Settings dialog UI panel
│   │   │   │   └── PersianMarkdownSettings.kt     # Application service & topic EventBus
│   │   │   └── startup/
│   │   │       └── PersianMarkdownStartupActivity.kt # Auto JCEF engine verifier
│   │   └── resources/
│   │       ├── META-INF/
│   │       │   └── plugin.xml                     # IntelliJ plugin registration descriptor
│   │       ├── fonts/                             # Embedded TTF font assets
│   │       │   ├── Vazirmatn-Regular.ttf
│   │       │   ├── Vazirmatn-Bold.ttf
│   │       │   ├── JetBrainsMono-Regular.ttf
│   │       │   └── JetBrainsMono-Bold.ttf
│   │       └── icons/                             # Plugin action vector icons
│   └── test/
│       └── kotlin/com/persian/markdown/
│           └── CssGeneratorTest.kt                # Unit test validation suite
├── build.gradle.kts                               # IntelliJ Platform Gradle configuration
├── gradle.properties                              # Version and compatibility definitions
└── README.md                                      # Documentation & user guide
```

---

## <a id="contributing"></a>🤝 Contributing

Contributions, bug reports, and suggestions are warmly appreciated!

1. Fork the Project.
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`).
3. Commit your Changes (`git commit -m 'feat: add amazing feature'`).
4. Push to the Branch (`git push origin feature/AmazingFeature`).
5. Open a Pull Request.

If you encounter any issues or have questions, feel free to **[Open an Issue](https://github.com/mahdiasd/MarkdownRTL/issues)**.

---

## <a id="license-and-credits"></a>📄 License & Credits

Distributed under the **Apache License 2.0**. See `LICENSE` for more information.

- **Vazirmatn Font:** Designed by Saber Rastikerdar under the [OFL License](https://scripts.sil.org/OFL).
- **JetBrains Mono:** Designed by JetBrains under the [Apache 2.0 License](https://www.apache.org/licenses/LICENSE-2.0).

---

<p align="center">
  Crafted with ❤️ for the Persian and RTL developer community.<br/>
  <b>If you like this plugin, don't forget to <a href="https://github.com/mahdiasd/MarkdownRTL">give it a ⭐ on GitHub</a> and <a href="https://plugins.jetbrains.com/plugin/34457-markdown-rtl">leave a review on Marketplace</a>!</b>
</p>
