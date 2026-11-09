<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# ⚡ Naabu - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███╗   ██╗ █████╗  █████╗ ██████╗ ██╗   ██╗
  ████╗  ██║██╔══██╗██╔══██╗██╔══██╗██║   ██║
  ██╔██╗ ██║███████║███████║██████╔╝██║   ██║
  ██║╚██╗██║██╔══██║██╔══██║██╔══██╗██║   ██║
  ██║ ╚████║██║  ██║██║  ██║██████╔╝╚██████╔╝
  ╚═╝  ╚═══╝╚═╝  ╚═╝╚═╝  ╚═╝╚═════╝  ╚═════╝ 
        Fast & Reliable SYN/CONNECT Port Scanner
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/Naabu-Port_Scanner-blue?style=for-the-badge" alt="Naabu">
  <img src="https://img.shields.io/badge/Author-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconhecimento e digitalização">
  <img src="https://img.shields.io/badge/Language-Go-cyan?style=for-the-badge" alt="Go">
</p>

<p alinhar="centro">
  <b>⚡ Scanner de porta rápido e confiável, focado na simplicidade e na tubulação perfeita com ferramentas ProjectDiscovery (httpx, núcleos)</b>
</p>

---

## 📋 Índice

- [Visão geral e recursos principais](#-visão geral--recursos-chave)
- [Instalação](#-instalação)
- [Verificação básica de portas](#-basic-port-scanning)
  - [Verificação de host único](#verificação de host único)
  - [Portas principais e faixa completa de portas](#top-ports--full-port-range)
- [Modos de digitalização: SYN (soquete bruto) vs CONNECT](#-scan-modes-syn-raw-socket-vs-connect)
- [Limite de taxa e ajuste de desempenho] (#-limitação de taxa - ajuste de desempenho)
- [Verificação de múltiplos alvos e blocos CIDR](#-varredura de vários alvos--cidr-blocks)
- [Fluxos de trabalho de pipeline contínuos](#-fluxos de trabalho de pipeline contínuos)
  - [Naabu + Httpx + Núcleos](#naabu--httpx--núcleos)
  - [Subfinder + Naabu](#subfinder--naabu)
- [Integração Nmap (`-nmap`)](#-nmap-integration--nmap)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e detecção](#-considerações defensivas--detecção)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e principais recursos

Desenvolvido pelo **ProjectDiscovery**, o **Naabu** é um scanner de porta projetado para ser rápido, preciso e otimizado para integração em pipelines modernos de automação de reconhecimento:
- **Verificações SYN & CONNECT:** Fallback automático para TCP CONNECT quando os privilégios de soquete bruto (`libpcap`/root) estão ausentes.
- **Ecossistema ProjectDiscovery:** Arquitetura de pipeline nativa que permite tubulação contínua de `subfinder` para `naabu` e depois para `httpx` e `nuclei`.
- **Mecanismo Nmap integrado:** Capacidade de acionar automaticamente o serviço Nmap ativo e a verificação de versão em portas abertas descobertas.
- **Exclusão de CDN:** Ignora automaticamente os nós de borda CDN da nuvem (Cloudflare, Akamai, CloudFront) para evitar desperdício de largura de banda e falsos positivos.

---

## 📥 Instalação
```bash
# Using Go (Recommended)
go install -v github.com/projectdiscovery/naabu/v2/cmd/naabu@latest

# Binary download via pdtm (ProjectDiscovery Tool Manager)
pdtm -i naabu

# Arch Linux (AUR)
yay -S naabu-bin

# Kali Linux
sudo apt update && sudo apt install -y naabu
```
> [!NOTA]
> No Linux, conceder `CAP_NET_RAW` permite executar varreduras SYN sem root:
>`sudo setcap cap_net_raw=+ep $(which naabu)`

---

## 🚀 Verificação básica de portas

### Verificação de host único
```bash
# Scan default top 100 ports
naabu -host example.com

# Scan specific ports
naabu -host example.com -p 80,443,8080,8443
```
### Principais portas e gama completa de portas
```bash
# Scan top 1000 ports
naabu -host example.com -top-ports 1000

# Full port scan (1-65535)
naabu -host example.com -p -
```
---

## ⚡ Modos de varredura: SYN (soquete bruto) vs CONNECT

- **SYN Scan (`-s syn` / root necessário):** Envia pacotes TCP SYN brutos sem terminar o handshake de 3 vias (scan semi-aberto). Muito mais rápido e menos barulhento nos logs de aplicativos.
- **CONNECT Scan (`-s conn`):** Usa a chamada de soquete padrão do SO `connect()`. Opera sem privilégios de root e penetra em ambientes virtuais restritivos.
```bash
# Explicit SYN Scan (Requires sudo or CAP_NET_RAW)
sudo naabu -host example.com -s syn

# Explicit TCP Connect Scan (No root required)
naabu -host example.com -s conn
```
---

## ⚙️ Limitação de taxa e ajuste de desempenho
```bash
# Adjust packet rate per second (default: 1000)
sudo naabu -host example.com -rate 2500

# Set connection timeout in milliseconds
naabu -host example.com -timeout 1000

# Exclude CDN edge servers (Cloudflare, CloudFront, Fastly)
naabu -host example.com -exclude-cdn
```
---

## 🌐 Varredura multialvo e blocos CIDR
```bash
# Scan targets from list
naabu -list targets.txt -p 80,443,8080 -o open_ports.txt

# Scan a /24 subnet block
sudo naabu -host 192.168.1.0/24 -p 22,80,443,445,3389
```
---

## 🔄 Fluxos de trabalho de pipeline contínuos

Naabu brilha quando combinado com outras ferramentas do ProjectDiscovery:

### Subfinder + Naabu
Enumere subdomínios e descubra imediatamente quais deles têm portas web/SSH abertas:
```bash
subfinder -d example.com -silent | naabu -silent -p 80,443,8080,8443
```
### Naabu + Httpx + Núcleos
O pipeline ofensivo canônico de reconhecimento de recompensas de bugs:
```bash
# 1. Port scan subdomains -> 2. Probe HTTP services -> 3. Vulnerability scan
subfinder -d target.com -silent \
  | naabu -silent -p 80,443,8080,8443 \
  | httpx -silent \
  | nuclei -severity medium,high,critical -o findings.txt
```
---

## 🔍 Integração Nmap (`-nmap`)

Passe as portas abertas descobertas diretamente para o Nmap para detecção de serviço (`-sV`) e varredura de script (`-sC`):
```bash
sudo naabu -host target.local -top-ports 1000 -nmap-cli 'nmap -sV -sC -oN naabu_nmap.txt'
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificação padrão (100 principais)** | `naabu -host <TARGET>` |
| **Varredura completa (1-65535)**| `naabu -host <TARGET> -p -` |
| **1000 principais portas** | `naabu -host <TARGET> -top-ports 1000` |
| **Verificação SYN (Rápida, Furtiva)**| `sudo naabu -host <TARGET> -s syn` |
| **Excluir IPs de borda CDN** | `naabu -host <TARGET> -exclude-cdn` |
| **Saída para JSON** | `naabu -host <TARGET> -json -o results.json` |
| **Encaminhar para HTTPX** | `naabu -host <TARGET> -silent \| httpx` |
| **Acionar verificação do serviço Nmap**| `sudo naabu -host <TARGET> -nmap-cli 'nmap -sV'` |

---

## 🛡️ Considerações defensivas e detecção

1. **Regras de Firewall e IDS:** Varreduras SYN de alta taxa geradas pelo Naabu produzem altas taxas TCP RST/SYN facilmente capturadas pelas regras Snort/Suricata.
2. **Limitação de taxa de rede:** Imponha limitação de taxa de conexão de entrada (`iptables -m limit --limit 50/s`) para desacelerar e interromper a verificação automatizada.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O Naabu foi desenvolvido estritamente para avaliações de segurança autorizadas, testes de penetração e descoberta de ativos defensivos. A varredura de redes sem permissão explícita é estritamente proibida.