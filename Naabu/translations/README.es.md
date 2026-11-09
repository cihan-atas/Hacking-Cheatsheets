<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Naabu - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```
<p align="centro">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconocimiento y escaneo">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Ir">
</p>

<p align="centro">
  <b>⚡ Escáner de puertos rápido y confiable centrado en la simplicidad y la conexión perfecta con herramientas ProjectDiscovery (httpx, nuclei)</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y funciones clave] (#-descripción general--características clave)
- [Instalación](#-instalación)
- [Escaneo de puertos básico] (#-escaneo-de-puertos-básico)
  - [Escaneo de host único] (# escaneo de host único)
  - [Puertos principales y rango de puertos completo](#puertos-top--rango-de-puertos completo)
- [Modos de escaneo: SYN (Raw Socket) vs CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [Limitación de velocidad y ajuste de rendimiento] (#-limitación de velocidad--ajuste de rendimiento)
- [Escaneo de múltiples objetivos y bloques CIDR] (#-escaneo de múltiples objetivos--bloques-cidr)
- [Flujos de trabajo de canalización sin interrupciones] (#-flujos de trabajo de canalización sin interrupciones)
  - [Naabu + Httpx + Núcleos](#naabu--httpx--núcleos)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Integración de Nmap (`-nmap`)](#-nmap-integration--nmap)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y características clave

Desarrollado por **ProjectDiscovery**, **Naabu** es un escáner de puertos diseñado para ser rápido, preciso y optimizado para su integración en procesos de automatización de reconocimiento modernos:
- **Escaneos SYN & CONNECT:** Retorno automático a TCP CONNECT cuando no existen privilegios de socket sin formato (`libpcap`/root).
- **ProjectDiscovery Ecosystem:** Arquitectura de tubería nativa que permite tuberías sin interrupciones desde `subfinder` a `naabu`, luego a `httpx` y `nuclei`.
- **Motor Nmap integrado:** Capacidad para activar automáticamente el servicio Nmap activo y el escaneo de versiones en puertos abiertos descubiertos.
- **Exclusión de CDN:** Omite automáticamente los nodos perimetrales de CDN en la nube (Cloudflare, Akamai, CloudFront) para evitar el desperdicio de ancho de banda y falsos positivos.

---

## 📥 Instalación
```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```
> [!NOTA]
> En Linux, otorgar `CAP_NET_RAW` permite ejecutar escaneos SYN sin root:
> `sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Escaneo de puertos básico

### Escaneo de host único
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### Puertos principales y rango completo de puertos
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ Modos de escaneo: SYN (Raw Socket) vs CONNECT

- **Escaneo SYN (se requiere `-s syn`/root):** Envía paquetes TCP SYN sin procesar sin finalizar el protocolo de enlace de tres vías (escaneo medio abierto). Mucho más rápido y menos ruidoso en los registros de aplicaciones.
- **CONNECT Scan (`-s conn`):** Utiliza la llamada de socket estándar OS `connect()`. Opera sin privilegios de root y penetra en entornos virtuales restrictivos.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ Limitación de velocidad y ajuste de rendimiento
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 Escaneo de múltiples objetivos y bloques CIDR
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 Flujos de trabajo de canalización sin interrupciones

Naabu brilla cuando se combina con otras herramientas de ProjectDiscovery:

### Subbuscador + Naabu
Enumere los subdominios y descubra inmediatamente cuáles tienen puertos web/SSH abiertos:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Naabu + Httpx + Núcleos
El canal canónico de reconocimiento de recompensas por errores ofensivos:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Integración de Nmap (`-nmap`)

Pase los puertos abiertos descubiertos directamente a Nmap para la detección de servicios (`-sV`) y el escaneo de scripts (`-sC`):
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escaneo predeterminado (Top 100)** | `naabu -host <TARGET>` |
| **Escaneo de rango completo (1-65535)**| `naabu -host <TARGET> -p -` |
| **1000 puertos principales** | `naabu -host <TARGET> -top-ports 1000` |
| **Escaneo SYN (Rápido, Sigiloso)**| `sudo naabu -host <TARGET> -s syn` |
| **Excluir IP perimetrales de CDN** | `naabu -host <TARGET> -exclude-cdn` |
| **Salida a JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Tubería a HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Activar escaneo del servicio Nmap**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Consideraciones defensivas y detección

1. **Reglas de Firewall e IDS:** Los barridos SYN de alta velocidad generados por Naabu producen altas proporciones TCP RST/SYN fácilmente detectadas por las reglas de Snort/Suricata.
2. **Limitación de velocidad de red:** Aplique una limitación de velocidad de conexión entrante (`iptables -m limit --limit 50/s`) para ralentizar e interrumpir el escaneo automatizado.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Naabu está desarrollado estrictamente para evaluaciones de seguridad autorizadas, pruebas de penetración y descubrimiento de activos defensivos. Está estrictamente prohibido escanear redes sin permiso explícito.