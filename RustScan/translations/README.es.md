<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Modern Port Scanner - Scan All 65k Ports in 3 Seconds into Nmap
```
<p align="centro">
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconocimiento y escaneo">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licencia GPLv3">
</p>

<p align="centro">
  <b>🌐 Escáner de puertos rápido, personalizable y automatizado diseñado para canalizar los puertos abiertos descubiertos directamente a los scripts de Nmap</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [Comparación de herramientas: RustScan vs Nmap vs Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [Instalación](#-instalación)
- [Uso básico y escaneo de host único] (#-uso-básico--escaneo-de-host-único)
- [Integración automatizada de Nmap] (#-integración-nmap-automatizada)
- [Ajuste de rendimiento (`ulimit` y tamaño de lote)](#-rendimiento-tuning-ulimit--batch-size)
- [Escaneo de rangos de puertos y subredes CIDR] (#-scanning-port-ranges--cidr-subnets)
- [Secuencias de comandos y formato de salida] (#-scripting--formato de salida)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

**RustScan** es un escáner de puertos moderno y de alta velocidad diseñado en **Rust**. Su filosofía central es simple: **escanear puertos increíblemente rápido, encontrar puertos abiertos en todo el espectro 1-65535 en 3 segundos e inmediatamente transferir esos puertos abiertos a Nmap para un servicio integral y escaneo de vulnerabilidades**.

En lugar de esperar entre 10 y 20 minutos para que `nmap -p-` complete sus barridos secuenciales de protocolo de enlace TCP, RustScan aprovecha el procesamiento por lotes de subprocesos múltiples asíncronos y el procesamiento por lotes de sockets del sistema operativo para identificar los puertos abiertos casi instantáneamente.

---

## ⚖️ Comparación de herramientas: RustScan vs Nmap vs Masscan

| Característica | Escaneo de óxido | Mapa N | Mascán |
|---------|----------|------|---------|
| **Objetivo principal** | Descubrimiento rápido -> Tubería Nmap | Servicio profundo y scripts NSE | Escaneo SYN de alta velocidad en todo Internet |
| **Velocidad de todos los puertos (1-65535)**| ⭐⭐⭐⭐⭐ (~3 segundos) | ⭐⭐ (5–20 minutos) | ⭐⭐⭐⭐⭐ (~2 segundos) |
| **Detección de versión del servicio**| A través de la tubería Nmap | Nativo (`-sV`) | ❌ Mínimo |
| **Motor de secuencias de comandos NSE** | A través de la tubería Nmap | Nativo (`-sC`, `--script`) | ❌ No |
| **Motor de paquetes** | Sockets TCP asíncronos | IP sin formato/pila TCP personalizada | Pila SYN asincrónica personalizada |
| **Facilidad de automatización** | Argumentos CLI nativos (`--`) | Amplias opciones CLI | Alta complejidad |

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux (.deb package)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Package Manager)
cargo install rustscan

# Docker Container
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```
---

## 🚀 Uso básico y escaneo de host único
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 Integración automatizada de Nmap

La característica más poderosa de RustScan es pasar puertos abiertos directamente a Nmap usando el separador final `--`:
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ Ajuste de rendimiento (`ulimit` y tamaño de lote)

RustScan se basa en los límites de los descriptores de archivos de su sistema operativo. De forma predeterminada, se ajusta automáticamente, pero el ajuste manual proporciona el máximo rendimiento:
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 Escaneo de rangos de puertos y subredes CIDR
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 Secuencias de comandos y formato de salida
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escanear todos los puertos de 65k** | `rustscan -a <IP>` |
| **Escanear todos los puertos + scripts Nmap**| `rustscan -a <IP> -- -sC -sV` |
| **Escanear subred CIDR** | `rustscan -a <SUBNET>/24` |
| **Rango de puertos de escaneo** | `rustscan -a <IP> -r 1-1000` |
| **Ajustar tamaño de lote y ulimit** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Modo silencioso (sin banner)** | `rustscan -q -a <IP>` |
| **Salida Greppable** | `rustscan -a <IP> --greppable` |
| **Exportar a archivo Nmap** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Alertas masivas de ruido e IDS:** Escanear 65k puertos en 3 segundos genera miles de paquetes TCP SYN instantáneamente. Activará inmediatamente la limitación de velocidad de Snort, Suricata y firewall.
2. **Inundación SYN y paquetes descartados:** En enlaces VPN inestables (por ejemplo, HackTheBox, TryHackMe), los tamaños de lote elevados (`-b 4000`) pueden provocar la pérdida de paquetes. Si los resultados parecen inconsistentes, reduzca el tamaño del lote a `-b 1000`.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia es únicamente para pruebas de penetración autorizadas e investigaciones educativas. Está estrictamente prohibido escanear redes sin autorización.