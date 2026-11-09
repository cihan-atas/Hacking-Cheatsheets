<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🧩 Ropper & ROPgadget - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗  ██████╗ ██████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔═══██╗██╔══██╗██╔══██╗██╔════╝██╔══██╗
  ██████╔╝██║   ██║██████╔╝██████╔╝█████╗  ██████╔╝
  ██╔══██╗██║   ██║██╔═══╝ ██╔═══╝ ██╔══╝  ██╔══██╗
  ██║  ██║╚██████╔╝██║     ██║     ███████╗██║  ██║
  ╚═╝  ╚═╝ ╚═════╝ ╚═╝     ╚═╝     ╚══════╝╚═╝  ╚═╝
     Advanced ROP & JOP Gadget Finder and Chain Generator
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Ropper-Gadget_Finder-blue?style=for-the-badge" alt="Ropper">
  <img src="https://img.shields.io/badge/ROPgadget-Exploit_Tool-orange?style=for-the-badge" alt="ROPgadget">
  <img src="https://img.shields.io/badge/Bypass-NX_%7C_DEP-red?style=for-the-badge" alt="NX Bypass">
  <img src="https://img.shields.io/badge/Category-Exploit_Development-green?style=for-the-badge" alt="Exploit Dev">
</p>

<p alinhar="centro">
  <b>🧩 Utilitários de linha de comando essenciais para descobrir gadgets de Programação Orientada a Retorno (ROP) e Programação Orientada a Salto (JOP) para contornar proteções de memória não executáveis (NX/DEP)</b>
</p>

---

## 📋 Índice

- [Visão geral e teoria ROP](#-overview--rop-theory)
- [Instalação](#-instalação)
- [Descoberta básica de gadgets](#-descoberta básica de gadgets)
  - [Extraindo todos os gadgets](#extraindo-todos-gadgets)
  - [Limitando a profundidade da instrução](#limitando-instrução-profundidade)
- [Pesquisa avançada e filtragem de padrões](#-pesquisa avançada--filtragem de padrões)
  - [Direcionando registros específicos (`rdi`, `rsi`, `rdx`, `rax`)](#targeting-specific-registers-rdi-rsi-rdx-rax)
  - [Gadgets de gravação de memória (escrever o que-onde)](#memory-write-gadgets-write-what-where)
  - [Syscall e gadgets de interrupção](#syscall--gadgets de interrupção)
- [Excluindo caracteres ruins (`--badbytes`)](#-excluindo-caracteres ruins---badbytes)
- [Geração automatizada de cadeia ROP] (#-automated-rop-chain-Generation)
  - [Correntes automatizadas Ropper](#ropper-automated-chains)
  - [Cadeias automatizadas ROPgadget](#ropgadget-automated-chains)
- [Analisando bibliotecas carregadas (libc ROP Chains)](#-analyzing-loaded-libraries-libc-rop-chains)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações e Mitigações Defensivas](#-considerações-defensivas--mitigações)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e teoria ROP

Os sistemas operacionais modernos impõem **NX (No-Execute) / DEP (Data Execution Prevention)**, que impede a execução direta de shellcode colocado na pilha ou heap.

**Programação Orientada a Retorno (ROP)** contorna essa restrição reutilizando pequenas instruções de máquina existentes já presentes na memória executável (seção `.text` das bibliotecas binárias ou compartilhadas como `libc.so`). Cada sequência de instruções que termina em uma instrução `ret` é chamada de **gadget**. Ao organizar os endereços na pilha, os invasores encadeiam os gadgets para:
1. Carregue argumentos nos registros da CPU (por exemplo, `pop rdi; ret` para convenções de chamada x64).
2. Escreva strings (como `/bin/sh`) em seções graváveis ​​`.bss` ou `.data`.
3. Acione `execve("/bin/sh", NULL, NULL)` ou invoque `system()`.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y ropper python3-ropgadget

# Python pip (Latest Releases)
pip install ropper capstone
pip install ROPgadget

# Arch Linux
sudo pacman -S ropper python-ropgadget
```
---

## 🚀 Descoberta básica de gadgets

### Extraindo todos os gadgets
```bash
# Using Ropper (Faster multi-threaded engine)
ropper --file ./target_binary --nocolor

# Using ROPgadget
ROPgadget --binary ./target_binary
```
### Limitando a profundidade das instruções
Reduza longas cadeias barulhentas restringindo o comprimento do gadget (número de instruções antes de `ret`):
```bash
# Limit to maximum 3 instructions before return
ropper --file ./target_binary --inst-count 3
ROPgadget --binary ./target_binary --depth 3
```
---

## 🔍 Pesquisa avançada e filtragem de padrões

### Direcionando registros específicos (`rdi`, `rsi`, `rdx`, `rax`)
Na convenção de chamada do Linux de 64 bits, os três primeiros argumentos para uma função ou syscall devem residir em `rdi`, `rsi` e `rdx`:
```bash
# Find pop rdi gadgets (1st argument)
ropper --file ./target_binary --search "pop rdi"

# Find pop rsi gadgets (2nd argument)
ropper --file ./target_binary --search "pop rsi"

# Find pop rdx gadgets (3rd argument)
ropper --file ./target_binary --search "pop rdx"

# Find gadgets that control RAX (Syscall number: 59 for execve)
ropper --file ./target_binary --search "pop rax"
```
Usando correspondência de regex ROPgadget:
```bash
ROPgadget --binary ./target_binary --only "pop|ret" | grep -E "rdi|rsi|rdx|rax"
```
### Gadgets de gravação de memória (escrever o que onde)
Quando você precisar armazenar `/bin/sh` ou argumentos arbitrários na memória:
```bash
# Find instructions moving registers into pointer memory
ropper --file ./target_binary --search "mov [???], ???"
```
*Exemplo de correspondência:* `mov qword ptr [rdi], rsi; ret` permite escrever o que estiver em `rsi` no endereço apontado por `rdi`.

### Syscall e gadgets de interrupção
```bash
# Find raw syscall instructions
ropper --file ./target_binary --search "syscall"

# x86 32-bit software interrupt
ropper --file ./target_binary --search "int 0x80"
```
---

## 🚫 Excluindo personagens ruins (`--badbytes`)

Vulnerabilidades de formatação de string ou funções como `strcpy()` terminam em bytes nulos (`\x00`), novas linhas (`\x0a`) ou retornos de carro (`\x0d`). Exclua endereços que contenham estes bytes:
```bash
# Filter out gadgets whose addresses contain bad characters
ropper --file ./target_binary --badbytes 000a0d --search "pop rdi"
ROPgadget --binary ./target_binary --badbytes "00|0a|0d"
```
---

## ⚡ Geração automatizada de cadeia ROP

Ambas as ferramentas podem construir automaticamente modelos de exploração Python funcionais que invocam `execve("/bin/sh")`:

### Correntes Automatizadas Ropper
```bash
ropper --file ./target_binary --chain "execve"
```
### Cadeias automatizadas ROPgadget
```bash
ROPgadget --binary ./target_binary --ropchain
```
*Isso produz um esqueleto de carga útil Python pronto para uso, encadeando `mprotect()` ou `execve()`.*

---

## 📚 Analisando Bibliotecas Carregadas (libc ROP Chains)

Quando o próprio binário for compilado como **PIE** ou contiver poucos gadgets úteis, extraia os gadgets diretamente da biblioteca C compartilhada do sistema de destino (`libc.so.6`):
```bash
# Search gadgets in libc
ropper --file /lib/x86_64-linux-gnu/libc.so.6 --search "pop rdi; ret"

# Find "/bin/sh" string offset inside libc
strings -a -t x /lib/x86_64-linux-gnu/libc.so.6 | grep "/bin/sh"
```
*(Adicione compensações descobertas ao endereço base vazado da libc em tempo de execução).*

---

## ⚡ Tabela de referência rápida

| Objetivo | Comando Ropper | Comando ROPgadget |
|-----------|----------------|-------------------|
| **Descarte todos os gadgets** | `ropper --file <bin>` | `ROPgadget --binary <bin>` |
| **Pesquisar gadget específico** | `ropper --file <bin> --search "pop rdi"` | `ROPgadget --binary <bin> \| grep "pop rdi"` |
| **Filtrar bytes inválidos** | `ropper --file <bin> --badbytes 000a` | `ROPgadget --binary <bin> --badbytes "00\|0a"` |
| **Limite de profundidade/comprimento** | `ropper --file <bin> --inst-count 3` | `ROPgadget --binary <bin> --depth 3` |
| **Pesquisar gadgets JOP** | `ropper --file <bin> --type jop` | `ROPgadget --binary <bin> --norop` |
| **Cadeia ROP de construção automática** | `ropper --file <bin> --chain "execve"`| `ROPgadget --binary <bin> --ropchain` |
| **Gravações na memória de pesquisa** | `ropper --file <bin> --search "mov [???], ???"` | `ROPgadget --binary <bin> \| grep "mov \["` |

---

## 🛡️ Considerações e mitigações defensivas

1. **Tecnologia de aplicação de fluxo de controle (Intel CET/Shadow Stack):** Os processadores modernos mantêm uma pilha de sombra de hardware isolada que verifica endereços de retorno em `ret`, encerrando cadeias ROP imediatamente.
2. **Clang/GCC Control Flow Integrity (CFI):** Valida os alvos do ponteiro de função em um gráfico de borda direta antes de chamadas indiretas.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Ropper e ROPgadget são utilitários de desenvolvimento de exploits e avaliação de segurança de software. Eles devem ser usados ​​estritamente em testes de penetração autorizados, desafios de CTF ou pesquisas de segurança defensivas. Explorar software sem autorização explícita é ilegal.