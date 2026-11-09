<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ sshuttle - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<p align="centro">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivotación y túneles">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Protocolos">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="Licencia LGPLv2.1">
</p>

<p align="centro">
  <b>🌐 Reenviar subredes internas completas y solicitudes DNS a través de una sesión SSH sin privilegios y sin acceso administrativo en el servidor remoto</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y filosofía de la "VPN del hombre pobre"] (#-descripción general--la-filosofía-vpn-del-pobre-hombre)
- [Cómo funciona sshuttle](#-cómo-funciona-sshuttle)
- [Comparación de herramientas: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Instalación](#-instalación)
- [Enrutamiento de subred básico](#-enrutamiento-de-subred-básico)
- [Reenvío de tráfico DNS (`--dns`)](#-dns-traffic-forwarding---dns)
- [Redirección de puerta de enlace completa (ruta predeterminada)](#-redirección-de-puerta-de-puerta-completa-ruta-predeterminada)
- [Exclusiones de subred (`-x`)](#-exclusiones de subred--x)
- [Opciones SSH y autenticación basada en claves (`-e`)](#-ssh-options--autenticación-basada-en-claves--e)
- [Ejecución en segundo plano del demonio (`-D`)](#-ejecución-en segundo plano-del-daemon--d)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y filosofía de la "VPN del pobre"

**sshuttle** es un servidor proxy transparente que funciona como una VPN liviana y sin configuración a través de una conexión SSH estándar.

En las típicas pruebas de penetración corporativas y evaluaciones del Red Team, el giro hacia las redes internas se ve obstaculizado por restricciones estrictas:
- ❌ Rara vez posee acceso `root` o `SYSTEM` en el host pivote o bastión SSH inicial.
- ❌ Las soluciones VPN tradicionales (OpenVPN, WireGuard) requieren derechos administrativos y dispositivos de red TUN/TAP virtuales en ambos puntos finales.
- ❌ Los proxies SOCKS estándar (como `ssh -D`) requieren la configuración de `proxychains` o ajustes de proxy de aplicación individuales para cada herramienta.

**sshuttle resuelve las tres limitaciones simultáneamente:**
- ✅ **No se requiere raíz remota:** El servidor pivote solo necesita una instalación estándar de Python (`python3`).
- ✅ **No se necesita TUN/TAP remoto:** Utiliza multiplexación de flujo TCP puro a través de la sesión SSH.
- ✅ **Enrutamiento transparente en todo el sistema:** Modifica `iptables` / `nftables` local en el cuadro del atacante, lo que permite que las herramientas (`nmap`, `curl`, `smbclient`, navegadores web) apunten a IP internas directamente sin envoltorios.

---

## ⚙️ Cómo funciona el servicio de traslado
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ Comparación de herramientas: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| Característica | `sshuttle` | CALCETINES (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|------------------|-------------|-----------|
| **Se requiere raíz remota** | ❌ No | ❌ No | ❌ No | ✅ Sí |
| **Se requiere raíz local** | ✅ Sí (iptables) | ❌ No | ✅ Sí (interfaz TUN) | ✅ Sí |
| **Enrutamiento transparente** | ✅ Sí | ❌ Requiere cadenas proxy | ✅ Sí (TUN nativo) | ✅ Sí |
| **Soporte de tráfico UDP** | ❌ Sólo DNS | ❌ No | ✅ Sí | ✅ UDP completo |
| **Requiere Python en Target**| ✅ Sí (Python 3) | ❌ No | ❌ No (Agente independiente)| ❌ No |
| **Escaneo SYN de Nmap (`-sS`)** | ❌ Conexión TCP (`-sT`)| ❌ Conexión TCP únicamente | ✅ Soporte de paquetes sin procesar | ✅ Paquetes crudos completos |

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 Enrutamiento de subred básico

Reenvíe una subred remota Clase C completa a través del host pivote SSH:
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
Una vez conectado, podrá comunicarse directamente con las máquinas internas:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 Reenvío de tráfico DNS (`--dns`)

Reenvíe consultas DNS corporativas internas (`.corp`, `.local`) al servidor DNS interno de la red remota:
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 Redirección de puerta de enlace completa (ruta predeterminada)

Enrute todo el tráfico saliente del atacante a través de la conexión SSH (comportamiento VPN completo):
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 Exclusiones de subred (`-x`)

Evite que se capturen subredes sensibles o de bucle invertido específicas:
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 Opciones SSH y autenticación basada en claves (`-e`)

Pase indicadores SSH personalizados (claves de identidad privadas, puertos SSH no estándar):
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 Ejecución en segundo plano del demonio (`-D`)

Ejecute sshuttle como demonio en segundo plano durante los compromisos en curso:
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Ruta de subred única** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Enrutar subred con DNS** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Enrutar varias subredes** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **Excluir IP/Subred** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Redireccionamiento total del tráfico** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Clave/puerto SSH personalizado** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Ejecutar en segundo plano** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Modificación del firewall:** sshuttle modifica las tablas del firewall local (`iptables` en Linux, `pf` en macOS). Si sshuttle falla de manera impura, use `iptables -F` o reinicie el servicio de firewall para eliminar las reglas de redirección obsoletas.
2. **Limitación solo de TCP:** No se admiten paquetes UDP que no sean DNS, solicitudes de eco ICMP (ping) ni escaneos SYN. Utilice siempre `nmap -sT -Pn -n` para escanear puertos a través de sshuttle.
3. **Huella de destino:** sshuttle carga automáticamente un pequeño script ensamblador de Python en el host pivot remoto durante la inicialización de la sesión y lo ejecuta a través de `python3`.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia está destinada únicamente a pruebas de penetración autorizadas y administración de sistemas. El enrutamiento y la interceptación no autorizados del tráfico de la red son ilegales.