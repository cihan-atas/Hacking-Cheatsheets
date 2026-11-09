<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p align="centro">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Idioma C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconocimiento y escaneo">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Licencia AGPLv3">
</p>

<p align="centro">
  <b>🌐 Escáner de puertos TCP asíncrono de alto rendimiento capaz de escanear todo Internet IPv4 en menos de 5 minutos</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura asincrónica] (#-descripción general--la-arquitectura-asincrónica)
- [Comparación de herramientas: Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Instalación](#-instalación)
- [Uso básico y objetivos de escaneo] (#-uso-básico--objetivos-de-escaneo)
- [Control de velocidad de transmisión (`--rate`)](#-control-de-velocidad-de-transmisión---velocidad)
- [Especificaciones de puerto (TCP y UDP)](#-especificaciones-de-puerto-tcp--udp)
- [Captación de banners y sondeo de protocolo básico](#-captación-de-banners--sondeo-de-protocolo-básico)
- [Exclusiones de destino y listas negras (`--excludefile`)](#-exclusiones-destino--listas negras---excludefile)
- [Formatos de salida y canalización en Nmap](#-formatos-de-salida--canalización-en-nmap)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y límites de hardware](#-consideraciones-defensivas--límites-de-hardware)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura asincrónica

Creado por **Robert Graham**, **Masscan** es aclamado como el escáner de puertos más rápido que existe. Produce resultados similares a Nmap, pero funciona internamente como `scanrand`, `unicornscan` y `ZMap`, utilizando **transmisión asíncrona**.

A diferencia de Nmap, que utiliza la pila TCP del núcleo del sistema operativo y espera respuestas antes de sondear los puertos posteriores, Masscan incorpora su propia pila TCP/IP de espacio de usuario personalizada. Explota paquetes SYN sin procesar a velocidades que exceden **10 millones de paquetes por segundo** sin mantener tablas de estado de conexión.

---

## ⚖️ Comparación de herramientas: Masscan vs Nmap vs RustScan

| Característica | Mascán | Mapa N | Escaneo de óxido |
|---------|---------|------|----------|
| **Velocidad máxima de paquetes** | 10.000.000 paquetes/seg | ~1.000 paquetes/seg | Límite de sockets del sistema operativo |
| **Exploración completa de Internet (/0)**| ✅ Menos de 5 minutos | ❌ Días/Semanas | ❌ No diseñado para /0 |
| **Implementación de la pila TCP**| Espacio de usuario personalizado Raw SYN| Núcleo del sistema operativo/sockets sin formato| Enchufes TCP asíncronos de Tokio |
| **Versión profunda y scripts NSE**| ❌ Estandartes Primitivos | ✅ Más de 600 guiones NSE | ✅ A través de la transferencia de Nmap |
| **Seguimiento del estado** | Apátrida (Blackrock PRNG) | Grupo de conexiones con estado | Tareas asíncronas con estado |
| **Alcance objetivo ideal** | Bloques clase A/B (/8, /16)| Destinos únicos y subredes /24 | Máquinas individuales y subredes /24 |

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 Uso básico y objetivos de escaneo
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!IMPORTANTE]
> Debido a que Masscan genera paquetes sin procesar a través de `libpcap`, **debe ejecutarse con privilegios de root/sudo**.

---

## ⚡ Control de velocidad de transmisión (`--rate`)

El parámetro `--rate` dicta cuántos paquetes por segundo transmite Masscan. Calibrar esto es fundamental para evitar saturar los equipos de red:
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 Especificaciones de puertos (TCP y UDP)

Masscan admite listas de puertos TCP y UDP simultáneamente:
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 Captación de banners y sondeo de protocolo básico

Si bien Masscan es principalmente un escáner SYN, puede completar apretones de manos de tres vías para capturar pancartas de aplicaciones:
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 Exclusiones de objetivos y listas negras (`--excludefile`)

Al realizar pruebas de penetración de amplio alcance, excluya siempre la infraestructura crítica y las direcciones prohibidas:
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 Formatos de salida y canalización en Nmap

Guarde los hosts descubiertos en formatos greppable o JSON, luego introdúzcalos en Nmap para una evaluación completa de la vulnerabilidad:
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escanear un solo host (todos los puertos)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Escanear subred CIDR en busca de puerto** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Escanear con captura de banner** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **Escanear puertos UDP** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **Excluir direcciones IP** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Salida a JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Salida a lista simple** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Consideraciones defensivas y límites de hardware

1. **Caída del enrutador del consumidor:** Configurar `--rate 100000` en un enrutador doméstico DSL/cable o Wi-Fi estándar abrumará su tabla de estado de seguimiento de conexión NAT y bloqueará el enrutador. Mantenga `--rate` por debajo de `1000` en conexiones residenciales.
2. **Consumo de ancho de banda:** La transmisión a 10 millones de paquetes por segundo consume aproximadamente 5,4 Gbps de ancho de banda de la red.
3. **Rechazo de firewall local (RST):** Debido a que Masscan utiliza una pila TCP de espacio de usuario personalizada, su kernel de Linux no conoce las conexiones abiertas y puede enviar paquetes TCP RST automáticos a los destinos. Para evitar esto durante la captura de banners:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Masscan es una utilidad de reconocimiento de red excepcionalmente poderosa. Escanear redes no autorizadas, rangos de IP gubernamentales o subredes públicas de Internet puede violar las leyes de telecomunicaciones y ciberseguridad. Verifique siempre la autorización escrita antes de iniciar los análisis.