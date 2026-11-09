<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace & ltrace - Komplett jukseark

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
            Linux Dynamic Binary Analysis: System & Library Call Tracers
```
<p align="center">
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="Dynamisk analyse">
</p>

<p align="center">
  <b>🔬 Viktige kommandolinjeverktøy for dynamisk binær analyse: avskjæring av Linux-kjernesystemanrop (strace) og dynamiske bibliotekanrop på brukerområdet (ltrace)</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Overview: System Calls vs Library Calls](#-overview-system-calls-vs-library-calls)
- [Installasjon](#-installasjon)
- [strace: System Call Tracing](#-strace-system-call-tracing)
  - [Basic Tracing Execution](#basic-tracing-execution)
  - [Filtrering av syscalls etter klasse eller navn (`-e`)](#filtering-syscalls-by-class-or-name--e)
  - [Tracing Child & Multi-Threaded Processes (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Knytte til kjørende prosesser (`-p`)](#attaching-to-running-processes--p)
  - [Expanding Strings & Buffer Outputs (`-s`)](#expanding-strings--buffer-outputs--s)
  - [Statistisk profilering og flaskehalsanalyse (`-c`)](#statistical-profiling--flaskehalsanalyse--c)
  - [Feilinnsprøytning for fuzzing og testing (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace: Dynamic Library Call Tracing](#-ltrace-dynamic-library-call-tracing)
  - [Basic Library Interception](#basic-library-interception)
  - [Cracking Hardcoded Password Checks (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [Demangling C++ Symbols (`-C`)](#demangling-c-symbols--c)
  - [Filtrering av spesifikke bibliotekfunksjoner](#filtreringsspesifikke-bibliotek-funksjoner)
- [Kombinering av strace- og ltrace-arbeidsflyter](#-kombinerer-strace-og-ltrace-arbeidsflyter)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Anti-Tracing](#-defensive-considerations--anti-tracing)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt: Systemanrop vs bibliotekanrop

Når du analyserer kompilerte Linux ELF-binærfiler, kan statisk analyse hindres av tilsløring, stripping eller pakking. Dynamisk sporing gir umiddelbar innsyn i kjøretidshandlinger:

1. **Systemanrop (`strace`):**
   - Grensesnittet mellom brukerromsapplikasjoner og Linux Linux-kjernen.
   - Spor disk I/O (`openat`, `read`, `write`), nettverk (`socket`, `connect`), minneallokering (`mmap`, `brk`) og prosessadministrasjon (`fork`, `execve`, `ptrace`).
2. **Biblioteksamtaler (`ltrace`):**
   - Grensesnittet mellom de binære og dynamiske delte bibliotekene (som `libc.so`, `libssl.so`).
   - Avskjærer samtaler som `strcmp()`, `strlen()`, `malloc()`, `system()` og kryptografiske rutiner, og avslører ofte rentekstlegitimasjon i minnet før kryptering.

---

## 📥 Installasjon
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ sporing: Systemanropssporing

### Grunnleggende sporingsutførelse
Kjør en målbinær og strøm alle systemanrop til standardfeil:
```bash
strace ./target_binary
```
Omdiriger utdata til en fil slik at den ikke roter til programkjøring:
```bash
strace -o trace.log ./target_binary
```
### Filtrering av syscaller etter klasse eller navn (`-e`)
Unngå støy ved å filtrere bare relevante syscall-klasser:
```bash
# Trace ONLY file-related operations (open, read, write, stat, access)
strace -e trace=file ./target_binary

# Trace ONLY network socket connections and data transfers
strace -e trace=network ./target_binary

# Trace process execution and fork/clone operations
strace -e trace=process ./target_binary

# Filter specific syscalls by exact name
strace -e trace=open,openat,read,write ./target_binary
```
### Spore underordnede og flertrådede prosesser (`-f`)
Moderne binærfiler og skadelig programvare skaper prosesser eller tråder for barnearbeidere. Uten `-f` vil strace savne dem:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### Koble til løpende prosesser (`-p`)
Overvåk en tjeneste eller aktiv skadevareprosess uten å starte den på nytt:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### Utvidende strenger og bufferutganger (`-s`)
Som standard avkorter strace trykte strenger med 32 tegn. Øk strenglengden for å inspisere fullstendige filbaner, URL-er eller nyttelast:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### Statistisk profilering og flaskehalsanalyse (`-c`)
Tell tid brukt, antall anrop og feil per syscall:
```bash
strace -c ./target_binary
```
*Eksempel på utdatasammendrag:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### Feilinnsprøytning for fuzzing og testing (`-e fault`)
Injiser vilkårlige feil i syscalls for å evaluere motstandskraft:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace: Dynamisk bibliotekanropssporing

### Grunnleggende bibliotekavskjæring
Avlytte anrop til dynamiske `.so`-biblioteker:
```bash
ltrace ./target_binary
```
### Knekking av hardkodede passordkontroller (`strcmp`)
I reverse engineering CTF-er og utfordringsbinærfiler avslører passordsammenligningsfunksjoner (`strcmp`, `strncmp`, `memcmp`) målpassordet i klartekstargumenter:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Utgangseksempel:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### Demangler C++-symboler (`-C`)
Avvikle kompilatormanglede C++-metodenavn (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```
### Filtrering av spesifikke bibliotekfunksjoner
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 Kombinere strace og ltrace arbeidsflyter

Når du utfører innledende triage på en ukjent binær:

1. **Trinn 1:** Kjør `strace -e trace=file,network ./binary` for å se hvilke filer og sockets den samhandler med.
2. **Trinn 2:** Kjør `ltrace -s 256 ./binary` for å avdekke interne bibliotekstrengsammenlikninger, kryptoanrop og minnelogikk.
3. **Trinn 3:** Kombiner begge verktøyene:
   ```bash
   ltrace -S ./binary
   ```
*(`-S`-flagget forteller ltrace å vise systemanrop sammen med bibliotekanrop i en enkelt enhetlig visning).*

---

## ⚡ Hurtigreferansetabell

| Mål | Kommando |
|----------------|--------|
| **Spor alle syscalls** | `strace ./binary` |
| **Logg til fil** | `strace -o log.txt ./binary` |
| **Trace File Operations** | `strace -e trace=file ./binary` |
| **Spor nettverksaktivitet** | `strace -e trace=network ./binary` |
| **Følg barneprosesser** | `strace -f ./binary` |
| **Knytt til løpende PID** | `sudo strace -f -p <PID>` |
| **Utvid strenglengde** | `strace -s 2048 ./binary` |
| **Syscall-sammendragsstatistikk** | `strace -c ./binary` |
| **Spor biblioteksamtaler** | `ltrace ./binary` |
| **Fang passordsjekk** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **Demangle C++-navn** | `ltrace -C ./binary` |
| **Kombinert Lib + Syscall**| `ltrace -S ./binary` |

---

## 🛡️ Defensive betraktninger og anti-sporing

1. **Ptrace Self-Checking:** Linux tillater bare en enkelt tracer å koble til en prosess via `ptrace`. Å ringe `ptrace(PTRACE_TRACEME, 0, 1, 0)` vil mislykkes hvis `strace` eller `ltrace` allerede er tilkoblet.
2. **Statisk kobling (`-static`):** Binærfiler kompilert med statisk kobling inneholder ingen dynamiske bibliotekreferanser (`DT_NEEDED`), noe som gjør `ltrace` fullstendig ineffektiv (bruk `strace` i stedet).

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> strace og ltrace er dynamiske diagnoseverktøy utviklet for programvareutvikling, feilsøking og autoriserte sikkerhetsvurderinger. Å spore uautoriserte prosesser eller avskjære proprietær kommunikasjon uten tillatelse er strengt forbudt.