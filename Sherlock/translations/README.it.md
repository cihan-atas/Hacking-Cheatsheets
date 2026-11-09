<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Sherlock Cheatsheet

[Sherlock](https://github.com/sherlock-project/sherlock) è uno strumento di ricognizione OSINT che dà la caccia agli account dei social media in base al nome utente su più di 400 piattaforme social, forum, siti di hosting di codice e comunità online contemporaneamente.

---

## Sommario

- [Funzionalità chiave](#capacità-chiave)
- [Installazione](#installazione)
- [Ricerca nome utente singolo e multiplo](#single--multiple-username-search)
- [Filtrare siti e piattaforme](#filtrare-siti--piattaforme)
- [Proxy e routing Tor](#proxy--tor-routing)
- [Ottimizzazione timeout e threading](#timeout--ottimizzazione del threading)
- [Formati di esportazione (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
- [Automazione e integrazione della pipeline OSINT](#automazione--osint-pipeline-integrazione)

---

## Funzionalità chiave

- **Oltre 400 piattaforme:** interroga GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin e centinaia di altre.
- **Concorrenza asincrona:** Scansione parallela ad alta velocità utilizzando richieste Python asincrone.
- **Rilevamento accurato:** rileva codici di stato HTTP, messaggi di errore e reindirizzamenti per evitare falsi positivi.
- **Supporto Tor e SOCKS5:** protegge l'anonimato degli investigatori e aggira i limiti di velocità.

---

## Installazione
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## Ricerca di nomi utente singoli e multipli

### 1. Ricerca di base del nome utente
Cerca un singolo handle di destinazione in tutti gli oltre 400 siti:
```bash
sherlock target_user
```
### 2. Cerca più nomi utente contemporaneamente
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. Stampa solo account trovati (elimina Non trovato/Errori)
```bash
sherlock --print-found target_user
```
---

## Filtraggio di siti e piattaforme

Restringi le ricerche a piattaforme specifiche per risparmiare tempo o concentrarti su categorie di intelligence specifiche:

### 1. Effettua query solo su siti specifici
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. Ricerca senza piattaforme specifiche (ignora i siti pesanti falsi positivi)
```bash
sherlock target_user --site-list targets.txt
```
---

## Proxy e routing Tor

Per prevenire ban IP, limiti di velocità e mantenere la sicurezza operativa:

### 1. Percorso attraverso la rete Tor
Instrada automaticamente le richieste tramite il proxy Tor SOCKS locale (`127.0.0.1:9050`):
```bash
sherlock --tor target_user
```
### 2. Proxy HTTP/SOCKS5 personalizzato
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## Timeout e ottimizzazione del threading

Controlla le velocità delle richieste e le tolleranze di latenza:
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## Formati di esportazione (CSV, XLSX, JSON)

Salva le scoperte per indagini, pacchetti di prove o report:

### 1. Salva direttamente nel file CSV
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. Salva l'output in una directory specifica
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Genera `/tmp/recon_data/target_user.txt` contenente gli URL completi dei profili confermati.

### 3. Salva come foglio di calcolo Excel (XLSX)
```bash
sherlock --xlsx target_user
```
---

## Automazione e integrazione della pipeline OSINT

Incatena Sherlock in uno script di investigatore automatizzato:
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```