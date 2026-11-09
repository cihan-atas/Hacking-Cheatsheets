<!-- language-home-navigation -->
[🏠 Русская главная страница](../../README.ru.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx и Apktool — полная шпаргалка

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
       ██╗ █████╗ ██████╗ ██╗  ██╗    ███████╗    █████╗ ██████╗ ██╗  ██╗████████╗ ██████╗  ██████╗ ██╗     
       ██║██╔══██╗██╔══██╗╚██╗██╔╝    ██╔════╝   ██╔══██╗██╔══██╗██║ ██╔╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║███████║██║  ██║ ╚███╔╝     ███████╗   ███████║██████╔╝█████╔╝    ██║   ██║   ██║██║   ██║██║     
  ██   ██║██╔══██║██║  ██║ ██╔██╗     ╚════██║   ██╔══██║██╔═══╝ ██╔═██╗    ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝██║  ██║██████╔╝██╔╝ ██╗    ███████║██╗██║  ██║██║     ██║  ██╗   ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝    ╚══════╝╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
           Android APK Decompilation, Smali Patching & Repackaging
```
<p выравнивание="центр">
  <img src="https://img.shields.io/badge/Jadx-DEX_to_Java-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Disassembler-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Category-Mobile_Reverse_Engineering-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</p>

<p выравнивание="центр">
  <b>🔍 Полное руководство по реверс-инжинирингу Android: декомпиляция файлов APK/DEX/AAR в читаемый исходный код Java с помощью Jadx, а также дизассемблирование/исправление кода Smali и переупаковка с помощью Apktool</b>
</p>

---

## 📋 Содержание

- [Обзор и архитектура](#-обзор--архитектура)
- [Установка](#-установка)
- [Jadx: декомпиляция в исходный код Java](#-jadx-decompiling-to-java-source)
  - [Режим графического интерфейса Jadx](#jadx-gui-mode)
  - [Пакетная декомпиляция Jadx CLI](#jadx-cli-batch-decompilation)
  - [Извлечение ценных активов и манифест](#extracting-high-value-assets--manifest)
- [Apktool: Дизассемблирование и извлечение Smali](#-apktool-disassembly--smali-extraction)
  - [Декомпиляция в Smali и ресурсы](#decompiling-to-smali--resources)
  - [Анализ и исправление кода Smali](#analyzing--patching-smali-code)
  - [Обход конфигурации сетевой безопасности (Cleartext/Burp)](#bypassing-network-security-config-cleartextburp)
- [Восстановление, согласование и подписание исправленного APK](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. Перестроить с помощью Apktool] (#1-rebuild-with-apktool)
  - [2. Создать сертификат хранилища ключей](#2-генерировать-сертификат хранилища ключей)
  - [3. Оптимизация Zipalign] (#3-zipalign-оптимизация)
  - [4. Подписать с помощью Apksigner](#4-sign-with-apksigner)
- [Краткая справочная таблица](#-краткая справочная-таблица)
- [Защитные соображения и обфускация кода](#-защитные-соображения--код-обфускация)
- [Юридическая оговорка](#-юридическая оговорка)

---

## 🎯 Обзор и архитектура

Приложения Android (`.apk`) по сути представляют собой ZIP-архивы, содержащие скомпилированный байт-код исполняемого файла Dalvik (`classes.dex`), предварительно скомпилированные ресурсы (`resources.arsc`), двоичные XML-манифесты (`AndroidManifest.xml`) и собственные библиотеки C/C++ (`lib/`).

Два важных взаимодополняющих инструмента составляют основу анализа Android:
1. **Jadx (декомпилятор Dex в Java):** преобразует байт-код Dalvik непосредственно в чистый, удобочитаемый исходный код Java. Он имеет быстрый графический интерфейс с перекрестными ссылками на код (xrefs), поиском методов и алгоритмами деобфускации.
2. **Apktool:** Дизассемблирует ресурсы APK в их почти исходную форму и преобразует байт-код Dalvik в промежуточную сборку **Smali**. Он позволяет изменять логику приложения, внедрять собственные перехватчики Smali и переупаковывать модифицированный APK.

---

## 📥 Установка
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx: декомпиляция в исходный код Java

### Режим графического интерфейса Jadx
Самый простой способ интерактивного анализа APK:
```bash
jadx-gui target.apk
```
*Keyboard Shortcuts in Jadx-GUI:*
- `Ctrl + Shift + F`: Global text and class search.
- `R`: найти перекрестные ссылки (где используется класс или метод).
- `N`: переименовать переменную/метод (с автоматическим рефакторингом).

### Jadx CLI Batch Decompilation
Экспортируйте исходный код, ресурсы и ресурсы Java в выходной каталог без запуска графического интерфейса:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### Извлечение ценных активов и манифест
- `resources/AndroidManifest.xml`: идентифицирует экспортированные действия, услуги, приемники вещания и фильтры намерений.
- `resources/res/values/strings.xml`: часто содержит жестко закодированные ключи API, URL-адреса серверов и секреты клиента OAuth.

---

## 🔨 Apktool: разборка и извлечение Smali

### Декомпиляция в Smali и ресурсы
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
Внутри `apk_workdir`:
- `smali/`: инструкция по сборке Dalvik.
- `res/`: декодированные ресурсы Android (макеты, строки, рисунки).
- `AndroidManifest.xml`: XML-манифест в виде обычного текста.

### Анализ и исправление кода Smali
Чтобы обойти проверку в приложении (например, `isPremium()` или `isRooted()`):
Откройте соответствующий файл `.smali` и найдите метод:
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
Изменение `const/4 v0, 0x1` на `0x0` заставляет метод всегда возвращать `false`.

### Обход конфигурации сетевой безопасности (Cleartext/Burp)
Чтобы захватить HTTPS-трафик из приложений Android 7+ (API 24+) в Burp Suite:
1. В `AndroidManifest.xml` проверьте тег `application`:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. В `res/xml/network_security_config.xml` разрешите сертификаты пользователей:
   ```xml
   <?xml version="1.0" encoding="utf-8"?>
   <network-security-config>
       <base-config>
           <trust-anchors>
               <certificates src="system" />
               <certificates src="user" />
           </trust-anchors>
       </base-config>
   </network-security-config>
   ```
---

## 🔄 Восстановление, выравнивание и подписание исправленного APK

Android строго требует, чтобы каждый установленный APK был выровнен по zip-архиву и имел криптографическую подпись.

### 1. Пересобрать с помощью Apktool
```bash
apktool b apk_workdir -o modified.apk
```
### 2. Создать сертификат хранилища ключей (однократно)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. Оптимизация Zipalign
Выровняйте несжатые данные по 4-байтовым границам для повышения эффективности отображения в памяти:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. Войдите с помощью Apksigner
Подпишитесь с помощью схемы подписи APK v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
Установите на подключенное Android-устройство:
```bash
adb install -r modified_signed.apk
```
---

## ⚡ Краткая справочная таблица

| Цель | Команда |
|-----------|---------|
| **Интерактивная декомпиляция графического интерфейса** | `jadx-gui <target.apk>` |
| **Экспорт Java через CLI** | `jadx -d out_dir <target.apk>` |
| **Режим деобфускации CLI** | `jadx --deobf -d out_dir <target.apk>` |
| **Дизассемблировать на Смали** | `apktool d <target.apk> -o workdir` |
| **Пересобрать APK** | `apktool b workdir -o patched.apk` |
| **Двоичный файл Zipalign (4 байта)** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **Подписать исправленный APK** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **Проверьте подпись APK** | `apksigner verify final.apk` |

---

## 🛡️ Соображения по защите и обфускация кода

1. **Минификация R8/ProGuard:** Удалите неиспользуемые классы и переименуйте идентификаторы в отдельные неописательные символы (`a.b.c`).
2. **DexGuard/коммерческое запутывание:** шифруйте строки, динамически скрывайте вызовы API и внедряйте встроенные средства самопроверки целостности (обнаружение несанкционированного доступа).

---

## ⚠️ Юридическая оговорка

> [!ВНИМАНИЕ]
> Jadx и Apktool предназначены исключительно для авторизованного исследования уязвимостей, обратного проектирования собственного программного обеспечения и защитного анализа. Декомпиляция проприетарного стороннего программного обеспечения без разрешения может привести к нарушению лицензионных соглашений и законов об авторских правах.