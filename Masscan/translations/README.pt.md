<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Masscan - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███╗   ███╗ █████╗ ███████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ████╗ ████║██╔══██╗██╔════╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██╔████╔██║███████║███████╗███████╗██║     ███████║██╔██╗ ██║
  ██║╚██╔╝██║██╔══██║╚════██║╚════██║██║     ██╔══██║██║╚██╗██║
  ██║ ╚═╝ ██║██║  ██║███████║███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝     ╚═╝╚═╝  ╚═╝╚══════╝╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Fastest Internet Port Scanner - Asynchronous 10M Packets/Second
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Masscan-Internet_Port_Scanner-blue?style=for-the-badge" alt="Masscan">
  <img src="https://img.shields.io/badge/Language-C-orange?style=for-the-badge" alt="Idioma C">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconhecimento e digitalização">
  <img src="https://img.shields.io/badge/License-AGPLv3-red?style=for-the-badge" alt="Licença AGPLv3">
</p>

<p alinhar="centro">
  <b>🌐 Scanner de porta TCP assíncrono de alto desempenho, capaz de escanear toda a Internet IPv4 em menos de 5 minutos</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura assíncrona](#-overview--the-asynchronous-architecture)
- [Comparação de ferramentas: Masscan vs Nmap vs RustScan](#-tool-comparison-masscan-vs-nmap-vs-rustscan)
- [Instalação](#-instalação)
- [Alvos básicos de uso e verificação](#-basic-usage--scanning-targets)
- [Controle de taxa de transmissão (`--rate`)](#-controle de taxa de transmissão---taxa)
- [Especificações de porta (TCP e UDP)](#-port-specifications-tcp--udp)
- [Captura de banner e sondagem de protocolo básico](#-captura de banner--sondagem de protocolo básico)
- [Exclusões de alvos e listas negras (`--excludefile`)](#-exclusões de alvos--listas negras---excludefile)
- [Formatos de saída e tubulação no Nmap](#-output-formats--piping-into-nmap)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e limites de hardware](#-considerações defensivas--limites de hardware)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura assíncrona

Criado por **Robert Graham**, o **Masscan** é aclamado como o scanner de porta mais rápido que existe. Ele produz resultados semelhantes ao Nmap, mas funciona internamente como `scanrand`, `unicornscan` e `ZMap`, utilizando **transmissão assíncrona**.

Ao contrário do Nmap, que usa a pilha TCP do kernel do sistema operacional e espera por respostas antes de testar as portas subsequentes, o Masscan incorpora sua própria pilha TCP/IP de espaço de usuário personalizada. Ele envia pacotes SYN brutos a taxas superiores a **10 milhões de pacotes por segundo** sem manter tabelas de estado de conexão.

---

## ⚖️ Comparação de ferramentas: Masscan vs Nmap vs RustScan

| Recurso | Masscan | Nmap | RustScan |
|--------|---------|------|----------|
| **Taxa máxima de pacotes** | 10.000.000 pacotes/seg | ~1.000 pacotes/seg | Limite de soquete do SO |
| **Verificação completa da Internet (/0)**| ✅ Menos de 5 minutos | ❌ Dias/Semanas | ❌ Não projetado para /0 |
| **Implementação de pilha TCP**| Espaço de usuário personalizado Raw SYN| Kernel do SO/soquetes brutos | Soquetes TCP assíncronos do Tokio |
| **Versão profunda e scripts NSE**| ❌ Banners Primitivos | ✅ Mais de 600 scripts NSE | ✅ Transferência via Nmap |
| **Acompanhamento de estado** | Apátridas (Blackrock PRNG) | Conjunto de conexões com estado | Tarefas assíncronas com estado |
| **Escopo alvo ideal** | Blocos classe A/B (/8, /16)| Destinos únicos e sub-redes /24 | Máquinas únicas e /24 sub-redes |

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt update && sudo apt install -y masscan

# Arch Linux
sudo pacman -S masscan

# Build from source (Latest GitHub version)
sudo apt install -y git gcc make libpcap-dev
git clone https://github.com/robertdavidgraham/masscan
cd masscan && make -j$(nproc)
sudo make install
```
---

## 🚀 Uso básico e metas de digitalização
```bash
# Scan a single IP for top web ports
sudo masscan 10.10.10.50 -p80,443,8080 --rate 1000

# Scan an entire Class B internal subnet (172.16.0.0/16) for SMB port 445
sudo masscan 172.16.0.0/16 -p445 --rate 5000

# Scan all 65,535 ports on a specific IP
sudo masscan 10.10.10.50 -p1-65535 --rate 2000
```
> [!IMPORTANTE]
> Como o Masscan gera pacotes brutos via `libpcap`, ele **deve ser executado com privilégios root/sudo**.

---

## ⚡ Controle de taxa de transmissão (`--rate`)

O parâmetro `--rate` determina quantos pacotes por segundo o Masscan transmite. Calibrar isso é fundamental para evitar a saturação dos equipamentos de rede:
```bash
# Gentle rate for home routers / WiFi (100–500 pkts/sec)
sudo masscan 10.10.10.0/24 -p1-1000 --rate 300

# Standard corporate gigabit internal network rate (5,000–10,000 pkts/sec)
sudo masscan 10.0.0.0/8 -p80,443 --rate 10000

# High-speed data center 10Gbps uplink rate (100,000+ pkts/sec)
sudo masscan 0.0.0.0/0 -p443 --rate 100000
```
---

## 🔌 Especificações de porta (TCP e UDP)

Masscan suporta listas de portas TCP e UDP simultaneamente:
```bash
# Specific TCP ports
sudo masscan 10.10.10.50 -p21,22,80,443,445,3389

# Top ports shortcut (similar to Nmap)
sudo masscan 10.10.10.50 --top-ports 100

# UDP ports (prefixed with U:)
sudo masscan 10.10.10.50 -p U:53,U:161,U:123 --rate 500
```
---

## 🔍 Captura de banner e sondagem de protocolo básico

Embora o Masscan seja principalmente um scanner SYN, ele pode realizar handshakes de três vias para capturar banners de aplicativos:
```bash
# Grab HTTP, FTP, and SSH banners
sudo masscan 10.10.10.0/24 -p80,21,22 --banners --rate 1000
```
---

## 🚫 Exclusões de alvo e listas negras (`--excludefile`)

Ao realizar testes de penetração de amplo escopo, sempre exclua infraestruturas críticas e endereços fora dos limites:
```bash
# Exclude government and military ranges or local gateway
sudo masscan 10.0.0.0/8 -p80 --rate 2000 --exclude 10.0.0.1,10.1.1.1

# Use an exclusion file (e.g., standard exclude.conf)
sudo masscan 192.168.0.0/16 -p445 --rate 2000 --excludefile /etc/masscan/exclude.conf
```
---

## 📊 Formatos de saída e tubulação no Nmap

Salve hosts descobertos em formatos greppable ou JSON e, em seguida, alimente-os no Nmap para avaliação completa da vulnerabilidade:
```bash
# Save to JSON format
sudo masscan 10.10.10.0/24 -p80,443 -oJ masscan_results.json

# Save to simple List format (-oL)
sudo masscan 10.10.10.0/24 -p1-65535 --rate 2000 -oL open_ports.txt

# Extract discovered ports and feed directly to Nmap
cat open_ports.txt | awk '{print $4}' | sort -u | paste -sd, - | xargs -I{} nmap -sC -sV -p{} 10.10.10.50
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificar host único (todas as portas)** | `sudo masscan <IP> -p1-65535 --rate 2000` |
| **Verificar sub-rede CIDR em busca de porta** | `sudo masscan <SUBNET>/16 -p<PORT> --rate 5000` |
| **Digitalizar com captura de banner** | `sudo masscan <IP> -p80,22 --banners --rate 1000` |
| **Verificar portas UDP** | `sudo masscan <IP> -p U:53,U:161 --rate 500` |
| **Excluir endereços IP** | `sudo masscan <NET> -p80 --exclude <IP1>,<IP2>` |
| **Saída para JSON** | `sudo masscan <IP> -p80,443 -oJ output.json` |
| **Saída para Lista Simples** | `sudo masscan <IP> -p80,443 -oL output.txt` |

---

## 🛡️ Considerações defensivas e limites de hardware

1. **Falha no roteador do consumidor:** Definir `--rate 100000` em um roteador DSL/cabo doméstico ou Wi-Fi padrão sobrecarregará sua tabela de estado de rastreamento de conexão NAT e travará o roteador. Mantenha `--rate` abaixo de `1000` em conexões residenciais.
2. **Consumo de largura de banda:** A transmissão de 10 milhões de pacotes por segundo consome aproximadamente 5,4 Gbps de largura de banda da rede.
3. **Rejeição de Firewall Local (RST):** Como o Masscan usa uma pilha TCP de espaço de usuário personalizada, seu kernel Linux não sabe sobre conexões abertas e pode enviar pacotes TCP RST automáticos aos alvos. Para evitar isso durante a captura de banners:
   ```bash
   sudo iptables -A INPUT -p tcp --dport 60000 -j DROP
   sudo masscan ... --source-port 60000
   ```
---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Masscan é um utilitário de reconhecimento de rede excepcionalmente poderoso. A varredura de redes não autorizadas, intervalos de IP governamentais ou sub-redes públicas da Internet pode violar as leis de telecomunicações e segurança cibernética. Sempre verifique a autorização por escrito antes de iniciar as verificações.