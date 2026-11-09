<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<p align="centro">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licencia GPLv3">
</p>

<p align="centro">
  <b>🌐 Herramienta de dominio automatizada de Windows diseñada para rastrear recursos compartidos de SMB, localizar archivos de configuración jugosos, bases de datos KeePass y credenciales de texto sin formato</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y el problema de la búsqueda de acciones] (#-descripción general--el-problema-de-la-búsqueda-de-acciones)
- [Cómo funciona Snaffler](#-cómo-funciona-snaffler)
- [Opciones de instalación y ejecución](#-instalación--opciones-de-ejecución)
- [Triaje de Snaffler y niveles de gravedad] (#-snaffler-triage--severity-levels)
- [Flujos de trabajo de escaneo comunes](#-flujos-de-trabajo-de-escaneo-comunes)
  - [Rastreo de recursos compartidos en todo el dominio] (#rastreo de recursos compartidos en todo el dominio)
  - [Apuntar a hosts o recursos compartidos específicos](#targeting-hosts-or-shares-específicos)
  - [Excluyendo computadoras y raíces DFS] (#excluyendo-computadoras--dfs-roots)
- [Objetivos jugosos y reglas de coincidencia de archivos](#-objetivos-juicy--reglas-de-coincidencia de archivos)
- [Registro y análisis de salida] (#-registro-de-salida--análisis)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y OPSEC](#-consideraciones-defensivas--opsec)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y el problema de la búsqueda de acciones

En entornos de Active Directory medianos y grandes, los servidores de archivos y las estaciones de trabajo empresariales albergan miles de recursos compartidos de red abiertos para SMB. Los administradores de sistemas, desarrolladores y empleados con frecuencia dejan atrás:

- 🔑 **Bases de datos KeepPass (`.kdbx`):** A menudo contiene contraseñas maestras o claves maestras fácilmente descifrables.
- ⚙️ **Archivos de configuración (`web.config`, `appsettings.json`):** Contienen cadenas de conexión SQL codificadas, contraseñas de cuentas de servicio y claves API.
- 📜 **Scripts de implementación (`unattend.xml`, `.ps1`, `.bat`):** Credenciales administrativas codificadas.
- 🔏 **Certificados privados (`.pfx`, `.p12`, `.pem`, `.ppk`):** Utilizables para escalada de privilegios o movimiento lateral.

Es imposible explorar manualmente miles de recursos compartidos. **Snaffler**, desarrollado por **l33tb455**, automatiza todo este proceso: consulta Active Directory en busca de objetos de computadora, enumera recursos compartidos SMB legibles, recorre el árbol de directorios y evalúa archivos según conjuntos de reglas de expresiones regulares seleccionadas.

---

## ⚙️ Cómo funciona Snaffler
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 Opciones de instalación y ejecución

Snaffler se compila como un binario .NET independiente:
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### Ejecutando en Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### Ejecutando a través de Cobalt Strike / C2 (Ejecutar-Ensamblaje):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Clasificación y niveles de gravedad de Snaffler

Snaffler clasifica los hallazgos en categorías de urgencia codificadas por colores:

- 🔴 **`{Red}` / Negro:** ¡Hallazgo definitivo de alto valor! Contraseñas de texto sin formato, claves privadas (`.pfx`, `.id_rsa`), tokens de AWS y archivos de instalación desatendida.
- 🟡 **`{Yellow}`:** Hallazgo probable. Archivos de configuración con cadenas de contraseña, cadenas de conexión de bases de datos, instantáneas.
- 🟢 **`{Green}`:** Informativo/Interés. Nombres de archivos que coinciden con patrones administrativos (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Flujos de trabajo de escaneo comunes

### Rastreo de recursos compartidos en todo el dominio

Escanee todos los recursos compartidos accesibles en todo el dominio de Active Directory:
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### Dirigirse a hosts o recursos compartidos específicos

Cuando desee evitar escanear todo el dominio y centrarse en un servidor de archivos o controlador de dominio:
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### Excluyendo computadoras y raíces DFS

Evite escanear computadoras lentas o sensibles:
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 Objetivos jugosos y reglas de coincidencia de archivos

De forma predeterminada, Snaffler busca las siguientes extensiones críticas:

| Extensión/Archivo | Contenido de alto valor |
|------------------|--------------------|
| `.kdbx` / `.kdb` | Cajas fuertes del administrador de contraseñas KeePass |
| `web.config` / `app.config` | Contraseñas de bases de datos en texto sin cifrar |
| `unattend.xml` / `sysprep.inf` | Contraseñas de administrador de Windows |
| `.ppk` / `.pem` / `id_rsa` | Claves privadas SSH |
| `.pfx` / `.p12` | Certificados de Directorio Activo |
| `.rdp` / `.vnc` | Perfiles de conexión a escritorio remoto |
| `passwords.xlsx` / `creds.txt` | Listas de contraseñas de empleados |

---

## 📊 Registro y análisis de salida
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Escaneo de todo el dominio** | `Snaffler.exe -s -o out.log` |
| **Ordenador único de destino** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Participación específica objetivo** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Ajustar hilos de simultaneidad**| `Snaffler.exe -s -t 120 -o out.log` |
| **Excluir host del análisis** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Límite máximo de tamaño de archivo**| `Snaffler.exe -s -z 5000000` *(Límite de archivos de 5 MB)* |
| **Ejecutar mediante ensamblaje C2**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Consideraciones defensivas y OPSEC

1. **Picos masivos de conexión SMB:** Snaffler se conecta al puerto 445 en cada máquina del dominio en cuestión de minutos. Esto genera miles de ID de evento 4624 (tipo de inicio de sesión exitoso 3: red) y ID de evento 5140 (se accedió a un objeto compartido de red).
2. **Honeyfiles & Decoys:** Los defensores modernos de Active Directory implementan archivos compartidos Canary y señuelos `.kdbx` que activan alertas SOC instantáneas cuando se abren con herramientas como Snaffler.
3. **Límites de ejecución:** Establezca `-z` (tamaño máximo en bytes de archivo) para evitar fallas en los recursos compartidos de red al leer archivos de respaldo ISO o VHD de varios gigabytes.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Snaffler está diseñado exclusivamente para pruebas de penetración autorizadas, compromisos con equipos rojos y auditorías de seguridad internas. Escanear recursos compartidos de dominio sin autorización constituye una invasión ilegal de la red.