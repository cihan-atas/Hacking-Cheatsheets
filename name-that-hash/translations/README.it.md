<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Cheatsheet Nome-Quel-Hash (ennesimo).

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) è un motore di identificazione hash moderno e ultraveloce che identifica accuratamente hash sconosciuti, hash incorporati in testo o file e genera modalità Hashcat dirette (`-m`) e John the Ripper (`--format`). Decodifica automaticamente le codifiche nidificate come Base64 e Hex.

---

## Sommario

- [Caratteristiche principali](#caratteristiche principali)
- [Installazione](#installazione)
- [Identificazione hash CLI](#cli-hash-identification)
- [Multi-Hash ed elaborazione file](#multi-hash--elaborazione file)
- [Output in formato Hashcat e John the Ripper](#hashcat--john-the-ripper-format-output)
- [Decodifica automatica e filtri di popolarità](#decodifica automatica--filtri di popolarità)
- [Integrazione con Cracking Pipelines](#integrazione-con-cracking-pipeline)

---

## Caratteristiche principali

- **Oltre 300 tipi di hash:** copre algoritmi moderni, formati Active Directory, hash di database, firme CMS e cifrari esoterici.
- **Mappatura Hashcat e John Flag:** visualizza immediatamente i valori `-m <mode>` e `--format=<format>`.
- **Decodifica automatica:** Elimina la codifica Base64, Hex e URL prima di calcolare il tipo di hash.
- **Ricerca sottostringa/testo:** trova gli hash sepolti all'interno di log, dump della memoria o file di configurazione.
- **Ordinamento per popolarità:** classifica i risultati in base alla probabilità di utilizzo nel mondo reale.

---

## Installazione
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

## Identificazione hash CLI

### 1. Identificare un singolo hash
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. Identificare direttamente le modalità di formato Hashcat e John
Visualizza solo i tipi di hash che hanno supportato le modalità di cracking Hashcat o John:
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. Visualizza informazioni dettagliate (descrizione, velocità, esempi)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## Elaborazione di file e hash multipli

### 1. Identificare gli hash all'interno di un file (riga per riga)
```bash
nth -f /path/to/hashes.txt
```
### 2. Filtra i risultati solo in base agli hash più popolari
Riduci i falsi positivi provenienti da tipi di hash oscuri filtrando per popolarità:
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. Cerca ed estrai gli hash incorporati nel testo non strutturato
Esegui la scansione di stringhe di memoria non elaborate, file di registro o codice sorgente per individuare gli hash:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Output in formato Hashcat e John lo Squartatore

`nth` rende banale inserire gli hash nei cracker senza memorizzare i numeri di modalità:

| Formato hash di esempio | Algoritmo rilevato | Modalità Hashcat (`-m`) | Formato Giovanni (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | bcrypt | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Kerberoast (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | AS-REP Torrefazione | `18200` | `krb5asrep` |

---

## Decodifica automatica e filtri di popolarità

Molte applicazioni memorizzano gli hash in Base64 o Hex prima dell'inserimento nel database. `nth` può invertirlo automaticamente:
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Integrazione con le pipeline di cracking

Invia rapidamente un hash sconosciuto a Hashcat utilizzando l'identificazione automatizzata:
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```