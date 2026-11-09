<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec & Binutils - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```
<p align="center">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p align="center">
  <b>🛡️ Βασικά εργαλεία γραμμής εντολών για τον έλεγχο εκτελέσιμων μετριασμών ασφαλείας του Linux (RELRO, Stack Canary, NX, PIE, Fortify) και την ανατομή δυαδικών κεφαλίδων, τμημάτων και πινάκων συμβόλων ELF</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Overview & Binary Mitigations Matrix](#-overview--binary-mitigations-matrix)
- [Εγκατάσταση] (#-εγκατάσταση)
- [checksec: Rapid Mitigation Auditing](#-checksec-rapid-mitigation-auditing)
  - [Auditing Single & Batch Binaries](#auditing-single--batch-binaries)
  - [Auditing Running Processes](#auditing-running-processes)
  - [Έξοδος JSON & CSV](#json--csv-output)
- [readelf: ELF Header & Section Dissection](#-readelf-elf-header--section-dissection)
  - [ELF File Header (`-h`)](#elf-file-header--h)
  - [Κεφαλίδες και τμήματα προγράμματος (`-l`)](#program-headers--segments--l)
  - [Section Headers (`-S`)](#section-headers--s)
  - [Πίνακες συμβόλων (`-s` / `--syms`)](#symbol-tables--s----syms)
  - [Dynamic Dependencies & Libraries (`-d`)](#dynamic-dependencies--libraries--d)
- [objdump: Disassembly & Relocations](#-objdump-disassembly--relocations)
  - [Intel Disassembly (`-d -M intel`)](#intel-disassembly--d--m-intel)
  - [Εγγραφές μετεγκατάστασης & GOT (`-R`)](#relocation-entries--got--r)
- [nm & strings: Symbol Resolution & String](#-nm--strings-symbol-resolution--strings)
  - [Καταχώριση απογυμνωμένων συμβόλων με `nm`](#listing-demangled-symbols-with-nm)
  - [Εξαγωγή συσκοτισμένων συμβολοσειρών με `strings`](#extracting-obfuscated-strings-with-strings)
- [Hardened Compilation Reference](#-hardened-compilation-reference)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & δυαδικός πίνακας μετριασμού

Πριν ξεκινήσετε οποιαδήποτε ροή εργασιών αντίστροφης μηχανικής ή εκμετάλλευσης, είναι υποχρεωτικό να κατανοήσετε ποιοι μετριασμούς ασφαλείας είναι ενεργοποιημένοι:

| Μετριασμός | Πλήρες Όνομα | Μηχανισμός Προστασίας | Στρατηγική Παράκαμψης |
|------------|-----------|---------------------|-----------------|
| **RELRO** | Μετακίνηση Μόνο για ανάγνωση | Κάνει το `.got` μόνο για ανάγνωση (`Full RELRO`) για να αποτρέψει την αντικατάσταση δείκτη | Στοχεύστε δομές σωρού, διευθύνσεις επιστροφής ή άγκιστρα συναρτήσεων |
| **Καναρίνι** | Stack Protector | Τοποθετεί τυχαία cookie πριν από τον δείκτη επιστροφής. ελέγχει την ακεραιότητα κατά την επιστροφή | Διαρροή τιμής καναρίνι μέσω διαρροής μνήμης, ή ωμής δύναμης (διακλάδωση δαίμονες) |
| **NX** | Χωρίς εκτέλεση / DEP | Επισημαίνει τμήματα στοίβας, σωρού και δεδομένων ως μη εκτελέσιμα | Προγραμματισμός προσανατολισμένου προς την επιστροφή (ROP) ή προγραμματισμός προσανατολισμένου προς την επιστροφή (JOP) |
| **ΠΙΤΑ** | Θέση Ανεξάρτητο Εκτελεστό | Φορτώνει τη δυαδική διεύθυνση βάσης σε τυχαία τοποθεσία στην εικονική μνήμη (ASLR) | Δείκτης κώδικα διαρροής για τον υπολογισμό της μετατόπισης διεύθυνσης βάσης |
| **Οχυρώστε** | `_FORTIFY_SOURCE` | Αντικαθιστά μη ασφαλείς συναρτήσεις συμβολοσειράς (`strcpy` -> `__strcpy_chk`) με έλεγχο ορίων | Λογικά σφάλματα χωρίς υπερχείλιση, μορφοποίηση συμβολοσειράς χωρίς υπερχείλιση buffer |

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec: Ταχεία Έλεγχος Μετριασμού

### Έλεγχος δυαδικών απλών & παρτίδων
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Παράδειγμα τυπικής εξόδου:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### Έλεγχος Εκτελούμενων Διαδικασιών
Έλεγχος μετριασμού μνήμης και κατάστασης ASLR ενός ενεργού δαίμονα:
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### Έξοδος JSON & CSV
Εξαγωγή ευρημάτων για αυτοματοποιημένη ανάλυση:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf: ELF Header & Section Dissection

Το βοηθητικό πρόγραμμα `readelf` εμφανίζει πληροφορίες από αρχεία ELF χωρίς να επικαλείται καμία εκτέλεση:

### Κεφαλίδα αρχείου ELF (`-h`)
Αποκαλύπτει την αρχιτεκτονική της CPU, την κλάση 32 έναντι 64-bit, τη διεύθυνση σημείου εισόδου και την endianness:
```bash
readelf -h ./target_binary
```
### Κεφαλίδες και τμήματα προγράμματος (`-l`)
Εμφανίζει τμήματα που έχουν φορτωθεί στη μνήμη κατά το χρόνο εκτέλεσης και σημαίες (`R`, `W`, `E`). Είναι ζωτικής σημασίας για τον προσδιορισμό του εάν η στοίβα (`GNU_STACK`) έχει δικαιώματα εκτέλεσης:
```bash
readelf -l ./target_binary
```
*(Εάν το `GNU_STACK` έχει `RWE`, επιτρέπεται η εκτέλεση στοίβας, το NX είναι απενεργοποιημένο).*

### Κεφαλίδες Ενοτήτων (`-S`)
Ελέγξτε τις διατάξεις, τις διευθύνσεις και τις μετατοπίσεις μνήμης για `.text` (κωδικός), `.data` (αρχικοποιημένα δεδομένα), `.rodata` (σταθερές), `.bss` (μη αρχικοποιημένα δεδομένα), `.plt` και `.got`:
```bash
readelf -S ./target_binary
```
### Πίνακες συμβόλων (`-s` / `--syms`)
Καταχωρίστε όλες τις εξαγόμενες συναρτήσεις, τις καθολικές μεταβλητές και τα εισαγόμενα σύμβολα libc:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### Δυναμικές εξαρτήσεις και βιβλιοθήκες (`-d`)
Εμφανίζει τις απαιτούμενες κοινόχρηστες βιβλιοθήκες (ετικέτες `NEEDED`, π.χ. `libc.so.6`, `libssl.so`):
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump: Αποσυναρμολόγηση & μετατοπίσεις

### Αποσυναρμολόγηση Intel (`-d -M intel`)
Αποσυναρμολογήστε εκτελέσιμα τμήματα σε καθαρό συγκρότημα σύνταξης Intel:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### Εγγραφές μετεγκατάστασης & GOT (`-R`)
Εμφάνιση των διευθύνσεων μετεγκατάστασης του Παγκόσμιου Πίνακα Μετατόπισης (GOT) για εισαγόμενες κλήσεις βιβλιοθήκης:
```bash
objdump -R ./target_binary
```
*Εμφανίζει στόχους διευθύνσεων για λειτουργίες όπως `puts`, `printf` και `system`.*

---

## 🔍 nm & συμβολοσειρές: Ανάλυση συμβόλων & συμβολοσειρές

### Καταχώριση απογυμνωμένων συμβόλων με `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### Εξαγωγή ασαφούς συμβολοσειρών με το `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ Αναφορά σκληρυμένης συλλογής

Για να δημιουργήσετε ένα δυαδικό ELF με μέγιστους μετριασμούς ασφαλείας:
```bash
gcc -O2 \
  -Wall \
  -fstack-protector-strong \
  -D_FORTIFY_SOURCE=2 \
  -Wl,-z,relro,-z,now \
  -fPIE -pie \
  -Wl,-z,noexecstack \
  source.c -o secure_binary
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Μετριασμούς Ελέγχου** | `checksec --file=<bin>` |
| **Audit Running PID** | `checksec --proc=<pid>` |
| **Προβολή κεφαλίδας ELF** | `readelf -h <bin>` |
| **Προβολή Stack NX Flag** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **Ενότητες λίστας (.got, .plt)**| `readelf -S <bin>` |
| **Προβολή δυναμικών συμβόλων** | `readelf --dyn-syms <bin>` |
| **Αποσυναρμολόγηση Intel** | `objdump -d -M intel <bin>` |
| **Επιθεώρηση GOT Relocations** | `objdump -R <bin>` |
| **Σύμβολα λίστας (Αποδιαλυμένα)** | `nm -C <bin>` |
| **Εξαγωγή συμβολοσειρών UTF-16** | `strings -e l -a <bin>` |
| **Εμφάνιση μετατοπίσεων συμβολοσειρών** | `strings -a -t x <bin>` |

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το checksec και το GNU Binutils είναι εργαλεία διάγνωσης και αντίστροφης μηχανικής που προορίζονται για έλεγχο ασφάλειας λογισμικού, επικύρωση άμυνας και εξουσιοδοτημένη δυαδική έρευνα. Η επιθεώρηση μη εξουσιοδοτημένου λογισμικού πρέπει να συμμορφώνεται με τους τοπικούς κανονισμούς πνευματικής ιδιοκτησίας.