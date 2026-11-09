<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas do fcrackzip

[fcrackzip](https://github.com/hyc/fcrackzip) é um cracker de senha de linha de comando rápido e multithread para arquivos ZIP criptografados. Ele suporta ataques baseados em dicionário e ataques de força bruta otimizados contra o ZipCrypto tradicional (cifra de fluxo PKZIP) e pode verificar a integridade do arquivo sem extração completa.

---

## Índice

- [Recursos principais e limitações](#core-features--limitations)
- [Instalação](#instalação)
- [Ataques de dicionário (`-D -p`)](#ataques de dicionário--d--p)
- [Ataques de força bruta (`-b`)](#ataques de força bruta--b)
- [Conjuntos de caracteres e especificação de comprimento](#conjuntos de caracteres--especificação de comprimento)
- [Benchmark e otimização de desempenho](#benchmark--performance-optimization)
- [ZipCrypto vs. AES-256 (Alternativa John/Hashcat)](#zipcrypto-vs-aes-256-johnhashcat-alternative)

---

## Principais recursos e limitações

- **Cracker ZipCrypto ultrarrápido:** Escrito em C, otimizado para o algoritmo ZipCrypto legado (criptografia PKZIP padrão).
- **Modo Dicionário:** Lê as senhas dos candidatos sequencialmente nas listas de palavras.
- **Modo de força bruta:** Gera candidatos dinamicamente usando conjuntos de caracteres configuráveis ​​e limites de comprimento.
- **Teste de descompactação de arquivo (`-u`):** Verifica senhas candidatas tentando descompactar, praticamente eliminando falsos positivos.
- **Limitação:** **não** oferece suporte à criptografia WinZip AES-256 moderna. Para ZIPs AES-256, use `zip2john` + John the Ripper ou Hashcat (modo 13600).

---

## Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y fcrackzip

# Arch Linux / BlackArch
sudo pacman -S fcrackzip

# Fedora / CentOS / RHEL
sudo dnf install -y fcrackzip

# Verify command
fcrackzip -h
```
---

## Ataques de dicionário (`-D -p`)

A maneira mais rápida de testar listas de palavras comuns:

### 1. Ataque básico de dicionário com verificação de descompactação
```bash
fcrackzip -u -D -p /usr/share/wordlists/rockyou.txt secret_archive.zip
```
- `-u`: Modo descompactar (verifica o CRC do arquivo extraído para evitar falsos positivos).
- `-D`: modo de ataque de dicionário.
- `-p <file>`: Caminho para o arquivo do dicionário.

### 2. Modo de saída detalhado
Exibir velocidade testada, senha atual e informações de depuração:
```bash
fcrackzip -v -u -D -p passwords.txt backup.zip
```
---

## Ataques de força bruta (`-b`)

Quando os ataques de dicionário falham, use força bruta contra senhas curtas desconhecidas:

### 1. PINs numéricos de força bruta (4 a 6 dígitos)
```bash
fcrackzip -u -b -c 1 -l 4-6 secure.zip
```
- `-b`: modo de força bruta.
- `-c 1`: Conjunto de caracteres: somente dígitos (`0-9`).
- `-l 4-6`: Faixa de comprimento: mínimo 4, máximo 6 caracteres.

### 2. Caracteres alfanuméricos minúsculos (1 a 5 caracteres)
```bash
fcrackzip -u -b -c a1 -l 1-5 archive.zip
```
- `-c a1`: Letras minúsculas (`a-z`) e dígitos (`0-9`).

### 3. Minúsculas + Maiúsculas + Dígitos + Símbolos
```bash
fcrackzip -u -b -c aA1! -l 6-8 protected.zip
```
- `-c a`: Letras minúsculas (`a-z`).
- `-c A`: Letras maiúsculas (`A-Z`).
- `-c 1`: Números (`0-9`).
- `-c !`: Símbolos especiais (`[!:$%&/()=?{[]}+*~#]`).

---

## Conjuntos de caracteres e especificação de comprimento

| Bandeira | Conjunto de caracteres incluído | Exemplo de Candidatos |
|---|---|---|
| `-c a` | Letras minúsculas (`a-z`) | `admin`, `secret` |
| `-c A` | Letras maiúsculas (`A-Z`) | `SECRET`, `ROOT` |
| `-c 1` | Dígitos (`0-9`) | `1234`, `2024` |
| `-c !` | Caracteres de pontuação especiais | `!@#$` |
| `-c aA1` | Caracteres alfanuméricos combinados | `Admin123` |
| `-p <prefix>` | Sequência inicial inicial | Útil para retomar a força bruta |

Especificando limites de comprimento:
```bash
# Minimum length 6, Maximum length 8
-l 6-8

# Exact length 8
-l 8-8
```
---

## Benchmark e otimização de desempenho

Teste o rendimento da CPU em sua máquina:
```bash
fcrackzip -B
```
---

## ZipCrypto vs. AES-256 (Alternativa John/Hashcat)

Se `fcrackzip` retornar `not a zipfile, or unsupported encryption type`, o arquivo será criptografado com AES-256 (WinZip AES). Utilize `zip2john`:
```bash
# 1. Extract hash using zip2john
zip2john aes_protected.zip > zip.hash

# 2. Crack using John the Ripper
john --wordlist=/usr/share/wordlists/rockyou.txt zip.hash

# 3. Or crack using Hashcat (Mode 13600: WinZip)
cat zip.hash | cut -d ':' -f 2 > hashcat_zip.hash
hashcat -m 13600 hashcat_zip.hash /usr/share/wordlists/rockyou.txt
```