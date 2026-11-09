<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx e Apktool - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Jadx-DEX_to_Java-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Disassembler-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Category-Mobile_Reverse_Engineering-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</p>

<palign="centro">
  <b>🔍 Guida completa per il reverse engineering di Android: decompilazione di file APK/DEX/AAR in codice sorgente Java leggibile con Jadx e disassemblaggio/patch del codice Smali e riconfezionamento con Apktool</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Installazione](#-installazione)
- [Jadx: decompilazione in Java Source](#-jadx-decompiling-to-java-source)
  - [Modalità GUI Jadx](#jadx-gui-mode)
  - [Decompilazione batch Jadx CLI](#jadx-cli-batch-decompilazione)
  - [Estrazione di beni di alto valore e manifesto](#estrazione-di-alto-valore--manifesto)
- [Apktool: Disassemblaggio ed estrazione Smali](#-apktool-disassembly--smali-extraction)
  - [Decompilazione in Smali e risorse](#decompiling-to-smali--resources)
  - [Analisi e patch del codice SMALI in corso](#analyzing--patching-smali-code)
  - [Evitare la configurazione della sicurezza di rete (Cleartext/Burp)](#bypassing-network-security-config-cleartextburp)
- [Ricostruzione, allineamento e firma dell'APK con patch](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. Ricostruisci con Apktool](#1-ricostruisci-con-apktool)
  - [2. Genera certificato archivio chiavi](#2-genera-certificato-keystore)
  - [3. Ottimizzazione Zipalign](#3-ottimizzazione-zipalign)
  - [4. Firma con Apksigner](#4-firma-con-apksigner)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e offuscamento del codice](#-considerazioni-difensive--code-offuscamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

Le applicazioni Android (`.apk`) sono essenzialmente archivi ZIP contenenti bytecode Dalvik Executable compilato (`classes.dex`), risorse precompilate (`resources.arsc`), manifest XML binari (`AndroidManifest.xml`) e librerie C/C++ native (`lib/`).

Due strumenti complementari essenziali costituiscono la spina dorsale dell'analisi Android:
1. **Jadx (decompilatore da Dex a Java):** Converte il bytecode Dalvik direttamente in codice sorgente Java pulito e leggibile dall'uomo. È dotato di una GUI veloce con riferimenti incrociati di codice (xref), ricerche di metodi e algoritmi di deoffuscamento.
2. **Apktool:** disassembla le risorse APK nella loro forma quasi originale e converte il bytecode Dalvik nell'assemblaggio intermedio **Smali**. Consente di modificare la logica dell'applicazione, inserire hook Smali personalizzati e riconfezionare l'APK modificato.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx: decompilazione in codice sorgente Java

### Modalità GUI Jadx
Il modo più semplice per analizzare un APK in modo interattivo:
```bash
jadx-gui target.apk
```
*Scorciatoie da tastiera nella Jadx-GUI:*
- `Ctrl + Shift + F`: ricerca globale di testo e classe.
- `R`: trova riferimenti incrociati (dove viene utilizzata una classe o un metodo).
- `N`: rinomina variabile/metodo (con refactoring automatizzato).

### Decompilazione batch Jadx CLI
Esporta il codice sorgente, le risorse e le risorse Java in una directory di output senza avviare la GUI:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### Estrazione di asset di alto valore e manifest
- `resources/AndroidManifest.xml`: identifica le attività, i servizi, i ricevitori di trasmissione e i filtri degli intenti esportati.
- `resources/res/values/strings.xml`: contiene spesso chiavi API codificate, URL del server e segreti client OAuth.

---

## 🔨 Apktool: disassemblaggio ed estrazione piccoli

### Decompilazione in Smalli e Risorse
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
All'interno di `apk_workdir`:
- `smali/`: istruzioni di montaggio Dalvik.
- `res/`: risorse Android decodificate (layout, stringhe, drawable).
- `AndroidManifest.xml`: manifesto XML in testo semplice.

### Analisi e patch del codice Small
Per ignorare un controllo in-app (ad esempio `isPremium()` o `isRooted()`):
Apri il file `.smali` corrispondente e individua il metodo:
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
La modifica di `const/4 v0, 0x1` in `0x0` impone al metodo di restituire sempre `false`.

### Bypassare la configurazione della sicurezza di rete (testo non crittografato/Burp)
Per acquisire il traffico HTTPS dalle app Android 7+ (API 24+) in Burp Suite:
1. In `AndroidManifest.xml`, controlla il tag `application`:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. In `res/xml/network_security_config.xml`, consenti i certificati utente:
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

## 🔄 Ricostruzione, allineamento e firma dell'APK con patch

Android richiede rigorosamente che ogni APK installato sia allineato con zip e firmato crittograficamente.

### 1. Ricostruisci con Apktool
```bash
apktool b apk_workdir -o modified.apk
```
### 2. Genera certificato archivio chiavi (una tantum)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. Ottimizzazione Zipalign
Allinea i dati non compressi sui limiti di 4 byte per l'efficienza mappata in memoria:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. Firma con Apksigner
Accedi utilizzando lo schema di firma APK v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
Installa sul dispositivo Android connesso:
```bash
adb install -r modified_signed.apk
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Decompilazione GUI interattiva** | `jadx-gui <target.apk>` |
| **Esportazione CLI Java** | `jadx -d out_dir <target.apk>` |
| **Modalità deoffuscamento CLI** | `jadx --deobf -d out_dir <target.apk>` |
| **Smontare a Smalli** | `apktool d <target.apk> -o workdir` |
| **Ricostruisci APK** | `apktool b workdir -o patched.apk` |
| **Zipalign binario (4 byte)** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **Firma APK con patch** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **Verifica firma APK** | `apksigner verify final.apk` |

---

## 🛡️ Considerazioni difensive e offuscamento del codice

1. **Riduzione R8/ProGuard:** Elimina le classi inutilizzate e rinomina gli identificatori in singoli caratteri non descrittivi (`a.b.c`).
2. **DexGuard/Offuscamento commerciale:** Crittografa stringhe, nasconde dinamicamente le chiamate API e inserisce autocontrolli di integrità nativi (rilevamento di manomissioni).

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Jadx e Apktool sono destinati esclusivamente alla ricerca autorizzata delle vulnerabilità, al reverse engineering di software di proprietà e all'analisi difensiva. La decompilazione di software proprietario di terze parti senza autorizzazione può violare gli accordi di licenza e le leggi sul copyright.