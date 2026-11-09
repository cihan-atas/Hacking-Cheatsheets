<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
      UNIX-like Command-Line Reverse Engineering Framework
```
<p align="centro">
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="Ingeniería inversa">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p align="centro">
  <b>⚡ Potente marco de código abierto basado en terminal para análisis binario, desmontaje, depuración, edición hexadecimal y parcheo binario</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y arquitectura] (#-descripción general--arquitectura)
- [Instalación](#-instalación)
- [Abrir archivos y modos de sesión] (#-abrir-archivos--modos-de-sesión)
- [Comandos de análisis binario (`a`)](#-comandos-de-análisis-binario-a)
- [Navegación y búsqueda (`s`)](#-navegación--búsqueda-s)
- [Impresión y desmontaje (`p`)](#-printing--disassembly-p)
- [Modos interactivos visuales y gráficos (`V` y `VV`)](#-interactive-visual--graph-modes-v--vv)
- [Parches y modificaciones binarias (`w`)](#-binary-patching--modification-w)
- [El conjunto de herramientas complementario de Radare2](#-el-conjunto-de-herramientas-complementario-de-radare2)
  - [rabin2: Extracción de información binaria](#rabin2-extracción-de-información-binaria)
  - [rasm2: ensamblador y desensamblador](#rasm2-ensamblador--desensamblador)
  - [radiff2: motor de diferencia binaria](#radiff2-motor-de-diferencia-binaria)
  - [rafind2: búsqueda de bytes y cadenas](#rafind2-byte--búsqueda de cadenas)
  - [rax2: Convertidor numérico y base](#rax2-base--convertidor-numérico)
- [Conceptos básicos de depuración con r2 (`-d`)](#-conceptos básicos de depuración-con-r2--d)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y anti-reversa](#-consideraciones-defensivas--anti-reversa)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y arquitectura

Creado por **Sergi Álvarez (pancake)**, **Radare2** (`r2`) es un marco de ingeniería inversa modular y portátil construido enteramente en torno a la filosofía UNIX. Se ejecuta completamente dentro de tu terminal sin requerir grandes dependencias gráficas:
- **Soporte completo de arquitectura:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik y EVM.
- **Utilidades CLI componibles:** Viene con herramientas de línea de comandos especializadas (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Modos visuales interactivos:** Editores hexadecimales de pantalla dividida, gráficos de flujo de control ASCII, monitores de registro y vistas de descompilador.
- **Programable y extensible:** Se integra con Python, r2pipe, Ghidra (`r2ghidra`) y Rizin.

---

## 📥 Instalación
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

## 🚀 Abrir archivos y modos de sesión
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

## 🔬 Comandos de análisis binario (`a`)

Dentro del shell `r2`, los comandos son letras individuales o prefijos cortos. Puede agregar `?` a cualquier comando para ver la ayuda en línea:
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

## 🧭 Navegación y búsqueda (`s`)

El comando de búsqueda cambia la dirección actual del cursor:
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

## 📜 Impresión y desmontaje (`p`)
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

## 🖥️ Modos visuales y gráficos interactivos (`V` y `VV`)

Al presionar `V` o `VV`, Radare2 se convierte en una rica aplicación de terminal interactiva:

- **Ingrese al modo visual:** Escriba `V`
  - Presione `p` / `P` para recorrer las vistas (volcado hexadecimal -> Desmontaje -> Depurador -> Descompilador).
  - Presione `c` para alternar el modo de cursor (use las teclas de flecha o `hjkl` para seleccionar bytes).
  - Presione `d` para redefinir bytes como código, datos o cadenas.
  - Presione `q` para regresar al mensaje estándar.
- **Ingrese a la vista de gráfico:** Escriba `VV`
  - Representa un gráfico de flujo de control ASCII (CFG).
  - Utilice `Tab` / `Shift+Tab` para saltar entre nodos de rama condicionales.
  - Presione `t` para seguir la rama verdadera; `f` para seguir la rama falsa.
  - Presione `u` para salir de una rama.

---

## ✍️ Parches y modificaciones binarias (`w`)

Para modificar un binario en el disco, asegúrese de que r2 esté abierto en modo de escritura (`r2 -w target` o escriba `oo+` dentro de una sesión activa):
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ El conjunto de herramientas complementario de Radare2

Radare2 incluye utilidades CLI independientes:

### rabin2: Extracción de información binaria
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
### rasm2: ensamblador y desensamblador
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
### radiff2: motor de diferencia binaria
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2: Búsqueda de bytes y cadenas
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2: Convertidor de bases y números
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 Conceptos básicos de depuración con r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
Dentro del shell del depurador:
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

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Análisis profundo** | `aaa` |
| **Lista de funciones** | `afl` |
| **Buscar función/dirección** | `s <addr>` / `s main` |
| **Función de desmontaje** | `pdf` |
| **Función de descompilación (r2ghidra)** | `pdc` |
| **Desmontaje visual interactivo** | `V` |
| **Gráfico de flujo ASCII** | `VV` |
| **Instrucciones de montaje** | `wa <instruction>` |
| **Escribir bytes hexadecimales** | `wx <bytes>` |
| **Reabrir en Lectura-Escritura** | `oo+` |
| **Extraer información binaria** | `rabin2 -I <binary>` |
| **Montar CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Consideraciones defensivas y anti-reversión

1. **Detección antidepuración:** El depurador de Radare2 se basa en el estándar Linux `ptrace`. Los archivos binarios con autoverificaciones `ptrace(PTRACE_TRACEME)` pueden bloquear los archivos adjuntos a menos que se parcheen.
2. **Ofuscación y aplanamiento del flujo de control:** Los binarios muy ofuscados (OLLVM) confunden la recuperación básica de bloques. Utilice `aaaa` o motores de ejecución simbólica (como angr) para simplificar funciones aplanadas.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Radare2 es un conjunto de herramientas de análisis e ingeniería inversa destinado estrictamente a evaluaciones de seguridad autorizadas, análisis de malware, investigación de vulnerabilidades y uso educativo personal. El software propietario de ingeniería inversa sin permiso puede violar los acuerdos de licencia de usuario final y las leyes de derechos de autor.