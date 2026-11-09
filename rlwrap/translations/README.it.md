<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ██████╗ ██╗     ██╗    ██╗██████╗  █████╗ ██████╗ 
  ██╔══██╗██║     ██║    ██║██╔══██╗██╔══██╗██╔══██╗
  ██████╔╝██║     ██║ █╗ ██║██████╔╝███████║██████╔╝
  ██╔══██╗██║     ██║███╗██║██╔══██╗██╔══██║██╔═══╝ 
  ██║  ██║███████╗╚███╔███╔╝██║  ██║██║  ██║██║     
  ╚═╝  ╚═╝╚══════╝ ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚═╝     
       Readline Wrapper for Reverse Shells & Dumb Terminals
```
<palign="centro">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Stabilizzazione Shell">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Sfruttamento e conchiglie">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licenza GPLv2">
</p>

<palign="centro">
  <b>🌐 Trasforma i grezzi ascoltatori Netcat e le stupide shell inverse in interfacce a riga di comando complete con tasti freccia, cronologia e completamento automatico</b>
</p>

---

## 📋 Sommario

- [Introduzione: il problema del "dumb-shell"](#-introduzione-il-problema-del-dumb-shell)
- [Installazione](#-installazione)
- [Utilizzo di base](#-utilizzo-di-base)
- [Catching Reverse Shells (`rlwrap + nc`)](#-changing-reverse-shells-rlwrap--nc)
- [Gestione della cronologia persistente](#-gestione-della-cronologia-persistente)
- [Completamento automatico comando (`-f`)](#-command-auto-completion--f)
- [Flusso di lavoro completo per l'aggiornamento della shell TTY in 5 passaggi](#-flusso di lavoro completo per l'aggiornamento della shell TTY in 5 passaggi)
- [Utilizzo di rlwrap con altri strumenti Pentest](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis e Telnet](#redis--telnet)
  - [Socat e socket personalizzati](#socat--custom-socket)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Suggerimenti e best practice](#-tips--best-practice)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Introduzione: il problema del "guscio stupido".

Quando catturi una shell inversa utilizzando strumenti tradizionali come Netcat (`nc -lvnp 4444`), ottieni un **terminale stupido**. Un terminale stupido non dispone di funzionalità standard di modifica della riga:

- ❌ Premendo i **tasti freccia su/giù** vengono stampate sequenze di escape come `^[[A` o `^[[B` invece di scorrere la cronologia dei comandi.
- ❌ Premendo **Backspace** o **Elimina** si stampa `^H` o `^?` invece di eliminare i caratteri.
- ❌ Premendo **Ctrl + C** si uccide immediatamente l'ascoltatore della shell inversa, perdendo l'accesso guadagnato con tanta fatica.
- ❌ Comandi come `su`, `sudo`, `nano`, `vim` o `passwd` falliscono perché non è collegato uno pseudo-terminale (PTY).
- ❌ Nessuna cronologia persistente dei comandi viene conservata tra le sessioni.

**rlwrap** (Readline Wrapper, scritto da Hans Lub) risolve questo problema posizionandosi tra il terminale e il comando di destinazione. Intercetta i colpi di tastiera, sfrutta **GNU Readline** e fornisce navigazione con tasti freccia, modifica di righe, file di cronologia dei comandi persistenti e completamento automatico basato su dizionario!

---

## 📥Installazione

`rlwrap` è leggero e disponibile nei repository di pacchetti predefiniti praticamente di ogni distribuzione Linux:

### Kali Linux, Debian e Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora, CentOS e RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
###ArchLinux
```bash
# Arch Linux / Manjaro
sudo pacman -S rlwrap
```
### macOS (Homebrew)
```bash
# macOS
brew install rlwrap
```
---

## 🚀 Utilizzo di base

La sintassi di base consiste semplicemente nel racchiudere qualsiasi riga di comando con `rlwrap`:
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### Bandiere comuni

| Bandiera | Descrizione |
|------|-------------|
| `-c` | Abilita il completamento del nome del file e del percorso |
| `-A` | Ignora i codici di escape della formattazione del prompt (previene errori nella riga del prompt) |
| `-r` | Ricorda tutte le parole immesse durante l'input e inseriscile nell'elenco di parole di completamento |
| `-H <file>` | Specificare un file di cronologia dedicato per salvare e richiamare i comandi |
| `-s <num>` | Imposta il numero massimo di righe della cronologia (impostazione predefinita: 300) |
| `-f <file>` | Carica un file del dizionario di completamento personalizzato |
| `-b <chars>` | Definire i caratteri di interruzione di parola |
| `-m` | Abilita la modifica dell'input su più righe |
| `-p[color]` | Colora il prompt dei comandi |

---

## 🎣 Catturare i gusci inversi (`rlwrap + nc`)

Il caso d'uso più comune nella sicurezza offensiva è il confezionamento di un listener Netcat (vedere il [Netcat Cheatsheet](../Netcat/README.md) per varianti e flag completi del listener):
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### Spiegazione dei flag `-cAr`:

1. **`-c` (Completamento file):** consente di utilizzare `<TAB>` per completare automaticamente i nomi di file e directory.
2. **`-A` (Prompt):** Previene strani a capo di riga e la duplicazione dei prompt quando il server remoto invia stringhe di prompt non standardizzate (ad esempio `root@victim:/#`).
3. **`-r` (Ricorda parole):** impara le parole digitate nella shell in modo da poterle completare con tab in seguito.

---

## 💾 Gestione della cronologia persistente

Per impostazione predefinita, `rlwrap` salva la cronologia dei comandi in `~/.<command>_history`. Puoi configurare file di cronologia personalizzati per impegno:
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 Completamento automatico dei comandi (`-f`)

Puoi fornire a `rlwrap` un file di testo contenente comandi Linux o Windows comuni e li completerà automaticamente quando premi `<TAB>`:
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

## 🛠️ Flusso di lavoro completo per l'aggiornamento della shell TTY in 5 passaggi

Sebbene `rlwrap` fornisca navigazione e cronologia immediate con i tasti freccia, alcuni programmi interattivi (`su`, `sudo`, `vim`) richiedono ancora un terminale TTY completamente allocato. Ecco la pipeline di aggiornamento in 5 fasi standard del settore:

### Passaggio 1: avvia Listener con rlwrap
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### Passaggio 2: attiva la shell inversa e genera PTY

Una volta che la tua shell inversa si connette al tuo ascoltatore, genera uno pseudo-terminale sul bersaglio:
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### Passaggio 3: creare lo sfondo della Shell
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### Passaggio 4: configurare la modalità Raw del terminale locale
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(Premi Invio due volte dopo aver eseguito `fg` per ripristinare il prompt)*

### Passaggio 5: reimposta e imposta le dimensioni del terminale
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
> ✅ **Risultato:** Ora disponi di una shell bash interattiva indistruttibile e completa di funzionalità con completamento tramite schede, schermo trasparente (`Ctrl+L`), gestione del segnale (`Ctrl+C` non si disconnette) e dimensioni del terminale complete!

---

## 🔧 Utilizzo di rlwrap con altri strumenti Pentest

`rlwrap` non è limitato a Netcat. Qualsiasi strumento CLI privo di modifica della riga ne trae vantaggio:

### OracleSQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis e Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Prese Socat e personalizzate
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ Tabella di consultazione rapida

| Compito | Comando |
|------|---------|
| **Ascoltatore Netcat migliorato** | `rlwrap nc -lvnp 4444` |
| **File completo e flag di richiesta** | `rlwrap -cAr nc -lvnp 4444` |
| **File di cronologia dedicato** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Completamento scheda personalizzato** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Aumenta dimensione cronologia** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Wrap Oracle SQLPlus** | `rlwrap sqlplus user/pass@host/db` |
| **Avvolgi la CLI Redis** | `rlwrap redis-cli -h 10.10.10.50` |
| **Ascoltatore Wrap Socat** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Suggerimenti e migliori pratiche

1. **Crea un alias Bash:** Aggiungilo al tuo `~/.bashrc` o `~/.zshrc` in modo da non dimenticare mai di usarlo:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Non perdere mai la sicurezza di `Ctrl+C`:** Quando rilevi shell instabili, `rlwrap` intercetta i problemi di base della tastiera, ma ricorda sempre di eseguire l'aggiornamento `stty raw -echo; fg` in 5 passaggi se prevedi di eseguire comandi interattivi lunghi.
3. **Isolare le cronologie dei client:** durante il test di più client o macchine, utilizzare sempre `-H ~/.client_name.hist` per mantenere audit trail e cronologie dei comandi separati.

---

## ⚠️ Disclaimer legale

> **IMPORTANTE:** Le reverse shell e gli strumenti post-sfruttamento devono essere utilizzati solo nei penetration test autorizzati, negli impegni del team rosso e negli ambienti di laboratorio didattici con esplicito consenso scritto.