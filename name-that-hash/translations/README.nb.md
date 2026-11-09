<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# Name-That-Hash (nth) Cheatsheet

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) er en moderne, ultrarask hash-identifikasjonsmotor som nøyaktig identifiserer ukjente hashes, hashes innebygd i tekst eller filer, og sender ut direkte Hashcat (`-m`) og John the Ripper (X)-modus. Den dekoder automatisk nestede kodinger som Base64 og Hex.

---

## Innholdsfortegnelse

- [Nøkkelfunksjoner](#key-features)
- [Installasjon](#installation)
- [CLI Hash Identification](#cli-hash-identification)
- [Multi-hash og filbehandling](#multi-hash--filbehandling)
- [Hashcat & John the Ripper-formatutgang](#hashcat--john-the-ripper-format-utgang)
– [Auto-decode & Popularity Filters](#auto-decode--popularity-filters)
- [Integrasjon med cracking-rørledninger](#integration-with-cracking-pipelines)

---

## Nøkkelfunksjoner

- **300+ Hash-typer:** Dekker moderne algoritmer, Active Directory-formater, database-hasher, CMS-signaturer og esoteriske chiffer.
- **Hashcat & John Flag Mapping:** Viser umiddelbart `-m <mode>`- og `--format=<format>`-verdier.
- **Auto-dekoding:** Striper Base64, Hex og URL-koding før hash-type beregnes.
- **Sub-streng / tekstsøk:** Finner hashes begravd i logger, minnedumper eller konfigurasjonsfiler.
- **Popularitetssortering:** Rangerer resultater etter sannsynlighet for bruk i den virkelige verden.

---

## Installasjon
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## CLI-hash-identifikasjon

### 1. Identifiser en enkelt hash
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. Identifiser Hashcat- og John-formatmodusene direkte
Vis bare hash-typer som har støttet Hashcat- eller John-cracking-moduser:
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. Vis detaljert informasjon (beskrivelse, hastighet, eksempler)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## Multi-hash og filbehandling

### 1. Identifiser hashes inne i en fil (linje for linje)
```bash
nth -f /path/to/hashes.txt
```
### 2. Filtrer resultatene kun til topp populære hashes
Reduser falske positiver fra obskure hash-typer ved å filtrere etter popularitet:
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. Søk og trekk ut hashes innebygd i ustrukturert tekst
Skann råminnestrenger, loggfiler eller kildekode for hashes:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Hashcat & John the Ripper-formatutgang

`nth` gjør det trivielt å mate hash til kjeks uten å huske modusnumre:

| Eksempel på Hash-format | Oppdaget algoritme | Hashcat-modus (`-m`) | John Format (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Kerberoast (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | AS-REP Steking | `18200` | `krb5asrep` |

---

## Auto-dekoding og popularitetsfiltre

Mange applikasjoner lagrer hashes i Base64 eller Hex før databaseinnsetting. `nth` kan reversere dette automatisk:
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Integrasjon med Cracking Pipelines

Send raskt en ukjent hash til Hashcat ved hjelp av automatisert identifikasjon:
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```