<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌲 adidnsdump - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   █████╗ ██████╗ ██╗██████╗ ███╗   ██╗███████╗██████╗ ██╗   ██╗███╗   ███╗██████╗ 
  ██╔══██╗██╔══██╗██║██╔══██╗████╗  ██║██╔════╝██╔══██╗██║   ██║████╗ ████║██╔══██╗
  ███████║██║  ██║██║██║  ██║██╔██╗ ██║███████╗██║  ██║██║   ██║██╔████╔██║██████╔╝
  ██╔══██║██║  ██║██║██║  ██║██║╚██╗██║╚════██║██║  ██║██║   ██║██║╚██╔╝██║██╔═══╝ 
  ██║  ██║██████╔╝██║██████╔╝██║ ╚████║███████║██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     
  ╚═╝  ╚═╝╚═════╝ ╚═╝╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═════╝  ╚═════╝ ╚═╝     ╚═╝╚═╝     
           Active Directory Integrated DNS (ADIDNS) Reconnaissance Tool
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Tool-adidnsdump-blue?style=for-the-badge" alt="adidnsdump">
  <img src="https://img.shields.io/badge/Author-Dirk--jan_Mollema-orange?style=for-the-badge" alt="dirkjanm">
  <img src="https://img.shields.io/badge/Category-Active_Directory-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/Protocol-LDAP_%2F_LDAPS-purple?style=for-the-badge" alt="LDAP">
</p>

<p alinhar="centro">
  <b>🌲 Enumerar e despejar zonas e registros DNS integrados do Active Directory (ADIDNS) por LDAP sem transferências de zona DNS (AXFR)</b>
</p>

---

## 📋 Índice

- [Visão geral e histórico técnico](#-overview--technical-background)
- [Instalação](#-instalação)
- [Métodos de autenticação](#-métodos de autenticação)
  - [Autenticação de senha](#autenticação de senha)
  - [Passar o hash (NTLM)](#passar o hash-ntlm)
  - [Tíquete Kerberos (ccache)](#kerberos-ticket-ccache)
- [Comandos de Core Dumping](#-core-dumping-commands)
  - [Despejo de zona DNS padrão do Active Directory](#standard-active-directory-dns-zone-dump)
  - [Despejando registros excluídos/excluídos](#dumping-tombstoned--deleted-records)
  - [Dumping de DNS de floresta e zonas de DNS de domínio](#dumping-forest-dns--domain-dns-zones)
  - [Direcionando zonas DNS específicas](#targeting-specific-dns-zones)
- [Formatos de exportação e pós-processamento](#-export-formats--post-processing)
  - [Saída CSV](#csv-output)
  - [Integração com BloodHound e Nmap](#integrating-with-bloodhound--nmap)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e histórico técnico

Em ambientes padrão do Windows Active Directory, o DNS geralmente é integrado ao Active Directory (**DNS/ADIDNS integrado ao Active Directory**). Nessas configurações, os registros DNS não são armazenados em arquivos simples de texto simples, mas como objetos LDAP dentro da partição do domínio (como `DC=DomainDnsZones` e `DC=ForestDnsZones`).

As transferências tradicionais de zona DNS (`AXFR`) são quase universalmente desativadas em controladores de domínio. No entanto, **qualquer usuário de domínio autenticado** (mesmo contas com poucos privilégios ou contas de serviço comprometidas) tem permissões de leitura nos nós LDAP que armazenam registros ADIDNS por padrão.

Desenvolvido por **Dirk-jan Mollema (@dirkjanm)**, **`adidnsdump`** se conecta ao controlador de domínio via LDAP ou LDAPS, consulta as partições ADIDNS e extrai todos os registros (A, AAAA, CNAME, SRV, TXT, MX e registros marcados para exclusão), criando um inventário preciso de cada computador, estação de trabalho e serviço web interno no domínio.

---

## 📥 Instalação
```bash
# Clone the repository and install with pip
git clone https://github.com/dirkjanm/adidnsdump.git
cd adidnsdump
pip install .

# Or install directly via pipx / pip
pipx install git+https://github.com/dirkjanm/adidnsdump.git
```
---

## 🔑 Métodos de autenticação

### Autenticação de senha
```bash
adidnsdump -u 'CORP\jdoe' -p 'P@ssw0rd123!' 192.168.1.10
```
### Pass-the-Hash (NTLM)
Se você extraiu hashes NTLM via Mimikatz ou Secretsdump:
```bash
adidnsdump -u 'CORP\jdoe' --hashes :31d6cfe0d16ae931b73c59d7e0c089c0 192.168.1.10
```
### Bilhete Kerberos (ccache)
Usando um ticket Kerberos TGT/TGS existente armazenado em `KRB5CCNAME`:
```bash
export KRB5CCNAME=/tmp/jdoe.ccache
adidnsdump -k -u 'CORP\jdoe' dc01.corp.local
```
---

## 🚀 Comandos de dump principal

### Despejo de zona DNS padrão do Active Directory
Despeja todos os registros DNS ativos da partição DomainDnsZones padrão:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldap://dc01.corp.local
```
Isso gera um arquivo chamado `records.csv` em seu diretório de trabalho atual.

### Despejo de registros excluídos/marcados para exclusão
O DNS do Windows marca os registros excluídos como "marcados para exclusão" por vários dias antes da exclusão física. Esses registros geralmente revelam sistemas desativados, servidores legados ou ambientes de teste:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --include-tombstoned ldap://dc01.corp.local
```
### Dumping de DNS florestal e zonas DNS de domínio
Extraia registros de partições DNS em nível de floresta:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --forest ldap://dc01.corp.local
```
### Direcionando zonas DNS específicas
Se o alvo operar múltiplas zonas ou subdomínios DNS split-brain:
```bash
adidnsdump -u 'corp.local\jdoe' -p 'P@ssw0rd123!' --zone dev.corp.local ldap://dc01.corp.local
```
### Comunicação criptografada LDAPS segura
Evite que as credenciais atravessem a rede em texto simples:
```bash
adidnsdump --ssl -u 'corp.local\jdoe' -p 'P@ssw0rd123!' ldaps://dc01.corp.local
```
---

## 📊 Formatos de exportação e pós-processamento

### Saída CSV
Por padrão, `adidnsdump` gera `records.csv`. Os campos incluem:
- `zone`: A zona DNS à qual o registro pertence.
- `name`: Grave o nome do host ou gravadora.
- `type`: Tipo de registro (A, AAAA, CNAME, SRV, etc.).
- `value`: endereço IP de destino, nome de host ou string.

### Integrando com BloodHound e Nmap
Extraia todos os endereços IP exclusivos dos registros descartados para verificação automatizada:
```bash
# Filter unique IP addresses (Type A records)
awk -F',' '$3=="A" {print $4}' records.csv | sort -u > live_ips.txt

# Run fast port scan against newly discovered internal hosts
nmap -sS -Pn -iL live_ips.txt -T4 -p 80,443,445,3389 -oA internal_assets
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Despejo ADIDNS padrão** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' <DC_IP>` |
| **Passe o hash (NTLM)** | `adidnsdump -u 'DOMAIN\User' --hashes :<NTLM> <DC_IP>` |
| **Autenticação de ingresso Kerberos** | `adidnsdump -k -u 'DOMAIN\User' <DC_FQDN>` |
| **Despejar registros marcados para exclusão**| `adidnsdump -u 'DOMAIN\User' -p 'Pass' --include-tombstoned <DC_IP>` |
| **Zonas florestais de despejo** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --forest <DC_IP>` |
| **Especifique a zona alvo** | `adidnsdump -u 'DOMAIN\User' -p 'Pass' --zone <ZONE> <DC_IP>` |
| **Aplicar LDAPS (SSL)** | `adidnsdump --ssl -u 'DOMAIN\User' -p 'Pass' ldaps://<DC_IP>` |

---

## 🛡️ Considerações defensivas e detecção

1. **Monitoramento de consulta LDAP:** Habilite a Política de auditoria avançada para acesso ao serviço de diretório (ID do evento 4662) para alertar sobre consultas de alto volume em `CN=MicrosoftDNS,DC=DomainDnsZones`.
2. **Restringir ACLs ADIDNS:** Restrinja as permissões de leitura no contêiner `MicrosoftDNS` para administradores de domínio ou funções administrativas específicas se não administradores não exigirem introspecção DNS interna.
3. **Honeytokens:** Plante registros DNS atraentes (por exemplo, `backup-vault.corp.local` ou `admin-portal.corp.local`) apontando para endereços IP de honeypot.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> adidnsdump destina-se estritamente a avaliações de segurança autorizadas, auditorias do Active Directory e testes de penetração dentro dos escopos permitidos. Consultar serviços de diretório em redes não autorizadas é ilegal.