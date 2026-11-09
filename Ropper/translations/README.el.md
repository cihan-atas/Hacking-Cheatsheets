<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget - Ολοκληρωμένο Cheatsheet

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```
<p align="center">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="NX Bypass">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="Exploit Dev">
</p>

<p align="center">
  <b>🧩 Βασικά βοηθητικά προγράμματα γραμμής εντολών για την ανακάλυψη gadget προγραμματισμού προσανατολισμένου στην επιστροφή (ROP) και προγραμματισμού προσανατολισμού μετάβασης (JOP) για να παρακάμψετε τις μη εκτελέσιμες (NX / DEP) προστασίες μνήμης</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Θεωρία ROP](#-επισκόπηση--θεωρία rop)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Basic Gadget Discovery](#-basic-gadget-discovery)
  - [Εξαγωγή όλων των gadget] (#extracting-all-gadgets)
  - [Limiting Instruction Depth](#limiting-instruction-depth)
- [Σύνθετη αναζήτηση και φιλτράρισμα μοτίβων](#-advanced-search--pattern-filtering)
  - [Targeting Specific Registers (`rdi`, `rsi`, `rdx`, `rax`)](#targeting-specific-registers-rdi-rsi-rdx-rax)
  - [Gadgets εγγραφής μνήμης (Write-What-Where)](#memory-write-gadgets-write-what-where)
  - [Gadget Syscall & Interrupt](#syscall--interrupt-gadgets)
- [Εξαιρούνται οι κακοί χαρακτήρες (`--badbytes`)] (#-εξαιρούνται οι κακοί χαρακτήρες---badbyte)
- [Automated ROP Chain Generation] (#-automated-rop-chain-generation)
  - [Ropper Automated Chains] (#ropper-automated-chains)
  - [ROPgadget Automated Chains](#ropgadget-automated-chains)
- [Analysing Loaded Libraries (libc ROP Chains)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Mitigations](#-defensive-considerations--mitigations)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Θεωρία ROP

Τα σύγχρονα λειτουργικά συστήματα επιβάλλουν το **NX (No-Execute) / DEP (Data Execution Prevention)**, το οποίο εμποδίζει την άμεση εκτέλεση του shellcode που τοποθετείται στη στοίβα ή στο σωρό.

Ο **Προγραμματισμός με προσανατολισμό στην επιστροφή (ROP)** παρακάμπτει αυτόν τον περιορισμό επαναχρησιμοποιώντας μικρές υπάρχουσες οδηγίες μηχανήματος που υπάρχουν ήδη στην εκτελέσιμη μνήμη (τμήμα `.text` της δυαδικής ή κοινόχρηστων βιβλιοθηκών όπως η `libc.so`). Κάθε ακολουθία εντολών που τελειώνει σε μια εντολή `ret` ονομάζεται **gadget**. Τακτοποιώντας διευθύνσεις στη στοίβα, οι εισβολείς ενώνουν τα gadget μεταξύ τους για να:
1. Φορτώστε ορίσματα σε καταχωρητές CPU (π.χ. `pop rdi; ret` για συμβάσεις κλήσης x64).
2. Γράψτε συμβολοσειρές (όπως `/bin/sh`) σε εγγράψιμες ενότητες `.bss` ή `.data`.
3. Ενεργοποιήστε το `execve("/bin/sh", NULL, NULL)` ή καλέστε το `system()`.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y ropper python3-ropgadget

# Python pip (Latest Releases)
pip install ropper capstone
pip install ROPgadget

# Arch Linux
sudo pacman -S ropper python-ropgadget
```
---

## 🚀 Βασική Ανακάλυψη Gadget

### Εξαγωγή όλων των gadget
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### Περιορισμός βάθους εντολών
Μειώστε τις θορυβώδεις μακριές αλυσίδες περιορίζοντας το μήκος του gadget (αριθμός οδηγιών πριν από το `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 Προηγμένη αναζήτηση και φιλτράρισμα μοτίβων

### Συγκεκριμένοι καταχωρητές στόχευσης (`rdi`, `rsi`, `rdx`, `rax`)
Στη σύμβαση κλήσης Linux 64-bit, τα τρία πρώτα ορίσματα σε μια συνάρτηση ή μια κλήση συστήματος πρέπει να βρίσκονται στα `rdi`, `rsi` και `rdx`:
```bash
# Find pop rdi gadgets (1st argument)
ropper --file ./target_binary --search "pop rdi"

# Find pop rsi gadgets (2nd argument)
ropper --file ./target_binary --search "pop rsi"

# Find pop rdx gadgets (3rd argument)
ropper --file ./target_binary --search "pop rdx"

# Find gadgets that control RAX (Syscall number: 59 for execve)
ropper --file ./target_binary --search "pop rax"
```
Χρήση αντιστοίχισης ROPgadget regex:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### Gadgets εγγραφής μνήμης (Write-What-Where)
Όταν χρειάζεται να αποθηκεύσετε `/bin/sh` ή αυθαίρετα ορίσματα στη μνήμη:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Παράδειγμα αντιστοίχισης:* Το `mov qword ptr [rdi], rsi; ret` επιτρέπει την εγγραφή ό,τι υπάρχει στο `rsi` στη διεύθυνση που υποδεικνύει το `rdi`.

### Syscall & Interrupt Gadgets
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 Εξαιρούνται οι κακοί χαρακτήρες (`--badbytes`)

Οι ευπάθειες ή οι συναρτήσεις μορφοποίησης συμβολοσειρών όπως το `strcpy()` τερματίζονται σε μηδενικά byte (`\x00`), νέες γραμμές (`\x0a`) ή επιστροφές μεταφοράς (`\x0d`). Εξαίρεση διευθύνσεων που περιέχουν αυτά τα byte:
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ Αυτοματοποιημένη δημιουργία αλυσίδας ROP

Και τα δύο εργαλεία μπορούν να δημιουργήσουν αυτόματα λειτουργικά πρότυπα εκμετάλλευσης Python που καλούν το `execve("/bin/sh")`:

### Αυτοματοποιημένες αλυσίδες Ropper
```bash
ropper --file ./target_binary --chain "execve"
```
### Αυτοματοποιημένες αλυσίδες ROPgadget
```bash
ROPgadget --binary ./target_binary --ropchain
```
*Αυτό παράγει έναν έτοιμο προς χρήση σκελετό ωφέλιμου φορτίου Python που αλυσιδώνει `mprotect()` ή `execve()`.*

---

## 📚 Ανάλυση φορτωμένων βιβλιοθηκών (libc ROP Chains)

Όταν το ίδιο το δυαδικό έχει μεταγλωττιστεί ως **PIE** ή περιέχει λίγα χρήσιμα gadget, εξάγετε gadget απευθείας από την κοινόχρηστη βιβλιοθήκη C του συστήματος προορισμού (`libc.so.6`):
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Προσθέστε μετατοπίσεις που εντοπίστηκαν στη διεύθυνση βάσης του libc που διέρρευσε κατά το χρόνο εκτέλεσης).*

---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή Ropper | Εντολή ROPgadget |
|-----------|----------------|-------------------|
| **Απόθεση όλων των gadget** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Συγκεκριμένο gadget Αναζήτησης** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Φιλτράρισμα κακών bytes** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Όριο βάθους / μήκους** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **Αναζήτηση JOP Gadgets** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **Αυτόματη κατασκευή ROP Αλυσίδα** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Αναζήτηση Μνήμης Γράφει** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Αμυντικοί προβληματισμοί & μετριασμοί

1. **Τεχνολογία επιβολής ροής ελέγχου (Intel CET / Shadow Stack):** Οι σύγχρονοι επεξεργαστές διατηρούν μια απομονωμένη σκιώδη στοίβα υλικού που επαληθεύει τις διευθύνσεις επιστροφής στο `ret`, τερματίζοντας αμέσως τις αλυσίδες ROP.
2. **Clang / Ακεραιότητα ροής ελέγχου GCC (CFI):** Επικυρώνει τους στόχους του δείκτη συναρτήσεων έναντι ενός γραφήματος προς τα εμπρός πριν από έμμεσες κλήσεις.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Ropper και το ROPgadget είναι βοηθητικά προγράμματα ανάπτυξης εκμετάλλευσης και αξιολόγησης ασφάλειας λογισμικού. Πρέπει να χρησιμοποιούνται αυστηρά σε εξουσιοδοτημένες δοκιμές διείσδυσης, προκλήσεις CTF ή αμυντική έρευνα ασφάλειας. Η εκμετάλλευση λογισμικού χωρίς ρητή εξουσιοδότηση είναι παράνομη.