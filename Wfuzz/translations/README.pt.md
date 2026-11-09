<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 Wfuzz - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██╗    ██╗███████╗██╗   ██╗███████╗███████╗
  ██║    ██║██╔════╝██║   ██║╚══███╔╝╚══███╔╝
  ██║ █╗ ██║█████╗  ██║   ██║  ███╔╝   ███╔╝ 
  ██║███╗██║██╔══╝  ██║   ██║ ███╔╝   ███╔╝  
  ╚███╔███╔╝██║     ╚██████╔╝███████╗███████╗
   ╚══╝╚══╝ ╚═╝      ╚═════╝ ╚══════╝╚══════╝
       The Flexible Web Application Fuzzer & Parameter Explorer
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Wfuzz-Web_Fuzzer-blue?style=for-the-badge" alt="Wfuzz">
  <img src="https://img.shields.io/badge/Language-Python-orange?style=for-the-badge" alt="Linguagem Python">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Segurança da Web e API">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Fuzzer HTTP altamente personalizável projetado para descobrir parâmetros ocultos, bypasses, cabeçalhos e matrizes de múltiplas cargas</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura de palavras-chave](#-overview--the-keyword-architecture)
- [Comparação Wfuzz vs ffuf](#-wfuzz-vs-ffuf-comparação)
- [Instalação](#-instalação)
- [Diretório básico e descoberta de arquivos](#-diretório-básico--descoberta de arquivos)
- [Filtragem e ocultação de ruído (`--hc`, `--hl`, `--hw`, `--hh`)](#-filtragem--ocultação-ruído---hc---hl---hw---hh)
- [Tipos de carga útil e codificadores dinâmicos (`-z`)](#-tipos de carga útil--codificadores dinâmicos--z)
- [Parâmetro e Fuzzing de Variável Oculta](#-parameter--hidden-variable-fuzzing)
- [Cabeçalhos, Cookies e Autenticação](#-cabeçalhos-cookies--autenticação)
- [Fuzzing multivariável (`FUZZ` e `FUZ2Z`)](#-fuzzing multivariável-fuzz-and-fuz2z)
- [Roteamento através do Burp Suite (`-p`)](#-routing-through-burp-suite--p)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura de palavras-chave

Escrito em Python por **Xavi Mendez**, **Wfuzz** é uma ferramenta clássica e testada em batalha para avaliar aplicações web. Ele substitui espaços reservados designados (padrão: `FUZZ`) dentro de URLs, parâmetros, cabeçalhos e corpos POST por entradas de dicionário ou cargas geradas por algoritmos.

Wfuzz se destaca por sua incomparável **versatilidade de carga útil** (faixas, geradores de IP, strings regex, codificadores on-the-fly como MD5 ou Base64) e mecânica de filtragem modular.

---

## ⚖️ Comparação Wfuzz vs ffuf

| Recurso | Wfuzz | affu |
|--------|-------|------|
| **Linguagem de implementação** | Pitão | Vá |
| **Raw Speed** | ⭐⭐⭐ (Good) | ⭐⭐⭐⭐⭐ (Extremamente rápido) |
| **Codificadores de carga útil dinâmica**| ✅ Native (`-z file,pass.txt,md5`)| ❌ Requer scripts externos |
| **Combinações de múltiplas cargas**| ✅ Nativo (`FUZZ`, `FUZ2Z`, `FUZ3Z`)| ✅ Supported (`-w w1:W1 -w w2:W2`)|
| **Geradores de carga útil integrados**| ✅ Intervalos, IPs, Hex, Dicionários | ❌ Somente listas de palavras de arquivos |
| **Resource Usage** | Moderate RAM / CPU | Low Memory footprint |

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y wfuzz

# Python Pip
pip3 install wfuzz

# Arch Linux
sudo pacman -S wfuzz
```
---

## 🚀 Diretório básico e descoberta de arquivos
```bash
# Basic directory fuzzing
wfuzz -w /usr/share/wordlists/dirb/common.txt http://target.local/FUZZ

# Fuzzing file extensions
wfuzz -w extensions.txt http://target.local/index.FUZZ

# Fuzzing hidden backup files
wfuzz -w words.txt http://target.local/FUZZ.bak
```
---

## 🎯 Filtrando e ocultando ruído (`--hc`, `--hl`, `--hw`, `--hh`)

Servidores do mundo real retornam respostas mesmo para páginas inexistentes (páginas 404 personalizadas ou redirecionamentos). Wfuzz fornece sinalizadores de filtragem refinados:
```bash
# Hide specific HTTP status codes (e.g. 404, 403)
wfuzz -w wordlist.txt --hc 404,403 http://target.local/FUZZ

# Hide responses by line count (filters out static custom 404 pages)
wfuzz -w wordlist.txt --hl 42 http://target.local/FUZZ

# Hide responses by word count
wfuzz -w wordlist.txt --hw 120 http://target.local/FUZZ

# Hide responses by character count / body length
wfuzz -w wordlist.txt --hh 0,1052 http://target.local/FUZZ

# Show ONLY HTTP 200 and 302 responses
wfuzz -w wordlist.txt --sc 200,302 http://target.local/FUZZ
```
---

## 📦 Tipos de carga útil e codificadores dinâmicos (`-z`)

Wfuzz fornece geradores integrados que eliminam a necessidade de arquivos de texto pré-gerados:
```bash
# Numeric Range Fuzzing (e.g., IDOR testing from 1 to 500)
wfuzz -z range,1-500 http://target.local/user.php?id=FUZZ

# Custom Inline List
wfuzz -z list,admin-root-test-dev http://target.local/FUZZ/

# On-the-fly MD5 encoding of wordlist entries
wfuzz -z file,passwords.txt,md5 http://target.local/login?hash=FUZZ

# On-the-fly Base64 encoding
wfuzz -z file,usernames.txt,base64 http://target.local/auth?token=FUZZ
```
---

## 🔍 Fuzzing de parâmetros e variáveis ocultas

Descubra parâmetros GET/POST desvinculados:
```bash
# GET parameter discovery
wfuzz -w /usr/share/seclists/Discovery/Web-Content/burp-parameter-names.txt \
  --hc 404 --hh 2340 "http://target.local/search.php?FUZZ=test"

# POST body parameter discovery
wfuzz -w parameters.txt -d "FUZZ=admin" --hc 404 http://target.local/api/update
```
---

## 🍪 Cabeçalhos, cookies e autenticação
```bash
# Cookie Fuzzing (Session hijacking / privilege testing)
wfuzz -w wordlist.txt -b "session=FUZZ" http://target.local/dashboard

# HTTP Header Fuzzing (IP bypasses via X-Forwarded-For)
wfuzz -z file,ips.txt -H "X-Forwarded-For: FUZZ" http://target.local/admin

# Basic Authentication brute force
wfuzz -z file,users.txt -z file,passwords.txt --basic "FUZZ:FUZ2Z" http://target.local/protected/
```
---

## ⚡ Fuzzing multivariável (`FUZZ` e `FUZ2Z`)

Combine várias listas de palavras usando tags de espaço reservado distintas:
```bash
# Test username and password matrix simultaneously
wfuzz -w users.txt -w passwords.txt -d "user=FUZZ&pass=FUZ2Z" --hc 200 http://target.local/login.php

# Fuzz path and extension together
wfuzz -w words.txt -w extensions.txt http://target.local/FUZZ.FUZ2Z
```
---

## 🔁 Roteamento através do Burp Suite (`-p`)

Inspecione cargas úteis e respostas dentro do Burp Suite:
```bash
# Route requests through local Burp Suite proxy
wfuzz -p 127.0.0.1:8080:HTTP -w wordlist.txt http://target.local/FUZZ
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Difusão de diretório** | `wfuzz -w <DICT> <URL>/FUZZ` |
| **Ocultar códigos de status HTTP** | `wfuzz -w <DICT> --hc 404,403 <URL>/FUZZ` |
| **Ocultar por comprimento da resposta** | `wfuzz -w <DICT> --hh <LENGTH> <URL>/FUZZ` |
| **Parâmetro Fuzz GET** | `wfuzz -w <PARAMS> "<URL>?FUZZ=test"` |
| **Corpo POST Fuzz** | `wfuzz -w <PARAMS> -d "FUZZ=test" <URL>` |
| **Cargas de intervalo numérico** | `wfuzz -z range,1-100 "<URL>?id=FUZZ"` |
| **Codifique cargas em tempo real**| `wfuzz -z file,<DICT>,base64 "<URL>?token=FUZZ"` |
| **Difusão de várias palavras-chave** | `wfuzz -w <W1> -w <W2> -d "u=FUZZ&p=FUZ2Z" <URL>` |
| **Roteamento via Burp Proxy** | `wfuzz -p 127.0.0.1:8080:HTTP -w <DICT> <URL>/FUZZ` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Bloqueio de WAF:** A difusão de centenas de solicitações por segundo aciona a limitação de taxa do Cloudflare, do AWS WAF e do ModSecurity. Use `-s <seconds>` para acelerar a velocidade da solicitação.
2. **Esgotamento de recursos:** A difusão intensa em consultas de banco de dados não indexadas pode causar condições de negação de serviço (DoS) em servidores web legados.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas é apenas para testes de penetração autorizados e auditoria de vulnerabilidades. A difusão de aplicativos não autorizados é estritamente proibida.