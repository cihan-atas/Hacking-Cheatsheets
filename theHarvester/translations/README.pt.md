<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# folha de dicas do Harvester

[theHarvester](https://github.com/laramies/theHarvester) é uma das ferramentas de inteligência de código aberto (OSINT) mais amplamente utilizadas para coletar e-mails, subdomínios, nomes de funcionários, hosts virtuais, portas abertas e chaves PGP de funcionários de diversas fontes públicas (mecanismos de pesquisa, servidores de chaves PGP, Shodan, Censys, Certspotter, etc.).

---

## Índice

- [Fontes de dados suportadas](#supported-data-sources)
- [Instalação e configuração de chave de API](#installation--api-key-configuration)
- [Reconhecimento de domínio básico](#reconhecimento de domínio básico)
- [Colheita passiva de múltiplas fontes](#colheita passiva de múltiplas fontes)
- [Verificação de DNS ativo e força bruta](#active-dns-verification--brute-forcing)
- [Formatos de saída (HTML, XML, JSON)](#output-formats-html-xml-json)
- [Integração de fluxo de trabalho Pentest](#pentest-workflow-integration)

---

## Fontes de dados suportadas

theHarvester agrega inteligência de dezenas de fontes passivas e ativas:
- **Mecanismos de pesquisa (gratuitos/sem chave):** Google, Bing, DuckDuckGo, Yahoo, Baidu.
- **Transparência do certificado:** CRT.sh, Certspotter.
- **Repositórios de códigos e ameaças:** GitHub, AlienVault OTX, ThreatCrowd, Rapid7, VirusTotal.
- **APIs de reconhecimento e OSINT:** Shodan, Censys, Hunter.io, SecurityTrails, Intelx, FullContact.
- **Rede/DNS:** Anubis, HackerTarget, DNSDumpster.

---

## Instalação e configuração da chave API

### 1. Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y theharvester

# From GitHub Source (Latest Version)
git clone https://github.com/laramies/theHarvester.git
cd theHarvester
python3 -m pip install -r requirements/base.txt

# Test binary
theHarvester -h
```
### 2. Configurar chaves de API (`api-keys.yaml`)
Muitas fontes (Hunter, Shodan, Censys, Intelx, GitHub) exigem chaves de API gratuitas ou pagas.
Crie ou edite `api-keys.yaml` no caminho de configuração:
```yaml
apikeys:
  hunter:
    key: "YOUR_HUNTER_KEY"
  shodan:
    key: "YOUR_SHODAN_KEY"
  github:
    key: "YOUR_GITHUB_TOKEN"
  securityTrails:
    key: "YOUR_SECURITYTRAILS_KEY"
```
---

## Reconhecimento Básico de Domínio

### 1. Colete e-mails e subdomínios usando mecanismos gratuitos
```bash
theHarvester -d example.com -l 500 -b duckduckgo,bing,crtsh
```
- `-d <domain>`: Domínio alvo para reconhecimento.
- `-l <limit>`: Número de resultados a consultar por motor.
- `-b <sources>`: lista de fontes separadas por vírgulas (ou `all`).

### 2. Consulte todas as fontes configuradas
```bash
theHarvester -d example.com -l 500 -b all
```
---

## Colheita passiva de múltiplas fontes

Consulte bancos de dados específicos de inteligência contra ameaças e transparência de certificados sem enviar tráfego ao alvo:

### 1. Transparência de certificados e ameaças Intel
```bash
theHarvester -d example.com -b crtsh,certspotter,anubis,hackertarget,otx
```
### 2. Alvo para grupos de origem específicos
```bash
# Query GitHub commits and issues for leaked emails and subdomains
theHarvester -d example.com -b github-code

# Query Shodan for discovered IP addresses and open ports
theHarvester -d example.com -b shodan
```
---

## Verificação de DNS ativo e força bruta

### 1. Resolver subdomínios descobertos (`-r`)
Execute pesquisas de DNS ativas para verificar se os subdomínios coletados estão ativos no momento:
```bash
theHarvester -d example.com -b crtsh,bing -r
```
### 2. Força bruta de DNS (`-c`)
Execute a força bruta de subdomínio DNS baseada em lista de palavras diretamente no Harvester:
```bash
theHarvester -d example.com -b duckduckgo -c
```
### 3. Verificação de host virtual (`-v`)
Verifique hosts virtuais por meio de solicitações HTTP:
```bash
theHarvester -d example.com -b duckduckgo -v
```
---

## Formatos de saída (HTML, XML, JSON)

Exporte resultados para relatórios automatizados ou reconhecimento downstream:

### 1. Exportar para HTML e XML
```bash
theHarvester -d example.com -b duckduckgo,crtsh -f /tmp/theharvester_results
```
Cria `/tmp/theharvester_results.html` e `/tmp/theharvester_results.xml`.

### 2. Analise subdomínios descobertos com `jq`
Se estiver exportando ou analisando JSON:
```bash
theHarvester -d example.com -b crtsh,duckduckgo -f recon_out
# Extract clean host list
cat recon_out.xml | grep -oP '(?<=<host>).*?(?=</host>)' | sort -u > live_hosts.txt
```
---

## Integração de fluxo de trabalho Pentest

Encadeie os resultados do theHarvester diretamente com scanners de vulnerabilidade ativos:
```bash
# 1. Harvest passive subdomains
theHarvester -d target.corp -b crtsh,duckduckgo,bing -f target_recon

# 2. Extract unique domain names
grep -oP '(?<=<host>)[^<]+' target_recon.xml | sort -u > hosts.txt

# 3. Probe for live web services using httpx
cat hosts.txt | httpx -silent -status-code -title > live_web.txt

# 4. Scan live web services with Nuclei
nuclei -l live_web.txt -severity medium,high,critical
```