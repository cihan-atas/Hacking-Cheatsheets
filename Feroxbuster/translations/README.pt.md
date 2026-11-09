<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 Feroxbuster - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗███████╗██████╗  ██████╗ ██╗  ██╗██████╗ ██╗   ██╗███████╗████████╗███████╗██████╗ 
  ██╔════╝██╔════╝██╔══██╗██╔═══██╗╚██╗██╔╝██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗
  █████╗  █████╗  ██████╔╝██║   ██║ ╚███╔╝ ██████╔╝██║   ██║███████╗   ██║   █████╗  ██████╔╝
  ██╔══╝  ██╔══╝  ██╔══██╗██║   ██║ ██╔██╗ ██╔══██╗██║   ██║╚════██║   ██║   ██╔══╝  ██╔══██╗
  ██║     ███████╗██║  ██║╚██████╔╝██╔╝ ██╗██████╔╝╚██████╔╝███████║   ██║   ███████╗██║  ██║
  ╚═╝     ╚══════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝╚═════╝  ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝╚═╝  ╚═╝
                Fast, Simple, Recursive Content Discovery
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Feroxbuster-Content_Discovery-orange?style=for-the-badge" alt="Feroxbuster">
  <img src="https://img.shields.io/badge/Rust-Blazingly_Fast-blue?style=for-the-badge" alt="Rust">
  <img src="https://img.shields.io/badge/Auto_Tune-Recursive-green?style=for-the-badge" alt="Recursive">
  <img src="https://img.shields.io/badge/Bug_Bounty-Web_Pentest-red?style=for-the-badge" alt="Web Pentest">
</p>

<p alinhar="centro">
  <b>⚡ Uma ferramenta de descoberta de conteúdo rápida, simples e recursiva escrita em Rust</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Recursos principais](#-recursos-chave)
- [Comparação de ferramentas](#-comparação de ferramentas)
- [Instalação](#-instalação)
- [Sintaxe básica e início rápido](#-sintaxe básica--início rápido)
- [Opções de comando principais](#-core-command-options)
  - [Controle de recursão](#controle de recursão)
  - [Simultaneidade e desempenho](#simultaneidade--desempenho)
  - [Extensões de arquivo](#extensões de arquivo)
  - [Filtragem e correspondência](#filtering--matching)
  - [Recursos inteligentes (ajuste automático, extração de link, backups)](#smart-features)
  - [Cabeçalhos, Cookies e Autenticação](#cabeçalhos-cookies--auth)
  - [Integração com Proxy e Burp Suite](#proxy--burp-suite-integration)
- [Verificação multi-alvo e pipelines](#-varredura multi-alvo--pipelines)
- [Fluxos de trabalho do mundo real](#-fluxos de trabalho do mundo real)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução

**Feroxbuster** é um scanner de conteúdo da web recursivo, multithread e de alto desempenho escrito em Rust. Projetado para ser rápido, simples e intuitivo, ele combina a velocidade dos fuzzers baseados em Go/Rust com descoberta de diretório recursiva automática nativa.

Ele brilha durante testes de penetração e engajamentos de recompensas de bugs, onde árvores profundas de sites e diretórios ocultos devem ser descobertos rapidamente, sem gerar scripts manuais complexos.

---

## 🌟 Principais recursos

- ⚡ **Extremamente rápido:** Alimentado por Rust assíncrono (`tokio` e `reqwest`), capaz de milhares de solicitações por segundo.
- 🔄 **Descoberta recursiva nativa:** descobre automaticamente novos diretórios e os verifica recursivamente até limites de profundidade configuráveis.
- 🧠 **Auto-Tune e Auto-Bail:** Acelera automaticamente a velocidade de verificação se o servidor web de destino ficar lento ou aborta se ocorrerem erros excessivos de conexão.
- 🔗 **Extração de link em linha:** analisa respostas HTML e JavaScript para descobrir pontos de extremidade ocultos e os envia de volta para a fila de verificação.
- 📦 **Busca de backup automatizada:** Acrescenta variações `.bak`, `~`, `.old` e `.orig` automaticamente aos arquivos descobertos.
- 🎯 **Entrada Pipeable:** Aceita perfeitamente listas de alvos de `stdin` (de ferramentas como `subfinder`, `httpx` ou `gau`).

---

## ⚖️ Comparação de ferramentas

| Recurso | Feroxbuster | affu | pesquisa direta | Gobuster | Dirb |
|---------|-------------|------|-----------|----------|------|
| **Idioma** | Ferrugem | Vá | Pitão | Vá | C |
| **Velocidade** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐ |
| **Recursão padrão** | ✅ Automático | ❌ Sinalizador manual | ✅ Automático (`-r`) | ❌ Nenhum | ✅ Automático |
| **Extração de link** | ✅ Sim (`--extract-links`) | ❌ Não | ❌ Não | ❌ Não | ❌ Não |
| **Ajuste automático de taxa** | ✅ Sim (`--auto-tune`) | ❌ Não | ❌ Não | ❌ Não | ❌ Não |
| **PÓS Fuzzing** | ❌ Não | ✅ Nativo | ✅ Nativo (`--data`) | ❌ Não | ❌ Não |
| **Stdin multialvo** | ✅ Nativo | ❌ Limitado | ✅ Sim (`-l`) | ❌ Não | ❌ Não |
| **Detecção de arquivo de backup**| ✅ Nativo (`--collect-backups`)| ❌ Não | ✅ Nativo (`-e *`) | ❌ Não | ❌ Não |

---

## 📥 Instalação

###Kali Linux e Debian/Ubuntu
```bash
# Update repository and install
sudo apt update && sudo apt install -y feroxbuster
```
### Usando Cargo (Gerenciador de Pacotes Rust)
```bash
cargo install feroxbuster
```
### macOS (Homebrew)
```bash
brew install feroxbuster
```
### Docker
```bash
docker run --init -it epi052/feroxbuster -u https://target.com -w /usr/share/seclists/Discovery/Web-Content/common.txt
```
### Binário pré-compilado (versões do GitHub)
```bash
# Download latest Linux release
curl -sL https://raw.githubusercontent.com/epi052/feroxbuster/main/install-nix.sh | bash
sudo mv feroxbuster /usr/local/bin/
```
---

## 🚀 Sintaxe básica e início rápido
```bash
feroxbuster -u <TARGET_URL> -w <WORDLIST> [OPTIONS]
```
### Exemplos de verificação rápida
```bash
# Scan a single target with default settings
feroxbuster -u http://10.10.10.10

# Scan with custom SecLists directory list
feroxbuster -u https://example.com -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt

# Scan target including specific file extensions
feroxbuster -u https://example.com -w /usr/share/wordlists/dirb/common.txt -x php,html,txt,json
```
---

## ⚙️ Opções de comando principais

### Controle de recursão
```bash
# Specify maximum recursion depth (default is 4)
feroxbuster -u https://example.com -w common.txt -d 2

# Completely disable recursion (scan root directory only)
feroxbuster -u https://example.com -w common.txt -n

# Don't scan nested subdirectories automatically (interactive prompt)
feroxbuster -u https://example.com -w common.txt --dont-scan /api,/static
```
### Simultaneidade e desempenho
```bash
# Set number of concurrent threads per directory (default: 50)
feroxbuster -u https://example.com -t 100

# Limit total concurrent directory scans running simultaneously (default: unlimited)
feroxbuster -u https://example.com -L 3

# Rate limit requests per second to avoid triggering WAF/firewall blocks
feroxbuster -u https://example.com --rate-limit 150

# Request timeout in seconds (default: 7)
feroxbuster -u https://example.com -T 5
```
### Extensões de arquivo
```bash
# Append extensions to each wordlist item
feroxbuster -u https://example.com -x php,asp,aspx,jsp,txt,bak

# Automatically collect and fuzz file extensions found during crawling
feroxbuster -u https://example.com --collect-extensions
```
### Filtragem e correspondência
```bash
# Match only specific status codes (e.g., 200 OK and 301 Redirects)
feroxbuster -u https://example.com -s 200 301 302

# Filter out specific status codes (e.g., exclude 404 and 403)
feroxbuster -u https://example.com -C 404 403 500

# Filter responses matching exact size in bytes
feroxbuster -u https://example.com -S 4092 1024

# Filter responses matching exact word count
feroxbuster -u https://example.com -W 42

# Filter responses matching exact line count
feroxbuster -u https://example.com -N 12

# Filter responses matching regular expressions (e.g., generic error pages)
feroxbuster -u https://example.com --filter-regex "Under Construction|Page Not Found"

# Filter responses similar to a known 404/wildcard page
feroxbuster -u https://example.com --filter-similar-to https://example.com/definitelynotfoundpage404
```
### Recursos inteligentes
```bash
# Auto-tune: Automatically adjusts request concurrency based on server response latency
feroxbuster -u https://example.com --auto-tune

# Auto-bail: Aborts scanning automatically when more than 100 consecutive network errors occur
feroxbuster -u https://example.com --auto-bail

# Extract links: Parses HTML and JavaScript to automatically uncover endpoints
feroxbuster -u https://example.com --extract-links

# Collect backups: Automatically tests for backup copies (.bak, ~, .old) of every discovered file
feroxbuster -u https://example.com --collect-backups
```
### Cabeçalhos, Cookies e Autenticação
```bash
# Custom HTTP Header
feroxbuster -u https://example.com -H "Authorization: Bearer <TOKEN>" -H "X-Forwarded-For: 127.0.0.1"

# Session Cookie
feroxbuster -u https://example.com -b "session=abc123xyz; role=admin"

# Custom User-Agent string
feroxbuster -u https://example.com -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"

# Ignore invalid / self-signed TLS certificates
feroxbuster -u https://10.10.10.10 -k
```
### Integração com proxy e Burp Suite
```bash
# Route all requests through Burp Suite proxy
feroxbuster -u https://example.com -p http://127.0.0.1:8080 -k

# Convenience shortcut flag for Burp Suite (enables -k and sets proxy to localhost:8080)
feroxbuster -u https://example.com --burp
```
---

## 🔁 Varredura e pipelines multialvo

O Feroxbuster lê nativamente URLs de destino a partir da entrada padrão (`stdin`), tornando-o ideal para integração com pipelines de descoberta de ativos:
```bash
# Pipeline: Subdomain discovery -> HTTP Probing -> Feroxbuster Fuzzing
subfinder -d example.com -silent | \
  httpx -silent -mc 200,301,302 | \
  feroxbuster --stdin -w /usr/share/seclists/Discovery/Web-Content/raft-medium-directories.txt -t 30 -L 4
```
### Verificando URLs de um arquivo
```bash
cat targets.txt | feroxbuster --stdin -w common.txt -v
```
---

## 🎬 Fluxos de trabalho do mundo real

### Fluxo de trabalho 1: Reconhecimento abrangente de recompensas de bugs na Web

Alta simultaneidade, ajuste automático de taxa, extração de link e detecção de backup:
```bash
feroxbuster -u https://target.com \
  -w /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt \
  -x php,js,json,txt,html \
  --extract-links \
  --collect-backups \
  --auto-tune \
  -o ferox_target_scan.txt
```
### Fluxo de trabalho 2: Descoberta interna do portal corporativo/Active Directory

Digitalizando um aplicativo web interno via Burp Suite com autenticação e certificados autoassinados:
```bash
feroxbuster -u https://internal-portal.corp.local \
  -w /usr/share/seclists/Discovery/Web-Content/common.txt \
  -x aspx,ashx,asmx,config,txt \
  -b "ASP.NET_SessionId=k2m9fl3p8..." \
  --burp \
  -d 3
```
### Fluxo de trabalho 3: API rápida e não recursiva e descoberta de endpoint
```bash
feroxbuster -u https://api.target.com/v1 \
  -w /usr/share/seclists/Discovery/Web-Content/api/api-endpoints.txt \
  -n \
  -s 200,201,204,401,403 \
  -t 100 \
  --json \
  -o api_results.json
```
---

## 📊 Tabela de referência rápida

| Bandeira | Curto | Descrição | Exemplo |
|------|-------|-------------|---------|
| `--url` | `-u` | URL de destino para verificação | `feroxbuster -u https://target.com` |
| `--wordlist` | `-w` | Caminho para a lista de palavras | `feroxbuster -w /path/to/list.txt` |
| `--threads` | `-t` | Número de threads simultâneos por diretório | `feroxbuster -t 80` |
| `--scan-limit` | `-L` | Limitar o total de varreduras de diretório simultâneas | `feroxbuster -L 3` |
| `--depth` | `-d` | Profundidade máxima de recursão (padrão 4) | `feroxbuster -d 2` |
| `--no-recursion` | `-n` | Desative a verificação recursiva de diretórios | `feroxbuster -n` |
| `--extensions` | `-x` | Extensões de arquivo para anexar | `feroxbuster -x php,html,bak` |
| `--status-codes` | `-s` | Códigos de status a incluir | `feroxbuster -s 200 301 302` |
| `--filter-status` | `-C` | Códigos de status a serem excluídos | `feroxbuster -C 404 500` |
| `--filter-size` | `-S` | Tamanhos de resposta a serem excluídos | `feroxbuster -S 4096` |
| `--filter-words` | `-W` | Contagens de palavras a serem excluídas | `feroxbuster -W 15` |
| `--filter-lines` | `-N` | Contagens de linhas a serem excluídas | `feroxbuster -N 8` |
| `--auto-tune` | Nenhum | Ajuste dinamicamente a velocidade com base na latência | `feroxbuster --auto-tune` |
| `--auto-bail` | Nenhum | Anular verificação em caso de erros excessivos de rede | `feroxbuster --auto-bail` |
| `--extract-links`| Nenhum | Analise HTML/JS para novos endpoints | `feroxbuster --extract-links` |
| `--collect-backups` | Nenhum | Verifique se há variações de backup (`.bak`, `.old`) | `feroxbuster --collect-backups` |
| `--proxy` | `-p` | Enviar tráfego através de proxy HTTP | `feroxbuster -p http://127.0.0.1:8080` |
| `--burp` | Nenhum | Atalho para `-p http://127.0.0.1:8080 -k` | `feroxbuster --burp` |
| `--stdin` | Nenhum | Leia URLs da entrada padrão | `cat urls.txt \| feroxbuster --stdin` |
| `--output` | `-o` | Caminho do arquivo de saída | `feroxbuster -o results.txt` |
| `--json` | Nenhum | Formatar saída como JSON | `feroxbuster --json -o out.json` |

---

## 💡 Dicas e práticas recomendadas

1. **Use `--auto-tune` para alvos remotos:**
   - Em vez de adivinhar contagens de threads (`-t`), o `--auto-tune` mede ativamente os tempos de resposta alvo e acelera ou acelera dinamicamente, evitando a negação acidental de serviço do servidor.
2. **Combine `--extract-links` e `--collect-backups`:**
   - Esta combinação é excepcionalmente letal em avaliações de recompensas de bugs: Feroxbuster extrai rotas JS ocultas, verifica-as e verifica imediatamente se os backups do desenvolvedor (`.bak`, `.swp`, `~`) foram deixados no servidor.
3. **Limitar verificações em listas de palavras grandes com `-L`:**
   - Se estiver usando um dicionário com mais de 200.000 palavras, a recursão irrestrita pode lançar dezenas de threads simultâneos. Use `-L 2` ou `-L 3` para manter a memória e a largura de banda dentro dos limites operacionais seguros.
4. **Use arquivos de estado para retomar grandes auditorias:**
   - Feroxbuster salva periodicamente arquivos de estado (`.state`). Se ocorrer uma varredura interrompida, retome instantaneamente com `feroxbuster --resume-from <FILE>.state`.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** O Feroxbuster foi desenvolvido estritamente para avaliações de segurança autorizadas, competições CTF e testes de penetração realizados com permissão por escrito. A verificação de alvos não autorizados pode saturar a infraestrutura da rede e é estritamente proibida por lei.

---

## 📚 Recursos

- [Repositório GitHub Feroxbuster](https://github.com/epi052/feroxbuster)
- [Documentação oficial do Feroxbuster](https://epi052.github.io/feroxbuster-docs/)
- [Listas de descoberta de SecLists](https://github.com/danielmiessler/SecLists)