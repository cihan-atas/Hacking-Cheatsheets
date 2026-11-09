<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔍 dirsearch - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ██╗██████╗ ███████╗███████╗ █████╗ ██████╗  ██████╗██╗  ██╗
  ██╔══██╗██║██╔══██╗██╔════╝██╔════╝██╔══██╗██╔══██╗██╔════╝██║  ██║
  ██║  ██║██║██████╔╝███████╗█████╗  ███████║██████╔╝██║     ███████║
  ██║  ██║██║██╔══██╗╚════██║██╔══╝  ██╔══██║██╔══██╗██║     ██╔══██║
  ██████╔╝██║██║  ██║███████║███████╗██║  ██║██║  ██║╚██████╗██║  ██║
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝
       Advanced Web Path Scanner & Directory Brute-Forcer
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/dirsearch-Web_Bruteforcer-blue?style=for-the-badge" alt="dirsearch">
  <img src="https://img.shields.io/badge/Language-Python_Async-green?style=for-the-badge" alt="Python Async">
  <img src="https://img.shields.io/badge/Recon-Web_Discovery-orange?style=for-the-badge" alt="Descoberta na Web">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Verificador de caminho da web de linha de comando rápido e repleto de recursos, com manipulação inteligente de extensões, recursão e rastreamento</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Comparação de ferramentas](#-comparação de ferramentas)
- [Instalação](#-instalação)
- [Uso básico e seleção de alvo](#-basic-usage--target-selection)
- [Gerenciamento de extensões e sufixos](#-extensão--gerenciamento de sufixos)
- [Listas de palavras e dicionários](#-listas de palavras--dicionários)
- [Recursão e rastreamento da Web](#-recursão--rastreamento da Web)
- [Threading, limitação de taxa e atrasos](#-threading-rate-limiting--delays)
- [Filtragem e respostas correspondentes](#-filtering--matching-responses)
- [Personalização de solicitação HTTP](#-http-request-customization)
  - [Cabeçalhos personalizados e agentes de usuário](#cabeçalhos personalizados--agentes de usuário)
  - [Cookies e autenticação](#cookies--autenticação)
  - [Dados POST e métodos de solicitação](#post-data--request-methods)
- [Proxies, Tor e Evasão](#-proxies-tor--evasão)
- [Formatos de saída e relatórios](#-formatos de saída--relatórios)
- [Receitas de Pentest e Bug Bounty do mundo real] (#-real-world-pentest--bug-bounty-recipes)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Introdução

**dirsearch** (criado por Mauro Soria) é uma das ferramentas de linha de comando mais populares e versáteis para descobrir arquivos e diretórios ocultos em servidores web. Escrito em Python, ele oferece flexibilidade incomparável com seus ricos argumentos de linha de comando, saída de terminal colorida, rastreamento recursivo automático, expansão inteligente de extensão curinga e controles de evasão integrados.

Esteja você realizando testes de penetração na web, caça a recompensas de bugs ou avaliações internas de CTF, o `dirsearch` é personalizado para descobrir interfaces administrativas ocultas, bancos de dados de backup, endpoints de API esquecidos e credenciais confidenciais.

### Principais recursos

| Recurso | Descrição |
|--------|-------------|
| **Multi-threaded** | Mecanismo de solicitação assíncrona e encadeada de alto desempenho |
| **Cartões curinga de extensão** | Expanda `-e *` para testar automaticamente todas as extensões de arquivo da web conhecidas |
| **Rastreamento integrado** | Segue links HTML/JS para propagar filas de diretório recursivas |
| **Filtragem rica** | Filtre as respostas por código de status, tamanho de bytes, contagem de palavras, contagem de linhas ou regex |
| **Repetição de solicitação bruta** | Carregar solicitações HTTP completas do Burp Suite via `--raw` |
| **Vários formatos** | Gera relatórios em texto simples, JSON, CSV, Markdown e HTML |

---

## ⚖️ Comparação de ferramentas

| Recurso | pesquisa direta | affu | Feroxbuster | Gobuster | DIRB |
|---------|-----------|------|------------|----------|------|
| **Idioma** | Pitão | Vá | Ferrugem | Vá | C |
| **Velocidade** | ⭐⭐⭐⭐ (Rápido) | ⭐⭐⭐⭐⭐ (Extremamente rápido) | ⭐⭐⭐⭐⭐ (Extremamente rápido) | ⭐⭐⭐⭐ (Rápido) | ⭐⭐ (Fio baixo) |
| **Recursão padrão** | ✅ Automático (`-r`) | Opcional (`-recursion`) | ✅ Automático (`-d`) | Opcional (`-r`) | ✅ Automático |
| **Cartão curinga de extensão (`*`)**| ✅ Nativo (`-e *`) | ❌ Não | ❌ Não | ❌ Não | ❌ Não |
| **Rastreador da Web integrado** | ✅ Sim (`--crawl`) | ❌ Não | ✅ Sim (`--extract-links`) | ❌ Não | ❌ Não |
| **POST/difusão de dados** | ✅ Sim (`--data`) | ✅ HTTP completo | ❌ Limitado | ❌ Não | ❌ Não |
| **Reprodução de solicitação bruta do Burp**| ✅ Sim (`--raw`) | ✅ Sim (`-request`) | ❌ Não | ❌ Não | ❌ Não |
| **Lista de palavras integrada** | ✅ Curadoria (`db/dicc.txt`)| ❌ Nenhum | ❌ Nenhum | ❌ Nenhum | ✅ Lista pequena |

---

## 📥 Instalação

### Usando pipx (recomendado para Kali e Linux moderno)
```bash
# Install via pipx (isolated Python environment)
pipx install dirsearch

# Verify installation
dirsearch --version
```
### Usando pip
```bash
# Install via standard pip
pip3 install dirsearch
```
### Usando Git Clone (direto da fonte)
```bash
# Clone the official repository
git clone https://github.com/maurosoria/dirsearch.git --depth 1
cd dirsearch

# Install requirements and run
pip3 install -r requirements.txt
python3 dirsearch.py --version
```
### Pacote Kali Linux e Docker
```bash
# Kali / Debian repository
sudo apt update && sudo apt install -y dirsearch

# Docker container
docker run -it --rm maurosoria/dirsearch -u https://example.com -e php,html
```
---

## 🚀 Uso básico e seleção de alvo

### Alvo único
```bash
# Basic scan against a single URL specifying extensions
dirsearch -u https://target.com -e php,html,js,txt

# Scan with trailing slash forced
dirsearch -u https://target.com -e php -f
```
### Vários alvos
```bash
# Scan a list of targets from file
dirsearch -l targets.txt -e php,aspx,txt

# Scan stdin input from subfinder / httpx
cat live_hosts.txt | dirsearch --stdin -e php
```
### Verificação de portas e segmentação CIDR
```bash
# Scan specific ports on a host
dirsearch -u https://target.com --ports 80,443,8080,8443 -e php

# Scan an entire subnet range
dirsearch --cidr 192.168.1.0/24 -e html
```
---

## 🧩 Gerenciamento de extensões e sufixos

`dirsearch` possui o sistema de gerenciamento de extensões mais poderoso entre todos os scanners de diretório:
```bash
# Specify multiple extensions
dirsearch -u https://target.com -e php,html,json,txt,zip

# Use the wildcard (*) to test ALL known extensions automatically
dirsearch -u https://target.com -e *

# Add prefixes to wordlist entries (e.g. test_admin, dev_admin)
dirsearch -u https://target.com -e php --prefixes test_,dev_,staging_,old_

# Add suffixes to wordlist entries (e.g. admin_backup, admin_old)
dirsearch -u https://target.com -e php --suffixes _bak,~,_2026,_old
```
---

## 📚 Listas de palavras e dicionários

`dirsearch` reúne uma lista de palavras abrangente e testada em batalha em `db/dicc.txt`:
```bash
# Run using the default bundled dictionary
dirsearch -u https://target.com -e php

# Use SecLists Common or Medium wordlists
dirsearch -u https://target.com -e php -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Use multiple wordlists simultaneously
dirsearch -u https://target.com -e php -w list1.txt,list2.txt

# Force all wordlist entries to lowercase or uppercase
dirsearch -u https://target.com -e php --lowercase
dirsearch -u https://target.com -e php --uppercase
```
---

## 🔄 Recursão e rastreamento da Web

### Verificação recursiva
```bash
# Recursively scan newly found directories
dirsearch -u https://target.com -e php -r

# Limit recursion depth to 2 levels
dirsearch -u https://target.com -e php -r --recursion-depth 2

# Recursively scan only if HTTP status is 200, 301, or 302
dirsearch -u https://target.com -e php -r --recursion-status 200,301,302
```
### Rastreador da Web integrado
```bash
# Crawl the target website for internal URLs and feed them into the scan queue
dirsearch -u https://target.com -e php --crawl
```
---

## ⚡ Segmentação, limitação de taxa e atrasos

Controle a agressividade da verificação para evitar o acionamento de limitação de taxa ou Web Application Firewalls (WAF):
```bash
# Set number of concurrent threads (default: 25)
dirsearch -u https://target.com -e php -t 50

# Add a delay between requests (e.g. 0.5 seconds)
dirsearch -u https://target.com -e php -s 0.5

# Enforce a maximum request rate per second (e.g. max 15 req/sec)
dirsearch -u https://target.com -e php --max-rate 15

# Customize HTTP connection timeouts and retries
dirsearch -u https://target.com -e php --timeout 8 --retries 3

# Exit scan immediately on excessive connection errors
dirsearch -u https://target.com -e php --exit-on-error
```
---

## 🎯 Filtragem e correspondência de respostas

### Inclusão e exclusão de código de status
```bash
# Include only specific status codes (e.g., ignore everything except 200, 301, 403)
dirsearch -u https://target.com -e php -i 200,301,302,403

# Exclude specific status codes (e.g., exclude 404 and 500)
dirsearch -u https://target.com -e php -x 404,400,500,502,503
```
### Filtragem por tamanho, palavras e conteúdo
```bash
# Exclude responses by specific byte size (e.g. ignore custom 404 pages)
dirsearch -u https://target.com -e php --exclude-sizes 0B,1542B,3421B

# Exclude responses matching specific text on the page
dirsearch -u https://target.com -e php --exclude-texts "Not Found","Access Denied"

# Exclude responses matching a regular expression
dirsearch -u https://target.com -e php --exclude-regex "^Error:.*"
```
---

## 🛠️ Personalização de solicitação HTTP

### Cabeçalhos personalizados e agentes de usuário
```bash
# Set a custom header (e.g., authorization token or internal bypass header)
dirsearch -u https://target.com -e php -H "X-Forwarded-For: 127.0.0.1" -H "X-Requested-With: XMLHttpRequest"

# Load headers from a file
dirsearch -u https://target.com -e php --header-list custom_headers.txt

# Use a randomized User-Agent per request to bypass basic WAFs
dirsearch -u https://target.com -e php --random-agent

# Set a custom User-Agent
dirsearch -u https://target.com -e php --user-agent "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
```
### Cookies e autenticação
```bash
# Provide session cookies for authenticated scanning
dirsearch -u https://target.com -e php -b "PHPSESSID=abc123xyz; admin_logged=true"

# HTTP Basic Authentication (username:password)
dirsearch -u https://target.com -e php --auth admin:SecretPass123 --auth-type basic

# NTLM Authentication for corporate Windows portals
dirsearch -u https://target.com -e php --auth CORP\\alice:Welcome2026 --auth-type ntlm
```
### Dados POST e métodos de solicitação
```bash
# Perform fuzzing using HTTP POST requests
dirsearch -u https://target.com/api -m POST -d "action=test&token=xyz" -e json

# Replay an entire raw HTTP request saved from Burp Suite
dirsearch -u https://target.com --raw request.txt -e php
```
---

## 🛡️ Proxies, Tor e Evasão
```bash
# Route all traffic through an HTTP or SOCKS5 proxy (e.g., Burp Suite)
dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080

# Route all traffic through the local Tor SOCKS proxy
dirsearch -u https://target.com -e php --tor

# Skip SSL/TLS certificate verification
dirsearch -u https://target.com -e php --no-check-certificate
```
---

## 📄 Formatos de saída e relatórios
```bash
# Save output as a clean text file
dirsearch -u https://target.com -e php -o results.txt

# Export results to JSON format
dirsearch -u https://target.com -e php -o results.json --format json

# Export results to HTML, CSV, or Markdown
dirsearch -u https://target.com -e php -o report.html --format html
dirsearch -u https://target.com -e php -o results.csv --format csv
dirsearch -u https://target.com -e php -o report.md --format markdown
```
---

## 🎯 Receitas de Pentest e Bug Bounty do mundo real

### 1. Reconhecimento rápido e completo da Web
```bash
# Scan with common web extensions, follow redirects, random UA, 40 threads
dirsearch -u https://target.com \
  -e php,html,js,json,txt,zip \
  -t 40 \
  --random-agent \
  -i 200,301,302,403 \
  -o target_recon.txt
```
### 2. Caça a backups esquecidos e vazamentos de fontes
```bash
# Test for database dumps, git repos, env files, and zip archives
dirsearch -u https://target.com \
  -e bak,old,orig,zip,tar.gz,sql,env,git \
  -w /usr/share/seclists/Discovery/Web-Content/raft-medium-files.txt \
  -i 200 \
  --exclude-sizes 0B
```
### 3. Descoberta recursiva profunda de diretório
```bash
# 2-level recursion, crawling pages for new paths, ignoring error status codes
dirsearch -u https://target.com \
  -e php,html \
  -r --recursion-depth 2 \
  --crawl \
  -x 404,500,502 \
  -o deep_scan.json --format json
```
### 4. Avaliação do Portal Interno Autenticado
```bash
# Scan through Burp Suite with internal session cookies and WAF bypass headers
dirsearch -u https://intranet.corp.local \
  -e aspx,ashx,config,txt \
  -b "ASP.NET_SessionId=xyz123; AuthToken=ey..." \
  -H "X-Forwarded-For: 127.0.0.1" \
  --proxy http://127.0.0.1:8080
```
---

## ⚡ Tabela de referência rápida

| Tarefa | Comando |
|------|---------|
| **Verificação básica com extensões** | `dirsearch -u https://target.com -e php,html,txt` |
| **Todas as extensões curinga** | `dirsearch -u https://target.com -e *` |
| **Lista de palavras personalizada** | `dirsearch -u https://target.com -e php -w /path/to/wordlist.txt` |
| **Verificar vários alvos** | `dirsearch -l targets.txt -e php,aspx` |
| **Descoberta recursiva** | `dirsearch -u https://target.com -e php -r --recursion-depth 2` |
| **Rastrear links de destino** | `dirsearch -u https://target.com -e php --crawl` |
| **Incluir códigos de status** | `dirsearch -u https://target.com -e php -i 200,301,302,403` |
| **Excluir códigos de status** | `dirsearch -u https://target.com -e php -x 404,500,502` |
| **Excluir tamanhos de falsos positivos**| `dirsearch -u https://target.com -e php --exclude-sizes 0B,1240B` |
| **Autenticar com cookies** | `dirsearch -u https://target.com -e php -b "session=abc123"` |
| **Percurso pela Burp Suite** | `dirsearch -u https://target.com -e php --proxy http://127.0.0.1:8080` |
| **Salvar relatório em JSON** | `dirsearch -u https://target.com -e php -o report.json --format json` |

---

## 💡 Dicas e práticas recomendadas

1. **Use `-e *` com moderação em listas de palavras grandes:** A expansão da extensão curinga multiplica cada palavra por mais de 20 extensões. Use `-e *` apenas com listas menores como `common.txt` ou `dicc.txt`.
2. **Filtrar tamanhos de falsos positivos:** Estruturas web modernas retornam páginas 404 personalizadas com HTTP 200. Inspecione o tamanho da resposta padrão e adicione `--exclude-sizes <size>` para eliminar ruído.
3. **Sempre verifique caminhos proibidos 403:** Em ambientes corporativos e de nuvem modernos, as respostas HTTP 403 indicam que o diretório ou endpoint existe. Documente todos os 403s para desvio de cabeçalho e teste de passagem de caminho.
4. **Aproveite prefixos e sufixos para backups:** Os desenvolvedores frequentemente salvam cópias de backup de arquivos confidenciais (por exemplo, `config.php.bak`, `old_database.sql`). Use `--prefixes` e `--suffixes` para descobri-los automaticamente.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** Varredura não autorizada de portas, força bruta de diretório e enumeração da Web em servidores sem permissão explícita e por escrito são ilegais. Use o `dirsearch` estritamente em compromissos legais de recompensa por bugs, testes de penetração autorizados e ambientes de laboratório CTF.