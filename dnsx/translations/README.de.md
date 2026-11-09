<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p align="center">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="License MIT">
</p>

<p align="center">
  <b>🌐 Leistungsstarke DNS-Auflösung, Wildcard-Filterung, umgekehrte DNS-Suche und Extraktions-Engine für mehrere Abfragen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Architektur](#-overview--architecture)
- [Warum DNSX? (Wildcard-Handhabung und Geschwindigkeit)](#-why-dnsx-wildcard-handling--speed)
- [Installation](#-Installation)
- [Grundlegende Unterdomänenprüfung und -auflösung](#-basic-subdomain-probing--resolution)
- [Wildcard-DNS-Filterung (`-wd`)](#-wildcard-dns-filtering---wd)
- [Bestimmte DNS-Eintragstypen abfragen](#-querying-special-dns-record-types)
- [Reverse-DNS-Lookups (`-ptr`)](#-reverse-dns-lookups---ptr)
- [Verwendung benutzerdefinierter Resolver und Ratenbegrenzung](#-using-custom-resolvers--rate-limiting)
- [Pipeline-Automatisierung (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und OPSEC](#-defensive-considerations--opsec)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht & Architektur

**dnsx** wurde von **ProjectDiscovery** in Go entwickelt und ist ein schnelles und vielseitiges DNS-Toolkit, das für Aufklärung, Bug-Bounty-Jagd und Netzwerkerkennung entwickelt wurde.

Während Tools wie `dig`,

---

## ⚡ Warum DNSX? (Wildcard-Handhabung und Geschwindigkeit)

Bei der Bug-Bounty-Erkundung geben passive Tools (`subfinder`, `amass`) oft Tausende von toten oder Wildcard-Subdomains zurück. Das Testen von HTTP-Endpunkten anhand toter DNS-Einträge verschwendet enorme Bandbreite.

`dnsx` löst dieses Problem durch:
1. **Platzhalter herausfiltern (`-wd`):** Erkennt, ob `*.domain.com` in eine Catch-All-IP aufgelöst wird, und filtert Phantom-Subdomains heraus.
2. **Geschwindigkeit und Parallelität:** Löst mehr als 10.000 Domänen pro Minute über öffentliche und private Resolver-Pools auf.
3. **Saubere Ausgangsleitungen:** Führt direkt in `httpx` oder `nmap`.

---

## 📥 Installation
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 Grundlegende Subdomain-Prüfung und -Auflösung
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 Wildcard-DNS-Filterung (`-wd`)

Platzhalter geben eine gültige IP für nicht vorhandene Subdomains (`random123xyz.target.com`) zurück, wodurch Aufklärungsdatensätze vergiftet werden.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 Abfragen bestimmter DNS-Eintragstypen

Fragen Sie bestimmte Datensatztypen in Tausenden von Domänen ab:
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 Reverse-DNS-Lookups (`-ptr`)

Entdecken Sie aktive Infrastruktur und virtuelle Hosts, indem Sie PTR-Suchen für einen IP-Bereich durchführen:
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ Verwendung benutzerdefinierter Resolver und Ratenbegrenzung

Vermeiden Sie eine Ratenbegrenzung durch Ihren ISP oder Google DNS, indem Sie einen kuratierten Hochgeschwindigkeits-Resolver-Pool nutzen:
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ Pipeline-Automatisierung (Subfinder + DNSX + httpx)

Der ultimative Aufklärungsworkflow für Bug-Bounty-Jäger und Penetrationstester:
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Subdomains auflösen** | `dnsx -l subdomains.txt` |
| **Nur IP-Adressen extrahieren** | `dnsx -l subdomains.txt -resp-only` |
| **Wildcard-Domänen filtern** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **CNAME extrahieren (Übernahme)** | `dnsx -l subdomains.txt -cname -resp` |
| **Alle Datensätze abfragen (Recon)** | `dnsx -l domains.txt -recon` |
| **Reverse DNS Lookup (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Benutzerdefinierte Resolver und Threads**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **JSON-Ausgabe** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Defensive Überlegungen und OPSEC

1. **DNS-Flooding:** Das Auslösen von Zehntausenden DNS-Anfragen über unternehmensinterne DNS-Server führt zu einer Erhöhung der CPU-Last und löst SIEM-Warnungen für DNS-Tunneling/DDoS aus.
2. **Resolver-Poisoning:** Verwenden Sie immer verifizierte, vertrauenswürdige Resolver-Listen, um zu verhindern, dass böswillige Resolver Abfragen abfangen.

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Dieses Cheatsheet ist nur für autorisierte Penetrationstests und Bug-Bounty-Einsätze bestimmt. Die Durchführung umfangreicher DNS-Prüfungen gegen nicht autorisierte Infrastruktur ist strengstens untersagt.