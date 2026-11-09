<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# wafw00f Cheatsheet

[wafw00f](https://github.com/EnableSecurity/wafw00f) è uno strumento di rilevamento e rilevamento delle impronte digitali di Web Application Firewall (WAF) da riga di comando open source. Identifica e rileva oltre 150 soluzioni di sicurezza WAF commerciali e open source che proteggono i siti Web di destinazione inviando richieste HTTP normali e provocatorie e analizzando risposte comportamentali, intestazioni e cookie.

---

## Sommario

- [Perché i WAF per impronte digitali?](#perché-waf per impronte digitali)
- [Installazione](#installazione)
- [Scansione di base e target](#basic--target-scanning)
- [Rilevamento esaustivo multi-WAF (`-a`)](#rilevamento esaustivo-multi-waf--a)
- [Scansione in blocco dei target da file (`-i`)](#scansione in blocco dei target da file--i)
- [Proxy e routing Tor (`-p`)](#proxy--tor-routing--p)
- [Formati di esportazione (JSON, CSV, testo)](#export-formats-json-csv-text)
- [Elenco firme WAF supportate (`-l`)](#list-supported-waf-signatures--l)
- [Integrazione Bug Bounty e Penetration Testing](#bug-bounty--penetration-testing-integration)

---

## Perché i WAF per impronte digitali?

Il lancio di attacchi web attivi (SQL injection, XSS, SSRF) contro un obiettivo protetto senza previa ricognizione spesso si traduce in ban istantanei di IP o risultati di vulnerabilità falsi negativi. L'identificazione dell'esatto WAF consente:
- **Evasione su misura:** selezione di tecniche di bypass specifiche del fornitore (ad esempio, normalizzazione Unicode per ModSecurity, codifica di trasferimento in blocchi per Cloudflare).
- **Ricerca IP di origine:** se viene rilevato Cloudflare o Akamai, ruotare per trovare l'IP di origine del backend prima del test.
- **Consapevolezza soglia regola:** ottimizzazione della concorrenza dello scanner e dei limiti di velocità al di sotto delle soglie di blocco.

---

## Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## Scansione di base e di destinazione

### 1. Scansiona un singolo dominio
```bash
wafw00f https://example.com
```
*wafw00f invia richieste HTTP di base seguite da modelli dannosi personalizzati per attivare filtri di sicurezza, identificando firme nelle intestazioni, codici di stato e pagine di errore.*

### 2. Modalità di output dettagliata (`-v` / `-vv`)
Ispeziona intestazioni corrispondenti, codici di stato HTTP ed euristiche specifiche:
```bash
wafw00f https://example.com -v
```
### 3. Disattiva il reindirizzamento successivo (`-r`)
Impedisci a wafw00f di seguire reindirizzamenti 301/302 per separare i proxy inversi edge dalle applicazioni backend:
```bash
wafw00f https://example.com -r
```
---

## Rilevamento multi-WAF esaustivo (`-a`)

Per impostazione predefinita, wafw00f interrompe la scansione non appena viene confermata la prima corrispondenza WAF. Utilizza `-a` per controllare tutte le firme dei fornitori noti (essenziale quando un'applicazione si trova dietro una difesa multilivello come Cloudflare CDN + AWS WAF):
```bash
wafw00f https://example.com -a
```
---

## Scansione in blocco dei target da file (`-i`)

Scansiona un intero elenco di sottodomini o risorse di destinazione raccolti da `subfinder` o `amass`:
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## Instradamento proxy e Tor (`-p`)

Instrada il traffico di sondaggio attraverso i proxy per prevenire l'inserimento nella lista nera degli IP o ispezionare le query in Burp Suite:

### 1. Instradare attraverso il proxy Burp Suite
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. Instradamento tramite proxy Tor SOCKS5 locale
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## Formati di esportazione (JSON, CSV, testo)

Esporta i risultati per il reporting automatizzato o gli strumenti di ricognizione a valle:

### 1. Esporta in formato JSON
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. Esporta in un foglio di calcolo CSV
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## Elenco delle firme WAF supportate (`-l`)

Visualizza tutti gli oltre 150 prodotti WAF rilevabili:
```bash
wafw00f -l
```
I principali prodotti supportati includono:
- **Fornitori cloud:** Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.
- **Appliance aziendali:** F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Open Source/Software:** ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Integrazione di Bug Bounty e Penetration Testing

Integra wafw00f in una pipeline di attacco web automatizzata:
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```