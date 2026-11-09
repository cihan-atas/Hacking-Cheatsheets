<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# Yersinia Jukseark

[Yersinia](https://github.com/tomac/yersinia) er et kraftig Layer-2 (Data Link layer) nettverkspenetrasjonstestramme designet for å analysere og utnytte svakheter i Layer-2-protokoller som STP (Spanning Tree Protocol), CDP (Cisco Discovery Protocol), DTP (Dynamic Trunking Protocol, DHQ20, DHQ1, CPHS, DHQ20, DHQ20, DHQ20). trunking.

---

## Innholdsfortegnelse

- [Støttede protokoller og moduser](#supported-protocols--modes)
- [Installasjon](#installation)
- [Interactive Curses Mode (`-I`)](#interactive-curses-mode--i)
- [Spanning Tree Protocol (STP)-angrep](#spanning-tree-protocol-stp-attacks)
- [Cisco Discovery Protocol (CDP)-angrep](#cisco-discovery-protocol-cdp-attacks)
- [Dynamic Trunking Protocol (DTP) & VLAN Hopping](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP Rogue & Starvation Attacks](#dhcp-rogue--sult-angrep)
– [HSRP- og VRRP-kapring](#hsrp--vrrp-kapring)
- [avbøtende og herding](#begrensning--herding)

---

## Støttede protokoller og moduser

Yersinia retter seg mot protokoller som opererer under IP:
- **STP:** Spanning Tree Protocol (overtakelse av rotbro-rolle, meldinger om topologiendringer).
- **CDP:** Cisco Discovery Protocol (enhetsflom, falsk naboinjeksjon).
- **DTP:** Dynamic Trunking Protocol (forhandling av trunkkoblinger for å få tilgang til alle VLAN).
- **DHCP:** Dynamic Host Configuration Protocol (Starvation & rogue DHCP-servere).
- **HSRP:** Hot Standby Router Protocol (overtakelse av aktiv gateway).
- **802.1Q / 802.1X:** Dot1Q VLAN-hopping og EAPOL-utmattelse.
- **VTP:** VLAN Trunking Protocol (VLAN-databasesletting / overskriving).

Utførelsesmoduser:
- **`-I` (Interactive Curses):** Fullskjerms terminalgrensesnitt for valg av protokoller, konfigurering av angrep og overvåking av live-pakker.
- **Ikke-interaktiv CLI:** Direkte kjøring for automatiserte skript.

---

## Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```
---

## Interactive Curses Mode (`-I`)

Den mest praktiske og sikre måten å bruke Yersinia i terminalen uten GUI:
```bash
sudo yersinia -I
```
- **`h`**: Vis hjelp og tastebindinger.
- **`g`**: Bytteprotokoller (STP, CDP, DTP, DHCP, HSRP, etc.).
- **`x`**: Utfør angrepsmeny for gjeldende protokoll.
- **`c`**: Avbryt løpende angrep.
- **`l`**: Slå av/på pakkelogging.
- **`q`**: Gå ut av Yersinia.

---

## Spanning Tree Protocol (STP) angrep

STP velger en rotbro med den laveste bro-ID (Priority + MAC). Yersinia kan injisere falske BPDUer (Bridge Protocol Data Units) for å kreve rotstatus og omdirigere trafikk gjennom angriperverten.

### 1. Krev rotrolle (BPDU-injeksjon)
Send BPDUer med Priority `0` og en lavere MAC-adresse for å bli STP Root Bridge:
```bash
sudo yersinia stp -attack 1 -i eth0
```
### 2. Gjør krav på rotrolle med topologiendring (TCN)
Tving alle brytere til å tømme CAM-tabeller og konvergere på nytt gjennom angriperen:
```bash
sudo yersinia stp -attack 2 -i eth0
```
### 3. STP DoS Attack (Flood Config BPDUs)
Crash eller saturate switch CPU med massive motstridende BPDU-rammer:
```bash
sudo yersinia stp -attack 0 -i eth0
```
---

## Cisco Discovery Protocol (CDP)-angrep

CDP deler enhetsnavn, modeller, muligheter og programvareversjoner uautentisert på tvers av Cisco-enheter.

### 1. Flom falske CDP-naboer
Eksosbryterminne og lag fantomenheter i `show cdp neighbors`:
```bash
sudo yersinia cdp -attack 0 -i eth0
```
### 2. Ta opp og inspiser legitime CDP-pakker
Kjør interaktiv forbannelsesmodus (`yersinia -I`), trykk `g`, velg `CDP`, og observer byttevertsnavn, administrasjons-IP, maskinvaremodell og innebygd VLAN-ID.

---

## Dynamic Trunking Protocol (DTP) & VLAN Hopping

DTP forhandler automatisk trunking-koblinger hvis svitsjporter er konfigurert i `dynamic desirable`- eller `dynamic auto`-modus.

### 1. Forhandle trunkinglink (ønsket DTP-pakke)
Lure bryterporten til å bli til en 802.1Q trunk:
```bash
sudo yersinia dtp -attack 0 -i eth0
```
Når stammen er forhandlet, fanger du opp rammer fra alle VLAN-er eller tag-pakker til isolerte undernett ved hjelp av Linux 802.1Q-grensesnitt:
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```
---

## DHCP Rogue & Starvation Attacks

### 1. DHCP-sultangrep (IP-eksosbasseng)
Send tusenvis av DHCP DISCOVER-forespørsler med tilfeldige MAC-adresser for å konsumere alle tilgjengelige IP-adresser i bassenget:
```bash
sudo yersinia dhcp -attack 1 -i eth0
```
### 2. Sett opp Rogue DHCP Server
Distribuer en useriøs DHCP-server med tilpasset DNS og gateway:
```bash
sudo yersinia dhcp -attack 2 -i eth0
```
---

## HSRP- og VRRP-kapring

HSRP lar flere rutere presentere en enkelt virtuell standardgateway.

### 1. Gjør krav på aktiv ruterprioritet
Injiser HSRP HELLO-pakker med prioritet 255 for å overta standard gateway-IP:
```bash
sudo yersinia hsrp -attack 0 -i eth0
```
---

## Avbøtende og herding

| Protokoll | Angrepsvektor | Cisco / Standard Mitigation |
|---|---|---|
| **STP** | Fake Root Bridge Injection | `spanning-tree bpduguard enable` på tilgangsporter; `spanning-tree rootguard` på koblinger mellom brytere. |
| **DTP** | Rogue Trunk Negotiation | `switchport mode access` og `switchport nonegotiate` på alle brukervendte tilgangsporter. |
| **CDP** | Flom / Avsløring av informasjon | `no cdp run` (globalt) eller `no cdp enable` på uklarerte grensesnittporter. |
| **DHCP** | Starvation & Rogue Server | Aktiver `ip dhcp snooping`, begrens hastighet per port, konfigurer pålitelige opplink-porter. |
| **HSRP** | Prioritert kapring | Konfigurer MD5-autentisering med hemmelige nøkler (`standby 1 authentication md5 key-string <SECRET>`). |