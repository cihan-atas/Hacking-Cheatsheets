<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🐶 Snaffler - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗███╗   ██╗ █████╗ ███████╗███████╗██╗     ███████╗██████╗ 
  ██╔════╝████╗  ██║██╔══██╗██╔════╝██╔════╝██║     ██╔════╝██╔══██╗
  ███████╗██╔██╗ ██║███████║█████╗  █████╗  ██║     █████╗  ██████╔╝
  ╚════██║██║╚██╗██║██╔══██║██╔══╝  ██╔══╝  ██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚████║██║  ██║██║     ██║     ███████╗███████╗██║  ██║
  ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝     ╚═╝     ╚══════╝╚══════╝╚═╝  ╚═╝
       Active Directory Domain SMB Share Credential & Secret Hunter
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Snaffler-Secret_Hunter-blue?style=for-the-badge" alt="Snaffler">
  <img src="https://img.shields.io/badge/Language-C%23_/_NET-orange?style=for-the-badge" alt="C# / .NET">
  <img src="https://img.shields.io/badge/Category-Active_Directory_&_Network-green?style=for-the-badge" alt="Active Directory">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licença GPLv3">
</p>

<p alinhar="centro">
  <b>🌐 Ferramenta automatizada de domínio do Windows projetada para rastrear compartilhamentos SMB, localizar arquivos de configuração interessantes, bancos de dados KeePass e credenciais de texto simples</b>
</p>

---

## 📋 Índice

- [Visão geral e o problema da caça ao compartilhamento] (#-overview--the-share-hunting-problem)
- [Como funciona o Snaffler](#-como-funciona o Snaffler)
- [Opções de instalação e execução](#-installation--execution-options)
- [Triagem do Snaffler e níveis de gravidade](#-snaffler-triage--severity-levels)
- [Fluxos de trabalho de verificação comuns](#-common-scanning-workflows)
  - [Rastreamento de compartilhamento em todo o domínio](#rastreamento de compartilhamento em todo o domínio)
  - [Segmentando hosts ou compartilhamentos específicos](#targeting-specific-hosts-or-shares)
  - [Excluindo computadores e raízes DFS](#eexcluindo-computadores--dfs-roots)
- [Juicy Targets e regras de correspondência de arquivos](#-juicy-targets--file-match-rules)
- [Registro e análise de saída](#-registro de saída--análise)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e o problema da caça ao compartilhamento

Em ambientes Active Directory de médio a grande porte, servidores de arquivos corporativos e estações de trabalho hospedam milhares de compartilhamentos de rede SMB abertos. Administradores de sistema, desenvolvedores e funcionários frequentemente deixam para trás:

- 🔑 **Bancos de dados KeePass (`.kdbx`):** Geralmente contém senhas mestras ou chaves mestras facilmente quebráveis.
- ⚙️ **Arquivos de configuração (`web.config`, `appsettings.json`):** Contendo cadeias de conexão SQL codificadas, senhas de contas de serviço e chaves de API.
- 📜 **Scripts de implantação (`unattend.xml`, `.ps1`, `.bat`):** Credenciais administrativas codificadas.
- 🔏 **Certificados privados (`.pfx`, `.p12`, `.pem`, `.ppk`):** Utilizáveis ​​para escalonamento de privilégios ou movimentação lateral.

É impossível navegar manualmente em milhares de compartilhamentos. O **Snaffler**, desenvolvido por **l33tb455**, automatiza todo esse processo: ele consulta o Active Directory em busca de objetos de computador, enumera compartilhamentos SMB legíveis, percorre a árvore de diretórios e avalia arquivos em relação a conjuntos de regras regex selecionados.

---

## ⚙️ Como funciona o Snaffler
```
[ Compromised Domain Machine ]
         │
         ├── 1. Queries Domain Controller via LDAP for Computer Objects
         ├── 2. Connects to port 445 (SMB) across all discovered hosts
         ├── 3. Enumerates Read/Write permissions on C$, SYSVOL, NETLOGON, Custom Shares
         └── 4. Evaluates file extensions, file names, and file contents against rules
```
---

## 📥 Opções de instalação e execução

O Snaffler é compilado como um binário .NET independente:
```bash
# Clone and build using Visual Studio or dotnet CLI on Windows
git clone https://github.com/SnaffCon/Snaffler.git
cd Snaffler
dotnet build -c Release
```
### Executando no Windows:
```cmd
# Standard execution outputting to screen and log file
Snaffler.exe -s -o snaffler_log.txt
```
### Executando via Cobalt Strike / C2 (Execute-Assembly):
```text
beacon> execute-assembly C:\Tools\Snaffler.exe -s -o C:\Temp\loot.txt
```
---

## 🚦 Triagem e níveis de gravidade do Snaffler

O Snaffler categoriza as descobertas em grupos de urgência codificados por cores:

- 🔴 **`{Red}` / Preto:** Achado definitivo de alto valor! Senhas em texto simples, chaves privadas (`.pfx`, `.id_rsa`), tokens AWS, arquivos de instalação autônoma.
- 🟡 **`{Yellow}`:** Provável achado. Arquivos de configuração com sequências de senha, sequências de conexão de banco de dados, cópias de sombra.
- 🟢 **`{Green}`:** Informativo/Interesse. Nomes de arquivos que correspondem aos padrões administrativos (`passwords.xlsx`, `vpn.ovpn`).

---

## 🚀 Fluxos de trabalho de digitalização comuns

### Rastreamento de compartilhamento em todo o domínio

Verifique todos os compartilhamentos acessíveis em todo o domínio do Active Directory:
```cmd
# Run with live console streaming (-s) and save to disk
Snaffler.exe -s -o C:\loot\snaffler_domain.txt

# Run with multi-threading tuning (Default: 60 threads)
Snaffler.exe -s -t 100 -o C:\loot\snaffler_fast.txt
```
### Visando hosts ou compartilhamentos específicos

Quando você quiser evitar a verificação de todo o domínio e focar em um servidor de arquivos ou controlador de domínio:
```cmd
# Scan a single targeted file server
Snaffler.exe -c fs01.corp.local -s

# Scan a comma-separated list of machines
Snaffler.exe -c dc01.corp.local,fs02.corp.local,sql01.corp.local -s

# Scan a specific shared folder path directly
Snaffler.exe -i \\fs01.corp.local\Departments\IT\ -s
```
### Excluindo computadores e raízes DFS

Evite verificar computadores lentos ou sensíveis:
```cmd
# Exclude specific backup or surveillance servers
Snaffler.exe -s -c- backup01.corp.local,cctv01.corp.local
```
---

## 🎯 Alvos suculentos e regras de correspondência de arquivos

Por padrão, o Snaffler procura as seguintes extensões críticas:

| Extensão/Arquivo | Conteúdo de alto valor |
|------------------|--------------------|
| `.kdbx`/`.kdb` | Cofres do gerenciador de senhas KeePass |
| `web.config`/`app.config` | Senhas de banco de dados em texto não criptografado |
| `unattend.xml`/`sysprep.inf` | Senhas de administrador do Windows |
| `.ppk`/`.pem`/`id_rsa` | Chaves privadas SSH |
| `.pfx`/`.p12` | Certificados do Active Directory |
| `.rdp`/`.vnc` | Perfis de conexão de área de trabalho remota |
| `passwords.xlsx`/`creds.txt` | Listas de senhas de funcionários |

---

## 📊 Registro e análise de saída
```bash
# Grep for immediate RED credentials from a Linux terminal
grep -i "{Red}" snaffler_domain.txt

# Find all discovered KeePass safes
grep -i "\.kdbx" snaffler_domain.txt

# Filter out database connection strings
grep -i "password=" snaffler_domain.txt
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificação de todo o domínio** | `Snaffler.exe -s -o out.log` |
| **Computador único de destino** | `Snaffler.exe -c <HOSTNAME> -s` |
| **Parte específica alvo** | `Snaffler.exe -i \\<HOST>\<SHARE> -s` |
| **Ajustar threads de simultaneidade**| `Snaffler.exe -s -t 120 -o out.log` |
| **Excluir host da verificação** | `Snaffler.exe -s -c- <EXCLUDE_HOST>` |
| **Limite máximo de tamanho de arquivo**| `Snaffler.exe -s -z 5000000` *(Limite a arquivos de 5MB)* |
| **Executar via montagem C2**| `execute-assembly Snaffler.exe -s` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Picos massivos de conexão SMB:** O Snaffler se conecta à porta 445 em cada máquina de domínio em poucos minutos. Isso gera milhares de ID de evento 4624 (Logon bem-sucedido tipo 3 - Rede) e ID de evento 5140 (Um objeto de compartilhamento de rede foi acessado).
2. **Honeyfiles e chamarizes:** Os defensores modernos do Active Directory implantam compartilhamentos Canary e iscam arquivos `.kdbx` que acionam alertas SOC instantâneos quando abertos por ferramentas como o Snaffler.
3. **Limites de execução:** Defina `-z` (tamanho máximo de bytes do arquivo) para evitar falhas nos compartilhamentos de rede ao ler arquivos de backup ISO ou VHD de vários gigabytes.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O Snaffler foi projetado exclusivamente para testes de penetração autorizados, compromissos de equipe vermelha e auditorias de segurança interna. A verificação de compartilhamentos de domínio sem autorização constitui invasão ilegal de rede.