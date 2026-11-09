<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎯 pwndbg & GEF - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ██╗    ██╗███╗   ██╗██████╗ ██████╗  ██████╗      ██████╗ ███████╗███████╗
  ██╔══██╗██║    ██║████╗  ██║██╔══██╗██╔══██╗██╔════╝     ██╔════╝ ██╔════╝██╔════╝
  ██████╔╝██║ █╗ ██║██╔██╗ ██║██║  ██║██████╔╝██║  ███╗    ██║  ███╗█████╗  █████╗  
  ██╔═══╝ ██║███╗██║██║╚██╗██║██║  ██║██╔══██╗██║   ██║    ██║   ██║██╔══╝  ██╔══╝  
  ██║     ╚███╔███╔╝██║ ╚████║██████╔╝██████╔╝╚██████╔╝    ╚██████╔╝███████╗██║     
  ╚═╝      ╚══╝╚══╝ ╚═╝  ╚═══╝╚═════╝ ╚═════╝  ╚═════╝      ╚═════╝ ╚══════╝╚═╝     
         Next-Gen GDB Enhancements for Reversing & Binary Exploitation
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/pwndbg-GDB_Plugin-blue?style=for-the-badge" alt="pwndbg">
  <img src="https://img.shields.io/badge/GEF-GDB_Enhanced_Features-orange?style=for-the-badge" alt="GEF">
  <img src="https://img.shields.io/badge/Category-Binary_Exploitation_&_RE-green?style=for-the-badge" alt="Reversing & Pwn">
  <img src="https://img.shields.io/badge/License-MIT_%7C_FOSS-purple?style=for-the-badge" alt="Licença FOSS">
</p>

<p alinhar="centro">
  <b>🎯 Os principais aprimoramentos de terminal GDB de código aberto projetados para engenharia reversa binária, análise de corrupção de memória, inspeção de layout de heap e desenvolvimento de exploração CTF</b>
</p>

---

## 📋 Índice

- [Visão geral e por que o Vanilla GDB precisa de melhorias](#-overview--why-vanilla-gdb-needs-enhancements)
- [Instalação e alternância entre frameworks](#-installation--switching-between-frameworks)
  - [Instalando o pwndbg](#instalando-pwndbg)
  - [Instalando GEF](#instalando-gef)
  - [Gerenciando `.gdbinit`](#managing-gdbinit)
- [O mecanismo de exibição de contexto](#-the-context-display-engine)
  - [Seção de registros](#seção de registros)
  - [Previsões de desmontagem e salto](#disassembly--jump-predictions)
  - [Pilha e Telescópio](#stack--telescópio)
  - [Backtrace e Threads](#backtrace--threads)
- [Inspeção e navegação de memória](#-inspeção de memória--navegação)
  - [Ponteiros telescópicos (`telescope`)](#ponteiros telescópicos-telescópio)
  - [Mapeamentos de memória de processo (`vmmap`)](#process-memory-mappings-vmmap)
  - [Pesquisando memória (`search`)](#searching-memory-search)
- [Exploração e inspeção de heap](#-heap-exploitation--inspection)
  - [Visualizando pedaços e arenas](#viewing-chunks--arenas)
  - [Inspecting-free-bins-tcache-fastbins-smallbins)](#inspecting-free-bins-tcache-fastbins-smallbins)
  - [Visualizando layout de heap (`vis_heap_chunks`)](#visualizing-heap-layout-vis_heap_chunks)
- [Exploit Development Helpers](#-exploit-development-helpers)
  - [Checksec e auditoria de mitigação](#checksec--mitigation-audit)
  - [Localizador de pilha canário (`canary`)](#stack-canary-locator-canary)
  - [Padrões cíclicos e cálculo de deslocamento] (# padrões cíclicos - cálculo de deslocamento)
  - [Caça de gadgets ROP na memória](#caça de gadgets ROP na memória)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e proteções binárias](#-considerações defensivas--proteções binárias)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e por que Vanilla GDB precisa de melhorias

O Depurador GNU padrão (`gdb`) é uma ferramenta de força industrial, mas sua experiência de usuário pronta para uso é visualmente austera. Ele não atualiza automaticamente estados de registro, desreferencia ponteiros de vários níveis ou exibe visualizações de pilha ao passar pelas instruções.

**pwndbg** e **GEF** transformam o GDB em um pacote de elite de engenharia reversa e exploração binária:
- **pwndbg:** Projetado especificamente para exploração de software de baixo nível, depuração de heap (glibc 2.27 - 2.39+ internos do alocador) e desreferenciação automatizada de ponteiro.
- **GEF (GDB Enhanced Features):** Um único script Python independente com zero dependências externas, oferecendo recursos independentes de arquitetura (ARM, MIPS, PowerPC, x86) e análise de estrutura dinâmica.

---

## 📥 Instalação e alternância entre frameworks

### Instalando o pwndbg
```bash
git clone https://github.com/pwndbg/pwndbg
cd pwndbg
./setup.sh
```
### Instalando o GEF
```bash
# Automated one-liner download
bash -c "$(curl -fsSL https://gef.blah.cat/sh)"
```
### Gerenciando `.gdbinit`
Para alternar entre pwndbg e GEF sem conflito, configure seu `~/.gdbinit`:
```bash
# To use pwndbg:
echo "source /path/to/pwndbg/gdbinit.py" > ~/.gdbinit

# To use GEF:
echo "source ~/.gdbinit-gef.py" > ~/.gdbinit
```
---

## 🖥️ O mecanismo de exibição de contexto

Cada vez que a execução é interrompida em um ponto de interrupção ou etapa, tanto o pwndbg quanto o GEF imprimem uma visualização de terminal automatizada com vários painéis:
```text
──────────────────────────[ REGISTERS / FLAGS ]──────────────────────────
RAX: 0x0 
RBX: 0x7fffffffe000 
RCX: 0x7ffff7ec2000 --> 0x00010102464c457f 
RDX: 0x7fffffffe1a0 --> 0x7fffffffe494 ('USER=zeus')
RSI: 0x7fffffffe198 --> 0x7fffffffe47e ('/bin/target')
RDI: 0x1 
RBP: 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
RSP: 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
RIP: 0x401140 (<main+12>: cmp QWORD PTR [rbp-0x8], 0x0)
──────────────────────────[ DISASM / CODE ]──────────────────────────
   0x401135 <main+1>     push   rbp
   0x401136 <main+2>     mov    rbp, rsp
   0x401139 <main+5>     sub    rsp, 0x10
 ► 0x401140 <main+12>    cmp    QWORD PTR [rbp-0x8], 0
   0x401145 <main+17>    je     0x401151 <main+29>  <-- JUMP IS TAKEN
   0x401147 <main+19>    mov    eax, 0x1
──────────────────────────[ STACK ]──────────────────────────
00:0000│ rsp 0x7fffffffe080 --> 0x401156 (<main+34>: mov eax, 0)
01:0008│     0x7fffffffe088 --> 0x0
02:0010│ rbp 0x7fffffffe0a0 --> 0x7fffffffe0c0 --> 0x0
03:0018│     0x7fffffffe0a8 --> 0x7ffff7df6d90 (<__libc_start_call_main+128>: mov edi, eax)
──────────────────────────[ BACKTRACE ]──────────────────────────
 ► 0   0x401140 main+12
   1   0x7ffff7df6d90 __libc_start_call_main+128
   2   0x7ffff7df6e40 __libc_start_main+128
   3   0x401065 _start+37
```
---

## 🔍 Inspeção e navegação de memória

### Ponteiros telescópicos (`telescope`)
Desreferencia ponteiros de memória recursivamente, exibindo texto ASCII, símbolos de função ou ponteiros de destino:
```text
pwndbg> telescope $rsp 16       # Dereference 16 entries starting at RSP
pwndbg> telescope 0x404000 8    # Dereference data section
```
### Mapeamentos de memória de processo (`vmmap`)
Exibe segmentos completos de memória virtual com permissões de leitura/gravação/execução:
```text
pwndbg> vmmap
```
*Localize rapidamente segmentos com permissões `rwx` (vital para injeção de shellcode).*

### Pesquisando na memória (`search`)
Localize strings, números inteiros ou ponteiros em qualquer lugar nas páginas de memória carregadas:
```text
pwndbg> search -s "/bin/sh"     # Search for string "/bin/sh"
pwndbg> search -p 0x401135      # Search for pointers to main()
pwndbg> search -x "41414141"    # Search for raw hexadecimal bytes
```
---

## 🧱 Exploração e inspeção de heap

A análise de alocadores de heap `glibc` modernos é simples com comandos de heap dedicados:

### Visualizando pedaços e arenas
```text
pwndbg> heap                    # Display entire heap layout
pwndbg> chunk 0x602000          # Parse chunk headers (size, flags: PREV_INUSE, IS_MMAPPED, NON_MAIN_ARENA)
pwndbg> arenas                  # List all active heap arenas
```
### Inspecionando Bins Livres (Tcache, Fastbins, Smallbins)
```text
pwndbg> bins                    # Show all free bins simultaneously
pwndbg> tcache                  # Display per-thread tcache entries
pwndbg> fastbins                # List single-linked fastbins
pwndbg> unsortedbin             # List unsorted bin chunks (used for libc leak)
```
### Visualizando layout de heap (`vis_heap_chunks`)
```text
pwndbg> vis_heap_chunks 10      # Draw color-coded visual representation of consecutive heap chunks
```
---

## 🛠️ Explorar ajudantes de desenvolvimento

### Checksec e auditoria de mitigação
Verifique as mitigações de segurança do compilador diretamente no prompt do GDB:
```text
pwndbg> checksec
```
*Inspeciona:*
- **RELRO:** RELRO parcial vs total (protege a tabela de compensação global `.got.plt`)
- **Pilha:** Canário encontrado vs Nenhum canário
- **NX:** NX habilitado (pilha/heap não executável)
- **PIE:** PIE habilitado (Executável Independente de Posição / ASLR)

### Localizador Canário de Pilha (`canary`)
Localize o valor canário do thread no Thread Local Storage (TLS) e verifique onde ele reside no quadro de pilha atual:
```text
pwndbg> canary
```
### Padrões cíclicos e cálculo de deslocamento
Determine o deslocamento exato para sobrescrever o ponteiro de instrução (EIP/RIP):
```text
# Generate a 100-byte De Bruijn cyclic sequence
pwndbg> cyclic 100

# After the crash (SIGSEGV), calculate offset from faulty address or register value
pwndbg> cyclic -l $rip
# Output: Finding cyclic pattern at offset 72
```
### Caça de gadgets ROP na memória
Pesquise segmentos executáveis carregados para gadgets específicos:
```text
pwndbg> rop --grep "pop rdi"
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Comece com pwndbg/GEF**| `gdb ./target_binary` |
| **Inspecione registros/pilha** | `context` |
| **Ponteiro Recursivo Deref**| `telescope $rsp 10` |
| **Mapa de memória e verificação RWX**| `vmmap` |
| **String de pesquisa na memória**| `search -s "/bin/sh"` |
| **Mitigações de auditoria** | `checksec` |
| **Encontre Stack Canary** | `canary` |
| **Visão geral dos pedaços de pilha** | `heap` |
| **Inspecione caixas gratuitas** | `bins` |
| **Calcular deslocamento do buffer**| `cyclic 100` ->`cyclic -l <val>` |
| **Definir ponto de interrupção no símbolo**| `b *main` |
| **Executar/Continuar/Etapa** | `r`/`c`/`si`/`ni` |

---

## 🛡️ Considerações defensivas e proteções binárias

1. **RELRO completo:** Compilar com `-Wl,-z,relro,-z,now` torna o `.got` completamente somente leitura, derrotando ataques de substituição do GOT.
2. **Stack Canaries:** Compilar com `-fstack-protector-all` coloca cookies antes dos endereços de retorno, causando `__stack_chk_fail` em buffer overflows.
3. **PIE & ASLR:** Randomiza código, heap e endereços base de biblioteca em cada execução.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> pwndbg e GEF são ferramentas de engenharia de exploração e análise binária destinadas estritamente à pesquisa autorizada de vulnerabilidades, auditoria de software defensivo e competições acadêmicas de CTF. Explorar software sem permissão é ilegal.