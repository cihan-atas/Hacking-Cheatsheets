<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# wafw00f Folha de dicas

[wafw00f](https://github.com/EnableSecurity/wafw00f) é uma ferramenta de código aberto de impressão digital e detecção de linha de comando do Web Application Firewall (WAF). Ele identifica e identifica mais de 150 soluções de segurança WAF comerciais e de código aberto, protegendo sites de destino, enviando solicitações HTTP normais e provocativas e analisando respostas comportamentais, cabeçalhos e cookies.

---

## Índice

- [Por que WAFs de impressão digital?](#why-fingerprint-wafs)
- [Instalação](#instalação)
- [Verificação básica e de alvo](#basic--target-scanning)
- [Detecção Exaustiva Multi-WAF (`-a`)](#exhaustive-multi-waf-detection--a)
- [Verificação de alvo em massa do arquivo (`-i`)](#varredura de alvo em massa do arquivo--i)
- [Roteamento de proxy e Tor (`-p`)](#proxy--tor-routing--p)
- [Formatos de exportação (JSON, CSV, Texto)](#export-formats-json-csv-text)
- [Listar assinaturas WAF suportadas (`-l`)](#list-supported-waf-signatures--l)
- [Integração de recompensas de bugs e testes de penetração](#bug-bounty--penetration-testing-integration)

---

## Por que WAFs de impressão digital?

O lançamento de ataques ativos na Web (injeção de SQL, XSS, SSRF) contra um alvo protegido sem reconhecimento prévio geralmente resulta em proibições instantâneas de IP ou resultados de vulnerabilidade falso-negativos. Identificar o WAF exato permite:
- **Evasão personalizada:** Seleção de técnicas de desvio específicas do fornecedor (por exemplo, normalização Unicode para ModSecurity, codificação de transferência fragmentada para Cloudflare).
- **Origin IP Hunting:** Se Cloudflare ou Akamai for detectado, gire para encontrar o IP de origem do back-end antes de testar.
- **Reconhecimento de limites de regras:** Ajustando a simultaneidade do scanner e os limites de taxa abaixo dos limites de bloqueio.

---

## Instalação
```bash
# Debian / Ubuntu / Kali Linux
sudo apt-get update && sudo apt-get install -y wafw00f

# Recommended: Install via pipx
pipx install wafw00f

# Via standard Python pip
python3 -m pip install --user wafw00f

# Verify installation
wafw00f --version
```
---

## Verificação básica e de destino

### 1. Digitalize um único domínio
```bash
wafw00f https://example.com
```
*wafw00f envia solicitações HTTP básicas seguidas de padrões maliciosos personalizados para acionar filtros de segurança, identificando assinaturas em cabeçalhos, códigos de status e páginas de erro.*

### 2. Modo de saída detalhado (`-v` / `-vv`)
Inspecione cabeçalhos correspondentes específicos, códigos de status HTTP e heurísticas:
```bash
wafw00f https://example.com -v
```
### 3. Desative o redirecionamento seguinte (`-r`)
Evite que wafw00f siga redirecionamentos 301/302 para separar proxies reversos de borda de aplicativos de back-end:
```bash
wafw00f https://example.com -r
```
---

## Detecção Exaustiva Multi-WAF (`-a`)

Por padrão, wafw00f interrompe a verificação assim que a primeira correspondência de WAF é confirmada. Use `-a` para verificar todas as assinaturas de fornecedores conhecidos (essencial quando um aplicativo fica atrás de uma defesa multicamadas como Cloudflare CDN + AWS WAF):
```bash
wafw00f https://example.com -a
```
---

## Verificação de destino em massa do arquivo (`-i`)

Digitalize uma lista completa de subdomínios ou ativos alvo coletados de `subfinder` ou `amass`:
```bash
wafw00f -i /path/to/domains.txt -o /tmp/waf_results.txt
```
---

## Roteamento de proxy e Tor (`-p`)

Roteie o tráfego de sondagem por meio de proxies para evitar a lista negra de IP ou inspecionar consultas no Burp Suite:

### 1. Rota através do proxy Burp Suite
```bash
wafw00f https://example.com -p http://127.0.0.1:8080
```
### 2. Rota através do proxy Tor SOCKS5 local
```bash
wafw00f https://example.com -p socks5://127.0.0.1:9050
```
---

## Formatos de exportação (JSON, CSV, texto)

Exporte resultados para relatórios automatizados ou ferramentas de reconhecimento downstream:

### 1. Exportar para formato JSON
```bash
wafw00f https://example.com -o results.json --format json
```
### 2. Exportar para planilha CSV
```bash
wafw00f -i targets.txt -o results.csv --format csv
```
---

## Listar assinaturas WAF suportadas (`-l`)

Veja todos os mais de 150 produtos WAF detectáveis:
```bash
wafw00f -l
```
Os principais produtos suportados incluem:
- **Provedores de nuvem:** Cloudflare, AWS WAF, Akamai, Microsoft Azure Web Application Firewall, Google Cloud Armor.
- **Dispositivos empresariais:** F5 BIG-IP ASM, Imperva SecureSphere, Barracuda, Fortinet FortiWeb, Citrix NetScaler.
- **Código aberto/Software:** ModSecurity, NAXSI, Shadow Daemon, OpenResty.

---

## Integração com recompensas de bugs e testes de penetração

Integre wafw00f em um pipeline automatizado de ataque na web:
```bash
#!/usr/bin/env bash
TARGET="https://app.target.corp"

echo "[*] Detecting WAF for $TARGET..."
WAF=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].detected')

if [ "$WAF" == "true" ]; then
    WAF_NAME=$(wafw00f "$TARGET" -o - --format json | jq -r '.[0].firewall')
    echo "[!] Active WAF detected: $WAF_NAME"
    echo "[+] Adjusting sqlmap to use vendor-specific tamper scripts..."
    if [[ "$WAF_NAME" =~ "Cloudflare" ]]; then
        sqlmap -u "$TARGET/item?id=1" --tamper=between,randomcase,space2comment
    fi
else
    echo "[+] No active WAF detected. Proceeding with standard scanner."
    sqlmap -u "$TARGET/item?id=1" --batch
fi
```