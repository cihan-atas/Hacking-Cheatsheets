<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# theHarvester Cheatsheet

[theHarvester](https://github.com/laramies/theHarvester) ist eines der am weitesten verbreiteten Open-Source-Intelligence-Tools (OSINT) zum Sammeln von E-Mails, Subdomains, Mitarbeiternamen, virtuellen Hosts, offenen Ports und Mitarbeiter-PGP-Schlüsseln aus verschiedenen öffentlichen Quellen (Suchmaschinen, PGP-Schlüsselserver, Shodan, Censys, Certspotter usw.).

---

## Inhaltsverzeichnis

- [Unterstützte Datenquellen](#supported-data-sources)
- [Installation & API-Schlüsselkonfiguration](#installation--api-key-configuration)
- [Basic Domain Reconnaissance](#basic-domain-reconnaissance)
- [Passives Multi-Source-Harvesting](#passive-multi-source-harvesting)
- [Aktive DNS-Verifizierung und Brute-Forcing](#active-dns-verification--brute-forcing)
- [Ausgabeformate (HTML, XML, JSON)](#output-formats-html-xml-json)
- [Pentest-Workflow-Integration](#pentest-workflow-integration)

---

## Unterstützte Datenquellen

theHarvester sammelt Informationen aus Dutzenden passiver und aktiver Quellen:
- **Suchmaschinen (kostenlos / ohne Schlüssel):** Google, Bing, DuckDuckGo, Yahoo, Baidu.
- **Zertifikatstransparenz:** CRT.sh, Certspotter.
- **Code- und Bedrohungs-Repositorys:** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **Aufklärungs- und OSINT-APIs:** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **Netzwerk / DNS:** Anubis, HackerTarget, DNSDumpster.

---

## Installation und API-Schlüsselkonfiguration

### 1. Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```
### 2. API-Schlüssel konfigurieren (`api-keys.yaml`)
Viele Quellen (Hunter, Shodan, Censys, Intelx, GitHub) erfordern kostenlose oder kostenpflichtige API-Schlüssel.
Erstellen oder bearbeiten Sie `api-keys.yaml` im Konfigurationspfad:
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```
---

## Grundlegende Domänenaufklärung

### 1. Sammeln Sie E-Mails und Subdomains mit kostenlosen Engines
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: Zieldomäne zur Aufklärung.
- `-l <limit>`: Anzahl der pro Engine abzufragenden Ergebnisse.
- `-b <sources>`: Durch Kommas getrennte Liste der Quellen (oder `all`).

### 2. Fragen Sie alle konfigurierten Quellen ab
```bash
theHarvester -d example.com -l 500 -b all
```
---

## Passives Multi-Source-Harvesting

Fragen Sie Datenbanken zu spezifischen Bedrohungsinformationen und Zertifikatstransparenz ab, ohne Datenverkehr an das Ziel zu senden:

### 1. Zertifikatstransparenz und Bedrohung Intel
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. Zielgruppenspezifische Zielgruppen ansprechen
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## Aktive DNS-Verifizierung und Brute-Forcing

### 1. Erkannte Subdomains auflösen (`-r`)
Führen Sie aktive DNS-Suchen durch, um zu überprüfen, ob die gesammelten Subdomains derzeit aktiv sind:
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. DNS Brute-Force (`-c`)
Führen Sie wortlistenbasiertes DNS-Subdomain-Brute-Forcing direkt in theHarvester durch:
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3. Überprüfung des virtuellen Hosts (`-v`)
Überprüfen Sie virtuelle Hosts über HTTP-Anfragen:
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## Ausgabeformate (HTML, XML, JSON)

Exportieren Sie Ergebnisse für automatisierte Berichte oder nachgelagerte Aufklärung:

### 1. Export nach HTML und XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
Erstellt `/tmp/theharvester_results.html` und `/tmp/theharvester_results.xml`.

### 2. Erkannte Subdomains mit `jq` analysieren
Beim Exportieren oder Parsen von JSON:
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## Pentest-Workflow-Integration

Verketten Sie die Ergebnisse von theHarvester direkt mit aktiven Schwachstellenscannern:
```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```