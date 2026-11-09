<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🛰️ sshuttle - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗███████╗██╗  ██╗██╗   ██╗████████╗████████╗██╗     ███████╗
  ██╔════╝██╔════╝██║  ██║██║   ██║╚══██╔══╝╚══██╔══╝██║     ██╔════╝
  ███████╗███████╗███████║██║   ██║   ██║      ██║   ██║     █████╗  
  ╚════██║╚════██║██╔══██║██║   ██║   ██║      ██║   ██║     ██╔══╝  
  ███████║███████║██║  ██║╚██████╔╝   ██║      ██║   ███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝ ╚═════╝    ╚═╝      ╚═╝   ╚══════╝╚══════╝
       Transparent Proxy Server that Works as a Poor Man's VPN over SSH
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/sshuttle-Poor_Man's_VPN-blue?style=for-the-badge" alt="sshuttle">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunneling-orange?style=for-the-badge" alt="Pivotação e tunelamento">
  <img src="https://img.shields.io/badge/Protocols-TCP_DNS_SSH-green?style=for-the-badge" alt="Protocolos">
  <img src="https://img.shields.io/badge/License-LGPLv2.1-red?style=for-the-badge" alt="Licença LGPLv2.1">
</p>

<p alinhar="centro">
  <b>🌐 Encaminhe sub-redes internas inteiras e solicitações de DNS por meio de uma sessão SSH sem privilégios, sem acesso administrativo no servidor remoto</b>
</p>

---

## 📋 Índice

- [Visão geral e a filosofia "VPN do pobre homem"](#-overview--the-poor-mans-vpn-philosophy)
- [Como funciona o sshuttle](#-como-shuttle-funciona)
- [Comparação de ferramentas: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN](#-tool-comparison-sshuttle-vs-socks-vs-ligolo-ng-vs-openvpn)
- [Instalação](#-instalação)
- [Roteamento de sub-rede básico](#-basic-subnet-routing)
- [Encaminhamento de tráfego DNS (`--dns`)](#-dns-traffic-forwarding---dns)
- [Redirecionamento completo do gateway (rota padrão)](#-full-gateway-redirection-default-route)
- [Exclusões de sub-rede (`-x`)](#-exclusões de sub-rede--x)
- [Opções SSH e autenticação baseada em chave (`-e`)](#-ssh-options--key-based-authentication--e)
- [Execução em segundo plano do Daemon (`-D`)](#-daemon-background-execution--d)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e a filosofia da "VPN do pobre homem"

**sshuttle** é um servidor proxy transparente que opera como uma VPN leve e de configuração zero em uma conexão SSH padrão.

Em testes de penetração corporativos típicos e em avaliações do Red Team, a migração para redes internas é dificultada por restrições estritas:
- ❌ Você raramente possui acesso `root` ou `SYSTEM` no host bastião/pivô SSH inicial.
- ❌ As soluções VPN tradicionais (OpenVPN, WireGuard) requerem direitos administrativos e dispositivos de rede virtuais TUN/TAP em ambos os endpoints.
- ❌ Os proxies SOCKS padrão (como `ssh -D`) exigem a configuração do `proxychains` ou de proxy de aplicativo individual para cada ferramenta.

**sshuttle resolve todas as três limitações simultaneamente:**
- ✅ **Não é necessário root remoto:** O servidor pivô só precisa de uma instalação padrão do Python (`python3`).
- ✅ **Não é necessário TUN/TAP remoto:** Usa multiplexação de fluxo TCP puro na sessão SSH.
- ✅ **Roteamento transparente em todo o sistema:** Modifica `iptables` / `nftables` local na caixa do invasor, permitindo que ferramentas (`nmap`, `curl`, `smbclient`, navegadores da web) direcionem IPs internos diretamente sem wrappers!

---

## ⚙️ Como funciona o transporte
```
[ Attacker Box (Local Root) ]
  ├── iptables / nftables redirects internal IP traffic (10.10.10.0/24) to local port
  └── sshuttle client multiplexes TCP packets over SSH tunnel
              │
              ▼ (Encrypted SSH Channel - Port 22)
[ Remote Pivot Server (Unprivileged User + Python) ]
  └── Assembles TCP connections and forwards them directly to internal hosts
              │
              ▼
[ Target Internal Network (10.10.10.0/24) ]
```
---

## ⚖️ Comparação de ferramentas: sshuttle vs SOCKS vs Ligolo-ng vs OpenVPN

| Recurso | `sshuttle` | MEIAS (`ssh -D`) | `Ligolo-ng` | `OpenVPN` |
|---------|------------|------------------|------------|-----------|
| **Raiz remota necessária** | ❌ Não | ❌ Não | ❌ Não | ✅ Sim |
| **Raiz local necessária** | ✅ Sim (iptables) | ❌ Não | ✅ Sim (interface TUN) | ✅ Sim |
| **Roteamento Transparente** | ✅ Sim | ❌ Requer Proxychains | ✅ Sim (TUN Nativo) | ✅ Sim |
| **Suporte a tráfego UDP** | ❌ Somente DNS | ❌ Não | ✅ Sim | ✅ UDP completo |
| **Requer Python no Target**| ✅ Sim (Python 3) | ❌ Não | ❌ Não (Agente autônomo)| ❌ Não |
| **Verificação SYN do Nmap (`-sS`)** | ❌ Conexão TCP (`-sT`)| ❌ Somente conexão TCP | ✅ Suporte a pacotes brutos | ✅ Pacotes completos de matéria-prima |

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y sshuttle

# Arch Linux
sudo pacman -S sshuttle

# macOS (Homebrew)
brew install sshuttle

# Python Pipx / Pip
pipx install sshuttle
```
---

## 🚀 Roteamento básico de sub-rede

Encaminhe uma sub-rede Classe C remota inteira por meio do host pivô SSH:
```bash
# Route 10.10.10.0/24 through user@192.168.1.50
sshuttle -r user@192.168.1.50 10.10.10.0/24

# Route multiple disjoint subnets simultaneously
sshuttle -r user@192.168.1.50 10.10.10.0/24 172.16.0.0/16 192.168.100.0/24
```
Uma vez conectado, você pode se comunicar diretamente com máquinas internas:
```bash
# No proxychains needed!
curl http://10.10.10.25:8080/
smbclient -L //10.10.10.10/ -U 'domain/user%pass'
```
---

## 🌐 Encaminhamento de tráfego DNS (`--dns`)

Encaminhe consultas DNS corporativas internas (`.corp`, `.local`) para o servidor DNS interno da rede remota:
```bash
# Route subnet and proxy internal DNS queries
sshuttle --dns -r user@192.168.1.50 10.10.10.0/24

# Query internal domain directly via nslookup/dig
dig dc01.internal.corp
```
---

## 🌍 Redirecionamento completo de gateway (rota padrão)

Roteie todo o tráfego de saída do invasor por meio da conexão SSH (comportamento VPN completo):
```bash
# Route all traffic through remote SSH server (0.0.0.0/0)
sshuttle -r user@remote-vps.com 0.0.0.0/0

# Route all traffic except local network
sshuttle -r user@remote-vps.com 0.0.0.0/0 -x 192.168.1.0/24
```
---

## 🚫 Exclusões de sub-rede (`-x`)

Evite que sub-redes sensíveis ou de loopback específicas sejam capturadas:
```bash
# Route entire 10.0.0.0/8 network, but exclude the pivot host itself
sshuttle -r user@10.0.0.50 10.0.0.0/8 -x 10.0.0.50/32
```
---

## 🔑 Opções SSH e autenticação baseada em chave (`-e`)

Passe sinalizadores SSH personalizados (chaves de identidade privadas, portas SSH não padrão):
```bash
# Use custom identity file and non-standard SSH port 2222
sshuttle -e 'ssh -i /path/to/id_rsa -p 2222' -r user@192.168.1.50 10.10.10.0/24
```
---

## 🤖 Execução em segundo plano do Daemon (`-D`)

Execute o sshuttle como um daemon em segundo plano durante compromissos em andamento:
```bash
# Start background daemon with pidfile
sudo sshuttle -D --pidfile /tmp/sshuttle.pid -r user@192.168.1.50 10.10.10.0/24

# Terminate daemon when finished
sudo kill $(cat /tmp/sshuttle.pid)
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Sub-rede única de rota** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24` |
| **Rotear sub-rede com DNS** | `sshuttle --dns -r <USER>@<PIVOT> <SUBNET>/24` |
| **Rotear múltiplas sub-redes** | `sshuttle -r <USER>@<PIVOT> <NET1>/24 <NET2>/16` |
| **Excluir IP/Sub-rede** | `sshuttle -r <USER>@<PIVOT> <SUBNET>/24 -x <EXCLUDE_IP>` |
| **Redirecionamento total de tráfego** | `sshuttle -r <USER>@<PIVOT> 0.0.0.0/0` |
| **Chave/porta SSH personalizada** | `sshuttle -e 'ssh -i <KEY> -p <PORT>' -r <USER>@<PIVOT> <SUBNET>` |
| **Executar em segundo plano** | `sshuttle -D --pidfile /tmp/sshuttle.pid -r <USER>@<PIVOT> <SUBNET>` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Modificação de firewall:** sshuttle modifica tabelas de firewall locais (`iptables` no Linux, `pf` no macOS). Se o sshuttle travar de maneira inadequada, use `iptables -F` ou reinicie o serviço de firewall para liberar regras de redirecionamento obsoletas.
2. **Limitação somente TCP:** Pacotes UDP não DNS, solicitações de eco ICMP (ping) e varreduras SYN não são suportados. Sempre use o `nmap -sT -Pn -n` para varredura de portas no sshuttle.
3. **Pegada alvo:** sshuttle carrega automaticamente um pequeno script de montagem Python para o host pivô remoto durante a inicialização da sessão e o executa via `python3`.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas destina-se apenas a testes de penetração autorizados e administração de sistema. O roteamento e a interceptação não autorizados do tráfego de rede são ilegais.