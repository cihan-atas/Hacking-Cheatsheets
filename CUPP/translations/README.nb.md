<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# CUPP (Common User Passwords Profiler) Jukseark

[CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler) er en spesialisert passordordbokgenerator designet for målrettet menneskelig profilering. I stedet for å tvangspresse med generiske ordbøker, genererer CUPP permutasjoner basert på personlige detaljer (målnavn, partner, kjæledyr, fødselsdatoer, selskap, hobbyer, leet speak-variasjoner og spesialtegnkombinasjoner).

---

## Innholdsfortegnelse

- [Oversikt og brukstilfeller](#oversikt--brukstilfeller)
- [Installasjon](#installation)
- [Interactive Profiling Wizard (`-i`)](#interactive-profiling-wizard--i)
- [Konfigurasjon og Leet-regler (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Last ned og slå sammen ordbøker (`-l` & `-w`)](#downloading--merging-dictionaries--l---w)
- [Piping Output to Hashcat & Hydra](#piping-output-to-hashcat--hydra)
– [Defensive Insights & Password Policy Guidelines](#defensive-insights--password-policy-guidelines)

---

## Oversikt og brukstilfeller

Tradisjonelle ordbokangrep mislykkes ofte mot komplekse passord som følger menneskelige forutsigbare mønstre (f.eks. `John1984!`, `Sarah_Fluffy2022#`). CUPP adresserer dette ved:
- Innhenting av målspesifikk personlig etterretning.
- Kombinere navn, fødselsår, viktige datoer og kjæledyrnavn.
- Bruk av regelbaserte mutasjoner (store bokstaver, erstatning av leet speak, spesialtegn, tilføying av tilfeldige tall).
- Produserer en optimalisert, målrettet ordliste som er dramatisk mindre enn `rockyou.txt`, men med høyere målrelevans.

---

## Installasjon
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

## Interactive Profiling Wizard (`-i`)

Start spørreskjemaet for å lage en personlig ordliste:
```bash
python3 cupp.py -i
```
Eksempel på ledetekst:
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
Utdatafil `alice.csv` (eller spesifisert tekstfil) genereres med tusenvis av tilpassede mutasjoner (f.eks. `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Konfigurasjon og Leet-regler (`cupp.cfg`)

CUPP-konfigurasjonen styres via `cupp.cfg`. Nøkkelseksjoner inkluderer:
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
Rediger `cupp.cfg` for å justere til mållokale og språkspesifikke.

---

## Laste ned og slå sammen ordbøker (`-l` & `-w`)

### 1. Last ned forhåndskompilerte ordlister fra arkiver
Last ned standard språklige ordlister:
```bash
python3 cupp.py -l
```
### 2. Forbedre en eksisterende ordliste med Leet / Spesialregler
Bruk CUPP mutasjonsregler på en liste over eksisterende ord:
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## Piping-utgang til Hashcat & Hydra

Mat den genererte tilpassede ordboken direkte inn i brute-force-verktøy:

### Å knekke målhasher med Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### Online autentisering Brute-Force med Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## Retningslinjer for defensiv innsikt og passordretningslinjer

- **Forby personlige attributter:** Forby brukerpassord som inneholder ansattes fornavn, etternavn, brukernavn, firmamerkenavn eller fødselsdatoer.
- **Håndhev lengde over kompleksitet:** En passordfrase på 16+ tegn (f.eks. fire tilfeldige ordbokord) er betydelig mer motstandsdyktig mot profileringsverktøy enn et 8-tegns `NameYear!`-mønster.
- **Multi-Factor Authentication (MFA):** Håndhev maskinvare FIDO2- eller TOTP-tokens slik at tilpasset ordbokgenerering alene ikke kan kompromittere kontoer.