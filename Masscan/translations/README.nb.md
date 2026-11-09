<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p align="center">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Språk C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Recon & Scanning">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="License AGPLv3">
</p>

<p align="center">
  <b>🌐 Høyytelses asynkron TCP-portskanner som kan skanne hele IPv4-internettet på under 5 minutter</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og den asynkrone arkitekturen](#-oversikt--den-asynkrone-arkitekturen)
- [Verktøysammenligning: Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Installasjon](#-installasjon)
– [Grunnleggende bruk og skanningsmål](#-grunnbruks--skanningsmål)
- [Transmission Rate Control (`--rate`)](#-transmission-rate-control---rate)
- [Portspesifikasjoner (TCP og UDP)](#-port-spesifikasjoner-tcp--udp)
- [Bannergrabbing & Basic Protocol Probing](#-banner-grabbing--basic-protocol-probing)
- [Target Exclusions & Blacklists (`--excludefile`)](#-target-exclusions--blacklists---excludefile)
- [Output Formats & Piping into Nmap](#-output-formats--piping-into-nmap)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Hardware Limits](#-defensive-considerations--hardware-limits)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og den asynkrone arkitekturen

Laget av **Robert Graham**, **Masscan** er hyllet som den raskeste portskanneren som finnes. Den gir resultater som ligner på Nmap, men fungerer internt som `scanrand`, `unicornscan` og `ZMap`, og bruker **asynkron overføring**.

I motsetning til Nmap, som bruker operativsystemets kjerne-TCP-stack og venter på svar før de undersøker etterfølgende porter, inkorporerer Masscan sin egen tilpassede brukerplass-TCP/IP-stack. Den sprenger rå SYN-pakker med hastigheter som overstiger **10 millioner pakker per sekund** uten å opprettholde tabeller for tilkoblingstilstand.

---

## ⚖️ Verktøysammenligning: Masscan vs Nmap vs RustScan

| Funksjon | Masscan | Nmap | RustScan |
|--------|--------|------|--------|
| **Maksimal pakkehastighet** | 10 000 000 pkts/sek | ~1000 pkts/sek | OS socket limit |
| **Hele Internettskanning (/0)**| ✅ Under 5 minutter | ❌ Dager / uker | ❌ Ikke designet for /0 |
| **TCP-stakkimplementering**| Tilpasset brukerrom Raw SYN| OS-kjerne / Raw Sockets| Async Tokio TCP-sockets |
| **Dypversjon og NSE-skript**| ❌ Primitive bannere | ✅ 600+ NSE-skript | ✅ Via Nmap-overlevering |
| **Tilstandssporing** | Statsløs (Blackrock PRNG) | Stateful forbindelse basseng| Stateful asynkrone oppgaver |
| **Ideell målomfang** | Klasse A/B-blokker (/8, /16)| Enkeltmål og /24 undernett | Enkeltmaskiner og /24 undernett |

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 Grunnleggende bruk og skannemål
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!VIKTIG]
> Fordi Masscan genererer råpakker via `libpcap`, **må den kjøres med root / sudo-privilegier**.

---

## ⚡ Transmisjonshastighetskontroll (`--rate`)

`--rate`-parameteren dikterer hvor mange pakker per sekund Masscan sender. Kalibrering av dette er avgjørende for å unngå metning av nettverksutstyr:
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 Portspesifikasjoner (TCP og UDP)

Masscan støtter både TCP- og UDP-portlister samtidig:
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 Banner Grabbing & Basic Protocol Probing

Mens Masscan først og fremst er en SYN-skanner, kan den fullføre treveis håndtrykk for å fange applikasjonsbannere:
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 Målekskluderinger og svartelister (`--excludefile`)

Når du utfører omfattende penetrasjonstester, utelukk alltid kritisk infrastruktur og adresser som ikke er begrenset:
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 Utdataformater og overførsel til Nmap

Lagre oppdagede verter i grepbare eller JSON-formater, og mat dem deretter inn i Nmap for fullstendig sårbarhetsvurdering:
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Skann enkeltvert (alle porter)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Skann CIDR-undernett for port** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Skann med bannerfanging** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **Skann UDP-porter** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **Ekskluder IP-adresser** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Utgang til JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Utgang til enkel liste** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Defensive vurderinger og maskinvaregrenser

1. **Forbrukerruterkrasj:** Innstilling av `--rate 100000` på en hjemme-DSL/kabelruter eller standard Wi-Fi vil overvelde NAT-tilkoblingssporingsstatustabellen og krasje ruteren. Hold `--rate` under `1000` på boligtilkoblinger.
2. **Båndbreddeforbruk:** Å sende med 10 millioner pakker per sekund bruker omtrent 5,4 Gbps nettverksbåndbredde.
3. **Lokal brannmuravvisning (RST):** Fordi Masscan bruker en tilpasset brukerplass TCP-stakk, vet ikke Linux-kjernen om åpne tilkoblinger og kan sende automatiske TCP RST-pakker til mål. For å unngå dette under bannergrep:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Masscan er et usedvanlig kraftig nettverksrekognoseringsverktøy. Skanning av uautoriserte nettverk, offentlige IP-områder eller offentlige Internett-undernett kan bryte med lover om telekommunikasjon og cybersikkerhet. Bekreft alltid skriftlig autorisasjon før du starter skanninger.