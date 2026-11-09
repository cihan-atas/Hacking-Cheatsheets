<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<p align="centro">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Idioma Python">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Seguridad web y API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Fuzzer HTTP altamente personalizable diseñado para descubrir parámetros ocultos, omisiones, encabezados y matrices de carga múltiple</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura de palabras clave] (#-descripción general--la-arquitectura-de-palabras clave)
- [Comparación entre Wfuzz y ffuf] (#-comparación-wfuzz-vs-ffuf)
- [Instalación](#-instalación)
- [Directorio básico y descubrimiento de archivos] (#-directorio-básico--descubrimiento-de-archivos)
- [Filtrado y ocultación de ruido (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtrado--ocultación-de-ruido---hc---hl---hw---hh)
- [Tipos de carga útil y codificadores dinámicos (`-z`)](#-tipos de carga útil--codificadores-dinámicos--z)
- [Parámetro y fuzzing de variables ocultas](#-parámetro--fuzzing-de-variables-ocultas)
- [Encabezados, cookies y autenticación](#-encabezados-cookies--autenticación)
- [Fuzing multivariable (`FUZZ` y `FUZ2Z`)](#-multi-variable-fuzzing-fuzz-and-fuz2z)
- [Enrutamiento a través de Burp Suite (`-p`)](#-enrutamiento-a través de Burp-suite--p)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura de palabras clave

Escrito en Python por **Xavi Méndez**, **Wfuzz** es una herramienta clásica y probada para evaluar aplicaciones web. Reemplaza los marcadores de posición designados (predeterminado: `FUZZ`) dentro de las URL, parámetros, encabezados y cuerpos POST con entradas de diccionario o cargas útiles generadas algorítmicamente.

Wfuzz se destaca por su incomparable **versatilidad de carga útil** (rangos, generadores de IP, cadenas de expresiones regulares, codificadores sobre la marcha como MD5 o Base64) y su mecánica de filtrado modular.

---

## ⚖️ Comparación de Wfuzz y ffuf

| Característica | Wfuzz | ffuf |
|---------|-------|------|
| **Idioma de implementación** | Pitón | Ir |
| **Velocidad bruta** | ⭐⭐⭐ (Bueno) | ⭐⭐⭐⭐⭐ (Increíblemente rápido) |
| **Codificadores de carga útil dinámica**| ✅ Nativo (`-z file,pass.txt,md5`)| ❌ Requiere scripts externos |
| **Combinaciones de cargas múltiples**| ✅ Nativo (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Compatible (`-w w1:W1 -w w2:W2`)|
| **Generadores de carga útil incorporados**| ✅ Rangos, IP, Hexadecimal, Diccionarios| ❌ Archivo sólo listas de palabras |
| **Uso de recursos** | RAM/CPU moderada | Huella de memoria baja |

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 Descubrimiento básico de directorios y archivos
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 Filtrado y ocultación de ruido (`--hc`, `--hl`, `--hw`, `--hh`)

Los servidores del mundo real devuelven respuestas incluso para páginas inexistentes (páginas 404 personalizadas o redirecciones). Wfuzz proporciona indicadores de filtrado detallados:
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 Tipos de carga útil y codificadores dinámicos (`-z`)

Wfuzz proporciona generadores integrados que eliminan la necesidad de archivos de texto generados previamente:
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 Fuzzing de parámetros y variables ocultas

Descubra los parámetros GET/POST no vinculados:
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 Encabezados, cookies y autenticación
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ Fuzzing multivariable (`FUZZ` y `FUZ2Z`)

Combine varias listas de palabras utilizando etiquetas de marcador de posición distintas:
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 Enrutamiento a través de Burp Suite (`-p`)

Inspeccione las cargas útiles y las respuestas dentro de Burp Suite:
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Difusor de directorio** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **Ocultar códigos de estado HTTP** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Ocultar por longitud de respuesta** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Parámetro GET de Fuzz** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Cuerpo de la publicación Fuzz** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Cargas útiles de rango numérico** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Codifica cargas útiles sobre la marcha**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Difuminación de varias palabras clave** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Ruta a través de Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Bloqueo de WAF:** La confusión de cientos de solicitudes por segundo activa la limitación de velocidad de Cloudflare, AWS WAF y ModSecurity. Utilice `-s <seconds>` para acelerar la velocidad de solicitud.
2. **Agotamiento de recursos:** Una gran confusión en consultas de bases de datos no indexadas puede causar condiciones de denegación de servicio (DoS) en servidores web heredados.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia es únicamente para pruebas de penetración autorizadas y auditorías de vulnerabilidad. Está estrictamente prohibido modificar aplicaciones no autorizadas.