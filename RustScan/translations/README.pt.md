<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦀 RustScan - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ██╗   ██╗███████╗████████╗███████╗ ██████╗ █████╗ ███╗   ██╗
  ██╔══██╗██║   ██║██╔════╝╚══██╔══╝██╔════╝██╔════╝██╔══██╗████╗  ██║
  ██████╔╝██║   ██║███████╗   ██║   ███████╗██║     ███████║██╔██╗ ██║
  ██╔══██╗██║   ██║╚════██║   ██║   ╚════██║██║     ██╔══██║██║╚██╗██║
  ██║  ██║╚██████╔╝███████║   ██║   ███████║╚██████╗██║  ██║██║ ╚████║
  ╚═╝  ╚═╝ ╚═════╝ ╚══════╝   ╚═╝   ╚══════╝ ╚═════╝╚═╝  ╚═╝╚═╝  ╚═══╝
       The Modern Port Scanner - Scan All 65k Ports in 3 Seconds into Nmap
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/RustScan-Port_Scanner-blue?style=for-the-badge" alt="RustScan">
  <img src="https://img.shields.io/badge/Language-Rust-orange?style=for-the-badge" alt="Language Rust">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconhecimento e digitalização">
  <img src="https://img.shields.io/badge/License-GPLv3-red?style=for-the-badge" alt="Licença GPLv3">
</p>

<p alinhar="centro">
  <b>🌐 Scanner de portas rápido, personalizável e automatizado, projetado para canalizar portas abertas descobertas diretamente em scripts Nmap</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Comparação de ferramentas: RustScan vs Nmap vs Masscan](#-tool-comparison-rustscan-vs-nmap-vs-masscan)
- [Instalação](#-instalação)
- [Uso básico e verificação de host único](#-basic-usage--single-host-scanning)
- [Integração automatizada do Nmap](#-automated-nmap-integration)
- [Ajuste de desempenho (`ulimit` e tamanho do lote)](#-ajuste de desempenho-ulimit--tamanho do lote)
- [Verificação de intervalos de portas e sub-redes CIDR](#-scanning-port-ranges--cidr-subnets)
- [Scripting e formatação de saída](#-scripting--output-formatting)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

**RustScan** é um scanner de porta moderno e de alta velocidade projetado em **Rust**. Sua filosofia central é simples: **escanear portas com extrema rapidez, encontrar portas abertas em todo o espectro 1-65535 em 3 segundos e transferir imediatamente essas portas abertas para o Nmap para serviço abrangente e verificação de vulnerabilidades**.

Em vez de esperar de 10 a 20 minutos para que o `nmap -p-` conclua suas varreduras sequenciais de handshake TCP, o RustScan aproveita o multi-threading assíncrono e o lote de soquetes do sistema operacional para identificar portas abertas quase instantaneamente.

---

## ⚖️ Comparação de ferramentas: RustScan vs Nmap vs Masscan

| Recurso | RustScan | Nmap | Masscan |
|--------|----------|------|--------|
| **Meta principal** | Descoberta rápida -> tubo Nmap | Scripts de serviço profundo e NSE | Varredura SYN de alta taxa em toda a Internet |
| **Velocidade de todas as portas (1-65535)**| ⭐⭐⭐⭐⭐ (~3 segundos) | ⭐⭐ (5–20 minutos) | ⭐⭐⭐⭐⭐ (~2 segundos) |
| **Detecção de versão de serviço**| Através do tubo Nmap | Nativo (`-sV`) | ❌ Mínimo |
| **Mecanismo de script NSE** | Através do tubo Nmap | Nativo (`-sC`, `--script`) | ❌ Não |
| **Mecanismo de pacotes** | Soquetes TCP assíncronos | Pilha IP bruta/TCP personalizada | Pilha SYN assíncrona personalizada |
| **Facilidade de automação** | Argumentos CLI nativos (`--`) | Extensas opções de CLI | Alta complexidade |

---

## 📥 Instalação
```bash
# Debian / Ubuntu / Kali Linux (.deb package)
wget https://github.com/RustScan/RustScan/releases/download/2.3.0/rustscan_2.3.0_amd64.deb
sudo dpkg -i rustscan_2.3.0_amd64.deb

# Arch Linux (AUR)
yay -S rustscan

# Cargo (Rust Package Manager)
cargo install rustscan

# Docker Container
docker run -it --rm --name rustscan rustscan/rustscan:2.1.1 -a 10.10.10.50
```
---

## 🚀 Uso básico e verificação de host único
```bash
# Scan all 65,535 ports on a single IP
rustscan -a 10.10.10.50

# Scan with greppable one-line output
rustscan -a 10.10.10.50 --greppable

# Quiet mode (suppresses the ASCII banner)
rustscan -q -a 10.10.10.50
```
---

## 🔗 Integração automatizada do Nmap

O recurso mais poderoso do RustScan é passar portas abertas diretamente para o Nmap usando o separador `--` final:
```bash
# Scan target and execute default safe scripts & version detection on open ports
rustscan -a 10.10.10.50 -- -sC -sV

# Pass custom Nmap NSE scripts and output options
rustscan -a 10.10.10.50 -- -sC -sV -oN nmap_report.txt

# Run vulnerability audit with Nmap Vuln scripts
rustscan -a 10.10.10.50 -- --script vuln -oA target_vulns
```
---

## ⚡ Ajuste de desempenho (`ulimit` e tamanho do lote)

RustScan depende dos limites do descritor de arquivos do seu sistema operacional. Por padrão, ele se ajusta automaticamente, mas o ajuste manual fornece desempenho máximo:
```bash
# Increase batch size and socket ulimit for high-speed local networks
rustscan -a 10.10.10.50 -b 5000 -u 5000

# Lower batch size for high-latency or packet-dropping WiFi networks
rustscan -a 10.10.10.50 -b 500 --scan-order "random"
```
---

## 🌐 Verificação de intervalos de portas e sub-redes CIDR
```bash
# Scan a specific port range instead of all ports
rustscan -a 10.10.10.50 -r 1-1000

# Scan multiple IP addresses
rustscan -a 10.10.10.50,10.10.10.51

# Scan an entire CIDR subnet range
rustscan -a 192.168.1.0/24
```
---

## 📊 Scripting e formatação de saída
```bash
# Output discovered open ports in JSON format
rustscan -a 10.10.10.50 --accessible

# Read target hosts from a file
rustscan -a 'hosts.txt'
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verifique todas as portas de 65k** | `rustscan -a <IP>` |
| **Verificar todas as portas + scripts Nmap**| `rustscan -a <IP> -- -sC -sV` |
| **Verificar sub-rede CIDR** | `rustscan -a <SUBNET>/24` |
| **Faixa de portas de digitalização** | `rustscan -a <IP> -r 1-1000` |
| **Ajustar tamanho do lote e ulimit** | `rustscan -a <IP> -b 3000 -u 3000` |
| **Modo silencioso (sem banner)** | `rustscan -q -a <IP>` |
| **Saída Greppável** | `rustscan -a <IP> --greppable` |
| **Exportar para arquivo Nmap** | `rustscan -a <IP> -- -sC -sV -oN nmap.txt` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Alertas massivos de ruído e IDS:** A varredura de 65 mil portas em 3 segundos gera milhares de pacotes TCP SYN instantaneamente. Isso acionará imediatamente o Snort, o Suricata e a limitação de taxa do firewall.
2. **Inundação SYN e pacotes descartados:** Em links VPN instáveis ​​(por exemplo, HackTheBox, TryHackMe), tamanhos de lote altos (`-b 4000`) podem causar perda de pacotes. Se os resultados parecerem inconsistentes, reduza o tamanho do lote para `-b 1000`.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas é apenas para testes de penetração autorizados e pesquisas educacionais. A varredura de redes sem autorização é estritamente proibida.