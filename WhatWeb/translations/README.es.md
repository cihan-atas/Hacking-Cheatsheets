<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p align="centro">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconocimiento y escaneo">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Escáner web de próxima generación diseñado para identificar sistemas de gestión de contenidos (CMS), plataformas de blogs, bibliotecas de JavaScript y tecnologías de servidor</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y tecnología de huellas dactilares] (#-descripción general--tecnología-huellas dactilares)
- [Instalación](#-instalación)
- [Huellas dactilares básicas del sitio web] (#-huellas dactilares-del-sitio-web-básico)
- [Niveles de agresión explicados (`-a`)](#-niveles-de-agresión-explicados--a)
  - [Modo pasivo sigiloso (`-a 1`)](#modo-pasivo-sigiloso--a-1)
  - [Modo agresivo (`-a 3`)](#modo-agresivo--a-3)
  - [Modo pesado/bruto (`-a 4`)](#heavy--modo-bruto--a-4)
- [Escaneo de subredes y archivos de destino] (#-escaneo-subredes--archivos-destino)
- [Sistema de complementos y auditoría selectiva] (#-sistema-de-complementos--auditoría-selectiva)
- [Registro de salida y exportación de informes](#-output-logging--report-exporting)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y tecnología de huellas dactilares

Antes de lanzar exploits o fuzzers de fuerza bruta contra una aplicación web, es imperativo identificar su pila tecnológica exacta.

Creado por **Andrew Horton (aventurero urbano)**, **WhatWeb** reconoce más de 1.800 tecnologías web distintas:
- **Sistemas de gestión de contenidos (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Servidores web y proxy:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Frameworks backend:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Bibliotecas frontend:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Dispositivos integrados:** Cámaras web, enrutadores, impresoras, interfaces SCADA.

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 Huellas dactilares básicas en sitios web
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ Explicación de los niveles de agresión (`-a`)

WhatWeb equilibra el sigilo con la profundidad utilizando cuatro niveles de agresión:

### Modo pasivo sigiloso (`-a 1`)
Envía una única solicitud HTTP GET a la URL proporcionada y analiza los encabezados de respuesta, cookies y etiquetas HTML devueltos sin solicitar páginas adicionales:
```bash
whatweb -a 1 https://target.local
```
### Modo agresivo (`-a 3`)
Recomendado para pruebas de penetración. Sigue redirecciones y solicita archivos de complementos comunes (por ejemplo, `wp-login.php`, `robots.txt`, hashes de favicon):
```bash
whatweb -a 3 https://target.local
```
### Modo pesado/bruto (`-a 4`)
Intenta miles de conjeturas de complementos y URL de huellas dactilares conocidas:
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 Escaneo de subredes y archivos de destino
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 Sistema de complementos y auditoría selectiva

WhatWeb presenta una rica arquitectura de complementos:
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 Registro de salida y exportación de informes
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escaneo estándar** | `whatweb <URL>` |
| **Informe detallado detallado** | `whatweb -v <URL>` |
| **Escaneo pasivo sigiloso** | `whatweb -a 1 <URL>` |
| **Escaneo profundo agresivo** | `whatweb -a 3 <URL>` |
| **Escanear subred (CIDR)** | `whatweb <SUBNET>/24` |
| **Escanear objetivos desde archivo** | `whatweb -i targets.txt` |
| **Exportar a JSON** | `whatweb <URL> --log-json report.json` |
| **Filtrar por complementos específicos**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Enmascaramiento de encabezado del servidor:** Elimine o aleatorice los encabezados de respuesta HTTP `Server` y `X-Powered-By` en Apache/Nginx para anular la toma de huellas dactilares pasiva de los banners.
2. **Favicon Hash Masking:** WhatWeb utiliza hashes MD5 de `/favicon.ico` para identificar marcos web. Personalice los favicons del marco predeterminado.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> WhatWeb está destinado únicamente a reconocimiento autorizado, auditoría de seguridad e inventario de sistemas. Escanear redes o aplicaciones web no autorizadas sin permiso puede violar las leyes aplicables.