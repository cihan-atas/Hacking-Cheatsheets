<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔬 strace & ltrace - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗████████╗██████╗  █████╗  ██████╗███████╗    ██╗  ████████╗██████╗  █████╗  ██████╗███████╗
  ██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝    ██║  ╚══██╔══╝██╔══██╗██╔══██╗██╔════╝██╔════╝
  ███████╗   ██║   ██████╔╝███████║██║     █████╗      ██║     ██║   ██████╔╝███████║██║     █████╗  
  ╚════██║   ██║   ██╔══██╗██╔══██║██║     ██╔══╝      ██║     ██║   ██╔══██╗██╔══██║██║     ██╔══╝  
  ███████║   ██║   ██║  ██║██║  ██║╚██████╗███████╗    ███████╗██║   ██║  ██║██║  ██║╚██████╗███████╗
  ╚══════╝   ╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝    ╚══════╝╚═╝   ╚═╝  ╚═╝╚═╝  ╚═╝ ╚═════╝╚══════╝
            Linux Dynamic Binary Analysis: System & Library Call Tracers
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/strace-System_Calls-blue?style=for-the-badge" alt="strace">
  <img src="https://img.shields.io/badge/ltrace-Library_Calls-orange?style=for-the-badge" alt="ltrace">
  <img src="https://img.shields.io/badge/Platform-Linux_ELF-green?style=for-the-badge" alt="Linux">
  <img src="https://img.shields.io/badge/Category-Dynamic_Analysis-purple?style=for-the-badge" alt="Análise Dinâmica">
</p>

<p alinhar="centro">
  <b>🔬 Utilitários de linha de comando essenciais para análise binária dinâmica: interceptando chamadas de sistema do kernel Linux (strace) e chamadas de biblioteca dinâmica de espaço de usuário (ltrace)</b>
</p>

---

## 📋 Índice

- [Visão geral: chamadas de sistema versus chamadas de biblioteca](#-overview-system-calls-vs-library-calls)
- [Instalação](#-instalação)
- [strace: rastreamento de chamadas do sistema](#-strace-system-call-tracing)
  - [Execução de rastreamento básico](#basic-tracing-execution)
  - [Filtrando Syscalls por classe ou nome (`-e`)](#filtering-syscalls-by-class-or-name--e)
  - [Rastreamento de processos filhos e multithreaded (`-f`)](#tracing-child--multi-threaded-processes--f)
  - [Anexando a processos em execução (`-p`)](#attaching-to-running-processes--p)
  - [Expansão de strings e saídas de buffer (`-s`)](#expanding-strings--buffer-outputs--s)
  - [Perfil estatístico e análise de gargalos (`-c`)](#perfil estatístico--análise de gargalos--c)
  - [Injeção de falha para difusão e teste (`-e fault`)](#fault-injection-for-fuzzing--testing--e-fault)
- [ltrace: rastreamento de chamadas de biblioteca dinâmica](#-ltrace-dynamic-library-call-tracing)
  - [Interceptação básica da biblioteca](#basic-library-interception)
  - [Cracking-hardcoded-password-checks-strcmp)
  - [Desmontando símbolos C++ (`-C`)](#demangling-c-symbols--c)
  - [Filtrando funções específicas da biblioteca](#filtering-specific-library-functions)
- [Combinando fluxos de trabalho strace e ltrace](#-combining-strace-and-ltrace-workflows)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e anti-rastreamento](#-considerações-defensivas--anti-rastreamento)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral: chamadas de sistema versus chamadas de biblioteca

Ao analisar binários Linux ELF compilados, a análise estática pode ser impedida por ofuscação, remoção ou empacotamento. O rastreamento dinâmico fornece visibilidade imediata das ações de tempo de execução:

1. **Chamadas de sistema (`strace`):**
   - A interface entre os aplicativos do espaço do usuário e o kernel Linux Linux.
   - Rastreia E/S de disco (`openat`, `read`, `write`), rede (`socket`, `connect`), alocação de memória (`mmap`, `brk`) e gerenciamento de processos (`fork`, `execve`, `ptrace`).
2. **Chamadas de Biblioteca (`ltrace`):**
   - A interface entre as bibliotecas compartilhadas binárias e dinâmicas (como `libc.so`, `libssl.so`).
   - Intercepta chamadas como `strcmp()`, `strlen()`, `malloc()`, `system()` e rotinas criptográficas, expondo frequentemente credenciais de texto simples na memória antes da criptografia.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y strace ltrace

# Arch Linux
sudo pacman -S strace ltrace

# Fedora / Red Hat / CentOS
sudo dnf install -y strace ltrace
```
---

## ⚡ strace: rastreamento de chamadas do sistema

### Execução básica de rastreamento
Execute um binário de destino e transmita todas as chamadas do sistema para o erro padrão:
```bash
strace ./target_binary
```
Redirecione a saída para um arquivo para que não atrapalhe a execução do programa:
```bash
strace -o trace.log ./target_binary
```
### Filtrando Syscalls por Classe ou Nome (`-e`)
Evite ruídos filtrando apenas classes de syscall relevantes:
```bash
# Trace ONLY file-related operations (open, read, write, stat, access)
strace -e trace=file ./target_binary

# Trace ONLY network socket connections and data transfers
strace -e trace=network ./target_binary

# Trace process execution and fork/clone operations
strace -e trace=process ./target_binary

# Filter specific syscalls by exact name
strace -e trace=open,openat,read,write ./target_binary
```
### Rastreando processos filhos e multithread (`-f`)
Binários modernos e malware geram processos ou threads de trabalhadores infantis. Sem `-f`, strace sentirá falta deles:
```bash
# Trace all child forks and threads
strace -f ./target_binary

# Trace children and save each process output into separate files (trace.pid)
strace -ff -o trace ./target_binary
```
### Anexando a processos em execução (`-p`)
Monitore um serviço ou processo de malware ativo sem reiniciá-lo:
```bash
# Attach by Process ID (PID)
sudo strace -p 1337

# Attach to process and follow all newly spawned threads
sudo strace -f -p 1337 -o attached.log
```
### Expandindo Strings e Saídas de Buffer (`-s`)
Por padrão, strace trunca strings impressas em 32 caracteres. Aumente o comprimento da string para inspecionar caminhos completos de arquivos, URLs ou cargas úteis:
```bash
# Display up to 2048 characters of strings and buffers
strace -s 2048 -e trace=read,write ./target_binary
```
### Perfil estatístico e análise de gargalos (`-c`)
Conte o tempo gasto, o número de chamadas e os erros por syscall:
```bash
strace -c ./target_binary
```
*Exemplo de resumo de saída:*
```text
% time     seconds  usecs/call     calls    errors syscall
------ ----------- ----------- --------- --------- ----------------
 54.32    0.001200          40        30           read
 32.10    0.000710          71        10         2 openat
 13.58    0.000300          15        20           write
```
### Injeção de falha para difusão e testes (`-e fault`)
Injete erros arbitrários em syscalls para avaliar a resiliência:
```bash
# Force the 3rd invocation of openat() to fail with Permission Denied (EACCES)
strace -e fault=openat:error=EACCES:when=3 ./target_binary
```
---

## 🧩 ltrace: rastreamento de chamadas de biblioteca dinâmica

### Interceptação Básica de Biblioteca
Chamadas de interceptação feitas para bibliotecas `.so` dinâmicas:
```bash
ltrace ./target_binary
```
### Quebrando verificações de senha codificadas (`strcmp`)
Em CTFs de engenharia reversa e binários de desafio, as funções de comparação de senha (`strcmp`, `strncmp`, `memcmp`) revelam a senha alvo em argumentos de texto simples:
```bash
ltrace -s 128 -e strcmp,strncmp ./license_check
```
*Exemplo de saída:*
```text
strcmp("user_input", "SUPER_SECRET_FLAG_2026") = -1
```
### Desmontando Símbolos C++ (`-C`)
Demangle nomes de métodos C++ mutilados pelo compilador (`_ZNK4Test3fooEv` -> `Test::foo()`):
```bash
ltrace -C ./cpp_binary
```
### Filtrando funções específicas da biblioteca
```bash
# Intercept only memory allocation and string functions
ltrace -e malloc+free+strdup ./target_binary

# Intercept calls originating from specific dynamic libraries
ltrace -l /lib/x86_64-linux-gnu/libcrypto.so.3 ./target_binary
```
---

## 🔄 Combinando fluxos de trabalho strace e ltrace

Ao realizar a triagem inicial em um binário desconhecido:

1. **Etapa 1:** Execute `strace -e trace=file,network ./binary` para ver com quais arquivos e soquetes ele interage.
2. **Etapa 2:** Execute `ltrace -s 256 ./binary` para descobrir comparações de strings de bibliotecas internas, chamadas criptográficas e lógica de memória.
3. **Etapa 3:** Combine as duas ferramentas:
   ```bash
   ltrace -S ./binary
   ```
*(O sinalizador `-S` diz ao ltrace para exibir as chamadas do sistema junto com as chamadas da biblioteca em uma única visualização unificada).*

---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Rastrear todos os Syscalls** | `strace ./binary` |
| **Registrar em arquivo** | `strace -o log.txt ./binary` |
| **Operações de arquivos de rastreamento** | `strace -e trace=file ./binary` |
| **Rastrear atividade de rede** | `strace -e trace=network ./binary` |
| **Seguir processos infantis** | `strace -f ./binary` |
| **Anexar ao PID em execução** | `sudo strace -f -p <PID>` |
| **Expandir comprimento da string** | `strace -s 2048 ./binary` |
| **Estatísticas resumidas do Syscall** | `strace -c ./binary` |
| **Traçar chamadas da biblioteca** | `ltrace ./binary` |
| **Capturar verificação de senha** | `ltrace -s 128 -e strcmp,strncmp ./binary` |
| **Desmontar nomes C++** | `ltrace -C ./binary` |
| **Lib + Syscall combinados**| `ltrace -S ./binary` |

---

## 🛡️ Considerações defensivas e anti-rastreamento

1. **Autoverificação Ptrace:** O Linux permite que apenas um único rastreador seja anexado a um processo via `ptrace`. Chamar `ptrace(PTRACE_TRACEME, 0, 1, 0)` falhará se `strace` ou `ltrace` já estiver conectado.
2. **Vinculação estática (`-static`):** Binários compilados com vinculação estática não contêm referências de biblioteca dinâmica (`DT_NEEDED`), tornando `ltrace` completamente ineficaz (use `strace`).

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> strace e ltrace são ferramentas de diagnóstico dinâmicas projetadas para desenvolvimento de software, depuração e avaliações de segurança autorizadas. Rastrear processos não autorizados ou interceptar comunicações proprietárias sem permissão é estritamente proibido.