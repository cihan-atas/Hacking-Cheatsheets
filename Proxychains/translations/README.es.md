<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<p align="centro">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivotación y túneles">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protocolos">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Enrute las herramientas de red CLI estándar de Linux de forma dinámica a través de servidores proxy HTTP y SOCKS de múltiples saltos a través de LD_PRELOAD</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y cómo funciona Proxychains] (#-descripción general--cómo-funciona-proxychains)
- [Instalación](#-instalación)
- [Archivos de configuración y jerarquía de búsqueda](#-archivos-de-configuración--jerarquía-de-búsqueda)
- [Modos de encadenamiento explicados] (#-modos-de-encadenamiento-explicados)
  - [`dynamic_chain` (recomendado)](#dynamic_chain-recomendado)
  - [`strict_chain`](#cadena_estricta)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`] (#cadena_aleatoria)
- [Prevención de fugas de DNS (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Configuración de listas de proxy](#-configuring-proxy-lists)
- [Flujos de trabajo pivotantes](#-flujos de trabajo pivotantes)
  - [Proxy SSH Dynamic SOCKS (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Relé de calcetines de cincel y ligolo-ng] (#chisel--ligolo-ng-socks-relay)
- [Integración de herramientas y errores](#-integración-de-herramientas--errores)
  - [Nmap con Proxychains (Por qué falla `-sS`)](#nmap-with-proxychains-why--ss-fails)
  - [Herramientas de Active Directory (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y cómo funcionan Proxychains

**Proxychains** (bifurcación activa moderna: **Proxychains-NG** / `proxychains4`) permite ejecutar cualquier comando dinámico a través de proxies SOCKS4, SOCKS5 o HTTP(S) encadenados únicos o múltiples.

Funciona precargando una biblioteca de enlaces personalizada (`libproxychains4.so`) utilizando el mecanismo de enlace dinámico (`LD_PRELOAD`). Esto intercepta las llamadas API de socket (`connect()`, `gethostbyname()`) realizadas por las aplicaciones de destino y redirige su tráfico TCP a la cadena de proxy configurada de forma transparente.

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ Archivos de configuración y jerarquía de búsqueda

Proxychains busca su archivo de configuración en el siguiente estricto orden de prioridad:

1. Ruta establecida en la variable de entorno `PROXYCHAINS_CONF_FILE`.
2. Archivo pasado a través del indicador `-f` (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` en el directorio de trabajo actual.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (predeterminado del sistema).

---

## 🔗 Explicación de los modos de encadenamiento

Edite `/etc/proxychains4.conf` para elegir el algoritmo de enrutamiento de la cadena:

### `dynamic_chain` (recomendado)
Omite automáticamente los servidores proxy inactivos de la lista sin cancelar la conexión. Si el Proxy n.° 1 está activo, el Proxy n.° 2 está inactivo y el Proxy n.° 3 está activo, el tráfico se dirige a través de 1 -> 3.
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
Sigue estrictamente el orden de proxy configurado. Si algún proxy de la cadena falla o se agota el tiempo de espera, se rechaza toda la conexión.
```ini
strict_chain
```
### `round_robin_chain`
Recorre la lista de proxy secuencialmente para cada nueva solicitud de conexión.
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
Selecciona aleatoriamente servidores proxy de la lista hasta `chain_len`.

---

## 🛡️ Prevención de fugas de DNS (`proxy_dns`)

Una de las fallas de OPSEC más peligrosas durante el reconocimiento externo o el pivote interno es la filtración de consultas de DNS al ISP local o a la puerta de enlace de destino.

Asegúrese de que `proxy_dns` esté activo en `/etc/proxychains4.conf`:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 Configuración de listas de proxy

En la parte inferior de `/etc/proxychains4.conf`, debajo de `[ProxyList]`:
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 Flujos de trabajo pivotantes

### Proxy SOCKS dinámico SSH (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Relé SOCKS de cincel y ligolo-ng

Al ejecutar túneles cliente-servidor de Chisel:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ Integración de herramientas y errores

### Nmap con Proxychains (Por qué falla `-sS`)

> [!ADVERTENCIA]
> Los proxies SOCKS solo admiten sesiones TCP establecidas. **SYN Stealth Scans (`-sS`) fallará o se bloqueará** porque requieren manipulación de socket sin formato que no es compatible con SOCKS.
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Herramientas de Active Directory (NetExec, Evil-WinRM, Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Ejecutar comando a través de proxy** | `proxychains4 <command> [args]` |
| **Archivo de configuración personalizado** | `proxychains4 -f ./custom.conf <command>` |
| **Ejecución silenciosa (sin banner)** | `proxychains4 -q <command>` |
| **Objetivo interno de escaneo TCP** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Solicitud Web Interna** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Prueba interna de SQLMap** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Sonda interna NetExec** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Fugas de DNS:** Si se comenta `proxy_dns`, su sistema resolverá los nombres de host a través del DNS local, alertando a los equipos azules sobre los nombres de host específicos.
2. **Velocidad y latencia:** El encadenamiento de varios servidores proxy aumenta el tiempo de ida y vuelta (RTT). Establezca tiempos de espera razonables (`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Binarios estáticos:** Los binarios compilados estáticamente (por ejemplo, los binarios Go/Rust que no vinculan `libc.so`) omiten los enlaces `LD_PRELOAD` y no se enrutarán a través de Proxychains.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia está destinada únicamente a pruebas de penetración autorizadas, interacciones con el Equipo Rojo e investigaciones educativas. Está estrictamente prohibido enrutar el tráfico sin autorización.