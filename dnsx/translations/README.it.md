<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Ricognizione e scansione">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Licenza MIT">
</p>

<palign="centro">
  <b>🌐 Risoluzione DNS ad alte prestazioni, filtro con caratteri jolly, ricerche DNS inverse e motore di estrazione multi-query</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Perché dnsx? (Gestione e velocità dei caratteri jolly)](#-why-dnsx-wildcard-handling--speed)
- [Installazione](#-installazione)
- [Sondaggio e risoluzione di base del sottodominio](#-basic-subdomain-probing--solving)
- [Filtro DNS con caratteri jolly (`-wd`)](#-wildcard-dns-filtering---wd)
- [Query su tipi di record DNS specifici](#-querying-specific-dns-record-types)
- [Ricerche DNS inverse (`-ptr`)](#-ricerche-dns-inverse---ptr)
- [Utilizzo di risolutori personalizzati e limitazione della velocità](#-using-custom-resolvers--rate-limiting)
- [Automazione pipeline (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

Progettato in Go da **ProjectDiscovery**, **dnsx** è un toolkit DNS veloce e multiuso progettato per la ricognizione, la ricerca di bug bounty e l'individuazione della rete.

Mentre strumenti come `dig`, `nslookup` e `host` risolvono i record uno per uno, `dnsx` elabora migliaia di query in modo asincrono, riprova automaticamente i risolutori non riusciti, estrae IP di risposta puliti ed elimina il rumore dei caratteri jolly che inquina l'enumerazione dei sottodomini.

---

## ⚡ Perché dnsx? (Gestione e velocità dei caratteri jolly)

Durante la ricognizione del bug bounty, gli strumenti passivi (`subfinder`, `amass`) spesso restituiscono migliaia di sottodomini morti o jolly. Testare gli endpoint HTTP su voci DNS inattive comporta uno spreco enorme di larghezza di banda.

`dnsx` risolve questo problema:
1. **Filtro dei caratteri jolly (`-wd`):** Rileva se `*.domain.com` si risolve in un IP catch-all e filtra i sottodomini fantasma.
2. **Velocità e concorrenza:** risolve oltre 10.000 domini al minuto tra pool di risoluzione pubblici e privati.
3. **Pulire le tubazioni di uscita:** alimenta direttamente in `httpx` o `nmap`.

---

## 📥Installazione
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

## 🚀 Sondaggio e risoluzione di base dei sottodomini
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

## 🚫 Filtraggio DNS con caratteri jolly (`-wd`)

I caratteri jolly restituiscono un IP valido per sottodomini inesistenti (`random123xyz.target.com`), avvelenando i set di dati di ricognizione.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 Interrogazione di tipi di record DNS specifici

Esegui query su tipi di record specifici su migliaia di domini:
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

## 🔄 Ricerche DNS inverse (`-ptr`)

Scopri l'infrastruttura attiva e gli host virtuali eseguendo ricerche PTR su un intervallo IP:
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ Utilizzo di risolutori personalizzati e limitazione della velocità

Evita i limiti di velocità imposti dal tuo ISP o da Google DNS utilizzando un pool di risoluzione curato e ad alta velocità:
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ Automazione della pipeline (Subfinder + dnsx + httpx)

Il flusso di lavoro di ricognizione definitivo per cacciatori di bug e penetration tester:
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Risolvi sottodomini** | `dnsx -l subdomains.txt` |
| **Estrai solo indirizzi IP** | `dnsx -l subdomains.txt -resp-only` |
| **Filtra domini jolly** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **Estrai CNAME (Takeover)** | `dnsx -l subdomains.txt -cname -resp` |
| **Interroga tutti i record (ricognizione)** | `dnsx -l domains.txt -recon` |
| **Ricerca DNS inversa (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Resolver e thread personalizzati**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **Uscita JSON** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Flooding DNS:** l'esecuzione di decine di migliaia di query DNS tramite server DNS interni aziendali aumenterà i carichi della CPU e attiverà avvisi SIEM per tunneling DNS/DDoS.
2. **Resolver Poisoning:** utilizza sempre elenchi di risolutori verificati e attendibili per impedire a risolutori dannosi di intercettare le query.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è destinato esclusivamente ai test di penetrazione autorizzati e agli impegni di bug bounty. È severamente vietato condurre indagini DNS su volumi elevati contro infrastrutture non autorizzate.