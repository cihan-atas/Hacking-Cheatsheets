<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace & ltrace - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
            Linux Dynamic Binary Analysis: System & Library Call Tracers
```
<p align="centro">
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="Análisis dinámico">
</p>

<p align="centro">
  <b>🔬 Utilidades de línea de comandos esenciales para el análisis binario dinámico: interceptación de llamadas al sistema del kernel de Linux (strace) y llamadas a bibliotecas dinámicas en el espacio de usuario (ltrace)</b>
</p>

---

## 📋 Tabla de contenidos

- [Descripción general: llamadas al sistema frente a llamadas a la biblioteca](#-overview-system-calls-vs-library-calls)
- [Instalación](#-instalación)
- [strace: Seguimiento de llamadas del sistema](#-strace-system-call-tracing)
  - [Ejecución de seguimiento básico](#ejecución-de-seguimiento-basico)
  - [Filtrar llamadas al sistema por clase o nombre (`-e`)](#filtrado-syscalls-por-clase-o-nombre--e)
  - [Seguimiento de procesos secundarios y multiproceso (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Adjuntar a procesos en ejecución (`-p`)](#adjuntar-a-procesos-en-ejecución--p)
  - [Cadenas expandibles y salidas de búfer (`-s`)](#cadenas-expandidas--salidas-búfer--s)
  - [Perfiles estadísticos y análisis de cuellos de botella (`-c`)](# perfil-estadístico--análisis-de-cuellos--c)
  - [Inyección de fallos para fuzzing y pruebas (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace: Seguimiento dinámico de llamadas de biblioteca](#-ltrace-dynamic-library-call-tracing)
  - [Interceptación básica de biblioteca](#intercepción-biblioteca-básica)
  - [Descifrar comprobaciones de contraseñas codificadas (`strcmp`)](#cracking-hardcoded-password-checks-strcmp)
  - [Demangling de símbolos C++ (`-C`)](#demangling-c-symbols--c)
  - [Filtrado de funciones de biblioteca específicas](#filtrado-de-funciones-de-biblioteca-específicas)
- [Combinación de flujos de trabajo de strace y ltrace](#-combining-strace-and-ltrace-workflows)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consideraciones defensivas y anti-rastreo](#-consideraciones-defensivas--anti-rastreo)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Descripción general: llamadas al sistema frente a llamadas a la biblioteca

Al analizar archivos binarios ELF de Linux compilados, el análisis estático puede verse impedido por la ofuscación, la eliminación o el empaquetado. El seguimiento dinámico proporciona visibilidad inmediata de las acciones en tiempo de ejecución:

1. **Llamadas al sistema (`strace`):**
   - La interfaz entre las aplicaciones del espacio de usuario y el kernel de Linux.
   - Rastrea E/S de disco (`openat`, `read`, `write`), redes (`socket`, `connect`), asignación de memoria (`mmap`, `brk`) y gestión de procesos (`fork`, `execve`, `ptrace`).
2. **Llamadas a la biblioteca (`ltrace`):**
   - La interfaz entre las bibliotecas compartidas binarias y dinámicas (como `libc.so`, `libssl.so`).
   - Intercepta llamadas como `strcmp()`, `strlen()`, `malloc()`, `system()` y rutinas criptográficas, exponiendo frecuentemente credenciales de texto sin formato en la memoria antes del cifrado.

---

## 📥 Instalación
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ strace: Seguimiento de llamadas del sistema

### Ejecución de seguimiento básico
Ejecute un binario de destino y transmita todas las llamadas al sistema al error estándar:
```bash
strace ./target_binary
```
Redirigir la salida a un archivo para que no obstruya la ejecución del programa:
```bash
strace -o trace.log ./target_binary
```
### Filtrado de llamadas al sistema por clase o nombre (`-e`)
Evite el ruido filtrando solo las clases de llamadas al sistema relevantes:
```bash
# Trace ONLY file-related operations (open, read, write, stat, access)
strace -e trace=file ./target_binary

# Trace ONLY network socket connections and data transfers
strace -e trace=network ./target_binary

# Trace process execution and fork/clone operations
strace -e trace=process ./target_binary

# Filter specific syscalls by exact name
strace -e trace=open,openat,read,write ./target_binary
```
### Seguimiento de procesos secundarios y multiproceso (`-f`)
Los archivos binarios y el malware modernos generan procesos o subprocesos de trabajadores infantiles. Sin `-f`, strace los extrañará:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### Adjuntar a procesos en ejecución (`-p`)
Supervise un servicio o proceso de malware activo sin reiniciarlo:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### Expandiendo cadenas y salidas de búfer (`-s`)
De forma predeterminada, strace trunca las cadenas impresas a 32 caracteres. Aumente la longitud de la cadena para inspeccionar rutas completas de archivos, URL o cargas útiles:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### Perfiles estadísticos y análisis de cuellos de botella (`-c`)
Cuente el tiempo invertido, la cantidad de llamadas y los errores por llamada al sistema:
```bash
strace -c ./target_binary
```
*Ejemplo de resumen de salida:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### Inyección de fallos para fuzzing y pruebas (`-e fault`)
Inyecte errores arbitrarios en llamadas al sistema para evaluar la resiliencia:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace: Seguimiento de llamadas de biblioteca dinámica

### Intercepción básica de biblioteca
Interceptar llamadas realizadas a bibliotecas dinámicas `.so`:
```bash
ltrace ./target_binary
```
### Descifrar comprobaciones de contraseñas codificadas (`strcmp`)
En CTF de ingeniería inversa y binarios de desafío, las funciones de comparación de contraseñas (`strcmp`, `strncmp`, `memcmp`) revelan la contraseña de destino en argumentos de texto sin formato:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Ejemplo de salida:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### Exigiendo símbolos C++ (`-C`)
Demangle nombres de métodos C++ destrozados por el compilador (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```
### Filtrado de funciones de biblioteca específicas
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 Combinando flujos de trabajo strace y ltrace

Al realizar la clasificación inicial en un binario desconocido:

1. **Paso 1:** Ejecute `strace -e trace=file,network ./binary` para ver con qué archivos y sockets interactúa.
2. **Paso 2:** Ejecute `ltrace -s 256 ./binary` para descubrir comparaciones de cadenas de bibliotecas internas, llamadas criptográficas y lógica de memoria.
3. **Paso 3:** Combine ambas herramientas:
   ```bash
   ltrace -S ./binary
   ```
*(El indicador `-S` le indica a ltrace que muestre las llamadas al sistema junto con las llamadas a la biblioteca en una única vista unificada).*

---

## ⚡ Tabla de referencia rápida

| Objetivo | Comando |
|-----------|------------------|
| **Rastrear todas las llamadas al sistema** | `strace ./binary` |
| **Iniciar sesión en el archivo** | `strace -o log.txt ./binary` |
| **Operaciones de seguimiento de archivos** | `strace -e trace=file ./binary` |
| **Seguimiento de la actividad de la red** | `strace -e trace=network ./binary` |
| **Seguir procesos secundarios** | `strace -f ./binary` |
| **Adjuntar al PID en ejecución** | `sudo strace -f -p <PID>` |
| **Ampliar longitud de cadena** | `strace -s 2048 ./binary` |
| **Estadísticas resumidas de llamadas al sistema** | `strace -c ./binary` |
| **Seguimiento de llamadas de biblioteca** | `ltrace ./binary` |
| **Capturar verificación de contraseña** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **Demandar nombres de C++** | `ltrace -C ./binary` |
| **Liberación combinada + llamada al sistema**| `ltrace -S ./binary` |

---

## 🛡️ Consideraciones defensivas y anti-rastreo

1. **Ptrace Self-Checking:** Linux permite que solo se conecte un único rastreador a un proceso a través de `ptrace`. La llamada a `ptrace(PTRACE_TRACEME, 0, 1, 0)` fallará si `strace` o `ltrace` ya están conectados.
2. **Enlace estático (`-static`):** Los binarios compilados con enlace estático no contienen referencias de biblioteca dinámicas (`DT_NEEDED`), lo que hace que `ltrace` sea completamente ineficaz (use `strace` en su lugar).

---

## ⚠️ Aviso legal

> [!PRECAUCIÓN]
> strace y ltrace son herramientas de diagnóstico dinámico diseñadas para el desarrollo, la depuración y las evaluaciones de seguridad autorizadas de software. Está estrictamente prohibido rastrear procesos no autorizados o interceptar comunicaciones patentadas sin permiso.