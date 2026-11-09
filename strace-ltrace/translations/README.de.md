<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace & ltrace – Vollständiger Cheatsheet

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
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="Dynamische Analyse">
</p>

<p align="center">
  <b>🔬 Wichtige Befehlszeilen-Dienstprogramme für die dynamische Binäranalyse: Abfangen von Linux-Kernel-Systemaufrufen (strace) und dynamischen Bibliotheksaufrufen im User-Space (ltrace)</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht: Systemaufrufe vs. Bibliotheksaufrufe](#-overview-system-calls-vs-library-calls)
- [Installation](#-Installation)
- [strace: Systemaufrufverfolgung](#-strace-system-call-tracing)
  - [Grundlegende Ablaufverfolgungsausführung](#basic-tracing-execution)
  - [Systemaufrufe nach Klasse oder Name filtern (`-e`)](#filtering-syscalls-by-class-or-name--e)
  - [Tracing untergeordneter und Multithread-Prozesse (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Anhängen an laufende Prozesse (`-p`)](#attaching-to-running-processes--p)
  - [Erweitern von Strings und Pufferausgängen (`-s`)](#expanding-strings--buffer-outputs--s)
  - [Statistische Profilerstellung und Engpassanalyse (`-c`)](#statistical-profiling--bottleneck-analysis--c)
  - [Fehlerinjektion für Fuzzing und Tests (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace: Dynamic Library Call Tracing](#-ltrace-dynamic-library-call-tracing)
  - [Basic Library Interception](#basic-library-interception)
  - [Festcodierte Passwortprüfungen knacken (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [C++-Symbole entwirren (`-C`)](#demangling-c-symbols--c)
  - [Spezifische Bibliotheksfunktionen filtern](#filtering-special-library-functions)
- [Strace- und Ltrace-Workflows kombinieren](#-combining-strace-and-ltrace-workflows)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Anti-Tracing](#-defensive-considerations--anti-tracing)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht: Systemaufrufe vs. Bibliotheksaufrufe

Bei der Analyse kompilierter Linux ELF-Binärdateien kann die statische Analyse durch Verschleierung, Strippen oder Packen behindert werden. Dynamische Ablaufverfolgung bietet sofortigen Einblick in Laufzeitaktionen:

1. **Systemaufrufe (`strace`):**
   – Die Schnittstelle zwischen User-Space-Anwendungen und dem Linux-Linux-Kernel.
   - Verfolgt Festplatten-E/A (`openat`, `read`, `write`), Netzwerk (`socket`, `connect`), Speicherzuweisung (`mmap`, `brk`) und Prozessverwaltung (`fork`,
2. **Bibliotheksaufrufe (`ltrace`):**
   - Die Schnittstelle zwischen den binären und dynamischen gemeinsam genutzten Bibliotheken (z. B. `libc.so`, `libssl.so`).
   – Fängt Aufrufe wie `strcmp()`, `strlen()`, `malloc()`, `system()` und kryptografische Routinen ab und legt vor der Verschlüsselung häufig Klartext-Anmeldeinformationen im Speicher offen.

---

## 📥 Installation
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ strace: Systemanrufverfolgung

### Grundlegende Ablaufverfolgungsausführung
Führen Sie eine Zielbinärdatei aus und streamen Sie alle Systemaufrufe in den Standardfehler:
```bash
strace ./target_binary
```
Leiten Sie die Ausgabe in eine Datei um, damit die Programmausführung nicht beeinträchtigt wird:
```bash
strace -o trace.log ./target_binary
```
### Systemaufrufe nach Klasse oder Name filtern (`-e`)
Vermeiden Sie Rauschen, indem Sie nur relevante Systemaufrufklassen filtern:
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
### Untergeordnete und Multithread-Prozesse verfolgen (`-f`)
Moderne Binärdateien und Malware erzeugen untergeordnete Worker-Prozesse oder Threads. Ohne `-f` wird Strace sie vermissen:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### Anhängen an laufende Prozesse (`-p`)
Überwachen Sie einen Dienst oder aktiven Malware-Prozess, ohne ihn neu zu starten:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### Erweitern von Zeichenfolgen und Pufferausgängen (`-s`)
Standardmäßig schneidet strace gedruckte Zeichenfolgen auf 32 Zeichen ab. Erhöhen Sie die Zeichenfolgenlänge, um vollständige Dateipfade, URLs oder Nutzdaten zu überprüfen:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### Statistische Profilerstellung und Engpassanalyse (`-c`)
Zählen Sie die aufgewendete Zeit, die Anzahl der Aufrufe und Fehler pro Systemaufruf:
```bash
strace -c ./target_binary
```
*Beispiel für eine Ausgabezusammenfassung:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### Fehlerinjektion für Fuzzing und Tests (`-e fault`)
Fügen Sie beliebige Fehler in Systemaufrufe ein, um die Ausfallsicherheit zu bewerten:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace: Dynamische Bibliotheksaufrufverfolgung

### Grundlegendes Abfangen der Bibliothek
Abfangen von Aufrufen an dynamische `.so`-Bibliotheken:
```bash
ltrace ./target_binary
```
### Hartcodierte Passwortprüfungen knacken (`strcmp`)
Beim Reverse Engineering von CTFs und Challenge-Binärdateien enthüllen Passwortvergleichsfunktionen (`strcmp`, `strncmp`, `memcmp`) das Zielpasswort in Klartextargumenten:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Ausgabebeispiel:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### C++-Symbole entwirren (`-C`)
Vom Compiler entstellte C++-Methodennamen entzerren (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```
### Filtern spezifischer Bibliotheksfunktionen
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 Kombination von Strace- und Ltrace-Workflows

Bei der ersten Triage einer unbekannten Binärdatei:

1. **Schritt 1:** Führen Sie `strace -e trace=file,network ./binary` aus, um zu sehen, mit welchen Dateien und Sockets es interagiert.
2. **Schritt 2:** Führen Sie `ltrace -s 256 ./binary` aus, um interne Bibliothekszeichenfolgenvergleiche, Kryptoaufrufe und Speicherlogik aufzudecken.
3. **Schritt 3:** Kombinieren Sie beide Tools:
   ```bash
   ltrace -S ./binary
   ```
*(Das Flag `-S` weist ltrace an, Systemaufrufe neben Bibliotheksaufrufen in einer einzigen einheitlichen Ansicht anzuzeigen.*

---

## ⚡ Kurzreferenztabelle

| Ziel | Befehl |
|-----------|---------|
| **Alle Systemaufrufe verfolgen** | `strace ./binary` |
| **In Datei protokollieren** | `strace -o log.txt ./binary` |
| **Trace-Dateivorgänge** | `strace -e trace=file ./binary` |
| **Netzwerkaktivität verfolgen** | `strace -e trace=network ./binary` |
| **Untergeordneten Prozessen folgen** | `strace -f ./binary` |
| **An laufende PID anhängen** | `sudo strace -f -p <PID>` |
| **Stringlänge erweitern** | `strace -s 2048 ./binary` |
| **Syscall-Zusammenfassungsstatistik** | `strace -c ./binary` |
| **Bibliotheksaufrufe verfolgen** | `ltrace ./binary` |
| **Passwortprüfung abfangen** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **C++-Namen entschlüsseln** | `ltrace -C ./binary` |
| **Kombinierte Bibliothek + Syscall**| `ltrace -S ./binary` |

---

## 🛡️ Verteidigungsüberlegungen und Anti-Tracing

1. **Ptrace-Selbstprüfung:** Unter Linux ist nur die Verbindung eines einzelnen Tracers mit einem Prozess über `ptrace` möglich. Der Aufruf von `ptrace(PTRACE_TRACEME, 0, 1, 0)` schlägt fehl, wenn `strace` oder `ltrace` bereits angehängt ist.
2. **Statische Verknüpfung (`-static`):** Mit statischer Verknüpfung kompilierte Binärdateien enthalten keine dynamischen Bibliotheksverweise (`DT_NEEDED`), wodurch `ltrace` völlig wirkungslos wird (verwenden Sie stattdessen `strace`).

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> strace und ltrace sind dynamische Diagnosetools für Softwareentwicklung, Debugging und autorisierte Sicherheitsbewertungen. Das Nachverfolgen nicht autorisierter Prozesse oder das Abfangen geschützter Kommunikation ohne Erlaubnis ist strengstens untersagt.