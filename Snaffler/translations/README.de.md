<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler – Vollständiger Cheatsheet

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
  <b>🌐 Automatisiertes Windows-Domänentool zum Crawlen von SMB-Freigaben, Auffinden saftiger Konfigurationsdateien, KeePass-Datenbanken und Klartext-Anmeldeinformationen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Überblick und das Share-Hunting-Problem](#-overview--the-share-hunting-problem)
- [Wie Snaffler funktioniert](#-how-snaffler-works)
- [Installations- und Ausführungsoptionen](#-installation--execution-options)
- [Snaffler-Triage und Schweregrade](#-snaffler-triage--severity-levels)
- [Allgemeine Scan-Workflows](#-common-scanning-workflows)
  - [Domainweites Share-Crawl](#domain-wide-share-crawl)
  - [Targeting auf bestimmte Hosts oder Freigaben](#targeting-special-hosts-or-shares)
  - [Ausgenommen Computer und DFS-Roots](#exclusive-computers--dfs-roots)
- [Juicy Targets & File Match Rules](#-juicy-targets--file-match-rules)
- [Ausgabeprotokollierung und -analyse](#-output-logging--analysis)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Überblick und das Problem der Aktienjagd

In mittleren bis großen Active Directory-Umgebungen hosten Unternehmensdateiserver und Workstations Tausende von offenen SMB-Netzwerkfreigaben. Systemadministratoren, Entwickler und Mitarbeiter hinterlassen häufig Folgendes:

- 🔑 **KeePass-Datenbanken (`.kdbx`):** Enthält oft Master-Passwörter oder leicht zu knackende Master-Schlüssel.
- ⚙️ **Konfigurationsdateien (`web.config`, `appsettings.json`):** Enthält hartcodierte SQL-Verbindungszeichenfolgen, Dienstkontokennwörter und API-Schlüssel.
- 📜 **Bereitstellungsskripte (`unattend.xml`, `.ps1`, `.bat`):** Fest codierte Administratoranmeldeinformationen.
- 🔏 **Private Zertifikate (`.pfx`, `.p12`,

Das manuelle Durchsuchen von Tausenden von Freigaben ist unmöglich. **Snaffler**, entwickelt von **l33tb455**, automatisiert diesen gesamten Prozess: Es fragt Active Directory nach Computerobjekten ab, zählt lesbare SMB-Freigaben auf, durchsucht den Verzeichnisbaum und wertet Dateien anhand kuratierter Regex-Regelsätze aus.

---

## ⚙️ Wie Snaffler funktioniert
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 Installations- und Ausführungsoptionen

Snaffler wird als eigenständige .NET-Binärdatei kompiliert:
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### Läuft unter Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### Ausführen über Cobalt Strike / C2 (Execute-Assembly):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Snaffler-Triage und Schweregrade

Snaffler kategorisiert Ergebnisse in farblich gekennzeichnete Dringlichkeitsbereiche:

- 🔴 **`{Red}` / Schwarz:** Auf jeden Fall ein hochwertiger Fund! Klartext-Passwörter, private Schlüssel (`.pfx`, `.id_rsa`), AWS-Tokens, unbeaufsichtigte Installationsdateien.
- 🟡 **`{Yellow}`:** Wahrscheinlicher Befund. Konfigurationsdateien mit Passwortzeichenfolgen, Datenbankverbindungszeichenfolgen, Schattenkopien.
- 🟢 **`{Green}`:** Information / Interesse. Dateinamen, die mit Verwaltungsmustern übereinstimmen (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Gängige Scan-Workflows

### Domainweiter Share Crawl

Scannen Sie alle zugänglichen Freigaben in der gesamten Active Directory-Domäne:
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### Ausrichtung auf bestimmte Hosts oder Freigaben

Wenn Sie das Scannen der gesamten Domäne vermeiden und sich auf einen Dateiserver oder Domänencontroller konzentrieren möchten:
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### Ausgenommen Computer und DFS-Roots

Vermeiden Sie das Scannen langsamer oder empfindlicher Computer:
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 Juicy Targets & File Match Rules

Standardmäßig sucht Snaffler nach den folgenden kritischen Erweiterungen:

| Erweiterung / Datei | Hochwertiger Inhalt |
|------------------|--------------------|
| `.kdbx` / `.kdb` | KeePass-Passwort-Manager-Safes |
| `web.config` / `app.config` | Klartext-Datenbank-Passwörter |
| `unattend.xml` / `sysprep.inf` | Windows-Administratorkennwörter |
| `.ppk` / `.pem` / `id_rsa` | Private SSH-Schlüssel |
| `.pfx` / `.p12` | Active Directory-Zertifikate |
| `.rdp` / `.vnc` | Remote-Desktop-Verbindungsprofile |
| `passwords.xlsx` / `creds.txt` | Passwortlisten für Mitarbeiter |

---

## 📊 Ausgabeprotokollierung und -analyse
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Domänenweiter Scan** | `Snaffler.exe -s -o out.log` |
| **Einzelner Zielcomputer** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Zielspezifischer Anteil** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Parallelitätsthreads optimieren**| `Snaffler.exe -s -t 120 -o out.log` |
| **Host vom Scan ausschließen** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Maximale Dateigrößenbeschränkung**| `Snaffler.exe -s -z 5000000` *(Begrenzt auf 5 MB Dateien)* |
| **Ausführen über C2 Assembly**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **Massive SMB-Verbindungsspitzen:** Snaffler stellt innerhalb von Minuten eine Verbindung zu Port 445 auf jedem Domänencomputer her. Dadurch werden Tausende von Ereignis-ID 4624 (Erfolgreiche Anmeldung Typ 3 – Netzwerk) und Ereignis-ID 5140 (Auf ein Netzwerkfreigabeobjekt wurde zugegriffen) generiert.
2. **Honeyfiles & Decoys:** Moderne Active Directory-Verteidiger setzen Canary-Freigaben ein und locken `.kdbx`-Dateien an, die beim Öffnen durch Tools wie Snaffler sofortige SOC-Warnungen auslösen.
3. **Ausführungsbeschränkungen:** Legen Sie `-z` (maximale Dateibytegröße) fest, um einen Absturz von Netzwerkfreigaben durch das Lesen von ISO- oder VHD-Sicherungsdateien mit mehreren Gigabyte zu vermeiden.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Snaffler ist ausschließlich für autorisierte Penetrationstests, Red-Team-Einsätze und interne Sicherheitsüberprüfungen konzipiert. Das unbefugte Scannen von Domain-Freigaben stellt einen illegalen Netzwerkmissbrauch dar.