<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📁 DIRB - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ██████╗ ██╗██████╗ ██████╗ 
  ██╔══██╗██║██╔══██╗██╔══██╗
  ██║  ██║██║██████╔╝██████╔╝
  ██║  ██║██║██╔══██╗██╔══██╗
  ██████╔╝██║██║  ██║██████╔╝
  ╚═════╝ ╚═╝╚═╝  ╚═╝╚═════╝ 
        Web Content Scanner
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/DIRB-Content_Scanner-orange?style=for-the-badge" alt="DIRB">
  <img src="https://img.shields.io/badge/Wordlists-Bruteforce-blue?style=for-the-badge" alt="Listas de palavras">
  <img src="https://img.shields.io/badge/Kali_Linux-Preinstalled-green?style=for-the-badge" alt="Kali Linux">
  <img src="https://img.shields.io/badge/Web-Pentesting-red?style=for-the-badge" alt="Web Pentesting">
</p>

<p alinhar="centro">
  <b>🔍 Verificador de conteúdo web clássico lançando ataques baseados em dicionário contra servidores web</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [DIRB vs alternativas modernas](#-dirb-vs-modern-alternatives)
- [Instalação](#-instalação)
- [Listas de palavras padrão](#-default-wordlists)
- [Sintaxe Básica](#-sintaxe básica)
- [Cenários de verificação comuns](#-common-scanning-scenarios)
- [Opções avançadas e ajuste](#-opções avançadas--ajuste)
- [Caça de vulnerabilidades com listas integradas](#-caça de vulnerabilidades com listas integradas)
- [Fluxos de trabalho do mundo real](#-fluxos de trabalho do mundo real)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução

**DIRB** (Directory Buster) é um scanner de conteúdo da Web clássico, leve e baseado em URL, escrito em C. Ele lança ataques baseados em dicionário contra servidores da Web, procurando objetos da Web existentes e ocultos (arquivos, diretórios, scripts e endpoints).

O DIRB funciona gerando solicitações HTTP para cada palavra em uma lista de palavras, analisando os códigos de resposta do servidor (200, 301, 302, 403, etc.) e determinando se existe um recurso.

### Principais características

- ✅ **Verificação recursiva automática:** Verifica os subdiretórios descobertos automaticamente por padrão.
- ✅ **Listas de palavras especializadas integradas:** Vem com listas de palavras direcionadas para servidores web comuns, CMSs e vulnerabilidades.
- ✅ **Baixa pegada de recursos:** Binário leve que funciona perfeitamente mesmo em hardware de baixa especificação ou sistemas mais antigos.
- ✅ **Suporte a cabeçalho personalizado e proxy:** Fácil integração com Burp Suite e proxies HTTP upstream.

---

## ⚖️ DIRB vs alternativas modernas

| Recurso | DIRB | Gobuster | affu | Feroxbuster | pesquisa direta |
|---------|------|----------|------|------------|-----------|
| **Idioma** | C | Vá | Vá | Ferrugem | Pitão |
| **Velocidade** | ⭐⭐ (Fio baixo) | ⭐⭐⭐⭐⭐ (Rápido) | ⭐⭐⭐⭐⭐ (Extremamente rápido) | ⭐⭐⭐⭐⭐ (ferrugem assíncrona) | ⭐⭐⭐⭐ (Rápido) |
| **Recursão padrão** | ✅ Sim (Automático) | ❌ Não | ❌ Não (Opcional `-recursion`) | ✅ Sim (Automático) | ✅ Sim (`-r`) |
| **Listas de palavras personalizadas incluídas** | ✅ Sim (`/usr/share/dirb/`) | ❌ Não | ❌ Não | ❌ Não | ✅ Sim (`db/dicc.txt`) |
| **VHost Fuzzing** | ❌ Não | ✅ Sim | ✅ Sim | ❌ Não | ✅ Sim (`--subdirs`) |
| **POST/difusão de dados** | ❌ Não | ❌ Não | ✅ Sim | ❌ Não | ✅ Sim (`-d`) |
| **Uso de memória/CPU** | Muito baixo | Baixo | Baixo | Muito baixo | Moderado |

---

## 📥 Instalação

### Kali Linux e Parrot OS

DIRB vem pré-instalado no Kali Linux e Parrot OS:
```bash
# Verify installation
dirb

# Update to latest package
sudo apt update && sudo apt install -y dirb
```
###Debian/Ubuntu
```bash
sudo apt update
sudo apt install -y dirb
```
### Compilando a partir do código-fonte
```bash
# Install build dependencies
sudo apt install -y build-essential libcurl4-openssl-dev

# Download and extract source
wget https://sourceforge.net/projects/dirb/files/dirb/2.22/dirb222.tar.gz
tar -xzvf dirb222.tar.gz
cd dirb222

# Configure permissions and compile
chmod -R 755 conf
chmod 755 configure
./configure
make
sudo make install
```
---

## 📚 Listas de palavras padrão

Quando instalado no Kali Linux, o DIRB inclui listas de palavras dedicadas localizadas em `/usr/share/dirb/wordlists/`:

| Caminho da lista de palavras | Entradas | Finalidade |
|---------------|---------|---------|
| `/usr/share/dirb/wordlists/common.txt` | ~4.614 | Lista padrão usada quando nenhuma lista de palavras é especificada |
| `/usr/share/dirb/wordlists/small.txt` | ~959 | Descoberta rápida de caminhos de alta probabilidade |
| `/usr/share/dirb/wordlists/big.txt` | ~20.469 | Pesquisa exaustiva de diretórios e arquivos |
| `/usr/share/dirb/wordlists/catala.txt` | ~6.846 | Lista de palavras catalãs |
| `/usr/share/dirb/wordlists/spanish.txt` | ~11.720 | Lista de palavras em espanhol |
| `/usr/share/dirb/wordlists/mutations_common.txt` | ~1.000 | Variações comuns de prefixo/sufixo |
| `/usr/share/dirb/wordlists/extensions_common.txt` | ~40 | Extensões da web padrão (`.php`, `.asp`, `.html`, etc.) |

### Listas de palavras de vulnerabilidade integradas (`/usr/share/dirb/wordlists/vulns/`)

- `apache.txt` - arquivos, páginas de status e diretórios conhecidos específicos do Apache
- `iis.txt` - arquivos do servidor web Microsoft IIS (`web.config`, `aspnet_client`, etc.)
- `tomcat.txt` - Administração do Apache Tomcat e caminhos de gerenciamento
- `cgis.txt` - Scripts e endpoints da Common Gateway Interface (CGI)
-`fatwire.txt`, `hpsmh.txt`, `hyperion.txt`, `iplanet.txt`, `netware.txt`, `oracle.txt`

---

## ⌨️ Sintaxe Básica
```bash
dirb <url_base> [<wordlist_file(s)>] [options]
```
### Exemplos essenciais
```bash
# Basic scan using default common.txt wordlist
dirb http://10.10.10.10

# Scan using a specific custom wordlist
dirb https://example.com /usr/share/wordlists/dirb/big.txt

# Scan with custom SecLists directory list
dirb http://target.local /usr/share/seclists/Discovery/Web-Content/directory-list-2.3-medium.txt
```
---

## 🔍 Cenários comuns de verificação

### 1. Digitalizando com extensões de arquivo (`-X` e `-x`)

Pesquise extensões de arquivo específicas anexadas a cada palavra no dicionário:
```bash
# Test specific extensions on every wordlist entry
dirb http://target.local -X .php,.html,.txt,.bak

# Use an extensions list file
dirb http://target.local -x /usr/share/dirb/wordlists/extensions_common.txt

# Combine directory brute-force with extension brute-force
dirb http://target.local /usr/share/dirb/wordlists/common.txt -X .php,.json,.zip
```
### 2. Controle de recursão (`-r` e `-R`)

Por padrão, o DIRB rastreia recursivamente cada diretório descoberto. Você pode controlar esse comportamento:
```bash
# Disable recursion completely (scan root level only)
dirb http://target.local -r

# Interactive recursion (DIRB asks before descending into each directory)
dirb http://target.local -R
```
### 3. Verificação sem distinção entre maiúsculas e minúsculas (`-i`)

Útil ao direcionar servidores Windows/IIS onde os caminhos de arquivo não diferenciam maiúsculas de minúsculas:
```bash
dirb http://target.local -i
```
### 4. Ignorando códigos de status HTTP específicos (`-N`)

Filtre códigos de resposta indesejados (por exemplo, loops de redirecionamento ou caminhos proibidos):
```bash
# Ignore 302 redirects and 403 forbidden responses
dirb http://target.local -N 302,403
```
---

## ⚙️ Opções avançadas e ajuste

### Autenticação (`-u`)

DIRB suporta autenticação HTTP Basic e Digest:
```bash
# Supply HTTP authentication credentials
dirb http://target.local -u "admin:SecretP@ssw0rd!"

# Combine auth with custom wordlist
dirb http://target.local /usr/share/dirb/wordlists/big.txt -u "pentester:Summer2026"
```
### Cookies personalizados e tokens de sessão (`-c`)

Teste rotas de aplicativos web autenticadas fornecendo cookies de sessão:
```bash
# Pass session cookie
dirb http://target.local -c "PHPSESSID=d92j4fn82mdf82ncb184; logged_in=true"
```
### Agente de usuário personalizado e cabeçalhos HTTP (`-a` e `-H`)

Ignore as regras básicas do WAF ou simule navegadores legítimos:
```bash
# Set custom User-Agent
dirb http://target.local -a "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"

# Add custom HTTP Header
dirb http://target.local -H "Authorization: Bearer eyJhbGciOi..."
dirb http://target.local -H "X-Forwarded-For: 127.0.0.1"
```
### Roteamento de proxy e integração com o Burp Suite (`-p` e `-P`)

Envie tráfego DIRB por meio de um proxy HTTP upstream como Burp Suite ou OWASP ZAP:
```bash
# Route through local Burp Suite proxy
dirb http://target.local -p 127.0.0.1:8080

# Authenticated proxy
dirb http://target.local -p 10.10.10.1:3128 -P "proxyuser:proxypass"
```
### Limitação de taxa e evasão (`-z`)

Evite disparar alertas IDS/IPS ou fazer DoS em serviços web frágeis adicionando um atraso entre as solicitações (em milissegundos):
```bash
# Wait 250 milliseconds between each request
dirb http://target.local -z 250

# Slow, stealthy scan (1 second delay)
dirb http://target.local -z 1000
```
### Registro de saída (`-o` e `-w`)
```bash
# Save results to a file
dirb http://target.local -o /home/zeus/scans/dirb_target.txt

# Do not halt on WARNING messages (useful for uninterrupted scans)
dirb http://target.local -w -o dirb_output.txt
```
---

## 🎯 Caça a vulnerabilidades com listas integradas

DIRB inclui listas direcionadas em `/usr/share/dirb/wordlists/vulns/` para detectar servidores não corrigidos e endpoints perigosos:
```bash
# Target Apache server vulnerabilities and status pages
dirb http://target.local /usr/share/dirb/wordlists/vulns/apache.txt

# Target IIS endpoints and misconfigurations
dirb http://target.local /usr/share/dirb/wordlists/vulns/iis.txt

# Target Tomcat management consoles
dirb http://target.local /usr/share/dirb/wordlists/vulns/tomcat.txt

# Search for old CGI scripts and shellshock entrypoints
dirb http://target.local /usr/share/dirb/wordlists/vulns/cgis.txt
```
---

## 🎬 Fluxos de trabalho do mundo real

### Fluxo de trabalho 1: reconhecimento inicial rápido

Quando você encontra pela primeira vez um servidor web na porta 80/443:
```bash
# Rapid scan without recursion using small wordlist
dirb http://10.10.10.50 /usr/share/dirb/wordlists/small.txt -r
```
### Fluxo de trabalho 2: avaliação exaustiva de aplicativos da Web

Execute uma verificação profunda em busca de backups de configuração, código-fonte e endpoints de API:
```bash
dirb https://app.target.local /usr/share/dirb/wordlists/common.txt \
  -X .php,.bak,.sql,.config,.txt \
  -a "Mozilla/5.0" \
  -w \
  -o dirb_app_scan.txt
```
### Fluxo de trabalho 3: rastreamento de portal autenticado via Burp Suite
```bash
dirb https://internal.target.local/portal/ /usr/share/dirb/wordlists/big.txt \
  -c "session=s%3A_kJw81..." \
  -p 127.0.0.1:8080 \
  -X .jsp,.do,.action \
  -z 100
```
---

## 📊 Tabela de referência rápida

| Bandeira | Argumento | Descrição | Exemplo |
|------|----------|-------------|---------|
| `-a` | `<user_agent>` | Especifique a string User-Agent personalizada | `dirb http://target -a "Mozilla/5.0"` |
| `-c` | `<cookie>` | Definir cabeçalho de cookie HTTP | `dirb http://target -c "id=123"` |
| `-H` | `<header>` | Adicionar cabeçalho HTTP personalizado | `dirb http://target -H "X-API-Key: secret"` |
| `-i` | Nenhum | Pesquisa sem distinção entre maiúsculas e minúsculas | `dirb http://target -i` |
| `-N` | `<codes>` | Ignorar respostas que correspondam ao código de status | `dirb http://target -N 302,403` |
| `-o` | `<output_file>` | Salvar os resultados da verificação em arquivo | `dirb http://target -o results.txt` |
| `-p` | `<proxy:port>` | Usar proxy HTTP | `dirb http://target -p 127.0.0.1:8080` |
| `-P` | `<user:pass>` | Credenciais de autenticação de proxy | `dirb http://target -p proxy:8080 -P u:p` |
| `-r` | Nenhum | Não pesquise recursivamente | `dirb http://target -r` |
| `-R` | Nenhum | Pesquisa recursiva interativa | `dirb http://target -R` |
| `-u` | `<user:pass>` | Autenticação HTTP Básica/Digest | `dirb http://target -u "admin:1234"` |
| `-w` | Nenhum | Não interrompa a verificação de mensagens de AVISO | `dirb http://target -w` |
| `-X` | `<ext_list>` | Anexar extensões separadas por vírgula | `dirb http://target -X .php,.html` |
| `-x` | `<ext_file>` | Anexar extensões do arquivo | `dirb http://target -x ext.txt` |
| `-z` | `<millisecs>` | Adicionar atraso entre solicitações | `dirb http://target -z 250` |

---

## 💡 Dicas e práticas recomendadas

1. **Cuidado com as respostas Wildcard 200/302:**
   - Alguns aplicativos da web retornam HTTP 200 ou 302 para cada solicitação (manipulador 404 personalizado). O DIRB reportará cada entrada conforme encontrada. Use `-N 302` ou mude para `ffuf` / `feroxbuster` para filtragem avançada de tamanho/palavra.
2. **Combine com `-r` para dicionários grandes:**
   - Se estiver usando `directory-list-2.3-medium.txt` (220 mil palavras), sempre desative a recursão com `-r`. Caso contrário, o DIRB poderá levar dias para rastrear diretórios profundamente aninhados.
3. **Sempre verifique as extensões:**
   - Os back-ends modernos frequentemente expõem arquivos confidenciais como `.env`, `.git`, `.bak`, `.conf` ou `.sql`. Sempre teste extensões comuns com `-X`.
4. **Use `-w` para verificações autônomas:**
   - Por padrão, o DIRB pode pausar se encontrar avisos de SSL ou de rede. A aprovação no `-w` garante a conclusão dos testes sem interação manual.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** O DIRB foi projetado para auditoria de segurança, testes de penetração e pesquisas éticas em sistemas que você possui ou tem permissão explícita e documentada para testar. Ataques de dicionário não autorizados contra servidores web podem causar interrupção do serviço e são ilegais de acordo com a legislação mundial sobre crimes informáticos.

---

## 📚 Recursos

- [Projeto DIRB SourceForge](https://sourceforge.net/projects/dirb/)
- [Documentação do pacote Kali Linux DIRB](https://www.kali.org/tools/dirb/)
- [SecLists - listas de palavras para descoberta de conteúdo da Web](https://github.com/danielmiessler/SecLists)