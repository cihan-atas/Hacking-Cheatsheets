<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Cheatsheet di Searchsploit

[Searchsploit](https://www.exploit-db.com/searchsploit) è l'utilità della riga di comando ufficiale per [Exploit-Database](https://www.exploit-db.com/). Consente ai professionisti della sicurezza, ai penetration tester e ai ricercatori di vulnerabilità di condurre ricerche offline su decine di migliaia di exploit, shellcode e white paper pubblicati senza inviare traffico di rete a motori di ricerca di terze parti.

---

## Sommario

- [Panoramica e architettura](#panoramica--architettura)
- [Installazione e aggiornamenti del database locale](#installazione--local-database-updates)
- [Ricerca di exploit di base e avanzata](#basic--advanced-exploit-searching)
- [Esaminazione e copia degli exploit (`-m` / `-p` / `-x`)](#esame--copia-exploit--m---p---x)
- [Filtro per piattaforma e tipo](#filtro per piattaforma--tipo)
- [Integrazione scansione Nmap XML/Grepable](#nmap-xml--grepable-scanning-integration)
- [Output JSON e automazione degli script](#json-output--script-automation)
- [Migliori pratiche nei test di penetrazione](#migliori pratiche nei test di penetrazione)

---

## Panoramica e architettura

Searchsploit interroga un clone git locale di Exploit-DB archiviato in `/usr/share/exploitdb/`.
- **Zero Network Fingerprint:** le ricerche vengono eseguite su CSV locali e strutture di directory.
- **Affidabilità offline:** Pienamente funzionale in ambienti target e reti di coinvolgimento con air gap.
- **Mirroring integrato:** copia i file degli exploit direttamente nella directory di lavoro corrente per revisione e modifica immediate.

---

## Installazione e aggiornamenti del database locale

### Sistema operativo Debian/Ubuntu/Kali Linux/Parrot
```bash
# Kali and Parrot include searchsploit by default
sudo apt-get update && sudo apt-get install -y exploitdb

# Update the local exploit database archive
searchsploit -u
```
### Installazione del repository Git (qualsiasi distribuzione Linux)
```bash
git clone https://gitlab.com/exploit-database/exploitdb.git /opt/exploitdb
sudo ln -sf /opt/exploitdb/searchsploit /usr/local/bin/searchsploit

# Check version and status
searchsploit -v
```
---

## Ricerca di exploit di base e avanzata

### 1. Ricerca semplice per parole chiave
Cerca servizi, demoni o software specifici:
```bash
searchsploit apache 2.4.49
searchsploit openssh 7.2
searchsploit wordpress core 5.8
```
### 2. Corrispondenza esatta del titolo (`-t`)
Limita rigorosamente le query di ricerca per sfruttare i titoli ed eliminare le corrispondenze rumorose delle descrizioni:
```bash
searchsploit -t "Privilege Escalation" linux kernel
searchsploit -t "Remote Code Execution" smb
```
### 3. Corrispondenza rigorosa di maiuscole e termini (`-e`)
Trova versioni esatte e corrispondenze di modelli:
```bash
searchsploit -e "Apache Struts"
```
### 4. Ricerca per identificatore CVE
Esegui query sugli exploit associati a specifici numeri di vulnerabilità ed esposizioni comuni:
```bash
searchsploit --cve 2021-44228
searchsploit --cve 2017-0144
```
### 5. Escludi risultati indesiderati (`--exclude`)
Filtra gli script denial-of-service (DoS) o ausiliari durante la ricerca dell'esecuzione di codice remoto (RCE):
```bash
searchsploit linux kernel 5.4 --exclude="Denial of Service"
```
---

## Esame e copia degli exploit (`-m` / `-p` / `-x`)

Non modificare o eseguire mai exploit direttamente nella directory `/usr/share/exploitdb/`. Utilizza le funzioni di copia e ispezione di Searchsploit.

### 1. Copia (mirror) dell'exploit nella directory corrente (`-m`)
Copia un file PoC dell'exploit (Python, C, Ruby, bash) direttamente nella directory di lavoro locale:
```bash
# Mirror by Exploit-DB ID (EBD-ID)
searchsploit -m 50383

# Mirror by search path pattern
searchsploit -m windows/remote/48537.py
```
### 2. Esamina i metadati dell'exploit e il percorso completo (`-p`)
Visualizza il percorso assoluto del filesystem e l'URL Exploit-DB per un ID EDB:
```bash
searchsploit -p 50383
```
Produzione:
```text
  Exploit: Apache HTTP Server 2.4.49 - Path Traversal & Remote Code Execution (RCE)
      URL: https://www.exploit-db.com/exploits/50383
     Path: /usr/share/exploitdb/exploits/multiple/webapps/50383.sh
File Type: Bourne-Again shell script, ASCII text executable
```
### 3. Leggi il codice exploit nel cercapersone del terminale (`-x`)
Apri il codice dell'exploit direttamente in `less` per un'ispezione rapida:
```bash
searchsploit -x 50383
```
---

## Filtraggio per piattaforma e tipo

Searchsploit indicizza exploit, shellcode e documenti di ricerca su più architetture:

### 1. Filtra per sistema operativo di destinazione
```bash
# Search only Linux exploits
searchsploit linux kernel 4.4

# Search only Windows exploits
searchsploit windows smb eternal

# Search web applications
searchsploit webapps tomcat
```
### 2. Filtra solo per codici Shell grezzi (`-s`)
Cerca i payload dello shellcode specifici dell'architettura (ad esempio execve `/bin/sh` senza byte null):
```bash
searchsploit -s linux/x64 "execve"
searchsploit -s windows/x86 "reverse shell"
```
### 3. Filtra per documenti sulla sicurezza e whitepaper (`-w`)
```bash
searchsploit -w "Active Directory Kerberos"
```
---

## Integrazione scansione Nmap XML/Grepable

Searchsploit può analizzare automaticamente gli output della scansione XML di Nmap e abbinare le versioni del software scoperte con gli exploit noti:
```bash
# 1. Perform service detection and save XML output
nmap -sV -p- -T4 -oX /tmp/target_scan.xml 192.168.1.50

# 2. Feed the XML scan file directly to Searchsploit
searchsploit --nmap /tmp/target_scan.xml
```
*Searchsploit ispeziona ogni banner di porta aperta e stringa di versione, interrogando gli exploit locali per un triage immediato.*

---

## Output JSON e automazione degli script

Per l'automazione della pipeline e gli strumenti di ricognizione personalizzati, l'output risulta in JSON strutturato:
```bash
searchsploit "Apache Struts" --json > struts_exploits.json
```
Filtra i percorsi dei file exploit utilizzando `jq`:
```bash
searchsploit --cve 2021-41773 --json | jq -r '.RESULTS_EXPLOIT[].Path'
```
---

## Migliori pratiche nei test di penetrazione

1. **Verifica prima dell'esecuzione:** ispeziona sempre il codice sorgente dell'exploit (`searchsploit -x <id>`) prima della compilazione o dell'esecuzione. Verifica lo shellcode hardcoded, scegli gli indirizzi IP di destinazione e assicurati che non siano presenti backdoor o stager di payload dannosi.
2. **Compila con flag corretti:** Molti exploit Exploit-DB C richiedono flag GCC specifici (ad esempio, `-m32`, `-lpthread` o `-lcrypto`):
   ```bash
   searchsploit -m 40839
   gcc -pthread 40839.c -o dirtycow -lcrypt
   ```
3. **Mantieni aggiornato il database:** esegui `searchsploit -u` regolarmente per garantire che gli zero-day e i proof-of-concept appena pubblicati siano indicizzati localmente.