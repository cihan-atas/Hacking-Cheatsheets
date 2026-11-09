<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p align="centro">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursivo">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p align="centro">
  <b>⚡ Una herramienta de descubrimiento de contenido recursiva, rápida y sencilla escrita en Rust</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [Características clave] (#-características clave)
- [Comparación de herramientas](#-comparación-de-herramientas)
- [Instalación](#-instalación)
- [Sintaxis básica e inicio rápido](#-sintaxis-básica--inicio-rápido)
- [Opciones de comando principal] (#-opciones-de-comando-núcleo)
  - [Control de recursividad](#recursion-control)
  - [Simultaneidad y rendimiento](#concurrencia--rendimiento)
  - [Extensiones de archivo](#extensiones-de-archivo)
  - [Filtrado y coincidencia] (#filtrado--coincidencia)
  - [Funciones inteligentes (ajuste automático, extracción de enlaces, copias de seguridad)](#smart-features)
  - [Encabezados, cookies y autenticación](#encabezados-cookies--auth)
  - [Integración de Proxy y Burp Suite] (#proxy--burp-suite-integration)
- [Escaneo y canalizaciones de objetivos múltiples] (#-escaneo-de-objetivos múltiples--canalizaciones)
- [Flujos de trabajo del mundo real](#-flujos de trabajo-del-mundo-real)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción

**Feroxbuster** es un escáner de contenido web recursivo, multiproceso y de alto rendimiento escrito en Rust. Diseñado para ser rápido, simple e intuitivo, combina la velocidad de los fuzzers basados ​​en Go/Rust con el descubrimiento de directorios recursivo automático nativo.

Brilla durante las pruebas de penetración y los compromisos de recompensas por errores, donde se deben descubrir rápidamente árboles de sitios web profundos y directorios ocultos sin generar secuencias de comandos manuales complejas.

---

## 🌟 Características clave

- ⚡ **Increíblemente rápido:** Desarrollado por Rust asíncrono (`tokio` y `reqwest`), capaz de atender miles de solicitudes por segundo.
- 🔄 **Descubrimiento recursivo nativo:** Descubre automáticamente nuevos directorios y los escanea de forma recursiva hasta límites de profundidad configurables.
- 🧠 **Auto-Tune y Auto-Bail:** Acelera automáticamente la velocidad de escaneo si el servidor web de destino se ralentiza o se cancela si se producen errores de conexión excesivos.
- 🔗 **Extracción de enlaces en línea:** Analiza respuestas HTML y JavaScript para descubrir puntos finales ocultos y los devuelve a la cola de escaneo.
- 📦 **Búsqueda de copias de seguridad automatizada:** Agrega las variaciones `.bak`, `~`, `.old` y `.orig` automáticamente a los archivos descubiertos.
- 🎯 **Entrada canalizable:** Acepta sin problemas listas de objetivos de `stdin` (de herramientas como `subfinder`, `httpx` o `gau`).

---

## ⚖️ Comparación de herramientas

| Característica | Feroxbuster | ffuf | búsqueda de directorios | Gobuster | Dirb |
|---------|-------------|------|-----------|----------|------|
| **Idioma** | Óxido | Ir | Pitón | Ir | C |
| **Velocidad** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Recursión predeterminada** | ✅ Automático | ❌ Bandera manual | ✅ Automático (`-r`) | ❌ Ninguno | ✅ Automático |
| **Extracción de enlaces** | ✅ Sí (`--extract-links`) | ❌ No | ❌ No | ❌ No | ❌ No |
| **Ajuste automático de velocidad** | ✅ Sí (`--auto-tune`) | ❌ No | ❌ No | ❌ No | ❌ No |
| **POST Fuzzing** | ❌ No | ✅ Nativo | ✅ Nativo (`--data`) | ❌ No | ❌ No |
| **Stdin multiobjetivo** | ✅ Nativo | ❌ Limitado | ✅ Sí (`-l`) | ❌ No | ❌ No |
| **Detección de archivos de copia de seguridad**| ✅ Nativo (`--collect-backups`)| ❌ No | ✅ Nativo (`-e *`) | ❌ No | ❌ No |

---

## 📥 Instalación

### Kali Linux y Debian/Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### Uso de Cargo (Administrador de paquetes Rust)
```bash
cargo install feroxbuster
```
### macOS (cervecería casera)
```bash
brew install feroxbuster
```
### acoplador
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### Binario precompilado (versiones de GitHub)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 Sintaxis básica e inicio rápido
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### Ejemplos de escaneo rápido
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ Opciones de comando principal

### Control de recursividad
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### Simultaneidad y rendimiento
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### Extensiones de archivo
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### Filtrado y coincidencia
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### Funciones inteligentes
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### Encabezados, cookies y autenticación
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### Integración de Proxy y Burp Suite
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 Escaneo y canalizaciones de múltiples objetivos

Feroxbuster lee de forma nativa las URL de destino desde la entrada estándar (`stdin`), lo que lo hace ideal para la integración con canales de descubrimiento de activos:
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### Escaneando URL desde un archivo
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 Flujos de trabajo del mundo real

### Flujo de trabajo 1: Reconocimiento web completo de recompensas por errores

Alta concurrencia, ajuste automático de tasas, extracción de enlaces y detección de copias de seguridad:
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### Flujo de trabajo 2: Descubrimiento del portal interno de la empresa/Active Directory

Escaneo de una aplicación web interna a través de Burp Suite con autenticación y certificados autofirmados:
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### Flujo de trabajo 3: API rápida no recursiva y descubrimiento de terminales
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 Tabla de referencia rápida

| Bandera | Corto | Descripción | Ejemplo |
|------|-------|-------------|-----------------|
| `--url` | `-u` | URL de destino para escanear | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Ruta a la lista de palabras | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Número de subprocesos simultáneos por directorio | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Limitar el total de exploraciones simultáneas de directorios | `feroxbuster -L 3` |
| `--depth` | `-d` | Profundidad máxima de recursividad (predeterminado 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Deshabilitar el escaneo recursivo de directorios | `feroxbuster -n` |
| `--extensions` | `-x` | Extensiones de archivo para agregar | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Códigos de estado a incluir | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Códigos de estado para excluir | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Tamaños de respuesta para excluir | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Recuento de palabras a excluir | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Recuentos de líneas para excluir | `feroxbuster -N 8` |
| `--auto-tune` | Ninguno | Ajustar dinámicamente la velocidad según la latencia | `feroxbuster --auto-tune` |
| `--auto-bail` | Ninguno | Cancelar análisis en caso de errores excesivos de red | `feroxbuster --auto-bail` |
| `--extract-links`| Ninguno | Analizar HTML/JS para nuevos puntos finales | `feroxbuster --extract-links` |
| `--collect-backups` | Ninguno | Verificar variaciones de respaldo (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Enviar tráfico a través del proxy HTTP | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Ninguno | Acceso directo para `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | Ninguno | Leer URL desde la entrada estándar | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Ruta del archivo de salida | `feroxbuster -o results.txt` |
| `--json` | Ninguno | Formatear la salida como JSON | `feroxbuster --json -o out.json` |

---

## 💡 Consejos y mejores prácticas

1. **Utilice `--auto-tune` para objetivos remotos:**
   - En lugar de adivinar el número de subprocesos (`-t`), `--auto-tune` mide activamente los tiempos de respuesta objetivo y acelera dinámicamente, evitando la denegación accidental de servicio del servidor.
2. **Combine `--extract-links` y `--collect-backups`:**
   - Esta combinación es excepcionalmente letal en las evaluaciones de recompensas por errores: Feroxbuster extrae rutas JS ocultas, las escanea y comprueba inmediatamente si se dejaron copias de seguridad del desarrollador (`.bak`, `.swp`, `~`) en el servidor.
3. **Limitar escaneos en listas de palabras grandes con `-L`:**
   - Si utiliza un diccionario de más de 200.000 palabras, la recursividad sin restricciones puede iniciar docenas de hilos simultáneos. Utilice `-L 2` o `-L 3` para mantener la memoria y el ancho de banda dentro de límites operativos seguros.
4. **Utilice archivos estatales para reanudar auditorías grandes:**
   - Feroxbuster guarda periódicamente archivos de estado (`.state`). Si se interrumpe el escaneo, reanúdelo instantáneamente con `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** Feroxbuster está diseñado estrictamente para evaluaciones de seguridad autorizadas, competencias CTF y pruebas de penetración realizadas con permiso por escrito. El escaneo de objetivos no autorizados puede saturar la infraestructura de la red y está estrictamente prohibido por ley.

---

## 📚 Recursos

- [Repositorio Feroxbuster GitHub](https://github.com/epi052/feroxbuster)
- [Documentación oficial de Feroxbuster](https://epi052.github.io/feroxbuster-docs/)
- [Listas de descubrimiento de SecLists] (https://github.com/danielmiessler/SecLists)