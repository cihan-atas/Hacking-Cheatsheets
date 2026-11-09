<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Searchsploit-Cheatsheet

[Searchsploit](https://www.exploit-db.com/searchsploit) ist das offizielle Befehlszeilenprogramm für [Exploit-Database](https://www.exploit-db.com/). Damit können Sicherheitsexperten, Penetrationstester und Schwachstellenforscher Offline-Suchen in Zehntausenden veröffentlichten Exploits, Shellcodes und Whitepapers durchführen, ohne Netzwerkverkehr an Suchmaschinen von Drittanbietern zu senden.

---

## Inhaltsverzeichnis

- [Übersicht & Architektur](#overview--architecture)
- [Installation und lokale Datenbankaktualisierungen](#installation--local-database-updates)
- [Einfache und erweiterte Exploit-Suche](#basic--advanced-exploit-searching)
- [Untersuchen und Kopieren von Exploits (`-m` / `-p` / `-x`)](#examining--copying-exploits--m---p---x)
- [Filtern nach Plattform und Typ](#filtering-by-platform--type)
- [Nmap XML / Grepable Scanning-Integration](#nmap-xml--grepable-scanning-integration)
- [JSON-Ausgabe- und Skriptautomatisierung](#json-output--script-automation)
- [Best Practices beim Penetrationstest](#best-practices-in-penetration-testing)

---

## Übersicht & Architektur

Searchsploit fragt einen lokalen Git-Klon von Exploit-DB ab, der in `/usr/share/exploitdb/` gespeichert ist.
- **Kein Netzwerk-Fingerabdruck:** Suchvorgänge werden anhand lokaler CSV- und Verzeichnisstrukturen ausgeführt.
- **Offline-Zuverlässigkeit:** Voll funktionsfähig in Air-Gap-Zielumgebungen und Engagement-Netzwerken.
- **Integrierte Spiegelung:** Kopiert Exploit-Dateien direkt in das aktuelle Arbeitsverzeichnis zur sofortigen Überprüfung und Änderung.

---

## Installation und lokale Datenbankaktualisierungen

### Debian / Ubuntu / Kali Linux / Parrot OS
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Git-Repository-Installation (jede Linux-Distribution)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## Einfache und erweiterte Exploit-Suche

### 1. Einfache Stichwortsuche
Suchen Sie nach bestimmten Diensten, Daemons oder Software:
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. Exakte Titelübereinstimmung (`-t`)
Beschränken Sie Suchanfragen strikt auf die Ausnutzung von Titeln, um verrauschte Beschreibungsübereinstimmungen zu vermeiden:
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. Strikte Zuordnung von Groß- und Kleinschreibung (`-e`)
Finden Sie genaue Versions- und Musterübereinstimmungen:
```bash
searchsploit -e "Apache Struts"
```
### 4. Suche nach CVE-Identifikator
Abfragen von Exploits, die bestimmten häufigen Schwachstellen- und Gefährdungsnummern zugeordnet sind:
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. Unerwünschte Ergebnisse ausschließen (`--exclude`)
Filtern Sie Denial-of-Service (DoS) oder Hilfsskripts heraus, wenn Sie nach Remote Code Execution (RCE) suchen:
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## Exploits untersuchen und kopieren (`-m` / `-p` / `-x`)

Bearbeiten oder führen Sie Exploits niemals direkt im `/usr/share/exploitdb/`-Verzeichnis aus. Nutzen Sie die Kopier- und Prüffunktionen von Searchsploit.

### 1. Exploit in aktuelles Verzeichnis kopieren (spiegeln) (`-m`)
Kopieren Sie eine Exploit-PoC-Datei (Python, C, Ruby, Bash) direkt in Ihr lokales Arbeitsverzeichnis:
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. Exploit-Metadaten und vollständigen Pfad prüfen (`-p`)
Zeigen Sie den absoluten Dateisystempfad und die Exploit-DB-URL für eine EDB-ID an:
```bash
searchsploit -p 50383
```
Ausgabe:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. Exploit-Code im Terminal-Pager lesen (`-x`)
Öffnen Sie den Exploit-Code zur schnellen Überprüfung direkt in `less`:
```bash
searchsploit -x 50383
```
---

## Filtern nach Plattform und Typ

Searchsploit indiziert Exploits, Shellcode und Forschungsarbeiten über mehrere Architekturen hinweg:

### 1. Filtern Sie nach Zielbetriebssystem
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. Nur nach rohen Shellcodes filtern (`-s`)
Suchen Sie nach architekturspezifischen Shellcode-Nutzlasten (z. B. execve `/bin/sh` ohne Nullbytes):
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. Filtern Sie nach Sicherheitspapieren und Whitepapers (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Nmap XML / Grepable Scanning-Integration

Searchsploit kann Nmap-XML-Scan-Ausgaben automatisch analysieren und entdeckte Softwareversionen mit bekannten Exploits abgleichen:
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Searchsploit überprüft jedes offene Port-Banner und jede Versionszeichenfolge und fragt lokale Exploits zur sofortigen Einstufung ab.*

---

## JSON-Ausgabe und Skriptautomatisierung

Für Pipeline-Automatisierungs- und benutzerdefinierte Aufklärungstools werden Ergebnisse in strukturiertem JSON ausgegeben:
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
Filtern Sie Exploit-Dateipfade mit `jq`:
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## Best Practices beim Penetrationstest

1. **Vor der Ausführung überprüfen:** Überprüfen Sie vor der Kompilierung oder Ausführung immer den Exploit-Quellcode (`searchsploit -x <id>`). Überprüfen Sie den fest codierten Shellcode, zielen Sie auf IP-Adressen ab und stellen Sie sicher, dass keine Hintertüren oder böswilligen Payload-Staginger vorhanden sind.
2. **Mit korrekten Flags kompilieren:** Viele Exploit-DB C-Exploits erfordern bestimmte GCC-Flags (z. B. `-m32`, `-lpthread` oder `-lcrypto`):
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **Datenbank auf dem neuesten Stand halten:** Führen Sie `searchsploit -u` regelmäßig aus, um sicherzustellen, dass neu veröffentlichte Zero-Days und Proof-of-Concepts lokal indiziert werden.