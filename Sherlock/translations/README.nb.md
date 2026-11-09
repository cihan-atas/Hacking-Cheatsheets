<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# Sherlock Cheatsheet

[Sherlock](https://github.com/sherlock-project/sherlock) er et OSINT-rekognoseringsverktøy som leter etter sosiale mediekontoer etter brukernavn på mer enn 400 sosiale plattformer, fora, kodevertssider og nettsamfunn samtidig.

---

## Innholdsfortegnelse

- [Key Capabilities](#key-capabilities)
- [Installasjon](#installation)
- [Enkelt- og flere brukernavnsøk](#single--multiple-username-search)
- [Filtrering av nettsteder og plattformer](#filtreringsnettsteder--plattformer)
- [Proxy- og Tor-ruting](#proxy--tor-ruting)
- [Timeout & Threading Tuning](#timeout--threading-tuning)
- [Eksporter formater (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
- [Automasjon og OSINT Pipeline Integration](#automation--osint-pipeline-integration)

---

## Nøkkelfunksjoner

- **400+ plattformer:** Spørringer GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin og hundrevis flere.
- **Asynkron samtidighet:** Høyhastighets parallellskanning ved bruk av asynkrone Python-forespørsler.
- **Nøyaktig deteksjon:** Oppdager HTTP-statuskoder, feilmeldinger og omdirigeringer for å unngå falske positiver.
- **Tor & SOCKS5-støtte:** Beskytter etterforskers anonymitet og omgår hastighetsgrenser.

---

## Installasjon
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

## Søk etter enkelt og flere brukernavn

### 1. Grunnleggende brukernavnsøk
Søk etter ett enkelt målhåndtak på tvers av alle 400+ nettsteder:
```bash
sherlock target_user
```
### 2. Søk i flere brukernavn samtidig
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. Skriv ut kun funnet kontoer (undertrykk ikke funnet / feil)
```bash
sherlock --print-found target_user
```
---

## Filtrering av nettsteder og plattformer

Begrens søk til spesifikke plattformer for å spare tid eller fokusere på spesifikke intelligenskategorier:

### 1. Bare spør etter spesifikke nettsteder
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. Søk uten spesifikke plattformer (Ignorer falsk-positive tunge nettsteder)
```bash
sherlock target_user --site-list targets.txt
```
---

## Proxy- og Tor-ruting

For å forhindre IP-forbud, takstgrenser og opprettholde driftssikkerhet:

### 1. Rute gjennom Tor-nettverket
Rut forespørsler automatisk gjennom lokal Tor SOCKS-proxy (`127.0.0.1:9050`):
```bash
sherlock --tor target_user
```
### 2. Egendefinert HTTP / SOCKS5 Proxy
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## Tidsavbrudd og trådjustering

Kontroller forespørselshastigheter og latenstidstoleranser:
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## Eksportformater (CSV, XLSX, JSON)

Lagre funn for undersøkelser, bevispakker eller rapportering:

### 1. Lagre direkte til CSV-fil
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. Lagre utdata i spesifikk katalog
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Genererer `/tmp/recon_data/target_user.txt` som inneholder fullstendige URL-er til bekreftede profiler.

### 3. Lagre som Excel-regneark (XLSX)
```bash
sherlock --xlsx target_user
```
---

## Automatisering og OSINT Pipeline Integration

Lede Sherlock inn i et automatisert etterforskermanus:
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