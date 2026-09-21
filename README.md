# Markdown RTL Plugin for IntelliJ IDEA & Android Studio

[![GitHub Repo](https://img.shields.io/badge/GitHub-mahdiasd%2FMarkdownRTL-blue?logo=github)](https://github.com/mahdiasd/MarkdownRTL)

پلاگین قدرتمند راست‌چین‌سازی هوشمند (Smart RTL) و ارتقای تایپوگرافی زبان فارسی و عربی در پیش‌نمایش فایل‌های Markdown برای تمامی IDEهای مبتنی بر پلتفرم IntelliJ و Android Studio.
شامل ویجت شناور مدرن تنظیمات زنده (Live Settings Card)، فونت تعبیه‌شده وزیرمتن، و هماهنگی کامل با کدهای برنامه‌نویسی.

---

## 🌟 ویژگی‌های برجسته (Features)

1. **مدیریت جهت‌گیری متن (Direction Modes):**
   - **Auto RTL (پیش‌فرض هوشمند):** تشخیص خودکار جهت‌گیری متن بر اساس قواعد یونیکد (BiDi) برای هر پاراگراف یا سرفصل به‌صورت مجزا. متن‌های فارسی راست‌به‌چپ و متون انگلیسی چپ‌به‌راست نمایش داده می‌شوند.
   - **Force RTL:** راست‌چین کردن کل پیش‌نمایش.
   - **Force LTR:** حالت چپ‌چین استاندارد.
2. **محافظت کامل از قطعه‌کدها (Code Blocks Protection):**
   - تمامی بلوک‌های کد (`pre`, `code`, `.code-fence`) حتی در حالت Force RTL کاملاً چپ‌به‌راست (LTR) و با فونت اختصاصی برنامه‌نویسی JetBrains Mono باقی می‌مانند.
3. **تایپوگرافی ویژه فارسی (Typography):**
   - **فونت داخلی Vazirmatn:** فونت آزاد و خوانای وزیرمتن (در دو وزن Regular و Bold) درون پلاگین باندل شده است؛ بنابراین حتی بدون نصب فونت در سیستم‌عامل کاربر، پیش‌نمایش با ظاهر حرفه‌ای رندر می‌شود.
   - **انتخاب فونت دلخواه (Font Family):** امکان تغییر زنجیره فونت به فونت‌های دیگر مانند Shabnam، Sahel، IRANSans، Tahoma و ...
   - **سایز فونت (Font Size):** تنظیم اندازه فونت به پیکسل با اعمال متناسب بر روی سرفصل‌ها (H1 تا H6).
   - **فاصله خطوط (Line Height):** کنترل ضریب فاصله خطوط (پیشنهاد ۱.۷ تا ۲.۰ برای خط فارسی).
4. **رابط کاربری و کلید میان‌بر:**
   - پنجره تنظیمات یکپارچه در مسیر: `Settings / Preferences` > `Tools` > `Persian Markdown`.
   - کلید میان‌بر `Shift + Alt + R` برای جابه‌جایی سریع میان حالت‌های Auto RTL / Force RTL / Force LTR.

---

## 🚀 نحوه ساخت و نصب (Build & Installation)

### پیش‌نیازها:
- Java JDK 21
- IntelliJ IDEA (نسخه 2024.3 تا 2026+) یا Android Studio (نسخه‌های Ladybug / Otter / Meerkat)

### دستورات ساخت:
برای کامپایل و اجرای تست‌ها:
```bash
./gradlew test
```

برای تولید فایل فشرده پلاگین (`.zip`):
```bash
./gradlew buildPlugin
```
فایل خروجی در مسیر زیر ایجاد می‌شود:
```
build/distributions/PersianMarkdown-1.0.0.zip
```

### نصب در محیط IntelliJ IDEA یا Android Studio:
1. وارد **Settings** (در مک: `Cmd + ,`، در ویندوز/لینوکس: `Ctrl + Alt + S`) شوید.
2. به بخش **Plugins** بروید.
3. روی آیکون چرخ‌دنده (⚙️) بالای صفحه کلیک کرده و گزینه **Install Plugin from Disk...** را انتخاب کنید.
4. فایل `build/distributions/PersianMarkdown-1.0.0.zip` را انتخاب و سپس IDE را Restart کنید.

---

## 🧪 تست در محیط توسعه ایزوله (runIde)

برای باز کردن یک نسخه شبیه‌سازی‌شده از IntelliJ IDEA به همراه این پلاگین:
```bash
./gradlew runIde
```
