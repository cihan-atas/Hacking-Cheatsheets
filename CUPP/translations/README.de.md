<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# CUPP (Common User Passwords Profiler) Cheatsheet

[CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler) ist ein spezieller Passwort-Wörterbuch-Generator, der für die gezielte Erstellung von Personenprofilen entwickelt wurde. Anstatt mit generischen Wörterbüchern brutal zu forcieren, generiert CUPP Permutationen auf der Grundlage persönlicher Daten (Zielname, Partner, Haustiere, Geburtsdaten, Firma, Hobbys, Leet-Speak-Variationen und Sonderzeichenkombinationen).

---

## Inhaltsverzeichnis

- [Übersicht & Anwendungsfälle](#overview--use-cases)
- [Installation](#installation)
- [Assistent zur interaktiven Profilerstellung (`-i`)](#interactive-profiling-wizard--i)
- [Konfiguration und Leet-Regeln (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Wörterbücher herunterladen und zusammenführen (`-l` & `-w`)](#downloading--merging-dictionaries--l---w)
- [Ausgabe an Hashcat und Hydra weiterleiten](#piping-output-to-hashcat--hydra)
- [Richtlinien zu Defensive Insights und Passwortrichtlinien](#defensive-insights--password-policy-guidelines)

---

## Übersicht und Anwendungsfälle

Herkömmliche Wörterbuchangriffe schlagen oft bei komplexen Passwörtern fehl, die vom Menschen vorhersehbaren Mustern folgen (z. B. `John1984!`, `Sarah_Fluffy2022#`). CUPP begegnet diesem Problem durch:
- Sammeln zielspezifischer persönlicher Informationen.
- Kombinieren von Namen, Geburtsjahren, wichtigen Daten und Kosenamen.
- Anwenden regelbasierter Mutationen (Großschreibung, Leet-Speak-Ersetzung, Sonderzeichen, Anhängen von Zufallszahlen).
- Erstellen einer optimierten, zielgerichteten Wortliste, die deutlich kleiner als `rockyou.txt` ist, aber eine höhere Zielrelevanz aufweist.

---

## Installation
```bash
# Clone the repository
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian repository (if available)
sudo apt-get update && sudo apt-get install -y cupp

# Check help options
python3 cupp.py -h
```
---

## Interaktiver Profilierungsassistent (`-i`)

Starten Sie die Fragebogen-Eingabeaufforderung, um eine personalisierte Wortliste zu erstellen:
```bash
python3 cupp.py -i
```
Beispielhafte Anleitung für Eingabeaufforderungen:
```text
[+] Insert the targets information:
> First Name: Alice
> Surname: Smith
> Nickname: ally
> Birthdate (DDMMYYYY): 15061992

[+] Insert partners information:
> Partners name: Bob
> Partners nickname: bobby
> Partners birthdate (DDMMYYYY): 04111990

[+] Insert child's information:
> Child's name: Charlie
> Child's nickname: char
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Luna

[+] Insert company name:
> Company name: CyberCorp

[+] Do you want to add some special words about the victim? Y/n: y
> (comma separated): football, chelsea, guitar, devops

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (i.e. leet = 1337) Y/n: y
```
Die Ausgabedatei `alice.csv` (oder eine angegebene Textdatei) wird mit Tausenden von benutzerdefinierten Mutationen generiert (z. B. `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Konfiguration und Leet-Regeln (`cupp.cfg`)

Die CUPP-Konfiguration wird über `cupp.cfg` gesteuert. Zu den wichtigsten Abschnitten gehören:
```ini
[years]
# Set year range for automated suffix appending
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Common number patterns appended to passwords
nums = 1,12,123,1234,12345,123456,777,999,007,69

[special]
# Special character appending
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak substitutions
a = 4,@
b = 8
e = 3
i = 1,!
o = 0
s = 5,$
t = 7
```
Bearbeiten Sie `cupp.cfg`, um es an die Besonderheiten des Zielgebietsschemas und der Sprache anzupassen.

---

## Wörterbücher herunterladen und zusammenführen (`-l` & `-w`)

### 1. Laden Sie vorkompilierte Wortlisten aus Repositories herunter
Laden Sie standardmäßige sprachliche Wortlisten herunter:
```bash
python3 cupp.py -l
```
### 2. Verbessern Sie eine vorhandene Wortliste mit Leet / Sonderregeln
Wenden Sie CUPP-Mutationsregeln auf eine Liste vorhandener Wörter an:
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## Ausgabe an Hashcat und Hydra weiterleiten

Geben Sie das generierte benutzerdefinierte Wörterbuch direkt in Brute-Force-Tools ein:

### Ziel-Hashes mit Hashcat knacken
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### Online-Authentifizierung Brute-Force mit Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## Defensive Insights und Richtlinien für Passwortrichtlinien

- **Persönliche Attribute verbieten:** Verbieten Sie Benutzerkennwörter, die Vornamen, Nachnamen, Benutzernamen, Firmenmarkennamen oder Geburtsdaten von Mitarbeitern enthalten.
- **Länge vor Komplexität erzwingen:** Eine Passphrase mit mehr als 16 Zeichen (z. B. vier zufällige Wörterbuchwörter) ist deutlich widerstandsfähiger gegen Profiling-Tools als ein `NameYear!`-Muster mit 8 Zeichen.
- **Multi-Faktor-Authentifizierung (MFA):** Erzwingen Sie Hardware-FIDO2- oder TOTP-Tokens, sodass die benutzerdefinierte Wörterbuchgenerierung allein keine Konten gefährden kann.