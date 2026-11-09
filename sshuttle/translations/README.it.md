<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ sshuttle - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<palign="centro">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivoting e tunneling">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Protocolli">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="Licenza LGPLv2.1">
</p>

<palign="centro">
  <b>🌐 Inoltra intere sottoreti interne e richieste DNS tramite una sessione SSH non privilegiata senza accesso amministrativo sul server remoto</b>
</p>

---

## 📋 Sommario

- [Panoramica e filosofia della "VPN dei poveri"](#-panoramica-filosofia-vpn-dei-poveri)
- [Come funziona sshuttle](#-how-sshuttle-funziona)
- [Confronto strumenti: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Installazione](#-installazione)
- [Routing sottorete di base](#-basic-subnet-routing)
- [Inoltro traffico DNS (`--dns`)](#-dns-inoltro-traffico---dns)
- [Reindirizzamento completo del gateway (percorso predefinito)](#-full-gateway-redirection-default-route)
- [Esclusioni sottorete (`-x`)](#-esclusioni sottorete--x)
- [Opzioni SSH e autenticazione basata su chiave (`-e`)](#-ssh-options--key-based-authentication--e)
- [Esecuzione in background del demone (`-D`)](#-demone-esecuzione-in background--d)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e filosofia della "VPN dei poveri".

**sshuttle** è un server proxy trasparente che funziona come una VPN leggera e senza configurazione su una connessione SSH standard.

Nei tipici penetration test aziendali e nelle valutazioni del Red Team, il passaggio alle reti interne è ostacolato da vincoli rigorosi:
- ❌ Raramente possiedi l'accesso `root` o `SYSTEM` sull'host bastion/pivot SSH iniziale.
- ❌ Le soluzioni VPN tradizionali (OpenVPN, WireGuard) richiedono diritti amministrativi e dispositivi di rete TUN/TAP virtuali su entrambi gli endpoint.
- ❌ I proxy SOCKS standard (come `ssh -D`) richiedono la configurazione di `proxychains` o delle impostazioni proxy dell'applicazione individuale per ogni singolo strumento.

**sshuttle risolve tutte e tre le limitazioni contemporaneamente:**
- ✅ **Non è richiesto alcun root remoto:** Il server pivot necessita solo di un'installazione Python standard (`python3`).
- ✅ **Non è necessario alcun TUN/TAP remoto:** utilizza il multiplexing del flusso TCP puro sulla sessione SSH.
- ✅ **Routing trasparente a livello di sistema:** Modifica `iptables` / `nftables` locale sul box dell'aggressore, consentendo agli strumenti (`nmap`, `curl`, `smbclient`, browser web) di prendere di mira gli IP interni direttamente senza wrapper!

---

## ⚙️ Come funziona sshuttle
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ Confronto strumenti: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| Caratteristica | `sshuttle` | CALZE (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|-----------||------------|-------------|-----------|
| **Root remoto richiesto** | ❌No | ❌No | ❌No | ✅ Sì |
| **Radice locale richiesta** | ✅ Sì (iptables) | ❌No | ✅ Sì (interfaccia TUN) | ✅ Sì |
| **Routing trasparente** | ✅ Sì | ❌ Richiede Proxychains | ✅ Sì (TUN nativo) | ✅ Sì |
| **Supporto traffico UDP** | ❌ Solo DNS | ❌No | ✅ Sì | ✅UDP completo |
| **Richiede Python su Target**| ✅ Sì (Python 3) | ❌No | ❌ No (agente autonomo)| ❌No |
| **Nmap SYN Scan (`-sS`)** | ❌ Connessione TCP (`-sT`)| ❌ Solo connessione TCP | ✅ Supporto per pacchetti grezzi | ✅Pacchetti crudi interi |

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 Routing sottorete di base

Inoltra un'intera sottorete remota di Classe C tramite l'host pivot SSH:
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
Una volta connesso potrai comunicare direttamente con le macchine interne:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 Inoltro del traffico DNS (`--dns`)

Inoltra le query DNS aziendali interne (`.corp`, `.local`) al server DNS interno della rete remota:
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 Reindirizzamento completo del gateway (percorso predefinito)

Instradare tutto il traffico dell'aggressore in uscita attraverso la connessione SSH (comportamento VPN completo):
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 Esclusioni sottorete (`-x`)

Impedire l'acquisizione di sottoreti sensibili o di loopback specifiche:
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 Opzioni SSH e autenticazione basata su chiave (`-e`)

Passa flag SSH personalizzati (chiavi di identità private, porte SSH non standard):
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 Esecuzione del demone in background (`-D`)

Esegui sshuttle come demone in background durante gli impegni in corso:
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Instradamento sottorete singola** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Instrada la sottorete con DNS** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Instrada più sottoreti** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **Escludi IP/Sottorete** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Reindirizzamento completo del traffico** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Chiave/porta SSH personalizzata** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Esegui in background** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Modifica del firewall:** sshuttle modifica le tabelle del firewall locale (`iptables` su Linux, `pf` su macOS). Se sshuttle si arresta in modo anomalo, utilizza `iptables -F` o riavvia il servizio firewall per eliminare le regole di reindirizzamento obsolete.
2. **Limitazione solo TCP:** I pacchetti UDP non DNS, le richieste echo ICMP (ping) e le scansioni SYN non sono supportati. Usa sempre `nmap -sT -Pn -n` per la scansione delle porte su sshuttle.
3. **Target Footprint:** sshuttle carica automaticamente un piccolo script di assembly Python sull'host pivot remoto durante l'inizializzazione della sessione e lo esegue tramite `python3`.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è destinato esclusivamente ai test di penetrazione autorizzati e all'amministrazione del sistema. L'instradamento e l'intercettazione non autorizzati del traffico di rete sono illegali.