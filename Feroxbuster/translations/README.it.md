<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster - Scheda completa

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<palign="centro">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<palign="centro">
  <b>⚡ Uno strumento di scoperta dei contenuti veloce, semplice e ricorsivo scritto in Rust</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Caratteristiche principali](#-caratteristiche principali)
- [Confronto strumenti](#-confronto-strumenti)
- [Installazione](#-installazione)
- [Sintassi di base e avvio rapido](#-basic-syntax--quick-start)
- [Opzioni comandi principali](#-core-command-options)
  - [Controllo ricorsione](#controllo-ricorsione)
  - [Concorrenza e prestazioni](#concorrenza--prestazioni)
  - [Estensioni file](#estensioni-file)
  - [Filtro e corrispondenza](#filtro--corrispondenza)
  - [Funzioni intelligenti (Sintonizzazione automatica, Estrazione collegamenti, Backup)](#funzioni-intelligenti)
  - [Intestazioni, cookie e autenticazione](#headers-cookies--auth)
  - [Integrazione proxy e Burp Suite](#proxy--burp-suite-integration)
- [Scansione e pipeline multi-target](#-multi-target-scanning--pipelines)
- [Flussi di lavoro nel mondo reale](#-flussi di lavoro nel mondo reale)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione

**Feroxbuster** è uno scanner di contenuti web ricorsivo, multi-thread ad alte prestazioni scritto in Rust. Progettato per essere veloce, semplice e intuitivo, combina la velocità dei fuzzer basati su Go/Rust con il rilevamento automatico e nativo delle directory ricorsive.

Brilla durante i test di penetrazione e gli impegni di bug bounty in cui è necessario scoprire rapidamente alberi di siti Web profondi e directory nascoste senza generare script manuali complessi.

---

## 🌟 Caratteristiche principali

- ⚡ **Incredibilmente veloce:** Alimentato da Rust asincrono (`tokio` e `reqwest`), capace di migliaia di richieste al secondo.
- 🔄 **Scoperta ricorsiva nativa:** scopre automaticamente nuove directory e le scansiona ricorsivamente fino a limiti di profondità configurabili.
- 🧠 **Sintonizzazione automatica e salvataggio automatico:** Limita automaticamente la velocità di scansione se il server Web di destinazione rallenta o interrompe la scansione se si verificano eccessivi errori di connessione.
- 🔗 **Estrazione dei collegamenti in linea:** analizza le risposte HTML e JavaScript per scoprire endpoint nascosti e reinserirli nella coda di scansione.
- 📦 **Ricerca backup automatizzata:** aggiunge automaticamente le varianti `.bak`, `~`, `.old` e `.orig` ai file rilevati.
- 🎯 **Pipeable Input:** accetta perfettamente elenchi di target da `stdin` (da strumenti come `subfinder`, `httpx` o `gau`).

---

## ⚖️ Confronto degli strumenti

| Caratteristica | Feroxbuster | uff | ricerca | Gobuster | Dirb |
|---------|-------------|------|-----------|----------|------|
| **Lingua** | Ruggine | Vai | Pitone | Vai | C|
| **Velocità** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Ricorsione predefinita** | ✅ Automatico | ❌Bandiera manuale | ✅ Automatico (`-r`) | ❌ Nessuno | ✅ Automatico |
| **Estrazione link** | ✅ Sì (`--extract-links`) | ❌No | ❌No | ❌No | ❌No |
| **Sintonizzazione automatica della velocità** | ✅ Sì (`--auto-tune`) | ❌No | ❌No | ❌No | ❌No |
| **POST Fuzzing** | ❌No | ✅Nativo | ✅ Nativo (`--data`) | ❌No | ❌No |
| **Standard multi-target** | ✅Nativo | ❌Limitato | ✅ Sì (`-l`) | ❌No | ❌No |
| **Rilevamento file di backup**| ✅ Nativo (`--collect-backups`)| ❌No | ✅ Nativo (`-e *`) | ❌No | ❌No |

---

## 📥Installazione

### Kali Linux e Debian/Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### Utilizzo di Cargo (Gestore pacchetti Rust)
```bash
cargo install feroxbuster
```
### macOS (Homebrew)
```bash
brew install feroxbuster
```
### Finestra mobile
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### Binario precompilato (versioni GitHub)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 Sintassi di base e avvio rapido
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### Esempi di scansione rapida
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ Opzioni dei comandi principali

### Controllo della ricorsione
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### Concorrenza e prestazioni
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### Estensioni dei file
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### Filtraggio e corrispondenza
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### Funzionalità intelligenti
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### Intestazioni, cookie e autenticazione
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### Integrazione proxy e suite Burp
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 Scansione e pipeline multi-target

Feroxbuster legge nativamente gli URL di destinazione dall'input standard (`stdin`), rendendolo ideale per l'integrazione con le pipeline di rilevamento delle risorse:
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### Scansione degli URL da un file
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 Flussi di lavoro nel mondo reale

### Flusso di lavoro 1: ricognizione Web completa di bug bounty

Concorrenza elevata, ottimizzazione automatica della velocità, estrazione dei collegamenti e rilevamento dei backup:
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### Flusso di lavoro 2: individuazione del portale interno aziendale/Active Directory

Scansione di un'applicazione web interna tramite Burp Suite con autenticazione e certificati autofirmati:
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### Flusso di lavoro 3: API rapida non ricorsiva e rilevamento degli endpoint
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 Tabella di consultazione rapida

| Bandiera | Breve | Descrizione | Esempio |
|------|-------|-------------|---------|
| `--url` | `-u` | URL di destinazione da scansionare | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Percorso dell'elenco delle parole | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Numero di thread simultanei per directory | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Limita il totale delle scansioni simultanee delle directory | `feroxbuster -L 3` |
| `--depth` | `-d` | Profondità massima di ricorsione (predefinito 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Disabilita la scansione ricorsiva delle directory | `feroxbuster -n` |
| `--extensions` | `-x` | Estensioni di file da aggiungere | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Codici di stato da includere | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Codici di stato da escludere | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Dimensioni delle risposte da escludere | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Conteggio parole per escludere | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Conteggi delle righe da escludere | `feroxbuster -N 8` |
| `--auto-tune` | Nessuno | Regola dinamicamente la velocità in base alla latenza | `feroxbuster --auto-tune` |
| `--auto-bail` | Nessuno | Interrompere la scansione in caso di eccessivi errori di rete | `feroxbuster --auto-bail` |
| `--extract-links`| Nessuno | Analizza HTML/JS per nuovi endpoint | `feroxbuster --extract-links` |
| `--collect-backups` | Nessuno | Controlla le variazioni del backup (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Invia traffico tramite proxy HTTP | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Nessuno | Scorciatoia per `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | Nessuno | Leggi gli URL dall'input standard | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Percorso file di output | `feroxbuster -o results.txt` |
| `--json` | Nessuno | Formatta l'output come JSON | `feroxbuster --json -o out.json` |

---

## 💡 Suggerimenti e migliori pratiche

1. **Utilizza `--auto-tune` per bersagli remoti:**
   - Invece di indovinare il numero di thread (`-t`), `--auto-tune` misura attivamente i tempi di risposta target e limita o accelera dinamicamente, evitando la negazione del servizio accidentale del server.
2. **Combina `--extract-links` e `--collect-backups`:**
   - Questa combinazione è eccezionalmente letale nelle valutazioni di bug bounty: Feroxbuster estrae percorsi JS nascosti, li scansiona e controlla immediatamente se i backup degli sviluppatori (`.bak`, `.swp`, `~`) sono stati lasciati sul server.
3. **Limita le scansioni su elenchi di parole di grandi dimensioni con `-L`:**
   - Se si utilizza un dizionario di oltre 200.000 parole, la ricorsione illimitata può avviare dozzine di thread simultanei. Utilizza `-L 2` o `-L 3` per mantenere la memoria e la larghezza di banda entro limiti operativi sicuri.
4. **Utilizza file di stato per riprendere audit di grandi dimensioni:**
   - Feroxbuster salva periodicamente i file di stato (`.state`). Se si verifica una scansione interrotta, riprenderla immediatamente con `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** Feroxbuster è costruito rigorosamente per valutazioni di sicurezza autorizzate, competizioni CTF e test di penetrazione condotti con permesso scritto. La scansione di obiettivi non autorizzati può saturare l'infrastruttura di rete ed è severamente vietata dalla legge.

---

## 📚 Risorse

- [Repository GitHub di Feroxbuster](https://github.com/epi052/feroxbuster)
- [Documentazione ufficiale di Feroxbuster](https://epi052.github.io/feroxbuster-docs/)
- [Elenchi di rilevamento SecLists](https://github.com/danielmiessler/SecLists)