<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛡️ checksec & Binutils - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗██╗  ██╗███████╗ ██████╗██╗  ██╗███████╗███████╗ ██████╗
  ██╔════╝██║  ██║██╔════╝██╔════╝██║ ██╔╝██╔════╝██╔════╝██╔════╝
  ██║     ███████║█████╗  ██║     █████╔╝ ███████╗█████╗  ██║     
  ██║     ██╔══██║██╔══╝  ██║     ██╔═██╗ ╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║███████╗╚██████╗██║  ██╗███████║███████╗╚██████╗
   ╚═════╝╚═╝  ╚═╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝
          Linux ELF Binary Mitigation Auditor & Inspection Toolkit
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/checksec-Security_Mitigations-blue?style=for-the-badge" alt="checksec">
  <img src="https://img.shields.io/badge/readelf-ELF_Headers-orange?style=for-the-badge" alt="readelf">
  <img src="https://img.shields.io/badge/objdump-Disassembly-green?style=for-the-badge" alt="objdump">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-purple?style=for-the-badge" alt="Linux ELF">
</p>

<p alinhar="centro">
  <b>🛡️ Ferramentas de linha de comando essenciais para auditar mitigações de segurança executáveis do Linux (RELRO, Stack Canary, NX, PIE, Fortify) e dissecar cabeçalhos binários ELF, seções e tabelas de símbolos</b>
</p>

---

## 📋 Índice

- [Visão geral e matriz de mitigações binárias](#-overview--binary-mitigations-matrix)
- [Instalação](#-instalação)
- [checksec: Auditoria de Mitigação Rápida](#-checksec-rapid-mitigation-auditing)
  - [Auditoria de binários individuais e em lote](#auditing-single--batch-binaries)
  - [Auditoria de processos em execução](#auditing-running-processes)
  - [Saída JSON e CSV](#json--csv-output)
- [readelf: cabeçalho ELF e dissecção de seção](#-readelf-elf-header--section-dissection)
  - [Cabeçalho do arquivo ELF (`-h`)](#elf-file-header--h)
  - [Cabeçalhos e segmentos de programas (`-l`)](#program-headers--segments--l)
  - [Cabeçalhos de seção (`-S`)](#cabeçalhos de seção--s)
  - [Tabelas de símbolos (`-s` / `--syms`)](#tabelas de símbolos--s----syms)
  - [Dependências e bibliotecas dinâmicas (`-d`)](#dynamic-dependencies--libraries--d)
- [objdump: Desmontagem e realocações](#-objdump-disassembly--relocations)
  - [Desmontagem Intel (`-d -M intel`)](#desmontagem Intel--d--m-intel)
  - [Entradas de realocação e GOT (`-R`)](#relocation-entries--got--r)
- [nm & strings: resolução de símbolos e strings](#-nm--strings-symbol-resolution--strings)
  - [Listando símbolos desmontados com `nm`](#listando-símbolos-desmontados-com-nm)
  - [Extraindo strings ofuscadas com `strings`](#extraindo-strings-obfuscadas-com-strings)
- [Referência de compilação reforçada](#-referência de compilação reforçada)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e matriz de mitigações binárias

Antes de iniciar qualquer fluxo de trabalho de engenharia reversa ou exploração, é obrigatório compreender quais mitigações de segurança estão habilitadas:

| Mitigação | Nome Completo | Mecanismo de Proteção | Estratégia de desvio |
|------------|-----------|-----------|-----------------|
| **RELRO** | Realocação somente leitura | Torna `.got` somente leitura (`Full RELRO`) para evitar a substituição do ponteiro | Estruturas de heap de destino, endereços de retorno ou ganchos de função |
| **Canário** | Protetor de pilha | Coloca cookie aleatório antes do ponteiro de retorno; verifica a integridade no retorno | Vazamento de valor canário por meio de vazamento de memória ou força bruta (forking daemons) |
| **NX** | Não-Executar / DEP | Marca segmentos de pilha, heap e dados como não executáveis ​​| Programação Orientada a Retorno (ROP) ou Programação Orientada a Salto (JOP) |
| **TORTA** | Executável independente de posição | Carrega endereço base binário em local aleatório na memória virtual (ASLR) | Ponteiro de código de vazamento para calcular o deslocamento do endereço base |
| **Fortalecer** | `_FORTIFY_SOURCE` | Substitui funções de string inseguras (`strcpy` -> `__strcpy_chk`) por verificação de limites | Bugs lógicos sem estouro, string de formato sem estouro de buffer |

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y checksec binutils

# Arch Linux
sudo pacman -S checksec binutils

# Python pip (standalone checksec)
pip install checksec.py
```
---

## ⚡ checksec: Auditoria de mitigação rápida

### Auditoria de binários únicos e em lote
```bash
# Audit a single executable
checksec --file=./target_binary

# Audit all binaries in directory
checksec --dir=/usr/bin/
```
*Exemplo de saída padrão:*
```text
RELRO           STACK CANARY      NX            PIE             RPATH      RUNPATH      FILE
Full RELRO      Canary found      NX enabled    PIE enabled     No RPATH   No RUNPATH   ./target_binary
```
### Auditoria de processos em execução
Audite mitigações de memória e status ASLR de um daemon ativo:
```bash
# Audit by Process ID (PID)
checksec --proc=1337

# Audit process by name
checksec --proc-all
```
### Saída JSON e CSV
Exporte resultados para análise automatizada:
```bash
checksec --file=./target_binary --output=json > security.json
checksec --file=./target_binary --output=csv > security.csv
```
---

## 🔬 readelf: Cabeçalho ELF e dissecação de seção

O utilitário `readelf` exibe informações de arquivos ELF sem invocar qualquer execução:

### Cabeçalho do arquivo ELF (`-h`)
Revela arquitetura de CPU, classe de 32 vs 64 bits, endereço de ponto de entrada e endianness:
```bash
readelf -h ./target_binary
```
### Cabeçalhos e segmentos do programa (`-l`)
Exibe segmentos carregados na memória em tempo de execução e sinalizadores (`R`, `W`, `E`). Crucial para identificar se a pilha (`GNU_STACK`) possui permissões de execução:
```bash
readelf -l ./target_binary
```
*(Se `GNU_STACK` tiver `RWE`, a execução da pilha é permitida; NX está desabilitado).*

### Cabeçalhos de seção (`-S`)
Inspecione layouts de memória, endereços e deslocamentos para `.text` (código), `.data` (dados inicializados), `.rodata` (constantes), `.bss` (dados não inicializados), `.plt` e `.got`:
```bash
readelf -S ./target_binary
```
### Tabelas de símbolos (`-s` / `--syms`)
Liste todas as funções exportadas, variáveis globais e símbolos libc importados:
```bash
# List all symbols
readelf -s ./target_binary

# Filter dynamic symbols used at runtime
readelf --dyn-syms ./target_binary
```
### Dependências e bibliotecas dinâmicas (`-d`)
Lista as bibliotecas compartilhadas necessárias (tags `NEEDED`, por exemplo, `libc.so.6`, `libssl.so`):
```bash
readelf -d ./target_binary
```
---

## 🧩 objdump: Desmontagem e realocações

### Desmontagem da Intel (`-d -M intel`)
Desmonte as seções executáveis em um assembly limpo de sintaxe Intel:
```bash
# Disassemble .text section in Intel syntax
objdump -d -M intel ./target_binary

# Disassemble specific function (e.g. main)
objdump -d -M intel ./target_binary | grep -A 30 "<main>:"
```
### Entradas de realocação e GOT (`-R`)
Exiba os endereços de realocação da tabela de deslocamento global (GOT) para chamadas de biblioteca importadas:
```bash
objdump -R ./target_binary
```
*Mostra destinos de endereço para funções como `puts`, `printf` e `system`.*

---

## 🔍 nm & strings: resolução de símbolos e strings

### Listando símbolos desmontados com `nm`
```bash
# List defined symbols only (ignore external imports)
nm --defined-only -C ./target_binary

# List undefined external symbols (functions resolved by dynamic linker)
nm -u -C ./target_binary
```
### Extraindo Strings Ofuscadas com `strings`
```bash
# Extract ASCII strings with minimum 8 characters
strings -a -n 8 ./target_binary

# Extract 16-bit Little Endian wide strings (UTF-16 e.g., Windows / Java)
strings -e l -a ./target_binary

# Print byte offset of each string in hex
strings -a -t x ./target_binary
```
---

## 🛡️ Referência de compilação reforçada

Para construir um binário ELF com mitigações de segurança máximas:
```bash
gcc -O2 \
  -Wall \
  -fstack-protector-strong \
  -D_FORTIFY_SOURCE=2 \
  -Wl,-z,relro,-z,now \
  -fPIE -pie \
  -Wl,-z,noexecstack \
  source.c -o secure_binary
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Mitigações de auditoria** | `checksec --file=<bin>` |
| **PID em execução de auditoria** | `checksec --proc=<pid>` |
| **Ver cabeçalho ELF** | `readelf -h <bin>` |
| **Ver sinalizador Stack NX** | `readelf -l <bin> \| grep -A 1 GNU_STACK` |
| **Listar seções (.got, .plt)**| `readelf -S <bin>` |
| **Ver símbolos dinâmicos** | `readelf --dyn-syms <bin>` |
| **Desmontagem da Intel** | `objdump -d -M intel <bin>` |
| **Inspecionar realocações GOT** | `objdump -R <bin>` |
| **Símbolos de lista (desmantelados)** | `nm -C <bin>` |
| **Extraia cadeias de caracteres UTF-16** | `strings -e l -a <bin>` |
| **Mostrar deslocamentos de string** | `strings -a -t x <bin>` |

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> checksec e GNU Binutils são ferramentas de diagnóstico e engenharia reversa destinadas a auditoria de segurança de software, validação de defesa e pesquisa binária autorizada. A inspeção de software não autorizado deve cumprir os regulamentos locais de propriedade intelectual.