---
name: document-analyzer
title: تحلیل‌گر هوشمند مستندات
version: 2.1.0-beta
author: Mahdi Asadollahpour
language: fa-IR
tags: [analysis, artificial-intelligence, فارسی, پردازش-متن]
description: این ابزار متن‌های دوزبانه را پردازش کرده و بخش‌های مرتبط را بازمی‌گرداند.
source_url: https://github.com/mahdiasd/MarkdownRTL
---

# تحلیل‌گر هوشمند مستندات

این سند حاوی **فرانت‌متر دوزبانه (Bilingual)** است:
- کلیدها انگلیسی هستند (`name`, `title`, `version`, `tags`, `description`).
- مقادیر ترکیبی از فارسی و انگلیسی هستند.

## نتیجه مورد انتظار
- در جدول متادیتا، ستون کلیدها با فونت انگلیسی مونو و جهت چپ‌چین نمایش داده شود.
- مقادیر فارسی (مانند عنوان و توضیحات) با جهت راست‌چین (RTL) و فونت فارسی رندر شوند.
- مقادیر انگلیسی (مانند `source_url` و `version`) با جهت چپ‌چین (LTR) رندر شوند.
- تگ‌های داخل آرایه به صورت نشان‌های تفکیک‌شده (Chips) باشند.
