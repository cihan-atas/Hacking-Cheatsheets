<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🕶️ mitmproxy - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███╗   ███╗██╗████████╗███╗   ███╗██████╗ ██████╗  ██████╗ ██╗  ██╗██╗   ██╗
  ████╗ ████║██║╚══██╔══╝████╗ ████║██╔══██╗██╔══██╗██╔═══██╗╚██╗██╔╝╚██╗ ██╔╝
  ██╔████╔██║██║   ██║   ██╔████╔██║██████╔╝██████╔╝██║   ██║ ╚███╔╝  ╚████╔╝ 
  ██║╚██╔╝██║██║   ██║   ██║╚██╔╝██║██╔═══╝ ██╔══██╗██║   ██║ ██╔██╗   ╚██╔╝  
  ██║ ╚═╝ ██║██║   ██║   ██║ ╚═╝ ██║██║     ██║  ██║╚██████╔╝██╔╝ ██╗   ██║   
  ╚═╝     ╚═╝╚═╝   ╚═╝   ╚═╝     ╚═╝╚═╝     ╚═╝  ╚═╝ ╚═════╝ ╚═╝  ╚═╝   ╚═╝   
       Interactive, Scriptable Terminal HTTP/HTTPS Proxy & Traffic Engine
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/mitmproxy-Terminal_Proxy-blue?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Author-mitmproxy_Project-orange?style=for-the-badge" alt="mitmproxy">
  <img src="https://img.shields.io/badge/Category-Network_&_Web_Analysis-green?style=for-the-badge" alt="Análise de rede">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p alinhar="centro">
  <b>🕶️ Um proxy HTTP de interceptação interativo compatível com SSL/TLS para testadores de penetração, pesquisadores de segurança e engenheiros de software com uma interface de console avançada e API de script Python</b>
</p>

---

## 📋 Índice

- [Visão geral e os três binários](#-visão geral--os-três-binários)
- [Instalação e configuração de certificado](#-installation--certificate-setup)
  - [Instalando no Linux / macOS](#installing-on-linux--macos)
  - [Instalando certificado CA para interceptação SSL/TLS](#installing-ca-certificate-for-ssltls-interception)
- [Modos de operação: proxy regular, transparente e reverso](#-operating-modes-regular-transparent--reverse-proxy)
- [Navegação TUI interativa (`mitmproxy`)](#-interactive-tui-navigation-mitmproxy)
  - [Atalhos de teclado](#teclado-atalhos)
  - [Filtragem e interceptação de fluxos](#filtering--intercepting-flows)
- [Automação não interativa (`mitmdump`)](#-non-interactive-automation-mitmdump)
  - [Gravação e repetição de tráfego](#recording--replaying-traffic)
- [Scripts e complementos Python](#-python-scripting--addons)
  - [Script automatizado de manipulação de cabeçalho](#automated-header-manipulation-script)
  - [Adulteração corporal de resposta ao vivo](#live-response-body-tampering)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e os três binários

Ao contrário dos proxies gráficos de desktop volumosos, o **mitmproxy** oferece interceptação HTTP/1, HTTP/2 e WebSockets ultrarrápida, programável e de total fidelidade, diretamente dentro do seu terminal ou pipeline de automação.

Ele vem com três binários de ponto de entrada:
1. **`mitmproxy`:** A interface de usuário de terminal (TUI) interativa baseada em ncurses.
2. **`mitmdump`:** A versão de linha de comando não interativa (o equivalente `tcpdump` para HTTP).
3. **`mitmweb`:** Uma interface gráfica web leve baseada em navegador.

---

## 📥 Instalação e configuração do certificado

### Instalando no Linux/macOS
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y mitmproxy

# Arch Linux
sudo pacman -S mitmproxy

# Python pip / pipx
pipx install mitmproxy
```
### Instalando certificado CA para interceptação SSL/TLS
Para interceptar o tráfego HTTPS, os alvos devem confiar na Autoridade de Certificação dinâmica do mitmproxy:
1. Inicie o mitmproxy uma vez: `mitmproxy` (cria certificados em `~/.mitmproxy/`).
2. No dispositivo/navegador cliente, configure o proxy `127.0.0.1:8080`.
3. Navegue até `http://mitm.it` e baixe/instale o certificado para Android, iOS, Windows ou Linux.

---

## ⚙️ Modos de operação: proxy regular, transparente e reverso

### Modo proxy normal (padrão)
Escuta de proxy de encaminhamento padrão na porta 8080:
```bash
mitmproxy -p 8080
```
### Modo proxy reverso
Retransmite o tráfego de entrada para um servidor de destino específico (ideal para testes de API):
```bash
mitmproxy --mode reverse:https://api.target.com -p 8080
```
### Modo Transparente
Intercepta o tráfego roteado através do iptables sem definir as configurações de proxy do cliente:
```bash
mitmproxy --mode transparent --showhost
```
---

## 🖥️ Navegação TUI interativa (`mitmproxy`)

### Atalhos de teclado
- `?`: Abra a sobreposição de ajuda.
- `Enter`: Inspecione o fluxo HTTP selecionado (Solicitação/Resposta/Detalhe).
- `q`: Voltar/sair.
- `d`: Excluir fluxo atual.
- `r`: Reproduz a solicitação selecionada para o servidor.
- `e`: Edite cabeçalhos, caminho ou corpo de solicitação/resposta.
- `a`: Retoma o fluxo pausado (ao interceptar).
- `A`: Retoma todos os fluxos pausados.

### Filtrando e interceptando fluxos
Pressione `i` para inserir uma expressão de filtro de interceptação:
```text
~u /api/v1/ & ~m POST      # Intercept only POST requests to /api/v1/
~c 401 | ~c 403            # Filter flows by HTTP status code
~b "password"              # Filter flows where body contains "password"
```
---

## ⚡ Automação Não Interativa (`mitmdump`)

### Gravação e reprodução de tráfego
Capture o tráfego ativo diretamente em um arquivo de despejo binário:
```bash
# Record all HTTP traffic to file
mitmdump -w traffic.dump

# Read and inspect captured dump
mitmdump -n -r traffic.dump

# Replay client requests from dump against target
mitmdump --client-replay traffic.dump
```
---

## 🐍 Scripts e complementos Python

mitmproxy apresenta uma API Python nativa para modificar o tráfego programaticamente:

### Script automatizado de manipulação de cabeçalho (`add_headers.py`)
```python
from mitmproxy import http

class AddHeader:
    def request(self, flow: http.HTTPFlow):
        # Inject custom authorization or testing header into every outbound request
        flow.request.headers["X-Security-Audit"] = "Authorized-Pentest"
        flow.request.headers["Authorization"] = "Bearer injected_token_xyz"

addons = [AddHeader()]
```
Corra com:
```bash
mitmproxy -s add_headers.py
```
### Adulteração corporal de resposta ao vivo (`tamper_response.py`)
```python
from mitmproxy import http

def response(flow: http.HTTPFlow):
    # Force admin privilege in returned JSON response
    if "api/user/me" in flow.request.url:
        flow.response.text = flow.response.text.replace('"isAdmin": false', '"isAdmin": true')
```
Corra com:
```bash
mitmdump -s tamper_response.py
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Iniciar TUI interativa** | `mitmproxy -p 8080` |
| **Iniciar GUI da Web** | `mitmweb -p 8080` |
| **Registrador CLI silencioso** | `mitmdump -p 8080` |
| **Alvo de proxy reverso** | `mitmproxy --mode reverse:https://target.com` |
| **Registrar tráfego em arquivo** | `mitmdump -w capture.mitm` |
| **Carregar script de complemento Python**| `mitmproxy -s script.py` |
| **Ignorar hosts de destino** | `mitmproxy --ignore-hosts ".*apple\.com"` |

---

## 🛡️ Considerações defensivas e detecção

1. **Fixação de certificado:** Os aplicativos móveis que aplicam a fixação SSL rejeitam o certificado CA do mitmproxy. Combine com Frida/Objeção para ignorar as verificações do lado do cliente.
2. **Detecção de proxy no handshake TLS:** Os proxies introduzem conjuntos de criptografia TLS personalizados e impressões digitais de negociação ALPN que WAFs e gerenciadores de bot podem detectar.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> mitmproxy intercepta e descriptografa comunicações de rede confidenciais. Deve ser estritamente implantado em sistemas e redes onde a autorização explícita tenha sido garantida. Interceptar comunicações sem consentimento é ilegal.