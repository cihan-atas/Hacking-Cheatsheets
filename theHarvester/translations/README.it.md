<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# TheHarvester Cheatsheet

[theHarvester](https://github.com/laramies/theHarvester) è uno degli strumenti di intelligence open source (OSINT) più utilizzati per raccogliere e-mail, sottodomini, nomi di dipendenti, host virtuali, porte aperte e chiavi PGP dei dipendenti da diverse fonti pubbliche (motori di ricerca, server di chiavi PGP, Shodan, Censys, Certspotter, ecc.).

---

## Sommario

- [Origini dati supportate](#supported-data-sources)
- [Installazione e configurazione della chiave API](#installazione--configurazione-chiave-api)
- [Riconoscimento dominio base](#ricognizione-dominio-base)
- [Raccolta passiva multi-fonte](#raccolta passiva-multi-fonte)
- [Verifica DNS attiva e forzatura bruta](#active-dns-verification--brute-forcing)
- [Formati di output (HTML, XML, JSON)](#formati-output-html-xml-json)
- [Integrazione del flusso di lavoro Pentest](#pentest-integrazione del flusso di lavoro)

---

## Origini dati supportate

theHarvester aggrega informazioni da dozzine di fonti passive e attive:
- **Motori di ricerca (gratuiti/nessuna chiave):** Google, Bing, DuckDuckGo, Yahoo, Baidu.
- **Trasparenza del certificato:** CRT.sh, Certspotter.
- **Repository di codici e minacce:** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **API di ricognizione e OSINT:** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **Rete/DNS:** Anubis, HackerTarget, DNSDumpster.

---

## Installazione e configurazione della chiave API

### 1. Installazione
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
### 2. Configurare le chiavi API (`api-keys.yaml`)
Molte fonti (Hunter, Shodan, Censys, Intelx, GitHub) richiedono chiavi API gratuite o a pagamento.
Crea o modifica `api-keys.yaml` nel percorso di configurazione:
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

## Ricognizione di base del dominio

### 1. Raccogli email e sottodomini utilizzando motori gratuiti
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: dominio di destinazione da ricognire.
- `-l <limit>`: Numero di risultati da interrogare per motore.
- `-b <sources>`: elenco di fonti separate da virgole (o `all`).

### 2. Interrogare tutte le origini configurate
```bash
theHarvester -d example.com -l 500 -b all
```
---

## Raccolta passiva multi-sorgente

Esegui query su specifici database di intelligence sulle minacce e di trasparenza dei certificati senza inviare traffico alla destinazione:

### 1. Trasparenza dei certificati e informazioni sulle minacce
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. Puntare a gruppi di fonti specifici
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## Verifica DNS attiva e forzatura bruta

### 1. Risolvi i sottodomini rilevati (`-r`)
Esegui ricerche DNS attive per verificare se i sottodomini raccolti sono attualmente attivi:
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. Forza bruta DNS (`-c`)
Esegui il brute-forcing dei sottodomini DNS basato su elenco di parole direttamente all'interno di theHarvester:
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3. Verifica dell'host virtuale (`-v`)
Verifica gli host virtuali tramite richieste HTTP:
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## Formati di output (HTML, XML, JSON)

Esporta i risultati per il reporting automatizzato o la ricognizione a valle:

### 1. Esporta in HTML e XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
Crea `/tmp/theharvester_results.html` e `/tmp/theharvester_results.xml`.

### 2. Analizzare i sottodomini rilevati con `jq`
Se esporti o analizzi JSON:
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## Integrazione del flusso di lavoro Pentest

Concatena i risultati di theHarvester direttamente con gli scanner di vulnerabilità attivi:
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