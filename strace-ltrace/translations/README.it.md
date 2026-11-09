<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace & ltrace - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="Analisi dinamica">
</p>

<palign="centro">
  <b>🔬 Utilità essenziali da riga di comando per l'analisi binaria dinamica: intercettazione delle chiamate di sistema del kernel Linux (strace) e delle chiamate di libreria dinamica nello spazio utente (ltrace)</b>
</p>

---

## 📋 Sommario

- [Panoramica: chiamate di sistema e chiamate alla biblioteca](#-overview-system-calls-vs-library-calls)
- [Installazione](#-installazione)
- [strace: tracciamento delle chiamate di sistema](#-strace-system-call-tracing)
  - [Esecuzione della traccia di base](#esecuzione-traccia-base)
  - [Filtraggio delle chiamate di sistema per classe o nome (`-e`)](#filtro delle chiamate di sistema per classe o nome--e)
  - [Tracciamento di processi secondari e multi-thread (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Collegamento ai processi in esecuzione (`-p`)](#attaching-to-running-processes--p)
  - [Espansione di stringhe e uscite buffer (`-s`)](#espansione-stringhe--buffer-uscite--s)
  - [Profilazione statistica e analisi dei colli di bottiglia (`-c`)](#profilazione-statistica--analisi-colli di bottiglia--c)
  - [Iniezione errori per fuzzing e test (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace: tracciamento delle chiamate della libreria dinamica](#-ltrace-dynamic-library-call-tracing)
  - [Intercettazione della libreria di base](#intercettazione-della-biblioteca-di-base)
  - [Cracking dei controlli delle password hardcoded (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [Demangolazione dei simboli C++ (`-C`)](#demangling-c-symbols--c)
  - [Filtro delle funzioni specifiche della libreria](#filtro-funzioni-specifiche-della-libreria)
- [Combinazione dei flussi di lavoro strace e ltrace](#-combining-strace-and-ltrace-workflows)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e anti-tracciamento](#-considerazioni-difensive--anti-tracciamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica: chiamate di sistema e chiamate in biblioteca

Quando si analizzano i binari ELF Linux compilati, l'analisi statica può essere impedita da offuscamento, rimozione o compressione. Il tracciamento dinamico fornisce visibilità immediata sulle azioni di runtime:

1. **Chiamate di sistema (`strace`):**
   - L'interfaccia tra le applicazioni dello spazio utente e il kernel Linux di Linux.
   - Traccia l'I/O del disco (`openat`, `read`, `write`), la rete (`socket`, `connect`), l'allocazione della memoria (`mmap`, `brk`) e la gestione dei processi (`fork`, `execve`, `ptrace`).
2. **Chiamate alla biblioteca (`ltrace`):**
   - L'interfaccia tra le librerie condivise binarie e dinamiche (come `libc.so`, `libssl.so`).
   - Intercetta chiamate come `strcmp()`, `strlen()`, `malloc()`, `system()` e routine crittografiche, esponendo spesso credenziali di testo normale in memoria prima della crittografia.

---

## 📥Installazione
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ strace: tracciamento delle chiamate di sistema

### Esecuzione della traccia di base
Esegui un binario di destinazione e trasmetti tutte le chiamate di sistema all'errore standard:
```bash
strace ./target_binary
```
Reindirizzare l'output su un file in modo da non ingombrare l'esecuzione del programma:
```bash
strace -o trace.log ./target_binary
```
### Filtraggio delle chiamate di sistema per classe o nome (`-e`)
Evita il rumore filtrando solo le classi di chiamata di sistema rilevanti:
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
### Tracciamento di processi secondari e multi-thread (`-f`)
I file binari e i malware moderni generano processi o thread di lavoro minori. Senza `-f`, a Strace mancheranno:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### Collegamento a processi in esecuzione (`-p`)
Monitora un servizio o un processo malware attivo senza riavviarlo:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### Espansione di stringhe e uscite buffer (`-s`)
Per impostazione predefinita, strace tronca le stringhe stampate a 32 caratteri. Aumenta la lunghezza della stringa per controllare percorsi di file completi, URL o payload:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### Profilazione statistica e analisi dei colli di bottiglia (`-c`)
Conta il tempo impiegato, il numero di chiamate e gli errori per chiamata di sistema:
```bash
strace -c ./target_binary
```
*Esempio di riepilogo dell'output:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### Iniezione errori per fuzzing e test (`-e fault`)
Iniettare errori arbitrari nelle chiamate di sistema per valutare la resilienza:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace: tracciamento delle chiamate della libreria dinamica

### Intercettazione di base della biblioteca
Intercetta le chiamate effettuate alle librerie `.so` dinamiche:
```bash
ltrace ./target_binary
```
### Violazione dei controlli delle password hardcoded (`strcmp`)
Nei CTF di reverse engineering e nei file binari di sfida, le funzioni di confronto delle password (`strcmp`, `strncmp`, `memcmp`) rivelano la password di destinazione negli argomenti in chiaro:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Esempio di output:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### Smantellamento dei simboli C++ (`-C`)
Elimina i nomi dei metodi C++ alterati dal compilatore (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```
### Filtraggio di funzioni specifiche della libreria
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 Combinazione dei flussi di lavoro strace e ltrace

Quando si esegue il triage iniziale su un binario sconosciuto:

1. **Passaggio 1:** Esegui `strace -e trace=file,network ./binary` per vedere con quali file e socket interagisce.
2. **Passaggio 2:** Esegui `ltrace -s 256 ./binary` per scoprire confronti di stringhe della libreria interna, chiamate crittografiche e logica di memoria.
3. **Passaggio 3:** Combina entrambi gli strumenti:
   ```bash
   ltrace -S ./binary
   ```
*(Il flag `-S` indica a ltrace di visualizzare le chiamate di sistema insieme alle chiamate di libreria in un'unica visualizzazione unificata).*

---

## ⚡ Tabella di consultazione rapida

| Obiettivo | Comando |
|-----------|---------|
| **Traccia tutte le chiamate di sistema** | `strace ./binary` |
| **Accedi al file** | `strace -o log.txt ./binary` |
| **Operazioni sui file di traccia** | `strace -e trace=file ./binary` |
| **Traccia attività di rete** | `strace -e trace=network ./binary` |
| **Segui i processi secondari** | `strace -f ./binary` |
| **Allega al PID in esecuzione** | `sudo strace -f -p <PID>` |
| **Espandi la lunghezza della stringa** | `strace -s 2048 ./binary` |
| **Statistiche riepilogative delle chiamate di sistema** | `strace -c ./binary` |
| **Traccia chiamate alla libreria** | `ltrace ./binary` |
| **Prendi il controllo della password** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **Demangle nomi C++** | `ltrace -C ./binary` |
| **Lib + Syscall combinati**| `ltrace -S ./binary` |

---

## 🛡️ Considerazioni difensive e anti-tracciamento

1. **Autocontrollo del tracciamento:** Linux consente a un solo tracciante di collegarsi a un processo tramite `ptrace`. La chiamata a `ptrace(PTRACE_TRACEME, 0, 1, 0)` fallirà se `strace` o `ltrace` è già collegato.
2. **Collegamento statico (`-static`):** I file binari compilati con collegamento statico non contengono riferimenti alla libreria dinamica (`DT_NEEDED`), rendendo `ltrace` completamente inefficace (utilizza invece `strace`).

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> strace e ltrace sono strumenti diagnostici dinamici progettati per lo sviluppo di software, il debug e le valutazioni di sicurezza autorizzate. È severamente vietato tracciare processi non autorizzati o intercettare comunicazioni proprietarie senza autorizzazione.