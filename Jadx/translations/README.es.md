<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx y Apktool: hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
       ██╗ █████╗ ██████╗ ██╗  ██╗    ███████╗    █████╗ ██████╗ ██╗  ██╗████████╗ ██████╗  ██████╗ ██╗     
       ██║██╔══██╗██╔══██╗╚██╗██╔╝    ██╔════╝   ██╔══██╗██╔══██╗██║ ██╔╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║███████║██║  ██║ ╚███╔╝     ███████╗   ███████║██████╔╝█████╔╝    ██║   ██║   ██║██║   ██║██║     
  ██   ██║██╔══██║██║  ██║ ██╔██╗     ╚════██║   ██╔══██║██╔═══╝ ██╔═██╗    ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝██║  ██║██████╔╝██╔╝ ██╗    ███████║██╗██║  ██║██║     ██║  ██╗   ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝    ╚══════╝╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
           Android APK Decompilation, Smali Patching & Repackaging
```
<p align="centro">
  <img src="https://img.shields.io/badge/Jadx-DEX_to_Java-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Disassembler-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Category-Mobile_Reverse_Engineering-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</p>

<p align="centro">
  <b>🔍 Guía completa para ingeniería inversa de Android: descompilar archivos APK/DEX/AAR en código fuente Java legible con Jadx, y desensamblar/parchear código Smali y reempaquetar con Apktool</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [Instalación](#-instalación)
- [Jadx: Descompilar en fuente Java](#-jadx-decompilar-en-fuente-java)
  - [Modo GUI de Jadx] (#jadx-gui-mode)
  - [Descompilación por lotes de CLI de Jadx](#jadx-cli-batch-decompilation)
  - [Extracción de activos de alto valor y manifiesto](#extracción-de-activos-de-alto-valor--manifiesto)
- [Apktool: Desmontaje y extracción Smali](#-apktool-disassembly--smali-extraction)
  - [Descompilar en Smali y Recursos](#decompilar-en-smali--resources)
  - [Análisis y parcheo de código pequeño](#analyzing--patching-small-code)
  - [Omitir la configuración de seguridad de red (Cleartext/Burp)](#bypassing-network-security-config-cleartextburp)
- [Reconstrucción, alineación y firma del APK parcheado](#-rebuilding-aligning--signing-the-parcheado-apk)
  - [1. Reconstruir con Apktool](#1-reconstruir-con-apktool)
  - [2. Generar certificado de almacén de claves](#2-generar-certificado-de-almacén de claves)
  - [3. Optimización de Zipalign](#3-optimización-zipalign)
  - [4. Firmar con Apksigner](#4-sign-with-apksigner)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y ofuscación del código](#-consideraciones-defensivas--ofuscación-del-código)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

Las aplicaciones de Android (`.apk`) son esencialmente archivos ZIP que contienen código de bytes ejecutable Dalvik compilado (`classes.dex`), recursos precompilados (`resources.arsc`), manifiestos XML binarios (`AndroidManifest.xml`) y bibliotecas C/C++ nativas (`lib/`).

Dos herramientas complementarias esenciales forman la columna vertebral del análisis de Android:
1. **Jadx (descompilador de Dex a Java):** Convierte el código de bytes de Dalvik directamente en un código fuente Java limpio y legible por humanos. Cuenta con una GUI rápida con referencias cruzadas de código (refx), búsquedas de métodos y algoritmos de desofuscación.
2. **Apktool:** Desensambla los recursos APK en su forma casi original y convierte el código de bytes de Dalvik en un ensamblaje intermedio **Smali**. Permite modificar la lógica de la aplicación, inyectar ganchos Smali personalizados y volver a empaquetar el APK modificado.

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx: Descompilación en código fuente Java

### Modo GUI de Jadx
La forma más sencilla de analizar un APK de forma interactiva:
```bash
jadx-gui target.apk
```
*Atajos de teclado en Jadx-GUI:*
- `Ctrl + Shift + F`: Búsqueda global de texto y clases.
- `R`: Buscar referencias cruzadas (donde se utiliza una clase o método).
- `N`: Cambiar nombre de variable/método (con refactorización automatizada).

### Descompilación por lotes de CLI de Jadx
Exporte el código fuente, los activos y los recursos de Java a un directorio de salida sin iniciar la GUI:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### Extracción de activos de alto valor y manifiesto
- `resources/AndroidManifest.xml`: Identifica actividades exportadas, servicios, receptores de transmisión y filtros de intención.
- `resources/res/values/strings.xml`: con frecuencia contiene claves API codificadas, URL de servidor y secretos de cliente OAuth.

---

## 🔨 Apktool: Desmontaje y extracción de Smali

### Descompilando en Smali y Recursos
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
Dentro de `apk_workdir`:
- `smali/`: Instrucciones de montaje de Dalvik.
- `res/`: Recursos de Android decodificados (diseños, cadenas, elementos dibujables).
- `AndroidManifest.xml`: manifiesto XML en texto plano.

### Análisis y parcheo de código pequeño
Para omitir una verificación en la aplicación (por ejemplo, `isPremium()` o `isRooted()`):
Abra el archivo `.smali` correspondiente y busque el método:
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
Cambiar `const/4 v0, 0x1` a `0x0` obliga al método a devolver siempre `false`.

### Omitir la configuración de seguridad de la red (texto sin cifrar/eructar)
Para capturar el tráfico HTTPS de aplicaciones de Android 7+ (API 24+) en Burp Suite:
1. En `AndroidManifest.xml`, verifique la etiqueta `application`:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. En `res/xml/network_security_config.xml`, permita certificados de usuario:
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

## 🔄 Reconstrucción, alineación y firma del APK parcheado

Android requiere estrictamente que cada APK instalado esté alineado con zip y firmado criptográficamente.

### 1. Reconstruir con Apktool
```bash
apktool b apk_workdir -o modified.apk
```
### 2. Generar certificado de almacén de claves (una sola vez)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. Optimización de Zipalign
Alinee los datos sin comprimir en límites de 4 bytes para lograr eficiencia asignada en memoria:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. Firmar con Apksigner
Firme utilizando el esquema de firma APK v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
Instalar en un dispositivo Android conectado:
```bash
adb install -r modified_signed.apk
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Descompilación de GUI interactiva** | `jadx-gui <target.apk>` |
| **Exportación CLI Java** | `jadx -d out_dir <target.apk>` |
| **Modo de desofuscación CLI** | `jadx --deobf -d out_dir <target.apk>` |
| **Desmontar a Smalli** | `apktool d <target.apk> -o workdir` |
| **Reconstruir APK** | `apktool b workdir -o patched.apk` |
| **Zipalign binario (4 bytes)** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **Firmar APK parcheado** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **Verificar firma APK** | `apksigner verify final.apk` |

---

## 🛡️ Consideraciones defensivas y ofuscación del código

1. **R8 / Minificación ProGuard:** Elimina las clases no utilizadas y cambia el nombre de los identificadores a caracteres únicos no descriptivos (`a.b.c`).
2. **DexGuard/Ofuscación comercial:** Cifre cadenas, oculte llamadas API dinámicamente e inyecte autoverificaciones de integridad nativas (detección de manipulación).

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Jadx y Apktool están destinados estrictamente a la investigación de vulnerabilidades autorizada, la ingeniería inversa de software propio y el análisis defensivo. Descompilar software propietario de terceros sin autorización puede violar acuerdos de licencia y leyes de derechos de autor.