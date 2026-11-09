<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# enum4linux-ng Cheatsheet

[enum4linux-ng](https://github.com/cddmp/enum4linux-ng) é uma reescrita de última geração da ferramenta herdada `enum4linux` escrita em Python 3. Ele realiza coleta abrangente de informações do Windows e Samba por meio de SMB (Server Message Block), NetBIOS, RPC (Remote Procedure Call) e LDAP. Ele extrai contas de usuários, associações de grupos, pastas compartilhadas, políticas de senha, informações do sistema operacional e ciclos RID.

---

## Índice

- [Principais melhorias em relação ao enum4linux legado](#key-improvements-over-legacy-enum4linux)
- [Instalação e Requisitos](#instalação--requisitos)
- [Enumeração básica de SMB e domínio](#basic-smb--domain-enumeration)
- [Modos de autenticação (sessão nula vs. usuário válido)](#authentication-modes-null-session-vs-valid-user)
- [Módulos de enumeração direcionados](#targeted-enumeration-modules)
- [RID Cycling e coleta de conta de usuário](#rid-cycling--user-account-harvesting)
- [Exportando relatórios (JSON, YAML, Texto)](#exporting-reports-json-yaml-text)
- [Endurecimento defensivo para SMB e RPC](#endurecimento defensivo para SMB--rpc)

---

## Principais melhorias em relação ao enum4linux legado

- **Mecanismo Python 3 moderno:** Execução multithread baseada em `impacket`, eliminando travamentos de scripts Perl antigos.
- **Detecção aprimorada de sistema operacional:** Detecção precisa do Windows Server (2012–2025), Windows 10/11 e versões modernas do Samba.
- **Relatórios estruturados:** Exportação nativa para JSON e YAML para integração perfeita de SIEM ou scripts.
**Ciclagem RID mais inteligente:** Ajustes de faixa flexíveis e detecção automática de RID de administrador/convidado.
- **Saída CLI colorida:** Indicadores visuais claros para configurações incorretas de segurança e sessões nulas.

---

## Instalação e requisitos
```bash
# Clone the repository
git clone https://github.com/cddmp/enum4linux-ng.git
cd enum4linux-ng

# Install Python dependencies (impacket)
pip3 install -r requirements.txt

# Or install globally as a symlink
sudo ln -sf "$(pwd)/enum4linux-ng.py" /usr/local/bin/enum4linux-ng

# Verify command
enum4linux-ng -h
```
---

## Enumeração básica de SMB e domínio

### 1. Execução de enumeração padrão (`-A`)
Execute todas as verificações de enumeração não intrusivas no destino:
```bash
enum4linux-ng -A 192.168.1.50
```
Este módulo consulta:
- Nomes NetBIOS e nomes de grupos de trabalho/domínios.
- Versão e arquitetura do sistema operacional.
- Diretórios compartilhados e permissões de acesso.
- Política de senha (limite de bloqueio, comprimento mínimo, histórico).
- Usuários e grupos locais via RPC/SAMR.

---

## Modos de autenticação (sessão nula vs. usuário válido)

### 1. Sessão anônima/nula (`-u '' -p ''`)
Teste se o destino permite conexões IPC$ não autenticadas:
```bash
enum4linux-ng -u "" -p "" 192.168.1.50
```
### 2. Domínio autenticado/conta local
Forneça credenciais válidas de baixo privilégio para expandir drasticamente os dados coletados:
```bash
enum4linux-ng -u "jdoe" -p "Password123!" 192.168.1.50
```
### 3. Forneça um nome de domínio específico (`-d`)
```bash
enum4linux-ng -u "jdoe" -p "Password123!" -d "CORP.LOCAL" 192.168.1.50
```
---

## Módulos de enumeração direcionados

Execute módulos específicos para minimizar o impacto na rede e evitar alertas de detecção de intrusão:

### 1. Pastas compartilhadas e permissões de acesso (`-S`)
Enumere compartilhamentos, comentários e verifique o acesso de leitura/gravação:
```bash
enum4linux-ng -S 192.168.1.50
```
### 2. Extração de política de senha (`-P`)
Identifique os limites de bloqueio de conta para planejar com segurança a pulverização de senhas:
```bash
enum4linux-ng -P 192.168.1.50
```
Principais resultados a observar:
- **Comprimento mínimo da senha:** por exemplo, 8 caracteres.
- **Limite de bloqueio de conta:** por exemplo, 5 tentativas (não pulverize mais rápido do que a janela de bloqueio!).
- **Redefinir contador de bloqueio:** por exemplo, 30 minutos.

### 3. Somente usuários e grupos (`-U` e `-G`)
```bash
# Enumerate user list
enum4linux-ng -U 192.168.1.50

# Enumerate domain and local groups
enum4linux-ng -G 192.168.1.50
```
### 4. Somente informações do sistema operacional e NetBIOS (`-O` e `-I`)
```bash
enum4linux-ng -O -I 192.168.1.50
```
---

## Ciclismo RID e coleta de contas de usuário

Quando a enumeração de usuários por meio de consultas RPC padrão for restrita, execute o ciclo do Identificador Relativo (RID) por meio de chamadas de consulta SAMR:

### 1. Ciclagem RID padrão (faixa 500 - 1100)
```bash
enum4linux-ng -R 192.168.1.50
```
### 2. Faixa RID personalizada (por exemplo, 500 a 5.000 para grandes organizações)
```bash
enum4linux-ng -r 500-5000 192.168.1.50
```
### 3. Extraia a lista de palavras do nome de usuário limpo
Filtre os nomes de usuário RID descobertos em um arquivo de texto para pulverização de senha:
```bash
enum4linux-ng -R 192.168.1.50 -oJ /tmp/enum_out
jq -r '.users[].username' /tmp/enum_out.json | sort -u > /tmp/users.txt
```
---

## Exportando relatórios (JSON, YAML, Texto)

Exporte os resultados da enumeração para documentação ou análise programática:

### 1. Exportar em todos os formatos simultaneamente (`-oA`)
```bash
enum4linux-ng -A 192.168.1.50 -oA /tmp/target_recon
```
Gera:
-`/tmp/target_recon.json`
-`/tmp/target_recon.yaml`
-`/tmp/target_recon.txt`

### 2. Exportar para formato específico
```bash
# JSON only
enum4linux-ng -A 192.168.1.50 -oJ /tmp/target.json

# YAML only
enum4linux-ng -A 192.168.1.50 -oY /tmp/target.yaml
```
---

## Endurecimento Defensivo para SMB e RPC

| Vetor | Risco | Recomendação de endurecimento |
|---|---|---|
| **Sessão Nula (IPC$ Anônimo)** | Reconhecimento de domínio não autenticado | Defina `RestrictNullSessAccess=1` no registro do Windows; configure `RestrictAnonymous=2` no SAM. |
| **SMBv1 ativo** | Ataques EternalBlue (MS17-010) e MITM | Desative completamente o SMBv1 em todos os endpoints e controladores de domínio. |
| **Permissões de compartilhamento de texto simples** | Arquivos confidenciais e credenciais expostas | Permissões de compartilhamento de auditoria; impor enumeração baseada em acesso (ABE). |
| **Enumeração de ciclismo RID** | Extração completa da lista de contas de usuário | Restrinja a enumeração de contas SAM anônimas por meio da Política de Segurança Local. |