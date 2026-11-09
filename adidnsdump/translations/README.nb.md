<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<p align="center">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p align="center">
  <b>🌲 Oppregne og dump Active Directory Integrated DNS (ADIDNS) soner og poster over LDAP uten DNS sone transfers (AXFR)</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og teknisk bakgrunn](#-oversikt--teknisk-bakgrunn)
- [Installasjon](#-installasjon)
- [Autentiseringsmetoder](#-autentiseringsmetoder)
  - [Passordautentisering](#password-autentisering)
  - [Pass-the-Hash (NTLM)](#pass-the-hash-ntlm)
  - [Kerberos Ticket (ccache)](#kerberos-ticket-ccache)
- [Core Dumping Commands](#-core-dumping-commands)
  - [Standard Active Directory DNS Zone Dump](#standard-active-directory-dns-zone-dump)
  - [Dumping Tombstoned / Deleted Records](#dumping-tombstoned--deleted-records)
  – [Dumping Forest DNS & Domain DNS Zones](#dumping-forest-dns--domain-dns-zones)
  – [Targeting Specific DNS Zones](#targeting-specific-dns-zones)
- [Eksporter formater og etterbehandling](#-eksportformater--etterbehandling)
  - [CSV-utdata](#csv-utdata)
  - [Integrating with BloodHound & Nmap](#integrating-with-bloodhound--nmap)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og teknisk bakgrunn

I standard Windows Active Directory-miljøer er DNS vanligvis integrert i Active Directory (**Active Directory-integrert DNS / ADIDNS**). I disse konfigurasjonene lagres ikke DNS-poster i enkle flate tekstfiler, men som LDAP-objekter innenfor domenepartisjonen (som `DC=DomainDnsZones` og `DC=ForestDnsZones`).

Tradisjonelle DNS-soneoverføringer (`AXFR`) er nesten universelt deaktivert på domenekontrollere. Imidlertid har **enhver autentisert domenebruker** (selv lavprivilegerte kontoer eller kompromitterte tjenestekontoer) leserettigheter over LDAP-nodene som lagrer ADIDNS-poster som standard.

Utviklet av **Dirk-jan Mollema (@dirkjanm)**, kobler **`adidnsdump`** til domenekontrolleren via LDAP eller LDAPS, spør etter ADIDNS-partisjonene og trekker ut alle poster (A, AAAA, CNAME, SRV, TXT, MX og gravsteinsoppføringer), og skaper en nøyaktig datamaskin-, nettbasert inventar i domenet og på en nettstasjon.

---

## 📥 Installasjon
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 Autentiseringsmetoder

### Passordautentisering
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### Pass-the-Hash (NTLM)
Hvis du hentet ut NTLM-hasher via Mimikatz eller Secretsdump:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Kerberos-billett (ccache)
Bruke en eksisterende Kerberos TGT/TGS-billett lagret i `KRB5CCNAME`:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 Kjernedumping-kommandoer

### Standard Active Directory DNS Zone Dump
Dumper alle aktive DNS-poster fra standard DomainDnsZones-partisjonen:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Dette genererer en fil som heter `records.csv` i din nåværende arbeidskatalog.

### Dumping Tombstones / Deleted Records
Windows DNS merker slettede poster som "tombstoned" i flere dager før fysisk sletting. Disse postene avslører ofte utrangerte systemer, eldre servere eller testmiljøer:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### Dumping Forest DNS & Domain DNS Zones
Trekk ut poster fra DNS-partisjoner på skognivå:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### Målretting mot spesifikke DNS-soner
Hvis målet opererer flere DNS-soner eller underdomener med delt hjerne:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### Sikker LDAPS-kryptert kommunikasjon
Hindre legitimasjon fra å krysse nettverket i klartekst:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 Eksporter formater og etterbehandling

### CSV-utgang
Som standard gir `adidnsdump` ut `records.csv`. Feltene inkluderer:
- `zone`: DNS-sonen posten tilhører.
- `name`: Ta opp vertsnavn eller etikett.
- `type`: Record type (A, AAAA, CNAME, SRV, etc.).
- `value`: Mål-IP-adresse, vertsnavn eller streng.

### Integrering med BloodHound & Nmap
Trekk ut alle unike IP-adresser fra de dumpede postene for automatisert skanning:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Standard ADIDNS Dump** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Pass-the-Hash (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Kerberos Ticket Auth** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Dump Tombstoned Records**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Dump skogsoner** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Spesifiser målsone** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **Håndhev LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **LDAP-spørringsovervåking:** Aktiver avansert revisjonspolicy for katalogtjenestetilgang (hendelses-ID 4662) for å varsle om høyvolumspørringer mot `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **Begrens ADIDNS-tilgangskontrollister:** Begrens lesetillatelser på `MicrosoftDNS`-beholderen til domeneadministratorer eller spesifikke administrative roller hvis ikke-administratorer ikke krever intern DNS-introspeksjon.
3. **Honeytokens:** Plant attraktive DNS-poster (f.eks. `backup-vault.corp.local` eller `admin-portal.corp.local`) som peker til honeypot-IP-adresser.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> adidnsdump er ment for autoriserte sikkerhetsvurderinger, Active Directory-revisjoner og penetrasjonstesting innenfor tillatte rammer. Spørring av katalogtjenester på uautoriserte nettverk er ulovlig.