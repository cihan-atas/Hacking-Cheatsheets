<!-- language-home-navigation -->
[🏠 Página Principal Española](../../README.es.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap - Hoja de referencia completa

[🇹🇷 Türkçe Sürüm](traducciones/README.tr.md)
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<p align="centro">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Estabilización de Shell">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Explotación y Shells">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licencia GPLv2">
</p>

<p align="centro">
  <b>🌐 Convierta oyentes de Netcat sin formato y shells inversos tontos en interfaces de línea de comandos completas con teclas de flecha, historial y autocompletado</b>
</p>

---

## 📋 Tabla de contenidos

- [Introducción: El problema del "caparazón tonto"](#-introducción-el-problema-del-caparazón-tonto)
- [Instalación](#-instalación)
- [Uso básico](#-uso-básico)
- [Captura de conchas inversas (`rlwrap + nc`)](#-catcher-reverse-shells-rlwrap--nc)
- [Gestión del historial persistente](#-gestión-del-historial-persistente)
- [Comando Autocompletar (`-f`)](#-command-auto-completion--f)
- [Flujo de trabajo completo de actualización de Shell TTY de 5 pasos](#-full-5-step-tty-shell-upgrade-workflow)
- [Usando rlwrap con otras herramientas Pentest](#-usando-rlwrap-con-otras-herramientas-pentest)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis y Telnet](#redis--telnet)
  - [Socat y enchufes personalizados](#socat--sockets personalizados)
- [Tabla de referencia rápida](#-tabla-de-referencia-rápida)
- [Consejos y mejores prácticas](#-consejos--mejores-prácticas)
- [Aviso legal](#-aviso-legal)

---

## 🎯 Introducción: El problema del "caparazón tonto"

Cuando capturas un shell inverso usando herramientas tradicionales como Netcat (`nc -lvnp 4444`), obtienes una **terminal tonta**. Una terminal tonta carece de capacidades estándar de edición de líneas:

- ❌ Al presionar las **teclas de flecha arriba/abajo** se imprimen secuencias de escape como `^[[A` o `^[[B` en lugar de recorrer el historial de comandos.
- ❌ Al presionar **Retroceso** o **Eliminar** se imprime `^H` o `^?` en lugar de eliminar caracteres.
- ❌ Al presionar **Ctrl + C** se elimina inmediatamente el oyente del shell inverso, perdiendo el acceso que tanto le costó ganar.
- ❌ Comandos como `su`, `sudo`, `nano`, `vim` o `passwd` fallan porque no hay ningún pseudoterminal (PTY) adjunto.
- ❌ No se conserva ningún historial de comandos persistente entre sesiones.

**rlwrap** (Readline Wrapper, escrito por Hans Lub) resuelve esto sentándose entre su terminal y el comando de destino. Intercepta las pulsaciones del teclado, aprovecha **GNU Readline** y proporciona navegación con teclas de flecha, edición de líneas, archivos de historial de comandos persistentes y autocompletado basado en diccionarios.

---

## 📥 Instalación

`rlwrap` es liviano y está disponible en los repositorios de paquetes predeterminados de prácticamente todas las distribuciones de Linux:

### Kali Linux, Debian y Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora, CentOS y RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### Arco Linux
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### macOS (cervecería casera)
```bash
# macOS
brew install rlwrap
```
---

## 🚀 Uso básico

La sintaxis básica es simplemente envolver cualquier línea de comando con `rlwrap`:
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### Banderas comunes

| Bandera | Descripción |
|------|-------------|
| `-c` | Habilitar la finalización de nombres de archivos y rutas |
| `-A` | Ignorar los códigos de escape de formato de solicitud (evita fallos en la línea de solicitud) |
| `-r` | Recuerde todas las palabras ingresadas al ingresarlas y colóquelas en la lista de palabras de finalización |
| `-H <file>` | Especifique un archivo de historial dedicado para guardar y recuperar comandos |
| `-s <num>` | Establecer el número máximo de líneas del historial (predeterminado: 300) |
| `-f <file>` | Cargar un archivo de diccionario de finalización personalizado |
| `-b <chars>` | Definir caracteres de separación de palabras |
| `-m` | Habilitar edición de entrada de varias líneas |
| `-p[color]` | Colorear el símbolo del sistema |

---

## 🎣 Atrapando conchas inversas (`rlwrap + nc`)

El caso de uso más común en seguridad ofensiva es empaquetar un oyente de Netcat (consulte la [Hoja de referencia de Netcat](../Netcat/README.md) para conocer las variantes y los indicadores completos del oyente):
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### Explicación de las banderas `-cAr`:

1. **`-c` (Finalización de archivos):** Permite usar `<TAB>` para autocompletar nombres de archivos y directorios.
2. **`-A` (solicitudes):** Previene cambios de línea extraños y duplicación de indicaciones cuando el servidor remoto envía cadenas de indicaciones no estandarizadas (por ejemplo, `root@victim:/#`).
3. **`-r` (Recordar palabras):** Aprende las palabras que escribes en el shell para poder completarlas con tabulaciones más tarde.

---

## 💾 Gestión del historial persistente

De forma predeterminada, `rlwrap` guarda el historial de comandos en `~/.<command>_history`. Puede configurar archivos de historial personalizados por interacción:
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 Comando Autocompletar (`-f`)

Puede proporcionar a `rlwrap` un archivo de texto que contenga comandos comunes de Linux o Windows, y los completará automáticamente cuando presione `<TAB>`:
```bash
# Create a wordlist of essential post-exploitation commands
cat << 'EOF' > /tmp/pentest_commands.txt
whoami
id
hostname
uname -a
cat /etc/passwd
cat /etc/shadow
sudo -l
find / -perm -4000 2>/dev/null
netstat -tulnp
ss -tulnp
ps aux
ip a
route -n
crontab -l
EOF

# Start listener with custom tab-completion dictionary
rlwrap -f /tmp/pentest_commands.txt nc -lvnp 4444
```
---

## 🛠️ Flujo de trabajo completo de actualización del shell TTY de 5 pasos

Si bien `rlwrap` proporciona navegación e historial inmediatos con las teclas de flecha, ciertos programas interactivos (`su`, `sudo`, `vim`) aún requieren una terminal TTY completamente asignada. Aquí está el proceso de actualización de 5 pasos estándar de la industria:

### Paso 1: Iniciar Listener con rlwrap
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### Paso 2: Activar Shell inverso y generar PTY

Una vez que su shell inverso se conecte a su oyente, genere un pseudo-terminal en el objetivo:
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### Paso 3: Poner en segundo plano el Shell
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### Paso 4: Configurar el modo sin formato del terminal local
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(Presione Enter dos veces después de ejecutar `fg` para restaurar el mensaje)*

### Paso 5: Restablecer y configurar las dimensiones de los terminales
```bash
# Target shell: set terminal type and environment
reset
export TERM=xterm-256color
export SHELL=bash

# Check your attacker terminal size in another window:
# $ stty size  -> Output: 38 160

# Apply rows and columns to target shell:
stty rows 38 columns 160
```
> ✅ **Resultado:** ¡Ahora tienes un shell bash interactivo, indestructible y con todas las funciones, con función de tabulación, pantalla clara (`Ctrl+L`), manejo de señales (`Ctrl+C` no se desconecta) y dimensiones completas del terminal!

---

## 🔧 Uso de rlwrap con otras herramientas Pentest

`rlwrap` no se limita a Netcat. Cualquier herramienta CLI que carezca de edición de líneas se beneficia de ella:

### Oracle SQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis y Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Socat y enchufes personalizados
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ Tabla de referencia rápida

| Tarea | Comando |
|------|---------|
| **Escucha Netcat mejorada** | `rlwrap nc -lvnp 4444` |
| **Archivo completo e indicadores de aviso** | `rlwrap -cAr nc -lvnp 4444` |
| **Archivo de historial dedicado** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Tabulación personalizada** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Aumentar tamaño del historial** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Ajustar Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **Ajuste la CLI de Redis** | `rlwrap redis-cli -h 10.10.10.50` |
| **Envolver al oyente de Socat** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Consejos y mejores prácticas

1. **Crea un alias de Bash:** Agrega esto a tu `~/.bashrc` o `~/.zshrc` para que nunca olvides usarlo:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Nunca te pierdas la seguridad de `Ctrl+C`:** Al detectar shells inestables, `rlwrap` intercepta fallas básicas del teclado, pero recuerda siempre realizar la actualización de 5 pasos de `stty raw -echo; fg` si planeas ejecutar comandos interactivos largos.
3. **Aislar historiales de clientes:** Cuando pruebe varios clientes o máquinas, utilice siempre `-H ~/.client_name.hist` para mantener registros de auditoría e historiales de comandos separados.

---

## ⚠️ Aviso legal

> **IMPORTANTE:** Los shells inversos y las herramientas posteriores a la explotación solo deben usarse en pruebas de penetración autorizadas, compromisos de equipos rojos y entornos de laboratorios educativos con el consentimiento explícito por escrito.