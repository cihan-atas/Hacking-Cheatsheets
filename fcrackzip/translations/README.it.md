<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# fcrackzip Cheatsheet

[fcrackzip](https://github.com/hyc/fcrackzip) è un cracker di password da riga di comando veloce e multi-thread per archivi ZIP crittografati. Supporta sia attacchi basati su dizionario che attacchi di forza bruta ottimizzati contro ZipCrypto tradizionale (cifrario di flusso PKZIP) e può verificare l'integrità dell'archivio senza estrazione completa.

---

## Sommario

- [Caratteristiche principali e limitazioni](#caratteristiche principali--limitazioni)
- [Installazione](#installazione)
- [Attacchi dizionario (`-D -p`)](#attacchi-dizionari--d--p)
- [Attacchi a forza bruta (`-b`)](#attacchi-a-forza-bruta--b)
- [Set di caratteri e specifica della lunghezza](#set di caratteri--specifica della lunghezza)
- [Benchmark e ottimizzazione delle prestazioni](#benchmark--ottimizzazione-delle-prestazioni)
- [ZipCrypto vs. AES-256 (alternativa a John/Hashcat)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## Caratteristiche principali e limitazioni

- **Cracker ZipCrypto ultraveloce:** Scritto in C, ottimizzato per l'algoritmo ZipCrypto legacy (crittografia PKZIP standard).
- **Modalità dizionario:** Legge le password dei candidati in sequenza da elenchi di parole.
- **Modalità forza bruta:** genera candidati al volo utilizzando set di caratteri e limiti di lunghezza configurabili.
- **Test di decompressione degli archivi (`-u`):** Verifica le password candidate tentando la decompressione, eliminando virtualmente i falsi positivi.
- **Limitazione:** **Non** supporta la moderna crittografia WinZip AES-256. Per gli ZIP AES-256, utilizzare `zip2john` + John the Ripper o Hashcat (modalità 13600).

---

## Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## Attacchi dizionario (`-D -p`)

Il modo più veloce per testare elenchi di parole comuni:

### 1. Attacco di dizionario di base con verifica di decompressione
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`: modalità decompressione (verifica il CRC del file estratto per evitare falsi positivi).
- `-D`: modalità di attacco del dizionario.
- `-p <file>`: percorso del file dizionario.

### 2. Modalità di output dettagliata
Visualizza la velocità testata, la password corrente e le informazioni di debug:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## Attacchi di forza bruta (`-b`)

Quando gli attacchi con dizionario falliscono, usa la forza bruta contro password brevi sconosciute:

### 1. PIN numerici a forza bruta (da 4 a 6 cifre)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`: modalità forza bruta.
- `-c 1`: set di caratteri: solo cifre (`0-9`).
- `-l 4-6`: Intervallo di lunghezza: min 4, max 6 caratteri.

### 2. Caratteri alfanumerici minuscoli (da 1 a 5 caratteri)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`: Lettere minuscole (`a-z`) e cifre (`0-9`).

### 3. Minuscolo + Maiuscolo + Cifre + Simboli
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`: Lettere minuscole (`a-z`).
- `-c A`: lettere maiuscole (`A-Z`).
- `-c 1`: Numeri (`0-9`).
- `-c !`: Simboli speciali (`[!:$%&/()=?{[]}+*~#]`).

---

## Set di caratteri e specifiche di lunghezza

| Bandiera | Set di caratteri incluso | Candidati di esempio |
|---|---|---|
| `-c a` | Lettere minuscole (`a-z`) | `admin`, `secret` |
| `-c A` | Lettere maiuscole (`A-Z`) | `SECRET`, `ROOT` |
| `-c 1` | Cifre (`0-9`) | `1234`, `2024` |
| `-c !` | Caratteri speciali di punteggiatura | `!@#$` |
| `-c aA1` | Caratteri alfanumerici combinati | `Admin123` |
| `-p <prefix>` | Stringa seed iniziale iniziale | Utile per riprendere la forza bruta |

Specificare i limiti di lunghezza:
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## Benchmark e ottimizzazione delle prestazioni

Testa il throughput della CPU sul tuo computer:
```bash
fcrackzip -B
```
---

## ZipCrypto vs. AES-256 (alternativa a John/Hashcat)

Se `fcrackzip` restituisce `not a zipfile, or unsupported encryption type`, l'archivio è crittografato con AES-256 (WinZip AES). Usa `zip2john`:
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```