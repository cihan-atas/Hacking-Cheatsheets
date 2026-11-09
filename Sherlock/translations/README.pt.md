<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# Folha de dicas do Sherlock

[Sherlock](https://github.com/sherlock-project/sherlock) é uma ferramenta de reconhecimento OSINT que rastreia contas de mídia social por nome de usuário em mais de 400 plataformas sociais, fóruns, sites de hospedagem de código e comunidades online simultaneamente.

---

## Índice

- [Capacidades-chave](#capacidades-chave)
- [Instalação](#instalação)
- [Pesquisa de nome de usuário único e múltiplo](#single--multiple-username-search)
- [Filtrando sites e plataformas](#filtering-sites--platforms)
- [Roteamento de proxy e Tor](#proxy--tor-routing)
- [Ajuste de tempo limite e threading](#timeout--threading-tuning)
- [Formatos de exportação (CSV, XLSX, JSON)](#export-formats-csv-xlsx-json)
- [Automação e integração de pipeline OSINT](#automation--osint-pipeline-integration)

---

## Principais recursos

- **Mais de 400 plataformas:** Consulta GitHub, Twitter/X, Instagram, Reddit, TikTok, LinkedIn, Telegram, Discord, Steam, Spotify, Docker Hub, Pastebin e centenas de outras.
- **Simultaneidade assíncrona:** Verificação paralela de alta velocidade usando solicitações Python assíncronas.
- **Detecção precisa:** Detecta códigos de status HTTP, mensagens de erro e redirecionamentos para evitar falsos positivos.
- **Suporte Tor e SOCKS5:** Protege o anonimato do investigador e ignora os limites de taxa.

---

## Instalação
```bash
# Recommended: Install via pipx
pipx install sherlock-project

# Or install from GitHub repository
git clone https://github.com/sherlock-project/sherlock.git
cd sherlock
python3 -m pip install -r requirements.txt

# Debian / Kali Linux repository
sudo apt-get update && sudo apt-get install -y sherlock

# Verify command
sherlock --version
```
---

## Pesquisa de nome de usuário único e múltiplo

### 1. Pesquisa básica de nome de usuário
Pesquise um único identificador de destino em mais de 400 sites:
```bash
sherlock target_user
```
### 2. Pesquise vários nomes de usuário simultaneamente
```bash
sherlock target_user1 target_user2 alias_dev
```
### 3. Imprimir apenas contas encontradas (suprimir não encontradas/erros)
```bash
sherlock --print-found target_user
```
---

## Filtrando sites e plataformas

Restrinja as pesquisas a plataformas específicas para economizar tempo ou focar em categorias de inteligência específicas:

### 1. Consulte apenas sites específicos
```bash
sherlock --site github --site twitter --site reddit --site gitlab target_user
```
### 2. Pesquise sem plataformas específicas (ignore sites pesados ​​​​de falsos positivos)
```bash
sherlock target_user --site-list targets.txt
```
---

## Roteamento de proxy e Tor

Para evitar proibições de IP, limites de taxas e manter a segurança operacional:

### 1. Rota pela rede Tor
Roteie solicitações automaticamente por meio do proxy Tor SOCKS local (`127.0.0.1:9050`):
```bash
sherlock --tor target_user
```
### 2. Proxy HTTP / SOCKS5 personalizado
```bash
# SOCKS5 proxy
sherlock --proxy socks5://127.0.0.1:1080 target_user

# Authenticated HTTP proxy
sherlock --proxy http://user:pass@proxy.corp.internal:8080 target_user
```
---

## Ajuste de tempo limite e threading

Controle as velocidades de solicitação e as tolerâncias de latência:
```bash
# Set HTTP timeout to 5 seconds per site (default is usually 60s)
sherlock target_user --timeout 5

# Add delay between requests to avoid rate limits
sherlock target_user --delay 1
```
---

## Formatos de exportação (CSV, XLSX, JSON)

Salve descobertas para investigações, conjuntos de evidências ou relatórios:

### 1. Salve diretamente em arquivo CSV
```bash
sherlock --csv --output /tmp/sherlock_results target_user
```
### 2. Salve a saída em um diretório específico
```bash
sherlock --folderoutput /tmp/recon_data/ target_user
```
Gera `/tmp/recon_data/target_user.txt` contendo URLs completos de perfis confirmados.

### 3. Salvar como planilha Excel (XLSX)
```bash
sherlock --xlsx target_user
```
---

## Automação e integração de pipeline OSINT

Encadeie Sherlock em um script de investigador automatizado:
```bash
#!/usr/bin/env bash
TARGET="$1"
OUTDIR="/tmp/osint_${TARGET}"
mkdir -p "$OUTDIR"

echo "[*] Scanning for username: $TARGET"
sherlock --print-found --timeout 10 --folderoutput "$OUTDIR" "$TARGET"

if [ -f "$OUTDIR/${TARGET}.txt" ]; then
    echo "[+] Found $(wc -l < "$OUTDIR/${TARGET}.txt") accounts:"
    cat "$OUTDIR/${TARGET}.txt"
fi
```