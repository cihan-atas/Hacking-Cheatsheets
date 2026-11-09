<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```
<p align="centro">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="CLI Network Sniffer">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Motor Wireshark">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="Disección PCAP">
</p>

<p align="centro">
  <b>🌐 Disección profunda de paquetes de red basada en terminales, análisis de flujo y extracción de campo con tecnología de Wireshark</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [Comparación de herramientas: tshark vs tcpdump vs Wireshark](#-comparación-de-herramientas)
- [Instalación y privilegios](#-instalación--privilegios)
- [Captura y reproducción básicas] (#-captura-básica--reproducción)
- [Filtrado de tráfico: captura frente a visualización] (#-filtrado-tráfico)
- [Extracción de campo (`-T fields`)](#-extracción de campo)
- [Pentest práctico y recetas forenses](#-pentest-practico--recetas-forenses)
  - [Credencial HTTP y búsqueda de URI] (#http-credential--uri-hunting)
  - [Extracción de consultas DNS y detección de túneles](#dns-query-extraction--tunneling-detection)
  - [Extracción TLS/HTTPS SNI](#tls--https-sni-extracción)
  - [Extracción Kerberos AS-REQ/AS-REP](#kerberos-as-req--as-rep-extraction)
  - [Seguimiento de autenticación SMB y NTLM] (#smb--ntlm-authentication-tracking)
  - [Auditoría de protocolo de texto sin cifrar (FTP, Telnet, SMTP)](#cleartext-protocol-auditing)
  - [Búsqueda de protocolo de enlace de 4 vías WPA / EAPOL] (#wpa--eapol-búsqueda de protocolo de enlace de 4 vías)
- [Seguimiento de transmisión y reensamblaje] (#-siguiente de transmisión--reensamblaje)
- [Estadísticas, conversaciones y puntos finales](#-estadísticas-conversaciones--puntos finales)
- [Descifrado de tráfico TLS/SSL](#-tls--ssl-traffic-decryption)
- [Formatos de exportación (JSON, CSV, Elasticsearch)](#-export-formats)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Introducción

**tshark** es la contraparte de terminal y línea de comandos de **Wireshark**. Se basa en la biblioteca de captura de paquetes (libpcap/Npcap) y el motor de disección de paquetes idénticos a los del Wireshark gráfico, lo que la convierte en una de las herramientas de análisis de red más poderosas disponibles en Linux, macOS y Windows.

A diferencia de las herramientas básicas de rastreo de paquetes que solo inspeccionan los encabezados de los protocolos, `tshark` presenta capacidades de inspección profunda de paquetes en miles de protocolos, sintaxis de filtro de visualización nativa de Wireshark (`-Y`), extracción robusta de campos de protocolo (`-T fields`) y formato de salida flexible para canalizaciones de automatización (`jq`, scripts de shell, Python).

### Características clave

| Característica | Descripción |
|---------|-------------|
| **Disección profunda** | Inspecciona datos de carga útil en miles de protocolos de aplicaciones |
| **Filtros Wireshark** | Soporte completo para la sintaxis de filtro de visualización enriquecida de Wireshark (`-Y`) |
| **Extracción de campo** | Extraiga campos de protocolo individuales directamente (`-T fields -e ...`) |
| **Seguimiento de transmisión** | Reensambla sesiones TCP/UDP completas en formatos ASCII, hexadecimal o sin formato |
| **Análisis forense automatizado**| Scriptable en Shell, Python o canalizado a `jq` a través de `-T json` |
| **Descifrado** | Admite el descifrado TLS en tiempo de ejecución con `SSLKEYLOGFILE` |

---

## ⚖️ Comparación de herramientas

| Característica | tiburón | tcpdump| Tiburón de alambre |
|---------|--------|---------|-----------|
| **Interfaz** | CLI (Terminal) | CLI (Terminal) | GUI (Gráfica) |
| **Uso de recursos** | Bajo/Moderado | Extremadamente minimalista | Superior (RAM y CPU) |
| **Remoto (SSH / Sin cabeza)** | Nativo sin cabeza / CLI | Tubería SSH nativa | Requiere X11 / Agente remoto |
| **Motor de disección** | Disectores Wireshark completos | Encabezados básicos | Disectores Wireshark completos |
| **Filtros de captura** | Sólo BPF (`-f`) | Sólo BPF (`-i`) | Sólo BPF |
| **Filtros de visualización** | ✅ Sintaxis completa de Wireshark (`-Y`) | ❌ Ninguno | ✅ Barra de filtro GUI completa |
| **Formatos de exportación** | PCAP, JSON, CSV, elástico, XML | PCAP, ASCII, hexadecimal | PCAP, CSV, XML, JSON |
| **Secuencias de comandos y automatización**| Shell, Python, jq | Shell / Awk / Sed | Complementos de Lua, GUI |

---

## 📥 Instalación y privilegios

### Linux (Debian/Ubuntu/Kali Linux)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### Permitir la captura de paquetes no raíz

Para evitar ejecutar `tshark` como `root` (lo que puede ser un riesgo de seguridad debido a la gran superficie del disector), agregue su usuario al grupo `wireshark`:
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (Homebrew) y Windows (chocolate)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 Captura y reproducción básicas

### Lista de interfaces disponibles
```bash
# List interfaces with numeric IDs
tshark -D
```
### Capturando paquetes en vivo
```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```
### Lectura de archivos PCAP
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 Filtrado de tráfico: captura frente a visualización

`tshark` emplea dos motores de filtrado distintos:

1. **Filtros de captura (`-f`):** Filtros de paquetes Berkeley (BPF) evaluados por el kernel antes de que los paquetes lleguen al espacio de usuario. De alto rendimiento, pero limitado a primitivas básicas de IP/puerto.
2. **Filtros de visualización (`-Y`):** Filtros de protocolo profundo evaluados por el motor de disección de Wireshark. Admite capas de aplicaciones, búsqueda de cadenas, expresiones regulares e indicadores de protocolo.
```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```
---

## 📊 Extracción de campo (`-T fields`)

El modo `-T fields` es la característica más poderosa de `tshark` para reconocimiento automatizado, secuencias de comandos y desafíos CTF.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### Indicadores de formato de extracción de claves

| Bandera | Descripción |
|------|-------------|
| `-T fields` | Cambia el modo de salida a extracción de campos personalizados |
| `-e <field>` | Nombra el campo de protocolo a extraer (se puede repetir) |
| `-E header=y` | Imprime los nombres de las columnas como fila de encabezado |
| `-E separator=,` | Establece el separador de campos (`,` para CSV, `\t` para TSV) |
| `-E occurrence=f` | Extrae solo la primera (`f`), la última (`l`) o todas las apariciones (`a`) |
| `-E quote=d` | Encierra valores entre comillas dobles |

### Hoja de referencia de campos del protocolo principal
```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```
---

## 🎯 Pentest práctico y recetas forenses

### Credencial HTTP y búsqueda de URI
```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```
### Extracción de consultas DNS y detección de túneles
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### Extracción TLS/HTTPS SNI

Incluso cuando las cargas HTTPS están cifradas, la **Indicación de nombre de servidor (SNI)** en el protocolo de enlace inicial de saludo del cliente TLS revela los nombres de dominio de destino:
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Extracción Kerberos AS-REQ / AS-REP
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### Seguimiento de autenticación SMB y NTLM
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### Auditoría de protocolo de texto sin cifrar
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### Búsqueda de protocolo de enlace de 4 vías WPA / EAPOL
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 Seguimiento y reensamblaje de la transmisión

`tshark` permite reensamblar flujos TCP y UDP completos sin necesidad de la GUI de Wireshark.
```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```
---

## 📈 Estadísticas, conversaciones y puntos finales
```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```
---

## 🔐 Descifrado de tráfico TLS / SSL

Si tiene claves secretas maestras previas (exportadas a través del navegador `SSLKEYLOGFILE` o recuperadas durante la zona de pruebas de malware):
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 Formatos de exportación
```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```
---

## ⚡ Tabla de referencia rápida

| Tarea | Comando |
|------|---------|
| **Capture tráfico en vivo** | `tshark -i eth0 -w output.pcap` |
| **Captura con filtro BPF** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **Leer PCAP sin DNS** | `tshark -r capture.pcap -n` |
| **Aplicar filtro de visualización** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **Extraer IP y puertos** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **Extraer consultas DNS** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **Extraer nombres de host TLS SNI**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **Extraer URL HTTP** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **Seguir flujo TCP** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Jerarquía de protocolo** | `tshark -r capture.pcap -q -z io,phs` |
| **Descifrar con Keylog** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Exportar a JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Consejos y mejores prácticas

1. **Utilice siempre `-n` para PCAP grandes:** La resolución de nombres DNS ralentiza drásticamente el análisis. Agregue `-n` para evitar la resolución de direcciones IP.
2. **Combine `-f` y `-Y`:** Utilice los filtros de captura `-f` durante la captura en vivo para reducir la caída de paquetes y los filtros de visualización `-Y` durante el análisis posterior para una inspección profunda del protocolo.
3. **Tubería con `jq`:** La canalización de `tshark -T json` a `jq` permite consultar campos de protocolo anidados con lógica y transformación booleanas completas.
4. **Búferes de anillo para rastreo 24 horas al día, 7 días a la semana:** Cuando ejecute capturas de red continuas en un host dinámico, siempre aplique límites de tamaño y archivos (`-b filesize:50000 -b files:10`) para evitar fallas en el disco de destino.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** La captura no autorizada de paquetes, la escucha clandestina del tráfico de la red y la interceptación de credenciales en redes sin un permiso explícito y documentado son ilegales y están sujetos a procesamiento penal. Utilice `tshark` estrictamente en sistemas y redes de su propiedad o que esté autorizado contractualmente a evaluar.