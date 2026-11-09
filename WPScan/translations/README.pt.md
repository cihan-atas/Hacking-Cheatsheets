<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ WPScan - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██╗    ██╗██████╗ ███████╗ ██████╗ █████╗ ███╗   ██╗
  ██║    ██║██╔══██╗██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██║ █╗ ██║██████╔╝███████╗██║     ███████║██╔██╗ ██║
  ██║███╗██║██╔═══╝ ╚════██║██║     ██╔══██║██║╚██╗██║
  ╚███╔███╔╝██║     ███████║╚██████╗██║  ██║██║ ╚████║
   ╚══╝╚══╝ ╚═╝     ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The WordPress Security & Vulnerability Scanner
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/WPScan-WordPress_Security-blue?style=for-the-badge" alt="WPScan">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Segurança da Web">
  <img src="https://img.shields.io/badge/Database-WPVDB_API-green?style=for-the-badge" alt="API WPVDB">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licença GPLv3">
</p>

<p alinhar="centro">
  <b>🌐 Scanner de vulnerabilidade de caixa preta projetado para detectar vulnerabilidades de núcleo, plugin, tema e usuário do WordPress</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Instalação](#-instalação)
- [Integração de token de API (WPVDB)](#-api-token-integration-wpvdb)
- [Verificação básica e enumeração de alvo](#-basic-scan--target-enumeration)
- [Detalhamento dos sinalizadores de enumeração (`-e`)](#-enumeration-flags-breakdown--e)
  - [Plugins vulneráveis (`-e vp`)](#plugins-vulneráveis--e-vp)
  - [Todos os plug-ins (`-e ap`)](#all-plugins--e-ap)
  - [Temas (`-e vt`, `-e at`)](#temas--e-vt--e-at)
  - [Nomes de usuários (`-e u`)](#nomes de usuários--e-u)
  - [Backups sensíveis (`-e cb`, `-e dbe`)](#backups sensíveis--e-cb--e-dbe)
- [Modos de detecção (`--plugins-detection`)](#-modos de detecção---detecção de plugins)
- [Forçamento bruto de senha (`wp-login.php` e XML-RPC)](#-password-brute-forcing-wp-loginphp--xml-rpc)
- [Evasão furtiva e WAF](#-stealth--waf-evasão)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Endurecimento](#-considerações defensivas--endurecimento)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

Com o WordPress alimentando mais de 40% dos 10 milhões de sites principais, é o Sistema de Gerenciamento de Conteúdo (CMS) mais direcionado do mundo.

Escrito em Ruby, **WPScan** é o scanner de vulnerabilidade de caixa preta padrão da indústria para instalações do WordPress. Ele cataloga versões principais ativas, enumera plug-ins e temas instalados, extrai nomes de usuários administrativos registrados e os correlaciona com o **WPScan Vulnerability Database (WPVDB)** contendo milhares de CVEs conhecidos.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wpscan

# Arch Linux
sudo pacman -S wpscan

# Ruby Gem (Any platform)
gem install wpscan

# Docker Container
docker run -it --rm wpscan/wpscan --url https://target.local
```
---

## 🔑 Integração de token de API (WPVDB)

Para exibir vulnerabilidades conhecidas e pontuações CVE, o WPScan requer um token de API gratuito de [wpscan.com](https://wpscan.com/):
```bash
# Provide API token via CLI
wpscan --url https://target.local --api-token YOUR_API_TOKEN

# Or export to environment variable
export WPSCAN_API_TOKEN="YOUR_API_TOKEN"
```
---

## 🚀 Varredura básica e enumeração de alvo
```bash
# Quick non-intrusive scan
wpscan --url https://target.local

# Update the local database before scanning
wpscan --update
```
---

## 🔍 Detalhamento dos sinalizadores de enumeração (`-e`)

WPScan permite enumeração modular de alvos usando o sinalizador `--enumerate` (ou `-e`):

### Plug-ins Vulneráveis (`-e vp`)
Verifica plug-ins que apresentam vulnerabilidades de segurança conhecidas no banco de dados WPVDB:
```bash
wpscan --url https://target.local -e vp --api-token YOUR_TOKEN
```
### Todos os plug-ins (`-e ap`)
Investiga milhares de plug-ins conhecidos por meio de força bruta de dicionário:
```bash
wpscan --url https://target.local -e ap --plugins-detection aggressive
```
### Temas (`-e vt`, `-e at`)
```bash
# Enumerate vulnerable themes
wpscan --url https://target.local -e vt

# Enumerate all installed themes
wpscan --url https://target.local -e at
```
### Nomes de usuário (`-e u`)
Extrai nomes de usuários de autores registrados por meio de endpoints da API REST (`/wp-json/wp/v2/users`), arquivos de autores (`/?author=1`) e mensagens de erro de login:
```bash
wpscan --url https://target.local -e u1-10
```
### Backups confidenciais (`-e cb`, `-e dbe`)
Pesquisa por `wp-config.php.bak` exposto, dumps de banco de dados e arquivos de backup:
```bash
wpscan --url https://target.local -e cb,dbe
```
---

## ⚙️ Modos de detecção (`--plugins-detection`)

Controla o quão minuciosamente o WPScan pesquisa plug-ins:
```bash
# Passive (Fast, inspects HTML source, script tags, style links)
wpscan --url https://target.local --plugins-detection passive

# Aggressive (Sends HTTP requests for thousands of plugin directories)
wpscan --url https://target.local --plugins-detection aggressive

# Mixed (Recommended default - passive followed by targeted active requests)
wpscan --url https://target.local --plugins-detection mixed
```
---

## 🔓 Força bruta de senha (`wp-login.php` e XML-RPC)

Depois que os nomes de usuário válidos forem enumerados, inicie a força bruta de credenciais direcionadas:
```bash
# Brute force login using enumerated username list
wpscan --url https://target.local -U users.txt -P /usr/share/wordlists/rockyou.txt

# Password spray a single password across all users
wpscan --url https://target.local -U users.txt -p 'Winter2026!'

# Multi-threaded password attack
wpscan --url https://target.local -U admin -P passwords.txt -t 20
```
> [!NOTA]
> Se `xmlrpc.php` estiver habilitado no servidor, o WPScan aproveita automaticamente os métodos de multichamada XML-RPC para testar centenas de combinações de senha por única solicitação HTTP!

---

## 🥷 Furtividade e Evasão WAF
```bash
# Randomize User-Agent header to evade simple bot signatures
wpscan --url https://target.local --random-user-agent

# Throttle request rate (milliseconds between requests) to bypass rate limits
wpscan --url https://target.local --throttle 1000

# Route through SOCKS proxy / Tor
wpscan --url https://target.local --proxy socks5://127.0.0.1:9050

# Ignore SSL/TLS certificate warnings
wpscan --url https://target.local --disable-tls-checks
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificação padrão** | `wpscan --url <URL> --api-token <TOKEN>` |
| **Enumerar plug-ins vulneráveis** | `wpscan --url <URL> -e vp --api-token <TOKEN>` |
| **Enumerar todos os plug-ins** | `wpscan --url <URL> -e ap --plugins-detection aggressive` |
| **Enumerar usuários** | `wpscan --url <URL> -e u` |
| **Enumerar backups e configurações** | `wpscan --url <URL> -e cb,dbe` |
| **Força bruta de senha** | `wpscan --url <URL> -U <USERS> -P <PASSWORDS>` |
| **Agente de usuário aleatório e furtivo** | `wpscan --url <URL> --random-user-agent --stealthy` |
| **Exportar para arquivo JSON** | `wpscan --url <URL> -f json -o wpscan_report.json` |

---

## 🛡️ Considerações defensivas e endurecimento

1. **Desative XML-RPC:** Bloqueie o acesso ao `xmlrpc.php` por meio da configuração `.htaccess` ou Nginx para neutralizar ataques de força bruta multichamadas de alta velocidade.
2. **Restringir endpoint de usuários da API REST:** Desative o acesso público ao `/wp-json/wp/v2/users` para evitar a enumeração trivial do autor.
3. **Corrija plug-ins regularmente:** Mais de 90% das vulnerabilidades do WordPress residem em plug-ins e temas de terceiros, e não no núcleo do WordPress.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas destina-se apenas a testes de penetração autorizados, avaliações de vulnerabilidade e auditoria de aplicativos da web. Forçar senhas ou sistemas de varredura sem autorização explícita é ilegal.