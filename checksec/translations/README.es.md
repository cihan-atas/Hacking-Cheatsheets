<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec y Binutils - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```
<p align="centro">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p align="centro">
  <b>🛡️ Herramientas de línea de comandos esenciales para auditar las mitigaciones de seguridad de ejecutables de Linux (RELRO, Stack Canary, NX, PIE, Fortify) y analizar encabezados binarios, secciones y tablas de símbolos ELF</b>
</p>

---

## 📋 Tabla de contenidos

- [Matriz de mitigaciones binarias y descripción general](#-overview--matriz-de-mitigaciones-binarias)
- [Instalación](#-instalación)
- [checksec: Auditoría de mitigación rápida](#-checksec-auditoría-de-mitigación-rápida)
  - [Auditoría de binarios únicos y por lotes] (#auditoría-única--binarios por lotes)
  - [Auditoría de procesos en ejecución](#auditoría-de-procesos-en-ejecución)
  - [Salida JSON y CSV](#json--csv-output)
- [readelf: Encabezado ELF y disección de sección](#-readelf-elf-header--section-dissection)
  - [Encabezado de archivo ELF (`-h`)](#elf-file-header--h)
  - [Encabezados y segmentos de programa (`-l`)](#program-headers--segments--l)
  - [Encabezados de sección (`-S`)](#section-headers--s)
  - [Tablas de símbolos (`-s` / `--syms`)](#symbol-tables--s----syms)
  - [Dependencias y bibliotecas dinámicas (`-d`)](#dependencias-dinámicas--bibliotecas--d)
- [objdump: Desmontaje y reubicaciones](#-objdump-disassembly--reubicaciones)
  - [Desmontaje de Intel (`-d -M intel`)](#intel-desmontaje--d--m-intel)
  - [Entradas de reubicación y GOT (`-R`)](#relocation-entries--got--r)
- [nm y cadenas: Resolución de símbolos y cadenas](#-nm--cadenas-resolución-de-símbolos--cadenas)
  - [Listado de símbolos demandados con `nm`](#listing-demangled-symbols-with-nm)
  - [Extracción de cadenas ofuscadas con `strings`](#extracción-cadenas-ofuscadas-con-cadenas)
- [Referencia de compilación reforzada](#-referencia-de-compilación reforzada)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y matriz de mitigaciones binarias

Antes de comenzar cualquier flujo de trabajo de explotación o ingeniería inversa, es obligatorio comprender qué mitigaciones de seguridad están habilitadas:

| Mitigación | Nombre completo | Mecanismo de protección | Estrategia de derivación |
|------------|-----------|----------------------|-----------------|
| **RELO** | Reubicación Sólo lectura | Hace que `.got` sea de solo lectura (`Full RELRO`) para evitar la sobrescritura del puntero | Estructuras de montón de destino, direcciones de retorno o enlaces de funciones |
| **Canarias** | Protector de pila | Coloca una cookie aleatoria antes del puntero de retorno; comprueba la integridad al regresar | Fuga de valor canario mediante pérdida de memoria o fuerza bruta (bifurcación de demonios) |
| **NX** | No ejecutar/DEP | Marca los segmentos de pila, montón y datos como no ejecutables | Programación Orientada al Retorno (ROP) o Programación Orientada a Saltos (JOP) |
| **TARTA** | Ejecutable independiente de posición | Carga la dirección base binaria en una ubicación aleatoria en la memoria virtual (ASLR) | Puntero de código de fuga para calcular el desplazamiento de la dirección base |
| **Fortificar** | `_FORTIFY_SOURCE` | Reemplaza funciones de cadena inseguras (`strcpy` -> `__strcpy_chk`) con verificación de límites | Errores de lógica sin desbordamiento, cadena de formato sin desbordamiento del búfer |

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec: Auditoría Rápida de Mitigación

### Auditoría de binarios individuales y por lotes
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Ejemplo de salida estándar:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### Auditoría de procesos en ejecución
Audite las mitigaciones de memoria y el estado de ASLR de un demonio activo:
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### Salida JSON y CSV
Exportar resultados para análisis automatizado:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf: Disección de sección y encabezado ELF

La utilidad `readelf` muestra información de archivos ELF sin invocar ninguna ejecución:

### Encabezado de archivo ELF (`-h`)
Revela la arquitectura de la CPU, clase de 32 frente a 64 bits, dirección de punto de entrada y endianidad:
```bash
readelf -h ./target_binary
```
### Encabezados y segmentos del programa (`-l`)
Muestra segmentos cargados en la memoria en tiempo de ejecución y banderas (`R`, `W`, `E`). Crucial para identificar si la pila (`GNU_STACK`) tiene permisos de ejecución:
```bash
readelf -l ./target_binary
```
*(Si `GNU_STACK` tiene `RWE`, se permite la ejecución de la pila; NX está deshabilitado).*

### Encabezados de sección (`-S`)
Inspeccione los diseños de memoria, direcciones y compensaciones para `.text` (código), `.data` (datos inicializados), `.rodata` (constantes), `.bss` (datos no inicializados), `.plt` y `.got`:
```bash
readelf -S ./target_binary
```
### Tablas de símbolos (`-s` / `--syms`)
Enumere todas las funciones exportadas, variables globales y símbolos libc importados:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### Dependencias dinámicas y bibliotecas (`-d`)
Enumera las bibliotecas compartidas necesarias (etiquetas `NEEDED`, por ejemplo, `libc.so.6`, `libssl.so`):
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump: Desmontaje y reubicaciones

### Desmontaje de Intel (`-d -M intel`)
Desensamble las secciones ejecutables en un ensamblaje limpio de sintaxis Intel:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### Entradas de reubicación y GOT (`-R`)
Muestre las direcciones de reubicación de la Tabla de compensación global (GOT) para llamadas de biblioteca importadas:
```bash
objdump -R ./target_binary
```
*Muestra direcciones de destino para funciones como `puts`, `printf` y `system`.*

---

## 🔍 nm y cadenas: resolución de símbolos y cadenas

### Listado de símbolos demandados con `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### Extracción de cadenas ofuscadas con `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ Referencia de compilación endurecida

Para construir un binario ELF con máximas mitigaciones de seguridad:
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

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Mitigaciones de auditoría** | `checksec --file=<bin>` |
| **Auditoría de PID en ejecución** | `checksec --proc=<pid>` |
| **Ver encabezado ELF** | `readelf -h <bin>` |
| **Ver bandera de Stack NX** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **Lista de secciones (.got, .plt)**| `readelf -S <bin>` |
| **Ver símbolos dinámicos** | `readelf --dyn-syms <bin>` |
| **Desmontaje de Intel** | `objdump -d -M intel <bin>` |
| **Inspeccionar reubicaciones GOT** | `objdump -R <bin>` |
| **Símbolos de lista (demandados)** | `nm -C <bin>` |
| **Extraer cadenas UTF-16** | `strings -e l -a <bin>` |
| **Mostrar desplazamientos de cadena** | `strings -a -t x <bin>` |

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> checksec y GNU Binutils son herramientas de diagnóstico e ingeniería inversa destinadas a la auditoría de seguridad del software, la validación de defensa y la investigación binaria autorizada. La inspección de software no autorizado debe cumplir con las regulaciones locales de propiedad intelectual.