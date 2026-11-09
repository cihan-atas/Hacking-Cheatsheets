<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# enum4linux-ng Jukseark

[enum4linux-ng](https://github.com/cddmp/enum4linux-ng) er en neste generasjons omskrivning av det eldre `enum4linux`-verktøyet skrevet i Python 3. Det utfører omfattende Windows- og Samba-informasjonsinnsamling over SMB (Server Message Block), NetBIOS, RPC (Remote Call) og LDAP. Den trekker ut brukerkontoer, gruppemedlemskap, delte mapper, passordpolicyer, OS-informasjon og RID-sykluser.

---

## Innholdsfortegnelse

- [Nøkkelforbedringer over eldre enum4linux](#nøkkelforbedringer-over-legacy-enum4linux)
- [Installasjon og krav](#installation--requirements)
– [Grunnleggende SMB- og domeneoppregning](#grunnleggende-smb--domeneoppregning)
- [Autentiseringsmoduser (nulløkt vs. gyldig bruker)](#autentiseringsmoduser-null-økt-vs-gyldig-bruker)
- [Targeted Enumeration Modules](#targeted-enumeration-modules)
- [RID Cycling & User Account Harvesting](#rid-cycling--user-account-harvesting)
- [Eksportere rapporter (JSON, YAML, tekst)](#exporting-reports-json-yaml-text)
- [Defensive Hardening for SMB & RPC](#defensive-hardening-for-smb--rpc)

---

## Nøkkelforbedringer i forhold til eldre enum4linux

- **Moderne Python 3-motor:** Flertrådsutførelse basert på `impacket`, eliminerer gamle Perl-skriptkrasj.
- **Forbedret OS-deteksjon:** Nøyaktig gjenkjenning av Windows Server (2012–2025), Windows 10/11 og moderne Samba-versjoner.
- **Strukturert rapportering:** Innebygd eksport til JSON og YAML for sømløs SIEM- eller skriptintegrasjon.
- **Smartere RID-sykling:** Fleksible rekkeviddejusteringer og automatisk administrator-/gjeste-RID-deteksjon.
- **Farget CLI-utgang:** Tydelige visuelle indikatorer for sikkerhetsfeilkonfigurasjoner og null-økter.

---

## Installasjon og krav
```bash
# Clone the repository
git clone https://github.com/cddmp/enum4linux-ng.git
cd enum4linux-ng

# Install Python dependencies (impacket)
pip3 install -r requirements.txt

# Or install globally as a symlink
sudo ln -sf "$(pwd)/enum4linux-ng.py" /usr/local/bin/enum4linux-ng

# Verify command
enum4linux-ng -h
```
---

## Grunnleggende SMB- og domeneoppregning

### 1. Standard opptellingskjøring (`-A`)
Kjør alle ikke-påtrengende oppregningskontroller mot målet:
```bash
enum4linux-ng -A 192.168.1.50
```
Denne modulen spør:
- NetBIOS-navn og arbeidsgruppe-/domenenavn.
- Operativsystemversjon og arkitektur.
- Delte kataloger og tilgangstillatelser.
- Passordpolicy (sperregrense, minimumslengde, historikk).
- Brukere og lokale grupper via RPC/SAMR.

---

## Autentiseringsmodi (nulløkt vs. gyldig bruker)

### 1. Anonym / null økt (`-u '' -p ''`)
Test om målet tillater uautentiserte IPC$-tilkoblinger:
```bash
enum4linux-ng -u "" -p "" 192.168.1.50
```
### 2. Autentisert domene / lokal konto
Oppgi gyldig legitimasjon med lavt privilegium for å dramatisk utvide innhøstede data:
```bash
enum4linux-ng -u "jdoe" -p "Password123!" 192.168.1.50
```
### 3. Oppgi spesifikt domenenavn (`-d`)
```bash
enum4linux-ng -u "jdoe" -p "Password123!" -d "CORP.LOCAL" 192.168.1.50
```
---

## Målrettede oppregningsmoduler

Kjør spesifikke moduler for å minimere nettverksfotavtrykk og unngå inntrengingsvarsler:

### 1. Delte mapper og tilgangstillatelser (`-S`)
Oppregne delinger, kommentarer og sjekk lese-/skrivetilgang:
```bash
enum4linux-ng -S 192.168.1.50
```
### 2. Password Policy Extraction (`-P`)
Identifiser terskler for kontosperre for å trygt planlegge passordspraying:
```bash
enum4linux-ng -P 192.168.1.50
```
Nøkkelutganger å observere:
- **Minste passordlengde:** f.eks. 8 tegn.
- **Terskel for kontosperre:** f.eks. 5 forsøk (ikke spray raskere enn sperrevinduet!).
- **Tilbakestill lockout-teller:** f.eks. 30 minutter.

### 3. Kun brukere og grupper (`-U` og `-G`)
```bash
# Enumerate user list
enum4linux-ng -U 192.168.1.50

# Enumerate domain and local groups
enum4linux-ng -G 192.168.1.50
```
### 4. Operativsystem og NetBIOS Info Kun (`-O` og `-I`)
```bash
enum4linux-ng -O -I 192.168.1.50
```
---

## RID-sykling og høsting av brukerkontoer

Når brukeroppregning via standard RPC-spørringer er begrenset, utfør Relative Identifier (RID)-sykling via SAMR-spørringsanrop:

### 1. Standard RID-sykling (område 500–1100)
```bash
enum4linux-ng -R 192.168.1.50
```
### 2. Egendefinert RID-område (f.eks. 500 til 5000 for store organisasjoner)
```bash
enum4linux-ng -r 500-5000 192.168.1.50
```
### 3. Pakk ut ren brukernavnordliste
Filtrer oppdagede RID-brukernavn inn i en tekstfil for passordspraying:
```bash
enum4linux-ng -R 192.168.1.50 -oJ /tmp/enum_out
jq -r '.users[].username' /tmp/enum_out.json | sort -u > /tmp/users.txt
```
---

## Eksportere rapporter (JSON, YAML, tekst)

Eksporter opptellingsresultater for dokumentasjon eller programmatisk parsing:

### 1. Eksporter i alle formater samtidig (`-oA`)
```bash
enum4linux-ng -A 192.168.1.50 -oA /tmp/target_recon
```
Genererer:
- `/tmp/target_recon.json`
- `/tmp/target_recon.yaml`
- `/tmp/target_recon.txt`

### 2. Eksporter til spesifikt format
```bash
# JSON only
enum4linux-ng -A 192.168.1.50 -oJ /tmp/target.json

# YAML only
enum4linux-ng -A 192.168.1.50 -oY /tmp/target.yaml
```
---

## Defensiv herding for SMB og RPC

| Vektor | Risiko | Herdeanbefaling |
|---|---|---|
| **Null-økt (anonym IPC$)** | Uautentisert domenerekognosering | Sett `RestrictNullSessAccess=1` i Windows-registeret; konfigurer `RestrictAnonymous=2` i SAM. |
| **SMBv1 aktiv** | EternalBlue (MS17-010) & MITM-angrep | Deaktiver SMBv1 fullstendig på tvers av alle endepunkter og domenekontrollere. |
| **Delingstillatelser for klartekst** | Sensitive filer og legitimasjon avslørt | Revisjon deletillatelser; håndheve tilgangsbasert enumeration (ABE). |
| **RID Cycling Enumeration** | Full uttrekk av brukerkontoliste | Begrens oppregning av anonyme SAM-kontoer via lokal sikkerhetspolicy. |