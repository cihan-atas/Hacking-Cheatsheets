<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 WhatWeb - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██╗    ██╗██╗  ██╗ █████╗ ████████╗██╗    ██╗███████╗██████╗ 
  ██║    ██║██║  ██║██╔══██╗╚══██╔══╝██║    ██║██╔════╝██╔══██╗
  ██║ █╗ ██║███████║███████║   ██║   ██║ █╗ ██║█████╗  ██████╔╝
  ██║███╗██║██╔══██║██╔══██║   ██║   ██║███╗██║██╔══╝  ██╔══██╗
  ╚███╔███╔╝██║  ██║██║  ██║   ██║   ╚███╔███╔╝███████╗██████╔╝
   ╚══╝╚══╝ ╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝    ╚══╝╚══╝ ╚══════╝╚═════╝ 
       Next Generation Web Scanner & Technology Fingerprinter
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/WhatWeb-Web_Fingerprinter-blue?style=for-the-badge" alt="WhatWeb">
  <img src="https://img.shields.io/badge/Author-Andrew_Horton-orange?style=for-the-badge" alt="urbanadventurer">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconhecimento e digitalização">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Scanner web de última geração projetado para identificar sistemas de gerenciamento de conteúdo (CMS), plataformas de blogs, bibliotecas JavaScript e tecnologias de servidor</b>
</p>

---

## 📋 Índice

- [Visão geral e impressão digital de tecnologia] (#-visão geral - impressão digital de tecnologia)
- [Instalação](#-instalação)
- [Impressão digital básica do site](#-impressão digital básica do site)
- [Níveis de agressão explicados (`-a`)](#-níveis de agressão explicados--a)
  - [Modo passivo furtivo (`-a 1`)](#modo passivo furtivo--a-1)
  - [Modo Agressivo (`-a 3`)](#modo agressivo--a-3)
  - [Modo Pesado / Bruto (`-a 4`)](#heavy--brute-mode--a-4)
- [Verificação de sub-redes e arquivos de destino](#-scanning-subnets--target-files)
- [Sistema de plug-ins e auditoria seletiva](#-plugin-system--auditoria seletiva)
- [Registro de saída e exportação de relatório](#-output-logging--report-exporting)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e tecnologia de impressão digital

Antes de lançar explorações ou difusores de força bruta contra um aplicativo da Web, é fundamental identificar sua pilha de tecnologia exata.

Criado por **Andrew Horton (aventureiro urbano)**, **WhatWeb** reconhece mais de 1.800 tecnologias web distintas:
- **Sistemas de gerenciamento de conteúdo (CMS):** WordPress, Drupal, Joomla, Ghost, Magento.
- **Servidores Web e Proxies:** Apache, Nginx, Microsoft-IIS, Cloudflare, Envoy.
- **Frameworks de Backend:** Laravel, Django, Ruby on Rails, ASP.NET, Spring Boot.
- **Bibliotecas de front-end:** React, Vue.js, Angular, jQuery, Bootstrap.
- **Dispositivos Embarcados:** Webcams, roteadores, impressoras, interfaces SCADA.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y whatweb

# Arch Linux
sudo pacman -S whatweb

# macOS (Homebrew)
brew install whatweb
```
---

## 🚀 Impressão digital básica de sites
```bash
# Scan a single domain
whatweb https://target.local

# Brief one-line output format
whatweb --color=never -q https://target.local

# Verbose detailed report showing evidence matches
whatweb -v https://target.local
```
---

## ⚡ Níveis de agressão explicados (`-a`)

WhatWeb equilibra furtividade e profundidade usando quatro níveis de agressão:

### Modo Passivo Furtivo (`-a 1`)
Envia uma única solicitação HTTP GET para o URL fornecido e analisa os cabeçalhos de resposta, cookies e tags HTML retornados sem solicitar páginas extras:
```bash
whatweb -a 1 https://target.local
```
### Modo Agressivo (`-a 3`)
Recomendado para testes de penetração. Segue redirecionamentos e solicita arquivos de plugins comuns (por exemplo, `wp-login.php`, `robots.txt`, hashes de favicon):
```bash
whatweb -a 3 https://target.local
```
### Modo Pesado/Bruto (`-a 4`)
Tenta milhares de suposições de plug-ins e URLs de impressão digital conhecidos:
```bash
whatweb -a 4 https://target.local
```
---

## 🌐 Verificando sub-redes e arquivos de destino
```bash
# Scan an entire CIDR network block
whatweb 192.168.1.0/24

# Scan multiple URLs listed in a text file
whatweb -i targets.txt

# Pipe live web endpoints from httpx into WhatWeb
cat live_hosts.txt | whatweb --no-errors
```
---

## 🧩 Sistema de plugins e auditoria seletiva

WhatWeb apresenta uma rica arquitetura de plugins:
```bash
# List all 1,800+ available plugins
whatweb -l

# Search for specific plugins (e.g., WordPress or Apache)
whatweb --grep wordpress

# Run ONLY a specific plugin against a target
whatweb --plugins="apache,php,wordpress" https://target.local
```
---

## 📊 Registro de saída e exportação de relatórios
```bash
# Export results to clean JSON format
whatweb https://target.local --log-json results.json

# Export to XML format
whatweb https://target.local --log-xml results.xml

# Export to compact CSV format
whatweb -i targets.txt --log-brief results.txt
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificação padrão** | `whatweb <URL>` |
| **Relatório detalhado detalhado** | `whatweb -v <URL>` |
| **Verificação passiva furtiva** | `whatweb -a 1 <URL>` |
| **Verificação profunda agressiva** | `whatweb -a 3 <URL>` |
| **Verificar sub-rede (CIDR)** | `whatweb <SUBNET>/24` |
| **Verificar alvos do arquivo** | `whatweb -i targets.txt` |
| **Exportar para JSON** | `whatweb <URL> --log-json report.json` |
| **Filtrar por plug-ins específicos**| `whatweb --plugins="wordpress,nginx" <URL>` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Mascaramento de cabeçalho do servidor:** Remova ou randomize os cabeçalhos de resposta HTTP `Server` e `X-Powered-By` no Apache/Nginx para anular a impressão digital passiva de banner.
2. **Favicon Hash Masking:** WhatWeb usa hashes MD5 de `/favicon.ico` para identificar estruturas da web. Personalize os favoritos da estrutura padrão.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> WhatWeb destina-se apenas a reconhecimento autorizado, auditoria de segurança e inventário de sistema. A verificação de redes ou aplicativos da Web não autorizados sem permissão pode violar as leis aplicáveis.