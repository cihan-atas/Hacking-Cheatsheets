<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔢 Crunch - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗██████╗ ██╗   ██╗███╗   ██╗ ██████╗██╗  ██╗
  ██╔════╝██╔══██╗██║   ██║████╗  ██║██╔════╝██║  ██║
  ██║     ██████╔╝██║   ██║██╔██╗ ██║██║     ███████║
  ██║     ██╔══██╗██║   ██║██║╚██╗██║██║     ██╔══██║
  ╚██████╗██║  ██║╚██████╔╝██║ ╚████║╚██████╗██║  ██║
   ╚═════╝╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═══╝ ╚═════╝╚═╝  ╚═╝
       High-Speed Pattern-Based Wordlist & Character Matrix Generator
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Crunch-Wordlist_Generator-blue?style=for-the-badge" alt="Crunch">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Idioma C">
  <img src="https://img.shields.io/badge/Category-Password_Cracking-green?style=for-the-badge" alt="Quebra de senha">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Gerador de lista de palavras que gera todas as combinações possíveis de conjuntos de caracteres usando comprimentos, padrões e símbolos personalizados</b>
</p>

---

## 📋 Índice

- [Visão geral e o mecanismo de permutação](#-overview--the-permutation-engine)
- [Instalação](#-instalação)
- [Sintaxe básica e conjuntos de caracteres](#-sintaxe básica--conjuntos de caracteres)
- [Correspondência de padrões e espaços reservados (`-t`)](#-pattern-matching--placeholders--t)
- [Usando conjuntos de caracteres do sistema (`charset.lst`)](#-using-system-charsets-charsetlst)
- [Limite de caracteres duplicados (`-d`)](#-limitação de caracteres duplicados--d)
- [Dividindo listas de palavras em pedaços (`-b` e `-c`)](#-dividindo listas de palavras em pedaços--b---c)
- [Piping diretamente nas ferramentas de ataque (espaço zero em disco)](#-piping-directly-into-attack-tools-zero-disk-space)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e avisos de cálculo](#-considerações defensivas--avisos de cálculo)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e o mecanismo de permutação

Ao atacar padrões de senha conhecidos (por exemplo, PINs corporativos, números de telefone ou senhas em conformidade com regras de complexidade padrão, como `Word2026!`), dicionários estáticos padrão, como `rockyou.txt`, ou não atingem o alvo ou são ineficientes.

Escrito em C por **bofheaded**, **Crunch** é um gerador de lista de palavras extremamente rápido. Ele calcula permutações matematicamente, permitindo definir posições exatas de caracteres, comprimentos, conjuntos de caracteres e restrições de duplicação.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y crunch

# Arch Linux (AUR)
yay -S crunch
```
---

## 🚀 Sintaxe básica e conjuntos de caracteres

A sintaxe principal requer comprimento mínimo, comprimento máximo e, opcionalmente, um alfabeto:
```bash
# crunch <min> <max> [characterset] [options]

# Generate all 4-digit numeric PINs (0000 - 9999)
crunch 4 4 0123456789 -o 4digit_pins.txt

# Generate 5-character words using specific custom letters
crunch 5 5 abcdef -o custom_words.txt

# Generate lowercase alphabet combinations from length 3 to 4
crunch 3 4 abcdefghijklmnopqrstuvwxyz -o alpha_3_4.txt
```
---

## 🎭 Correspondência de padrões e espaços reservados (`-t`)

O sinalizador `-t` permite corrigir caracteres conhecidos enquanto difunde os slots desconhecidos usando espaços reservados especiais:

| Espaço reservado | Tipo de caractere |
|-------------|----------------|
| `@` | Letras minúsculas (`a-z`) |
| `,` | Letras maiúsculas (`A-Z`) |
| `%` | Números (`0-9`) |
| `^` | Símbolos especiais/pontuação (`!@#$%^&*...`) |

### Exemplos práticos de padrões:
```bash
# Generate Turkish phone numbers (e.g., 0532XXXXXXX)
crunch 11 11 -t 0532%%%%%%% -o phone_numbers.txt

# Generate passwords conforming to: Capital letter + 4 lowercase + 2 digits + 1 symbol
# e.g., Admin24! or Super99#
crunch 8 8 -t ,@@@@%%^ -o policy_passwords.txt

# Target known prefix: Spring2026! or Spring2026#
crunch 11 11 -t Spring2026^ -o spring_passwords.txt
```
---

## 📚 Usando conjuntos de caracteres do sistema (`charset.lst`)

Crunch agrupa matrizes de caracteres pré-configuradas em `/usr/share/crunch/charset.lst`:
```bash
# Use pre-defined alphanumeric set (mixalpha-numeric)
crunch 6 6 -f /usr/share/crunch/charset.lst mixalpha-numeric -o out.txt

# Use hexadecimal lowercase characters (0-9, a-f)
crunch 8 8 -f /usr/share/crunch/charset.lst hex-lower -o hex_keys.txt
```
---

## 🚫 Limitação de caracteres duplicados (`-d`)

Imponha limites a caracteres duplicados consecutivos para eliminar senhas improváveis:
```bash
# Generate 8-character lowercase words with at most 2 consecutive identical letters
# (e.g., prevents 'aaabbb' while allowing 'aab')
crunch 8 8 abcdefghijklmnopqrstuvwxyz -d 2@ -o deduped.txt

# Limit consecutive numbers to at most 1 (prevents '11' or '22')
crunch 6 6 -t %%%%%% -d 1% -o no_repeated_digits.txt
```
---

## 📦 Dividindo listas de palavras em pedaços (`-b` e `-c`)

Evite que arquivos de vários terabytes esgotem o espaço em disco, dividindo-os em volumes gerenciáveis:
```bash
# Split wordlist into 500MB chunks
crunch 8 8 -f /usr/share/crunch/charset.lst mixalpha-numeric -b 500mb -o START

# Split by line count (every 1,000,000 lines)
crunch 8 8 0123456789 -c 1000000 -o START
```
---

## ⚡ Canalização direta para ferramentas de ataque (zero espaço em disco)

Nunca escreva listas de palavras gigantescas em disco quando você pode transmitir permutações diretamente para ferramentas de cracking:
```bash
# Stream 8-digit numeric PINs straight into Aircrack-ng
crunch 8 8 0123456789 | aircrack-ng -w - -b 00:11:22:33:44:55 capture.cap

# Stream policy passwords directly into Hydra SSH brute-force
crunch 8 8 -t Admin%%^ | hydra -l admin -P - 10.10.10.50 ssh

# Stream into John the Ripper
crunch 6 6 0123456789 | john --stdin --format=raw-md5 hashes.txt
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **PINs numéricos (4 dígitos)** | `crunch 4 4 0123456789 -o pins.txt` |
| **Padrão: Maiúscula + Palavra + Números**| `crunch 8 8 -t ,@@@@%%^ -o passwords.txt` |
| **Padrão: Prefixo Específico** | `crunch 10 10 -t Company%%% -o wordlist.txt` |
| **Usar arquivo Charset** | `crunch 6 6 -f charset.lst mixalpha-numeric -o out.txt` |
| **Limite Consecutivo Idêntico**| `crunch 8 8 -t @@@@@@@@ -d 2@ -o out.txt` |
| **Dividido em arquivos de 1 GB** | `crunch 8 8 <CHARS> -b 1gb -o START` |
| **Tubo para Aircrack-ng** | `crunch 8 8 0123456789 \| aircrack-ng -w - target.cap` |
| **Tubo para Hydra** | `crunch 7 7 -t User%%% \| hydra -l user -P - <IP> ssh` |

---

## 🛡️ Considerações defensivas e avisos de cálculo

> [!AVISO]
> **Explosões de permutação:** Sempre verifique o tamanho de saída projetado do Crunch antes de confirmar a geração do disco. Uma lista de palavras alfanumérica irrestrita de 8 caracteres produz **mais de 200 Terabytes** de dados de texto! Saída de tubulação sempre que possível.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O Crunch destina-se apenas à recuperação de senha autorizada, testes de penetração e auditoria de segurança. A força bruta de senhas em sistemas não autorizados é estritamente ilegal.