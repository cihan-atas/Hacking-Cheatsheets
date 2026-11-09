<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```
<p align="centro">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="NX Bypass">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="Exploit Dev">
</p>

<p align="centro">
  <b>🧩 Utilidades de línea de comandos esenciales para descubrir dispositivos de programación orientada al retorno (ROP) y programación orientada a saltos (JOP) para evitar las protecciones de memoria no ejecutables (NX/DEP)</b>
</p>

---

## 📋 Tabla de contenidos

- [Resumen y teoría ROP](#-overview--rop-theory)
- [Instalación](#-instalación)
- [Descubrimiento de gadgets básicos](#-descubrimiento-de-gadgets-basicos)
  - [Extrayendo todos los gadgets](#extracting-all-gadgets)
  - [Limitar la profundidad de la instrucción](#limiting-instrucción-profundidad)
- [Búsqueda avanzada y filtrado de patrones] (#-búsqueda-avanzada--filtrado-de-patrones)
  - [Apuntando a registros específicos (`rdi`, `rsi`, `rdx`, `rax`)](#targeting-specific-registers-rdi-rsi-rdx-rax)
  - [Dispositivos de escritura de memoria (escribir-qué-dónde)] (#dispositivos-de-escritura de memoria-escribir-qué-dónde)
  - [Gadgets de interrupción y llamada al sistema] (#syscall--interrupt-gadgets)
- [Excluyendo caracteres malos (`--badbytes`)](#-excluyendo-caracteres-malos---badbytes)
- [Generación automatizada de cadena ROP] (#-generación-automatizada-de-cadena-rop)
  - [Cadenas automatizadas Ropper](#ropper-cadenas-automatizadas)
  - [Cadenas automatizadas ROPgadget](#ropgadget-cadenas-automatizadas)
- [Análisis de bibliotecas cargadas (cadenas ROP de libc)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y mitigaciones](#-consideraciones-defensivas--mitigaciones)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general y teoría ROP

Los sistemas operativos modernos aplican **NX (No-Execute)/DEP (Prevención de ejecución de datos)**, lo que impide la ejecución directa del código shell colocado en la pila o el montón.

**Programación orientada al retorno (ROP)** evita esta restricción al reutilizar pequeñas instrucciones de máquina existentes que ya están presentes en la memoria ejecutable (sección `.text` de las bibliotecas binarias o compartidas como `libc.so`). Cada secuencia de instrucciones que termina en una instrucción `ret` se denomina **gadget**. Al organizar las direcciones en la pila, los atacantes encadenan dispositivos para:
1. Cargue argumentos en los registros de la CPU (por ejemplo, `pop rdi; ret` para convenciones de llamadas x64).
2. Escriba cadenas (como `/bin/sh`) en secciones `.bss` o `.data` grabables.
3. Active `execve("/bin/sh", NULL, NULL)` o invoque `system()`.

---

## 📥 Instalación
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

## 🚀 Descubrimiento básico de gadgets

### Extrayendo todos los gadgets
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### Limitar la profundidad de la instrucción
Reduzca las cadenas largas ruidosas restringiendo la longitud del dispositivo (número de instrucciones antes de `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 Búsqueda avanzada y filtrado de patrones

### Orientación a registros específicos (`rdi`, `rsi`, `rdx`, `rax`)
En la convención de llamadas de Linux de 64 bits, los primeros tres argumentos de una función o llamada al sistema deben residir en `rdi`, `rsi` y `rdx`:
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
Usando la coincidencia de expresiones regulares de ROPgadget:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### Gadgets de escritura en memoria (Escribir-qué-dónde)
Cuando necesite almacenar `/bin/sh` o argumentos arbitrarios en la memoria:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Ejemplo de coincidencia:* `mov qword ptr [rdi], rsi; ret` permite escribir lo que esté en `rsi` en la dirección señalada por `rdi`.

### Gadgets de llamada al sistema e interrupción
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 Excluyendo personajes malos (`--badbytes`)

Las vulnerabilidades de formato de cadenas o funciones como `strcpy()` terminan en bytes nulos (`\x00`), nuevas líneas (`\x0a`) o retornos de carro (`\x0d`). Excluya las direcciones que contengan estos bytes:
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ Generación automatizada de cadena ROP

Ambas herramientas pueden crear automáticamente plantillas funcionales de explotación de Python que invocan `execve("/bin/sh")`:

### Cadenas automatizadas Ropper
```bash
ropper --file ./target_binary --chain "execve"
```
### Cadenas automatizadas ROPgadget
```bash
ROPgadget --binary ./target_binary --ropchain
```
*Esto produce un esqueleto de carga útil de Python listo para usar que encadena `mprotect()` o `execve()`.*

---

## 📚 Análisis de bibliotecas cargadas (cadenas ROP libc)

Cuando el binario en sí está compilado como **PIE** o contiene pocos elementos útiles, extraiga los elementos directamente de la biblioteca C compartida del sistema de destino (`libc.so.6`):
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Agregue compensaciones descubiertas a la dirección base filtrada de libc en tiempo de ejecución).*

---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando Ropper | Comando ROPgadget |
|-----------|----------------|-------------|
| **Volcar todos los gadgets** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Buscar gadget específico** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Filtrar bytes incorrectos** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Límite de profundidad/longitud** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **Buscar gadgets JOP** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **Cadena ROP de construcción automática** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Buscar escrituras en memoria** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Consideraciones defensivas y mitigaciones

1. **Tecnología de aplicación de flujo de control (Intel CET/Shadow Stack):** Los procesadores modernos mantienen una pila de sombra de hardware aislada que verifica las direcciones de retorno en `ret`, terminando las cadenas ROP de inmediato.
2. **Integridad del flujo de control (CFI) de Clang/GCC:** Valida los objetivos del puntero de función frente a un gráfico de vanguardia antes de las llamadas indirectas.

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> Ropper y ROPgadget son utilidades de evaluación de seguridad de software y desarrollo de exploits. Deben usarse estrictamente en pruebas de penetración autorizadas, desafíos CTF o investigaciones de seguridad defensiva. Explotar software sin autorización explícita es ilegal.