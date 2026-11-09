<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
         Next-Gen GDB Enhancements for Reversing & Binary Exploitation
```
<p align="centro">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="Inversión y Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="Licencia FOSS">
</p>

<p align="centro">
  <b>🎯 Las principales mejoras del terminal GDB de código abierto diseñadas para ingeniería inversa binaria, análisis de corrupción de memoria, inspección de diseño de montón y desarrollo de exploits CTF</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general y por qué Vanilla GDB necesita mejoras](#-overview--why-vanilla-gdb-needs-enhancements)
- [Instalación y cambio entre marcos] (#-instalación--cambio-entre-marcos)
  - [Instalando pwndbg](#instalando-pwndbg)
  - [Instalando GEF](#installing-gef)
  - [Administrando `.gdbinit`](#administrando-gdbinit)
- [El motor de visualización de contexto](#-the-context-display-engine)
  - [Sección de registros](#registers-section)
  - [Predicciones de desmontaje y salto] (#desmontaje--predicciones de salto)
  - [Pila y telescopio] (#stack--telescope)
  - [Rastreo e hilos](#backtrace--hilos)
- [Inspección de memoria y navegación](#-inspección-memoria--navegación)
  - [punteros telescópicos (`telescope`)](#punteros-telescópicos-telescopio)
  - [Asignaciones de memoria de proceso (`vmmap`)](#process-memory-mappings-vmmap)
  - [Buscando Memoria (`search`)](#buscando-memoria-busqueda)
- [Explotación e inspección del montón](#-explotación-del montón--inspección)
  - [Visualización de fragmentos y arenas] (#viewing-chunks--arenas)
  - [Inspeccionar contenedores libres (Tcache, Fastbins, Smallbins)](#inspeccionar-bins-libres-tcache-fastbins-smallbins)
  - [Visualización del diseño del montón (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [Ayudantes de desarrollo de exploits](#-exploit-development-helpers)
  - [Checksec y auditoría de mitigación](#checksec--mitigation-audit)
  - [Localizador canario de pila (`canary`)] (#stack-canary-locator-canary)
  - [Patrones cíclicos y cálculo de compensación] (#patrones-cíclicos--cálculo-de compensación)
  - [Búsqueda de gadgets ROP en memoria](#caza-de-gadgets-rop-en-memoria)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y protecciones binarias](#-consideraciones-defensivas--protecciones-binarias)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y por qué Vanilla GDB necesita mejoras

El depurador GNU estándar (`gdb`) es una herramienta industrial, pero su experiencia de usuario lista para usar es visualmente austera. No actualiza automáticamente los estados de los registros, no elimina la referencia a punteros de varios niveles ni muestra vistas de pila al seguir las instrucciones.

**pwndbg** y **GEF** convierten a GDB en una suite de élite de ingeniería inversa y explotación binaria:
- **pwndbg:** Diseñado específicamente para la explotación de software de bajo nivel, depuración de montón (glibc 2.27 - 2.39+ componentes internos del asignador) y desreferenciación automatizada de punteros.
- **GEF (Características mejoradas de GDB):** Un único script de Python independiente sin dependencias externas, que ofrece características independientes de la arquitectura (ARM, MIPS, PowerPC, x86) y análisis dinámico de estructuras.

---

## 📥 Instalación y cambio entre marcos

### Instalando pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### Instalación del FMAM
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### Gestión de `.gdbinit`
Para cambiar entre pwndbg y GEF sin conflictos, configure su `~/.gdbinit`:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ El motor de visualización de contexto

Cada vez que la ejecución se detiene en un punto de interrupción o paso, tanto pwndbg como GEF imprimen una vista de terminal automatizada de múltiples paneles:
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

## 🔍 Inspección y navegación de la memoria

### Punteros telescópicos (`telescope`)
Elimina las referencias a los punteros de memoria de forma recursiva, mostrando texto ASCII, símbolos de función o punteros de destino:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### Asignaciones de memoria de proceso (`vmmap`)
Muestra segmentos completos de memoria virtual con permisos de lectura/escritura/ejecución:
```text
pwndbg> vmmap
```
*Detecta rápidamente segmentos con permisos `rwx` (vital para la inyección de shellcode).*

### Buscando memoria (`search`)
Ubique cadenas, números enteros o punteros en cualquier lugar de las páginas de memoria cargadas:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 Explotación e inspección del montón

Analizar los asignadores de montón `glibc` modernos es sencillo con comandos de montón dedicados:

### Visualización de fragmentos y arenas
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### Inspeccionar contenedores libres (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### Visualizando el diseño del montón (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ Ayudantes de desarrollo de exploits

### Verificación de seguridad y auditoría de mitigación
Verifique las mitigaciones de seguridad del compilador directamente desde el indicador de GDB:
```text
pwndbg> checksec
```
*Inspecciona:*
- **RELRO:** RELRO parcial vs completo (protege la tabla de compensación global `.got.plt`)
- **Pila:** Canario encontrado vs No canario
- **NX:** NX habilitado (pila/montón no ejecutable)
- **PIE:** PIE habilitado (ejecutable independiente de posición/ASLR)

### Localizador canario de pila (`canary`)
Localice el valor canary del subproceso en el Almacenamiento local de subprocesos (TLS) y verifique dónde reside en el marco de pila actual:
```text
pwndbg> canary
```
### Patrones cíclicos y cálculo de compensación
Determine el desplazamiento exacto para sobrescribir el puntero de instrucción (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### Búsqueda de dispositivos ROP en memoria
Busque segmentos ejecutables cargados para gadgets específicos:
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Comience con pwndbg/GEF**| `gdb ./target_binary` |
| **Inspeccionar registros/pila** | `context` |
| **Puntero recursivo Deref**| `telescope $rsp 10` |
| **Mapa de memoria y verificación RWX**| `vmmap` |
| **Buscar cadena en la memoria**| `search -s "/bin/sh"` |
| **Mitigaciones de auditoría** | `checksec` |
| **Buscar pila canario** | `canary` |
| **Descripción general de fragmentos de montón** | `heap` |
| **Inspeccionar contenedores gratuitos** | `bins` |
| **Calcular compensación de búfer**| `cyclic 100` -> `cyclic -l <val>` |
| **Establecer punto de interrupción en el símbolo**| `b *main` |
| **Ejecutar/Continuar/Paso** | `r` / `c` / `si` / `ni` |

---

## 🛡️ Consideraciones defensivas y protecciones binarias

1. **RELRO completo:** La compilación con `-Wl,-z,relro,-z,now` hace que `.got` sea completamente de solo lectura, lo que derrota los ataques de sobrescritura GOT.
2. **Stack Canaries:** La compilación con `-fstack-protector-all` coloca cookies antes de las direcciones de retorno, lo que provoca que `__stack_chk_fail` se desborde el búfer.
3. **PIE y ASLR:** Aleatoriza las direcciones base de código, montón y biblioteca en cada ejecución.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> pwndbg y GEF son herramientas de ingeniería de explotación y análisis binario destinadas estrictamente a la investigación de vulnerabilidades autorizada, la auditoría de software defensivo y las competencias académicas de CTF. Explotar software sin permiso es ilegal.