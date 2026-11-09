<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# Searchsploit Cheatsheet

[Searchsploit](https://www.exploit-db.com/searchsploit) er det offisielle kommandolinjeverktøyet for [Exploit-Database](https://www.exploit-db.com/). Den lar sikkerhetseksperter, penetrasjonstestere og sårbarhetsforskere utføre offline søk på tvers av titusenvis av publiserte utnyttelser, shellcodes og whitepapers uten å sende nettverkstrafikk til tredjeparts søkemotorer.

---

## Innholdsfortegnelse

- [Oversikt og arkitektur](#oversikt--arkitektur)
- [Installasjon og lokale databaseoppdateringer](#installation--local-database-updates)
– [Basic & Advanced Exploit Searching](#basic--advanced-exploit-searching)
- [Undersøker og kopierer utnyttelser (`-m` / `-p` / `-x`)](#undersøker--kopierer-utnytter--m---p---x)
- [Filtrering etter plattform og type](#filtrering etter plattform--type)
- [Nmap XML / Grepable Scanning Integration](#nmap-xml--grepable-scanning-integration)
- [JSON Output & Script Automation](#json-output--script-automation)
– [Beste fremgangsmåter for penetrasjonstesting](#best-practices-in-penetration-testing)

---

## Oversikt og arkitektur

Searchsploit spør etter en lokal git-klon av Exploit-DB lagret i `/usr/share/exploitdb/`.
- **Null nettverksfingeravtrykk:** Søk kjøres mot lokale CSV- og katalogstrukturer.
- **Offline-pålitelighet:** Fullt funksjonell i luftgappede målmiljøer og engasjementsnettverk.
- **Integrert speiling:** Kopierer utnyttelsesfiler direkte til gjeldende arbeidskatalog for umiddelbar gjennomgang og endring.

---

## Installasjon og lokale databaseoppdateringer

### Debian / Ubuntu / Kali Linux / Parrot OS
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Git Repository Installasjon (enhver Linux-distribusjon)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## Grunnleggende og avansert utnyttelsessøking

### 1. Enkelt søkeordsøk
Søk etter spesifikke tjenester, demoner eller programvare:
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. Eksakt tittelmatching (`-t`)
Begrens søk strengt for å utnytte titler for å eliminere støyende beskrivelsestreff:
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. Strikt samsvar mellom store og små bokstaver og termer (`-e`)
Finn eksakte versjoner og mønstertreff:
```bash
searchsploit -e "Apache Struts"
```
### 4. Søk etter CVE Identifier
Forespørselsutnyttelser tilordnet spesifikke Common Vulnerabilities and Exposures-numre:
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. Ekskluder uønskede resultater (`--exclude`)
Filtrer ut tjenestenekt (DoS) eller hjelpeskript når du søker ekstern kjøring av kode (RCE):
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## Undersøker og kopierer utnyttelser (`-m` / `-p` / `-x`)

Aldri rediger eller utfør utnyttelser direkte i `/usr/share/exploitdb/`-katalogen. Bruk Searchsploits kopierings- og inspeksjonsfunksjoner.

### 1. Kopier (speil) utnyttelse til gjeldende katalog (`-m`)
Kopier en utnyttelses PoC-fil (Python, C, Ruby, bash) direkte til din lokale arbeidskatalog:
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. Inspiser utnyttelsesmetadata og fullstendig bane (`-p`)
Vis den absolutte filsystembanen og Exploit-DB URL for en EDB-ID:
```bash
searchsploit -p 50383
```
Produksjon:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. Les Exploit Code i Terminal Personsøker (`-x`)
Åpne utnyttelseskoden direkte i `less` for rask inspeksjon:
```bash
searchsploit -x 50383
```
---

## Filtrering etter plattform og type

Searchsploit indekserer utnyttelser, shellcode og forskningsartikler på tvers av flere arkitekturer:

### 1. Filtrer etter måloperativsystem
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. Filter kun for rå skallkoder (`-s`)
Slå opp arkitekturspesifikke shellcode-nyttelaster (f.eks. execve `/bin/sh` uten nullbyte):
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. Filter for sikkerhetspapirer og whitepapers (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Nmap XML / Grepable Scanning Integration

Searchsploit kan automatisk analysere Nmap XML-skanneutdata og matche oppdagede programvareversjoner mot kjente utnyttelser:
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Searchsploit inspiserer alle åpne portbannere og versjonsstrenger, og spør etter lokale utnyttelser for umiddelbar triage.*

---

## JSON Output & Script Automation

For rørledningsautomatisering og tilpassede rekognoseringsverktøy, resulterer utdata i strukturert JSON:
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
Filtrer utnyttelsesfilbaner ved hjelp av `jq`:
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## Beste praksis for penetrasjonstesting

1. **Bekreft før kjøring:** Inspiser alltid utnyttelseskildekoden (`searchsploit -x <id>`) før kompilering eller kjøring. Bekreft hardkodet skallkode, målrett IP-adresser og sørg for at det ikke er noen bakdører eller ondsinnede nyttelaster.
2. **Kompiler med riktige flagg:** Mange Exploit-DB C-utnyttelser krever spesifikke GCC-flagg (f.eks. `-m32`, `-lpthread` eller `-lcrypto`):
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **Hold databasen oppdatert:** Kjør `searchsploit -u` regelmessig for å sikre at nylig publiserte zero-days og proof-of-concepts indekseres lokalt.