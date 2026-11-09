<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
         Next-Gen GDB Enhancements for Reversing & Binary Exploitation
```
<p align="center">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="Reversing & Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="License FOSS">
</p>

<p align="center">
  <b>🎯 Οι κορυφαίες βελτιώσεις τερματικού GDB ανοιχτού κώδικα που έχουν σχεδιαστεί για δυαδική αντίστροφη μηχανική, ανάλυση καταστροφής μνήμης, επιθεώρηση διάταξης σωρού και ανάπτυξη εκμετάλλευσης CTF</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Γιατί το Vanilla GDB χρειάζεται βελτιώσεις](#-επισκόπηση--why-vanilla-gdb-needs-enhancements)
- [Installation & Switching Between Frameworks](#-installation--switching-between-frameworks)
  - [Εγκατάσταση pwndbg](#installing-pwndbg)
  - [Εγκατάσταση GEF](#installing-gef)
  - [Διαχείριση `.gdbinit`](#managing-gdbinit)
- [The Context Display Engine] (#-the-context-display-engine)
  - [Ενότητα Εγγραφών](#registers-section)
  - [Disassembly & Jump Predictions](#disassembly--jump-predictions)
  - [Stack & Telescope](#stack--telescope)
  - [Backtrace & Threads](#backtrace--threads)
- [Επιθεώρηση και πλοήγηση μνήμης](#-memory-inspection--navigation)
  - [Telescoping Pointers (`telescope`)](#telescoping-pointers-telescope)
  - [Αντιστοιχίσεις μνήμης διεργασιών (`vmmap`)](#process-memory-mappings-vmmap)
  - [Αναζήτηση μνήμης (`search`)](#searching-memory-search)
- [Επιθεώρηση & Εκμετάλλευση Σωρού](#-heap-exploitation--inspection)
  - [Προβολή κομμάτια και αρένες](#viewing-chunks--arenas)
  - [Επιθεώρηση δωρεάν κάδων (Tcache, Fastbins, Smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [Οπτικοποίηση διάταξης σωρού (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [Exploit Development Helpers](#-exploit-development-helpers)
  - [Checksec & Mitigation Audit](#checksec--mitigation-audit)
  - [Stack Canary Locator (`canary`)](#stack-canary-locator-canary)
  - [Cyclic Patterns & Offset Calculation](#cyclic-patterns--offset-calculation)
  - [In-Memory ROP Gadget Hunting](#in-memory-rop-gadget-hunting)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Binary Protections](#-defensive-considerations--binary-protections)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Γιατί το Vanilla GDB χρειάζεται βελτιώσεις

Ο τυπικός εντοπισμός σφαλμάτων GNU (`gdb`) είναι ένα εργαλείο βιομηχανικής αντοχής, αλλά η εμπειρία χρήστη του είναι αυστηρή οπτικά. Δεν ενημερώνει αυτόματα τις καταστάσεις εγγραφής, τους δείκτες πολλαπλών επιπέδων αποσύνδεσης ή την εμφάνιση προβολών στοίβας κατά τη μετάβαση στις οδηγίες.

Τα **pwndbg** και **GEF** μετατρέπουν το GDB σε μια ελίτ σουίτα αντίστροφης μηχανικής και δυαδικής εκμετάλλευσης:
- **pwndbg:** Ειδικά σχεδιασμένο για εκμετάλλευση λογισμικού χαμηλού επιπέδου, εντοπισμό σφαλμάτων σωρού (glibc 2.27 - 2.39+ εσωτερικά στοιχεία εκχωρητή) και αυτοματοποιημένη αποαναφορά δείκτη.
- **GEF (GDB Enhanced Features):** Ένα ενιαίο αυτόνομο σενάριο Python με μηδενικές εξωτερικές εξαρτήσεις, που προσφέρει αγνωστικά χαρακτηριστικά αρχιτεκτονικής (ARM, MIPS, PowerPC, x86) και δυναμική ανάλυση δομής.

---

## 📥 Εγκατάσταση & εναλλαγή μεταξύ πλαισίων

### Εγκατάσταση του pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### Εγκατάσταση GEF
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### Διαχείριση `.gdbinit`
Για εναλλαγή μεταξύ pwndbg και GEF χωρίς διένεξη, διαμορφώστε το `~/.gdbinit`:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ Η μηχανή εμφάνισης περιβάλλοντος

Κάθε φορά που η εκτέλεση σταματά σε σημείο διακοπής ή βήμα, τόσο το pwndbg όσο και το GEF εκτυπώνουν μια αυτοματοποιημένη προβολή τερματικού πολλαπλών πλαισίων:
```text
──────────────────────────[ REGISTERS / FLAGS ]──────────────────────────
RAX: 0x0 
RBX: 0x7fffffffe000 
RCX: 0x7ffff7ec2000 --> 0x00010102464c457f 
RDX: 0x7fffffffe1a0 --> 0x7fffffffe494 ('USER=zeus')
RSI: 0x7fffffffe198 --> 0x7fffffffe47e ('/bin/target')
RDI: 0x1 
RBP: 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
RSP: 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
RIP: 0x401140 (<main+12>: cmp QWORD PTR [rbp-0x8], 0x0)
──────────────────────────[ DISASM / CODE ]──────────────────────────
   0x401135 <main+1>     push   rbp
   0x401136 <main+2>     mov    rbp, rsp
   0x401139 <main+5>     sub    rsp, 0x10
 ► 0x401140 <main+12>    cmp    QWORD PTR [rbp-0x8], 0
   0x401145 <main+17>    je     0x401151 <main+29>  <-- JUMP IS TAKEN
   0x401147 <main+19>    mov    eax, 0x1
──────────────────────────[ STACK ]──────────────────────────
00:0000│ rsp 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
01:0008│     0x7fffffffe088 --> 0x0
02:0010│ rbp 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
03:0018│     0x7fffffffe0a8 --> 0x7ffff7df6d90 (<__libc_start_call_main+128>: mov edi, eax)
──────────────────────────[ BACKTRACE ]──────────────────────────
 ► 0   0x401140 main+12
   1   0x7ffff7df6d90 __libc_start_call_main+128
   2   0x7ffff7df6e40 __libc_start_main+128
   3   0x401065 _start+37
```
---

## 🔍 Επιθεώρηση Μνήμης & Πλοήγηση

### Τηλεσκοπικοί δείκτες (`telescope`)
Αποαναφορά δείκτες μνήμης αναδρομικά, εμφανίζοντας κείμενο ASCII, σύμβολα συναρτήσεων ή δείκτες στόχων:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### Αντιστοιχίσεις μνήμης διεργασιών (`vmmap`)
Εμφανίζει πλήρη τμήματα εικονικής μνήμης με δικαιώματα ανάγνωσης/εγγραφής/εκτέλεσης:
```text
pwndbg> vmmap
```
*Γρήγορη εντόπιση τμημάτων με άδειες `rwx` (ζωτική σημασία για την έγχυση κώδικα κελύφους).*

### Μνήμη αναζήτησης (`search`)
Εντοπίστε συμβολοσειρές, ακέραιους αριθμούς ή δείκτες οπουδήποτε στις σελίδες φορτωμένης μνήμης:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 Εκμετάλλευση & Επιθεώρηση Σωρού

Η ανάλυση των σύγχρονων κατανεμητών σωρού `glibc` είναι απλή με αποκλειστικές εντολές σωρού:

### Προβολή κομματιών και αρένας
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### Επιθεώρηση δωρεάν κάδων (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### Οπτικοποίηση διάταξης σωρού (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ Exploit Development Helpers

### Έλεγχος ελέγχου και περιορισμού
Ελέγξτε τους μετριασμούς ασφαλείας του μεταγλωττιστή απευθείας από την προτροπή GDB:
```text
pwndbg> checksec
```
*Επιθεωρεί:*
- **RELRO:** Μερικό έναντι πλήρους RELRO (προστατεύει τον παγκόσμιο πίνακα μετατόπισης `.got.plt`)
- **Στοίβα:** Καναρίνι βρέθηκε vs Κανένα καναρίνι
- **NX:** NX ενεργοποιημένο (Μη εκτελέσιμη στοίβα/σωρό)
- **PIE:** PIE ενεργοποιημένο (Εκτέλεσιμο ανεξάρτητο θέσης / ASLR)

### Stack Canary Locator (`canary`)
Εντοπίστε την τιμή του καναρινιού νήματος στο Thread Local Storage (TLS) και επαληθεύστε πού βρίσκεται στο πλαίσιο της τρέχουσας στοίβας:
```text
pwndbg> canary
```
### Κυκλικά μοτίβα & υπολογισμός μετατόπισης
Προσδιορίστε την ακριβή μετατόπιση για την αντικατάσταση του δείκτη εντολών (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### In-Memory ROP Gadget Hunting
Αναζήτηση φορτωμένων εκτελέσιμων τμημάτων για συγκεκριμένα gadget:
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Ξεκινήστε με pwndbg/GEF**| `gdb ./target_binary` |
| **Επιθεώρηση Μητρώων/Στοίβα** | `context` |
| **Αναδρομικός δείκτης Deref**| `telescope $rsp 10` |
| **Χάρτης μνήμης & Έλεγχος RWX**| `vmmap` |
| **Αναζήτηση συμβολοσειράς στη μνήμη**| `search -s "/bin/sh"` |
| **Μετριασμούς Ελέγχου** | `checksec` |
| **Βρείτε Stack Canary** | `canary` |
| **Επισκόπηση τεμαχίων σωρού** | `heap` |
| **Επιθεώρηση δωρεάν κάδων** | `bins` |
| **Υπολογισμός μετατόπισης buffer**| `cyclic 100` -> `cyclic -l <val>` |
| **Ρύθμιση σημείου διακοπής στο σύμβολο**| `b *main` |
| **Εκτέλεση / Συνέχεια / Βήμα ** | `r` / `c` / `si` / `ni` |

---

## 🛡️ Αμυντικές εκτιμήσεις & δυαδικές προστασίες

1. **Πλήρης RELRO:** Η μεταγλώττιση με `-Wl,-z,relro,-z,now` καθιστά το `.got` εντελώς μόνο για ανάγνωση, νικώντας τις επιθέσεις αντικατάστασης GOT.
2. **Stack Canaries:** Η μεταγλώττιση με το `-fstack-protector-all` τοποθετεί τα cookies πριν από τις διευθύνσεις επιστροφής, προκαλώντας υπερχείλιση buffer του `__stack_chk_fail`.
3. **PIE & ASLR:** Τυχαιοποιεί τις διευθύνσεις κώδικα, σωρού και βάσης βιβλιοθήκης σε κάθε εκτέλεση.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το pwndbg και το GEF είναι εργαλεία δυαδικής ανάλυσης και εκμετάλλευσης μηχανικής που προορίζονται αυστηρά για εξουσιοδοτημένη έρευνα ευπάθειας, αμυντικό έλεγχο λογισμικού και ακαδημαϊκούς διαγωνισμούς CTF. Η εκμετάλλευση λογισμικού χωρίς άδεια είναι παράνομη.