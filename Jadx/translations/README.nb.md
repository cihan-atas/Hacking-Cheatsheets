<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx & Apktool - Komplett jukseark

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
  <b>> Komplett veiledning for Android omvendt utvikling: dekompilere APK/DEX/AAR-filer til lesbar Java-kildekode med Jadx, og demontering/lapping av Smali-kode og ompakking med Apktool</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og arkitektur](#-oversikt--arkitektur)
- [Installasjon](#-installasjon)
- [Jadx: Dekompilering til Java-kilde](#-jadx-dekompilering-til-java-kilde)
  - [Jadx GUI-modus](#jadx-gui-modus)
  - [Jadx CLI Batch Decompilation](#jadx-cli-batch-decompilation)
  – [Extracting High Value Assets & Manifest](#extracting-high-value-assets--manifest)
- [Apktool: Demontering og Smali-ekstraksjon](#-apktool-disassembly--smali-ekstraksjon)
  - [Dekompilering til Smali og ressurser](#dekompilering-til-smali--ressurser)
  - [Analyser og lapper Smali-kode](#analyse--lapper-smali-kode)
  - [Bypassing Network Security Config (Cleartext/Burp)](#bypassing-network-security-config-cleartextburp)
- [Rebuilding, aligning & signing the patched APK](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. Rebuild with Apktool](#1-rebuild-with-apktool)
  - [2. Generer nøkkellagersertifikat](#2-generer nøkkellagersertifikat)
  - [3. Zipalign Optimization](#3-zipalign-optimization)
  - [4. Sign with Apksigner](#4-sign-with-apksigner)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Code Obfuscation](#-defensive-considerations--code-obfuscation)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og arkitektur

Android-applikasjoner (`.apk`) er i hovedsak ZIP-arkiver som inneholder kompilert Dalvik Executable bytecode (`classes.dex`), forhåndskompilerte ressurser (`resources.arsc`), binære XML-manifester (`AndroidManifest.xml`) og native C/C++-biblioteker (`lib/`).

To viktige komplementære verktøy utgjør ryggraden i Android-analyse:
1. **Jadx (Dex til Java-dekompiler):** Konverterer Dalvik-bytekode direkte til ren, lesbar Java-kildekode. Den har en rask GUI med kodekryssreferanser (xrefs), metodesøk og deobfuskeringsalgoritmer.
2. **Apktool:** Demonterer APK-ressurser til sin nesten originale form og konverterer Dalvik-bytekode til **Smali** mellommontering. Det lar deg endre applikasjonslogikk, injisere tilpassede Smali-kroker og pakke om den modifiserte APK-en.

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx: Dekompilering til Java-kilde

### Jadx GUI-modus
Den enkleste måten å analysere en APK interaktivt på:
```bash
jadx-gui target.apk
```
*Tastatursnarveier i Jadx-GUI:*
- `Ctrl + Shift + F`: Globalt tekst- og klassesøk.
- `R`: Finn kryssreferanser (hvor en klasse eller metode brukes).
- `N`: Gi nytt navn til variabel / metode (med automatisert refactoring).

### Jadx CLI Batch Dekompilering
Eksporter Java-kildekode, eiendeler og ressurser til en utdatakatalog uten å starte GUI:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### Uttak av verdifulle eiendeler og manifest
- `resources/AndroidManifest.xml`: Identifiserer eksporterte aktiviteter, tjenester, kringkastingsmottakere og hensiktsfiltre.
- `resources/res/values/strings.xml`: Inneholder ofte hardkodede API-nøkler, server-URLer og OAuth-klienthemmeligheter.

---

## 🔨 Apktool: Demontering og liten utvinning

### Dekompilering til Smali og ressurser
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
Inne i `apk_workdir`:
- `smali/`: Dalvik monteringsanvisning.
- `res/`: Dekodede Android-ressurser (oppsett, strenger, tegnbare).
- `AndroidManifest.xml`: Vanlig tekst XML-manifest.

### Analysere og lappe Smali-kode
For å omgå en sjekk i appen (f.eks. `isPremium()` eller `isRooted()`):
Åpne den tilsvarende `.smali`-filen og finn metoden:
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
Endring av `const/4 v0, 0x1` til `0x0` tvinger metoden til alltid å returnere `false`.

### Omgå nettverkssikkerhetskonfigurasjon (klartekst/burp)
For å fange opp HTTPS-trafikk fra Android 7+ (API 24+)-apper i Burp Suite:
1. I `AndroidManifest.xml`, sjekk `application`-taggen:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. Tillat brukersertifikater i `res/xml/network_security_config.xml`:
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

## 🔄 Gjenoppbygge, justere og signere den patchede APK-en

Android krever strengt tatt at hver installerte APK-fil er zip-justert og signert kryptografisk.

### 1. Gjenoppbygg med Apktool
```bash
apktool b apk_workdir -o modified.apk
```
### 2. Generer nøkkellagersertifikat (engangs)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. Zipalign-optimalisering
Juster ukomprimerte data på 4-byte grenser for minnetilordnet effektivitet:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. Signer med Apksigner
Signer med APK Signature Scheme v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
Installer på tilkoblet Android-enhet:
```bash
adb install -r modified_signed.apk
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Interaktiv GUI-dekompilering** | `jadx-gui <target.apk>` |
| **CLI Java Export** | `jadx -d out_dir <target.apk>` |
| **CLI deobfuscation Mode** | `jadx --deobf -d out_dir <target.apk>` |
| **Demonter til Smali** | `apktool d <target.apk> -o workdir` |
| **Gjenoppbygg APK** | `apktool b workdir -o patched.apk` |
| **Zipalign binær (4-byte)** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **Sign patched APK** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **Bekreft APK-signatur** | `apksigner verify final.apk` |

---

## 🛡️ Defensive betraktninger og kodeforvirring

1. **R8 / ProGuard Minification:** Strip ubrukte klasser og gi nytt navn til identifikatorer til enkle ikke-beskrivende tegn (`a.b.c`).
2. **DexGuard / Commercial Obfuscation:** Krypter strenger, skjul API-anrop dynamisk, og injiser innfødte integritets-selvsjekker (sabotasjedeteksjon).

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Jadx og Apktool er ment for autorisert sårbarhetsforskning, omvendt utvikling av selveid programvare og defensiv analyse. Dekompilering av proprietær tredjepartsprogramvare uten autorisasjon kan være i strid med lisensavtaler og lover om opphavsrett.