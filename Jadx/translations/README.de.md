<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx & Apktool – Vollständiger Cheatsheet

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
<p align="center">
  <img src="https://img.shields.io/badge/Jadx-DEX_to_Java-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Disassembler-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Category-Mobile_Reverse_Engineering-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</p>

<p align="center">
  <b>🔍 Vollständiger Leitfaden für Android Reverse Engineering: Dekompilieren von APK/DEX/AAR-Dateien in lesbaren Java-Quellcode mit Jadx und Disassemblieren/Patchen von Smali-Code und Neupacken mit Apktool</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Installation](#-Installation)
- [Jadx: Dekompilierung zur Java-Quelle](#-jadx-decompiling-to-java-source)
  - [Jadx GUI-Modus](#jadx-gui-mode)
  - [Jadx CLI Batch-Dekompilierung](#jadx-cli-batch-decompilation)
  - [Extrahieren hochwertiger Vermögenswerte und Manifest](#extracting-high-value-assets--manifest)
- [Apktool: Disassembly & Smali-Extraktion](#-apktool-disassembly--smali-extraction)
  - [Nach Smali & Ressourcen dekompilieren](#decompiling-to-smali--resources)
  - [Smali-Code analysieren und patchen](#analyzing--patching-smali-code)
  - [Umgehen der Netzwerksicherheitskonfiguration (Klartext/Burp)](#bypassing-network-security-config-cleartextburp)
- [Erneutes Erstellen, Ausrichten und Signieren der gepatchten APK](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. Mit Apktool neu erstellen](#1-rebuild-with-apktool)
  - [2. Keystore-Zertifikat generieren](#2-generate-keystore-certificate)
  - [3. Zipalign-Optimierung](#3-zipalign-optimization)
  - [4. Mit Apksigner signieren](#4-sign-with-apksigner)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Code-Verschleierung](#-defensive-considerations--code-obfuscation)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

Android-Anwendungen (`.apk`) sind im Wesentlichen ZIP-Archive, die kompilierten Dalvik Executable-Bytecode (`classes.dex`), vorkompilierte Ressourcen (`resources.arsc`), binäre XML-Manifeste (`AndroidManifest.xml`) und native C/C++-Bibliotheken (`lib/`) enthalten.

Zwei wesentliche komplementäre Tools bilden das Rückgrat der Android-Analyse:
1. **Jadx (Dex-zu-Java-Dekompiler):** Konvertiert Dalvik-Bytecode direkt in sauberen, für Menschen lesbaren Java-Quellcode. Es verfügt über eine schnelle GUI mit Code-Querverweisen (XRefs), Methodensuchen und Entschleierungsalgorithmen.
2. **Apktool:** Zerlegt APK-Ressourcen in ihre nahezu ursprüngliche Form und konvertiert Dalvik-Bytecode in **Smali**-Zwischenassembly. Es ermöglicht das Ändern der Anwendungslogik, das Einfügen benutzerdefinierter Smali-Hooks und das Neupacken der geänderten APK.

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx: Dekompilierung in Java-Quelle

### Jadx GUI-Modus
Der einfachste Weg, eine APK interaktiv zu analysieren:
```bash
jadx-gui target.apk
```
*Tastaturkürzel in der Jadx-GUI:*
- `Ctrl + Shift + F`: Globale Text- und Klassensuche.
- `R`: Querverweise finden (wo eine Klasse oder Methode verwendet wird).
- `N`: Variable/Methode umbenennen (mit automatisiertem Refactoring).

### Jadx CLI-Batch-Dekompilierung
Exportieren Sie Java-Quellcode, Assets und Ressourcen in ein Ausgabeverzeichnis, ohne die GUI zu starten:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### Extrahieren hochwertiger Vermögenswerte und Manifestieren
- `resources/AndroidManifest.xml`: Identifiziert exportierte Aktivitäten, Dienste, Rundfunkempfänger und Absichtsfilter.
- `resources/res/values/strings.xml`: Enthält häufig hartcodierte API-Schlüssel, Server-URLs und OAuth-Client-Geheimnisse.

---

## 🔨 Apktool: Demontage und Smali-Extraktion

### Dekompilierung nach Smali & Resources
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
Innerhalb von `apk_workdir`:
- `smali/`: Dalvik-Montageanleitung.
- `res/`: Dekodierte Android-Ressourcen (Layouts, Strings, Drawables).
- `AndroidManifest.xml`: Nur-Text-XML-Manifest.

### Analysieren und Patchen von Smali-Code
Um eine In-App-Prüfung zu umgehen (z. B. `isPremium()` oder `isRooted()`):
Öffnen Sie die entsprechende `.smali`-Datei und suchen Sie die Methode:
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
Durch die Änderung von `const/4 v0, 0x1` in `0x0` wird die Methode gezwungen, immer `false` zurückzugeben.

### Umgehen der Netzwerksicherheitskonfiguration (Klartext/Burp)
So erfassen Sie HTTPS-Verkehr von Apps mit Android 7+ (API 24+) in Burp Suite:
1. Überprüfen Sie in `AndroidManifest.xml` das `application`-Tag:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. Erlauben Sie in `res/xml/network_security_config.xml` Benutzerzertifikate:
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

## 🔄 Die gepatchte APK neu erstellen, ausrichten und signieren

Für Android ist es unbedingt erforderlich, dass jedes installierte APK zip-ausgerichtet und kryptografisch signiert ist.

### 1. Mit Apktool neu erstellen
```bash
apktool b apk_workdir -o modified.apk
```
### 2. Keystore-Zertifikat generieren (einmalig)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. Zipalign-Optimierung
Richten Sie unkomprimierte Daten an 4-Byte-Grenzen aus, um die Effizienz der Speicherzuordnung zu gewährleisten:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. Mit Apksigner signieren
Signieren mit APK Signature Scheme v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
Auf angeschlossenem Android-Gerät installieren:
```bash
adb install -r modified_signed.apk
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Interaktive GUI-Dekompilierung** | `jadx-gui <target.apk>` |
| **CLI-Java-Export** | `jadx -d out_dir <target.apk>` |
| **CLI-Deobfuscation-Modus** | `jadx --deobf -d out_dir <target.apk>` |
| **Nach Smali zerlegen** | `apktool d <target.apk> -o workdir` |
| **APK neu erstellen** | `apktool b workdir -o patched.apk` |
| **Zipalign-Binärdatei (4 Byte)** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **Gepatchtes APK signieren** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **APK-Signatur überprüfen** | `apksigner verify final.apk` |

---

## 🛡️ Verteidigungsüberlegungen und Code-Verschleierung

1. **R8 / ProGuard-Minimierung:** Nicht verwendete Klassen entfernen und Bezeichner in einzelne, nicht beschreibende Zeichen umbenennen (`a.b.c`).
2. **DexGuard / Kommerzielle Verschleierung:** Zeichenfolgen verschlüsseln, API-Aufrufe dynamisch ausblenden und native Integritätsselbstprüfungen einbauen (Manipulationserkennung).

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Jadx und Apktool sind ausschließlich für autorisierte Schwachstellenforschung, Reverse Engineering eigener Software und defensive Analysen gedacht. Das unbefugte Dekompilieren proprietärer Software Dritter kann gegen Lizenzvereinbarungen und Urheberrechte verstoßen.