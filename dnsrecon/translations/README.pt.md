<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# folha de dicas do dnsrecon

[dnsrecon](https://github.com/darkoperator/dnsrecon) é uma ferramenta abrangente de reconhecimento e enumeração de DNS escrita em Python. Ele oferece suporte a transferências de zona (AXFR), pesquisas reversas, força bruta de domínio, descoberta de registro SRV, detecção de curinga, espionagem de cache e expansão de TLD.

---

## Índice

- [Capacidades principais](#capacidades principais)
- [Instalação](#instalação)
- [Enumeração padrão e transferências de zona (`-d -t std / axfr`)](#standard-enumeration--zone-transfers--d--t-std--axfr)
- [Força bruta de subdomínio DNS (`-t brt`)](#dns-subdomínio-força bruta--t-brt)
- [Varredura de intervalo de pesquisa reversa (`-t rvl`)](#reverse-lookup-range-scanning--t-rvl)
- [Snooping de cache DNS (`-t snoop`)](#dns-cache-snooping--t-snoop)
- [Segmentando servidores de nomes específicos (`-n`)](#targeting-specific-nameservers--n)
- [Formatos de saída (CSV, JSON, SQLite, XML)](#output-formats-csv-json-sqlite-xml)
- [Enforcamento defensivo para servidores DNS](#endurecimento defensivo para servidores DNS)

---

## Capacidades principais

- **Teste de transferência de zona (AXFR):** Testa automaticamente todos os servidores de nomes autorizados para transferências de zona não autenticadas.
- **Enumeração de registros:** recupera registros SOA, NS, A, AAAA, MX, TXT, SPF e SRV.
- **Força bruta de subdomínio:** Pesquisas de dicionário multithread com filtragem de curinga.
- **Verificação reversa de faixa de IP:** Extração de registros PTR em blocos CIDR arbitrários.
- **Cache Snooping:** testa se os servidores de nomes recursivos têm domínios específicos armazenados em cache na memória.
- **Expansão de TLD:** descobre domínios correspondentes entre códigos de país e domínios genéricos de nível superior.

---

## Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y dnsrecon

# Python pip / pipx
pipx install dnsrecon

# From source
git clone https://github.com/darkoperator/dnsrecon.git
cd dnsrecon
pip install -r requirements.txt

# Verify installation
dnsrecon -h
```
---

## Enumeração padrão e transferências de zona (`-d -t std / axfr`)

### 1. Enumeração de registros DNS padrão
Coleta registros SOA, NS, A, AAAA, MX e TXT e tenta automaticamente transferências de zona contra cada NS autoritativo:
```bash
dnsrecon -d example.com -t std
```
### 2. Apenas teste de transferência de zona dedicada (AXFR)
Teste apenas a vulnerabilidade AXFR em todos os servidores de nomes autorizados:
```bash
dnsrecon -d example.com -t axfr
```
*Se for bem-sucedido, todo o banco de dados DNS do alvo será descartado sem força bruta.*

---

## Força bruta de subdomínio DNS (`-t brt`)

Subdomínios de força bruta usando listas de palavras personalizadas ou padrão:

### 1. Força bruta de subdomínio com lista de palavras personalizada
```bash
dnsrecon -d example.com -t brt -D /usr/share/wordlists/dnsmap.txt
```
- `-d <domain>`: Domínio alvo.
- `-t brt`: modo de força bruta.
- `-D <file>`: arquivo de dicionário de subdomínio.

### 2. Força bruta com filtragem curinga
Impede que configurações de DNS curinga (`*.example.com` resolvendo para um único IP pega-tudo) poluam os resultados:
```bash
dnsrecon -d example.com -t brt -D /usr/share/seclists/Discovery/DNS/subdomains-top1million-5000.txt --iw
```
- `--iw`: ignora os resultados da resolução curinga.

---

## Varredura de intervalo de pesquisa reversa (`-t rvl`)

Enumerar registros PTR para um bloco IP para identificar hosts internos, gateways de correio e instâncias de teste:
```bash
dnsrecon -r 192.168.1.0/24 -t rvl
```
- `-r <range>`: intervalo de IP em notação CIDR (por exemplo, `192.168.1.0/24`) ou notação início-fim (`192.168.1.1-192.168.1.254`).
- `-t rvl`: modo de pesquisa reversa.

---

## Espionagem de cache DNS (`-t snoop`)

Determine se os clientes em uma rede de destino resolveram recentemente domínios específicos por meio de seu resolvedor interno:
```bash
dnsrecon -t snoop -n 192.168.1.1 -D sensitive_domains.txt
```
- `-n <resolver_ip>`: servidor DNS de destino a ser consultado.
- `-D <domains_file>`: Lista de domínios a serem testados (por exemplo, concorrentes, C2s maliciosos, portais internos).

---

## Visando servidores de nomes específicos (`-n`)

Consulte servidores DNS internos ou específicos de destino diretamente, em vez de resolvedores upstream públicos:
```bash
# Query the company's internal DNS server directly
dnsrecon -d corp.internal -n 10.10.10.2 -t std
```
---

## Formatos de saída (CSV, JSON, SQLite, XML)

Salve os resultados para integração com tickets, SIEM ou outras fases de ataque:

### 1. Exportar para JSON
```bash
dnsrecon -d example.com -t std --json /tmp/dns_recon.json
```
### 2. Exportar para banco de dados SQLite
```bash
dnsrecon -d example.com -t std --db /tmp/dns_results.sqlite
```
### 3. Exportar para CSV e XML
```bash
dnsrecon -d example.com -t std --csv /tmp/dns_results.csv --xml /tmp/dns_results.xml
```
---

## Endurecimento Defensivo para Servidores DNS

| Vulnerabilidade | Risco de exploração | Defesa Recomendada |
|---|---|---|
| **AXFR irrestrito** | Mapeamento completo da infraestrutura interna | Restrinja as transferências de zona apenas para IPs de servidores de nomes secundários autorizados (`allow-transfer { 10.0.0.2; };` no BIND). |
| **Resolvedor recursivo aberto** | Amplificação de DNS DDoS e espionagem | Desative a recursão externa (`recursion no;` no BIND). |
| **Nomes de host previsíveis** | Força bruta de subdomínio | Use subdomínios aleatórios para ambientes de teste confidenciais. |
| **Aquisição de subdomínio** | CNAME apontando para serviços AWS/GitHub/Azure não reivindicados | Audite registros DNS CNAME pendentes e remova mapeamentos não utilizados imediatamente. |