<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<p align="center">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Shell Stabilization">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Gjør rå Netcat-lyttere og dumme omvendte skjell til komplette kommandolinjegrensesnitt med piltaster, historikk og autofullføring</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Introduksjon: "Dumb Shell"-problemet](#-introduction-the-dumb-shell-problem)
- [Installasjon](#-installasjon)
- [Grunnleggende bruk](#-grunnbruk)
- [Catching Reverse Shells (`rlwrap + nc`)](#-catching-reverse-shells-rlwrap--nc)
– [Persistent History Management](#-persistent-history-management)
- [Kommando autofullføring (`-f`)](#-kommando-autofullføring--f)
- [Full 5-Step TTY Shell Upgrade Workflow](#-full-5-step-tty-shell-upgrade-workflow)
- [Bruk av rlwrap med andre Pentest-verktøy](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis & Telnet](#redis--telnet)
  - [Socat & Custom Sockets](#socat--custom-sockets)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Tips og beste fremgangsmåter](#-tips--beste praksis)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Introduksjon: "Dumb Shell"-problemet

Når du fanger et omvendt skall med tradisjonelle verktøy som Netcat (`nc -lvnp 4444`), får du en **dum terminal**. En dum terminal mangler standard linjeredigeringsmuligheter:

- ❌ Ved å trykke på **Opp/Ned-piltastene** skrives ut escape-sekvenser som `^[[A` eller `^[[B` i stedet for å bla gjennom kommandohistorikken.
- ❌ Ved å trykke på **Tilbake** eller **Slett** skrives `^H` eller `^?` ut i stedet for å slette tegn.
- ❌ Hvis du trykker på **Ctrl + C**, drepes den omvendte shell-lytteren umiddelbart, og mister hardt opptjent tilgang.
- ❌ Kommandoer som `su`, `sudo`, `nano`, `vim` eller `passwd` mislykkes fordi det ikke er koblet til en pseudoterminal (PTY).
- ❌ Ingen vedvarende kommandohistorikk beholdes på tvers av økter.

**rlwrap** (Readline Wrapper, skrevet av Hans Lub) løser dette ved å sitte mellom terminalen og målkommandoen. Den avskjærer tastaturslag, utnytter **GNU Readline**, og gir piltastnavigering, linjeredigering, vedvarende kommandohistorikkfiler og ordbokbasert autofullføring!

---

## 📥 Installasjon

`rlwrap` er lett og tilgjengelig i standardpakkedepotene til praktisk talt alle Linux-distribusjoner:

### Kali Linux, Debian og Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora, CentOS & RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### Arch Linux
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### macOS (hjemmebrygget)
```bash
# macOS
brew install rlwrap
```
---

## 🚀 Grunnleggende bruk

Den grunnleggende syntaksen er ganske enkelt å pakke hvilken som helst kommandolinje med `rlwrap`:
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### Vanlige flagg

| Flagg | Beskrivelse |
|------|-------------|
| `-c` | Aktiver fullføring av fil- og banenavn |
| `-A` | Ignorer escape-koder for promptformatering (hindrer feil på ledeteksten) |
| `-r` | Husk alle ordene du har skrevet inn og legg dem inn i fullføringsordlisten |
| `-H <file>` | Angi en dedikert historikkfil for lagring og tilbakekalling av kommandoer |
| `-s <num>` | Angi maksimalt antall historielinjer (standard: 300) |
| `-f <file>` | Last inn en egendefinert fullføringsordbokfil |
| `-b <chars>` | Definer ordbrytende tegn |
| `-m` | Aktiver redigering av flere linjer |
| `-p[color]` | Fargelegg ledeteksten |

---

## 🎣 Catching Reverse Shells (`rlwrap + nc`)

Den vanligste brukssaken i støtende sikkerhet er å pakke inn en Netcat-lytter (se [Netcat Cheatsheet](../Netcat/README.md) for fullstendige lyttervarianter og flagg):
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### Forklaring av `-cAr`-flaggene:

1. **`-c` (Filfullføring):** Lar bruk av `<TAB>` for å autofullføre fil- og katalognavn.
2. **`-A` (Prompter):** Forhindrer rare linjeombrytelser og spørsmålsduplisering når den eksterne serveren sender ustandardiserte ledetekststrenger (f.eks. `root@victim:/#`).
3. **`-r` (Husk ord):** Lærer ord du skriver inn i skallet slik at du kan tabulatorfullføre dem senere.

---

## 💾 Vedvarende historiestyring

Som standard lagrer `rlwrap` kommandohistorikk i `~/.<command>_history`. Du kan konfigurere tilpassede historiefiler per engasjement:
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 Automatisk fullføring av kommando (`-f`)

Du kan gi `rlwrap` en tekstfil som inneholder vanlige Linux- eller Windows-kommandoer, og den vil automatisk fullføre dem når du trykker `<TAB>`:
```bash
# Create a wordlist of essential post-exploitation commands
cat << 'EOF' > /tmp/pentest_commands.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Start listener with custom tab-completion dictionary
rlwrap -f /tmp/pentest_commands.txt nc -lvnp 4444
```
---

## 🛠️ Full 5-trinns TTY Shell-oppgraderingsarbeidsflyt

Mens `rlwrap` gir umiddelbar piltastnavigasjon og historikk, krever enkelte interaktive programmer (`su`, `sudo`, `vim`) fortsatt en fullt allokert TTY-terminal. Her er industristandarden 5-trinns oppgraderingspipeline:

### Trinn 1: Start Listener med rlwrap
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### Trinn 2: Utløs Reverse Shell & Spawn PTY

Når det omvendte skallet ditt kobles til lytteren din, skap en pseudoterminal på målet:
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### Trinn 3: Bakgrunn for skallet
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### Trinn 4: Konfigurer Local Terminal Raw-modus
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(Trykk Enter to ganger etter å ha kjørt `fg` for å gjenopprette ledeteksten)*

### Trinn 5: Tilbakestill og still inn terminaldimensjoner
```bash
# Target shell: set terminal type and environment
reset
export TERM=xterm-256color
export SHELL=bash

# Check your attacker terminal size in another window:
# $ stty size  -> Output: 38 160

# Apply rows and columns to target shell:
stty rows 38 columns 160
```
> ✅ **Resultat:** Du har nå et uforgjengelig, fullverdig interaktivt bash-skall med fanekomplettering, klar skjerm (`Ctrl+L`), signalhåndtering (`Ctrl+C` kobles ikke fra) og fulle terminaldimensjoner!

---

## 🔧 Bruke rlwrap med andre Pentest-verktøy

`rlwrap` er ikke begrenset til Netcat. Ethvert CLI-verktøy som mangler linjeredigering drar nytte av det:

### Oracle SQL*Pluss
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis og Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Socat og tilpassede stikkontakter
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ Hurtigreferansetabell

| Oppgave | Kommando |
|------|--------|
| **Forbedret Netcat Listener** | `rlwrap nc -lvnp 4444` |
| **Fullstendig fil og forespørselsflagg** | `rlwrap -cAr nc -lvnp 4444` |
| **Dedikert historiefil** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Tilpasset fanefullføring** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Øk historiestørrelse** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Send inn Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **Wrap Redis CLI** | `rlwrap redis-cli -h 10.10.10.50` |
| **Wrap Socat Listener** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Tips og beste fremgangsmåter

1. **Opprett et Bash Alias:** Legg dette til din `~/.bashrc` eller `~/.zshrc` slik at du aldri glemmer å bruke den:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Aldri gå glipp av `Ctrl+C`-sikkerhet:** Når du fanger ustabile skjell, fanger `rlwrap` opp grunnleggende tastaturfeil, men husk alltid å gjøre 5-trinns `stty raw -echo; fg`-oppgraderingen hvis du planlegger å kjøre lange interaktive kommandoer.
3. **Isoler klienthistorier:** Når du tester flere klienter eller maskiner, bruk alltid `-H ~/.client_name.hist` for å opprettholde separate revisjonsspor og kommandohistorikk.

---

## ⚠️ Juridisk ansvarsfraskrivelse

> **VIKTIG:** Omvendte skall og verktøy etter utnyttelse må kun brukes i autoriserte penetrasjonstester, engasjementer for røde team og pedagogiske laboratoriemiljøer med eksplisitt skriftlig samtykke.