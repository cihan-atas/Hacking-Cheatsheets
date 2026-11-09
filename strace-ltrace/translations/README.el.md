<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 Strace & ltrace - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
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
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="Dynamic Analysis">
</p>

<p align="center">
  <b>🔬 Βασικά βοηθητικά προγράμματα γραμμής εντολών για δυναμική δυαδική ανάλυση: υποκλοπή κλήσεων συστήματος πυρήνα Linux (strace) και κλήσεις δυναμικής βιβλιοθήκης χώρου χρήστη (ltrace)</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση: Κλήσεις συστήματος έναντι κλήσεων βιβλιοθήκης](#-overview-system-calls-vs-library-calls)
- [Εγκατάσταση] (#-εγκατάσταση)
- [strace: System Call Tracing](#-strace-system-call-tracing)
  - [Εκτέλεση βασικής ανίχνευσης](#basic-tracing-execution)
  - [Filtering Syscalls by Class or Name (`-e`)](#filtering-syscalls-by-class-or-name--e)
  - [Tracing Child & Multi-threaded Processes (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Attaching to Running Processes (`-p`)](#attaching-to-running-processes--p)
  - [Expanding Strings & Buffer Outputs (`-s`)](#expanding-strings--buffer-outputs--s)
  - [Στατιστικό προφίλ και ανάλυση συμφόρησης (`-c`)](#statistical-profiling--bottleneck-analysis--c)
  - [Fault Injection for Fuzzing & Testing (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace: Dynamic Library Call Tracing](#-ltrace-dynamic-library-call-tracing)
  - [Basic Library Interception](#basic-library-interception)
  - [Διάσπαση ελέγχων κωδικοποιημένου κωδικού πρόσβασης (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [Demangling C++ Symbols (`-C`)](#demangling-c-symbols--c)
  - [Filtering Specific Library Functions](#filtering-specific-library-functions)
- [Combining strace and ltrace Workflows](#-combining-strace-and-ltrace-workflows)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Anti-Tracing](#-defensive-considerations--anti-tracing)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση: Κλήσεις συστήματος έναντι κλήσεων βιβλιοθήκης

Κατά την ανάλυση μεταγλωττισμένων δυαδικών αρχείων Linux ELF, η στατική ανάλυση μπορεί να παρεμποδιστεί από συσκότιση, απογύμνωση ή συσκευασία. Η δυναμική ανίχνευση παρέχει άμεση ορατότητα σε ενέργειες χρόνου εκτέλεσης:

1. **Κλήσεις συστήματος (`strace`):**
   - Η διεπαφή μεταξύ των εφαρμογών χώρου χρήστη και του πυρήνα Linux Linux.
   - Ανιχνεύει I/O δίσκου (`openat`, `read`, `write`), δικτύωση (`socket`, `connect`), εκχώρηση μνήμης (`mmap`, `brk`) και διαχείριση διεργασιών (`fork`, `execve`, `ptrace`).
2. **Κλήσεις βιβλιοθήκης (`ltrace`):**
   - Η διεπαφή μεταξύ των δυαδικών και δυναμικών κοινόχρηστων βιβλιοθηκών (όπως `libc.so`, `libssl.so`).
   - Αναχαιτίζει κλήσεις όπως `strcmp()`, `strlen()`, `malloc()`, `system()` και κρυπτογραφικές ρουτίνες, εκθέτοντας συχνά διαπιστευτήρια απλού κειμένου στη μνήμη πριν από την κρυπτογράφηση.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ strace: System Call Tracing

### Εκτέλεση βασικής ανίχνευσης
Εκτελέστε ένα δυαδικό αρχείο προορισμού και μεταδώστε όλες τις κλήσεις συστήματος σε τυπικό σφάλμα:
```bash
strace ./target_binary
```
Ανακατευθύνετε την έξοδο σε ένα αρχείο, ώστε να μην ακατασταθεί η εκτέλεση του προγράμματος:
```bash
strace -o trace.log ./target_binary
```
### Φιλτράρισμα Syscalls κατά τάξη ή όνομα (`-e`)
Αποφύγετε το θόρυβο φιλτράροντας μόνο σχετικές κλάσεις syscall:
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
### Ανίχνευση θυγατρικών και διεργασιών πολλαπλών νημάτων (`-f`)
Τα σύγχρονα δυαδικά αρχεία και το κακόβουλο λογισμικό δημιουργούν διαδικασίες ή νήματα παιδικών εργαζομένων. Χωρίς `-f`, ο strace θα τα χάσει:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### Προσάρτηση σε διεργασίες που εκτελούνται (`-p`)
Παρακολουθήστε μια υπηρεσία ή μια διαδικασία ενεργού κακόβουλου λογισμικού χωρίς να την επανεκκινήσετε:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### Επέκταση συμβολοσειρών και εξόδων buffer (`-s`)
Από προεπιλογή, το strace περικόπτει τις τυπωμένες συμβολοσειρές με 32 χαρακτήρες. Αυξήστε το μήκος συμβολοσειράς για να επιθεωρήσετε πλήρεις διαδρομές αρχείων, διευθύνσεις URL ή ωφέλιμα φορτία:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### Στατιστικό προφίλ και ανάλυση συμφόρησης (`-c`)
Μέτρηση χρόνου, αριθμός κλήσεων και σφαλμάτων ανά syscall:
```bash
strace -c ./target_binary
```
*Παράδειγμα σύνοψης εξόδου:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### Έγχυση σφάλματος για θόλωση και δοκιμή (`-e fault`)
Εισάγετε αυθαίρετα σφάλματα στα syscals για να αξιολογήσετε την ανθεκτικότητα:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace: Dynamic Library Call Tracing

### Βασική Παρακολούθηση Βιβλιοθήκης
Υποκλοπή κλήσεων που γίνονται σε δυναμικές βιβλιοθήκες `.so`:
```bash
ltrace ./target_binary
```
### Διάσπαση ελέγχων κωδικοποιημένων κωδικών πρόσβασης (`strcmp`)
Στην αντίστροφη μηχανική CTF και την πρόκληση δυαδικών αρχείων, οι συναρτήσεις σύγκρισης κωδικών πρόσβασης (`strcmp`, `strncmp`, `memcmp`) αποκαλύπτουν τον κωδικό πρόσβασης στόχου σε ορίσματα απλού κειμένου:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Παράδειγμα εξόδου:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### Αφαίρεση συμβόλων C++ (`-C`)
Ονόματα μεθόδων C++ με παραμόρφωση μεταγλωττιστή (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```
### Φιλτράρισμα συγκεκριμένων λειτουργιών βιβλιοθήκης
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 Συνδυασμός ροών εργασίας strace και ltrace

Κατά την εκτέλεση αρχικής διαλογής σε ένα άγνωστο δυαδικό:

1. **Βήμα 1:** Εκτελέστε το `strace -e trace=file,network ./binary` για να δείτε με ποια αρχεία και υποδοχές αλληλεπιδρά.
2. **Βήμα 2:** Εκτελέστε το `ltrace -s 256 ./binary` για να αποκαλύψετε συγκρίσεις συμβολοσειρών εσωτερικής βιβλιοθήκης, κλήσεις κρυπτογράφησης και λογική μνήμης.
3. **Βήμα 3:** Συνδυάστε και τα δύο εργαλεία:
   ```bash
   ltrace -S ./binary
   ```
*(Η σημαία `-S` λέει στο ltrace να εμφανίζει κλήσεις συστήματος παράλληλα με κλήσεις βιβλιοθήκης σε μια ενιαία προβολή).*

---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Ανίχνευση όλων των Syscalls** | `strace ./binary` |
| **Σύνδεση στο αρχείο** | `strace -o log.txt ./binary` |
| **Λειτουργίες αρχείου ανίχνευσης** | `strace -e trace=file ./binary` |
| **Ανίχνευση δραστηριότητας δικτύου** | `strace -e trace=network ./binary` |
| **Ακολουθήστε τις διαδικασίες για παιδιά** | `strace -f ./binary` |
| **Επισύναψη στο τρέχον PID** | `sudo strace -f -p <PID>` |
| **Ανάπτυξη μήκους συμβολοσειράς** | `strace -s 2048 ./binary` |
| **Συνοπτικά στατιστικά του Syscall** | `strace -c ./binary` |
| **Ιχνηλάτηση κλήσεων βιβλιοθήκης** | `ltrace ./binary` |
| **Ελέγξτε τον κωδικό πρόσβασης** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **Demangle C++ Names** | `ltrace -C ./binary` |
| **Συνδυασμένη Lib + Syscall**| `ltrace -S ./binary` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Anti-Tracing

1. **Ptrace Self-Checking:** Το Linux επιτρέπει μόνο έναν ανιχνευτή να συνδεθεί σε μια διεργασία μέσω `ptrace`. Η κλήση του `ptrace(PTRACE_TRACEME, 0, 1, 0)` θα αποτύχει εάν το `strace` ή το `ltrace` είναι ήδη συνδεδεμένο.
2. **Στατική σύνδεση (`-static`):** Τα δυαδικά αρχεία που μεταγλωττίζονται με στατική σύνδεση δεν περιέχουν αναφορές δυναμικής βιβλιοθήκης (`DT_NEEDED`), καθιστώντας το `ltrace` εντελώς αναποτελεσματικό (αντ' αυτού χρησιμοποιήστε το `strace`).

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το strace και το ltrace είναι δυναμικά διαγνωστικά εργαλεία σχεδιασμένα για ανάπτυξη λογισμικού, εντοπισμό σφαλμάτων και εξουσιοδοτημένες αξιολογήσεις ασφαλείας. Η παρακολούθηση μη εξουσιοδοτημένων διαδικασιών ή η υποκλοπή αποκλειστικής επικοινωνίας χωρίς άδεια απαγορεύεται αυστηρά.