<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<palign="centro">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Elenchi di parole">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</p>

<palign="centro">
  <b>🔍 Classico scanner di contenuti Web che lancia attacchi basati su dizionario contro i server Web</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [DIRB vs alternative moderne](#-dirb-vs-modern-alternative)
- [Installazione](#-installazione)
- [Elenchi di parole predefinite](#-elenchi di parole predefinite)
- [Sintassi di base](#-sintassi-base)
- [Scenari di scansione comuni](#-scenari-di-scansione-comuni)
- [Opzioni avanzate e ottimizzazione](#-opzioni-avanzate--ottimizzazione)
- [Caccia alle vulnerabilità con elenchi integrati](#-ricerca-delle-vulnerabilità-con-elenchi integrati)
- [Flussi di lavoro nel mondo reale](#-flussi di lavoro nel mondo reale)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione

**DIRB** (Directory Buster) è uno scanner di contenuti Web classico, leggero, basato su URL, scritto in C. Lancia attacchi basati su dizionario contro server Web, cercando oggetti Web esistenti e nascosti (file, directory, script ed endpoint).

DIRB funziona generando richieste HTTP per ogni parola in un elenco di parole, analizzando i codici di risposta del server (200, 301, 302, 403, ecc.) e determinando se esiste una risorsa.

### Caratteristiche principali

- ✅ **Scansione ricorsiva automatica:** scansiona automaticamente le sottodirectory scoperte per impostazione predefinita.
- ✅ **Elenchi di parole specializzati integrati:** Viene fornito con elenchi di parole mirati per server Web, CMS e vulnerabilità comuni.
- ✅ **Basso impatto sulle risorse:** File binario leggero che funziona senza problemi anche su hardware con specifiche ridotte o sistemi meno recenti.
- ✅ **Supporto proxy e intestazione personalizzata:** Facile integrazione con Burp Suite e proxy HTTP upstream.

---

## ⚖️DIRB vs Alternative Moderne

| Caratteristica | DIRB | Gobuster | uff | Feroxbuster | ricerca |
|---------|------|----------|------|-------------|-----------|
| **Lingua** | C| Vai | Vai | Ruggine | Pitone |
| **Velocità** | ⭐⭐ (Filo basso) | ⭐⭐⭐⭐⭐ (Veloce) | ⭐⭐⭐⭐⭐ (Estremamente veloce) | ⭐⭐⭐⭐⭐ (Async Rust) | ⭐⭐⭐⭐ (Veloce) |
| **Ricorsione predefinita** | ✅ Sì (automatico) | ❌No | ❌ No (`-recursion` opzionale) | ✅ Sì (automatico) | ✅ Sì (`-r`) |
| **Elenchi di parole personalizzati inclusi** | ✅ Sì (`/usr/share/dirb/`) | ❌No | ❌No | ❌No | ✅ Sì (`db/dicc.txt`) |
| **VHost Fuzzing** | ❌No | ✅ Sì | ✅ Sì | ❌No | ✅ Sì (`--subdirs`) |
| **POST/Fuzzing dei dati** | ❌No | ❌No | ✅ Sì | ❌No | ✅ Sì (`-d`) |
| **Utilizzo memoria/CPU** | Molto basso | Basso | Basso | Molto basso | Moderato |

---

## 📥Installazione

### Kali Linux e sistema operativo Parrot

DIRB è preinstallato in Kali Linux e Parrot OS:
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
### Debian/Ubuntu
```bash
sudo apt update
sudo apt install -y dirb
```
### Compilazione dal sorgente
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 Elenchi di parole predefiniti

Se installato su Kali Linux, DIRB include elenchi di parole dedicati situati in `/usr/share/dirb/wordlists/`:

| Percorso elenco parole | Voci | Scopo |
|---------------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | ~4.614 | Elenco predefinito utilizzato quando non è specificata alcuna lista di parole |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Rapida scoperta di percorsi ad alta probabilità |
| `/usr/share/dirb/wordlists/big.txt` | ~20.469 | Ricerca esaustiva di directory e file |
| `/usr/share/dirb/wordlists/catala.txt` | ~6.846 | Elenco delle parole catalane |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11.720 | Elenco parole spagnole |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1.000 | Variazioni comuni di prefisso/suffisso |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Estensioni web standard (`.php`, `.asp`, `.html`, ecc.) |

### Elenchi di parole di vulnerabilità incorporati (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt`: file specifici di Apache, pagine di stato e directory conosciute
- `iis.txt`: file del server Web Microsoft IIS (`web.config`, `aspnet_client`, ecc.)
- `tomcat.txt` - Percorsi di amministrazione e gestione di Apache Tomcat
- `cgis.txt` - Script ed endpoint CGI (Common Gateway Interface).
-`fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Sintassi di base
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### Esempi essenziali
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 Scenari di scansione comuni

### 1. Scansione con estensioni di file (`-X` e `-x`)

Cerca estensioni di file specifiche allegate a ciascuna parola nel dizionario:
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. Controllo della ricorsione (`-r` e `-R`)

Per impostazione predefinita, DIRB esegue la scansione ricorsivamente di ogni directory rilevata. Puoi controllare questo comportamento:
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. Scansione senza distinzione tra maiuscole e minuscole (`-i`)

Utile quando si prendono di mira server Windows/IIS in cui i percorsi dei file non fanno distinzione tra maiuscole e minuscole:
```bash
dirb http://target.local -i
```
### 4. Ignorare codici di stato HTTP specifici (`-N`)

Filtra i codici di risposta indesiderati (ad esempio, loop di reindirizzamento o percorsi vietati):
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ Opzioni avanzate e ottimizzazione

### Autenticazione (`-u`)

DIRB supporta l'autenticazione HTTP Basic e Digest:
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### Cookie personalizzati e token di sessione (`-c`)

Testare i percorsi delle applicazioni Web autenticate fornendo cookie di sessione:
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### User-Agent personalizzato e intestazioni HTTP (`-a` e `-H`)

Ignora le regole WAF di base o simula browser legittimi:
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### Routing proxy e integrazione della suite Burp (`-p` e `-P`)

Invia traffico DIRB tramite un proxy HTTP upstream come Burp Suite o OWASP ZAP:
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### Limitazione della velocità ed evasione (`-z`)

Evita di attivare avvisi IDS/IPS o DoSing di servizi web fragili aggiungendo un ritardo tra le richieste (in millisecondi):
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### Registrazione output (`-o` e `-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 Caccia alle vulnerabilità con elenchi integrati

DIRB include elenchi mirati in `/usr/share/dirb/wordlists/vulns/` per rilevare server senza patch ed endpoint pericolosi:
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 Flussi di lavoro nel mondo reale

### Flusso di lavoro 1: ricognizione iniziale rapida

Quando incontri per la prima volta un server web sulla porta 80/443:
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### Flusso di lavoro 2: valutazione esaustiva dell'applicazione Web

Esegui una scansione approfondita cercando backup di configurazione, codice sorgente ed endpoint API:
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### Flusso di lavoro 3: scansione del portale autenticata tramite Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 Tabella di consultazione rapida

| Bandiera | Argomento | Descrizione | Esempio |
|------|----------|-------------|---------|
| `-a` | `<user_agent>` | Specificare la stringa agente utente personalizzata | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | Imposta l'intestazione del cookie HTTP | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Aggiungi intestazione HTTP personalizzata | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Nessuno | Ricerca senza distinzione tra maiuscole e minuscole | `dirb http://target -i` |
| `-N` | `<codes>` | Ignora le risposte corrispondenti al codice di stato | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Salva i risultati della scansione nel file | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | Utilizza proxy HTTP | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Credenziali di autenticazione proxy | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Nessuno | Non cercare ricorsivamente | `dirb http://target -r` |
| `-R` | Nessuno | Ricerca ricorsiva interattiva | `dirb http://target -R` |
| `-u` | `<user:pass>` | Autenticazione HTTP Basic/Digest | `dirb http://target -u "admin:1234"` |
| `-w` | Nessuno | Non interrompere la scansione dei messaggi di AVVISO | `dirb http://target -w` |
| `-X` | `<ext_list>` | Aggiungi estensioni separate da virgole | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Aggiungi estensioni dal file | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Aggiungi ritardo tra le richieste | `dirb http://target -z 250` |

---

## 💡 Suggerimenti e migliori pratiche

1. **Attenzione alle risposte con caratteri jolly 200/302:**
   - Alcune applicazioni Web restituiscono HTTP 200 o 302 per ogni richiesta (gestore 404 personalizzato). DIRB riporterà ogni singola voce come trovata. Utilizza `-N 302` o passa a `ffuf` / `feroxbuster` per il filtraggio avanzato di dimensioni/parole.
2. **Combina con `-r` per dizionari di grandi dimensioni:**
   - Se si utilizza `directory-list-2.3-medium.txt` (220k parole), disabilita sempre la ricorsione con `-r`. In caso contrario, DIRB potrebbe impiegare giorni per eseguire la scansione delle directory profondamente annidate.
3. **Controlla sempre le estensioni:**
   - I backend moderni espongono spesso file sensibili come `.env`, `.git`, `.bak`, `.conf` o `.sql`. Testare sempre le estensioni comuni con `-X`.
4. **Utilizza `-w` per scansioni non presidiate:**
   - Per impostazione predefinita, DIRB potrebbe essere messo in pausa se rileva avvisi SSL o di rete. Il superamento di `-w` garantisce il completamento dei test senza interazione manuale.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** DIRB è progettato per il controllo di sicurezza, i test di penetrazione e la ricerca etica sui sistemi che possiedi o che hai un'autorizzazione esplicita e documentata per testare. Gli attacchi con dizionario non autorizzati contro i server Web possono causare l'interruzione del servizio e sono illegali ai sensi della legislazione sulla criminalità informatica in tutto il mondo.

---

## 📚 Risorse

- [Progetto DIRB SourceForge](https://sourceforge.net/projects/dirb/)
- [Documentazione del pacchetto DIRB di Kali Linux](https://www.kali.org/tools/dirb/)
- [SecLists - Elenchi di parole per la scoperta dei contenuti Web](https://github.com/danielmiessler/SecLists)