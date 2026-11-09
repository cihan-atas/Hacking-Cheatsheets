<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕷️ CeWL - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗███████╗██╗    ██╗██╗     
  ██╔════╝██╔════╝██║    ██║██║     
  ██║     █████╗  ██║ █╗ ██║██║     
  ██║     ██╔══╝  ██║███╗██║██║     
  ╚██████╗███████╗╚███╔███╔╝███████╗
   ╚═════╝╚══════╝ ╚══╝╚══╝ ╚══════╝
       Custom Word List Generator by Robin Wood (DigiNinja)
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/CeWL-Custom_Wordlist_Generator-blue?style=for-the-badge" alt="CeWL">
  <img src="https://img.shields.io/badge/Author-DigiNinja-orange?style=for-the-badge" alt="DigiNinja">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Quebra de senha">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Gerador de dicionário de senhas específico que rastreia sites para coletar terminologia corporativa especializada, nomes de marcas e e-mails de funcionários</b>
</p>

---

## 📋 Índice

- [Visão geral e a filosofia da senha direcionada](#-overview--the-targeted-password-philosophy)
- [Instalação](#-instalação)
- [Rastreamento básico de sites](#-basic-website-crawling)
- [Controles de profundidade de aranha e comprimento de palavra] (#-spider-profundidade--controles de comprimento de palavra)
- [Colhendo endereços de e-mail (`-e`)](#-colhendo endereços de e-mail--e)
- [Incluindo números e letras minúsculas](#-incluindo-números--minúsculas)
- [Extração de metadados de documentos (`-a`)](#-document-metadata-extraction--a)
- [Contagem de palavras e perfil de frequência (`-c`)](#-contagem de palavras--perfil de frequência--c)
- [Encadeando CeWL com Hashcat e John Rules](#-chaining-cewl-with-hashcat--john-rules)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e filosofia de senha direcionada

Listas de palavras genéricas como `rockyou.txt` são eficazes para senhas de consumidores, mas **frequentemente falham em relação às políticas de senha corporativa** que exigem mais de 12 caracteres, símbolos especiais e rotações sazonais.

Os funcionários baseiam rotineiramente suas senhas corporativas em:
- Nomes de projetos da empresa, codinomes internos e slogans (`Pegasus2026!`).
- Terminologia específica do setor, jargão técnico ou marcas de produtos.
- Nomes de executivos, pontos de referência de localização e endereços de escritórios.

Criado por **Robin Wood (DigiNinja)**, **CeWL** (gerador de lista de palavras personalizadas) é um spider Ruby que rastreia um URL de destino especificado até uma profundidade definida, extrai palavras exclusivas que atendem aos requisitos de comprimento mínimo e cria um dicionário de senhas personalizado para o engajamento.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y cewl

# Arch Linux (AUR)
yay -S cewl

# Ruby Gem
gem install cewl
```
---

## 🚀 Rastreamento básico de sites
```bash
# Crawl website and output words to console
cewl https://target.local

# Crawl and save to a custom wordlist file
cewl https://target.local -w company_wordlist.txt
```
---

## 🕸️ Controles de profundidade de aranha e comprimento de palavras

Controle a profundidade com que o CeWL segue os links e quais tamanhos de palavras ele captura:
```bash
# Crawl 2 links deep (-d 2) and enforce minimum word length of 8 characters (-m 8)
cewl https://target.local -d 2 -m 8 -w wordlist_min8.txt

# Aggressive 3-level deep crawl for comprehensive corporate terminology
cewl https://target.local -d 3 -m 7 --no-words -w deep_crawl.txt
```
---

## 📧 Coletando endereços de e-mail (`-e`)

Extraia endereços de e-mail corporativos para simulações de phishing ou pulverização de credenciais subsequentes:
```bash
# Extract emails to a separate dedicated file
cewl https://target.local -e --email_file emails.txt -w words.txt
```
---

## 🔡 Incluindo números e letras minúsculas
```bash
# Include words containing alphanumeric combinations (e.g., product names like V300, X86)
cewl https://target.local --with-numbers -m 6 -w words_with_numbers.txt

# Convert all discovered words to lowercase (great for rule generation)
cewl https://target.local --lowercase -w lowercase_words.txt
```
---

## 📄 Extração de metadados de documentos (`-a`)

Extraia nomes de usuário de autor e criador de documentos baixados em PDF e Microsoft Office:
```bash
# Download and analyze document metadata to extract employee usernames
cewl https://target.local -a --meta_file extracted_authors.txt
```
---

## 📊 Contagem de palavras e perfil de frequência (`-c`)

Classifique as palavras por frequência para identificar os chavões corporativos mais difundidos:
```bash
# Append occurrence count next to each word
cewl https://target.local -c -m 6 -w frequency.txt

# Sort by most frequent words in descending order
sort -k2 -nr frequency.txt | head -n 30
```
---

## ⚡ Encadeando CeWL com Hashcat e John Rules

Uma lista de palavras CeWL bruta contém raízes de dicionário básicas. Para produzir senhas viáveis, expanda a lista de palavras usando **Regras de mutação Hashcat/John the Ripper**:
```bash
# 1. Harvest base words from corporate website
cewl https://target.local -m 5 --lowercase -w base_words.txt

# 2. Mutate words using Hashcat's best64 rule set
hashcat --force base_words.txt -r /usr/share/hashcat/rules/best64.rule --stdout | sort -u > mutated_passwords.txt

# Example mutations produced:
# Acmetool -> Acmetool2026!, !Acmetool123, 4cmet00l, Acmetool#1
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Rastreamento básico para arquivo** | `cewl <URL> -w wordlist.txt` |
| **Definir comprimento e profundidade mínimos** | `cewl <URL> -m 8 -d 2 -w wordlist.txt` |
| **Extraia e-mails corporativos**| `cewl <URL> -e --email_file emails.txt` |
| **Extrair metadados do documento**| `cewl <URL> -a --meta_file authors.txt` |
| **Incluir palavras alfanuméricas**| `cewl <URL> --with-numbers -w wordlist.txt` |
| **Saída em minúsculas** | `cewl <URL> --lowercase -w wordlist.txt` |
| **Contar frequências de palavras** | `cewl <URL> -c -w frequencies.txt` |
| **Autenticar por meio de login**| `cewl <URL> -a "Cookie: session=xyz"` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Logs de acesso ao servidor Web:** CeWL gera centenas de solicitações HTTP GET rápidas, produzindo logs de acesso spider identificáveis. Use strings User-Agent personalizadas (`-u "Mozilla/5.0..."`) para se misturar.
2. **Armadilhas de palavras-de-mel:** As equipes azuis defensivas podem incorporar links HTML invisíveis ou ocultos que levam a credenciais canário para sinalizar spiders automatizados.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> CeWL foi projetado apenas para avaliações de segurança autorizadas, testes de penetração e pesquisas de auditoria de senha. Raspar ou rastrear sistemas não autorizados é estritamente proibido.