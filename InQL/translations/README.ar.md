<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕸️ InQL - ورقة الغش الكاملة

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗███╗   ██╗ ██████╗ ██╗     
  ██║████╗  ██║██╔═══██╗██║     
  ██║██╔██╗ ██║██║   ██║██║     
  ██║██║╚██╗██║██║▄▄ ██║██║     
  ██║██║ ╚████║╚██████╔╝███████╗
  ╚═╝╚═╝  ╚═══╝ ╚══▀▀═╝ ╚══════╝
      Comprehensive GraphQL Security Assessment & Introspection Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/InQL-GraphQL_Security-blue?style=for-the-badge" alt="InQL">
  <img src="https://img.shields.io/badge/Author-Doyensec-orange?style=for-the-badge" alt="Doyensec">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="أمان الويب">
  <img src="https://img.shields.io/badge/License-BSD--3--Clause-purple?style=for-the-badge" alt="BSD">
</ص>

<p align="center">
  <b>🕸️ إطار عمل أمان CLI متقدم وملحق Burp Suite تم تطويره بواسطة Doyensec لتفريغ مخططات استبطان GraphQL وإنشاء قوالب استعلام وتدقيق أمان GraphQL API</b>
</ص>

---

## 📋 جدول المحتويات

- [نظرة عامة وأساسيات أمان GraphQL](#-overview--graphql-security-fundamentals)
  - [مخاطر استعلام الاستبطان](#مخاطر استعلام الاستبطان)
- [التثبيت](#-التثبيت)
- [إلقاء الاستبطان وإنشاء المخطط](#-إغراق الاستبطان--إنشاء المخطط)
  - [استعلام استبطان CLI](#cli-introspection-query)
  - [الإغراق إلى لغة تعريف المخطط (SDL)](#dumping-to-schema-definition-language-sdl)
  - [إنشاء قوالب استعلام جاهزة للتشغيل](#إنشاء قوالب استعلام جاهزة للتشغيل)
- [تدقيق الطفرات والاستعلامات الحساسة](#-تدقيق-طفرات--استعلامات-حساسة)
- [اكتشاف رفض الخدمة وأخطاء التجميع](#-كشف-رفض الخدمة--عيوب التجميع)
  - [الاستعلام الدفعي (تجميع الصفيف)](#batch-querying-array-batching)
  - [المراجع الدائرية والاستعلامات المتداخلة](#المراجع الدائرية--الاستعلامات المتداخلة)
- [الجدول المرجعي السريع](#-الجدول المرجعي السريع)
- [الاعتبارات الدفاعية والتصلب](#-الاعتبارات الدفاعية--التصلب)
- [إخلاء المسؤولية القانونية](#-إخلاء المسؤولية القانونية)

---

## 🎯 نظرة عامة وأساسيات أمان GraphQL

على عكس واجهات برمجة تطبيقات REST القياسية ذات عناوين URL المنفصلة (`/api/v1/users`)، يعرض GraphQL نقطة نهاية HTTP واحدة (عادةً `/graphql`) حيث يطلب العملاء أشكال بيانات مخصصة عبر الاستعلامات والطفرات.

### مخاطر الاستعلام عن الاستبطان
يتميز GraphQL بنظام استبطان مدمج (`__schema`، `__type`) مصمم لأدوات المطورين. إذا تركت ممكنة في الإنتاج:
- يمكن للمهاجمين الاستعلام عن مخطط الواجهة الخلفية بأكمله.
- الميزات التي لم يتم إصدارها، والطفرات الإدارية الداخلية (مثل `deleteUser`، `changeRole`)، والحقول الخاصة مكشوفة بالكامل.

تم إنشاء **InQL** بواسطة **Doyensec**، ويقوم بأتمتة استخراج الاستبطان، وتحليل المخططات، وإنشاء الوثائق، وإنشاء قوالب استغلال جاهزة للاستخدام.

---

## 📥 التثبيت
```bash
# Using Python pip (Recommended)
pip install inql

# Or clone from GitHub
git clone https://github.com/doyensec/inql.git
cd inql && pip install .

# Verify installation
inql -h
```
---

## 🚀 تفريغ الاستبطان وإنشاء المخططات

### استعلام استبطان CLI
الاستعلام عن نقطة نهاية GraphQL بعيدة، واستخراج المخطط الكامل، وحفظه في مجلد الإخراج:
```bash
inql -t https://target.com/graphql -o /tmp/graphql_audit/
```
*تم إنشاؤها في دليل الإخراج:*
- `schema.json`: تفريغ استبطان JSON الخام.
- `schema.graphql`: لغة تعريف المخطط المقروءة بواسطة الإنسان (SDL).
- `queries/`: قوالب استعلام يتم إنشاؤها تلقائيًا لكل استعلام متاح.
- `mutations/`: قوالب طفرة تم إنشاؤها تلقائيًا لتعديل البيانات.

### المسح المصدق والرؤوس المخصصة
تمرير ملفات تعريف الارتباط للجلسة أو رموز التفويض أو الرؤوس المخصصة:
```bash
inql -t https://target.com/graphql \
  -H "Authorization: Bearer <TOKEN>" \
  -H "X-API-Key: secret123" \
  -o ./audit_report/
```
### الإغراق في لغة تعريف المخطط (SDL)
تحويل مخطط JSON الخام الموجود إلى SDL:
```bash
inql -f schema.json --generate-sdl -o ./sdl_output/
```
---

## 💥 تدقيق الطفرات والاستعلامات الحساسة

قم بمراجعة ملفات الطفرة التي تم إنشاؤها تلقائيًا في مجلد الإخراج:
```text
mutations/
├── registerUser.graphql
├── resetPassword.graphql
├── updateProfile.graphql
└── adminPromoteUser.graphql
```
أرسل طفرات عالية التأثير باستخدام Curl أو Burp Suite:
```bash
curl -X POST https://target.com/graphql \
  -H "Content-Type: application/json" \
  -d '{"query": "mutation { adminPromoteUser(userId: 10, role: \"SUPERADMIN\") { success } }"}'
```
---

## ⚡كشف عيوب رفض الخدمة والتجميع

### الاستعلام الدفعي (تجميع المصفوفات)
اختبار ما إذا كان الخادم يعالج صفائف الاستعلامات في طلب HTTP واحد (يتجاوز تحديد المعدل):
```json
[
  {"query": "query { getUser(id: 1) { email } }"},
  {"query": "query { getUser(id: 2) { email } }"},
  {"query": "query { getUser(id: 3) { email } }"}
]
```
### المراجع الدائرية والاستعلامات المتداخلة
اختبار تعطل العلاقة العودية:
```graphql
query {
  thread {
    messages {
      thread {
        messages {
          thread {
            messages {
              id
            }
          }
        }
      }
    }
  }
}
```
---

## ⚡ جدول مرجعي سريع

| الهدف | الأمر |
|-----------|--------|
| **تفريغ الاستبطان** | `inql -t https://target.com/graphql -o ./out/` |
| **الاستبطان الموثق**| `inql -t <URL> -H "Authorization: Bearer <TOKEN>" -o ./out/` |
| ** تحويل مخطط JSON إلى SDL ** | `inql -f schema.json --generate-sdl -o ./sdl/` |
| **إنشاء مستندات HTML** | `inql -t <URL> --generate-html -o ./docs/` |

---

## 🛡️ الاعتبارات الدفاعية والتصلب

1. **تعطيل الاستبطان في الإنتاج:** قم بتعطيل `introspection` في تكوين خادم GraphQL (Apollo، Yoga، GraphQL-Java).
2. ** فرض عمق الاستعلام وتحليل التكلفة: ** قم بتنفيذ مكتبات مثل `graphql-depth-limit` أو تحليل تكلفة الاستعلام لإسقاط الاستعلامات المتداخلة بعمق.
3. **تعطيل التجميع:** قم بتقييد أو تعطيل تنفيذ الاستعلام الدفعي القائم على المصفوفة إذا لم يكن ذلك ضروريًا تمامًا.

---

## ⚠️إخلاء المسؤولية القانونية

> [!تنبيه]
> تم تطوير InQL لإجراء تقييمات أمنية معتمدة، واختبارات الاختراق، وتدقيق أمان واجهة برمجة التطبيقات (API). يعد استخراج المخططات أو تعديل البيانات على نقاط النهاية غير المصرح بها أمرًا غير قانوني.