<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Name-That-Hash (enésimo) Cheatsheet

[Name-That-Hash](https://github.com/HashPals/Name-That-Hash) (`nth`) é um mecanismo de identificação de hash moderno e ultrarrápido que identifica com precisão hashes desconhecidos, hashes incorporados em texto ou arquivos e gera modos diretos Hashcat (`-m`) e John the Ripper (`--format`). Ele decodifica automaticamente codificações aninhadas como Base64 e Hex.

---

## Índice

- [Recursos principais](#recursos-chave)
- [Instalação](#instalação)
- [Identificação de hash CLI](#cli-hash-identification)
- [Multi-Hash e processamento de arquivos](#multi-hash--processamento de arquivos)
- [Saída do formato Hashcat e John, o Estripador](#hashcat--saída do formato john-the-ripper)
- [Decodificação automática e filtros de popularidade](#auto-decode--popularity-filters)
- [Integração com Cracking Pipelines](#integration-with-cracking-pipelines)

---

## Principais recursos

- **Mais de 300 tipos de hash:** Abrange algoritmos modernos, formatos do Active Directory, hashes de banco de dados, assinaturas CMS e cifras esotéricas.
- **Mapeamento de sinalizadores Hashcat e John:** Exibe imediatamente os valores `-m <mode>` e `--format=<format>`.
- **Decodificação automática:** Remove a codificação Base64, Hex e URL antes de calcular o tipo de hash.
- **Pesquisa de substring/texto:** Encontra hashes enterrados em logs, despejos de memória ou arquivos de configuração.
- **Classificação por popularidade:** Classifica os resultados por probabilidade de uso no mundo real.

---

## Instalação
```bash
# Via pipx (recommended for isolated CLI tools)
pipx install name-that-hash

# Via standard Python pip
python3 -m pip install --user name-that-hash

# Kali Linux / Debian repository
sudo apt-get update && sudo apt-get install -y python3-name-that-hash

# Verify installation
nth --version
```
---

## Identificação de hash CLI

### 1. Identifique um único hash
```bash
nth -t "5d41402abc4b2a76b9719d911017c592"
```
### 2. Identifique os modos de formato Hashcat e John diretamente
Exiba apenas tipos de hash que suportam os modos de cracking Hashcat ou John:
```bash
nth -t "b8c37e33defde51cf91e1e03e51657da" --hashcat --john
```
### 3. Exibir informações detalhadas (descrição, velocidade, exemplos)
```bash
nth -t "$6$rounds=5000$usesomesalt$..." -v
```
---

## Multi-Hash e processamento de arquivos

### 1. Identifique hashes dentro de um arquivo (linha por linha)
```bash
nth -f /path/to/hashes.txt
```
### 2. Filtre os resultados apenas para os principais hashes populares
Reduza falsos positivos de tipos de hash obscuros filtrando por popularidade:
```bash
# Level 1: Most popular hashes only (MD5, SHA1, SHA256, NTLM, bcrypt, etc.)
nth -f hashes.txt --popularity 1
```
### 3. Pesquise e extraia hashes incorporados em texto não estruturado
Verifique strings de memória bruta, arquivos de log ou código-fonte em busca de hashes:
```bash
nth --text "Found hash in config: 098f6bcd4621d373cade4e832627b4f6 on port 8080"
```
---

## Saída em formato Hashcat e John, o Estripador

`nth` torna trivial alimentar hashes em crackers sem memorizar os números dos modos:

| Exemplo de formato hash | Algoritmo Detectado | Modo Hashcat (`-m`) | Formato João (`--format`) |
|---|---|---|---|
| `e80b5017098950fc58aad83c8c14978e` | MD5 | `0` | `raw-md5` |
| `b4b9b02e6f09a9bd760f388b67351e2b` | NTLM | `1000` | `nt` |
| `284ddc0d513372c7d764a0ff813473f2...` | SHA-256 | `1400` | `raw-sha256` |
| `$2a$12$DOW8W6...` | criptografar | `3200` | `bcrypt` |
| `$6$salt$hash...` | SHA-512 (Unix) | `1800` | `sha512crypt` |
| `$krb5tgs$23$*...` | Kerberoast (TGS-REP) | `13100` | `krb5tgs` |
| `$krb5asrep$23$...` | Torrefação AS-REP | `18200` | `krb5asrep` |

---

## Decodificação automática e filtros de popularidade

Muitos aplicativos armazenam hashes em Base64 ou Hex antes da inserção no banco de dados. `nth` pode reverter isso automaticamente:
```bash
# Decode Base64 hash before checking
nth -t "XUkQS8xLJnY5cZ0REXxFkg==" --base64

# Decode hex encoded string before checking
nth -t "3564343134303261626334623261373662393731396439313130313763353932" --hex
```
---

## Integração com Pipelines de Cracking

Canalize rapidamente um hash desconhecido para o Hashcat usando identificação automatizada:
```bash
# 1. Check hash and capture Hashcat mode
HASH="b4b9b02e6f09a9bd760f388b67351e2b"
MODE=$(nth -t "$HASH" --hashcat --popularity 1 | grep -oP '(?<=Hashcat: -m )\d+' | head -n 1)

# 2. Launch Hashcat with identified mode
hashcat -m "$MODE" -a 0 "$HASH" /usr/share/wordlists/rockyou.txt
```