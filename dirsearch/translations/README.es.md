<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```
<p align="centro">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Descubrimiento web">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Escáner de ruta web de línea de comandos rápido y repleto de funciones con manejo inteligente de extensiones, recursividad y rastreo</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [Comparación de herramientas](#-comparación-de-herramientas)
- [Instalación](#-instalación)
- [Uso básico y selección de objetivos] (#-uso-básico--selección-de-objetivos)
- [Gestión de extensiones y sufijos](#-extensión--gestión-de-sufijos)
- [Listas de palabras y diccionarios](#-listas de palabras--diccionarios)
- [Recursión y rastreo web](#-recursión--rastreo web)
- [Subprocesos, limitación de velocidad y retrasos](#-threading-rate-limiting--delays)
- [Filtrado y coincidencias de respuestas](#-filtrado--respuestas coincidentes)
- [Personalización de solicitud HTTP](#-http-request-customization)
  - [Encabezados personalizados y agentes de usuario] (#encabezados-personalizados--agentes-de-usuario)
  - [Cookies y autenticación](#cookies--autenticación)
  - [POST datos y métodos de solicitud] (#post-data--request-methods)
- [Proxies, Tor y Evasión](#-proxies-tor--evasión)
- [Formatos de salida e informes](#-formatos-de-salida--informes)
- [Recetas de Pentest y Bug Bounty del mundo real] (#-pentest-del-mundo-real--recetas-de-recompensas-de-errores)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Introducción

**dirsearch** (creado por Mauro Soria) es una de las herramientas de línea de comandos más populares y versátiles para descubrir archivos y directorios ocultos en servidores web. Escrito en Python, proporciona una flexibilidad inigualable con sus ricos argumentos de línea de comandos, salida de terminal coloreada, rastreo recursivo automático, expansión inteligente de extensiones con comodines y controles de evasión integrados.

Ya sea que esté realizando pruebas de penetración web, búsqueda de recompensas por errores o evaluaciones CTF internas, `dirsearch` está diseñado para descubrir interfaces administrativas ocultas, bases de datos de respaldo, puntos finales de API olvidados y credenciales confidenciales.

### Características clave

| Característica | Descripción |
|---------|-------------|
| **Multiproceso** | Motor de solicitudes asincrónicas y subprocesos de alto rendimiento |
| **Comodines de extensión** | Expanda `-e *` para probar automáticamente todas las extensiones de archivos web conocidas |
| **Rastreo integrado** | Sigue enlaces HTML/JS para generar colas de directorio recursivas |
| **Filtrado enriquecido** | Filtrar respuestas por código de estado, tamaño de bytes, recuento de palabras, recuento de líneas o expresión regular |
| **Repetición de solicitud sin procesar** | Cargue solicitudes HTTP completas de Burp Suite a través de `--raw` |
| **Múltiples formatos** | Genera informes en texto plano, JSON, CSV, Markdown y HTML |

---

## ⚖️ Comparación de herramientas

| Característica | búsqueda de directorios | ffuf | Feroxbuster | Gobuster | DIRB |
|---------|-----------|------|-------------|----------|------|
| **Idioma** | Pitón | Ir | Óxido | Ir | C |
| **Velocidad** | ⭐⭐⭐⭐ (Rápido) | ⭐⭐⭐⭐⭐ (Extremadamente rápido) | ⭐⭐⭐⭐⭐ (Increíblemente rápido) | ⭐⭐⭐⭐ (Rápido) | ⭐⭐ (Hilo bajo) |
| **Recursión predeterminada** | ✅ Automático (`-r`) | Opcional (`-recursion`) | ✅ Automático (`-d`) | Opcional (`-r`) | ✅ Automático |
| **Comodín de extensión (`*`)**| ✅ Nativo (`-e *`) | ❌ No | ❌ No | ❌ No | ❌ No |
| **Rastreador web integrado** | ✅ Sí (`--crawl`) | ❌ No | ✅ Sí (`--extract-links`) | ❌ No | ❌ No |
| **POST / Fuzzing de datos** | ✅ Sí (`--data`) | ✅ HTTP completo | ❌ Limitado | ❌ No | ❌ No |
| **Repetición de solicitud de eructar sin procesar**| ✅ Sí (`--raw`) | ✅ Sí (`-request`) | ❌ No | ❌ No | ❌ No |
| **Lista de palabras incorporada** | ✅ Curado (`db/dicc.txt`)| ❌ Ninguno | ❌ Ninguno | ❌ Ninguno | ✅ Lista pequeña |

---

## 📥 Instalación

### Usando pipx (recomendado para Kali y Linux moderno)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### Usando pip
```bash
# Install via standard pip
pip3 install dirsearch
```
### Usando Git Clone (directo desde la fuente)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Paquete Kali Linux y Docker
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 Uso básico y selección de objetivos

### Objetivo único
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### Múltiples objetivos
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### Escaneo de puertos y orientación CIDR
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 Gestión de extensiones y sufijos

`dirsearch` tiene el sistema de gestión de extensiones más potente entre todos los escáneres de directorios:
```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```
---

## 📚 Listas de palabras y diccionarios

`dirsearch` incluye una lista de palabras completa y probada en batalla en `db/dicc.txt`:
```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```
---

## 🔄 Recursividad y rastreo web

### Recursive Scanning
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### Rastreador web integrado
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ Subprocesamiento, limitación de velocidad y retrasos

Controle la agresividad del análisis para evitar que se activen firewalls de aplicaciones web (WAF) o de limitación de velocidad:
```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```
---

## 🎯 Filtrado y coincidencia de respuestas

### Inclusión y exclusión del código de estado
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### Filtrado por tamaño, palabras y contenido
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ Personalización de solicitudes HTTP

### Encabezados personalizados y agentes de usuario
```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```
### Cookies y autenticación
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### POST datos y métodos de solicitud
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ Proxies, Tor y Evasión
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 Formatos de salida e informes
```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```
---

## 🎯 Recetas de Pentest y Bug Bounty del mundo real

### 1. Reconocimiento web rápido de alcance completo
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. Búsqueda de copias de seguridad olvidadas y fugas de fuentes
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. Descubrimiento profundo de directorios recursivo
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. Evaluación del portal interno autenticado
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ Tabla de referencia rápida

| Tarea | Comando |
|------|---------|
| **Escaneo básico con extensiones** | `dirsearch -u https://target.com -e php,html,txt` |
| **Todas las extensiones comodín** | `dirsearch -u https://target.com -e *` |
| **Lista de palabras personalizada** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Escanear múltiples objetivos** | `dirsearch -l targets.txt -e php,aspx` |
| **Descubrimiento recursivo** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Rastrear enlaces de destino** | `dirsearch -u https://target.com -e php --crawl` |
| **Incluir códigos de estado** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Excluir códigos de estado** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Excluir tamaños de falsos positivos**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Autenticarse con cookies** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Ruta por Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Guardar informe en JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Consejos y mejores prácticas

1. **Utilice `-e *` con moderación en listas de palabras grandes:** La expansión de la extensión comodín multiplica cada palabra por más de 20 extensiones. Utilice `-e *` sólo con listas más pequeñas como `common.txt` o `dicc.txt`.
2. **Filtrar tamaños falsos positivos:** Los marcos web modernos devuelven páginas 404 personalizadas con HTTP 200. Inspeccione el tamaño de la respuesta predeterminada y agregue `--exclude-sizes <size>` para eliminar el ruido.
3. **Verifique siempre las rutas prohibidas 403:** En los entornos empresariales y de nube modernos, las respuestas HTTP 403 indican que el directorio o el punto final existe. Documente todos los 403 para pruebas de derivación de encabezado y recorrido de ruta.
4. **Aproveche los prefijos y sufijos para las copias de seguridad:** Los desarrolladores guardan con frecuencia copias de seguridad de archivos confidenciales (por ejemplo, `config.php.bak`, `old_database.sql`). Utilice `--prefixes` y `--suffixes` para descubrirlos automáticamente.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** El escaneo de puertos no autorizado, la fuerza bruta de directorios y la enumeración web en servidores sin un permiso explícito por escrito son ilegales. Utilice `dirsearch` estrictamente en compromisos legales de recompensas por errores, pruebas de penetración autorizadas y entornos de laboratorio CTF.