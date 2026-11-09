<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 Jadx & Apktool - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
       ██╗ █████╗ ██████╗ ██╗  ██╗    ███████╗    █████╗ ██████╗ ██╗  ██╗████████╗ ██████╗  ██████╗ ██╗     
       ██║██╔══██╗██╔══██╗╚██╗██╔╝    ██╔════╝   ██╔══██╗██╔══██╗██║ ██╔╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║███████║██║  ██║ ╚███╔╝     ███████╗   ███████║██████╔╝█████╔╝    ██║   ██║   ██║██║   ██║██║     
  ██   ██║██╔══██║██║  ██║ ██╔██╗     ╚════██║   ██╔══██║██╔═══╝ ██╔═██╗    ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝██║  ██║██████╔╝██╔╝ ██╗    ███████║██╗██║  ██║██║     ██║  ██╗   ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝ ╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝    ╚══════╝╚═╝╚═╝  ╚═╝╚═╝     ╚═╝  ╚═╝   ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
           Android APK Decompilation, Smali Patching & Repackaging
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Jadx-DEX_to_Java-blue?style=for-the-badge" alt="Jadx">
  <img src="https://img.shields.io/badge/Apktool-Smali_Disassembler-orange?style=for-the-badge" alt="Apktool">
  <img src="https://img.shields.io/badge/Category-Mobile_Reverse_Engineering-green?style=for-the-badge" alt="Mobile RE">
  <img src="https://img.shields.io/badge/Platform-Android-purple?style=for-the-badge" alt="Android">
</p>

<p alinhar="centro">
  <b>🔍 Guia completo para engenharia reversa do Android: descompilando arquivos APK/DEX/AAR em código-fonte Java legível com Jadx e desmontando/corrigindo código Smali e reempacotando com Apktool</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Instalação](#-instalação)
- [Jadx: descompilando para fonte Java](#-jadx-decompiling-to-java-source)
  - [Modo GUI Jadx](#jadx-gui-mode)
  - [Descompilação em lote Jadx CLI](#jadx-cli-batch-decompilação)
  - [Extração de ativos e manifesto de alto valor](#extração de ativos de alto valor--manifesto)
- [Apktool: Desmontagem e extração de Smali](#-apktool-disassembly--smali-extraction)
  - [Descompilando para Smali e Recursos](#decompiling-to-smali--resources)
  - [Analisando e corrigindo código Smali](#analyzing--patching-smali-code)
  - [Ignorando configuração de segurança de rede (texto claro/Burp)](#bypassing-network-security-config-cleartextburp)
- [Reconstruindo, alinhando e assinando o APK corrigido](#-rebuilding-aligning--signing-the-patched-apk)
  - [1. Reconstruir com Apktool](#1-reconstruir com apktool)
  - [2. Gerar certificado de keystore](#2-generate-keystore-certificate)
  - [3. Otimização Zipalign](#3-otimização zipalign)
  - [4. Assine com Apksigner](#4-assine com apksigner)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e ofuscação de código](#-considerações defensivas--ofuscação de código)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

Os aplicativos Android (`.apk`) são essencialmente arquivos ZIP contendo bytecode Dalvik Executable compilado (`classes.dex`), recursos pré-compilados (`resources.arsc`), manifestos XML binários (`AndroidManifest.xml`) e bibliotecas C/C++ nativas (`lib/`).

Duas ferramentas complementares essenciais formam a espinha dorsal da análise do Android:
1. **Jadx (descompilador Dex para Java):** Converte bytecode Dalvik diretamente em código-fonte Java limpo e legível por humanos. Ele apresenta uma GUI rápida com referências cruzadas de código (xrefs), pesquisas de métodos e algoritmos de desofuscação.
2. **Apktool:** desmonta recursos APK em sua forma quase original e converte bytecode Dalvik em montagem intermediária **Smali**. Ele permite modificar a lógica do aplicativo, injetar ganchos Smali personalizados e reempacotar o APK modificado.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y jadx apktool apksigner zipalign

# macOS (Homebrew)
brew install jadx apktool android-commandlinetools

# Arch Linux
sudo pacman -S jadx apktool
```
---

## ☕ Jadx: descompilando para fonte Java

### Modo GUI Jadx
A maneira mais fácil de analisar um APK de forma interativa:
```bash
jadx-gui target.apk
```
*Atalhos de teclado no Jadx-GUI:*
- `Ctrl + Shift + F`: Pesquisa global de texto e classe.
- `R`: Encontre referências cruzadas (onde uma classe ou método é usado).
- `N`: Renomear variável/método (com refatoração automatizada).

### Descompilação em lote Jadx CLI
Exporte código-fonte, ativos e recursos Java para um diretório de saída sem iniciar a GUI:
```bash
# Decompile entire APK into src/ directory
jadx -d output_src target.apk

# Decompile without decoding resources (faster)
jadx -r -d output_src target.apk

# Enable automated deobfuscation of scrambled names (a, b, c -> p001_a)
jadx --deobf -d output_deobf target.apk
```
### Extraindo ativos e manifestos de alto valor
- `resources/AndroidManifest.xml`: Identifica atividades, serviços, receptores de transmissão e filtros de intenção exportados.
- `resources/res/values/strings.xml`: contém frequentemente chaves de API codificadas, URLs de servidor e segredos de cliente OAuth.

---

## 🔨 Apktool: desmontagem e extração de Smali

### Descompilando para Smali e Recursos
```bash
# Disassemble APK into a work folder
apktool d target.apk -o apk_workdir

# Keep broken resources without crashing
apktool d -r target.apk -o apk_workdir
```
Dentro do `apk_workdir`:
- `smali/`: instruções de montagem Dalvik.
- `res/`: Recursos Android decodificados (layouts, strings, drawables).
- `AndroidManifest.xml`: manifesto XML de texto simples.

### Analisando e corrigindo código Smali
Para ignorar uma verificação no aplicativo (por exemplo, `isPremium()` ou `isRooted()`):
Abra o arquivo `.smali` correspondente e localize o método:
```smali
.method public isRooted()Z
    .registers 2

    # Original verification logic...
    # Return false (0x0)
    const/4 v0, 0x0
    return v0
.end method
```
Alterar `const/4 v0, 0x1` para `0x0` força o método a sempre retornar `false`.

### Ignorando a configuração de segurança de rede (texto claro/Burp)
Para capturar o tráfego HTTPS de aplicativos Android 7+ (API 24+) no Burp Suite:
1. Em `AndroidManifest.xml`, verifique a tag `application`:
   ```xml
   android:networkSecurityConfig="@xml/network_security_config"
   ```
2. Em `res/xml/network_security_config.xml`, permita certificados de usuário:
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

## 🔄 Reconstruindo, alinhando e assinando o APK corrigido

O Android exige estritamente que cada APK instalado seja alinhado com zip e assinado criptograficamente.

### 1. Reconstruir com Apktool
```bash
apktool b apk_workdir -o modified.apk
```
### 2. Gerar certificado de keystore (único)
```bash
keytool -genkey -v -keystore my-release-key.jks -alias alias_name \
  -keyalg RSA -keysize 2048 -validity 10000 -storepass password123 -keypass password123 \
  -dname "CN=Security, OU=Testing, O=Audit, L=City, S=State, C=US"
```
### 3. Otimização Zipalign
Alinhe dados não compactados em limites de 4 bytes para eficiência mapeada na memória:
```bash
zipalign -p -f -v 4 modified.apk modified_aligned.apk
```
### 4. Assine com Apksigner
Assine usando o esquema de assinatura APK v2/v3:
```bash
apksigner sign --ks my-release-key.jks --ks-pass pass:password123 --out modified_signed.apk modified_aligned.apk

# Verify signature
apksigner verify modified_signed.apk
```
Instale no dispositivo Android conectado:
```bash
adb install -r modified_signed.apk
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Descompilação GUI interativa** | `jadx-gui <target.apk>` |
| **Exportação CLI Java** | `jadx -d out_dir <target.apk>` |
| **Modo de desofuscação CLI** | `jadx --deobf -d out_dir <target.apk>` |
| **Desmontar para Smali** | `apktool d <target.apk> -o workdir` |
| **Reconstruir APK** | `apktool b workdir -o patched.apk` |
| **Zipalign Binário (4 bytes)** | `zipalign -p -f -v 4 in.apk aligned.apk` |
| **Assinar APK corrigido** | `apksigner sign --ks key.jks --out final.apk aligned.apk` |
| **Verifique a assinatura do APK** | `apksigner verify final.apk` |

---

## 🛡️ Considerações defensivas e ofuscação de código

1. **Minificação R8/ProGuard:** Remova as classes não utilizadas e renomeie os identificadores em caracteres únicos não descritivos (`a.b.c`).
2. **DexGuard/Ofuscação Comercial:** Criptografe strings, oculte chamadas de API dinamicamente e injete autoverificações de integridade nativa (detecção de adulteração).

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Jadx e Apktool destinam-se estritamente à pesquisa autorizada de vulnerabilidades, engenharia reversa de software próprio e análise defensiva. A descompilação de software proprietário de terceiros sem autorização pode violar acordos de licenciamento e leis de direitos autorais.