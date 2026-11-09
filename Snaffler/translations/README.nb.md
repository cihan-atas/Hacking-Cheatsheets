<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<p align="center">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="License GPLv3">
</p>

<p align="center">
  <b>🌐 Automatisert Windows-domeneverktøy utviklet for å gjennomsøke SMB-andeler, finne saftige konfigurasjonsfiler, KeePass-databaser og rentekstlegitimasjon</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og delingsjaktproblemet](#-oversikt--aksjejaktproblemet)
- [How Snaffler Works](#-how-snaffler-works)
- [Installasjons- og utførelsesalternativer](#-installasjon--utførelsesalternativer)
– [Snaffler-triage og alvorlighetsnivåer](#-snaffler-triage--severity-levels)
- [Vanlige skannearbeidsflyter](#-vanlige skannearbeidsflyter)
  - [Domain-Wide Share Crawl](#domain-wide-share-crawl)
  – [Målretting mot spesifikke verter eller delinger](#targeting-specific-hosts-or-shares)
  - [Ekskluderer datamaskiner og DFS-røtter](#ekskluderer-datamaskiner--dfs-røtter)
- [Juicy Targets & File Match Rules](#-juicy-targets--file-match-rules)
- [Output Logging & Analysis](#-output-logging--analyse)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og andeljaktproblemet

I middels til store Active Directory-miljøer er bedriftsfilservere og arbeidsstasjoner vert for tusenvis av åpne SMB-nettverksdelinger. Systemadministratorer, utviklere og ansatte etterlater ofte:

- 🔑 **KeePass-databaser (`.kdbx`):** Inneholder ofte hovedpassord eller enkelt knekkelige hovednøkler.
- ⚙️ **Konfigurasjonsfiler (`web.config`, `appsettings.json`):** Inneholder hardkodede SQL-tilkoblingsstrenger, passord for tjenestekontoer og API-nøkler.
- 📜 **Distribusjonsskript (`unattend.xml`, `.ps1`, `.bat`):** Hardkodet administrativ legitimasjon.
- 🔏 **Private sertifikater (`.pfx`, `.p12`, `.pem`, `.ppk`):** Kan brukes for rettighetseskalering eller sideveis bevegelse.

Manuelt bla gjennom tusenvis av delinger er umulig. **Snaffler**, utviklet av **l33tb455**, automatiserer hele denne prosessen: den spør etter Active Directory for dataobjekter, teller opp lesbare SMB-andeler, går gjennom katalogtreet og evaluerer filer mot kurerte regelsett for regulært uttrykk.

---

## ⚙️ Hvordan Snaffler fungerer
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 Installasjons- og utførelsesalternativer

Snaffler er kompilert som en frittstående .NET-binærfil:
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### Kjører på Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### Kjører via Cobalt Strike / C2 (Execute-Assembly):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Snaffler Triage og alvorlighetsnivåer

Snaffler kategoriserer funn i fargekodede hastespann:

- 🔴 **`{Red}` / Svart:** Definitivt funn av høy verdi! Klartekstpassord, private nøkler (`.pfx`, `.id_rsa`), AWS-tokens, uovervåket installasjonsfiler.
- 🟡 **`{Yellow}`:** Sannsynlig funn. Konfigurasjonsfiler med passordstrenger, databasetilkoblingsstrenger, skyggekopier.
- **`{Green}`:** Informasjon / interesse. Filnavn som samsvarer med administrative mønstre (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Vanlige skannearbeidsflyter

### Delingsgjennomsøking for hele domenet

Skann alle tilgjengelige delinger på tvers av hele Active Directory-domenet:
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### Målretting mot spesifikke verter eller delinger

Når du vil unngå å skanne hele domenet og fokusere på en filserver eller domenekontroller:
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### Ekskluderer datamaskiner og DFS-røtter

Unngå å skanne trege eller sensitive datamaskiner:
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 Juicy Targets & File Match-regler

Som standard leter Snaffler etter følgende kritiske utvidelser:

| Utvidelse / fil | Innhold med høy verdi |
|----------------|------------------------|
| `.kdbx` / `.kdb` | KeePass passordbehandling safer |
| `web.config` / `app.config` | Klartekst databasepassord |
| `unattend.xml` / `sysprep.inf` | Windows-administratorpassord |
| `.ppk` / `.pem` / `id_rsa` | SSH private nøkler |
| `.pfx` / `.p12` | Active Directory-sertifikater |
| `.rdp` / `.vnc` | Tilkoblingsprofiler for eksternt skrivebord |
| `passwords.xlsx` / `creds.txt` | Passordlister for ansatte |

---

## 📊 Utdatalogging og analyse
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Domeneomfattende skanning** | `Snaffler.exe -s -o out.log` |
| **Målet enkelt datamaskin** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Målspesifikk andel** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Still inn samtidige tråder**| `Snaffler.exe -s -t 120 -o out.log` |
| **Ekskluder vert fra skanning** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Maksimal filstørrelsesgrense**| `Snaffler.exe -s -z 5000000` *(Begrens til 5MB filer)* |
| **Utfør via C2-montering**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Massive SMB Connection Spikes:** Snaffler kobles til port 445 på hver domenemaskin innen minutter. Dette genererer tusenvis av hendelses-ID 4624 (vellykket påloggingstype 3 - nettverk) og hendelses-ID 5140 (det ble åpnet et nettverksdelingsobjekt).
2. **Honeyfiles & Decoys:** Moderne Active Directory-forsvarere distribuerer Canary-aksjer og lokker `.kdbx`-filer som utløser øyeblikkelige SOC-varsler når de åpnes av verktøy som Snaffler.
3. **Utførelsesgrenser:** Angi `-z` (maksimal filbytestørrelse) for å unngå krasj nettverksdelinger ved å lese multi-gigabyte ISO- eller VHD-sikkerhetskopifiler.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Snaffler er designet eksklusivt for autoriserte penetrasjonstester, røde team-engasjementer og interne sikkerhetsrevisjoner. Skanning av domeneandeler uten autorisasjon utgjør ulovlig nettverksinngrep.