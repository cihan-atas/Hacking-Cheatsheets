<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Foglio informativo dnsrecon

[dnsrecon](https://github.com/darkoperator/dnsrecon) è uno strumento completo di ricognizione ed enumerazione DNS scritto in Python. Supporta trasferimenti di zona (AXFR), ricerche inverse, forzatura bruta del dominio, rilevamento di record SRV, rilevamento di caratteri jolly, snooping della cache ed espansione TLD.

---

## Sommario

- [Funzionalità principali](#capacità-core)
- [Installazione](#installazione)
- [Enumerazione standard e trasferimenti di zona (`-d -t std / axfr`)](#enumerazione-standard--trasferimenti-zona--d--t-std--axfr)
- [Forzatura bruta del sottodominio DNS (`-t brt`)](#dns-subdomain-brute-forcing--t-brt)
- [Scansione intervallo di ricerca inversa (`-t rvl`)](#scansione-intervallo-ricerca-inversa--t-rvl)
- [Snooping cache DNS (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [Mirsing server dei nomi specifici (`-n`)](#targeting-nameservers-specifici--n)
- [Formati di output (CSV, JSON, SQLite, XML)](#output-formats-csv-json-sqlite-xml)
- [Rafforzamento difensivo per server DNS](#rafforzamento-difensivo-per-server-dns)

---

## Funzionalità principali

- **Zone Transfer Testing (AXFR):** testa automaticamente tutti i server dei nomi autorevoli per rilevare eventuali trasferimenti di zona non autenticati.
- **Enumerazione record:** recupera i record SOA, NS, A, AAAA, MX, TXT, SPF e SRV.
- **Sottodominio Brute-Force:** Ricerche di dizionari multi-thread con filtro con caratteri jolly.
- **Scansione inversa dell'intervallo IP:** Estrazione di record PTR su blocchi CIDR arbitrari.
- **Snooping della cache:** verifica se i server dei nomi ricorsivi hanno domini specifici memorizzati nella cache della memoria.
- **Espansione TLD:** scopre i domini corrispondenti tra codici paese e domini generici di primo livello.

---

## Installazione
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

## Enumerazione standard e trasferimenti di zona (`-d -t std / axfr`)

### 1. Enumerazione dei record DNS standard
Raccoglie record SOA, NS, A, AAAA, MX e TXT e tenta automaticamente i trasferimenti di zona contro ciascun NS autorevole:
```bash
dnsrecon -d example.com -t std
```
### 2. Solo test di trasferimento di zona dedicata (AXFR).
Testa solo la vulnerabilità AXFR contro tutti i nameserver autorevoli:
```bash
dnsrecon -d example.com -t axfr
```
*In caso di successo, l'intero database DNS della destinazione viene scaricato senza forza bruta.*

---

## Forzatura bruta del sottodominio DNS (`-t brt`)

Sottodomini a forza bruta utilizzando elenchi di parole personalizzati o predefiniti:

### 1. Sottodominio Brute-Force con elenco di parole personalizzato
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: dominio di destinazione.
- `-t brt`: modalità forza bruta.
- `-D <file>`: file dizionario del sottodominio.

### 2. Forza bruta con filtro con caratteri jolly
Impedisce alle configurazioni DNS con caratteri jolly (`*.example.com` che si risolve in un singolo IP catch-all) di inquinare i risultati:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: ignora i risultati della risoluzione dei caratteri jolly.

---

## Scansione intervallo di ricerca inversa (`-t rvl`)

Enumerare i record PTR per un blocco IP per identificare host interni, gateway di posta e istanze di gestione temporanea:
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: intervallo IP nella notazione CIDR (ad esempio `192.168.1.0/24`) o nella notazione inizio-fine (`192.168.1.1-192.168.1.254`).
- `-t rvl`: modalità di ricerca inversa.

---

## Snooping della cache DNS (`-t snoop`)

Determina se i client su una rete di destinazione hanno recentemente risolto domini specifici tramite il loro risolutore interno:
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: server DNS di destinazione da interrogare.
- `-D <domains_file>`: elenco di domini da testare (ad esempio concorrenti, C2 dannosi, portali interni).

---

## Targeting di server dei nomi specifici (`-n`)

Interroga direttamente i server DNS interni o specifici della destinazione anziché i risolutori upstream pubblici:
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## Formati di output (CSV, JSON, SQLite, XML)

Salva i risultati per l'integrazione con ticketing, SIEM o ulteriori fasi di attacco:

### 1. Esporta in JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2. Esporta nel database SQLite
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. Esporta in CSV e XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## Rafforzamento difensivo per server DNS

| Vulnerabilità | Rischio di sfruttamento | Difesa consigliata |
|---|---|---|
| **AXFR senza restrizioni** | Mappatura completa dell'infrastruttura interna | Limita i trasferimenti di zona solo agli IP dei server dei nomi secondari autorizzati (`allow-transfer { 10.0.0.2; };` in BIND). |
| **Apri risolutore ricorsivo** | Amplificazione DNS DDoS e snooping | Disabilita la ricorsione esterna (`recursion no;` in BIND). |
| **Nomi host prevedibili** | Sottodominio forzatura bruta | Utilizza sottodomini randomizzati per ambienti di staging sensibili. |
| **Acquisizione del sottodominio** | CNAME che punta a servizi AWS/GitHub/Azure non rivendicati | Controlla i record DNS CNAME penzolanti e rimuovi immediatamente le mappature inutilizzate. |