<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Radare2 (r2) – Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗  █████╗ ██████╗  █████╗ ██████╗ ███████╗██████╗ 
  ██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔══██╗██╔════╝╚════██╗
  ██████╔╝███████║██║  ██║███████║██████╔╝█████╗   █████╔╝
  ██╔══██╗██╔══██║██║  ██║██╔══██║██╔══██╗██╔══╝  ██╔═══╝ 
  ██║  ██║██║  ██║██████╔╝██║  ██║██║  ██║███████╗███████╗
  ╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝
      UNIX-like Command-Line Reverse Engineering Framework
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Radare2-CLI_Reversing-blue?style=for-the-badge" alt="Radare2">
  <img src="https://img.shields.io/badge/Author-Sergi_Álvarez-orange?style=for-the-badge" alt="pancake">
  <img src="https://img.shields.io/badge/Category-Reverse_Engineering-green?style=for-the-badge" alt="Engenharia reversa">
  <img src="https://img.shields.io/badge/License-LGPLv3-purple?style=for-the-badge" alt="LGPLv3">
</p>

<p alinhar="centro">
  <b>⚡ Estrutura poderosa, de código aberto e baseada em terminal para análise binária, desmontagem, depuração, edição hexadecimal e correção binária</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Instalação](#-instalação)
- [Abrindo arquivos e modos de sessão](#-opening-files--session-modes)
- [Comandos de análise binária (`a`)](#-binary-análise-comandos-a)
- [Navegação e busca (`s`)](#-navegação--busca-s)
- [Impressão e desmontagem (`p`)](#-impressão--desmontagem-p)
- [Modos visuais e gráficos interativos (`V` e `VV`)](#-interativo-visual--graph-modes-v--vv)
- [Patching e modificação binária (`w`)](#-binary-patching--modification-w)
- [Conjunto de ferramentas complementares do Radare2](#-the-radare2-companion-toolset)
  - [rabin2: Extração de informações binárias](#rabin2-binary-information-extraction)
  - [rasm2: Montador e Desmontador](#rasm2-assembler--disassembler)
  - [radiff2: mecanismo de diferença binária](#radiff2-binary-difference-engine)
  - [rafind2: pesquisa de bytes e strings](#rafind2-byte--string-search)
  - [rax2: Conversor Base e Numérico](#rax2-base--numeric-converter)
- [Noções básicas de depuração com r2 (`-d`)](#-debugging-basics-with-r2--d)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Anti-reversão](#-considerações-defensivas--anti-reversão)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

Criado por **Sergi Álvarez (pancake)**, **Radare2** (`r2`) é uma estrutura portátil e modular de engenharia reversa construída inteiramente em torno da filosofia UNIX. Ele roda inteiramente dentro do seu terminal sem exigir dependências gráficas pesadas:
- **Suporte total à arquitetura:** x86/x64, ARM, ARM64, MIPS, PowerPC, AVR, SPARC, WebAssembly, Java, Dalvik e EVM.
- **Utilitários CLI Composable:** Vem com ferramentas de linha de comando especializadas (`rabin2`, `rasm2`, `radiff2`, `rafind2`, `rax2`).
- **Modos visuais interativos:** Editores hexadecimais de tela dividida, gráficos de fluxo de controle ASCII, monitores de registro e visualizações de descompilador.
- **Scriptável e extensível:** Integra-se com Python, r2pipe, Ghidra (`r2ghidra`) e Rizin.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y radare2

# Arch Linux
sudo pacman -S radare2

# Install latest Git master (Recommended for most features)
git clone https://github.com/radareorg/radare2.git
cd radare2 && sys/install.sh

# Install r2ghidra decompiler plugin
r2pm update
r2pm -i r2ghidra
```
---

## 🚀 Abrindo arquivos e modos de sessão
```bash
# Open binary in read-only mode
r2 /bin/ls

# Open binary in read-write mode (required for patching)
r2 -w target.bin

# Open binary in debugger mode
r2 -d target.bin

# Open binary without parsing headers (raw flat binary / shellcode)
r2 -b 32 -n /path/to/raw.bin

# Open in quiet mode (no greeting banner)
r2 -q target.bin
```
---

## 🔬 Comandos de análise binária (`a`)

Dentro do shell `r2`, os comandos são letras únicas ou prefixos curtos. Você pode anexar `?` a qualquer comando para ver a ajuda embutida:
```text
[0x00000000]> a?        # Show analysis help
[0x00000000]> aa        # Basic analysis (symbols, basic blocks)
[0x00000000]> aaa       # Deep analysis (functions, cross-references, calls)
[0x00000000]> aaaa      # Experimental auto-analysis (takes longer)

[0x00000000]> afl       # List all identified functions
[0x00000000]> afi       # Display function info at current address
[0x00000000]> afvn      # Rename local variable: afvn <new_name> <old_name>
[0x00000000]> axt <addr># Find cross-references (xrefs) TO address
[0x00000000]> axf <addr># Find cross-references (xrefs) FROM address
```
---

## 🧭 Navegação e busca (`s`)

O comando search altera o endereço atual do cursor:
```text
[0x00000000]> s main        # Seek to main function
[0x00401122]> s 0x401200    # Seek to absolute address
[0x00401200]> s+ 32         # Seek 32 bytes forward
[0x00401220]> s- 16         # Seek 16 bytes backward
[0x00401210]> s/ hit0_0     # Seek to search hit
[0x00401210]> u             # Undo seek (go back to previous address)
[0x00401200]> U             # Redo seek
```
---

## 📜 Impressão e desmontagem (`p`)
```text
[0x00401122]> pd 10     # Disassemble 10 instructions
[0x00401122]> pdf       # Disassemble entire current function
[0x00401122]> pdc       # Decompile function using r2ghidra plugin
[0x00401122]> px 64     # Print 64 bytes in hex dump format
[0x00401122]> ps        # Print string at current address
[0x00401122]> pz        # Print zero-terminated string
[0x00401122]> pi 5      # Print 5 instructions (concise format)
```
---

## 🖥️ Modos visuais e gráficos interativos (`V` e `VV`)

Pressionar `V` ou `VV` transforma o Radare2 em um rico aplicativo de terminal interativo:

- **Entre no modo visual:** Digite `V`
  - Pressione `p` / `P` para alternar as visualizações (Hex dump -> Disassembly -> Debugger -> Decompiler).
  - Pressione `c` para alternar o modo cursor (use as teclas de seta ou `hjkl` para selecionar bytes).
  - Pressione `d` para redefinir bytes como código, dados ou strings.
  - Pressione `q` para retornar ao prompt padrão.
- **Entre na visualização do gráfico:** Digite `VV`
  - Renderiza gráfico de fluxo de controle ASCII (CFG).
  - Use `Tab`/`Shift+Tab` para saltar entre nós de ramificação condicional.
  - Pressione `t` para seguir o verdadeiro ramo; `f` para seguir o ramo falso.
  - Pressione `u` para sair de um galho.

---

## ✍️ Correção e modificação binária (`w`)

Para modificar um binário no disco, certifique-se de que r2 esteja aberto no modo de gravação (`r2 -w target` ou digite `oo+` dentro de uma sessão ativa):
```text
[0x00401150]> oo+                           # Re-open current binary with write permissions
[0x00401150]> wa nop                        # Assemble 'nop' instruction at current address
[0x00401150]> wa jmp 0x401190               # Patch a jump instruction
[0x00401150]> wx 90909090                   # Write hex bytes (4 NOPs)
[0x00401150]> w "Modified string\x00"       # Overwrite string with null-terminator
[0x00401150]> w0 16                         # Zero-fill 16 bytes (NUL padding)
```
---

## 🛠️ O conjunto de ferramentas complementares Radare2

Radare2 inclui utilitários CLI independentes:

### rabin2: Extração de informações binárias
```bash
# Print general binary architecture, bits, and security mitigations (NX, PIE, Canary)
rabin2 -I target.bin

# List imported symbols and functions (libc calls)
rabin2 -i target.bin

# List exported symbols
rabin2 -E target.bin

# Extract ASCII & UTF-16 strings
rabin2 -z target.bin

# List sections (.text, .data, .rodata)
rabin2 -S target.bin
```
### rasm2: Montador e Desmontador
```bash
# Assemble x64 instructions into hexadecimal opcode
rasm2 -a x86 -b 64 "xor eax, eax; ret"
# Output: 31c0c3

# Disassemble hex opcodes into human-readable assembly
rasm2 -a x86 -b 64 -d "31c0c3"
# Output:
# xor eax, eax
# ret
```
### radiff2: mecanismo de diferença binária
```bash
# Compare two binaries and count differing bytes
radiff2 -c original.bin patched.bin

# Detailed code comparison graph
radiff2 -g main original.bin patched.bin | xdot -
```
### rafind2: pesquisa de bytes e strings
```bash
# Search for ASCII string in file
rafind2 -s "admin_password" target.bin

# Search for hexadecimal pattern
rafind2 -x "4889e5" target.bin
```
### rax2: Conversor Base e Numérico
```bash
# Convert hex to decimal
rax2 0x1337      # Output: 4919

# Convert decimal to hex
rax2 4919        # Output: 0x1337

# Convert string to hex
rax2 -S "admin"  # Output: 61646d696e
```
---

## 🐛 Noções básicas de depuração com r2 (`-d`)
```bash
# Start debugging binary with arguments
r2 -d target.bin arg1 arg2
```
Dentro do shell do depurador:
```text
[0x7f112233]> db main       # Set software breakpoint at main
[0x7f112233]> db 0x401150   # Set breakpoint at specific address
[0x7f112233]> dbl           # List all active breakpoints
[0x7f112233]> dc            # Continue execution until breakpoint
[0x7f112233]> ds            # Step into (single instruction)
[0x7f112233]> dso           # Step over function calls
[0x7f112233]> dr            # Print all CPU registers
[0x7f112233]> dr rax        # Print specific register value
[0x7f112233]> dr rax = 0x1  # Modify register value
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Análise Profunda** | `aaa` |
| **Funções de lista** | `afl` |
| **Procure Função/Endereço** | `s <addr>`/`s main` |
| **Função de desmontagem** | `pdf` |
| **Função de descompilação (r2ghidra)** | `pdc` |
| **Desmontagem Visual Interativa** | `V` |
| **Gráfico de fluxo ASCII** | `VV` |
| **Instruções de montagem** | `wa <instruction>` |
| **Escrever bytes hexadecimais** | `wx <bytes>` |
| **Reabrir em leitura-gravação** | `oo+` |
| **Extrair informações binárias** | `rabin2 -I <binary>` |
| **Montar CLI** | `rasm2 -a x86 -b 64 "<asm>"` |

---

## 🛡️ Considerações defensivas e anti-reversão

1. **Detecção anti-depuração:** O depurador do Radare2 depende do padrão Linux `ptrace`. Binários com autoverificações `ptrace(PTRACE_TRACEME)` podem bloquear anexos, a menos que sejam corrigidos.
2. **Ofuscação e nivelamento de fluxo de controle:** Binários fortemente ofuscados (OLVM) confundem a recuperação básica de blocos. Use `aaaa` ou mecanismos de execução simbólica (como angr) para simplificar funções niveladas.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Radare2 é um kit de ferramentas de engenharia reversa e análise destinado estritamente a avaliações de segurança autorizadas, análise de malware, pesquisa de vulnerabilidades e uso educacional pessoal. Software proprietário de engenharia reversa sem permissão pode violar contratos de licença de usuário final e leis de direitos autorais.