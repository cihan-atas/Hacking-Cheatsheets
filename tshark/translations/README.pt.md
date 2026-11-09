<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🦈 tshark - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ████████╗███████╗██╗  ██╗ █████╗ ██████╗ ██╗  ██╗
  ╚══██╔══╝██╔════╝██║  ██║██╔══██╗██╔══██╗██║ ██╔╝
     ██║   ███████╗███████║███████║██████╔╝█████╔╝ 
     ██║   ╚════██║██╔══██║██╔══██║██╔══██╗██╔═██╗ 
     ██║   ███████║██║  ██║██║  ██║██║  ██║██║  ██╗
     ╚═╝   ╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝╚═╝  ╚═╝
         Terminal Packet Analysis & Dissection Engine
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/tshark-Packet_Analyzer-blue?style=for-the-badge" alt="tshark">
  <img src="https://img.shields.io/badge/CLI-Network_Sniffer-red?style=for-the-badge" alt="CLI Network Sniffer">
  <img src="https://img.shields.io/badge/Wireshark-Engine-green?style=for-the-badge" alt="Wireshark Engine">
  <img src="https://img.shields.io/badge/PCAP-Dissection-orange?style=for-the-badge" alt="Dissecção PCAP">
</p>

<p alinhar="centro">
  <b>🌐 Dissecação profunda de pacotes de rede baseada em terminal, análise de fluxo e extração de campo com tecnologia Wireshark</b>
</p>

---

## 📋 Índice

- [Introdução](#-introdução)
- [Comparação de ferramentas: tshark vs tcpdump vs Wireshark](#-tool-comparison)
- [Instalação e privilégios](#-instalação--privilégios)
- [Captura e reprodução básica](#-captura básica--reprodução)
- [Filtragem de tráfego: captura vs exibição](#-filtering-traffic)
- [Extração de campo (`-T fields`)](#-extração de campo)
- [Pensest Prático e Receitas Forenses](#-pentest-prático--receitas-forenses)
  - [Credencial HTTP e busca de URI](#http-credential--uri-hunting)
  - [Extração de consulta DNS e detecção de tunelamento](#dns-query-extraction--tunneling-detection)
  - [Extração SNI TLS/HTTPS](#tls--https-sni-extraction)
  - [Extração Kerberos AS-REQ / AS-REP](#kerberos-as-req--as-rep-extraction)
  - [Rastreamento de autenticação SMB e NTLM](#smb--ntlm-authentication-tracking)
  - [Auditoria de protocolo de texto claro (FTP, Telnet, SMTP)](#cleartext-protocol-auditing)
  - [Caça de aperto de mão de 4 vias WPA / EAPOL](#wpa--eapol-4-way-handshake-hunting)
- [Seguimento e remontagem de stream](#-stream-following--reassembly)
- [Estatísticas, conversas e endpoints](#-statistics-conversations--endpoints)
- [Descriptografia de tráfego TLS / SSL](#-tls--ssl-traffic-decryption)
- [Formatos de exportação (JSON, CSV, Elasticsearch)](#-export-formats)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Dicas e práticas recomendadas](#-dicas--melhores práticas)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Introdução

**tshark** é o terminal e a contraparte da linha de comando do **Wireshark**. Ele se baseia na biblioteca de captura de pacotes (libpcap/Npcap) e no mecanismo de dissecação de pacotes idênticos ao Wireshark gráfico, tornando-o uma das ferramentas de análise de rede mais poderosas disponíveis em Linux, macOS e Windows.

Ao contrário das ferramentas básicas de sniffer de pacotes que inspecionam apenas cabeçalhos de protocolo, o `tshark` apresenta recursos de inspeção profunda de pacotes em milhares de protocolos, sintaxe de filtro de exibição nativa do Wireshark (`-Y`), extração robusta de campo de protocolo (`-T fields`) e formatação de saída flexível para pipelines de automação (`jq`, scripts de shell, Python).

### Principais recursos

| Recurso | Descrição |
|--------|-------------|
| **Dissecção Profunda** | Inspeciona dados de carga útil em milhares de protocolos de aplicativos |
| **Filtros Wireshark** | Suporte completo para sintaxe de filtro de exibição avançada do Wireshark (`-Y`) |
| **Extração de campo** | Extraia campos de protocolo individuais diretamente (`-T fields -e ...`) |
| **Acompanhamento de transmissão** | Remonta sessões TCP/UDP completas em formatos ASCII, Hex ou raw |
| **Perícia Forense Automatizada**| Scriptável em shell, Python ou canalizado para `jq` via `-T json` |
| **Descriptografia** | Suporta descriptografia TLS em tempo de execução com `SSLKEYLOGFILE` |

---

## ⚖️ Comparação de ferramentas

| Recurso | tubarão | tcpdump | Wireshark |
|--------|--------|---------|-----------|
| **Interface** | CLI (Terminal) | CLI (Terminal) | GUI (Gráfica) |
| **Uso de recursos** | Baixo/Moderado | Extremamente Mínimo | Superior (RAM e CPU) |
| **Remoto (SSH/sem cabeça)** | Sem cabeça nativo/CLI | Tubo SSH nativo | Requer X11/agente remoto |
| **Mecanismo de dissecação** | Dissecadores Wireshark completos | Cabeçalhos básicos | Dissecadores Wireshark completos |
| **Filtros de captura** | Somente BPF (`-f`) | Somente BPF (`-i`) | Somente BPF |
| **Filtros de exibição** | ✅ Sintaxe completa do Wireshark (`-Y`) | ❌ Nenhum | ✅ Barra de filtro GUI completa |
| **Formatos de exportação** | PCAP, JSON, CSV, Elástico, XML | PCAP, ASCII, Hexadecimal | PCAP, CSV, XML, JSON |
| **Scripting e automação**| Concha, Python, jq | Concha/Awk/Sed | Plug-ins Lua, GUI |

---

## 📥 Instalação e privilégios

###Linux (Debian/Ubuntu/Kali Linux)
```bash
# Update and install tshark
sudo apt update && sudo apt install -y tshark

# Verify installation and version
tshark --version
```
### Permitindo captura de pacotes não raiz

Para evitar a execução de `tshark` como `root` (o que pode ser um risco de segurança devido à vasta superfície do dissector), adicione seu usuário ao grupo `wireshark`:
```bash
# Configure wireshark-common for non-superusers
sudo dpkg-reconfigure wireshark-common
# (Select "Yes" when prompted)

# Add your user to the wireshark group
sudo usermod -aG wireshark $USER

# Apply group changes to current session
newgrp wireshark
```
### macOS (Homebrew) e Windows (Chocolate)
```bash
# macOS
brew install wireshark

# Windows
choco install wireshark
```
---

## 🚀 Captura e reprodução básicas

### Listar interfaces disponíveis
```bash
# List interfaces with numeric IDs
tshark -D
```
### Capturando pacotes ao vivo
```bash
# Capture packets on interface eth0
tshark -i eth0

# Capture only 100 packets and stop
tshark -i eth0 -c 100

# Capture and save traffic directly to a pcap file
tshark -i eth0 -w capture.pcap

# Capture with ring buffer (rotate files every 100MB, keep max 5 files)
tshark -i eth0 -b filesize:100000 -b files:5 -w rolling_traffic.pcap

# Capture with a duration trigger (capture for 60 seconds)
tshark -i eth0 -a duration:60 -w one_minute.pcap
```
### Lendo arquivos PCAP
```bash
# Read and display packets from a pcap file
tshark -r capture.pcap

# Read PCAP without resolving IP addresses to DNS hostnames (speeds up processing)
tshark -r capture.pcap -n

# Suppress packet count summary upon completion
tshark -r capture.pcap -q
```
---

## 🔍 Filtragem de tráfego: captura vs exibição

`tshark` emprega dois mecanismos de filtragem distintos:

1. **Filtros de Captura (`-f`):** Filtros de Pacotes Berkeley (BPF) avaliados pelo kernel antes que os pacotes cheguem ao espaço do usuário. Altamente desempenho, mas limitado a primitivos básicos de IP/porta.
2. **Filtros de exibição (`-Y`):** Filtros de protocolo profundos avaliados pelo mecanismo de dissecação do Wireshark. Suporta camadas de aplicativos, pesquisa de strings, regex e sinalizadores de protocolo.
```bash
# Capture filter: Capture only TCP traffic to port 80 or 443
tshark -i eth0 -f "tcp port 80 or tcp port 443" -w web_traffic.pcap

# Display filter: Show only successful HTTP GET responses (Status 200)
tshark -r capture.pcap -Y "http.request.method == \"GET\" and http.response.code == 200"

# Display filter: Filter by specific subnet and exclude broadcast
tshark -r capture.pcap -Y "ip.addr == 192.168.1.0/24 and not eth.dst == ff:ff:ff:ff:ff:ff"

# Read-filter (-R): Single-pass filter used during capture
tshark -r capture.pcap -2 -R "dns.flags.response == 1"
```
---

## 📊 Extração de campo (`-T fields`)

O modo `-T fields` é o recurso mais poderoso do `tshark` para reconhecimento automatizado, scripts e desafios CTF.
```bash
# Basic syntax for field extraction
tshark -r capture.pcap -T fields -e <field1> -e <field2> -E header=y -E separator=,
```
### Sinalizadores de formatação de extração de chave

| Bandeira | Descrição |
|------|-------------|
| `-T fields` | Muda o modo de saída para extração de campo personalizado |
| `-e <field>` | Nomeia o campo do protocolo a extrair (pode ser repetido) |
| `-E header=y` | Imprime nomes de colunas como linha de cabeçalho |
| `-E separator=,` | Define separador de campos (`,` para CSV, `\t` para TSV) |
| `-E occurrence=f` | Extrai apenas a primeira (`f`), a última (`l`) ou todas as ocorrências (`a`) |
| `-E quote=d` | Inclui valores entre aspas duplas |

### Cheatsheet dos campos do protocolo principal
```bash
# IP layer fields
ip.src, ip.dst, ip.proto, ip.ttl

# TCP layer fields
tcp.srcport, tcp.dstport, tcp.flags, tcp.stream, tcp.payload

# HTTP fields
http.host, http.request.method, http.request.uri, http.response.code, http.user_agent, http.cookie

# DNS fields
dns.qry.name, dns.qry.type, dns.a, dns.flags.response

# TLS fields
tls.handshake.extensions_server_name, tls.handshake.ciphersuite
```
---

## 🎯 Pentest prático e receitas forenses

### Credencial HTTP e busca de URI
```bash
# Extract all visited URLs and HTTP Hosts
tshark -r capture.pcap -Y "http.request" -T fields -e ip.src -e http.host -e http.request.method -e http.request.uri -E header=y

# Extract User-Agent strings across internal clients
tshark -r capture.pcap -Y "http.user_agent" -T fields -e ip.src -e http.user_agent | sort -u

# Hunt for HTTP POST data (logins, passwords, tokens)
tshark -r capture.pcap -Y "http.request.method == \"POST\"" -T fields -e ip.src -e ip.dst -e http.host -e http.file_data

# Hunt for HTTP Basic Authentication headers (Base64 encoded)
tshark -r capture.pcap -Y "http.authorization" -T fields -e ip.src -e http.authorization
```
### Extração de consulta DNS e detecção de encapsulamento
```bash
# List all requested domain names (DNS queries)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e ip.src -e dns.qry.name | sort -u

# Detect potential DNS tunneling (display long subdomains > 30 characters)
tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name | awk 'length($1) > 30' | sort -u

# Correlate DNS queries with their resolved IP addresses
tshark -r capture.pcap -Y "dns.flags.response == 1 and dns.a" -T fields -e dns.qry.name -e dns.a -E header=y
```
### Extração SNI TLS/HTTPS

Mesmo quando as cargas HTTPS são criptografadas, a **Indicação de nome de servidor (SNI)** no handshake inicial do TLS Client Hello revela nomes de domínio de destino:
```bash
# Extract TLS Server Name Indication (SNI) hostnames
tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e ip.src -e ip.dst -e tls.handshake.extensions_server_name | sort -u
```
### Extração Kerberos AS-REQ / AS-REP
```bash
# Extract Kerberos domain client names (hunting for user accounts)
tshark -r capture.pcap -Y "kerberos.CNameString" -T fields -e ip.src -e kerberos.realm -e kerberos.CNameString | sort -u

# Hunt for Kerberos errors (e.g. KRB5KDC_ERR_PREAUTH_REQUIRED, bad passwords)
tshark -r capture.pcap -Y "kerberos.error_code" -T fields -e ip.src -e ip.dst -e kerberos.error_code
```
### Rastreamento de autenticação SMB e NTLM
```bash
# Extract SMB accessed file paths and shares
tshark -r capture.pcap -Y "smb2.filename" -T fields -e ip.src -e ip.dst -e smb2.filename | sort -u

# Extract NTLMSSP usernames and domain names during authentication
tshark -r capture.pcap -Y "ntlmssp.auth.username" -T fields -e ip.src -e ntlmssp.auth.domain -e ntlmssp.auth.username
```
### Auditoria de protocolo de texto simples
```bash
# Extract FTP usernames and passwords
tshark -r capture.pcap -Y "ftp.request.command == \"USER\" or ftp.request.command == \"PASS\"" -T fields -e ip.src -e ftp.request.command -e ftp.request.arg

# Extract Telnet session input
tshark -r capture.pcap -Y "telnet" -T fields -e telnet.data
```
### Caça de aperto de mão de 4 vias WPA / EAPOL
```bash
# Filter for 802.11 EAPOL handshakes to crack with Hashcat/Aircrack-ng
tshark -r wifi.pcap -Y "eapol" -w handshakes_only.pcap
```
---

## 🧵 Acompanhamento e remontagem de transmissão

`tshark` permite remontar fluxos TCP e UDP inteiros sem a necessidade da GUI do Wireshark.
```bash
# List all TCP conversations to find stream IDs
tshark -r capture.pcap -q -z conv,tcp

# Follow TCP stream 0 in ASCII format
tshark -r capture.pcap -q -z follow,tcp,ascii,0

# Follow TCP stream 2 in Hexdump format
tshark -r capture.pcap -q -z follow,tcp,hex,2

# Follow HTTP stream (stream index 1)
tshark -r capture.pcap -q -z follow,http,ascii,1
```
---

## 📈 Estatísticas, conversas e endpoints
```bash
# Protocol hierarchy statistics (breakdown of all protocols in PCAP)
tshark -r capture.pcap -q -z io,phs

# IP conversations table (who talked to whom, packet count, bytes)
tshark -r capture.pcap -q -z conv,ip

# Port conversation statistics (TCP ports)
tshark -r capture.pcap -q -z conv,tcp

# List all unique IP endpoints in PCAP
tshark -r capture.pcap -q -z endpoints,ip

# HTTP request statistics tree
tshark -r capture.pcap -q -z http,tree

# Wireshark Expert Info warnings and errors
tshark -r capture.pcap -q -z expert
```
---

## 🔐 Descriptografia de tráfego TLS / SSL

Se você tiver chaves secretas pré-mestres (exportadas pelo navegador `SSLKEYLOGFILE` ou recuperadas durante o sandbox de malware):
```bash
# Decrypt TLS traffic on the fly using keylog file
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -Y "http" -T fields -e http.host -e http.request.uri

# Follow decrypted HTTP stream inside TLS
tshark -r https_traffic.pcap -o "tls.keylog_file:/path/to/sslkeys.log" -q -z follow,http,ascii,0
```
---

## 📦 Exportar formatos
```bash
# Output as structured JSON (ideal for piping into jq or Python)
tshark -r capture.pcap -c 5 -T json

# Extract specific JSON field with jq
tshark -r capture.pcap -T json | jq '.[]._source.layers.ip["ip.src"]'

# Output in Elasticsearch Bulk Format (-T ek)
tshark -r capture.pcap -T ek > packets_for_elastic.json

# Output as CSV table
tshark -r capture.pcap -T fields -e frame.number -e ip.src -e ip.dst -e _ws.col.Protocol -E header=y -E separator=, > traffic.csv
```
---

## ⚡ Tabela de referência rápida

| Tarefa | Comando |
|------|---------|
| **Capturar tráfego ao vivo** | `tshark -i eth0 -w output.pcap` |
| **Captura com filtro BPF** | `tshark -i eth0 -f "port 80" -w http.pcap` |
| **Leia PCAP sem DNS** | `tshark -r capture.pcap -n` |
| **Aplicar filtro de exibição** | `tshark -r capture.pcap -Y "http.response.code >= 400"` |
| **Extrair IPs e Portas** | `tshark -r capture.pcap -T fields -e ip.src -e tcp.srcport -e ip.dst -e tcp.dstport` |
| **Extrair consultas DNS** | `tshark -r capture.pcap -Y "dns.flags.response == 0" -T fields -e dns.qry.name` |
| **Extraia nomes de host TLS SNI**| `tshark -r capture.pcap -Y "tls.handshake.type == 1" -T fields -e tls.handshake.extensions_server_name` |
| **Extraia URLs HTTP** | `tshark -r capture.pcap -Y "http.request" -T fields -e http.host -e http.request.uri` |
| **Siga o fluxo TCP** | `tshark -r capture.pcap -q -z follow,tcp,ascii,0` |
| **Hierarquia de protocolo** | `tshark -r capture.pcap -q -z io,phs` |
| **Descriptografar com Keylog** | `tshark -r traffic.pcap -o "tls.keylog_file:keys.log"` |
| **Exportar para JSON** | `tshark -r capture.pcap -T json > packets.json` |

---

## 💡 Dicas e práticas recomendadas

1. **Sempre use `-n` para PCAPs grandes:** A resolução de nomes DNS retarda drasticamente a análise. Adicione `-n` para evitar a resolução de endereços IP.
2. **Combine `-f` e `-Y`:** Use filtros de captura `-f` durante a captura ao vivo para reduzir quedas de pacotes e filtros de exibição `-Y` durante a pós-análise para inspeção profunda do protocolo.
3. **Pipe com `jq`:** Pipelining `tshark -T json` em `jq` permite consultar campos de protocolo aninhados com lógica booleana completa e transformação.
4. **Buffers de anel para detecção 24 horas por dia, 7 dias por semana:** Ao executar capturas de rede contínuas em um host pivô, sempre imponha limites de tamanho e arquivo (`-b filesize:50000 -b files:10`) para evitar travamento do disco do alvo.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** A captura não autorizada de pacotes, a espionagem no tráfego de rede e a interceptação de credenciais em redes sem permissão explícita e documentada são ilegais e estão sujeitas a processo criminal. Use o `tshark` estritamente em sistemas e redes que você possui ou está contratualmente autorizado a avaliar.