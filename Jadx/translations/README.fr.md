<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx & Apktool - Aide-mémoire complet

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
  <b>🔍 Guide complet pour l'ingénierie inverse Android : décompilation des fichiers APK/DEX/AAR en code source Java lisible avec Jadx, désassemblage/correction du code Smali et reconditionnement avec Apktool</b>
</p>

---

## 📋 Table des matières

- [Vue d'ensemble et architecture](#-overview--architecture)
- [Installation](#-installation)
- [Jadx : Décompilation vers la source Java](#-jadx-decompiling-to-java-source)
  - [Mode GUI Jadx](#jadx-gui-mode)
  - [Décompilation par lots Jadx CLI](#jadx-cli-batch-decompilation)
  - [Extraction d'actifs et de manifestes de grande valeur](#extracting-high-value-assets--manifest)
- [Apktool : Démontage et extraction de Smali](#-apktool-disassembly--smali-extraction)
  - [Décompilation vers Smali & Ressources](#decompiling-to-smali--resources)
  - [Analyse et correction du code Smali](#analyzing--patching-smali-code)
  - [Contourner la configuration de sécurité réseau (Cleartext/Burp)](#bypassing-network-security-config-cleartextburp)
- [Reconstruire, aligner et signer l'APK corrigé](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. Reconstruire avec Apktool](#1-rebuild-with-apktool)
  - [2. Générer un certificat Keystore](#2-generate-keystore-certificate)
  - [3. Optimisation Zipalign](#3-zipalign-optimisation)
  - [4. Signer avec Apksigner](#4-sign-with-apksigner)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et obscurcissement du code](#-considérations-defensives--obscurcissement du code)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture

Les applications Android (`.apk`) sont essentiellement des archives ZIP contenant le bytecode Dalvik Executable compilé (`classes.dex`), des ressources précompilées (`resources.arsc`), des manifestes XML binaires (`AndroidManifest.xml`) et des bibliothèques C/C++ natives (`lib/`).

Deux outils complémentaires essentiels constituent l’épine dorsale de l’analyse Android :
1. **Jadx (décompilateur Dex vers Java) :** Convertit le bytecode Dalvik directement en code source Java propre et lisible par l'homme. Il dispose d'une interface graphique rapide avec des références croisées de code (xréfs), des recherches de méthodes et des algorithmes de désobscurcissement.
2. **Apktool :** désassemble les ressources APK dans leur forme presque originale et convertit le bytecode Dalvik en assemblage intermédiaire **Smali**. Il permet de modifier la logique de l'application, d'injecter des hooks Smali personnalisés et de reconditionner l'APK modifié.

---

## 📥Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx : Décompilation vers la source Java

### Mode interface graphique Jadx
Le moyen le plus simple d’analyser un APK de manière interactive :
```bash
jadx-gui target.apk
```
*Raccourcis clavier dans Jadx-GUI :*
- `Ctrl + Shift + F` : Recherche globale de texte et de classe.
- `R` : Rechercher des références croisées (où une classe ou une méthode est utilisée).
- `N` : Renommer variable/méthode (avec refactoring automatisé).

### Décompilation par lots Jadx CLI
Exportez le code source, les ressources et les ressources Java dans un répertoire de sortie sans lancer l'interface graphique :
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### Extraction d'actifs et de manifestes de grande valeur
- `resources/AndroidManifest.xml` : identifie les activités, services, récepteurs de diffusion et filtres d'intention exportés.
- `resources/res/values/strings.xml` : contient fréquemment des clés API codées en dur, des URL de serveur et des secrets de client OAuth.

---

## 🔨 Apktool : Démontage et extraction de Smali

### Décompilation en Smali et ressources
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
À l'intérieur de `apk_workdir` :
- `smali/` : notice de montage Dalvik.
- `res/` : ressources Android décodées (layouts, chaînes, drawables).
- `AndroidManifest.xml` : manifeste XML en texte brut.

### Analyser et corriger le code Smali
Pour contourner une vérification dans l'application (par exemple `isPremium()` ou `isRooted()`) :
Ouvrez le fichier `.smali` correspondant et localisez la méthode :
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
La modification de `const/4 v0, 0x1` en `0x0` force la méthode à toujours renvoyer `false`.

### Contournement de la configuration de sécurité réseau (Cleartext/Burp)
Pour capturer le trafic HTTPS des applications Android 7+ (API 24+) dans Burp Suite :
1. Dans `AndroidManifest.xml`, vérifiez la balise `application` :
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. Dans `res/xml/network_security_config.xml`, autorisez les certificats utilisateur :
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

## 🔄 Reconstruire, aligner et signer l'APK corrigé

Android exige strictement que chaque APK installé soit aligné sur le zip et signé cryptographiquement.

### 1. Reconstruire avec Apktool
```bash
apktool b apk_workdir -o modified.apk
```
### 2. Générer un certificat Keystore (une seule fois)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. Optimisation Zipalign
Alignez les données non compressées sur des limites de 4 octets pour une efficacité de mappage en mémoire :
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. Signez avec Apksigner
Signez à l’aide du schéma de signature APK v2/v3 :
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
Installer sur un appareil Android connecté :
```bash
adb install -r modified_signed.apk
```
---

## ⚡ Tableau de référence rapide

| Objectif | Commande |
|-----------|---------|
| **Décompilation GUI interactive** | `jadx-gui <target.apk>` |
| **Exportation Java CLI** | `jadx -d out_dir <target.apk>` |
| **Mode de désobscurcissement CLI** | `jadx --deobf -d out_dir <target.apk>` |
| **Démonter à Smali** | `apktool d <target.apk> -o workdir` |
| **Reconstruire l'APK** | `apktool b workdir -o patched.apk` |
| **Binaire Zipalign (4 octets)** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **Signez l'APK corrigé** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **Vérifier la signature APK** | `apksigner verify final.apk` |

---

## 🛡️ Considérations défensives et obscurcissement du code

1. **R8 / ProGuard Minification :** Supprimez les classes inutilisées et renommez les identifiants en caractères uniques non descriptifs (`a.b.c`).
2. **DexGuard / Obfuscation commerciale :** Chiffrez les chaînes, masquez les appels d'API de manière dynamique et injectez des auto-vérifications d'intégrité natives (détection de falsification).

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Jadx et Apktool sont strictement destinés à la recherche de vulnérabilités autorisées, à l'ingénierie inverse de logiciels détenus et à l'analyse défensive. La décompilation de logiciels tiers propriétaires sans autorisation peut enfreindre les accords de licence et les lois sur les droits d'auteur.