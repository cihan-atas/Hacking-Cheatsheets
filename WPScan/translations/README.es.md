<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<p align="centro">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Seguridad web">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="API de WPVDB">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licencia GPLv3">
</p>

<p align="centro">
  <b>🌐 Escáner de vulnerabilidades de caja negra diseñado para detectar vulnerabilidades del núcleo, complementos, temas y usuarios de WordPress</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [Instalación](#-instalación)
- [Integración de token API (WPVDB)](#-api-token-integration-wpvdb)
- [Escaneo básico y enumeración de objetivos] (#-escaneo-básico--enumeración-de-objetivos)
- [Desglose de indicadores de enumeración (`-e`)](#-enumeration-flags-breakdown--e)
  - [Complementos vulnerables (`-e vp`)](#vulnerable-plugins--e-vp)
  - [Todos los complementos (`-e ap`)] (#todos los complementos--e-ap)
  - [Temas (`-e vt`, `-e at`)](#temas--e-vt--e-at)
  - [Nombres de usuario (`-e u`)](#nombres de usuario--e-u)
  - [Copias de seguridad confidenciales (`-e cb`, `-e dbe`)](#sensitive-backups--e-cb--e-dbe)
- [Modos de detección (`--plugins-detection`)](#-modos-detección---detección-complementos)
- [Fuerza bruta de contraseña (`wp-login.php` y XML-RPC)](#-contraseña-fuerza bruta-wp-loginphp--xml-rpc)
- [Sigilo y evasión WAF] (#-stealth--waf-evasión)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y endurecimiento](#-consideraciones-defensivas--endurecimiento)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

Dado que WordPress impulsa más del 40% de los 10 millones de sitios web principales, es el sistema de gestión de contenidos (CMS) más específico del mundo.

Escrito en Ruby, **WPScan** es el escáner de vulnerabilidades de caja negra estándar de la industria para instalaciones de WordPress. Cataloga versiones de núcleos activos, enumera complementos y temas instalados, extrae nombres de usuario administrativos registrados y los correlaciona con la **WPScan Vulnerability Database (WPVDB)** que contiene miles de CVE conocidos.

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 Integración de token API (WPVDB)

Para mostrar vulnerabilidades conocidas y puntuaciones CVE, WPScan requiere un token API gratuito de [wpscan.com](https://wpscan.com/):
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 Escaneo básico y enumeración de objetivos
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 Desglose de indicadores de enumeración (`-e`)

WPScan permite la enumeración de objetivos modulares utilizando el indicador `--enumerate` (o `-e`):

### Complementos vulnerables (`-e vp`)
Busca complementos que tengan vulnerabilidades de seguridad conocidas en la base de datos WPVDB:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### Todos los complementos (`-e ap`)
Sondeos para miles de complementos conocidos a través de la fuerza bruta del diccionario:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### Temas (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### Nombres de usuario (`-e u`)
Extrae los nombres de usuario de los autores registrados a través de los puntos finales de la API REST (`/wp-json/wp/v2/users`), los archivos del autor (`/?author=1`) y los mensajes de error de inicio de sesión:
```bash
wpscan --url https://target.local -e u1-10
```
### Copias de seguridad confidenciales (`-e cb`, `-e dbe`)
Busca `wp-config.php.bak` expuesto, volcados de bases de datos y archivos de respaldo:
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ Modos de detección (`--plugins-detection`)

Controla la profundidad con la que WPScan busca complementos:
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 Fuerza bruta de contraseñas (`wp-login.php` y XML-RPC)

Una vez que se enumeran los nombres de usuario válidos, inicie la fuerza bruta de credenciales dirigida:
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!NOTA]
> Si `xmlrpc.php` está habilitado en el servidor, WPScan aprovecha automáticamente los métodos de llamada múltiple XML-RPC para probar cientos de combinaciones de contraseñas por cada solicitud HTTP.

---

## 🥷 Sigilo y evasión WAF
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escaneo estándar** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Enumerar complementos vulnerables** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Enumerar todos los complementos** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Enumerar usuarios** | `wpscan --url <URL> -e u` |
| **Enumerar copias de seguridad y configuraciones** | `wpscan --url <URL> -e cb,dbe` |
| **Fuerza bruta de contraseña** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Agente de usuario aleatorio y sigiloso** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **Exportar a archivo JSON** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Consideraciones defensivas y endurecimiento

1. **Deshabilite XML-RPC:** Bloquee el acceso a `xmlrpc.php` a través de la configuración `.htaccess` o Nginx para neutralizar ataques de fuerza bruta multillamada de alta velocidad.
2. **Restringir el punto final de los usuarios de la API REST:** Deshabilite el acceso público a `/wp-json/wp/v2/users` para evitar una enumeración trivial de autores.
3. **Parche los complementos con regularidad:** Más del 90% de las vulnerabilidades de WordPress residen en complementos y temas de terceros en lugar del núcleo de WordPress.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia está destinada únicamente a pruebas de penetración autorizadas, evaluaciones de vulnerabilidad y auditoría de aplicaciones web. La fuerza bruta de contraseñas o sistemas de escaneo sin autorización explícita es ilegal.