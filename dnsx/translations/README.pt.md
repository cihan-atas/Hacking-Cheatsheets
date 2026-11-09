<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📡 dnsx - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ███╗   ██╗███████╗██╗  ██╗
  ██╔══██╗████╗  ██║██╔════╝╚██╗██╔╝
  ██║  ██║██╔██╗ ██║███████╗ ╚███╔╝ 
  ██║  ██║██║╚██╗██║╚════██║ ██╔██╗ 
  ██████╔╝██║ ╚████║███████║██╔╝ ██╗
  ╚═════╝ ╚═╝  ╚═══╝╚══════╝╚═╝  ╚═╝
       Fast & Multi-Purpose DNS Toolkit by ProjectDiscovery
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/dnsx-DNS_Toolkit-blue?style=for-the-badge" alt="dnsx">
  <img src="https://img.shields.io/badge/Developer-ProjectDiscovery-orange?style=for-the-badge" alt="ProjectDiscovery">
  <img src="https://img.shields.io/badge/Category-Recon_&_Scanning-green?style=for-the-badge" alt="Reconhecimento e digitalização">
  <img src="https://img.shields.io/badge/License-MIT-red?style=for-the-badge" alt="Licença MIT">
</p>

<p alinhar="centro">
  <b>🌐 Resolução DNS de alto desempenho, filtragem de caracteres curinga, pesquisas reversas de DNS e mecanismo de extração de múltiplas consultas</b>
</p>

---

## 📋 Índice

- [Visão geral e arquitetura](#-visão geral--arquitetura)
- [Por que dnsx? (Manipulação e velocidade de curinga)](#-why-dnsx-wildcard-handling--speed)
- [Instalação](#-instalação)
- [Investigação e resolução básica de subdomínio](#-basic-subdomain-probing--resolution)
- [Filtragem de DNS curinga (`-wd`)](#-filtro de DNS curinga---wd)
- [Consultando tipos específicos de registros DNS](#-querying-specific-dns-record-types)
- [Pesquisas reversas de DNS (`-ptr`)](#-reverse-dns-lookups---ptr)
- [Usando resolvedores personalizados e limitação de taxa](#-using-custom-resolvers--rate-limiting)
- [Automação de pipeline (Subfinder + dnsx + httpx)](#-pipeline-automation-subfinder--dnsx--httpx)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e OPSEC](#-considerações defensivas--opsec)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e arquitetura

Projetado em Go por **ProjectDiscovery**, **dnsx** é um kit de ferramentas DNS rápido e multifuncional projetado para reconhecimento, caça a recompensas de bugs e descoberta de rede.

Enquanto ferramentas como `dig`, `nslookup` e `host` resolvem registros um por um, o `dnsx` processa milhares de consultas de forma assíncrona, tenta automaticamente resolvedores com falha, extrai IPs de resposta limpos e elimina ruído curinga que polui a enumeração de subdomínios.

---

## ⚡ Por que dnsx? (Manuseio e velocidade de curinga)

Durante o reconhecimento de recompensas de bugs, ferramentas passivas (`subfinder`, `amass`) geralmente retornam milhares de subdomínios mortos ou curinga. Testar endpoints HTTP em entradas DNS inativas desperdiça uma largura de banda enorme.

`dnsx` resolve isso:
1. **Filtragem de curingas (`-wd`):** Detecta se `*.domain.com` resolve para um IP pega-tudo e filtra subdomínios fantasmas.
2. **Velocidade e simultaneidade:** resolve mais de 10.000 domínios por minuto em pools de resolução públicos e privados.
3. **Limpar tubulação de saída:** Alimenta diretamente no `httpx` ou `nmap`.

---

## 📥 Instalação
```bash
# Go Install (Recommended)
go install -v github.com/projectdiscovery/dnsx/cmd/dnsx@latest

# Kali Linux & Debian
sudo apt update && sudo apt install -y dnsx

# MacOS (Homebrew)
brew install dnsx

# Verify installation
dnsx -version
```
---

## 🚀 Sondagem e resolução básica de subdomínio
```bash
# Resolve a single domain
echo "target.com" | dnsx

# Resolve a list of subdomains from file
dnsx -l subdomains.txt

# Display domain and its corresponding A record IP
dnsx -l subdomains.txt -a -resp

# Output only resolved IP addresses (ready for Nmap)
dnsx -l subdomains.txt -resp-only
```
---

## 🚫 Filtragem DNS curinga (`-wd`)

Os curingas retornam um IP válido para subdomínios inexistentes (`random123xyz.target.com`), envenenando conjuntos de dados de reconhecimento.
```bash
# Filter wildcard subdomains and print only genuine hosts
dnsx -l subdomains.txt -wd target.com -o valid_hosts.txt

# Filter wildcards with custom threshold
dnsx -l subdomains.txt -wd target.com -wt 3
```
---

## 📋 Consultando tipos específicos de registros DNS

Consulte tipos de registros específicos em milhares de domínios:
```bash
# Query CNAME records (Crucial for Subdomain Takeover detection)
dnsx -l subdomains.txt -cname -resp

# Query Mail Exchanger (MX) records
dnsx -l domains.txt -mx -resp

# Query TXT records (SPF, DKIM, verification tokens)
dnsx -l domains.txt -txt -resp

# Query All standard records (A, AAAA, CNAME, NS, TXT, MX, SOA)
dnsx -l domains.txt -recon
```
---

## 🔄 Pesquisas reversas de DNS (`-ptr`)

Descubra infraestrutura ativa e hosts virtuais realizando pesquisas PTR em um intervalo de IP:
```bash
# Reverse resolve a CIDR IP range to hostnames
dnsx -ptr -l ip_ranges.txt -resp

# Reverse resolve a single subnet
echo "192.168.1.0/24" | dnsx -ptr -resp
```
---

## ⚙️ Usando resolvedores personalizados e limitação de taxa

Evite ser limitado pela taxa pelo seu ISP ou DNS do Google, utilizando um conjunto de resolvedores com curadoria de alta velocidade:
```bash
# Use a custom trusted resolver list
dnsx -l subdomains.txt -r resolvers.txt

# Set thread concurrency and query rate limit (queries per second)
dnsx -l subdomains.txt -t 100 -rl 300
```
---

## ⛓️ Automação de pipeline (Subfinder + dnsx + httpx)

O fluxo de trabalho de reconhecimento definitivo para caçadores de recompensas e testadores de penetração:
```bash
# Discover subdomains -> Validate via dnsx -> Probe live HTTP web services
subfinder -d target.com -silent | \
  dnsx -silent -wd target.com | \
  httpx -silent -status-code -title -tech-detect
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Resolver Subdomínios** | `dnsx -l subdomains.txt` |
| **Extrair apenas endereços IP** | `dnsx -l subdomains.txt -resp-only` |
| **Filtrar domínios curinga** | `dnsx -l subdomains.txt -wd <DOMAIN>` |
| **Extrair CNAME (aquisição)** | `dnsx -l subdomains.txt -cname -resp` |
| **Consultar todos os registros (Recon)** | `dnsx -l domains.txt -recon` |
| **Pesquisa de DNS reverso (PTR)** | `dnsx -l ips.txt -ptr -resp` |
| **Resolvedores e threads personalizados**| `dnsx -l list.txt -r resolvers.txt -t 150` |
| **Saída JSON** | `dnsx -l list.txt -json -o results.json` |

---

## 🛡️ Considerações defensivas e OPSEC

1. **Inundação de DNS:** Disparar dezenas de milhares de consultas de DNS por meio de servidores DNS internos corporativos aumentará as cargas de CPU e acionará alertas SIEM para tunelamento de DNS/DDoS.
2. **Intoxicação por resolvedor:** Sempre use listas de resolvedores confiáveis ​​e verificados para evitar que resolvedores mal-intencionados interceptem consultas.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> Esta folha de dicas é apenas para testes de penetração autorizados e engajamentos de recompensas de bugs. A realização de investigações DNS de alto volume contra infraestruturas não autorizadas é estritamente proibida.