<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪁 Kiterunner - Foglio informativo completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██╗  ██╗██╗████████╗███████╗██████╗ ██╗   ██╗███╗   ██╗███╗   ██╗███████╗██████╗ 
  ██║ ██╔╝██║╚══██╔══╝██╔════╝██╔══██╗██║   ██║████╗  ██║████╗  ██║██╔════╝██╔══██╗
  █████╔╝ ██║   ██║   █████╗  ██████╔╝██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██████╔╝
  ██╔═██╗ ██║   ██║   ██╔══╝  ██╔══██╗██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██╔══██╗
  ██║  ██╗██║   ██║   ███████╗██║  ██║╚██████╔╝██║ ╚████║██║ ╚████║███████╗██║  ██║
  ╚═╝  ╚═╝╚═╝   ╚═╝   ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Context-Aware API Endpoint & Route Discovery Engine by Assetnote
```
<palign="centro">
  <img src="https://img.shields.io/badge/Kiterunner-API_Discovery-blue?style=for-the-badge" alt="Kiterunner">
  <img src="https://img.shields.io/badge/Author-Assetnote-orange?style=for-the-badge" alt="Assetnote">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Sicurezza web e API">
  <img src="https://img.shields.io/badge/License-Apache_2.0-red?style=for-the-badge" alt="Licenza Apache 2.0">
</p>

<palign="centro">
  <b>🌐 Brute-forcer del percorso API REST e GraphQL ad alte prestazioni progettato per mappare le moderne architetture API utilizzando set di dati contestuali</b>
</p>

---

## 📋 Sommario

- [Panoramica e problema di rilevamento dell'API](#-overview--the-api-discovery-problem)
- [Confronto strumenti: Kiterunner vs ffuf vs dirsearch](#-tool-comparison-kiterunner-vs-ffuf-vs-dirsearch)
- [Installazione](#-installazione)
- [Download dei set di dati di Assetnote Kite](#-downloading-assetnote-kite-datasets)
- [API di scansione (`kr scan`)](#-scanning-apis-kr-scan)
- [Forzatura bruta elenco parole standard (`kr brute`)](#-standard-wordlist-brute-forcing-kr-brute)
- [Intestazioni, cookie e autenticazione](#-headers-cookies--autenticazione)
- [Filtri e codici di stato](#-filtering--status-codes)
- [Riproduzione dei percorsi scoperti (output `curl`)](#-replaying-discovered-routes-curl-output)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e problema di rilevamento delle API

Gli strumenti tradizionali di rilevamento dei contenuti Web (`ffuf`, `dirsearch`, `gobuster`) utilizzano dizionari semplici di una sola parola (ad esempio, `admin`, `api`, `login`). Anche se funziona con le gerarchie di file tradizionali, **non funziona con le moderne API REST e GraphQL**.

Le API moderne utilizzano percorsi nidificati e multilivello come:
`/api/v2/organizations/{org_id}/users/export?format=json`

**Kiterunner**, creato da **Assetnote**, risolve questo problema compilando OpenAPI, Swagger, Postman e definizioni di schema del mondo reale in set di dati binari compatti (`.kite`). Invece di indovinare parole casuali, Kiterunner ricostruisce intere strutture gerarchiche di chiamate API con metodi HTTP corretti (`GET`, `POST`, `PUT`, `DELETE`).

---

## ⚖️ Confronto strumenti: Kiterunner vs ffuf vs dirsearch

| Caratteristica | Kiterunner (`kr`) | uff | ricerca |
|---------|-------------------|------|-----------|
| **Attenzione al target principale** | REST, GraphQL, OpenAPI | Fuzzing Web generale | Directory Forzatura bruta |
| **Consapevolezza della gerarchia dei percorsi**| ✅ Nativo (file `.kite`) | ❌ Elenco di parole piatte | ❌ Elenco di parole piatte |
| **Scambio di verbi HTTP** | ✅ Test GET, POST, PUT, DELETE| ❌ Configurazione manuale | ❌Limitato |
| **Analisi spavalda/OpenAPI** | ✅ Ingestione di schemi nativi | ❌No | ❌No |
| **Velocità e concorrenza** | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐⭐ (Go Engine) | ⭐⭐⭐⭐ (Pitone) |

---

## 📥Installazione
```bash
# Download precompiled binary from GitHub Releases
wget https://github.com/assetnote/kiterunner/releases/download/v1.0.2/kiterunner_1.0.2_linux_amd64.tar.gz
tar -xzvf kiterunner_1.0.2_linux_amd64.tar.gz
sudo mv kr /usr/local/bin/

# Verify installation
kr version
```
---

## 📦 Download dei set di dati Assetnote Kite

Kiterunner si affida a set di dati di percorso `.kite` predefiniti creati da milioni di API pubbliche analizzate:
```bash
# Create wordlists directory
mkdir -p ~/.kiterunner && cd ~/.kiterunner

# Small dataset (~40MB, 2.5 million API routes - Recommended for fast scans)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-small.kite.tar.gz
tar -xzvf routes-small.kite.tar.gz

# Large dataset (~130MB, 8.5 million API routes - Exhaustive testing)
wget https://wordlists-cdn.assetnote.io/data/kiterunner/routes-large.kite.tar.gz
tar -xzvf routes-large.kite.tar.gz
```
---

## 🚀 API di scansione (`kr scan`)

Il comando `scan` utilizza i set di dati `.kite` per sondare gli obiettivi utilizzando gerarchie di percorsi contestuali:
```bash
# Scan a single target using the small dataset
kr scan https://target.local/api -w ~/.kiterunner/routes-small.kite

# Scan multiple hosts from a file with 100 concurrent workers
kr scan targets.txt -w ~/.kiterunner/routes-small.kite -c 100 -j 50

# Scan with maximum verbosity
kr scan https://target.local -w ~/.kiterunner/routes-small.kite -v
```
---

## 🔨 Forzatura bruta dell'elenco di parole standard (`kr brute`)

Se desideri utilizzare elenchi di parole `.txt` standard sfruttando al tempo stesso il motore API multimetodo di Kiterunner:
```bash
# Brute force standard paths using a text dictionary
kr brute https://target.local -w /usr/share/wordlists/dirb/common.txt

# Brute force with custom API prefixes
kr brute https://target.local/api/v1 -w endpoints.txt
```
---

## 🔑 Intestazioni, cookie e autenticazione

Le API richiedono spesso JWT, token Bearer o intestazioni di organizzazione personalizzate:
```bash
# Pass custom Authorization header
kr scan https://target.local/api -w routes-small.kite -H "Authorization: Bearer eyJhbGci..."

# Pass custom API key and Cookie
kr scan https://target.local -w routes-small.kite -H "X-API-Key: secret123" -H "Cookie: session=xyz"
```
---

## 🎯 Filtri e codici di stato
```bash
# Exclude standard error codes (404, 403, 502)
kr scan https://target.local -w routes-small.kite -x 404,403,502

# Filter out responses by length / content size
kr scan https://target.local -w routes-small.kite --filter-length 0,324
```
---

## 🔄 Riproduzione dei percorsi scoperti (uscita `curl`)

Kiterunner può emettere le rotte scoperte come comandi `curl` utilizzabili, consentendo la riproduzione istantanea nel terminale o nella Burp Suite:
```bash
# Print reproducible curl command lines for all successful matches
kr scan https://target.local -w routes-small.kite -o json | jq -r '.[] | .curl'
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **API di scansione con set di dati di piccole dimensioni** | `kr scan <URL> -w routes-small.kite` |
| **API di scansione con set di dati di grandi dimensioni** | `kr scan <URL> -w routes-large.kite` |
| **Scansiona più host** | `kr scan hosts.txt -w routes-small.kite -c 100` |
| **Elenco parole testo forza bruta** | `kr brute <URL> -w wordlist.txt` |
| **Autenticazione con token al portatore**| `kr scan <URL> -w routes-small.kite -H "Authorization: Bearer <TOKEN>"` |
| **Escludi codici di stato** | `kr scan <URL> -w routes-small.kite -x 404,403` |
| **Output nel file JSON** | `kr scan <URL> -w routes-small.kite -o json -oFile results.json` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Enorme volume di richieste:** il dizionario `routes-large.kite` genera milioni di richieste. Assicurarsi che la limitazione della velocità (`-q 200`) sia abilitata durante il test degli ambienti di produzione.
2. **Metodi di cambio di stato:** Kiterunner invia richieste `POST`, `PUT` e `DELETE`. Sulle API di test non protette, ciò può modificare o eliminare inavvertitamente i record di backend.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Kiterunner è progettato solo per test di sicurezza API autorizzati e impegni di bug bounty. Il fuzzing di API non autorizzate è strettamente illegale.