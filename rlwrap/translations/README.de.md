<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⌨️ rlwrap – Vollständiger Cheatsheet

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
<p align="center">
  <img src="https://img.shields.io/badge/rlwrap-Readline_Wrapper-blue?style=for-the-badge" alt="rlwrap">
  <img src="https://img.shields.io/badge/Utility-Shell_Stabilization-green?style=for-the-badge" alt="Shell Stabilization">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploitation & Shells">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="License GPLv2">
</p>

<p align="center">
  <b>🌐 Verwandeln Sie rohe Netcat-Listener und dumme Reverse-Shells in vollständige Befehlszeilenschnittstellen mit Pfeiltasten, Verlauf und automatischer Vervollständigung</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Einführung: Das „Dumb Shell“-Problem](#-introduction-the-dumb-shell-problem)
- [Installation](#-Installation)
- [Grundlegende Nutzung](#-basic-usage)
- [Reverse Shells einfangen (`rlwrap + nc`)](#-catcher-reverse-shells-rlwrap--nc)
- [Persistente Verlaufsverwaltung](#-persistent-history-management)
- [Befehlsautomatische Vervollständigung (`-f`)](#-command-auto-completion--f)
- [Vollständiger 5-stufiger TTY-Shell-Upgrade-Workflow](#-full-5-step-tty-shell-upgrade-workflow)
- [Verwenden von rlwrap mit anderen Pentest-Tools](#-using-rlwrap-with-other-pentest-tools)
  - [Oracle SQL*Plus](#oracle-sqlplus)
  - [Redis & Telnet](#redis--telnet)
  - [Socat & benutzerdefinierte Sockets](#socat--custom-sockets)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Tipps und Best Practices](#-tips--best-practices)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Einführung: Das „Dumb Shell“-Problem

Wenn Sie mit herkömmlichen Tools wie Netcat (`nc -lvnp 4444`) eine Reverse-Shell abfangen, erhalten Sie ein **dummes Terminal**. Einem dummen Terminal fehlen die Standardfunktionen zur Zeilenbearbeitung:

- ❌ Durch Drücken der **Aufwärts-/Abwärtspfeiltasten** werden Escape-Sequenzen wie `^[[A` oder `^[[B` gedruckt, anstatt durch den Befehlsverlauf zu blättern.
- ❌ Durch Drücken der **Rücktaste** oder **Entf** wird `^H` oder `^?` gedruckt, anstatt Zeichen zu löschen.
- ❌ Durch Drücken von **Strg + C** wird der Reverse-Shell-Listener sofort beendet und der hart erarbeitete Zugriff verloren.
- ❌ Befehle wie `su`, `sudo`, `nano`, `vim` oder `passwd` schlagen fehl, weil kein Pseudo-Terminal (PTY) angeschlossen ist.
- ❌ Es wird kein dauerhafter Befehlsverlauf über Sitzungen hinweg gespeichert.

**rlwrap** (Readline Wrapper, geschrieben von Hans Lub) löst dieses Problem, indem es zwischen Ihrem Terminal und dem Zielbefehl sitzt. Es fängt Tastaturanschläge ab, nutzt **GNU Readline** und bietet Pfeiltastennavigation, Zeilenbearbeitung, persistente Befehlsverlaufsdateien und wörterbuchbasierte automatische Vervollständigung!

---

## 📥 Installation

`rlwrap` ist leichtgewichtig und in den Standardpaket-Repositorys praktisch jeder Linux-Distribution verfügbar:

### Kali Linux, Debian und Ubuntu
```bash
# Update and install rlwrap
sudo apt update && sudo apt install -y rlwrap

# Verify installation and version
rlwrap --version
```
### Fedora, CentOS und RHEL
```bash
# Fedora / CentOS / RHEL
sudo dnf install -y rlwrap || sudo yum install -y epel-release && sudo yum install -y rlwrap
```
### Arch Linux
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

## 🚀 Grundlegende Verwendung

Die grundlegende Syntax besteht einfach darin, jede Befehlszeile mit `rlwrap` zu umschließen:
```bash
# General syntax
rlwrap [options] <command> [command-arguments]
```
### Gemeinsame Flaggen

| Flagge | Beschreibung |
|------|-------------|
| `-c` | Datei- und Pfadnamenvervollständigung aktivieren |
| `-A` | Ignorieren Sie Escape-Codes für die Eingabeaufforderungsformatierung (verhindert Störungen in der Eingabeaufforderungszeile) |
| `-r` | Merken Sie sich alle bei der Eingabe eingegebenen Wörter und fügen Sie sie in die Vervollständigungswortliste | ein
| `-H <file>` | Geben Sie eine dedizierte Verlaufsdatei zum Speichern und Abrufen von Befehlen an |
| `-s <num>` | Legen Sie die maximale Anzahl an Verlaufszeilen fest (Standard: 300) |
| `-f <file>` | Laden Sie eine benutzerdefinierte Vervollständigungswörterbuchdatei |
| `-b <chars>` | Wortumbruchzeichen definieren |
| `-m` | Mehrzeilige Eingabebearbeitung aktivieren |
| `-p[color]` | Färben Sie die Eingabeaufforderung ein |

---

## 🎣 Reverse Shells fangen (`rlwrap + nc`)

Der häufigste Anwendungsfall bei offensiver Sicherheit ist das Umschließen eines Netcat-Listeners (vollständige Listener-Varianten und Flags finden Sie im [Netcat-Cheatsheet](../Netcat/README.md)):
```bash
# Basic Netcat listener with arrow-key editing & history
rlwrap nc -lvnp 4444

# Professional pentester flags: -c (file completion), -A (prompt fix), -r (remember words)
rlwrap -cAr nc -lvnp 4444
```
### Erläuterung der `-cAr`-Flags:

1. **`-c` (Dateivervollständigung):** Ermöglicht die Verwendung von `<TAB>` zur automatischen Vervollständigung von Datei- und Verzeichnisnamen.
2. **`-A` (Eingabeaufforderungen):** Verhindert seltsame Zeilenumbrüche und die Duplizierung von Eingabeaufforderungen, wenn der Remote-Server nicht standardisierte Eingabeaufforderungszeichenfolgen sendet (z. B. `root@victim:/#`).
3. **`-r` (Wörter merken):** Lernt Wörter, die Sie in die Shell eingeben, damit Sie sie später durch Tabulatoren vervollständigen können.

---

## 💾 Persistente Verlaufsverwaltung

Standardmäßig speichert `rlwrap` den Befehlsverlauf in `~/.<command>_history`. Sie können benutzerdefinierte Verlaufsdateien pro Engagement konfigurieren:
```bash
# Save history to a dedicated engagement file
rlwrap -H ~/.history_target_corp nc -lvnp 4444

# Increase history size to 5,000 entries
rlwrap -H ~/.history_target -s 5000 nc -lvnp 4444

# Case-insensitive history search
rlwrap -i nc -lvnp 4444
```
---

## 🔍 Automatische Befehlsvervollständigung (`-f`)

Sie können `rlwrap` eine Textdatei bereitstellen, die gängige Linux- oder Windows-Befehle enthält. Diese werden automatisch vervollständigt, wenn Sie `<TAB>` drücken:
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

## 🛠️ Vollständiger 5-stufiger TTY-Shell-Upgrade-Workflow

Während `rlwrap` eine sofortige Pfeiltastennavigation und einen Verlauf bietet, erfordern bestimmte interaktive Programme (`su`, `sudo`, `vim`) immer noch ein vollständig zugewiesenes TTY-Terminal. Hier ist die branchenübliche 5-stufige Upgrade-Pipeline:

### Schritt 1: Listener mit rlwrap starten
```bash
# Attacker machine: start enhanced listener
rlwrap -cAr nc -lvnp 4444
```
### Schritt 2: Reverse Shell & Spawn PTY auslösen

Sobald Ihre Reverse-Shell eine Verbindung zu Ihrem Listener herstellt, erzeugen Sie ein Pseudo-Terminal auf dem Ziel:
```bash
# Target shell: Spawn PTY via Python
python3 -c 'import pty; pty.spawn("/bin/bash")'

# OR via script if Python is unavailable:
/usr/bin/script -qc /bin/bash /dev/null
```
### Schritt 3: Hintergrund der Shell
```bash
# Press Ctrl + Z to put the reverse shell into the background
[1]+  Stopped                 rlwrap -cAr nc -lvnp 4444
```
### Schritt 4: Konfigurieren Sie den Raw-Modus des lokalen Terminals
```bash
# Attacker machine: disable local echo and enable raw keystrokes
stty raw -echo; fg
```
*(Drücken Sie nach dem Ausführen von `fg` zweimal die Eingabetaste, um die Eingabeaufforderung wiederherzustellen)*

### Schritt 5: Terminalabmessungen zurücksetzen und festlegen
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
> ✅ **Ergebnis:** Sie haben jetzt eine unzerstörbare, voll funktionsfähige interaktive Bash-Shell mit Tab-Vervollständigung, klarem Bildschirm (`Ctrl+L`), Signalverarbeitung (`Ctrl+C` trennt die Verbindung nicht) und vollständigen Terminalabmessungen!

---

## 🔧 Verwendung von rlwrap mit anderen Pentest-Tools

`rlwrap` ist nicht auf Netcat beschränkt. Jedes CLI-Tool ohne Zeilenbearbeitung profitiert davon:

### Oracle SQL*Plus
```bash
# Oracle's sqlplus utility notoriously lacks line editing and history on Linux
rlwrap sqlplus scott/tiger@10.10.10.50:1521/ORCL
```
### Redis und Telnet
```bash
# Redis CLI interactive console
rlwrap redis-cli -h 10.10.10.50 -p 6379

# Raw Telnet or socket interaction
rlwrap telnet 10.10.10.50 25
```
### Socat- und benutzerdefinierte Sockel
```bash
# Socat listener with rlwrap
rlwrap socat file:`tty`,raw,echo=0 tcp-listen:4444

# Connecting to raw sockets / CTF pwn challenges
rlwrap nc ctf.challenge.local 1337
```
---

## ⚡ Kurzreferenztabelle

| Aufgabe | Befehl |
|------|---------|
| **Erweiterter Netcat-Listener** | `rlwrap nc -lvnp 4444` |
| **Vollständige Datei- und Eingabeaufforderungsflags** | `rlwrap -cAr nc -lvnp 4444` |
| **Dedizierte Verlaufsdatei** | `rlwrap -H ~/.nc_history nc -lvnp 4444` |
| **Benutzerdefinierte Tab-Vervollständigung** | `rlwrap -f /path/to/wordlist.txt nc -lvnp 4444` |
| **Verlaufsgröße erhöhen** | `rlwrap -s 10000 nc -lvnp 4444` |
| **Oracle SQLPlus umschließen** | `rlwrap sqlplus user/pass@host/db` |
| **Redis-CLI umschließen** | `rlwrap redis-cli -h 10.10.10.50` |
| **Socat-Listener umschließen** | `rlwrap socat TCP-LISTEN:4444,reuseaddr -` |

---

## 💡 Tipps und Best Practices

1. **Erstellen Sie einen Bash-Alias:** Fügen Sie dies Ihrem `~/.bashrc` oder `~/.zshrc` hinzu, damit Sie nie vergessen, es zu verwenden:
   ```bash
   alias nc='rlwrap -cAr nc'
   ```
2. **Verpassen Sie niemals die `Ctrl+C`-Sicherheit:** Beim Abfangen instabiler Shells fängt `rlwrap` grundlegende Tastaturfehler ab, aber denken Sie immer daran, das 5-stufige `stty raw -echo; fg`-Upgrade durchzuführen, wenn Sie vorhaben, lange interaktive Befehle auszuführen.
3. **Client-Verläufe isolieren:** Wenn Sie mehrere Clients oder Maschinen testen, verwenden Sie immer `-H ~/.client_name.hist`, um separate Prüfprotokolle und Befehlsverläufe zu verwalten.

---

## ⚠️ Rechtlicher Haftungsausschluss

> **WICHTIG:** Reverse Shells und Post-Exploitation-Tools dürfen nur mit ausdrücklicher schriftlicher Genehmigung in autorisierten Penetrationstests, Red-Team-Einsätzen und Lehrlaborumgebungen verwendet werden.