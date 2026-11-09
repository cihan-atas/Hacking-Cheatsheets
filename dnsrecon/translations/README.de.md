<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# dnsrecon Cheatsheet

[dnsrecon](https://github.com/darkoperator/dnsrecon) ist ein umfassendes DNS-Aufklärungs- und Enumerationstool, das in Python geschrieben ist. Es unterstützt Zonentransfers (AXFR), Reverse-Lookups, Domain-Brute-Forcing, SRV-Datensatzerkennung, Wildcard-Erkennung, Cache-Snooping und TLD-Erweiterung.

---

## Inhaltsverzeichnis

- [Kernfähigkeiten](#core-capabilities)
- [Installation](#installation)
- [Standardaufzählung und Zonenübertragungen (`-d -t std / axfr`)](#standard-enumeration--zone-transfers--d--t-std--axfr)
- [DNS Subdomain Brute-Forcing (`-t brt`)](#dns-subdomain-brute-forcing--t-brt)
- [Reverse-Lookup-Range-Scanning (`-t rvl`)](#reverse-lookup-range-scanning--t-rvl)
- [DNS-Cache-Snooping (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [Targeting auf bestimmte Nameserver (`-n`)](#targeting-special-nameservers--n)
- [Ausgabeformate (CSV, JSON, SQLite, XML)](#output-formats-csv-json-sqlite-xml)
- [Defensive Härtung für DNS-Server](#defensive-hardening-for-dns-servers)

---

## Kernkompetenzen

- **Zone Transfer Testing (AXFR):** Testet automatisch alle autorisierenden Nameserver auf nicht authentifizierte Zonenübertragungen.
- **Datensatzaufzählung:** Ruft SOA-, NS-, A-, AAAA-, MX-, TXT-, SPF- und SRV-Datensätze ab.
- **Subdomain Brute-Force:** Multithread-Wörterbuchsuche mit Platzhalterfilterung.
- **Reverse IP Range Scanning:** Extraktion von PTR-Datensätzen über beliebige CIDR-Blöcke.
- **Cache-Snooping:** Testet, ob rekursive Nameserver bestimmte Domänen im Speicher zwischengespeichert haben.
- **TLD-Erweiterung:** Erkennt übereinstimmende Domains über Ländercodes und generische Top-Level-Domains hinweg.

---

## Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```
---

## Standardaufzählung und Zonenübertragungen (`-d -t std / axfr`)

### 1. Standard-DNS-Eintragsaufzählung
Sammelt SOA-, NS-, A-, AAAA-, MX- und TXT-Einträge und versucht automatisch Zonenübertragungen gegen jeden autorisierenden NS:
```bash
dnsrecon -d example.com -t std
```
### 2. Nur Dedicated Zone Transfer (AXFR)-Test
Nur auf AXFR-Schwachstelle gegenüber allen autorisierenden Nameservern testen:
```bash
dnsrecon -d example.com -t axfr
```
*Bei Erfolg wird die gesamte DNS-Datenbank des Ziels ohne Brute-Force-Erzwingung gelöscht.*

---

## DNS-Subdomain-Brute-Forcing (`-t brt`)

Brute-Force-Subdomains mit benutzerdefinierten oder Standard-Wortlisten:

### 1. Subdomain Brute-Force mit benutzerdefinierter Wortliste
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: Zieldomäne.
- `-t brt`: Brute-Force-Modus.
- `-D <file>`: Subdomain-Wörterbuchdatei.

### 2. Brute-Force mit Wildcard-Filterung
Verhindert, dass Wildcard-DNS-Konfigurationen (`*.example.com` löst sich in eine einzelne Catch-All-IP auf) die Ergebnisse verfälschen:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: Ergebnisse der Wildcard-Auflösung ignorieren.

---

## Reverse Lookup Range Scanning (`-t rvl`)

Zählen Sie PTR-Datensätze für einen IP-Block auf, um interne Hosts, Mail-Gateways und Staging-Instanzen zu identifizieren:
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: IP-Bereich in CIDR-Notation (z. B. `192.168.1.0/24`) oder Start-End-Notation (`192.168.1.1-192.168.1.254`).
- `-t rvl`: Reverse-Lookup-Modus.

---

## DNS-Cache-Snooping (`-t snoop`)

Ermitteln Sie, ob Clients in einem Zielnetzwerk kürzlich bestimmte Domänen über ihren internen Resolver aufgelöst haben:
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: Ziel-DNS-Server für die Abfrage.
- `-D <domains_file>`: Liste der zu testenden Domänen (z. B. Konkurrenten, bösartige C2s, interne Portale).

---

## Targeting bestimmter Nameserver (`-n`)

Fragen Sie interne oder zielspezifische DNS-Server direkt ab, anstatt öffentliche Upstream-Resolver:
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## Ausgabeformate (CSV, JSON, SQLite, XML)

Speichern Sie die Ergebnisse für die Integration mit Ticketing, SIEM oder weiteren Angriffsphasen:

### 1. Nach JSON exportieren
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2. In die SQLite-Datenbank exportieren
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. Export nach CSV und XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## Defensive Härtung für DNS-Server

| Sicherheitslücke | Ausbeutungsrisiko | Empfohlene Verteidigung |
|---|---|---|
| **Uneingeschränktes AXFR** | Vollständige interne Infrastrukturzuordnung | Beschränken Sie Zonenübertragungen nur auf autorisierte sekundäre Nameserver-IPs (`allow-transfer { 10.0.0.2; };` in BIND). |
| **Rekursiven Resolver öffnen** | DNS-Verstärkung DDoS & Snooping | Deaktivieren Sie die externe Rekursion (`recursion no;` in BIND). |
| **Vorhersehbare Hostnamen** | Subdomain-Brute-Forcing | Verwenden Sie randomisierte Subdomänen für sensible Staging-Umgebungen. |
| **Subdomain-Übernahme** | CNAME verweist auf nicht beanspruchte AWS/GitHub/Azure-Dienste | Überwachen Sie ungenutzte DNS-CNAME-Einträge und entfernen Sie ungenutzte Zuordnungen sofort. |