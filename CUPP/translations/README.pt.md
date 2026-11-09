<!-- language-home-navigation -->
[🏠 Página Principal Portuguesa](../../README.pt.md) • [🌐 Languages](../../README.md#-available-languages)

[🇹🇷 Türkçe](README.tr.md) • [🇩🇪 Deutsch](README.de.md) • [🇪🇸 Español](README.es.md) • [🇫🇷 Français](README.fr.md)

[🇮🇹 Italiano](README.it.md) • [🇷🇺 Русский](README.ru.md) • [🇨🇳 简体中文](README.zh-CN.md) • [🇸🇦 العربية](README.ar.md) • [🇬🇷 Ελληνικά](README.el.md) • [🇳🇴 Norsk](README.nb.md) • [🇬🇧 English](../README.md)

---


# CUPP (Criador de perfil de senhas de usuário comum) Cheatsheet

[CUPP](https://github.com/Mebus/cupp) (Common User Passwords Profiler) é um gerador de dicionário de senhas especializado projetado para perfis humanos direcionados. Em vez de forçar dicionários genéricos, o CUPP gera permutações com base em detalhes pessoais (nome do alvo, parceiro, animais de estimação, datas de nascimento, empresa, hobbies, variações de leet speak e combinações especiais de caracteres).

---

## Índice

- [Visão geral e casos de uso](#overview--use-cases)
- [Instalação](#instalação)
- [Assistente de criação de perfil interativo (`-i`)](#interactive-profiling-wizard--i)
- [Configuração e regras Leet (`cupp.cfg`)](#configuration--leet-rules-cuppcfg)
- [Baixando e mesclando dicionários (`-l` e `-w`)](#downloading--merging-dictionaries--l---w)
- [Saída de tubulação para Hashcat e Hydra](#piping-output-to-hashcat--hydra)
- [Insights defensivos e diretrizes de política de senha](#defensive-insights--password-policy-guidelines)

---

## Visão geral e casos de uso

Os ataques de dicionário tradicionais geralmente falham contra senhas complexas que seguem padrões previsíveis por humanos (por exemplo, `John1984!`, `Sarah_Fluffy2022#`). O CUPP aborda isso:
- Coletar inteligência pessoal específica do alvo.
- Combinando nomes, anos de nascimento, datas significativas e nomes de animais de estimação.
- Aplicação de mutações baseadas em regras (letras maiúsculas, substituição de leet speak, caracteres especiais, anexação de números aleatórios).
- Produzir uma lista de palavras otimizada e direcionada dramaticamente menor que `rockyou.txt`, mas com maior relevância alvo.

---

## Instalação
```bash
# Clone the repository
git clone https://github.com/Mebus/cupp.git
cd cupp

# Kali Linux / Debian repository (if available)
sudo apt-get update && sudo apt-get install -y cupp

# Check help options
python3 cupp.py -h
```
---

## Assistente de criação de perfil interativo (`-i`)

Inicie o prompt do questionário para criar uma lista de palavras personalizada:
```bash
python3 cupp.py -i
```
Exemplo de passo a passo do prompt:
```text
[+] Insert the targets information:
> First Name: Alice
> Surname: Smith
> Nickname: ally
> Birthdate (DDMMYYYY): 15061992

[+] Insert partners information:
> Partners name: Bob
> Partners nickname: bobby
> Partners birthdate (DDMMYYYY): 04111990

[+] Insert child's information:
> Child's name: Charlie
> Child's nickname: char
> Child's birthdate (DDMMYYYY): 20082018

[+] Insert pet's info:
> Pet's name: Luna

[+] Insert company name:
> Company name: CyberCorp

[+] Do you want to add some special words about the victim? Y/n: y
> (comma separated): football, chelsea, guitar, devops

[+] Do you want to add special characters at the end of words? Y/n: y
[+] Do you want to add some random numbers at the end of words? Y/n: y
[+] Leet mode? (i.e. leet = 1337) Y/n: y
```
O arquivo de saída `alice.csv` (ou arquivo de texto especificado) é gerado com milhares de mutações personalizadas (por exemplo, `Alice1992!`, `LunaCharlie2018`, `CyberCorp@2022`).

---

## Configuração e regras Leet (`cupp.cfg`)

A configuração do CUPP é controlada via `cupp.cfg`. As seções principais incluem:
```ini
[years]
# Set year range for automated suffix appending
years = 1970,1980,1985,1990,1995,2000,2010,2020,2023,2024,2025,2026

[nums]
# Common number patterns appended to passwords
nums = 1,12,123,1234,12345,123456,777,999,007,69

[special]
# Special character appending
chars = !,@,#,$,%,&,*,-,_

[leet]
# Leet speak substitutions
a = 4,@
b = 8
e = 3
i = 1,!
o = 0
s = 5,$
t = 7
```
Edite `cupp.cfg` para ajustar a localidade de destino e as especificidades do idioma.

---

## Baixando e mesclando dicionários (`-l` e `-w`)

### 1. Baixe listas de palavras pré-compiladas dos repositórios
Baixe listas de palavras linguísticas padrão:
```bash
python3 cupp.py -l
```
### 2. Melhore uma lista de palavras existente com Leet/regras especiais
Aplique regras de mutação CUPP a uma lista de palavras existentes:
```bash
python3 cupp.py -w /path/to/base_words.txt
```
---

## Saída de tubulação para Hashcat e Hydra

Alimente o dicionário personalizado gerado diretamente nas ferramentas de força bruta:

### Quebrando hashes de destino com Hashcat
```bash
# Generate targeted list
python3 cupp.py -i

# Run Hashcat against target NTLM hash
hashcat -m 1000 target_ntlm.hash alice.txt -r /usr/share/hashcat/rules/best64.rule
```
### Autenticação online de força bruta com Hydra
```bash
hydra -l alice -P alice.txt 192.168.1.50 ssh
```
---

## Insights defensivos e diretrizes de política de senha

- **Banir atributos pessoais:** Proibir senhas de usuários que contenham nomes, sobrenomes, nomes de usuário, nomes de marcas da empresa ou datas de nascimento de funcionários.
- **Impor comprimento em vez de complexidade:** uma senha com mais de 16 caracteres (por exemplo, quatro palavras aleatórias do dicionário) é significativamente mais resiliente contra ferramentas de criação de perfil do que um padrão `NameYear!` de 8 caracteres.
- **Autenticação multifator (MFA):** Aplique tokens FIDO2 ou TOTP de hardware para que a geração de dicionário personalizada por si só não comprometa as contas.