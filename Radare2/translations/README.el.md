<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
      UNIX-like Command-Line Reverse Engineering Framework
```
<p align="center">
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="Αντίστροφη μηχανική">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p align="center">
  <b>⚡ Ισχυρό πλαίσιο ανοιχτού κώδικα, βασισμένο σε τερματικό για δυαδική ανάλυση, αποσυναρμολόγηση, εντοπισμό σφαλμάτων, επεξεργασία δεκαεξαδικού και δυαδική ενημέρωση κώδικα</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική](#-επισκόπηση--αρχιτεκτονική)
- [Εγκατάσταση] (#-εγκατάσταση)
- [Opening Files & Session Modes](#-opening-files--session-modes)
- [Δυαδικές εντολές ανάλυσης (`a`)](#-binary-analysis-commands-a)
- [Πλοήγηση και αναζήτηση (`s`)](#-πλοήγηση--αναζήτηση-s)
- [Εκτύπωση και αποσυναρμολόγηση (`p`)](#-εκτύπωση--αποσυναρμολόγηση-p)
- [Διαδραστικές λειτουργίες οπτικού και γραφήματος (`V` & `VV`)](#-interactive-visual--graph-modes-v--vv)
- [Δυαδική επιδιόρθωση και τροποποίηση (`w`)](#-δυαδική επιδιόρθωση--τροποποίηση-w)
- [The Radare2 Companion Toolset](#-the-radare2-companion-toolset)
  - [rabin2: Binary Information Extraction](#rabin2-binary-information-extraction)
  - [rasm2: Assembler & Disassembler](#rasm2-assembler--disassembler)
  - [radiff2: Binary Difference Engine](#radiff2-binary-difference-engine)
  - [rafind2: Byte & String Search](#rafind2-byte--string-search)
  - [rax2: Base & Numeric Converter](#rax2-base--numeric-converter)
- [Βασικά στοιχεία εντοπισμού σφαλμάτων με r2 (`-d`)](#-debugging-basics-with-r2--d)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Anti-Reversing](#-defensive-considerations--anti-reversing)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική

Δημιουργήθηκε από τον **Sergi Álvarez (τηγανίτα)**, το **Radare2** (`r2`) είναι ένα φορητό, αρθρωτό πλαίσιο αντίστροφης μηχανικής που βασίζεται εξ ολοκλήρου στη φιλοσοφία του UNIX. Λειτουργεί εξ ολοκλήρου μέσα στο τερματικό σας χωρίς να απαιτεί μεγάλες εξαρτήσεις γραφικών:
- **Πλήρης υποστήριξη αρχιτεκτονικής:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik και EVM.
- **Composable CLI Utilities:** Συνοδεύεται από εξειδικευμένα εργαλεία γραμμής εντολών (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Διαδραστικές οπτικές λειτουργίες:** Επεξεργασίες επεξεργασίας διαίρεσης οθόνης, γραφήματα ροής ελέγχου ASCII, οθόνες εγγραφής και προβολές απομεταγλωττιστή.
- **Δυνατότητα σεναρίου και επεκτάσιμο:** Ενσωματώνεται με Python, r2pipe, Ghidra (`r2ghidra`) και Rizin.

---

## 📥 Εγκατάσταση
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y radare2

# Arch Linux
sudo pacman -S radare2

# Install latest Git master (Recommended for most features)
git clone https://github.com/radareorg/radare2.git
cd radare2 && sys/install.sh

# Install r2ghidra decompiler plugin
r2pm update
r2pm -i r2ghidra
```
---

## 🚀 Άνοιγμα αρχείων και τρόπων συνεδρίας
```bash
# Open binary in read-only mode
r2 /bin/ls

# Open binary in read-write mode (required for patching)
r2 -w target.bin

# Open binary in debugger mode
r2 -d target.bin

# Open binary without parsing headers (raw flat binary / shellcode)
r2 -b 32 -n /path/to/raw.bin

# Open in quiet mode (no greeting banner)
r2 -q target.bin
```
---

## 🔬 Εντολές δυαδικής ανάλυσης (`a`)

Μέσα στο κέλυφος `r2`, οι εντολές είναι μεμονωμένα γράμματα ή σύντομα προθέματα. Μπορείτε να προσθέσετε το `?` σε οποιαδήποτε εντολή για να δείτε ενσωματωμένη βοήθεια:
```text
[0x00000000]> a?        # Show analysis help
[0x00000000]> aa        # Basic analysis (symbols, basic blocks)
[0x00000000]> aaa       # Deep analysis (functions, cross-references, calls)
[0x00000000]> aaaa      # Experimental auto-analysis (takes longer)

[0x00000000]> afl       # List all identified functions
[0x00000000]> afi       # Display function info at current address
[0x00000000]> afvn      # Rename local variable: afvn <new_name> <old_name>
[0x00000000]> axt <addr># Find cross-references (xrefs) TO address
[0x00000000]> axf <addr># Find cross-references (xrefs) FROM address
```
---

## 🧭 Πλοήγηση και αναζήτηση (`s`)

Η εντολή αναζήτησης αλλάζει την τρέχουσα διεύθυνση δρομέα:
```text
[0x00000000]> s main        # Seek to main function
[0x00401122]> s 0x401200    # Seek to absolute address
[0x00401200]> s+ 32         # Seek 32 bytes forward
[0x00401220]> s- 16         # Seek 16 bytes backward
[0x00401210]> s/ hit0_0     # Seek to search hit
[0x00401210]> u             # Undo seek (go back to previous address)
[0x00401200]> U             # Redo seek
```
---

## 📜 Εκτύπωση και αποσυναρμολόγηση (`p`)
```text
[0x00401122]> pd 10     # Disassemble 10 instructions
[0x00401122]> pdf       # Disassemble entire current function
[0x00401122]> pdc       # Decompile function using r2ghidra plugin
[0x00401122]> px 64     # Print 64 bytes in hex dump format
[0x00401122]> ps        # Print string at current address
[0x00401122]> pz        # Print zero-terminated string
[0x00401122]> pi 5      # Print 5 instructions (concise format)
```
---

## 🖥️ Διαδραστικές οπτικές και γραφικές λειτουργίες (`V` & `VV`)

Πατώντας `V` ή `VV` μετατρέπεται το Radare2 σε μια πλούσια διαδραστική εφαρμογή τερματικού:

- **Εισαγωγή οπτικής λειτουργίας:** Πληκτρολογήστε `V`
  - Πατήστε `p` / `P` για εναλλαγή των προβολών (Hex dump -> Disassembly -> Debugger -> Decompiler).
  - Πατήστε `c` για εναλλαγή της λειτουργίας δρομέα (χρησιμοποιήστε τα πλήκτρα βέλους ή `hjkl` για να επιλέξετε byte).
  - Πατήστε `d` για να επαναπροσδιορίσετε τα byte ως κώδικα, δεδομένα ή συμβολοσειρές.
  - Πατήστε `q` για να επιστρέψετε στην τυπική προτροπή.
- **Εισαγάγετε την προβολή γραφήματος:** Πληκτρολογήστε `VV`
  - Αποδίδει γράφημα ροής ελέγχου ASCII (CFG).
  - Χρησιμοποιήστε `Tab` / `Shift+Tab` για να μεταπηδήσετε μεταξύ κόμβων διακλάδωσης υπό όρους.
  - Πατήστε `t` για να ακολουθήσετε τον αληθινό κλάδο. `f` για να ακολουθήσετε τον ψευδή κλάδο.
  - Πιέστε το `u` για να αποχωρήσετε από έναν κλάδο.

---

## ✍️ Δυαδική επιδιόρθωση και τροποποίηση (`w`)

Για να τροποποιήσετε ένα δυαδικό αρχείο στο δίσκο, βεβαιωθείτε ότι το r2 είναι ανοιχτό σε λειτουργία εγγραφής (`r2 -w target` ή πληκτρολογήστε `oo+` μέσα σε μια ενεργή περίοδο λειτουργίας):
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ Το σύνολο εργαλείων Radare2 Companion

Το Radare2 περιλαμβάνει αυτόνομα βοηθητικά προγράμματα CLI:

### rabin2: Εξαγωγή δυαδικών πληροφοριών
```bash
# Print general binary architecture, bits, and security mitigations (NX, PIE, Canary)
rabin2 -I target.bin

# List imported symbols and functions (libc calls)
rabin2 -i target.bin

# List exported symbols
rabin2 -E target.bin

# Extract ASCII & UTF-16 strings
rabin2 -z target.bin

# List sections (.text, .data, .rodata)
rabin2 -S target.bin
```
### rasm2: Συναρμολογητής & Αποσυναρμολογητής
```bash
# Assemble x64 instructions into hexadecimal opcode
rasm2 -a x86 -b 64 "xor eax, eax; ret"
# Output: 31c0c3

# Disassemble hex opcodes into human-readable assembly
rasm2 -a x86 -b 64 -d "31c0c3"
# Output:
# xor eax, eax
# ret
```
### radiff2: Binary Difference Engine
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2: Byte & String Search
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2: Μετατροπέας βάσης και αριθμών
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 Βασικά στοιχεία εντοπισμού σφαλμάτων με r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
Μέσα στο κέλυφος του προγράμματος εντοπισμού σφαλμάτων:
```text
[0x7f112233]> db main       # Set software breakpoint at main
[0x7f112233]> db 0x401150   # Set breakpoint at specific address
[0x7f112233]> dbl           # List all active breakpoints
[0x7f112233]> dc            # Continue execution until breakpoint
[0x7f112233]> ds            # Step into (single instruction)
[0x7f112233]> dso           # Step over function calls
[0x7f112233]> dr            # Print all CPU registers
[0x7f112233]> dr rax        # Print specific register value
[0x7f112233]> dr rax = 0x1  # Modify register value
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Στόχος | Εντολή |
|-----------|---------|
| **Βαθιά Ανάλυση** | `aaa` |
| **Συναρτήσεις λίστας** | `afl` |
| **Αναζήτηση λειτουργίας / Προσθήκη ** | `s <addr>` / `s main` |
| **Λειτουργία αποσυναρμολόγησης** | `pdf` |
| **Συνάρτηση Decompile (r2ghidra)** | `pdc` |
| **Διαδραστική οπτική αποσυναρμολόγηση** | `V` |
| **Γράφημα ροής ASCII** | `VV` |
| **Οδηγίες συναρμολόγησης** | `wa <instruction>` |
| **Γράψτε Hex Bytes** | `wx <bytes>` |
| **Ξαναάνοιγμα σε Read-Write** | `oo+` |
| **Εξαγωγή δυαδικών πληροφοριών** | `rabin2 -I <binary>` |
| **Συναρμολόγηση CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Αμυντικά βλέμματα & Αντιστροφή

1. **Ανίχνευση κατά του εντοπισμού σφαλμάτων:** Ο εντοπισμός σφαλμάτων του Radare2 βασίζεται στο τυπικό Linux `ptrace`. Τα δυαδικά αρχεία με αυτοελέγχους `ptrace(PTRACE_TRACEME)` μπορούν να μπλοκάρουν την προσάρτηση εκτός εάν επιδιορθωθούν.
2. **Συμπύκνωση & Ισοπέδωση Ροής Ελέγχου:** Τα δυαδικά αρχεία με έντονη ασάφεια (OLLVM) συγχέουν την ανάκτηση βασικών μπλοκ. Χρησιμοποιήστε `aaaa` ή μηχανισμούς συμβολικής εκτέλεσης (όπως το angr) για να απλοποιήσετε τις ισοπεδωμένες λειτουργίες.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Radare2 είναι ένα κιτ εργαλείων αντίστροφης μηχανικής και ανάλυσης που προορίζεται αυστηρά για εξουσιοδοτημένες αξιολογήσεις ασφαλείας, ανάλυση κακόβουλου λογισμικού, έρευνα ευπάθειας και προσωπική εκπαιδευτική χρήση. Το ιδιόκτητο λογισμικό αντίστροφης μηχανικής χωρίς άδεια ενδέχεται να παραβιάζει τις συμφωνίες άδειας χρήσης του τελικού χρήστη και τους νόμους περί πνευματικών δικαιωμάτων.